# RuleScript porting progress

_Last updated: 2026-10-12T15:46:19.978586+00:00_

**63/103 rules proved** (1 failed, 39 skipped as out of QED's supported fragment).

| Rule | Backend | Status | Scope | Attempts | Notes |
|---|---|---|---|---|---|
| `BooleanSimplification` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 7 | The encoding faithfully captures the "common factor elimination for conjunction" sub-case of Spark's BooleanSimplification — `(A∨B) ∧ (A∨... |
| `CollapseProject` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 5 | The encoding is a faithful, honestly-tagged special case of CollapseProject's core branch: `before()` (nested `Project([P0(c0,c1),P1(c0,c... |
| `CollapseRepartition` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 7 | The encoding is faithful: each Repartition is modeled as an identity projection, which is exactly the bag-semantic content of a shuffle n... |
| `CollapseWindow` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 49 | The encoding faithfully captures CollapseWindow's core partition-determined subcase: each whole-partition-frame window is modeled as an u... |
| `ColumnPruning` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 6 | before() and after() are genuinely structurally different (the inner Project emits 4 uninterpreted columns vs. 2), and the symbol sharing... |
| `CombineTypedFilters` | Apache Spark Catalyst | ✅ PROVED | FULL | 3 | `before()` (two nested `Filter`s with independent uninterpreted predicates `inner`/`outer` over a shared, universally quantified source) ... |
| `CombineUnions` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 4 | The encoding is a faithful fixed-shape instance of Spark's rule: running Spark's stack algorithm on exactly this input (Distinct over Uni... |
| `ConstantFolding` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 41 | The proof is not vacuous — before() contains a real AND node (`true ∧ false`) that after() eliminates, which is precisely the literal-eva... |
| `ConstantPropagation` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 4 | before() and after() genuinely differ (g(a) vs g(c) on the two distinct join fields), so the proof is non-vacuous: QED must use congruenc... |
| `ConvertToCatalyst` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 24 | The encoding faithfully captures the core transformation of `ConvertToCatalyst`'s `convertOperator` with-columns path: materializing a sh... |
| `ConvertToLocalRelation` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 31 | before() (a Project over a values relation) and after() (the folded values relation) are structurally distinct, so the proof is a genuine... |
| `CostBasedJoinReorder` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 7 | It is a faithful, non-vacuous instance — `before()` is left-deep ((A⋈B)⋈C)⋈D and `after()` is bushy (A⋈B)⋈(C⋈D); the four independent uni... |
| `EliminateAggregateFilter` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 30 | `before()` and `after()` differ exactly by the constant-true filter feeding the aggregate, which is the precise semantic content of the r... |
| `EliminateDistinct` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 6 | `before()` and `after()` are structurally distinct (they differ only in the `distinct` flag of the `AggCall` for the shared uninterpreted... |
| `EliminateMapObjects` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 7 | The encoding is faithful: `MapObjects(λv. v, input, None)` is semantically an identity transformation on its input, and the porter models... |
| `EliminateOuterJoin` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 4 | The encoding is non-vacuous (before = FULL outer join, after = INNER join with the identical filter left in place, exactly the source rul... |
| `ExtractGroupingPythonUDFFromAggregate` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 7 | The encoding correctly models the rule's core transformation—pulling an uninterpreted UDF g out of a grouping expression into a Project b... |
| `ExtractPythonUDFFromJoinCondition` | Apache Spark Catalyst | ✅ PROVED | FULL | 5 | The encoding is non-trivial (before is an INNER join with conjunctive condition `and(udf, rest)`; after is an INNER join with condition `... |
| `ExtractPythonUDFs` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 10 | The encoding faithfully captures the core semantic transformation of ExtractPythonUDFs for its declared partial scope: a UDF computation ... |
| `FinishAnalysis` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 8 | FinishAnalysis as a whole is a composite of ~19 heterogeneous rewrites (several members — ComputeCurrentTime, InsertMapSort*, subquery/vi... |
| `FoldablePropagation` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 6 | The encoding faithfully captures the core constant-substitution step of Spark's FoldablePropagation: a Project introduces a 0-arity (fold... |
| `GenerateOptimization` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 45 | The encoding faithfully captures the minimal non-trivial case of the JsonTuple pruning branch: a row-preserving generate with 2 uninterpr... |
| `GroupBasedRowLevelOperationScanPlanning` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 6 | before() and after() are genuinely structurally different (the target-only conjuncts live inside the join condition vs. in a filter above... |
| `InferFiltersFromConstraints` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 23 | The encoding is a non-trivial, honest PARTIAL special case of Spark's `InferFiltersFromConstraints`: it correctly models the core mechani... |
| `NullDownPropagation` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 26 | before() and after() are structurally and semantically distinct (Filter(IS_NULL(LT(x,y))) vs Filter(OR(IS_NULL(x), IS_NULL(y)))) over two... |
| `NullPropagation` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 26 | The encoding is a faithful, non-vacuous instance of the source rule's `IsNull(c) if !c.nullable → Literal(false)` branch: the non-nullabl... |
| `ObjectSerializerPruning` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 7 | The encoding faithfully captures the rule's unconditional core rewrite — `Project(_, SerializeFromObject)` where the outer project uses o... |
| `OptimizeIn` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 9 | The before() (filter `Or(eq, eq)`) and after() (filter `eq`) are genuinely distinct shapes, so the proof is non-vacuous; the single share... |
| `OptimizeJoinCondition` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 26 | The encoding proves exactly the rule's semantic core — `x = y OR (x IS NULL AND y IS NULL)` ≡ `x IS NOT DISTINCT FROM y` (Spark's `EqualN... |
| `OptimizeOneRowPlan` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 29 | `before()` (group-only `Aggregate` over the scan) and `after()` (`Project` of the key column) are structurally different, and the equival... |
| `OptimizeOneRowRelationSubquery` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 48 | The encoding faithfully captures the uncorrelated scalar-subquery branch's relational-algebra shape (inner cross-join with a one-row rela... |
| `OptimizeUpdateFields` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 64 | The encoding faithfully models the source rule's dedup case for its declared instance: each WithField is denoted by a projection whose va... |
| `PartitionPruning` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 89 | The encoding faithfully abstracts DPP's semantic effect — the inserted pruning filter keeps exactly the build rows whose join-key exists ... |
| `PlanExternalUDFs` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 22 | The encoding is non-vacuous and faithful to the rule's core `extract` transformation: `before()` (Project computing f(x) inline alongside... |
| `PropagateEmptyRelation` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 8 | `before()` (an INNER join with an empty left input) and `after()` (an independent empty relation of the join's output schema `l0,l1,r0,r1... |
| `PruneFileSourcePartitions` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 11 | `before()` (Project(π, Filter(P∧D, R))) and `after()` (Project(π, Filter(P∧D, Filter(P, R)))) are structurally different, so the proof is... |
| `PruneFilters` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 22 | before() and after() are structurally different in exactly the way Spark's rewrite acts — Filter(And(shared, restOuter), Filter(And(share... |
| `PullOutNestedDataOuterRefExpressions` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 25 | The encoding faithfully captures the rule's essential transformation with correct symbol sharing: before() uses the outer-only map-extrac... |
| `PullOutVariantExtractions` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 8 | The encoding faithfully captures the Aggregate case of the rule: `before` aggregates `f(ve(v))` directly over the scan, while `after` fir... |
| `PullUpProjectAliasThroughWindow` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 6 | The encoding is non-trivial (before has a 3-col bottom project + identity top project; after has a 2-col bottom project + top project com... |
| `PullupCorrelatedPredicates` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 30 | The encoding is a faithful, honestly-scoped PARTIAL capture of the rule's core transformation for the Exists branch: `before()` places th... |
| `PushDownJoinThroughUnion` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 22 | The encoding is a faithful, non-vacuous proof of a genuine special case of the Spark rule: `before` is `(X ∪+ Y) ⋈_LEFT O` and `after` is... |
| `PushDownLeftSemiAntiJoin` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 8 | The encoding is non-vacuous (before = SemiJoin(Project(X),Y) vs after = Project(SemiJoin(X,Y))) and semantically correct: because P0/P1 a... |
| `PushDownPredicates` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 22 | The encoding is a faithful, non-vacuous fragment of Spark's dispatcher: `Filter(left_only, L INNER join R on join_cond)` vs. `L filter le... |
| `PushExtraPredicateThroughJoin` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 40 | The encoding faithfully captures the rule's core semantic move — side-specific predicates that are implied by the join condition (p on L ... |
| `PushLeftSemiLeftAntiThroughJoin` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 24 | The encoding faithfully captures the TO_LEFT_BRANCH, INNER-child-join, LeftSemi (not anti) special case of Spark's rule: before is SEMI(I... |
| `PushPredicateThroughJoin` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 5 | before() (a top-level Filter of three distinct uninterpreted conjuncts over an INNER join with its own uninterpreted join condition) and ... |
| `PushPredicateThroughNonJoin` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 7 | The encoding faithfully models the rule's projection case (the avoid-double-filter-eval branch with no projection split): before is Filte... |
| `PushProjectionThroughUnion` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 7 | The encoding is structurally non-vacuous (Project(G, L ∪ R) vs Union(Project(G,L), Project(G,R))) and faithfully models the rule's core c... |
| `PushVariantIntoScan` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 25 | The encoding faithfully captures the core semantic transformation of PushVariantIntoScan in a narrow but non-degenerate form: before() pl... |
| `PushdownPredicatesAndPruneColumnsForCTEDef` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 21 | The encoding captures the rule's actual soundness core: the disjunction of the reference predicates is pushed into the shared CTE definit... |
| `RemoveLiteralFromGroupExpressions` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 63 | The encoding is non-trivial and faithful to the rule's core transformation: it removes a constant (uninterpreted 0-ary projection `c`) fr... |
| `RemoveNoopOperators` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 5 | The encoding is non-vacuous (before() contains a Project node that after() lacks), correctly captures the core no-op-Project semantic (id... |
| `RemoveNoopUnion` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 11 | before() = Distinct(A UNION ALL A) and after() = Distinct(A) are structurally different yet genuinely equivalent (group-by-all dedup coll... |
| `RemoveRedundantAggregates` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 24 | The encoding faithfully captures Case 1 (`isLowerRedundant`): a group-only upper aggregate whose single group key is `lower.field(0)=k1` ... |
| `RemoveRepetitionFromGroupExpressions` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 8 | The proof is non-vacuous: before() is a 3-key aggregate over a projection (with a trailing projection dropping the duplicate key) while a... |
| `ReorderJoin` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 26 | The encoding faithfully traces Spark's createOrderedJoin algorithm for a specific 4-way input (A,D,C,B) with conditions {pAB, pCD, qDBC}:... |
| `ReplaceCTERefWithRepartition` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 7 | The only bag-level content of ReplaceCTERefWithRepartition is that a CTERelationRef transparently denotes the CTE definition's bag and th... |
| `ReplaceDistinctWithAggregate` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 5 | The encoding is faithful and non-vacuous: `before()` (set-variant self-intersection, i.e. set(R)) and `after()` (GROUP BY on all columns ... |
| `ReplaceExceptWithAntiJoin` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 11 | The encoding faithfully models Spark's rule for a 2-column instance: `a.minus(false,b)` is the set-difference `EXCEPT` (matching `Except(... |
| `ReplaceExceptWithFilter` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 53 | The encoding faithfully mirrors the rule's core: `Except(left,right,false)` → `minus(false, …)` on a shared base (matching the rule's "sa... |
| `ReplaceIntersectWithSemiJoin` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 25 | The encoding faithfully mirrors every operator choice in the rule — `intersect(false)` for INTERSECT DISTINCT, `JoinRelType.SEMI` for Lef... |
| `ReplaceNullWithFalseInPredicate` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 23 | before() (Filter(And(p, NULL), R)) and after() (Filter(And(p, FALSE), R)) are structurally distinct, and the proof is of the genuine univ... |
| `OptimizeExpand` | Apache Spark Catalyst | ❌ FAILED | — | 77 |  |
| `CheckCartesianProducts` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 10 | `CheckCartesianProducts` is a pure validation rule — its only effect is to `throw` a compilation error on a detected Cartesian join, so i... |
| `CleanUpTempCTEInfo` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 23 | The only difference between before and after is the value of `originalPlanWithPredicates` on `CTERelationDef`, a pass-through node whose ... |
| `CleanupDynamicPruningFilters` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 49 | The rule's only plan-level move is deleting a DynamicPruning conjunct (semantically `key IN <dynamically computed value set>`) on the gro... |
| `CollapseGroupedSumOfCount` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 41 | QED treats aggregate functions (COUNT, SUM, MAX, MIN) as uninterpreted symbols and can only equate aggregate calls via per-group input ba... |
| `CombineApproximatePercentiles` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 23 | The rewrite's only non-trivial step replaces two scalar `approx_percentile` calls with one fused array-valued call plus `GetArrayItem` ex... |
| `CombineConcats` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 29 | CombineConcats is a purely scalar rewrite whose soundness rests exactly on algebraic properties of `concat` (associativity/flattening) an... |
| `DecimalAggregates` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 24 | Every arm is sound only by numeric facts about decimal types — a scale-preserving widening cast is value-identical/monotone (so it commut... |
| `EliminateLimits` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 7 | All three families of EliminateLimits depend on semantics QED explicitly lacks: the elimination case needs a `maxRows` cardinality upper ... |
| `EliminateOffsets` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 21 | All three subcases of EliminateOffsets are axioms about row-ordering semantics — skipping the first n rows in order, producing an empty r... |
| `EliminateSerialization` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 10 | Every branch of EliminateSerialization is sound only because a deserializer is the inverse of its matching serializer (deser(ser(x)) = x)... |
| `EliminateSorts` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 47 | Every branch of EliminateSorts — subsumption of lower sorts under a global sort, joins re-ordering/destroying input order, sorts below or... |
| `EliminateWindowPartitions` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 24 | QED has no Window operator and models aggregation opaquely per key tuple, so it cannot relate the per-partition groupings a spec induces ... |
| `ExtractExternalUDFFromWindow` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 23 | The rule's only semantic content is commuting a row-wise external UDF across the Window boundary (per-row eval(udf(w)) = udf(eval(w))), a... |
| `ExtractPythonUDFFromAggregate` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 27 | The rule's entire content is moving a scalar Python UDF from inside Spark's Aggregate expression list (where Spark permits arbitrary expr... |
| `ExtractPythonUDTFs` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 80 | QED's Q-expression/semiring calculus has no table-generating (row-producing) operator — nothing that, per input row, yields a data-depend... |
| `InferFiltersFromGenerate` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 33 | The rule is sound only because Spark's Explode/Inline generators emit zero rows when the generator input is NULL or an empty array, so ro... |
| `InferWindowGroupLimit` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 25 | The rule's soundness rests entirely on order-dependent semantics that QED by design does not model: `WindowGroupLimit` keeps the first `l... |
| `InjectRuntimeFilter` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 41 | The rewrite's soundness rests on the bloom filter's no-false-negative invariant — `BloomFilterMightContain(agg(right_keys), hash(l.key))`... |
| `InlineCTE` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 41 | CTE inlining is a let-expansion rule (`WithCTE(B, Q(t))` ≡ `Q[B/t]`), but QED's Q-expression language has no let-binding or defined-name ... |
| `LikeSimplification` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 41 | Every branch of LikeSimplification asserts an extensional equivalence between string pattern operators (LIKE 'a%' ≡ StartsWith, LIKE '%s%... |
| `LimitPushDown` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 21 | Every branch of Spark's LimitPushDown relocates or adjusts Limit/LocalLimit/Offset nodes (through Union, through Join, limit-1 through a ... |
| `LimitPushDownThroughWindow` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 11 | The rule's soundness rests on order-dependent facts that no bag-semantic model can express: `Limit(n, Sort(orderSpec))` must retain the *... |
| `MarkSingleTaskExecution` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 7 | MarkSingleTaskExecution makes zero change to relational/bag content — it returns a structurally identical clone with only a physical Tree... |
| `MergeSubplans` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 25 | The rule's core content is a whole-plan common-subexpression elimination that identifies structurally identical but separately-instantiat... |
| `NormalizeFloatingNumbers` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 27 | Both operative sub-rules rest on semantics QED's theory fundamentally cannot express: the Window partition-spec case needs a Window opera... |
| `OptimizeCsvJsonExprs` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 10 | Every sub-rewrite in this bundle depends on a specific algebraic identity between Spark's bespoke JSON/CSV operators — JsonToStructs(Stru... |
| `OptimizeMetadataOnlyDeleteFromTable` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 9 | Both sides of the rewrite are DML statements (row-level ReplaceData/WriteDelta DELETE → metadata-only DeleteFromTableWithFilters/DeleteFr... |
| `OptimizeMetadataOnlyQuery` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 26 | The rule's after-side is a constant `LocalRelation` whose rows are the partition specs pulled from the catalog/file-system, so its soundn... |
| `OptimizeRand` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 48 | Every branch of OptimizeRand is only valid under the semantic axiom that rand() ∈ [0,1) (the folding) or under an equivalence between dis... |
| `OptimizeRepartition` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 11 | The rule's only effect is flipping a physical planner hint — `optNumPartitions` on `RepartitionByExpression` from `None` to `Some(1)` — a... |
| `OptimizeSubqueries` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 57 | The rule's only relational-algebraic transformation is removing a top-level Sort from a subquery body (Sort(R) ⇒ R), and QED's bag-semant... |
| `OptimizeWindowFunctions` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 10 | This rule is a pure window-function substitution (first(col) → nth_value(col, 1) under an identical ROWS frame), and its validity rests e... |
| `PushFoldableIntoBranches` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 47 | The rule's correctness depends on the distributivity law f(ite(c,t,e)) ≡ ite(c,f(t),f(e)), which is only entailed when ite has its native... |
| `PushProjectionThroughLimitAndOffset` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 10 | The rule's validity rests on Limit/Offset selecting a positional prefix of an ordered row stream while preserving row identity, but QED h... |
| `ReassignLambdaVariableID` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 42 | This rule is a purely syntactic alpha-conversion of `LambdaVariable` bound variables, whose validity rests on binder scoping and equivale... |
| `RemoveRedundantAliases` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 10 | QED's model is positional bag semantics — every field reference serializes to {"column": <int>} over uninterpreted integer-typed values, ... |
| `RemoveRedundantSorts` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 13 | Both branches of RemoveRedundantSorts rest on list-level properties that QED's bag semantics cannot express: the local branch is only sou... |
| `ReorderAssociativeOperator` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 101 | RuleScript encodes scalar operators like `Add`/`Multiply` as uninterpreted function symbols in QED's SMT translation, carrying no algebra... |
| `ReplaceDeduplicateWithAggregate` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 21 | The rule's soundness is a non-deterministic equivalence — both `Deduplicate(keys)` and `Aggregate(keys, First)` emit one row per key but ... |

## Details

### `BooleanSimplification` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 396-639
- Attempts used: 7
- Last updated: 2026-10-12T00:02:53.693529+00:00
- Reason / notes: The encoding faithfully captures the "common factor elimination for conjunction" sub-case of Spark's BooleanSimplification — `(A∨B) ∧ (A∨C) ⟹ A ∨ (B∧C)` — which is exactly what the source produces for a pair of two-disjunct disjunctions sharing one factor; A is correctly shared as the common factor with B/C as distinct residuals, A/B/C are genuinely uninterpreted predicates (not baked-in concrete comparisons), the And/Or operators and nesting match, and this particular case carries no nullability precondition. before() and after() are structurally distinct (a conjunction of disjunctions vs. a disjunction containing a conjunction), so the proof is non-vacuous, and the PARTIAL scope tag is honest and specific about the shape restriction (a genuine, non-degenerate special case rather than the full rule).
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 62308208}, panicked=False

### `CollapseProject` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 1648-2008
- Attempts used: 5
- Last updated: 2026-10-12T00:12:05.551609+00:00
- Reason / notes: The encoding is a faithful, honestly-tagged special case of CollapseProject's core branch: `before()` (nested `Project([P0(c0,c1),P1(c0,c1)], Project([E0(x,y),x], S))`) and `after()` (single `Project([P0(E0(x,y),x), P1(E0(x,y),x)], S)`) are structurally different plans whose equivalence is exactly the logical content of `buildCleanedProjectList` — alias references substituted by the alias body while the pass-through attribute is re-rooted directly on the source — with the same uninterpreted symbols (E0, P0, P1) correctly shared across both sides. The shape restrictions (2-column source, one alias + one pass-through below, two upper expressions each referencing both lower outputs, full inlining only) are genuine under-generalizations, are precisely and specifically stated in the `SCOPE: PARTIAL` line, and yield a non-degenerate rule; the source rule's physical side conditions (determinism, cheapness, Python-UDF eval types) govern execution cost/evaluation behavior and do not constrain bag semantics of pure uninterpreted functions, so no logical precondition is silently missing from the claim QED proved.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 390708}, panicked=False

### `CollapseRepartition` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2013-2039
- Attempts used: 7
- Last updated: 2026-10-12T00:25:39.737730+00:00
- Reason / notes: The encoding is faithful: each Repartition is modeled as an identity projection, which is exactly the bag-semantic content of a shuffle node, so `before()` (stacked identity projections) and `after()` (a single one) are structurally distinct and the proof genuinely captures the "drop the redundant repartition" optimization rather than being a vacuous before==after. The covered branch (`case _` of Case 1, keep-top) has no bag-level precondition that is missing — the numPartitions guard exists only in the excluded (coalesce, shuffle) branch — and reusing one identity symbol for both stacked nodes is safe because the proved statement abstracts the partition-count parameter that the rewrite does not depend on for bag equality. The scope is honestly marked PARTIAL: the restriction is genuine and specific (Cases 2–3 require eliminating a Sort, which QED cannot model; the excluded branch is a physical comparison), and the covered rule is a non-degenerate, useful special case; the only mild narrowing (fixed 3-column arity) is structural, not semantic, and is unavoidable in the DSL.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 85292}, panicked=False

### `CollapseWindow` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2080-2152
- Attempts used: 49
- Last updated: 2026-10-12T01:06:16.321475+00:00
- Reason / notes: The encoding faithfully captures CollapseWindow's core partition-determined subcase: each whole-partition-frame window is modeled as an uninterpreted per-partition aggregate joined back on the NOT-NULL partition key (the null-rejecting inner join is exactly why `k` is declared non-nullable, correctly avoiding the NULL-partition blowup that would break the join-back model), the parent's expression is computed over `base` to enforce the `references ∩ windowOutputSet = ∅` independence, and `before()` (two aggregates + two joins) is structurally distinct from `after()` (one two-call aggregate + one join), so the proof is a genuine universal aggregate-merge statement, not a vacuous identity. The omitted generality (differing/empty order-spec merge directions, bounded frames, row-dependent functions) rests on QED's lack of ordering/frame semantics — a fundamental prover limitation, not a DSL gap — and the `// SCOPE: PARTIAL` line honestly and specifically enumerates the assumptions (identical non-empty partition specs, identical order specs, one partition-determined expression per window, grandchild-only references, empty-order-spec directions excluded), making this a useful, non-degenerate special case.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 97173209}, panicked=False

### `ColumnPruning` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 1493-1638
- Attempts used: 6
- Last updated: 2026-10-12T00:34:35.159481+00:00
- Reason / notes: before() and after() are genuinely structurally different (the inner Project emits 4 uninterpreted columns vs. 2), and the symbol sharing is exactly right — the pruned inner reuses p2_c0/p2_c2 and the outer reuses g0/g1 on the same underlying columns (field 0 and field 2 before; fields 0 and 1 after) — so the proof establishes the real pruning equivalence, not a vacuous identity or an over-constrained coincidence. The fixed shape (4 inner columns, outer referencing positions 0 and 2) is unavoidable in this pattern DSL, while the column expressions and outer projections remain uninterpreted, giving maximal expressible generality for this case; no preconditions are missing since Spark's guard (!outputSet.subsetOf(references)) is satisfied structurally. The SCOPE: PARTIAL line is honest and specific — it names exactly what is covered (Project-over-Project, cols 0 and 2 of 4) and what is omitted (Aggregate/Expand/Union/Window/LeftExistenceJoin cases), and the result is a non-degenerate, useful rule within that stated scope. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 101583}, panicked=False

### `CombineTypedFilters` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/objects.scala, lines 78-107
- Attempts used: 3
- Last updated: 2026-10-12T00:49:15.288499+00:00
- Reason / notes: `before()` (two nested `Filter`s with independent uninterpreted predicates `inner`/`outer` over a shared, universally quantified source) and `after()` (one `Filter` on `AND(inner, outer)`) are structurally distinct, so the proof is non-vacuous and captures exactly the rule's logical transformation — merging two nested filters into a conjunction over the same child, with `scan` standing in for the arbitrary child plan per the DSL idiom. Spark's `t1.deserializer.dataType == t2.deserializer.dataType` guard is a node-constructability constraint (the merged `TypedFilter` must have a single deserializer), not a semantic precondition: the nested-to-conjunctive-filter equivalence holds universally, and QED's quantified proof with uninterpreted (even nullable) predicates strictly subsumes the real rule's instance, so `// SCOPE: FULL` is honest.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 64034250}, panicked=False

### `CombineUnions` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2314-2388
- Attempts used: 4
- Last updated: 2026-10-12T00:57:01.650973+00:00
- Reason / notes: The encoding is a faithful fixed-shape instance of Spark's rule: running Spark's stack algorithm on exactly this input (Distinct over Union(A, Distinct(Union(B,C)), D)) yields precisely Distinct(UNION ALL(A,B,C,D)), so the encoding matches the rule's actual rewrite, not a superficially similar one. Operators are correct — `all=true` for UNION ALL (Spark's `Union`), and both top and nested `Distinct`/`Deduplicate(keys=output)` are modeled as group-by-all-columns with no aggregates, which captures the rule's `AttributeSet(keys) == u.outputSet` precondition; the shared type "T" across scans reflects the required same-schema union (`isSameType`) while A–D remain independent uninterpreted relations, and before()/after() are structurally distinct so the proof is non-vacuous. The proof therefore establishes the genuinely non-trivial content of the rule — that a nested distinct is redundant under an outer distinct after union-all flattening — and the PARTIAL scope tag is honest: the full rule additionally handles arbitrary arity/depth, bare-union flattening (flattenDistinct=false), and project collapse/pushdown, which this fixed 3/2-shape instance does not cover. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 86774625}, panicked=False

### `ConstantFolding` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 50-122
- Attempts used: 41
- Last updated: 2026-10-12T01:17:14.510571+00:00
- Reason / notes: The proof is not vacuous — before() contains a real AND node (`true ∧ false`) that after() eliminates, which is precisely the literal-evaluation step Spark's `ConstantFolding` performs on this fully-literal foldable expression, applied in a legitimate expression context (a Filter's condition, which `mapExpressions` rewrites in place). The narrowing to one specific closed boolean formula is forced by a genuine QED limitation rather than a porter error: uninterpreted symbols carry no evaluation algebra, so the general claim "any foldable expression equals its evaluated literal" is unstateable/unprovable in this framework, and the `SCOPE: PARTIAL` tag names the assumed condition accurately. Symbol sharing is correct (the same scan is the input relation in both plans, and no independent symbol is needed), and no preconditions (determinism, non-null literals) are silently missing for this fully-constant case.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 315583}, panicked=False

### `ConstantPropagation` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 138-270
- Attempts used: 4
- Last updated: 2026-10-12T01:05:55.686733+00:00
- Reason / notes: before() and after() genuinely differ (g(a) vs g(c) on the two distinct join fields), so the proof is non-vacuous: QED must use congruence of the uninterpreted predicate g under the join-enforced a=c to show the bags match. The encoding faithfully captures the rule's core transform — inline a constant for an attribute in one conjunct while keeping the equality conjunct on both sides — with the literal modeled as a unique single-column relation joined on a=c (a sound DSL workaround, since on every join row a=c and thus g(a)≡g(c), and null-a rows are excluded by the inner join). It is a narrow but honestly-tagged PARTIAL special case (single attribute, single equality conjunct, single top-level conjunct, no Or/Not nesting or collation/null-safety branching), and that restriction is genuine, specific, and still yields a non-degenerate, useful rule. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 69250250}, panicked=False

### `ConvertToCatalyst` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 1035-1411
- Attempts used: 24
- Last updated: 2026-10-12T01:26:48.746576+00:00
- Reason / notes: The encoding faithfully captures the core transformation of `ConvertToCatalyst`'s `convertOperator` with-columns path: materializing a shared subexpression `E(x)` as a Project column so the predicate reads it twice from a column instead of re-evaluating it twice inline. The before (`Filter(F(E(x),E(x)), S)`) and after (`Project(x, Filter(F(t,t), Project(x,E(x),S)))`) are structurally distinct, the same uninterpreted symbols `eOp`/`fOp` are correctly shared across both sides, and the SCOPE:PARTIAL line accurately names the genuine restriction (single Filter, one shared subexpression, two reads) versus the full rule's generality (arbitrary single-child operators, multiple shared expressions, non-deterministic handling, lambda/command/aggregate exclusions).
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 68514666}, panicked=False

### `ConvertToLocalRelation` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3296-3325
- Attempts used: 31
- Last updated: 2026-10-12T01:29:54.652128+00:00
- Reason / notes: before() (a Project over a values relation) and after() (the folded values relation) are structurally distinct, so the proof is a genuine non-vacuous check of the constant fold, and the instance (column-reference projection) satisfies the rule's "no unevaluable expressions" guard. The hard-coding is forced rather than accidental: this rule's after-side is a closed data-dependent literal that QED cannot express parametrically over an uninterpreted data symbol, the Limit/LocalLimit branches have no bag-semantic meaning in QED, and Filter folding would require interpreted predicates which the DSL/QED only offer as uninterpreted (unfodable) symbols — making a concrete Project-branch instance the maximal expressible fragment. The SCOPE: PARTIAL line names exactly these restrictions and is honest, and the result is non-degenerate (row reordering/projection verified, not a structurally identical pair). ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 74595833}, panicked=False

### `CostBasedJoinReorder` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/CostBasedJoinReorder.scala, lines 36-102
- Attempts used: 7
- Last updated: 2026-10-12T01:31:36.698898+00:00
- Reason / notes: It is a faithful, non-vacuous instance — `before()` is left-deep ((A⋈B)⋈C)⋈D and `after()` is bushy (A⋈B)⋈(C⋈D); the four independent uninterpreted conjuncts bind to the same (table,tbl) columns in consistent argument order on both sides, and `pCD` genuinely moves from the root into the C⋈D subtree, so the proof is of real tree reordering plus condition re-placement, not a structural identity. INNER is the correct kind (the sound members of `InnerLike`), the `joinField` ordinals are correct, and inner-join reordering requires no key/NOT-NULL precondition. The fixed 4-way left-deep→bushy shape and single-column inputs are an honestly-labeled, non-degenerate PARTIAL scope rather than a coincidental or vacuous proof.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 78223209}, panicked=False

### `EliminateAggregateFilter` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 635-651
- Attempts used: 30
- Last updated: 2026-10-12T01:55:43.003089+00:00
- Reason / notes: `before()` and `after()` differ exactly by the constant-true filter feeding the aggregate, which is the precise semantic content of the rule's True-literal arm (FILTER (WHERE true) is a no-op), and since the DSL's `AggCall` has no per-call filter, modeling it as an input-level `Filter(trueLiteral)` is the faithful representation of that arm. The scan, its types, the group key/value fields, and the uninterpreted aggregate `f` are all shared correctly between the two sides (same table, same `f`, same grouping), so the proof is not vacuous and must go through the group-by/aggregate structure rather than relying on a coincidental symbol identity. The `PARTIAL` tag is honest and specific — the False-literal arm requires identity/empty-value algebra for specific aggregate functions, which QED's uninterpreted aggregates deliberately don't model — and the minimal one-key/one-call/one-operand shape loses no proof content, since the true-filter-identity argument is arity-independent.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 516125}, panicked=False

### `EliminateDistinct` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 603-629
- Attempts used: 6
- Last updated: 2026-10-11T23:49:22.729821+00:00
- Reason / notes: `before()` and `after()` are structurally distinct (they differ only in the `distinct` flag of the `AggCall` for the shared uninterpreted aggregate `f`), so the proof is of the real Spark Catalyst `EliminateDistinct` transformation (drop `DISTINCT`) and not vacuous; the symbols are shared correctly — the group key `g(col)` is functionally determined by the same single unique column `col` that is the sole aggregate argument, so all rows in each group carry one argument value and the distinct and plain input bags coincide, making `f(distinct)=f(plain)` provable without any assumption about `f`; the one genuine restriction is that the distinct key on the child is encoded as a unique single-column scan with the group key derived from that same column (a valid subsumption of the `distinctKeys ⊇ {arg}` condition), while Spark's duplicate-agnostic case (Min/Max/etc.) is truly unprovable in QED and is honestly excluded, so the PARTIAL scope tag is accurate.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 85040833}, panicked=False

### `EliminateMapObjects` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/objects.scala, lines 115-120
- Attempts used: 7
- Last updated: 2026-10-12T01:41:13.731791+00:00
- Reason / notes: The encoding is faithful: `MapObjects(λv. v, input, None)` is semantically an identity transformation on its input, and the porter models it as a concrete identity projection (`project(field(0))`) over an uninterpreted input relation — the correct relational analog, structurally distinct from the bare `after()`, so the proof is non-vacuous; the `serializedColumns = None` premise is reflected (no extra columns) and the `forceNullable = false` premise is mirrored in the non-nullable type, which does not weaken the equivalence. The only restriction is fixed single-column arity, where the original admits any arity — that is genuinely inexpressible in the DSL (no arity-polymorphic patterns; schemas are fixed-arity), is honestly and specifically declared in the SCOPE line, and the result remains a non-degenerate, true instance of the rule's content.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 325542}, panicked=False

### `EliminateOuterJoin` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/joins.scala, lines 158-238
- Attempts used: 4
- Last updated: 2026-10-12T01:51:33.144172+00:00
- Reason / notes: The encoding is non-vacuous (before = FULL outer join, after = INNER join with the identical filter left in place, exactly the source rule's `j.copy(joinType=...)` shape) and correctly shares `on`/`q`/the two `IS_NOT_NULL` conjuncts so the proof is of the real rewrite, not a coincidentally over-constrained one; the source's `canFilterOutNull` precondition is honored by using `IS_NOT_NULL`, the one null-rejecting form QED's semantics can universally verify (it cannot reason that an arbitrary uninterpreted predicate rejects null-extended rows), and the FULL→INNER case is the hardest of the join-type branches since it requires null-rejection on both sides. The SCOPE: PARTIAL tag is honest — it omits the Left/Right/one-sided-Full branches and all four Aggregate/Project elimination cases (and models each side as a single column), so this is a genuine, specific, non-degenerate special case rather than the full rule.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 38215083}, panicked=False

### `ExtractGroupingPythonUDFFromAggregate` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/python/ExtractPythonUDFs.scala, lines 92-150
- Attempts used: 7
- Last updated: 2026-10-12T02:31:29.565732+00:00
- Reason / notes: The encoding correctly models the rule's core transformation—pulling an uninterpreted UDF g out of a grouping expression into a Project below the Aggregate and replacing the expression with a column reference—using consistent symbol sharing (same "g" operator in the groupBy expression and the projection, same "f" aggregate operator in both AggCalls) so the proof is non-vacuous. It is a genuinely narrow special case (single column, one group key, one non-distinct aggregate over that same column, UDF applied directly to the column rather than an arbitrary sub-expression, no distinct flag, no multiple group keys or aggregates), which the SCOPE: PARTIAL line accurately and specifically states; the full rule handles any number of columns, keys, and aggregates and UDFs over arbitrary sub-expressions, but the encoded fragment is non-degenerate (before is Aggregate(Scan,…g(x)…f(x)…) while after is Aggregate(Project(Scan,[g(x),x]),…col0…f(col1)…), and the equivalence rests on genuine bag-semantic reasoning about grouping by the same value and feeding the same input bag to the uninterpreted aggregate, not on structural identity.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 90344791}, panicked=False

### `ExtractPythonUDFFromJoinCondition` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/joins.scala, lines 245-282
- Attempts used: 5
- Last updated: 2026-10-12T02:41:01.337260+00:00
- Reason / notes: The encoding is non-trivial (before is an INNER join with conjunctive condition `and(udf, rest)`; after is an INNER join with condition `rest` plus a top filter `udf`), correctly uses INNER to stand in for the source's InnerLike-only scope, and reuses the same uninterpreted `udf`/`rest` symbols on both sides, so QED verifies the genuine bag-semantic filter-pullup identity for all instantiations rather than a vacuous or over-constrained claim; the minor deviations (INNER rather than Cross, predicates expressed over left's columns, `rest` always present as a real predicate) do not alter the universally valid identity and faithfully reflect the rule's effective effect.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1906834}, panicked=False

### `ExtractPythonUDFs` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/python/ExtractPythonUDFs.scala, lines 162-396
- Attempts used: 10
- Last updated: 2026-10-12T02:56:23.279590+00:00
- Reason / notes: The encoding faithfully captures the core semantic transformation of ExtractPythonUDFs for its declared partial scope: a UDF computation f(x) is extracted from an upper Project into a dedicated lower node (modeled as a Project that computes f and passes through x), and the upper node references the pre-computed column instead of recomputing. The before/after plans are structurally distinct (one Project vs. Project-over-Project), so the proof is non-vacuous; f and g are properly uninterpreted projection operators (not hard-coded predicates or constants); and the SCOPE:PARTIAL tag honestly and specifically declares the single-column/single-UDF/single-expression restriction, which is a legitimate simplest non-degenerate case of the general rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 439000}, panicked=False

### `FinishAnalysis` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 347-384
- Attempts used: 8
- Last updated: 2026-10-12T03:04:27.242802+00:00
- Reason / notes: FinishAnalysis as a whole is a composite of ~19 heterogeneous rewrites (several members — ComputeCurrentTime, InsertMapSort*, subquery/view/alias eliminations — have no bag-semantic meaning or are unexpressible in the DSL), so a full encoding is impossible; the porter instead encoded one genuine member, PullOutGroupingExpressions, in a precisely scoped, non-vacuous instance. The before/after plans are structurally different (Aggregate over a scan vs. Aggregate over a Project that materializes the complex key g(a)), share the same uninterpreted symbols g and f on both sides, use a plain bag scan with nullable types and no hidden key/NOT NULL preconditions that the real rule lacks, and the rewrite shape — complex group key pulled into a project below the aggregate with the key and aggregate argument re-referenced as projected fields — exactly matches Spark's rule. The SCOPE: PARTIAL line honestly and specifically names the restriction (two-column scan, one non-plain key, one non-distinct aggregate over disjoint columns), so the provable result is a faithful, useful special case rather than a vacuous or over-claimed one. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 532041}, panicked=False

### `FoldablePropagation` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 1110-1241
- Attempts used: 6
- Last updated: 2026-10-12T04:30:47.566970+00:00
- Reason / notes: The encoding faithfully captures the core constant-substitution step of Spark's FoldablePropagation: a Project introduces a 0-arity (foldable) uninterpreted expression as its second column, and a parent Filter's reference to that output column is replaced by the foldable expression directly — the before/after differ structurally (project-output reference vs. inlined 0-arity expr) and QED's non-zero SMT/unify work confirms it verified genuine equivalence, not identity. All symbols (scan type, foldable operator, predicate) are properly uninterpreted, the 0-arity Proj correctly encodes "foldable = independent of input," and the SCOPE: PARTIAL tag honestly and specifically delimits the restrictions (single Filter parent, 1-column scan, single foldable) rather than hiding them. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 67739167}, panicked=False

### `GenerateOptimization` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3576-3628
- Attempts used: 45
- Last updated: 2026-10-12T04:14:49.640248+00:00
- Reason / notes: The encoding faithfully captures the minimal non-trivial case of the JsonTuple pruning branch: a row-preserving generate with 2 uninterpreted outputs where the outer project uses only 1, and the rewrite drops the unused output. Before/after are structurally distinct (2-col ProjectMany+1-field proj vs 1-col ProjectMany+1-field proj), the equivalence is a genuine bag-semantic fact (not vacuous), symbols out1/out2 are independent, and the SCOPE line honestly and specifically states all restrictions (2 outputs, 1 selected, no child attributes, row-preserving branch only).
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 338750}, panicked=False

### `GroupBasedRowLevelOperationScanPlanning` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/v2/GroupBasedRowLevelOperationScanPlanning.scala, lines 40-140
- Attempts used: 6
- Last updated: 2026-10-12T04:25:45.053180+00:00
- Reason / notes: before() and after() are genuinely structurally different (the target-only conjuncts live inside the join condition vs. in a filter above the target scan), so the proof is non-vacuous, and the encoding faithfully models the rule's core MERGE step: the fully-evaluated command-condition conjuncts (uninterpreted eval1/eval2, correct symbol reuse across both sides) are pushed into the target scan and dropped from the condition, with the uninterpreted both-sided `rest` standing in for the surviving condition — right operators (scan/filter/inner-join), no concrete predicates, and no conflated symbols. The INNER-join restriction is a genuine, honest PARTIAL scope: dropping these conjuncts is bag-equivalent only for inner joins (an outer join would preserve unmatched left rows that fail eval1/eval2, breaking equality), while the source rule's `Join(...)` pattern leaves the join type unconstrained, so the narrower provable subset is correctly and specifically documented, and the rule remains non-degenerate. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 71342834}, panicked=False

### `InferFiltersFromConstraints` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2238-2309
- Attempts used: 23
- Last updated: 2026-10-12T04:49:38.517267+00:00
- Reason / notes: The encoding is a non-trivial, honest PARTIAL special case of Spark's `InferFiltersFromConstraints`: it correctly models the core mechanism (equality `L=R` being null-rejecting, so `IS NOT NULL` is inferred on both sides, and a left-only conjunct `g(L)` is pushed as a filter below the join) with proper symbol sharing (`g` appears in both the join condition and the left filter; `left.field(0)` and `left.joinField(0, right)` denote the same column). The before/after are structurally distinct and the equivalence is non-vacuous (the after adds explicit filters that the before implicitly enforces via the null-rejecting join condition). The SCOPE line accurately enumerates the restrictions (inner join only, single-column scans, no pre-existing constraints, fixed condition shape), and none of the failure modes apply.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 23729375}, panicked=False

### `NullDownPropagation` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 1068-1094
- Attempts used: 26
- Last updated: 2026-10-12T05:58:26.613391+00:00
- Reason / notes: before() and after() are structurally and semantically distinct (Filter(IS_NULL(LT(x,y))) vs Filter(OR(IS_NULL(x), IS_NULL(y)))) over two independent columns of the scan, so the proof is not vacuous; the fact that it proves at all shows QED has real null-propagation semantics for IS_NULL/LT — an uninterpreted operator symbol could never satisfy IsNull(f(x,y)) <=> IsNull(x) OR IsNull(y), so LessThan is the minimal concrete choice and its nullIntolerant/deterministic guards are baked in by operator identity, not silently dropped. The PARTIAL scope is honest and specific (only the IsNull arm for the LT branch of Spark's five-operator/two-arm rule), the narrowing is a fundamental QED limitation (no null-propagation reasoning for uninterpreted op symbols, no DSL mechanism to attach such a contract) rather than a lazy escape, and the encoded rule is a genuine, non-degenerate instance of the original. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 368625}, panicked=False

### `NullPropagation` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 992-1061
- Attempts used: 26
- Last updated: 2026-10-12T06:00:29.575690+00:00
- Reason / notes: The encoding is a faithful, non-vacuous instance of the source rule's `IsNull(c) if !c.nullable → Literal(false)` branch: the non-nullable precondition is actually carried in the encoding via the non-nullable `VarType` (and `unique=false` avoids smuggling in a key), `IS_NULL` is the exact operator Spark folds (not a baked-in stand-in), and `before()`/`after()` are structurally distinct (IS_NULL predicate vs. false literal under the same filter), so QED's proof certifies a real rewrite rather than an identity. The scope line honestly tags it PARTIAL and correctly enumerates the unencoded branches (IsNotNull, Coalesce, In/InSubquery, Count, EqualNullSafe), which depend on operator semantics (aggregates, IN, null-safe equality) that QED does not model, making the narrowing genuine and the retained rule non-degenerate. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 359125}, panicked=False

### `ObjectSerializerPruning` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/objects.scala, lines 126-245
- Attempts used: 7
- Last updated: 2026-10-12T06:47:07.506945+00:00
- Reason / notes: The encoding faithfully captures the rule's unconditional core rewrite — `Project(_, SerializeFromObject)` where the outer project uses only a subset of serializer outputs — by modeling serializers as uninterpreted functions `ser0/ser1/ser2` over the input column, with `before()` computing the unused `ser1` and dropping it and `after()` omitting it, so the proof is non-vacuous and reflects the real optimization; the ordinal remapping (outer field 2 → pruned field 1) and symbol reuse of `ser0`/`ser2` are correct, and the SCOPE line honestly marks it PARTIAL because the nested-struct `pruneSerializer`/`SchemaPruning` path (gated on `serializerNestedSchemaPruningEnabled`) is genuinely outside this DSL's bag-semantics model, leaving a specific, non-degenerate, useful rule. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 334750}, panicked=False

### `OptimizeIn` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 355-385
- Attempts used: 9
- Last updated: 2026-10-12T06:56:58.247613+00:00
- Reason / notes: The before() (filter `Or(eq, eq)`) and after() (filter `eq`) are genuinely distinct shapes, so the proof is non-vacuous; the single shared `V` type and shared field `e` are correctly uninterpreted (not a concrete predicate or fixed operator) and the reused `eq` symbol faithfully encodes the source rule's "identical list elements" precondition, since `In(v,[e,e])` is semantically `(v=e) OR (v=e)`. The only caveat is the rule is a narrow, honestly-tagged special case (dedup-to-one / two-identical-elements), with the empty-list, InSet-threshold, and general-dedup branches omitted as the SCOPE line states — a legitimate limitation given QED has no `In`/`InSet` operator and can't express the `inSetConvertible`/threshold conditions — but the encoded fragment is a specific, non-degenerate rewrite, not a vacuous `before()==after()`.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 324375}, panicked=False

### `OptimizeJoinCondition` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeJoinCondition.scala, lines 29-65
- Attempts used: 26
- Last updated: 2026-10-12T07:16:25.949042+00:00
- Reason / notes: The encoding proves exactly the rule's semantic core — `x = y OR (x IS NULL AND y IS NULL)` ≡ `x IS NOT DISTINCT FROM y` (Spark's `EqualNullSafe`) — with the correct operators, and correctly embedded in a join where only TRUE passes, so the NULL-vs-FALSE discrepancy in the mixed-null case is behaviorally irrelevant and the equivalence genuinely holds. `before()` and `after()` are structurally different and the two-column/two-scan symbols (`x` on `L`, `y` on `R`) are independent, so the proof is non-vacuous rather than a symbol-sharing accident. The narrowings to an INNER join, the whole condition (no And/Or subterm recursion), and single-column same-type scans are genuine, specific, and honestly declared in the `PARTIAL` scope line rather than silently baked in, making this a faithful non-trivial special case of the source rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 378625}, panicked=False

### `OptimizeOneRowPlan` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeOneRowPlan.scala, lines 44-68
- Attempts used: 29
- Last updated: 2026-10-12T08:07:36.850160+00:00
- Reason / notes: `before()` (group-only `Aggregate` over the scan) and `after()` (`Project` of the key column) are structurally different, and the equivalence genuinely fails without the uniqueness constraint (a bag with duplicate key values would collapse under group-by but not under project), so the proof is non-vacuous and the rewrite is real. The original rule's `maxRows <= 1` is a statistics/physical precondition with no representation in QED's bag semantics, so the porter correctly substituted the actual semantic validity condition — the grouping column being a unique key (with the non-nullable type correctly sidestepping NULL group-merging) — which is the most general shape the DSL can attach a key constraint to (`ScanMany` carries no keys, and grouping by any non-identity expression over the key would break injectivity). The omitted arms (Sort removal, distinct-flip) depend on capabilities QED fundamentally lacks (ordering semantics and aggregate-function algebra), and the single `// SCOPE: PARTIAL` line accurately and specifically discloses exactly what was assumed, making this an honest, non-degenerate, useful special case.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 354250}, panicked=False

### `OptimizeOneRowRelationSubquery` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/subquery.scala, lines 1104-1167
- Attempts used: 48
- Last updated: 2026-10-12T08:11:11.570403+00:00
- Reason / notes: The encoding faithfully captures the uncorrelated scalar-subquery branch's relational-algebra shape (inner cross-join with a one-row relation → inlined constant in the outer project), is structurally non-vacuous (before has a Join node that after eliminates), uses the correct INNER join kind with a true condition matching Spark's scalar-subquery decorrelation, and its narrowing to a constant-valued VALUES relation is a genuine DSL limitation (no cardinality constraint on uninterpreted scans, so an arbitrary one-row relation with an unknown value cannot be expressed); the SCOPE tag honestly and specifically states this restriction.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 66445625}, panicked=False

### `OptimizeUpdateFields` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/UpdateFields.scala, lines 33-77
- Attempts used: 64
- Last updated: 2026-10-12T09:47:16.962072+00:00
- Reason / notes: The encoding faithfully models the source rule's dedup case for its declared instance: each WithField is denoted by a projection whose value expression (distinct uninterpreted ops E1/E2/E3) is evaluated against the carried original row, matching Spark's expression semantics (all ops evaluate against the input row, applied in order, last write wins), and the after() list [WF(a,E3), WF(b,E2)] exactly matches the rule's "last value per name, first-occurrence order" reconstruction. before() and after() are structurally different plans — before() carries the dead E1(a,b) computation that after() eliminates — so the proof is not vacuous; it genuinely verifies that an overwritten WithField can be dropped. The 2-field/3-operation fixed shape and all-WithField restriction are inherent to this fixed-shape pattern language (no unbounded arity or DeleteField/case-folding modeling possible), are honestly and specifically declared in the PARTIAL scope line, and the instance is non-degenerate rather than trivially identical. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 321083}, panicked=False

### `PartitionPruning` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/dynamicpruning/PartitionPruning.scala, lines 52-373
- Attempts used: 89
- Last updated: 2026-10-12T11:22:57.590807+00:00
- Reason / notes: The encoding faithfully abstracts DPP's semantic effect — the inserted pruning filter keeps exactly the build rows whose join-key exists on the other side (a SEMI join), which is then re-joined under the same condition — and QED's proof of `build ⋈ probe = (build ▷ probe) ⋈ probe` is a genuine, non-vacuous identity (the two plans are structurally different) that is precisely the losslessness invariant that makes DPP sound. The assumptions (INNER join, build-side pruning, join condition identical to the pruning condition, deterministic scans) are inherent to how DPP works rather than artificial over-constraints, the PARTIAL scope tag is honest and specific, and the source's cost/benefit guards do not bear on the semantic equivalence being proved.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 74873583}, panicked=False

### `PlanExternalUDFs` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/externalUDF/PlanExternalUDFs.scala, lines 41-271
- Attempts used: 22
- Last updated: 2026-10-12T10:06:22.841690+00:00
- Reason / notes: The encoding is non-vacuous and faithful to the rule's core `extract` transformation: `before()` (Project computing f(x) inline alongside g(x)) and `after()` (a lower projection computing f(x) and passing x through — mirroring `ExecuteExternalUDF`'s result-plus-child-output shape — with the upper operator referencing the produced UDF column and keeping g over the pass-through column) are structurally different, and the proof verifies the genuine bag-equivalence π[f(x),g(x)] ≡ π[id,g∘id] ∘ π[f,id] rather than a trivial identity. Symbol handling is correct: f and g are distinct uninterpreted operators (matching the rule's UDF-vs-rest split), f is consistently shared between both plans as the same UDF, no constants, keys, or nullability assumptions are baked in, and the join-kind/union/aggregate shapes are correctly absent since the target case is a Project. The narrowing — single-column input, one deterministic UDF, Project as the host operator (vs. the rule's arbitrary operator, multiple-UDF recursion, and the separate join-condition/aggregate extraction paths) — is exactly what the SCOPE line states, so it is a genuine, specifically-stated, useful special case of the rule's logical core, which is the strongest claim QED (with no physical/external-execution semantics) can make about this rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 434542}, panicked=False

### `PropagateEmptyRelation` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PropagateEmptyRelation.scala, lines 219-224
- Attempts used: 8
- Last updated: 2026-10-12T10:18:31.198847+00:00
- Reason / notes: `before()` (an INNER join with an empty left input) and `after()` (an independent empty relation of the join's output schema `l0,l1,r0,r1`) are structurally distinct yet semantically equivalent by INNER-join semantics, so the proof is non-vacuous; the join condition is left as an uninterpreted `pred` and the column types are correctly shared across both sides to assert schema preservation, with no bogus over-constraint. The scope is honestly tagged PARTIAL for a genuine, specific fragment (INNER join, empty left side, fixed two-column arity — the latter being a real DSL arity limitation, and INNER being a sound choice since "any join type / either side empty" is not universally valid, e.g. LEFTANTI/FULLOUTER), making this a faithful, non-degenerate special case of the source rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1554708}, panicked=False

### `PruneFileSourcePartitions` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/PruneFileSourcePartitions.scala, lines 35-98
- Attempts used: 11
- Last updated: 2026-10-12T10:38:36.201275+00:00
- Reason / notes: `before()` (Project(π, Filter(P∧D, R))) and `after()` (Project(π, Filter(P∧D, Filter(P, R)))) are structurally different, so the proof is non-vacuous, and P/D/π are properly left uninterpreted with P restricted to the partition columns exactly as Spark's `getPartitionFiltersAndDataFilters` requires, with the full filter kept on top as in `rebuildPhysicalOperation`. The single modeling choice — the pruned index being *exactly* `Filter(P, R)` rather than any intermediate superset — reflects a real QED/DSL limitation (no way to assert row-set containment between two uninterpreted scans), and it is an instance Spark genuinely produces when all partitions are statically evaluable, so the result is a real, non-degenerate special case. The `SCOPE: PARTIAL` line accurately and specifically discloses this containment gap, so the "provable" verdict is faithful and not misleading.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 15834333}, panicked=False

### `PruneFilters` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2510-2540
- Attempts used: 22
- Last updated: 2026-10-12T10:35:05.864520+00:00
- Reason / notes: before() and after() are structurally different in exactly the way Spark's rewrite acts — Filter(And(shared, restOuter), Filter(And(shared, restInner), R)) becomes Filter(restOuter, Filter(And(shared, restInner), R)) — so the proof's content is the non-vacuous absorption shared∧…∧shared∧… ≡ shared∧…, not a tautology. Symbol sharing is precisely right: "shared" being the same symbol in both filters models Spark's actual precondition (the child filter's conjunct appearing in the child's constraints, i.e. constraints.contains(cond)), while rest_inner/rest_outer correctly remain independent uninterpreted predicates, so no coincidental over-constraint or missing independence is inflating the result. The narrowing (child must itself be a Filter, one textually identical conjunct pruned, literal true/false branches omitted) is forced by QED's inability to model constraint propagation or entailment between independent symbols, it is honestly and specifically stated in the SCOPE line, and what remains is a genuine, common, non-degenerate instance of the real rule's constraint-pruning case (including its "deterministic conjunct" requirement, since all QED predicates are deterministic by construction). ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 16561750}, panicked=False

### `PullOutNestedDataOuterRefExpressions` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PullOutNestedDataOuterRefExpressions.scala, lines 73-136
- Attempts used: 25
- Last updated: 2026-10-12T11:13:01.832012+00:00
- Reason / notes: The encoding faithfully captures the rule's essential transformation with correct symbol sharing: before() uses the outer-only map-extraction `Gmv(o)` inline in the filter, while after() materializes that same `Gmv(o)` as a new projection column, filters on it, then projects it away — exactly mirroring the source rule's "add the outer expression as a new attribute, reference it, then remove it", and before()/after() are structurally different (so the proof is non-vacuous). The key precondition that makes the optimization sound — the extracted expression referencing only outer columns, not inner ones (`e.references.isEmpty`) — is genuinely respected since `Gmv` is applied to the outer field `o` alone, and the `Gmv`/`Cmp` symbols are the same instances on both sides (no accidental independence). The scope tag honestly concedes it is a PARTIAL special case (single extraction, single comparison, correlation modeled as an INNER cross-join) rather than the fully general rule, and this narrower form remains a genuine, non-degenerate, useful equivalence (materialize-then-drop a computed column vs. computing it inline), which is exactly the core the real rule optimizes.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 70465167}, panicked=False

### `PullOutVariantExtractions` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/PullOutVariantExtractions.scala, lines 118-505
- Attempts used: 8
- Last updated: 2026-10-12T10:49:52.929754+00:00
- Reason / notes: The encoding faithfully captures the Aggregate case of the rule: `before` aggregates `f(ve(v))` directly over the scan, while `after` first hoists `ve(v)` into a Project below the Aggregate (dropping the raw variant column, matching the source rule's `passthrough` logic where only group keys + hoisted aliases survive), then aggregates `f` over the projected column. The structural difference is genuine and non-vacuous, the uninterpreted symbols (`ve`, `f`, the scan) are correctly shared, and the SCOPE tag accurately documents that this is a single-group-key, single-aggregate, single-extraction special case with no Sort/Join handling.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 464625}, panicked=False

### `PullUpProjectAliasThroughWindow` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PullUpProjectAliasThroughWindow.scala, lines 64-140
- Attempts used: 6
- Last updated: 2026-10-12T11:04:16.986253+00:00
- Reason / notes: The encoding is non-trivial (before has a 3-col bottom project + identity top project; after has a 2-col bottom project + top project computing f(v)), correctly shares the uninterpreted symbols f and w across both sides, uses the right relational shape (Project→[Agg+InnerJoin+Proj]→Project) to model a single window, and captures the rule's key preconditions (determinism via uninterpreted projection, alias inputs ⊆ retained window-referenced columns, bare pass-through in top project, no expression consumption). The scope line honestly and specifically discloses the genuine narrowings: one window (QED lacks Window semantics), one alias, and a non-nullable partition key (required for the inner equi-join model to be sound).
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 91621500}, panicked=False

### `PullupCorrelatedPredicates` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/subquery.scala, lines 467-693
- Attempts used: 30
- Last updated: 2026-10-12T11:44:52.142267+00:00
- Reason / notes: The encoding is a faithful, honestly-scoped PARTIAL capture of the rule's core transformation for the Exists branch: `before()` places the pulled-up conjunct `c` inside the subquery filter (`inner.filter(c∧local)`, empty conditions slot) and `after()` moves it to the conditions slot (`inner.filter(local)`, cond `c`), with structurally distinct plans so the proof is non-vacuous and non-trivial; the key algebraic fact proven — moving a conjunct independent of the inner row across the EXISTS boundary (`∃i: P∧Q(i) ⟺ P∧∃i Q(i)`) — is exactly the soundness condition the real pullup relies on, and making `c` 0-ary (documented in SCOPE) is the correct, disclosed stand-in for "independent of the inner row" given the DSL lacks inner-visible outer references. The genuine narrowings (one conjunct each, single filter, empty pre-existing conditions, EXISTS-only, no output reshaping/dedup) are all specific and disclosed, and no symbol-sharing error or missing precondition makes the result unsound.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 24514500}, panicked=False

### `PushDownJoinThroughUnion` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushDownJoinThroughUnion.scala, lines 51-157
- Attempts used: 22
- Last updated: 2026-10-12T11:27:55.267652+00:00
- Reason / notes: The encoding is a faithful, non-vacuous proof of a genuine special case of the Spark rule: `before` is `(X ∪+ Y) ⋈_LEFT O` and `after` is `(X ⋈_LEFT O) ∪+ (Y ⋈_LEFT O)`, which are structurally different and whose equality (union-all distributing over a left-outer join on the left side) is exactly the rewrite Spark performs. The symbol choices are the *correct* fidelity moves, not traps: reusing one condition symbol `C` across `condU/condX/condY` accurately models Spark reusing a single join condition (only attribute references are remapped per branch), and reusing one right-side scan `O` correctly abstracts Spark's `dedupRight` copy, which is bag-identical to the original. The narrowing to LeftOuter / 2-branch UNION ALL / single-column is real and disclosed in the `// SCOPE: PARTIAL` line (the source also covers INNER and arbitrary arity, and the broadcast guards are cost-only, not semantic preconditions), so the "provable" verdict is meaningful and not misleading — it is a true universal proof of a useful, non-degenerate subset rather than a vacuous `before()==after()` or an accidentally over-constrained pattern.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 126763125}, panicked=False

### `PushDownLeftSemiAntiJoin` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushDownLeftSemiAntiJoin.scala, lines 35-172
- Attempts used: 8
- Last updated: 2026-10-12T11:37:32.244384+00:00
- Reason / notes: The encoding is non-vacuous (before = SemiJoin(Project(X),Y) vs after = Project(SemiJoin(X,Y))) and semantically correct: because P0/P1 are shared uninterpreted (hence functional) symbols and C is shared, the condition inlines identically on both sides, which mirrors Spark's determinism precondition, and under bag-semijoin semantics both plans yield the same left-leg rows. It is a genuine, non-degenerate special case—LeftSemi only (not LeftAnti) and the over-Project case only (not Aggregate/Window/Union/Unary), with self-joins excluded by using two distinct scans—and the SCOPE: PARTIAL line honestly and specifically discloses exactly those restrictions, so the provable result is meaningful for what it covers.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 73494459}, panicked=False

### `PushDownPredicates` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2547-2554
- Attempts used: 22
- Last updated: 2026-10-12T11:38:41.646890+00:00
- Reason / notes: The encoding is a faithful, non-vacuous fragment of Spark's dispatcher: `Filter(left_only, L INNER join R on join_cond)` vs. `L filter left_only INNER join R on join_cond` are structurally distinct shapes, and the proof is meaningful precisely because the *same* uninterpreted predicate `left_only` is shared on the same L column in both positions — the pushdown is sound only for the identical predicate, so the required symbol sharing is correct rather than an over-constraining coincidence (had the two predicate occurrences been independent symbols the rewrite would be unsound and QED would have reported not-provable), while `join_cond` remains an arbitrary uninterpreted binary predicate over both inputs. The PARTIAL tag is honest and specific — INNER join only, a single left-only parent conjunct, with right-only/common conjuncts and the dispatcher's other branches (outer-join cases, CombineFilters, non-join pushdown) explicitly omitted — and pushing a left-only filter below an inner join is a genuine, high-value, non-degenerate sub-case of the source rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 68906667}, panicked=False

### `PushExtraPredicateThroughJoin` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushExtraPredicateThroughJoin.scala, lines 33-81
- Attempts used: 40
- Last updated: 2026-10-12T12:47:33.576265+00:00
- Reason / notes: The encoding faithfully captures the rule's core semantic move — side-specific predicates that are implied by the join condition (p on L appears in every disjunct; q∨s on R) are pushed down as filters into the respective inputs while the join condition itself is left unchanged, exactly as the source does. The before/after forms are structurally distinct (no vacuity), the field/symbol sharing is correct (pl=plj, pr=prj, sr=srj, so no coincidental over-constraint), and the INNER-join + fixed disjunctive-condition shape is an honest, non-degenerate special case of the rule that genuinely exercises its extraction logic, accurately reflected in the `SCOPE: PARTIAL` line. The source rule is broader (six join types with type-specific push sides, arbitrary column counts and condition shapes), so this is correctly a narrower case rather than the full rule, but nothing in the encoding is unsound or misleading. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 72032459}, panicked=False

### `PushLeftSemiLeftAntiThroughJoin` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushDownLeftSemiAntiJoin.scala, lines 190-275
- Attempts used: 24
- Last updated: 2026-10-12T12:08:03.463602+00:00
- Reason / notes: The encoding faithfully captures the TO_LEFT_BRANCH, INNER-child-join, LeftSemi (not anti) special case of Spark's rule: before is SEMI(INNER(X,Y,cOp), Z, sOp) with sOp referencing only X and Z fields (matching the precondition that all semi-condition conjuncts reference only childLeft∪semiRight), and after is INNER(SEMI(X,Z,sOp), Y, cOp) — a structurally different plan where the semi-join is pushed through the inner join. The uninterpreted predicates sOp and cOp are correctly shared across before/after with proper field re-indexing, no key/NOT-NULL preconditions are needed for this bag-semantic commutation, and the single-column scans don't lose generality since the predicates are opaque functions whose only constraint (which tables they reference) is already captured. The SCOPE: PARTIAL annotation honestly lists each restriction (no LeftAnti, no TO_RIGHT_BRANCH, INNER only vs. InnerLike/LeftOuter, non-empty condition only).
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 78432750}, panicked=False

### `PushPredicateThroughJoin` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2973-3088
- Attempts used: 5
- Last updated: 2026-10-12T12:17:13.239651+00:00
- Reason / notes: before() (a top-level Filter of three distinct uninterpreted conjuncts over an INNER join with its own uninterpreted join condition) and after() (each single-side conjunct pushed as a Filter on its input, the common conjunct ANDed into the join condition) are structurally different and encode exactly Spark's InnerLike branch of the Filter-over-Join case, so the proof is non-vacuous. Symbol sharing is used correctly, not coincidentally: the same predicate operators are re-bound to the pre-join columns, whose values are identical in the join row (if QED had treated them as independent symbols the equivalence would fail, so the success confirms correct column unification), and left/right/common/join-conjuncts are properly kept as four independent symbols. The restrictions — INNER join only (other join types in the source rule have a different, filter-remaining-on-top rewrite), exactly one conjunct per category, single-column inputs, and the implicit assumption that the common conjunct is absorbable into the join condition (no `others` filter left on top, which is semantically neutral under QED's bag model that doesn't model throwable/stateful expressions) — are genuine, specific, and honestly declared in the SCOPE line, so this is a faithful partial encoding of the real optimization. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 71225750}, panicked=False

### `PushPredicateThroughNonJoin` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2567-2961
- Attempts used: 7
- Last updated: 2026-10-12T12:24:57.097770+00:00
- Reason / notes: The encoding faithfully models the rule's projection case (the avoid-double-filter-eval branch with no projection split): before is Filter(AND(F(π₀), G(π₁)), Project([P1,P2])) and after is Project([P1,P2]) over Filter(F(P1(x))) with G(π₁) retained on top in alias form — exactly Spark's output, where the pushed conjunct is alias-substituted onto the projection expression (P1, F, G correctly shared across both sides) and the retained conjunct stays above referencing the projection's output column. before() and after() are structurally different (the F-filter moves from above to below the project), so the proof is non-vacuous; the rule's real preconditions (projection determinism per SPARK-13473, no subquery/attribute overlap) are subsumed by QED's deterministic uninterpreted symbols or trivially satisfied by this fragment, and the cheap/expensive distinction is a cost heuristic of the optimizer, not a semantic condition. The PARTIAL scope line accurately and specifically states the restrictions (two-column projection over a single [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 67216417}, panicked=False

### `PushProjectionThroughUnion` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 1421-1481
- Attempts used: 7
- Last updated: 2026-10-12T12:46:56.809490+00:00
- Reason / notes: The encoding is structurally non-vacuous (Project(G, L ∪ R) vs Union(Project(G,L), Project(G,R))) and faithfully models the rule's core claim: a deterministic projection (here, one uninterpreted whole-row function G, whose function semantics automatically enforces Spark's `projectList.forall(_.deterministic)` precondition) commuting past a bag union, with the shared G symbol and positionally aligned identical schemas correctly mirroring Spark's `AttributeMap(left.output.zip(right.output))` remapping and union's left-child output types. The restrictions — two branches, fixed two-column width, single projection symbol, and omission of the exprId-duplicate/Alias-regeneration guards — are genuine DSL limitations or Spark plan-bookkeeping concerns rather than semantic preconditions (bag equivalence holds unconditionally), and they are specifically and honestly flagged in the PARTIAL scope line, so the provable result is a legitimate, non-degenerate special case of the source rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1921750}, panicked=False

### `PushVariantIntoScan` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/PushVariantIntoScan.scala, lines 365-442
- Attempts used: 25
- Last updated: 2026-10-12T12:51:27.061012+00:00
- Reason / notes: The encoding faithfully captures the core semantic transformation of PushVariantIntoScan in a narrow but non-degenerate form: before() places the filter (on P(ExtractA(v))) below the projection that materializes extractions, while after() models the "pushed-in" scan as a projection over the original scan, filters on the now-materialized field, and applies an identity projection—proving non-vacuous filter–project commutation under the stated materialization assumption. The SCOPE: PARTIAL tag is honest and specific (one variant column, two requested fields, one pass-through column), symbol sharing is correct (same uninterpreted P, ExtractA, ExtractB, and source scan on both sides), the relational shape (Project∘Filter∘Scan) matches the real rule's structure, and no backend precondition (Parquet format, variant-column existence, config flag) is silently misrepresented—those are acknowledged as unmodelable guarantees that QED correctly does not need to re-verify.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 67853250}, panicked=False

### `PushdownPredicatesAndPruneColumnsForCTEDef` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushdownPredicatesAndPruneColumnsForCTEDef.scala, lines 33-270
- Attempts used: 21
- Last updated: 2026-10-12T13:08:36.549620+00:00
- Reason / notes: The encoding captures the rule's actual soundness core: the disjunction of the reference predicates is pushed into the shared CTE definition (`cte.filter(Or(pred1, pred2))`) while each reference keeps its own filter, with the union-all pairing the two independent uses of the same scan symbol — and before()/after() genuinely differ, so the proof is of the real invariant `filter(p, R) == filter(p, filter(p1∨p2, R))` per branch, not a vacuous identity. Predicates stay fully uninterpreted (no baked-in conditions), the shared scan symbol correctly models the single shared definition, and the determinism/non-forward-reference preconditions of the source rule are QED-expressible-impossible (non-determinism) rather than silently dropped. The `// SCOPE: PARTIAL` line is honest and specific — no column pruning, exactly two references, both pushable — and two references is the minimal case that makes the Or-pushdown non-degenerate, so the result is a useful genuine special case.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 68432417}, panicked=False

### `RemoveLiteralFromGroupExpressions` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3556-3570
- Attempts used: 63
- Last updated: 2026-10-12T14:40:33.022867+00:00
- Reason / notes: The encoding is non-trivial and faithful to the rule's core transformation: it removes a constant (uninterpreted 0-ary projection `c`) from the group-by keys of an Aggregate and re-introduces it via a ProjectMany, which is exactly what the Spark rule does for the case where at least one non-literal key remains. The PARTIAL scope is honestly and specifically stated (one literal key, one column key, two fixed aggregates), the symbol sharing is correct (same `c`, `f`, `g` in both sides), and no precondition is silently dropped for this instance.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 106266042}, panicked=False

### `RemoveNoopOperators` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 843-867
- Attempts used: 5
- Last updated: 2026-10-11T23:46:43.276692+00:00
- Reason / notes: The encoding is non-vacuous (before() contains a Project node that after() lacks), correctly captures the core no-op-Project semantic (identity projection ≡ child), and the single-column/scan-child restrictions are honest consequences of QED's fixed-schema, no-metavariable design rather than porter error; the PARTIAL scope line accurately documents both the Window sub-case exclusion (no Window operator in QED) and the arity limitation.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 452667}, panicked=False

### `RemoveNoopUnion` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 873-921
- Attempts used: 11
- Last updated: 2026-10-12T13:40:18.873611+00:00
- Reason / notes: before() = Distinct(A UNION ALL A) and after() = Distinct(A) are structurally different yet genuinely equivalent (group-by-all dedup collapses the duplicated bag to the same set of distinct rows), so the proof is non-vacuous, and reusing the single scan `a` for both union children correctly models the rule's "canonically-equal children" precondition rather than over-constraining it. The encoding faithfully captures the core rewrite (collapse a no-op duplicate union under a Distinct-like node, matching the Spark trace Distinct→withNewChildren(simplifyUnion)→head), and is honestly and specifically scoped PARTIAL (plain Distinct, exactly two inputs, strictly identical children) while remaining a non-degenerate, useful special case of the broader canonicalization-based rule.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 64497084}, panicked=False

### `RemoveRedundantAggregates` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/RemoveRedundantAggregates.scala, lines 30-69
- Attempts used: 24
- Last updated: 2026-10-12T13:43:41.129793+00:00
- Reason / notes: The encoding faithfully captures Case 1 (`isLowerRedundant`): a group-only upper aggregate whose single group key is `lower.field(0)=k1` (a deterministic non-aggregate output) over a non-global lower aggregate, rewritten to an aggregate over `Project([k1], source)` — which matches `upper.copy(child = Project(projectList, lower.child))` with `projectList=[k1]` — and all three of `isLowerRedundant`'s preconditions (no duplicate-sensitive aggs, upper refs only deterministic non-agg, non-global lower) hold by construction, while `before()` (nested `Aggregate`) genuinely differs from `after()` (`Aggregate` over `Project`) so the proof is non-vacuous. The narrowing (one-column scan, zero upper agg calls, exactly two lower group keys plus one unreferenced agg call) is a specific, non-degenerate special case honestly flagged as PARTIAL, and full generality is genuinely out of reach because the rule's dynamic `projectList = aggregateExpressions.filter(references.contains)` and unbounded key/agg counts cannot be expressed as a fixed pattern; no symbol-sharing or missing-precondition error is present.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 77893375}, panicked=False

### `RemoveRepetitionFromGroupExpressions` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3634-3645
- Attempts used: 8
- Last updated: 2026-10-12T14:28:46.749195+00:00
- Reason / notes: The proof is non-vacuous: before() is a 3-key aggregate over a projection (with a trailing projection dropping the duplicate key) while after() is a 2-key aggregate directly on the scan, so QED verified a real equivalence, not a structural tautology. The duplicate group key being encoded as literally the same underlying column (s2.field(2) ≡ s.field(0)) is the maximal generality QED can certify, since the source rule removes only *identical* grouping expressions and QED cannot prove semantic equality of two distinct uninterpreted projections. The remaining narrowing (single uninterpreted aggregate call, "sum", on the non-key column) is specific and honestly declared in the PARTIAL scope tag, the aggregate operand maps to the same base column on both sides, and no preconditions from the source rule are missing. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 86584125}, panicked=False

### `ReorderJoin` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/joins.scala, lines 45-122
- Attempts used: 26
- Last updated: 2026-10-12T14:55:26.955966+00:00
- Reason / notes: The encoding faithfully traces Spark's createOrderedJoin algorithm for a specific 4-way input (A,D,C,B) with conditions {pAB, pCD, qDBC}: the algorithm correctly selects B first (pAB is the only condition referencing both A and B), then D (no remaining condition is subset of {A,B}∪{D}), then C with the residual conditions — exactly matching the porter's after() tree. All field indices are verified correct (e.g., qDBC_before = qDBC(D=1, B=3, C=2) in the (A,D,C,B) layout and qDBC_after = qDBC(D=2, B=1, C=3) in the (A,B,D,C) layout apply the same uninterpreted symbol to the same logical columns), the cross-join-with-true-condition correctly models Spark's Cross/Inner-with-no-condition, the trailing project [0,2,3,1] matches the rule's sameOutput/Project injection, and the SCOPE: PARTIAL line accurately and specifically delimits the narrowness (one concrete 4-way topology, single-column scans) while keeping the predicates uninterpreted so the bag-equivalence proof is genuinely universal over all predicate instantiations. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 74281625}, panicked=False

### `ReplaceCTERefWithRepartition` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/ReplaceCTERefWithRepartition.scala, lines 37-95
- Attempts used: 7
- Last updated: 2026-10-12T14:53:07.734009+00:00
- Reason / notes: The only bag-level content of ReplaceCTERefWithRepartition is that a CTERelationRef transparently denotes the CTE definition's bag and the RepartitionByExpression it is swapped in for is bag-identical to its input — the encoding captures exactly that with one uninterpreted definition relation (correctly shared between before/after, since the ref is a transparent alias) and an identity projection standing in for the shuffle, and `cte` vs `cte.project(cte.fields())` are structurally distinct so the proof is not vacuous. No concrete predicates, types, or join kinds are baked in, and the genuinely unmodelable residue — single-shuffle reuse across multiple refs, the canSkipExtraRepartition/underSubquery guards, and the self-join re-aliasing on output-set mismatch — is physical/attribute-level (invisible to bag semantics, including the guards, since adding or skipping the repartition is a bag no-op either way) and is specifically enumerated in the SCOPE: PARTIAL line, so the narrower scope is both honest and the strongest claim QED's bag model can support for this rule. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 265875}, panicked=False

### `ReplaceDistinctWithAggregate` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3333-3338
- Attempts used: 5
- Last updated: 2026-10-12T14:57:22.171721+00:00
- Reason / notes: The encoding is faithful and non-vacuous: `before()` (set-variant self-intersection, i.e. set(R)) and `after()` (GROUP BY on all columns with zero aggregate calls) are structurally distinct operator shapes, and the single `Input` scan symbol is reused exactly where the source rule requires it (the same relation on both arms of the intersect, an arbitrary non-unique bag), so the proven fact set(R) = group-by-all-columns(R) is precisely the semantic identity underlying Spark's `Distinct(child) => Aggregate(child.output, child.output, child)`. Column types remain uninterpreted (`Col0_Type`, `Col1_Type`), so nothing concrete is baked in, and the only restrictions — fixed 2-column arity and Distinct being modeled as the only dedup construct QED models — are genuine DSL/QED limitations (no variable-arity scans, no bag-variant INTERSECT) that are stated specifically in the SCOPE line, leaving a useful, non-degenerate special case rather than a trivial identity. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 61505625}, panicked=False

### `ReplaceExceptWithAntiJoin` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3404-3412
- Attempts used: 11
- Last updated: 2026-10-12T15:24:50.139463+00:00
- Reason / notes: The encoding faithfully models Spark's rule for a 2-column instance: `a.minus(false,b)` is the set-difference `EXCEPT` (matching `Except(left,right,false)`), and the after-side is a `DISTINCT` (group-by on all output columns, no agg calls) over a left-anti join (`JoinRelType.ANTI` = Spark's `LeftAnti`) whose condition is the AND of per-column null-safe equalities built from `SqlStdOperatorTable.IS_NOT_DISTINCT_FROM` — exactly Spark's `EqualNullSafe`, which is essential because `EXCEPT`'s row equality is null-safe. `A` and `B` are independent scan symbols ( [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 26441000}, panicked=False

### `ReplaceExceptWithFilter` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/ReplaceExceptWithFilter.scala, lines 44-118
- Attempts used: 53
- Last updated: 2026-10-12T15:46:19.976433+00:00
- Reason / notes: The encoding faithfully mirrors the rule's core: `Except(left,right,false)` → `minus(false, …)` on a shared base (matching the rule's "same base" eligibility), the right-side filter predicate stays uninterpreted, and `Not(COALESCE(c,false))` is correctly expanded to `Not(pR) OR IS_NULL(pR)` under 3-valued logic, with the `Distinct` wrapper modeled as `f.intersect(false,f)`; before() and after() are structurally different yet semantically equivalent (set of values where P_L holds and P_R is false/null), so the proof is non-vacuous, and the single-column restriction is an honestly and specifically labeled PARTIAL scope that is still a non-degenerate, useful rewrite.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 38897417}, panicked=False

### `ReplaceIntersectWithSemiJoin` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3382-3390
- Attempts used: 25
- Last updated: 2026-10-12T15:35:22.080708+00:00
- Reason / notes: The encoding faithfully mirrors every operator choice in the rule — `intersect(false)` for INTERSECT DISTINCT, `JoinRelType.SEMI` for LeftSemi, `IS_NOT_DISTINCT_FROM` for Spark's `EqualNullSafe`, and the `Distinct`-as-group-by-all `Aggregate` over the semi-join's left columns — with `a` and `b` as independent same-schema scans and correctly paired per-column null-safe join fields, so `before` (set intersection) and `after` (semi-join + distinct) are genuinely different shapes QED had to relate (a non-vacuous proof, since the identity depends on the interpreted null-safe equality). The sole narrowing is a fixed 2-column arity versus the rule's parametric n, which is inherent to the DSL's fixed-shape patterns (arity can't be a symbol), is called out in the SCOPE line, and is non-degenerate since the construction is identical for any n.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 63866625}, panicked=False

### `ReplaceNullWithFalseInPredicate` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/ReplaceNullWithFalseInPredicate.scala, lines 53-183
- Attempts used: 23
- Last updated: 2026-10-12T15:39:49.024757+00:00
- Reason / notes: before() (Filter(And(p, NULL), R)) and after() (Filter(And(p, FALSE), R)) are structurally distinct, and the proof is of the genuine universal claim — for every row of every relation and every uninterpreted predicate p, p∧NULL and p∧FALSE agree in filter (three-valued) semantics — which is exactly the null-as-false justification the Spark rule relies on in a Filter context. The only predicate is an uninterpreted symbol correctly shared across both sides, types are all consistently boolean, and the shape (null in one arm of a top-level And under a Filter) matches what replaceNullWithFalse actually produces there. The encoding is admittedly a narrow fragment — Join/DML/Or/CaseWhen/If/Not-In/Like contexts are not covered (and most are beyond the DSL anyway, e.g. no LIKE/In/lambda operators, no DML nodes) — but the SCOPE: PARTIAL line is accurate and specific, and the result is non-degenerate: had the other literal been TRUE instead of FALSE the equivalence would fail, so the verdict validates the real semantics rather than a vacuous before()==after() coincidence. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 201375}, panicked=False

### `OptimizeExpand` — ❌ FAILED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeExpand.scala, lines 56-127
- Attempts used: 77
- Last updated: 2026-10-12T10:14:58.348315+00:00
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 70285500}, panicked=False

### `CheckCartesianProducts` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3160-3183
- Attempts used: 10
- Last updated: 2026-10-11T23:57:12.518065+00:00
- Reason / notes: `CheckCartesianProducts` is a pure validation rule — its only effect is to `throw` a compilation error on a detected Cartesian join, so it produces no rewritten "after" plan, while RRule/QED can only model and prove bag-semantic equivalence between two concrete relations (there is no rejection/exception construct anywhere in the DSL, serializer, or prover contract). On top of that, its core decision (`isCartesianProduct`) is a *syntactic, plan-structural* inspection of which columns the join condition references — a meta-level property of the operator that QED's uninterpreted, bag-semantic semantics has no way to see or reason about (and no clean relational equivalence to stand in for it, since a one-side-only condition does not make the join bag-equal to a pure cross product). So there is genuinely no faithful before/after pair to encode, and the porter's UNSUPPORTED conclusion stands.

### `CleanUpTempCTEInfo` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushdownPredicatesAndPruneColumnsForCTEDef.scala, lines 276-282
- Attempts used: 23
- Last updated: 2026-10-12T00:06:00.562227+00:00
- Reason / notes: The only difference between before and after is the value of `originalPlanWithPredicates` on `CTERelationDef`, a pass-through node whose bag is exactly its child's, and that field is planner bookkeeping (idempotency for CTE pushdown) that no rule or the executor reads for query results. QED's model sees only bag relations with uninterpreted row-level functions, so both patterns necessarily compile to the identical Q-expression over the child, and any `extend_dsl_file` operator carrying the field would either hard-code its inertness into `semantics()` (making the "proof" circular) or attach a JSON attribute the immutable Rust prover does not interpret. The rule's correctness rests entirely on the Spark-planner-level fact that the field is semantically inert, which is outside the bag-semantic model by construction, so the porter's UNSUPPORTED conclusion is sound.

### `CleanupDynamicPruningFilters` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/dynamicpruning/CleanupDynamicPruningFilters.scala, lines 35-94
- Attempts used: 49
- Last updated: 2026-10-12T00:36:31.678765+00:00
- Reason / notes: The rule's only plan-level move is deleting a DynamicPruning conjunct (semantically `key IN <dynamically computed value set>`) on the grounds that a same-row conjunct `key = const` already pins the key, but the value set is a backend-internal artifact with no plan representation, so soundness rests on the entailment `key = const ⟹ key IN <dpp-values>` between independent uninterpreted symbols plus Spark's DPP invariant that values come from the join's filtering side. QED's bag-semantic model has no way to relate an uninterpreted DPP predicate to an equality predicate or to a dynamically-materialized set, and no DSL operator can manufacture that link — the porter's complete_fragment=true negative confirms this is a fundamental limitation, not an encoding bug. ```

### `CollapseGroupedSumOfCount` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/CollapseGroupedSumOfCount.scala, lines 35-124
- Attempts used: 41
- Last updated: 2026-10-12T00:24:33.646795+00:00
- Reason / notes: QED treats aggregate functions (COUNT, SUM, MAX, MIN) as uninterpreted symbols and can only equate aggregate calls via per-group input bag equality, so it cannot derive the identity this rule's soundness rests on — that summing per-fine-group COUNT(1) outputs over a coarser grouping equals a direct SUM(1) over the underlying rows (count additivity over a partition), nor the MAX/MIN idempotence over deduplicated grouping keys. This gap lives in the prover's fixed aggregate semantics, so no `extend_dsl_file` change to the Java DSL (which already serializes aggregate operator names and operands) can close it. I independently checked the narrowest possible special case — identical fine/coarse groupings with a single `SUM` — and it is still unprovable, since the outer SUM's per-group input on the before side is a bag of uninterpreted count values while on the after side it is a bag of literal 1s, which no SMT reasoning over uninterpreted functions can equate.

### `CombineApproximatePercentiles` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/CombineApproximatePercentiles.scala, lines 73-225
- Attempts used: 23
- Last updated: 2026-10-12T00:44:09.587443+00:00
- Reason / notes: The rewrite's only non-trivial step replaces two scalar `approx_percentile` calls with one fused array-valued call plus `GetArrayItem` extractions, and its correctness is the engine-internal identity "element i of the fused digest equals the scalar percentile p_i over the same input bag" — an algebraic axiom between two distinct aggregate functions, while QED treats every aggregate as an opaque uninterpreted symbol and relates different symbols only through bag equality of their inputs. Compounding this, QED has no list/array semantics at all, so the fused digest and its element access cannot even be expressed in the core language, and `extend_dsl_file` cannot close that gap because the missing semantics would have to live in the immutable prover. The only encoding QED could prove would reuse a single aggregate symbol with identical arguments on both sides (a tautological identity that doesn't correspond to the actual rule, whose whole point is fusing calls with different percentage arguments), so no genuine special case is provable.

### `CombineConcats` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 1343-1376
- Attempts used: 29
- Last updated: 2026-10-12T00:51:34.455754+00:00
- Reason / notes: CombineConcats is a purely scalar rewrite whose soundness rests exactly on algebraic properties of `concat` (associativity/flattening) and on `Cast` distributing over a nested `concat` — both of which QED models as bare uninterpreted function symbols with no axioms, so any non-vacuous encoding of nested⟹flat admits an SMT countermodel and only identity encodings (same symbol, same argument shape on both sides) would be provable. This is not a missing-DSL gap that `extend_dsl_file` could close: the fixed prover's JSON interface (schemas/queries, table "guaranteed" constraints) has no channel for function-level axioms, so the limitation is fundamental to the uninterpreted-operator semantics the porter identified. ```

### `DecimalAggregates` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3191-3290
- Attempts used: 24
- Last updated: 2026-10-12T01:40:05.502801+00:00
- Reason / notes: Every arm is sound only by numeric facts about decimal types — a scale-preserving widening cast is value-identical/monotone (so it commutes with Min/Max), decimal Sum is homomorphic over the unscaled long sum (the Sum/MakeDecimal arms), and Average's double-division path is value-identical (the Average arms) — but QED encodes all casts/projections and aggregate functions as uninterpreted symbols with no arithmetic, monotonicity, or aggregate-algebra axioms (only input-bag equality), so even the smallest instance, e.g. `min(f(x)) ≡ f(min(x))` for uninterpreted f, admits a countermodel (non-monotone f) and is unprovable. The window arms additionally require Window, which has no bag semantics in QED at all. Since the prover's operator interpretation is fixed and unmodifiable and a DSL extension could only add more uninterpreted symbols, no encoding can close this gap — consistent with the verified ReduceDecimals and EliminateCast rejections of the same cast/numeric-identity class.

### `EliminateLimits` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3098-3124
- Attempts used: 7
- Last updated: 2026-10-12T01:36:14.187325+00:00
- Reason / notes: All three families of EliminateLimits depend on semantics QED explicitly lacks: the elimination case needs a `maxRows` cardinality upper bound (QED models only key uniqueness, never row counts/statistics), and both `Limit(0, X) ≡ ∅` and the nested-merge `Limit(le, Limit(ne, X)) ≡ Limit(min(le,ne), X)` require axioms that Limit selects a first-n prefix of an ordered row stream (qed.pdf §6.2 states QED has no ordering semantics for Limit/Offset/Order By — the emitted `sort` QOp is a fully uninterpreted bag operator, so SMT refutes even the degenerate `Limit(n, Limit(n, X)) ≡ Limit(n, X)`). The gap is in the trusted, unmodifiable prover's semantics, not the DSL surface: a new Limit/Sort builder could only serialize that same uninterpreted operator and could not supply the missing axioms, so no RuleScript encoding can be provable.

### `EliminateOffsets` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3134-3144
- Attempts used: 21
- Last updated: 2026-10-12T01:49:40.103667+00:00
- Reason / notes: All three subcases of EliminateOffsets are axioms about row-ordering semantics — skipping the first n rows in order, producing an empty relation when the offset reaches the child's maxRows, and additivity of nested offsets — and QED is a bag-semantics decision procedure with no model of ordering at all (Sort/Limit/Offset/Window/Sample have no bag-semantic meaning, and Offset is an uninterpreted/no-semantics operator in the prover), so none of the rewrites is entailed for all instantiations. A DSL extension could at most add a Java-side Offset builder, but the missing Offset axioms would have to live in the QED prover itself, which is the fixed, unmodifiable arbiter, so the rule is genuinely outside RuleScript/QED. ```

### `EliminateSerialization` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/objects.scala, lines 40-72
- Attempts used: 10
- Last updated: 2026-10-12T02:12:15.328507+00:00
- Reason / notes: Every branch of EliminateSerialization is sound only because a deserializer is the inverse of its matching serializer (deser(ser(x)) = x) on matching types, and the AppendColumns branch additionally depends on the internal behavior of the backend's AppendColumns/AppendColumnsWithObject operator pair. QED models projections as uninterpreted functions with no equation/entailment reasoning between them, so a faithful encoding is unprovable (as the porter's tested encoding confirmed), and any provable variant must either make before/after structurally identical (round-trip erased to a pass-through) or axiomatize the inverse law — i.e. assume the rewrite's own content, leaving only a vacuous tautology. No DSL operator or scan constraint can supply a specific backend operator's semantics, so this is a genuine QED limitation (no function entailment / bespoke operator semantics), not a naming or modeling bug the porter missed. ```

### `EliminateSorts` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2430-2502
- Attempts used: 47
- Last updated: 2026-10-12T02:15:56.221517+00:00
- Reason / notes: Every branch of EliminateSorts — subsumption of lower sorts under a global sort, joins re-ordering/destroying input order, sorts below order-irrelevant aggregates (Min/Max/Count/non-float Sum) being useless, and foldable-key sorts being no-ops — is justified solely by row-order/list semantics, but QED is a bag-semantic decision procedure with no model of Sort/Limit/Window ordering and no algebra for aggregate functions beyond bag equality of their inputs. No faithful encoding exists: the only "proof" obtainable would be the degenerate Sort ≡ identity claim (which would be a *false* backend rewrite for a top-level ORDER BY), not the rule itself, and adding a Sort builder via extend_dsl_file cannot close the gap because the unmodifiable prover has no ordering logic in which to check such a rewrite — this is a fundamental QED limitation, exactly the class the reference marks as unsupported. ```

### `EliminateWindowPartitions` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/EliminateWindowPartitions.scala, lines 28-41
- Attempts used: 24
- Last updated: 2026-10-12T02:17:39.994952+00:00
- Reason / notes: QED has no Window operator and models aggregation opaquely per key tuple, so it cannot relate the per-partition groupings a spec induces with vs. without a foldable (constant) column; the whole correctness of the rule is exactly "partitioning by (spec, const) == partitioning by spec," which requires constant/FD reasoning QED lacks, and the strongest self-referential probe (group-by (k,k) vs (k)) is rejected before SMT because the two sides carry different key-tuple arities and QED cannot equate window-function values computed under two different key tuples — consistent with the independently-verified SKIPPED sibling ReduceWindowPartitionCols.

### `ExtractExternalUDFFromWindow` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/externalUDF/ExtractExternalUDFFromWindow.scala, lines 33-71
- Attempts used: 23
- Last updated: 2026-10-12T03:22:47.323733+00:00
- Reason / notes: The rule's only semantic content is commuting a row-wise external UDF across the Window boundary (per-row eval(udf(w)) = udf(eval(w))), and QED's input language has no Window node — JSONSerializer's Rel switch has no LogicalWindow case and falls through to "Not implemented", so the window can only be the aggregate-joined-back composite used in the accepted CollapseWindow precedent. In that composite the window's "inside" and "outside" are both just Projects around one uninterpreted aggregate, so any faithful encoding renders before() and after() structurally identical (udf at the same relational level in both, a vacuous self-proof), while modeling the pre-rewrite window's UDF'd column as a distinct opaque aggregate would require QED to prove g(B) = udf(f(B)) for free aggregate symbols — an equation its uninterpreted-aggregate model, which knows nothing beyond bag equality of inputs, cannot establish. Even the narrowest modelable slice (partition-determined, order-free expressions) therefore yields no non-vacuous provable statement, and the general case additionally needs ordering semantics QED lacks entirely, so the porter's UNSUPPORTED conclusion and its stated reason are correct. ```

### `ExtractPythonUDFFromAggregate` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/python/ExtractPythonUDFs.scala, lines 39-85
- Attempts used: 27
- Last updated: 2026-10-12T02:48:44.685297+00:00
- Reason / notes: The rule's entire content is moving a scalar Python UDF from inside Spark's Aggregate expression list (where Spark permits arbitrary expressions wrapping aggregate results) to a Project above the Aggregate, but in RuleScript's core language an Aggregate's output is exactly the group keys plus flat uninterpreted AggCalls over input rows — there is no position for a scalar expression over the aggregate's own result, and QED's JSON/Q-expression format has no corresponding node form, so a `extend_dsl_file` builder extension could not introduce it without changing the unmodifiable Rust prover. Any compilable before/after pair is therefore either the identical `Project(g, Aggregate(...))` on both sides (a vacuous identity, as the reviewer flagged) or unfaithful (e.g. placing g below the aggregate, which changes per-group vs per-row bag semantics). UNSUPPORTED is the correct verdict. ```

### `ExtractPythonUDTFs` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/python/ExtractPythonUDFs.scala, lines 401-420
- Attempts used: 80
- Last updated: 2026-10-12T04:24:24.807377+00:00
- Reason / notes: QED's Q-expression/semiring calculus has no table-generating (row-producing) operator — nothing that, per input row, yields a data-dependent set of fresh output rows correlated to that row (a SQL LATERAL/UDTF/UNNEST) — yet both the before-side `Generate(PythonUDTF, …)` and the after-side `BatchEvalPythonUDTF`/`ArrowEvalPythonUDTF` are exactly such UDTF nodes, so neither pattern is even expressible and the rewrite's correctness rests entirely on the UDTF's row-generation semantics, a bespoke internal behavior QED cannot see through as an uninterpreted function. This gap is not closeable via `extend_dsl_file`: `JSONSerializer` has no serialization case for a table-function operator and the unmodifiable Rust QED prover has no Q-expression node for one, and `correlate` does not substitute because it is a correlated join over a fixed independent right relation, not a per-row function application producing a variable number of rows. ```

### `InferFiltersFromGenerate` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2191-2227
- Attempts used: 33
- Last updated: 2026-10-12T04:48:43.071362+00:00
- Reason / notes: The rule is sound only because Spark's Explode/Inline generators emit zero rows when the generator input is NULL or an empty array, so rows failing `size(x)>0 ∧ x IS NOT NULL` contribute nothing to the bag below the generate — that is a backend operator's bespoke internal semantics. QED's format has no row-multiplying/table-function node (only scan/filter/project/join/correlate/group/union/sort), and its only stand-in, an uninterpreted projection, lacks the drop behavior, so the prover's refutation of the porter's encoding is a genuine counterexample (a P-false row still emits `gen(r)` on the left), not a symbol-sharing bug. Since QED also cannot do predicate entailment between independent uninterpreted symbols, even a join-based model of element enumeration could not be axiomatically linked to the inferred filter, so the rule is fundamentally unexpressible to the prover.

### `InferWindowGroupLimit` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/InferWindowGroupLimit.scala, lines 54-138
- Attempts used: 25
- Last updated: 2026-10-12T04:50:30.435092+00:00
- Reason / notes: The rule's soundness rests entirely on order-dependent semantics that QED by design does not model: `WindowGroupLimit` keeps the first `limit` rows of each partition under `orderSpec`, and row_number/rank/dense_rank assign ordinal positions within that ordering — the equivalence holds only because `rank <= limit` selects exactly that order-prefix (with tie behavior handled by the retained filter). QED is a bag-semiring decision procedure that explicitly lacks ordering semantics for Limit/Offset/Order By (qed.pdf §6.2), has no Window/rank-frame operator in the DSL or the prover's theory, and cannot perform predicate inference between independent uninterpreted symbols, so even a faithful encoding with `WindowGroupLimit` and the rank column as uninterpreted symbols leaves the critical prefix↔rank relation as an unknowable internal semantic. This is a fundamental limitation of the Rust prover's theory, not a missing builder that `extend_dsl_file` could close. ```

### `InjectRuntimeFilter` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/InjectRuntimeFilter.scala, lines 38-466
- Attempts used: 41
- Last updated: 2026-10-12T06:59:58.415285+00:00
- Reason / notes: The rewrite's soundness rests on the bloom filter's no-false-negative invariant — `BloomFilterMightContain(agg(right_keys), hash(l.key))` must hold for every left row that satisfies the join condition — and QED models both `BloomFilterAggregate` (a specific aggregate whose algebra it knows nothing about) and `BloomFilterMightContain` as uninterpreted, so under an interpretation where the predicate is identically false the after-plan is a strict sub-bag and equivalence is genuinely not derivable. This is the "backend operator's bespoke internal semantics / aggregate algebra" limitation, compounded by the fact that table `guaranteed` constraints are scoped to a table's own columns and cannot express the needed cross-table link between the join condition and the filter; adding a scalar-subquery builder via `extend_dsl_file` would not add the missing axioms, and the prover itself is off-limits, so no non-vacuous encoding (the only provable one is the no-op filter = true) can capture the rule.

### `InlineCTE` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/InlineCTE.scala, lines 58-339
- Attempts used: 41
- Last updated: 2026-10-12T05:08:17.614847+00:00
- Reason / notes: CTE inlining is a let-expansion rule (`WithCTE(B, Q(t))` ≡ `Q[B/t]`), but QED's Q-expression language has no let-binding or defined-name construct — the JSON vocabulary the prover consumes (scan, filter, project, join, correlate, aggregate, set ops, sort, values) contains no define/with/bind node — so the before pattern cannot link a CTE reference to its defining body. The only alternative encodings all fail: modeling the reference as an uninterpreted base table makes the identity false for arbitrary instantiations (QED correctly refutes it, since `t = B` is not expressible as a table constraint), and pre-identifying the reference with the body collapses the rule to a trivial identity; because the Rust prover is the untrusted-change-forbidden arbiter and has no `With` semantics, this is a fundamental QED limitation rather than a Java-DSL gap that `extend_dsl_file` could close. ```

### `LikeSimplification` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 878-984
- Attempts used: 41
- Last updated: 2026-10-12T05:04:04.369432+00:00
- Reason / notes: Every branch of LikeSimplification asserts an extensional equivalence between string pattern operators (LIKE 'a%' ≡ StartsWith, LIKE '%s%' ≡ Contains, LIKE 'a%b' ≡ StartsWith∧EndsWith with a length guard, LIKE 'x' ≡ =), but QED's SMT encoding translates all virtual types — including strings (see RelType's "All virtual types will be translated to integer for prover") — and treats each of these operators as an independent uninterpreted symbol with no axioms connecting them, so Filter(L,R) = Filter(S,R) is universally false for distinct symbols L, S and can only be "proven" by reusing one symbol name on both sides, which collapses the rule into a vacuous tautology that verifies nothing about the actual rewrite. This is a fundamental gap in the prover's theory (a backend operator's bespoke internal semantics over a string domain it does not model), not a missing Java-side builder — extend_dsl_file can only emit operator names the unmodifiable Rust prover already interprets as uninterpreted functions, and no amount of new builders can add the missing LIKE/StartsWith/Contains axioms. ```

### `LimitPushDown` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 926-1030
- Attempts used: 21
- Last updated: 2026-10-12T05:21:27.228244+00:00
- Reason / notes: Every branch of Spark's LimitPushDown relocates or adjusts Limit/LocalLimit/Offset nodes (through Union, through Join, limit-1 through a group-only Aggregate, merging Limit over Offset, limit-1 onto the right side of a condition-less semi/anti join), and all of these operators are defined by row-position/prefix semantics (keep-first-n, skip-first-m, arbitrary-row selection) that QED's bag-semantic model has no meaning for — "the first n rows of a bag" is not a deterministic bag operation, so no SMT equivalence decision is even defined over such plans. The one branch with a bag-flavored core (limit-1 on the right of a cross semi/anti join, where only non-emptiness of the right side matters) still requires a Limit operator whose choice of *which* row survives is nondeterministic and therefore inexpressible as a QED relation, and the DSL has no Limit/Offset/Sort builders at all; since the missing piece is in QED's own semantics (it does not model ordering/prefix semantics) rather than a missing builder, `extend_dsl_file` cannot close the gap, leaving no non-trivial provable subcase. The porter's UNSUPPORTED verdict is correct.

### `LimitPushDownThroughWindow` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/LimitPushDownThroughWindow.scala, lines 32-59
- Attempts used: 11
- Last updated: 2026-10-12T05:16:01.232267+00:00
- Reason / notes: The rule's soundness rests on order-dependent facts that no bag-semantic model can express: `Limit(n, Sort(orderSpec))` must retain the *first* n rows of the global order (bag semantics cannot distinguish that from an arbitrary n-element subset), and RowNumber/Rank/DenseRank over a global `UNBOUNDED PRECEDING → CURRENT ROW` frame must assign to those leading rows values that depend only on preceding rows (prefix-safety), which is a specific window-frame algebra QED cannot know about a function it can at best treat as uninterpreted. QED's JSON format can technically carry a `sort` node, so adding Sort/Limit/Window builders via `extend_dsl_file` wouldn't help — a sort would reduce to a bag identity (making any "proof" vacuous and semantically empty) or an unconstrained bag transform (making the two sides unprovable), and a standalone Limit has no expressible node at all. This matches the reference limitation list exactly (Sort/Limit/Offset/Window/Sample have no bag-semantic meaning; bespoke operator internals can't be reasoned about), and the porter's cited prior SKIPPED verdict on CockroachDB's `PushLimitIntoWindow` for the same order-dependence is consistent with my independent analysis — the gap is in the prover, not a surface DSL mistake. ```

### `MarkSingleTaskExecution` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/MarkSingleTaskExecution.scala, lines 49-177
- Attempts used: 7
- Last updated: 2026-10-12T05:23:12.572440+00:00
- Reason / notes: MarkSingleTaskExecution makes zero change to relational/bag content — it returns a structurally identical clone with only a physical TreeNodeTag (a planner hint) set on scan/Expand nodes, so the before() and after() patterns would be the *same* logical plan. QED is a bag-semantic logical-equivalence prover with no notion of node tags or physical hints (its JSON/Q-expression format carries only relational structure plus table constraints, no per-node metadata), so the only obtainable result is the vacuous identity before ≡ before, which asserts nothing about this rule. This is a fundamental limitation, not a missing builder: extend_dsl_file cannot help because the unmodifiable prover checks equivalence purely on relational semantics, where a tag is either invisible (giving a tautology) or, if surfaced, would make before/after non-equivalent in a way that contradicts the rule's annotation-only intent — unlike CollapseRepartition, whose removed shuffle node is genuinely bag-equivalent to identity and thus had real content to prove. ```

### `MergeSubplans` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/planmerging/MergeSubplans.scala, lines 151-341
- Attempts used: 25
- Last updated: 2026-10-12T05:38:23.879118+00:00
- Reason / notes: The rule's core content is a whole-plan common-subexpression elimination that identifies structurally identical but separately-instantiated scalar subplans and hoists them into new CTE definitions, and none of it is provable here: RuleScript rules carry no side conditions, so the "these two subplans are structurally equal" precondition cannot be stated (encoding them as one shared symbol makes the rewrite vacuous and unrepresentable, since the before/after shape depends on how many subqueries the input plan happens to contain), and the fixed QED arbiter's semiring/JSON language has no CTE definition/reference binding nor arbitrary scalar-subquery semantics (its subquery node serves only exists/in/unique higher-order operators), so the gap is in the arbiter, not a missing builder that `extend_dsl_file` could close; the rule's auxiliary one-row and determinism preconditions and the struct pack/unpack identities between independent uninterpreted functions are likewise beyond what QED can assume or know.

### `NormalizeFloatingNumbers` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/NormalizeFloatingNumbers.scala, lines 67-201
- Attempts used: 27
- Last updated: 2026-10-12T05:46:01.220568+00:00
- Reason / notes: Both operative sub-rules rest on semantics QED's theory fundamentally cannot express: the Window partition-spec case needs a Window operator, which the DSL does not expose and which QED explicitly has no bag-semantic meaning for (list/ordering operators are out of its model), and the join/NAAJ case rewrites each equi-join `l = r` into `norm(l) = norm(r)` where `norm` (NaN→canonical NaN, −0.0→0.0) is a non-injective canonicalization whose soundness depends on IEEE floating-point equality facts — in QED's theory all virtual types collapse to integers and `norm` is a plain uninterpreted function, so `norm(l)=norm(r)` is a strict weakening of `l=r` and no axiom relating the two exists, making the rewrite unprovable; the only degenerate instance that would be provable (norm = identity) is exactly the case where Spark's `needNormalize` guard is false and the rule doesn't fire, so no faithful provable special case exists. ```

### `OptimizeCsvJsonExprs` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeCsvJsonExprs.scala, lines 44-484
- Attempts used: 10
- Last updated: 2026-10-12T06:47:10.438062+00:00
- Reason / notes: Every sub-rewrite in this bundle depends on a specific algebraic identity between Spark's bespoke JSON/CSV operators — JsonToStructs(StructsToJson(x)) ≡ x (inverse composition), field_i(JTS(full schema)) ≡ field_0(JTS(pruned schema)) (parse-consistency under schema projection), field_i(MultiGetJsonObject) ≡ GetJsonObject (a Hive-compatible first-match semantics), and struct(field_0..n(JTS)) ≡ JTS (reconstruction). QED models all scalar expressions as uninterpreted functions with no axioms relating them, so each of these identities is refutable by a countermodel in the QED semiring semantics (e.g., two independent function symbols f, g for which g∘f ≠ id), and no faithful relational encoding — however cleverly symbol-shared — can make any of them universally valid. This is precisely the "operator whose specific internal semantics QED cannot see through as an uninterpreted function" limitation, and the guards in the original rule (empty options, corrupt-record exclusion, same-input coalesce checks) encode exactly the parser-internal conditions QED cannot express, so UNSUPPORTED is correct. ```

### `OptimizeMetadataOnlyDeleteFromTable` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/v2/OptimizeMetadataOnlyDeleteFromTable.scala, lines 39-128
- Attempts used: 9
- Last updated: 2026-10-12T07:15:05.760513+00:00
- Reason / notes: Both sides of the rewrite are DML statements (row-level ReplaceData/WriteDelta DELETE → metadata-only DeleteFromTableWithFilters/DeleteFromTable), and RuleScript's core language plus the fixed prover's JSON format expose only SELECT operators (scan/filter/project/join/set-ops/aggregate), so no faithful before()/after() pair can be constructed, and an `extend_dsl_file` addition of a delete node is infeasible because the unmodifiable Rust prover cannot interpret a new DML node type. Even a hypothetical uninterpreted Delete symbol would not help: the rewrite's validity rests on the connector-specific `translateFilterV2` conversion being meaning-preserving and on the `canDeleteWhere`/`SupportsDeleteV2`/`TruncatableTable` side conditions, i.e. predicate entailment between independent uninterpreted symbols and a backend operator's bespoke internal semantics — precisely the limitations QED is designed not to cross.

### `OptimizeMetadataOnlyQuery` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/OptimizeMetadataOnlyQuery.scala, lines 46-182
- Attempts used: 26
- Last updated: 2026-10-12T07:36:55.695984+00:00
- Reason / notes: The rule's after-side is a constant `LocalRelation` whose rows are the partition specs pulled from the catalog/file-system, so its soundness rests on the physical invariant that a partitioned table's data, projected onto its partition columns (and filtered), yields exactly those catalog-determined partition rows, plus on Max/Min idempotence or aggregate distinctness. QED models scans and aggregates as uninterpreted bag symbols with no notion of physical partition layout, has no way to axiomatize a relation between two independent uninterpreted symbols (the scan and any metadata stand-in), knows no aggregate algebra beyond input bag-equality (so the `isAllDistinctAgg` side can't be justified either), and the DSL cannot even build a non-empty constant relation — so any encoding would be either inexpressible or refuted by SMT, making this a genuine QED modeling limitation rather than a missed encoding.

### `OptimizeRand` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeRand.scala, lines 33-63
- Attempts used: 48
- Last updated: 2026-10-12T08:43:12.371938+00:00
- Reason / notes: Every branch of OptimizeRand is only valid under the semantic axiom that rand() ∈ [0,1) (the folding) or under an equivalence between distinct comparison predicates (the operand swap), and QED supports neither: it models all function and predicate symbols as uninterpreted with no user-axiom channel, and the only side-information the rule format can carry is "guaranteed" constraints on base-table columns, which cannot attach to the output of a scalar function like rand(). Hence no narrowing faithful to the rule is provable — dropping the range assumption makes the folds unsound, and modeling rand as a constrained table column is a different rule — so this is a genuine QED limitation (the prover has no concept of function axioms) rather than a missing DSL builder that `extend_dsl_file` could close.

### `OptimizeRepartition` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2045-2053
- Attempts used: 11
- Last updated: 2026-10-12T08:22:55.946990+00:00
- Reason / notes: The rule's only effect is flipping a physical planner hint — `optNumPartitions` on `RepartitionByExpression` from `None` to `Some(1)` — and its guard is purely syntactic (partition expressions non-empty and all foldable); a shuffle/repartition is a bag-identity over its child regardless of how many partitions it allocates, so before and after are bag-identical to the shared child and the only faithful encoding is the vacuous `before ≡ after`. This rests on a backend operator's bespoke physical semantics (partition count) that QED's bag-semantic model has no notion of, and the core language has no repartition operator at all — extending the DSL would only yield either a degenerate identity or two uninterpreted symbols that differ in an invisible field, so there is no non-trivial relational content to prove. ```

### `OptimizeSubqueries` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 389-490
- Attempts used: 57
- Last updated: 2026-10-12T09:43:03.214332+00:00
- Reason / notes: The rule's only relational-algebraic transformation is removing a top-level Sort from a subquery body (Sort(R) ⇒ R), and QED's bag-semantic model has no Sort operator at all — Sort/Limit/Window/Sample require list semantics and are explicitly outside its decidable fragment, which the porter confirmed empirically: after properly extending RelRN.java with a Sort builder (now present in the DSL source) and serializing a clean `sort` node, the minimal Sort(scan) ⇒ scan test returned provable=false with no panic, proving the wall is in the prover, not the DSL. Every encoding of this rule's actual rewrite must place a Sort in the before-pattern, so no narrower sort-based instance can be provable. The rule's remaining branches — re-running the entire optimizer on the subquery, preserving the top Aggregate through re-optimization, and Spark exprId/attribute-identity bookkeeping in the COUNT-bug path — are backend-internal meta-operations (no SubqueryExpression operator exists in the DSL, and "optimizer.execute(...)" is a fixed-point operation with no before/after RA pattern), so they are inexpressible rather than merely untried; a fresh attempt cannot succeed. ```

### `OptimizeWindowFunctions` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2058-2069
- Attempts used: 10
- Last updated: 2026-10-12T09:56:02.553692+00:00
- Reason / notes: This rule is a pure window-function substitution (first(col) → nth_value(col, 1) under an identical ROWS frame), and its validity rests entirely on the order- and frame-dependent identity of those two aggregates — "value of the first-ordered row of the partition" — which requires ordering semantics that QED does not model (Sort/Limit/Offset/Window have no bag-semantic meaning in the prover). Since the core language exposes aggregates only as uninterpreted symbols related by bag equality of their inputs, a faithful encoding with two distinct symbols for first and nth_value gives QED no way to relate them (as the porter's empirical provable=false confirms), while collapsing them to one shared symbol is a vacuous tautology that assumes rather than verifies the very identity in question. ```

### `PushFoldableIntoBranches` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 788-855
- Attempts used: 47
- Last updated: 2026-10-12T12:18:55.631214+00:00
- Reason / notes: The rule's correctness depends on the distributivity law f(ite(c,t,e)) ≡ ite(c,f(t),f(e)), which is only entailed when ite has its native conditional-selection semantics in the SMT encoding; QED serializes IF/CASE as a universally quantified uninterpreted function (as the porter's empirical run confirmed: complete_fragment=true, no timeout, provable=false), so no DSL-level encoding can recover the missing ite algebra without modifying the QED prover itself. ```

### `PushProjectionThroughLimitAndOffset` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushProjectionThroughLimitAndOffset.scala, lines 27-43
- Attempts used: 10
- Last updated: 2026-10-12T12:26:49.328453+00:00
- Reason / notes: The rule's validity rests on Limit/Offset selecting a positional prefix of an ordered row stream while preserving row identity, but QED has no ordering semantics for Sort/Limit/Offset (qed.pdf §6.2) and models them as uninterpreted bag operators, under which SMT refutes Project(E, Limit(n, R)) ≡ Limit(n, Project(E, R)) (and the Offset/GlobalLimit variants) for any non-identity projection E — e.g., a Limit model that returns one fixed row on non-empty input breaks commutation with any E that doesn't fix that row. The only universally valid instance (identity projection) is vacuous, and adding a Limit/Offset builder via extend_dsl_file could only serialize the same uninterpreted sort QOp that the unmodifiable prover interprets opaquely, so no encoding can recover the required ordered-prefix/row-count axioms — consistent with the verified AGREE precedents for the structurally identical datafusion CommuteLimitProjection and same-backend spark EliminateLimits. ```

### `ReassignLambdaVariableID` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/objects.scala, lines 251-294
- Attempts used: 42
- Last updated: 2026-10-12T13:11:42.483980+00:00
- Reason / notes: This rule is a purely syntactic alpha-conversion of `LambdaVariable` bound variables, whose validity rests on binder scoping and equivalence-under-renaming — notions QED's first-order bag-semantics translation (uninterpreted functions over relations) has no concept of, and neither the DSL nor the prover's JSON format exposes any lambda/binder scalar node to represent a `LambdaVariable` at all. Since the Rust prover is the unchanging arbiter, this cannot be closed by a `extend_dsl_file` change: a binder node emitted by the serializer would be uninterpretable to the prover anyway. Hence any non-vacuous encoding must name the old-ID and new-ID variants as distinct uninterpreted symbols, which QED treats as independent and cannot equate, while the only provable encoding — identical before/after patterns — is the vacuous identity P = P that encodes nothing of the actual rewrite. ```

### `RemoveRedundantAliases` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 657-838
- Attempts used: 10
- Last updated: 2026-10-12T13:50:27.812333+00:00
- Reason / notes: QED's model is positional bag semantics — every field reference serializes to {"column": <int>} over uninterpreted integer-typed values, so it has no notion of column names or attribute identity, and the Spark rule's two load-bearing preconditions (the `name == attr.name` schema-preservation gate, and the exclusion sets that prevent duplicate attribute names across join/union inputs — a name-collision/well-formedness property, not a row-multiset property) are inexpressible in it. In RuleScript, `Alias(attr, name)` and bare `attr` both encode as the identical positional projection, so any encoding collapses to a vacuous P = P that discards the rule's actual preconditions. This is a fundamental limitation of QED's semantic model (the prover's JSON has no name-identity channel and the prover itself is off-limits), not a fixable DSL gap, so no narrower encodable special case of this rule exists. ```

### `RemoveRedundantSorts` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/RemoveRedundantSorts.scala, lines 29-62
- Attempts used: 13
- Last updated: 2026-10-12T13:53:41.848877+00:00
- Reason / notes: Both branches of RemoveRedundantSorts rest on list-level properties that QED's bag semantics cannot express: the local branch is only sound when the child's guaranteed output ordering satisfies the requested one (`orderingSatisfies(child.outputOrdering, orders)`), and the global branch rewrites into `RepartitionByExpression`, a physical shuffle operator with no relational meaning. QED models relations as bags with no Sort operator, no ordering-guarantee symbol, and per-row table "guarantee" constraints cannot encode a row-order property, so the rule's actual correctness precondition is inexpressible rather than merely unbuilt. The only bag-checkable reading (Sort ≡ identity) is vacuously provable for any child but verifies none of the rule's soundness condition and would justify an incorrect rewrite of a top-level ORDER BY, confirming this is a fundamental prover limitation that extending RelRN cannot close. ```

### `ReorderAssociativeOperator` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 275-344
- Attempts used: 101
- Last updated: 2026-10-12T14:50:18.261307+00:00
- Reason / notes: RuleScript encodes scalar operators like `Add`/`Multiply` as uninterpreted function symbols in QED's SMT translation, carrying no algebraic axioms, so the prover cannot establish the associativity/commutativity, identity (`a+0=a`, `a*1=a`), zero-absorption, or literal-folding properties this rule's soundness rests on; even its narrowest subcase (`Add(Add(a,1),2) → Add(a,3)`) compares two syntactically distinct uninterpreted terms that QED can model as unequal, making it a genuine fundamental limitation rather than a fixable encoding gap.

### `ReplaceDeduplicateWithAggregate` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3343-3368
- Attempts used: 21
- Last updated: 2026-10-12T15:12:06.769323+00:00
- Reason / notes: The rule's soundness is a non-deterministic equivalence — both `Deduplicate(keys)` and `Aggregate(keys, First)` emit one row per key but each picks an arbitrary (implementation-defined) representative for the non-key columns — so it is equality up to representative choice, not the deterministic bag equality QED decides. QED models every aggregate as an opaque deterministic function of its input bag and knows nothing about its returned value, and `Deduplicate` is a Spark-internal operator whose bespoke pick-a-representative semantics QED cannot see through as an uninterpreted function, so the only core-language encodings of the before-side are either textually identical to the after-side (vacuous) or a differently-named picker the SMT solver refutes, with keys=all-columns collapsing to the already-proven DistinctToGroupBy.

