# RuleScript porting progress

_Last updated: 2026-09-23T00:02:22.993607+00:00_

**40/60 rules proved** (0 failed, 20 skipped as out of QED's supported fragment).

| Rule | Backend | Status | Scope | Attempts | Notes |
|---|---|---|---|---|---|
| `AggregateExpandDistinctAggregates` | Apache Calcite | ✅ PROVED | PARTIAL | 72 | The encoding faithfully mirrors the source rule's `convertMonopole` branch: `before()` is `GROUP BY k` of two distinct calls `f(DISTINCT ... |
| `AggregateExtractProject` | Apache Calcite | ✅ PROVED | PARTIAL | 14 | The encoding faithfully mirrors the source rule's actual computation: it computes the used columns (group col 0, agg arg col 1), projects... |
| `AggregateFilterTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 9 | The encoding faithfully captures Case 1 of the source rule: the uninterpreted predicate depends only on the group key (i.e. all filter co... |
| `AggregateJoinRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 67 | The encoding correctly captures the core semantic content of AggregateJoinRemove's LEFT-join branch: a DISTINCT aggregate (group key on t... |
| `AggregateJoinTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 126 | Hand-applied by harness operator: added additive RelRN.scanMany/ScanMany multi-column scan, encoded the DEFAULT-config (no-agg-function) ... |
| `AggregateMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 37 | The encoding is a genuine, non-vacuous instance of the source rule: when the top aggregate has no aggregate calls, Calcite's onMatch skip... |
| `AggregateProjectMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 68 | The encoding is a line-faithful instance of the source rule's `apply`: for the swap project `[x1, x0]` the interesting-field map is `{0→1... |
| `AggregateProjectPullUpConstants` | Apache Calcite | ✅ PROVED | PARTIAL | 63 | The encoding faithfully captures the rule's core transformation: an aggregate whose leading group key is a constant column is rewritten t... |
| `AggregateRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 34 | `before()` (a SIMPLE group-by on column 0 with zero agg calls, i.e. SELECT DISTINCT) is structurally and semantically distinct from `afte... |
| `AggregateUnionAggregate` | Apache Calcite | ✅ PROVED | PARTIAL | 8 | The encoding is faithful and non-vacuous: `before()` truly differs from `after()` (an extra group-by-all, no-agg-call dedup beneath the s... |
| `AggregateValues` | Apache Calcite | ✅ PROVED | PARTIAL | 10 | The encoding faithfully captures the rule's dedup branch — a simple aggregate (group by the relation's only column, zero aggregate calls)... |
| `ExpandDisjunctionForJoinInputs` | Apache Calcite | ✅ PROVED | PARTIAL | 35 | before() and after() are structurally different — after() ANDs into the join condition the two per-side disjunctions (pl∨rl) and (pr∨sr) ... |
| `FilterAggregateTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 13 | The encoding is a non-vacuous, faithful instance of the rule's core transformation: the conjunct P depending only on the (identity) group... |
| `FilterJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 37 | before() and after() are structurally different (Filter above an inner join vs. the AND-composed join condition), and the symbol sharing ... |
| `FilterMerge` | Apache Calcite | ✅ PROVED | FULL | 4 | before() is a genuinely nested Filter(Filter(scan)) while after() is a single Filter over an uninterpreted conjunction, so the proof is o... |
| `FilterProjectTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 42 | The encoding faithfully captures the core mechanism of FilterProjectTranspose — pushing a predicate below a project by substituting the p... |
| `FilterRemoveIsNotDistinctFrom` | Apache Calcite | ✅ PROVED | PARTIAL | 37 | The encoding is non-trivial and operator-correct: before() is Filter(IS_NOT_DISTINCT_FROM(x,y)) and after() is the DNF `(x IS NULL AND y ... |
| `FilterSetOpTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 6 | before() = Filter(P, UnionAll(L, R)) and after() = UnionAll(Filter(P, L), Filter(P, R)) are structurally distinct and exactly the pushdow... |
| `FullToLeftAndRightJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 11 | The encoding mirrors the source rule's exact shape — before() is the FULL join, after() is (LEFT join) UNION ALL ((RIGHT join) filtered b... |
| `IntersectReorder` | Apache Calcite | ✅ PROVED | PARTIAL | 8 | before (A∩B∩C) and after (C∩A∩B) differ by a genuine 3-cycle permutation of three distinct uninterpreted inputs sharing one type, so the ... |
| `JoinAggregateTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 36 | The encoding mirrors the source rule's exact plan shape — Aggregate(below an INNER join) ⟹ INNER join under an Aggregate whose group set ... |
| `JoinAssociate` | Apache Calcite | ✅ PROVED | PARTIAL | 34 | The encoding correctly re-associates ((A⋈B)⋈C)→(A⋈(B⋈C)) with INNER joins, faithfully splitting conditions by A-reference (PAB/PABC on to... |
| `JoinCommute` | Apache Calcite | ✅ PROVED | PARTIAL | 39 | before() is Join(L, R, INNER, P(l, r)) and after() is Join(R, L, INNER, P(l, r)) with the references correctly remapped to positions (1, ... |
| `JoinDeriveIsNotNullFilter` | Apache Calcite | ✅ PROVED | PARTIAL | 10 | The rewrite is nontrivial and matches the source's inner-join transformation by adding IS NOT NULL filters implied by the null-rejecting ... |
| `JoinExtractFilter` | Apache Calcite | ✅ PROVED | FULL | 8 | <1-3 sentences ...> ``` No code fences? It shows with code block? It says Reply with exactly this format (nothing else): ``` ... ``` Prob... |
| `JoinProjectTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 10 | The before/after plans are structurally different (Project below the Join in before, Project above it in after), with correct symbol shar... |
| `JoinPushExpressions` | Apache Calcite | ✅ PROVED | PARTIAL | 37 | before() and after() are genuinely structurally different and differ in exactly the way the real rule rewrites — a bare inner join versus... |
| `JoinPushThroughJoin` | Apache Calcite | ✅ PROVED | PARTIAL | 7 | The encoding faithfully captures the RIGHT instance of JoinPushThroughJoin: before is (A ⋈_{SA∧SB} B) ⋈_{ST∧TC} C with layout (A,B,C), an... |
| `JoinUnionTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 33 | The encoding is a faithful, non-vacuous instance of the rule: before() = (X ∪ALL Y) ⋈_C O vs after() = (X ⋈_C O) ∪ALL (Y ⋈_C O) is a genu... |
| `ProjectAggregateMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 36 | The encoding is non-vacuous and correctly structured: before() computes an unused third aggregate per group and projects around it (field... |
| `ProjectFilterTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 39 | The encoding is a faithful fixed-shape instantiation of the rule's whole-expressions mode: before() = Filter(P(E(x,y), y), S) → Project(E... |
| `ProjectJoinTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 10 | The encoding is a faithful, non-vacuous special case: `before()` (inner join of raw scans with condition C(TL(l),TR(r)) and top projectio... |
| `ProjectMerge` | Apache Calcite | ✅ PROVED | FULL | 6 | `before()` (a Project stacked on a Project) and `after()` (a single Project) are structurally distinct, so the proof is non-vacuous and m... |
| `ProjectRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 41 | The encoding faithfully and non-vacuously captures the rule's core transformation: `before()` is a Project whose sole expression is `sour... |
| `ProjectSetOpTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 68 | The encoding faithfully captures the core semantics of ProjectSetOpTranspose: a top-level uninterpreted projection F is pushed below a UN... |
| `SetOpToFilter` | Apache Calcite | ✅ PROVED | PARTIAL | 11 | The encoding exactly captures Calcite's rewrite for the single-source, two-filter case — `Union(DISTINCT, [σ_P1(S), σ_P2(S)])` → all-fiel... |
| `UnionEliminator` | Apache Calcite | ✅ PROVED | FULL | 10 | The encoding exactly mirrors the rule's matches condition for the union variant — a set op with all=true over exactly one input (input.un... |
| `UnionMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 5 | The encoding faithfully reproduces Calcite's UnionMerge onMatch for the UNION ALL instance — top union with a nested union in the second ... |
| `UnionPullUpConstants` | Apache Calcite | ✅ PROVED | PARTIAL | 34 | The proof is of a real, nontrivial declared special case: a shared boolean-constant projection duplicated under a union-all is hoisted ab... |
| `UnionToDistinct` | Apache Calcite | ✅ PROVED | PARTIAL | 7 | The encoding matches the rule's exact shape: `before()` is a UNION DISTINCT (`union(false)`) over two independent uninterpreted inputs, a... |
| `AggregateCaseToFilter` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's correctness rests on aggregate-function algebra that QED explicitly does not model — that null-skipping aggregates (COUNT/SUM)... |
| `AggregateFilterToFilteredAggregate` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's validity rests entirely on the algebraic identity `agg(input restricted to rows where P) ≡ agg FILTER (WHERE P)(full input)` —... |
| `AggregateGroupingSetsToUnion` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's left-hand side is a GROUPING SETS aggregate, but QED's language has no such operator: `RelRN.Aggregate` builds only simple gro... |
| `AggregateJoinJoinRemove` | Apache Calcite | ⏭️ SKIPPED | — | 60 | The rule's soundness hinges on the null-extension branch of the bottom left join: rows l with no matching m (¬∃m. PB(l,m)) must be shown ... |
| `AggregateReduceFunctionsOnGroupKeys` | Apache Calcite | ⏭️ SKIPPED | — | 30 | Every reduction branch of this rule (MAX/MIN/AVG/ANY_VALUE applied to a group key becoming the key reference, or a SqlConstantValueAggFun... |
| `AggregateRemoveDuplicateKeys` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rewrite's soundness rests on ANY_VALUE's choice semantics — its result must be the group's functionally-determined value — but QED tr... |
| `AggregateRemoveLiteralAgg` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's entire correctness argument is the Calcite-internal algebraic identity that LITERAL_AGG(lit) evaluates to lit on every group p... |
| `AggregateUnionTranspose` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's soundness rests on the split/merge algebra of specific aggregate functions (SUM additivity, MIN idempotence, COUNT→SUM0): it c... |
| `FilterTableScan` | Apache Calcite | ⏭️ SKIPPED | — | 9 | FilterTableScanRule is a purely physical pushdown (Filter(TableScan) → BindableTableScan) whose before and after differ only in represent... |
| `IntersectToDistinct` | Apache Calcite | ⏭️ SKIPPED | — | 30 | IntersectToDistinct's correctness rests on the counting algebra of COUNT(*) — that aggregating each branch by all columns makes each dist... |
| `IntersectToExists` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The porter's stated reason was merely an LLM context-length crash, but the conclusion is correct for a real reason: the EXISTS arm of the... |
| `JoinConditionExpandIsNotDistinctFrom` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's correctness rests entirely on the null-aware data semantics of specific backend operators — `IS NOT DISTINCT FROM`, `COALESCE`... |
| `JoinExpandOrToUnion` | Apache Calcite | ⏭️ SKIPPED | — | 0 | The rewrite's core identity — Join(Or(P,Q)) ⟹ UnionAll(Join(P), Join(And(Q, ¬P))) — was tested directly against QED under both candidate ... |
| `JoinPushTransitivePredicates` | Apache Calcite | ⏭️ SKIPPED | — | 30 | This rule is metadata-driven predicate inference, not a structural rewrite — Calcite decomposes the join condition into equi-join equival... |
| `MinusToDistinct` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rule's correctness rests on the algebra of COUNT: the post-rewrite plan's `count_0 > 0` and `count_i = 0` tests are what implement th... |
| `MinusToFilter` | Apache Calcite | ⏭️ SKIPPED | — | 30 | The rewrite's correctness depends on `NOT Q` being the exact complement of `Q` (i.e., rows with `Q = NULL` appearing on neither side), bu... |
| `ProjectJoinJoinRemove` | Apache Calcite | ⏭️ SKIPPED | — | 60 | The rule is valid only because the bottom join's condition is an equi-join on Y's unique key, guaranteeing at most one Y row per X row — ... |
| `ProjectJoinRemove` | Apache Calcite | ⏭️ SKIPPED | — | 90 | The rule's correctness rests on the join condition being an equality on the non-preserved side's key columns, which lets the key constrai... |
| `ProjectOverSumToSum0` | Apache Calcite | ⏭️ SKIPPED | — | 18 | The rule's subject — `SUM(x) OVER <frame>` vs `SUM0(x) OVER <frame>` inside a project — is inherently a windowed aggregate, and QED's the... |
| `SortRemove` | Apache Calcite | ⏭️ SKIPPED | — | 30 | SortRemove is fundamentally a row-ordering rule: its subject operator (a Sort with no offset/limit) and its defining precondition (the in... |

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

### `ExpandDisjunctionForJoinInputs` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ExpandDisjunctionForJoinInputsRule.java
- Attempts used: 35
- Last updated: 2026-09-22T20:49:14.643173+00:00
- Reason / notes: before() and after() are structurally different — after() ANDs into the join condition the two per-side disjunctions (pl∨rl) and (pr∨sr) — and the equivalence is a genuine, non-vacuous theorem: each added disjunction is logically implied by the two-branch DNF condition (pl∧pr)∨(rl∧sr), but the implication is not syntactic, so the prover had to verify it for arbitrary instantiations. The encoding is faithful in its details: pl/rl reference only L's columns and pr/sr only R's (joinField ordinals 0–3 with a 2+2 split, verified against JoinField's left-column arithmetic), the four predicate symbols are independent as the rule needs, and INNER is inside the source rule's applicability (both canPush flags true for INNER, so both extras are legitimately added — no dropped precondition, and the bloat limit and no-op equality guard are heuristics, not semantic preconditions). The remaining narrowing — INNER only (LEFT/RIGHT variants add fewer extras), the join-condition matcher only rather than the filter-on-join matcher, and a fixed two-branch one-conjunct-per-side DNF rather than arbitrary DNF — is exactly what the single PARTIAL scope line claims, so this is an honestly labeled, non-degenerate special case of the real rule. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 71751833}, panicked=False

### `FilterAggregateTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/FilterAggregateTransposeRule.java
- Attempts used: 13
- Last updated: 2026-09-22T10:25:40.096368+00:00
- Reason / notes: The encoding is a non-vacuous, faithful instance of the rule's core transformation: the conjunct P depending only on the (identity) group key moves from the filter above the SIMPLE aggregate to a filter below it, while the conjuncts over the aggregate's output stay above, and P is correctly one shared uninterpreted symbol applied to the corresponding key column on both sides (modeling the rule's identity RexInputConverter). Operator usage and preconditions match the source — Filter-over-Aggregate shape, SIMPLE grouping, non-distinct uninterpreted aggregate, groupCount>0 and canPush's key-only condition built in, and per-key filtering commutes with group-by even under null semantics — with the two-column scan-join input being the standard DSL construction for a multi-column table (scans are single-column), not a fixed join the rule constrains. The narrowings (single identity group key, one aggregate call, one pushed conjunct) are genuine shape restrictions, honestly and specifically disclosed in the PARTIAL scope line, and since the proven equivalence is the exact semantic atom the rule applies per conjunct (the extra key-referencing remaining conjunct Qa is inert to P's movement), the provable verdict is meaningful rather than coincidental. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 100425542}, panicked=False

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

### `JoinUnionTranspose` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinUnionTransposeRule.java
- Attempts used: 33
- Last updated: 2026-09-22T23:49:19.463599+00:00
- Reason / notes: The encoding is a faithful, non-vacuous instance of the rule: before() = (X ∪ALL Y) ⋈_C O vs after() = (X ⋈_C O) ∪ALL (Y ⋈_C O) is a genuine distributivity law (structurally distinct shapes, not an identity), the double `union(true, ...)` matches the source's `union.all` requirement, INNER keeps the union off any null-generating side so no semantic precondition is dropped, and the single shared condition symbol C correctly mirrors the source reusing the identical `join.getCondition()` in all three joins (independent symbols would have been the error). Nullable, keyless scans match the source's lack of PK/NOT NULL preconditions, so no hidden constraint is assumed. The scope line is honest and specific about the genuine narrowing (INNER join, left-side union) — full multi-join-type coverage cannot be a single provable rule because the join kind is concrete in QED's serialization (meta join types only feed the family generator, whose assignments are proven individually) — and the result is still a substantive, useful special case of the real rule. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1883000}, panicked=False

### `ProjectAggregateMerge` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectAggregateMergeRule.java
- Attempts used: 36
- Last updated: 2026-09-22T22:51:42.740589+00:00
- Reason / notes: The encoding is non-vacuous and correctly structured: before() computes an unused third aggregate per group and projects around it (fields 0,1,3 of 4), while after() omits that call and identity-projects the reduced aggregate (fields 0,1,2 of 3) — genuinely different plans over properly introduced and shared uninterpreted symbols (distinct ops f1/f2/f3, the same group-key op g and table S on both sides), matching the exact Project-over-Aggregate shape and column permutation Calcite's rule produces for this input family, with no precondition the removal half requires. The shape restrictions (single group key, plain field-reference projections, one of three calls) are a genuine, specific, non-degenerate sub-case of the real rule rather than a symbol-sharing artifact, and the excluded COALESCE(SUM,0)→SUM0 half rests on aggregate-function algebra QED fundamentally cannot model (it treats distinct aggregate calls as unrelated uninterpreted group-bag functions), so both limitations are real and are accurately disclosed by the concrete SCOPE: PARTIAL line — making the universal proof meaningful rather than vacuous or over-claimed. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 97910084}, panicked=False

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

### `AggregateCaseToFilter` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateCaseToFilterRule.java
- Attempts used: 30
- Last updated: 2026-09-22T16:22:09.045404+00:00
- Reason / notes: The rule's correctness rests on aggregate-function algebra that QED explicitly does not model — that null-skipping aggregates (COUNT/SUM) ignore NULL inputs, that SUM0 is additive with 0/NULL as neutral, and that COUNT of a non-null constant equals counting rows — and per its own evaluation QED can only equate aggregates whose input bags are equal, whereas here the two sides aggregate different bags (CASE values over all rows, NULLs from the else branch included, versus plain values over only the rows satisfying the filter). The gap is on the prover side, not the DSL: even extending the DSL (which today also lacks a `filterArg` on `RelRN.AggCall` and any CASE/NULL literal in `RexRN`, so neither side is expressible as-is) could not make the equivalence derivable, since the null-skipping/sum identity is precisely the "bespoke internal semantics of an aggregate operator" QED cannot see through.

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

### `AggregateUnionTranspose` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateUnionTransposeRule.java
- Attempts used: 30
- Last updated: 2026-09-22T10:33:50.070619+00:00
- Reason / notes: The rule's soundness rests on the split/merge algebra of specific aggregate functions (SUM additivity, MIN idempotence, COUNT→SUM0): it claims that re-aggregating the per-branch aggregate *results* equals aggregating the raw unioned rows. QED models every aggregate call as an uninterpreted symbol with no algebraic knowledge, and the top aggregate's input bag (the per-branch f-images) is not the same bag the original aggregate sees (the raw rows), so the two applications of the same uninterpreted function have universally different inputs and the SMT solver can build a countermodel for every candidate encoding; this limitation lives in the trusted prover's semantics, not in a missing DSL builder, so no RuleScript encoding of the transpose itself (not even a narrowed special case) is provable. ```

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

### `ProjectOverSumToSum0` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectOverSumToSum0Rule.java
- Attempts used: 18
- Last updated: 2026-09-23T00:02:22.990569+00:00
- Reason / notes: The rule's subject — `SUM(x) OVER <frame>` vs `SUM0(x) OVER <frame>` inside a project — is inherently a windowed aggregate, and QED's theory has no window/frame/ordering semantics at all (no window node in its Q-expression format, and a Calcite `RexOver` would mis-serialize as a plain scalar `RexCall` with partition/order/frame silently dropped), so the rule's shape cannot be faithfully encoded, not merely narrowed. The residual content — `SUM` ≡ `SUM0` — is an aggregate null/empty-handling algebra identity QED cannot know (aggregates are uninterpreted beyond input bag equality, and the identity is in fact false over a general bag), which the porter's frameless probe empirically confirmed with a clean, complete `provable: false` rather than a symbol-sharing bug. These are fundamental QED limitations (no list/ordering semantics for Window; no aggregate-function algebra), not a missing DSL builder that `extend_dsl_file` could close.

### `SortRemove` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveRule.java
- Attempts used: 30
- Last updated: 2026-09-22T01:36:57.264726+00:00
- Reason / notes: SortRemove is fundamentally a row-ordering rule: its subject operator (a Sort with no offset/limit) and its defining precondition (the input already satisfies the sort's collation, i.e. RelCollation trait preservation) both live in list/ordering semantics, which QED's bag-semantic model explicitly does not model — Sort/Limit/Offset/Window/Sample have no bag-semantic meaning — and no bag-level property can stand in for "input is sorted", so any encoding that drops the Sort node or the sortedness condition ceases to be this rule. Extending the DSL (e.g. adding a sort builder, which the JSON serializer could even carry) cannot close the gap because the missing semantics are on the QED prover side, not the DSL side, so UNSUPPORTED is the correct conclusion even though the porter's stated reason was just an LLM context-length error rather than that analysis. ```

