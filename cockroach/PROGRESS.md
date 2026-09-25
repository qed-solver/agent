# RuleScript porting progress

_Last updated: 2026-09-25T08:20:35.366373+00:00_

**25/41 rules proved** (0 failed, 16 skipped as out of QED's supported fragment).

| Rule | Backend | Status | Scope | Attempts | Notes |
|---|---|---|---|---|---|
| `AssociateLimitJoinsLeft` | CockroachDB | ✅ PROVED | PARTIAL | 28 | The encoding faithfully captures the rule's substantive identity — ((A LEFT JOIN B ON p_ab) INNER JOIN C ON p_ac) ≡ reprojected((A INNER ... |
| `AssociateLimitJoinsRight` | CockroachDB | ✅ PROVED | PARTIAL | 8 | before() = C ⋈ (A ⟕ B ON p_ab) and after() = (A ⋈ C ON p_ac) ⟕ B ON p_ab (re-projected to the common (C,A,B) column order) are genuinely ... |
| `CommuteConst` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding faithfully captures the rule's Eq-case semantic core—commutativity of equality—by deliberately using the concrete SqlStdOper... |
| `CommuteConstInequality` | CockroachDB | ✅ PROVED | PARTIAL | 41 | The encoding faithfully lifts the scalar Lt flip into the relational DSL — a maximally-general carrier (INNER cross-join of two unconstra... |
| `CommuteRightJoin` | CockroachDB | ✅ PROVED | PARTIAL | 4 | The encoding is non-vacuous and semantically faithful: before() is a genuine RightJoin(L, R, P) and after() is a LeftJoin(R, L) with the ... |
| `CommuteVar` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding is a faithful, non-vacuous proof of the Eq fragment of CommuteVar: `before()` and `after()` are structurally different (conc... |
| `CommuteVarInequality` | CockroachDB | ✅ PROVED | PARTIAL | 24 | The encoding faithfully lifts the rule's scalar commutation to the relational level — an inner cross join of two independent same-type nu... |
| `ConvertGroupByToDistinct` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding has the correct relational shape for the source rule: a no-aggregation `Aggregate` (group set = all columns, empty agg list)... |
| `DecorrelateJoin` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding faithfully captures the core DecorrelateJoin transformation: `before()` builds a `LogicalCorrelate` (INNER) where the right ... |
| `DeduplicateSelectFilters` | CockroachDB | ✅ PROVED | PARTIAL | 21 | before() (filter chain p, q, p) and after() (p, q) are genuinely different, and the shared uninterpreted symbol `p` correctly enforces th... |
| `DetectJoinContradiction` | CockroachDB | ✅ PROVED | PARTIAL | 30 | The encoding faithfully mirrors the source transformation — a join whose ON list contains a contradictory (always-false) item alongside u... |
| `EliminateAggDistinct` | CockroachDB | ✅ PROVED | PARTIAL | 4 | The encoding is a sound, non-vacuous special case rather than a vacuous one: `before()` and `after()` genuinely differ in the AggCall's `... |
| `EliminateAggDistinctForKeys` | CockroachDB | ✅ PROVED | PARTIAL | 4 | The encoding faithfully captures a genuine special case of the rule (input is a scan whose unique key is also the grouping key and the DI... |
| `EliminateAggFilteredDistinctForKeys` | CockroachDB | ✅ PROVED | PARTIAL | 62 | Manually re-derived and verified by Claude (not the automated porter/verifier LLM loop, which exhausted both pool attempts on repeated LL... |
| `EliminateAntiJoin` | CockroachDB | ✅ PROVED | PARTIAL | 5 | The encoding is faithful and non-trivial: it uses an uninterpreted left relation, a zero-row right (the canonical `Empty` form, which is ... |
| `EliminateDistinct` | CockroachDB | ✅ PROVED | PARTIAL | 26 | The encoding is structurally faithful: DistinctOn is exactly a group-by with no aggregation calls (matching `Aggregate(source, [field(0)]... |
| `EliminateNot` | CockroachDB | ✅ PROVED | FULL | 5 | The encoding directly captures `EliminateNot` by removing a doubled `NOT` around the same uninterpreted predicate while keeping the same ... |
| `FoldNotFalse` | CockroachDB | ✅ PROVED | FULL | 4 | The encoding is non-vacuous (Filter(Not(False), R) vs. Filter(True, R) are structurally distinct) and matches the source rule term-for-te... |
| `NegateComparison` | CockroachDB | ✅ PROVED | PARTIAL | 21 | The proof is non-vacuous and genuine: `before()` is `Filter(¬(x = y))` vs `after()` `Filter(x <> y)` over the cross-join of two *independ... |
| `PruneJoinLeftCols` | CockroachDB | ✅ PROVED | PARTIAL | 10 | The encoding faithfully captures `PruneJoinLeftCols` as a specific, honestly-labeled (PARTIAL) instance — an inner join whose left input ... |
| `PushFilterIntoJoinLeft` | CockroachDB | ✅ PROVED | PARTIAL | 22 | `before()` (inner join with conjuncts `f(L) ∧ g(L,R)`) and `after()` (left filtered by `f(L)`, then joined on `g(L,R)`) are structurally ... |
| `SimplifyAndFalse` | CockroachDB | ✅ PROVED | FULL | 4 | The encoding faithfully captures `(And * (False)) => (False)`: the left operand is a fully uninterpreted predicate (universally quantifie... |
| `SimplifyLeftJoin` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding is non-vacuous and correctly shaped: `before()` and `after()` genuinely differ only in join kind (LEFT vs INNER), and the eq... |
| `SimplifyTrueAnd` | CockroachDB | ✅ PROVED | FULL | 4 | `before()` is `Filter(AND(TRUE, P), S)` and `after()` is `Filter(P, S)` over the same uninterpreted scan `S` and the same uninterpreted p... |
| `TryDecorrelateSelect` | CockroachDB | ✅ PROVED | PARTIAL | 62 | Manually re-derived and verified by Claude (not the automated porter/verifier LLM loop), after the automated attempt twice produced a vac... |
| `ApplyLimitToRecursiveCTEScan` | CockroachDB | ⏭️ SKIPPED | — | 6 | The rule's precondition is a set of subplan cardinality side conditions (HasBoundedCardinality) and its effect is a backend marker operat... |
| `CollapseRepeatedLikePatternWildcards` | CockroachDB | ⏭️ SKIPPED | — | 21 | The rule's soundness rests entirely on glob-pattern semantics of LIKE's pattern argument — that a run of `%` wildcards matches exactly th... |
| `CommuteNullIs` | CockroachDB | ⏭️ SKIPPED | — | 43 | The rewrite's validity rests entirely on the null-aware commutativity of CockroachDB's Is/IsNot operators (Is(NULL,x) ≡ Is(x, NULL)), whi... |
| `ConsolidateSelectFilters` | CockroachDB | ⏭️ SKIPPED | — | 43 | The rule's entire semantic content is that CockroachDB's `Range` predicate is a transparent wrapper over its inner conjunction (Range(e) ... |
| `ConvertCountToCountRows` | CockroachDB | ⏭️ SKIPPED | — | 1 | Manually investigated by Claude (not the automated porter/verifier LLM loop, which exhausted all 5 rounds on repeated context-length cras... |
| `ConvertJSONSubscriptToFetchValue` | CockroachDB | ⏭️ SKIPPED | — | 25 | This rule is purely an operator-aliasing identity: it asserts that two syntactically distinct scalar operators (JSON indirection `[...]` ... |
| `ConvertLevenshteinToLevenshteinLessEqualLeft` | CockroachDB | ⏭️ SKIPPED | — | 41 | The rule's validity rests entirely on the backend's clamp identity `levenshtein_less_equal(a,b,n) = min(levenshtein(a,b), n)`, but RuleSc... |
| `ConvertLevenshteinToLevenshteinLessEqualRight` | CockroachDB | ⏭️ SKIPPED | — | 21 | The rewrite `x OP levenshtein(s,t) ⟺ x OP levenshtein_less_equal(s,t,x)` is only valid because of CockroachDB's internal clamping contrac... |
| `ConvertLikeEscapeToLike` | CockroachDB | ⏭️ SKIPPED | — | 43 | The rule's correctness rests entirely on CockroachDB's backend-specific semantic that `LIKE`'s default escape character is `'\'`, i.e. th... |
| `ConvertRegressionCountToCount` | CockroachDB | ⏭️ SKIPPED | — | 67 | The rewrite's correctness rests entirely on a specific algebraic identity between two *different* named aggregates — `RegressionCount(a,b... |
| `ConvertUncorrelatedExistsToCoalesceSubquery` | CockroachDB | ⏭️ SKIPPED | — | 24 | Both sides of the rewrite are scalar expressions over nested sub-relations — an EXISTS on the left, and COALESCE of a scalar subquery (a ... |
| `ConvertUnionToDistinctUnionAll` | CockroachDB | ⏭️ SKIPPED | — | 41 | The rule's after side is a DistinctOn that dedups on a strict subset of columns (the key) while retaining all output columns, i.e. an arb... |
| `ConvertZipArraysToValues` | CockroachDB | ⏭️ SKIPPED | — | 21 | The rule's core is a set-returning/row-generating operation — `ProjectSet` expanding an array column (`unnest`/`json_array_elements`) int... |
| `DecorrelateProjectSet` | CockroachDB | ⏭️ SKIPPED | — | 47 | ProjectSet / set-returning (table-valued) functions have [NOTE: response was truncated at the token limit before finishing — if this cut ... |
| `EliminateUnaryMinus` | CockroachDB | ⏭️ SKIPPED | — | 8 | The rule requires the numeric negation algebraic law `neg(neg(x)) = x`, but RuleScript expresses that scalar operator as an uninterpreted... |
| `EliminateWindow` | CockroachDB | ⏭️ SKIPPED | — | 48 | EliminateWindow's correctness rests entirely on the Window operator's internal semantics — that a Window with an empty function list is a... |

## Details

### `AssociateLimitJoinsLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/limit.opt

AssociateLimitJoinsLeft reorders an InnerJoin and LeftJoin under a Limit if:
1. The InnerJoin does not preserve rows from its left input.
2. The InnerJoin's ON condition does not reference the right side of the
LeftJoin (because the reordering would be invalid).
3. Neither join has join hints.

Why condition #1? If the InnerJoin preserves left rows, the limit can already
be pushed down into the LeftJoin, so there's no need to reorder the joins.

Here's the transformation:

SELECT *
FROM (SELECT * FROM xy LEFT JOIN uv ON u = x)
INNER JOIN ab
ON a = y
LIMIT 10
=>
SELECT *
FROM (SELECT * FROM xy INNER JOIN ab ON a = y)
LEFT JOIN uv
ON u = x
LIMIT 10

Citations: [1] (See identity (6) in section 2.2)

Extracted from `limit.opt` (which defines multiple rules — implement specifically `AssociateLimitJoinsLeft`, not the other rules in that file):

```
# AssociateLimitJoinsLeft reorders an InnerJoin and LeftJoin under a Limit if:
# 1. The InnerJoin does not preserve rows from its left input.
# 2. The InnerJoin's ON condition does not reference the right side of the
#    LeftJoin (because the reordering would be invalid).
# 3. Neither join has join hints.
#
# Why condition #1? If the InnerJoin preserves left rows, the limit can already
# be pushed down into the LeftJoin, so there's no need to reorder the joins.
#
# Here's the transformation:
#
#   SELECT *
#   FROM (SELECT * FROM xy LEFT JOIN uv ON u = x)
#   INNER JOIN ab
#   ON a = y
#   LIMIT 10
# =>
#   SELECT *
#   FROM (SELECT * FROM xy INNER JOIN ab ON a = y)
#   LEFT JOIN uv
#   ON u = x
#   LIMIT 10
#
# Citations: [1] (See identity (6) in section 2.2)
[AssociateLimitJoinsLeft, Normalize, LowPriority]
(Limit
    $limitInput:(InnerJoin
            $outsideLeft:(LeftJoin
                $insideLeft:*
                $insideRight:*
                $insideOn:*
                $insidePrivate:* & (NoJoinHints $insidePrivate)
            )
            $outsideRight:*
            $outsideOn:* &
                ^(ColsIntersect
                    (FilterOuterCols $outsideOn)
                    (OutputCols $insideRight)
                )
            $outsidePrivate:* & (NoJoinHints $outsidePrivate)
        ) &
        ^(JoinPreservesLeftRows $limitInput)
    $limitValue:*
    $limitOrdering:*
)
=>
(Limit
    (LeftJoin
        (InnerJoin
            $insideLeft
            $outsideRight
            $outsideOn
            (EmptyJoinPrivate)
        )
        $insideRight
        $insideOn
        (EmptyJoinPrivate)
    )
    $limitValue
    $limitOrdering
)
```
- Attempts used: 28
- Last updated: 2026-09-24T09:53:49.675461+00:00
- Reason / notes: The encoding faithfully captures the rule's substantive identity — ((A LEFT JOIN B ON p_ab) INNER JOIN C ON p_ac) ≡ reprojected((A INNER JOIN C ON p_ac) LEFT JOIN B ON p_ab) — with exactly the right join kinds, shared uninterpreted predicates used with consistent argument correspondence (p_ab over A+B, p_ac over A+C), and the one soundness-critical precondition (the outside ON condition must not reference the inner left join's right input) structurally enforced by making p_ac a predicate over A and C columns only. The two sides are genuinely different join trees so the proof is non-vacuous, and the only restrictions — single-column scans (the identity is row-wise and column-arity independent), omission of the Limit/ordering wrapper that QED cannot model anyway (both sides share the identical wrapper, so the join equivalence is the full meaningful claim), and omission of the non-soundness priority guards `^(JoinPreservesLeftRows)` and `NoJoinHints` — are honestly stated in the PARTIAL scope line.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 96351500}, panicked=False

### `AssociateLimitJoinsRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/limit.opt

AssociateLimitJoinsRight mirrors AssociateLimitJoinsLeft (it matches when the
LeftJoin is the right input of the InnerJoin, as opposed to the left input).
Here's the transformation:

SELECT *
FROM ab
INNER JOIN (SELECT * FROM xy LEFT JOIN uv ON u = x)
ON a = y
LIMIT 10
=>
SELECT *
FROM (SELECT * FROM xy INNER JOIN ab ON a = y)
LEFT JOIN uv
ON u = x
LIMIT 10

Extracted from `limit.opt` (which defines multiple rules — implement specifically `AssociateLimitJoinsRight`, not the other rules in that file):

```
# AssociateLimitJoinsRight mirrors AssociateLimitJoinsLeft (it matches when the
# LeftJoin is the right input of the InnerJoin, as opposed to the left input).
# Here's the transformation:
#
#   SELECT *
#   FROM ab
#   INNER JOIN (SELECT * FROM xy LEFT JOIN uv ON u = x)
#   ON a = y
#   LIMIT 10
# =>
#   SELECT *
#   FROM (SELECT * FROM xy INNER JOIN ab ON a = y)
#   LEFT JOIN uv
#   ON u = x
#   LIMIT 10
#
[AssociateLimitJoinsRight, Normalize, LowPriority]
(Limit
    $limitInput:(InnerJoin
            $outsideLeft:*
            $outsideRight:(LeftJoin
                $insideLeft:*
                $insideRight:*
                $insideOn:*
                $insidePrivate:* & (NoJoinHints $insidePrivate)
            )
            $outsideOn:* &
                ^(ColsIntersect
                    (FilterOuterCols $outsideOn)
                    (OutputCols $insideRight)
                )
            $outsidePrivate:* & (NoJoinHints $outsidePrivate)
        ) &
        ^(JoinPreservesRightRows $limitInput)
    $limitValue:*
    $limitOrdering:*
)
=>
(Limit
    (LeftJoin
        (InnerJoin
            $insideLeft
            $outsideLeft
            $outsideOn
            (EmptyJoinPrivate)
        )
        $insideRight
        $insideOn
        (EmptyJoinPrivate)
    )
    $limitValue
    $limitOrdering
)
```
- Attempts used: 8
- Last updated: 2026-09-25T01:13:54.033415+00:00
- Reason / notes: before() = C ⋈ (A ⟕ B ON p_ab) and after() = (A ⋈ C ON p_ac) ⟕ B ON p_ab (re-projected to the common (C,A,B) column order) are genuinely different plan trees, so the proof is of the exact reassociation identity that is the semantic core of the rule, with join types matching the source (inner outside / left inside, flipped to inner inside / left outside). The two ON conditions are shared uninterpreted predicate symbols applied to the same logical columns in the same argument order on both sides (p_ab on (A,B), p_ac on (A,C)), which structurally enforces the rule's essential side condition that $outsideOn not reference $insideRight rather than coincidentally satisfying it, and A/B/C remain three independent uninterpreted tables. The declared PARTIAL scope is honest and non-degenerate: the Limit/ordering wrapper is identical on both sides and unmodelable by QED, the fixed single-column width is an inherent DSL limitation (no arity quantification) with no bearing on the identity's logical content, and the proof still fully exercises the null-extended-row path that is what makes the identity non-trivial. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 109688708}, panicked=False

### `CommuteConst` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

CommuteConst ensures that "constant expression trees" are on the right side
of commutative comparison and binary operators. A constant expression tree
has no unbound variables that refer to outer columns. It therefore always
evaluates to the same result. Note that this is possible even if the tree
contains variable expressions, as long as they are bound, such as in
uncorrelated subqueries:

SELECT * FROM a WHERE a.x = (SELECT SUM(b.x) FROM b)

The right side of the equality expression is a constant expression tree, even
though it contains an entire subquery, because it always evaluates to the same
result. The left side is not a constant expression tree, even though it
contains just a single variable, because its value can be different for each
row in the table "a".

The goal of this and related patterns is to push constant expression trees to
the right side until only a Variable remains on the left (if possible). Other
patterns can rely on this normal form and only handle one combination.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `CommuteConst`, not the other rules in that file):

```
# CommuteConst ensures that "constant expression trees" are on the right side
# of commutative comparison and binary operators. A constant expression tree
# has no unbound variables that refer to outer columns. It therefore always
# evaluates to the same result. Note that this is possible even if the tree
# contains variable expressions, as long as they are bound, such as in
# uncorrelated subqueries:
#
#   SELECT * FROM a WHERE a.x = (SELECT SUM(b.x) FROM b)
#
# The right side of the equality expression is a constant expression tree, even
# though it contains an entire subquery, because it always evaluates to the same
# result. The left side is not a constant expression tree, even though it
# contains just a single variable, because its value can be different for each
# row in the table "a".
#
# The goal of this and related patterns is to push constant expression trees to
# the right side until only a Variable remains on the left (if possible). Other
# patterns can rely on this normal form and only handle one combination.
[CommuteConst, Normalize]
(Eq | Ne | Is | IsNot | Plus | Mult | Bitand | Bitor | Bitxor
    $left:(ConstValue)
    $right:^(ConstValue)
)
=>
((OpName) $right $left)
```
- Attempts used: 22
- Last updated: 2026-09-24T10:11:28.437033+00:00
- Reason / notes: The encoding faithfully captures the rule's Eq-case semantic core—commutativity of equality—by deliberately using the concrete SqlStdOperatorTable.EQUALS operator (not an uninterpreted pred), which is exactly what lets QED know the operator is commutative and prove the non-vacuous swap x = y ↔ y = x under three-valued logic; before() and after() are structurally distinct (operand order in the predicate), so the proof is not vacuous. The join is identical scaffolding on both sides, x and y are independent columns from two distinct scans, and no source precondition is silently dropped, since the Eq swap holds under full three-valued semantics (verified across the NULL cases). The scope is honestly and specifically PARTIAL—only the Eq variant of the 9-operator rule, with the arithmetic/bitwise variants correctly identified as beyond QED's commutativity reasoning—so this is a genuine, non-degenerate special case rather than a misleading or trivially-provable encoding. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 22556750}, panicked=False

### `CommuteConstInequality` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

CommuteConstInequality is similar to CommuteConst (in scalar.opt), except
that it handles inequality comparison operators that need special handling to
commute operands.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `CommuteConstInequality`, not the other rules in that file):

```
# CommuteConstInequality is similar to CommuteConst (in scalar.opt), except
# that it handles inequality comparison operators that need special handling to
# commute operands.
[CommuteConstInequality, Normalize]
(Le | Lt | Ge | Gt $left:(ConstValue) $right:^(ConstValue))
=>
(CommuteInequality (OpName) $left $right)
```
- Attempts used: 41
- Last updated: 2026-09-25T01:25:14.779402+00:00
- Reason / notes: The encoding faithfully lifts the scalar Lt flip into the relational DSL — a maximally-general carrier (INNER cross-join of two unconstrained scans, same type "V", no shared symbols) with `LESS_THAN(x,y)` in before and `GREATER_THAN(y,x)` in after — which is a genuinely non-vacuous SMT check of the exact duality `CommuteInequality` performs (an uninterpreted-operator model would not prove the swapped-argument equivalence). The source rule's "left is a constant / right is not" guard is a firing condition, not a semantic precondition, so the proved equivalence is at least as general as the rule requires, and the missing operator pairs are an inherent limitation of the one-record-per-rule shape (a FULL version would be a family of the four Lt↔Gt / Le↔Ge / Ge↔Le / Gt↔Lt records), making the PARTIAL scope line accurate, specific, and non-degenerate. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 71246167}, panicked=False

### `CommuteRightJoin` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

CommuteRightJoin converts a RightJoin to a LeftJoin with the left and right
inputs swapped. This allows other normalization rules to only worry about the
LeftJoin case.

Extracted from `join.opt` (which defines multiple rules — implement specifically `CommuteRightJoin`, not the other rules in that file):

```
# CommuteRightJoin converts a RightJoin to a LeftJoin with the left and right
# inputs swapped. This allows other normalization rules to only worry about the
# LeftJoin case.
[CommuteRightJoin, Normalize, HighPriority]
(RightJoin $left:* $right:* $on:* $private:*)
=>
(LeftJoin $right $left $on (CommuteJoinFlags $private))
```
- Attempts used: 4
- Last updated: 2026-09-24T20:05:28.603721+00:00
- Reason / notes: The encoding is non-vacuous and semantically faithful: before() is a genuine RightJoin(L, R, P) and after() is a LeftJoin(R, L) with the *same* uninterpreted predicate P applied to the correctly permuted column references (L's column at swapped-join ordinal 1, R's at 0), followed by a projection restoring the original (L||R) output column order — exactly the semantic content of CommuteRightJoin, whose only other component (the $private flag commute) is a non-semantic backend attribute, and the source rule has no preconditions that are missing. Sharing one predicate symbol across both sides is correct, not an over-constraint, because the rule reuses the identical ON condition; the single-column-per-input shape restriction is honestly and specifically declared in the PARTIAL tag, and the proved equivalence (right join = swapped left join with consistent null-extension) is the rule's genuine, non-degenerate core claim rather than a distorted or structurally identical one.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 95942958}, panicked=False

### `CommuteVar` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

CommuteVar ensures that variable references are on the left side of
commutative comparison and binary operators. Other patterns don't need to
handle both combinations.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `CommuteVar`, not the other rules in that file):

```
# CommuteVar ensures that variable references are on the left side of
# commutative comparison and binary operators. Other patterns don't need to
# handle both combinations.
[CommuteVar, Normalize]
(Eq | Ne | Is | IsNot | Plus | Mult | Bitand | Bitor | Bitxor
        | VectorDistance | VectorCosDistance
        | VectorNegInnerProduct
    $left:^(Variable)
    $right:(Variable)
)
=>
((OpName) $right $left)
```
- Attempts used: 23
- Last updated: 2026-09-24T20:28:25.926555+00:00
- Reason / notes: The encoding is a faithful, non-vacuous proof of the Eq fragment of CommuteVar: `before()` and `after()` are structurally different (concrete `EQUALS` operand order swapped), the operand shapes match the rule's contract exactly (uninterpreted non-variable expression `e` on the left vs. column `x` — the "Variable" — on the right, over a standard filter embedding of the scalar rewrite), and using the concrete `=` operator is precisely what makes the claim checkable, since QED correctly refuses to assume commutativity of uninterpreted operators (which is a genuine prover limitation, not a DSL gap that `extend_dsl_file` could fix). The missing preconditions check passes (no PK/NOT NULL assumptions; nullable `VarType` keeps three-valued semantics in play, and `=` is symmetric in it), there are no over-constraining symbol-sharing errors (the same `e` and `x` are correctly shared between both sides), and the SCOPE line is honest and specific about the Eq-only narrowing — a genuine, useful, non-degenerate special case, notably the exact variant that `InlineExistsSelectTuple` in the same source file relies on ("CommuteVar ensures that the variable is on the left" above an `Eq` pattern).
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 68633791}, panicked=False

### `CommuteVarInequality` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

CommuteVarInequality is similar to CommuteVar (in scalar.opt), except it
handles inequality comparison operators that need special handling to commute
operands.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `CommuteVarInequality`, not the other rules in that file):

```
# CommuteVarInequality is similar to CommuteVar (in scalar.opt), except it
# handles inequality comparison operators that need special handling to commute
# operands.
[CommuteVarInequality, Normalize]
(Le | Lt | Ge | Gt $left:^(Variable) $right:(Variable))
=>
(CommuteInequality (OpName) $left $right)
```
- Attempts used: 24
- Last updated: 2026-09-25T01:39:19.557889+00:00
- Reason / notes: The encoding faithfully lifts the rule's scalar commutation to the relational level — an inner cross join of two independent same-type nullable scans (so x and y are genuinely independent operands) filtered by x≤y versus y≥x, which is precisely the Le→Ge swap produced by the source rule's `CommuteInequality(Le, left, right)`. The proof is non-vacuous (before≠after structurally), uses the concrete ≤/≥ operators that this commutation genuinely requires (an uninterpreted predicate could not be proved commutable), and holds under three-valued/null semantics since the columns are nullable. The PARTIAL scope (Le only, not Lt/Ge/Gt) is honestly and specifically labeled and is a real consequence of the DSL exposing only a single before/after pair with no meta-operator mechanism, not a hidden over-constraint that would make the result misleading.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 76185416}, panicked=False

### `ConvertGroupByToDistinct` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

ConvertGroupByToDistinct converts a GroupBy operator that has no aggregations
to an equivalent DistinctOn operator.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `ConvertGroupByToDistinct`, not the other rules in that file):

```
# ConvertGroupByToDistinct converts a GroupBy operator that has no aggregations
# to an equivalent DistinctOn operator.
[ConvertGroupByToDistinct, Normalize]
(GroupBy $input:* $aggregations:[] $groupingPrivate:*)
=>
(DistinctOn $input $aggregations $groupingPrivate)
```
- Attempts used: 23
- Last updated: 2026-09-24T11:54:50.019287+00:00
- Reason / notes: The encoding has the correct relational shape for the source rule: a no-aggregation `Aggregate` (group set = all columns, empty agg list) on a single shared input, rewritten to a set-variant self-intersect (`all=false`, the only INTERSECT QED models/serializes), which is exactly what a DistinctOn with empty aggregations computes — unique rows on the grouping columns — and the Optgen rule is unconditional, so no preconditions are silently dropped. The fixed 2-column arity is a genuine limitation of the fixed-arity DSL rather than a hard-coded concrete symbol (no baked-in predicates or join kinds; the "no extra input columns" choice is semantically inert since a no-agg GroupBy's output depends only on its grouping keys), and this restriction is honestly disclosed in the `// SCOPE: PARTIAL` line. The two sides are structurally different operators (group-by vs. set-intersect) over the same symbol, so the proof establishes a real, non-trivial equivalence — a faithful special case of ConvertGroupByToDistinct, not a vacuous one.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 76884625}, panicked=False

### `DecorrelateJoin` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

DecorrelateJoin maps an apply join into the corresponding join without an
apply if the right side of the join is not correlated with the left side.
This allows the optimizer to consider additional physical join operators that
are unable to handle correlated inputs.

NOTE: Keep this before other decorrelation patterns, as if the correlated
join can be removed first, it avoids unnecessarily matching other
patterns that only exist to get to this pattern.

Citations: [3]

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `DecorrelateJoin`, not the other rules in that file):

```
# DecorrelateJoin maps an apply join into the corresponding join without an
# apply if the right side of the join is not correlated with the left side.
# This allows the optimizer to consider additional physical join operators that
# are unable to handle correlated inputs.
#
# NOTE: Keep this before other decorrelation patterns, as if the correlated
#       join can be removed first, it avoids unnecessarily matching other
#       patterns that only exist to get to this pattern.
#
# Citations: [3]
[DecorrelateJoin, Normalize]
(JoinApply
    $left:*
    $right:* & ^(IsCorrelated $right (OutputCols $left))
    $on:*
    $private:*
)
=>
(ConstructNonApplyJoin (OpName) $left $right $on $private)
```
- Attempts used: 22
- Last updated: 2026-09-24T12:20:15.031415+00:00
- Reason / notes: The encoding faithfully captures the core DecorrelateJoin transformation: `before()` builds a `LogicalCorrelate` (INNER) where the right side is filtered by a condition referencing the left row via correlated variables, and `after()` builds a `LogicalJoin` (INNER) with the same uninterpreted predicate over both sides' fields. The right input is a plain scan with no outer references, correctly satisfying the original rule's `^(IsCorrelated $right ...)` precondition. QED is verifying a genuine semantic equivalence between two structurally distinct operators (serialized as `correlate` vs. `join` in the JSON), so the proof is non-vacuous. The PARTIAL tag honestly states the two real restrictions (INNER only; binary predicate over one column per side), both of which are genuine narrowing versus the original rule's `(OpName)` / arbitrary-column generality. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 78528417}, panicked=False

### `DeduplicateSelectFilters` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

DeduplicateSelectFilters removes duplicate filters in a Select. Duplicate
filters are usually eliminated by a combination of filter-simplifying rules,
like InlineConstVar, SimplifySelectFilters, and ConsolidateSelectFilters.
However, duplicate filters can be generated by the interaction of the
normalization rules PushFilterIntoJoinLeftAndRight and
PushSelectIntoInlinableProject when a table has a virtual column.

This rule is low priority. All other Select rules should run first to avoid
trying to deduplicate filters multiple times during normalization.

Extracted from `select.opt` (which defines multiple rules — implement specifically `DeduplicateSelectFilters`, not the other rules in that file):

```
# DeduplicateSelectFilters removes duplicate filters in a Select. Duplicate
# filters are usually eliminated by a combination of filter-simplifying rules,
# like InlineConstVar, SimplifySelectFilters, and ConsolidateSelectFilters.
# However, duplicate filters can be generated by the interaction of the
# normalization rules PushFilterIntoJoinLeftAndRight and
# PushSelectIntoInlinableProject when a table has a virtual column.
#
# This rule is low priority. All other Select rules should run first to avoid
# trying to deduplicate filters multiple times during normalization.
[DeduplicateSelectFilters, Normalize, LowPriority]
(Select $input:* $filters:* & (HasDuplicateFilters $filters))
=>
(Select $input (DeduplicateFilters $filters))
```
- Attempts used: 21
- Last updated: 2026-09-24T12:20:05.835600+00:00
- Reason / notes: before() (filter chain p, q, p) and after() (p, q) are genuinely different, and the shared uninterpreted symbol `p` correctly enforces that the two conjuncts being deduplicated are the same predicate, exactly mirroring the source rule's HasDuplicateFilters guard; nested `filter()` calls are the DSL's canonical encoding of a Select's conjunctive filter list (as in the FilterMerge example), so the identity actually proved — idempotency of a repeated conjunct under bag semantics — is precisely the semantic content of DeduplicateSelectFilters, with no missing preconditions since the rule is unconditional once duplicates exist. The restriction to a fixed three-conjunct list [p, q, p] with the duplicate non-adjacent is a genuine consequence of pattern-based encoding (the DSL has no symbolic list or relational hole for an arbitrary-length filter list), it is honestly and specifically tagged in the PARTIAL SCOPE line, and the result is non-degenerate: it establishes the core redundancy lemma that the general rule is built from, for all tables and all uninterpreted predicates. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 75012083}, panicked=False

### `DetectJoinContradiction` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

DetectJoinContradiction replaces a Join condition with False if it detects a
contradiction in the filter.

Extracted from `join.opt` (which defines multiple rules — implement specifically `DetectJoinContradiction`, not the other rules in that file):

```
# DetectJoinContradiction replaces a Join condition with False if it detects a
# contradiction in the filter.
[DetectJoinContradiction, Normalize]
(Join
    $left:*
    $right:*
    $on:[
            ...
            $item:(FiltersItem) & (IsContradiction $item)
            ...
        ] &
        ^(IsFilterFalse $on)
    $private:*
)
=>
((OpName) $left $right [ (FiltersItem (False)) ] $private)
```
- Attempts used: 30
- Last updated: 2026-09-24T20:21:46.225801+00:00
- Reason / notes: The encoding faithfully mirrors the source transformation — a join whose ON list contains a contradictory (always-false) item alongside uninterpreted remaining items is rewritten to a join with ON = literal False — with `rest` a genuinely uninterpreted predicate over the (L, R) join row and L/R fully general scans, so the proof is non-vacuous (before and after are structurally different plans, both being the empty inner join) and holds universally over all relations and all remaining predicates, exactly as the rule requires; the only narrowing is that the join kind is fixed to INNER while the source's generic `(Join ...)` pattern spans all join kinds (for which the equivalence also holds, since both ON conditions match no pair), and this is honestly and specifically tagged as PARTIAL, is inherent to the one-concrete-join-kind-per-record contract (other kinds are separate family instances rather than a single checkable encoding), and does not make the result degenerate.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 303167}, panicked=False

### `EliminateAggDistinct` — ✅ PROVED

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
- Attempts used: 4
- Last updated: 2026-09-25T06:47:57.434269+00:00
- Reason / notes: The encoding is a sound, non-vacuous special case rather than a vacuous one: `before()` and `after()` genuinely differ in the AggCall's `distinct` flag (which the JSON serializer emits), the same uninterpreted aggregate "f" is shared on both sides so the claim is really "distinctification changes nothing here," and that claim depends essentially on the disclosed premise — the scan is unique on field 0 and groups are by field 0, so every group is a singleton and bag distinctification is the identity (drop `unique=true` and QED would refute the equivalence, since it models aggregates as uninterpreted functions). The full rule (Min/Max/BoolAnd/BoolOr idempotent over arbitrary inputs) is genuinely out of reach of QED: no DSL construct or JSON field exists to attach an idempotency axiom to an uninterpreted aggregate, and the prover is the fixed arbiter, so this is a real QED limitation, not an unexplored DSL gap. The `// SCOPE: PARTIAL` line is present, specific, and accurately matches the code (uniqueness on field 0, group-by field 0, aggregation over field 0), with no missing preconditions or symbol-sharing errors.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 80494625}, panicked=False

### `EliminateAggDistinctForKeys` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateAggDistinctForKeys eliminates unnecessary AggDistinct modifiers when
it is known that the aggregation argument is unique within each group.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateAggDistinctForKeys`, not the other rules in that file):

```
# EliminateAggDistinctForKeys eliminates unnecessary AggDistinct modifiers when
# it is known that the aggregation argument is unique within each group.
[EliminateAggDistinctForKeys, Normalize]
(GroupBy | ScalarGroupBy
    $input:* & (HasStrictKey $input)
    $aggregations:[
        ...
        $item:(AggregationsItem (AggDistinct $agg:*))
        ...
    ]
    $groupingPrivate:* &
        (CanRemoveAggDistinctForKeys
            $input
            $groupingPrivate
            $agg
        )
)
=>
((OpName)
    $input
    (ReplaceAggregationsItem $aggregations $item $agg)
    $groupingPrivate
)
```
- Attempts used: 4
- Last updated: 2026-09-25T06:58:46.185021+00:00
- Reason / notes: The encoding faithfully captures a genuine special case of the rule (input is a scan whose unique key is also the grouping key and the DISTINCT aggregate's argument), `before()` and `after()` differ only in the `distinct` flag on an otherwise-identical uninterpreted aggregate call so the proof is not vacuous, the `unique=true` scan constraint is precisely the precondition QED needs to equate the per-group bag with the per-group set (making the SMT step non-trivial), and the PARTIAL scope tag honestly and specifically documents that the general "grouping cols functionally determine the agg arg over an arbitrary input" condition is inexpressible — leaving a useful, non-degenerate result.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 75363042}, panicked=False

### `EliminateAggFilteredDistinctForKeys` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateAggFilteredDistinctForKeys is similar to EliminateAggDistinctForKeys,
except that it works when an AggFilter operator is also present.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateAggFilteredDistinctForKeys`, not the other rules in that file):

```
# EliminateAggFilteredDistinctForKeys is similar to EliminateAggDistinctForKeys,
# except that it works when an AggFilter operator is also present.
[EliminateAggFilteredDistinctForKeys, Normalize]
(GroupBy | ScalarGroupBy
    $input:* & (HasStrictKey $input)
    $aggregations:[
        ...
        $item:(AggregationsItem
            (AggFilter (AggDistinct $agg:*) $filter:*)
        )
        ...
    ]
    $groupingPrivate:* &
        (CanRemoveAggDistinctForKeys
            $input
            $groupingPrivate
            $agg
        )
)
=>
((OpName)
    $input
    (ReplaceAggregationsItem
        $aggregations
        $item
        (AggFilter $agg $filter)
    )
    $groupingPrivate
)
```
- Attempts used: 62
- Last updated: 2026-09-24T20:55:26.736192+00:00
- Reason / notes: Manually re-derived and verified by Claude (not the automated porter/verifier LLM loop, which exhausted both pool attempts on repeated LLM context-length crashes before ever reaching a proof attempt). The automated run's last crash-time candidate (stashed at UnprovableRRuleInstances/EliminateAggFilteredDistinctForKeys.java.rejected) had already identified the right narrow special case — aggregation argument = the single unique grouping key — but was missing the required SCOPE tag and was never actually run through QED before the crash. Testing it directly: provable=true. Verified non-vacuous with a negative control (dropping the scan's unique=true flag, everything else identical) which correctly comes back provable=false — confirming the proof genuinely depends on the uniqueness fact (every group has at most one row, so AggDistinct trivially coincides with the plain aggregate), not some artifact QED would accept unconditionally. The source rule's AggFilter wrapper is not modeled (RelRN/AggCall has no FILTER-clause construct) — the covered identity is the AggDistinct->plain rewrite alone, on the premise that $filter is present unchanged on both sides of the source rule and that filtering rows out of an already-unique-keyed relation cannot introduce duplicates into what remains, so the argument extends unaffected to the filtered case; only the AggFilter wrapper itself goes unverified here. NOTE: this same narrow encoding, tested directly against the sibling rule EliminateAggDistinctForKeys (already recorded SKIPPED), also comes back provable=true with the same non-vacuous negative-control behavior — contradicting that rule's recorded verifier reasoning that 'no encoding, including the narrow primary-key special case, can prove this.' That looks like a false SKIPPED verdict worth reopening, but I have not touched that entry since it wasn't the rule asked about here.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 30284291}, panicked=False

### `EliminateAntiJoin` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

EliminateAntiJoin discards an AntiJoin operator when it's known that the right
input never returns any rows.

Extracted from `join.opt` (which defines multiple rules — implement specifically `EliminateAntiJoin`, not the other rules in that file):

```
# EliminateAntiJoin discards an AntiJoin operator when it's known that the right
# input never returns any rows.
[EliminateAntiJoin, Normalize]
(AntiJoin | AntiJoinApply
    $left:*
    $right:* & (HasZeroRows $right)
)
=>
$left
```
- Attempts used: 5
- Last updated: 2026-09-24T20:29:57.814206+00:00
- Reason / notes: The encoding is faithful and non-trivial: it uses an uninterpreted left relation, a zero-row right (the canonical `Empty` form, which is exactly the `HasZeroRows` precondition and the only way the DSL can express it), an arbitrary uninterpreted anti-join condition (covering any ON-list, including empty, since QED quantifies over the predicate), and `JoinRelType.ANTI` (left anti-join) is the correct kind for CockroachDB's AntiJoin, with the condition correctly dropped in `after()` — so bag semantics give `left` exactly, with no symbol-sharing or missing-precondition issues. The single narrowing, the correlated `AntiJoinApply` variant, is explicitly disclosed in the SCOPE line, is a genuine shape-based restriction, and is semantically subsumed by the proven case (with a provably empty right side the correlation is moot, so the lemma covers the rule's entire semantic content).
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 15469000}, panicked=False

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

### `NegateComparison` — ✅ PROVED

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
- Attempts used: 21
- Last updated: 2026-09-25T07:31:01.548614+00:00
- Reason / notes: The proof is non-vacuous and genuine: `before()` is `Filter(¬(x = y))` vs `after()` `Filter(x <> y)` over the cross-join of two *independent* single-column scans, so x and y are universally quantified independent values and the equivalence (which holds even under null semantics, where both sides share the same three-valued truth table) is a real theorem covering the Eq→Ne instance of the source rule's operator map for all value pairs. Using the concrete `EQUALS`/`NOT_EQUALS` operators is correct here, not hard-coding: the rule's entire content is the semantic link between those two operators, which would be unprovable — and false as stated — with independent uninterpreted predicate symbols, and the filter context is the natural relational home for this expression-level rule in a RelRN-based DSL. There are no symbol-sharing or missing-precondition issues (the source's `CanNegateComparison` guard only excludes JSON/geospatial operators, irrelevant to Eq), and the SCOPE line honestly and specifically discloses the narrowing to one of the 12 operator pairs.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 363500}, panicked=False

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

### `SimplifyLeftJoin` — ✅ PROVED

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
- Attempts used: 23
- Last updated: 2026-09-25T07:54:35.698404+00:00
- Reason / notes: The encoding is non-vacuous and correctly shaped: `before()` and `after()` genuinely differ only in join kind (LEFT vs INNER), and the equivalence is a real theorem that holds *only* because both inputs are the same scan and the condition is `col = col` on a non-nullable column — this is precisely the structural encoding of the rule's `JoinFiltersMatchAllLeftRows` precondition (the self-join example from the rule's own doc comment), and each piece is load-bearing: two independent scans would fail (right side could be empty or lack a matching value) and a nullable column would fail (NULL = NULL is not true, so a NULL left row would get null-extended), so QED could not have proved a looser, wrong claim. The narrowing to plain `LeftJoin` with a single non-nullable-column self-join on equality is specific, non-degenerate, and accurately disclosed in the SCOPE line, so the provable result faithfully certifies a genuine special case of the source rule rather than a trivial or accidentally over-constrained one.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 51125125}, panicked=False

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

### `TryDecorrelateSelect` — ✅ PROVED

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
- Attempts used: 62
- Last updated: 2026-09-24T20:50:51.317961+00:00
- Reason / notes: Manually re-derived and verified by Claude (not the automated porter/verifier LLM loop), after the automated attempt twice produced a vacuous proof (mismatched ON-condition arity between before()/after()). Key insight: the source rule's `(HasOuterCols $right)` guard is an application-scope heuristic for when CockroachDB's optimizer fires this rule during decorrelation, not a soundness precondition — the identity `Join(L, Select(R, pred), on) = Join(L, R, on AND pred)` holds for INNER, LEFT, SEMI, and ANTI regardless of whether pred/on reference outer (correlated) columns, because R's row set that survives to be matched against a given L row is identical whether pred is applied as a pre-filter or folded into the join's row-level ON test. This encoding shares a single `select_filter` predicate symbol at the same arity (1-ary over R's column) between the row-scoped filter (before()) and the join-scoped ON conjunct (after()), avoiding the arity-mismatch bug that made the prior attempt vacuous. QED confirms provable=true with complete_fragment=true (the fully-verified decidable fragment, restricted to INNER joins in this DSL/prover). Verified non-vacuous via three negative controls, each correctly rejected as provable=false: (1) dropping the pushed predicate from after()'s ON condition, (2) referencing the wrong join field (L's column instead of R's) in the pushed predicate. LEFT/SEMI/ANTI variants of this same encoding also returned provable=true, but with complete_fragment=false (inherent to any non-Inner join in this prover — see qed-prover/src/pipeline/relation.rs Relation::complete(), which only returns true for JoinKind::Inner — not a specific red flag about this proof, but outside the prover's formally-guaranteed decidable fragment). Scoped to INNER only here to match this project's established conservative practice (e.g. PushFilterIntoJoinLeft/Right also restrict to a fragment narrower than the full source rule); LEFT/SEMI/ANTI coverage is a candidate follow-up, not confirmed to the same standard.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 19209459}, panicked=False

### `ApplyLimitToRecursiveCTEScan` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/with.opt

ApplyLimitToRecursiveCTEScan updates the properties of the recursive with
scans in the input of a recursive CTE to reflect a limit that applies to
all iterations.

Extracted from `with.opt` (which defines multiple rules — implement specifically `ApplyLimitToRecursiveCTEScan`, not the other rules in that file):

```
# ApplyLimitToRecursiveCTEScan updates the properties of the recursive with
# scans in the input of a recursive CTE to reflect a limit that applies to
# all iterations.
[ApplyLimitToRecursiveCTEScan, Normalize]
(RecursiveCTE
    $binding:* & ^(HasBoundedCardinality $binding)
    $initial:* & (HasBoundedCardinality $initial)
    $recursive:* & (HasBoundedCardinality $recursive)
    $private:*
)
=>
(ApplyLimitToRecursiveCTEScan
    $binding
    $initial
    $recursive
    $private
)
```
- Attempts used: 6
- Last updated: 2026-09-25T01:07:26.741610+00:00
- Reason / notes: The rule's precondition is a set of subplan cardinality side conditions (HasBoundedCardinality) and its effect is a backend marker operator that changes operator *properties*, not the produced relation — RuleScript's before/after patterns can only express row-level relational structure, with no mechanism to state cardinality-bound side conditions on subexpressions or to capture property propagation at all. Additionally, the operators the rule manipulates are outside QED's model: RecursiveCTE/fixpoint has no bag-semantic meaning, and Limit is explicitly among the operators QED cannot reason about, so neither side of the rewrite is encodable; extend_dsl_file cannot close this gap because the trusted Rust prover (off-limits for modification) has no Q-expression semantics for recursion or ordering/limit. ```

### `CollapseRepeatedLikePatternWildcards` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

CollapseLikePatternWildcards collapses repeated '%' wildcards into a single
'%' in the pattern of a LIKE expression.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `CollapseRepeatedLikePatternWildcards`, not the other rules in that file):

```
# CollapseLikePatternWildcards collapses repeated '%' wildcards into a single
# '%' in the pattern of a LIKE expression.
[CollapseRepeatedLikePatternWildcards, Normalize]
(Like | NotLike | ILike | NotILike
    $input:*
    $pattern:* &
        (Let
            (
                $collapsed
                $ok
            ):(CollapseRepeatedLikePatternWildcards $pattern)
            $ok
        )
)
=>
((OpName) $input $collapsed)
```
- Attempts used: 21
- Last updated: 2026-09-25T01:11:07.344978+00:00
- Reason / notes: The rule's soundness rests entirely on glob-pattern semantics of LIKE's pattern argument — that a run of `%` wildcards matches exactly the same string set as a single `%` — which is an internal string-semantics property QED cannot see, since it models predicates only as uninterpreted functions over bag semantics and its SMT backend has no string/glob theory. No DSL extension can close this gap: even with a string-literal constructor, `LIKE(x,'a%%b')` and `LIKE(x,'a%b')` remain two distinct uninterpreted function applications over which SMT can build distinguishing countermodels, and the prover itself is a fixed, unmodifiable arbiter. Hence the only encodable form reuses one predicate symbol for both sides, degenerating into a vacuous identity — a genuine fundamental limitation, not a missing operator the porter failed to find.

### `CommuteNullIs` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

CommuteNullIs moves a NULL onto the right side of an IS/IS NOT comparison.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `CommuteNullIs`, not the other rules in that file):

```
# CommuteNullIs moves a NULL onto the right side of an IS/IS NOT comparison.
[CommuteNullIs, Normalize]
(Is | IsNot $left:(Null) $right:^(Null))
=>
((OpName) $right $left)
```
- Attempts used: 43
- Last updated: 2026-09-25T04:19:13.537920+00:00
- Reason / notes: The rewrite's validity rests entirely on the null-aware commutativity of CockroachDB's Is/IsNot operators (Is(NULL,x) ≡ Is(x, NULL)), which requires null-aware-equality semantics (NULL IS NULL is TRUE, whereas NULL = NULL is UNKNOWN) that QED's built-in three-valued equality cannot express and that no RexRN/RelRN construct can build. Any Is/NULL symbol introduced via extend_dsl_file would land as an uninterpreted operator in the fixed prover's SMT encoding, where only congruence holds and no axiom relates Is(a,b) to Is(b,a) or to a null-test, so no relational encoding (e.g. Filter(Is(NULL,col),R) ≡ Filter(Is(col,NULL),R)) is provable — this is squarely the "bespoke internal semantics of a backend operator QED cannot see through as an uninterpreted function" limitation, and the prover itself cannot be given the missing axiom. ```

### `ConsolidateSelectFilters` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

ConsolidateSelectFilters consolidates filters that constrain a single
variable. For example, filters x >= 5 and x <= 10 would be combined into a
single Range operation.

The benefit of consolidating these filters is it allows a single constraint
to be generated for the variable instead of multiple. In the example above,
we can generate the single constraint [/5 - /10] instead of the two
constraints [/5 - ] and [ - /10]. The single constraint allows us to better
estimate the selectivity of the predicate when calculating statistics for
the Select expression.

This rule is low priority so other rules in this file such as
RemoveNotNullCondition can run first.

Extracted from `select.opt` (which defines multiple rules — implement specifically `ConsolidateSelectFilters`, not the other rules in that file):

```
# ConsolidateSelectFilters consolidates filters that constrain a single
# variable. For example, filters x >= 5 and x <= 10 would be combined into a
# single Range operation.
#
# The benefit of consolidating these filters is it allows a single constraint
# to be generated for the variable instead of multiple. In the example above,
# we can generate the single constraint [/5 - /10] instead of the two
# constraints [/5 - ] and [ - /10]. The single constraint allows us to better
# estimate the selectivity of the predicate when calculating statistics for
# the Select expression.
#
# This rule is low priority so other rules in this file such as
# RemoveNotNullCondition can run first.
[ConsolidateSelectFilters, Normalize, LowPriority]
(Select $input:* $filters:* & (CanConsolidateFilters $filters))
=>
(Select $input (ConsolidateFilters $filters))
```
- Attempts used: 43
- Last updated: 2026-09-25T04:33:47.399765+00:00
- Reason / notes: The rule's entire semantic content is that CockroachDB's `Range` predicate is a transparent wrapper over its inner conjunction (Range(e) ≡ e in filter context); in RuleScript, `Range` can only be introduced as an uninterpreted predicate symbol, and QED fundamentally cannot prove an uninterpreted symbol equivalent to its argument, and the JSON/DSL format has no channel for operator axioms (the "guaranteed" field only attaches to base-table scans, and RelRN/RexRN/JSONSerializer carry no operator-axiom construct), so extending the DSL cannot supply the required transparency axiom without modifying the trusted prover. Any encoding that drops `Range` collapses the rule to nested-filter AND merging — i.e. the already PROVED FULL FilterMerge — so there is no genuine non-trivial PARTIAL fallback to port. ```

### `ConvertCountToCountRows` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

ConvertCountToCountRows replaces a Count operator performed on a non-null
expression with a CountRows operator. CountRows is significantly faster to
execute than Count.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `ConvertCountToCountRows`, not the other rules in that file):

```
# ConvertCountToCountRows replaces a Count operator performed on a non-null
# expression with a CountRows operator. CountRows is significantly faster to
# execute than Count.
[ConvertCountToCountRows, Normalize]
(GroupBy | ScalarGroupBy
    $input:*
    $aggregations:[
        ...
        $item:(AggregationsItem (Count $arg:*)) &
            (ExprIsNeverNull $arg (NotNullCols $input))
        ...
    ]
    $groupingPrivate:*
)
=>
((OpName)
    $input
    (ReplaceAggregationsItem $aggregations $item (CountRows))
    $groupingPrivate
)
```
- Attempts used: 1
- Last updated: 2026-09-25T05:31:47.400997+00:00
- Reason / notes: Manually investigated by Claude (not the automated porter/verifier LLM loop, which exhausted all 5 rounds on repeated context-length crashes without ever completing a try_rule call). A prior crashed attempt's cached JSON (.cache/tmp-rules/ConvertCountToCountRows.json) showed it had found QED's real built-in COUNT handling (qed-prover/src/pipeline/relation.rs special-cases the literal string "COUNT") but used the lowercase generic-aggregate operator name "count", which falls through to the generic/uninterpreted-HOp path instead — structurally unrelatable between the two sides, hence not provable. Fixing the operator name to "COUNT" (the RuleScript DSL lets a porter pick any string as an aggregate's name, so this required no DSL change) does make QED report provable=true. However, this is a VACUOUS proof, confirmed via a negative control: the exact same encoding still reports provable=true even when the aggregated column's schema is marked nullable=true (i.e. even when the source rule's actual precondition, ExprIsNeverNull, is violated). The reason: JSONSerializer.java always emits "ignoreNulls": bool(call.ignoreNulls()), and RelRN.Aggregate.semantics() builds the Calcite AggregateCall via the plain RelBuilder.aggregateCall(op, distinct, filter=null, name, operands) overload, which Calcite defaults to ignoreNulls=false with no DSL-exposed way to override it — RelRN.AggCall has no ignoreNulls field at all. QED's own prover core (relation.rs) DOES implement real null-skipping when ignore_nulls=true (it ANDs in a not-null predicate per aggregated row before summing), but the DSL can never actually produce that flag as true, so any COUNT(x)-vs-COUNT() proof built with today's DSL is really proving the strictly weaker, unconditional identity 'COUNT(x) with no null-skipping == COUNT(*)' (true for ANY column, nullable or not, since both sides just tally rows) rather than the source rule's actual conditional identity. Publishing that as PROVED would misrepresent what was verified. This is a genuine, fixable DSL gap (add an ignoreNulls control to RelRN.AggCall, threaded through a RelBuilder call that supports it) rather than a fundamental QED limitation — QED's core already models null-skipping correctly — so it's recorded here as SKIPPED with this specific, actionable cause rather than a generic FAILED.

### `ConvertJSONSubscriptToFetchValue` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

ConvertJSONSubscriptToFetchValue converts json subscripting
`[...]` into a fetch value operator `->`.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `ConvertJSONSubscriptToFetchValue`, not the other rules in that file):

```
# ConvertJSONSubscriptToFetchValue converts json subscripting
# `[...]` into a fetch value operator `->`.
[ConvertJSONSubscriptToFetchValue, Normalize]
(Indirection $input:* $index:* & (IsJSON $input))
=>
(FetchVal $input $index)
```
- Attempts used: 25
- Last updated: 2026-09-25T05:45:12.771131+00:00
- Reason / notes: This rule is purely an operator-aliasing identity: it asserts that two syntactically distinct scalar operators (JSON indirection `[...]` and fetch value `->`) agree on JSON inputs. In RuleScript/QED that forces two *distinct* uninterpreted projection symbols, and QED's theory (bag semantics over uninterpreted functions) contains no axiom relating distinct function symbols — so a countermodel exists even restricted to the guarded (IsJSON) domain, since the guard is itself uninterpreted and the two functions can be instantiated to differ on it. No encoding or DSL extension can close this: the JSON format has no notion of "these two operator names denote the same function," and the prover (the unchangeable arbiter) is the only place such an axiom could live — this is exactly the "backend operator's bespoke internal semantics" limitation, and the porter's complete (non-timeout) SMT refutation on the fixed two-column encoding confirms it. ```

### `ConvertLevenshteinToLevenshteinLessEqualLeft` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

ConvertLevenshteinToLevenshteinLessEqualLeft converts a comparison of an
integer and the result of a levenshtein function into a comparison with a
levenshtein_less_equal function.

levenshtein('foo', 'bar') < 5
=>
levenshtein_less_equal('foo', 'bar', 5) < 5

levenshtein_less_equal is more efficient than levenshtein because it returns
early once the distance exceeds the given max distance.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `ConvertLevenshteinToLevenshteinLessEqualLeft`, not the other rules in that file):

```
# ConvertLevenshteinToLevenshteinLessEqualLeft converts a comparison of an
# integer and the result of a levenshtein function into a comparison with a
# levenshtein_less_equal function.
#
#   levenshtein('foo', 'bar') < 5
#   =>
#   levenshtein_less_equal('foo', 'bar', 5) < 5
#
# levenshtein_less_equal is more efficient than levenshtein because it returns
# early once the distance exceeds the given max distance.
[ConvertLevenshteinToLevenshteinLessEqualLeft, Normalize]
(Eq | Ge | Gt | Le | Lt
    (Function $args:* $private:(FunctionPrivate "levenshtein"))
    $right:* &
        (IsInt $right) &
        (Let ($arg1 $arg2 $ok):(ScalarPair $args) $ok)
)
=>
((OpName)
    (MakeLevenshteinLessEqualFunction $arg1 $arg2 $right)
    $right
)
```
- Attempts used: 41
- Last updated: 2026-09-25T06:01:41.266984+00:00
- Reason / notes: The rule's validity rests entirely on the backend's clamp identity `levenshtein_less_equal(a,b,n) = min(levenshtein(a,b), n)`, but RuleScript can only introduce these as independent uninterpreted symbols and offers no way to state any relationship between them — RexRN has no equality/arithmetic/min, there are no function-axiom mechanisms, and a scan's "guaranteed" constraint would itself be an uninterpreted predicate over columns that cannot bridge two distinct function terms. Since QED must prove equivalence for *all* instantiations of uninterpreted symbols and the fixed prover has no path for predicate inference between independent symbols, no genuine encoding is provable — this is precisely the "bespoke internal semantics of a backend operator" limitation.

### `ConvertLevenshteinToLevenshteinLessEqualRight` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

ConvertLevenshteinToLevenshteinLessEqualRight is the same as
ConvertLevenshteinToLevenshteinLessEqualLeft but matches comparisons with the
levenshtein function on the RHS.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `ConvertLevenshteinToLevenshteinLessEqualRight`, not the other rules in that file):

```
# ConvertLevenshteinToLevenshteinLessEqualRight is the same as
# ConvertLevenshteinToLevenshteinLessEqualLeft but matches comparisons with the
# levenshtein function on the RHS.
[ConvertLevenshteinToLevenshteinLessEqualRight, Normalize]
(Eq | Ge | Gt | Le | Lt
    $left:* & (IsInt $left)
    (Function $args:* $private:(FunctionPrivate "levenshtein")) &
        (Let ($arg1 $arg2 $ok):(ScalarPair $args) $ok)
)
=>
((OpName)
    $left
    (MakeLevenshteinLessEqualFunction $arg1 $arg2 $left)
)
```
- Attempts used: 21
- Last updated: 2026-09-25T05:51:20.920855+00:00
- Reason / notes: The rewrite `x OP levenshtein(s,t) ⟺ x OP levenshtein_less_equal(s,t,x)` is only valid because of CockroachDB's internal clamping contract for `levenshtein_less_equal` (returns the true distance when it is ≤ the bound, and a value strictly greater than the bound otherwise) — and checking each of Eq/Ge/Gt/Le/Lt, every one of the five directions of the equivalence depends on that relationship, which is an entailment between two function symbols that QED models as independent uninterpreted functions. RuleScript's core language offers no arithmetic or conditional terms and no axiom/assume mechanism to state such a contract, and since the frozen prover itself never relates independent uninterpreted symbols, no DSL builder extension could communicate it either — so no general or special-cased encoding (even with a literal bound, which would still leave the predicate and both function symbols uninterpreted) is provable. ```

### `ConvertLikeEscapeToLike` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

ConvertLikeEscapeToLike converts a LIKE x ESCAPE '\' expression, which is
parsed as a like_escape function call, to a LIKE expression. This is valid
because the default escape character is '\'.

Note that the FunctionExpr match pattern is enough to ensure that we are not
transforming a UDF because UDF invocations are always built as UDFCallExprs.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `ConvertLikeEscapeToLike`, not the other rules in that file):

```
# ConvertLikeEscapeToLike converts a LIKE x ESCAPE '\' expression, which is
# parsed as a like_escape function call, to a LIKE expression. This is valid
# because the default escape character is '\'.
#
# Note that the FunctionExpr match pattern is enough to ensure that we are not
# transforming a UDF because UDF invocations are always built as UDFCallExprs.
[ConvertLikeEscapeToLike, Normalize]
(Function
    $args:*
    $private:(FunctionPrivate "like_escape") &
        (Let ($input $iOk):(ScalarExprAt $args 0) $iOk) &
        (Let ($pattern $pOk):(ScalarExprAt $args 1) $pOk) &
        (Let ($escape $eOk):(ScalarExprAt $args 2) $eOk) &
        (ConstStringEquals $escape "\\")
)
=>
(Like $input $pattern)
```
- Attempts used: 43
- Last updated: 2026-09-25T06:08:28.520442+00:00
- Reason / notes: The rule's correctness rests entirely on CockroachDB's backend-specific semantic that `LIKE`'s default escape character is `'\'`, i.e. the entailment `like_escape(x, p, '\')` ⟺ `like(x, p)`. In QED both `like_escape` and `like` can only be introduced as distinct uninterpreted predicate symbols, and the SMT solver has no axiom relating independent symbols (or symbols of different arity), so the equivalence is invalid under the interpretations QED quantifies over. No DSL extension can close this gap, since the missing piece is operator-specific semantic knowledge that lives in the prover — the trusted, unmodifiable arbiter — rather than a missing operator or shape in the DSL. ```

### `ConvertRegressionCountToCount` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

ConvertRegressionCountToCount replaces a RegressionCount operator
performed on a non-null expression with a Count operator. Count can be
normalized again to CountRows which is significantly faster to execute
than RegressionCount.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `ConvertRegressionCountToCount`, not the other rules in that file):

```
# ConvertRegressionCountToCount replaces a RegressionCount operator
# performed on a non-null expression with a Count operator. Count can be
# normalized again to CountRows which is significantly faster to execute
# than RegressionCount.
[ConvertRegressionCountToCount, Normalize]
(GroupBy | ScalarGroupBy
    $input:*
    $aggregations:[
        ...
        $item:(AggregationsItem
                (RegressionCount $arg1:* $arg2:*)
            ) &
            (Let
                ($newArg $ok):(SingleRegressionCountArgument
                    $arg1
                    $arg2
                    $input
                )
                $ok
            )
        ...
    ]
    $groupingPrivate:*
)
=>
((OpName)
    $input
    (ReplaceAggregationsItem $aggregations $item (Count $newArg))
    $groupingPrivate
)
```
- Attempts used: 67
- Last updated: 2026-09-25T06:39:54.589255+00:00
- Reason / notes: The rewrite's correctness rests entirely on a specific algebraic identity between two *different* named aggregates — `RegressionCount(a,b)` (counts rows where both args are non-null) equaling `Count(b)` (counts rows where one arg is non-null) under the side condition that `a` is provably never-null — and QED treats aggregate names as uninterpreted beyond bag equality of their input, with no mechanism in the Java DSL or JSON format to declare such a semantic axiom (the prover is Rust-side and untouchable). Any genuine instance of this rule necessarily changes the aggregate's name and arity, so no non-vacuous special case survives: even the narrowest encoding (e.g. one arg a non-nullable-typed column) still requires the prover to bridge an opaque 2-operand `RegressionCount` to an interpreted 1-operand `Count`, which SMT will refute by choosing an interpretation of the uninterpreted aggregate that disagrees with the count. This is precisely the documented limitation "knows nothing about a specific aggregate function's algebra beyond bag equality of its input" / "a backend operator's bespoke internal semantics," not a gap `extend_dsl_file` could close (multi-operand `AggCall` already exists; only the prover-side semantics are missing). ```

### `ConvertUncorrelatedExistsToCoalesceSubquery` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

ConvertUncorrelatedExistsToCoalesceSubquery converts an uncorrelated Exists
expression to a Coalesce expression with a Subquery. For example:

SELECT EXISTS (
SELECT * FROM b
) FROM a
=>
SELECT COALESCE(
(SELECT true FROM (SELECT * FROM b) LIMIT 1),
false
) FROM a

This transformation simplifies execbuilder and execution code - we do not need
a special execution mode for uncorrelated Exists.

Note: We can use an empty ordering for the Limit because Exists never have an
ordering.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `ConvertUncorrelatedExistsToCoalesceSubquery`, not the other rules in that file):

```
# ConvertUncorrelatedExistsToCoalesceSubquery converts an uncorrelated Exists
# expression to a Coalesce expression with a Subquery. For example:
#
#   SELECT EXISTS (
#     SELECT * FROM b
#   ) FROM a
#   =>
#   SELECT COALESCE(
#     (SELECT true FROM (SELECT * FROM b) LIMIT 1),
#     false
#   ) FROM a
#
# This transformation simplifies execbuilder and execution code - we do not need
# a special execution mode for uncorrelated Exists.
#
# Note: We can use an empty ordering for the Limit because Exists never have an
# ordering.
[ConvertUncorrelatedExistsToCoalesceSubquery, Normalize]
(Exists $input:* & ^(HasOuterCols $input) $existsPrivate:*)
=>
(Coalesce
    [
        (Subquery
            (Project
                (Limit
                    $input
                    (IntConst (DInt 1))
                    (EmptyOrdering)
                )
                [ (ProjectionsItem (True) (MakeBoolCol)) ]
                (MakeEmptyColSet)
            )
            (EmbeddedSubqueryPrivate $existsPrivate)
        )
        (False)
    ]
)
```
- Attempts used: 24
- Last updated: 2026-09-25T06:15:21.834748+00:00
- Reason / notes: Both sides of the rewrite are scalar expressions over nested sub-relations — an EXISTS on the left, and COALESCE of a scalar subquery (a Project over a LIMIT) and false on the right — and QED's decision procedure cannot decide scalar expressions that contain a sub-relation at all (the prover marks such nodes non-complete/unhandled), so it has no way to even state the equivalence between the two forms. The rule's soundness also load-bears on LIMIT's row-cap (at most one row, non-empty iff input non-empty), the empty-scalar-subquery→NULL convention, and COALESCE's first-non-null behavior, all of which are bespoke uninterpreted semantics outside QED's bag algebra (and Limit has no bag-semantic meaning per its own evaluation), so no DSL-builder extension can supply them — consistent with the prior SubQueryRemove check where the Exists builder was added and the prover still rejected the encoding. ```

### `ConvertUnionToDistinctUnionAll` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/set.opt

ConvertUnionToDistinctUnionAll replaces a Union with a DistinctOn on top of a
UnionAll. This is a valid transformation when we can obtain a key over the
output of the UnionAll that not only functionally determines all columns from
both inputs, but functionally determines the *same* values from both inputs.
ConvertUnionToDistinctUnionAll can match when the left and right inputs satisfy
the following conditions:

1) All columns from both inputs originate from the same base table. This is
necessary because it is safe to de-duplicate over a subset of columns
that form a key over the base table (assuming condition #2 is also
satisfied).

2) All columns from a given side originate from the same meta table. This
avoids cases where joins reuse the same ColumnIDs but add nulls or mix
columns from different subqueries on the same table.

3) Each pair of columns whose rows are unioned together occupy the same
ordinal positions in the original base table. This ensures that the
output (and inputs) of the UnionAll only contains tuples that existed in
the base table (though it may contain duplicates, and with the exception
of null-extension - see condition #5).

4) The output columns of each of the inputs form a strict key over the base
table. It is not sufficient to use keys directly from the input
expressions because the keys from the input expressions may have dropped
columns due to filtering, when those columns may be necessary to
distinguish rows resulting from the union. Ex: union together the same
(single) row, for which the empty set is a key.

5) There must be at least one key column, since in the empty-key case
null-extension by outer joins can violate the requirement that a given
tuple of values on the key columns implies the same values on all other
columns over both sides. (e.g. for an empty key, the key values would
always be an empty tuple, while the remaining columns could have
different values). Null-extension is allowed when the key is non-empty
because when all columns have the same (NULL) value, grouping on any
subset of them results in one (all-NULL) row. This condition only applies
in the rare case when a table can be statically proven to contain only
one row (see #85502).

6) Finally, the key columns must form a strict subset of the union columns.
This is not strictly necessary for correctness, but the transformation
does not gain anything if the number of columns to de-duplicate on does
not decrease.

The above conditions ensure that the DistinctOn-UnionAll complex is equivalent
to the original Union. This transformation allows less comparisons to be made
in de-duplicating the rows, which can add up to significant speedups when rows
are wide. Cases like this one can be produced by rules like SplitDisjunction
and SplitScanIntoUnionScans, which produce a Union over a series of scans over
the same table.

Extracted from `set.opt` (which defines multiple rules — implement specifically `ConvertUnionToDistinctUnionAll`, not the other rules in that file):

```
# ConvertUnionToDistinctUnionAll replaces a Union with a DistinctOn on top of a
# UnionAll. This is a valid transformation when we can obtain a key over the
# output of the UnionAll that not only functionally determines all columns from
# both inputs, but functionally determines the *same* values from both inputs.
# ConvertUnionToDistinctUnionAll can match when the left and right inputs satisfy
# the following conditions:
#
#    1) All columns from both inputs originate from the same base table. This is
#       necessary because it is safe to de-duplicate over a subset of columns
#       that form a key over the base table (assuming condition #2 is also
#       satisfied).
#
#    2) All columns from a given side originate from the same meta table. This
#       avoids cases where joins reuse the same ColumnIDs but add nulls or mix
#       columns from different subqueries on the same table.
#
#    3) Each pair of columns whose rows are unioned together occupy the same
#       ordinal positions in the original base table. This ensures that the
#       output (and inputs) of the UnionAll only contains tuples that existed in
#       the base table (though it may contain duplicates, and with the exception
#       of null-extension - see condition #5).
#
#    4) The output columns of each of the inputs form a strict key over the base
#       table. It is not sufficient to use keys directly from the input
#       expressions because the keys from the input expressions may have dropped
#       columns due to filtering, when those columns may be necessary to
#       distinguish rows resulting from the union. Ex: union together the same
#       (single) row, for which the empty set is a key.
#
#    5) There must be at least one key column, since in the empty-key case
#       null-extension by outer joins can violate the requirement that a given
#       tuple of values on the key columns implies the same values on all other
#       columns over both sides. (e.g. for an empty key, the key values would
#       always be an empty tuple, while the remaining columns could have
#       different values). Null-extension is allowed when the key is non-empty
#       because when all columns have the same (NULL) value, grouping on any
#       subset of them results in one (all-NULL) row. This condition only applies
#       in the rare case when a table can be statically proven to contain only
#       one row (see #85502).
#
#    6) Finally, the key columns must form a strict subset of the union columns.
#       This is not strictly necessary for correctness, but the transformation
#       does not gain anything if the number of columns to de-duplicate on does
#       not decrease.
#
# The above conditions ensure that the DistinctOn-UnionAll complex is equivalent
# to the original Union. This transformation allows less comparisons to be made
# in de-duplicating the rows, which can add up to significant speedups when rows
# are wide. Cases like this one can be produced by rules like SplitDisjunction
# and SplitScanIntoUnionScans, which produce a Union over a series of scans over
# the same table.
[ConvertUnionToDistinctUnionAll, Normalize]
(Union
    $left:*
    $right:*
    $private:(SetPrivate $leftCols:* $rightCols:* $outCols:*) &
        (Let
            ($keyCols $ok):(CanConvertUnionToDistinctUnionAll
                $leftCols
                $rightCols
            )
            $ok
        )
)
=>
(DistinctOn
    (UnionAll $left $right $private)
    (MakeAggCols
        ConstAgg
        (TranslateColSet
            (DifferenceCols (OutputCols $left) $keyCols)
            $leftCols
            $outCols
        )
    )
    (MakeGrouping
        (TranslateColSet $keyCols $leftCols $outCols)
        (EmptyOrdering)
    )
)
```
- Attempts used: 41
- Last updated: 2026-09-25T07:17:24.865146+00:00
- Reason / notes: The rule's after side is a DistinctOn that dedups on a strict subset of columns (the key) while retaining all output columns, i.e. an arbitrary "ConstAgg" selection of the non-key values per key group, and no operator in QED's prover language has those semantics: the set-family operators (distinct/union/intersect/except) only dedup on the whole row, and GroupBy's non-key outputs are uninterpreted aggregates that QED is told nothing about beyond bag equality of their inputs, so no expression can stand in for the dedup side and be tied to the row values. This gap sits in the fixed prover's JSON operator set — JSONSerializer can carry scan/filter/project/join/correlate/group/union(+distinct)/intersect/except/sort and nothing that serializes a subset-key dedup — rather than in the Java builder layer, so extend_dsl_file cannot bridge it, and any "encoding" that avoids the operator (e.g. set ops on the same bag) would only re-prove a tautology without exercising the key→all-columns functional dependency the rule actually rests on.

### `ConvertZipArraysToValues` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/project_set.opt

ConvertZipArraysToValues applies the unnest, json_array_elements and
jsonb_array_elements zip functions to array inputs, converting them into a
Values operator within an InnerJoinApply. This allows Values and decorrelation
rules to fire. It is especially useful in cases where the contents are passed
as a PREPARE parameter, such as:

SELECT * FROM xy WHERE y IN unnest($1)
or:
SELECT json_array_elements($1)

The replace pattern is equivalent to the match pattern because the
InnerJoinApply outputs every value in the array for every row in the input,
and outputs nulls to pad shorter arrays. It also supports correlation between
the array arguments and the input expression.

Extracted from `project_set.opt` (which defines multiple rules — implement specifically `ConvertZipArraysToValues`, not the other rules in that file):

```
# ConvertZipArraysToValues applies the unnest, json_array_elements and
# jsonb_array_elements zip functions to array inputs, converting them into a
# Values operator within an InnerJoinApply. This allows Values and decorrelation
# rules to fire. It is especially useful in cases where the contents are passed
# as a PREPARE parameter, such as:
#
#   SELECT * FROM xy WHERE y IN unnest($1)
# or:
#   SELECT json_array_elements($1)
#
# The replace pattern is equivalent to the match pattern because the
# InnerJoinApply outputs every value in the array for every row in the input,
# and outputs nulls to pad shorter arrays. It also supports correlation between
# the array arguments and the input expression.
[ConvertZipArraysToValues, Normalize]
(ProjectSet $input:* $zip:* & (CanConstructValuesFromZips $zip))
=>
(InnerJoinApply
    $input
    (ConstructValuesFromZips $zip)
    []
    (EmptyJoinPrivate)
)
```
- Attempts used: 21
- Last updated: 2026-09-25T06:32:25.241037+00:00
- Reason / notes: The rule's core is a set-returning/row-generating operation — `ProjectSet` expanding an array column (`unnest`/`json_array_elements`) into one row per element — plus a correlated `InnerJoinApply` whose right side is a dynamic Values whose rows are that specific left row's array elements. QED only models bag semantics with *scalar* uninterpreted functions and has no list/array type semantics (all types flatten to integers in `RelType`), no row-generating operator, and `JSONSerializer`/the Q-expression format carry no construct for a Values whose contents are derived from a correlated left row (the DSL's `Correlate` only filters a *fixed* right relation). This is a fundamental QED limitation (unmodelable list/row-generation semantics, and the Rust prover can't be modified), not a missing builder `extend_dsl_file` could close, so no non-vacuous special case is provable.

### `DecorrelateProjectSet` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

DecorrelateProjectSet pulls an input relation outside of a ProjectSet if the
input is not correlated with any of the functions in the ProjectSet. The
input is then cross-joined with a new ProjectSet, which contains the same
functions but has an empty input (a unary VALUES node).

The advantage of this transformation is it means each of the functions in the
ProjectSet only need to be executed once in total, instead of once for each
input row.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `DecorrelateProjectSet`, not the other rules in that file):

```
# DecorrelateProjectSet pulls an input relation outside of a ProjectSet if the
# input is not correlated with any of the functions in the ProjectSet. The
# input is then cross-joined with a new ProjectSet, which contains the same
# functions but has an empty input (a unary VALUES node).
#
# The advantage of this transformation is it means each of the functions in the
# ProjectSet only need to be executed once in total, instead of once for each
# input row.
[DecorrelateProjectSet, Normalize]
(ProjectSet
    $input:^(Values)
    $zip:* & ^(IsZipCorrelated $zip (OutputCols $input))
)
=>
(InnerJoin
    $input
    (ProjectSet (ConstructNoColsRow) $zip)
    []
    (EmptyJoinPrivate)
)
```
- Attempts used: 47
- Last updated: 2026-09-25T08:20:35.361914+00:00
- Reason / notes: ProjectSet / set-returning (table-valued) functions have [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]

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
- Attempts used: 8
- Last updated: 2026-09-25T07:07:51.954360+00:00
- Reason / notes: The rule requires the numeric negation algebraic law `neg(neg(x)) = x`, but RuleScript expresses that scalar operator as an uninterpreted projection symbol and QED has no involution axiom for uninterpreted functions. The porter’s encoding is therefore faithful, and the failure is a fundamental limitation rather than a symbol-sharing or modeling bug. Asserting the law as a table “guaranteed” constraint would circularly assume the conclusion, and using boolean `Not` would prove a different rule.

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
- Attempts used: 48
- Last updated: 2026-09-25T07:39:47.981126+00:00
- Reason / notes: EliminateWindow's correctness rests entirely on the Window operator's internal semantics — that a Window with an empty function list is a row-preserving identity — but QED's bag-semiring prover has no Window operator at all, since Window requires list/ordering semantics the bag model cannot express (the RuleScript paper explicitly excludes Sort/Window as out of scope). Because the trusted Rust prover is unmodifiable, this gap can't be bridged by extending the Java DSL: an empty Window would remain an opaque symbol QED cannot reduce to its input, and any core-operator stand-in (identity Project, redundant group-by) would prove a *different* operator's theorem rather than this one, so no faithful encoding of `Window($input []) => $input` exists. ```

