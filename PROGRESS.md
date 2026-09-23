# RuleScript porting progress

_Last updated: 2026-09-23T16:43:29.496475+00:00_

**72/151 rules proved** (7 failed, 72 skipped as out of QED's supported fragment).

| Rule | Backend | Status | Scope | Attempts | Notes |
|---|---|---|---|---|---|
| `AggregateExpandDistinctAggregates` | Apache Calcite | ✅ PROVED | PARTIAL | 72 | The encoding faithfully mirrors the source rule's `convertMonopole` branch: `before()` is `GROUP BY k` of two distinct calls `f(DISTINCT ... |
| `AggregateExtractProject` | Apache Calcite | ✅ PROVED | PARTIAL | 14 | The encoding faithfully mirrors the source rule's actual computation: it computes the used columns (group col 0, agg arg col 1), projects... |
| `AggregateFilterTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 9 | The encoding faithfully captures Case 1 of the source rule: the uninterpreted predicate depends only on the group key (i.e. all filter co... |
| `AggregateJoinRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 67 | The encoding correctly captures the core semantic content of AggregateJoinRemove's LEFT-join branch: a DISTINCT aggregate (group key on t... |
| `AggregateJoinTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 126 | Hand-applied by harness operator: added additive RelRN.scanMany/ScanMany multi-column scan, encoded the DEFAULT-config (no-agg-function) ... |
| `AggregateMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 37 | The encoding is a genuine, non-vacuous instance of the source rule: when the top aggregate has no aggregate calls, Calcite's onMatch skip... |
| `AggregateProjectConstantToDummyJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 62 | The encoding mirrors the source rule's onMatch exactly — INNER join of the input with a one-row values table on constant true, a project ... |
| `AggregateProjectMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 68 | The encoding is a line-faithful instance of the source rule's `apply`: for the swap project `[x1, x0]` the interesting-field map is `{0→1... |
| `AggregateProjectPullUpConstants` | Apache Calcite | ✅ PROVED | PARTIAL | 63 | The encoding faithfully captures the rule's core transformation: an aggregate whose leading group key is a constant column is rewritten t... |
| `AggregateRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 34 | `before()` (a SIMPLE group-by on column 0 with zero agg calls, i.e. SELECT DISTINCT) is structurally and semantically distinct from `afte... |
| `AggregateUnionAggregate` | Apache Calcite | ✅ PROVED | PARTIAL | 8 | The encoding is faithful and non-vacuous: `before()` truly differs from `after()` (an extra group-by-all, no-agg-call dedup beneath the s... |
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
| `FilterMerge` | Apache Calcite | ✅ PROVED | FULL | 4 | before() is a genuinely nested Filter(Filter(scan)) while after() is a single Filter over an uninterpreted conjunction, so the proof is o... |
| `FilterProjectTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 42 | The encoding faithfully captures the core mechanism of FilterProjectTranspose — pushing a predicate below a project by substituting the p... |
| `FilterReduceExpressions` | Apache Calcite | ✅ PROVED | PARTIAL | 9 | The encoding precisely mirrors the source rule's false-literal branch of `onMatch` — `newConditionExp instanceof RexLiteral` (false) → `c... |
| `FilterRemoveIsNotDistinctFrom` | Apache Calcite | ✅ PROVED | PARTIAL | 37 | The encoding is non-trivial and operator-correct: before() is Filter(IS_NOT_DISTINCT_FROM(x,y)) and after() is the DNF `(x IS NULL AND y ... |
| `FilterSetOpTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 6 | before() = Filter(P, UnionAll(L, R)) and after() = UnionAll(Filter(P, L), Filter(P, R)) are structurally distinct and exactly the pushdow... |
| `FilterTableFunctionTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 35 | The encoding faithfully captures the rule: under the source's side conditions (single input, identity 1-to-1 non-derived mapping), the ta... |
| `FilterToCalc` | Apache Calcite | ✅ PROVED | FULL | 9 | The encoding captures exactly what FilterToCalcRule rewrites: `before()` is the bare `Filter(cond, R)`, and `after()` is the Calc the rul... |
| `FilterWindowTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 15 | The encoding is a faithful, honest special case: since QED cannot model Window at all (no bag semantics, no JSON/prover support), modelin... |
| `FullToLeftAndRightJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 11 | The encoding mirrors the source rule's exact shape — before() is the FULL join, after() is (LEFT join) UNION ALL ((RIGHT join) filtered b... |
| `IntersectReorder` | Apache Calcite | ✅ PROVED | PARTIAL | 8 | before (A∩B∩C) and after (C∩A∩B) differ by a genuine 3-cycle permutation of three distinct uninterpreted inputs sharing one type, so the ... |
| `IntersectToSemiJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 8 | The pattern is non-vacuous and matches the source rule's binary step: set INTERSECT over independent uninterpreted A and B is rewritten t... |
| `JoinAddRedundantSemiJoin` | Apache Calcite | ✅ PROVED | FULL | 12 | The encoding exactly mirrors the source rule's transformation: before = X INNER⋈_C Y, after = (X SEMI⋈_C Y) INNER⋈_C Y, with the same uni... |
| `JoinAggregateTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 36 | The encoding mirrors the source rule's exact plan shape — Aggregate(below an INNER join) ⟹ INNER join under an Aggregate whose group set ... |
| `JoinAssociate` | Apache Calcite | ✅ PROVED | PARTIAL | 34 | The encoding correctly re-associates ((A⋈B)⋈C)→(A⋈(B⋈C)) with INNER joins, faithfully splitting conditions by A-reference (PAB/PABC on to... |
| `JoinCommute` | Apache Calcite | ✅ PROVED | PARTIAL | 39 | before() is Join(L, R, INNER, P(l, r)) and after() is Join(R, L, INNER, P(l, r)) with the references correctly remapped to positions (1, ... |
| `JoinConditionPush` | Apache Calcite | ✅ PROVED | PARTIAL | 66 | The proof is not vacuous and the encoding is sound: before() and after() differ structurally, and reusing the same uninterpreted predicat... |
| `JoinDeriveIsNotNullFilter` | Apache Calcite | ✅ PROVED | PARTIAL | 10 | The rewrite is nontrivial and matches the source's inner-join transformation by adding IS NOT NULL filters implied by the null-rejecting ... |
| `JoinExtractFilter` | Apache Calcite | ✅ PROVED | FULL | 8 | <1-3 sentences ...> ``` No code fences? It shows with code block? It says Reply with exactly this format (nothing else): ``` ... ``` Prob... |
| `JoinOnUniqueToSemiJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 81 | The encoding mirrors the source rule's INNER case exactly — Project(Left)∘(L ⋈_{l.c=r.c} R) ⟹ Project(Left)∘(L ⋉_{l.c=r.c} R) — with befo... |
| `JoinProjectTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 10 | The before/after plans are structurally different (Project below the Join in before, Project above it in after), with correct symbol shar... |
| `JoinPushExpressions` | Apache Calcite | ✅ PROVED | PARTIAL | 37 | before() and after() are genuinely structurally different and differ in exactly the way the real rule rewrites — a bare inner join versus... |
| `JoinPushThroughJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 7 | The encoding faithfully captures the RIGHT instance of JoinPushThroughJoin: before is (A ⋈_{SA∧SB} B) ⋈_{ST∧TC} C with layout (A,B,C), an... |
| `JoinReduceExpressions` | Apache Calcite | ✅ PROVED | PARTIAL | 32 | The encoding faithfully mirrors JoinReduceExpressionsRule's actual transformation — the rule rewrites via `join.copy(traitSet, reducedCon... |
| `JoinUnionTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 33 | The encoding is a faithful, non-vacuous instance of the rule: before() = (X ∪ALL Y) ⋈_C O vs after() = (X ⋈_C O) ∪ALL (Y ⋈_C O) is a genu... |
| `MinusToAntiJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 5 | The encoding reproduces the rule's 2-way transformation exactly — set-minus (all=false) rewritten to an ANTI join on IS NOT DISTINCT FROM... |
| `MultiJoinOptimizeBushy` | Apache Calcite | ✅ PROVED | PARTIAL | 33 | The encoding is non-vacuous and symbolically sound: before() is the left-deep chain (((A⋈B)⋈C)⋈F D) and after() is the genuinely bushy (A... |
| `ProjectAggregateMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 36 | The encoding is non-vacuous and correctly structured: before() computes an unused third aggregate per group and projects around it (field... |
| `ProjectCalcMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 9 | The encoding faithfully desugars the Project-over-Calc match (a Calc as filter(B)∘project(E0,E1), exact because the fixed shape has no lo... |
| `ProjectCorrelateTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 62 | The encoding is a genuine, non-vacuous special case: before() projects above a 4-column cross product while after() prunes each input to ... |
| `ProjectFilterTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 39 | The encoding is a faithful fixed-shape instantiation of the rule's whole-expressions mode: before() = Filter(P(E(x,y), y), S) → Project(E... |
| `ProjectJoinTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 10 | The encoding is a faithful, non-vacuous special case: `before()` (inner join of raw scans with condition C(TL(l),TR(r)) and top projectio... |
| `ProjectMerge` | Apache Calcite | ✅ PROVED | FULL | 6 | `before()` (a Project stacked on a Project) and `after()` (a single Project) are structurally distinct, so the proof is non-vacuous and m... |
| `ProjectRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 41 | The encoding faithfully and non-vacuously captures the rule's core transformation: `before()` is a Project whose sole expression is `sour... |
| `ProjectSetOpTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 68 | The encoding faithfully captures the core semantics of ProjectSetOpTranspose: a top-level uninterpreted projection F is pushed below a UN... |
| `ProjectTableScan` | Apache Calcite | ✅ PROVED | PARTIAL | 6 | `before()` (Project over Scan) and `after()` (Project over Project over Scan) are structurally distinct, and the proved equivalence is ex... |
| `ProjectToCalc` | Apache Calcite | ✅ PROVED | PARTIAL | 8 | The encoding faithfully maps the rule's two node shapes — `before()` is the bare `Project([E1,E2], S)` (the `LogicalProject`), and `after... |
| `PruneEmpty` | Apache Calcite | ✅ PROVED | PARTIAL | 61 | The encoding is a faithful, non-vacuous rendering of Calcite's `RemoveEmptySingleRuleConfig.FILTER` instance (a Filter over an empty `Val... |
| `PruneSingleValue` | Apache Calcite | ✅ PROVED | PARTIAL | 62 | The encoding is non-vacuous and matches the source rule's core inner-join transformation exactly: `O ⋈_{jcond} Values{true}` is rewritten... |
| `RemoveEmptySingle` | Apache Calcite | ✅ PROVED | PARTIAL | 10 | The encoding faithfully captures the AGGREGATE variant of RemoveEmptySingleRule — a non-grand-total aggregate (group set non-empty, match... |
| `SemiJoinFilterTranspose` | Apache Calcite | ✅ PROVED | FULL | 16 | The encoding faithfully captures the original rule: before() is SemiJoin(Filter(X, F), Y, C) and after() is Filter(SemiJoin(X, Y, C), F),... |
| `SemiJoinJoinTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 66 | The encoding is faithful: before() = (X ⋈_C Y) ⋉_S Z and after() = (X ⋉_S Z) ⋈_C Y are structurally different plans whose bag-equality is... |
| `SemiJoinProjectTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 80 | The encoding faithfully mirrors the rule's semi-join branch: before() = SemiJoin(Project([P0,P1], X), Y, C) and after() = Project([P0,P1]... |
| `SetOpToFilter` | Apache Calcite | ✅ PROVED | PARTIAL | 11 | The encoding exactly captures Calcite's rewrite for the single-source, two-filter case — `Union(DISTINCT, [σ_P1(S), σ_P2(S)])` → all-fiel... |
| `UnionEliminator` | Apache Calcite | ✅ PROVED | FULL | 10 | The encoding exactly mirrors the rule's matches condition for the union variant — a set op with all=true over exactly one input (input.un... |
| `UnionMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 5 | The encoding faithfully reproduces Calcite's UnionMerge onMatch for the UNION ALL instance — top union with a nested union in the second ... |
| `UnionPullUpConstants` | Apache Calcite | ✅ PROVED | PARTIAL | 34 | The proof is of a real, nontrivial declared special case: a shared boolean-constant projection duplicated under a union-all is hoisted ab... |
| `UnionToDistinct` | Apache Calcite | ✅ PROVED | PARTIAL | 7 | The encoding matches the rule's exact shape: `before()` is a UNION DISTINCT (`union(false)`) over two independent uninterpreted inputs, a... |
| `UnnestDecorrelate` | Apache Calcite | ✅ PROVED | PARTIAL | 40 | The encoding faithfully captures the rule's semantic claim: the INNER correlate over a single-row Values feeding a correlated uncollect i... |
| `ValuesReduce` | Apache Calcite | ✅ PROVED | PARTIAL | 42 | The encoding is a genuine, non-vacuous trace of the rule's PROJECT config — Project([c,a]) over the 2-row Values (1,2,3),(4,5,6) folds to... |
| `JoinToCorrelate` | Apache Calcite | ❌ FAILED | — | 0 | agent error: Command '['grep', '-rn', '--include=*.java', '-E', 'Correlate', '/Users/wkaiz/Desktop/rule-porting-agent/.cache/workspaces/J... |
| `JoinToSemiJoin` | Apache Calcite | ❌ FAILED | — | 0 | agent error: timed out |
| `LoptOptimizeJoin` | Apache Calcite | ❌ FAILED | — | 0 | LLM error: HTTP 400 from http://169.229.48.114:8000/v1/chat/completions: {"error":{"message":"This model's maximum context length is 3276... |
| `MultiJoinProjectTranspose` | Apache Calcite | ❌ FAILED | — | 0 | agent error: timed out |
| `OuterJoinToAntiJoin` | Apache Calcite | ❌ FAILED | — | 150 | LLM error: HTTP 400 from http://169.229.48.114:8000/v1/chat/completions: {"error":{"message":"This model's maximum context length is 3276... |
| `ProjectSortMeasure` | Apache Calcite | ❌ FAILED | — | 0 | agent error: timed out |
| `SubQueryRemove` | Apache Calcite | ❌ FAILED | — | 0 | LLM error: HTTP 400 from http://169.229.48.114:8000/v1/chat/completions: {"error":{"message":"This model's maximum context length is 3276... |
| `AggregateCaseToFilter` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's correctness rests on aggregate-function algebra that QED explicitly does not model — that null-skipping aggregates (COUNT/SUM)... |
| `AggregateExpandWithinDistinct` | Apache Calcite | ⏭️ SKIPPED | — | 30 | QED treats every aggregate operator as uninterpreted and only proves aggregate equality via bag-equality of the inputs, but this rule's c... |
| `AggregateFilterToCase` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The identity f(x) FILTER (WHERE p) ≡ f(CASE WHEN p THEN x END) (including the COUNT() no-arg special case) is valid only by algebra of sp... |
| `AggregateFilterToFilteredAggregate` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's validity rests entirely on the algebraic identity `agg(input restricted to rows where P) ≡ agg FILTER (WHERE P)(full input)` —... |
| `AggregateGroupingSetsToUnion` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's left-hand side is a GROUPING SETS aggregate, but QED's language has no such operator: `RelRN.Aggregate` builds only simple gro... |
| `AggregateJoinJoinRemove` | Apache Calcite | ⏭️ SKIPPED | — | 60 | The rule's soundness hinges on the null-extension branch of the bottom left join: rows l with no matching m (¬∃m. PB(l,m)) must be shown ... |
| `AggregateMeasure` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's correctness is exactly the identity AGG_M2V(c) over group G = SINGLE_VALUE of the per-row M2X(c, SAME_PARTITION(G)) — an algeb... |
| `AggregateMeasure2` | Apache Calcite | ⏭️ SKIPPED | — | 30 | AggregateMeasure2's validity rests entirely on the Calcite measure-framework identity `AGG_M2V(c) ≡ expand(AGG_M2M(c))`, where `expand` i... |
| `AggregateMinMaxToLimit` | Apache Calcite | ⏭️ SKIPPED | — | 13 | The rewrite's target is a `ORDER BY c ASC/DESC LIMIT 1` scalar subquery, and QED has no ordering semantics — Sort/Limit carry no bag-sema... |
| `AggregateProjectStarTable` | Apache Calcite | ⏭️ SKIPPED | — | 6 | The rule's soundness depends on two things QED structurally cannot capture: (1) its "after" side is a scan of a *different* materialized ... |
| `AggregateReduceFunctions` | Apache Calcite | ⏭️ SKIPPED | — | 30 | Every reduction branch of this rule — AVG = SUM/COUNT, SUM → SUM0 wrapped in a CASE over COUNT, the STDDEV/VAR/COVAR/REGR expansions, and... |
| `AggregateReduceFunctionsOnGroupKeys` | Apache Calcite | ⏭️ SKIPPED | — | 30 | Every reduction branch of this rule (MAX/MIN/AVG/ANY_VALUE applied to a group key becoming the key reference, or a SqlConstantValueAggFun... |
| `AggregateRemoveDuplicateKeys` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rewrite's soundness rests on ANY_VALUE's choice semantics — its result must be the group's functionally-determined value — but QED tr... |
| `AggregateRemoveLiteralAgg` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's entire correctness argument is the Calcite-internal algebraic identity that LITERAL_AGG(lit) evaluates to lit on every group p... |
| `AggregateStarTable` | Apache Calcite | ⏭️ SKIPPED | — | 9 | This rule is a materialization substitution, not a relational identity: it replaces Aggregate(Scan(starTable)) with a Scan of a *separate... |
| `AggregateToSemiJoin` | Apache Calcite | ⏭️ SKIPPED | — | 120 | The rewrite is only valid because a GROUP BY over the right input yields exactly one row per distinct key, making `L INNER JOIN (right ag... |
| `AggregateUnionTranspose` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's soundness rests on the split/merge algebra of specific aggregate functions (SUM additivity, MIN idempotence, COUNT→SUM0): it c... |
| `CalcSplit` | Apache Calcite | ⏭️ SKIPPED | — | 30 | CalcSplit merely de-fuses a Calc into Filter+Project, but RuleScript's core language has no fused Calc operator and a Calc is semanticall... |
| `CalcToWindow` | Apache Calcite | ⏭️ SKIPPED | — | 30 | CalcToWindow’s validity depends on the actual semantics of `RexOver`/`LogicalWindow`—partitioning, ordering, and frames—to turn a scalar ... |
| `CoerceInputs` | Apache Calcite | ⏭️ SKIPPED | — | 16 | The rule's only non-trivial content is inserting per-input cross-type casts, which in any faithful model are value-changing transformatio... |
| `CombineSimpleEquivalence` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's essence is to factor a shared sub-plan through a materializing `Spool` — a producer writes the sub-plan's rows to a temp table... |
| `CorrelateUncollectOuter` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's validity rests on `Uncollect`'s (Unnest's) bespoke internal semantics — specifically that `isOuter=true` guarantees at least o... |
| `ExchangeRemoveConstantKeys` | Apache Calcite | ⏭️ SKIPPED | — | 30 | ExchangeRemoveConstantKeysRule only rewrites the physical properties of an Exchange/SortExchange (hash-distribution keys and collation fi... |
| `FilterDateRange` | Apache Calcite | ⏭️ SKIPPED | — | 30 | FilterDateRangeRule's soundness rests on the specific calendar/timestamp semantics of EXTRACT/FLOOR/CEIL — e.g. that `EXTRACT(YEAR FROM d... |
| `FilterHilbert` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rewrite's soundness rests on the bespoke internal semantics of Calcite's HilbertCurve2D/SpaceFillingCurve2D and JTS spatial functions... |
| `FilterMultiJoinMerge` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule rewrites the *internal* post-join filter state of Calcite's MultiJoin — a backend-specific N-ary join operator (joinFilter + pos... |
| `FilterSampleTranspose` | Apache Calcite | ⏭️ SKIPPED | — | 10 | Sample has no deterministic bag semantics for QED to reason about — Bernoulli sampling is per-row probabilistic and system sampling depen... |
| `FilterSortMeasure` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule matches a `Filter` directly above a `Sort` in order to push down `M2V` (measure-to-value) calls whose values are defined relativ... |
| `FilterSortTranspose` | Apache Calcite | ⏭️ SKIPPED | — | 30 | FilterSortTranspose's correctness rests on row-ordering semantics — the rule (which only fires on pure-order sorts, i.e. no limit/offset)... |
| `FilterTableScan` | Apache Calcite | ⏭️ SKIPPED | — | 9 | FilterTableScanRule is a purely physical pushdown (Filter(TableScan) → BindableTableScan) whose before and after differ only in represent... |
| `IntersectToDistinct` | Apache Calcite | ⏭️ SKIPPED | — | 30 | IntersectToDistinct's correctness rests on the counting algebra of COUNT(*) — that aggregating each branch by all columns makes each dist... |
| `IntersectToExists` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The porter's stated reason was merely an LLM context-length crash, but the conclusion is correct for a real reason: the EXISTS arm of the... |
| `JoinConditionExpandIsNotDistinctFrom` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's correctness rests entirely on the null-aware data semantics of specific backend operators — `IS NOT DISTINCT FROM`, `COALESCE`... |
| `JoinExpandOrToUnion` | Apache Calcite | ⏭️ SKIPPED | — | 0 | The rewrite's core identity — Join(Or(P,Q)) ⟹ UnionAll(Join(P), Join(And(Q, ¬P))) — was tested directly against QED under both candidate ... |
| `JoinPushTransitivePredicates` | Apache Calcite | ⏭️ SKIPPED | — | 30 | This rule is metadata-driven predicate inference, not a structural rewrite — Calcite decomposes the join condition into equi-join equival... |
| `JoinToHyperGraph` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's after-side is Calcite's bespoke `HyperGraph` node (N inputs, hyperedges as node-bitmaps, conflict rules, `notProjectInputs`), ... |
| `JoinToMultiJoin` | Apache Calcite | ⏭️ SKIPPED | — | 30 | Calcite's MultiJoin (the rule's target operator) has no representation in RuleScript's core language or in QED's JSON node vocabulary — t... |
| `MarkToSemiOrAntiJoin` | Apache Calcite | ⏭️ SKIPPED | — | 60 | The blocker is the mark join itself: LEFT_MARK is a Calcite-specific operator whose defining feature — an appended boolean marker column ... |
| `Match` | Apache Calcite | ⏭️ SKIPPED | — | 30 | Calcite's MatchRule is a pure self-copy of a `LogicalMatch` (SQL MATCH_RECOGNIZE) node, so the only faithful encoding needs a Match opera... |
| `MaterializedViewFilterScan` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The porter's recorded failure was an LLM context-length error, not a real attempt — but the unsupported conclusion is independently corre... |
| `MaterializedViewOnlyAggregate` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The porter's stated reason is just a pipeline crash (context-length HTTP 400), not an analysis, but the UNSUPPORTED conclusion is nonethe... |
| `MaterializedViewOnlyFilter` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's validity rests on a catalog data-maintenance invariant — the materialized view table's contents must equal the result of its d... |
| `MaterializedViewOnlyJoin` | Apache Calcite | ⏭️ SKIPPED | — | 30 | MaterializedViewOnlyJoinRule is not a closed-form pattern-to-pattern logical rewrite — its validity rests on a side condition that an ext... |
| `MaterializedViewProjectAggregate` | Apache Calcite | ⏭️ SKIPPED | — | 30 | This rule is a materialized-view rewrite, not a local bag-equivalence transformation: it must replace a `Project(Aggregate)` query with a... |
| `MaterializedViewProjectFilter` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's soundness rests on an external data invariant — the materialized view's rows equal its defining query over the base tables, pl... |
| `MaterializedViewProjectJoin` | Apache Calcite | ⏭️ SKIPPED | — | 30 | MaterializedViewProjectJoinRule is a view-matching substitution, not an algebraic identity: it replaces a Project(Join(...)) query with a... |
| `MinusToDistinct` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's correctness rests on the algebra of COUNT: the post-rewrite plan's `count_0 > 0` and `count_i = 0` tests are what implement th... |
| `MinusToFilter` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rewrite's correctness depends on `NOT Q` being the exact complement of `Q` (i.e., rows with `Q = NULL` appearing on neither side), bu... |
| `ProjectJoinJoinRemove` | Apache Calcite | ⏭️ SKIPPED | — | 60 | The rule is valid only because the bottom join's condition is an equi-join on Y's unique key, guaranteeing at most one Y row per X row — ... |
| `ProjectJoinRemove` | Apache Calcite | ⏭️ SKIPPED | — | 90 | The rule's correctness rests on the join condition being an equality on the non-preserved side's key columns, which lets the key constrai... |
| `ProjectMeasure` | Apache Calcite | ⏭️ SKIPPED | — | 30 | ProjectMeasureRule's soundness rests on Calcite's measure-calculus law — that SINGLE_VALUE(M2X(M2V(E), SAME_PARTITION(g))) aggregated ove... |
| `ProjectMultiJoinMerge` | Apache Calcite | ⏭️ SKIPPED | — | 30 | Calcite's `ProjectMultiJoinMerge` only enriches a `MultiJoin` with `projFields` planner metadata while leaving the logical relation uncha... |
| `ProjectOverSumToSum0` | Apache Calcite | ⏭️ SKIPPED | — | 18 | The rule's subject — `SUM(x) OVER <frame>` vs `SUM0(x) OVER <frame>` inside a project — is inherently a windowed aggregate, and QED's the... |
| `ProjectReduceExpressions` | Apache Calcite | ⏭️ SKIPPED | — | 30 | ProjectReduceExpressions works by *evaluating* constant subtrees (e.g. 1+2→3, redundant CAST(x AS T)→x) using a RexExecutor, but QED mode... |
| `ProjectToLogicalProjectAndWindow` | Apache Calcite | ⏭️ SKIPPED | — | 9 | The rule's entire semantic content is that a windowed aggregate (RexOver) evaluated inline inside a Project yields the same per-row value... |
| `ProjectToSemiJoin` | Apache Calcite | ⏭️ SKIPPED | — | 60 | The rule's validity rests on the right-side aggregate grouping by exactly the join keys, so its output is unique per key value — which is... |
| `ProjectWindowTranspose` | Apache Calcite | ⏭️ SKIPPED | — | 12 | The rule's correctness rests entirely on Window semantics (one output row per input row, partition/order/frame-dependent values), but QED... |
| `ReduceDecimals` | Apache Calcite | ⏭️ SKIPPED | — | 30 | ReduceDecimals is a representation-level decimal expansion rule whose correctness depends on Calcite’s decimal scale/precision, REINTERPR... |
| `SampleToFilter` | Apache Calcite | ⏭️ SKIPPED | — | 13 | The rule's only claimed equivalence, Sample(R) ≡ Filter(rand() < rate, R), is a probabilistic (Bernoulli) identity — both sides keep rows... |
| `SemiJoinRemove` | Apache Calcite | ⏭️ SKIPPED | — | 60 | `SemiJoinRemove` rewrites `X SEMI JOIN Y ON c` to `X`, but a semi-join is an existence filter of X by Y, so this is not a bag-semantic eq... |
| `SortJoinCopy` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The only semantic content of SortJoinCopy is ordering — it rewrites Sort(collation[, offset, fetch])(Join(L, R, cond)) by adding decompos... |
| `SortJoinTranspose` | Apache Calcite | ⏭️ SKIPPED | — | 30 | SortJoinTranspose's soundness rests entirely on ordering semantics — pushing a Sort's top-(offset+fetch) down through an outer join so th... |
| `SortMerge` | Apache Calcite | ⏭️ SKIPPED | — | 30 | SortMerge's entire correctness claim is limit/sort composition — LIMIT_T over (SORT_B, LIMIT_B over X) rewriting to (SORT_B, LIMIT min(T,... |
| `SortProjectTranspose` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The entire semantic content of SortProjectTranspose is row-ordering: the sort must be remapped through the projection's collation (with o... |
| `SortRemove` | Apache Calcite | ⏭️ SKIPPED | — | 30 | SortRemove is fundamentally a row-ordering rule: its subject operator (a Sort with no offset/limit) and its defining precondition (the in... |
| `SortRemoveConstantKeys` | Apache Calcite | ⏭️ SKIPPED | — | 9 | The rule's entire content is order-preserving — it drops (or deletes) sort keys whose columns are constant so the emitted row *sequence* ... |
| `SortRemoveDuplicateKeys` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's only nontrivial content is at the sequence level: both sorts emit every input row exactly once, so bag-equivalence holds uncon... |
| `SortRemoveRedundant` | Apache Calcite | ⏭️ SKIPPED | — | 30 | This rule rewrites `Sort` nodes in both their ORDER BY and LIMIT forms, and QED explicitly does not model list/ordering semantics — `Sort... |
| `SortUnionTranspose` | Apache Calcite | ⏭️ SKIPPED | — | 30 | SortUnionTranspose's correctness rests entirely on top-N ordering semantics — pushing a Sort(offset O, fetch F) into each UNION ALL branc... |
| `TableScan` | Apache Calcite | ⏭️ SKIPPED | — | 13 | TableScanRule's only non-trivial case (view expansion via `table.toRel`) asserts that a scanned table's contents equal the relational pla... |
| `UnionToValues` | Apache Calcite | ⏭️ SKIPPED | — | 13 | UnionToValues is a constant-folding rule whose operand definition (a Union whose inputs are all Values) gives it no free relational or pr... |
| `WindowReduceExpressions` | Apache Calcite | ⏭️ SKIPPED | — | 30 | WindowReduceExpressions rewrites a Window's spec (constant-folding aggregation operands, dropping constant partition keys, dropping const... |

## Details

### `AggregateExpandDistinctAggregates` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateExpandDistinctAggregatesRule.java
- Attempts used: 72
- Last updated: 2026-09-22T16:57:03.593558+00:00
- Reason / notes: The encoding faithfully mirrors the source rule's `convertMonopole` branch: `before()` is `GROUP BY k` of two distinct calls `f(DISTINCT x), g(DISTINCT x)`, and `after()` makes them non-distinct on top of an intermediate `GROUP BY k, x` (no calls) that is exactly the source's `SELECT DISTINCT`-via-aggregate ded [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 114404667}, panicked=False

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

### `AggregateUnionAggregate` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateUnionAggregateRule.java
- Attempts used: 8
- Last updated: 2026-09-22T18:36:22.131201+00:00
- Reason / notes: The encoding is faithful and non-vacuous: `before()` truly differs from `after()` (an extra group-by-all, no-agg-call dedup beneath the second UNION ALL arm), and the proved identity dedup(A ⊎ dedup(L)) ≡ dedup(A ⊎ L) is exactly the algebraic content of Calcite's rule — UNION ALL (`all=true` as the source requires), top aggregate `isSimple` (group by all columns, no agg calls), bottom aggregate with no agg calls, and L/A are independent scans sharing only the row type the union itself mandates, so there is no triviality, wrong operator, sharing error, or silently dropped precondition. It is an honest, specific PARTIAL special case (single-column arms, inner dedup fixed to the second arm, and the bottom aggregate group-by-all rather than Calcite's allowed subset-group-set with an inserted Project), but the restriction is genuine rather than degenerate: the rule still removes a real aggregation, and the dropped generality (multi-column rows, which the current one-column `RelRN.scan` cannot express, and first-arm placement, which follows by UNION ALL symmetry) is what it is.

(This proof relies on an accepted RuleScript DSL extension made during this session — see the extended file(s) for what changed.)
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 68903375}, panicked=False

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
- Attempts used: 4
- Last updated: 2026-09-22T00:59:16.683479+00:00
- Reason / notes: before() is a genuinely nested Filter(Filter(scan)) while after() is a single Filter over an uninterpreted conjunction, so the proof is of the actual merge, not a vacuous structural identity. The input scan carries no key or guaranteed constraints (so in QED's translation it is a fully uninterpreted relation, faithfully standing in for the operand tree's `anyInputs`), and "inner"/"outer" are two distinct uninterpreted predicates referenced identically on both sides, exactly matching Calcite's onMatch (push(bottomFilter.getInput()).filter(bottomCond, topCond)), which RelBuilder implements as an AND. The source rule has no side conditions (no keys, nullability guarantees, etc.), so SCOPE: FULL is honest. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 67433250}, panicked=False

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

### `JoinReduceExpressions` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java

Note: ReduceExpressionsRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `JoinReduceExpressionsRule` variant (not CalcReduceExpressionsRule, FilterReduceExpressionsRule, ProjectReduceExpressionsRule, WindowReduceExpressionsRule, which are separate rules ported under their own spec names).
- Attempts used: 32
- Last updated: 2026-09-23T07:35:05.859495+00:00
- Reason / notes: The encoding faithfully mirrors JoinReduceExpressionsRule's actual transformation — the rule rewrites via `join.copy(traitSet, reducedCond, left, right, joinType, semiJoinDone)`, i.e. the same join with a constant-folded condition and deliberately NO empty relation (unlike the Filter/Calc variants), which is exactly what after() encodes — and `C AND FALSE` → `FALSE` is a genuine, non-vacuous instance of constant reduction (RexSimplify folds an AND with a FALSE operand to FALSE) with the surviving predicate expressed as a shared uninterpreted symbol, so before() and after() are structurally different and the proof is real. The narrowing to INNER join kind and to this one reducible condition shape is exactly as disclosed in the SCOPE: PARTIAL line, and is unavoidable within the single-record contract since MetaJoinType serializes to INNER in the Calcite semantics QED sees, making the INNER instance the best achievable proof and a non-degenerate, faithful special case of the source rule. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 531750}, panicked=False

### `JoinUnionTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinUnionTransposeRule.java
- Attempts used: 33
- Last updated: 2026-09-22T23:49:19.463599+00:00
- Reason / notes: The encoding is a faithful, non-vacuous instance of the rule: before() = (X ∪ALL Y) ⋈_C O vs after() = (X ⋈_C O) ∪ALL (Y ⋈_C O) is a genuine distributivity law (structurally distinct shapes, not an identity), the double `union(true, ...)` matches the source's `union.all` requirement, INNER keeps the union off any null-generating side so no semantic precondition is dropped, and the single shared condition symbol C correctly mirrors the source reusing the identical `join.getCondition()` in all three joins (independent symbols would have been the error). Nullable, keyless scans match the source's lack of PK/NOT NULL preconditions, so no hidden constraint is assumed. The scope line is honest and specific about the genuine narrowing (INNER join, left-side union) — full multi-join-type coverage cannot be a single provable rule because the join kind is concrete in QED's serialization (meta join types only feed the family generator, whose assignments are proven individually) — and the result is still a substantive, useful special case of the real rule. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1883000}, panicked=False

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

### `SetOpToFilter` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SetOpToFilterRule.java
- Attempts used: 11
- Last updated: 2026-09-22T23:25:33.293479+00:00
- Reason / notes: The encoding exactly captures Calcite's rewrite for the single-source, two-filter case — `Union(DISTINCT, [σ_P1(S), σ_P2(S)])` → all-fields group-by (no agg calls) over `σ_(P1∨P2)(S)` — and is faithful in the details: `union(false, ...)` preserves the rule's `!setOp.all` precondition, and rendering `.distinct()` as a group-by-all-fields Aggregate is precisely what Calcite's `RelBuilder.distinct()` itself builds, so the right side matches the rule's actual output (single branch, no 1-input set op). The proof is non-vacuous (the two sides are structurally different and require genuine filter-over-set-op reasoning with properly uninterpreted P1/P2 over a shared single source, exactly the sharing the rule requires), and the declared `SCOPE: PARTIAL` is honest and specific — 2-input UNION DISTINCT over filters on one shared source is a genuine, non-degenerate special case of the general arity/multi-source/INTERSECT/MINUS rule.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 74560667}, panicked=False

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

### `JoinToCorrelate` — ❌ FAILED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinToCorrelateRule.java
- Attempts used: 0
- Last updated: 2026-09-23T16:43:29.489291+00:00
- Reason / notes: agent error: Command '['grep', '-rn', '--include=*.java', '-E', 'Correlate', '/Users/wkaiz/Desktop/rule-porting-agent/.cache/workspaces/JoinToCorrelate']' timed out after 30 seconds

### `JoinToSemiJoin` — ❌ FAILED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRule.java

Note: SemiJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `JoinToSemiJoinRule` variant (not AggregateToSemiJoinRule, JoinOnUniqueToSemiJoinRule, ProjectToSemiJoinRule, which are separate rules ported under their own spec names).
- Attempts used: 0
- Last updated: 2026-09-23T10:47:58.877681+00:00
- Reason / notes: agent error: timed out

### `LoptOptimizeJoin` — ❌ FAILED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/LoptOptimizeJoinRule.java
- Attempts used: 0
- Last updated: 2026-09-23T09:24:47.531881+00:00
- Reason / notes: LLM error: HTTP 400 from http://169.229.48.114:8000/v1/chat/completions: {"error":{"message":"This model's maximum context length is 32768 tokens. However, you requested 1024 output tokens and your prompt contains at least 31745 input tokens, for a total of at least 32769 tokens. Please reduce the length of the input prompt or the number of requested output tokens. (parameter=input_tokens, value=31745)","type":"BadRequestError","param":"input_tokens","code":400}}

### `MultiJoinProjectTranspose` — ❌ FAILED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MultiJoinProjectTransposeRule.java
- Attempts used: 0
- Last updated: 2026-09-23T10:48:13.592478+00:00
- Reason / notes: agent error: timed out

### `OuterJoinToAntiJoin` — ❌ FAILED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/OuterJoinToAntiJoinRule.java
- Attempts used: 150
- Last updated: 2026-09-23T10:18:43.992947+00:00
- Reason / notes: LLM error: HTTP 400 from http://169.229.48.114:8000/v1/chat/completions: {"error":{"message":"This model's maximum context length is 32768 tokens. However, you requested 1024 output tokens and your prompt contains at least 31745 input tokens, for a total of at least 32769 tokens. Please reduce the length of the input prompt or the number of requested output tokens. (parameter=input_tokens, value=31745)","type":"BadRequestError","param":"input_tokens","code":400}}

### `ProjectSortMeasure` — ❌ FAILED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectSortMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, ProjectMeasureRule, FilterSortMeasureRule, which are separate rules ported under their own spec names).
- Attempts used: 0
- Last updated: 2026-09-23T10:48:31.917381+00:00
- Reason / notes: agent error: timed out

### `SubQueryRemove` — ❌ FAILED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SubQueryRemoveRule.java
- Attempts used: 0
- Last updated: 2026-09-23T13:54:53.358999+00:00
- Reason / notes: LLM error: HTTP 400 from http://169.229.48.114:8000/v1/chat/completions: {"error":{"message":"This model's maximum context length is 32768 tokens. However, you requested 1024 output tokens and your prompt contains at least 31745 input tokens, for a total of at least 32769 tokens. Please reduce the length of the input prompt or the number of requested output tokens. (parameter=input_tokens, value=31745)","type":"BadRequestError","param":"input_tokens","code":400}}

### `AggregateCaseToFilter` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateCaseToFilterRule.java
- Attempts used: 30
- Last updated: 2026-09-22T16:22:09.045404+00:00
- Reason / notes: The rule's correctness rests on aggregate-function algebra that QED explicitly does not model — that null-skipping aggregates (COUNT/SUM) ignore NULL inputs, that SUM0 is additive with 0/NULL as neutral, and that COUNT of a non-null constant equals counting rows — and per its own evaluation QED can only equate aggregates whose input bags are equal, whereas here the two sides aggregate different bags (CASE values over all rows, NULLs from the else branch included, versus plain values over only the rows satisfying the filter). The gap is on the prover side, not the DSL: even extending the DSL (which today also lacks a `filterArg` on `RelRN.AggCall` and any CASE/NULL literal in `RexRN`, so neither side is expressible as-is) could not make the equivalence derivable, since the null-skipping/sum identity is precisely the "bespoke internal semantics of an aggregate operator" QED cannot see through.

### `AggregateExpandWithinDistinct` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateExpandWithinDistinctRule.java
- Attempts used: 30
- Last updated: 2026-09-23T01:29:14.110059+00:00
- Reason / notes: QED treats every aggregate operator as uninterpreted and only proves aggregate equality via bag-equality of the inputs, but this rule's core step — equating an outer SUM/COUNT over per-(group, distinct-key)-subgroup MIN results with the before-side WITHIN DISTINCT aggregate computed directly over the input rows — requires knowing that MIN selects the functionally-dependent unique value and that SUM over subgroup minima equals SUM over the deduplicated values, which is exactly the aggregate algebra QED explicitly cannot know. The rewrite is also only sound under a user-asserted functional dependence (distinct key functionally determines the argument value within each group) — a side condition Calcite itself guards with THROW_UNLESS — and QED proves for all instantiations, so the FD cannot be assumed away; a countermodel with two rows sharing (group, distinct-key) but differing argument values refutes any universal proof. On top of that, the DSL/JSON carries no WITHIN DISTINCT or GROUPING SETS at all (the serializer drops `distinctKeys`), and since the QED prover is the immutable arbiter that would need a model for both, even the minimal single-grouping-set special case (outer `f(MIN(x))` over inner `Aggregate(group ∪ D, MIN(x))`) is unprovable — so the UNSUPPORTED conclusion is correct, not a product of the porter's context-length failure.

### `AggregateFilterToCase` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateFilterToCaseRule.java
- Attempts used: 30
- Last updated: 2026-09-23T01:31:20.971178+00:00
- Reason / notes: The identity f(x) FILTER (WHERE p) ≡ f(CASE WHEN p THEN x END) (including the COUNT() no-arg special case) is valid only by algebra of specific aggregate functions — null-ignoring aggregation over the filtered row subset vs. aggregation over a CASE column that yields NULL on non-matching rows, plus filter/grouping commutation — and QED explicitly knows nothing about an aggregate's algebra beyond input bag equality, so it cannot relate the two uninterpreted aggregate applications whose input bags differ. No DSL extension rescues this: the JSON serializer's `group` node doesn't even carry `filterArg` (and there is no CASE/NULL in the core language), so expressing the filtered side would just lose the filter to the prover rather than reveal the null/CASE interaction it models. There is no non-trivial special case (e.g. restricting to COUNT or a tautological filter) that avoids needing the prover to know null-counting/CASE semantics, so UNSUPPORTED is the correct, fundamental conclusion — the porter's context-length crash merely short-circuited what any real attempt would have reached. ```

### `AggregateFilterToFilteredAggregate` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateFilterToFilteredAggregateRule.java
- Attempts used: 30
- Last updated: 2026-09-22T16:20:55.450247+00:00
- Reason / notes: The rule's validity rests entirely on the algebraic identity `agg(input restricted to rows where P) ≡ agg FILTER (WHERE P)(full input)` — the internal semantics of a *filtered* aggregate call — and QED equates aggregate calls only by same uninterpreted operator plus bag-equality of input, knowing nothing else about aggregate algebra, so it cannot relate the two sides. The "after" side cannot even be faithfully encoded: QED's JSON model of an aggregate (`group.function`) carries only `operator/operand/distinct/ignoreNulls/type` with no `filter` field, and the DSL's `AggCall` has no filter parameter. Extending the Java-side DSL cannot close this gap because the missing semantics lives in the (off-limits) Rust prover, so the only encodable version would be a degenerate identity — UNSUPPORTED is genuinely correct (the porter's recorded HTTP-400 error is a mechanical failure, but its pre-failure analysis had already landed on this exact limitation).

### `AggregateGroupingSetsToUnion` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateGroupingSetsToUnionRule.java
- Attempts used: 30
- Last updated: 2026-09-22T16:30:44.135714+00:00
- Reason / notes: The rule's left-hand side is a GROUPING SETS aggregate, but QED's language has no such operator: `RelRN.Aggregate` builds only simple group-key aggregates (`RelBuilder.groupKey` + single group set), and the JSON contract the Rust prover consumes serializes a `group` node with only one "keys" list, so the prover has no interpretation for the operator whose expansion identity is the entire content of this rule. Encoding the "before" as a simple aggregate over the full group set instead would assert a false equivalence (one aggregate vs. a UNION ALL of sub-aggregates over its subsets, not bag-equal under QED's uninterpreted-aggregate model), and `extend_dsl_file` cannot help since it only touches the Java builders/serializer, not the unmodifiable prover's semantics. (A secondary, fixable gap is that `RexRN` has no NULL-literal builder for the padding columns — but the missing grouping-sets operator is the real blocker, so a fresh attempt would only confirm non-provability.) ```

### `AggregateJoinJoinRemove` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateJoinJoinRemoveRule.java
- Attempts used: 60
- Last updated: 2026-09-22T17:41:52.597225+00:00
- Reason / notes: The rule's soundness hinges on the null-extension branch of the bottom left join: rows l with no matching m (¬∃m. PB(l,m)) must be shown to survive via null-extension and then collapse under the distinct group-by, so proving the rewrite requires case-splitting on the existence of a match for an uninterpreted join condition PB — i.e. reasoning about the existential image of an uninterpreted predicate over independent symbols, which QED's bag-semantics normal-form/unification pipeline cannot do (its prover explicitly cannot reason about predicate inference/entailment between independent symbols). The porter's Probe 1 confirms this at the core: even the simplest non-trivial instance, a pure distinct group-by (no aggregate calls) over the nested left join vs. the single left join, was not provable, and no special case can remove that dichotomy without ceasing to be a left-join rule (a true-condition join still leaves the ∃m / M-empty case split over an uninterpreted table). ```

### `AggregateMeasure` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `AggregateMeasureRule` variant (not AggregateMeasure2Rule, ProjectMeasureRule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
- Attempts used: 30
- Last updated: 2026-09-23T01:37:10.379088+00:00
- Reason / notes: The rule's correctness is exactly the identity AGG_M2V(c) over group G = SINGLE_VALUE of the per-row M2X(c, SAME_PARTITION(G)) — an algebraic relationship between two *distinct* aggregate functions, plus the coupling of the SAME_PARTITION scalar expression to the aggregate's grouping — that Calcite supplies via RelMdMeasure metadata, i.e. a backend operator's bespoke internal semantics. QED models every aggregate call as an uninterpreted group function and "knows nothing about a specific aggregate function's algebra beyond bag equality of its input," so it can only equate identical calls over identical input bags and will countermodel any faithful encoding; the DSL already exposes every shape needed (scanMany, ProjectMany, generic projection/aggregate symbols, multi-key Aggregate), so this is a fundamental QED limitation rather than a missing builder, and the only "provable" version would alias the symbols into a vacuous tautology that no longer states the rule. (The porter's stated reason was actually an HTTP 400 context-overflow crash before any test, but the UNSUPPORTED conclusion itself is sound.) ```

### `AggregateMeasure2` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `AggregateMeasure2Rule` variant (not AggregateMeasureRule, ProjectMeasureRule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
- Attempts used: 30
- Last updated: 2026-09-23T01:39:06.488941+00:00
- Reason / notes: AggregateMeasure2's validity rests entirely on the Calcite measure-framework identity `AGG_M2V(c) ≡ expand(AGG_M2M(c))`, where `expand` is a `RelMdMeasure` metadata-driven expansion into a correlated `RexSubQuery`. That is an algebraic relation between two *distinct* uninterpreted aggregate symbols (QED only equates an aggregate's input bag; it has no axioms relating different aggregate operators) compounded with correlated-subquery/scoping semantics that QED's semiring model does not capture at all, so no RuleScript encoding — general or narrower special case — is provable.

### `AggregateMinMaxToLimit` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateMinMaxToLimitRule.java
- Attempts used: 13
- Last updated: 2026-09-23T01:37:40.988870+00:00
- Reason / notes: The rewrite's target is a `ORDER BY c ASC/DESC LIMIT 1` scalar subquery, and QED has no ordering semantics — Sort/Limit carry no bag-semantic meaning (the serializer's `LogicalSort` case only shows the JSON *format* can carry such nodes; the prover still cannot decide them), so the "after" side cannot be faithfully encoded, and that gap lives in the prover's semantics, not in a missing builder `extend_dsl_file` could add. Independently, the rule's correctness is precisely the algebraic identity "MIN/MAX of a column = top-1 row of its ordering", but QED treats aggregate operators as uninterpreted beyond bag equality of their inputs, so it cannot connect an `aggOp(c)` call on one side with a sort/limit pipeline on the other. Since every instance of this rule — no matter how specialized (single column, unique key, filters) — requires exactly that identity to be established, no non-degenerate special case is provable, and the porter's UNSUPPORTED conclusion is correct. ```

### `AggregateProjectStarTable` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateProjectStarTableRule.java
- Attempts used: 6
- Last updated: 2026-09-23T04:05:17.942440+00:00
- Reason / notes: The rule's soundness depends on two things QED structurally cannot capture: (1) its "after" side is a scan of a *different* materialized table chosen at match time from the planner's external lattice state, and the only constraint that would make that substitution valid — "materialization = group-by/aggregate over the star table" — is an inter-table definitional relation, while QED's per-table "guaranteed" constraints (per JSONSerializer) are row-wise predicates over a single table's own columns; even the no-rollup exact-match special case degenerates into swapping one uninterpreted scan for an unrelated uninterpreted scan, which no constraint in the format can link. (2) The roll-up variant (e.g. rolling COUNT(*) up as SUM over a coarser group set) requires additivity/regrouping algebra for specific aggregate functions, which QED explicitly does not model for uninterpreted aggregate operators — and this is a prover-level limitation no DSL extension can fix, so the porter's UNSUPPORTED conclusion is correct. ```

### `AggregateReduceFunctions` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateReduceFunctionsRule.java
- Attempts used: 30
- Last updated: 2026-09-23T03:53:19.302230+00:00
- Reason / notes: Every reduction branch of this rule — AVG = SUM/COUNT, SUM → SUM0 wrapped in a CASE over COUNT, the STDDEV/VAR/COVAR/REGR expansions, and the MAX/MIN/AVG group-key shortcut — is an algebraic identity about specific aggregate functions, but QED models each aggregate call as an uninterpreted function of its input bag, so no encoding (general or narrowed) can establish before/after equality for arbitrary symbol instantiations; the FIRST_VALUE/LAST_VALUE branches additionally rest on row ordering, which QED does not model at all. Since the gap is in the prover's theory rather than the DSL, `extend_dsl_file` cannot close it, and any variant that avoids the aggregate algebra degenerates into a trivial identity rather than a real reduction. This is the same fundamental limitation already empirically confirmed by the rejected `AggregateReduceFunctionsOnGroupKeys` probe in the UnprovableRRuleInstances directory.

### `AggregateReduceFunctionsOnGroupKeys` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateReduceFunctionsOnGroupKeysRule.java
- Attempts used: 30
- Last updated: 2026-09-22T18:14:41.912969+00:00
- Reason / notes: Every reduction branch of this rule (MAX/MIN/AVG/ANY_VALUE applied to a group key becoming the key reference, or a SqlConstantValueAggFunction becoming its declared constant, with NULL-preserving CASE) rests on the algebra of the specific aggregate function, whereas QED models every aggregate call as an uninterpreted function of its input bag — so the before/after equality fails for some instantiation of the aggregate symbol for any encoding, and the porter's probe (not provable, no timeout, complete) was the correct experiment, not a symbol-mismatch bug to diagnose further. No non-trivial special case survives: any provably-correct variant would have to omit the call elimination entirely, which is a trivial identity rather than the rule. This is a genuine QED limitation (no aggregate algebra beyond bag equality of inputs, and QED itself is unmodifiable), not a missing DSL capability, so extend_dsl_file cannot close the gap. ```

### `AggregateRemoveDuplicateKeys` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateRemoveDuplicateKeysRule.java
- Attempts used: 30
- Last updated: 2026-09-22T18:01:18.051427+00:00
- Reason / notes: The rewrite's soundness rests on ANY_VALUE's choice semantics — its result must be the group's functionally-determined value — but QED treats aggregate functions as uninterpreted functions of their input bag and, per the reference, "knows nothing about a specific aggregate function's algebra beyond bag equality of its input," so it cannot prove ANY_VALUE(group-bag) = f(a). The mq.determinesSet functional-dependency premise can at best be baked into a narrower source shape, but the rule's `after` always contains an ANY_VALUE call for each removed key, so no non-degenerate instance of this exact before/after shape is provable; the only provable variant (re-deriving the removed key as f(A) in a trailing project instead of ANY_VALUE) is a different rewrite, not a special case of this rule. ```

### `AggregateRemoveLiteralAgg` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateRemoveLiteralAggRule.java
- Attempts used: 30
- Last updated: 2026-09-22T17:16:34.923982+00:00
- Reason / notes: The rule's entire correctness argument is the Calcite-internal algebraic identity that LITERAL_AGG(lit) evaluates to lit on every group produced by GROUP BY (every produced group is non-empty), and QED models aggregates as uninterpreted functions whose only known behavior is bag-equality of inputs — it cannot derive that LITERAL_AGG's output equals the literal the after-side re-projects, so an SMT model with LITERAL_AGG ≠ lit is a valid counterexample to any faithful encoding. No narrowing fixes this (the dependency on LITERAL_AGG's bespoke semantics is present even in the minimal one-group-key, one-LITERAL_AGG(true) case), and the gap is not a missing DSL builder — the before/after shapes (scan, Aggregate with an AggCall over a literal operand, ProjectMany restoring fields plus a literal) are fully expressible with the current API — but a genuine limitation of QED's aggregate semantics, with no axiom/interpretation hook (table "guaranteed" constraints only constrain scan columns, and the prover itself is off-limits) that a DSL extension could supply. ```

### `AggregateStarTable` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateStarTableRule.java
- Attempts used: 9
- Last updated: 2026-09-23T04:53:04.543952+00:00
- Reason / notes: This rule is a materialization substitution, not a relational identity: it replaces Aggregate(Scan(starTable)) with a Scan of a *separate* pre-aggregated table (optionally topped by a roll-up Aggregate or a reordering Project), and its soundness rests entirely on the planner lattice guaranteeing that the pre-aggregated table's rows equal the aggregation of the star table — a cross-table content invariant QED cannot express, since each scan is an independent uninterpreted relation whose schema carries only types, keys, and per-row guaranteed predicates (no builder or JSON field can relate one table's contents to another's, and the immutable prover has no such mechanism). The roll-up branch independently requires concrete SUM/COUNT roll-up aggregate algebra ("sum of sums = sum") that QED explicitly does not model for uninterpreted aggregate functions. The porter's diagnosis is correct and confirmed empirically: the two tables must be distinct symbols in any faithful encoding, the hint's fixable causes (name mismatch, missing composition, key/flag) genuinely don't apply, and the complete, non-timed-out `provable: false` run shows the gap is fundamental — and no narrower non-vacuous special case exists because the aggregate→scan-of-different-table substitution is the rule itself. ```

### `AggregateToSemiJoin` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRule.java

Note: SemiJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `AggregateToSemiJoinRule` variant (not JoinOnUniqueToSemiJoinRule, JoinToSemiJoinRule, ProjectToSemiJoinRule, which are separate rules ported under their own spec names).
- Attempts used: 120
- Last updated: 2026-09-23T05:27:37.660248+00:00
- Reason / notes: The rewrite is only valid because a GROUP BY over the right input yields exactly one row per distinct key, making `L INNER JOIN (right aggregate)` equivalent to `L SEMI JOIN (raw right)` — that dedup/partition fact is precisely aggregate algebra, which QED explicitly does not model beyond bag equality of an aggregate's input. Consequently the before-side top aggregate receives an input bag that includes the join's right columns while the after-side does not, and since aggregate calls are uninterpreted functions of their input bag, no encoding can make QED equate the two sides — the porter's "not provable" reflects a genuine QED limitation, not a fixable symbol-sharing or shape bug (the recorded HTTP 400 merely aborted a correctly diagnosed attempt).

### `AggregateUnionTranspose` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateUnionTransposeRule.java
- Attempts used: 30
- Last updated: 2026-09-22T10:33:50.070619+00:00
- Reason / notes: The rule's soundness rests on the split/merge algebra of specific aggregate functions (SUM additivity, MIN idempotence, COUNT→SUM0): it claims that re-aggregating the per-branch aggregate *results* equals aggregating the raw unioned rows. QED models every aggregate call as an uninterpreted symbol with no algebraic knowledge, and the top aggregate's input bag (the per-branch f-images) is not the same bag the original aggregate sees (the raw rows), so the two applications of the same uninterpreted function have universally different inputs and the SMT solver can build a countermodel for every candidate encoding; this limitation lives in the trusted prover's semantics, not in a missing DSL builder, so no RuleScript encoding of the transpose itself (not even a narrowed special case) is provable. ```

### `CalcSplit` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/CalcSplitRule.java
- Attempts used: 30
- Last updated: 2026-09-23T04:49:42.528003+00:00
- Reason / notes: CalcSplit merely de-fuses a Calc into Filter+Project, but RuleScript's core language has no fused Calc operator and a Calc is semantically just Project∘Filter, so the before and after sides are literally the same expression and the rewrite is a definitional tautology rather than a non-trivial equivalence. QED's Q-expression/JSON layer has no Calc node it can interpret — it only sees the decomposed Filter/Project/Join/Aggregate forms — so the fused operator's internal semantics are invisible to the prover and there is nothing beyond X==X for QED to decide; the porter's stated HTTP 400 context error is a red herring, the real blocker is the absent fused-operator semantics. ```

### `CalcToWindow` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectToWindowRule.java

Note: ProjectToWindowRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `CalcToWindowRule` variant (not ProjectToLogicalProjectAndWindowRule, which are separate rules ported under their own spec names).
- Attempts used: 30
- Last updated: 2026-09-23T05:15:37.179583+00:00
- Reason / notes: CalcToWindow’s validity depends on the actual semantics of `RexOver`/`LogicalWindow`—partitioning, ordering, and frames—to turn a scalar `OVER` expression into a per-row window relation. QED is stated to have no list/ordering semantics for `Window` (and its serializer/prover has no `LogicalWindow`/`RexOver` model), so a faithful before/after pair cannot be proved without treating the window as an opaque symbol that loses the rule’s content.

### `CoerceInputs` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/CoerceInputsRule.java
- Attempts used: 16
- Last updated: 2026-09-23T06:00:21.425141+00:00
- Reason / notes: The rule's only non-trivial content is inserting per-input cross-type casts, which in any faithful model are value-changing transformations, so QED must treat the cast as a non-identity uninterpreted function and correctly cannot prove `consumer(cast(x)) ≡ consumer(x)` under the consumer's uninterpreted predicates. Its soundness additionally rests on the consumer's `getExpectedInputRowType(i)` contract — a type-level side condition on a backend operator that the pattern language has no way to express. Adding a cast builder via `extend_dsl_file` would not close the gap: the only value-transparent instance (input type already equal, rename-only) is either a no-op the rule wouldn't perform or inexpressible as a rename in the core language, so no faithful, non-vacuous encoding exists for QED to prove. ```

### `CombineSimpleEquivalence` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/CombineSimpleEquivalenceRule.java
- Attempts used: 30
- Last updated: 2026-09-23T05:01:35.311151+00:00
- Reason / notes: The rule's essence is to factor a shared sub-plan through a materializing `Spool` — a producer writes the sub-plan's rows to a temp table and a separate consumer `LogicalTableScan` in a different branch reads them back — which is a stateful, cross-branch producer→consumer data dependency that QED cannot express, since it models every relation as a pure bag function of independent, uninterpreted table scans (to QED the consumer scan is just an arbitrary table, not the producer's output). The rule's trigger is also structural `RelDigest` common-sub-expression detection, a syntactic notion QED never evaluates, so no `before`/`after` encoding could be shown bag-equivalent.

### `CorrelateUncollectOuter` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/CorrelateUncollectOuterRule.java
- Attempts used: 30
- Last updated: 2026-09-23T05:27:14.998780+00:00
- Reason / notes: The rule's validity rests on `Uncollect`'s (Unnest's) bespoke internal semantics — specifically that `isOuter=true` guarantees at least one output row (a NULL row) even when the expanded collection is empty — which is what lets the LEFT-Correlate's NULL-padding case be fully absorbed so that LEFT collapses to INNER; QED treats operators as uninterpreted bag functions with no concept of collection expansion or output-cardinality guarantees, and cannot see that `Uncollect(isOuter=true)` is non-empty (nor accept a side-premise that the correlate's right side is guaranteed non-empty), so the equivalence is not provable. Moreover, `Uncollect` has neither a `RelRN`/`RexRN` builder nor any case in the QED JSON/Q-expr serialization format (only Correlate and correlated field-access are partially supported), so the rule is not even expressible. This is a fundamental QED limitation (unseeable operator-internal semantics), not a missing encoding the porter could have fixed.

### `ExchangeRemoveConstantKeys` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ExchangeRemoveConstantKeysRule.java
- Attempts used: 30
- Last updated: 2026-09-23T05:11:13.262830+00:00
- Reason / notes: ExchangeRemoveConstantKeysRule only rewrites the physical properties of an Exchange/SortExchange (hash-distribution keys and collation fields); under bag semantics both sides of the rewrite are literally the same relation, and the rule's actual correctness argument — that a known-constant key makes hash distribution degenerate to singleton placement and a constant collation field vacuous — is a claim about node placement and row ordering, exactly the physical semantics QED does not model (same category as its unmodeled Sort/Window/list semantics). No faithful encoding exists: the DSL has no Exchange operator and none can be added meaningfully (an identity model would only vacuously prove the *general* removal Calcite deliberately does not do, without ever using the constant-key premise; an uninterpreted-relational model would leave before/after as distinct symbols QED cannot equate), so this is a genuine QED limitation, not a missing-builder gap — the porter's context-overflow error notwithstanding, a real attempt could at best produce a trivial X≡X proof with no content.

### `FilterDateRange` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/DateRangeRules.java
- Attempts used: 30
- Last updated: 2026-09-23T06:01:44.475263+00:00
- Reason / notes: FilterDateRangeRule's soundness rests on the specific calendar/timestamp semantics of EXTRACT/FLOOR/CEIL — e.g. that `EXTRACT(YEAR FROM d) = v` holds exactly over the interval [v-01-01, (v+1)-01-01) — which requires axioms relating those functions to comparisons on `d` that RuleScript/QED have no mechanism to state. The DSL can't express either side structurally (no date literals, no calendar functions), so both sides reduce to independent uninterpreted predicate symbols over the same column, and QED cannot infer entailment between independent symbols or see through an operator's internal semantics. The only provable encoding is the trivial identity (same predicate name on both sides), so the rule is genuinely outside RuleScript/QED's capability, not a failure of encoding. ```

### `FilterHilbert` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SpatialRules.java
- Attempts used: 30
- Last updated: 2026-09-23T06:41:59.391530+00:00
- Reason / notes: The rewrite's soundness rests on the bespoke internal semantics of Calcite's HilbertCurve2D/SpaceFillingCurve2D and JTS spatial functions (points within distance d of a constant geometry ⇒ Hilbert-8 index in specific numeric ranges), which QED can only model as uninterpreted symbols; under the sole expressible constraint `h = hilbert(lon, lat)` (a UF equality), no entailment from `ST_DWithin(g, pt(lon,lat), d)` to a range predicate on `h` is derivable, and the concrete bounds are computed in Java outside the pattern language, so no DSL extension could close the gap (adding the implication itself as a constraint would be circular and vacuous). The porter's transcript shows it had already independently reached exactly this fundamental point before the HTTP 400 context-length error cut it off, so the UNSUPPORTED conclusion is correct despite the garbled termination reason. ```

### `FilterMultiJoinMerge` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterMultiJoinMergeRule.java
- Attempts used: 30
- Last updated: 2026-09-23T06:17:49.314107+00:00
- Reason / notes: The rule rewrites the *internal* post-join filter state of Calcite's MultiJoin — a backend-specific N-ary join operator (joinFilter + postJoinFilter + per-pair outer-join conditions + optional projFields) whose semantics live inside the operator, and which has no node in QED's core language or its JSON format (only binary joins; the Rust prover, the trusted arbiter, cannot be modified to accept a multi-join node, and a DSL extension could at best desugar it back to core nodes). Uninterpreted symbols exist only for tables/predicates/projections/join-kinds/types — not for relational operators — so MultiJoin cannot be introduced as an opaque symbol whose inner state is shared between before() and after() and conjunctively enriched on the after side. The only available encoding flattens the inner-join/no-projection MultiJoin into nested true-joins plus stacked filters, which reduces the rule to the plain FilterMerge identity already in the core language (trivially provable, but it verifies nothing about MultiJoin itself — the flattening itself would be an unverified assumption); the outer-join and projFields variants cannot even be flattened, since QED cannot relate a predicate over a projected row to its translation below an uninterpreted projection. ```

### `FilterSampleTranspose` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterSampleTransposeRule.java
- Attempts used: 10
- Last updated: 2026-09-23T06:56:46.529856+00:00
- Reason / notes: Sample has no deterministic bag semantics for QED to reason about — Bernoulli sampling is per-row probabilistic and system sampling depends on row positions — and the Rust prover, which cannot be modified, has no model of the operator (qed.pdf explicitly lists Sample among operators with no bag-semantic meaning; even the Java-side JSONSerializer has no LogicalSample case, so a DSL extension could not close the gap). The only encodable stand-in, an uninterpreted per-row predicate, reduces the rule to Filter(P,Filter(S,R)) = Filter(S,Filter(P,R)), i.e. trivial conjunctive filter commutativity — a different theorem, not a special case of the transpose, since the rule's content *is* the Sample operator and every faithful instance must contain it. Hence there is no genuine non-vacuous special case for QED to prove, and the UNSUPPORTED conclusion stands. ```

### `FilterSortMeasure` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `FilterSortMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, ProjectMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
- Attempts used: 30
- Last updated: 2026-09-23T06:33:08.219881+00:00
- Reason / notes: The rule matches a `Filter` directly above a `Sort` in order to push down `M2V` (measure-to-value) calls whose values are defined relative to sort position, but QED has no model for list/order semantics (`Sort` has no bag-semantic meaning) and `M2V`/the measure framework carries bespoke backend internal semantics (RelMdMeasure expansion over sorted rows) that QED cannot see through as uninterpreted functions. As written, `onMatch` merely rebuilds the identical `Filter(Sort(X))` tree, so the only encodable form would be a Sort-free tautology that proves nothing about the actual rule — there is no faithful, non-trivial special case to salvage. (The porter's stated reason was only an LLM context-length error, but the UNSUPPORTED conclusion happens to be correct.)

### `FilterSortTranspose` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterSortTransposeRule.java
- Attempts used: 30
- Last updated: 2026-09-23T07:27:48.692620+00:00
- Reason / notes: FilterSortTranspose's correctness rests on row-ordering semantics — the rule (which only fires on pure-order sorts, i.e. no limit/offset) is valid because filtering a sorted input preserves its collation order, and that ordering claim is exactly what QED does not model: it decides bag-semantic equivalence only, where Sort/collation carries no meaning, so both sides collapse to the same bag and the non-trivial content of the rule is inexpressible and unverifiable. This is a genuine fundamental limitation of the prover, not an encoding gap — even the porter's DSL extension adding a `sort` builder (which compiled regression-free) would at best yield a vacuous bag-identity proof, so UNSUPPORTED is the right conclusion. ```

### `FilterTableScan` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterTableScanRule.java
- Attempts used: 9
- Last updated: 2026-09-22T18:36:24.001981+00:00
- Reason / notes: FilterTableScanRule is a purely physical pushdown (Filter(TableScan) → BindableTableScan) whose before and after differ only in representation — the BindableTableScan's output rows are *defined* by the backend FilterableTable/BindableTableScan contract to equal the logical Filter-over-Scan, so there is no logical algebraic identity for QED to verify. The bag-semantic core language has no physical/native-filtered-scan operator, so the after-side can only be encoded as the textually identical Filter(P,Scan) (a tautology that proves nothing about the rewrite) or as an uninterpreted operator to which the fixed QED prover has no axiom linking it back to Filter(P,Scan) — and this gap is a genuine limitation of QED's logical model, not a missing DSL builder that `extend_dsl_file` could close, since the physical operator has no distinct logical identity at all. ```

### `IntersectToDistinct` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/IntersectToDistinctRule.java
- Attempts used: 30
- Last updated: 2026-09-22T03:13:20.981500+00:00
- Reason / notes: IntersectToDistinct's correctness rests on the counting algebra of COUNT(*) — that aggregating each branch by all columns makes each distinct row appear exactly once per branch, and that a second group-by + count over the UNION ALL then yields the number of branches containing a row, which equals n precisely for set-intersection membership. QED treats aggregate calls as uninterpreted and can only establish aggregate equivalence from bag equality of their inputs (it knows no COUNT/aggregate algebra), so it has no axioms relating the plain set-semantic INTERSECT on the before side to the nested-aggregate pipeline on the after side, regardless of encoding. The porter actually died on an infrastructure error (context-length overflow) before testing anything, but the conclusion holds; the non-pushdown variant is doubly out of reach since its `COUNT(*) FILTER (WHERE ...)` form isn't even expressible in the current AggCall API, and no DSL extension can change QED's aggregate semantics. ```

### `IntersectToExists` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/IntersectToExistsRule.java
- Attempts used: 30
- Last updated: 2026-09-22T18:07:09.441253+00:00
- Reason / notes: The porter's stated reason was merely an LLM context-length crash, but the conclusion is correct for a real reason: the EXISTS arm of the rewrite depends on per-field IS NOT DISTINCT FROM comparisons (and correlated-subquery semantics), which QED treats as uninterpreted symbols with no built-in equality meaning, while the before side is a bare INTERSECT containing no such symbols — so QED would have to prove equivalence for *every* instantiation of that comparison (e.g. one that is always false, making the result just A rather than A∩B) and fail. The DSL also has no correlate/exists builder at all (only JSONSerializer's output format can carry them), but extending the DSL would not close the gap, since the blocker is QED's inability to see through the comparison operator's row-identity semantics, not a missing builder. ```

### `JoinConditionExpandIsNotDistinctFrom` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinConditionExpandIsNotDistinctFromRule.java
- Attempts used: 30
- Last updated: 2026-09-22T11:29:39.053881+00:00
- Reason / notes: The rule's correctness rests entirely on the null-aware data semantics of specific backend operators — `IS NOT DISTINCT FROM`, `COALESCE`, and `IS_NULL` (i.e., that `x IS NOT DISTINCT FROM y` iff `COALESCE(x,0)=COALESCE(y,0) AND (x IS NULL)=(y IS NULL)`). QED operates on uninterpreted symbols and "can't reason about predicate inference/entailment between independent symbols or a backend operator's bespoke internal semantics," so with these operators modeled as opaque generic ops the left and right sides are unrelated SMT symbols and no universal proof exists; QED only knows the Boolean `AND`-composition layer, not the operator-null algebra this rewrite depends on. Adding DSL builders for these operators via `extend_dsl_file` cannot close the gap, since a builder only introduces another uninterpreted name and cannot inject SMT semantics into the Rust prover (the unchangeable arbiter) — so even a narrowed special case (e.g., non-null operands) is unprovable. ```

### `JoinExpandOrToUnion` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinExpandOrToUnionRule.java
- Attempts used: 0
- Last updated: 2026-09-22T23:59:02.692419+00:00
- Reason / notes: The rewrite's core identity — Join(Or(P,Q)) ⟹ UnionAll(Join(P), Join(And(Q, ¬P))) — was tested directly against QED under both candidate negation operators and both failed identically: provable=false, complete_fragment=true (QED confidently explored the whole fragment, not a timeout). This was tried twice each, including in the simplest possible reduced form (a single relation, Filter(Or(P,Q)) vs Union(Filter(P), Filter(And(Q,¬P))), no join at all):

1. Using RexRN.Not (plain negation): not provable.
2. Using a new, correctly-implemented RexRN.IsNotTrue node (QED's Rust source at pipeline/relation.rs:618 confirms "IS NOT TRUE" is a real, recognized operator with proper 3-valued semantics distinct from NOT): not provable, same signature.

Since the reduced single-relation form is a textbook propositional tautology (P ∨ (Q ∧ ¬P) ≡ P ∨ Q, trivial by case analysis on P), a hard "not provable" on it rules out both the specific-negation-operator hypothesis and any join-specific cause. The most likely explanation is a genuine QED equivalence-checking limitation: it does not correlate the same uninterpreted predicate symbol referenced both positively and negatively across two separate UNION branches of the *same* side, unlike a plain before()/after() pair (which is how every already-proved rule in this project uses shared symbols, and which QED handles fine). This is not a missing DSL operator that extend_dsl_file could add — both required operators (NOT, IS NOT TRUE) already exist and are correctly wired to QED's own recognized semantics; the gap is in how QED relates an uninterpreted symbol's occurrences across independent branches of a bag union, which is outside what a DSL-level change can address. UNSUPPORTED.

### `JoinPushTransitivePredicates` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinPushTransitivePredicatesRule.java
- Attempts used: 30
- Last updated: 2026-09-22T01:21:21.713240+00:00
- Reason / notes: This rule is metadata-driven predicate inference, not a structural rewrite — Calcite decomposes the join condition into equi-join equivalence classes and pushes rewrites of pulled-up predicates, so the added filters are valid only as logical consequences of the existing ones (e.g., l.a=r.b ∧ l.a=5 ⇒ r.b=5). In RuleScript the join condition and all filters are independent uninterpreted symbols, and QED explicitly cannot reason about predicate inference/entailment between independent [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]

### `JoinToHyperGraph` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinToHyperGraphRule.java
- Attempts used: 30
- Last updated: 2026-09-23T09:14:49.374969+00:00
- Reason / notes: The rule's after-side is Calcite's bespoke `HyperGraph` node (N inputs, hyperedges as node-bitmaps, conflict rules, `notProjectInputs`), whose meaning is defined by planner-side enumeration machinery rather than any single bag-semantic expression, and QED's Q-expression/JSON model only knows scan/values/filter/project/join/correlate/group/set-ops/sort (see the switch in JSONSerializer.java) with no uninterpreted-relation symbol to stand in for a new operator — and the fixed Rust prover cannot be taught a "hypergraph" node, so `extend_dsl_file` is a dead end. The only QED-visible encoding (desugaring the after-side to a core-language expression, e.g. cross product + combined filter) proves a different rule — a join-flattening/reassociation identity, and only for INNER joins, since the rule's supported LEFT/FULL/SEMI/ANTI kinds genuinely lack tree-shape invariance — not the transformation into a HyperGraph. This is exactly the limitation category of a backend operator's bespoke internal semantics that QED cannot see through, so UNSUPPORTED is correct despite the porter's actual failure being a context-length API error. ```

### `JoinToMultiJoin` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinToMultiJoinRule.java
- Attempts used: 30
- Last updated: 2026-09-23T07:43:20.556273+00:00
- Reason / notes: Calcite's MultiJoin (the rule's target operator) has no representation in RuleScript's core language or in QED's JSON node vocabulary — the serializer's switch only knows a binary `join` node — and the unmodifiable Rust prover cannot be given an N-ary-join constructor, so `extend_dsl_file` cannot close the gap (a new Java builder would emit JSON the prover cannot parse). MultiJoin's semantics are also bespoke backend-internal state — per-input outer-join type/condition arrays, the full-outer flag, post-join filters, and join-field reference counts — that QED's bag-semiring model does not interpret, so even a hypothetical encoding could not be checked. Any DSL-expressible "after" side degenerates to a binary-join tree, collapsing the statement to join associativity (a different, already-provable rule class like JoinAssociate), leaving no non-trivial special case of this rule to encode. ```

### `MarkToSemiOrAntiJoin` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MarkToSemiOrAntiJoinRule.java
- Attempts used: 60
- Last updated: 2026-09-23T08:12:59.287872+00:00
- Reason / notes: The blocker is the mark join itself: LEFT_MARK is a Calcite-specific operator whose defining feature — an appended boolean marker column encoding "this left row has a match under the join condition" — is bespoke internal semantics QED's bag-semiring model has no encoding for, and it cannot be reconstructed from the core language (a LEFT join + not-null filter has the wrong bag multiplicities and no null test is expressible; a group-by/exists reconstruction collapses the left bag and would require aggregate algebra QED does not know; an uninterpreted marker symbol would make the semi/anti equivalence an unprovable entailment about an independent symbol), and the anti branch additionally rests on the "join condition is not strong" null-behavior notion QED cannot model. (The porter's recorded "reason" was actually an LLM context-overflow error before it finished checking the LEFT_MARK kind's round-trip through the prover, not a reasoned limitation analysis — but the UNSUPPORTED conclusion is correct for the fundamental reason above.)

### `Match` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MatchRule.java
- Attempts used: 30
- Last updated: 2026-09-23T10:57:45.481535+00:00
- Reason / notes: Calcite's MatchRule is a pure self-copy of a `LogicalMatch` (SQL MATCH_RECOGNIZE) node, so the only faithful encoding needs a Match operator on both sides — but MATCH_RECOGNIZE is a sequence/ordering-dependent operator (partition/order keys, pattern matching over ordered rows, FIRST/LAST measures, strict start/end, interval) in the same fundamental class as the Sort/Window/Limit operators that QED explicitly cannot model with bag semantics, and the DSL offers no relation-level uninterpreted operator to stand in for it (uninterpreted symbols cover only scalar predicates/projections, tables, types, and join kinds, and QED's JSON format has no match node type to serialize to). Thus no before/after pair can even express the rule, and the only provable encoding — a content-free identity `R ⟹ R` over an uninterpreted relation — is a different, vacuous statement, not a port of this rule.

### `MaterializedViewFilterScan` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MaterializedViewFilterScanRule.java
- Attempts used: 30
- Last updated: 2026-09-23T09:36:58.025606+00:00
- Reason / notes: The porter's recorded failure was an LLM context-length error, not a real attempt — but the unsupported conclusion is independently correct. The rule's soundness rests on the externally maintained materialization invariant "the MV storage table's contents equal its defining query's result," i.e. a cross-table content equality between two independent uninterpreted tables; QED checks before/after equivalence under *all* instantiations of those tables, and the DSL's only per-table facts are keys (via `unique`) and row-level `guaranteed` predicates (not even exposed by any current `scan` builder), so no encoding can state that one table's bag equals a query over another. Hence every real encoding fails: distinct table names for the base table and the MV are refuted by a counterexample (e.g. an empty MV), while reusing one name degenerates to a vacuous identity — and besides the unstateable premise, the rule emits a catalog-dependent family of substitutions (via `SubstitutionVisitor` plus an internal Hep normalization program) rather than a fixed before/after plan pair.

### `MaterializedViewOnlyAggregate` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewOnlyAggregateRule.java
- Attempts used: 30
- Last updated: 2026-09-23T08:19:34.705011+00:00
- Reason / notes: The porter's stated reason is just a pipeline crash (context-length HTTP 400), not an analysis, but the UNSUPPORTED conclusion is nonetheless correct on the merits. The rule's soundness rests on aggregate-algebra identities QED by design cannot know: rollup/roll-down re-partitioning (e.g., SUM over a coarser grouping equals the SUM of SUMs over a finer grouping, COUNT(*) rolled up via SUM, idempotent MIN/MAX), and its union-rewriting path additionally needs complementary-filter derivation between independent predicate symbols. QED only equates aggregates that are structurally identical with bag-equal inputs and has no way to constrain an MV's materialized table to equal the result of the MV's defining aggregate over the query's base scan, so the only provable encoding would be the degenerate no-op where the MV aggregate is structurally identical to the query's — not a genuine instance of the rule.

### `MaterializedViewOnlyFilter` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewOnlyFilterRule.java
- Attempts used: 30
- Last updated: 2026-09-23T11:10:01.217418+00:00
- Reason / notes: The rule's validity rests on a catalog data-maintenance invariant — the materialized view table's contents must equal the result of its defining plan over the base tables — i.e., a required coupling between two uninterpreted relations that QED cannot assume, since it decides bag-equivalence for *every* instantiation of free table symbols and the DSL/JSON format carries only per-table keys/constraints, with no cross-table "defined-as" relation or hypothesis mechanism. Any encoding that keeps the view as a distinct `Scan` on the after side is unprovable (the view and base tables are independent symbols), while inlining the view definition on both sides — the only way to get a proof — erases the rule's actual content (serving the query from a precomputed artifact without re-scanning the base) and degenerates to the pure relational tautology σ_q(A) ≡ σ_{q∧v}(A) ∪ σ_{q∧¬v}(A), which no longer involves a materialized view at all; the rule's semantic core is therefore outside QED's decision domain, not a missing DSL builder.

### `MaterializedViewOnlyJoin` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewOnlyJoinRule.java
- Attempts used: 30
- Last updated: 2026-09-23T09:56:01.979521+00:00
- Reason / notes: MaterializedViewOnlyJoinRule is not a closed-form pattern-to-pattern logical rewrite — its validity rests on a side condition that an externally defined materialized-view table (an independent uninterpreted scan symbol in the DSL) is maintained as the bag result of a specific query over the base tables, a table-equals-derived-relation fact that RuleScript's core language has no construct to state (no view/definition operator; "guaranteed" table constraints are only row-wise scalar predicates, not relation equality), and on compensation predicates derived via predicate entailment between uninterpreted predicate symbols, which QED explicitly cannot reason about. The only possible encoding (before: Join over base tables; after: scan of the MV table) is not universally bag-equivalent — the prover would find a trivial countermodel (e.g. empty MV, nonempty join) — so no re-encoding can be proved, and the porter's conclusion (though actually caused by an LLM context-length error rather than a QED verdict) is correct.

### `MaterializedViewProjectAggregate` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewProjectAggregateRule.java
- Attempts used: 30
- Last updated: 2026-09-23T08:30:34.392459+00:00
- Reason / notes: This rule is a materialized-view rewrite, not a local bag-equivalence transformation: it must replace a `Project(Aggregate)` query with a separate view scan, possibly with compensation and a roll-up aggregate. QED can only relate expressions built over the same uninterpreted scans/operators, and it treats aggregate functions as uninterpreted except for input-bag equality, so it cannot assume a view scan equals its defining aggregate or prove re-aggregation identities. Any faithful encoding would therefore require unsupported premises about the view and aggregate algebra, making the UNSUPPORTED conclusion correct.

### `MaterializedViewProjectFilter` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewProjectFilterRule.java
- Attempts used: 30
- Last updated: 2026-09-23T11:18:25.069926+00:00
- Reason / notes: The rule's soundness rests on an external data invariant — the materialized view's rows equal its defining query over the base tables, plus the query-to-view table/lineage mapping — which is planner-catalog state, not plan semantics: RuleScript rules are self-contained before/after pairs and QED decides equivalence for every instantiation of their symbols with no premise/assumption channel (table annotations are only keys and single-table "guaranteed" predicates), so any encoding that reads the MV as a separate uninterpreted table is unprovable (QED would treat the MV as an arbitrary relation unrelated to the query), and the only provable degenerate form — inlining the MV's definition, which collapses the rule to the filter-splitting identity π(σ_P(A)) ≡ π(σ_{Q∧P}(A)) ∪ π(σ_{¬Q∧P}(A)) — contains no materialized view at all and hence does not express this rule. (The porter's UNSUPPORTED line was actually emitted by an LLM context-length HTTP error rather than a technical conclusion, but the conclusion itself holds on the merits.) ```

### `MaterializedViewProjectJoin` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewProjectJoinRule.java
- Attempts used: 30
- Last updated: 2026-09-23T10:00:41.613712+00:00
- Reason / notes: MaterializedViewProjectJoinRule is a view-matching substitution, not an algebraic identity: it replaces a Project(Join(...)) query with a scan of a registered materialized view (or, with generateUnionRewriting, a Union of that view with incremental update deltas), so its soundness rests on the view being *defined* as a query over the same base tables plus MV freshness/delta semantics. RuleScript tables are independent uninterpreted symbols — nothing in the language (or even the JSON schema's "guaranteed" constraints, which are row-level predicates over a table's own columns) can express that one table's contents equal an expression over other tables — and QED's bag-semantics model has no notion of temporal deltas, so for arbitrary instantiations the MV scan is just an unconstrained relation and no before/after encoding could ever be proven equivalent.

### `MinusToDistinct` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MinusToDistinctRule.java
- Attempts used: 30
- Last updated: 2026-09-22T22:51:00.083616+00:00
- Reason / notes: The rule's correctness rests on the algebra of COUNT: the post-rewrite plan's `count_0 > 0` and `count_i = 0` tests are what implement the "present in the first operand / absent from every other" membership test of a set difference, so proving equivalence requires knowing that a group's count is 0 iff no row of that group exists in the corresponding input (and > 0 iff it does) — an aggregate algebraic identity QED explicitly does not have (it only knows bag equality of aggregate inputs). Since the before pattern (distinct MINUS) contains no aggregate at all, there is no bag-equal counterpart aggregate on the other side for the prover to equate against, so the SMT decision procedure has nothing to bridge the count-based membership test with EXCEPT semantics. The DSL gaps (no filtered aggregate call in `AggCall`, no literal-constant projection or equality Rex to build the branch-index labeling) are secondary: extending the DSL cannot help because the fixed Rust prover would still lack COUNT's semantics to decide the filter conditions. ```

### `MinusToFilter` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MinusToFilterRule.java
- Attempts used: 30
- Last updated: 2026-09-22T13:39:17.224120+00:00
- Reason / notes: The rewrite's correctness depends on `NOT Q` being the exact complement of `Q` (i.e., rows with `Q = NULL` appearing on neither side), but QED models uninterpreted predicates as 3-valued SQL predicates and universally quantifies over all instantiations, including null-returning ones — so rows with `Q = NULL` remain in the set-minus input but are dropped by `Filter(NOT Q)`, a counterexample no re-encoding can avoid. This is confirmed by the porter's discriminating test (the single-filter variant `MINUS(base, Filter(Q,base))` ⟹ `Distinct(Filter(NOT Q,base))` failed), and it cannot be fixed via `extend_dsl_file`: predicate nullability is not even serialized into the prover's JSON (operator types lose nullability in `JSONSerializer.type()`), so no RuleScript encoding can declare a predicate total, and the QED prover itself is off-limits.

### `ProjectJoinJoinRemove` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectJoinJoinRemoveRule.java
- Attempts used: 60
- Last updated: 2026-09-22T11:03:48.203341+00:00
- Reason / notes: The rule is valid only because the bottom join's condition is an equi-join on Y's unique key, guaranteeing at most one Y row per X row — but QED models join conditions as uninterpreted predicates and cannot infer that selectivity from the unique-key constraint (predicate inference/entailment it explicitly cannot do). A constant-true join predicate is a counterexample that doubles the output multiplicity, so no correct encoding (even one reusing the same predicate symbol and building the top-join condition over only the non-Y columns) is provable; this is a genuine QED limitation, not a missing DSL shape the porter failed to find. ```

### `ProjectJoinRemove` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectJoinRemoveRule.java
- Attempts used: 90
- Last updated: 2026-09-22T10:13:01.588724+00:00
- Reason / notes: The rule's correctness rests on the join condition being an equality on the non-preserved side's key columns, which lets the key constraint bound join fanout to ≤1; RuleScript's core language has no equality operator, so any join condition is an uninterpreted predicate, and with an uninterpreted condition the equivalence is actually false (a left row can match multiple distinct rows of the keyed table, duplicating it), and QED cannot infer an entailment between an independent predicate symbol and the table's key constraint. Thus no encoding — including one that correctly sets the scan's `unique` flag and shares symbols — is provable; the porter's LLM context-length error merely masked a conclusion its transcript was already converging on (QED never even reached SMT, rejecting structurally).

### `ProjectMeasure` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
- Attempts used: 30
- Last updated: 2026-09-23T10:29:01.474001+00:00
- Reason / notes: ProjectMeasureRule's soundness rests on Calcite's measure-calculus law — that SINGLE_VALUE(M2X(M2V(E), SAME_PARTITION(g))) aggregated over a g-partition collapses to the measure value E (e.g. SUM(c)+1) computed over that partition — which is a bespoke internal semantic identity, not a bag-semantic equivalence. QED treats M2V/M2X/V2M/SAME_PARTITION and the aggregates as uninterpreted symbols with only functional-consistency, so it cannot see through or relate them (an adversary can instantiate M2X to a constant to break it), and the before-plan's measure-bearing Project has no clean per-row core-language interpretation to model in the first place. This is a genuine QED limitation (uninterpretable measure/aggregate algebra) rather than a missing builder that extend_dsl_file could close, so no full or narrowed faithful encoding is provable.

### `ProjectMultiJoinMerge` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectMultiJoinMergeRule.java
- Attempts used: 30
- Last updated: 2026-09-23T12:20:45.885129+00:00
- Reason / notes: Calcite's `ProjectMultiJoinMerge` only enriches a `MultiJoin` with `projFields` planner metadata while leaving the logical relation unchanged, and RuleScript/QED has no `MultiJoin` operator or metadata side channel in its bag-semantic model. A faithful encoding would either collapse to a trivial identity or require QED to reason about Calcite-internal `MultiJoin` semantics it does not model, so no nontrivial proof of this rewrite is possible.

### `ProjectOverSumToSum0` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectOverSumToSum0Rule.java
- Attempts used: 18
- Last updated: 2026-09-23T00:02:22.990569+00:00
- Reason / notes: The rule's subject — `SUM(x) OVER <frame>` vs `SUM0(x) OVER <frame>` inside a project — is inherently a windowed aggregate, and QED's theory has no window/frame/ordering semantics at all (no window node in its Q-expression format, and a Calcite `RexOver` would mis-serialize as a plain scalar `RexCall` with partition/order/frame silently dropped), so the rule's shape cannot be faithfully encoded, not merely narrowed. The residual content — `SUM` ≡ `SUM0` — is an aggregate null/empty-handling algebra identity QED cannot know (aggregates are uninterpreted beyond input bag equality, and the identity is in fact false over a general bag), which the porter's frameless probe empirically confirmed with a clean, complete `provable: false` rather than a symbol-sharing bug. These are fundamental QED limitations (no list/ordering semantics for Window; no aggregate-function algebra), not a missing DSL builder that `extend_dsl_file` could close.

### `ProjectReduceExpressions` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java

Note: ReduceExpressionsRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectReduceExpressionsRule` variant (not CalcReduceExpressionsRule, FilterReduceExpressionsRule, JoinReduceExpressionsRule, WindowReduceExpressionsRule, which are separate rules ported under their own spec names).
- Attempts used: 30
- Last updated: 2026-09-23T11:37:57.505236+00:00
- Reason / notes: ProjectReduceExpressions works by *evaluating* constant subtrees (e.g. 1+2→3, redundant CAST(x AS T)→x) using a RexExecutor, but QED models every such operator (arithmetic, CAST, CASE, etc.) as an uninterpreted symbol and only proves equivalences valid for *all* instantiations — it has no knowledge of any specific operator's algebra, so it cannot verify that f(const-args) folds to a literal. The porter's stated reason was actually an LLM context-length HTTP 400 (it never wrote the file or ran QED), not a technical judgment, but the UNSUPPORTED conclusion is nonetheless correct: constant folding / redundant-cast removal fundamentally depends on operator-specific internal semantics that QED cannot see through, so no non-trivial special case of this rule is provable. ```

### `ProjectToLogicalProjectAndWindow` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectToWindowRule.java

Note: ProjectToWindowRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectToLogicalProjectAndWindowRule` variant (not CalcToWindowRule, which are separate rules ported under their own spec names).
- Attempts used: 9
- Last updated: 2026-09-23T11:03:55.831842+00:00
- Reason / notes: The rule's entire semantic content is that a windowed aggregate (RexOver) evaluated inline inside a Project yields the same per-row values as the same aggregate evaluated by a separate LogicalWindow node over the same input — i.e., partition/ordering-dependent window semantics, which QED explicitly does not model (Window has no bag-semantic meaning, and QED knows nothing about an aggregate's algebra beyond bag equality of its input). Any encoding in the core language either desugars the window on both sides (self-join back to a group-by), making before and after the identical tree so the proof is vacuous, or relates an opaque uninterpreted window function to a group-by/join construction, which QED cannot derive for uninterpreted symbols. This is a fundamental limitation on the prover side (no window semantics in the QED backend), not a missing DSL builder, so extend_dsl_file cannot close the gap and no non-trivial narrower special case of the rule's content is provable. ```

### `ProjectToSemiJoin` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRule.java

Note: SemiJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectToSemiJoinRule` variant (not AggregateToSemiJoinRule, JoinOnUniqueToSemiJoinRule, JoinToSemiJoinRule, which are separate rules ported under their own spec names).
- Attempts used: 60
- Last updated: 2026-09-23T13:07:53.473300+00:00
- Reason / notes: The rule's validity rests on the right-side aggregate grouping by exactly the join keys, so its output is unique per key value — which is precisely what turns the inner join into a semi-join over the pre-aggregate input. QED's stated limitation is that it "knows nothing about a specific aggregate function's algebra beyond bag equality of its input": it cannot see through an aggregate to equate its grouped, unique-on-keys output with a semi-join over the raw input relation, and that is exactly the reasoning step this rule requires (the null hypothesis the porter chased is a red herring — both the inner-join-with-aggregate and the semi-join drop null-key rows identically). The porter's encoding was structurally sound (aggregate + inner join in before, semi-join in after, shared projection symbol over the same left columns) yet still returned a clean "not provable" with no panic or parse error, so the blocker is this fundamental aggregate-modeling gap in QED rather than a fixable symbol-sharing or nullability bug, and the DSL already exposes both `aggregate` and `JoinRelType.SEMI` so it is not a closable DSL-side gap either. ```

### `ProjectWindowTranspose` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectWindowTransposeRule.java
- Attempts used: 12
- Last updated: 2026-09-23T12:07:17.628951+00:00
- Reason / notes: The rule's correctness rests entirely on Window semantics (one output row per input row, partition/order/frame-dependent values), but QED — the unmodifiable prover — has no window model and its JSON input language has no Window node (JSONSerializer's switch has no LogicalWindow case), so no faithful encoding exists. The only apparent special case (partition-wide aggregation via join+group-by) also fails: reconstructing per-row aggregate values requires an equi-join, but the DSL only offers uninterpreted join conditions with no equality operator, so the two sides' joins cannot be related. Substituting an Aggregate would collapse each partition to one row (wrong bag semantics) and prove only an unrelated column-pruning fact, and treating the window as an opaque scan discards the very relation the rule establishes (that the recomputed window over trimmed input matches the original).

### `ReduceDecimals` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceDecimalsRule.java
- Attempts used: 30
- Last updated: 2026-09-23T12:10:46.446504+00:00
- Reason / notes: ReduceDecimals is a representation-level decimal expansion rule whose correctness depends on Calcite’s decimal scale/precision, REINTERPRET/decode/encode, casts, rounding, and numeric operator semantics. QED models such backend-specific operators only as uninterpreted symbols and can only prove bag-semantic equivalence under that abstraction, so it cannot validate the decimal-to-bigint/double rewrites.

### `SampleToFilter` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SampleToFilterRule.java
- Attempts used: 13
- Last updated: 2026-09-23T13:57:18.351374+00:00
- Reason / notes: The rule's only claimed equivalence, Sample(R) ≡ Filter(rand() < rate, R), is a probabilistic (Bernoulli) identity — both sides keep rows nondeterministically — while QED is a deterministic decision procedure over bag semantics with uninterpreted functions, in which Sample has no bag meaning at all (same no-semantics class as Sort/Limit/Window) and rand() admits no deterministic function-of-input reading. The two sides can therefore only be modeled as independent uninterpreted symbols (Sample-as-operator vs Filter(P,R), or P vs Q), whose equality QED cannot infer — the porter's probe confirmed this is unprovable — and extend_dsl_file cannot close the gap because no deterministic semantics exists to attach that would not simply hardcode the conclusion rather than prove it. ```

### `SemiJoinRemove` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRemoveRule.java
- Attempts used: 60
- Last updated: 2026-09-23T13:29:48.499483+00:00
- Reason / notes: `SemiJoinRemove` rewrites `X SEMI JOIN Y ON c` to `X`, but a semi-join is an existence filter of X by Y, so this is not a bag-semantic equivalence: it is false in general (e.g., when Y is empty the semi-join result is empty while X is not), and QED decides universal equivalence over every instantiation of the uninterpreted symbols, so no encoding of "drop the semi-join" can be provable. The rule's real precondition — that the semi-join is "advisory," a global, context-dependent planner property that dropping it cannot change the query answer — has no local expression in RuleScript's before/after pattern language and no side-condition mechanism in QED's semantics; even the narrowest special case (e.g., a true-conditional semi-join) still degenerates to empty when the right side is empty, so no provable special case exists, and the porter's abandonment (caused here by an LLM context-length crash rather than a reasoned analysis) happens to land on the correct conclusion.

### `SortJoinCopy` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortJoinCopyRule.java
- Attempts used: 30
- Last updated: 2026-09-23T13:01:34.247399+00:00
- Reason / notes: The only semantic content of SortJoinCopy is ordering — it rewrites Sort(collation[, offset, fetch])(Join(L, R, cond)) by adding decomposed/shifted sorts to the join inputs, and its correctness is the claim that the final sorted row order (and which rows survive the offset/fetch) is preserved. QED decides equivalence under bag semantics and explicitly does not model row order (Sort/Limit/Offset/Window/Sample have no meaning there), so under the only semantics QED can adjudicate both sides are just the same join over the same bags and the best achievable "proof" would be a vacuous identity rather than an encoding of the rule. (Note the porter's recorded termination reason was an LLM context-limit crash, not a QED verdict, but the diagnosis the porter reached in-transcript — that the ordering dependence is a fundamental QED limitation — is correct, and no non-vacuous bag-level special case exists to salvage.) ```

### `SortJoinTranspose` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortJoinTransposeRule.java
- Attempts used: 30
- Last updated: 2026-09-23T15:15:01.106490+00:00
- Reason / notes: SortJoinTranspose's soundness rests entirely on ordering semantics — pushing a Sort's top-(offset+fetch) down through an outer join so the surviving rows still cover the outer Sort/Offset/Fetch slice — and QED explicitly does not model list/ordering semantics (Sort/Limit/Offset have no bag-semantic meaning), and the core DSL exposes no Sort builder at all. Under pure bag semantics the two sides are not even equivalent, because the inner fetch drops rows out of the join input (e.g. a LEFT join loses unmatched-key rows from the other side entirely), so no bag-equivalence proof can exist for the real rule. The only bag-valid variant — a plain sort with no fetch/offset that drops nothing — reduces both sides to the same join bag and would be a vacuous tautology, not a genuine port of the rule. ```

### `SortMerge` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortMergeRule.java
- Attempts used: 30
- Last updated: 2026-09-23T13:40:20.769352+00:00
- Reason / notes: SortMerge's entire correctness claim is limit/sort composition — LIMIT_T over (SORT_B, LIMIT_B over X) rewriting to (SORT_B, LIMIT min(T,B) over X) — which requires modeling ordering and top-N row selection, semantics QED explicitly does not have (Sort/Limit/Offset/Window/Sample have no bag-semantic meaning). The core DSL exposes no sort/limit operator at all (RelRN has only scan/filter/project/join/set-ops/aggregate), so the pattern cannot even be written faithfully; adding one would not help, since QED would reduce both sides to the same underlying bag and the literal fetches plus the min(T,B) computation (done at rule-fire time in Java, not in the plan) would remain unexpressible and unprovable. (Note: the porter's logged "reason" was an LLM context-length API error, but its transcript shows it had already converged on exactly this limitation before failing, so the UNSUPPORTED conclusion is substantively correct.) ```

### `SortProjectTranspose` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortProjectTransposeRule.java
- Attempts used: 30
- Last updated: 2026-09-23T13:08:53.917927+00:00
- Reason / notes: The entire semantic content of SortProjectTranspose is row-ordering: the sort must be remapped through the projection's collation (with offset/limit carried along), which is only valid under structural side conditions — every sort key maps to a plain input reference or a monotonic cast — linking collation fields to specific projection expressions. QED decides bag-semantic equivalence only, where Sort/Limit/Offset have no meaning (the prover models them as identities, and the DSL doesn't even expose a sort builder), so any encoding would either be vacuously "provable" while certifying nothing about ordering, or unable to express the collation↔projection linkage at all. The porter's stated reason was an infrastructure failure (LLM HTTP 400 context-length error, no attempt ever made) rather than an analysis — but the conclusion happens to be correct, since ordering semantics are a fundamental QED limitation per the reference, not a DSL gap closable via extend_dsl_file. ```

### `SortRemove` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveRule.java
- Attempts used: 30
- Last updated: 2026-09-22T01:36:57.264726+00:00
- Reason / notes: SortRemove is fundamentally a row-ordering rule: its subject operator (a Sort with no offset/limit) and its defining precondition (the input already satisfies the sort's collation, i.e. RelCollation trait preservation) both live in list/ordering semantics, which QED's bag-semantic model explicitly does not model — Sort/Limit/Offset/Window/Sample have no bag-semantic meaning — and no bag-level property can stand in for "input is sorted", so any encoding that drops the Sort node or the sortedness condition ceases to be this rule. Extending the DSL (e.g. adding a sort builder, which the JSON serializer could even carry) cannot close the gap because the missing semantics are on the QED prover side, not the DSL side, so UNSUPPORTED is the correct conclusion even though the porter's stated reason was just an LLM context-length error rather than that analysis. ```

### `SortRemoveConstantKeys` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveConstantKeysRule.java
- Attempts used: 9
- Last updated: 2026-09-23T15:22:55.001936+00:00
- Reason / notes: The rule's entire content is order-preserving — it drops (or deletes) sort keys whose columns are constant so the emitted row *sequence* is unchanged — but QED's bag model gives Sort no ordering (a Sort is bag-identical to its input), so any before/after pair collapses to the vacuous R = R and certifies nothing about the actual sort order. Compounding this, the rule's precondition (a key column is constant) is read from RelOptPredicateList.constantMap, i.e. it is predicate-inference over the input's pulled-up predicates, which QED explicitly cannot perform between independent uninterpreted symbols. Both are fundamental limitations of the prover's model (not missing DSL builders that extend_dsl_file could fix), so the porter's UNSUPPORTED call is correct. ```

### `SortRemoveDuplicateKeys` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveDuplicateKeysRule.java
- Attempts used: 30
- Last updated: 2026-09-23T13:48:27.405156+00:00
- Reason / notes: The rule's only nontrivial content is at the sequence level: both sorts emit every input row exactly once, so bag-equivalence holds unconditionally, and what actually needs justification is that collation [k1, k2] induces the same row order as [k1], which follows only from the k1-determines-k2 functional dependency. QED does not model list/ordering semantics — Sort has no bag-semantic meaning in its Q-expression translation, per its own stated limitations — so any encoding (including one via a hypothetical sort builder added to RelRN, whose JSON the serializer already carries) would either be rejected by the prover or degenerate into the vacuous, unconditionally-true identity "sort ≡ its input" that holds for any two collations and verifies none of the rule's actual claim. This is a genuine QED limitation (correctness resting on row order), not a DSL gap to close with extend_dsl_file, so UNSUPPORTED is the correct conclusion even though the porter's stated reason was an API error rather than this analysis.

### `SortRemoveRedundant` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveRedundantRule.java
- Attempts used: 30
- Last updated: 2026-09-23T13:24:44.906498+00:00
- Reason / notes: This rule rewrites `Sort` nodes in both their ORDER BY and LIMIT forms, and QED explicitly does not model list/ordering semantics — `Sort`/`Limit`/`Offset` have no bag-semantic meaning — so the core claims (an ORDER BY is removable when the input has ≤1 row; a LIMIT is removable when the input has ≤fetch rows) are about row order and list truncation, which the prover cannot decide. Adding a `Sort`/`sortLimit` builder to `RelRN` via `extend_dsl_file` would not close the gap, since the limitation is in the prover's semantics rather than the DSL surface, and the JSON serializer's `LogicalSort` support is only about emitting plans QED still cannot reason about. Independently of that, the rule is not a universal equivalence: its validity rests on the optimizer-metadata side condition "input's max row count ≤ threshold," which cannot be expressed as a precondition on uninterpreted relations in RuleScript, so even the special cases (e.g. over a group-less aggregate) cannot be faithfully stated and proved.

### `SortUnionTranspose` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortUnionTransposeRule.java
- Attempts used: 30
- Last updated: 2026-09-23T15:32:11.063131+00:00
- Reason / notes: SortUnionTranspose's correctness rests entirely on top-N ordering semantics — pushing a Sort(offset O, fetch F) into each UNION ALL branch as a Sort(fetch O+F) and re-sorting only holds because of the sorted top-(O+F) argument. QED explicitly does not model list/ordering semantics (Sort/Limit/Offset have no bag-semantic meaning), and the RuleScript core language exposes no Sort/Limit/Offset operator at all, so the rule cannot even be expressed, let alone genuinely verified; extending the DSL with a Sort builder would only make both sides collapse to the same union (a vacuous proof), not an actual check of the top-N push-down. ```

### `TableScan` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/TableScanRule.java
- Attempts used: 13
- Last updated: 2026-09-23T13:33:24.157908+00:00
- Reason / notes: TableScanRule's only non-trivial case (view expansion via `table.toRel`) asserts that a scanned table's contents equal the relational plan stored in its table metadata, and QED models every scan as an independent uninterpreted bag — RuleScript/JSON has no construct tying a table symbol to a defining plan (the "guaranteed" field carries only row-level predicates, not bag equality), so the required equivalence is fundamentally inexpressible/invisible to the prover, not a fixable symbol-sharing or DSL gap. The porter's concrete non-trivial encoding returned a definitive not-provable (complete fragment, no SMT timeout), and the only provable instance is the vacuous identity rewrite scan→scan, which captures none of the rule's actual transformation. ```

### `UnionToValues` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/UnionToValuesRule.java
- Attempts used: 13
- Last updated: 2026-09-23T15:48:48.067587+00:00
- Reason / notes: UnionToValues is a constant-folding rule whose operand definition (a Union whose inputs are all Values) gives it no free relational or predicate symbols, and in the JSON/DSL model a `values` node is a closed bag of concrete literal rows — the DSL only exposes the empty values, and even a hypothetical `values(rows)` builder could only fix one ground instance, leaving nothing relational to universally quantify over. The rule's soundness rests on the engine-internal invariant that a Values computes exactly the bag of its hard-coded tuple list and that the rule's list concatenation/deduplication preserves it — a backend operator's bespoke internal semantics that QED takes as an axiom of its model rather than something it can verify, so any encodable "proof" would be a single closed bag equality (or, at best, a scalar-constant bag-identity checking QED's own model consistency) rather than a genuine proof of the rewrite.

### `WindowReduceExpressions` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java

Note: ReduceExpressionsRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `WindowReduceExpressionsRule` variant (not CalcReduceExpressionsRule, FilterReduceExpressionsRule, JoinReduceExpressionsRule, ProjectReduceExpressionsRule, which are separate rules ported under their own spec names).
- Attempts used: 30
- Last updated: 2026-09-23T15:56:08.996853+00:00
- Reason / notes: WindowReduceExpressions rewrites a Window's spec (constant-folding aggregation operands, dropping constant partition keys, dropping constant order keys), and its correctness rests on two things QED fundamentally lacks: window semantics (partition/order/frame-dependent values are not bag operations, and the DSL and serializer have no Window node to begin with — a listed QED limitation, not a closable DSL gap) and predicate-derived constant inference (QED cannot infer from an uninterpreted predicate that a filtered column takes a single value, and it knows no algebra for the concrete operators constant-folding relies on). Even the narrowest bag-encodable special case — a whole-partition window written as a group-by aggregate joined back to the input — still requires QED to prove a partition key is constant under a pulled-up filter, which is exactly the predicate entailment QED cannot do, leaving only a trivial no-op "rule" with identical before/after.

