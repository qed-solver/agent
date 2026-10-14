# RuleScript porting progress

_Last updated: 2026-10-14T15:59:53.925350+00:00_

**96/151 rules proved** (0 failed, 55 skipped as out of QED's supported fragment).

| Rule | Backend | Status | Scope | Attempts | Notes |
|---|---|---|---|---|---|
| `AggregateExpandDistinctAggregates` | Apache Calcite | ✅ PROVED | PARTIAL | 72 | The encoding faithfully mirrors the source rule's `convertMonopole` branch: `before()` is `GROUP BY k` of two distinct calls `f(DISTINCT ... |
| `AggregateExpandWithinDistinct` | Apache Calcite | ✅ PROVED | PARTIAL | 24 | The encoding is non-vacuous (a single COUNT(DISTINCT x) aggregate vs. a nested dedup-then-count plan), and tracing the source rule on the... |
| `AggregateExtractProject` | Apache Calcite | ✅ PROVED | PARTIAL | 14 | The encoding faithfully mirrors the source rule's actual computation: it computes the used columns (group col 0, agg arg col 1), projects... |
| `AggregateFilterTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 9 | The encoding faithfully captures Case 1 of the source rule: the uninterpreted predicate depends only on the group key (i.e. all filter co... |
| `AggregateJoinJoinRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 50 | The encoding is a faithful, honestly-tagged PARTIAL instance of the rule — pure DISTINCT on (A.col, C.col) over single-column inputs, exa... |
| `AggregateJoinRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 67 | The encoding correctly captures the core semantic content of AggregateJoinRemove's LEFT-join branch: a DISTINCT aggregate (group key on t... |
| `AggregateJoinTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 126 | Hand-applied by harness operator: added additive RelRN.scanMany/ScanMany multi-column scan, encoded the DEFAULT-config (no-agg-function) ... |
| `AggregateMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 37 | The encoding is a genuine, non-vacuous instance of the source rule: when the top aggregate has no aggregate calls, Calcite's onMatch skip... |
| `AggregateProjectConstantToDummyJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 62 | The encoding mirrors the source rule's onMatch exactly — INNER join of the input with a one-row values table on constant true, a project ... |
| `AggregateProjectMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 68 | The encoding is a line-faithful instance of the source rule's `apply`: for the swap project `[x1, x0]` the interesting-field map is `{0→1... |
| `AggregateProjectPullUpConstants` | Apache Calcite | ✅ PROVED | PARTIAL | 63 | The encoding faithfully captures the rule's core transformation: an aggregate whose leading group key is a constant column is rewritten t... |
| `AggregateRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 34 | `before()` (a SIMPLE group-by on column 0 with zero agg calls, i.e. SELECT DISTINCT) is structurally and semantically distinct from `afte... |
| `AggregateToSemiJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 23 | The encoding faithfully captures the rule's INNER transformation — `GROUP BY l.k (L INNER-JOIN (GROUP BY r.k R) ON l.k=r.k)` rewritten to... |
| `AggregateUnionAggregate` | Apache Calcite | ✅ PROVED | PARTIAL | 8 | The encoding is faithful and non-vacuous: `before()` truly differs from `after()` (an extra group-by-all, no-agg-call dedup beneath the s... |
| `AggregateUnionTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 25 | The encoding faithfully represents the no-aggregate-call (DISTINCT / group-by-all) limit of `AggregateUnionTranspose` over UNION ALL, wit... |
| `AggregateValues` | Apache Calcite | ✅ PROVED | PARTIAL | 10 | The encoding faithfully captures the rule's dedup branch — a simple aggregate (group by the relation's only column, zero aggregate calls)... |
| `CalcMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 14 | The encoding faithfully captures CalcMerge's core transformation — the top Calc's condition and projections substituted over the bottom C... |
| `CalcReduceExpressions` | Apache Calcite | ✅ PROVED | PARTIAL | 9 | The encoding faithfully captures the Calc branch where the condition is constant FALSE: before is a FALSE-filtered source projected by E1... |
| `CalcRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 5 | The encoding is faithful: before() is a ProjectMany with the identity field list over a scan, and after() is the bare scan — structurally... |
| `DphypJoinReorder` | Apache Calcite | ✅ PROVED | PARTIAL | 9 | The PARTIAL scope line correctly acknowledges that this is a 3-way, fully-connected, inner-join instance rather than the full cost-driven... |
| `ExpandDisjunctionForJoinInputs` | Apache Calcite | ✅ PROVED | PARTIAL | 35 | before() and after() are structurally different — after() ANDs into the join condition the two per-side disjunctions (pl∨rl) and (pr∨sr) ... |
| `ExpandDisjunctionForTable` | Apache Calcite | ✅ PROVED | PARTIAL | 33 | `before()` and `after()` are structurally distinct (after ANDs in the per-table disjuncts `(a∨c)` and `(b∨d)`), and the encoding uses pro... |
| `FilterAggregateTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 13 | The encoding is a non-vacuous, faithful instance of the rule's core transformation: the conjunct P depending only on the (identity) group... |
| `FilterCalcMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 11 | The encoding faithfully models Filter-over-Calc (with condition B and projections [E0,E1]) as Filter(T)∘Project∘Filter(B) ⟹ Project∘Filte... |
| `FilterCorrelate` | Apache Calcite | ✅ PROVED | PARTIAL | 128 | before() and after() are structurally different (side-local conjuncts pushed below the join while the cross-side conjunct stays above), t... |
| `FilterFlattenCorrelatedCondition` | Apache Calcite | ✅ PROVED | PARTIAL | 34 | It proves a non-vacuous, honestly scoped special case of the Calcite rewrite: one uninterpreted comparison between an outer column and an... |
| `FilterJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 37 | before() and after() are structurally different (Filter above an inner join vs. the AND-composed join condition), and the symbol sharing ... |
| `FilterMerge` | Apache Calcite | ✅ PROVED | FULL | 1 | (no verifier LLM configured — result not independently reviewed) |
| `FilterMultiJoinMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 8 | The before() and after() trees are structurally different (a Filter node above the root join vs. the same two uninterpreted predicates AN... |
| `FilterProjectTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 42 | The encoding faithfully captures the core mechanism of FilterProjectTranspose — pushing a predicate below a project by substituting the p... |
| `FilterReduceExpressions` | Apache Calcite | ✅ PROVED | PARTIAL | 9 | The encoding precisely mirrors the source rule's false-literal branch of `onMatch` — `newConditionExp instanceof RexLiteral` (false) → `c... |
| `FilterRemoveIsNotDistinctFrom` | Apache Calcite | ✅ PROVED | PARTIAL | 37 | The encoding is non-trivial and operator-correct: before() is Filter(IS_NOT_DISTINCT_FROM(x,y)) and after() is the DNF `(x IS NULL AND y ... |
| `FilterSetOpTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 6 | before() = Filter(P, UnionAll(L, R)) and after() = UnionAll(Filter(P, L), Filter(P, R)) are structurally distinct and exactly the pushdow... |
| `FilterTableFunctionTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 35 | The encoding faithfully captures the rule: under the source's side conditions (single input, identity 1-to-1 non-derived mapping), the ta... |
| `FilterToCalc` | Apache Calcite | ✅ PROVED | FULL | 9 | The encoding captures exactly what FilterToCalcRule rewrites: `before()` is the bare `Filter(cond, R)`, and `after()` is the Calc the rul... |
| `FilterWindowTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 15 | The encoding is a faithful, honest special case: since QED cannot model Window at all (no bag semantics, no JSON/prover support), modelin... |
| `FullToLeftAndRightJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 11 | The encoding mirrors the source rule's exact shape — before() is the FULL join, after() is (LEFT join) UNION ALL ((RIGHT join) filtered b... |
| `IntersectReorder` | Apache Calcite | ✅ PROVED | PARTIAL | 8 | before (A∩B∩C) and after (C∩A∩B) differ by a genuine 3-cycle permutation of three distinct uninterpreted inputs sharing one type, so the ... |
| `IntersectToExists` | Apache Calcite | ✅ PROVED | PARTIAL | 31 | The encoding faithfully mirrors the rule's actual transformation — INTERSECT with all=false ⟹ a SEMI-correlate (the decorrelated EXISTS f... |
| `IntersectToSemiJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 8 | The pattern is non-vacuous and matches the source rule's binary step: set INTERSECT over independent uninterpreted A and B is rewritten t... |
| `JoinAddRedundantSemiJoin` | Apache Calcite | ✅ PROVED | FULL | 12 | The encoding exactly mirrors the source rule's transformation: before = X INNER⋈_C Y, after = (X SEMI⋈_C Y) INNER⋈_C Y, with the same uni... |
| `JoinAggregateTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 36 | The encoding mirrors the source rule's exact plan shape — Aggregate(below an INNER join) ⟹ INNER join under an Aggregate whose group set ... |
| `JoinAssociate` | Apache Calcite | ✅ PROVED | PARTIAL | 34 | The encoding correctly re-associates ((A⋈B)⋈C)→(A⋈(B⋈C)) with INNER joins, faithfully splitting conditions by A-reference (PAB/PABC on to... |
| `JoinCommute` | Apache Calcite | ✅ PROVED | PARTIAL | 39 | before() is Join(L, R, INNER, P(l, r)) and after() is Join(R, L, INNER, P(l, r)) with the references correctly remapped to positions (1, ... |
| `JoinConditionExpandIsNotDistinctFrom` | Apache Calcite | ✅ PROVED | PARTIAL | 65 | The encoding is a faithful, non-degenerate special case: before (INNER join on IS_NOT_DISTINCT_FROM) and after (INNER join on the 3-value... |
| `JoinConditionPush` | Apache Calcite | ✅ PROVED | PARTIAL | 66 | The proof is not vacuous and the encoding is sound: before() and after() differ structurally, and reusing the same uninterpreted predicat... |
| `JoinDeriveIsNotNullFilter` | Apache Calcite | ✅ PROVED | PARTIAL | 10 | The rewrite is nontrivial and matches the source's inner-join transformation by adding IS NOT NULL filters implied by the null-rejecting ... |
| `JoinExtractFilter` | Apache Calcite | ✅ PROVED | FULL | 8 | <1-3 sentences ...> ``` No code fences? It shows with code block? It says Reply with exactly this format (nothing else): ``` ... ``` Prob... |
| `JoinOnUniqueToSemiJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 81 | The encoding mirrors the source rule's INNER case exactly — Project(Left)∘(L ⋈_{l.c=r.c} R) ⟹ Project(Left)∘(L ⋉_{l.c=r.c} R) — with befo... |
| `JoinProjectTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 10 | The before/after plans are structurally different (Project below the Join in before, Project above it in after), with correct symbol shar... |
| `JoinPushExpressions` | Apache Calcite | ✅ PROVED | PARTIAL | 37 | before() and after() are genuinely structurally different and differ in exactly the way the real rule rewrites — a bare inner join versus... |
| `JoinPushThroughJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 7 | The encoding faithfully captures the RIGHT instance of JoinPushThroughJoin: before is (A ⋈_{SA∧SB} B) ⋈_{ST∧TC} C with layout (A,B,C), an... |
| `JoinPushTransitivePredicates` | Apache Calcite | ✅ PROVED | PARTIAL | 27 | The rewrite is non-vacuous — `after()` genuinely adds `Filter(p, LeftInput)` on top of the same join — and the equivalence it proves (`x ... |
| `JoinReduceExpressions` | Apache Calcite | ✅ PROVED | PARTIAL | 32 | The encoding faithfully mirrors JoinReduceExpressionsRule's actual transformation — the rule rewrites via `join.copy(traitSet, reducedCon... |
| `JoinToCorrelate` | Apache Calcite | ✅ PROVED | PARTIAL | 1 | Hand-implemented by the harness operator after 5 automated rounds all crashed on LLM context-overflow before ever calling try_rule (see r... |
| `JoinToHyperGraph` | Apache Calcite | ✅ PROVED | PARTIAL | 23 | The encoding is a faithful, non-trivial special case: before is the left-deep tree (A⋈_{pAB}B)⋈_{pAC∧pBC}C and after is the right-deep tr... |
| `JoinToMultiJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 23 | The encoding faithfully captures the core INNER-join 3-way flattening: `before` nests Fm at the inner join (Y⋈_Fm Z) with C at the outer ... |
| `JoinToSemiJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 37 | The encoding is non-vacuous and faithfully captures the rule's core rewrite — an INNER join of L with (R GROUP BY the join key), projecte... |
| `JoinUnionTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 33 | The encoding is a faithful, non-vacuous instance of the rule: before() = (X ∪ALL Y) ⋈_C O vs after() = (X ⋈_C O) ∪ALL (Y ⋈_C O) is a genu... |
| `LoptOptimizeJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 0 |  |
| `MarkToSemiOrAntiJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 45 | The union-all expansion of LEFT_MARK into marker-tagged semi and anti branches is bag-exactly faithful, and before/after are structurally... |
| `MinusToAntiJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 5 | The encoding reproduces the rule's 2-way transformation exactly — set-minus (all=false) rewritten to an ANTI join on IS NOT DISTINCT FROM... |
| `MultiJoinOptimizeBushy` | Apache Calcite | ✅ PROVED | PARTIAL | 33 | The encoding is non-vacuous and symbolically sound: before() is the left-deep chain (((A⋈B)⋈C)⋈F D) and after() is the genuinely bushy (A... |
| `MultiJoinProjectTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 7 | The encoding faithfully captures the LEFT_PROJECT variant's core rewrite — pulling a (pure, uninterpreted) projection up through an inner... |
| `OuterJoinToAntiJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 1 | Hand-implemented by the harness operator. This is the same LEFT-join/non-nullable-right-column special case worked out across this rule's... |
| `ProjectAggregateMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 36 | The encoding is non-vacuous and correctly structured: before() computes an unused third aggregate per group and projects around it (field... |
| `ProjectCalcMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 9 | The encoding faithfully desugars the Project-over-Calc match (a Calc as filter(B)∘project(E0,E1), exact because the fixed shape has no lo... |
| `ProjectCorrelateTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 62 | The encoding is a genuine, non-vacuous special case: before() projects above a 4-column cross product while after() prunes each input to ... |
| `ProjectFilterTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 39 | The encoding is a faithful fixed-shape instantiation of the rule's whole-expressions mode: before() = Filter(P(E(x,y), y), S) → Project(E... |
| `ProjectJoinRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 24 | The encoding is a non-vacuous, correctly-modeled special case of the source rule: it captures the LEFT-join self-join-on-unique-key form ... |
| `ProjectJoinTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 10 | The encoding is a faithful, non-vacuous special case: `before()` (inner join of raw scans with condition C(TL(l),TR(r)) and top projectio... |
| `ProjectMerge` | Apache Calcite | ✅ PROVED | FULL | 6 | `before()` (a Project stacked on a Project) and `after()` (a single Project) are structurally distinct, so the proof is non-vacuous and m... |
| `ProjectReduceExpressions` | Apache Calcite | ✅ PROVED | PARTIAL | 21 | This is a genuine, non-vacuous special case of the real rule: ProjectReduceExpressionsRule's simplification pass (RexSimplify.simplifyPre... |
| `ProjectRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 41 | The encoding faithfully and non-vacuously captures the rule's core transformation: `before()` is a Project whose sole expression is `sour... |
| `ProjectSetOpTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 68 | The encoding faithfully captures the core semantics of ProjectSetOpTranspose: a top-level uninterpreted projection F is pushed below a UN... |
| `ProjectTableScan` | Apache Calcite | ✅ PROVED | PARTIAL | 6 | `before()` (Project over Scan) and `after()` (Project over Project over Scan) are structurally distinct, and the proved equivalence is ex... |
| `ProjectToCalc` | Apache Calcite | ✅ PROVED | PARTIAL | 8 | The encoding faithfully maps the rule's two node shapes — `before()` is the bare `Project([E1,E2], S)` (the `LogicalProject`), and `after... |
| `ProjectToSemiJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 26 | The encoding faithfully captures the INNER-join branch of Calcite's ProjectToSemiJoinRule: before() is a left-fields-only Project over L ... |
| `ProjectWindowTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 8 | The encoding is a genuine, non-vacuous PARTIAL instance of the rule: it models a whole-partition, single-group window as an inner equi-jo... |
| `PruneEmpty` | Apache Calcite | ✅ PROVED | PARTIAL | 61 | The encoding is a faithful, non-vacuous rendering of Calcite's `RemoveEmptySingleRuleConfig.FILTER` instance (a Filter over an empty `Val... |
| `PruneSingleValue` | Apache Calcite | ✅ PROVED | PARTIAL | 62 | The encoding is non-vacuous and matches the source rule's core inner-join transformation exactly: `O ⋈_{jcond} Values{true}` is rewritten... |
| `RemoveEmptySingle` | Apache Calcite | ✅ PROVED | PARTIAL | 10 | The encoding faithfully captures the AGGREGATE variant of RemoveEmptySingleRule — a non-grand-total aggregate (group set non-empty, match... |
| `SemiJoinFilterTranspose` | Apache Calcite | ✅ PROVED | FULL | 16 | The encoding faithfully captures the original rule: before() is SemiJoin(Filter(X, F), Y, C) and after() is Filter(SemiJoin(X, Y, C), F),... |
| `SemiJoinJoinTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 66 | The encoding is faithful: before() = (X ⋈_C Y) ⋉_S Z and after() = (X ⋉_S Z) ⋈_C Y are structurally different plans whose bag-equality is... |
| `SemiJoinProjectTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 80 | The encoding faithfully mirrors the rule's semi-join branch: before() = SemiJoin(Project([P0,P1], X), Y, C) and after() = Project([P0,P1]... |
| `SemiJoinRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 9 | The encoding faithfully mirrors the source rule's transformation — replacing a SEMI join with its left input (`getInput(0)`), with correc... |
| `SetOpToFilter` | Apache Calcite | ✅ PROVED | PARTIAL | 11 | The encoding exactly captures Calcite's rewrite for the single-source, two-filter case — `Union(DISTINCT, [σ_P1(S), σ_P2(S)])` → all-fiel... |
| `SubQueryRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 87 | The encoding faithfully mirrors Calcite's uncorrelated-EXISTS-under-Filter (logic=TRUE) branch: before is `Filter(EXISTS(S), R)` and afte... |
| `UnionEliminator` | Apache Calcite | ✅ PROVED | FULL | 10 | The encoding exactly mirrors the rule's matches condition for the union variant — a set op with all=true over exactly one input (input.un... |
| `UnionMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 5 | The encoding faithfully reproduces Calcite's UnionMerge onMatch for the UNION ALL instance — top union with a nested union in the second ... |
| `UnionPullUpConstants` | Apache Calcite | ✅ PROVED | PARTIAL | 34 | The proof is of a real, nontrivial declared special case: a shared boolean-constant projection duplicated under a union-all is hoisted ab... |
| `UnionToDistinct` | Apache Calcite | ✅ PROVED | PARTIAL | 7 | The encoding matches the rule's exact shape: `before()` is a UNION DISTINCT (`union(false)`) over two independent uninterpreted inputs, a... |
| `UnionToValues` | Apache Calcite | ✅ PROVED | PARTIAL | 24 | `before()` (a 3-way UNION ALL of three constant `Values` nodes) and `after()` (a single `Values`) are structurally different plans, and t... |
| `UnnestDecorrelate` | Apache Calcite | ✅ PROVED | PARTIAL | 40 | The encoding faithfully captures the rule's semantic claim: the INNER correlate over a single-row Values feeding a correlated uncollect i... |
| `ValuesReduce` | Apache Calcite | ✅ PROVED | PARTIAL | 42 | The encoding is a genuine, non-vacuous trace of the rule's PROJECT config — Project([c,a]) over the 2-row Values (1,2,3),(4,5,6) folds to... |
| `WindowReduceExpressions` | Apache Calcite | ✅ PROVED | PARTIAL | 0 |  |
| `AggregateCaseToFilter` | Apache Calcite | ⏭️ SKIPPED | — | 22 | QED models aggregates as uninterpreted bag functions and, per its stated limitation, "knows nothing about a specific aggregate function's... |
| `AggregateFilterToCase` | Apache Calcite | ⏭️ SKIPPED | — | 0 | The before-side is an aggregate call carrying a SQL `FILTER (WHERE ...)` clause. `RelRN.AggCall` has no filter field, `Aggregate.semantic... |
| `AggregateFilterToFilteredAggregate` | Apache Calcite | ⏭️ SKIPPED | — | 25 | The rule's after-side is a per-call SQL `FILTER (WHERE P)`, which the pipeline cannot carry: `RelRN.AggCall` has no filter field and `Agg... |
| `AggregateGroupingSetsToUnion` | Apache Calcite | ⏭️ SKIPPED | — | 26 | The rule's only match is a non-SIMPLE aggregate (real GROUPING SETS/CUBE/ROLLUP), but RuleScript's `Aggregate` and QED's serialized `grou... |
| `AggregateMeasure` | Apache Calcite | ⏭️ SKIPPED | — | 47 | The rule's validity rests on Calcite's measure invariants — that a measure is constant within the group-key partition and that AGG_M2V(c)... |
| `AggregateMeasure2` | Apache Calcite | ⏭️ SKIPPED | — | 25 | The rule's validity rests entirely on Calcite's measure invariant — AGG_M2V(m) over a group equals the scalar subquery produced by mq.exp... |
| `AggregateMinMaxToLimit` | Apache Calcite | ⏭️ SKIPPED | — | 21 | The rule's after side (scalar subqueries of the form SELECT c FROM t WHERE c IS NOT NULL ORDER BY c ASC/DESC LIMIT 1 over a one-row VALUE... |
| `AggregateProjectStarTable` | Apache Calcite | ⏭️ SKIPPED | — | 11 | The rule's `after` plan swaps the base-table scan for a scan of a *different* pre-aggregated materialized table chosen from the external ... |
| `AggregateReduceFunctions` | Apache Calcite | ⏭️ SKIPPED | — | 27 | Every non-degenerate branch of AggregateReduceFunctionsRule is an algebraic identity between distinct aggregate functions (AVG=SUM/COUNT,... |
| `AggregateReduceFunctionsOnGroupKeys` | Apache Calcite | ⏭️ SKIPPED | — | 24 | The only substantive transformation in this rule is replacing aggregate calls (MAX/MIN/AVG/ANY_VALUE on a group key, or constant-value ag... |
| `AggregateRemoveDuplicateKeys` | Apache Calcite | ⏭️ SKIPPED | — | 22 | The rule's essential step replaces a dropped group key with an ANY_VALUE aggregate call over the coarser grouping, so the proof requires ... |
| `AggregateRemoveLiteralAgg` | Apache Calcite | ⏭️ SKIPPED | — | 22 | The rule's soundness rests entirely on Calcite's internal semantics that `LITERAL_AGG(lit)` returns `lit` for every group, which is a spe... |
| `AggregateStarTable` | Apache Calcite | ⏭️ SKIPPED | — | 7 | Every branch of this rule (exact match, roll-up, or measure reordering) rewrites `Aggregate(base)` into a plan over a *different* scan wh... |
| `CalcSplit` | Apache Calcite | ⏭️ SKIPPED | — | 24 | CalcSplitRule's correctness rests purely on the definitional identity that a fused Calc operator equals its Filter-then-Project decomposi... |
| `CalcToWindow` | Apache Calcite | ⏭️ SKIPPED | — | 21 | CalcToWindow's before pattern necessarily contains a RexOver window expression and its after pattern necessarily produces a LogicalWindow... |
| `CoerceInputs` | Apache Calcite | ⏭️ SKIPPED | — | 39 | QED serializes every scalar operator — including any proposed "cast" symbol — as an uninterpreted function with no identity/transparency ... |
| `CombineSimpleEquivalence` | Apache Calcite | ⏭️ SKIPPED | — | 21 | The rewrite introduces a new spool table and then consumes it, but QED treats table scans as independent input bags with no let/view/spoo... |
| `CorrelateUncollectOuter` | Apache Calcite | ⏭️ SKIPPED | — | 64 | The claim holds: the rewrite's only soundness argument is that Uncollect(isOuter=true) over the single-row LogicalValues guarantees at le... |
| `ExchangeRemoveConstantKeys` | Apache Calcite | ⏭️ SKIPPED | — | 13 | The rule's entire correctness content is physical placement — hashing on a constant-valued key puts every row in one bucket, so that key ... |
| `FilterDateRange` | Apache Calcite | ⏭️ SKIPPED | — | 23 | FilterDateRange's soundness rests on the concrete calendar semantics of EXTRACT/FLOOR/CEIL and date literals — the claim that {t | EXTRAC... |
| `FilterHilbert` | Apache Calcite | ⏭️ SKIPPED | — | 22 | The rule's soundness is exactly the entailment "under h = HILBERT(lon, lat), ST_DWithin/ST_Contains(g, ST_Point(lon, lat), d) ⇒ (h BETWEE... |
| `FilterSampleTranspose` | Apache Calcite | ⏭️ SKIPPED | — | 9 | The validity of Filter(c, Sample(R)) ⟹ Sample(Filter(c, R)) rests on sampling being a per-row, probabilistic selection whose keep/drop de... |
| `FilterSortMeasure` | Apache Calcite | ⏭️ SKIPPED | — | 8 | As written, Calcite's `FilterSortMeasureRule` is dead code: `onMatch` assigns `condition = filter.getCondition()` and then guards on `con... |
| `FilterSortTranspose` | Apache Calcite | ⏭️ SKIPPED | — | 7 | The rule's entire correctness content is that filtering a sorted stream preserves relative row order — a list/ordering property — and QED... |
| `FilterTableScan` | Apache Calcite | ⏭️ SKIPPED | — | 6 | FilterTableScanRule is a physical push-down whose correctness rests entirely on the FilterableTable/BindableTableScan execution contract ... |
| `IntersectToDistinct` | Apache Calcite | ⏭️ SKIPPED | — | 28 | Both variants of IntersectToDistinct are correct only by COUNT's cardinality algebra — `COUNT(*) FILTER (WHERE i=k) > 0` means "branch k ... |
| `JoinExpandOrToUnion` | Apache Calcite | ⏭️ SKIPPED | — | 66 | The porter's encoding was verified faithful — the serialized JSON shows the identical uninterpreted symbols c1/c2 applied to the same joi... |
| `Match` | Apache Calcite | ⏭️ SKIPPED | — | 28 | Calcite's `LogicalMatch` is an ordering/frame-dependent CEP operator (pattern matching over an ordered stream with measures/interval), so... |
| `MaterializedViewFilterScan` | Apache Calcite | ⏭️ SKIPPED | — | 58 | MaterializedViewFilterScan's soundness rests on the cross-table invariant that the MV scan's bag equals its defining query over the base ... |
| `MaterializedViewOnlyAggregate` | Apache Calcite | ⏭️ SKIPPED | — | 28 | The rule's sole correctness premise is the registration-time invariant that a separately registered, externally refreshed MV table's cont... |
| `MaterializedViewOnlyFilter` | Apache Calcite | ⏭️ SKIPPED | — | 9 | This is not an algebraic equivalence between two plans but a catalog-driven rewrite whose soundness rests on the external planner invaria... |
| `MaterializedViewOnlyJoin` | Apache Calcite | ⏭️ SKIPPED | — | 29 | The rewrite substitutes a query join subplan with a scan of a separately registered MV table (cross-joined with query-only tables), and i... |
| `MaterializedViewProjectAggregate` | Apache Calcite | ⏭️ SKIPPED | — | 21 | The rule replaces Project(Aggregate(Scan T)) with a scan of the MV's physical table, and its soundness rests on an external database inva... |
| `MaterializedViewProjectFilter` | Apache Calcite | ⏭️ SKIPPED | — | 23 | The rule's before pattern (Project over Filter over base tables) and after pattern (projection/filter over the MV scan) are individually ... |
| `MaterializedViewProjectJoin` | Apache Calcite | ⏭️ SKIPPED | — | 14 | The rule's soundness rests entirely on the planner-maintained invariant that the MV's physical table holds exactly the bag produced by it... |
| `MinusToDistinct` | Apache Calcite | ⏭️ SKIPPED | — | 10 | The rewrite's correctness rests entirely on reading membership off aggregate outputs — `COUNT() FILTER tag=i` > 0 / == 0 testing whether ... |
| `MinusToFilter` | Apache Calcite | ⏭️ SKIPPED | — | 45 | I independently verified the rewrite is mathematically sound, but any faithful encoding forces QED to relate its EXCEPT (set-minus) primi... |
| `ProjectJoinJoinRemove` | Apache Calcite | ⏭️ SKIPPED | — | 105 | The rewrite's row-level validity hinges on B's key being unique AND the top join condition referencing only the shared A-key column plus ... |
| `ProjectMeasure` | Apache Calcite | ⏭️ SKIPPED | — | 41 | ProjectMeasureRule's rewrite (Aggregate(g, SINGLE_VALUE(d)) over a Project with d = M2X(V2M(e)) AS d) is only sound because of the measur... |
| `ProjectMultiJoinMerge` | Apache Calcite | ⏭️ SKIPPED | — | 23 | Calcite's `ProjectMultiJoinMerge` leaves the MultiJoin's inputs, join conditions, row type, and even the top-level project (reused verbat... |
| `ProjectOverSumToSum0` | Apache Calcite | ⏭️ SKIPPED | — | 23 | The rewrite operates entirely on window aggregates (`SUM(x) OVER (frame)` → `SUM0(x) OVER (frame)`), and QED has no bag-semantic model fo... |
| `ProjectSortMeasure` | Apache Calcite | ⏭️ SKIPPED | — | 34 | The rule's only non-vacuous content is that re-emitting the Sort (identical sortExps/offset/fetch) below the inserted M2V projection pres... |
| `ProjectToLogicalProjectAndWindow` | Apache Calcite | ⏭️ SKIPPED | — | 10 | This rule's payload is the identity between an inline `RexOver` (windowed aggregate) inside a `Project` and a separate `LogicalWindow` op... |
| `ReduceDecimals` | Apache Calcite | ⏭️ SKIPPED | — | 22 | Every sub-rewrite in this rule — including the simplest one, `REINTERPRET(REINTERPRET(x)) → x` — rests on a value-level identity between ... |
| `SampleToFilter` | Apache Calcite | ⏭️ SKIPPED | — | 6 | Bernoulli `Sample`'s output is a random per-row selection, not a deterministic function of its input, so it has no bag semantics in QED's... |
| `SortJoinCopy` | Apache Calcite | ⏭️ SKIPPED | — | 8 | SortJoinCopyRule's entire contract is row-ordering: it decomposes the top Sort's collation across the join's two sides, consults "already... |
| `SortJoinTranspose` | Apache Calcite | ⏭️ SKIPPED | — | 23 | SortJoinTranspose is meaningful only through ordering semantics: the outer Sort/Offset/Fetch must be preserved while a derived Fetch/Limi... |
| `SortMerge` | Apache Calcite | ⏭️ SKIPPED | — | 9 | SortMerge's soundness is purely a top-N/row-position statement — it must know which rows each LIMIT keeps and, even in the degenerate sub... |
| `SortProjectTranspose` | Apache Calcite | ⏭️ SKIPPED | — | 9 | The rule's soundness is purely an order-preservation claim: a permutation (plus monotonicity-preserving casts) over a stream sorted by co... |
| `SortRemove` | Apache Calcite | ⏭️ SKIPPED | — | 9 | SortRemoveRule is sound only under the non-bag premise that the child already emits rows in the Sort's collation (a planner trait fact), ... |
| `SortRemoveConstantKeys` | Apache Calcite | ⏭️ SKIPPED | — | 6 | The rule is sound only because dropping a sort key that the input's predicates prove constant preserves the output row *ordering* — and Q... |
| `SortRemoveDuplicateKeys` | Apache Calcite | ⏭️ SKIPPED | — | 10 | Both plans sort the same input, so their row bags are identical by construction; the rule's only non-trivial claim is that dropping a key... |
| `SortRemoveRedundant` | Apache Calcite | ⏭️ SKIPPED | — | 22 | The rewrite is conditional on a cost-model fact — the input's max row count being ≤ 1 (ORDER BY) or ≤ fetch (pure LIMIT) — and RuleScript... |
| `SortUnionTranspose` | Apache Calcite | ⏭️ SKIPPED | — | 5 | The rule’s correctness depends on row-order/top-N semantics: each union branch is capped at `offset + fetch` only because the outer `Sort... |
| `TableScan` | Apache Calcite | ⏭️ SKIPPED | — | 24 | TableScanRule's entire correctness content is the identity "the bag of table V equals the relation produced by V's RelOptTable.toRel" (it... |

## Details

### `AggregateExpandDistinctAggregates` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateExpandDistinctAggregatesRule.java
- Attempts used: 72
- Last updated: 2026-09-22T16:57:03.593558+00:00
- Reason / notes: The encoding faithfully mirrors the source rule's `convertMonopole` branch: `before()` is `GROUP BY k` of two distinct calls `f(DISTINCT x), g(DISTINCT x)`, and `after()` makes them non-distinct on top of an intermediate `GROUP BY k, x` (no calls) that is exactly the source's `SELECT DISTINCT`-via-aggregate ded [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 114404667}, panicked=False

### `AggregateExpandWithinDistinct` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateExpandWithinDistinctRule.java
- Attempts used: 24
- Last updated: 2026-10-13T16:53:17.548093+00:00
- Reason / notes: The encoding is non-vacuous (a single COUNT(DISTINCT x) aggregate vs. a nested dedup-then-count plan), and tracing the source rule on the no-grouping, single `COUNT(*) WITHIN DISTINCT (x)` input (empty arg list, no filter, one grouping set ⇒ no GROUPING()/MIN/MAX/throw clauses are emitted) shows it produces exactly this dedup-then-count shape, with the input written as COUNT(DISTINCT x) — the documented-identical form that the DSL can express since AggCall has no distinctKeys/filter fields. The before/after pair also holds under real bag+NULL semantics (both sides count distinct non-null x: the outer count takes the dedup key as operand, so a NULL group is ignored just as in COUNT(DISTINCT x)), and the PARTIAL scope line honestly and specifically states the assumed restriction (no grouping, one call); full-rule generality (grouping sets, GROUPING(), filters, THROW_UNLESS) is genuinely inexpressible in the current DSL, so the special case is a legitimate, non-degenerate result.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 82157166}, panicked=False

### `AggregateExtractProject` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateExtractProjectRule.java
- Attempts used: 14
- Last updated: 2026-09-22T10:27:58.038083+00:00
- Reason / notes: The encoding faithfully mirrors the source rule's actual computation: it computes the used columns (group col 0, agg arg col 1), projects them in ascending order below the aggregate, remaps the group key and agg operand to the projected columns (0→0, 1→1), and shares the same uninterpreted aggregate operator `f` and types across both sides, while before() and after() are structurally different (the projection genuinely drops the unused third column), so the proof is non-vacuous. The fixed 3-column / one-group-key / one-non-distinct-agg shape and the wide base modeled as a cross-join of three one-column scans are inherent to the DSL (scans are single-column and Aggregate's group/agg Seqs have fixed length), are honestly and specifically disclosed in the SCOPE: PARTIAL line, and the cross-join base exploits no accidental algebra since the equivalence holds for any base relation by per-group multiset preservation under projection. No missing semantic preconditions (the source rule's "input is not a Project" clause is a cycle-prevention matching constraint, not a semantic side condition), no spurious symbol sharing, and the remapping matches the original rule's inverse-surjection mapping exactly. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 96821708}, panicked=False

### `AggregateFilterTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateFilterTransposeRule.java
- Attempts used: 9
- Last updated: 2026-09-22T10:12:35.906990+00:00
- Reason / notes: The encoding faithfully captures Case 1 of the source rule: the uninterpreted predicate depends only on the group key (i.e. all filter columns are in the group set), the aggregate is a simple group-by with the same group set and agg call on both sides, and the filter genuinely moves from below to above the aggregate, so the proof is non-vacuous and matches the rule's transformation exactly. The symbols are shared correctly — the same `p` and `f` occur in both patterns, and the after-side filter references the aggregate's group-key output column, which is precisely what the rule's column mapping does when the group set is unchanged; the source rule's uniqueness check is a termination/rule-firing guard, not a semantic precondition, so its absence doesn't weaken the equivalence claim. The stated PARTIAL scope (one group key, two-column input, one non-distinct call) is honest, specific, and non-degenerate: fixed-shape patterns with uninterpreted symbols are all this DSL can express, and the essential semantic condition (predicate over group keys only) is captured, so the narrower arity is an inherent language limitation rather than a hard-coded concrete value.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 93033833}, panicked=False

### `AggregateJoinJoinRemove` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateJoinJoinRemoveRule.java
- Attempts used: 50
- Last updated: 2026-09-25T11:17:28.269863+00:00
- Reason / notes: The encoding is a faithful, honestly-tagged PARTIAL instance of the rule — pure DISTINCT on (A.col, C.col) over single-column inputs, exactly the rule's own doc example — and all of the rule's real preconditions hold structurally in it: the group set and top condition never reference the removed B (BR) column, both joins are LEFT (the rule's only join kinds), and the left-key sets are equal (both {A.col}), with B's uniqueness correctly NOT assumed (plain non-unique scans). Symbol sharing is correct rather than coincidentally over-constraining: top_cond is deliberately shared between before/after with the same index remapping (0,2)→(0,1) that the source rule performs via RexUtil.shift, while bottom_cond remains an independent uninterpreted predicate exactly as the source rule treats it (only its referenced left columns are constrained, its predicate itself is not). before() genuinely contains the extra A ⋈_LEFT B join that after() eliminates, so the QED proof is non-vacuous and establishes the rule's actual claim — distinct(A,C) over (A⋈B)⋈C equals distinct(A,C) over A⋈C under independent uninterpreted conditions — for this non-degenerate fragment, with the narrowing (no aggregate calls, one column per table) specifically and truthfully stated in the SCOPE line. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 115958208}, panicked=False

### `AggregateJoinRemove` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateJoinRemoveRule.java
- Attempts used: 67
- Last updated: 2026-09-22T09:11:30.690550+00:00
- Reason / notes: The encoding correctly captures the core semantic content of AggregateJoinRemove's LEFT-join branch: a DISTINCT aggregate (group key on the preserved side only, no non-preserved column references, vacuously-satisfied all-distinct condition) over a LEFT JOIN is equivalent to the same aggregate over the left input alone. The before/after are structurally different (join present vs. absent), the proof is non-vacuous (requires reasoning about left-join row preservation combined with group-by idempotence), the join condition is properly uninterpreted, symbol sharing is correct (left appears in both sides as the same table, right only in before), and no spurious preconditions (keys, NOT NULL) are introduced. The scope line honestly and specifically enumerates every restriction (LEFT-only, one-column scans, single group key, no aggregate calls); the one-column limitation is a genuine DSL constraint (the Scan constructor always emits a single-column table), the LEFT-only choice is inherent to the single before/after format, and the no-agg-calls restriction reflects QED's inability to reason about DISTINCT aggregate algebra — so the narrowing is to a provable core rather than an avoidable omission. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 77373667}, panicked=False

### `AggregateJoinTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateJoinTransposeRule.java
- Attempts used: 126
- Last updated: 2026-09-22T18:36:14.690113+00:00
- Reason / notes: Hand-applied by harness operator: added additive RelRN.scanMany/ScanMany multi-column scan, encoded the DEFAULT-config (no-agg-function) group-by-through-join fragment per the round-5 verifier's own concrete suggestion. 0 regressions across 27 baseline rules; LLM auditor verdict SAFE.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 27863916}, panicked=False

### `AggregateMerge` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateMergeRule.java
- Attempts used: 37
- Last updated: 2026-09-22T08:20:54.996874+00:00
- Reason / notes: The encoding is a genuine, non-vacuous instance of the source rule: when the top aggregate has no aggregate calls, Calcite's onMatch skips the call-merging loop entirely and emits exactly the encoded after() — a single aggregate over the bottom's input, grouped by the top's (permuted) group keys, with the bottom's unreferenced calls dropped — so the proved equivalence AGG_{k1}(AGG_{k1,k2; v(x)}(S)) ≡ AGG_{k1}(S) is the real two-level-collapse transformation with correct symbol sharing (k1 reused between bottom's first group key and the merged key, which is load-bearing; v dropped because no top call references it). The PARTIAL tag honestly and specifically lists every assumption (no top calls, exactly one bottom call, 2→1 group keys, one-column scan input), and excluding the call-merging half (SUM-of-SUM, etc.) is forced by QED's fundamental inability to reason about aggregate-function algebra with uninterpreted symbols, not by a missing DSL capability, so this is the maximal provable core of the rule rather than an avoidable narrowing.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 82287750}, panicked=False

### `AggregateProjectConstantToDummyJoin` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateProjectConstantToDummyJoinRule.java
- Attempts used: 62
- Last updated: 2026-09-23T04:44:55.227535+00:00
- Reason / notes: The encoding mirrors the source rule's onMatch exactly — INNER join of the input with a one-row values table on constant true, a project restoring the original column order (constant columns re-sourced from the dummy relation, the identity column from the input), and an aggregate with the identical group set and the same uninterpreted aggregate "f" on the same column, so the proved equivalence is the genuine, non-vacuous transformation (before has no join/values, after does). The only shared symbol is the aggregate function name (intended symbol reuse), the join kind INNER is what the source hard-codes, concrete boolean literals are valid instances of RexLiteral since the rule's validity is independent of the literal values, and no precondition is missing (no uniqueness/NOT NULL is required by the source; literals are non-nullable on both sides, S is nullable on both). The SCOPE: PARTIAL tag is accurate and specific to the code (two boolean constant columns + one identity column, group by all three, one call), and since this DSL encodes fixed plan shapes rather than parametric arity, the narrowing is an honest, realizable, non-degenerate instance of the rule rather than a misleading one. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 83528875}, panicked=False

### `AggregateProjectMerge` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateProjectMergeRule.java
- Attempts used: 68
- Last updated: 2026-09-22T09:57:40.682439+00:00
- Reason / notes: The encoding is a line-faithful instance of the source rule's `apply`: for the swap project `[x1, x0]` the interesting-field map is `{0→1}`, yielding `newGroupSet = {1}` and remapped call `f(1)`, and since the mapped key order `[1]` equals `newGroupSet.asList()` the rule emits no trailing project — exactly the `after()` given, while `before()` (Aggregate over Project over Join) differs structurally, so the proof is non-vacuous. The narrowings (two-column base as `A ⋈ B` because DSL scans are single-column, a bijective swap rather than an arbitrary input-ref project, one group key and one call with operand = key) are inherent to RuleScript's fixed-shape encodings, are accurately declared on the `// SCOPE: PARTIAL` line, and don't concretize anything that should be an uninterpreted symbol (the INNER/true join is mere base construction, absent from the real rule); symbol sharing (`f`, `A`, `B`) is correct, and the rule's input-ref precondition is satisfied by construction, so no failure mode (triviality, wrong operators, sharing error, missing precondition, dishonest scope) is present.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 91540333}, panicked=False

### `AggregateProjectPullUpConstants` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateProjectPullUpConstantsRule.java
- Attempts used: 63
- Last updated: 2026-09-22T17:06:18.994233+00:00
- Reason / notes: The encoding faithfully captures the rule's core transformation: an aggregate whose leading group key is a constant column is rewritten to an aggregate with that key dropped (group set kept non-empty, matching the rule's guard against an empty GROUP BY) plus a projection re-emitting the same literal in its original position, with the uninterpreted aggregate call shared unchanged and the original output column order preserved — and before/after are structurally different plans (2-key group-by vs 1-key group-by + project), so the proof is non-vacuous. Fixing the constant as a literal emitted by a Project directly below the aggregate is the only way to express "this column is constant" in QED (a pulled-up-predicate-deduced constant would require reasoning about a literal-equality constraint that the prover treats as opaque), so the stated PARTIAL scope is an honest, specific, genuine limitation rather than an avoidable under-generalization, and the specific literal value is immaterial since QED erases all types to integers. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 89852292}, panicked=False

### `AggregateRemove` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateRemoveRule.java
- Attempts used: 34
- Last updated: 2026-09-22T02:30:35.180701+00:00
- Reason / notes: `before()` (a SIMPLE group-by on column 0 with zero agg calls, i.e. SELECT DISTINCT) is structurally and semantically distinct from `after()` (the raw scan), and the rule's essential precondition — `areColumnsUnique(input, groupSet)` — is correctly modeled as the scan's declared key (`unique=true` → `key: [[0]]` in the serialized schema) rather than silently dropped, so the proof is a genuine, non-vacuous identity-over-a-key result. This is a faithful instance of the no-aggregate-calls branch of `AggregateRemoveRule` in the case where input and output field counts match (1 vs 1, so no trailing project is needed, exactly as the source rule would emit); every restriction — single-column base-table input, key on the sole group column, empty agg-call list, and the unmodeled splittable-function half (MAX(x)→x) — is precisely what the `SCOPE: PARTIAL` line states, and the input shape is forced by the DSL, which has no way to attach uniqueness guarantees to derived relations or multi-column scans. Symbol sharing and operator shapes are correct (one shared table symbol, one group column, SIMPLE group type with groupCount > 0, empty function list), so none of the triviality/wrong-operator/missing-precondition failure modes apply and the "provable" verdict is meaningful and honestly scoped.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 298167}, panicked=False

### `AggregateToSemiJoin` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRule.java

Note: SemiJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `AggregateToSemiJoinRule` variant (not JoinOnUniqueToSemiJoinRule, JoinToSemiJoinRule, ProjectToSemiJoinRule, which are separate rules ported under their own spec names).
- Attempts used: 23
- Last updated: 2026-09-25T10:05:55.654093+00:00
- Reason / notes: The encoding faithfully captures the rule's INNER transformation — `GROUP BY l.k (L INNER-JOIN (GROUP BY r.k R) ON l.k=r.k)` rewritten to `GROUP BY l.k (L SEMI-JOIN R ON l.k=r.k)` — and before() and after() are genuinely structurally different (inner join over the grouped right vs. semi join over the raw right), so the provable result is non-vacuous and the equi-key, "join keys = right group columns," and INNER-jointype preconditions are all correctly represented; the narrowing to INNER-only, pure group-by (zero agg calls), and single-key is honestly disclosed in the PARTIAL scope line while remaining a meaningful, non-degenerate instance of the real rule.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 92398292}, panicked=False

### `AggregateUnionAggregate` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateUnionAggregateRule.java
- Attempts used: 8
- Last updated: 2026-09-22T18:36:22.131201+00:00
- Reason / notes: The encoding is faithful and non-vacuous: `before()` truly differs from `after()` (an extra group-by-all, no-agg-call dedup beneath the second UNION ALL arm), and the proved identity dedup(A ⊎ dedup(L)) ≡ dedup(A ⊎ L) is exactly the algebraic content of Calcite's rule — UNION ALL (`all=true` as the source requires), top aggregate `isSimple` (group by all columns, no agg calls), bottom aggregate with no agg calls, and L/A are independent scans sharing only the row type the union itself mandates, so there is no triviality, wrong operator, sharing error, or silently dropped precondition. It is an honest, specific PARTIAL special case (single-column arms, inner dedup fixed to the second arm, and the bottom aggregate group-by-all rather than Calcite's allowed subset-group-set with an inserted Project), but the restriction is genuine rather than degenerate: the rule still removes a real aggregation, and the dropped generality (multi-column rows, which the current one-column `RelRN.scan` cannot express, and first-arm placement, which follows by UNION ALL symmetry) is what it is.

(This proof relies on an accepted RuleScript DSL extension made during this session — see the extended file(s) for what changed.)
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 68903375}, panicked=False

### `AggregateUnionTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateUnionTransposeRule.java
- Attempts used: 25
- Last updated: 2026-09-25T10:04:37.417369+00:00
- Reason / notes: The encoding faithfully represents the no-aggregate-call (DISTINCT / group-by-all) limit of `AggregateUnionTranspose` over UNION ALL, with structurally distinct before (dedup over union of A,B) and after (dedup over union of per-input dedups) that exactly mirrors what the real rule emits (empty `aggCallList`, empty `transformAggCalls`, top group-by-all), and this narrowing is genuinely forced by QED's fundamental inability to model aggregate-function algebra (COUNT→SUM0, MIN/MAX/SUM distribution) rather than a missing DSL builder; the two union inputs correctly share a single row type (a real UNION requirement, not an over-constraint), the `all=true` flag and dedup aggregate shape match the source, and the `// SCOPE: PARTIAL` line honestly and specifically states the no-calls / two-input / two-column assumptions, so the non-vacuous proof is neither trivial nor misleading.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 98375917}, panicked=False

### `AggregateValues` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateValuesRule.java
- Attempts used: 10
- Last updated: 2026-09-22T18:24:18.199365+00:00
- Reason / notes: The encoding faithfully captures the rule's dedup branch — a simple aggregate (group by the relation's only column, zero aggregate calls) over an uninterpreted scan is proven equivalent to its distinct-union (i.e. DISTINCT), and the two sides are structurally different, so the proof is non-vacuous. This is the genuinely provable half of the Calcite rule; the only unmodeled branch (empty-Values grand total) is correctly excluded in the PARTIAL scope because it requires aggregate-function algebra and empty-input semantics that QED does not model.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 64139500}, panicked=False

### `CalcMerge` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/CalcMergeRule.java
- Attempts used: 14
- Last updated: 2026-09-23T05:32:43.585392+00:00
- Reason / notes: The encoding faithfully captures CalcMerge's core transformation — the top Calc's condition and projections substituted over the bottom Calc's projection, with the two filters conjoined — and `before()` (filter→project→filter→project) genuinely differs from `after()` (single conjoined filter→project), so the proof is non-vacuous; bottom and top use independent symbols and the merged side correctly reuses the top's symbols while retaining the bottom's condition, so the substitution proven is the real one. The only narrowing is a fixed 2-column / 2-projection shape (the "over both columns" choice is a superset, not an under-generalization, since any subset-referencing expression is also a 2-arg function), which a single concrete-arity pattern fundamentally cannot generalize and which is accurately tagged `SCOPE: PARTIAL`. No triviality, wrong operator shape, harmful symbol-reuse, or silently-dropped precondition: the rule's no-windowed-aggregate guard is inherent in modeling the top's expressions as per-row uninterpreted functions, which window aggregates cannot be.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 63965583}, panicked=False

### `CalcReduceExpressions` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java

Note: ReduceExpressionsRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `CalcReduceExpressionsRule` variant (not FilterReduceExpressionsRule, JoinReduceExpressionsRule, ProjectReduceExpressionsRule, WindowReduceExpressionsRule, which are separate rules ported under their own spec names).
- Attempts used: 9
- Last updated: 2026-09-23T05:06:18.110098+00:00
- Reason / notes: The encoding faithfully captures the Calc branch where the condition is constant FALSE: before is a FALSE-filtered source projected by E1/E2, while after is an empty relation whose row type is taken from the same E1/E2 projection. The PARTIAL scope honestly identifies the real semantic restriction; the fixed two-column/two-expression arity is only the concrete RuleScript arity instance, and the proof is non-vacuous because before still contains the false filter/project.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1367125}, panicked=False

### `CalcRemove` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/CalcRemoveRule.java
- Attempts used: 5
- Last updated: 2026-09-23T05:32:22.520920+00:00
- Reason / notes: The encoding is faithful: before() is a ProjectMany with the identity field list over a scan, and after() is the bare scan — structurally distinct, so the proof is non-vacuous and correctly captures the core claim of CalcRemoveRule (identity projection with no filter is a no-op). The only restriction is fixed 2-column arity, which is an inherent structural limitation of the pattern DSL (no parametric arity exists), not a semantic under-generalization; the column types remain uninterpreted, and the SCOPE line accurately and specifically documents the restriction. No preconditions are missing, no symbols are mis-shared, and the operator shape (ProjectMany of in-order fields, no Filter) exactly matches the "trivial Calc" predicate in the source. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 356458}, panicked=False

### `DphypJoinReorder` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/DphypJoinReorderRule.java
- Attempts used: 9
- Last updated: 2026-09-23T06:17:55.990661+00:00
- Reason / notes: The PARTIAL scope line correctly acknowledges that this is a 3-way, fully-connected, inner-join instance rather than the full cost-driven algorithm, and within that scope the encoding is faithful: the before and after are genuinely different join trees sharing the same uninterpreted pairwise conditions and output column order. The proof is therefore non-vacuous and does not rely on accidental over-constraint or a hidden precondition.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 80800834}, panicked=False

### `ExpandDisjunctionForJoinInputs` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ExpandDisjunctionForJoinInputsRule.java
- Attempts used: 35
- Last updated: 2026-09-22T20:49:14.643173+00:00
- Reason / notes: before() and after() are structurally different — after() ANDs into the join condition the two per-side disjunctions (pl∨rl) and (pr∨sr) — and the equivalence is a genuine, non-vacuous theorem: each added disjunction is logically implied by the two-branch DNF condition (pl∧pr)∨(rl∧sr), but the implication is not syntactic, so the prover had to verify it for arbitrary instantiations. The encoding is faithful in its details: pl/rl reference only L's columns and pr/sr only R's (joinField ordinals 0–3 with a 2+2 split, verified against JoinField's left-column arithmetic), the four predicate symbols are independent as the rule needs, and INNER is inside the source rule's applicability (both canPush flags true for INNER, so both extras are legitimately added — no dropped precondition, and the bloat limit and no-op equality guard are heuristics, not semantic preconditions). The remaining narrowing — INNER only (LEFT/RIGHT variants add fewer extras), the join-condition matcher only rather than the filter-on-join matcher, and a fixed two-branch one-conjunct-per-side DNF rather than arbitrary DNF — is exactly what the single PARTIAL scope line claims, so this is an honestly labeled, non-degenerate special case of the real rule. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 71751833}, panicked=False

### `ExpandDisjunctionForTable` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ExpandDisjunctionForTableRule.java
- Attempts used: 33
- Last updated: 2026-09-23T05:53:57.614782+00:00
- Reason / notes: `before()` and `after()` are structurally distinct (after ANDs in the per-table disjuncts `(a∨c)` and `(b∨d)`), and the encoding uses properly distinct uninterpreted symbols — `a`,`c` on t1-only fields, `b`,`d` on t2-only fields, `e` cross-table — in the inner-join shape that mirrors the rule's actual transformation, so the proved identity (core ⇒ (a∨c)∧(b∨d)) is the genuine logical core of the rule and holds under bag semantics with no hidden PK/NOT NULL precondition. The narrowing to an INNER join of two plain scans with a two-branch, one-conjunct-per-table DNF is a real restriction versus the source (which matches any join type, Filters, and arbitrary disjunctions), but it is accurately and fully declared in the `SCOPE: PARTIAL` line, making the provable result an honest, non-vacuous special case rather than a misleading one.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 72386042}, panicked=False

### `FilterAggregateTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterAggregateTransposeRule.java
- Attempts used: 13
- Last updated: 2026-09-22T10:25:40.096368+00:00
- Reason / notes: The encoding is a non-vacuous, faithful instance of the rule's core transformation: the conjunct P depending only on the (identity) group key moves from the filter above the SIMPLE aggregate to a filter below it, while the conjuncts over the aggregate's output stay above, and P is correctly one shared uninterpreted symbol applied to the corresponding key column on both sides (modeling the rule's identity RexInputConverter). Operator usage and preconditions match the source — Filter-over-Aggregate shape, SIMPLE grouping, non-distinct uninterpreted aggregate, groupCount>0 and canPush's key-only condition built in, and per-key filtering commutes with group-by even under null semantics — with the two-column scan-join input being the standard DSL construction for a multi-column table (scans are single-column), not a fixed join the rule constrains. The narrowings (single identity group key, one aggregate call, one pushed conjunct) are genuine shape restrictions, honestly and specifically disclosed in the PARTIAL scope line, and since the proven equivalence is the exact semantic atom the rule applies per conjunct (the extra key-referencing remaining conjunct Qa is inert to P's movement), the provable verdict is meaningful rather than coincidental. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 100425542}, panicked=False

### `FilterCalcMerge` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterCalcMergeRule.java
- Attempts used: 11
- Last updated: 2026-09-23T06:45:19.676701+00:00
- Reason / notes: The encoding faithfully models Filter-over-Calc (with condition B and projections [E0,E1]) as Filter(T)∘Project∘Filter(B) ⟹ Project∘Filter(T(E0,E1)∧B), which is exactly what RexProgramBuilder.mergePrograms produces (top identity program + condition T merged with the bottom program), and the symbol reuse is correct and required: the same T, the same projections E0/E1, and the same bottom condition B must appear on both sides. The source rule's two guards (no windowed aggregates in the Calc, no subquery in the Filter) are structurally excluded by the uninterpreted filter+project model, so no precondition is silently dropped, and before()/after() are genuinely different shapes (a real filter-merge, not a vacuous identity). The PARTIAL scope is honest and specific — fixed 2-column/2-projection arity and a one-condition/two-projection Calc, all with uninterpreted (not hard-coded) predicates and projections — and the result is a non-degenerate, universally-proven special case rather than a coincidental over-constraint. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 67322500}, panicked=False

### `FilterCorrelate` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterCorrelateRule.java
- Attempts used: 128
- Last updated: 2026-09-23T06:32:20.737715+00:00
- Reason / notes: before() and after() are structurally different (side-local conjuncts pushed below the join while the cross-side conjunct stays above), the three predicate symbols and two scans are shared correctly and independently across the two patterns, and the successful proof itself rules out index miswiring — e.g. p_right pointing at the left column would not have proved equivalent. The shape matches FilterCorrelateRule's actual behavior for its INNER, non-correlated case (left-only → left, right-only → right, rest kept above), with INNER eliminating the null-generation preconditions the general rule handles via join-type flags, and since the DSL exposes no correlate operator the true-condition inner-join model preserves every semantic aspect the transformation depends on (predicate side-locality, bag semantics, no row loss), so the one-line PARTIAL scope tag is specific and honest rather than a degenerate or misleading narrowing.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 69803125}, panicked=False

### `FilterFlattenCorrelatedCondition` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterFlattenCorrelatedConditionRule.java
- Attempts used: 34
- Last updated: 2026-09-23T07:15:11.183416+00:00
- Reason / notes: It proves a non-vacuous, honestly scoped special case of the Calcite rewrite: one uninterpreted comparison between an outer column and an uninterpreted inner expression is transformed by hoisting that expression into a projection and referencing it by input ref. The shared Cmp/Expr symbols and the project/filter/project-back shape match the source rule’s core transformation, while the cross join models the correlated context because the DSL lacks a correlate builder.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 77974916}, panicked=False

### `FilterJoin` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterJoinRule.java
- Attempts used: 37
- Last updated: 2026-09-22T17:31:17.231734+00:00
- Reason / notes: before() and after() are structurally different (Filter above an inner join vs. the AND-composed join condition), and the symbol sharing is exactly right: the same `joinCond` and `aboveFilter` occur on both sides, they are two independent uninterpreted predicates over the join row (distinct operator names, same field list), and L/R are independent scans — so the proof is neither vacuous nor coincidentally over-constrained. For an INNER join, this merged form is bag-equivalent to what the source rule actually emits (it splits conjuncts into child filters + ON condition, which is semantically neutral under inner joins), and INNER is precisely the only join kind for which the unclassified "merge the whole filter into the condition" step is valid — matching the honest PARTIAL scope tag. The single-column scans are a DSL convention rather than a weakening, since both predicates are fully uninterpreted over the product row and the filter/join-commutation claim is width-independent. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 75058208}, panicked=False

### `FilterMerge` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterMergeRule.java
- Attempts used: 1
- Last updated: 2026-09-24T02:17:45.005280+00:00
- Reason / notes: (no verifier LLM configured — result not independently reviewed)
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 67062875}, panicked=False

### `FilterMultiJoinMerge` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterMultiJoinMergeRule.java
- Attempts used: 8
- Last updated: 2026-09-25T11:24:39.831602+00:00
- Reason / notes: The before() and after() trees are structurally different (a Filter node above the root join vs. the same two uninterpreted predicates ANDed into the root join condition), P and F are correctly shared symbols over the full (A,B,C) row, and A/B/C are three independent scans — so the proof is a genuine, non-vacuous verification of the rule's core rewrite (filter composed into the MultiJoin's post-join filter) for the case where that filter coincides with the root join condition. This is a faithfully and honestly tagged PARTIAL encoding: inner-only join types, fixed three-factor shape, and a non-null post-join filter are the stated restrictions, and the inner-only limit is forced by QED's binary-join modeling (an outer join's post-join filter cannot be folded into its condition), not porter laziness. The only blemish is a mildly misleading SCOPE clause — the real rule also merely ANDs rather than distributing conjuncts — which understates rather than overclaims generality, so the "provable" result remains meaningful.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 75955542}, panicked=False

### `FilterProjectTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterProjectTransposeRule.java
- Attempts used: 42
- Last updated: 2026-09-22T01:14:21.521707+00:00
- Reason / notes: The encoding faithfully captures the core mechanism of FilterProjectTranspose — pushing a predicate below a project by substituting the projection expression into the filter condition — for the single-column, single-conjunct case. Before (Filter(F(Proj(s)), Project(Proj, s))) and after (Project(Proj, Filter(F(Proj(s)), s))) are structurally distinct plan shapes that are semantically equivalent under bag semantics for all instantiations of the uninterpreted symbols F and Proj, so the proof is non-vacuous. The SCOPE tag accurately and specifically states the restriction (single column, single conjunct, condition references only that column), which is a genuine DSL limitation since the current `RelRN.project(RexRN)` API only produces single-column projects. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 67754459}, panicked=False

### `FilterReduceExpressions` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java

Note: ReduceExpressionsRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `FilterReduceExpressionsRule` variant (not CalcReduceExpressionsRule, JoinReduceExpressionsRule, ProjectReduceExpressionsRule, WindowReduceExpressionsRule, which are separate rules ported under their own spec names).
- Attempts used: 9
- Last updated: 2026-09-23T07:20:32.393498+00:00
- Reason / notes: The encoding precisely mirrors the source rule's false-literal branch of `onMatch` — `newConditionExp instanceof RexLiteral` (false) → `createEmptyRelOrEquivalent`, i.e. `builder.push(filter.getInput()).empty()`, which is exactly `source.filter(RexRN.falseLiteral())` ⟹ `source.empty()` (zero-row `Values` of the input's row type) — and the two sides are structurally different plans (Filter node vs. empty Values), so the provable equivalence is a genuine, non-vacuous statement of the real optimization (input scan eliminated). There are no silently dropped preconditions: this branch fires for any input regardless of keys/NOT NULL (those only gate the separate `reduceNotNullableFilter` path), and the single input relation is correctly a single scan symbol. The restriction to a condition that is *already* the constant FALSE literal is a genuine QED limitation rather than a missing DSL feature — the rule's general power is executor-based constant reduction of arbitrary subtrees (e.g. `1+1=2`), and QED cannot reason about the algebra of uninterpreted operator symbols, so the top-level literal case is the meaningful expressible special case — and the one-line `// SCOPE: PARTIAL` tag discloses exactly this, specifically and honestly.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 394459}, panicked=False

### `FilterRemoveIsNotDistinctFrom` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterRemoveIsNotDistinctFromRule.java
- Attempts used: 37
- Last updated: 2026-09-22T17:53:11.248101+00:00
- Reason / notes: The encoding is non-trivial and operator-correct: before() is Filter(IS_NOT_DISTINCT_FROM(x,y)) and after() is the DNF `(x IS NULL AND y IS NULL) OR IS_TRUE(x = y)`, which I verified against Calcite's RelOptUtil.isDistinctFrom(rexBuilder, x, y, true) CASE expansion across all four null/non-null combinations under three-valued logic — both sides are never NULL and agree on every branch (both-null → TRUE; one-null → FALSE; both non-null → x = y). Symbol usage is right: x and y are two independent nullable columns of a single shared uninterpreted type V (sharing the type mirrors SQL's same-type requirement for the operator, not an over-constraint), and because they live in an inner cross-product join, the proven identity is pointwise over all value pairs (a,b), which by substitution subsumes any occurrence of the operator nested inside a larger filter condition. The SCOPE:PARTIAL tag is honest and specific — the pattern matches only a top-level IS NOT DISTINCT FROM over a two-column join row, which is the strongest relational shape the DSL can express (scans are single-column and there is no uninterpreted predicate-context hole) — so the narrowing is real, degenerate, and loses no logical content. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 72516208}, panicked=False

### `FilterSetOpTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterSetOpTransposeRule.java
- Attempts used: 6
- Last updated: 2026-09-22T12:49:48.031901+00:00
- Reason / notes: before() = Filter(P, UnionAll(L, R)) and after() = UnionAll(Filter(P, L), Filter(P, R)) are structurally distinct and exactly the pushdown the source rule performs, with the details faithful: "P" is one uninterpreted predicate symbol shared across all three occurrences (matching the source rule reusing the same condition over each input after identity column remapping), `union(true, ...)` is a legitimate SetOp instance with the correct ALL flag, and the source rule has no hidden preconditions (keys, nullability) that the encoding silently drops. The PARTIAL tag is honest and specific — 2-input UNION ALL is one instance of the rule, where other arities/kinds need separate encodings and bag INTERSECT/MINUS are genuinely outside QED's set-only semantics — and the proven special case remains a useful, non-degenerate rewrite.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1623125}, panicked=False

### `FilterTableFunctionTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterTableFunctionTransposeRule.java
- Attempts used: 35
- Last updated: 2026-09-23T07:25:07.135893+00:00
- Reason / notes: The encoding faithfully captures the rule: under the source's side conditions (single input, identity 1-to-1 non-derived mapping), the table function is row-preserving, and the porter models it exactly as an INNER join of the input with an auxiliary witness table on uninterpreted J projected back to the input fields — which preserves per-row bag multiplicities for arbitrary (J, T), with the same P and J symbols consistently shared and typed on both sides. before() (Filter above Project(Join(S,T))) and after() (Join(Filter(S),T)) are structurally distinct, so the proof is a genuine, non-vacuous pushdown equivalence (verified by hand: multiplicities of surviving rows match on both sides). The only narrowing — fixing the input row at 2 columns instead of any equal field count — is inherent to the fixed-shape pattern language (arity cannot be a symbolic/uninterpreted dimension), is honestly and specifically tagged SCOPE: PARTIAL, and the 2-column case is a non-degenerate, representative instance that still exercises multi-column predicates P. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 71165291}, panicked=False

### `FilterToCalc` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterToCalcRule.java
- Attempts used: 9
- Last updated: 2026-09-23T06:42:17.572919+00:00
- Reason / notes: The encoding captures exactly what FilterToCalcRule rewrites: `before()` is the bare `Filter(cond, R)`, and `after()` is the Calc the rule builds (RexProgramBuilder `addIdentity()` + `addCondition()`), which — since the DSL has no Calc operator — is correctly expressed under the project-based Calc convention as the filter plus an explicit identity projection over `source.fields()`. The condition is a single uninterpreted predicate symbol shared correctly between both sides, the input is an uninterpreted scan standing for an arbitrary relation, and the structural delta (the added identity project) mirrors the real before/after shape difference rather than being an accidental tautology. The equivalence is semantically trivial only because this rule is inherently a pure representation change (its stated guards — no subquery, non-Filter/Project/Calc child — are planner-firing strategy, not semantic preconditions the proof depends on), so the SCOPE: FULL tag is honest and the proof faithfully validates the rule's soundness claim.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 381833}, panicked=False

### `FilterWindowTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterWindowTransposeRule.java
- Attempts used: 15
- Last updated: 2026-09-23T07:46:57.830759+00:00
- Reason / notes: The encoding is a faithful, honest special case: since QED cannot model Window at all (no bag semantics, no JSON/prover support), modeling it as a group-by over the partition key with an uninterpreted aggregate captures exactly the rule's core mechanism, and the proof is non-trivial (Filter(P(k)∧Q(k,f), GroupBy_k f(v)) vs Filter(Q, GroupBy_k f(v) over Filter(P(k), B)) genuinely differ and are equivalent only because P, an uninterpreted predicate structurally referencing the partition key alone — mirroring Calcite's `partitionKeys.contains(usedCols)` check — is constant per partition while Q, which references the aggregate value, correctly stays above). Symbol sharing is correct (same scan B, P, Q, f on both sides, with P's key column resolving consistently to the group key output column), no preconditions are silently dropped, and the multi-line but specific PARTIAL scope tag accurately states the real restrictions (single partition key, single aggregate, one pushed + one remaining conjunct, per-partition rather than per-row window output).
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 98738958}, panicked=False

### `FullToLeftAndRightJoin` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FullToLeftAndRightJoinRule.java
- Attempts used: 11
- Last updated: 2026-09-22T20:34:43.762223+00:00
- Reason / notes: The encoding mirrors the source rule's exact shape — before() is the FULL join, after() is (LEFT join) UNION ALL ((RIGHT join) filtered by IS_NOT_TRUE of the same condition) — with the correct join kinds, the correct union-all flag, and the condition/field references shared consistently, since all three join outputs over L and R have the same 2-column (L||R) layout so field(0)=L.col and field(1)=R.col line up in both the join predicates and the filter. The only deviation is fixing the condition to a single equality L.col = R.col instead of an arbitrary deterministic predicate, which is a genuine and honestly-labeled PARTIAL restriction: the decomposition is only valid (and QED-provable) when the condition is never TRUE on null-extended rows, a property an uninterpreted predicate cannot satisfy under QED's no-predicate-inference semantics but a concrete equality does. The narrowing is specific, faithful, and non-degenerate (before and after remain structurally distinct, proving a real equijoin full-outer-join decomposition), so the "provable" result is meaningful. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 111441042}, panicked=False

### `IntersectReorder` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/IntersectReorderRule.java
- Attempts used: 8
- Last updated: 2026-09-22T20:23:32.522794+00:00
- Reason / notes: before (A∩B∩C) and after (C∩A∩B) differ by a genuine 3-cycle permutation of three distinct uninterpreted inputs sharing one type, so the proof is non-vacuous and captures exactly the rule's semantic core (permutation invariance of INTERSECT), while Calcite's cost-sorted any-arity reordering is a heuristic family that no single equivalence can express — the PARTIAL scope note states this accurately. The encoding's n-ary set (all=false) INTERSECT matches the actual shape of the source rewrite (pushAll + n-ary intersect on LogicalIntersect) and QED's set-variant-only INTERSECT modeling, which is what justifies excluding INTERSECT ALL. There is no symbol-sharing error (A/B/C are independent scans; shared type "T" is required since intersect inputs must be type-compatible), and the source rule imposes no preconditions beyond inputs > 1 that the encoding silently drops. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 80958875}, panicked=False

### `IntersectToExists` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/IntersectToExistsRule.java
- Attempts used: 31
- Last updated: 2026-09-25T11:53:01.792610+00:00
- Reason / notes: The encoding faithfully mirrors the rule's actual transformation — INTERSECT with all=false ⟹ a SEMI-correlate (the decorrelated EXISTS filter) on IS NOT DISTINCT FROM over the whole row, followed by the rule's final DISTINCT as a group-all/no-aggregate-calls Aggregate — and before() (an INTERSECT node) vs. after() (AGGREGATE over CORRELATE) differ structurally, so the proof is non-vacuous; the concrete IS_NOT_DISTINCT_FROM operator and SEMI kind are exactly what the source rule constructs (an uninterpreted predicate or EQ would have been the unfaithful choice), A and B remain independent non-unique scans, and no hidden preconditions (PK/NOT NULL) are assumed since the rule needs none and nulls are handled by INDF on both sides. The PARTIAL scope is honest and specific — 2 inputs, single column, shared row type so the rule's defensive type-unification casts are identity, with the n-way case obtained compositionally — and it is a genuine, non-degenerate fragment of the real rule rather than a narrowed-to-identical trick.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 71712292}, panicked=False

### `IntersectToSemiJoin` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/IntersectToSemiJoinRule.java
- Attempts used: 8
- Last updated: 2026-09-23T07:37:15.152167+00:00
- Reason / notes: The pattern is non-vacuous and matches the source rule's binary step: set INTERSECT over independent uninterpreted A and B is rewritten to A SEMI-JOIN B on IS NOT DISTINCT FROM followed by a group-by-only distinct, with the correct all=false, SEMI, and final-duplicate-elimination shapes. The stated PARTIAL scope is honest because the full Calcite rule additionally handles n-way inputs, multi-column conjunctions, and least-type casts, but this single-column same-type case is a genuine non-degenerate special case.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 67415625}, panicked=False

### `JoinAddRedundantSemiJoin` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinAddRedundantSemiJoinRule.java
- Attempts used: 12
- Last updated: 2026-09-23T06:55:01.959227+00:00
- Reason / notes: The encoding exactly mirrors the source rule's transformation: before = X INNER⋈_C Y, after = (X SEMI⋈_C Y) INNER⋈_C Y, with the same uninterpreted condition C shared in all three joins — which is correct because Calcite's rule reuses `origJoinRel.getCondition()` for both the new semi-join and the outer join — and the correct join kinds (the rule explicitly restricts to INNER, so hard-coding INNER is faithful, not under-generalizing). The two source guards that are absent from the encoding (`isSemiJoinDone` and non-empty `leftKeys`) are bookkeeping/applicability heuristics, not semantic preconditions for correctness: the redundancy identity holds for every condition (e.g. even C referencing only Y or C=true), so QED's proof of the guard-free claim is stronger than, not weaker than, what the rule needs. X and Y are fully uninterpreted single-column relations with an uninterpreted predicate over the joined row — column count plays no role in the transformation's validity, so `// SCOPE: FULL` is honest and the plan shapes are structurally distinct (one join vs. two), making the proof a genuine verification of a real identity rather than a vacuous one. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 90690500}, panicked=False

### `JoinAggregateTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinAggregateTransposeRule.java
- Attempts used: 36
- Last updated: 2026-09-22T20:53:47.222643+00:00
- Reason / notes: The encoding mirrors the source rule's exact plan shape — Aggregate(below an INNER join) ⟹ INNER join under an Aggregate whose group set is extended with all right-side columns, followed by a project restoring the original column order — and it shares the same uninterpreted join predicate and the same uninterpreted aggregate symbol across both sides, with the condition referencing only the group key on the aggregate side (the precondition that makes the transpose sound). The two load-bearing preconditions of the original rule are correctly captured: the right input is declared unique on its join key via the `unique` scan flag (mirroring `mq.areColumnsUnique(right, info.rightSet())`), and the group set is non-empty by construction (so the empty-group/COUNT(*) caveat is moot); the proof is non-vacuous because it genuinely requires QED to relate the join-condition's group key through the aggregate on the before side. The stated PARTIAL scope (single-column, unique right table) is a real DSL-forced restriction (keyed multi-column scans are not exposed), and the implicit left-side minimalization to one group column plus one SUM call over the other column adds no new semantic content to the obligation, making this a faithful, non-degenerate special case rather than a mis-encoded or over-constrained coincidence. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 92112708}, panicked=False

### `JoinAssociate` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinAssociateRule.java
- Attempts used: 34
- Last updated: 2026-09-22T13:09:21.850562+00:00
- Reason / notes: The encoding correctly re-associates ((A⋈B)⋈C)→(A⋈(B⋈C)) with INNER joins, faithfully splitting conditions by A-reference (PAB/PABC on top, PB/PBC/PC moved to the new bottom B⋈C), with all five uninterpreted predicate symbols consistently shared and correctly re-indexed across both trees; the PARTIAL scope is honest and specific—the fixed conjunct split and single-column scans reflect a genuine DSL limitation (column-reference-based decomposition can't be quantified in bag semantics), and the instance is non-degenerate, exercising conjuncts moving from both levels.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 72733875}, panicked=False

### `JoinCommute` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinCommuteRule.java
- Attempts used: 39
- Last updated: 2026-09-22T07:09:24.122493+00:00
- Reason / notes: before() is Join(L, R, INNER, P(l, r)) and after() is Join(R, L, INNER, P(l, r)) with the references correctly remapped to positions (1, 0) (via right.joinField(1, left) / right.joinField(0, left)) followed by Project(l, r) to restore column order — exactly Calcite's VariableReplacer plus column-restoring project for the default INNER-only config, with precisely one shared predicate symbol and two distinct table symbols, so the proof is non-vacuous (a swapped P argument order, a missing project, or two independent predicate symbols would not have proved). The disclosed PARTIAL restrictions are genuine and specific: one-column-per-side is forced by the DSL's single-column Scan, and a single uninterpreted 2-ary predicate is actually the most general single-atom condition over the two-column row (any left-only or right-only condition is an instance of it). The source's self-join exclusion is a planner heuristic (swapping a self-join yields an identical tree), not a semantic precondition, so no essential assumption is silently dropped. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 66898375}, panicked=False

### `JoinConditionExpandIsNotDistinctFrom` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinConditionExpandIsNotDistinctFromRule.java
- Attempts used: 65
- Last updated: 2026-09-25T12:30:13.460981+00:00
- Reason / notes: The encoding is a faithful, non-degenerate special case: before (INNER join on IS_NOT_DISTINCT_FROM) and after (INNER join on the 3-valued-safe expansion (x IS NULL AND y IS NULL) OR IS_TRUE(x=y)) are genuinely different and QED-proven equivalent, with the expansion being semantically identical to IS NOT DISTINCT FROM (a valid alternative to Calcite's COALESCE form, as disclosed). The narrowing to an inner join / whole-condition / single shared column is real, specific, and honestly tagged PARTIAL, with no triviality, symbol-sharing, or missing-precondition defects that would make the proof misleading. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 347291}, panicked=False

### `JoinConditionPush` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterJoinRule.java

Note: FilterJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `JoinConditionPushRule` variant (not FilterIntoJoinRule (already ported separately as "FilterJoin"), which are separate rules ported under their own spec names).
- Attempts used: 66
- Last updated: 2026-09-23T08:30:02.616641+00:00
- Reason / notes: The proof is not vacuous and the encoding is sound: before() and after() differ structurally, and reusing the same uninterpreted predicate operators (left_conj, right_conj, cross_conj) in both plans correctly models the rule moving the very same conjuncts from the ON clause into child filters, with the right-side conjunct bound via joinField(1, right) so it references the join row's right column rather than mis-binding. No hidden preconditions are assumed (scans are non-unique and nullable, and uninterpreted predicates are deterministic by construction, matching the rule's determinism guard), and the narrowing — INNER join, no filter above, one left/right/cross conjunct, single-column inputs — is precisely and honestly declared in the SCOPE line, making this a genuine, non-degenerate, and useful special case of Calcite's FilterJoinRule pushdown (the INNER from-within case of JoinConditionPushRule).
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 78040500}, panicked=False

### `JoinDeriveIsNotNullFilter` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinDeriveIsNotNullFilterRule.java
- Attempts used: 10
- Last updated: 2026-09-22T20:33:10.839104+00:00
- Reason / notes: The rewrite is nontrivial and matches the source's inner-join transformation by adding IS NOT NULL filters implied by the null-rejecting equality conjunct while preserving the same condition. Although narrower than the fully generic Calcite rule, the SCOPE line accurately states the specific equality-plus-arbitrary-extra-conjuncts restriction, so the proven result is faithful and non-vacuous.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 25281250}, panicked=False

### `JoinExtractFilter` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinExtractFilterRule.java
- Attempts used: 8
- Last updated: 2026-09-22T09:20:16.505380+00:00
- Reason / notes: <1-3 sentences ...> ``` No code fences? It shows with code block? It says Reply with exactly this format (nothing else): ``` ... ``` Probably no markdown code fence? The example uses a fenced block? It shows: ``` <...> ``` or, if... ``` ... ``` The outer triple backticks in the prompt are just delimiting the format. It says exactly this format (nothing else). Usually we should output plain lines, not fenced. Need decide. The text: "Reply with **exactly** this format (nothing else): ``` <1-3 sentences on why this is a faithful, general encoding of the source rule> ``` or, if you find a real problem: ``` <specifically what is wrong and, if you can tell, what a correct encoding should do differently> ```" It likely expects no code fences, just the two lines. The triple backticks are showing a template. We should output exactly two lines. Ensure no extra. 1-3 sentences. Could be: The encoding uses the same shared uninterpreted condition on an INNER join versus a filter above an INNER join with true, matching Calcite's JoinExtractFilter transformation. The scans and predicate are uninterpreted symbols, so inputs and condition are general, and no missing semantic preconditions are required; the only absent guard is the source rule's non-trivial-condition firing check, which does not affect correctness. This is 2 sentences? Actually two sentences. Good. Need ensure "FULL" maybe. If we think the missing guard means not full, maybe we could still confirm? The prompt asks scope honesty; if narrower, reject? It says if you find a real problem. If the source rule has firing guard not always true, is that a real problem for faithful encoding? Let's examine expected standards. They want quality control. A "provable" result can be misleading if the encoding is not faithful. If the original rule only fires on inner joins with non-true conditions, the encoding proves equivalence for all predicates, including true. Is that a misleadingly broader claim? It is not a *narrower* special case, it's broader. But the rule as a rewrite rule: a rule's applicability is part of the rule. If it can be applied to true condition, it would be a no-op but still correct. In rewrite rule verification, proving a superset is usually okay? However if the source rule's guard is semantic (e.g. condition not null, join type inner), missing it could be bad. Here guard is just to avoid useless rewrite; not needed for soundness. The SCOPE tag says FULL. If we interpret full generality of the *transformation* (inner join with any condition), it is full. If interpret full generality including matcher guard, it's slightly over-general but not unsound. The output options only CONFIRMED or REJECTED. We likely should CONFIRMED because the core rewrite is faithful. The prompt's failure mode 5 says missing preconditions that make proof of different, easier claim. The missing `!isAlwaysTrue()` makes the claim broader, not easier? Actually if condition can be true, the after filter true over cartesian is equivalent but maybe more trivial. But the proof still has to handle all predicates; the presence of true as a possible predicate doesn't ease the overall proof. It's a broader family, but still true. If a source rule has a guard that excludes some cases for performance, encoding without guard is not misleading if the equivalence holds. But could it be considered not faithful because the original rule would not rewrite a join with condition true, so the "after" plan is not actually produced by the rule? In rule-based optimizers, a rule's correctness usually considered for the patterns it matches; matching guards that exclude semantically no-op cases are not part of the logical rewrite. The reference asks compare against full source and decide full vs partial. It says "if it's narrower, is the restriction genuine and specific". Here not narrower. It might be broader, but the tag is FULL; broader is okay? They didn't list over-generalization as failure. But "wrong operators" and "missing preconditions" could cover. I think confirm. Let's consider if the source rule actually applies to *any* join type? The class javadoc says inner, but the `Config.DEFAULT` `operand(LogicalJoin.class).anyInputs()` doesn't specify join type. If the base rule did not check inner, the rule would be unsound for outer joins? Calcite would not do that. It must check inner. Let's maybe recall exact `AbstractJoinExtractFilterRule` from Calcite: ``` public abstract class AbstractJoinExtractFilterRule extends JoinRule { protected abstract void onMatch(RelOptRuleCall call); @Override protected void onMatch(RelOptRuleCall call) { Join join = call.rels(0); if (!join.isInner()) { return; } RelBuilder builder = relBuilder(); RelNode newInput = builder.push(join.left).push(join.right) .join(JoinRelType.INNER, builder.literal(true)).build(); RelNode newFilter = builder.push(newInput).filter(join.condition).build(); call.transformTo(newFilter); } } ``` Maybe it also requires `join.getCondition() != null` and `!join.getCondition().isAlwaysTrue()`. Inner join condition can be null for cross join? `LogicalJoin` condition can be null? In Calcite, join condition is `RexNode`, may be `true` literal for cross? It may be null for cross? `RelBuilder.join(JoinRelType.INNER)` with no condition creates true. The rule likely handles condition true check. Potential issue: In before, the join condition is `joinCond` which is a `Pred` with sources all fields. In Calcite, an inner join's condition may reference only a subset of fields; uninterpreted predicate can be considered as over all fields with some ignored? In first-order logic, a predicate over all fields can represent any predicate over subset by ignoring args. So full. Potential issue: The source join may have *field names* and output row includes left then right. In RuleScript, `Join.field` overrides to `JoinField` with left/right. In after filter, using `joinCond` built from `left.joinPred(..., right)` not from the join node's fields. It refers to original left and right. Should be same. Potential issue: The after join's condition `trueLiteral()` is a `RexRN.True` with no sources. In `Join.semantics()`, it will create join condition true. Good. Potential issue: The rule's after plan in Calcite might be `Filter(Join(left, right, INNER, true), condition)` but if condition references only left or right, it might be a `LogicalFilter` with condition `RexInputRef` relative to join. Same. Potential issue: QED's equivalence class may be too weak? `complete_fragment: True`, no timeouts. Not our concern. Need maybe think about null semantics and 3-valued logic. Calcite uses SQL 3VL. For inner join, condition null => row dropped. Filter condition null => row dropped. Equivalent. If the join condition is `C` and after filter is `C`, same. No need NOT NULL. QED may model predicates as Boolean, not tri-valued, but both operations treat false/null the same for membership. If it models as total Boolean, still equivalent. If C can be null, mapping null to false preserves equivalence. So no missing NOT NULL. Potential issue: The original rule may have a condition that the join condition is *not constant false*? If constant false, original join yields empty; after filter false yields empty. Equivalent. If it doesn't fire, no issue. Potential issue: The source rule's `Config` can be configured to match any subclass of `Join`, not just `LogicalJoin`. The encoding uses the core `Join` pattern with inner type; in RuleScript that's abstract enough. It doesn't hard-code `LogicalJoin` subclass. Good. Potential issue: It uses scans for inputs; source rule's inputs can be any RelNode. As above, extensional. Could there be a more subtle issue: The original rule extracts the *join condition* as a filter, but in Calcite, if the join is an *INNER* join with condition `C`, and `C` contains *join field references*, moving it above the join is valid because the join output row includes both sides. However, if `C` contains *null* and the join is *INNER*, no issue. If the join type were `LEFT` or `RIGHT`, moving filter above changes null-extended rows; but source is inner. Good. Could there be an issue with *semi/anti* joins? Not inner. Could there be an issue with *bag duplicates*? Inner join true produces Cartesian product preserving multiplicities; filter preserves. Original inner join C produces product for satisfying pairs. Equivalent. Good. Need maybe decide if `// SCOPE: FULL` is honest. I think yes. We can mention in reasoning that uninterpreted scans and condition stand for arbitrary inputs/condition, inner join matches source, shared condition is correct. Let's consider alternative: The porter may have used `left.joinPred("join_cond", right)` which creates a predicate over `joinFields(right)` (all fields). But `left.joinFields(right)` returns fields over *all fields of left plus right* using `semantics().getRowType().getFieldCount()`. For scans one column each, 2. If the source rule could apply to joins where one side has zero columns? Relational algebra maybe not. No issue. Potential issue: `joinCond` is a `RexRN.Pred` with `operator` from `genericPredicateOp("join_cond", true)`. The second parameter likely `nullable`? In `RuleBuilder.genericPredicateOp(name, true)`. It may create a SqlOperator with name and nullable type. The operator is uninterpreted. Good. Potential issue: In after, `filter(joinCond)` where `joinCond` sources are `RexRN.JoinField`. But `Filter`'s input is a `Join` node, not a raw pair. The `JoinField.semantics()` pushes left and right, not the join? Let's inspect more: `RexRN.JoinField(ordinal, left, right).semantics()` does: ``` var leftCols = left.semantics().getRowType().getFieldCount(); return RuleBuilder.create().push(left.semantics()).push(right.semantics()).field(2, ordinal < leftCols ? 0 : 1, ordinal < leftCols ? ordinal : ordinal - leftCols); ``` This seems to build a `RexFieldAccess` with correlation variable? If so, using it in a filter over a join might be wrong? But QED proved, and the DSL is designed for join conditions. Let's understand `RuleBuilder.field(2, rel, index)`. It might be a method in QED's RuleBuilder to create an input ref with a "level" for join conditions? In Calcite, `RexInputRef` uses index, not rel. `RexFieldAccess` is for correlated references. But `field(2, 0, 0)` perhaps creates a `RexInputRef` with index 0 after pushing two rels? The `2` might be the number of operands? Hard to know. But the fact that `JSONSerializer` has special handling for `RexFieldAccess` suggests `JoinField` may serialize to `RexFieldAccess`? Let's inspect: `RexFieldAccess fieldAccess -> object(column = fieldAccess.getField().getIndex() + env.resolve(((RexCorrelVariable) fieldAccess.getReferenceExpr()).id), type...)`. For a normal join condition, `LogicalJoin` serialization uses a `Rex` visitor with base = leftCols + rightCols? Actually for join: ``` var visitor = new Rex(env.recorded(join.getVariablesSet()).rex(left.getRowType().getFieldCount() + right.getRowType().getFieldCount())); yield object(Map.of("join", object(Map.of("kind", ..., "condition", visitor.serialize(join.getCondition()), ... ``` `Rex.Env` has `base` = leftCols+rightCols? Wait `Env(int base, int delta, globals, tables)`. In `Rel.Env.rex(int delta)` returns `new Rex.Env(lvl, delta, globals, tables)`. For join, `env.recorded(...).rex(left.getFieldCount() + right.getFieldCount())`: base = left+right? Then for `RexInputRef`, `integer(inputRef.getIndex() + env.base())`. That would make column indices left+right+index, weird. For filter, base = input.getFieldCount(), so inputRef index + input field count. This seems strange. Maybe `base` is not offset but number of columns? The JSON column likely is absolute? For a filter over source with input fields 0..n-1, `base=n` would make columns n..2n-1, not right. Hmm. Maybe `env.base()` is 0 for filter? Let's check: `Rel.Env.rex(int delta)` passes `delta` as `base`? In `Rex.Env` constructor `(int base, int delta, ...)`. In `Rel.Env.rex(int delta)` returns `new Rex.Env(lvl, delta, globals, tables)`: base = lvl, delta = input field count? Wait `Rel.Env.lvl` is aggregate level, maybe 0. So `base=0`, `delta=inputFieldCount`. But `RexInputRef` uses `inputRef.getIndex() + env.base()`, so base 0. Then `delta` unused? In `RexFieldAccess`, uses `env.resolve(corrId)`. I may be misreading. `Rex.Env` has fields `base, delta`; `RexInputRef` uses `base`, not delta. For filter, base = lvl (0), so correct. For join, base = lvl (0) too, but delta = left+right. So condition input refs 0..n-1 correct. The `delta` maybe used for something else? Not important. `JoinField.semantics()` uses `RuleBuilder.create().push(left).push(right).field(2, rel, index)`. The `2` maybe number of pushed relations, then it creates a `RexFieldAccess`? If join condition uses `RexFieldAccess`, the serializer would need correlation id. But `LogicalJoin` serializer's `Rex` env has `globals` recorded from `join.getVariablesSet()`. A normal join condition doesn't have correlation variables. So likely `field(2, rel, index)` creates a `RexInputRef` with index rel*? Maybe `RuleBuilder.field` is a custom method to build field references over multiple inputs. Not a problem. But could using `joinPred` in a filter above a join create field references that are *correlated* to the original left/right rather than the filter input? If so, QED might still prove due to some equivalence? Need ensure not a symbol sharing error. In after, filter input is a join node. The condition `joinCond` was originally a join condition predicate over `left` and `right`. If its field references are somehow bound to the original left/right relations, not the join output, it might still refer to the same values because the join output contains them. In SQL semantics, a filter above a join cannot reference left/right directly, only join output. But if RuleScript's `JoinField` builds field refs with a level that might be wrong in filter context. However QED's prover accepted and proved; but could it prove a different plan? The JSON serializer would serialize the RexNode as produced by semantics. If `JoinField.semantics()` produces a `RexFieldAccess` with correlation variable, then in a filter context, the filter serializer uses `env.recorded(filter.getVariablesSet())`. If the RexNode has correlation ids not declared in filter, serializer would throw? But QED ran without panic, so it must produce valid normal input refs or the serializer handled. Since `complete_fragment` true, okay. Let's think if `left.joinPred("join_cond", right)` in after is the *same* object as before. In `Filter.cond` is that object. `Filter.semantics()` calls `cond.semantics()`. The `Pred.semantics()` maps sources `RexRN::semantics`. Each `JoinField.semantics()` pushes left and right and calls `field`. This may create a new RexNode each time. But the operator name is same. QED likely canonicalizes by name and structure. Good. Potential issue: The original rule's after join condition is `true`, but if the original join condition is *null* (cross join represented by null condition), the rule may not apply? In Calcite, `Join.getCondition()` may be null for cross join? `LogicalJoin` can have null condition? I think condition is `RexNode`, can be null for cross join? In `RelBuilder.join(JoinRelType.CROSS)` maybe condition null. But for INNER cross join, condition might be `true` literal. The encoding before uses an uninterpreted predicate, not null. If source rule could apply to null condition? It likely treats null as true and extracts? Not relevant. Potential issue: The source rule may be part of `AbstractJoinExtractFilterRule` which has a config `requireInputs`? No. Let's consider if `JoinExtractFilterRule` is actually the inverse of `JoinToFilterRule`? There is `CoreRules.JOIN_EXTRACT_FILTER` and `CoreRules.JOIN_TO_FILTER`? In Calcite, `JoinToFilterRule` converts a join with a condition that is a conjunction? Wait. `JoinExtractFilterRule` extracts filter above join; `FilterIntoJoinRule` pushes filter into join? There is also `AbstractJoinExtractFilterRule` that maybe converts a *filter above a join* into a join condition? Let's verify name: "Rule to convert an inner join to a filter on top of a cartesian inner join." That is clear: join -> filter(join(cartesian)). There is also `JoinPushFilterRule`? But provided class is JoinExtractFilterRule, extends AbstractJoinExtractFilterRule. The javadoc in provided source is authoritative. Good. Need maybe mention that the encoding uses a single uninterpreted predicate over both input rows, which is exactly the join condition, and the same symbol is used in filter, so no accidental independent predicate. Let's think about the proof being "complete_fragment: true" and `equiv_class_duration` 0. Could there be a risk that QED proved equivalence because both plans were normalized to the same Q-expression, maybe not capturing the condition? But the condition is uninterpreted; before join condition and after filter condition. If QED's equivalence normalization treats inner join with condition as filter over inner join with true automatically, then it's fine. If it erroneously drops condition, both would be equivalent but proof meaningless. But QED is trusted; we assume it correctly models. We are verifying encoding, not prover. Need maybe check if `after` uses `filter(joinCond)` where `joinCond` is over `left` and `right`, but the filter input row from the cartesian join has fields: left's one column then right's one column. In before, the join output row also has same layout. Good. Potential issue: The original rule may preserve the *output field names* of the join. In RuleScript, row layout only columns; no names. The before and after both have same two columns. Good. Potential issue: The source rule may require that the join condition is not *referencing outer relations* (correlated). A `LogicalJoin` condition cannot reference outer? It can be correlated in some contexts? In Calcite, join condition can only reference join inputs, not outer. RuleScript doesn't model correlation. Not a missing precondition. Potential issue: The source rule may be for `LogicalJoin` specifically, which is an inner join? `LogicalJoin` can be any join type. The base rule checks inner. Good. Potential issue: In RuleScript, [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1670000}, panicked=False

### `JoinOnUniqueToSemiJoin` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRule.java

Note: SemiJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `JoinOnUniqueToSemiJoinRule` variant (not AggregateToSemiJoinRule, JoinToSemiJoinRule, ProjectToSemiJoinRule, which are separate rules ported under their own spec names).
- Attempts used: 81
- Last updated: 2026-09-23T09:03:24.443957+00:00
- Reason / notes: The encoding mirrors the source rule's INNER case exactly — Project(Left)∘(L ⋈_{l.c=r.c} R) ⟹ Project(Left)∘(L ⋉_{l.c=r.c} R) — with before() and after() differing only in join kind, so the proof is non-vacuous and reflects the real optimization. Crucially, the rule's defining precondition (right input unique on the join's right key) is genuinely encoded, not silently dropped: `scan("R", T, true)` adds a key on R's column 0 in the serialized schema, and the condition is an equi-join on precisely that column, matching `mq.areColumnsUnique(right, joinInfo.rightSet())`. The narrowing — 1-column-per-side equi-join, INNER variant only, identity left-only projection — is specific, honestly flagged in the SCOPE line, and is not an artifact of laziness: multi-column equi-join uniqueness is inexpressible in the current DSL (ScanMany cannot declare a key), and a fully uninterpreted join condition would over-generalize a claim that is not valid for non-functionally-determining conditions, so this is a genuine, non-degenerate special case of the source rule.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 74156250}, panicked=False

### `JoinProjectTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinProjectTransposeRule.java
- Attempts used: 10
- Last updated: 2026-09-22T20:46:44.131806+00:00
- Reason / notes: The before/after plans are structurally different (Project below the Join in before, Project above it in after), with correct symbol sharing — the same uninterpreted TL, TR and C appear on both sides, and the after-side condition is properly expanded over the raw join columns via correct joinField references (0,1 = L; 2,3 = R) — so the proof is non-vacuous and mirrors exactly what Calcite computes for an inner join (bottom program = projected expressions, top program = identity + condition, merged/expanded), with no missing preconditions for that case. The PARTIAL scope line honestly and specifically states the assumed restrictions (inner join, both inputs projects with a single uninterpreted expression each, condition over the projected row), so the result is a genuine, non-degenerate special case of the source rule: bag(σ_C(π(TL,L) × π(TR,R))) = π(TL,TR)(σ_{C∘(TL,TR)}(L × R)) for all instantiations of TL, TR, C. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 73464209}, panicked=False

### `JoinPushExpressions` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinPushExpressionsRule.java
- Attempts used: 37
- Last updated: 2026-09-22T12:05:35.016770+00:00
- Reason / notes: before() and after() are genuinely structurally different and differ in exactly the way the real rule rewrites — a bare inner join versus a join over child projects that carry FL/FR, with the condition rewritten to reference the projected expression columns and a final projection restoring the original (L.c0, R.c0) row type — and FL, FR, EQ, and RES are shared across both sides while remaining mutually independent, so the SMT equivalence is non-vacuous and not a product of over-constrained symbol reuse. The scope line is honest and specific: the encoding is a real, non-degenerate special case (inner join, one equal conjunct, one residual predicate), and "single-column join inputs" is an artifact of the DSL's one-column scan representation rather than a semantic weakening, since FL/FR/RES are already uninterpreted functions/predicates over the whole row, which is all the rule's correctness depends on. No preconditions (keys, nullness) are required by the source rule's transformation and none are silently omitted, so the proved claim is a faithful instance of JoinPushExpressions.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 71744041}, panicked=False

### `JoinPushThroughJoin` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinPushThroughJoinRule.java
- Attempts used: 7
- Last updated: 2026-09-22T13:25:26.366430+00:00
- Reason / notes: The encoding faithfully captures the RIGHT instance of JoinPushThroughJoin: before is (A ⋈_{SA∧SB} B) ⋈_{ST∧TC} C with layout (A,B,C), and after is (A ⋈_{SA∧TC} C) ⋈_{ST∧SB} B followed by a project restoring (A,B,C) — exactly the conjunct redistribution Calcite's onMatchRight performs (SA/TC to the new A⋈C bottom, ST/SB to the new top), with both joins INNER as the rule requires, correct JoinField ordinals on every side, and consistent predicate-argument types so QED unifies the moved symbols correctly. The equivalence is a genuine, non-vacuous inner-join re-association identity (conjunction commutes over the a/b/c row variables), so before() and after() are structurally different yet truly bag-equivalent, and no symbol is shared where two independent predicates are needed. The PARTIAL scope is honest and specific — single-column scans (forced by the current `RelRN.scan`, which always yields one column) and a fixed representative conjunction split by B-reference rather than an arbitrary decomposition (which QED cannot express, since it has no notion of an uninterpreted condition partitioned by column reference) — so the narrowing is a real QED/DSL limitation, not an avoidable under-generalization, and the result remains a meaningful, non-degenerate instance of the rule. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 101742000}, panicked=False

### `JoinPushTransitivePredicates` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinPushTransitivePredicatesRule.java
- Attempts used: 27
- Last updated: 2026-09-25T12:09:19.247653+00:00
- Reason / notes: The rewrite is non-vacuous — `after()` genuinely adds `Filter(p, LeftInput)` on top of the same join — and the equivalence it proves (`x = y ∧ p(y) ⟹ p(x)`, so the added left filter is redundant) is exactly the transitive-predicate inference `JoinPushTransitivePredicatesRule` performs via `RelMdPredicates` and applies by adding a filter to the left input while keeping the join condition unchanged, with the shared `p`/`EQUALS`/`joinCond` symbols being the *correct* identifications rather than over-constraint (the same predicate on both keys is the essence of "transitive"; two independent symbols would make the rewrite invalid). The restrictions — INNER join, single-key equality, non-nullable key type, one-sided push — are a genuine special case forced by QED's documented inability to infer entailment between uninterpreted predicates from an arbitrary join condition (a real prover limitation, not a missing DSL operator), the `// SCOPE: PARTIAL` line honestly names them, and no implicit primary-key/uniqueness guarantee is leaned on (both scans are marked non-unique), so the proof is of the same claim the rule makes in this instance.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 74281250}, panicked=False

### `JoinReduceExpressions` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java

Note: ReduceExpressionsRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `JoinReduceExpressionsRule` variant (not CalcReduceExpressionsRule, FilterReduceExpressionsRule, ProjectReduceExpressionsRule, WindowReduceExpressionsRule, which are separate rules ported under their own spec names).
- Attempts used: 32
- Last updated: 2026-09-23T07:35:05.859495+00:00
- Reason / notes: The encoding faithfully mirrors JoinReduceExpressionsRule's actual transformation — the rule rewrites via `join.copy(traitSet, reducedCond, left, right, joinType, semiJoinDone)`, i.e. the same join with a constant-folded condition and deliberately NO empty relation (unlike the Filter/Calc variants), which is exactly what after() encodes — and `C AND FALSE` → `FALSE` is a genuine, non-vacuous instance of constant reduction (RexSimplify folds an AND with a FALSE operand to FALSE) with the surviving predicate expressed as a shared uninterpreted symbol, so before() and after() are structurally different and the proof is real. The narrowing to INNER join kind and to this one reducible condition shape is exactly as disclosed in the SCOPE: PARTIAL line, and is unavoidable within the single-record contract since MetaJoinType serializes to INNER in the Calcite semantics QED sees, making the INNER instance the best achievable proof and a non-degenerate, faithful special case of the source rule. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 531750}, panicked=False

### `JoinToCorrelate` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinToCorrelateRule.java
- Attempts used: 1
- Last updated: 2026-09-23T22:38:59.593181+00:00
- Reason / notes: Hand-implemented by the harness operator after 5 automated rounds all crashed on LLM context-overflow before ever calling try_rule (see round_05_summary.md) — the verifier's own analysis across those rounds had already worked out that JoinToCorrelate needed a new RelRN.Correlate DSL primitive (RelRN/RexRN had no correlated-field-access builder) and a concrete encoding plan; this attempt follows that plan directly. before() is left INNER-join right on one uninterpreted binary predicate `cond` over (L.col, R.col); after() is a new Correlate RelRN whose semantics() builds a real Calcite LogicalCorrelate: a fresh CorrelationId, a RexCorrelVariable over left's row type, and a LogicalFilter on right whose condition applies the SAME `cond` operator to (correlated left field, right's own field) — verified via JSONSerializer.java that Calcite's own JSON encoding already numbers correlated field access identically to a plain join's combined-row indexing, so `cond` is seen by QED as the exact same uninterpreted symbol on both sides. QED proved it on the very first try_rule call (provable=true, no compile errors, no retries). SCOPE: PARTIAL is honest — INNER join only, single uninterpreted binary predicate, no additional filter conjuncts; the source rule's RIGHT-join branch (which needs an input swap + condition remap) is not covered.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 67055084}, panicked=False

### `JoinToHyperGraph` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinToHyperGraphRule.java
- Attempts used: 23
- Last updated: 2026-09-25T12:41:58.032537+00:00
- Reason / notes: The encoding is a faithful, non-trivial special case: before is the left-deep tree (A⋈_{pAB}B)⋈_{pAC∧pBC}C and after is the right-deep tree A⋈_{pAB∧pAC}(B⋈_{pBC}C), both using the same three uninterpreted predicates with correct field indices, proving the core property that inner-join hypergraph enumeration order is semantically irrelevant. The SCOPE:PARTIAL tag accurately and specifically states the restrictions (3-way, inner-only, fixed left-child-HyperGraph shape, one particular re-enumeration), which are genuine limitations of expressing the general N-input, multi-join-type flattening into the current DSL without a HyperGraph operator.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 75001291}, panicked=False

### `JoinToMultiJoin` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinToMultiJoinRule.java
- Attempts used: 23
- Last updated: 2026-09-25T13:01:19.333812+00:00
- Reason / notes: The encoding faithfully captures the core INNER-join 3-way flattening: `before` nests Fm at the inner join (Y⋈_Fm Z) with C at the outer level, while `after` lifts Fm to the root alongside C (Fm∧C) and makes the inner edge a true/cross-join — a structurally distinct but semantically equivalent pair (both yield {(x,y,z) | Fm(y,z)∧C(x,y,z)}), so the proof is non-vacuous. The SCOPE tag is honest and specific: the restriction to INNER (excluding LEFT/RIGHT/FULL where the real rule's null-generating-input logic prevents flattening) and to a fixed 3-way tree (vs. N-way) are genuine, meaningful special cases of the source rule, and all symbol sharing (Fm, C re-indexed correctly across the (x,y,z) layout) is correct.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 74062292}, panicked=False

### `JoinToSemiJoin` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRule.java

Note: SemiJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `JoinToSemiJoinRule` variant (not AggregateToSemiJoinRule, JoinOnUniqueToSemiJoinRule, ProjectToSemiJoinRule, which are separate rules ported under their own spec names).
- Attempts used: 37
- Last updated: 2026-09-23T22:58:28.122903+00:00
- Reason / notes: The encoding is non-vacuous and faithfully captures the rule's core rewrite — an INNER join of L with (R GROUP BY the join key), projected down to L's columns, is bag-equivalent to a SEMI join of L with the pre-aggregate R — using independent L/R symbols, the semi-join correctly targeting the aggregate's input, and a genuine (non-structural) INNER-vs-SEMI difference that QED proves. It is an honestly-declared narrower special case (INNER only, single-column equi key that is the aggregate's sole group key, no aggregate calls, plus a dropping project to make the INNER case width-matching; the LEFT/empty-aggregate elimination is omitted), so the SCOPE: PARTIAL tag is accurate and the proof is meaningful rather than of an easier claim. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 74427458}, panicked=False

### `JoinUnionTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinUnionTransposeRule.java
- Attempts used: 33
- Last updated: 2026-09-22T23:49:19.463599+00:00
- Reason / notes: The encoding is a faithful, non-vacuous instance of the rule: before() = (X ∪ALL Y) ⋈_C O vs after() = (X ⋈_C O) ∪ALL (Y ⋈_C O) is a genuine distributivity law (structurally distinct shapes, not an identity), the double `union(true, ...)` matches the source's `union.all` requirement, INNER keeps the union off any null-generating side so no semantic precondition is dropped, and the single shared condition symbol C correctly mirrors the source reusing the identical `join.getCondition()` in all three joins (independent symbols would have been the error). Nullable, keyless scans match the source's lack of PK/NOT NULL preconditions, so no hidden constraint is assumed. The scope line is honest and specific about the genuine narrowing (INNER join, left-side union) — full multi-join-type coverage cannot be a single provable rule because the join kind is concrete in QED's serialization (meta join types only feed the family generator, whose assignments are proven individually) — and the result is still a substantive, useful special case of the real rule. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1883000}, panicked=False

### `LoptOptimizeJoin` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/LoptOptimizeJoinRule.java
- Attempts used: 0
- Last updated: 2026-10-14T15:59:53.329840+00:00
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 22550208}, panicked=False

### `MarkToSemiOrAntiJoin` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MarkToSemiOrAntiJoinRule.java
- Attempts used: 45
- Last updated: 2026-10-14T07:01:24.576016+00:00
- Reason / notes: The union-all expansion of LEFT_MARK into marker-tagged semi and anti branches is bag-exactly faithful, and before/after are structurally distinct (marker-filtered union vs. bare semi), so the proof is non-vacuous; cond, the residual predicate P, and the projection p are all uninterpreted and correctly shared, and the semi branch is the only case of the source rule with no side conditions, so no precondition is silently dropped. The scope line honestly discloses the genuine narrowing (semi case only, single-column inputs, single residual conjunct and projected column), and the proved statement remains a real, non-degenerate special case of the rule.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 75350667}, panicked=False

### `MinusToAntiJoin` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MinusToAntiJoinRule.java
- Attempts used: 5
- Last updated: 2026-09-23T08:39:43.238067+00:00
- Reason / notes: The encoding reproduces the rule's 2-way transformation exactly — set-minus (all=false) rewritten to an ANTI join on IS NOT DISTINCT FROM followed by a group-by-all (the rule's final DISTINCT), with correct operand direction and with both of the rule's preconditions (not all, ≥2 inputs) respected — and its restrictions (2-input, single-column, shared row type so the type-unification cast is identity) are precisely declared in the SCOPE line, are non-vacuous (structurally different plans), and coincide with the rule's own documented 2-way example; the n-way case reduces soundly to repeated composition of this binary step, as the rule's javadoc itself states, and the shared type symbol is harmless since QED erases all VarTypes to a single integer type.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 66095083}, panicked=False

### `MultiJoinOptimizeBushy` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MultiJoinOptimizeBushyRule.java
- Attempts used: 33
- Last updated: 2026-09-23T11:55:20.868952+00:00
- Reason / notes: The encoding is non-vacuous and symbolically sound: before() is the left-deep chain (((A⋈B)⋈C)⋈F D) and after() is the genuinely bushy (A⋈B)⋈F (C⋈D), over the same shared scans and the same uninterpreted 4-ary filter F applied in identical argument order on both sides, with INNER joins (matching the rule's outer-join refusal) and true-condition inner joins (matching the MultiJoin's unconstrained factors), so QED actually proved the rule's core semantic claim — that reshaping a flat multi-join into a bushy tree preserves bag semantics — rather than a tautology. The hard-coded choices are faithful to a real instance of the rule (a 4-factor multi-join whose only filter touches all four factors, which the real rule would itself leave as a top-level condition), and the shared-symbol usage is correct rather than coincidentally over-constraining. The SCOPE line honestly and specifically discloses the unavoidable narrowing — fixed 4-way arity, single shared filter, and no cost-driven edge selection or per-conjunct condition distribution, which QED fundamentally cannot model since the heuristic's output tree depends on cardinalities — so the provable result is a genuine, non-degenerate special case, not a misleading one.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1983250}, panicked=False

### `MultiJoinProjectTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MultiJoinProjectTransposeRule.java
- Attempts used: 7
- Last updated: 2026-09-24T01:10:14.243864+00:00
- Reason / notes: The encoding faithfully captures the LEFT_PROJECT variant's core rewrite — pulling a (pure, uninterpreted) projection up through an inner join, with the join condition's left-side reference rewritten from the project's output column (before: `leftProject.joinField(0,right)`) to the same projection recomputed over the raw base columns (after: `tlAfter`), and `tlOp`/`cOp` correctly shared so the proof is of the genuine commutation rather than a coincidental over-constraint. `before()` = `Join(C, Project(TL,L), R)` and `after()` = `Project(TL,r0,r1, Join(C',L,R))` are structurally distinct (project below vs. above the join) with matching (PL_Type,R0,R1) output schemas, so the proof is non-vacuous and the multiplicities line up under bag semantics. The restrictions (LEFT_PROJECT only, inner join, single uninterpreted projection over two base columns, base modeled as a scan) are genuine, specific, and honestly declared in the SCOPE line, making this a valid, non-degenerate PARTIAL encoding rather than a trivial or mis-scoped one. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 71357916}, panicked=False

### `OuterJoinToAntiJoin` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/OuterJoinToAntiJoinRule.java
- Attempts used: 1
- Last updated: 2026-09-23T22:53:55.559196+00:00
- Reason / notes: Hand-implemented by the harness operator. This is the same LEFT-join/non-nullable-right-column special case worked out across this rule's automated rounds (round_05_summary.md) and stashed under UnprovableRRuleInstances/OuterJoinToAntiJoin.java.rejected — but that exact stashed source was re-verified fresh here and genuinely returned provable=false (its own stale result.json, claiming provable=true, did not match a fresh run and must have been left over from an earlier, different candidate never actually finalized). Root cause found by direct probing of the real Calcite RelBuilder: RelBuilder.join(LEFT, ...) does NOT widen the null-generating (right) side's declared nullability on the join's own row type, so a plain field(1) reference to the right column still carries a NOT NULL Calcite type, and Calcite's own RexSimplify constant-folds IS_NULL(it) to FALSE before QED ever sees the rule, collapsing before() to an empty relation. A first fix attempt (bypassing the RelRN.Join.field() override to read the join's own row-type field via RexRN.Field instead of RexRN.JoinField) did not help, since the join's OWN row type is equally unwidened. A second fix attempt using RelDataTypeFactory.createTypeWithNullability also silently failed, confirmed by direct probe (forced-nullable type still printed nullable=false) because RelType.VarType overrides isNullable() directly and doesn't participate in the factory's normal type-rebuilding. The working fix constructs a fresh `new RelType.VarType("R_T", true)` by hand and builds the RexInputRef directly from it (see the rule-local ForcedTypeRef record), bypassing both the buggy field-access path and the no-op factory method. With that, before()'s plan genuinely retains the LogicalFilter(IS NULL($1)) over the real LEFT join (verified via the explain-plan `help` field, not just trusting the provable flag), and QED proved the equivalence to the ANTI join in `after()` on the first true attempt (smt_duration > 0, not vacuous). SCOPE: PARTIAL is honest — single uninterpreted binary predicate, no additional filter conjuncts, non-nullable right column, LEFT join only (not RIGHT).
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 24485417}, panicked=False

### `ProjectAggregateMerge` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectAggregateMergeRule.java
- Attempts used: 36
- Last updated: 2026-09-22T22:51:42.740589+00:00
- Reason / notes: The encoding is non-vacuous and correctly structured: before() computes an unused third aggregate per group and projects around it (fields 0,1,3 of 4), while after() omits that call and identity-projects the reduced aggregate (fields 0,1,2 of 3) — genuinely different plans over properly introduced and shared uninterpreted symbols (distinct ops f1/f2/f3, the same group-key op g and table S on both sides), matching the exact Project-over-Aggregate shape and column permutation Calcite's rule produces for this input family, with no precondition the removal half requires. The shape restrictions (single group key, plain field-reference projections, one of three calls) are a genuine, specific, non-degenerate sub-case of the real rule rather than a symbol-sharing artifact, and the excluded COALESCE(SUM,0)→SUM0 half rests on aggregate-function algebra QED fundamentally cannot model (it treats distinct aggregate calls as unrelated uninterpreted group-bag functions), so both limitations are real and are accurately disclosed by the concrete SCOPE: PARTIAL line — making the universal proof meaningful rather than vacuous or over-claimed. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 97910084}, panicked=False

### `ProjectCalcMerge` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectCalcMergeRule.java
- Attempts used: 9
- Last updated: 2026-09-23T12:07:54.814839+00:00
- Reason / notes: The encoding faithfully desugars the Project-over-Calc match (a Calc as filter(B)∘project(E0,E1), exact because the fixed shape has no local variables, so filter-then-project order is precise) and the after() side is exactly what RexProgramBuilder.mergePrograms produces for a null-condition top program — the bottom's condition B unchanged and the top's P0, P1 with their references to the calc outputs translated to E0, E1 — with the same uninterpreted symbols (B, E0, E1, P0, P1) correctly shared between sides, matching how the real rule reuses the bottom's expressions. before() and after() are structurally different plan trees (extra intermediate project layer vs. inlined composition), so the proof is of the genuine merge identity, not a vacuous before==after, and the source rule's guards (no RexOver, no subqueries) are embodied by the DSL's per-row uninterpreted projection semantics rather than silently dropped. The fixed 2-column input, two bottom projections, and two top projections are an honestly and specifically tagged PARTIAL restriction imposed by the DSL's lack of symbolic arity, leaving a non-degenerate, useful special case of the rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 373958}, panicked=False

### `ProjectCorrelateTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectCorrelateTransposeRule.java
- Attempts used: 62
- Last updated: 2026-09-23T11:27:08.750587+00:00
- Reason / notes: The encoding is a genuine, non-vacuous special case: before() projects above a 4-column cross product while after() prunes each input to one column first, so the plans are structurally different and the SMT proof did real work, and an uncorrelated INNER Correlate is semantically exactly a true-condition INNER join, so the shape corresponds to an instance where Calcite's rule actually fires (one column pruned per side defeats locateAllRefs(), and the trailing identity top-project mirrors the rule's always-emitted newProject). The PARTIAL tag is specific and honest: the uncorrelated/bare-reference/INNER/2-column-per-side restrictions are declared, the essential generality gap (re-indexing of correlated RexFieldAccess and other correlate join types) is a genuine DSL limitation since no Correlate builder exists, and the remaining result is still a real, useful column-pruning-below-product rule rather than a degenerate one. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 76322833}, panicked=False

### `ProjectFilterTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectFilterTransposeRule.java
- Attempts used: 39
- Last updated: 2026-09-22T23:10:32.128421+00:00
- Reason / notes: The encoding is a faithful fixed-shape instantiation of the rule's whole-expressions mode: before() = Filter(P(E(x,y), y), S) → Project(E(x,y), F(x)), and after() hoists E and F into Project(S, [x, y, E(x,y), F(x)]), filters on the hoisted columns via the same uninterpreted P, and projects them back out — exactly the sub-project/filter/top-project shape Calcite's Replacer builds. The proof is non-vacuous (the plans genuinely differ: filter over raw scan with embedded uninterpreted calls vs. filter over a larger intermediate projection), symbols are correctly shared (E, F, P reused by name across both sides, with no spurious identification of independent expressions), and no hidden preconditions are needed since bag-semantics commutation of Filter and Project holds for any uninterpreted operators and all column types — which the prover checked universally. The remaining gap to the full rule (arbitrary numbers of input columns, project expressions, and filter operands, plus ref-collection across arbitrary shapes) is inherent to RuleScript's lack of let-binding/bounded-schema constructs, not an avoidable hard-coding; the SCOPE: PARTIAL line is specific, accurate, and the rule is a useful non-degenerate special case. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 84999541}, panicked=False

### `ProjectJoinRemove` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectJoinRemoveRule.java
- Attempts used: 24
- Last updated: 2026-09-25T14:21:40.559005+00:00
- Reason / notes: The encoding is a non-vacuous, correctly-modeled special case of the source rule: it captures the LEFT-join self-join-on-unique-key form where projecting only the preserved column lets the join be eliminated, and QED must genuinely reason about uniqueness implying exactly-one-match (no null-extension, no duplicates) to establish equivalence with the plain scan projection. The SCOPE: PARTIAL line honestly and specifically names every restriction (LEFT-only, self-join, single non-nullable column, project on preserved column only), the uniqueness precondition is faithfully encoded via `unique=true`, and the concrete `EQUALS` condition is consistent with the source rule's requirement that join keys be extractable as equalities via `splitJoinCondition`.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 65425666}, panicked=False

### `ProjectJoinTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectJoinTransposeRule.java
- Attempts used: 10
- Last updated: 2026-09-22T11:48:04.948714+00:00
- Reason / notes: The encoding is a faithful, non-vacuous special case: `before()` (inner join of raw scans with condition C(TL(l),TR(r)) and top projection G(TL(l),TR(r))) and `after()` (the same C and G rewired onto pushed per-side projections π_TL, π_TR below the join) are structurally distinct plans, all symbols are uninterpreted, and the symbol sharing matches exactly what the rule's decomposition requires — no hard-coded predicate, no spurious coupling, and no missing precondition for the inner-join case (no uniqueness/NOT NULL assumptions are needed for this bag identity). The narrowing to inner joins, single-column sides, and condition/projection factoring through the same shared per-side expressions is a genuine, specific, and explicitly disclosed special case (the SCOPE line says so, and single-column sides are in any case forced by the DSL's one-column scans, so the full multi-column/all-join-kind rule is not expressible regardless); the proved universal equivalence is precisely the core transformation of ProjectJoinTranspose, so the result is useful and not misleading.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 70527875}, panicked=False

### `ProjectMerge` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectMergeRule.java
- Attempts used: 6
- Last updated: 2026-09-22T01:28:12.707767+00:00
- Reason / notes: `before()` (a Project stacked on a Project) and `after()` (a single Project) are structurally distinct, so the proof is non-vacuous and matches ProjectMerge's actual shape of collapsing two projections into one. Both projection layers are fully uninterpreted functions (`Bottom`, `Top`), and `Top`/`bottomExpr` are correctly shared as the *same* symbols on both sides, so what is proved is the fully general term-identity `Project(Top(Bottom(c)), Source) == Project(Top(Bottom(c)), Project(Bottom(c), Source))` rather than any concrete predicate or fixed arity-dependent structure. None of the source rule's guards (convention match, correlation variables, bloat/force) are bag-semantic preconditions, so nothing logical is missing; the only limitation is single-column arity, which is inherent to this DSL (scans and `Project` are 1-column) and does not narrow the algebraic content, since the per-expression composition is already fully general. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 308708}, panicked=False

### `ProjectReduceExpressions` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java

Note: ReduceExpressionsRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectReduceExpressionsRule` variant (not CalcReduceExpressionsRule, FilterReduceExpressionsRule, JoinReduceExpressionsRule, WindowReduceExpressionsRule, which are separate rules ported under their own spec names).
- Attempts used: 21
- Last updated: 2026-10-14T08:59:46.055967+00:00
- Reason / notes: This is a genuine, non-vacuous special case of the real rule: ProjectReduceExpressionsRule's simplification pass (RexSimplify.simplifyPreservingType) does in fact rewrite a project expression like FALSE AND P(x) to the literal FALSE, and QED can only prove Project([FALSE ∧ P]) ≡ Project([FALSE]) by using its actual three-valued AND semantics (had AND been modeled as uninterpreted, the proof would fail), so the result is not trivial. Nothing is illegitimately hard-coded: the predicate P is uninterpreted (no concrete "x > 5" baked in), the same Source symbol is correctly shared between before() and after(), and the source rule carries no side preconditions (no keys/NOT NULL) that the encoding silently drops — the matchNullability cast the real rule would add is semantically inert on a literal. The narrowing to a purely-logical constant is forced by QED's inability to evaluate concrete operator algebra (the full rule's constant folding of e.g. 1+2 is a fundamental QED limitation, not a missing DSL feature), and the SCOPE: PARTIAL line states the assumption accurately and specifically. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 351667}, panicked=False

### `ProjectRemove` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectRemoveRule.java
- Attempts used: 41
- Last updated: 2026-09-22T03:32:07.097090+00:00
- Reason / notes: The encoding faithfully and non-vacuously captures the rule's core transformation: `before()` is a Project whose sole expression is `source.field(0)`, an input reference to field 0 of the same scan, which exactly embodies the `RexUtil.isIdentity` precondition in the pattern, and `after()` is the bare scan — so the proof is precisely the "remove an identity project" rewrite, not a `before() == after()` tautology. The arity-1 restriction is genuine (the DSL's `Project` carries a single expression and a `Scan` exposes exactly one column, so an n-ary identity projection is inexpressible without a DSL extension), is specifically and honestly tagged `SCOPE: PARTIAL`, and the instance remains a real, useful sub-case of the original any-arity rule (e.g., `SELECT a FROM t` → `t`) rather than a degenerate encoding.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 299417}, panicked=False

### `ProjectSetOpTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectSetOpTransposeRule.java
- Attempts used: 68
- Last updated: 2026-09-22T12:40:02.027113+00:00
- Reason / notes: The encoding faithfully captures the core semantics of ProjectSetOpTranspose: a top-level uninterpreted projection F is pushed below a UNION ALL (correctly using `union(true, …)` to match the rule's `setOp.all` precondition), so before() = Project(F(x), UnionAll(L, R)) and after() = UnionAll(Project(F(x), L), Project(F(x), R)) — structurally distinct and non-vacuous. The symbol sharing is correct (one shared F, independent L and R), no preconditions are missing, and the SCOPE: PARTIAL line honestly and specifically names every restriction (2-input, single-column, single uninterpreted expression) relative to the original's arbitrary-arity/multi-expression/OVER-branch generality, making the proof a meaningful universal statement rather than a degenerate or coincidentally-provable artifact. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1702916}, panicked=False

### `ProjectTableScan` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectTableScanRule.java
- Attempts used: 6
- Last updated: 2026-09-23T12:36:59.032862+00:00
- Reason / notes: `before()` (Project over Scan) and `after()` (Project over Project over Scan) are structurally distinct, and the proved equivalence is exactly the rule's bag-semantic content: the `BindableTableScan(T,{0,2})` is modeled as `Project([T.0,T.2], Scan(T))` — the only bag meaning that operator can have, per the ProjectableFilterableTable contract — and the remapped project faithfully reproduces the source rule's `Mappings.target` bookkeeping (F(r0,r2)→F(y0,y1), r2→y1), reusing the *same* uninterpreted `F` on both sides, which models "the same expression, re-referenced" correctly rather than over-constraining. The fixed 3-column arity / selected set {0,2} / non-identity project shape is forced by QED's concrete pattern instantiation (its JSON format has no way to quantify over arity or carry a first-class column-restricted scan, so no DSL extension could generalize it), the uninterpreted `F` still makes the proof hold for all functions and table contents of that shape, and the `SCOPE: PARTIAL` line states these assumptions specifically and accurately.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 316708}, panicked=False

### `ProjectToCalc` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectToCalcRule.java
- Attempts used: 8
- Last updated: 2026-09-23T11:44:56.618542+00:00
- Reason / notes: The encoding faithfully maps the rule's two node shapes — `before()` is the bare `Project([E1,E2], S)` (the `LogicalProject`), and `after()` is `Project([E1,E2], Filter(TRUE, S))`, the DSL's filter-then-project encoding of a `LogicalCalc` whose RexProgram condition is absent (null→TRUE), in the correct order (condition on input columns, then projection), with E1/E2 as two independent uninterpreted projection operators rather than over-constrained shared ones. `before()` and `after()` are genuinely structurally distinct (the extra TRUE filter), so the proof is not vacuous; its near-instant success is consistent with the rule being a semantic no-op (a Calc with no condition *is* a Project), so the triviality is inherent to the rule, not a porter error. The only narrowing — 2-column input / 2-expression list — is honestly and specifically tagged PARTIAL, is not a concrete-value or join-type hard-coding that would change the semantic claim, and since "Filter(TRUE, X) ≡ X" is arity-independent, the 2-column instance is a faithful, non-degenerate representative of the fully general rewrite. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 92708}, panicked=False

### `ProjectToSemiJoin` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRule.java

Note: SemiJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectToSemiJoinRule` variant (not AggregateToSemiJoinRule, JoinOnUniqueToSemiJoinRule, JoinToSemiJoinRule, which are separate rules ported under their own spec names).
- Attempts used: 26
- Last updated: 2026-09-25T14:58:01.150197+00:00
- Reason / notes: The encoding faithfully captures the INNER-join branch of Calcite's ProjectToSemiJoinRule: before() is a left-fields-only Project over L INNER-JOIN (pure GROUP BY on R.1) with the equi condition L.0 = group key, and after() is the identical Project over L SEMI-JOIN the pre-aggregate relation R on the remapped condition L.0 = R.1 — exactly the transformation perform() performs (semi-join input is aggregate.getInput(), i.e. the shared rawRight symbol, with the right join key correctly remapped to the group key's source column), and the structural preconditions the rule checks (isEqui, rightSet = all group keys) are correctly baked in by using a concrete EQUALS on exactly the group key. before() and after() are genuinely different plans (inner join over a 1-column aggregate vs. semi-join over a 2-column raw scan), so the proof is non-vacuous, and the join→semi-join equivalence holds for every instantiation including nulls. The narrowing (INNER only — the LEFT branch is a distinct join-elimination rewrite, single equi key, no aggregate calls, project modeled as a left-field permutation) is a real, specific, non-degenerate fragment of the rule and is honestly disclosed in the one-line SCOPE tag.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 77653875}, panicked=False

### `ProjectWindowTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectWindowTransposeRule.java
- Attempts used: 8
- Last updated: 2026-10-14T09:33:49.370956+00:00
- Reason / notes: The encoding is a genuine, non-vacuous PARTIAL instance of the rule: it models a whole-partition, single-group window as an inner equi-join of the input with a group-by on the partition key (the maximal expressible form, since QED fundamentally has no Window/ordering semantics), bakes in exactly the rule's applicability precondition — column x referenced by neither the window functions (f(v), g(v)), the partition key, nor the top projection — and uses the same uninterpreted Top symbol with correctly shifted indices (0,1,4,5 → 0,1,3,4) to mirror the rule's index adjustment, so before() and after() genuinely differ and the proven pushdown identity is the real substance of the rewrite. The SCOPE: PARTIAL line accurately and specifically names the assumed restrictions (single partition group, whole-partition functions over one operand column, exactly one pruned passthrough column, top projection ignoring it), so the result is an honest, non-degenerate special case rather than an overclaimed full rule. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 208574417}, panicked=False

### `PruneEmpty` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/PruneEmptyRules.java

Note: PruneEmptyRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `PruneEmptyRule` variant (not RemoveEmptySingleRule, which are separate rules ported under their own spec names).
- Attempts used: 61
- Last updated: 2026-09-23T11:35:08.547936+00:00
- Reason / notes: The encoding is a faithful, non-vacuous rendering of Calcite's `RemoveEmptySingleRuleConfig.FILTER` instance (a Filter over an empty `Values` collapses to that same empty `Values`): the filter condition is an uninterpreted predicate symbol, the row type is an uninterpreted VarType, `before()` and `after()` are structurally different (the Filter node is removed, so the proof is not vacuous), and this variant carries no extra preconditions (no metadata check, no grand-total guard) that the encoding omits. The SCOPE: PARTIAL tag is honest and specific — `PruneEmptyRules` is a family of a dozen-plus distinct rewrites, this record covers the Filter member, which is a genuine standalone Calcite rule instance rather than an artificial narrowing, and the only residual restriction (a single-column schema, dictated by the DSL's concrete type handling) has no logical bearing on this schema-independent equivalence. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1262583}, panicked=False

### `PruneSingleValue` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SingleValuesOptimizationRules.java
- Attempts used: 62
- Last updated: 2026-09-23T13:50:51.453158+00:00
- Reason / notes: The encoding is non-vacuous and matches the source rule's core inner-join transformation exactly: `O ⋈_{jcond} Values{true}` is rewritten to `π(o, true)(σ_{jcond(o, true)} O)`, with the same uninterpreted predicate "jcond" correctly shared (in `before` its second argument is bound to the single-row Values' column, which holds `true`, matching the `true` literal in `after`'s filter and project, in the correct left-then-right output order). The proof universally quantifies over the table `O`, its row type, and the join condition — the parts that actually vary across queries — and no source precondition (single-row Values, non-outer join type) is silently dropped; the INNER choice satisfies the source's `isJoinTransformable` check. The restriction to the right-side inner-join case with a one-column Boolean-literal row is genuinely narrower than the full rule (which also covers LEFT/RIGHT/SEMI/LEFT_MARK variants, multi-column rows, and the Project-over-Values "WithExpr" variants, with the `true` constant hard-coded only because the DSL exposes no builder for a Values row with an uninterpreted constant), but that is specifically and honestly tagged as SCOPE: PARTIAL, and the instance remains non-degenerate, so the "provable" verdict is a faithful verification of the stated special case. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 67434084}, panicked=False

### `RemoveEmptySingle` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/PruneEmptyRules.java

Note: PruneEmptyRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `RemoveEmptySingleRule` variant (not PruneEmptyRule, which are separate rules ported under their own spec names).
- Attempts used: 10
- Last updated: 2026-09-23T11:45:40.620837+00:00
- Reason / notes: The encoding faithfully captures the AGGREGATE variant of RemoveEmptySingleRule — a non-grand-total aggregate (group set non-empty, matching the isNotGrandTotal precondition) over an empty Values of the input row type, rewritten to an empty Values of the aggregate's output row type (group column + f column), exactly as the source onMatch builds — with no hidden preconditions, no concrete predicates, and f/S_Type left uninterpreted. The proof is non-vacuous (before() contains a real group operator that after() eliminates; they are structurally distinct, not a trivial before()==after()), and no over-constrained symbol sharing changes the claim, since the input is empty and the aggregate's algebra is irrelevant. The scope is honestly PARTIAL: the variant choice is declared in the SCOPE line (project/filter/calc are also expressible but unchosen; sort/window are genuinely out of QED's bag-semantic reach), and the code comment discloses the further instantiation to one group key plus one aggregate call on column 0 — a specific, non-degenerate special case, since the theorem "grouped aggregate over empty input = empty" is independent of the number of keys/calls. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 28738167}, panicked=False

### `SemiJoinFilterTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinFilterTransposeRule.java
- Attempts used: 16
- Last updated: 2026-09-23T12:46:54.459492+00:00
- Reason / notes: The encoding faithfully captures the original rule: before() is SemiJoin(Filter(X, F), Y, C) and after() is Filter(SemiJoin(X, Y, C), F), with the same uninterpreted filter predicate F over X's columns and the same uninterpreted join condition C over (X,Y) columns in both sides. The join kind is SEMI (matching the source rule's `Join::isSemiJoin` predicate and `JoinRelType.SEMI` construction), the tree structures are genuinely different (filter below vs. above the join), and the fixed 2-column arity on X and Y is not a meaningful restriction since all predicates are uninterpreted symbols—the rule's logical content is purely about tree shape, which is fully captured. No preconditions are missing, no symbols are incorrectly shared or independent, and `// SCOPE: FULL` is honest. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 905333}, panicked=False

### `SemiJoinJoinTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinJoinTransposeRule.java
- Attempts used: 66
- Last updated: 2026-09-23T12:45:22.163199+00:00
- Reason / notes: The encoding is faithful: before() = (X ⋈_C Y) ⋉_S Z and after() = (X ⋉_S Z) ⋈_C Y are structurally different plans whose bag-equality is exactly the source rule's left-push transposition (no vacuity), and the flat-column arithmetic is correct — the semi-join emits only the left's columns, Z sits at flat 2 in before and flat 1 in after, and reapplying the same sOp/cOp symbols onto the re-anchored columns correctly models the rule's RexInputConverter adjustment while preserving the lower join's condition (join.copy) via shared C. All of the source rule's gating preconditions are either encoded or subsumed: upper join is SEMI, lower join is not semi and satisfies canPushLeftFromAbove (INNER does), and S referencing only X (plus Z) captures the nKeysFromX > 0 branch, in which the rule rejects mixed X/Y keys anyway. The remaining restrictions — single-column scans, INNER lower join rather than the full set of pushable join types, and left-push branch only — are genuine, non-degenerate, and accurately declared in the SCOPE: PARTIAL line, with no concrete predicates or join types baked in that the rule doesn't require. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 76951167}, panicked=False

### `SemiJoinProjectTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinProjectTransposeRule.java
- Attempts used: 80
- Last updated: 2026-09-23T15:06:19.982318+00:00
- Reason / notes: The encoding faithfully mirrors the rule's semi-join branch: before() = SemiJoin(Project([P0,P1], X), Y, C) and after() = Project([P0,P1], SemiJoin(X, Y, C')), with the same uninterpreted P0, P1, C symbols shared across both patterns and the condition correctly "adjusted" — in both patterns C is evaluated over the values (P0(x0,x1), P1(x0,x1), y0, y1), exactly matching `adjustCondition`'s substitution of the projection expressions for the LHS references — and the two plans are structurally different, so the proof is non-vacuous; the SEMI join kind is correctly preserved on both sides (and unlike an inner join, the per-row existence correspondence keeps bags equal). The only restriction relative to the source — semi-joins only, whereas Calcite also fires on anti-joins — is specific and honestly declared in the `SCOPE: PARTIAL` line, and the fixed two-column arity is an inherent fixed-arity DSL artifact that constrains no uninterpreted logic; allowing C to reference the left (projected) columns, which Calcite itself forbids, only makes the proven claim stronger, not weaker.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 73750625}, panicked=False

### `SemiJoinRemove` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRemoveRule.java
- Attempts used: 9
- Last updated: 2026-10-14T09:55:58.135184+00:00
- Reason / notes: The encoding faithfully mirrors the source rule's transformation — replacing a SEMI join with its left input (`getInput(0)`), with correct join kinds and uninterpreted, shared condition/right symbols rather than hard-coded predicates — and `before()`/`after()` are structurally distinct, so the proof is non-vacuous. The PARTIAL scope is honest and forced by a genuine limitation: the source rule's soundness rests on the global "all SemiJoins are advisory" plan property, which has no form as a local bag-equivalence (the unconditioned claim L SEMI⋈ R ≡ L is actually false, since a row of L with no match in R is dropped), so the locally-guaranteed redundancy instance the porter encoded is the only QED-provable statement and is a genuine, non-degenerate special case.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 24322292}, panicked=False

### `SetOpToFilter` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SetOpToFilterRule.java
- Attempts used: 11
- Last updated: 2026-09-22T23:25:33.293479+00:00
- Reason / notes: The encoding exactly captures Calcite's rewrite for the single-source, two-filter case — `Union(DISTINCT, [σ_P1(S), σ_P2(S)])` → all-fields group-by (no agg calls) over `σ_(P1∨P2)(S)` — and is faithful in the details: `union(false, ...)` preserves the rule's `!setOp.all` precondition, and rendering `.distinct()` as a group-by-all-fields Aggregate is precisely what Calcite's `RelBuilder.distinct()` itself builds, so the right side matches the rule's actual output (single branch, no 1-input set op). The proof is non-vacuous (the two sides are structurally different and require genuine filter-over-set-op reasoning with properly uninterpreted P1/P2 over a shared single source, exactly the sharing the rule requires), and the declared `SCOPE: PARTIAL` is honest and specific — 2-input UNION DISTINCT over filters on one shared source is a genuine, non-degenerate special case of the general arity/multi-source/INTERSECT/MINUS rule.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 74560667}, panicked=False

### `SubQueryRemove` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SubQueryRemoveRule.java
- Attempts used: 87
- Last updated: 2026-10-14T11:03:27.923521+00:00
- Reason / notes: The encoding faithfully mirrors Calcite's uncorrelated-EXISTS-under-Filter (logic=TRUE) branch: before is `Filter(EXISTS(S), R)` and after is `Project([0], InnerJoin(true, R, GroupBy(true, Project(true, S))))`, which matches the original's `push(S); project(true as i); aggregate(groupKey(0)); join(INNER, true); filter(true→noop); project([0])` sequence. The single-column-R restriction (imposed by `RelRN.scan` producing one column) and the filter-condition-is-purely-EXISTS restriction are both honestly stated in the SCOPE line, and the proof is non-vacuous since the two sides have structurally different shapes (filter-with-subquery vs join-with-aggregate).
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 23822333}, panicked=False

### `UnionEliminator` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/UnionEliminatorRule.java
- Attempts used: 10
- Last updated: 2026-09-22T06:23:57.022034+00:00
- Reason / notes: The encoding exactly mirrors the rule's matches condition for the union variant — a set op with all=true over exactly one input (input.union(true)) rewritten to that same input — so the proof is the genuine, non-vacuous bag-semantic theorem that a one-input UNION ALL equals its input, with a shared uninterpreted Input scan standing in for any relation of any type (nothing concrete is baked in). The SCOPE: FULL tag is honest for the rule as named: the file's INTERSECT and MINUS configs are separate rules (IntersectEliminator/MinusEliminator), and their all=true (bag) form is a documented QED limitation (only the set variant of INTERSECT/MINUS is modeled, per the serializer's when !all guards), so the porter assumed nothing the UnionEliminator rule itself does not require — this is the full rule, not a narrowed special case.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 277834}, panicked=False

### `UnionMerge` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/UnionMergeRule.java
- Attempts used: 5
- Last updated: 2026-09-22T01:46:37.725949+00:00
- Reason / notes: The encoding faithfully reproduces Calcite's UnionMerge onMatch for the UNION ALL instance — top union with a nested union in the second input (plus a third input, matching the source's "topOp.getInputs().size() may be more than 2" flattening) rewritten to the flat n-ary union, with the same all=true flag on both sides — and before() and after() are structurally different, so the proof is a genuine, non-vacuous bag-equality result over four independent uninterpreted scans sharing the row type that union requires. The SCOPE tag is accurate and specific: it names exactly the excluded instances (UNION DISTINCT, INTERSECT, MINUS), and the restriction is at least partly a real QED/DSL boundary (only the set variants of INTERSECT/MINUS are modelable — the serializer has no case for their ALL forms), and Calcite itself registers Union/Intersect/Minus merge as three separate rule instances, so this is a useful, non-degenerate special case of the rule family rather than an over-constrained fake. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1814042}, panicked=False

### `UnionPullUpConstants` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/UnionPullUpConstantsRule.java
- Attempts used: 34
- Last updated: 2026-09-22T23:13:07.889947+00:00
- Reason / notes: The proof is of a real, nontrivial declared special case: a shared boolean-constant projection duplicated under a union-all is hoisted above the union while the nonconstant column is carried through. This matches the source rule's constant-pullup transformation for that instance, and the restrictions (boolean literal, union-all, one constant/nonconstant column, explicit projection) are honest and do not make the equivalence vacuous.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 83683833}, panicked=False

### `UnionToDistinct` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/UnionToDistinctRule.java
- Attempts used: 7
- Last updated: 2026-09-22T10:16:12.700531+00:00
- Reason / notes: The encoding matches the rule's exact shape: `before()` is a UNION DISTINCT (`union(false)`) over two independent uninterpreted inputs, and `after()` is an Aggregate grouping by all output columns with zero aggregate calls over the same inputs with `union(true)` — precisely what Calcite's `relBuilder.distinct()` produces — with the shared "T" type being a genuine union precondition rather than an over-constraint, no uniqueness assumptions, and no triviality since the two sides are structurally different. The only restrictions are the fixed two-input arity and single-column inputs, both forced by the DSL's construction (fixed-arity `union`, single-column `Scan`) and honestly flagged in the SCOPE line as a PARTIAL special case. The result is a genuine, non-degenerate instance of the rule: the dedup-by-aggregate rewrite over arbitrary (possibly duplicate) bags of the same type, which is the rule's full semantic content at 2 inputs × 1 column. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 65529417}, panicked=False

### `UnionToValues` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/UnionToValuesRule.java
- Attempts used: 24
- Last updated: 2026-10-14T10:25:12.878717+00:00
- Reason / notes: `before()` (a 3-way UNION ALL of three constant `Values` nodes) and `after()` (a single `Values`) are structurally different plans, and the deliberately duplicated `(3,4)` row makes the proof a genuine check of bag-multiplicity preservation under UNION ALL — precisely the semantic content of the source rule's `all=true` branch — with identical row types on all inputs satisfying the rule's `equalsSansFieldNamesAndNullability` precondition. The hard-coded tuples, arity, and column count are forced rather than lazy over-constraint: the DSL has no uninterpreted constants or literal-row symbols for `Values` (uninterpreted symbols are only predicates/projections/aggregate ops/scan tables), so this concretization is the only encodable form and the PARTIAL tag discloses it honestly and specifically. The unmodeled `all=false` distinct variant is an omitted sibling instance, not a soundness hole in this proof, and the encoding is not degenerate (dropping one copy of `(3,4)` would have made it unprovable).
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 335917}, panicked=False

### `UnnestDecorrelate` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/UnnestDecorrelateRule.java
- Attempts used: 40
- Last updated: 2026-09-23T14:32:27.963511+00:00
- Reason / notes: The encoding faithfully captures the rule's semantic claim: the INNER correlate over a single-row Values feeding a correlated uncollect is abstracted as a bag join of the left with an element relation under the shared uninterpreted membership predicate M, with the outer (and optionally composed inner) projection P structurally applied only to the element column, mirroring the rule's side conditions (INNER only, outer project uses no left columns, single-row values, single correlated array column). The two sides genuinely differ — before joins the full two-column left while after drops the unused column via a projection below the join — so the SMT proof is of a real, non-vacuous bag-semantic column-dropping/projection-commutation identity rather than of identical plans, and all symbols (L, E, M, P) are shared exactly as the rewrite requires, with no spurious uniqueness assumptions. The `SCOPE: PARTIAL` line is honest and specific (left = array column + exactly one unused column; single-column element relation), the narrowing stems from QED's genuine lack of list/uncollect semantics (so a DSL correlate operator wouldn't buy more generality), and the proved special case is non-degenerate and captures the core soundness argument of the original rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 97560791}, panicked=False

### `ValuesReduce` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ValuesReduceRule.java
- Attempts used: 42
- Last updated: 2026-09-23T13:58:21.384082+00:00
- Reason / notes: The encoding is a genuine, non-vacuous trace of the rule's PROJECT config — Project([c,a]) over the 2-row Values (1,2,3),(4,5,6) folds to the concrete Values (3,1),(6,4) with the tuples correctly recomputed, the Project node eliminated, and non-empty Values precondition satisfied — so before() and after() are structurally different and the proof is not vacuous. The heavy concreteness (fixed tuples, fixed projection, no filter) is intrinsic rather than a symbol-sharing or under-generalization error: ValuesReduce is a constant-folding rule whose inputs and outputs are literal data that QED cannot stand in for with uninterpreted symbols, so no uninterpreted/general form is expressible, and the absence of the filter half is visible in before() and covered by the honest, specific PARTIAL scope tag (input-reference-only project). A stronger instance (e.g. the doc's `a - b` / `a + b > 4` example) would require literal/comparison builders the DSL lacks, but the proven instance is a real, correctly-computed special case rather than a degenerate identity. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 73364667}, panicked=False

### `WindowReduceExpressions` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java
- Attempts used: 0
- Last updated: 2026-10-14T15:59:53.922881+00:00
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 91459}, panicked=False

### `AggregateCaseToFilter` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateCaseToFilterRule.java
- Attempts used: 22
- Last updated: 2026-10-13T16:46:38.669539+00:00
- Reason / notes: QED models aggregates as uninterpreted bag functions and, per its stated limitation, "knows nothing about a specific aggregate function's algebra beyond bag equality of its input," so it can only prove two aggregate calls equal when their input bags are literally identical. This rule's soundness rests exactly on the NULL-identity/NULL-skip property it lacks: the pre-rewrite aggregate consumes `{x : p} ∪ {NULL : ¬p}` while the post-rewrite filtered aggregate consumes `{x : p}`, and those bags differ precisely by the CASE's non-matching NULLs (or the ELSE-0s in the SUM0 styles), an asymmetry no non-trivial special case removes. Hence even closing the (secondary) DSL gap — `AggCall`/`JSONSerializer` currently expose no `FILTER` argument — could not make it provable, because QED still could not equate the two uninterpreted aggregates over non-bag-equal inputs. ```

### `AggregateFilterToCase` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateFilterToCaseRule.java
- Attempts used: 0
- Last updated: 2026-10-13T18:58:37.360421+00:00
- Reason / notes: The before-side is an aggregate call carrying a SQL `FILTER (WHERE ...)` clause. `RelRN.AggCall` has no filter field, `Aggregate.semantics()` builds calls without a filter argument, and `JSONSerializer` emits only operator/operand/distinct/ignoreNulls/type for aggregate calls, so the pattern the rule matches cannot reach the prover. Re-encoding the filter as a `Filter` below the aggregate changes the result (groups with no matching rows disappear, which FILTER preserves). Equating the filtered call with `agg(CASE WHEN c THEN x END)` would additionally need the aggregate's NULL-skipping algebra, but QED treats aggregates as uninterpreted functions of their input bag and the two operand bags differ by the CASE's NULLs. The sibling rules AggregateFilterToFilteredAggregate and AggregateCaseToFilter were skipped for the same reasons.

### `AggregateFilterToFilteredAggregate` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateFilterToFilteredAggregateRule.java
- Attempts used: 25
- Last updated: 2026-10-13T17:49:23.216663+00:00
- Reason / notes: The rule's after-side is a per-call SQL `FILTER (WHERE P)`, which the pipeline cannot carry: `RelRN.AggCall` has no filter field and `Aggregate.semantics()` hardcodes the aggregateCall filter arg to `null`, and `JSONSerializer` emits only operator/operand/distinct/ignoreNulls/type for aggregate calls, so even a hand-built `LogicalAggregate` with a filter would be stripped before reaching the prover. qed.pdf §4.1 confirms QED's aggregate normal form is an uninterpreted function of its input bag with only the fixed non-NULL prefilter (COUNT(*) vs COUNT(col)), so the rule's core identity — `AGG FILTER(P) over R` = `AGG over Filter(P, R)` — is an aggregate-algebra fact QED fundamentally cannot express or prove. No re-encoding in the core language closes the gap (encoding the after-side as the before-side is a trivial self-equality, and a `CASE`-based surrogate would require QED to know its aggregate's algebra), so UNSUPPORTED is the correct conclusion, not a missed encoding. ```

### `AggregateGroupingSetsToUnion` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateGroupingSetsToUnionRule.java
- Attempts used: 26
- Last updated: 2026-10-13T18:06:14.133230+00:00
- Reason / notes: The rule's only match is a non-SIMPLE aggregate (real GROUPING SETS/CUBE/ROLLUP), but RuleScript's `Aggregate` and QED's serialized `group` node carry only a single flat grouping-key function, so the "before" operator is unnameable in the pattern language and `extend_dsl_file` can't close the gap because the fixed Rust prover has no multi-set-aggregate semantics to consult (a new JSON field would be ignored or rejected, yielding a vacuous proof against a simple aggregate). Compounding this, the rule folds `GROUPING` aggregate calls into literal bit-mask constants, which requires knowledge of that specific aggregate's algebra that QED explicitly lacks (aggregates are uninterpreted beyond bag equality of their input), and no meaningful narrower case exists: the rule bails on single-set/SIMPLE aggregates, and any encodable stand-in for the before side is either the trivial identity or circular (defining it in terms of the after side's own semantics).

### `AggregateMeasure` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `AggregateMeasureRule` variant (not AggregateMeasure2Rule, ProjectMeasureRule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
- Attempts used: 47
- Last updated: 2026-10-13T18:44:08.315200+00:00
- Reason / notes: The rule's validity rests on Calcite's measure invariants — that a measure is constant within the group-key partition and that AGG_M2V(c) coincides with SINGLE_VALUE(M2X(c, SAME_PARTITION(keys))) — which QED cannot model: every aggregate (AGG_M2V, SINGLE_VALUE) and scalar op (M2X, SAME_PARTITION) is an uninterpreted symbol with no axioms (in particular no "single_value over a group-constant bag returns that value" law), and the partition-constancy invariant is not expressible as a table constraint in the DSL. Hence the prover's counterexample is a genuine one in QED's semantics, not an encoding artifact — the two sides name different aggregate symbols over related bags, which no name-matching fix can align — and since the rule's firing condition is at least one AGG_M2V call whose replacement inherently introduces the SINGLE_VALUE/M2X construction, no non-vacuous special case avoids the barrier, so UNSUPPORTED is correct. ```

### `AggregateMeasure2` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `AggregateMeasure2Rule` variant (not AggregateMeasureRule, ProjectMeasureRule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
- Attempts used: 25
- Last updated: 2026-10-13T19:58:16.055475+00:00
- Reason / notes: The rule's validity rests entirely on Calcite's measure invariant — AGG_M2V(m) over a group equals the scalar subquery produced by mq.expand(m) (with AGG_M2M standing in for measure-to-measure references) — while QED models AGG_M2V, AGG_M2M, and any subquery/aggregate body as uninterpreted symbols with no axioms connecting them, so that equality is not a consequence of bag semantics and is not derivable by any DSL extension (a RexSubQuery builder would let the pattern be written but QED would soundly report "not provable" since the two sides are distinct uninterpreted terms). The only non-trivial content of the rewrite is precisely this M2V→subquery replacement; the pass-through of group keys and non-measure agg calls is either trivial or depends on the same measure axiom, so no genuine provable special case exists. ```

### `AggregateMinMaxToLimit` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateMinMaxToLimitRule.java
- Attempts used: 21
- Last updated: 2026-10-13T19:54:31.272646+00:00
- Reason / notes: The rule's after side (scalar subqueries of the form SELECT c FROM t WHERE c IS NOT NULL ORDER BY c ASC/DESC LIMIT 1 over a one-row VALUES) is only equivalent to MIN/MAX if the prover knows that the first row of an ordered bag is its minimum/maximum — but QED assigns no bag-semantic meaning to Sort/Limit (no ordering axioms exist), and MIN/MAX are uninterpreted higher-order aggregates it can only compare by bag equality of their inputs, so it cannot relate them to any ordering at all. Even if the DSL were extended with sort/limit/scalar-subquery builders (JSONSerializer already serializes LogicalSort and RexSubQuery, so that gap is closable), the semantic gap is fundamental: a faithful after side must carry ordering, and no ordering statement is provable without order semantics. Hence no provable special case exists, and the porter's UNSUPPORTED conclusion is correct (its one minor imprecision — saying QED "models" Sort/Limit as uninterpreted operators rather than leaving them semantically unmodeled — does not change the verdict). ```

### `AggregateProjectStarTable` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateProjectStarTableRule.java
- Attempts used: 11
- Last updated: 2026-10-13T20:12:45.576279+00:00
- Reason / notes: The rule's `after` plan swaps the base-table scan for a scan of a *different* pre-aggregated materialized table chosen from the external planner's lattice, and only when such a materialization exists — so its validity rests on a cross-table catalog invariant (the materialized table's rows equal the base table's pre-aggregation at the given grain) plus conditional planner state, not on pure bag semantics of the shown operators. RuleScript can only introduce scans as independent uninterpreted relations and attach per-table first-order `guaranteed` check constraints, with no operator or constraint form that relates two distinct scans via an aggregate-equivalence, so QED treats the base and materialized scans as unrelated and would refute rather than prove the equivalence. This is a genuine QED/DSL limitation (bespoke materialization semantics + an unexpressible cross-table relational premise), not a symbol-sharing or encoding slip the porter failed to diagnose. ```

### `AggregateReduceFunctions` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateReduceFunctionsRule.java
- Attempts used: 27
- Last updated: 2026-10-13T20:18:04.369766+00:00
- Reason / notes: Every non-degenerate branch of AggregateReduceFunctionsRule is an algebraic identity between distinct aggregate functions (AVG=SUM/COUNT, SUM vs SUM0/COUNT, the STDDEV/VAR/COVAR/REGR_SXX/SYY formulas over SUM/COUNT/REGR_COUNT, and MAX/MIN/AVG(x)=x when x is a group key), while QED treats each named aggregate as an uninterpreted symbol and has no axioms relating one aggregate's output to another's or to scalar arithmetic. The porter's empirical minimal test — the easiest branch, MAX(x) with x as the sole group key reduced to a call-free aggregate plus project — returned provable:false, which is exactly what the semantics predict, since no instantiation constraint forces an uninterpreted aggregate over a group-constant bag to return that constant. There is no narrower non-trivial special case to encode: any branch that actually rewrites an aggregate call differs in function names between before() and after(), so it is unprovable by construction, not by encoding. ```

### `AggregateReduceFunctionsOnGroupKeys` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateReduceFunctionsOnGroupKeysRule.java
- Attempts used: 24
- Last updated: 2026-10-13T20:23:01.777723+00:00
- Reason / notes: The only substantive transformation in this rule is replacing aggregate calls (MAX/MIN/AVG/ANY_VALUE on a group key, or constant-value aggs like STDDEV_POP→0) with a reference to the group key itself — every branch is an algebraic identity about what a specific named aggregate returns, and QED encodes each aggregate as an uninterpreted bag-to-scalar function with no axioms relating its output to the group-constant input or to constants. The porter's minimal empirical test (MAX(x) over GROUP BY x → projection over the call-free aggregate) returned a clean, complete provable:false, which is exactly what the semantics predict for even the simplest branch, so the limitation is in the prover's model of aggregates, not in the DSL or in a symbol-sharing/encoding bug — and since every branch of the rule rests on this same unknown aggregate algebra, no narrower special case is provable either. ```

### `AggregateRemoveDuplicateKeys` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateRemoveDuplicateKeysRule.java
- Attempts used: 22
- Last updated: 2026-10-13T20:32:00.508437+00:00
- Reason / notes: The rule's essential step replaces a dropped group key with an ANY_VALUE aggregate call over the coarser grouping, so the proof requires ANY_VALUE(bag) to equal the unique key value on that group — but per the reference and the DSL source (aggregate ops are introduced only via uninterpreted genericAggregateOp), QED treats aggregates as uninterpreted with no algebra, so it can neither force that equality nor (via extend_dsl_file) model ANY_VALUE's constant-group identity. This is a fundamental QED limitation, not a missing DSL capability or an encoding bug, so no genuine RuleScript encoding of this rewrite (full or key-recovery special case) can be proved. ```

### `AggregateRemoveLiteralAgg` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateRemoveLiteralAggRule.java
- Attempts used: 22
- Last updated: 2026-10-13T20:38:12.887071+00:00
- Reason / notes: The rule's soundness rests entirely on Calcite's internal semantics that `LITERAL_AGG(lit)` returns `lit` for every group, which is a specific algebraic property of one backend aggregate function; QED's model treats every aggregate call as an uninterpreted function of its group's input bag, so the before-side aggregate column is an unconstrained symbol that no amount of structural manipulation (group-key shape, arity, input, or a DSL builder addition) can pin to the constant the after-side project emits. Since the gap is in the prover's uninterpreted-aggregate semantics — not a missing operator or shape in the core language — no encoding, narrower or full, can state this rule without smuggling in exactly the identity QED cannot know, so UNSUPPORTED is the correct conclusion.

### `AggregateStarTable` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateStarTableRule.java
- Attempts used: 7
- Last updated: 2026-10-13T20:36:40.688667+00:00
- Reason / notes: Every branch of this rule (exact match, roll-up, or measure reordering) rewrites `Aggregate(base)` into a plan over a *different* scan whose equality to the base table's aggregation is an external lattice/catalog invariant, not an operator semantics — and QED's verdict is an *unconditional* bag-equivalence over independently-instantiated uninterpreted relations, whose only admitted side information is per-table keys and per-table first-order `guaranteed` check constraints, so the second-order premise "materialized table = aggregation of base table" cannot be expressed or accepted (extending `RelRN`/`JSONSerializer` can't help, since that premise would require semantics only the unmodifiable Rust prover could provide). Moreover, the roll-up branch additionally depends on aggregate roll-up algebra (e.g. sum-of-sums), which QED explicitly does not model, so no narrower sub-case of the rule is provable without the invariant — the porter's UNSUPPORTED verdict is correct.

### `CalcSplit` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/CalcSplitRule.java
- Attempts used: 24
- Last updated: 2026-10-13T20:56:00.453689+00:00
- Reason / notes: CalcSplitRule's correctness rests purely on the definitional identity that a fused Calc operator equals its Filter-then-Project decomposition; QED reasons only about relational-algebraic equivalences between plans built from its core operators, so a "fused" Calc is either (a) encoded as filter().project() in the DSL, making before() and after() structurally identical (a vacuous tautology), or (b) introduced as a new uninterpreted QOp for which QED has no axioms relating it to the Filter∘Project composition (unprovable). No DSL extension can close this gap because any serialization of the fused form either collapses to the split form (tautology) or remains opaque to the prover (uninterpretable), making this a genuine limitation of what a bag-semantic SMT-based equivalence checker can establish for operator-definition identities. ```

### `CalcToWindow` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectToWindowRule.java

Note: ProjectToWindowRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `CalcToWindowRule` variant (not ProjectToLogicalProjectAndWindowRule, which are separate rules ported under their own spec names).
- Attempts used: 21
- Last updated: 2026-10-14T04:20:33.492692+00:00
- Reason / notes: CalcToWindow's before pattern necessarily contains a RexOver window expression and its after pattern necessarily produces a LogicalWindow node, and QED's bag-semantic translation has no Window operator — a stated limitation, since window values depend on ordering/frames within a partition and have no bag-semantic meaning. No uninterpreted-symbol workaround captures the rule's actual content: a window function's value for a row depends on the whole partition, not the row, so row-local uninterpreted functions misrepresent the before side; modeling the entire window as an uninterpreted relation symbol reduces the rewrite to a tautological projection reshuffle (and the prover's fixed JSON/Q-expression format has no window construct for a DSL extension to target, e.g. JSONSerializer throws on LogicalWindow); and the group-by substitute emits one row per partition instead of the one row per input row this cardinality-preserving decomposition requires. ```

### `CoerceInputs` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/CoerceInputsRule.java
- Attempts used: 39
- Last updated: 2026-10-14T04:52:13.598524+00:00
- Reason / notes: QED serializes every scalar operator — including any proposed "cast" symbol — as an uninterpreted function with no identity/transparency axiom (paper §6.2; RelType.java translates all virtual types to plain integers), and CoerceInputsRule is sound precisely because coercion casts preserve values while the consumer reads the coerced column, so any encoding where the cast output is load-bearing is unprovable, while any provable encoding must leave the cast column unread, at which point the equivalence holds for *any* uninterpreted function and reduces to trivial column-independence rather than coercion. The rule's very definition also depends on backend-specific `getExpectedInputRowType`/`createCastRel` semantics QED cannot see, and since the Rust prover is the fixed arbiter, no DSL extension could supply the missing cast-identity axiom.

### `CombineSimpleEquivalence` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/CombineSimpleEquivalenceRule.java
- Attempts used: 21
- Last updated: 2026-10-14T00:26:57.970237+00:00
- Reason / notes: The rewrite introduces a new spool table and then consumes it, but QED treats table scans as independent input bags with no let/view/spool binding tying a table's contents to the subplan that materializes it. Therefore the required materialization identity is outside QED's pure bag-semantics model, not just a missing builder or symbol-sharing bug.

### `CorrelateUncollectOuter` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/CorrelateUncollectOuterRule.java
- Attempts used: 64
- Last updated: 2026-10-14T05:09:33.600605+00:00
- Reason / notes: The claim holds: the rewrite's only soundness argument is that Uncollect(isOuter=true) over the single-row LogicalValues guarantees at least one (possibly NULL-filled) right row per left row, which makes the LEFT correlate behave as an INNER one — this is Uncollect's list-expansion (UNNEST) semantics, which QED does not model, and even if the Java side were extended to emit an Uncollect node, QED could at best treat it as uninterpreted and would then correctly refute the identity on a left row with an empty list. The encoding gap is real and not closable by extend_dsl_file: RelRN exposes no Uncollect builder, JSONSerializer's exhaustive Rel switch (scan/values/filter/project/join/correlate/group/union/intersect/except/sort) has no uncollect case and throws on LogicalUncollect, and the fixed prover's JSON vocabulary cannot carry one. Nor is there a fallback encoding or provable special case: the "≥1 row per left row" precondition cannot be stated in the DSL/JSON format (there is no non-emptiness assertion, and row-level "guaranteed" constraints are vacuous on empty relations), so any Uncollect-free encoding of LEFT→INNER correlate is simply invalid and would be refuted.

### `ExchangeRemoveConstantKeys` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ExchangeRemoveConstantKeysRule.java
- Attempts used: 13
- Last updated: 2026-10-14T00:20:09.353811+00:00
- Reason / notes: The rule's entire correctness content is physical placement — hashing on a constant-valued key puts every row in one bucket, so that key (or the whole Exchange, reduced to SINGLETON) can be dropped — and QED decides equivalence purely under bag semantics, in which row placement across nodes is invisible: before and after yield the identical bag for any input, and no axiom relating hash(K) to hash(K∖constants) or to the identity exists or could be expressed in the prover's fragment. The Exchange operator is also absent from both the DSL and the serializer/prover (an uninterpreted, axiom-free symbol if added — the same barrier confirmed for the Sort precedents EliminateSorts and CapSortFetchWithLimit), and stripping the physical semantics leaves only the trivial identity rewrite, so no genuine non-trivial provable special case remains. ```

### `FilterDateRange` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/DateRangeRules.java
- Attempts used: 23
- Last updated: 2026-10-14T00:32:40.603273+00:00
- Reason / notes: FilterDateRange's soundness rests on the concrete calendar semantics of EXTRACT/FLOOR/CEIL and date literals — the claim that {t | EXTRACT(YEAR, t) = 1997} equals {t | t BETWEEN '1997-01-01' AND '1997-12-31'} holds only for that specific operator and those specific computed constants, not for arbitrary uninterpreted functions, so QED (which models all scalar operators/predicates as uninterpreted symbols and cannot reason about predicate entailment between independent symbols or a backend operator's bespoke internal semantics) must refute the faithful two-sided encoding. The transcript's SMT result (provable: false, complete_fragment: true, no timeout) is a genuine decision, not an incompleteness artifact, and the only encoding QED could accept would reuse one predicate symbol on both sides — a tautology that is not the rule. The gap is date/calendar theory in the unmodifiable Rust prover, not a missing DSL builder, so extend_dsl_file cannot close it and the UNSUPPORTED conclusion is correct. ```

### `FilterHilbert` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SpatialRules.java
- Attempts used: 22
- Last updated: 2026-10-14T00:41:18.604388+00:00
- Reason / notes: The rule's soundness is exactly the entailment "under h = HILBERT(lon, lat), ST_DWithin/ST_Contains(g, ST_Point(lon, lat), d) ⇒ (h BETWEEN c1 AND c2 OR ...)", which holds only because of Calcite's Java-side knowledge of HilbertCurve2D's index ranges and ST_Buffer geometry — none of which appear in the relational expression. In any RuleScript encoding, ST_DWithin, ST_Point, HILBERT, ST_Buffer, and the spatial predicate are all independent uninterpreted symbols to QED, so the added range conjunct is not validly implied for all instantiations (SMT will model lon/lat with the equality h = HILBERT(lon,lat) true while the spatial predicate holds and h falls outside the range), making the faithful encoding correctly unprovable. No DSL extension can close this because QED's fixed prover cannot be taught bespoke spatial/Hilbert-curve operator semantics (and the prover itself must not be modified), and every sub-case (zero-distance → equality, negative-distance → false) rests on the same internal operator semantics.

### `FilterSampleTranspose` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterSampleTransposeRule.java
- Attempts used: 9
- Last updated: 2026-10-14T00:37:28.921716+00:00
- Reason / notes: The validity of Filter(c, Sample(R)) ⟹ Sample(Filter(c, R)) rests on sampling being a per-row, probabilistic selection whose keep/drop decision is independent of the row's attributes — QED is a deterministic bag-semantics decision procedure whose own reference lists Sample (with Sort/Limit/Offset/Window) as having no bag-semantic meaning, and whose unchangeable Rust prover has no sample/probability concept. The JSON format has no Sample node and no DSL parameter (rate/seed) to carry, so no `extend_dsl_file` can close the gap — the only encodable surrogate, Sample-as-uninterpreted-row-predicate, would merely prove trivial filter commutativity (Filter(c, Filter(s, R)) ⟹ Filter(s, Filter(c, R))) for two arbitrary filters, which says nothing about what makes an operator a sampler, so the porter's UNSUPPORTED conclusion is correct. ```

### `FilterSortMeasure` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `FilterSortMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, ProjectMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
- Attempts used: 8
- Last updated: 2026-10-14T03:15:30.317439+00:00
- Reason / notes: As written, Calcite's `FilterSortMeasureRule` is dead code: `onMatch` assigns `condition = filter.getCondition()` and then guards on `condition.equals(filter.getCondition())` — a reflexive self-comparison on the same object that is always true, so it always returns; even the unreachable branch merely rebuilds the identical `Filter(cond, Sort(R))` plan, so there is no substantive transformation to verify. The rule's matching shape (Filter over Sort, in service of the order-dependent M2V-pushdown intent seen in the sibling `ProjectSortMeasureRule`) fundamentally requires row-order semantics, which QED explicitly does not model (Sort/Limit have no bag-semantic meaning) and RuleScript's core language exposes no Sort operator — dropping the Sort yields only the vacuous identity `Filter(p, R) ≡ Filter(p, R)`, which certifies nothing.

### `FilterSortTranspose` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterSortTransposeRule.java
- Attempts used: 7
- Last updated: 2026-10-14T03:10:31.634758+00:00
- Reason / notes: The rule's entire correctness content is that filtering a sorted stream preserves relative row order — a list/ordering property — and QED only decides bag-semantic equivalence, under which `Filter(p, Sort(R))` and `Sort(Filter(p, R))` both reduce to the identical bag {r in R | p(r)}, leaving zero non-trivial content to prove. Even if a Sort builder were added via `extend_dsl_file` (the JSON serializer can already carry `LogicalSort`), the prover would treat Sort as having no bag meaning and certify the tautology Filter(p,R) ≡ Filter(p,R), so the gap is a fundamental QED limitation (Sort/Limit/Offset/Window/Sample excluded from scope), not a missing DSL capability — the porter's UNSUPPORTED conclusion and reasoning are correct. ```

### `FilterTableScan` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterTableScanRule.java
- Attempts used: 6
- Last updated: 2026-10-14T03:16:20.386687+00:00
- Reason / notes: FilterTableScanRule is a physical push-down whose correctness rests entirely on the FilterableTable/BindableTableScan execution contract (a scan configured with predicate P returns exactly the rows of Filter(P, T)) — a backend operator's bespoke internal semantics that QED cannot model: under bag semantics the "after" node is definitionally identical to the before side, and QED's fixed Q-expression language has no predicate-parameterized scan operator. QED's table model is an uninterpreted bag with only PrimaryKey/References/Checks row constraints, none of which can express "table T2 is the bag-filter of T", so no encoding (even via a DSL extension, since the prover's operator set is fixed) can distinguish the pushed-scan from the plain filter; the only encodable variants degenerate into a tautology or into constraint-based filter elimination, which is a different rule, so no narrower special case carries this rule's actual content.

### `IntersectToDistinct` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/IntersectToDistinctRule.java
- Attempts used: 28
- Last updated: 2026-10-14T03:29:14.120049+00:00
- Reason / notes: Both variants of IntersectToDistinct are correct only by COUNT's cardinality algebra — `COUNT(*) FILTER (WHERE i=k) > 0` means "branch k contributed the key" and `COUNT(*) = N` in the pushdown variant means "the key appears in all N branches" — while QED models every aggregate call as an uninterpreted function over its group bag and knows nothing about COUNT beyond bag equality of its input (confirmed by the serializer emitting only operator name/operands with no kind tag, per paper eq. (5)). The after() side's post-aggregate filter on an aggregate output is therefore an uninterpreted predicate the prover cannot relate to the set-intersection before() side, and since every faithful version of this rewrite (full or partial, either variant, any N) necessarily passes through a count comparison, no encodable special case exists; the gap sits in QED's aggregate semantics rather than the DSL (which can already express intersect-set, union-all, aggregate, filter, and project, and the JSON format already carries all aggregate info), so extend_dsl_file cannot close it without touching the forbidden Rust prover. ```

### `JoinExpandOrToUnion` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinExpandOrToUnionRule.java
- Attempts used: 66
- Last updated: 2026-10-14T06:13:41.367732+00:00
- Reason / notes: The porter's encoding was verified faithful — the serialized JSON shows the identical uninterpreted symbols c1/c2 applied to the same join columns and the shared L/R scans in both queries — and the prover's rejection is pre-SMT (smt_duration:0, unify≈2µs, reproduced twice), i.e., a structural unification decision rather than an encoding or symbol-sharing bug. Any sound encoding of this rule necessarily rewrites the join predicate into an exclusive disjunction (one join summand with p∨q vs. a union of joins with p and q∧¬p), and their equivalence rests on the predicate-level Boolean identity p∨q = p + q∧¬p over independent uninterpreted symbols — exactly the class of predicate inference QED does not perform, since its SMT stage only verifies domain alignment of structurally aligned summands. No alternate faithful shape (cross-join plus filters, distinct union over unique scans) avoids that coefficient rewrite, and the non-exclusive split is unsound under bag semantics, so the UNSUPPORTED conclusion is sound. ```

### `Match` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MatchRule.java
- Attempts used: 28
- Last updated: 2026-10-14T06:37:54.537780+00:00
- Reason / notes: Calcite's `LogicalMatch` is an ordering/frame-dependent CEP operator (pattern matching over an ordered stream with measures/interval), so it has no bag-semantic meaning — exactly the unmodeled class QED lists alongside Window/Sort. The Q-expression/JSON format has no `match` construct (JSONSerializer hits its `default -> throw` on `LogicalMatch`), and since QED interprets a fixed relational-operator set with no uninterpreted-relational-operator slot, there is no way to express a MATCH node in `before()`/`after()` — and we cannot modify the Rust prover to add one. MatchRule is a pure identity placeholder (rebuilds the same LogicalMatch from the same fields), so the rule's only content is `MATCH → MATCH`, which is neither expressible nor non-trivial here; a `scan → scan`-style stand-in would mis-model the rule rather than encode it. ```

### `MaterializedViewFilterScan` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MaterializedViewFilterScanRule.java
- Attempts used: 58
- Last updated: 2026-10-14T07:29:33.136374+00:00
- Reason / notes: MaterializedViewFilterScan's soundness rests on the cross-table invariant that the MV scan's bag equals its defining query over the base table (e.g. bag(MV) = Filter(D, bag(base))), which QED's theory cannot state: every scan is an independent uninterpreted bag whose only declared properties are per-table primary keys and check predicates (the JSON format's per-table "key"/"guaranteed" fields — the full schema vocabulary JSONSerializer emits), so no relation between two distinct table symbols is expressible and `Filter(Q∧D, base) ≡ Filter(Q, MV)` is unprovable for arbitrary independent bags. The only provable single-table encoding (filter-merge: `Filter(Q∧D, base) ≡ Filter(Q, Filter(D, base))`) eliminates the MV table entirely and is therefore a different rule, not a special case of this one; the gap is a fundamental limitation of the prover's per-table theory, not a missing DSL builder, so no `extend_dsl_file` change (which is gated on the existing prover/JSON semantics) could close it. ```

### `MaterializedViewOnlyAggregate` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewOnlyAggregateRule.java
- Attempts used: 28
- Last updated: 2026-10-14T07:09:06.870671+00:00
- Reason / notes: The rule's sole correctness premise is the registration-time invariant that a separately registered, externally refreshed MV table's contents equal its defining aggregate — a cross-table fact about a planner-registered artifact, not a relational-algebra identity QED can derive; QED treats the MV scan as an independent uninterpreted table, and the "guaranteed" constraints in the JSON format only carry single-table predicates, so no non-vacuous before/after pair (MV modeled as a distinct scan fails; modeling it as the same symbol degenerates the rule to the identity rewrite) can be proved. Its rollup/compensation paths independently require re-aggregation algebra (count→sum0, SUM/COUNT distribution through a finer-grained grouping) that QED explicitly does not model beyond bag equality of aggregate inputs, and the intermediate group-by changes that input bag — a fundamental limitation, not a DSL gap an extend_dsl_file attempt could meaningfully close. ```

### `MaterializedViewOnlyFilter` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewOnlyFilterRule.java
- Attempts used: 9
- Last updated: 2026-10-14T08:14:32.330254+00:00
- Reason / notes: This is not an algebraic equivalence between two plans but a catalog-driven rewrite whose soundness rests on the external planner invariant that the registered MV table is a current materialization of its defining query (bag(MV) = bag(defineQuery(base tables))), and that invariant cannot be stated as a premise in QED's theory. Table scans are independent uninterpreted bags whose only declared properties are per-table primary keys and single-table check predicates (`key`/`guaranteed` in the JSON), and neither the RuleScript DSL nor the prover's JSON schema has any construct for a table-to-plan or cross-table identity — closing that gap would require changing the trusted Rust prover, which is off-limits, not a Java-side `extend_dsl_file` fix. So any encoding that keeps the MV as a distinct scan symbol is unprovable, while identifying the MV scan with the defining-subplan symbol degenerates the rule to a vacuous filter rearrangement; the porter's UNSUPPORTED conclusion and its stated reason are correct.

### `MaterializedViewOnlyJoin` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewOnlyJoinRule.java
- Attempts used: 29
- Last updated: 2026-10-14T07:24:13.593506+00:00
- Reason / notes: The rewrite substitutes a query join subplan with a scan of a separately registered MV table (cross-joined with query-only tables), and its entire correctness rests on the external planner invariant that the MV table's contents equal its defining query — a cross-table identity between two independent uninterpreted relations. QED's table schema format only carries per-table keys and single-table "guaranteed" constraints (serialized in a Rex environment over that table's own row type), so the invariant cannot even be stated as a premise, and the SMT decision procedure over independent uninterpreted relations cannot derive it; identifying the MV scan with the subplan symbol degenerates the rule to the vacuous identity, so no non-trivial provable encoding exists. ```

### `MaterializedViewProjectAggregate` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewProjectAggregateRule.java
- Attempts used: 21
- Last updated: 2026-10-14T07:31:54.849913+00:00
- Reason / notes: The rule replaces Project(Aggregate(Scan T)) with a scan of the MV's physical table, and its soundness rests on an external database invariant — the registered MV table is maintained to equal the result of its defining aggregate — rather than on relational semantics. Under QED's model the MV table and the source table are independent uninterpreted relations, so universal quantification over arbitrary instantiations actively refutes Scan(MV) = Aggregate(Scan(T)); no encoding can bridge this (identifying the two tables would make the rule a vacuous identity, and table "guaranteed" constraints are per-row predicates, not inter-table equalities), and the rollup path independently requires re-aggregation algebra across granularities (e.g., sum-of-sums over a coarser grouping) that QED explicitly does not model. ```

### `MaterializedViewProjectFilter` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewProjectFilterRule.java
- Attempts used: 23
- Last updated: 2026-10-14T07:41:51.928273+00:00
- Reason / notes: The rule's before pattern (Project over Filter over base tables) and after pattern (projection/filter over the MV scan) are individually expressible, but the rewrite's soundness rests solely on the cross-table invariant bag(MV) = Project(Filter(..., base)) — that the maintained MV table's bag equals its defining query over the base tables. QED assigns every scan an independent uninterpreted bag, and the full schema vocabulary the serializer can emit (per-table "key" sets and "guaranteed" check predicates, each scoped to its own table's columns via a Rex env with that table's field-count delta) has no mechanism to relate two distinct table symbols or assert bag equality between them, so no DSL-level extension can supply the missing premise without modifying the trusted prover itself. The only encoding that would prove something would collapse the MV and base into a single shared scan symbol, which eliminates the rule's premise entirely and degenerates into an ordinary filter/predicate identity (a different rule), confirming UNSUPPORTED. ```

### `MaterializedViewProjectJoin` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewProjectJoinRule.java
- Attempts used: 14
- Last updated: 2026-10-14T07:39:51.724376+00:00
- Reason / notes: The rule's soundness rests entirely on the planner-maintained invariant that the MV's physical table holds exactly the bag produced by its defining query — a cross-table identity between two independent uninterpreted relations. QED's schema language only carries per-table premises (keys and single-table "guaranteed" check predicates, which are per-tuple first-order properties that cannot even express multiset equality between two tables), and its SMT procedure treats every table as an independent uninterpreted bag, so it can neither assume nor derive that scan(MV) ≡ the defining Project(Join) over base tables. The only encoding that would make the prover succeed is one that inlines the defining query in place of the MV scan, which degenerates the rule into a vacuous identity and no longer expresses materialization; hence no non-vacuous encoding exists (matching the correctly-verified sibling rules). ```

### `MinusToDistinct` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MinusToDistinctRule.java
- Attempts used: 10
- Last updated: 2026-10-14T07:53:26.548802+00:00
- Reason / notes: The rewrite's correctness rests entirely on reading membership off aggregate outputs — `COUNT() FILTER tag=i` > 0 / == 0 testing whether a grouped row came from each MINUS input — but QED models aggregate calls as uninterpreted functions of their input bag (special-casing only the COUNT(*)-vs-COUNT(col) non-NULL prefilter), so it can never connect those comparisons to set-difference membership. This is a fundamental prover limitation, not a missing builder: even adding a FILTER field to `RelRN.AggCall` (or re-encoding it as COUNT(CASE...)) would still leave the count values opaque, and no 2-branch special case can avoid the existence test, so no narrower provable encoding of this specific rule exists. ```

### `MinusToFilter` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MinusToFilterRule.java
- Attempts used: 45
- Last updated: 2026-10-14T08:25:37.144556+00:00
- Reason / notes: I independently verified the rewrite is mathematically sound, but any faithful encoding forces QED to relate its EXCEPT (set-minus) primitive to a group-all/distinct aggregate over a filtered scan, and both sides reduce to zero/positivity tests on semiring products of the form S·[predicate] (S·P_R = 0 on the before side, S·P_L·¬P_R > 0 from the distinct on the after side) — a zero-of-product / aggregate-algebra step beyond bag-equality that QED's semiring decision procedure cannot perform, and which no DSL extension or re-encoding can close since the prover itself is off-limits and the DSL already exposes every operator the rule needs. The porter's four fast, non-timed-out "not provable" results across the full encodable 2×2 space (plain vs filtered left × unique vs non-unique base) are consistent with this: even the unique-key variant still requires 0/1-multiplication facts under a support test, and the only distinct alternative (union(false, F, F)) serializes into QED's same "distinct" wrapper. ```

### `ProjectJoinJoinRemove` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectJoinJoinRemoveRule.java
- Attempts used: 105
- Last updated: 2026-10-14T09:26:22.814981+00:00
- Reason / notes: The rewrite's row-level validity hinges on B's key being unique AND the top join condition referencing only the shared A-key column plus C (never B); proving that as a *bag* identity over a two-level nullable LEFT-join stack with a column-skipping Project sits outside QED's complete bag-semantic fragment (complete_fragment: false, rejected structurally in ~1–2 µs before SMT across four encodings that varied equi-vs-uninterpreted condition, B unique/non-unique, and projection shape), while the structurally identical stack under an Aggregate proves — so the wall is QED's own incomplete fragment for a bag Project atop a multi-level LEFT-join stack, not a DSL-expressibility gap or an uncorrected symbol mismatch (a name mismatch would instead yield an authoritative complete_fragment:true "not provable").

### `ProjectMeasure` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
- Attempts used: 41
- Last updated: 2026-10-14T08:46:29.781104+00:00
- Reason / notes: ProjectMeasureRule's rewrite (Aggregate(g, SINGLE_VALUE(d)) over a Project with d = M2X(V2M(e)) AS d) is only sound because of the measure-algebra identity M2X(V2M(x, SAME_PARTITION(g))) = x plus partition-constancy of measures, but in QED's model M2X/V2M/SAME_PARTITION are uninterpreted scalar symbols and SINGLE_VALUE is an uninterpreted aggregate known only up to bag equality of its input, so no RuleScript encoding or DSL builder extension (the format has no facility for function-level axioms) can make the SMT prover accept the equivalence for all instantiations.

### `ProjectMultiJoinMerge` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectMultiJoinMergeRule.java
- Attempts used: 23
- Last updated: 2026-10-14T08:43:38.385000+00:00
- Reason / notes: Calcite's `ProjectMultiJoinMerge` leaves the MultiJoin's inputs, join conditions, row type, and even the top-level project (reused verbatim in `onMatch`, which is only valid because the multi-join output is unchanged) all identical — it merely fills in the per-input `projFields` bitmaps that downstream physical rules consult for column pruning, so the transformation is bag-identical by construction. QED's decision procedure quantifies only over the bag semantics of relational expressions and has no concept of such non-semantic optimizer metadata; the DSL has no `MultiJoin`/`projFields` construct, and `extend_dsl_file` cannot close this gap because the unmodifiable prover's Q-expression language has no field for an annotation with no semantic effect, so any faithful encoding collapses to a vacuous before≡after identity. The only alternative — interpreting `projFields` as real per-input projections — would be a different (pushdown) rule whose validity rests on a "J' is J restricted to a subset of columns" correspondence between uninterpreted predicates and field indices that QED fundamentally cannot express or infer.

### `ProjectOverSumToSum0` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectOverSumToSum0Rule.java
- Attempts used: 23
- Last updated: 2026-10-14T09:04:32.594342+00:00
- Reason / notes: The rewrite operates entirely on window aggregates (`SUM(x) OVER (frame)` → `SUM0(x) OVER (frame)`), and QED has no bag-semantic model for `Window` (listed with Sort/Limit/Offset/Sample), with no window node in its JSON format per `JSONSerializer.java` — a genuine prover-level gap no `extend_dsl_file` change can close, since the Rust prover is the fixed arbiter. Even abstracting away the OVER, `SUM` and `SUM0` would be two distinct uninterpreted aggregate symbols QED cannot algebraically relate, and the standalone rewrite is not universally valid in the first place: on an empty/all-NULL frame SUM yields NULL while SUM0 yields 0, a difference Calcite only reconciles through its SUM0-lowering (CASE/COALESCE) chain outside this rule's scope — so no honest encoding could be provable. ```

### `ProjectSortMeasure` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectSortMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, ProjectMeasureRule, FilterSortMeasureRule, which are separate rules ported under their own spec names).
- Attempts used: 34
- Last updated: 2026-10-14T09:34:24.957692+00:00
- Reason / notes: The rule's only non-vacuous content is that re-emitting the Sort (identical sortExps/offset/fetch) below the inserted M2V projection preserves sort order and top-k/offset selection, plus that M2V's per-row value is order-independent — QED fundamentally has no ordering semantics (Sort/Limit/Offset have no bag meaning; the prover path treats LogicalSort as identity, so even a DSL Sort builder couldn't express the order claim), and M2V's measure-expansion row-wise behavior is bespoke operator semantics invisible behind an uninterpreted symbol. Any faithful encoding therefore must drop the Sort and reduces to the generic let-binding identity Project[x0, f(g(x2))] ≡ Project[x0, f(z)] over Project[x0, x1, g(x2)], which holds for arbitrary deterministic f, g and verifies nothing specific to this rule — a genuine QED limitation, not an encoding mistake the porter could fix. ```

### `ProjectToLogicalProjectAndWindow` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectToWindowRule.java

Note: ProjectToWindowRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectToLogicalProjectAndWindowRule` variant (not CalcToWindowRule, which are separate rules ported under their own spec names).
- Attempts used: 10
- Last updated: 2026-10-14T09:16:06.262192+00:00
- Reason / notes: This rule's payload is the identity between an inline `RexOver` (windowed aggregate) inside a `Project` and a separate `LogicalWindow` operator, but QED's Q-expression language contains no window operator (the serializer emits only scan/filter/project/join/correlate/group/union/intersect/except/sort), and window semantics require ordered-partition/frame (list) semantics plus the aggregate identity "per-partition aggregate joined back to each row equals the inline OVER value" — both things QED's bag semantics explicitly excludes, per its own limitations. Any encoding is forced to choose between (a) row-local uninterpreted functions, which makes the before-side window value an independent symbol the SMT solver cannot relate to the after-side group/join-back construction (a countermodel exists, as the porter observed), or (b) modeling both sides with the same group-by-then-join-back, which degenerates the rule into a vacuous identity that proves nothing about windows; even a DSL extension (e.g. scans with guaranteed constraints) cannot restore totality/cardinality for a window-relation symbol. Hence the porter's UNSUPPORTED verdict is correct, though its "group-by+join-back breaks cardinality" sub-point is imprecise (inner join-back on the group key is actually row-preserving) — the true blocker is the missing window/order semantics, which is a fundamental QED limitation, not a missed encoding.

### `ReduceDecimals` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceDecimalsRule.java
- Attempts used: 22
- Last updated: 2026-10-14T09:33:55.228310+00:00
- Reason / notes: Every sub-rewrite in this rule — including the simplest one, `REINTERPRET(REINTERPRET(x)) → x` — rests on a value-level identity between decimal arithmetic and its unscaled-BIGINT expansion (10^scale scaling, ROUND_HALF_UP via CASE/DIVIDE_INTEGER, precision/overflow bounds, and the physical REINTERPRET type reinterpretation), while QED only supplies bag algebra over uninterpreted scalar symbols with every virtual type lowered to a plain integer and no arithmetic semantics on any operator. Consequently no faithful before/after pair is decidable: an honest encoding is either two independent uninterpreted terms (unprovable) or the identical term on both sides (degenerate, not a rule), and `extend_dsl_file` cannot close the gap because the unmodifiable prover would still treat any new CAST/REINTERPRET/arithmetic builders as uninterpreted black boxes, and encoding REINTERPRET as a bare type identity would silently alter the semantics rather than capture the rule. ```

### `SampleToFilter` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SampleToFilterRule.java
- Attempts used: 6
- Last updated: 2026-10-14T09:37:22.283508+00:00
- Reason / notes: Bernoulli `Sample`'s output is a random per-row selection, not a deterministic function of its input, so it has no bag semantics in QED's SMT model, and the rule's justification rests on statistical properties of `rand` (uniform in [0,1), independent across rows, true with probability exactly the sample rate) that a deterministic bag-equivalence prover neither models nor can verify. The only encodable surrogate — the sampler as an uninterpreted row predicate vs. the `rand(seed) < rate` filter as another independent uninterpreted predicate — is not universally equivalent, so QED would rightly refuse it, while sharing one predicate symbol would only prove the vacuous identity `Filter(P, R) ≡ Filter(P, R)`; even degenerate rate special cases are unprovable since QED cannot know the range or randomness of an uninterpreted `rand`, and the DSL/JSON layer exposes no Sample node that an extension could give meaningful (non-vacuous) semantics to.

### `SortJoinCopy` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortJoinCopyRule.java
- Attempts used: 8
- Last updated: 2026-10-14T09:42:53.761677+00:00
- Reason / notes: SortJoinCopyRule's entire contract is row-ordering: it decomposes the top Sort's collation across the join's two sides, consults "already sorted" metadata, and is only safe because the outer Sort is preserved to re-establish global order. QED models no ordering at all — Sort is a bag identity — so both sides erase to the identical Join bag and the only provable statement is the tautology Join ≡ Join; tellingly, QED would "prove" an unsound variant that drops the outer sort just as readily, so its verdict carries no information about this rule's real correctness. This is exactly the reference's explicit fundamental limitation (correctness fundamentally rests on Sort/row-ordering semantics), not a symbol-sharing bug or a mis-modeling a genuine attempt could fix. ```

### `SortJoinTranspose` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortJoinTransposeRule.java
- Attempts used: 23
- Last updated: 2026-10-14T09:46:12.295272+00:00
- Reason / notes: SortJoinTranspose is meaningful only through ordering semantics: the outer Sort/Offset/Fetch must be preserved while a derived Fetch/Limit is pushed into one join input. QED explicitly models queries with bag semantics and has no Sort/Limit/Offset/ordering semantics, so any encoding that removes those operators degenerates to the vacuous identity of the same join.

### `SortMerge` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortMergeRule.java
- Attempts used: 9
- Last updated: 2026-10-14T09:53:40.828071+00:00
- Reason / notes: SortMerge's soundness is purely a top-N/row-position statement — it must know which rows each LIMIT keeps and, even in the degenerate sub-case where the outer fetch ≥ inner fetch (top sort is a no-op), that the inner `Limit B` node produces at most B rows — and QED's theory is bag-semantics only, explicitly without any model for the ordering semantics of Sort/Limit/Offset (qed.pdf §6.2), so the SMT solver has no algebraic relation to bridge. The gap is in the prover's semantics rather than a missing DSL builder (RelRN exposes no sort/limit and the Rust prover, the unchanging arbiter, has no ordering theory), so `extend_dsl_file` cannot close it; a "proof" obtained by erasing sorts to identity would only show identity = identity and would verify nothing about which rows survive, and no genuinely non-trivial special case of the min-of-fetches law exists within bag semantics.

### `SortProjectTranspose` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortProjectTransposeRule.java
- Attempts used: 9
- Last updated: 2026-10-14T09:50:52.978317+00:00
- Reason / notes: The rule's soundness is purely an order-preservation claim: a permutation (plus monotonicity-preserving casts) over a stream sorted by collation C yields the same row ordering — and, with offset/fetch, the same top-k row *selection* — as sorting the input by the remapped collation; the permutation guard and the monotonic-cast check exist only to protect this list-semantic invariant. QED verifies only bag equivalence, where Sort/Offset/Fetch carry no semantic content (sort reduces to identity), so any encoding of before/after collapses to the vacuous tautology Project(perm, R) ≡ Project(perm, R) — a statement that holds even for non-permutation projects and thus cannot certify the collation remapping the rule actually depends on. This is the documented fundamental limitation on row-ordering/top-k reasoning, not a missing DSL operator, so `extend_dsl_file` could not close the gap and the UNSUPPORTED verdict stands.

### `SortRemove` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveRule.java
- Attempts used: 9
- Last updated: 2026-10-14T09:55:13.183318+00:00
- Reason / notes: SortRemoveRule is sound only under the non-bag premise that the child already emits rows in the Sort's collation (a planner trait fact), and QED decides only bag equivalence with no ordering semantics for Sort/Order By (qed.pdf §6.2), so the premise can neither be stated nor the redundancy checked. RuleScript's RRule has no guard/side-condition mechanism on uninterpreted children, and no bag property (e.g. key uniqueness on a scan) implies "already sorted", so no bag-semantic special case exists. Extending the DSL cannot close the gap because the missing ordering interpretation lives in the (unmodifiable) prover, not in the Java builders. ```

### `SortRemoveConstantKeys` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveConstantKeysRule.java
- Attempts used: 6
- Last updated: 2026-10-14T09:57:23.026523+00:00
- Reason / notes: The rule is sound only because dropping a sort key that the input's predicates prove constant preserves the output row *ordering* — and QED decides only bag equivalence, under which `Sort`/`Limit`/`Offset` carry no semantics at all, so both branches (partial key-dropping and full removal) reduce to bag tautologies like `R ≡ R` for any key set (e.g. `Sort([f1,f2], Filter(f1=c, R))` and `Sort([f2], Filter(f1=c, R))` are equal as bags even when f1 is *not* constant). `RelRN` exposes no order-carrying operator, and even if a `Sort` builder were added via `extend_dsl_file` (the serializer can carry collations, but the SMT encoding has no ordering model), the prover could only check the vacuous bag identity, not the order-preservation argument that constitutes the rule's entire correctness content. ```

### `SortRemoveDuplicateKeys` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveDuplicateKeysRule.java
- Attempts used: 10
- Last updated: 2026-10-14T10:07:57.509072+00:00
- Reason / notes: Both plans sort the same input, so their row bags are identical by construction; the rule's only non-trivial claim is that dropping a key `mq.determines` marks as functionally determined by earlier keys preserves the same row *order*, and QED has no ordering semantics for Sort (it reduces to its input bag-wise), so any encoding proves only a vacuous tautology. The `mq.determines` precondition is a content-dependent property the prover cannot even constrain, since proofs must hold for all instantiations of the uninterpreted table symbols. This is a fundamental QED limitation, not a DSL gap: the JSON serializer already carries `sort`, so a RelRN sort builder (via extend_dsl_file) would only enable vacuous proofs rather than close the missing ordering model, matching the verified AGREE skips for calcite SortRemove and datafusion DedupeSortExprs. ```

### `SortRemoveRedundant` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveRedundantRule.java
- Attempts used: 22
- Last updated: 2026-10-14T10:06:16.526771+00:00
- Reason / notes: The rewrite is conditional on a cost-model fact — the input's max row count being ≤ 1 (ORDER BY) or ≤ fetch (pure LIMIT) — and RuleScript patterns are unconditional before/after pairs with no hypothesis mechanism and no cardinality-bounded input (the `unique` flag encodes key/functional dependency, not a row bound), so any encoding that drops the precondition is simply unsound in general. Even the one structurally encodable instance, a Sort/Limit over the `empty` relation, is unprovable because QED assigns Sort/Limit/Offset no ordering or bag semantics at all — they are uninterpreted QOps whose outputs become unconstrained during normalization, making Sort(R) ≡ R reduce to "fresh unconstrained variable ≡ R". Adding a Sort builder via `extend_dsl_file` would not help, since the JSON format already carries sorts while the prover itself models them uninterpreted — the gap is a fundamental QED limitation, exactly the one the reference lists (Sort/Limit/Offset/Window/Sample have no bag-semantic meaning). ```

### `SortUnionTranspose` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortUnionTransposeRule.java
- Attempts used: 5
- Last updated: 2026-10-14T09:59:48.145088+00:00
- Reason / notes: The rule’s correctness depends on row-order/top-N semantics: each union branch is capped at `offset + fetch` only because the outer `Sort` reorders and re-selects after merging. QED’s model is bag-semantic and has no meaningful `Sort`/`Limit`/`Offset` semantics, so this equivalence cannot be universally proven even if a Sort builder were added to the DSL.

### `TableScan` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/TableScanRule.java
- Attempts used: 24
- Last updated: 2026-10-14T10:21:44.398788+00:00
- Reason / notes: TableScanRule's entire correctness content is the identity "the bag of table V equals the relation produced by V's RelOptTable.toRel" (its view definition over *other* table symbols), and QED's semantics make every named table an independent uninterpreted bag whose only admissible side conditions are per-table key and check predicates (the JSON schema carries only "key"/"guaranteed" per table, with guaranteed predicates scoped to that table's own row) — there is no operator, constraint kind, or JSON construct in the DSL to assert a table-to-expression identity, so any encoding is either the vacuous base-table tautology Scan(T) = Scan(T) (proving nothing) or unprovable independent-symbol inequality. This is a fundamental limitation of the trusted prover's table model, not a missing Java-builder gap closable via extend_dsl_file, and even the degenerate alias-view special case (V = scan of B) still requires the same inexpressible cross-symbol bag equality.

