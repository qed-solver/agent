# RuleScript porting progress

_Last updated: 2026-09-26T07:11:56.309924+00:00_

**69/113 rules proved** (0 failed, 44 skipped as out of QED's supported fragment).

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
| `EliminateDistinctSetLeft` | CockroachDB | ✅ PROVED | PARTIAL | 47 | The encoding matches the rule's real shape: before() is a distinct set operator (`union(false, …)`, i.e. CRDB's non-All `Union`) whose ri... |
| `EliminateDistinctSetRight` | CockroachDB | ✅ PROVED | PARTIAL | 5 | The encoding faithfully captures EliminateDistinctSetRight's semantics: a distinct `Union` (correctly `union(false, ...)`, since the sour... |
| `EliminateExistsGroupBy` | CockroachDB | ✅ PROVED | PARTIAL | 43 | The encoding is faithful and non-vacuous: before() places a non-scalar Aggregate (one uninterpreted group key, no aggregate calls) under ... |
| `EliminateExistsProject` | CockroachDB | ✅ PROVED | FULL | 44 | Manually investigated by Claude (the automated attempt found a real proof via a DSL extension, but it broke 25 already-proved rules on me... |
| `EliminateExistsZeroRows` | CockroachDB | ✅ PROVED | PARTIAL | 92 | Manually investigated by Claude (the automated run exhausted all 5 rounds on repeated context-length crashes, never reaching a real try_r... |
| `EliminateGroupByProject` | CockroachDB | ✅ PROVED | PARTIAL | 24 | `before()` (Aggregate over the column-dropping Project) and `after()` (Aggregate directly over the base) are structurally distinct, so th... |
| `EliminateJoinNoColsLeft` | CockroachDB | ✅ PROVED | PARTIAL | 83 | `before()` (an INNER join with a one-row, zero-column left) and `after()` (a plain filter on the right) are genuinely different shapes an... |
| `EliminateJoinNoColsRight` | CockroachDB | ✅ PROVED | PARTIAL | 49 | before() (INNER join whose right input is a one-row, zero-column Values) and after() (Filter over the left) are genuinely distinct shapes... |
| `EliminateJoinUnderGroupByLeft` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding is genuinely non-vacuous (before() contains the LEFT join plus the right scan; after() drops both) and the symbol sharing is... |
| `EliminateJoinUnderGroupByRight` | CockroachDB | ✅ PROVED | PARTIAL | 63 | The encoding is a non-vacuous, sound special case whose PARTIAL scope is honestly and specifically declared: before() genuinely contains ... |
| `EliminateJoinUnderProjectLeft` | CockroachDB | ✅ PROVED | PARTIAL | 32 | before() (a LEFT self-join of a unique, non-nullable single-column scan on `col = col`, projecting only field 0) is structurally distinct... |
| `EliminateJoinUnderProjectRight` | CockroachDB | ✅ PROVED | PARTIAL | 4 | The proof is non-vacuous — before() genuinely contains an inner self-join that after() removes, and equivalence depends essentially on th... |
| `EliminateNot` | CockroachDB | ✅ PROVED | FULL | 5 | The encoding directly captures `EliminateNot` by removing a doubled `NOT` around the same uninterpreted predicate while keeping the same ... |
| `EliminateProject` | CockroachDB | ✅ PROVED | PARTIAL | 5 | The encoding faithfully captures both preconditions of the source rule — an empty projections list and a passthrough set exactly equal to... |
| `EliminateRedundantBarrier` | CockroachDB | ✅ PROVED | PARTIAL | 21 | The encoding faithfully models a Barrier as an identity projection (project(field(0)) over a single-column scan), which correctly capture... |
| `EliminateSelect` | CockroachDB | ✅ PROVED | FULL | 4 | The encoding is faithful: `before()` is a Filter with a literal-True condition over a scan and `after()` is the same bare scan, so the tw... |
| `EliminateSemiJoin` | CockroachDB | ✅ PROVED | PARTIAL | 24 | The encoding is a genuine, non-vacuous instance of EliminateSemiJoin — the SEMI join kind and the `=> $left` conclusion match the source ... |
| `EliminateSetLeft` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding is faithful to the UnionAll arm: before() is π(Left) UNION ALL Empty while after() is π(Left) — structurally distinct, so th... |
| `EliminateSetRight` | CockroachDB | ✅ PROVED | PARTIAL | 7 | The encoding faithfully captures the source rule's semantics: `before()` is a UNION ALL of a structurally-empty left (faithful encoding o... |
| `EliminateUpsertDistinct` | CockroachDB | ✅ PROVED | PARTIAL | 24 | The encoding is non-trivial (before is an Aggregate/GROUP BY, after is a plain Project) and correctly proves that a GROUP BY on a unique ... |
| `EliminateZeroCardProject` | CockroachDB | ✅ PROVED | PARTIAL | 21 | The encoding correctly models the original rule's core semantic claim — a Project over a zero-cardinality input yields an empty relation ... |
| `EliminateZeroCardSelect` | CockroachDB | ✅ PROVED | PARTIAL | 3 | The encoding is faithful and non-degenerate: `before()` (two nested uninterpreted filters over a structurally-empty `.empty()` relation) ... |
| `ExtractJoinComparisons` | CockroachDB | ✅ PROVED | PARTIAL | 28 | before() = L ⋈_{cmp(f(L),g(R))} R and after() = push f,g into per-side Projections, join on the synthesized columns, then drop them, are ... |
| `ExtractRedundantConjunct` | CockroachDB | ✅ PROVED | PARTIAL | 21 | The encoding is exactly the source rule's documented second example, (A AND B) OR (A AND C) ⟹ A AND (B OR C), lifted to a filter over a s... |
| `FoldBinary` | CockroachDB | ✅ PROVED | PARTIAL | 21 | The encoding is a genuine, non-vacuous special case whose before/after shapes exactly match one concrete firing of the original FoldBinar... |
| `FoldColumnAccess` | CockroachDB | ✅ PROVED | PARTIAL | 43 | before() nests a single-column field access on top of a two-element tuple construction while after() drops the intermediate construction,... |
| `FoldComparison` | CockroachDB | ✅ PROVED | PARTIAL | 7 | The encoding is a faithful, honestly-labeled (PARTIAL) special case of FoldComparison: it picks the single concrete instance `TRUE = FALS... |
| `FoldComparisonWithAny` | CockroachDB | ✅ PROVED | PARTIAL | 41 | The proof is not vacuous: before() filters by OR(EQ(TRUE,FALSE), EQ(TRUE,TRUE)) while after() filters by the TRUE literal, so QED genuine... |
| `FoldEqFalse` | CockroachDB | ✅ PROVED | FULL | 8 | The encoding faithfully mirrors `FoldEqFalse`: `before()` is `Filter(left = False, Source)` and `after()` is `Filter(NOT left, Source)`, ... |
| `FoldEqTrue` | CockroachDB | ✅ PROVED | FULL | 22 | The encoding is non-vacuous (before is `Filter(EQUALS(left, true), Source)` vs after `Filter(left, Source)` — structurally distinct, forc... |
| `FoldGroupByAndWindow` | CockroachDB | ✅ PROVED | PARTIAL | 61 | The encoding faithfully captures the core logic of FoldGroupByAndWindow for case 5a: it models the Window as a per-partition aggregate jo... |
| `FoldGroupingOperators` | CockroachDB | ✅ PROVED | PARTIAL | 27 | The encoding is a faithful, non-degenerate special case: before() is a genuine nested Aggregate(Aggregate) that folds to a single Aggrega... |
| `FoldIndirection` | CockroachDB | ✅ PROVED | PARTIAL | 6 | The proof is non-vacuous — before() is a nested two-column projection plus a field access while after() is a single direct projection, so... |
| `FoldIsNotNull` | CockroachDB | ✅ PROVED | FULL | 29 | The encoding is faithful and non-vacuous: `before()` filters on `NULL IS NOT NULL` (a genuine null literal under the real `IS_NOT_NULL` o... |
| `FoldIsNull` | CockroachDB | ✅ PROVED | FULL | 21 | The source rule is the scalar constant-fold `NULL IS NULL => True`, a context-free boolean identity, and the porter embedded it in the ca... |
| `FoldIsNullProject` | CockroachDB | ✅ PROVED | PARTIAL | 28 | The encoding faithfully captures the rule's core — a Project item `x IS NULL` over an input column x guaranteed NOT NULL (encoded via C_T... |
| `FoldNeFalse` | CockroachDB | ✅ PROVED | FULL | 5 | The encoding is faithful: `before()` filters on the concrete `NOT_EQUALS(left, false)` while `after()` filters on `left` alone, so the st... |
| `FoldNeTrue` | CockroachDB | ✅ PROVED | FULL | 3 | The encoding faithfully lifts the scalar rule `(Ne $left:* (True)) => (Not $left)` into `Filter(scan, NOT_EQUALS(left, TRUE)) => Filter(s... |
| `FoldNonNullIsNotNull` | CockroachDB | ✅ PROVED | FULL | 27 | The encoding is a faithful relational lifting of the scalar rule `IsNot $left (Null) => True` under `IsNeverNull $left`: `before()` is `F... |
| `FoldNonNullIsNull` | CockroachDB | ✅ PROVED | FULL | 25 | `before()` (filter on `x IS NULL`) and `after()` (filter on the `False` literal) are structurally different, and the equivalence can hold... |
| `FoldNonNullTupleIsTupleNotNull` | CockroachDB | ✅ PROVED | PARTIAL | 45 | The encoding is non-vacuous — `before()` filters on `(f0 IS NOT NULL) AND (f1 IS NOT NULL)` while `after()` filters on `true`, and the pr... |
| `FoldNonNullTupleIsTupleNull` | CockroachDB | ✅ PROVED | PARTIAL | 3 | The encoding correctly relationalizes the scalar fold: before() is Filter(AND(e0 IS NULL, e1 IS NULL), source) — the exact semantics of I... |
| `FoldNotFalse` | CockroachDB | ✅ PROVED | FULL | 4 | The encoding is non-vacuous (Filter(Not(False), R) vs. Filter(True, R) are structurally distinct) and matches the source rule term-for-te... |
| `FoldNotInEmpty` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding is non-vacuous — before() is an ANTI join of L against a genuine zero-row relation and after() is the bare scan L — and the ... |
| `FoldNotNull` | CockroachDB | ✅ PROVED | FULL | 10 | The encoding faithfully captures CockroachDB's FoldNotNull rule (`(Not (Null)) => (Null (BoolType))`) by embedding the expression-level b... |
| `FoldNotTrue` | CockroachDB | ✅ PROVED | FULL | 3 | before() (Filter with condition NOT(TRUE)) and after() (Filter with condition FALSE) are structurally distinct, and the transformation is... |
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
| `EliminateCaseTrailingFalsyBranch` | CockroachDB | ⏭️ SKIPPED | — | 65 | The rule's soundness rests entirely on CASE's own 3-valued if-then-else semantics (a trailing WHEN with a False/Null THEN is dead only be... |
| `EliminateCast` | CockroachDB | ⏭️ SKIPPED | — | 24 | EliminateCast's soundness rests entirely on the cast operator's own value semantics — that a cast is value-identical to its input when th... |
| `EliminateCoalesce` | CockroachDB | ⏭️ SKIPPED | — | 128 | Manually finalized by Claude after reviewing the automated run's own verifier rejection (the porter's last candidate called a nonexistent... |
| `EliminateConstValueSubquery` | CockroachDB | ⏭️ SKIPPED | — | 24 | The rule's LHS is a scalar subquery wrapping a single-row constant VALUES, and QED's prover cannot represent or reason about a scalar ter... |
| `EliminateDistinctNoColumns` | CockroachDB | ⏭️ SKIPPED | — | 24 | The rule rewrites a no-grouping-columns DistinctOn (which emits at most one row — the input's first row under GroupingInputOrdering) into... |
| `EliminateDistinctOnValues` | CockroachDB | ⏭️ SKIPPED | — | 51 | Investigated in depth a second time at the user's request ('it should be provable and mergeable, right? but with some tweaks'). Read Cons... |
| `EliminateEnsureDistinctNoColumns` | CockroachDB | ⏭️ SKIPPED | — | 45 | Both sides of this rule are custom operators whose correctness rests on runtime error semantics — EnsureDistinctOn/EnsureUpsertDistinctOn... |
| `EliminateExistsLimit` | CockroachDB | ⏭️ SKIPPED | — | 27 | The rule's entire content is that a positive LIMIT preserves the emptiness of its input (empty stays empty, non-empty stays non-empty), w... |
| `EliminateGroupBy` | CockroachDB | ⏭️ SKIPPED | — | 30 | EliminateGroupBy's entire soundness rests on the algebraic identity "AggAnyNotNull/ConstNotNull over a ≤1-row group returns that row's va... |
| `EliminateLimit` | CockroachDB | ⏭️ SKIPPED | — | 21 | Limit is one of the operators QED explicitly does not model (Sort/Limit/Offset have no bag-semantic meaning; Limit is left uninterpreted ... |
| `EliminateMax1Row` | CockroachDB | ⏭️ SKIPPED | — | 23 | Max1Row is not a bag function — it is the identity only on inputs with at most one row and a runtime error otherwise — so it belongs to t... |
| `EliminateOffset` | CockroachDB | ⏭️ SKIPPED | — | 25 | EliminateOffset's entire content is that skipping 0 rows is the identity, which is purely a row-ordering/list-semantic fact, and QED expl... |
| `EliminateUDFCallSubquery` | CockroachDB | ⏭️ SKIPPED | — | 23 | The rule is a scalar rewrite whose LHS is a scalar subquery term (a relation embedded inside a scalar expression, `Subquery(Values(udf))`... |
| `EliminateUnaryMinus` | CockroachDB | ⏭️ SKIPPED | — | 8 | The rule requires the numeric negation algebraic law `neg(neg(x)) = x`, but RuleScript expresses that scalar operator as an uninterpreted... |
| `EliminateWindow` | CockroachDB | ⏭️ SKIPPED | — | 48 | EliminateWindow's correctness rests entirely on the Window operator's internal semantics — that a Window with an empty function list is a... |
| `FoldArray` | CockroachDB | ⏭️ SKIPPED | — | 41 | FoldArray rewrites an uninterpreted Array constructor applied to constant arguments into a concrete TArray ground constant, but QED model... |
| `FoldAssignmentCast` | CockroachDB | ⏭️ SKIPPED | — | 6 | The rule's correctness rests entirely on the assignment-cast operator's operational evaluation semantics (that casting a constant datum y... |
| `FoldCast` | CockroachDB | ⏭️ SKIPPED | — | 22 | FoldCast's RHS is a value produced by *evaluating* the cast operator on a constant input, but in RuleScript a cast can only be modeled as... |
| `FoldCollate` | CockroachDB | ⏭️ SKIPPED | — | 22 | FoldCollate's entire content is the backend-bespoke identity `Collate(c, locale) ≡ CastToCollatedString(c, locale)` on a constant input, ... |
| `FoldDivOne` | CockroachDB | ⏭️ SKIPPED | — | 25 | FoldDivOne depends on the numeric constant-folding identity `div(x, 1) = cast(x)`, including a representable literal `1` and the algebrai... |
| `FoldEqualsAnyNull` | CockroachDB | ⏭️ SKIPPED | — | 76 | Manually investigated by Claude (the automated run exhausted all 5 rounds on repeated context-length crashes, never reaching a real try_r... |
| `FoldFloorDivOne` | CockroachDB | ⏭️ SKIPPED | — | 21 | The rule's correctness rests on the numeric identity "floor division by 1 is the identity function" (plus a value-preserving cast), but R... |
| `FoldFunction` | CockroachDB | ⏭️ SKIPPED | — | 27 | FoldFunction's correctness rests on evaluating specific named functions over specific constant arguments and equating the result with the... |
| `FoldFunctionWithNullArg` | CockroachDB | ⏭️ SKIPPED | — | 61 | The rule's soundness rests on backend metadata about the function itself (CalledOnNullInput=false ⇒ a NULL argument forces a NULL result)... |
| `FoldInEmpty` | CockroachDB | ⏭️ SKIPPED | — | 49 | FoldInEmpty's soundness rests on the scalar IN operator's list-membership algebra (x ∈ [] = false), which QED does not model and RuleScri... |
| `FoldInNull` | CockroachDB | ⏭️ SKIPPED | — | 100 | Manually investigated by Claude (the automated run exhausted all 5 rounds on repeated context-length crashes, never reaching a real try_r... |
| `FoldJSONAccessIntoValues` | CockroachDB | ⏭️ SKIPPED | — | 41 | FoldJSONAccessIntoValues is a constant-folding rule whose correctness depends on evaluating a JSON field-access operator (e.g. `cust->'id... |
| `FoldLimits` | CockroachDB | ⏭️ SKIPPED | — | 22 | FoldLimits reduces to the identity Limit(Limit(R, n_in, ord_in), n_out, ord_out) ≡ Limit(R, n_out, ord_in) under n_out ≤ n_in and ord_in ... |
| `FoldMinusZero` | CockroachDB | ⏭️ SKIPPED | — | 23 | The rule's core is the numeric identity `x - 0 = cast(x)`, which needs QED to (a) name the numeric constant 0 — the DSL only exposes bool... |
| `FoldMultOne` | CockroachDB | ⏭️ SKIPPED | — | 22 | FoldMultOne depends on the numeric algebra identity `x * 1 = cast(x, T)`, but RuleScript/QED can only express `Mult` and `Cast` as uninte... |

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

### `EliminateDistinctSetLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/set.opt

EliminateDistinctSetLeft replaces a Union or Except operator with a right side
having a cardinality of zero, with a Distinct on just the left side operand.

Extracted from `set.opt` (which defines multiple rules — implement specifically `EliminateDistinctSetLeft`, not the other rules in that file):

```
# EliminateDistinctSetLeft replaces a Union or Except operator with a right side
# having a cardinality of zero, with a Distinct on just the left side operand.
[EliminateDistinctSetLeft, Normalize]
(Union | Except
    $left:*
    $right:* & (HasZeroRows $right)
    $colMap:*
)
=>
(DistinctOn
    $project:(Project
        $left
        (ProjectColMapLeft $colMap)
        (ProjectPassthroughLeft $colMap)
    )
    []
    (MakeGrouping (OutputCols $project) (EmptyOrdering))
)
```
- Attempts used: 47
- Last updated: 2026-09-25T20:13:27.231653+00:00
- Reason / notes: The encoding matches the rule's real shape: before() is a distinct set operator (`union(false, …)`, i.e. CRDB's non-All `Union`) whose right operand is the empty relation — the exact semantic content of `HasZeroRows $right`, the only expressible form of that precondition in the DSL — and after() is a group-by over all output columns with zero aggregate calls, which is precisely `DistinctOn(Project($left, $colMap), [], MakeGrouping (OutputCols …) (EmptyOrdering))`; before() and after() are structurally different plans (Union vs Aggregate), so the SMT proof is of the genuine algebraic law `DistinctUnion(X, ∅) = Distinct(X)`, not a vacuous identity. Generality is preserved where it matters: left/right are independent uninterpreted scans over uninterpreted types (mirroring the single shared $colMap, which the real rule also applies to both sides), and the fixed 3-column width / (2,0,1) permutation are DSL-inherent instantiations of $colMap that do not narrow the proven claim, since the law is uniform in the colmap. The omitted distinct-Except arm is a genuine single-record (one before/after tree) limitation, correctly and specifically disclosed in the SCOPE: PARTIAL line. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 61041500}, panicked=False

### `EliminateDistinctSetRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/set.opt

EliminateDistinctSetRight mirrors EliminateDistinctSetLeft. Note that it only
applies to Union because Except operators only output left input rows.

Extracted from `set.opt` (which defines multiple rules — implement specifically `EliminateDistinctSetRight`, not the other rules in that file):

```
# EliminateDistinctSetRight mirrors EliminateDistinctSetLeft. Note that it only
# applies to Union because Except operators only output left input rows.
[EliminateDistinctSetRight, Normalize]
(Union $left:* & (HasZeroRows $left) $right:* $colMap:*)
=>
(DistinctOn
    $project:(Project
        $right
        (ProjectColMapRight $colMap)
        (ProjectPassthroughRight $colMap)
    )
    []
    (MakeGrouping (OutputCols $project) (EmptyOrdering))
)
```
- Attempts used: 5
- Last updated: 2026-09-25T20:17:57.171689+00:00
- Reason / notes: The encoding faithfully captures EliminateDistinctSetRight's semantics: a distinct `Union` (correctly `union(false, ...)`, since the source `Union` is the dedup variant, not `UnionAll`/`Except`) whose left input has zero rows is replaced by a DistinctOn over the colmapped right input, which the porter models as a GROUP BY on all output fields with no aggregate calls — exactly the `DistinctOn(..., [], MakeGrouping(OutputCols, EmptyOrdering))` shape. `HasZeroRows $left` is encoded as a structurally empty relation of the union's row type, which is the only faithful encoding available (the DSL/JSON format has no cardinality-constraint mechanism), and this narrowing is honestly declared in the SCOPE line; the left and right remain independent table symbols, and the shared row types are *required* for a well-formed union rather than an over-constraint. `before()` (distinct union with an Empty) and `after()` (full-column distinct) are structurally different plans, so the proof is non-vacuous and establishes exactly the rule's content: dropping a provably-empty operand from a distinct union leaves the dedup of the surviving side; the non-identity projection applied identically to both operands correctly models the colmap remapping (incidental to the equivalence but present on both sides, as in the source). ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 63726334}, panicked=False

### `EliminateExistsGroupBy` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateExistsGroupBy discards a non-scalar GroupBy input to the Exists
operator. While non-scalar GroupBy (or DistinctOn) can change row cardinality,
it always returns a non-empty set if its input is non-empty. Similarly, if its
input is empty, then it returns the empty set. Therefore, it's a no-op for
Exists.

NOTE: EnsureDistinctOn has the side effect of error'ing if the input has
duplicates, so do not eliminate it.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateExistsGroupBy`, not the other rules in that file):

```
# EliminateExistsGroupBy discards a non-scalar GroupBy input to the Exists
# operator. While non-scalar GroupBy (or DistinctOn) can change row cardinality,
# it always returns a non-empty set if its input is non-empty. Similarly, if its
# input is empty, then it returns the empty set. Therefore, it's a no-op for
# Exists.
#
# NOTE: EnsureDistinctOn has the side effect of error'ing if the input has
# duplicates, so do not eliminate it.
[EliminateExistsGroupBy, Normalize]
(Exists (GroupBy | DistinctOn $input:*) $existsPrivate:*)
=>
(Exists $input $existsPrivate)
```
- Attempts used: 43
- Last updated: 2026-09-25T22:51:32.180734+00:00
- Reason / notes: The encoding is faithful and non-vacuous: before() places a non-scalar Aggregate (one uninterpreted group key, no aggregate calls) under a constant-true SEMI join — the exact plan-level semantics of an uncorrelated EXISTS — and after() drops it, so the proof genuinely requires both directions of the rule's core fact that group-by preserves empty/non-emptiness (it would fail if QED's group-by semantics produced a row for an empty input). The rule's key precondition is respected (non-scalar group-by, i.e. a non-empty group set — a scalar group-by would break the rule, and the porter correctly avoided that), the outer and subquery-input scans are properly independent uninterpreted symbols, and the omitted EnsureDistinctOn caveat is sidestepped by not encoding DistinctOn. The PARTIAL scope line is honest and specific about the real restrictions (uncorrelated EXISTS only, single uninterpreted group key, no aggregate calls), and the result is still a non-degenerate theorem rather than a structurally identical pair. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 75449875}, panicked=False

### `EliminateExistsProject` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateExistsProject discards a Project input to the Exists operator. The
Project operator never changes the row cardinality of its input, and row
cardinality is the only thing that Exists cares about, so Project is a no-op.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateExistsProject`, not the other rules in that file):

```
# EliminateExistsProject discards a Project input to the Exists operator. The
# Project operator never changes the row cardinality of its input, and row
# cardinality is the only thing that Exists cares about, so Project is a no-op.
[EliminateExistsProject, Normalize]
(Exists (Project $input:*) $existsPrivate:*)
=>
(Exists $input $existsPrivate)
```
- Attempts used: 44
- Last updated: 2026-09-25T23:11:30.813998+00:00
- Reason / notes: Manually investigated by Claude (the automated attempt found a real proof via a DSL extension, but it broke 25 already-proved rules on merge — correctly blocked). Instead of reusing that risky edit, encoded this fully within the porter's own candidate file: RexRN is just an interface (RexNode semantics()), so a custom local record can build a real Calcite RexSubQuery.exists(RelNode) directly via raw Calcite APIs, with zero shared DSL file changes (no extend_dsl_file, no regression risk). QED's prover core (qed-prover/src/pipeline/relation.rs, eval_logic's EXISTS arm) already has genuine interpreted EXISTS semantics: Logic::squash(UExpr::sum(scope, UExpr::app(rel, vars))) — true iff the subquery relation has at least one row — confirmed via JSONSerializer, which already serializes RexSubQuery objects with an 'EXISTS' operator name and embedded query relation. Encoded before() = outer.filter(EXISTS(Project(input))), after() = outer.filter(EXISTS(input)); QED proves them equal with real SMT engagement (not a structural triviality). Verified non-vacuous with a negative control: adding a filter to the projected side (which CAN remove rows) correctly breaks the proof (provable=false) — confirming the check is genuinely sensitive to row presence/absence, not just always-true. SCOPE: FULL, since EXISTS is unconditionally insensitive to any Project on its input (Project never changes row cardinality, which is the entire premise of the source rule).
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 24562875}, panicked=False

### `EliminateExistsZeroRows` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateExistsZeroRows converts an Exists subquery to False when it's known
that the input produces zero rows.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateExistsZeroRows`, not the other rules in that file):

```
# EliminateExistsZeroRows converts an Exists subquery to False when it's known
# that the input produces zero rows.
[EliminateExistsZeroRows, Normalize]
(Exists $input:* & (HasZeroRows $input))
=>
(False)
```
- Attempts used: 92
- Last updated: 2026-09-25T23:12:20.296418+00:00
- Reason / notes: Manually investigated by Claude (the automated run exhausted all 5 rounds on repeated context-length crashes, never reaching a real try_rule call). Reused the custom local Exists RexRN (built directly on Calcite's RexSubQuery.exists(RelNode), no shared DSL changes) from EliminateExistsProject. Encoded the zero-rows precondition via RelRN.Empty (a relation of the right shape guaranteed to have zero rows). before() = outer.filter(EXISTS(zeroRowInput)), after() = outer.filter(FALSE). QED proves them equal (the equivalence-class layer resolves it directly via UExpr algebra — squash(sum over an empty relation) reduces to false — without needing SMT). Verified non-vacuous with a negative control: replacing the empty input with an ordinary (non-empty) scan of the same shape correctly breaks the proof (provable=false), confirming the check genuinely depends on the input being empty, not just always-true regardless of row count.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 4527542}, panicked=False

### `EliminateGroupByProject` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateGroupByProject discards a nested Project operator that is only
removing columns from its input (and not synthesizing new ones). That's
something the GroupBy operators can do on their own. This rule does not match
UpsertDistinctOn expressions because they are not built with a Project as a
child, so there is no Project to eliminate.

Note: EliminateGroupByProject should be located above
EliminateJoinUnderGroupByLeft so that it can remove any interfering Projects.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateGroupByProject`, not the other rules in that file):

```
# EliminateGroupByProject discards a nested Project operator that is only
# removing columns from its input (and not synthesizing new ones). That's
# something the GroupBy operators can do on their own. This rule does not match
# UpsertDistinctOn expressions because they are not built with a Project as a
# child, so there is no Project to eliminate.
#
# Note: EliminateGroupByProject should be located above
# EliminateJoinUnderGroupByLeft so that it can remove any interfering Projects.
[EliminateGroupByProject, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn | EnsureDistinctOn
        | EnsureUpsertDistinctOn
    $input:(Project $innerInput:*) &
        (ColsAreSubset
            (OutputCols $input)
            (OutputCols $innerInput)
        )
    $aggregations:*
    $groupingPrivate:*
)
=>
((OpName) $innerInput $aggregations $groupingPrivate)
```
- Attempts used: 24
- Last updated: 2026-09-25T23:06:57.388919+00:00
- Reason / notes: `before()` (Aggregate over the column-dropping Project) and `after()` (Aggregate directly over the base) are structurally distinct, so the proof is non-vacuous and genuinely establishes that a pure column-dropping projection is transparent to grouping on the retained column — exactly the source rule's core claim, with its ColsAreSubset precondition satisfied by construction and the shared `S` scan/field references correctly mapping the pre- and post-elimination group keys. The only hard-coding is structural width (2 columns, fixed drop pattern), which the DSL cannot express symbolically, plus the deliberate DistinctOn/no-aggs instance, and the `// SCOPE: PARTIAL` line accurately and specifically names every assumption, so the provable result is faithful, non-degenerate, and not misleading.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 74522375}, panicked=False

### `EliminateJoinNoColsLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

EliminateJoinNoColsLeft eliminates an InnerJoin with a one row, zero column
left input set. These can be produced when a Values, scalar GroupBy, or other
one-row operator's columns are never used.

Extracted from `join.opt` (which defines multiple rules — implement specifically `EliminateJoinNoColsLeft`, not the other rules in that file):

```
# EliminateJoinNoColsLeft eliminates an InnerJoin with a one row, zero column
# left input set. These can be produced when a Values, scalar GroupBy, or other
# one-row operator's columns are never used.
[EliminateJoinNoColsLeft, Normalize]
(InnerJoin | InnerJoinApply
    $left:* &
        (ColsAreEmpty (OutputCols $left)) &
        (HasOneRow $left)
    $right:*
    $on:*
)
=>
(Select $right $on)
```
- Attempts used: 83
- Last updated: 2026-09-25T23:58:41.374846+00:00
- Reason / notes: `before()` (an INNER join with a one-row, zero-column left) and `after()` (a plain filter on the right) are genuinely different shapes and the equivalence is a real join→filter reduction, not a structural no-op; the uninterpreted predicate `on` is correctly shared between the join condition (which, with a zero-column left, ranges only over the right's columns via the join fields) and the filter (over the right's own fields), and the left is a concrete representative of the rule's `HasOneRow ∧ ColsAreEmpty` precondition — which is the *unique* one-row, zero-column relation in bag semantics, so no generality is lost by hard-coding it (the provable result also confirms it is exactly one row, since a 0- or 2-row left would break the equality). The right input and join condition stay fully uninterpreted and the join kind is correctly INNER, and the only exclusion — the correlated `InnerJoinApply` variant, which the DSL has no apply-join kind to express — is honestly flagged as PARTIAL with a specific reason, leaving a non-degenerate, useful rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1475541}, panicked=False

### `EliminateJoinNoColsRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

EliminateJoinNoColsRight eliminates an InnerJoin with a one row, zero column
right input set. These can be produced when a Values, scalar GroupBy, or other
one-row operator's columns are never used.

Extracted from `join.opt` (which defines multiple rules — implement specifically `EliminateJoinNoColsRight`, not the other rules in that file):

```
# EliminateJoinNoColsRight eliminates an InnerJoin with a one row, zero column
# right input set. These can be produced when a Values, scalar GroupBy, or other
# one-row operator's columns are never used.
[EliminateJoinNoColsRight, Normalize]
(InnerJoin | InnerJoinApply
    $left:*
    $right:* &
        (ColsAreEmpty (OutputCols $right)) &
        (HasOneRow $right)
    $on:*
)
=>
(Select $left $on)
```
- Attempts used: 49
- Last updated: 2026-09-25T23:32:16.711838+00:00
- Reason / notes: before() (INNER join whose right input is a one-row, zero-column Values) and after() (Filter over the left) are genuinely distinct shapes, and the proven equivalence — join with the bag-theoretic unit relation equals a left-only filter — is exactly the source rule's semantics: since the right has zero columns, $on is necessarily left-only, and the single right row preserves each matching left row's multiplicity, so the unit-Values encoding faithfully captures the HasOneRow/ColsAreEmpty preconditions for *any* one-row zero-column operator rather than under-generalizing (the single-column L and name-shared "on" correctly stand in for arbitrary inputs/conditions, and the successful proof confirms the "on" symbol unified across before/after, ruling out a distinct-symbol accident). The only true narrowing — the correlated InnerJoinApply variant, whose outer-dependent right cannot be structurally expressed in the DSL — is honest and specifically flagged in the SCOPE: PARTIAL line, and the non-apply case is a genuine, non-degenerate fragment of the rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1496834}, panicked=False

### `EliminateJoinUnderGroupByLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateJoinUnderGroupByLeft removes a Join operator and its right input if
it can be proven that the removal does not affect the output of the parent
grouping operator. This is the case if:

1. Only columns from the left input are being used by the grouping operator.

2. It can be proven that removal of the Join does not affect the result of the
grouping operator's aggregate functions.

3. The OrderingChoice of the grouping operator can be expressed with only
columns from the left input. Or in other words, at least one column in
every ordering group is one of the left output columns.

Condition #2 is only true when the following are all true:

1. All left rows are included in the output of the join. See the comment above
filtersMatchAllLeftRows in multiplicity_builder.go for more information on
when this is the case.
2. Either the join does not duplicate any left rows, or the join duplicates
left rows but the grouping operator's aggregate functions ignore duplicate
values. See the comment above filtersMatchLeftRowsAtMostOnce in
multiplicity_builder.go for more information on when rows are duplicated.
3. The join does not null-extend the left columns.

EliminateJoinUnderGroupByLeft should stay at the top of the file so that it
has a chance to fire before rules like EliminateDistinctOn that might prevent
matching.

Similar to the join-elimination rules that match on Project operators,
EliminateJoinUnderGroupByLeft can remap references to the right input of the
join to refer to equivalent columns on the left input.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateJoinUnderGroupByLeft`, not the other rules in that file):

```
# EliminateJoinUnderGroupByLeft removes a Join operator and its right input if
# it can be proven that the removal does not affect the output of the parent
# grouping operator. This is the case if:
#
# 1. Only columns from the left input are being used by the grouping operator.
#
# 2. It can be proven that removal of the Join does not affect the result of the
#    grouping operator's aggregate functions.
#
# 3. The OrderingChoice of the grouping operator can be expressed with only
#    columns from the left input. Or in other words, at least one column in
#    every ordering group is one of the left output columns.
#
# Condition #2 is only true when the following are all true:
#
# 1. All left rows are included in the output of the join. See the comment above
#    filtersMatchAllLeftRows in multiplicity_builder.go for more information on
#    when this is the case.
# 2. Either the join does not duplicate any left rows, or the join duplicates
#    left rows but the grouping operator's aggregate functions ignore duplicate
#    values. See the comment above filtersMatchLeftRowsAtMostOnce in
#    multiplicity_builder.go for more information on when rows are duplicated.
# 3. The join does not null-extend the left columns.
#
# EliminateJoinUnderGroupByLeft should stay at the top of the file so that it
# has a chance to fire before rules like EliminateDistinctOn that might prevent
# matching.
#
# Similar to the join-elimination rules that match on Project operators,
# EliminateJoinUnderGroupByLeft can remap references to the right input of the
# join to refer to equivalent columns on the left input.
[EliminateJoinUnderGroupByLeft, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn
    $input:(InnerJoin | LeftJoin $left:*)
    $aggs:*
    $private:(GroupingPrivate $groupingCols:* $ordering:*) &
        (OrderingCanProjectCols
            $ordering
            $leftCols:(OutputCols $left)
        ) &
        (CanRemapCols
            $toRemap:(UnionCols
                $groupingCols
                (AggregationOuterCols $aggs)
            )
            $leftCols
            $fds:(FuncDeps $input)
        ) &
        (CanUseImprovedJoinElimination $toRemap $leftCols) &
        (CanEliminateJoinUnderGroupByLeft $input $aggs)
)
=>
((OpName)
    (Project
        $left
        (ProjectRemappedCols $toRemap $leftCols $fds)
        $leftCols
    )
    $aggs
    (MakeGrouping
        $groupingCols
        (PruneOrdering $ordering $leftCols)
    )
)
```
- Attempts used: 22
- Last updated: 2026-09-25T23:16:42.038667+00:00
- Reason / notes: The encoding is genuinely non-vacuous (before() contains the LEFT join plus the right scan; after() drops both) and the symbol sharing is correct — the group key join.field(0) is the same preserved left column as left.field(0), and the join condition stays uninterpreted, so QED's proof holds for *any* join predicate, which is exactly the special case the rule covers. None of the source rule's side conditions are silently missing: a LEFT join includes every left row and never null-extends left columns, the absence of aggregate calls makes the "aggs must ignore duplicates" condition moot, there is no ordering to constrain, and the group key references the left column directly so ProjectRemappedCols/PruneOrdering are identity — the remaining generality (InnerJoin, multi-column remapping via functional dependencies, duplicate-tolerant aggregates, ordering) is unreachable by QED itself, not by a missing DSL feature. The PARTIAL scope line is accurate, specific, and the fragment proved (distinct of a left column under a LEFT join equals distinct of that column over the left input alone) is a real, useful piece of the original rule. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 85829375}, panicked=False

### `EliminateJoinUnderGroupByRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateJoinUnderGroupByRight is symmetric with
EliminateJoinUnderGroupByLeft, except that it matches on InnerJoins.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateJoinUnderGroupByRight`, not the other rules in that file):

```
# EliminateJoinUnderGroupByRight is symmetric with
# EliminateJoinUnderGroupByLeft, except that it matches on InnerJoins.
[EliminateJoinUnderGroupByRight, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn
    $input:(InnerJoin * $right:*)
    $aggs:*
    $private:(GroupingPrivate $groupingCols:* $ordering:*) &
        (OrderingCanProjectCols
            $ordering
            $rightCols:(OutputCols $right)
        ) &
        (CanRemapCols
            $toRemap:(UnionCols
                $groupingCols
                (AggregationOuterCols $aggs)
            )
            $rightCols
            $fds:(FuncDeps $input)
        ) &
        (CanUseImprovedJoinElimination $toRemap $rightCols) &
        (CanEliminateJoinUnderGroupByRight $input $aggs)
)
=>
((OpName)
    (Project
        $right
        (ProjectRemappedCols $toRemap $rightCols $fds)
        $rightCols
    )
    $aggs
    (MakeGrouping
        $groupingCols
        (PruneOrdering $ordering $rightCols)
    )
)
```
- Attempts used: 63
- Last updated: 2026-09-26T00:06:45.823234+00:00
- Reason / notes: The encoding is a non-vacuous, sound special case whose PARTIAL scope is honestly and specifically declared: before() genuinely contains the equality self-join that after() eliminates, and the equivalence holds universally because every non-null right row self-matches under the equality condition while the aggregate-free group-by collapses the duplication the join introduces. The concrete EQUALS condition, the non-nullable key column, and the shared scan (making it a self-join) are exactly the assumptions that make the source rule's unverifiable preconditions — all-right-rows-survive, functional-dependency-based column remapping, and ordering constraints — hold structurally, and all of them are named in the SCOPE line rather than hidden as symbol-sharing or silently dropped preconditions. This is a genuine, non-degenerate corner of the rule (dropping an O(n²) self-join beneath a distinct-on) that QED fundamentally cannot certify for arbitrary independent join inputs and uninterpreted conditions, so the provable result is faithful to what it claims. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 22228500}, panicked=False

### `EliminateJoinUnderProjectLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/project.opt

EliminateJoinUnderProjectLeft replaces an InnerJoin or LeftJoin with its left
input when:
1. The project doesn't use columns from the join's right input.
2. The join does not duplicate any left rows.
3. The join does not filter any left rows.

Note: EliminateJoinUnderProjectLeft should stay above EliminateProject so that
it has a chance to fire before the Project can be removed.

It is possible for references to the right input of the join to be replaced by
equivalent columns from the left input. This is handled by adding projections
that map the left column to the equivalent right column (leftCol AS rightCol).

It is ok to call MergeProjections without checking CanMergeProjections because
RemapProjectionCols returns projections that only reference columns from the
left input, and ProjectRemappedCols only projects columns from the right side
of the join.

Extracted from `project.opt` (which defines multiple rules — implement specifically `EliminateJoinUnderProjectLeft`, not the other rules in that file):

```
# EliminateJoinUnderProjectLeft replaces an InnerJoin or LeftJoin with its left
# input when:
# 1. The project doesn't use columns from the join's right input.
# 2. The join does not duplicate any left rows.
# 3. The join does not filter any left rows.
#
# Note: EliminateJoinUnderProjectLeft should stay above EliminateProject so that
# it has a chance to fire before the Project can be removed.
#
# It is possible for references to the right input of the join to be replaced by
# equivalent columns from the left input. This is handled by adding projections
# that map the left column to the equivalent right column (leftCol AS rightCol).
#
# It is ok to call MergeProjections without checking CanMergeProjections because
# RemapProjectionCols returns projections that only reference columns from the
# left input, and ProjectRemappedCols only projects columns from the right side
# of the join.
[EliminateJoinUnderProjectLeft, Normalize]
(Project
    $join:(InnerJoin | LeftJoin $left:* $right:*) &
        (JoinDoesNotDuplicateLeftRows $join) &
        (JoinPreservesLeftRows $join)
    $projections:*
    $passthrough:* &
        (CanRemapCols
            $fromCols:(UnionCols
                $passthrough
                (ProjectionOuterCols $projections)
            )
            $leftCols:(OutputCols $left)
            $fds:(FuncDeps $join)
        ) &
        (CanUseImprovedJoinElimination $fromCols $leftCols)
)
=>
(Project
    $left
    (MergeProjections
        (RemapProjectionCols $projections $leftCols $fds)
        (ProjectRemappedCols $passthrough $leftCols $fds)
        $passthrough
    )
    (DifferenceCols $passthrough (OutputCols $right))
)
```
- Attempts used: 32
- Last updated: 2026-09-25T23:51:40.108455+00:00
- Reason / notes: before() (a LEFT self-join of a unique, non-nullable single-column scan on `col = col`, projecting only field 0) is structurally distinct from after() (the same column projected directly from the scan), and their bag-equivalence genuinely depends on the encoded key/uniqueness (no duplicate left rows), the non-null guarantee (so `a.col = a.col` holds cleanly, no NULL-equality gap), and the LEFT-join kind (no filtered left rows) — so the proof is non-vacuous and exercises the rule's actual preconditions rather than a tautology. The join kind (LEFT, one of the rule's two allowed kinds), the "project avoids the right side" condition, and the join-safety preconditions are all correctly reflected, and the residual narrowness (fixed LEFT kind, single-column unique-key self-join) is inherent to the fact that the source rule's general `JoinDoesNotDuplicateLeftRows`/functional-dependency/`CanRemapCols` machinery is inexpressible in this DSL — and it is honestly and specifically tagged `// SCOPE: PARTIAL`, so the "provable" result is neither meaningless nor misleading. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 66001125}, panicked=False

### `EliminateJoinUnderProjectRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/project.opt

EliminateJoinUnderProjectRight mirrors EliminateJoinUnderProjectLeft, except
that it only matches InnerJoins.

Extracted from `project.opt` (which defines multiple rules — implement specifically `EliminateJoinUnderProjectRight`, not the other rules in that file):

```
# EliminateJoinUnderProjectRight mirrors EliminateJoinUnderProjectLeft, except
# that it only matches InnerJoins.
[EliminateJoinUnderProjectRight, Normalize]
(Project
    $join:(InnerJoin $left:* $right:*) &
        (JoinDoesNotDuplicateRightRows $join) &
        (JoinPreservesRightRows $join)
    $projections:*
    $passthrough:* &
        (CanRemapCols
            $fromCols:(UnionCols
                $passthrough
                (ProjectionOuterCols $projections)
            )
            $rightCols:(OutputCols $right)
            $fds:(FuncDeps $join)
        ) &
        (CanUseImprovedJoinElimination $fromCols $rightCols)
)
=>
(Project
    $right
    (MergeProjections
        (RemapProjectionCols $projections $rightCols $fds)
        (ProjectRemappedCols $passthrough $rightCols $fds)
        $passthrough
    )
    (DifferenceCols $passthrough (OutputCols $left))
)
```
- Attempts used: 4
- Last updated: 2026-09-25T23:56:33.183938+00:00
- Reason / notes: The proof is non-vacuous — before() genuinely contains an inner self-join that after() removes, and equivalence depends essentially on the declared facts (column 0 is a unique key and non-nullable), which structurally enforce the source rule's JoinDoesNotDuplicateRightRows/JoinPreservesRightRows preconditions rather than leaving them silently absent (key ⇒ at most one matching left row; non-null ⇒ every right row self-matches). INNER is the correct join kind because the Right variant, unlike the Left variant, matches only InnerJoins, and the project reading only the right column is a real instance of the CanRemapCols condition. The encoding is a narrower special case (self-join of a single unique non-nullable-keyed scan on equality, project on the preserved right column only), but that restriction is genuine, specific, and exactly as disclosed in the SCOPE line, so the proved statement is a legitimate, non-degenerate instance of the rule rather than a coincidental or over-constrained artifact. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 63861333}, panicked=False

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

### `EliminateProject` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/project.opt

EliminateProject discards a Project operator which is not adding or removing
columns.

Extracted from `project.opt` (which defines multiple rules — implement specifically `EliminateProject`, not the other rules in that file):

```
# EliminateProject discards a Project operator which is not adding or removing
# columns.
[EliminateProject, Normalize]
(Project
    $input:*
    $projections:[]
    $passthrough:* &
        (ColsAreEqual $passthrough (OutputCols $input))
)
=>
$input
```
- Attempts used: 5
- Last updated: 2026-09-26T00:14:04.466800+00:00
- Reason / notes: The encoding faithfully captures both preconditions of the source rule — an empty projections list and a passthrough set exactly equal to the input's output columns — as Project(Input, [col0, col1]) over an uninterpreted two-column scan rewritten to the bare scan, and before() is structurally distinct from after() (a Project node on top of the scan vs. the scan alone), so the proof is not vacuous. The only narrowing is the fixed arity of 2, which the SCOPE line states specifically and honestly; arbitrary input arity is not expressible end-to-end because QED decides over concrete schemas with no arity quantification, so this is a genuine, justified special case rather than a fixable DSL gap. There are no predicates, joins, null or key assumptions to get wrong, and no symbol-sharing issues, so no preconditions are silently absent. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 104625}, panicked=False

### `EliminateRedundantBarrier` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/barrier.opt

EliminateRedundantBarrier removes a Barrier operator when it wraps another
identical Barrier. This deduplication avoids unnecessary nesting of
equivalent Barriers. The rule applies only when both Barrier operators have
the same configuration.

Extracted from `barrier.opt` (which defines multiple rules — implement specifically `EliminateRedundantBarrier`, not the other rules in that file):

```
# EliminateRedundantBarrier removes a Barrier operator when it wraps another
# identical Barrier. This deduplication avoids unnecessary nesting of
# equivalent Barriers. The rule applies only when both Barrier operators have
# the same configuration.
[EliminateRedundantBarrier, Normalize]
(Barrier
    (Barrier $input:* $innerLeakproofPermeable:*)
    $outerLeakproofPermeable:* &
        (EqualsBool
            $innerLeakproofPermeable
            $outerLeakproofPermeable
        )
)
=>
(Barrier $input $outerLeakproofPermeable)
```
- Attempts used: 21
- Last updated: 2026-09-26T00:27:37.228671+00:00
- Reason / notes: The encoding faithfully models a Barrier as an identity projection (project(field(0)) over a single-column scan), which correctly captures the Barrier's logical pass-through semantics; the shape before()=Project∘Project∘Scan vs after()=Project∘Scan matches the source rule's Barrier(Barrier(input))→Barrier(input) and is structurally non-trivial (two projection nodes vs one), and the equal-config precondition is irrelevant to bag semantics since a Barrier's leakproofPermeable parameter is a flow-control knob with no effect on row values, so ignoring it is sound. The single-column restriction is a genuine but mild under-generalization (the identity∘identity=identity property is arity-independent and the DSL requires a fixed scan arity), and it is honestly tagged SCOPE: PARTIAL, leaving a useful non-degenerate proof. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 353833}, panicked=False

### `EliminateSelect` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

EliminateSelect discards an unnecessary Select operator in the case where its
filter is always true. Keep this near the top of the file so that it tends to
be checked early.

Extracted from `select.opt` (which defines multiple rules — implement specifically `EliminateSelect`, not the other rules in that file):

```
# EliminateSelect discards an unnecessary Select operator in the case where its
# filter is always true. Keep this near the top of the file so that it tends to
# be checked early.
[EliminateSelect, Normalize]
(Select $input:* [])
=>
$input
```
- Attempts used: 4
- Last updated: 2026-09-26T00:20:00.367976+00:00
- Reason / notes: The encoding is faithful: `before()` is a Filter with a literal-True condition over a scan and `after()` is the same bare scan, so the two sides are structurally distinct (non-vacuous) and correctly model a Select with an empty, always-true filter list — the true literal (not an uninterpreted predicate) is the right choice, since literal True passes every row with no null edge cases. The scan is a fully uninterpreted bag, so the proved claim σ_true(B) = B for all bags B is exactly the rule's complete semantic content: EliminateSelect is input-agnostic and has no side conditions, and treating the leaf as an uninterpreted scan is the DSL's standard treatment of "any $input" (as in the FilterMerge worked example), making `SCOPE: FULL` honest. There are no symbol-sharing, wrong-operator, or missing-precondition issues.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 293958}, panicked=False

### `EliminateSemiJoin` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

EliminateSemiJoin discards a SemiJoin when it's known that all left rows will
be matched by the join filters.

Extracted from `join.opt` (which defines multiple rules — implement specifically `EliminateSemiJoin`, not the other rules in that file):

```
# EliminateSemiJoin discards a SemiJoin when it's known that all left rows will
# be matched by the join filters.
[EliminateSemiJoin, Normalize]
(SemiJoin | SemiJoinApply
    $left:*
    $right:*
    $on:* & (JoinFiltersMatchAllLeftRows $left $right $on)
)
=>
$left
```
- Attempts used: 24
- Last updated: 2026-09-26T00:30:18.387489+00:00
- Reason / notes: The encoding is a genuine, non-vacuous instance of EliminateSemiJoin — the SEMI join kind and the `=> $left` conclusion match the source rule, and `before()` (self-semijoin on constant-true) is structurally distinct from `after()`, so the proof exercises real semijoin/emptiness semantics rather than a structural identity. The self-shared scan plus true filter is what makes the source rule's `JoinFiltersMatchAllLeftRows` precondition hold by plan structure alone (non-empty: every left row matches its own copy in the identical right input; empty: both sides are trivially empty), and this narrowing is legitimate because QED cannot express that precondition for independent left/right relations or an uninterpreted filter — it performs no predicate entailment or data-property reasoning, so the fully general rule is unprovable here for reasons inherent to the prover, not a missing builder. The `// SCOPE: PARTIAL` tag states the assumption specifically and accurately, so the provable result is honest: a real, if narrow, special case of the rule rather than a disguised or vacuous one.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 70274792}, panicked=False

### `EliminateSetLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/set.opt

EliminateSetLeft replaces a UnionAll or ExceptAll operator with a right side
having a cardinality of zero, with just the left side operand.

It is possible for the left and right sides of the set operator to have column
IDs that are also present in the output columns of the operator, e.g. after
the SplitDisjunction exploration rule has been applied. These columns are
included as passthrough columns in the generated Project because they do not
need to be projected. All other column IDs are added to the ProjectionsExpr.

Extracted from `set.opt` (which defines multiple rules — implement specifically `EliminateSetLeft`, not the other rules in that file):

```
# EliminateSetLeft replaces a UnionAll or ExceptAll operator with a right side
# having a cardinality of zero, with just the left side operand.
#
# It is possible for the left and right sides of the set operator to have column
# IDs that are also present in the output columns of the operator, e.g. after
# the SplitDisjunction exploration rule has been applied. These columns are
# included as passthrough columns in the generated Project because they do not
# need to be projected. All other column IDs are added to the ProjectionsExpr.
[EliminateSetLeft, Normalize]
(UnionAll | ExceptAll
    $left:*
    $right:* & (HasZeroRows $right)
    $colmap:*
)
=>
(Project
    $left
    (ProjectColMapLeft $colmap)
    (ProjectPassthroughLeft $colmap)
)
```
- Attempts used: 22
- Last updated: 2026-09-26T00:29:29.740921+00:00
- Reason / notes: The encoding is faithful to the UnionAll arm: before() is π(Left) UNION ALL Empty while after() is π(Left) — structurally distinct, so the proof is of the rule's genuine semantic core (bag-union with a zero-row operand is identity), not a vacuous equality; Left and Right are distinct scans correctly sharing row-type symbols (required for type-compatible set operands), and the non-identity colmap (2,0,1) is applied uniformly to both operands exactly as the source requires. The PARTIAL scope line is honest and specific: the ExceptAll arm is truly inexpressible (JSONSerializer only serializes set minus and throws for the bag variant), and modeling HasZeroRows as a structural Empty is the closest available proxy, since the DSL exposes no zero-row relation constraint (only uniqueness keys). ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1512375}, panicked=False

### `EliminateSetRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/set.opt

EliminateSetRight replaces a UnionAll operator with a left side having a
cardinality of zero, with just the right side operand. Note that it only
applies to UnionAll operators because Except operators only output left input
rows.

See the comment above EliminateSetLeft which describes when columns are
projected vs. passed-through.

Extracted from `set.opt` (which defines multiple rules — implement specifically `EliminateSetRight`, not the other rules in that file):

```
# EliminateSetRight replaces a UnionAll operator with a left side having a
# cardinality of zero, with just the right side operand. Note that it only
# applies to UnionAll operators because Except operators only output left input
# rows.
#
# See the comment above EliminateSetLeft which describes when columns are
# projected vs. passed-through.
[EliminateSetRight, Normalize]
(UnionAll $left:* & (HasZeroRows $left) $right:* $colmap:*)
=>
(Project
    $right
    (ProjectColMapRight $colmap)
    (ProjectPassthroughRight $colmap)
)
```
- Attempts used: 7
- Last updated: 2026-09-26T00:38:35.824238+00:00
- Reason / notes: The encoding faithfully captures the source rule's semantics: `before()` is a UNION ALL of a structurally-empty left (faithful encoding of `HasZeroRows $left`, the best the DSL can express) with the colmapped right, and `after()` is just the colmapped right — a genuinely non-trivial difference that proves `∅ ∪_bag R = R` for all instantiations. The colmap (concrete permutation applied identically to both sides) correctly models the type-alignment requirement of the set op, the `union(true, …)` flag matches UnionAll (bag union), and the PARTIAL scope tag is honest since the DSL cannot express "arbitrary expression constrained to zero rows" as distinct from "structurally empty relation."
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 503666}, panicked=False

### `EliminateUpsertDistinct` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateUpsertDistinct is similar to EliminateDistinct, but it only checks if
the grouping columns are a lax key because UpsertDistinctOn considers NULL
values to be distinct from one another for the purposes of grouping.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateUpsertDistinct`, not the other rules in that file):

```
# EliminateUpsertDistinct is similar to EliminateDistinct, but it only checks if
# the grouping columns are a lax key because UpsertDistinctOn considers NULL
# values to be distinct from one another for the purposes of grouping.
[EliminateUpsertDistinct, Normalize]
(UpsertDistinctOn | EnsureUpsertDistinctOn
    $input:*
    $aggs:*
    $groupingPrivate:* &
        (ColsAreLaxKey (GroupingCols $groupingPrivate) $input)
)
=>
(Project $input [] (GroupingOutputCols $groupingPrivate $aggs))
```
- Attempts used: 24
- Last updated: 2026-09-26T00:45:45.618066+00:00
- Reason / notes: The encoding is non-trivial (before is an Aggregate/GROUP BY, after is a plain Project) and correctly proves that a GROUP BY on a unique column is equivalent to projecting that column. It is a valid, honestly-labeled PARTIAL special case of EliminateUpsertDistinct: the unique-key precondition (strict key) is stronger than the source's lax-key requirement but implies it, and in the all-values-distinct case the UpsertDistinctOn NULLs-are-distinct semantics coincide with plain GROUP BY semantics, so the operator substitution is sound. No symbol-sharing or triviality issues are present. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 378125}, panicked=False

### `EliminateZeroCardProject` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/project.opt

EliminateZeroCardProject eliminates a Project when its input has zero
cardinality. The projection expressions are per-row and will never be
evaluated, so it is safe to remove them regardless of volatility. This
complements the SimplifyZeroCardinalityGroup rule which requires the entire
expression to be leakproof.

Extracted from `project.opt` (which defines multiple rules — implement specifically `EliminateZeroCardProject`, not the other rules in that file):

```
# EliminateZeroCardProject eliminates a Project when its input has zero
# cardinality. The projection expressions are per-row and will never be
# evaluated, so it is safe to remove them regardless of volatility. This
# complements the SimplifyZeroCardinalityGroup rule which requires the entire
# expression to be leakproof.
[EliminateZeroCardProject, Normalize]
(Project
    $input:* & (HasZeroRows $input) & (IsLeakproof $input)
    $projections:*
    $passthrough:*
)
=>
(ConstructEmptyValues
    (UnionCols $passthrough (ProjectionCols $projections))
)
```
- Attempts used: 21
- Last updated: 2026-09-26T00:58:45.572705+00:00
- Reason / notes: The encoding correctly models the original rule's core semantic claim — a Project over a zero-cardinality input yields an empty relation of the output type — by using `.empty()` to encode `HasZeroRows` and an uninterpreted function `f` for the projection expression; the before/after trees are structurally different (Project-over-∅ vs. bare ∅) so the proof is non-vacuous, and the PARTIAL scope tag honestly documents the fixed 2-column/1-projection shape that the DSL cannot express with arbitrary arities.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1447959}, panicked=False

### `EliminateZeroCardSelect` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

EliminateZeroCardSelect eliminates a Select when its input has zero
cardinality. The filter expressions are per-row and will never be evaluated,
so it is safe to remove them regardless of volatility. This complements the
SimplifyZeroCardinalityGroup rule which requires the entire expression to be
leakproof.

Extracted from `select.opt` (which defines multiple rules — implement specifically `EliminateZeroCardSelect`, not the other rules in that file):

```
# EliminateZeroCardSelect eliminates a Select when its input has zero
# cardinality. The filter expressions are per-row and will never be evaluated,
# so it is safe to remove them regardless of volatility. This complements the
# SimplifyZeroCardinalityGroup rule which requires the entire expression to be
# leakproof.
[EliminateZeroCardSelect, Normalize]
(Select $input:* & (HasZeroRows $input) $filters:*)
=>
$input
```
- Attempts used: 3
- Last updated: 2026-09-26T00:47:13.384558+00:00
- Reason / notes: The encoding is faithful and non-degenerate: `before()` (two nested uninterpreted filters over a structurally-empty `.empty()` relation) genuinely differs from `after()` (the bare empty relation), and the proof that they are bag-equivalent is exactly the rule's validity claim — filtering a zero-row input yields the same zero-row input, so the Select can be dropped. The `HasZeroRows` precondition is captured in the only form the DSL permits (a statically-known-empty shape rather than a general side-condition over an uninterpreted input), which is a real QED limitation rather than a missing operator, so the narrower special case is the correct fallback and is honestly flagged PARTIAL; the operators (nested Filters modeling a multi-filter Select, the empty relation) and the shared input symbol are all correct. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1406125}, panicked=False

### `ExtractJoinComparisons` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

ExtractJoinComparisons finds equality and inequality conditions such that
one side only depends on left columns and the other only on right columns
and pushes the expressions down into Project operators. The result is a
join that has an equality or inequality constraint, which is much more
efficient. For example:

SELECT * FROM abc JOIN xyz ON a=x+1

This join would be quadratic because we have no equality columns.
This rule rewrites it as:

SELECT a,b,c,x,y,z FROM abc JOIN (SELECT *, x+1 AS x1 FROM xyz) ON a=x1

This join can use hash join or lookup on the equality columns.

Depending on the expressions involved, one or both sides require a projection.

Extracted from `join.opt` (which defines multiple rules — implement specifically `ExtractJoinComparisons`, not the other rules in that file):

```
# ExtractJoinComparisons finds equality and inequality conditions such that
# one side only depends on left columns and the other only on right columns
# and pushes the expressions down into Project operators. The result is a
# join that has an equality or inequality constraint, which is much more
# efficient. For example:
#
#   SELECT * FROM abc JOIN xyz ON a=x+1
#
# This join would be quadratic because we have no equality columns.
# This rule rewrites it as:
#
#   SELECT a,b,c,x,y,z FROM abc JOIN (SELECT *, x+1 AS x1 FROM xyz) ON a=x1
#
# This join can use hash join or lookup on the equality columns.
#
# Depending on the expressions involved, one or both sides require a projection.
[ExtractJoinComparisons, Normalize]
(JoinNonApply
    $left:* & ^(HasOuterCols $left)
    $right:* & ^(HasOuterCols $right)
    $on:[
        ...
        $item:(FiltersItem
            (Eq | Lt | Le | Gt | Ge
                    $a:^(ConstValue)
                    $b:^(ConstValue)
                ) &
                (CanExtractJoinComparison
                    $a
                    $b
                    (OutputCols $left)
                    (OutputCols $right)
                )
        )
        ...
    ]
    $private:*
)
=>
(ExtractJoinComparison (OpName) $left $right $on $item $private)
```
- Attempts used: 28
- Last updated: 2026-09-26T01:02:01.074777+00:00
- Reason / notes: before() = L ⋈_{cmp(f(L),g(R))} R and after() = push f,g into per-side Projections, join on the synthesized columns, then drop them, are structurally distinct yet both denote {(x,y) : L(x)∧R(y)∧cmp(f(x),g(y))}, so the proof is non-vacuous and captures the rule's actual rewrite (moving computed comparison operands from the ON clause into pre-join Projections so the join key becomes a plain column). The comparison and its two operands are correctly modeled as uninterpreted symbols (cmp standing in for Eq/Lt/Le/Gt/Ge, f and g for the arbitrary side-bound expressions), the no-outer-columns guard is respected by using uncorrelated scans, and the side-split precondition (CanExtractJoinComparison) is built into the shape (f over L's column, g over R's column), so no precondition is silently dropped and no spurious symbol-sharing is making an unsound rewrite look sound. The narrowing to inner join / one column per side / a single comparison is narrower than the full JoinNonApply rule, but it is specifically and honestly labeled in the SCOPE line and remains a non-degenerate core of the optimization, so the provable result is a faithful encoding of its claimed scope rather than a misleading one. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 72561125}, panicked=False

### `ExtractRedundantConjunct` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

ExtractRedundantConjunct matches an OR expression in which the same conjunct
appears in both the left and right OR conditions:

A OR (A AND B)          =>  A
(A AND B) OR (A AND C)  =>  A AND (B OR C)

In both these cases, the redundant conjunct is A.

This transformation is useful for finding a conjunct that can be pushed down
in the query tree. For example, if the redundant conjunct A is fully bound by
one side of a join, it can be pushed through the join, even if B AND C cannot.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `ExtractRedundantConjunct`, not the other rules in that file):

```
# ExtractRedundantConjunct matches an OR expression in which the same conjunct
# appears in both the left and right OR conditions:
#
#   A OR (A AND B)          =>  A
#   (A AND B) OR (A AND C)  =>  A AND (B OR C)
#
# In both these cases, the redundant conjunct is A.
#
# This transformation is useful for finding a conjunct that can be pushed down
# in the query tree. For example, if the redundant conjunct A is fully bound by
# one side of a join, it can be pushed through the join, even if B AND C cannot.
[ExtractRedundantConjunct, Normalize]
(Or
    $left:^(Or)
    $right:^(Or) &
        (Let
            ($conjunct $ok):(FindRedundantConjunct $left $right)
            $ok
        )
)
=>
(ExtractRedundantConjunct $conjunct $left $right)
```
- Attempts used: 21
- Last updated: 2026-09-26T01:01:04.763389+00:00
- Reason / notes: The encoding is exactly the source rule's documented second example, (A AND B) OR (A AND C) ⟹ A AND (B OR C), lifted to a filter over a scan with a, b, c as three independent uninterpreted predicates — `a` is shared as the redundant conjunct precisely as the FindRedundantConjunct guard requires, b and c are distinct symbols, and before()/after() are structurally different, so the proof is non-vacuous and not over-constrained. The source rule's guards (operands not themselves Or, a common conjunct exists) are pure pattern-matching restrictions satisfied by this shape rather than semantic preconditions, and the factoring identity is valid in Kleene three-valued logic as well as classical logic, so no null- or key-related assumption was silently dropped. The SCOPE: PARTIAL line is honest: restricting both OR operands to binary conjunctions is a genuine narrowing (the full rule also covers absorption shapes like A OR (A AND B) => A), but the chosen instance is the join-pushdown case the rule's own comment highlights, remains non-degenerate, and is fully general in the Boolean structure via the uninterpreted predicates. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 69940667}, panicked=False

### `FoldBinary` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldBinary evaluates a binary operation over constant inputs, replacing the
entire expression with a constant. The rule applies as long as the evaluation
would not cause an error. Any errors should be saved for execution time,
since it's possible that the given operation will not be executed. For
example:

SELECT CASE WHEN true THEN 42 ELSE 1/0 END

In this query, the ELSE clause is not executed, so the divide-by-zero error
should not be triggered.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldBinary`, not the other rules in that file):

```
# FoldBinary evaluates a binary operation over constant inputs, replacing the
# entire expression with a constant. The rule applies as long as the evaluation
# would not cause an error. Any errors should be saved for execution time,
# since it's possible that the given operation will not be executed. For
# example:
#
#   SELECT CASE WHEN true THEN 42 ELSE 1/0 END
#
# In this query, the ELSE clause is not executed, so the divide-by-zero error
# should not be triggered.
[FoldBinary, Normalize]
(Binary
    $left:* & (IsConstValueOrGroupOfConstValues $left)
    $right:* &
        (IsConstValueOrGroupOfConstValues $right) &
        (Let
            ($result $ok):(FoldBinary (OpName) $left $right) $ok
        )
)
=>
$result
```
- Attempts used: 21
- Last updated: 2026-09-26T01:18:45.281969+00:00
- Reason / notes: The encoding is a genuine, non-vacuous special case whose before/after shapes exactly match one concrete firing of the original FoldBinary — an AND of the constant literals TRUE and FALSE in a filter position, folded to the constant FALSE — and the SCOPE line honestly and specifically names that restriction. The narrowing to boolean AND over literals is forced by a real QED limitation rather than a missed DSL capability: QED has no uninterpreted constant data values and treats all non-boolean operators as uninterpreted, so it fundamentally cannot express "fold an arbitrary operator over arbitrary constants to a fresh constant," making this the narrowest faithful instance expressible. The remaining failure modes are clean — the proof is not vacuous (the AND is actually eliminated), the shared `Source` relation is shared correctly across both sides, and the rule's `IsConstValueOrGroupOfConstValues`/`$ok` preconditions are vacuously satisfied for these literals, so the "provable" result is meaningful within its stated scope. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 311042}, panicked=False

### `FoldColumnAccess` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldColumnAccess eliminates a column access operator applied to a tuple value
that is statically constructed, like this:

(((i, i+1) as foo, bar)).foo
(((1, 2) as foo, bar)).bar

The rule replaces the column access operator with the referenced tuple
element.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldColumnAccess`, not the other rules in that file):

```
# FoldColumnAccess eliminates a column access operator applied to a tuple value
# that is statically constructed, like this:
#
#   (((i, i+1) as foo, bar)).foo
#   (((1, 2) as foo, bar)).bar
#
# The rule replaces the column access operator with the referenced tuple
# element.
[FoldColumnAccess, Normalize]
(ColumnAccess
    $input:*
    $idx:* &
        (Let ($result $ok):(FoldColumnAccess $input $idx) $ok)
)
=>
$result
```
- Attempts used: 43
- Last updated: 2026-09-26T01:45:17.235056+00:00
- Reason / notes: before() nests a single-column field access on top of a two-element tuple construction while after() drops the intermediate construction, so the proof is of a real, non-vacuous identity in the correct relational shape (Project of a field over a constructed row ⇒ Project of the element) that faithfully models "column access into a statically constructed tuple folds to the referenced element," with the statically-constructed-tuple precondition correctly represented by the intermediate ProjectMany. The tuple elements e0/e1 and the base table are fully uninterpreted and kept as independent symbols, so the proven identity holds for arbitrary element expressions (columns, constants, nested expressions) exactly as the source rule requires, with no coincidental over-constraint. The only hard-coded choices are tuple arity 2 and accessed index 1, which cannot be uninterpreted in this fixed-shape pattern language (projection width and field ordinal are concrete), match the rule's own documented two-tuple examples, and are precisely disclosed by the PARTIAL tag — a genuine, non-degenerate special case. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 467125}, panicked=False

### `FoldComparison` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldComparison is similar to FoldBinary, but it involves a comparison
operation. As with FoldBinary, FoldComparison applies as long as the
evaluation would not cause an error.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldComparison`, not the other rules in that file):

```
# FoldComparison is similar to FoldBinary, but it involves a comparison
# operation. As with FoldBinary, FoldComparison applies as long as the
# evaluation would not cause an error.
[FoldComparison, Normalize]
(Comparison
    $left:* & (IsConstValueOrGroupOfConstValues $left)
    $right:* &
        (IsConstValueOrGroupOfConstValues $right) &
        (Let
            ($result $ok):(FoldComparison (OpName) $left $right)
            $ok
        )
)
=>
$result
```
- Attempts used: 7
- Last updated: 2026-09-26T01:36:25.220974+00:00
- Reason / notes: The encoding is a faithful, honestly-labeled (PARTIAL) special case of FoldComparison: it picks the single concrete instance `TRUE = FALSE → FALSE` in a filter, which is the narrowest case where QED can actually evaluate a typed comparison over literals (the DSL exposes only boolean literals, and QED can only fold a comparison when both operands and the operator are concrete enough for it to compute the result). The hard-coding of EQUALS/TRUE/FALSE is not an unnecessary under-generalization—replacing any of them with an uninterpreted symbol would make the equivalence unprovable—so the scope restriction is genuine, specific, and the before/after are structurally distinct (a `Pred(EQUALS, true, false)` vs. a bare `false`), making the proof non-vacuous.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 304334}, panicked=False

### `FoldComparisonWithAny` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldComparisonWithAny evaluates a comparison operation over a constant
and an ANY/SOME clause, replacing the entire expression with a constant.
It iterates over elements in the clause and tries to find constants that
make the comparison a definite value.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldComparisonWithAny`, not the other rules in that file):

```
# FoldComparisonWithAny evaluates a comparison operation over a constant
# and an ANY/SOME clause, replacing the entire expression with a constant.
# It iterates over elements in the clause and tries to find constants that
# make the comparison a definite value.
[FoldComparisonWithAny, Normalize]
(AnyScalar
    $left:* & (IsConstValueOrGroupOfConstValues $left)
    $right:* & (IsTuple $right)
    $cmp:* &
        (Let
            ($result $ok):(FoldComparisonWithAny
                $cmp
                $left
                $right
            )
            $ok
        )
)
=>
$result
```
- Attempts used: 41
- Last updated: 2026-09-26T01:55:02.119532+00:00
- Reason / notes: The proof is not vacuous: before() filters by OR(EQ(TRUE,FALSE), EQ(TRUE,TRUE)) while after() filters by the TRUE literal, so QED genuinely had to validate the constant-folding identity (the tautology supplied by TRUE = TRUE) over the shared scan — a correct relational embedding of a scalar rewrite. The encoding is an extreme special case (one concrete boolean left, one concrete two-element tuple, EQUALS only), but that narrowing is forced by the DSL/QED having no tuple/ANY/list construct and no non-boolean or NULL literals, and the SCOPE line states this restriction specifically and honestly. The ANY-as-OR expansion is exact for this non-empty, null-free tuple, so the declared partial instance is faithful and non-degenerate, not a structurally identical before/after pair. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 281333}, panicked=False

### `FoldEqFalse` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

FoldEqFalse replaces x = False with NOT x.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldEqFalse`, not the other rules in that file):

```
# FoldEqFalse replaces x = False with NOT x.
[FoldEqFalse, Normalize]
(Eq $left:* (False))
=>
(Not $left)
```
- Attempts used: 8
- Last updated: 2026-09-26T01:49:57.419524+00:00
- Reason / notes: The encoding faithfully mirrors `FoldEqFalse`: `before()` is `Filter(left = False, Source)` and `after()` is `Filter(NOT left, Source)`, so they differ structurally exactly in the way the real rule rewrites, with `left` a single uninterpreted predicate (`pred("left")`) standing in for the arbitrary `$left:*` expression — no constants, column choices, or operator shapes are hard-coded, and the same symbol is correctly shared on both sides. The source rule has no side conditions (it's valid under three-valued logic too: NULL = False and NOT NULL both yield NULL, which filters drop), and the filter-over-scan embedding is the standard faithful relational form of a boolean-condition rewrite, with QED's proof quantifying over all table contents and all instantiations of the uninterpreted symbol. SCOPE: FULL is honest — the uninterpreted predicate covers any boolean expression over the input row, which is the full generality of the scalar rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 329875}, panicked=False

### `FoldEqTrue` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

FoldEqTrue replaces x = True with x.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldEqTrue`, not the other rules in that file):

```
# FoldEqTrue replaces x = True with x.
[FoldEqTrue, Normalize]
(Eq $left:* (True))
=>
$left
```
- Attempts used: 22
- Last updated: 2026-09-26T01:57:27.164044+00:00
- Reason / notes: The encoding is non-vacuous (before is `Filter(EQUALS(left, true), Source)` vs after `Filter(left, Source)` — structurally distinct, forcing QED to actually prove the `b = true ≡ b` identity), faithful in its operator choices (concrete `EQUALS` and `trueLiteral` because the rule specifically rewrites `Eq ... (True)`, with the left operand correctly left as an uninterpreted predicate over the row to match `$left:*`), and has no shared-symbol or missing-precondition issues: the same single `left` and `Source` symbols appear on both sides exactly as the rule requires, the source rule has no side conditions, and `x = True` ≡ `x` holds even under 3VL/NULL semantics, so `// SCOPE: FULL` is honest (filter-hosting a scalar boolean law over an arbitrary relation is the standard full encoding for this DSL, as in the FilterMerge example).
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 342667}, panicked=False

### `FoldGroupByAndWindow` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

FoldGroupByAndWindow merges a GroupBy operator with an input Window operator.
This is possible when the following conditions are satisfied:

1. The GroupBy is unordered. This may not technically be necessary, but
avoids complication in determining the correctness of ordering-sensitive
aggregations.

2. The window function output cols are functionally determined by the
partition-by cols. This means that the window function outputs the
same value for every row in the partition (group).

3. The Window operator partition-by cols and grouping cols are the same.
This ensures that an aggregate operator will act on the same set of rows,
whether it is part of the Window operator or the GroupBy operator.

4. The window functions are all aggregate functions. This ensures they are
compatible with GroupBy operators.

5. Finally, all of the GroupBy's aggregations must satisfy one of two cases:
a. The aggregate only references cols from the Window operator's input.
b. The aggregate is a ConstAgg (or ConstNotNull, AnyNotNull, or FirstAgg)
that passes through the result of a window function.

Assuming all of the above are satisfied, each GroupBy aggregate that only
references the Window's input can be left alone (5a). Then, each ConstAgg
referencing a window function can be replaced by that function (5b).

Here's an example with slightly altered SQL syntax:

SELECT max(b), const_agg(foo), const_agg(bar)
FROM
(
SELECT *, count(c) OVER w AS foo, array_agg(d) OVER w AS bar
FROM abcd
WINDOW w AS (
PARTITION BY a ORDER BY d
RANGE BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
)
)
GROUP BY a;
=>
SELECT max(b), count(c), array_agg(d ORDER BY d) FROM abcd GROUP BY a;

Note also that the Window's ordering should be preserved by the GroupBy to
ensure that ordering-sensitive aggregates produce correct results.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `FoldGroupByAndWindow`, not the other rules in that file):

```
# FoldGroupByAndWindow merges a GroupBy operator with an input Window operator.
# This is possible when the following conditions are satisfied:
#
#   1. The GroupBy is unordered. This may not technically be necessary, but
#      avoids complication in determining the correctness of ordering-sensitive
#      aggregations.
#
#   2. The window function output cols are functionally determined by the
#      partition-by cols. This means that the window function outputs the
#      same value for every row in the partition (group).
#
#   3. The Window operator partition-by cols and grouping cols are the same.
#      This ensures that an aggregate operator will act on the same set of rows,
#      whether it is part of the Window operator or the GroupBy operator.
#
#   4. The window functions are all aggregate functions. This ensures they are
#      compatible with GroupBy operators.
#
#   5. Finally, all of the GroupBy's aggregations must satisfy one of two cases:
#      a. The aggregate only references cols from the Window operator's input.
#      b. The aggregate is a ConstAgg (or ConstNotNull, AnyNotNull, or FirstAgg)
#         that passes through the result of a window function.
#
# Assuming all of the above are satisfied, each GroupBy aggregate that only
# references the Window's input can be left alone (5a). Then, each ConstAgg
# referencing a window function can be replaced by that function (5b).
#
# Here's an example with slightly altered SQL syntax:
#
#   SELECT max(b), const_agg(foo), const_agg(bar)
#   FROM
#     (
#       SELECT *, count(c) OVER w AS foo, array_agg(d) OVER w AS bar
#       FROM abcd
#       WINDOW w AS (
#         PARTITION BY a ORDER BY d
#         RANGE BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
#       )
#     )
#   GROUP BY a;
#   =>
#   SELECT max(b), count(c), array_agg(d ORDER BY d) FROM abcd GROUP BY a;
#
# Note also that the Window's ordering should be preserved by the GroupBy to
# ensure that ordering-sensitive aggregates produce correct results.
[FoldGroupByAndWindow, Normalize]
(GroupBy | ScalarGroupBy
    $window:(Window
            $input:*
            $windows:* & (WindowsAreAggregations $windows)
            $windowPrivate:*
        ) &
        (ColsAreDeterminedBy
            (WindowFuncOutputCols $windows)
            $partitionByCols:(WindowPartition $windowPrivate)
            $window
        )
    $aggs:* &
        (CanMergeAggsAndWindow
            $aggs
            $windows
            $inputCols:(OutputCols $input)
        )
    $groupingPrivate:* &
        (IsUnorderedGrouping $groupingPrivate) &
        (ColsAreEqual
            $groupingCols:(GroupingCols $groupingPrivate)
            $partitionByCols
        )
)
=>
((OpName)
    $input
    (MergeAggsAndWindow $aggs $windows $inputCols)
    (MakeGrouping
        (GroupingCols $groupingPrivate)
        (WindowOrdering $windowPrivate)
    )
)
```
- Attempts used: 61
- Last updated: 2026-09-26T04:24:57.296354+00:00
- Reason / notes: The encoding faithfully captures the core logic of FoldGroupByAndWindow for case 5a: it models the Window as a per-partition aggregate joined back to the input (conditions 2–4), groups by the partition column (condition 3), and shows that a GroupBy aggregate referencing only window-input columns (case 5a) can be pushed below the eliminated window. The before/after are structurally distinct (Join present vs. absent), the symbol sharing is correct (same k/v types, distinct agg names "w"/"a"), the join fields and INNER kind are right, and the PARTIAL scope tag honestly documents the exclusion of case 5b (ConstAgg pass-through requiring per-aggregate algebra QED lacks).
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 110046458}, panicked=False

### `FoldGroupingOperators` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

FoldGroupingOperators folds two grouping operators into one equivalent
operator. As an example, the following pairs of queries are equivalent:

SELECT sum(t) FROM (SELECT sum(b) FROM ab GROUP BY a) AS g(t);
=>
SELECT sum(b) FROM ab;

SELECT max(t) FROM (SELECT max(b) FROM ab GROUP BY a) AS g(t);
=>
SELECT max(b) FROM ab;

SELECT sum_int(t) FROM (SELECT count(b) FROM ab GROUP BY a) AS g(t);
=>
SELECT count(b) FROM ab;

SELECT DISTINCT ON (x), x, y
FROM (SELECT DISTINCT ON (a, b) a, b FROM ab) AS f(x y)
=>
SELECT DISTINCT ON (a) a, b FROM ab;

This transformation is possible when the following conditions are met:

1. All of the outer aggregates either aggregate on:
A. the output columns of the inner aggregates
B. a grouping column of the inner grouping operator.
2. All of the inner-outer aggregate pairs can be replaced with an equivalent
single aggregate. (See the AggregatesCanMerge comment in operator.go).
3. All of the outer aggregates that aggregate on inner grouping columns ignore
duplicate values (See AggregateIgnoresDuplicates comment in operator.go).
4. The grouping columns of the inner operator functionally determine the
grouping columns of the outer operator according to the functional
dependencies of the input of the inner operator.
5. Both grouping operators are unordered.

Why is it sufficient for the inner grouping columns to functionally determine
the outer grouping columns?
* Duplicate values in the determinant ("from" side) imply duplicate values in
the dependent ("to" side).
* Grouping on the determinant will not remove unique values from the
determinant. Therefore, the grouping will not remove unique values from the
dependent, by the properties of functional dependencies.
* Grouping on the dependent will simply reduce the dependent to its unique
values.
* Therefore, grouping on the dependent produces the same final groups as
grouping on the dependent after grouping on the determinant.
* The conditions guarantee that the aggregates produce the same result
regardless of how the grouping is accomplished, as long as the same groups
result in the end.

Take the following table as an example:

r a b
-----
1 4 3
2 4 3
3 2 3
4 2 3
5 6 5
6 6 5

Its functional dependencies: key(r), r-->(a, b), a-->(b)

Here are some examples of possible groupings taking the sum over the "r"
column:

Grouping by a: SUM(1, 2), SUM(3, 4), SUM(5, 6)
Grouping by b: SUM(1, 2, 3, 4), SUM(5, 6)
Grouping by a then b: SUM(SUM(1, 2), SUM(3, 4)), SUM(SUM(5, 6))

Rows can always be grouped together by subsequent groupings, but they can
never be "ungrouped". Grouping on a does not group any rows together that
would not also be grouped by b.

This situation is rare in direct SQL queries, but can arise when composing
views and queries.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `FoldGroupingOperators`, not the other rules in that file):

```
# FoldGroupingOperators folds two grouping operators into one equivalent
# operator. As an example, the following pairs of queries are equivalent:
#
#   SELECT sum(t) FROM (SELECT sum(b) FROM ab GROUP BY a) AS g(t);
#   =>
#   SELECT sum(b) FROM ab;
#
#   SELECT max(t) FROM (SELECT max(b) FROM ab GROUP BY a) AS g(t);
#   =>
#   SELECT max(b) FROM ab;
#
#   SELECT sum_int(t) FROM (SELECT count(b) FROM ab GROUP BY a) AS g(t);
#   =>
#   SELECT count(b) FROM ab;
#
#   SELECT DISTINCT ON (x), x, y
#   FROM (SELECT DISTINCT ON (a, b) a, b FROM ab) AS f(x y)
#   =>
#   SELECT DISTINCT ON (a) a, b FROM ab;
#
# This transformation is possible when the following conditions are met:
#
# 1. All of the outer aggregates either aggregate on:
#      A. the output columns of the inner aggregates
#      B. a grouping column of the inner grouping operator.
# 2. All of the inner-outer aggregate pairs can be replaced with an equivalent
#    single aggregate. (See the AggregatesCanMerge comment in operator.go).
# 3. All of the outer aggregates that aggregate on inner grouping columns ignore
#    duplicate values (See AggregateIgnoresDuplicates comment in operator.go).
# 4. The grouping columns of the inner operator functionally determine the
#    grouping columns of the outer operator according to the functional
#    dependencies of the input of the inner operator.
# 5. Both grouping operators are unordered.
#
# Why is it sufficient for the inner grouping columns to functionally determine
# the outer grouping columns?
# * Duplicate values in the determinant ("from" side) imply duplicate values in
#   the dependent ("to" side).
# * Grouping on the determinant will not remove unique values from the
#   determinant. Therefore, the grouping will not remove unique values from the
#   dependent, by the properties of functional dependencies.
# * Grouping on the dependent will simply reduce the dependent to its unique
#   values.
# * Therefore, grouping on the dependent produces the same final groups as
#   grouping on the dependent after grouping on the determinant.
# * The conditions guarantee that the aggregates produce the same result
#   regardless of how the grouping is accomplished, as long as the same groups
#   result in the end.
#
# Take the following table as an example:
#
#   r a b
#   -----
#   1 4 3
#   2 4 3
#   3 2 3
#   4 2 3
#   5 6 5
#   6 6 5
#
# Its functional dependencies: key(r), r-->(a, b), a-->(b)
#
# Here are some examples of possible groupings taking the sum over the "r"
# column:
#
# Grouping by a: SUM(1, 2), SUM(3, 4), SUM(5, 6)
# Grouping by b: SUM(1, 2, 3, 4), SUM(5, 6)
# Grouping by a then b: SUM(SUM(1, 2), SUM(3, 4)), SUM(SUM(5, 6))
#
# Rows can always be grouped together by subsequent groupings, but they can
# never be "ungrouped". Grouping on a does not group any rows together that
# would not also be grouped by b.
#
# This situation is rare in direct SQL queries, but can arise when composing
# views and queries.
[FoldGroupingOperators, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn
    (GroupBy | DistinctOn
        $innerInput:*
        $innerAggs:*
        $innerGrouping:* & (IsUnorderedGrouping $innerGrouping)
    )
    $outerAggs:*
    $outerGrouping:* &
        (IsUnorderedGrouping $outerGrouping) &
        (ColsAreDeterminedBy
            $outerGroupingCols:(GroupingCols $outerGrouping)
            $innerGroupingCols:(GroupingCols $innerGrouping)
            $innerInput
        ) &
        (CanMergeAggs $innerAggs $outerAggs $innerGroupingCols)
)
=>
((OpName)
    $innerInput
    (MergeAggs $innerAggs $outerAggs $innerGroupingCols)
    (MakeGrouping $outerGroupingCols (EmptyOrdering))
)
```
- Attempts used: 27
- Last updated: 2026-09-26T03:14:22.686771+00:00
- Reason / notes: The encoding is a faithful, non-degenerate special case: before() is a genuine nested Aggregate(Aggregate) that folds to a single Aggregate (the proof is non-vacuous), the symbol sharing is correct and in fact necessary (same source scan, same uninterpreted "k1" projection on both sides, and the outer keys on inner.field(0) — the inner's group-key value — realizing the rule's identity-FD instance of ColsAreDeterminedBy), and with zero outer aggregate calls the CanMergeAggs and unordered-grouping preconditions hold vacuously, so no unmodeled aggregate-merging algebra is needed (the uninterpreted inner aggregate "v" never reaches the output, which is why QED can decide it). The SCOPE: PARTIAL line accurately states the narrowing (outer has no aggregates; outer group key = inner group key), and the fold still does real work — eliminating an entire aggregation level plus its aggregate call, exactly what the full rule's MergeAggs yields for an empty outer-aggs instance. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 74781667}, panicked=False

### `FoldIndirection` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldIndirection eliminates a constant array indirection operator applied to an
array with a statically known number of elements, like this:

ARRAY[i, i+1][1]
ARRAY[1, 2, 3][2]

The rule replaces the indirection operator with the referenced array element.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldIndirection`, not the other rules in that file):

```
# FoldIndirection eliminates a constant array indirection operator applied to an
# array with a statically known number of elements, like this:
#
#   ARRAY[i, i+1][1]
#   ARRAY[1, 2, 3][2]
#
# The rule replaces the indirection operator with the referenced array element.
[FoldIndirection, Normalize]
(Indirection
    $input:*
    $index:* &
        (IsConstValueOrGroupOfConstValues $index) &
        (Let ($result $ok):(FoldIndirection $input $index) $ok)
)
=>
$result
```
- Attempts used: 6
- Last updated: 2026-09-26T04:04:44.444577+00:00
- Reason / notes: The proof is non-vacuous — before() is a nested two-column projection plus a field access while after() is a single direct projection, so QED genuinely had to prove that a constant-ordinal field access through a freshly built row composes to the referenced element expression (with correct symbol sharing: distinct uninterpreted e0/e1, and e1 consistently reused as the referenced element), which is exactly FoldIndirection's essence of replacing a constant-index access on a statically constructed composite with the element itself. The narrowing to a 2-element array with a fixed in-range index is forced by the DSL (field ordinals are concrete ints and QED's JSON format has no symbolic index operator, so no extension could express "any constant index"), not an avoidable hard-coding, and is precisely and specifically disclosed in the SCOPE: PARTIAL line. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 358042}, panicked=False

### `FoldIsNotNull` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

FoldIsNotNull replaces NULL IS NOT NULL with False.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldIsNotNull`, not the other rules in that file):

```
# FoldIsNotNull replaces NULL IS NOT NULL with False.
[FoldIsNotNull, Normalize]
(IsNot (Null) (Null))
=>
(False)
```
- Attempts used: 29
- Last updated: 2026-09-26T05:07:46.418751+00:00
- Reason / notes: The encoding is faithful and non-vacuous: `before()` filters on `NULL IS NOT NULL` (a genuine null literal under the real `IS_NOT_NULL` operator, the correct image of Optgen's binary `IsNot (Null) (Null)`) and `after()` filters on a `false` literal — structurally different patterns whose equivalence holds exactly because `NULL IS NOT NULL` evaluates to false, which is the entire content of the rule. There are no free variables or side conditions in the source rule to miss, the null literal's INTEGER type is irrelevant to IS NOT NULL semantics (and virtual types erase to integer in the prover anyway), and the input relation is left uninterpreted, so there is no under-generalization or missing precondition. The filter position is the canonical relational embedding of a scalar boolean constant fold, and since the condition doesn't reference the input, the proved statement (σ_{NULL IS NOT NULL}(R) = σ_false(R) for all R) is equivalent to the scalar claim itself — so `SCOPE: FULL` is honest, not a narrowed special case. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 334750}, panicked=False

### `FoldIsNull` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

FoldIsNull replaces NULL IS NULL with True.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldIsNull`, not the other rules in that file):

```
# FoldIsNull replaces NULL IS NULL with True.
[FoldIsNull, Normalize]
(Is (Null) (Null))
=>
(True)
```
- Attempts used: 21
- Last updated: 2026-09-26T05:27:25.226931+00:00
- Reason / notes: The source rule is the scalar constant-fold `NULL IS NULL => True`, a context-free boolean identity, and the porter embedded it in the canonical relational home for a boolean predicate (a filter), proving `Filter(NULL IS NULL, S) = Filter(TRUE, S)` over a fully uninterpreted scan `S`. Because the identity is context-free, this establishes the rule's complete logical content with no assumptions: CockroachDB's two-arg `Is(Null,Null)` is correctly rendered as unary `IS_NULL(NULL)`, the result as `trueLiteral()`, `before()`/`after()` are genuinely distinct (so the proof is non-vacuous and requires QED to recognize `IS_NULL(NULL)` as true), and there are no uninterpreted symbols to under-generalize, no missing preconditions, and no spurious relational narrowing — so `SCOPE: FULL` is honest. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 83708}, panicked=False

### `FoldIsNullProject` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/project.opt

FoldIsNullProject folds "x IS NULL" projections to false if "x" is not null in
the Project's input. It matches if there is at least one projection that can
be folded, and it replaces all projections that can be folded.

Extracted from `project.opt` (which defines multiple rules — implement specifically `FoldIsNullProject`, not the other rules in that file):

```
# FoldIsNullProject folds "x IS NULL" projections to false if "x" is not null in
# the Project's input. It matches if there is at least one projection that can
# be folded, and it replaces all projections that can be folded.
[FoldIsNullProject, Normalize]
(Project
    $input:*
    $projections:[
            ...
            $item:(ProjectionsItem (Is (Variable $col:*) (Null)))
            ...
        ] &
        (IsColNotNull $col $input)
    $passthrough:*
)
=>
(Project
    $input
    (FoldIsNullProjectionsItems $projections $input)
    $passthrough
)
```
- Attempts used: 28
- Last updated: 2026-09-26T05:27:39.512964+00:00
- Reason / notes: The encoding faithfully captures the rule's core — a Project item `x IS NULL` over an input column x guaranteed NOT NULL (encoded via C_Type's nullable=false, exactly mirroring `IsColNotNull`) is folded to the `false` literal while other columns pass through unchanged — and the proof is non-vacuous, since `before()` and `after()` are structurally distinct and the equivalence genuinely depends on the NOT NULL guarantee (flipping it would make the claim false), so the precondition is present, not silently dropped. The PARTIAL scope line is accurate: the DSL cannot quantify over an arbitrary non-empty set of foldable projections, so fixing one IS NULL projection plus one passthrough column is a genuine, non-degenerate minimal instance of the Optgen rule, with correct operators, no over-constrained symbol sharing, and no hardcoded symbol where an uninterpreted one was available.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 697000}, panicked=False

### `FoldNeFalse` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

FoldNeFalse replaces x != False with x.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNeFalse`, not the other rules in that file):

```
# FoldNeFalse replaces x != False with x.
[FoldNeFalse, Normalize]
(Ne $left:* (False))
=>
$left
```
- Attempts used: 5
- Last updated: 2026-09-26T05:46:51.937793+00:00
- Reason / notes: The encoding is faithful: `before()` filters on the concrete `NOT_EQUALS(left, false)` while `after()` filters on `left` alone, so the structures genuinely differ and QED's proof is of the real boolean identity `x <> false ≡ x` (valid under both two-valued and SQL three-valued null logic, matching the Optgen rule which has no side conditions). The single uninterpreted predicate `left` is the correct stand-in for Optgen's `$left:*` (any boolean expression over the row, reused on both sides exactly as the rule requires), and the right operand is the concrete `False` literal, matching the rule's exact match on `(False)` rather than over-generalizing to an arbitrary boolean. The only encoding choices are the relational anchor (a `Filter`) and a single-column carrier scan, neither of which restricts the universally quantified identity, so `// SCOPE: FULL` is honest. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 414500}, panicked=False

### `FoldNeTrue` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

FoldNeTrue replaces x != True with NOT x.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNeTrue`, not the other rules in that file):

```
# FoldNeTrue replaces x != True with NOT x.
[FoldNeTrue, Normalize]
(Ne $left:* (True))
=>
(Not $left)
```
- Attempts used: 3
- Last updated: 2026-09-26T05:47:47.094234+00:00
- Reason / notes: The encoding faithfully lifts the scalar rule `(Ne $left:* (True)) => (Not $left)` into `Filter(scan, NOT_EQUALS(left, TRUE)) => Filter(scan, NOT(left))`, modeling the arbitrary boolean `$left` as a single uninterpreted *nullable* predicate — since it is uninterpreted it stands for any boolean expression, so the one-column scan is a grounding vehicle, not a real restriction on generality. The operators map exactly (NOT_EQUALS↔Ne, trueLiteral↔True, Not↔Not) with the same `left` symbol shared across both sides, and the identity `x <> TRUE ≡ NOT x` holds under three-valued logic for every case (TRUE, FALSE, NULL), so no precondition (e.g. NOT NULL) is silently assumed — the structurally distinct before/after makes the proof non-vacuous and the FULL scope tag is honest. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 103583}, panicked=False

### `FoldNonNullIsNotNull` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

FoldNonNullIsNotNull replaces x IS NOT NULL with True where x is a non-Null constant.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNonNullIsNotNull`, not the other rules in that file):

```
# FoldNonNullIsNotNull replaces x IS NOT NULL with True where x is a non-Null constant.
[FoldNonNullIsNotNull, Normalize]
(IsNot $left:(IsNeverNull $left) (Null))
=>
(True)
```
- Attempts used: 27
- Last updated: 2026-09-26T05:59:55.141650+00:00
- Reason / notes: The encoding is a faithful relational lifting of the scalar rule `IsNot $left (Null) => True` under `IsNeverNull $left`: `before()` is `Filter(x IS NOT NULL, S)` and `after()` is `Filter(TRUE, S)` over an uninterpreted scan whose column is declared non-nullable — which is exactly the source rule's side condition, since "never null" (the property that makes the fold valid, whether x is a constant or not) is expressed in this DSL precisely as a non-nullable column type, and no concrete type, predicate, or constant is baked in. The two sides are structurally and semantically distinct in general (without the nullability annotation the equivalence would fail), so the fast, clean proof is non-vacuous — it can only go through by using the encoded `IsNeverNull` guarantee, i.e., it proves exactly the rule's content. SCOPE: FULL is honest: the only constraints in the encoding (argument is a top-level column, the expression is the whole filter condition) are the canonical embedding of a scalar rewrite in RuleScript (as in the FilterMerge example), not semantic assumptions beyond the original rule, and the result is a genuine, useful no-op-filter elimination rather than a degenerate one. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 405583}, panicked=False

### `FoldNonNullIsNull` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

FoldNonNullIsNull replaces x IS NULL with False where x is a non-Null constant.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNonNullIsNull`, not the other rules in that file):

```
# FoldNonNullIsNull replaces x IS NULL with False where x is a non-Null constant.
[FoldNonNullIsNull, Normalize]
(Is $left:(IsNeverNull $left) (Null))
=>
(False)
```
- Attempts used: 25
- Last updated: 2026-09-26T06:21:03.226020+00:00
- Reason / notes: `before()` (filter on `x IS NULL`) and `after()` (filter on the `False` literal) are structurally different, and the equivalence can hold only because the single uninterpreted source column is declared non-nullable — exactly the source rule's `IsNeverNull` side condition, expressed via the DSL's only mechanism for "never null" (a non-nullable `VarType`), so the proof is of the real claim, not a vacuous one. The table (`Source`) and value domain (`X_Type`) remain uninterpreted, the `IS_NULL`/`False` operators match `Is … (Null) => (False)`, and the shared scan symbol is correct, so the encoding is the full relational closure of the scalar rule and the `// SCOPE: FULL` tag is honest.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 373333}, panicked=False

### `FoldNonNullTupleIsTupleNotNull` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

FoldNonNullTupleIsTupleNotNull replaces x IS NOT NULL with True if x is a
tuple with only constant, non-null elements.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNonNullTupleIsTupleNotNull`, not the other rules in that file):

```
# FoldNonNullTupleIsTupleNotNull replaces x IS NOT NULL with True if x is a
# tuple with only constant, non-null elements.
[FoldNonNullTupleIsTupleNotNull, Normalize]
(IsTupleNotNull $input:(Tuple) & (HasAllNonNullElements $input))
=>
(True)
```
- Attempts used: 45
- Last updated: 2026-09-26T07:10:22.219369+00:00
- Reason / notes: The encoding is non-vacuous — `before()` filters on `(f0 IS NOT NULL) AND (f1 IS NOT NULL)` while `after()` filters on `true`, and the proof genuinely relies on QED's nullability modeling of the two `nullable=false` columns (the semantic content of HasAllNonNullElements), correctly expanding `IsTupleNotNull(x)` as the conjunction of its elements' `IS NOT NULL` predicates folding to `True`. Model
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 100959}, panicked=False

### `FoldNonNullTupleIsTupleNull` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

FoldNonNullTupleIsTupleNull replaces x IS NULL with False if x is a tuple
with at least one constant, non-null element.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNonNullTupleIsTupleNull`, not the other rules in that file):

```
# FoldNonNullTupleIsTupleNull replaces x IS NULL with False if x is a tuple
# with at least one constant, non-null element.
[FoldNonNullTupleIsTupleNull, Normalize]
(IsTupleNull $input:(Tuple) & (HasNonNullElement $input))
=>
(False)
```
- Attempts used: 3
- Last updated: 2026-09-26T06:59:58.869362+00:00
- Reason / notes: The encoding correctly relationalizes the scalar fold: before() is Filter(AND(e0 IS NULL, e1 IS NULL), source) — the exact semantics of IsTupleNull expanded over the tuple's elements — and after() is Filter(False, source), a non-vacuous proof that genuinely requires QED to use the non-nullable type of element 0 (had it ignored nullability, a (null, null) row would be a countermodel). The non-nullable VarType is the faithful stand-in for the side condition (HasNonNullElement): a non-null constant element implies a non-nullable column, so the proven family subsumes the rule's applicable instances with no missing precondition, while element 1 is left fully arbitrary (any value, nullable), exactly as the rule requires. The only restrictions are tuple arity 2 and the non-null element being at position 0, which are inherent to a single fixed-arity record (you cannot express "at least one of N positions" in one rule, and marking both columns non-nullable would over-restrict) and are honestly and specifically disclosed in the PARTIAL scope line. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 342875}, panicked=False

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

### `FoldNotInEmpty` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNotInEmpty replaces the NotIn with True when the right input is empty.
Note that this is correct even if the left side is Null, since even an unknown
value can't be in an empty set.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNotInEmpty`, not the other rules in that file):

```
# FoldNotInEmpty replaces the NotIn with True when the right input is empty.
# Note that this is correct even if the left side is Null, since even an unknown
# value can't be in an empty set.
[FoldNotInEmpty, Normalize]
(NotIn * (Tuple []))
=>
(True)
```
- Attempts used: 22
- Last updated: 2026-09-26T06:33:46.353120+00:00
- Reason / notes: The encoding is non-vacuous — before() is an ANTI join of L against a genuine zero-row relation and after() is the bare scan L — and the proved identity (anti-join with the empty set is the identity, for every uninterpreted condition, key, and type) is exactly the relational reading of "x NOT IN () ≡ TRUE, so the node disappears," including the NULL-left case the source comment explicitly calls out. ANTI is the correct join kind for NotIn (a semi-join would model IN), `(Tuple [])` is correctly a zero-row `LogicalValues` (not a Filter(False)), and all backend-specific elements (L_Type, R_Type, cond) are uninterpreted with no missing preconditions since the source rule has none. The PARTIAL tag is honest and specific: the DSL has no scalar In/NotIn operator or list constant, so the scalar rule is embedded in its relational filter/join context — a genuine, non-degenerate special case that remains a useful plan simplification. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 60533417}, panicked=False

### `FoldNotNull` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

FoldNotNull replaces NOT(Null) with Null.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `FoldNotNull`, not the other rules in that file):

```
# FoldNotNull replaces NOT(Null) with Null.
[FoldNotNull, Normalize]
(Not (Null))
=>
(Null (BoolType))
```
- Attempts used: 10
- Last updated: 2026-09-26T07:05:34.795023+00:00
- Reason / notes: The encoding faithfully captures CockroachDB's FoldNotNull rule (`(Not (Null)) => (Null (BoolType))`) by embedding the expression-level boolean normalization in a filter context: `Filter(Not(NULL), R)` before and `Filter(NULL), R)` after, with a properly-typed BOOLEAN null literal matching the source's `(Null (BoolType))`. The rule has no free variables, no preconditions, and no join-type or other parameters, so the scan is purely a relational carrier and SCOPE: FULL is accurate; the structural difference (presence vs. absence of the `Not` wrapper) is exactly the rewrite the source rule specifies, making the proof non-vacuous. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 80083}, panicked=False

### `FoldNotTrue` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

FoldNotTrue replaces NOT(True) with False.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `FoldNotTrue`, not the other rules in that file):

```
# FoldNotTrue replaces NOT(True) with False.
[FoldNotTrue, Normalize]
(Not (True))
=>
(False)
```
- Attempts used: 3
- Last updated: 2026-09-26T07:05:05.167974+00:00
- Reason / notes: before() (Filter with condition NOT(TRUE)) and after() (Filter with condition FALSE) are structurally distinct, and the transformation is exactly the source rule's rewrite, so the proof is non-vacuous. The source rule has no free variables — it is a pure constant fold — so nothing that should be uninterpreted was hard-coded: the scan is fully uninterpreted (arbitrary bag of arbitrary type), the proof covers every relation, and no preconditions or null assumptions are involved since NOT(TRUE)=FALSE holds unconditionally. As a scalar constant-fold rule embedded in a relational DSL that has no expression metavariables, placing the matched expression as the entire filter condition over an arbitrary source is its maximal relational form, so SCOPE: FULL is honest and the encoding is faithful and non-degenerate. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 346250}, panicked=False

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

### `EliminateCaseTrailingFalsyBranch` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

EliminateCaseTrailingFalsyBranch removes a trailing WHEN branch that results
in False or Null from a CASE expression in a filter when the ELSE branch also
results in False or Null. This is valid because the trailing WHEN branch
cannot change the result of the CASE expression, regardless of the results of
its condition.

For example:

SELECT * FROM abc WHERE CASE WHEN a > 1 THEN true WHEN b > 2 THEN false ELSE false END
=>
SELECT * FROM abc WHERE CASE WHEN a > 1 THEN true ELSE false END

NOTE: The optimizer guarantees that CASE branches with side-effects will only
be evaluated if their conditions are true and the ELSE branch with
side-effects will only be evaluated if non of the branch conditions are true
(see props.VolatilitySet). Therefore, this rule is only valid when the ELSE
branch is leakproof. We currently only match constant False and Null values,
which are guaranteed to be leakproof.

Extracted from `select.opt` (which defines multiple rules — implement specifically `EliminateCaseTrailingFalsyBranch`, not the other rules in that file):

```
# EliminateCaseTrailingFalsyBranch removes a trailing WHEN branch that results
# in False or Null from a CASE expression in a filter when the ELSE branch also
# results in False or Null. This is valid because the trailing WHEN branch
# cannot change the result of the CASE expression, regardless of the results of
# its condition.
#
# For example:
#
#  SELECT * FROM abc WHERE CASE WHEN a > 1 THEN true WHEN b > 2 THEN false ELSE false END
#  =>
#  SELECT * FROM abc WHERE CASE WHEN a > 1 THEN true ELSE false END
#
# NOTE: The optimizer guarantees that CASE branches with side-effects will only
# be evaluated if their conditions are true and the ELSE branch with
# side-effects will only be evaluated if non of the branch conditions are true
# (see props.VolatilitySet). Therefore, this rule is only valid when the ELSE
# branch is leakproof. We currently only match constant False and Null values,
# which are guaranteed to be leakproof.
[EliminateCaseTrailingFalsyBranch, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Case
                (True)
                $whens:[ ... (When * (False | Null)) ] &
                    (LenGT $whens 1)
                $orElse:(False | Null)
            )
        )
        ...
    ]
)
=>
(Select
    $input
    (ReplaceFiltersItem
        $filters
        $item
        (Case (True) (DropLast $whens) $orElse)
    )
)
```
- Attempts used: 65
- Last updated: 2026-09-25T19:30:43.792686+00:00
- Reason / notes: The rule's soundness rests entirely on CASE's own 3-valued if-then-else semantics (a trailing WHEN with a False/Null THEN is dead only because the ELSE is likewise False/Null in a filter context), but CASE is not in QED's modeled scalar fragment (And/Or/Not, comparisons, In/Some) — the paper's ite is only an internal encoding device for 3VL and NULL lifting, not an operator a pattern can name, so the two CASEs serialize as uninterpreted QOp applications of different arity (2N+1 vs 2N−1 arguments) that SMT congruence can never equate. Extending the Java DSL with a CASE builder cannot close this, because JSONSerializer would only emit a generic named call and the immutable Rust prover has no CASE interpretation to apply — this is squarely the "operator-specific internal semantics QED cannot see through as an uninterpreted function" limitation. No faithful narrower special case (e.g. exactly two whens, False-only branches) escapes it, since both pattern sides still contain the unmodeled CASE term, and any encoding that abstracts the CASEs away to shared symbols would prove a vacuous identity or a different rule rather than this one. ```

### `EliminateCast` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateCast discards a cast if its input already has a type that's identical
to the desired static type.

Note that CastExpr removes unnecessary casts during type-checking; this rule
can still be helpful if some other rule creates an unnecessary CastExpr.

EliminateCast is marked as high-priority so that it matches before FoldCast.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateCast`, not the other rules in that file):

```
# EliminateCast discards a cast if its input already has a type that's identical
# to the desired static type.
#
# Note that CastExpr removes unnecessary casts during type-checking; this rule
# can still be helpful if some other rule creates an unnecessary CastExpr.
#
# EliminateCast is marked as high-priority so that it matches before FoldCast.
[EliminateCast, Normalize, HighPriority]
(Cast $input:* $targetTyp:* & (HasColType $input $targetTyp))
=>
$input
```
- Attempts used: 24
- Last updated: 2026-09-25T18:49:08.074264+00:00
- Reason / notes: EliminateCast's soundness rests entirely on the cast operator's own value semantics — that a cast is value-identical to its input when the input type equals the target type — guarded by the type-level side condition `HasColType`, which the pattern language cannot express (type names are inert labels, all lowered to integers in the prover, and no predicate can assert type equality between symbols). Any faithful encoding therefore reduces to proving `cast(x) ≡ x` for an uninterpreted scalar function, which QED's SMT translation cannot justify; adding a cast builder via `extend_dsl_file` would not help because the unmodifiable prover interprets all scalar functions as uninterpreted regardless. The blocker is on the trusted-prover side, so no faithful, non-vacuous encoding exists — the only "provable" version (dropping the cast entirely) is the vacuous tautology, not the rule. ```

### `EliminateCoalesce` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateCoalesce discards the Coalesce operator if it has a single operand.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateCoalesce`, not the other rules in that file):

```
# EliminateCoalesce discards the Coalesce operator if it has a single operand.
[EliminateCoalesce, Normalize]
(Coalesce [ $item:* ])
=>
$item
```
- Attempts used: 128
- Last updated: 2026-09-25T21:06:47.571001+00:00
- Reason / notes: Manually finalized by Claude after reviewing the automated run's own verifier rejection (the porter's last candidate called a nonexistent RexRN.Coalesce(...) — hallucinated, RexRN.java has no such construct — so its self-reported 'PROVABLE' was spurious). Independently confirmed by grepping the entire DSL and qed-prover core: 'coalesce' does not appear anywhere (unlike e.g. COUNT, which turned out to be a real hidden built-in). EliminateCoalesce's soundness rests on COALESCE's own single-argument identity semantics (COALESCE(e) == e), which is operator-internal algebra QED has no mechanism to reason about via uninterpreted symbols. The only way to make it 'provable' would be to define single-arg COALESCE as the identity function inside the DSL itself, which would make before() and after() structurally identical — a vacuous triviality, not a genuine proof. Genuinely outside QED's supported fragment.

### `EliminateConstValueSubquery` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateConstValueSubquery replaces a subquery with a constant value if the
subquery's input is a single-row, single-column Values expression with a
constant value.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateConstValueSubquery`, not the other rules in that file):

```
# EliminateConstValueSubquery replaces a subquery with a constant value if the
# subquery's input is a single-row, single-column Values expression with a
# constant value.
[EliminateConstValueSubquery, Normalize]
(Subquery (Values [ (Tuple [ $value:(Const) ]) ]))
=>
$value
```
- Attempts used: 24
- Last updated: 2026-09-25T19:06:01.566654+00:00
- Reason / notes: The rule's LHS is a scalar subquery wrapping a single-row constant VALUES, and QED's prover cannot represent or reason about a scalar term that embeds a nested sub-relation (the scalar-subquery operator) — the porter confirmed this with a real experiment: a subquery builder was added, compiled, and serialized a genuine nested `query` node, yet the encoding still returned `provable=false` with `smt_duration=0` (fast-rejected before SMT), proving it is a prover limitation, not a closable DSL gap. Independently, the core language has no non-bool constant literal (needed for the RHS `$value`) and no non-empty VALUES builder (needed for the LHS single-row constant), so the rule can't even be written down. Taken together this is a genuine QED/DSL boundary (bespoke scalar-subquery semantics + scalar constant folding), so no faithful encoding can be made provable. ```

### `EliminateDistinctNoColumns` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateDistinctNoColumns eliminates a distinct operator with no grouping
columns, replacing it with a projection and a LIMIT 1. For example:
SELECT DISTINCT ON (a) a, b FROM ab WHERE a=1
is equivalent to:
SELECT a, b FROM ab WHERE a=1 LIMIT 1

Note that this rule does not apply to EnsureDistinctOn or
EnsureUpsertDistinctOn, since they will raise an error if there are duplicate
rows.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateDistinctNoColumns`, not the other rules in that file):

```
# EliminateDistinctNoColumns eliminates a distinct operator with no grouping
# columns, replacing it with a projection and a LIMIT 1. For example:
#   SELECT DISTINCT ON (a) a, b FROM ab WHERE a=1
# is equivalent to:
#   SELECT a, b FROM ab WHERE a=1 LIMIT 1
#
# Note that this rule does not apply to EnsureDistinctOn or
# EnsureUpsertDistinctOn, since they will raise an error if there are duplicate
# rows.
[EliminateDistinctNoColumns, Normalize]
(DistinctOn | UpsertDistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* & (HasNoGroupingCols $groupingPrivate)
)
=>
(ConstructProjectionFromDistinctOn
    (Limit
        $input
        (IntConst (DInt 1))
        (GroupingInputOrdering $groupingPrivate)
    )
    (MakeEmptyColSet)
    $aggregations
)
```
- Attempts used: 24
- Last updated: 2026-09-25T19:24:59.692832+00:00
- Reason / notes: The rule rewrites a no-grouping-columns DistinctOn (which emits at most one row — the input's first row under GroupingInputOrdering) into an ordered `LIMIT 1` over the same input, so its correctness rests entirely on both sides selecting the identical row *by order*. QED has no bag semantics for `Sort`/`Limit` (at best an uninterpreted `QOp`, qed.pdf §6.2 "List semantic" failure category), and the before side's first-row-under-ordering selection is likewise unencodable in the bag core language — a keyless `Aggregate` would emit a row on empty input (wrong cardinality) and its uninterpreted output values could never be linked to the opaque `Limit`, so even the most plausible special case (unique-keyed input) still leaves an inseparable opaque operator in `after()`. Since the gap sits in QED's own semantics rather than a missing DSL builder, `extend_dsl_file` cannot close it; UNSUPPORTED is correct. ```

### `EliminateDistinctOnValues` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateDistinctOnValues eliminates a distinct operator that has a constant
input Values operator that is already distinct with respect to the grouping
columns. The Values operator may be the immediate input, or it may be wrapped
by Select, Project, LeftJoin, and/or other operators. These are common
patterns that are generated by the optbuilder's upsert construction code,
which must ensure the same row cannot be updated twice. See the comment for
UpsertDistinctOn for more detail on NullsAreDistinct behavior.

Note: Doesn't match EnsureDistinctOn because test cases were too difficult to
find. If a test case for EnsureDistinctOn is found, it should be added to the
match pattern.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateDistinctOnValues`, not the other rules in that file):

```
# EliminateDistinctOnValues eliminates a distinct operator that has a constant
# input Values operator that is already distinct with respect to the grouping
# columns. The Values operator may be the immediate input, or it may be wrapped
# by Select, Project, LeftJoin, and/or other operators. These are common
# patterns that are generated by the optbuilder's upsert construction code,
# which must ensure the same row cannot be updated twice. See the comment for
# UpsertDistinctOn for more detail on NullsAreDistinct behavior.
#
# Note: Doesn't match EnsureDistinctOn because test cases were too difficult to
# find. If a test case for EnsureDistinctOn is found, it should be added to the
# match pattern.
[EliminateDistinctOnValues, Normalize]
(DistinctOn | UpsertDistinctOn | EnsureUpsertDistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* &
        (AreValuesDistinct
            $input
            (GroupingCols $groupingPrivate)
            (NullsAreDistinct $groupingPrivate)
        )
)
=>
(ConstructProjectionFromDistinctOn
    $input
    (GroupingCols $groupingPrivate)
    $aggregations
)
```
- Attempts used: 51
- Last updated: 2026-09-25T21:14:34.964010+00:00
- Reason / notes: Investigated in depth a second time at the user's request ('it should be provable and mergeable, right? but with some tweaks'). Read ConstructProjectionFromDistinctOn/AreValuesDistinct in groupby_funcs.go: the real underlying identity is 'Aggregate(input, groupingCols, [FirstAgg(x_i) AS y_i]) == Project(input, groupingCols passthrough, [x_i AS y_i])', valid whenever each group has at most one row — the source rule's AreValuesDistinct guard is just one specific way CockroachDB statically proves that precondition (literal Values distinctness), not a requirement of the identity itself, so (unlike my first pass) this does NOT actually need a literal-Values DSL construct — it can be encoded via the same unique=true scan trick already used elsewhere (e.g. the already-proven EliminateDistinct, which is exactly this same identity with an EMPTY aggregation list). Tried extending that to the rule's actual differentiator — a real FirstAgg column, not just grouping-column passthrough — two ways: (1) aggregating a derived value column grouped by a unique key (provable=false, but SMT genuinely engaged — a real, not structural, rejection); (2) aggregating the same column being grouped on, closest in shape to EliminateDistinct (provable=false, rejected before SMT even ran). Root cause: EliminateDistinct's proof works via QED's squash(singleton)==squash mechanism, which only fires when BOTH sides stay Aggregate-shaped (toggling the distinct flag on the same operator) — this rule's actual rewrite converts Aggregate into a Project entirely, a bigger structural change QED has no general rule for: ordinary aggregate names (unlike COUNT, which has hardcoded interpreted semantics in qed-prover/src/pipeline/relation.rs) get no 'aggregate over a singleton group equals its argument' derivation at all. So: the trivial empty-aggregation instance IS provable, but it's structurally identical to the already-proven EliminateDistinct, not a distinct contribution; the rule's actual differentiator (a real FirstAgg/ConstAgg column) hits a genuine QED reasoning gap, not a fixable DSL omission. No DSL extension needed — none would help without QED itself gaining general aggregate-collapse-under-singleton-group reasoning.

### `EliminateEnsureDistinctNoColumns` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateEnsureDistinctNoColumns is similar to EliminateDistinctNoColumns,
except that Max1Row will raise an error if there are no grouping columns and
the input has more than one row. No grouping columns means there is at most
one group. And the Max1Row operator is needed to raise an error if that group
has more than one row, which is a requirement of the EnsureDistinct and
EnsureUpsertDistinct operators.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateEnsureDistinctNoColumns`, not the other rules in that file):

```
# EliminateEnsureDistinctNoColumns is similar to EliminateDistinctNoColumns,
# except that Max1Row will raise an error if there are no grouping columns and
# the input has more than one row. No grouping columns means there is at most
# one group. And the Max1Row operator is needed to raise an error if that group
# has more than one row, which is a requirement of the EnsureDistinct and
# EnsureUpsertDistinct operators.
[EliminateEnsureDistinctNoColumns, Normalize]
(EnsureDistinctOn | EnsureUpsertDistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* & (HasNoGroupingCols $groupingPrivate)
)
=>
(ConstructProjectionFromDistinctOn
    (Max1Row $input (ErrorOnDup $groupingPrivate))
    (MakeEmptyColSet)
    $aggregations
)
```
- Attempts used: 45
- Last updated: 2026-09-25T20:50:21.312290+00:00
- Reason / notes: Both sides of this rule are custom operators whose correctness rests on runtime error semantics — EnsureDistinctOn/EnsureUpsertDistinctOn with an empty grouping col-set and Max1Row(ErrorOnDup) each *raise an error* when the input has more than one row — and QED decides total bag equivalence with no notion of errors, no Max1Row/Limit-style cardinality-cap operator in the core language, and no uninterpreted relational operator to stand in for either side. The only near-candidate encoding, a keyless group-by, diverges on empty input (it always yields one row, Max1Row yields zero), and the language offers no way to constrain an uninterpreted input to at most one row (scan `unique`/key marks column uniqueness, not row count), so the error condition that is the entire point of the "Ensure" variant can neither be expressed nor proven, even as a special case. ```

### `EliminateExistsLimit` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateExistsLimit discards a Limit operator with a positive limit inside an
Exist operator.

The Limit operator prevents decorrelation rules from being applied. By
discarding the Limit, which is a no-op inside of Exist operators, the query
can be decorrelated into a more efficient SemiJoin or AntiJoin.

Note that this rule uses HasOuterCols to ensure that it only matches
correlated Exists subqueries. There is no need to discard limits from
non-correlated Exists subqueries. Limits are preferred for non-correlated
Exists subqueries. See ConvertUncorrelatedExistsToCoalesceSubquery above for
details.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateExistsLimit`, not the other rules in that file):

```
# EliminateExistsLimit discards a Limit operator with a positive limit inside an
# Exist operator.
#
# The Limit operator prevents decorrelation rules from being applied. By
# discarding the Limit, which is a no-op inside of Exist operators, the query
# can be decorrelated into a more efficient SemiJoin or AntiJoin.
#
# Note that this rule uses HasOuterCols to ensure that it only matches
# correlated Exists subqueries. There is no need to discard limits from
# non-correlated Exists subqueries. Limits are preferred for non-correlated
# Exists subqueries. See ConvertUncorrelatedExistsToCoalesceSubquery above for
# details.
[EliminateExistsLimit, Normalize]
(Exists
    (Limit
        $input:* & (HasOuterCols $input)
        (Const $limit:*) & (IsPositiveInt $limit)
    )
    $existsPrivate:*
)
=>
(Exists $input $existsPrivate)
```
- Attempts used: 27
- Last updated: 2026-09-25T21:24:00.262011+00:00
- Reason / notes: The rule's entire content is that a positive LIMIT preserves the emptiness of its input (empty stays empty, non-empty stays non-empty), which is a list/ordering property that QED does not model — Limit can only be expressed as an uninterpreted bag→bag operator (QOp(Limit, k, ·)), and SMT reasoning cannot derive non-emptiness preservation of an uninterpreted function, so encoding EXISTS as a semi/correlate join gives before = semi(Outer, QOp(Limit,k,I)) vs after = semi(Outer, I), which is not merely unprovable but genuinely invalid under QED's uninterpreted semantics. No core-language construct can substitute for "at most k rows, non-empty iff input non-empty" (aggregates like COUNT(*) are equally uninterpreted — QED only knows bag-equality of aggregate inputs), so this is a fundamental QED limitation, not a missing builder that `extend_dsl_file` could close; adding a Limit builder would just feed the same uninterpreted operator to the prover and change nothing.

### `EliminateGroupBy` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateGroupBy is similar to EliminateDistinct, but it operates on GroupBy
expressions where the grouping columns are statically known to form a strict
key in the GroupBy's input.

It only applies if all the aggregate functions are ConstAgg, ConstNotNullAgg,
or AnyNotNullAgg. These aggregate functions all evaluate to the first non-NULL
value encountered, and NULL if there are no such values. Because the input is
guaranteed to produce one row per group, these aggregate functions are
equivalent to projecting their input column.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateGroupBy`, not the other rules in that file):

```
# EliminateGroupBy is similar to EliminateDistinct, but it operates on GroupBy
# expressions where the grouping columns are statically known to form a strict
# key in the GroupBy's input.
#
# It only applies if all the aggregate functions are ConstAgg, ConstNotNullAgg,
# or AnyNotNullAgg. These aggregate functions all evaluate to the first non-NULL
# value encountered, and NULL if there are no such values. Because the input is
# guaranteed to produce one row per group, these aggregate functions are
# equivalent to projecting their input column.
[EliminateGroupBy, Normalize]
(GroupBy
    $input:*
    $aggs:* & (AreAllAnyNotNullAggs $aggs)
    $groupingPrivate:* &
        (ColsAreStrictKey (GroupingCols $groupingPrivate) $input)
)
=>
(Project
    $input
    (ConvertAnyNotNullAggsToProjections $aggs)
    (IntersectionCols
        (GroupingOutputCols $groupingPrivate $aggs)
        (OutputCols $input)
    )
)
```
- Attempts used: 30
- Last updated: 2026-09-25T22:51:33.645711+00:00
- Reason / notes: EliminateGroupBy's entire soundness rests on the algebraic identity "AggAnyNotNull/ConstNotNull over a ≤1-row group returns that row's value (or NULL)", i.e. relating an aggregate output column to a plain field — in QED, aggregate calls are serialized as uninterpreted operator names (via `genericAggregateOp` in `Aggregate.semantics()`) and are only equated when they share a name over provably-equal group bags, so the prover has no axiom connecting `f({(k,x)})` to `x`, exactly the aggregate-algebra limitation QED's own evaluation documents ("knows nothing about a specific aggregate function's algebra beyond bag equality of its input"). This is a limitation of the unmodifiable Rust prover, not a missing DSL builder: the JSON format only carries an aggregate's name/operands, so `extend_dsl_file` cannot add a "sole-value" aggregate or its axiom, and no alternative encoding can remove the uninterpreted aggregate from the before side, which the rule defines. The porter's most-faithful instance (single-column unique scan, key = grouping key = aggregated value) is therefore the maximal encodable case, and its genuine `provable: false` verdict confirms the rule is outside QED's reach.

### `EliminateLimit` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/limit.opt

EliminateLimit discards a Limit operator if its constant limit is greater than
or equal to the maximum number of rows that can be returned by the input. In
this case, the Limit is just a no-op, because the rows are already limited.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `EliminateLimit`, not the other rules in that file):

```
# EliminateLimit discards a Limit operator if its constant limit is greater than
# or equal to the maximum number of rows that can be returned by the input. In
# this case, the Limit is just a no-op, because the rows are already limited.
[EliminateLimit, Normalize]
(Limit
    $input:*
    (Const $limit:*) & (LimitGeMaxRows $limit $input)
)
=>
$input
```
- Attempts used: 21
- Last updated: 2026-09-26T00:08:59.991251+00:00
- Reason / notes: Limit is one of the operators QED explicitly does not model (Sort/Limit/Offset have no bag-semantic meaning; Limit is left uninterpreted with no row-retention, ordering, or cardinality axioms), and the rule's correctness rests exactly on that missing semantics — the side condition LimitGeMaxRows needs a bound on the input's maximum row count, and the conclusion needs the axiom that a Limit with limit >= maxRows acts as the identity. Since the rewrite *removes* the Limit (the after side is the bare input), a faithful encoding cannot share the Limit symbol as a common wrapper on both sides to factor out as in the proven AssociateLimitJoinsLeft-style precedent; the two sides become Limit(x) versus x for an uninterpreted function, which is not valid for arbitrary x, and no core-language stand-in (filter, join, union, aggregate) can faithfully model positional row selection, so no narrower special case is provable either.

### `EliminateMax1Row` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/max1row.opt

EliminateMax1Row discards the Max1Row operator if its input is statically
guaranteed to have no more than one row. Removing the Max1Row operator is
important when decorrelating subqueries, as it interferes with ApplyJoin
pushdown when it's present.

Extracted from `max1row.opt` (which defines multiple rules — implement specifically `EliminateMax1Row`, not the other rules in that file):

```
# EliminateMax1Row discards the Max1Row operator if its input is statically
# guaranteed to have no more than one row. Removing the Max1Row operator is
# important when decorrelating subqueries, as it interferes with ApplyJoin
# pushdown when it's present.
[EliminateMax1Row, Normalize]
(Max1Row $input:* & (HasZeroOrOneRow $input))
=>
$input
```
- Attempts used: 23
- Last updated: 2026-09-26T00:17:13.541123+00:00
- Reason / notes: Max1Row is not a bag function — it is the identity only on inputs with at most one row and a runtime error otherwise — so it belongs to the row-count/Limit family that QED explicitly does not model, and it exists in neither the RelRN builders nor the prover's JSON vocabulary (the serializer's switch has no such node, and `extend_dsl_file` can only add builders over JSON the immutable Rust prover already understands, not new semantics). Furthermore, the rule is a conditional rewrite whose entire content is the `HasZeroOrOneRow` side condition, and `RRule` exposes only `before()`/`after()` with no assumption/guard channel, so the precondition must be satisfied structurally — but the core language's only "at most 1 row" shape is `.empty()` (exactly 0 rows; the `unique` scan flag is a distinct-key constraint, not a total row-count bound), which collapses the narrowest special case to a tautology that cannot even be written without the missing operator.

### `EliminateOffset` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/limit.opt

EliminateOffset discards an Offset operator if its offset value is zero.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `EliminateOffset`, not the other rules in that file):

```
# EliminateOffset discards an Offset operator if its offset value is zero.
[EliminateOffset, Normalize]
(Offset $input:* (Const 0))
=>
$input
```
- Attempts used: 25
- Last updated: 2026-09-26T00:16:02.509396+00:00
- Reason / notes: EliminateOffset's entire content is that skipping 0 rows is the identity, which is purely a row-ordering/list-semantic fact, and QED explicitly does not model ordering semantics — `Sort`/`Limit`/`Offset` have no bag-semantic meaning, and Offset is only an uninterpreted operator with no axioms (qed.pdf §3.1/§6.2). Hence `Offset(input) ≡ input` is unentailed for every input, including any degenerate special case, because the node being deleted is itself the unconstrained uninterpreted symbol that can't be factored out of both sides. The missing Offset builder in `RelRN` is a DSL gap, but closing it via `extend_dsl_file` (the JSON `sort` node with `offset` is already serializable) would not change the verdict, since the limitation sits in the prover's semantics, not the builder surface. ```

### `EliminateUDFCallSubquery` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateUDFCallSubquery replaces a subquery with a udf call if the subquery's
input is a single-row, single-column Values expression with a udf call.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateUDFCallSubquery`, not the other rules in that file):

```
# EliminateUDFCallSubquery replaces a subquery with a udf call if the subquery's
# input is a single-row, single-column Values expression with a udf call.
[EliminateUDFCallSubquery, Normalize]
(Subquery (Values [ (Tuple [ $udf:(UDFCall) ]) ]))
=>
$udf
```
- Attempts used: 23
- Last updated: 2026-09-26T00:42:02.950633+00:00
- Reason / notes: The rule is a scalar rewrite whose LHS is a scalar subquery term (a relation embedded inside a scalar expression, `Subquery(Values(udf))` ⟹ `udf`), and QED's core language is purely relational with bag/semiring semantics — it has no operator that maps a relation to a scalar term, so the only link between the two sides (the subquery's "evaluate and extract the single value" behavior) is bespoke operator semantics that QED's model cannot express or derive, leaving SMT with two unrelated uninterpreted terms. This is confirmed empirically by the structurally identical sibling EliminateConstValueSubquery (same `Subquery(Values(Tuple(single scalar)))` shape), which fast-rejected before SMT after 24 attempts and passed independent verification as unsupported. Closing the DSL gaps (non-empty Values, a subquery-term builder) would only let the pattern be serialized — proving it would require modeling scalar-subquery algebra inside the immutable prover, which is out of bounds, so the UNSUPPORTED call is sound. ```

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

### `FoldArray` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldArray evaluates an Array expression with constant inputs. It replaces the
Array with a Const datum with type TArray.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldArray`, not the other rules in that file):

```
# FoldArray evaluates an Array expression with constant inputs. It replaces the
# Array with a Const datum with type TArray.
[FoldArray, Normalize]
(Array $elems:* & (IsListOfConstants $elems) $typ:*)
=>
(FoldArray $elems $typ)
```
- Attempts used: 41
- Last updated: 2026-09-26T01:23:34.380552+00:00
- Reason / notes: FoldArray rewrites an uninterpreted Array constructor applied to constant arguments into a concrete TArray ground constant, but QED models the Array constructor as an uninterpreted function with no axioms relating its application to any ground term, so the SMT solver can never establish Array(c1,c2,…)=arrayConstant; additionally the DSL has no array-literal builder (RexRN exposes only boolean true/false literals, and the QED JSON format carries no array type or array-value semantics), so even the RHS is inexpressible — the only encodable encoding reuses the same uninterpreted symbol on both sides, yielding a vacuous identity rather than a genuine rewrite. ```

### `FoldAssignmentCast` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldAssignmentCast is similar to FoldCast, but it involves an assignment cast
operation. As with FoldCast, FoldAssignmentCast applies as long as the
evaluation would not cause an error.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldAssignmentCast`, not the other rules in that file):

```
# FoldAssignmentCast is similar to FoldCast, but it involves an assignment cast
# operation. As with FoldCast, FoldAssignmentCast applies as long as the
# evaluation would not cause an error.
[FoldAssignmentCast, Normalize]
(AssignmentCast
    $input:*
    $typ:* &
        (IsConstValueOrGroupOfConstValues $input) &
        (Let ($result $ok):(FoldAssignmentCast $input $typ) $ok)
)
=>
$result
```
- Attempts used: 6
- Last updated: 2026-09-26T01:09:00.954973+00:00
- Reason / notes: The rule's correctness rests entirely on the assignment-cast operator's operational evaluation semantics (that casting a constant datum yields a precomputed value without error), which QED cannot model because every scalar operator — including an AssignmentCast added via extend_dsl_file — is lowered to an uninterpreted function, so the LHS term assign_cast(c, T) and the folded constant r are distinct terms no available theory can equate (unlike the proven FoldNotFalse precedent, where ¬false ≡ true is a logical truth the SMT solver knows). The IsConstValueOrGroupOfConstValues guard and the $ok no-error side condition are symbol/type-level properties with no counterpart in the pattern language, so the only encoding QED could ever prove is the vacuous identity before() ≡ after(). ```

### `FoldCast` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldCast is similar to FoldUnary, but it involves a cast operation. As with
FoldUnary, FoldCast applies as long as the evaluation would not cause an
error.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldCast`, not the other rules in that file):

```
# FoldCast is similar to FoldUnary, but it involves a cast operation. As with
# FoldUnary, FoldCast applies as long as the evaluation would not cause an
# error.
[FoldCast, Normalize]
(Cast
    $input:*
    $typ:* &
        (IsConstValueOrGroupOfConstValues $input) &
        (Let ($result $ok):(FoldCast $input $typ) $ok)
)
=>
$result
```
- Attempts used: 22
- Last updated: 2026-09-26T01:19:37.362399+00:00
- Reason / notes: FoldCast's RHS is a value produced by *evaluating* the cast operator on a constant input, but in RuleScript a cast can only be modeled as an uninterpreted function symbol, and QED has no axiom relating cast(c, T) to any folded result — unlike FoldNotFalse, where NOT and the boolean literals are genuinely interpreted. The rule's side conditions are equally inexpressible: the pattern language has no way to state "input is a constant" (IsConstValueOrGroupOfConstValues), no Let-binder to carry the computed ($result, $ok) pair into the RHS, and no literal/constant constructors at all. So the only provable encoding is the vacuous identity with the same cast symbol on both sides; this rests on the operator's bespoke evaluation semantics that QED fundamentally cannot see through, not on a missed encoding trick. ```

### `FoldCollate` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

FoldCollate converts a Collate expr over an uncollated string into a collated
string constant.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `FoldCollate`, not the other rules in that file):

```
# FoldCollate converts a Collate expr over an uncollated string into a collated
# string constant.
[FoldCollate, Normalize]
(Collate $input:(Const) $locale:*)
=>
(CastToCollatedString $input $locale)
```
- Attempts used: 22
- Last updated: 2026-09-26T01:27:51.423248+00:00
- Reason / notes: FoldCollate's entire content is the backend-bespoke identity `Collate(c, locale) ≡ CastToCollatedString(c, locale)` on a constant input, which holds only by virtue of CockroachDB's definitions of those two operators, not by any bag-algebra fact. In QED's uninterpreted-function theory they must be two distinct symbol names (a single shared name would assume the rule's conclusion as a premise, and the input's constant-ness gives no leverage — distinct uninterpreted functions can disagree even at ground terms), so the equivalence is not a logical validity the SMT solver can establish; the only "proofs" available are vacuous encodings that stop expressing the rule. This lands squarely in the documented "backend operator's bespoke internal semantics" limitation, and no `extend_dsl_file` addition (new `Collate`/`CastToCollatedString` builders, a non-boolean constant literal) could close the gap, since the DSL has no axiom mechanism to relate two distinct symbols — the UNSUPPORTED verdict is correct.

### `FoldDivOne` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/numeric.opt

FoldDivOne folds $left / 1 for numeric types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldDivOne`, not the other rules in that file):

```
# FoldDivOne folds $left / 1 for numeric types.
[FoldDivOne, Normalize]
(Div $left:* $right:(Const 1))
=>
(Cast $left (BinaryType (OpName) $left $right))
```
- Attempts used: 25
- Last updated: 2026-09-26T01:48:25.295520+00:00
- Reason / notes: FoldDivOne depends on the numeric constant-folding identity `div(x, 1) = cast(x)`, including a representable literal `1` and the algebraic behavior of division/casting. QED only sees non-boolean scalar operators as uninterpreted function symbols over uninterpreted types, with no numeric literals and no axioms connecting distinct operators, so no faithful encoding can be universally proved.

### `FoldEqualsAnyNull` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldEqualsAnyNull, converts a scalar ANY operation to NULL if the right-hand
side tuple is NULL, e.g. x = ANY(NULL::int[]). See #42562.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldEqualsAnyNull`, not the other rules in that file):

```
# FoldEqualsAnyNull, converts a scalar ANY operation to NULL if the right-hand
# side tuple is NULL, e.g. x = ANY(NULL::int[]). See #42562.
[FoldEqualsAnyNull, Normalize]
(AnyScalar * (Null) *)
=>
(Null (BoolType))
```
- Attempts used: 76
- Last updated: 2026-09-26T04:23:54.528098+00:00
- Reason / notes: Manually investigated by Claude (the automated run exhausted all 5 rounds on repeated context-length crashes, never reaching a real try_rule call). The rule needs: AnyScalar(x, NULL-array, cmp) == NULL, for ANY x — i.e. an uninterpreted operator whose result is forced NULL whenever one specific argument is NULL. Checked how QED's prover core models NULL (qed-prover/src/pipeline/shared.rs): is_null() is defined purely as equality against a fresh, uninterpreted NULL sentinel term (Logic::Eq(Op('NULL',[],ty), self)) — there is no general rule anywhere that propagates NULL through an uninterpreted function/predicate application based on its arguments. Only a small hardcoded set of operators get real interpreted semantics in relation.rs (COUNT, EXISTS, IS NULL/IS NOT NULL, boolean AND/OR/NOT); a custom 'AnyScalar' predicate, like any other generic RexRN.Pred, would be fully opaque with no null-propagation behavior at all, and the DSL has no CASE/conditional expression construct to manually encode 'if this arg is null, return null'. Unlike EliminateExistsProject/EliminateExistsZeroRows (where EXISTS turned out to have real hidden interpreted semantics reachable via a custom local RexRN calling raw Calcite APIs), there is no equivalent hidden machinery for null-strictness to tap into here — this is a genuine missing capability (general null-propagation reasoning for uninterpreted operators), not a DSL omission a self-contained trick can route around.

### `FoldFloorDivOne` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/numeric.opt

FoldFloorDivOne folds $left // 1 for integer types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldFloorDivOne`, not the other rules in that file):

```
# FoldFloorDivOne folds $left // 1 for integer types.
[FoldFloorDivOne, Normalize]
(FloorDiv $left:* $right:(Const 1) & (IsInt $left))
=>
(Cast $left (BinaryType (OpName) $left $right))
```
- Attempts used: 21
- Last updated: 2026-09-26T02:08:13.431915+00:00
- Reason / notes: The rule's correctness rests on the numeric identity "floor division by 1 is the identity function" (plus a value-preserving cast), but RuleScript can only express `floor_div` and `cast` as uninterpreted generic projection operators, and QED's SMT encoding has no integer-arithmetic axioms connecting those fresh uninterpreted function symbols to the literal 1 — so `floor_div(x, 1) = x` (or `= cast(x)`) is false in some interpretations and any faithful encoding (projection, predicate, or join-based) is refuted by a countermodel. A genuine `try_rule` attempt (e.g. `scan.project(floor_div(col, 1))` vs `scan.project(col)`) would return not-provable for the same reason the porter cited, and the gap is not closable by `extend_dsl_file` (the DSL also lacks an integer-literal builder, but that is secondary — the fundamental wall is QED treating scalar arithmetic operators as uninterpreted functions). ```

### `FoldFunction` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldFunction is similar to FoldBinary, but it involves a function with
constant inputs. As with FoldBinary, FoldFunction applies as long as the
evaluation would not cause an error. Additionally, only certain functions
are safe to fold as part of normalization. Other functions rely on context
that may change between runs of a prepared query.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldFunction`, not the other rules in that file):

```
# FoldFunction is similar to FoldBinary, but it involves a function with
# constant inputs. As with FoldBinary, FoldFunction applies as long as the
# evaluation would not cause an error. Additionally, only certain functions
# are safe to fold as part of normalization. Other functions rely on context
# that may change between runs of a prepared query.
[FoldFunction, Normalize]
(Function
    $args:* & (IsListOfConstants $args)
    $private:* &
        (Let ($result $ok):(FoldFunction $args $private) $ok)
)
=>
$result
```
- Attempts used: 27
- Last updated: 2026-09-26T02:15:07.741693+00:00
- Reason / notes: FoldFunction's correctness rests on evaluating specific named functions over specific constant arguments and equating the result with the produced constant, but QED treats all function/predicate symbols as uninterpreted with no evaluation axioms — under universal quantification it can never derive a relation between f(c₁,…,cₙ) and any particular folded value — and the scalar language has only boolean True/False literals, so the constant arguments and the folded result can't even be written down (and a scan field is a variable, not a constant, making "f(x) ⇒ r" a different, unprovable statement). Closing this with extend_dsl_file is impossible in principle: new operators serialize as uninterpreted symbols in the JSON QED already consumes, and giving a symbol evaluation semantics would mean modifying the Rust prover itself, which is off-limits — so this is the fundamental "backend operator semantics QED cannot see through" limitation, not a missed encoding. ```

### `FoldFunctionWithNullArg` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldFunctionWithNullArg folds a Function to Null when one of its arguments is
Null and all of the following are true:

1. The function is not called when any of its inputs are null
(CalledOnNullInput=false).
2. The function is a normal function not an aggregate, window, or generator.

It is safe to fold functions to Null in this case because a function with
CalledOnNullInput=false would never error with a Null argument, even if the
other args are invalid. For example, calling encode with NULL bytes and an
invalid encoding format does not error:

SELECT encode(NULL::BYTES, 'foo')
=> NULL

Stable and volatile functions that rely on context or produce side-effects can
also be folded to Null in this case because a function with
CalledOnNullInput=false is never evaluated if any of its arguments are Null.
The function results directly in Null without being invoked, so it is
guaranteed not to rely on context or produce side-effects. See
Overload.CalledOnNullInput for more details.

FoldFunctionWithNullArg is defined before FoldFunction so that we can avoid
the overhead of evaluating the function in FoldFunction if it has any Null
arguments.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldFunctionWithNullArg`, not the other rules in that file):

```
# FoldFunctionWithNullArg folds a Function to Null when one of its arguments is
# Null and all of the following are true:
#
#   1. The function is not called when any of its inputs are null
#      (CalledOnNullInput=false).
#   2. The function is a normal function not an aggregate, window, or generator.
#
# It is safe to fold functions to Null in this case because a function with
# CalledOnNullInput=false would never error with a Null argument, even if the
# other args are invalid. For example, calling encode with NULL bytes and an
# invalid encoding format does not error:
#
#     SELECT encode(NULL::BYTES, 'foo')
#       => NULL
#
# Stable and volatile functions that rely on context or produce side-effects can
# also be folded to Null in this case because a function with
# CalledOnNullInput=false is never evaluated if any of its arguments are Null.
# The function results directly in Null without being invoked, so it is
# guaranteed not to rely on context or produce side-effects. See
# Overload.CalledOnNullInput for more details.
#
# FoldFunctionWithNullArg is defined before FoldFunction so that we can avoid
# the overhead of evaluating the function in FoldFunction if it has any Null
# arguments.
[FoldFunctionWithNullArg, Normalize]
(Function
    $args:*
    $private:* &
        (CanFoldFunctionWithNullArg $private) &
        (HasNullArg $args)
)
=>
(Null (FunctionReturnType $private))
```
- Attempts used: 61
- Last updated: 2026-09-26T02:50:13.889495+00:00
- Reason / notes: The rule's soundness rests on backend metadata about the function itself (CalledOnNullInput=false ⇒ a NULL argument forces a NULL result), but in QED's SMT encoding every scalar function symbol introduced by the DSL is a pure uninterpreted function, and null-propagation axioms are baked in only for QED's own primitives — so for any uninterpreted 1- or 2-arg f, `Project(f(…, NULL, …), S) ≡ Project(NULL, S)` is refuted by a model where f returns a non-NULL value on a NULL-containing tuple, and no relational trick (filters, joins, or table "guaranteed" constraints) can supply the missing axiom, since the gap is in the prover's semantics of the symbol, not in DSL expressiveness (granting blanket null-propagation to all uninterpreted ops would itself be unsound, e.g. for COALESCE/NULLIF). The only conceivable special case is instantiating the function with a built-in operator that genuinely propagates NULL — of the DSL-exposable And/Or/Not, only Not does (Not(NULL)→NULL), a degenerate single-operator instance that isn't really this rule and that the DSL can't even state without adding a NULL-literal builder. The porter's complete-fragment counterexamples on the 1-arg and 2-arg probes are consistent with this, so the UNSUPPORTED conclusion is correct.

### `FoldInEmpty` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldInEmpty replaces the In with False when the the right input is empty. Note
that this is correct even if the left side is Null, since even an unknown
value can't be in an empty set.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldInEmpty`, not the other rules in that file):

```
# FoldInEmpty replaces the In with False when the the right input is empty. Note
# that this is correct even if the left side is Null, since even an unknown
# value can't be in an empty set.
[FoldInEmpty, Normalize]
(In * (Tuple []))
=>
(False)
```
- Attempts used: 49
- Last updated: 2026-09-26T03:44:09.344095+00:00
- Reason / notes: FoldInEmpty's soundness rests on the scalar IN operator's list-membership algebra (x ∈ [] = false), which QED does not model and RuleScript cannot axiomatize: naming In as an uninterpreted symbol makes the claim unprovable (symbols are universally quantified, so Filter(in, R) ≡ Filter(False, R) fails under in ≡ true), and the only encodings QED could prove (e.g., semi-join against a relation built with empty() ≡ false filter) are vacuous bag identities where both sides are empty by construction and the fold itself never appears. This is a genuine "operator's bespoke internal semantics" limitation — the core RexRN/RelRN language has no scalar In, no tuple/list constant, and no axiom mechanism — not a missing builder that extend_dsl_file could close, since the unchanging prover has no list-membership semantics to attach to such a symbol. ```

### `FoldInNull` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

FoldInNull replaces the In/Not operator with Null when the tuple only
contains null. The NormalizeInConst pattern will reduce multiple nulls to a
single null when it removes duplicates, so this pattern will match that.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `FoldInNull`, not the other rules in that file):

```
# FoldInNull replaces the In/Not operator with Null when the tuple only
# contains null. The NormalizeInConst pattern will reduce multiple nulls to a
# single null when it removes duplicates, so this pattern will match that.
[FoldInNull, Normalize]
(In | NotIn $left:* (Tuple [ (Null) ]))
=>
(Null (BoolType))
```
- Attempts used: 100
- Last updated: 2026-09-26T07:11:56.305255+00:00
- Reason / notes: Manually investigated by Claude (the automated run exhausted all 5 rounds on repeated context-length crashes, never reaching a real try_rule call). Same root cause as FoldEqualsAnyNull: the rule needs (x IN (NULL)) == NULL and (x NOT IN (NULL)) == NULL for ANY x — an uninterpreted operator (IN) forced to NULL based on one specific argument (the singleton-NULL tuple), independent of the other (x). QED's prover core has no general null-propagation rule for uninterpreted function/predicate applications (confirmed via qed-prover/src/pipeline/shared.rs: is_null() is just equality against a fresh uninterpreted NULL sentinel, with no strictness rule tied to any operator's arguments) — only a small hardcoded set of operators (COUNT, EXISTS, IS NULL/IS NOT NULL, boolean AND/OR/NOT) get real interpreted semantics, and IN/NOT IN is not among them. No CASE/conditional construct exists in the DSL to manually encode the null-forcing branch either. Genuinely outside QED's supported fragment for the same reason as FoldEqualsAnyNull.

### `FoldJSONAccessIntoValues` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/project.opt

FoldJSONAccessIntoValues replaces a Values operator that has a single JSON
column and at least one row with a new Values operator that has a column for
each JSON key. This works as long as the surrounding Project does not
reference the original JSON column itself, since then it would be invalid to
eliminate that reference. However, references to fields within the JSON are
allowed, and are translated to the new unnested Values columns. The rule only
fires if all referenced JSON keys exist the first row, and if all JSON keys
from the first row also exist in all other rows.

FoldJSONAccessIntoValues has the side affect of pruning any keys that are not
present in the first Values row (since they are unreferenced).

This rule simplifies access to the Values operator in hopes of allowing other
rules to fire.

Example:

SELECT cust->'id' AS id, cust->'name' AS name
FROM (VALUES
('{"id": 1, "name": 'Drew'}'::JSON),
('{"id": 2, "name": 'Radu'}'::JSON),
('{"id": 3, "name": 'Rebecca'}'::JSON)
) v(cust)
=>
SELECT id, name
FROM (VALUES
(1::JSON, 'Drew'::JSON),
(2::JSON, 'Radu'::JSON),
(3::JSON, 'Rebecca'::JSON)
) v(id, name)

Extracted from `project.opt` (which defines multiple rules — implement specifically `FoldJSONAccessIntoValues`, not the other rules in that file):

```
# FoldJSONAccessIntoValues replaces a Values operator that has a single JSON
# column and at least one row with a new Values operator that has a column for
# each JSON key. This works as long as the surrounding Project does not
# reference the original JSON column itself, since then it would be invalid to
# eliminate that reference. However, references to fields within the JSON are
# allowed, and are translated to the new unnested Values columns. The rule only
# fires if all referenced JSON keys exist the first row, and if all JSON keys
# from the first row also exist in all other rows.
#
# FoldJSONAccessIntoValues has the side affect of pruning any keys that are not
# present in the first Values row (since they are unreferenced).
#
# This rule simplifies access to the Values operator in hopes of allowing other
# rules to fire.
#
# Example:
#
#   SELECT cust->'id' AS id, cust->'name' AS name
#   FROM (VALUES
#      ('{"id": 1, "name": 'Drew'}'::JSON),
#      ('{"id": 2, "name": 'Radu'}'::JSON),
#      ('{"id": 3, "name": 'Rebecca'}'::JSON)
#   ) v(cust)
#   =>
#   SELECT id, name
#   FROM (VALUES
#      (1::JSON, 'Drew'::JSON),
#      (2::JSON, 'Radu'::JSON),
#      (3::JSON, 'Rebecca'::JSON)
#   ) v(id, name)
#
[FoldJSONAccessIntoValues, Normalize]
(Project
    $input:(Values [ * ... ]) &
        (ColsAreLenOne (OutputCols $input))
    $projections:* &
        (CanUnnestJSONFromValues
            $input
            $projections
            $col:(SingleColFromSet (OutputCols $input))
        )
    $passthrough:* & (ColsAreEmpty $passthrough)
)
=>
(Project
    (UnnestJSONFromValues
        $input
        $jsonCols:(MakeColsForUnnestJSON $input $col)
    )
    (FoldJSONFieldAccess $projections $jsonCols $col $input)
    $passthrough
)
```
- Attempts used: 41
- Last updated: 2026-09-26T05:44:42.953443+00:00
- Reason / notes: FoldJSONAccessIntoValues is a constant-folding rule whose correctness depends on evaluating a JSON field-access operator (e.g. `cust->'id'`) over concrete JSON literals stored in a Values row to produce new literal values; QED's bag-semantic SMT theory treats every operator as an uninterpreted function with no algebraic axioms, so it cannot relate `jsonAccess(jsonLiteral, key)` to the extracted constant that the rewrite produces, making the before/after bags unprovable-equal for any faithful encoding. ```

### `FoldLimits` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/limit.opt

FoldLimits replaces a Limit on top of a Limit with a single Limit operator
when the outer limit value is smaller than or equal to the inner limit value
and the inner ordering implies the outer ordering. Note: the case when the
outer limit value is larger than the inner is handled by EliminateLimit.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `FoldLimits`, not the other rules in that file):

```
# FoldLimits replaces a Limit on top of a Limit with a single Limit operator
# when the outer limit value is smaller than or equal to the inner limit value
# and the inner ordering implies the outer ordering. Note: the case when the
# outer limit value is larger than the inner is handled by EliminateLimit.
[FoldLimits, Normalize]
(Limit
    (Limit
        $innerInput:*
        $innerLimitExpr:(Const $innerLimit:*)
        $innerOrdering:*
    )
    $outerLimitExpr:(Const $outerLimit:*) &
        ^(IsGreaterThan $outerLimit $innerLimit)
    $outerOrdering:* &
        (OrderingImplies $innerOrdering $outerOrdering)
)
=>
(Limit $innerInput $outerLimitExpr $innerOrdering)
```
- Attempts used: 22
- Last updated: 2026-09-26T05:42:39.254303+00:00
- Reason / notes: FoldLimits reduces to the identity Limit(Limit(R, n_in, ord_in), n_out, ord_out) ≡ Limit(R, n_out, ord_in) under n_out ≤ n_in and ord_in ⇒ ord_out, and its entire correctness rests on ordered positional row selection ("first k rows in ordering o") plus the ordering-implication relation. QED decides equivalence over bag semantics and, per its own stated limitations, does not model list/ordering semantics (Sort/Limit/Offset/Window/Sample have no bag-semantic meaning), and the core language exposes no Limit operator at all; modeling Limit as an uninterpreted operator would strip away exactly the row-retention/ordering axioms the identity needs, and it is not valid for an arbitrary uninterpreted operator (QED would find a countermodel), with no relational sub-identity to factor out since both sides terminate in a single limit over the same base input — so the SKIPPED EliminateLimit/EliminateOffset precedents correctly apply.

### `FoldMinusZero` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/numeric.opt

FoldMinusZero folds $left - 0 for numeric types. This rule requires a check
that $left is numeric because JSON - INT is valid and is not a no-op with a
zero value.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldMinusZero`, not the other rules in that file):

```
# FoldMinusZero folds $left - 0 for numeric types. This rule requires a check
# that $left is numeric because JSON - INT is valid and is not a no-op with a
# zero value.
[FoldMinusZero, Normalize]
(Minus $left:(IsAdditiveType (TypeOf $left)) $right:(Const 0))
=>
(Cast $left (BinaryType Minus $left $right))
```
- Attempts used: 23
- Last updated: 2026-09-26T05:40:17.146225+00:00
- Reason / notes: The rule's core is the numeric identity `x - 0 = cast(x)`, which needs QED to (a) name the numeric constant 0 — the DSL only exposes boolean literals (`RexRN.trueLiteral`/`falseLiteral`), and a `Scan` is an arbitrary relation, so there is no "single row containing 0" to join against — and (b) have arithmetic axioms connecting `minus` and `cast`; QED models non-boolean scalar operators as uninterpreted functions over uninterpreted types with no axioms relating distinct operator symbols, so for any faithful encoding the SMT solver can assign different functions to `minus` and `cast` and refute the equality. This is a fundamental limitation of the prover's theory (the "Peano arithmetic" in qed.pdf is used only for bag-multiplicity counting, not data-value arithmetic), and extending the DSL cannot close it since the axiom gap lives in the prover — consistent with the independently reviewed FoldDivOne precedent from the same file, which has the identical structure (`x / 1 = cast(x)`). ```

### `FoldMultOne` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/numeric.opt

FoldMultOne folds $left * 1 for numeric types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldMultOne`, not the other rules in that file):

```
# FoldMultOne folds $left * 1 for numeric types.
[FoldMultOne, Normalize]
(Mult $left:* $right:(Const 1))
=>
(Cast $left (BinaryType Mult $left $right))
```
- Attempts used: 22
- Last updated: 2026-09-26T05:49:40.931537+00:00
- Reason / notes: FoldMultOne depends on the numeric algebra identity `x * 1 = cast(x, T)`, but RuleScript/QED can only express `Mult` and `Cast` as uninterpreted scalar functions with no numeric-axiom knowledge, and the DSL cannot faithfully name the constant `1`. Even if a DSL extension introduced a numeric literal, it would lower to an uninterpreted constant in the immutable prover, and the mult-by-one identity would still be unprovable.

