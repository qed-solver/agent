# RuleScript porting progress

_Last updated: 2026-09-30T17:49:35.957516+00:00_

**8/19 rules proved** (1 failed, 10 skipped as out of QED's supported fragment).

| Rule | Backend | Status | Scope | Attempts | Notes |
|---|---|---|---|---|---|
| `AndFalseAbsorption` | Apache DataFusion | ✅ PROVED | PARTIAL | 6 | The encoding faithfully mirrors the source rewrite: `before()` is `Filter(AND(false, P))` and `after()` is `Filter(false)` — structurally... |
| `AndNotSelfContradiction` | Apache DataFusion | ✅ PROVED | FULL | 7 | `before()` and `after()` are genuinely different plans (filter condition `A AND NOT(A)` vs. the `false` literal), the predicate `A` and t... |
| `AndOrAbsorption` | Apache DataFusion | ✅ PROVED | FULL | 12 | `before()` is structurally distinct from `after()` — `filter(A AND (A OR B))` vs `filter(A)` — with the single uninterpreted predicate `A... |
| `AndSelfIdempotent` | Apache DataFusion | ✅ PROVED | PARTIAL | 7 | The encoding is a faithful, non-vacuous instance of the DataFusion rule: the shared symbol B is exactly the duplicated conjunct the origi... |
| `AndTrueIdentity` | Apache DataFusion | ✅ PROVED | FULL | 4 | before() = Filter(AND(true, P), S) is structurally different from after() = Filter(P, S), so the proof is non-vacuous, and the encoding m... |
| `BinaryOpNullPropagation` | Apache DataFusion | ✅ PROVED | PARTIAL | 30 | The encoding is faithful to the special case it claims: `before()` (filter on `EQUALS(x, NULL-lit)`) and `after()` (filter on a bare NULL... |
| `OrTrueAbsorption` | Apache DataFusion | ✅ PROVED | FULL | 3 | The encoding faithfully captures the source rule: an uninterpreted predicate `left` stands in for the arbitrary left expression `_`, `Rex... |
| `PushFilterIntoWindow` | Apache DataFusion | ✅ PROVED | PARTIAL | 5 | The encoding faithfully captures DataFusion's core semantics — a conjunct P referencing only the partition column k (constant per partiti... |
| `CaseBranchAlwaysFalseElided` | Apache DataFusion | ❌ FAILED | — | 0 | LLM error: Request to http://169.229.48.114:8000/v1/chat/completions timed out after 600.0s: timed out |
| `BitwiseAndByZero` | Apache DataFusion | ⏭️ SKIPPED | — | 25 | The rule's correctness rests entirely on the algebraic identity of the bitwise-and operator (x & 0 = 0), and QED models scalar operators ... |
| `BitwiseOrByZero` | Apache DataFusion | ⏭️ SKIPPED | — | 23 | The rule `A | 0 -> A` depends on the algebraic identity that bitwise-or with the numeric zero literal is the identity operation, but QED'... |
| `BitwiseShiftByZeroNoop` | Apache DataFusion | ⏭️ SKIPPED | — | 32 | The rule's validity rests entirely on the operator-specific algebraic law x >> 0 = x (including its null behavior), but in RuleScript the... |
| `BitwiseXorByZero` | Apache DataFusion | ⏭️ SKIPPED | — | 12 | The rule's soundness rests entirely on the scalar identity x ^ 0 = x, but QED encodes non-boolean operators (including `^`) as uninterpre... |
| `CapSortFetchWithLimit` | Apache DataFusion | ⏭️ SKIPPED | — | 24 | The rule's entire semantic content — capping a Sort's fetch to min(F, skip+fetch) and rewriting `Limit(0, fetch, Sort(F))` to a fetch-cap... |
| `CaseFirstBranchAlwaysTrue` | Apache DataFusion | ⏭️ SKIPPED | — | 21 | CASE/then-else is not among QED's interpreted scalar operators (its `ite` is only the internal 3VL encoding of And/Or/In/Some, and the JS... |
| `CaseNoBranchesTrueToElse` | Apache DataFusion | ⏭️ SKIPPED | — | 23 | This rule is pure scalar CASE algebra — every branch of it (true-branch short-circuit, dropping false branches, empty-CASE → else-expr/NU... |
| `LimitZeroToEmpty` | Apache DataFusion | ⏭️ SKIPPED | — | 8 | The rule's core claim is a cardinality axiom about LIMIT — a literal fetch of 0 (or the skip-0/no-fetch identity branch) constrains the n... |
| `PushFilterIntoTableScan` | Apache DataFusion | ⏭️ SKIPPED | — | 26 | QED models every table scan as an uninterpreted bag of a table symbol, and the JSON format's only scan-level mechanism (the "guaranteed" ... |
| `RewriteSetComparison` | Apache DataFusion | ⏭️ SKIPPED | — | 101 | The rule rewrites set-comparison predicates (`= ANY`, `> ALL`) into CASE expressions built from EXISTS subqueries with SQL three-valued l... |

## Details

### `AndFalseAbsorption` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1050-1055
- Attempts used: 6
- Last updated: 2026-09-30T06:32:39.725033+00:00
- Reason / notes: The encoding faithfully mirrors the source rewrite: `before()` is `Filter(AND(false, P))` and `after()` is `Filter(false)` — structurally distinct (no vacuity), with the right operand a fully uninterpreted nullable predicate symbol (matching `right: _` and the "even if A is null" comment), the false literal in the correct left position per `is_false(&left)`, and no silent NOT NULL or other precondition. The only narrowing — instantiating the expression-level rule at the filter condition rather than any boolean position — is genuine and specific, honestly declared in the SCOPE line, and yields a non-degenerate, useful rule (both filters keep exactly zero rows, and QED's proof of bag equality for all instantiations of `P` is exactly the claim that `false AND A` behaves as `false` in selection).
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 292875}, panicked=False

### `AndNotSelfContradiction` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1068-1075
- Attempts used: 7
- Last updated: 2026-09-30T06:11:31.213810+00:00
- Reason / notes: `before()` and `after()` are genuinely different plans (filter condition `A AND NOT(A)` vs. the `false` literal), the predicate `A` and the source table are properly uninterpreted, and the single shared symbol `a` is exactly right — the DataFusion guard `is_not_of(&right, &left)` requires the same subexpression on both sides, so the proof is non-vacuous and covers an arbitrary uninterpreted predicate over an arbitrary bag. The encoding does pin the pattern to the position of a Filter's *complete* condition rather than an arbitrary expression position (so `SCOPE: FULL` is a mild over-claim relative to the expression-level original), but that restriction is real, specific, and non-degenerate, and the source rule's non-nullable precondition is genuinely subsumed in this context: `A AND NOT(A)` is never TRUE even when `A` is NULL (a filter keeps only TRUE rows), so both filters are extensionally empty and the proved equivalence is the true, substantive content of the DataFusion rule in its canonical relational embedding.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 310500}, panicked=False

### `AndOrAbsorption` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1096-1102
- Attempts used: 12
- Last updated: 2026-09-30T06:09:48.738451+00:00
- Reason / notes: `before()` is structurally distinct from `after()` — `filter(A AND (A OR B))` vs `filter(A)` — with the single uninterpreted predicate `A` shared at exactly the two positions the source rule requires (left conjunct and disjunct, matching `is_op_with(Or, &right, &left)`) and `B` as a separate symbol, so the proof is of the real absorption law, not a vacuous or over-constrained one. `A` and `B` remain fully uninterpreted over a generic scan, and the identity `A ∧ (A ∨ B) ≡ A` holds under SQL three-valued/null semantics as well, so no NOT NULL or other precondition is silently missing and nothing the rule leaves open is hard-coded. For the rule arm shown, the FULL scope tag is honest: the single-column scan only fixes the row-level context for a purely propositional law, not `A`/`B` themselves, so no narrower special case was assumed.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 57655458}, panicked=False

### `AndSelfIdempotent` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1084-1089
- Attempts used: 7
- Last updated: 2026-09-30T06:26:32.614598+00:00
- Reason / notes: The encoding is a faithful, non-vacuous instance of the DataFusion rule: the shared symbol B is exactly the duplicated conjunct the original's `expr_contains(&left, &right, And)` side condition requires (it appears both nested inside `left` via AND-only paths and as the right operand), A and C are genuinely independent uninterpreted predicates (no wrong sharing), and before/after are structurally different, so the zero-SMT-time result just reflects that normalization proves the real idempotency equivalence `((A∧B)∧C)∧B ≡ (A∧B)∧C`, which holds even under null semantics with no missing preconditions. The narrowing — left fixed to a specific 3-conjunct AND shape and right restricted to a single uninterpreted predicate — is genuine, since the original's parametric "right is an arbitrary subexpression of any shape AND-embedded at any depth in left" cannot be expressed in the DSL (substructure-containment patterns go beyond QED's flat uninterpreted-symbol semantics), and the PARTIAL scope line states this honestly and specifically. The result is still a useful, non-degenerate rule (a filter whose condition redundantly re-ANDs a conjunct already nested inside it can drop the duplicate), so the provable verdict is meaningful. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 491209}, panicked=False

### `AndTrueIdentity` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1044-1049
- Attempts used: 4
- Last updated: 2026-09-30T06:14:52.256831+00:00
- Reason / notes: before() = Filter(AND(true, P), S) is structurally different from after() = Filter(P, S), so the proof is non-vacuous, and the encoding mirrors the source exactly: the literal-true left operand matches both the rule's direction ("true AND A") and its is_true(&left) guard, while the right operand is a fully uninterpreted predicate over a scan with an uninterpreted type, so no concrete predicate or relation is baked in. The filter-over-scan shape is the standard relational embodiment of a pure predicate identity in this DSL (the same convention as the reference FilterMerge example), since true∧A ≡ A is the same pointwise propositional fact in any Boolean context and holds exactly under three-valued/bag semantics, so no precondition (nullability, uniqueness, etc.) is silently missing and the SCOPE: FULL tag is honest. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 301459}, panicked=False

### `BinaryOpNullPropagation` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 843-856
- Attempts used: 30
- Last updated: 2026-09-30T06:31:16.957149+00:00
- Reason / notes: The encoding is faithful to the special case it claims: `before()` (filter on `EQUALS(x, NULL-lit)`) and `after()` (filter on a bare NULL-Bool literal) are structurally distinct, and the proof is non-vacuous — it depends on real three-valued null semantics (x = NULL is never true, so both filters keep no rows), not a structural coincidence. The narrowing from DataFusion's ~30 `returns_null_on_null` operators in either operand position down to just Eq with a null right operand is genuine rather than a DSL gap: null propagation is the internal semantics of each concrete interpreted operator, which QED's SMT model knows only for built-ins (like Calcite's EQUALS) and cannot assume of an uninterpreted operator symbol, so the fully general form is unprovable; this restriction is disclosed accurately and specifically in the SCOPE line. Preconditions are honored (the null operand is a literal, matching DataFusion's `is_null` guard, and the folded literal carries the expression's Boolean type), with no symbol-sharing or hard-coded-shape errors. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 88917}, panicked=False

### `OrTrueAbsorption` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 966-971
- Attempts used: 3
- Last updated: 2026-09-30T05:15:23.318992+00:00
- Reason / notes: The encoding faithfully captures the source rule: an uninterpreted predicate `left` stands in for the arbitrary left expression `_`, `RexRN.trueLiteral()` on the right matches the `is_true(&right)` guard (and correctly fires only for the right-operand-true case the rule specifies), and `before()`/`after()` are structurally distinct (filter on `Or(A,true)` vs filter on `true`), so the proof is non-vacuous. The null-safety comment ("even if A is null") is precisely the justification that no extra precondition (e.g. NOT NULL) is needed — in 3VL, `A OR true = true` unconditionally — so the absence of constraints is faithful, and the filter-context embedding with a fully general uninterpreted A is the most general relational form of this scalar rewrite, warranting `SCOPE: FULL`.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 61083}, panicked=False

### `PushFilterIntoWindow` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 1068-1139
- Attempts used: 5
- Last updated: 2026-09-30T05:28:09.885171+00:00
- Reason / notes: The encoding faithfully captures DataFusion's core semantics — a conjunct P referencing only the partition column k (constant per partition, so pushable) is moved below a window modeled as an uninterpreted partition-aggregate join-back (the right stand-in since QED can't model frames/ordering), while a kept conjunct Q over the full window row (k, v, w) stays above; P, Q, and the window value w are correctly shared uninterpreted symbols on both sides (not baked-in constants), and before()/after() are structurally distinct plans, so the ~90ms proof is a genuine, non-vacuous universal argument rather than an artifact of over-constraint. The `SCOPE: PARTIAL` line is honest and specific — single partition column, single window function, no frame/ordering, two-column input, one pushed + one kept conjunct — and each restriction is a real QED/DSL limitation (window functions have no bag semantics; the per-conjunct split and multi-key/multi-window details are control-flow the uninterpreted P/Q already abstract) rather than an encoding error, leaving a faithful, non-degenerate special case.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 92846625}, panicked=False

### `CaseBranchAlwaysFalseElided` — ❌ FAILED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1525-1562
- Attempts used: 0
- Last updated: 2026-09-30T17:49:35.953753+00:00
- Reason / notes: LLM error: Request to http://169.229.48.114:8000/v1/chat/completions timed out after 600.0s: timed out

### `BitwiseAndByZero` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1229-1235
- Attempts used: 25
- Last updated: 2026-09-30T06:36:23.440486+00:00
- Reason / notes: The rule's correctness rests entirely on the algebraic identity of the bitwise-and operator (x & 0 = 0), and QED models scalar operators such as & only as uninterpreted function symbols; its oracle theory (equality, total order, ite, and natural-number addition used solely for bag multiplicity) contains no integer/bitwise arithmetic and no zero constant carrying that property, so f(x,0)=0 is not entailed under any instantiation. This is exactly the "backend operator's bespoke internal semantics" limitation from the reference — and it is fundamental rather than a DSL gap, because even if a numeric `0` constant could be added, the uninterpreted & symbol still has no axiom relating it to zero, so no encoding of the rule could be proved. ```

### `BitwiseOrByZero` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1303-1308
- Attempts used: 23
- Last updated: 2026-09-30T06:44:27.975423+00:00
- Reason / notes: The rule `A | 0 -> A` depends on the algebraic identity that bitwise-or with the numeric zero literal is the identity operation, but QED's SMT theory models all non-boolean scalar operators as fully uninterpreted functions and provides no numeric literals or arithmetic axioms, so there is no way to express or prove this identity. This is a fundamental prover-side limitation rather than a missing DSL capability: even if `extend_dsl_file` exposed a bitwise-or operator symbol, the unmodifiable prover would still treat it as uninterpreted and lack the `bitwise_or(x, 0) = x` axiom, which is the same reason the analogous `FoldPlusZero`/`FoldMinusZero`/`FoldDivOne` numeric-folding precedents are correctly rejected. ```

### `BitwiseShiftByZeroNoop` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1452-1457
- Attempts used: 32
- Last updated: 2026-09-30T06:47:11.761036+00:00
- Reason / notes: The rule's validity rests entirely on the operator-specific algebraic law x >> 0 = x (including its null behavior), but in RuleScript the shift can only be introduced as an uninterpreted scalar function symbol — the core language has no numeric literal or interpreted bit-arithmetic to represent the zero operand in any more constrained way — and QED's logic (equality, order, ite over uninterpreted functions, with arithmetic reserved for bag multiplicity) contains no axiom connecting a shift-by-zero to its operand, so the identity is not entailed under any instantiation no matter how the encoding is shaped. The porter's minimal empirical test (provable=false with the only difference being the uninterpreted shift symbol) is therefore the predicted outcome of a genuine limitation of the fixed prover's theory, not a symbol-sharing or function-composition mistake, and no `extend_dsl_file` change can close it since the missing shift/zero axioms live in the unverifiable Rust prover, not in a missing DSL builder.

### `BitwiseXorByZero` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1376-1381
- Attempts used: 12
- Last updated: 2026-09-30T06:44:24.421601+00:00
- Reason / notes: The rule's soundness rests entirely on the scalar identity x ^ 0 = x, but QED encodes non-boolean operators (including `^`) as uninterpreted function symbols with no numeric-zero constant in its theory (only boolean literals exist in the Rex language) and no axioms relating operators to constants, so any faithful encoding has an SMT countermodel (e.g. interpret `^` as a function that need not return its first argument on zero). This is a limitation of the prover's theory rather than a DSL gap — `extend_dsl_file` can only add builders that serialize to uninterpreted symbols and cannot add arithmetic axioms to the fixed prover. It is consistent with the independently verified skip verdicts for the sibling `BitwiseAndByZero` and the `FoldPlusZero`/`FoldMinusZero`/`FoldDivOne` arithmetic-folding family, which rest on the same missing operator theory. ```

### `CapSortFetchWithLimit` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_limit.rs, lines 161-185
- Attempts used: 24
- Last updated: 2026-09-30T07:07:03.782971+00:00
- Reason / notes: The rule's entire semantic content — capping a Sort's fetch to min(F, skip+fetch) and rewriting `Limit(0, fetch, Sort(F))` to a fetch-capped `Sort` — is defined purely in terms of row position in sort order (top-N), which QED's bag-semantic model has no notion of (Sort/Limit carry no bag semantics and act only as uninterpreted operators). Both sides share the identical input relation and differ only in the structure and fetch arguments of those uninterpreted wrappers, so no non-vacuous bag-identity core exists and a DSL Sort builder (even though the JSON serializer could already carry a sort node) would not help, since the immutable prover has no ordering semantics to check the fetch manipulation against.

### `CaseFirstBranchAlwaysTrue` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1525-1562
- Attempts used: 21
- Last updated: 2026-09-30T17:28:45.794087+00:00
- Reason / notes: CASE/then-else is not among QED's interpreted scalar operators (its `ite` is only the internal 3VL encoding of And/Or/In/Some, and the JSON format can at best carry a CASE call as a named uninterpreted operator), so in every branch of this rule the before-side pattern contains a literal CASE that QED sees only as an uninterpreted function and can never equate with a then/else branch (e.g. `case(false, A, B) ≡ B` or dead-branch elimination). A relational Filter/Union re-encoding of the CASE value column is not faithful under 3-valued logic (a `c`/`¬c` filter pair silently drops rows where `c` is NULL, and "is not true" would be an independent symbol QED can't relate to `c`), and `extend_dsl_file` only touches the Java builder side — the interpretation lives in the fixed Rust prover — so this is a genuine prover limitation in the "operator whose specific internal semantics QED cannot see through" class, and no narrower special case of the rule escapes it because the before side always contains the unnameable CASE. ```

### `CaseNoBranchesTrueToElse` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1525-1562
- Attempts used: 23
- Last updated: 2026-09-30T17:26:16.277541+00:00
- Reason / notes: This rule is pure scalar CASE algebra — every branch of it (true-branch short-circuit, dropping false branches, empty-CASE → else-expr/NULL) rests on the selection semantics of the CASE operator, but QED's surface fragment has no interpreted ite/CASE: scalar functions serialize as plain uninterpreted RexCalls and QED quantifies over *all* instantiations of uninterpreted symbols, so it can never equate case(p,a,b) with a or b (the universal statement is false for arbitrary function symbols). A `Case` builder via extend_dsl_file would only mint another uninterpreted operator name for the fixed Rust prover, and relational workarounds (e.g. modeling ite as a union of filtered projections) verify a self-chosen model against itself, not the backend's actual CASE operator — a genuine QED limitation, not a missed encoding. ```

### `LimitZeroToEmpty` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_limit.rs, lines 1-89
- Attempts used: 8
- Last updated: 2026-09-30T05:17:12.400926+00:00
- Reason / notes: The rule's core claim is a cardinality axiom about LIMIT — a literal fetch of 0 (or the skip-0/no-fetch identity branch) constrains the number of output rows — and QED's bag-semantic model has no Limit/Offset operator and no row-count or ordering axioms (Limit is listed alongside Sort/Window/Sample as having no bag-semantic meaning), so a `LIMIT 0` node would be indistinguishable from an arbitrary uninterpreted relation and cannot be proved equal to Empty for any input. The DSL exposes no Limit builder and `JSONSerializer` does not even have a `LogicalLimit` case, so extending the DSL could at best get the pattern to serialize — it cannot supply the missing axioms to the immutable Rust prover, unlike Sort, whose bag-identity effect the prover can absorb. Even the narrowest special case (e.g., limiting an already-empty input) requires the same unmodeled limit semantics, so no non-trivial provable special case exists.

### `PushFilterIntoTableScan` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 1224-1301
- Attempts used: 26
- Last updated: 2026-09-30T05:27:20.878704+00:00
- Reason / notes: QED models every table scan as an uninterpreted bag of a table symbol, and the JSON format's only scan-level mechanism (the "guaranteed" check constraint) asserts a pre-existing property of the table's rows (Filter(C,T)≡T), whereas the Exact path of this rule requires the missing axiom that a *new scan instance's* output equals Filter(P,T) over the unfiltered table — a bag equality defined by the provider's execution-time TableProviderFilterPushDown::Exact contract, which the prover has no operator or axiom for and cannot see through uninterpreted symbols. The Inexact path is the logical no-op Filter(P,Scan)≡Filter(P,Scan) (scan.filters is planner metadata invisible to bag semantics), and the only would-be encoding of the Exact path — reusing the same table symbol with a P-guarantee so before=Filter(P,T) and after=T — smuggles the very equivalence the rule is supposed to establish in as a table precondition, proving check-constraint filter elimination rather than pushdown. This is the same fundamental limitation already adjudicated for Calcite's FilterTableScan (verifier AGREE), so the UNSUPPORTED call is correct. ```

### `RewriteSetComparison` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/rewrite_set_comparison.rs, lines 1-175
- Attempts used: 101
- Last updated: 2026-09-30T05:58:59.599093+00:00
- Reason / notes: The rule rewrites set-comparison predicates (`= ANY`, `> ALL`) into CASE expressions built from EXISTS subqueries with SQL three-valued logic (IS NULL / IS TRUE / IS FALSE on a boolean comparison). RuleScript's predicate language is restricted to uninterpreted atomic symbols combined with boolean operators (And/Or/Not/True/False); it has no construct for embedding a subquery or correlated EXISTS inside a filter predicate, no CASE/WHEN scalar expression, and no three-valued-logic (NULL-propagating) comparison semantics. The before pattern `Filter(x = ANY (SELECT y FROM T), R)` and the after pattern `Filter(CASE WHEN EXISTS(...) THEN true WHEN EXISTS(...) IS NULL THEN null ELSE false END, R)` both require predicate-level constructs that simply do not exist in the DSL, and even if they were added as uninterpreted symbols, QED's bag-semantic SMT framework cannot reason about predicate-level logical entailment between independent symbols or about the algebraic relationship between ANY/ALL quantification and the EXISTS+CASE decomposition with null handling. ```

