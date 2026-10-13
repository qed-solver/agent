# RuleScript porting progress

_Last updated: 2026-10-13T18:58:37.358081+00:00_

**78/129 rules proved** (0 failed, 51 skipped as out of QED's supported fragment).

| Rule | Backend | Status | Scope | Attempts | Notes |
|---|---|---|---|---|---|
| `BooleanSimplification` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 7 | The encoding faithfully captures the "common factor elimination for conjunction" sub-case of Spark's BooleanSimplification — `(A∨B) ∧ (A∨... |
| `CleanupDynamicPruningFilters` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 25 | The proof is non-vacuous and faithful to the rule's core: before() differs from after() only by the probe-side filter, and the rewrite is... |
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
| `EliminateSerialization` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 22 | The encoding faithfully abstracts the rule's `DeserializeToObject`-over-`TypedFilter` case: the scan is the serialized child, the uninter... |
| `EliminateWindowPartitions` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 64 | The encoding faithfully captures the exact invariant the Spark rule relies on — that a foldable (constant) component of a partition/group... |
| `ExtractGroupingPythonUDFFromAggregate` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 7 | The encoding correctly models the rule's core transformation—pulling an uninterpreted UDF g out of a grouping expression into a Project b... |
| `ExtractPythonUDFFromJoinCondition` | Apache Spark Catalyst | ✅ PROVED | FULL | 5 | The encoding is non-trivial (before is an INNER join with conjunctive condition `and(udf, rest)`; after is an INNER join with condition `... |
| `ExtractPythonUDFs` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 10 | The encoding faithfully captures the core semantic transformation of ExtractPythonUDFs for its declared partial scope: a UDF computation ... |
| `FinishAnalysis` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 8 | FinishAnalysis as a whole is a composite of ~19 heterogeneous rewrites (several members — ComputeCurrentTime, InsertMapSort*, subquery/vi... |
| `FoldablePropagation` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 6 | The encoding faithfully captures the core constant-substitution step of Spark's FoldablePropagation: a Project introduces a 0-arity (fold... |
| `GenerateOptimization` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 45 | The encoding faithfully captures the minimal non-trivial case of the JsonTuple pruning branch: a row-preserving generate with 2 uninterpr... |
| `GroupBasedRowLevelOperationScanPlanning` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 6 | before() and after() are genuinely structurally different (the target-only conjuncts live inside the join condition vs. in a filter above... |
| `InferFiltersFromConstraints` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 23 | The encoding is a non-trivial, honest PARTIAL special case of Spark's `InferFiltersFromConstraints`: it correctly models the core mechani... |
| `InjectRuntimeFilter` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 43 | before() = L ⋈ R and after() = (L SEMI-join R) ⋈ R are structurally different and the equivalence is a genuine non-vacuous theorem (semi-... |
| `NullDownPropagation` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 26 | before() and after() are structurally and semantically distinct (Filter(IS_NULL(LT(x,y))) vs Filter(OR(IS_NULL(x), IS_NULL(y)))) over two... |
| `NullPropagation` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 26 | The encoding is a faithful, non-vacuous instance of the source rule's `IsNull(c) if !c.nullable → Literal(false)` branch: the non-nullabl... |
| `ObjectSerializerPruning` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 7 | The encoding faithfully captures the rule's unconditional core rewrite — `Project(_, SerializeFromObject)` where the outer project uses o... |
| `OptimizeExpand` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 46 | The encoding is non-vacuous (after() inserts the dedup aggregate below the union-all-of-projections Expand, exactly the rule's rewrite) a... |
| `OptimizeIn` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 9 | The before() (filter `Or(eq, eq)`) and after() (filter `eq`) are genuinely distinct shapes, so the proof is non-vacuous; the single share... |
| `OptimizeJoinCondition` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 26 | The encoding proves exactly the rule's semantic core — `x = y OR (x IS NULL AND y IS NULL)` ≡ `x IS NOT DISTINCT FROM y` (Spark's `EqualN... |
| `OptimizeOneRowPlan` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 29 | `before()` (group-only `Aggregate` over the scan) and `after()` (`Project` of the key column) are structurally different, and the equival... |
| `OptimizeOneRowRelationSubquery` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 48 | The encoding faithfully captures the uncorrelated scalar-subquery branch's relational-algebra shape (inner cross-join with a one-row rela... |
| `OptimizeSubqueries` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 50 | The encoding faithfully captures the Exists case's projection step: before() is a SEMI join of L with a 3-column right side R where the (... |
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
| `PushFoldableIntoBranches` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 106 | The encoding is non-vacuous and faithful to its stated fragment: before() = U(CASE(cond,t,f)) vs after() = CASE(cond,U(t),U(f)) are struc... |
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
| `ReplaceDeduplicateWithAggregate` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 44 | The encoding faithfully captures the genuine special case where all child output columns are dedup keys, reducing the Spark rule to DISTI... |
| `ReplaceDistinctWithAggregate` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 5 | The encoding is faithful and non-vacuous: `before()` (set-variant self-intersection, i.e. set(R)) and `after()` (GROUP BY on all columns ... |
| `ReplaceExceptWithAntiJoin` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 11 | The encoding faithfully models Spark's rule for a 2-column instance: `a.minus(false,b)` is the set-difference `EXCEPT` (matching `Except(... |
| `ReplaceExceptWithFilter` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 53 | The encoding faithfully mirrors the rule's core: `Except(left,right,false)` → `minus(false, …)` on a shared base (matching the rule's "sa... |
| `ReplaceIntersectWithSemiJoin` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 25 | The encoding faithfully mirrors every operator choice in the rule — `intersect(false)` for INTERSECT DISTINCT, `JoinRelType.SEMI` for Lef... |
| `ReplaceNullWithFalseInPredicate` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 23 | before() (Filter(And(p, NULL), R)) and after() (Filter(And(p, FALSE), R)) are structurally distinct, and the proof is of the genuine univ... |
| `RewriteCorrelatedScalarSubquery` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 62 | The encoding is a narrow but honest partial instance: for an outer aggregate whose scalar subquery is a single equality-correlated, non-a... |
| `RewriteDistinctAggregates` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 25 | The encoding faithfully captures the source rule's no-grouping / foldable-argument special case (COUNT(DISTINCT literal)): before() is a ... |
| `RewriteLateralSubquery` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 52 | The encoding is non-vacuous and symbol-correct: `before()` is a `Correlate(LEFT, Filter(lateral_cond, S))` while `after()` is a plain `Jo... |
| `RewritePredicateSubquery` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 0 |  |
| `RowLevelOperationRuntimeGroupFiltering` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 86 | The encoding faithfully captures the rule's core invariant — injecting a runtime group filter (semi-join of the scan against the key set ... |
| `SimplifyBinaryComparison` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 43 | The proof is non-vacuous (Filter(a=a) vs Filter(true) are structurally different), and the `a, a` symbol sharing is exactly the rule's tr... |
| `TransposeWindow` | Apache Spark Catalyst | ✅ PROVED | PARTIAL | 64 | The encoding faithfully models each window as a partition-keyed aggregate joined back to the shared source, correctly bakes in the key pr... |
| `CheckCartesianProducts` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 11 | CheckCartesianProducts is a pure validator — its only effects are identity (plan passes through) or throwing joinConditionMissingOrTrivia... |
| `CleanUpTempCTEInfo` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 17 | CleanUpTempCTEInfo only clears the plan-metadata field CTERelationDef.originalPlanWithPredicates (Some→None) and changes none of the rows... |
| `CollapseGroupedSumOfCount` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 27 | RelRN can express the two aggregate shapes, but QED serializes every aggregate call as an uninterpreted operator and only reasons about e... |
| `CombineApproximatePercentiles` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 24 | The rule is sound only because Spark’s fused ApproximatePercentile has a backend-specific decomposition property: element i of the fused ... |
| `CombineConcats` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 21 | The rule's entire content is the identity concat(a, concat(b, c)) ≡ concat(a, b, c) (plus the same-shape cast pushdown cast(concat(a,b)) ... |
| `DecimalAggregates` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 9 | The claim holds: every arm rests on decimal arithmetic QED cannot know — aggregates are equatable only via bag-equality of their inputs (... |
| `EliminateLimits` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 24 | QED deliberately gives Sort/Limit/Offset no bag-semantic meaning — per qed.pdf, Limit is only expressible as the uninterpreted QOp(Limit,... |
| `EliminateOffsets` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 12 | All three case laws require the prover to know Offset's positional "skip the first k rows" semantics — an ordered-list property that QED'... |
| `EliminateSorts` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 11 | Every case of EliminateSorts rewrites a `Sort` node itself (dropping it or trimming its keys), so any before-pattern must contain a Sort,... |
| `ExtractExternalUDFFromWindow` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 11 | The rule rewrites a `Window` node into a `Project` over a differently-defined `Window`, and its correctness hinges on the Window operator... |
| `ExtractPythonUDFFromAggregate` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 25 | The rule's before-plan is a Spark `Aggregate` whose output column is an arbitrary "aggregate expression" `f(g, α)` mixing grouping keys a... |
| `ExtractPythonUDTFs` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 29 | The rule is a pure execution-strategy specialization: before (Generate(PythonUDTF,…)) and after (BatchEvalPythonUDTF/ArrowEvalPythonUDTF)... |
| `InferFiltersFromGenerate` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 27 | The rewrite's soundness rests on the generator's internal list semantics — Explode/Inline emits exactly zero rows iff its input is NULL o... |
| `InferWindowGroupLimit` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 21 | The rewrite's soundness rests on ordinal semantics: row_number/rank/dense_rank over an expanding (UNBOUNDED PRECEDING, CURRENT ROW) frame... |
| `InlineCTE` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 42 | CTE inlining fundamentally requires a let/named-subrelation construct — a reference node whose value is defined by another subplan — and ... |
| `LikeSimplification` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 23 | Every rewrite in LikeSimplification (LIKE 'p%' → StartsWith, '%s' → EndsWith, '%m%' → Contains, 'p%q' → Length>=k ∧ StartsWith ∧ EndsWith... |
| `LimitPushDown` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 13 | Every case of this rule rests on Spark's non-bag limit semantics (Limit(n, X) denotes an arbitrary subset of ≤ n rows, possibly in order)... |
| `LimitPushDownThroughWindow` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 11 | The rewrite `Limit(k, Window(R))` ⇒ `Window(Limit(k, Sort(R)))` is sound only by a positional invariant — with empty partition specs and ... |
| `MarkSingleTaskExecution` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 14 | MarkSingleTaskExecution's only effect is setting a `TreeNodeTag` planning hint on a private clone — it transforms zero rows, and RuleScri... |
| `MergeSubplans` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 49 | The rule's essence is hoisting duplicated one-row subplans into a shared CTE (WithCTE/CTERelationDef/CTERelationRef, evaluate-once), but ... |
| `NormalizeFloatingNumbers` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 23 | The equi-join and NAAJ branches' soundness rests entirely on relating the normalization function f to the join's equality semantics (f(x)... |
| `OptimizeCsvJsonExprs` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 30 | Each sub-rewrite asserts a parser-specific identity, e.g. `JsonToStructs(StructsToJson(x)) = x`, `π_i(JsonToStructs(x, fullSchema)) = Jso... |
| `OptimizeMetadataOnlyDeleteFromTable` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 8 | All four plan nodes involved (ReplaceData/WriteDelta with a DELETE command before, DeleteFromTableWithFilters/DeleteFromTable after) are ... |
| `OptimizeMetadataOnlyQuery` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 21 | The rewrite swaps a data-table scan for a *different* partition-metadata-table scan, and its soundness rests on the storage/catalog invar... |
| `OptimizeRand` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 0 | The rewrite `rand() < 1.0 -> true` / `rand() > 1.0 -> false` etc. is sound only because Rand() is known to produce values in [0, 1). QED ... |
| `OptimizeRepartition` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 26 | QED decides equivalence of row bags over Q-expressions, and that model has no notion of physical partitioning/distribution or of per-oper... |
| `OptimizeWindowFunctions` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 10 | This is a Window rewrite whose soundness rests on ordering/frame semantics (the "first row" of a non-empty-ORDER BY ROWS frame), but QED ... |
| `PushProjectionThroughLimitAndOffset` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 7 | The validity of all three cases (Project pushed under LocalLimit, under GlobalLimit∘LocalLimit, and under Offset) rests on Limit/Offset h... |
| `ReassignLambdaVariableID` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 41 | ReassignLambdaVariableID is a pure alpha-rename/ID-reallocation of bound lambda variables inside scalar expressions — an expression-synta... |
| `RemoveRedundantAliases` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 10 | The rule's entire content is naming/metadata semantics: its guard (`name == attr.name`), its purpose (preserving output schema names, inc... |
| `RemoveRedundantSorts` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 7 | The rule's two substantive rewrites are justified solely by non-bag facts: the child's output-ordering property already satisfying the so... |
| `ReorderAssociativeOperator` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 24 | The rule's correctness rests entirely on the internal algebra of integer +/× — associativity/commutativity for flatten/reorder, 0/1 ident... |
| `ReplaceUpdateFieldsExpression` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 21 | The rule inlines the definitional expansion of an uninterpreted scalar operator (UpdateFields(S, ops) → If(IsNull(S), NULL, CreateNamedSt... |
| `RewriteExceptAll` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 25 | The rule's LHS is bag/multiset EXCEPT ALL, which QED fundamentally does not model — it supports only the set variant of MINUS (the serial... |
| `RewriteIntersectAll` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 11 | The rule's left-hand side is `Intersect(left, right, true)` — bag INTERSECT ALL — and QED's Q-expression formalism has no multiset-inters... |
| `RewriteNonCorrelatedExists` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 24 | The rewrite's correctness rests entirely on Limit(1, ·) converting "Q is non-empty" into a well-defined single-row scalar-subquery result... |
| `RewriteSizeOfArrayStruct` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 89 | The rule rewrites `size(a)` into `size(GetArrayStructFields(a))`, and its soundness rests entirely on the operator-specific law that stru... |
| `RewriteWithExpression` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 49 | The rule's entire content is substituting a `With`/`CommonExpressionRef` let-binder by its definition, but neither the RuleScript scalar ... |
| `SchemaPruning` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 46 | The rule's before- and after-plans scan two distinct leaf relations (the full-schema HadoopFsRelation and its copy with a pruned data/met... |
| `SimplifyCaseConversionExpressions` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 46 | The rule's only step is the pointwise idempotence laws Upper(Upper(x))⇒Upper(x) and Lower(Lower(x))⇒Lower(x), whose validity rests entire... |
| `SimplifyCasts` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 27 | Every non-trivial case of SimplifyCasts is sound only because of cast algebra — same-type cast being the identity, widening casts absorbi... |
| `SimplifyConditionals` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 72 | SimplifyConditionals' actual content rests on three-valued conditional semantics — the `cond.nullable` guard, the `EqualNullSafe(cond, TR... |
| `SimplifyDateTimeConversions` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 12 | Both cases of the rule reduce to algebraic identities over the conversion functions — case 1 needs `to_str_p(to_ts_p(x)) = x` and case 2 ... |
| `SimplifyExtractValueOps` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 10 | Every case of SimplifyExtractValueOps is a constructor/accessor identity over Spark complex types (get_struct(mk_struct(v…),i)=v(i), get_... |
| `UnwrapCastInBinaryComparison` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 32 | QED's fixed prover models casts as uninterpreted functions and comparison predicates as uninterpreted operators, with no axioms for cast ... |
| `UpdateAttributeNullability` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 27 | UpdateAttributeNullability is a static type-metadata (nullable-flag) propagation pass with no effect on bag-level query results — it only... |
| `UpdateCTERelationStats` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 8 | The rule's entire content is plan-node metadata bookkeeping — it threads a `statsMap` through the tree and calls `withNewStats` on `CTERe... |
| `V1Writes` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 10 | V1Writes is a physical planning rule, not a semantics-preserving optimization — its defining steps (a deliberately non-identity Empty2Nul... |
| `V2ScanPartitioningAndOrdering` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 9 | V2ScanPartitioningAndOrdering is a pure physical-property annotation — both passes only rewrite plan-node attributes (`keyGroupedPartitio... |
| `V2ScanRelationPushDown` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 61 | I confirmed against the full DSL source that a pushed-down scan can only be encoded as a fresh uninterpreted scan symbol, which QED must ... |
| `V2Writes` | Apache Spark Catalyst | ⏭️ SKIPPED | — | 11 | V2Writes is a write-command construction rule, and its two observable changes are (1) filling a V2 write *command* node's `write` field w... |

## Details

### `BooleanSimplification` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 396-639
- Attempts used: 7
- Last updated: 2026-10-12T00:02:53.693529+00:00
- Reason / notes: The encoding faithfully captures the "common factor elimination for conjunction" sub-case of Spark's BooleanSimplification — `(A∨B) ∧ (A∨C) ⟹ A ∨ (B∧C)` — which is exactly what the source produces for a pair of two-disjunct disjunctions sharing one factor; A is correctly shared as the common factor with B/C as distinct residuals, A/B/C are genuinely uninterpreted predicates (not baked-in concrete comparisons), the And/Or operators and nesting match, and this particular case carries no nullability precondition. before() and after() are structurally distinct (a conjunction of disjunctions vs. a disjunction containing a conjunction), so the proof is non-vacuous, and the PARTIAL scope tag is honest and specific about the shape restriction (a genuine, non-degenerate special case rather than the full rule).
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 62308208}, panicked=False

### `CleanupDynamicPruningFilters` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/dynamicpruning/CleanupDynamicPruningFilters.scala, lines 35-94
- Attempts used: 25
- Last updated: 2026-10-13T02:47:24.766503+00:00
- Reason / notes: The proof is non-vacuous and faithful to the rule's core: before() differs from after() only by the probe-side filter, and the rewrite is valid precisely because the uninterpreted DPP predicate `dpp` is enforced as a conjunct of the inner join's condition — the deliberate sharing of `dpp` between filter and join condition correctly models the dependency (join condition subsumes the DPP filter) rather than an accidental over-constraint, with no hard-coded predicates or wrong operator shapes. The restriction to this conjunctive/inner-join form is genuine (QED cannot reason about implication from an IN-subquery predicate to a join condition, as the real DPP filter would be), is accurately disclosed in the SCOPE: PARTIAL line, and still yields a useful, universally quantified soundness lemma over arbitrary L, R, and predicates. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 72360250}, panicked=False

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

### `EliminateSerialization` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/objects.scala, lines 40-72
- Attempts used: 22
- Last updated: 2026-10-13T03:12:43.225641+00:00
- Reason / notes: The encoding faithfully abstracts the rule's `DeserializeToObject`-over-`TypedFilter` case: the scan is the serialized child, the uninterpreted projection `deser` stands in for deserialization, and the shared uninterpreted predicate `cond` on the deserialized column stands in for the typed filter's condition, yielding structurally distinct plans — `Project(Filter(Row, cond(deser)))` vs `Filter(Project(Row, deser), cond)` — whose bag-semantic equivalence is a genuine, non-vacuous theorem with correct symbol sharing and the type-equality guard enforced structurally. The `SCOPE: PARTIAL` line is honest and specific: it covers exactly one of the rule's four cases (no round-trip elimination, no `AppendColumns`, no typed-filter-through-serialize case), yet within that case the row type, object type, projection, and condition form are all left fully uninterpreted, so nothing real is hard-coded.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 66134708}, panicked=False

### `EliminateWindowPartitions` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/EliminateWindowPartitions.scala, lines 28-41
- Attempts used: 64
- Last updated: 2026-10-13T03:58:48.178177+00:00
- Reason / notes: The encoding faithfully captures the exact invariant the Spark rule relies on — that a foldable (constant) component of a partition/grouping key is redundant because it never splits rows — by placing the shared `constKey` literal as a group-by key in `before()` and removing it (while re-projecting it) in `after()`, so the proof genuinely depends on `constKey` being a constant rather than being vacuous. `before()` and `after()` are structurally distinct and the equivalence holds universally over the uninterpreted source table's contents, with no over-constraining symbol reuse, no missing precondition, and the aggregate correctly left uninterpreted so the claim is independent of the specific window/agg function. The group-by vehicle is the correct bag-semantic stand-in for window partitioning (QED cannot model windows at all), and the `// SCOPE: PARTIAL` line honestly and specifically discloses this, making the result a non-degenerate, useful fragment of the real rule rather than a misleading full proof. ```

(This proof relies on an accepted RuleScript DSL extension made during this session — see the extended file(s) for what changed.)
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 88863000}, panicked=False

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

### `InjectRuntimeFilter` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/InjectRuntimeFilter.scala, lines 38-466
- Attempts used: 43
- Last updated: 2026-10-13T04:58:59.214738+00:00
- Reason / notes: before() = L ⋈ R and after() = (L SEMI-join R) ⋈ R are structurally different and the equivalence is a genuine non-vacuous theorem (semi-join redundancy), which is exactly the core soundness lemma of Spark's rule: filtering the application side by the creation side's key set drops no join-matching row, with the sides and key correspondence matching the source (filter on left built from right's keys). The modeling choices are correct rather than accidental — L and R are independent scans, EQUALS is appropriate since the source rule operates on equi-join keys, and even predicate co-identification would not make the proof vacuous since the identity holds for an arbitrary condition. The narrowing to a single equi-key INNER join and the idealization of the bloom filter as an exact semi-join (no false positives) are genuine, specific restrictions — false-positive semantics are physically inexpressible in the DSL — and both are explicitly and accurately disclosed in the SCOPE line, making this a faithful, honest PARTIAL encoding of the rule's proof obligation. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 75529750}, panicked=False

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

### `OptimizeExpand` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeExpand.scala, lines 56-127
- Attempts used: 46
- Last updated: 2026-10-13T07:12:31.672618+00:00
- Reason / notes: The encoding is non-vacuous (after() inserts the dedup aggregate below the union-all-of-projections Expand, exactly the rule's rewrite) and the proven equivalence rests on the source rule's own correctness argument: the pure group-by sitting directly above the Expand erases row multiplicities because each branch output is a function of the input row, and the semantic precondition (all Expand-produced attributes are consumed by the inner group-by) is modeled, while the threshold/size checks in the source are performance heuristics, not semantic preconditions. Branch projections use distinct uninterpreted symbols per branch shared correctly across before()/after(), and the 3-branch/4-column/count-decomposition shape is a genuine, non-degenerate special case that is honestly and specifically tagged SCOPE: PARTIAL. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 352421417}, panicked=False

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

### `OptimizeSubqueries` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 389-490
- Attempts used: 50
- Last updated: 2026-10-13T06:34:44.967158+00:00
- Reason / notes: The encoding faithfully captures the Exists case's projection step: before() is a SEMI join of L with a 3-column right side R where the (uninterpreted) join condition references only R's columns 0 and 2, and after() replaces R with R.project(columns 0,2) and adjusts the condition's field ordinals accordingly — the JoinField arithmetic checks out (L.joinField(3,R) → R.col2 maps to L.joinField(2,R2) → R2.col1 = R.col2), the same uninterpreted predicate symbol is shared correctly, and the structural difference between before and after is genuine and non-vacuous. The SCOPE: PARTIAL tag is honest: the full Spark rule has three subcases (COUNT-bug scalar subquery, Exists, generic subquery) plus top-level-sort removal, and this encodes only the Exists column-pruning step, which is a specific, non-degenerate slice of the real optimization. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 73769958}, panicked=False

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

### `PushFoldableIntoBranches` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 788-855
- Attempts used: 106
- Last updated: 2026-10-13T09:00:53.890443+00:00
- Reason / notes: The encoding is non-vacuous and faithful to its stated fragment: before() = U(CASE(cond,t,f)) vs after() = CASE(cond,U(t),U(f)) are structurally distinct, and since the equivalence would be false if "CASE" were merely uninterpreted (a simple SMT countermodel exists), QED's PROVABLE verdict confirms it modeled a genuine ite, proving the real pointwise distribution U(ite)=ite∘U for an arbitrary uninterpreted U, cond, and independent branch columns t/f (same branch type being faithful, since Spark's If requires a common branch type). Spark's supportedUnaryExpression whitelist and atMostOneUnfoldable guard are determinism/profitability heuristics, not semantic preconditions — the uninterpreted deterministic U covers exactly the whitelisted semantics, so nothing is silently assumed. The `// SCOPE: PARTIAL` line is honest and specific (unary-If form only; CaseWhen and binary-with-foldable-operand variants omitted), and the proved fragment is a genuine, non-degenerate piece of the source rule rather than a narrowed-to-identity trick.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 68953625}, panicked=False

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

### `ReplaceDeduplicateWithAggregate` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3343-3368
- Attempts used: 44
- Last updated: 2026-10-13T08:28:09.395281+00:00
- Reason / notes: The encoding faithfully captures the genuine special case where all child output columns are dedup keys, reducing the Spark rule to DISTINCT ≡ GROUP BY-all-cols-with-no-aggregates; `intersect(false, source)` correctly models set self-intersection (DISTINCT) and the `Aggregate` with all fields as group keys and empty agg-calls correctly models the corresponding GROUP BY, so the proof is non-vacuous and semantically sound. The PARTIAL scope is honest and specific: the full rule's `First()` on non-key columns relies on aggregate semantics QED cannot reason about, making this a legitimate, non-degenerate narrowing.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 70348042}, panicked=False

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

### `RewriteCorrelatedScalarSubquery` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/subquery.scala, lines 698-1085
- Attempts used: 62
- Last updated: 2026-10-13T09:03:50.259090+00:00
- Reason / notes: The encoding is a narrow but honest partial instance: for an outer aggregate whose scalar subquery is a single equality-correlated, non-aggregated one-row scan, Catalyst's rewrite reduces to replacing the correlated left join with a plain left join, which is exactly what is proved. It is non-vacuous because before uses Correlate and after uses Join, and the PARTIAL scope accurately states the omitted HAVING, projection, multi-subquery, and count-bug generality.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 159579250}, panicked=False

### `RewriteDistinctAggregates` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/RewriteDistinctAggregates.scala, lines 199-437
- Attempts used: 25
- Last updated: 2026-10-13T10:06:09.636354+00:00
- Reason / notes: The encoding faithfully captures the source rule's no-grouping / foldable-argument special case (COUNT(DISTINCT literal)): before() is a single distinct aggregate and after() reproduces Spark's Expand→dedup-aggregate→outer non-distinct-count structure, where the single-projection Expand degenerates to a plain Project and the FILTER(gid=id) clause is vacuous (only one gid is ever emitted), so omitting it is sound. The two plans are structurally different and their aggregate input bags are not trivially identical (n constant copies vs. at most one deduplicated row), so the proof is non-vacuous. The scope is honestly tagged PARTIAL with a specific condition matching the rule's own foldable-children branch.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 84178209}, panicked=False

### `RewriteLateralSubquery` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/subquery.scala, lines 1090-1099
- Attempts used: 52
- Last updated: 2026-10-13T09:59:39.200433+00:00
- Reason / notes: The encoding is non-vacuous and symbol-correct: `before()` is a `Correlate(LEFT, Filter(lateral_cond, S))` while `after()` is a plain `Join(LEFT, lateral_cond)`, and the shared predicate is applied to the identical (L,S) argument list in both (verified against `joinFields`/`JoinField` semantics), so the proof captures the rule's genuine core claim — per-row correlated evaluation against a correlation predicate is bag-equivalent to a plain join using that predicate as the join condition. The restrictions versus the full Spark rule (LEFT join only, no separate non-correlation condition, subquery side is an unchanged scan so `rewriteDomainJoins` is a no-op) are real, specific, and accurately stated in the `SCOPE: PARTIAL` line, and none of them degenerates the rule into a structural identity; the fixed join kind in particular reflects a DSL gap (no meta-join-type for `Correlate`) rather than a modeling error.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 89330959}, panicked=False

### `RewritePredicateSubquery` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/subquery.scala, lines 56-457
- Attempts used: 0
- Last updated: 2026-10-13T18:58:37.353843+00:00
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 73593792}, panicked=False

### `RowLevelOperationRuntimeGroupFiltering` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/dynamicpruning/RowLevelOperationRuntimeGroupFiltering.scala, lines 49-169
- Attempts used: 86
- Last updated: 2026-10-13T12:00:14.644906+00:00
- Reason / notes: The encoding faithfully captures the rule's core invariant — injecting a runtime group filter (semi-join of the scan against the key set of `Filter(cond, T)`) under the delta condition is a semantic no-op — with `cond` and the shared table/scan correctly modeled as the same uninterpreted symbols, `SEMI` correctly standing in for the `InSubquery` filter, and the non-nullable key assumption (genuine for equality-join soundness) both encoded and disclosed. `before()` and `after()` are structurally distinct (the semi-join node is real), so the proof is non-vacuous, and the single-key-column/direct-`Filter`/WriteDelta restrictions are honest, specific PARTIAL-scope narrowings of a rule whose soundness invariant is shape-independent.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 71521125}, panicked=False

### `SimplifyBinaryComparison` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 649-697
- Attempts used: 43
- Last updated: 2026-10-13T12:17:49.620482+00:00
- Reason / notes: The proof is non-vacuous (Filter(a=a) vs Filter(true) are structurally different), and the `a, a` symbol sharing is exactly the rule's trigger (`semanticEquals` on both sides), not an accidental over-constraint — had the porter used two independent fields, the rewrite would be unsound and unprovable. The concrete `SqlStdOperatorTable.EQUALS` is the correct choice rather than an under-generalization: the `x = x → true` rewrite depends on equality's reflexivity, which an uninterpreted predicate on the diagonal could not establish, and the required non-nullability precondition is encoded via the non-nullable column type. The `SCOPE: PARTIAL` line is accurate and specific — only the EqualTo self-comparison-to-true case, in a filter context, under the static-typing (non-nullable) subcondition rather than the `IsNotNull`-conjunct path — so this is an honestly labeled, genuine, non-degenerate special case of the source rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 267708}, panicked=False

### `TransposeWindow` — ✅ PROVED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2159-2185
- Attempts used: 64
- Last updated: 2026-10-13T13:52:27.543114+00:00
- Reason / notes: The encoding faithfully models each window as a partition-keyed aggregate joined back to the shared source, correctly bakes in the key precondition (w1's aggregate is computed over the base relation, not over w2's output, matching `w1.references ∩ w2.windowOutputSet = ∅`), satisfies the partition-subset compatibility via (a) ⊂ (a,b), and uses distinct uninterpreted functions for the two windows; `before()` and `after()` genuinely differ in join order, so the proof captures the real transpose (commutation of the two window operations) rather than a vacuous identity. The narrowings (concrete (a)/(a,b) partition shape, whole-partition aggregates with no frame/ordering, and only case 1 of the two-case rule) are real but are honestly flagged in the `// SCOPE: PARTIAL` line and leave a non-degenerate, meaningful result.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 93054000}, panicked=False

### `CheckCartesianProducts` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3160-3183
- Attempts used: 11
- Last updated: 2026-10-13T02:24:02.152650+00:00
- Reason / notes: CheckCartesianProducts is a pure validator — its only effects are identity (plan passes through) or throwing joinConditionMissingOrTrivialError, so there is no successful post-rewrite plan and hence no non-vacuous before/after pair for QED to decide equivalence on; the only encodable candidate would be an identity join ⟹ join, which ignores the rule's content entirely. Compounding this, the rule's trigger is a syntactic property of the predicate term (whether its conjuncts' references span both the left and right output sets), which QED's encoding cannot even inspect, since uninterpreted predicates are opaque boolean functions whose argument-reference sets carry no semantic meaning in the prover. The DSL and JSONSerializer expose only relational operators (scan/filter/project/join/correlate/group/union/intersect/except/sort/values) with no throw/abort/guard node, so the rule's defining behavior is unrepresentable rather than mis-modeled. ```

### `CleanUpTempCTEInfo` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushdownPredicatesAndPruneColumnsForCTEDef.scala, lines 276-282
- Attempts used: 17
- Last updated: 2026-10-13T02:30:06.114111+00:00
- Reason / notes: CleanUpTempCTEInfo only clears the plan-metadata field CTERelationDef.originalPlanWithPredicates (Some→None) and changes none of the rows the plan produces, so under QED's bag-semantic model the before/after plans are the same relation and the metadata difference is neither representable nor observable (and the DSL has no CTE operator to distinguish the two node states). The only faithful encoding is therefore a vacuous before()==after() identity — precisely the degenerate proof the reviewer rejected — leaving no non-trivial semantic equivalence for QED to establish, so the UNSUPPORTED conclusion is correct. ```

### `CollapseGroupedSumOfCount` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/CollapseGroupedSumOfCount.scala, lines 35-124
- Attempts used: 27
- Last updated: 2026-10-13T02:42:28.217097+00:00
- Reason / notes: RelRN can express the two aggregate shapes, but QED serializes every aggregate call as an uninterpreted operator and only reasons about equality of its input bag, not about COUNT/SUM arithmetic. The Spark rewrite depends on the universal identity that summing one COUNT(1) per fine group over a coarser group equals summing/counting 1 over all raw rows in that coarse group. That identity is not a consequence of bag equality for arbitrary uninterpreted aggregate functions, so no faithful encoding is provable.

### `CombineApproximatePercentiles` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/CombineApproximatePercentiles.scala, lines 73-225
- Attempts used: 24
- Last updated: 2026-10-13T02:45:45.836720+00:00
- Reason / notes: The rule is sound only because Spark’s fused ApproximatePercentile has a backend-specific decomposition property: element i of the fused multi-percentage result equals the standalone call for percentage i, combined with array-indexing semantics. QED treats aggregates and scalar expressions as uninterpreted symbols with only bag-input equality and has no list/array element semantics, so it cannot prove or even meaningfully encode that identity without assuming the conclusion.

### `CombineConcats` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 1343-1376
- Attempts used: 21
- Last updated: 2026-10-13T02:54:41.536157+00:00
- Reason / notes: The rule's entire content is the identity concat(a, concat(b, c)) ≡ concat(a, b, c) (plus the same-shape cast pushdown cast(concat(a,b)) ≡ concat(cast(a), cast(b))), which holds only by string-concatenation associativity; QED translates all generic projection operators (concat, cast) into uninterpreted function symbols over integer-sorted rows with no algebraic axioms, so differently shaped terms like f(f(a,b),c) vs g(a,b,c) are not validly equivalent (a countermodel in the theory of uninterpreted functions always exists, with different arities/nesting being distinct symbols). No non-trivial special case can escape this, since every instance of flattening changes the term's nesting or arity, leaving only degenerate identity shapes — this is precisely the "operator's bespoke internal semantics QED cannot see through as an uninterpreted function" limitation, not a missing DSL capability. ```

### `DecimalAggregates` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3191-3290
- Attempts used: 9
- Last updated: 2026-10-13T02:52:02.819918+00:00
- Reason / notes: The claim holds: every arm rests on decimal arithmetic QED cannot know — aggregates are equatable only via bag-equality of their inputs (and every arm changes that input bag, e.g. Min(Cast(x)) vs Cast(Min(x)), where the cast is an uninterpreted function with no monotonicity/injectivity, so the commutation isn't even valid in QED's theory), and Cast/MakeDecimal/UnscaledValue/Divide are uninterpreted symbols with no arithmetic or ordering, making the Sum/Average scaling-and-division identities unprovable by construction. The window arm additionally needs window/list semantics QED explicitly lacks, and this is a real QED limitation rather than a missing DSL builder: the prover is a fixed Rust project whose JSON vocabulary has no way to attach numeric axioms (monotone, scaling, power-of-10 division) to uninterpreted functions, so no Java-side extension could make any arm provable. ```

### `EliminateLimits` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3098-3124
- Attempts used: 24
- Last updated: 2026-10-13T02:57:46.203889+00:00
- Reason / notes: QED deliberately gives Sort/Limit/Offset no bag-semantic meaning — per qed.pdf, Limit is only expressible as the uninterpreted QOp(Limit, n, Q) with no axioms — so `Limit(0,Q)=∅` and `Limit(a,Limit(b,Q))=Limit(min(a,b),Q)` are not derivable for an uninterpreted function, and the maxRows≤n elimination case additionally needs a cardinality side condition that RRule's bare before/after interface cannot express and which makes the unguarded universal statement false. This is corroborated by the DSL ground truth: `RelRN.java` exposes no Limit/Sort builder at all, and `extend_dsl_file` could at best surface QED's uninterpreted QOp, which the prover's bag-based theory (its explicit "List semantic" limitation) cannot reason about — a genuine QED limitation, not a missing or mismodeled encoding. ```

### `EliminateOffsets` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3134-3144
- Attempts used: 12
- Last updated: 2026-10-13T02:58:57.457525+00:00
- Reason / notes: All three case laws require the prover to know Offset's positional "skip the first k rows" semantics — an ordered-list property that QED's bag model explicitly lacks (Sort/Limit/Offset have no bag-semantic meaning and would be uninterpreted), and case 2 additionally needs static cardinality (maxRows) bounding over the child, which QED does not reason about. No core-language operator can express "drop the first k rows," so even the most degenerate special case (e.g., Offset over an empty relation) still hinges on the unmodeled Offset semantics, and adding a DSL builder would confer no decidability since the fixed prover has no bag interpretation for it — matching qed.pdf §6.2's stated limitation. ```

### `EliminateSorts` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2430-2502
- Attempts used: 11
- Last updated: 2026-10-13T03:07:47.984739+00:00
- Reason / notes: Every case of EliminateSorts rewrites a `Sort` node itself (dropping it or trimming its keys), so any before-pattern must contain a Sort, and the rule's soundness rests on ordering facts — empty/constant-only keys make a sort a no-op, join output has no defined order, and aggregation destroys input order — none of which QED's bag-semantic model captures; qed.pdf's own failure analysis (§6.2 "List semantics") states QED does not support ordering semantics of Sort/Limit/Offset, so a Sort node would be an uninterpreted, axiom-free operator and no equation like `Sort(x) ≡ x` or `Join(Sort(l), Sort(r)) ≡ Join(l, r)` is derivable. Even the degenerate "Sort with zero keys → child" case is unprovable, since it *is* the bag-identity claim for an operator the prover has no interpretation of, so adding a `RelRN.sort` builder (the JSON format can carry sort nodes, but the prover can't) would close no gap. This is consistent with the AGREE-d EliminateLimits/EliminateOffsets Spark precedents, where the fixed prover's lack of a bag interpretation for the operator in question was likewise confirmed as the fundamental barrier.

### `ExtractExternalUDFFromWindow` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/externalUDF/ExtractExternalUDFFromWindow.scala, lines 33-71
- Attempts used: 11
- Last updated: 2026-10-13T03:13:00.290565+00:00
- Reason / notes: The rule rewrites a `Window` node into a `Project` over a differently-defined `Window`, and its correctness hinges on the Window operator evaluating each window expression to the same per-row value under a fixed partition/order/frame, so that an external UDF applied above the window sees the identical input. QED reasons purely under bag semantics and has no model for list/ordering operators — `Window` (like `Sort`/`Limit`/`Sample`) carries no bag meaning and has no Q-expression/JSON form the unmodifiable Rust prover could interpret — and the two distinct Window invocations (one computing `externalUDF(w)`, the other computing bare `w`) cannot be related by any uninterpreted-symbol trick, so no DSL extension or narrower special case can render this provable. ```

### `ExtractPythonUDFFromAggregate` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/python/ExtractPythonUDFs.scala, lines 39-85
- Attempts used: 25
- Last updated: 2026-10-13T03:37:08.361091+00:00
- Reason / notes: The rule's before-plan is a Spark `Aggregate` whose output column is an arbitrary "aggregate expression" `f(g, α)` mixing grouping keys and inner aggregate calls, but RuleScript's `Aggregate` only emits group keys plus flat `AggCall`s, so the only expressible before-plan is `Project(f(g, α), Aggregate(g; α))` — textually identical to the after-plan, i.e. a vacuous self-identity rather than the rule. Extending the DSL would not close this: any distinct representation of the composite output would be modeled by QED as an uninterpreted aggregate symbol, and QED has no definitional knowledge that Spark's aggregate expression `f(g, α)` means "f applied per-group on top of the inner aggregate α" (a backend operator's bespoke internal semantics), so a non-vacuous before/after pair would pit independent uninterpreted symbols against each other and never be provable. The rule's actual justification — a physical constraint that Python UDFs be evaluated only after aggregation — is likewise outside QED's bag semantics entirely.

### `ExtractPythonUDTFs` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/python/ExtractPythonUDFs.scala, lines 401-420
- Attempts used: 29
- Last updated: 2026-10-13T03:37:20.412295+00:00
- Reason / notes: The rule is a pure execution-strategy specialization: before (Generate(PythonUDTF,…)) and after (BatchEvalPythonUDTF/ArrowEvalPythonUDTF) carry the identical child, requiredChildOutput, and generatorOutput, so the only real proof obligation — that the two physical Python-eval nodes compute the same rows as the logical Generate — is a claim about backend operators' bespoke internal semantics that QED cannot see through as uninterpreted functions. Additionally, a UDTF generate is a per-row table/list-producing operator, and neither the core language nor QED's JSON format (scan/filter/project/join/correlate/aggregate/set-ops/sort only) has any operator to model it, so any expressible encoding would reduce both sides to the same shape and prove only a tautology, not the rule's actual content. ```

### `InferFiltersFromGenerate` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2191-2227
- Attempts used: 27
- Last updated: 2026-10-13T04:00:25.360942+00:00
- Reason / notes: The rewrite's soundness rests on the generator's internal list semantics — Explode/Inline emits exactly zero rows iff its input is NULL or an empty list — and QED's bag-semantic theory over uninterpreted functions has no list type, no row-generation/unnest operator, and no axiom connecting a (necessarily uninterpreted) Generate operator to the `size(x) > 0 AND x IS NOT NULL` predicate, so any encoding in which Generate is an uninterpreted operator admits an SMT countermodel. The relational workaround of decomposing Explode into a join with an element-membership table also fails: the filter the rule inserts would need a "has at least one element" existential/size condition that the core language cannot express (no exist/subquery connectives, and QED cannot derive count=0 ⟺ empty input for aggregates), so this is qed.pdf's explicit "List semantics" limitation of the unmodifiable prover, not a missing builder that extend_dsl_file could close. ```

### `InferWindowGroupLimit` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/InferWindowGroupLimit.scala, lines 54-138
- Attempts used: 21
- Last updated: 2026-10-13T03:49:53.300238+00:00
- Reason / notes: The rewrite's soundness rests on ordinal semantics: row_number/rank/dense_rank over an expanding (UNBOUNDED PRECEDING, CURRENT ROW) frame under a non-empty ORDER BY, plus the property that truncating each partition to its first k rows (WindowGroupLimit) leaves the rank values of all rows with rank ≤ k unchanged — and QED's decision procedure operates over bag semantics with uninterpreted functions, with no model of ordering, window frames, or rank (Sort/Limit/Window are explicitly excluded). Since the needed invariant is a statement about the internal behavior of those operators, encoding Window/WindowGroupLimit as uninterpreted symbols would leave before/after as unrelated opaque functions with no algebraic relationship for the prover to discharge, and extending the DSL can't close this either because the trusted, unchangeable Rust prover itself lacks list/ordering semantics. ```

### `InlineCTE` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/InlineCTE.scala, lines 58-339
- Attempts used: 42
- Last updated: 2026-10-13T04:34:10.081226+00:00
- Reason / notes: CTE inlining fundamentally requires a let/named-subrelation construct — a reference node whose value is defined by another subplan — and QED's relational fragment and JSON grammar contain no such node (JSONSerializer handles only scan/values/filter/project/join/correlate/group/union/intersect/except/sort, and RelRN/RexRN expose no builder for it), so every encoding is either refuted (reference as an independent uninterpreted table) or a vacuous identity (body written inline, making before ≡ after). This gap is in the fixed Rust prover's fragment itself, not a missing Java builder that `extend_dsl_file` could close, since no existing JSON node kind carries let semantics and inventing a new one is off-limits.

### `LikeSimplification` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 878-984
- Attempts used: 23
- Last updated: 2026-10-13T04:37:53.926800+00:00
- Reason / notes: Every rewrite in LikeSimplification (LIKE 'p%' → StartsWith, '%s' → EndsWith, '%m%' → Contains, 'p%q' → Length>=k ∧ StartsWith ∧ EndsWith, 'x' → =, and the LikeAll/Any fan-out to AND/OR) is an entailment between distinct predicate symbols that QED encodes as independent uninterpreted functions, and the prover has no string theory or axioms linking a glob pattern to the string built-ins — so no encoding of before/after can make an SMT model separate the two sides. The `p%q` case additionally bakes pattern-content-derived constants (byte/code-point length guard) and the multi-LIKE cases depend on the bespoke semantics of LikeAll/LikeAny, which QED cannot see through as uninterpreted n-ary operators. Sharing a symbol name between the two sides would only prove a tautology, and a DSL extension cannot help because new operators still serialize as uninterpreted names and string semantics would have to live in the off-limits prover. ```

### `LimitPushDown` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 926-1030
- Attempts used: 13
- Last updated: 2026-10-13T04:42:00.892804+00:00
- Reason / notes: Every case of this rule rests on Spark's non-bag limit semantics (Limit(n, X) denotes an arbitrary subset of ≤ n rows, possibly in order), which QED explicitly does not model — Limit/Offset have no bag-semantic meaning and at best surface as uninterpreted operators with no axioms, so SMT has nothing to relate two distinct limit expressions. Worse, most cases are simply not bag equalities under any deterministic semantics: e.g. `Limit(n, A ⋈ B) → Limit(n, Limit(n, A) ⋈ Limit(n, B))` changes row counts, and `Limit(1, GroupBy(X)) → Limit(1, Project(keys, Limit(1, X)))` loses de-duplication and can select different group rows, so no correct bag-equivalence encoding exists. Adding a Limit builder (the JSON serializer can already carry sort/limit fields) would only expose the axiom-free uninterpreted QOp, confirming this is a fundamental prover limitation, not a missed encoding. ```

### `LimitPushDownThroughWindow` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/LimitPushDownThroughWindow.scala, lines 32-59
- Attempts used: 11
- Last updated: 2026-10-13T04:46:49.172884+00:00
- Reason / notes: The rewrite `Limit(k, Window(R))` ⇒ `Window(Limit(k, Sort(R)))` is sound only by a positional invariant — with empty partition specs and `(UNBOUNDED PRECEDING, CURRENT ROW)` frames, the row_number/rank/dense_rank values of the first k rows in `orderSpec` order depend only on those k rows, so truncating before the window preserves exactly the surviving rows' rank labels. QED models bag semantics only and explicitly lacks list/ordering semantics: `Sort`, `Limit`, and `Window` carry no bag-semantic meaning in Q-expressions and would be, at best, uninterpreted operators with no axioms (qed.pdf §6.2), so the needed identity is a non-universal commutation claim between uninterpreted functions that an SMT solver refutes under some instantiation, and extending the DSL with Sort/Limit/Window builders could not help because the fixed prover has no theory of row position or window frames to interpret them. There is genuinely no bag-semantic fragment of this rule left for the prover to discharge, and the physical guards (`maxRows`, `topKSortFallbackThreshold`) are heuristics rather than logic, so the UNSUPPORTED conclusion is correct.

### `MarkSingleTaskExecution` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/MarkSingleTaskExecution.scala, lines 49-177
- Attempts used: 14
- Last updated: 2026-10-13T04:50:39.105806+00:00
- Reason / notes: MarkSingleTaskExecution's only effect is setting a `TreeNodeTag` planning hint on a private clone — it transforms zero rows, and RuleScript's core language has no representation for plan metadata/annotations, so the tagged and untagged plans serialize to identical Q-expressions. QED decides bag-semantic equivalence of output rows and cannot model the config flags, the LocalRelation row-count bounds, or the physical planner behavior that give the tag its meaning, so the only encodable form is a vacuous `before()==after()` identity with nothing genuine to prove. This is a fundamental mismatch between a physical planning-hint rule and a logical bag-equivalence prover, not a DSL API gap that `extend_dsl_file` could close (any tag carrier would have to map to a semantically neutral RelNode, yielding the same vacuity).

### `MergeSubplans` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/planmerging/MergeSubplans.scala, lines 151-341
- Attempts used: 49
- Last updated: 2026-10-13T05:33:16.263723+00:00
- Reason / notes: The rule's essence is hoisting duplicated one-row subplans into a shared CTE (WithCTE/CTERelationDef/CTERelationRef, evaluate-once), but I verified JSONSerializer's Rel switch only supports scan/values/filter/project/join/correlate/group/union/intersect/except/sort with default->throw, so no let/CTE node kind exists in the fixed fragment and no Java-side extension can serialize into one. Additionally, the rewrite is bag-semantically transparent (shared reuse is only physically observable), and the reference path GetStructField(ScalarSubquery(CTERef), i) over CreateNamedStruct would require a get∘pack identity that is unprovable for uninterpreted functions — so any encoding is either refuted or collapses to the vacuous before ≡ after identity, consistent with the prior InlineCTE (inverse rule) exhaustion. ```

### `NormalizeFloatingNumbers` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/NormalizeFloatingNumbers.scala, lines 67-201
- Attempts used: 23
- Last updated: 2026-10-13T05:16:00.107705+00:00
- Reason / notes: The equi-join and NAAJ branches' soundness rests entirely on relating the normalization function f to the join's equality semantics (f(x)=f(y) coinciding with x=y under IEEE-754 float equality, −0.0≡0.0, NaN≢NaN), but QED's model reduces all values to integers with f and the join condition as universally-quantified uninterpreted symbols — the required relation is not valid for arbitrary f (a constant f breaks it), cannot be added as an axiom without presupposing the conclusion, and no narrower special case can constrain f since QED quantifies universally over it; the window-partition branch additionally requires window/ordering semantics that QED does not model at all. ```

### `OptimizeCsvJsonExprs` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeCsvJsonExprs.scala, lines 44-484
- Attempts used: 30
- Last updated: 2026-10-13T05:14:38.921673+00:00
- Reason / notes: Each sub-rewrite asserts a parser-specific identity, e.g. `JsonToStructs(StructsToJson(x)) = x`, `π_i(JsonToStructs(x, fullSchema)) = JsonToStructs(x, prunedSchema)`, or `GetStructField(MultiGetJsonObject(x, paths), i) = GetJsonObject(x, path_i)`. QED treats scalar/parser operators as uninterpreted with opaque outputs and has no axioms for Spark's JSON/CSV parser algebra, so no RuleScript encoding can make those equivalences universally provable.

### `OptimizeMetadataOnlyDeleteFromTable` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/v2/OptimizeMetadataOnlyDeleteFromTable.scala, lines 39-128
- Attempts used: 8
- Last updated: 2026-10-13T05:22:35.116657+00:00
- Reason / notes: All four plan nodes involved (ReplaceData/WriteDelta with a DELETE command before, DeleteFromTableWithFilters/DeleteFromTable after) are side-effecting DML commands with no output row bag, so the rewrite's meaning is an effect-on-storage statement, not a relation transformation — QED's decision procedure is bag equivalence of read-side relational expressions and has no domain for command effects, and an `extend_dsl_file` can't fix that since the fixed Rust prover only interprets query operators. Even re-casting the rule as equivalence of post-delete table states would require proving the engine-specific `translateFilterV2` translation is semantically equivalent to the original condition, which QED cannot do for independent uninterpreted predicates, while the `canDeleteWhere`/`TruncatableTable` capability guards and the no-subquery/translation-success preconditions are inexpressible in an unconditional RuleScript pattern pair; the only QED-provable fragment (the `cond == TrueLiteral` truncate case reducing to `Filter(False, T) ≡ Empty`) is a vacuous tautology that captures none of the rule's actual content.

### `OptimizeMetadataOnlyQuery` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/OptimizeMetadataOnlyQuery.scala, lines 46-182
- Attempts used: 21
- Last updated: 2026-10-13T05:42:23.677674+00:00
- Reason / notes: The rewrite swaps a data-table scan for a *different* partition-metadata-table scan, and its soundness rests on the storage/catalog invariant that the metadata table holds exactly one row per distinct partition of the data table — but QED models two differently-named scans as independent uninterpreted relations that are universally quantified over, and its per-table `guaranteed` constraints are Rex expressions over a single table's columns only, so no cross-table "metadata = distinct(data)" relation can be asserted, meaning the equivalence must (falsely) hold for arbitrary independent instantiations. Even if the after-side were recast as a DISTINCT over the *same* scan, the rule would still require Max/Min/Count-Distinct to be multiplicity-insensitive (its result over the full bag equals its result over the bag's distinct elements) — an aggregate-algebra identity the prover explicitly does not model, since it only equates aggregate results when the input bags are provably equal, and here the bags differ. Both gaps are in the unmodifiable prover's semantics (independent-scan quantification + uninterpreted aggregates), not a missing DSL operator, so extending the DSL (a Distinct operator or a cross-table constraint) could not close them and a faithful encoding is genuinely unprovable.

### `OptimizeRand` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeRand.scala, lines 33-63
- Attempts used: 0
- Last updated: 2026-10-13T18:58:37.356556+00:00
- Reason / notes: The rewrite `rand() < 1.0 -> true` / `rand() > 1.0 -> false` etc. is sound only because Rand() is known to produce values in [0, 1). QED treats a nondeterministic scalar function as an opaque uninterpreted function with no range axiom, and the only place the prover accepts value-range facts is a table-level check constraint on a scanned column, which describes a stored column rather than an expression. Encoding Rand as a constrained column proves a different pattern (redundant filter over a constrained table), and without the range fact the rewrite is not an equivalence for an uninterpreted function. No faithful narrowing exists.

### `OptimizeRepartition` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2045-2053
- Attempts used: 26
- Last updated: 2026-10-13T05:58:47.010607+00:00
- Reason / notes: QED decides equivalence of row bags over Q-expressions, and that model has no notion of physical partitioning/distribution or of per-operator physical attributes such as partition count — the DSL/JSON surface (scan/filter/project/join/correlate/aggregate/union/intersect/minus/sort) has no repartition/exchange node, and even a `extend_dsl_file` builder couldn't help since the immutable Rust prover would have to interpret such an attribute and simply doesn't model it. OptimizeRepartition's sole effect is flipping `optNumPartitions` from None to Some(1) (sound only because hash-partitioning on foldable/constant expressions sends every row to one bucket — a physical fact QED cannot see), while the output row bag is identical to the child's before and after. Any RuleScript encoding would therefore degenerate to an identity rewrite (child ≡ child) that captures neither the rule's effect nor its foldable-expressions guard, so the UNSUPPORTED call is correct.

### `OptimizeWindowFunctions` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2058-2069
- Attempts used: 10
- Last updated: 2026-10-13T06:42:29.923734+00:00
- Reason / notes: This is a Window rewrite whose soundness rests on ordering/frame semantics (the "first row" of a non-empty-ORDER BY ROWS frame), but QED decides only bag-semantic equivalence — Window has no bag-semantic meaning, is absent from both the core language and JSONSerializer, and the Rust prover has no model for it, so the gap is a fundamental limitation that extend_dsl_file cannot close. Independently, `First` and `NthValue(_,1)` are two distinct uninterpreted aggregate symbols with no axioms or identity relating them, so QED cannot prove their outputs agree even over an identical input bag. Thus the only "provable" encoding would collapse the two to one symbol, reducing the rule to a no-op — no genuine non-trivial fragment exists, so UNSUPPORTED is correct. ```

### `PushProjectionThroughLimitAndOffset` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushProjectionThroughLimitAndOffset.scala, lines 27-43
- Attempts used: 7
- Last updated: 2026-10-13T07:14:59.938194+00:00
- Reason / notes: The validity of all three cases (Project pushed under LocalLimit, under GlobalLimit∘LocalLimit, and under Offset) rests on Limit/Offset having positional "keep/skip the first k rows" semantics, but QED is a bag-semantic prover with no axioms for Sort/Limit/Offset — they surface only as uninterpreted, axiom-free QOps, so it cannot relate Limit(n, Project(p, X)) to Project(p, Limit(n, X)), nor can it derive even degenerate facts like Limit(n, ∅) = ∅ or Offset(k, ∅) = ∅ that a minimal special case would still require. The RelRN API exposes no Limit/Sort builder, and adding one (the JSON serializer can carry sort/limit fields) would merely surface the uninterpreted operator without confering any decidability, since the fixed prover's bag theory has no interpretation for it — a genuine QED limitation, not a missing DSL capability, consistent with the independently verified skips of the sibling Spark rules EliminateLimits, EliminateOffsets, and LimitPushDown. ```

### `ReassignLambdaVariableID` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/objects.scala, lines 251-294
- Attempts used: 41
- Last updated: 2026-10-13T07:38:39.576000+00:00
- Reason / notes: ReassignLambdaVariableID is a pure alpha-rename/ID-reallocation of bound lambda variables inside scalar expressions — an expression-syntax invariant with zero effect on any relational plan's bag semantics, so the correct before/after encoding degenerates into either the identical pattern (a vacuous self-equality) or two independent uninterpreted symbols that QED by design cannot relate. QED's model has no lambda/binder construct (its only higher-order shape, subquery/correlate, binds an implicit fresh row variable that never appears in the input syntax), and since the missing named-binder node form is absent from the prover's own input format rather than merely lacking a Java builder, extend_dsl_file cannot close the gap. ```

### `RemoveRedundantAliases` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 657-838
- Attempts used: 10
- Last updated: 2026-10-13T07:51:24.225218+00:00
- Reason / notes: The rule's entire content is naming/metadata semantics: its guard (`name == attr.name`), its purpose (preserving output schema names, incl. the AUTO_GENERATED_ALIAS metadata case), and all its guardrails (join self-reference excludes, Union's first child, subquery references) manage Spark attribute identity/exprIds — i.e. names, not values — while at the value level an `Alias` over a bare attribute is just a one-column passthrough projection, so before/after are trivially value-identical and the only thing QED could prove (identity-projection elimination) is a tautological consequence of projection semantics rather than non-trivial content of this rule. QED's model is name-erased bag semantics (every `RelType.VarType` serializes as "INTEGER" and the query JSON carries no output-column names or metadata at all), so the

### `RemoveRedundantSorts` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/RemoveRedundantSorts.scala, lines 29-62
- Attempts used: 7
- Last updated: 2026-10-13T07:52:57.463231+00:00
- Reason / notes: The rule's two substantive rewrites are justified solely by non-bag facts: the child's output-ordering property already satisfying the sort keys, and the global-sort-under-local-sort demotion existing to preserve *partitioning* via RepartitionByExpression, not ordering. QED's model is bag-only and explicitly does not support the ordering semantics of Sort/Order By (qed.pdf §6.2), and RuleScript has no way to express "child is already sorted by X" as a side condition on an uninterpreted child — nor any partitioning concept — and even if a `sort` builder were added to the DSL (JSONSerializer already carries LogicalSort), the unmodifiable Rust prover has no ordering/partitioning semantics to check against, so the only provable statement would be the vacuous bag identity `sort(child) ≡ child`, which is not the rule being ported. Thus the porter correctly identified a genuine QED limitation rather than a missing DSL capability, and UNSUPPORTED is the right conclusion. ```

### `ReorderAssociativeOperator` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 275-344
- Attempts used: 24
- Last updated: 2026-10-13T08:02:54.455005+00:00
- Reason / notes: The rule's correctness rests entirely on the internal algebra of integer +/× — associativity/commutativity for flatten/reorder, 0/1 identity elimination, literal constant folding, and zero-product collapse — while QED models every non-boolean scalar operator as an uninterpreted function with no arithmetic theory. Consequently even the weakest non-trivial instance, a pure reassociation like `f(f(x,y),z) ⟹ f(f(x,z),y)`, is invalid for an arbitrary uninterpreted `f` (the SMT solver would correctly produce a countermodel), so no symbol-sharing or re-encoding trick can make any variant pass. This is a genuine prover-side limitation rather than a DSL gap: `extend_dsl_file` only adds Java builders/serialization and cannot grant the fixed Rust prover interpreted arithmetic semantics, and the rule has no meaningful provable special case since all of its content (plus its null/failOnError subtleties) depends on that unmodeled algebra. ```

### `ReplaceUpdateFieldsExpression` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/UpdateFields.scala, lines 82-87
- Attempts used: 21
- Last updated: 2026-10-13T08:16:49.140851+00:00
- Reason / notes: The rule inlines the definitional expansion of an uninterpreted scalar operator (UpdateFields(S, ops) → If(IsNull(S), NULL, CreateNamedStruct(GetStructField(S,i), …))), so its correctness rests entirely on the backend-internal semantics of `UpdateFields` — precisely the "operator whose specific internal semantics QED cannot see through as an uninterpreted function" limitation. QED treats `UpdateFields`, `If`, `IsNull`, `CreateNamedStruct`, and `GetStructField` as distinct uninterpreted function symbols with no axioms tying `UpdateFields` to that expansion, so the two sides fail under arbitrary instantiation and no non-vacuous (nor any narrower) encoding is provable. This is a real QED-prover gap, not a missing DSL builder: `extend_dsl_file` can only add Java records, not the definitional axioms the unmodifiable prover would need.

### `RewriteExceptAll` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3445-3471
- Attempts used: 25
- Last updated: 2026-10-13T09:18:38.794710+00:00
- Reason / notes: The rule's LHS is bag/multiset EXCEPT ALL, which QED fundamentally does not model — it supports only the set variant of MINUS (the serializer throws on `minus.all == true`, and the prover deliberately omits a semiring monus), so the before() side cannot even be serialized, and no core-language expression can replicate count differencing since QED's semiring has no subtraction. Independently, the RHS requires numeric aggregate algebra (SUM of +1/−1 tags, `> 0` comparison) and Generate(ReplicateRows) count-based row replication: QED treats aggregates as uninterpreted beyond input bag equality, the DSL exposes no arithmetic operators (and `Const` serializes all values opaquely), and variable-count row replication has no bag-semantic meaning QED models. Since every instance of this rule has the unmodelable bag minus on the LHS, no narrower special case is encodable either.

### `RewriteIntersectAll` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3503-3550
- Attempts used: 11
- Last updated: 2026-10-13T09:13:55.730312+00:00
- Reason / notes: The rule's left-hand side is `Intersect(left, right, true)` — bag INTERSECT ALL — and QED's Q-expression formalism has no multiset-intersection operation (qed.pdf: the bag variants would require an ordering/monus operator the prover does not introduce), which the ground-truth `JSONSerializer` confirms has no node for (`case LogicalIntersect ... when !intersect.all` throws `Not implemented` on the bag variant, as the porter's probe showed), so the `before()` pattern cannot even be fed to the prover. Independently, the rewrite's correctness rests on `Generate(ReplicateRows(min(c1,c2), payload))` — data-dependent row replication to materialize per-value multiplicities — which has no DSL operator or JSON node, and cannot be emulated with core-language joins/tables because uninterpreted predicates and table contents carry no cardinality semantics QED could reason about.

### `RewriteNonCorrelatedExists` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/finishAnalysis.scala, lines 69-79
- Attempts used: 24
- Last updated: 2026-10-13T09:31:50.509246+00:00
- Reason / notes: The rewrite's correctness rests entirely on Limit(1, ·) converting "Q is non-empty" into a well-defined single-row scalar-subquery result, but QED assigns Limit no bag-semantic meaning — it becomes a fully uninterpreted QOp(Limit, 1, Q) with no axioms tying its output bag (size, non-emptiness) to its input, and the EXISTS / ScalarSubquery / IsNotNull stack on either side likewise cannot be interpreted as bag non-emptiness, leaving two independent uninterpreted atoms the SMT solver cannot relate. This is a fundamental QED limitation, not a missing DSL builder: extend_dsl_file only changes how patterns are serialized into JSON, while the trusted Rust prover is frozen and will never receive non-emptiness/cardinality axioms for Limit. No faithful narrowing exists either — dropping the load-bearing Limit changes the test's truth condition from ‖Q‖ > 0 to ‖Q‖ = 1 (error for more), so the porter's UNSUPPORTED conclusion is correct. ```

### `RewriteSizeOfArrayStruct` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/RewriteSizeOfArrayStruct.scala, lines 44-96
- Attempts used: 89
- Last updated: 2026-10-13T12:26:43.455075+00:00
- Reason / notes: The rule rewrites `size(a)` into `size(GetArrayStructFields(a))`, and its soundness rests entirely on the operator-specific law that struct-field extraction preserves array length and null-ness — the internal list/element semantics of Spark's `Size`/`GetArrayStructFields`, which QED by design does not model. In RuleScript both operators can only be named as uninterpreted projection symbols, and QED correctly cannot derive `size(f(x)) = size(x)` for an uninterpreted `f`; the only premise channel (row-constraints "guaranteed" on a scan) would have to assert that very equality, reducing the proof to trivial equality substitution and making the verdict vacuous. This is a fundamental prover limitation (opaque uninterpreted operator internals, no list semantics), not a missing DSL capability that `extend_dsl_file` could close, so the porter's UNSUPPORTED conclusion is correct. ```

### `RewriteWithExpression` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/RewriteWithExpression.scala, lines 45-472
- Attempts used: 49
- Last updated: 2026-10-13T10:47:42.973768+00:00
- Reason / notes: The rule's entire content is substituting a `With`/`CommonExpressionRef` let-binder by its definition, but neither the RuleScript scalar language (`RexRN` has no let/def node) nor QED's JSON Q-expression format (scalars are only column/literal/uninterpreted-call/subquery; relations only scan/filter/project/join/correlate/group/union/intersect/except/sort) contains a def/ref binder, so a reference and its definition can only be encoded either as the identical structure (baking in the equality, making the rule vacuous) or as independent uninterpreted symbols QED cannot prove equal. Because the Rust prover — the unmodifiable arbiter — accepts no let node, `extend_dsl_file` (Java-only) cannot supply the missing substitution semantics, and the firing gates (reference count, determinism, cheapness) are backend heuristics QED likewise does not model.

### `SchemaPruning` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/SchemaPruning.scala, lines 38-248
- Attempts used: 46
- Last updated: 2026-10-13T12:25:21.807624+00:00
- Reason / notes: The rule's before- and after-plans scan two distinct leaf relations (the full-schema HadoopFsRelation and its copy with a pruned data/metadata schema), and the rewrite is sound only under the physical file-format guarantee that a pruned Parquet/ORC read returns identical values for the kept fields — a cross-table premise that QED's for-all-instantiations equivalence problem cannot carry (its per-table "guaranteed" constraints are single-table and not exposed by the DSL at all), so with two independent uninterpreted scans the equivalence is refutable, not provable. The only encoding that could be "proven" — modeling the pruned scan as a projection over the same scan symbol — would bake the very data-source property under verification into the after-plan, leaving only a vacuous projection-identity proof rather than the actual rewrite. On top of that, the rule's core structure (pruning nested StructType fields, merging the metadata schema, ProjectionOverSchema mapping) has no representation in RuleScript's flat uninterpreted-type core language, which has no struct types or nested field access. ```

### `SimplifyCaseConversionExpressions` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 1284-1293
- Attempts used: 46
- Last updated: 2026-10-13T12:49:53.435213+00:00
- Reason / notes: The rule's only step is the pointwise idempotence laws Upper(Upper(x))⇒Upper(x) and Lower(Lower(x))⇒Lower(x), whose validity rests entirely on the internal algebra of the case-conversion functions. QED translates every named scalar operator — including both the generic symbols and the concrete SqlStdOperatorTable.UPPER/LOWER the porter tested — into an uninterpreted SMT function, and f(f(x))=f(x) is not a valid property of an arbitrary uninterpreted function, so the provable:false verdict is a genuine, complete (non-timeout) negative. No Java-side DSL extension can close this gap because the function axioms would have to live in the fixed Rust prover's translation, not in the pattern builders, making the UNSUPPORTED conclusion correct. ```

### `SimplifyCasts` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 1247-1272
- Attempts used: 27
- Last updated: 2026-10-13T12:45:15.945541+00:00
- Reason / notes: Every non-trivial case of SimplifyCasts is sound only because of cast algebra — same-type cast being the identity, widening casts absorbing (cast₂∘cast₁ = cast₂), and value/null preservation — but in RuleScript a cast can only be expressed as an uninterpreted function symbol, and QED's semiring theory (qed.pdf, "Typing") attaches no axioms to such symbols, so no encoding or DSL extension can supply the missing identity/absorption/null-preservation facts (the fixed Rust prover is the arbiter and cannot be given them). The porter's empirical check is correctly designed: modeling `Cast(e, T)` as uninterpreted `cast_T: T → T` and asking for equivalence with the bare column asks QED to prove `∀x. cast_T(x) = x`, which SMT rightly refutes for an uninterpreted function — this is a genuine theory gap, not a name-mismatch or composition-encoding bug, and the rule's only purely structural case (the annotated-STRING guard) is not a rewrite at all, so no genuine provable special case remains. ```

### `SimplifyConditionals` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 703-782
- Attempts used: 72
- Last updated: 2026-10-13T13:10:40.738835+00:00
- Reason / notes: SimplifyConditionals' actual content rests on three-valued conditional semantics — the `cond.nullable` guard, the `EqualNullSafe(cond, TRUE)` (IS TRUE) arm, and null-literal And/Or behavior — none of which QED's model can express, since predicates are total two-valued booleans with no nullability, no IS-NOT-DISTINCT-FROM, and no null propagation, a limitation of the fixed Rust prover that no DSL extension can change. The only encodable subcases (ite beta-reductions like If(TRUE, v, w) → v) are tautologies of the model's own definitional IF, confirmed by the porter's 0ns empirical proofs, so they verify nothing about the rule's null-handling content. The UNSUPPORTED conclusion is a genuine semantic limitation, not a missed encoding.

### `SimplifyDateTimeConversions` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 1298-1338
- Attempts used: 12
- Last updated: 2026-10-13T12:55:21.452979+00:00
- Reason / notes: Both cases of the rule reduce to algebraic identities over the conversion functions — case 1 needs `to_str_p(to_ts_p(x)) = x` and case 2 needs `to_ts_p(to_str_p(to_ts_p(x))) = to_ts_p(x)` — and that round-trip-cancellation property is precisely the bespoke internal semantics of Spark's date_format/to_timestamp operators, which QED cannot see: it models every conversion as an uninterpreted function symbol with only congruence, and neither the DSL nor the JSON the fixed prover consumes has any channel for declaring inverse/composition axioms about function symbols (the schema side only carries table keys and row constraints). The porter's faithful encoding failing with a complete (non-timeout) refutation is consistent with this, and no non-vacuous special case exists: the only way to make an instance provable would be to assume the round-trip identity as a table premise and then prove it, which is tautological and not a faithful narrowing of Spark's unconditional rule — so UNSUPPORTED is the correct conclusion. ```

### `SimplifyExtractValueOps` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/ComplexTypes.scala, lines 28-72
- Attempts used: 10
- Last updated: 2026-10-13T12:59:00.434037+00:00
- Reason / notes: Every case of SimplifyExtractValueOps is a constructor/accessor identity over Spark complex types (get_struct(mk_struct(v…),i)=v(i), get_array_item(mk_array(e…),k)=e(k), get_map(mk_map(p…),k)=case-when(k,p), plus a null-guarded UpdateFields case) that is valid only under Spark's intended struct/array/map and 3-valued-null semantics. QED's fixed semiring model has no list/index, map-key-lookup, or SQL-null semantics and represents all of these operators as uninterpreted scalar functions, and since its SMT decision must hold for every instantiation of uninterpreted symbols, no composition law such as get(mk(v…)) = v(i) is derivable — so this is a fundamental limitation of the unchanged Rust prover (same class as RewriteSizeOfArrayStruct), and adding DSL builders could not close the gap.

### `UnwrapCastInBinaryComparison` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/UnwrapCastInBinaryComparison.scala, lines 102-522
- Attempts used: 32
- Last updated: 2026-10-13T13:19:48.966817+00:00
- Reason / notes: QED's fixed prover models casts as uninterpreted functions and comparison predicates as uninterpreted operators, with no axioms for cast injectivity/losslessness, round-trip preservation, value ranges, or date/timestamp floor behavior — and every non-trivial branch of Spark's UnwrapCastInBinaryComparison (moving the cast onto the literal side, collapsing out-of-range comparisons to true/false, the date<->timestamp floor rewrites, and In/InSet round-trip filtering) is valid precisely because of those cast axioms, so the equivalence is not a bag-semantic tautology under free interpretation. This is a fundamental prover limitation, not a DSL gap: extending RelRN/RexRN to expose a cast builder would only add another uninterpreted function, and the Rust prover cannot be modified to reason about cast algebra, so no encoding or narrower special case can be proved. The porter's probe (`Cast(x,T)=v -> x=v` not provable) and its citation of qed.pdf's "solvers do not reason about type casting" are consistent with this, so the UNSUPPORTED conclusion is sound. ```

### `UpdateAttributeNullability` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/analysis/UpdateAttributeNullability.scala, lines 34-56
- Attempts used: 27
- Last updated: 2026-10-13T13:38:05.092880+00:00
- Reason / notes: UpdateAttributeNullability is a static type-metadata (nullable-flag) propagation pass with no effect on bag-level query results — it only rewrites attribute annotations, and QED's Q-expression semantics equip every type with a distinguished NULL value without ever consulting the schema's nullable flag, so any before/after pair differing only in that flag encodes as the identical Q-expression and "proves" vacuously. The rule's sole content lives in nullability metadata, and the hard boundary forbids modifying the QED prover to make it consult nullable flags (extending the Java DSL alone cannot create any provable, non-vacuous difference), so it is genuinely outside RuleScript/QED's expressible-and-checkable scope. ```

### `UpdateCTERelationStats` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 495-526
- Attempts used: 8
- Last updated: 2026-10-13T13:25:17.988405+00:00
- Reason / notes: The rule's entire content is plan-node metadata bookkeeping — it threads a `statsMap` through the tree and calls `withNewStats` on `CTERelationDef`/`CTERelationRef` nodes, changing only the cached `stats: Option[Statistics]` field while leaving the emitted rows (and the plan structure) completely unchanged. That state has no home in either layer: RuleScript's core language has no CTE/def-ref operator and no per-node attributes or side channels, and QED's JSON model carries only table schemas (fields/types/keys/constraints) plus bag-semantic relational algebra, so `statistics` simply cannot be represented. Any faithful encoding would therefore degenerate to the vacuous identity `before() == after()` — trivially "provable" but proving nothing about the rule's actual behavior (the id-keyed stats lookup and attachment), which is a genuine modeling limitation rather than a missed encoding trick. ```

### `V1Writes` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/V1Writes.scala, lines 68-115
- Attempts used: 10
- Last updated: 2026-10-13T13:34:04.252791+00:00
- Reason / notes: V1Writes is a physical planning rule, not a semantics-preserving optimization — its defining steps (a deliberately non-identity Empty2Null coercion Project on partition columns, an ordering-dependent Sort insertion for requiredOrdering, and substitution of the child under the bespoke WriteFiles operator) mean the before/after plans are intentionally *not* equivalent relations, so there is no before≡after bag-equivalence statement for QED to make. QED models only bag semantics with no Sort/ordering interpretation (qed.pdf §6.2) and cannot see through a physical write operator's bespoke internal semantics, and even the most degenerate special case (no partition columns, empty requiredOrdering) collapses to an operator swap between two uninterpreted physical operators, which the prover cannot equate. The porter's UNSUPPORTED conclusion is correct. ```

### `V2ScanPartitioningAndOrdering` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/v2/V2ScanPartitioningAndOrdering.scala, lines 38-149
- Attempts used: 9
- Last updated: 2026-10-13T13:40:53.137762+00:00
- Reason / notes: V2ScanPartitioningAndOrdering is a pure physical-property annotation — both passes only rewrite plan-node attributes (`keyGroupedPartitioning`, `ordering`) via `d.copy` while the scan's row bag and schema are left untouched, so before and after are trivially bag-identical. QED is a bag-semantics decision procedure with no model of partitioning/ordering metadata (the JSON surface carries only rows plus `key`/`guaranteed` constraints, and even a `sort` node is defined to have no bag meaning), and the unmodifiable Rust prover has no semantics to interpret such attributes against, leaving the rule's key-resolvability and subset-of-output guards with nothing to check. Any RuleScript encoding therefore degenerates to the vacuous `scan ≡ scan` that verifies none of the rule's actual behavior — a fundamental QED limitation rather than a missing DSL builder the porter overlooked.

### `V2ScanRelationPushDown` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/v2/V2ScanRelationPushDown.scala, lines 48-1382
- Attempts used: 61
- Last updated: 2026-10-13T14:08:30.706587+00:00
- Reason / notes: I confirmed against the full DSL source that a pushed-down scan can only be encoded as a fresh uninterpreted scan symbol, which QED must equate with `Filter(preds, base)` under *every* instantiation — impossible, since the DSL doesn't even expose per-table `guaranteed` constraints and such constraints could at best express one-way inclusion, never two-way equality with a second table. The limit/offset and sample sub-rules rest on ordering/limit semantics QED explicitly doesn't model, and the partial-aggregate-pushdown fragments (min-of-min, sum-of-sum, count→sum, avg→sum/count) require aggregate algebra QED cannot know — so no faithful, non-trivial provable fragment of this rule exists, and the porter's diagnosis is sound rather than a modeling miss.

### `V2Writes` — ⏭️ SKIPPED

- Source backend: Apache Spark Catalyst
- Source rule: Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/v2/V2Writes.scala, lines 42-198
- Attempts used: 11
- Last updated: 2026-10-13T13:46:36.471044+00:00
- Reason / notes: V2Writes is a write-command construction rule, and its two observable changes are (1) filling a V2 write *command* node's `write` field with an opaque engine-built Write object — a side-effect payload that is not a relation and is bespoke engine-internal state QED cannot see through — and (2) wrapping the child query in Sort/Repartition/Rebalance to satisfy the write's distribution/ordering, which QED's bag model has no notion of (no ordering/distribution semantics at all). The before/after plans are the same uninterpreted command node whose child *bag* is actually invariant (distribution and the `write` field are bag-irrelevant), so there is no bag-equivalence statement between two relations for QED to decide; this is a genuine QED/DSL limitation, not a mismodeling or a missing symbol-sharing step. ```

