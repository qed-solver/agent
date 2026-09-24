# RuleScript porting progress

_Last updated: 2026-09-24T09:12:58.901922+00:00_

**7/13 rules proved** (1 failed, 5 skipped as out of QED's supported fragment).

| Rule | Backend | Status | Scope | Attempts | Notes |
|---|---|---|---|---|---|
| `EliminateDistinct` | CockroachDB | ✅ PROVED | PARTIAL | 26 | The encoding is structurally faithful: DistinctOn is exactly a group-by with no aggregation calls (matching `Aggregate(source, [field(0)]... |
| `EliminateNot` | CockroachDB | ✅ PROVED | FULL | 5 | The encoding directly captures `EliminateNot` by removing a doubled `NOT` around the same uninterpreted predicate while keeping the same ... |
| `FoldNotFalse` | CockroachDB | ✅ PROVED | FULL | 4 | The encoding is non-vacuous (Filter(Not(False), R) vs. Filter(True, R) are structurally distinct) and matches the source rule term-for-te... |
| `PruneJoinLeftCols` | CockroachDB | ✅ PROVED | PARTIAL | 10 | The encoding faithfully captures `PruneJoinLeftCols` as a specific, honestly-labeled (PARTIAL) instance — an inner join whose left input ... |
| `PushFilterIntoJoinLeft` | CockroachDB | ✅ PROVED | PARTIAL | 22 | `before()` (inner join with conjuncts `f(L) ∧ g(L,R)`) and `after()` (left filtered by `f(L)`, then joined on `g(L,R)`) are structurally ... |
| `SimplifyAndFalse` | CockroachDB | ✅ PROVED | FULL | 4 | The encoding faithfully captures `(And * (False)) => (False)`: the left operand is a fully uninterpreted predicate (universally quantifie... |
| `SimplifyTrueAnd` | CockroachDB | ✅ PROVED | FULL | 4 | `before()` is `Filter(AND(TRUE, P), S)` and `after()` is `Filter(P, S)` over the same uninterpreted scan `S` and the same uninterpreted p... |
| `TryDecorrelateSelect` | CockroachDB | ❌ FAILED | — | 0 | agent error: timed out |
| `EliminateAggDistinct` | CockroachDB | ⏭️ SKIPPED | — | 20 | The rule's soundness rests on Min/Max/BoolAnd/BoolOr being idempotent with respect to duplicate input values — an algebraic identity of s... |
| `EliminateUnaryMinus` | CockroachDB | ⏭️ SKIPPED | — | 20 | EliminateUnaryMinus is a pure scalar algebraic identity — numeric negation being an involution, −(−x) = x — and the only faithful encodin... |
| `EliminateWindow` | CockroachDB | ⏭️ SKIPPED | — | 9 | RuleScript has no Window builder and QED’s serializer/prover have no window semantics, so the source left-hand side cannot be represented... |
| `NegateComparison` | CockroachDB | ⏭️ SKIPPED | — | 20 | NegateComparison is not a pure And/Or/Not identity; it depends on specific comparison-operator algebra, e.g. ¬EQ being NE and ¬GT being L... |
| `SimplifyLeftJoin` | CockroachDB | ⏭️ SKIPPED | — | 20 | The rule's soundness rests entirely on the side condition `JoinFiltersMatchAllLeftRows` (for every left row, ∃ a right row satisfying the... |

## Details

### `EliminateDistinct` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateDistinct discards a DistinctOn operator that is eliminating duplicate
rows by using grouping columns that are statically known to form a strict key.
By definition, a strict key does not allow duplicate values, so the GroupBy is
redundant and can be eliminated.

Since a DistinctOn operator can serve as a projection operator, we need to
replace it with a Project so that the correct columns are projected. The
project itself may be eliminated later by other rules.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateDistinct`, not the other rules in that file):

```
# EliminateDistinct discards a DistinctOn operator that is eliminating duplicate
# rows by using grouping columns that are statically known to form a strict key.
# By definition, a strict key does not allow duplicate values, so the GroupBy is
# redundant and can be eliminated.
#
# Since a DistinctOn operator can serve as a projection operator, we need to
# replace it with a Project so that the correct columns are projected. The
# project itself may be eliminated later by other rules.
[EliminateDistinct, Normalize]
(DistinctOn | EnsureDistinctOn
    $input:*
    $aggs:*
    $groupingPrivate:* &
        (ColsAreStrictKey (GroupingCols $groupingPrivate) $input)
)
=>
(Project $input [] (GroupingOutputCols $groupingPrivate $aggs))
```
- Attempts used: 26
- Last updated: 2026-09-24T07:44:05.564089+00:00
- Reason / notes: The encoding is structurally faithful: DistinctOn is exactly a group-by with no aggregation calls (matching `Aggregate(source, [field(0)], [])`), and the RHS is a plain `Project` of the input's grouping column, and the source rule's one substantive precondition — grouping columns statically form a strict key of the input — is correctly expressed as the scan's declared `unique` non-nullable key, which is precisely what makes before/after non-vacuously different (the proof can only hold because of that key; without it a group-by over a duplicate column would not equal a plain projection). The narrowing (base-table input, single grouping column, identity projection) is forced by real QED/DSL limits — strict keys can only be declared on `scan` (a `ScanMany` cannot declare a key, and no other node exposes key declarations) — and the file's SCOPE line states exactly these assumptions, so the partial scope is honest and the result is a genuine, non-degenerate proof. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 340041}, panicked=False

### `EliminateNot` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

EliminateNot discards a doubled Not operator.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `EliminateNot`, not the other rules in that file):

```
# EliminateNot discards a doubled Not operator.
[EliminateNot, Normalize]
(Not (Not $input:*))
=>
$input
```
- Attempts used: 5
- Last updated: 2026-09-24T07:26:02.759736+00:00
- Reason / notes: The encoding directly captures `EliminateNot` by removing a doubled `NOT` around the same uninterpreted predicate while keeping the same source and filter context. The `scan`/`filter` wrapper is only the relational vehicle needed to give the boolean expression meaning, and the uninterpreted predicate makes the result universal over arbitrary boolean inputs without adding extra assumptions.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 336500}, panicked=False

### `FoldNotFalse` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

FoldNotFalse replaces NOT(False) with True.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `FoldNotFalse`, not the other rules in that file):

```
# FoldNotFalse replaces NOT(False) with True.
[FoldNotFalse, Normalize]
(Not (False))
=>
(True)
```
- Attempts used: 4
- Last updated: 2026-09-24T07:36:36.521196+00:00
- Reason / notes: The encoding is non-vacuous (Filter(Not(False), R) vs. Filter(True, R) are structurally distinct) and matches the source rule term-for-term: FoldNotFalse is a closed, non-schematic rewrite NOT(FALSE) ⟹ TRUE with no variables to leave uninterpreted, so the unconstrained scan is a neutral carrier, the filter condition is the DSL's minimal relational vehicle for a boolean term (same carrier as the FilterMerge example), and no rule instance is left out — the DSL has no expression-level hole to quantify over arbitrary positions, so this is as general as the rule can be expressed here, making the FULL tag honest. No preconditions (keys, NOT NULL) are involved and the literal is non-null, so no null-semantics edge case is silently dropped; the 0.26 ms proof is simply reflecting that the rule's content is a trivial constant identity, not a degenerate encoding.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 260666}, panicked=False

### `PruneJoinLeftCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneJoinLeftCols discards columns on the left side of a join that are never
used. AddDerivedOnClauseConditionsFromFKContraints builds equijoin predicates
which might be added during optimization, if any, to ensure those columns are
not pruned away.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneJoinLeftCols`, not the other rules in that file):

```
# PruneJoinLeftCols discards columns on the left side of a join that are never
# used. AddDerivedOnClauseConditionsFromFKContraints builds equijoin predicates
# which might be added during optimization, if any, to ensure those columns are
# not pruned away.
[PruneJoinLeftCols, Normalize]
(Project
    $input:(Join $left:* $right:* $on:* $private:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $left
            $needed:(UnionCols4
                (OuterCols $right)
                (FilterOuterCols
                    (AddDerivedOnClauseConditionsFromFKContraints
                        $on
                        $left
                        $right
                    )
                )
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    ((OpName $input)
        (PruneCols $left $needed)
        $right
        $on
        $private
    )
    $projections
    $passthrough
)
```
- Attempts used: 10
- Last updated: 2026-09-24T08:05:27.004841+00:00
- Reason / notes: The encoding faithfully captures `PruneJoinLeftCols` as a specific, honestly-labeled (PARTIAL) instance — an inner join whose left input has a join-key column plus one unused column, with the on-clause and outer projection as uninterpreted functions over the retained (key, right) columns — so the pruned left column is genuinely unreferenced and projecting it below the join is exactly the source rule's `PruneCols $left $needed`. `before()` and `after()` are structurally different (the after inserts a Project pushing the key down to the join's left input and re-indexes the join row from (key, extra, right) to (key, right)), so the proof is non-vacuous, and reusing the same `C`/`G` symbols correctly reflects that the rewrite preserves the on-clause and outer projection; field indexing checks out in both directions, no key/NOT-NULL precondition is silently dropped (nullable types used, and the left being a Scan satisfies the `CanPruneCols` merge precondition), so the provable result is meaningful rather than coincidentally over-constrained.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 99601958}, panicked=False

### `PushFilterIntoJoinLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

PushFilterIntoJoinLeft pushes Join filter conditions into the left side of the
join. This is possible in the case of InnerJoin, as long as the condition has
no dependencies on the right side of the join. Left and Full joins are not
eligible, since filtering left rows will change the number of rows in the
result for those types of joins:

-- A row with nulls on the right side is returned for a.x=1, a.y=2, b.x=1.
SELECT * FROM a LEFT JOIN b ON a.x=b.x AND a.y < 0

-- But if the filter is incorrectly pushed down, then no row is returned.
SELECT * FROM (SELECT * FROM a WHERE a.y < 0) a LEFT JOIN b ON a.x=b.x

In addition, AntiJoin is not eligible for this rule, as illustrated by this
example:

-- A row is returned for a.y=2.
SELECT * FROM a ANTI JOIN b ON a.y < 0

-- But if the filter is incorrectly pushed down, then no row is returned.
SELECT * FROM (SELECT * FROM a WHERE a.y < 0) a ANTI JOIN b ON True

Citations: [1]

Extracted from `join.opt` (which defines multiple rules — implement specifically `PushFilterIntoJoinLeft`, not the other rules in that file):

```
# PushFilterIntoJoinLeft pushes Join filter conditions into the left side of the
# join. This is possible in the case of InnerJoin, as long as the condition has
# no dependencies on the right side of the join. Left and Full joins are not
# eligible, since filtering left rows will change the number of rows in the
# result for those types of joins:
#
#   -- A row with nulls on the right side is returned for a.x=1, a.y=2, b.x=1.
#   SELECT * FROM a LEFT JOIN b ON a.x=b.x AND a.y < 0
#
#   -- But if the filter is incorrectly pushed down, then no row is returned.
#   SELECT * FROM (SELECT * FROM a WHERE a.y < 0) a LEFT JOIN b ON a.x=b.x
#
# In addition, AntiJoin is not eligible for this rule, as illustrated by this
# example:
#
#   -- A row is returned for a.y=2.
#   SELECT * FROM a ANTI JOIN b ON a.y < 0
#
#   -- But if the filter is incorrectly pushed down, then no row is returned.
#   SELECT * FROM (SELECT * FROM a WHERE a.y < 0) a ANTI JOIN b ON True
#
# Citations: [1]
[PushFilterIntoJoinLeft, Normalize]
(InnerJoin | InnerJoinApply | SemiJoin | SemiJoinApply
    $left:* & ^(HasOuterCols $left)
    $right:*
    $on:[
        ...
        $item:* & (IsBoundBy $item $leftCols:(OutputCols $left))
        ...
    ]
    $private:*
)
=>
((OpName)
    (Select $left (ExtractBoundConditions $on $leftCols))
    $right
    (ExtractUnboundConditions $on $leftCols)
    $private
)
```
- Attempts used: 22
- Last updated: 2026-09-24T08:01:20.655902+00:00
- Reason / notes: `before()` (inner join with conjuncts `f(L) ∧ g(L,R)`) and `after()` (left filtered by `f(L)`, then joined on `g(L,R)`) are structurally different, share the same uninterpreted symbols `f`, `g`, and the L column (so `f` is genuinely left-bound in both and correctly identified as the pushed conjunct), making the QED proof non-vacuous and semantically valid for inner joins; the narrowing to INNER-only with one left-bound/one unbound conjunct and single-column inputs is a genuine, honestly-labeled (PARTIAL), non-degenerate special case of the source rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 79625666}, panicked=False

### `SimplifyAndFalse` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyAndFalse maps the And operator to False if its right input is False.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyAndFalse`, not the other rules in that file):

```
# SimplifyAndFalse maps the And operator to False if its right input is False.
[SimplifyAndFalse, Normalize]
(And * $right:(False))
=>
$right
```
- Attempts used: 4
- Last updated: 2026-09-24T07:49:22.474118+00:00
- Reason / notes: The encoding faithfully captures `(And * (False)) => (False)`: the left operand is a fully uninterpreted predicate (universally quantified over all instantiations, which subsumes any boolean expression the `*` could match), the right operand is correctly hard-coded as the literal `False` because the rule requires the syntactic False constant, and `before()`/`after()` genuinely differ (the And conjunct is eliminated), so the proof is non-vacuous. Embedding the boolean identity in filter position over an arbitrary, unconstrained scan is the standard semantics-preserving translation in this DSL (same treatment as the FilterMerge worked example), and in QED's two-valued semantics the proven statement `∀R, f: filter(And(f,⊥)) = filter(⊥)` is exactly equivalent to the pointwise expression equality `And(x, False) ≡ False` for all x — no keys, NOT NULLs, or other preconditions are assumed that the unguarded source rule does not itself impose, and the single symbol `left` is shared only where the source pattern requires. Hence `// SCOPE: FULL` is honest: no assumption anywhere narrows the family beyond the canonical relational witness of the expression rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 284458}, panicked=False

### `SimplifyTrueAnd` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyTrueAnd simplifies the And operator by discarding a True condition on
the left side.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyTrueAnd`, not the other rules in that file):

```
# SimplifyTrueAnd simplifies the And operator by discarding a True condition on
# the left side.
[SimplifyTrueAnd, Normalize]
(And (True) $right:*)
=>
$right
```
- Attempts used: 4
- Last updated: 2026-09-24T08:42:26.464990+00:00
- Reason / notes: `before()` is `Filter(AND(TRUE, P), S)` and `after()` is `Filter(P, S)` over the same uninterpreted scan `S` and the same uninterpreted predicate `P`, so the proof is a genuine, non-vacuous universal proof of exactly the boolean identity `And(True, E) ≡ E` the source rule states, with the True literal on the correct (left) side, no concrete predicates or operators baked in, and symbol sharing matching the rule precisely. The identity holds under SQL three-valued logic for every truth value including NULL (`TRUE AND NULL = NULL`), so no precondition is silently missing, and embedding the scalar rule in a filter over a general scan (same shape as the reference FilterMerge exemplar) captures the rule's full logical content — the identity is compositional and its proof is universal over the uninterpreted symbols — so the `SCOPE: FULL` tag is honest.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1029375}, panicked=False

### `TryDecorrelateSelect` — ❌ FAILED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateSelect "pushes down" the join apply into the select operator,
in order to eliminate any correlation between the select filter list and the
left side of the join, and also to keep "digging" down to find and eliminate
other unnecessary correlation. Eventually, the hope is to trigger the
DecorrelateJoin pattern to turn JoinApply operators into non-apply Join
operators.

Note that citation [3] doesn't directly contain this identity, since it
assumes that the Select will be hoisted above the Join rather than becoming
part of its On condition. PushFilterIntoJoinRight allows the condition to be
pushed down, so this rule can correctly pull it up.

Citations: [3] (see identity #3)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateSelect`, not the other rules in that file):

```
# TryDecorrelateSelect "pushes down" the join apply into the select operator,
# in order to eliminate any correlation between the select filter list and the
# left side of the join, and also to keep "digging" down to find and eliminate
# other unnecessary correlation. Eventually, the hope is to trigger the
# DecorrelateJoin pattern to turn JoinApply operators into non-apply Join
# operators.
#
# Note that citation [3] doesn't directly contain this identity, since it
# assumes that the Select will be hoisted above the Join rather than becoming
# part of its On condition. PushFilterIntoJoinRight allows the condition to be
# pushed down, so this rule can correctly pull it up.
#
# Citations: [3] (see identity #3)
[TryDecorrelateSelect, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (Select $input:* $filters:*)
    $on:*
    $private:*
)
=>
((OpName) $left $input (ConcatFilters $on $filters) $private)
```
- Attempts used: 0
- Last updated: 2026-09-24T08:42:58.015008+00:00
- Reason / notes: agent error: timed out

### `EliminateAggDistinct` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/agg.opt

EliminateAggDistinct removes AggDistinct for aggregations where DISTINCT
never modifies the result; for example: min(DISTINCT x).

Extracted from `agg.opt` (which defines multiple rules — implement specifically `EliminateAggDistinct`, not the other rules in that file):

```
# EliminateAggDistinct removes AggDistinct for aggregations where DISTINCT
# never modifies the result; for example: min(DISTINCT x).
[EliminateAggDistinct, Normalize]
(AggDistinct $input:(Min | Max | BoolAnd | BoolOr))
=>
$input
```
- Attempts used: 20
- Last updated: 2026-09-24T09:12:58.900013+00:00
- Reason / notes: The rule's soundness rests on Min/Max/BoolAnd/BoolOr being idempotent with respect to duplicate input values — an algebraic identity of specific aggregate functions — but QED models every aggregate (even one literally named "Min") as an uninterpreted function over its input bag and knows only bag-equality of inputs, so it cannot relate agg(DISTINCT x), which aggregates over the deduplicated bag, to agg(x) over the raw bag; an SMT countermodel (an aggregate that depends on value multiplicity) always exists, and the source rule's restriction to those four functions has no semantic content in the DSL since generic aggregate operators carry no algebra. The richer input-key-based special case (like the proven PARTIAL `EliminateDistinct` precedent) at best covers degenerate inputs where deduplication is provably a no-op, not the function-based justification this rule actually has. The porter's recorded failure was an LLM context-length crash rather than an actual QED run, but the UNSUPPORTED conclusion is correct on the merits.

### `EliminateUnaryMinus` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/numeric.opt

EliminateUnaryMinus discards a doubled UnaryMinus operator.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `EliminateUnaryMinus`, not the other rules in that file):

```
# EliminateUnaryMinus discards a doubled UnaryMinus operator.
[EliminateUnaryMinus, Normalize]
(UnaryMinus (UnaryMinus $input:*))
=>
$input
```
- Attempts used: 20
- Last updated: 2026-09-24T07:31:20.773927+00:00
- Reason / notes: EliminateUnaryMinus is a pure scalar algebraic identity — numeric negation being an involution, −(−x) = x — and the only faithful encoding in RuleScript is a projection operator by name (e.g. scan → project("neg", project("neg", x)) ≡ x), which QED models as an uninterpreted function with no involution axiom, so SMT refutes it with a countermodel where "neg" is an arbitrary non-involutive function. That property is exactly a backend operator's bespoke internal semantics, which lives in the Rust prover's theory (off-limits) and cannot be supplied by any Java DSL extension; asserting f(f(x))=x as a table "guaranteed" constraint would merely assume the conclusion, and a boolean Not(Not(p))≡p encoding would be a different rule, not a special case of numeric negation. ```

### `EliminateWindow` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/window.opt

EliminateWindow removes a Window operator with no window functions (which can
occur via column pruning).

Extracted from `window.opt` (which defines multiple rules — implement specifically `EliminateWindow`, not the other rules in that file):

```
# EliminateWindow removes a Window operator with no window functions (which can
# occur via column pruning).
[EliminateWindow, Normalize]
(Window $input:* [])
=>
$input
```
- Attempts used: 9
- Last updated: 2026-09-24T07:44:05.767331+00:00
- Reason / notes: RuleScript has no Window builder and QED’s serializer/prover have no window semantics, so the source left-hand side cannot be represented as a window node. Modeling an empty window as an identity projection instead only proves the unrelated identity-projection tautology, not that CockroachDB’s no-op Window operator can be eliminated.

### `NegateComparison` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

NegateComparison inverts eligible comparison operators when they are negated
by the Not operator. For example, Eq maps to Ne, and Gt maps to Le. All
comparisons can be negated except for the JSON and Geospatial comparisons.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `NegateComparison`, not the other rules in that file):

```
# NegateComparison inverts eligible comparison operators when they are negated
# by the Not operator. For example, Eq maps to Ne, and Gt maps to Le. All
# comparisons can be negated except for the JSON and Geospatial comparisons.
[NegateComparison, Normalize]
(Not
    $input:(Comparison $left:* $right:*) &
        (CanNegateComparison $op:(OpName $input))
)
=>
(NegateComparison $op $left $right)
```
- Attempts used: 20
- Last updated: 2026-09-24T07:41:28.689596+00:00
- Reason / notes: NegateComparison is not a pure And/Or/Not identity; it depends on specific comparison-operator algebra, e.g. ¬EQ being NE and ¬GT being LE. RuleScript/QED treats predicate symbols as uninterpreted and has no axioms linking an operator to its negated counterpart, so QED cannot prove this rewrite for any non-trivial comparison pair. ```

### `SimplifyLeftJoin` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

SimplifyLeftJoin reduces a LeftJoin operator to an InnerJoin operator (or a
FullJoin to a RightJoin) when it's known that every row in the join's left
input will match at least one row in the right input. Since every row matches,
NULL-extended rows will never be added by the outer join, and therefore can be
mapped to an InnerJoin (or RightJoin in case of FullJoin). See
filtersMatchAllLeftRows comment for conditions in which this rule can match.

Self-join example:
SELECT * FROM xy LEFT JOIN xy AS xy2 ON xy.y = xy2.y
=>
SELECT * FROM xy INNER JOIN xy AS xy2 ON xy.y = xy2.y

Foreign-key example:
SELECT * FROM orders o LEFT JOIN customers c ON o.customer_id = c.id
=>
SELECT * FROM orders o INNER JOIN customers c ON o.customer_id = c.id

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyLeftJoin`, not the other rules in that file):

```
# SimplifyLeftJoin reduces a LeftJoin operator to an InnerJoin operator (or a
# FullJoin to a RightJoin) when it's known that every row in the join's left
# input will match at least one row in the right input. Since every row matches,
# NULL-extended rows will never be added by the outer join, and therefore can be
# mapped to an InnerJoin (or RightJoin in case of FullJoin). See
# filtersMatchAllLeftRows comment for conditions in which this rule can match.
#
# Self-join example:
#   SELECT * FROM xy LEFT JOIN xy AS xy2 ON xy.y = xy2.y
#   =>
#   SELECT * FROM xy INNER JOIN xy AS xy2 ON xy.y = xy2.y
#
# Foreign-key example:
#   SELECT * FROM orders o LEFT JOIN customers c ON o.customer_id = c.id
#   =>
#   SELECT * FROM orders o INNER JOIN customers c ON o.customer_id = c.id
[SimplifyLeftJoin, Normalize]
(LeftJoin | LeftJoinApply | FullJoin
    $left:*
    $right:*
    $on:* & (JoinFiltersMatchAllLeftRows $left $right $on)
    $private:*
)
=>
(ConstructNonLeftJoin (OpName) $left $right $on $private)
```
- Attempts used: 20
- Last updated: 2026-09-24T08:47:18.212026+00:00
- Reason / notes: The rule's soundness rests entirely on the side condition `JoinFiltersMatchAllLeftRows` (for every left row, ∃ a right row satisfying the join predicate) — a quantified entailment between uninterpreted symbols that QED explicitly cannot reason about, and RuleScript's pattern-pair format has no mechanism to state rule preconditions; without that guard, QED correctly sees that LeftJoin ≠ InnerJoin for arbitrary instantiations (a left row with no match yields a NULL-extended row in the left join but no row in the inner join), and no non-trivial special case (self-join, `True` condition) is provable either, since the join predicate is uninterpreted (no reflexivity) and non-emptiness of the right input is likewise inexpressible. ```

