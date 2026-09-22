# RuleScript porting progress

_Last updated: 2026-09-22T07:09:24.125702+00:00_

**8/11 rules proved** (0 failed, 3 skipped as out of QED's supported fragment).

| Rule | Backend | Status | Scope | Attempts | Notes |
|---|---|---|---|---|---|
| `AggregateRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 34 | `before()` (a SIMPLE group-by on column 0 with zero agg calls, i.e. SELECT DISTINCT) is structurally and semantically distinct from `afte... |
| `FilterMerge` | Apache Calcite | ✅ PROVED | FULL | 4 | before() is a genuinely nested Filter(Filter(scan)) while after() is a single Filter over an uninterpreted conjunction, so the proof is o... |
| `FilterProjectTranspose` | Apache Calcite | ✅ PROVED | PARTIAL | 42 | The encoding faithfully captures the core mechanism of FilterProjectTranspose — pushing a predicate below a project by substituting the p... |
| `JoinCommute` | Apache Calcite | ✅ PROVED | PARTIAL | 39 | before() is Join(L, R, INNER, P(l, r)) and after() is Join(R, L, INNER, P(l, r)) with the references correctly remapped to positions (1, ... |
| `ProjectMerge` | Apache Calcite | ✅ PROVED | FULL | 6 | `before()` (a Project stacked on a Project) and `after()` (a single Project) are structurally distinct, so the proof is non-vacuous and m... |
| `ProjectRemove` | Apache Calcite | ✅ PROVED | PARTIAL | 41 | The encoding faithfully and non-vacuously captures the rule's core transformation: `before()` is a Project whose sole expression is `sour... |
| `UnionEliminator` | Apache Calcite | ✅ PROVED | FULL | 10 | The encoding exactly mirrors the rule's matches condition for the union variant — a set op with all=true over exactly one input (input.un... |
| `UnionMerge` | Apache Calcite | ✅ PROVED | PARTIAL | 5 | The encoding faithfully reproduces Calcite's UnionMerge onMatch for the UNION ALL instance — top union with a nested union in the second ... |
| `IntersectToDistinct` | Apache Calcite | ⏭️ SKIPPED | — | 30 | IntersectToDistinct's correctness rests on the counting algebra of COUNT(*) — that aggregating each branch by all columns makes each dist... |
| `JoinPushTransitivePredicates` | Apache Calcite | ⏭️ SKIPPED | — | 30 | This rule is metadata-driven predicate inference, not a structural rewrite — Calcite decomposes the join condition into equi-join equival... |
| `SortRemove` | Apache Calcite | ⏭️ SKIPPED | — | 30 | SortRemove is fundamentally a row-ordering rule: its subject operator (a Sort with no offset/limit) and its defining precondition (the in... |

## Details

### `AggregateRemove` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateRemoveRule.java
- Attempts used: 34
- Last updated: 2026-09-22T02:30:35.180701+00:00
- Reason / notes: `before()` (a SIMPLE group-by on column 0 with zero agg calls, i.e. SELECT DISTINCT) is structurally and semantically distinct from `after()` (the raw scan), and the rule's essential precondition — `areColumnsUnique(input, groupSet)` — is correctly modeled as the scan's declared key (`unique=true` → `key: [[0]]` in the serialized schema) rather than silently dropped, so the proof is a genuine, non-vacuous identity-over-a-key result. This is a faithful instance of the no-aggregate-calls branch of `AggregateRemoveRule` in the case where input and output field counts match (1 vs 1, so no trailing project is needed, exactly as the source rule would emit); every restriction — single-column base-table input, key on the sole group column, empty agg-call list, and the unmodeled splittable-function half (MAX(x)→x) — is precisely what the `SCOPE: PARTIAL` line states, and the input shape is forced by the DSL, which has no way to attach uniqueness guarantees to derived relations or multi-column scans. Symbol sharing and operator shapes are correct (one shared table symbol, one group column, SIMPLE group type with groupCount > 0, empty function list), so none of the triviality/wrong-operator/missing-precondition failure modes apply and the "provable" verdict is meaningful and honestly scoped.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 298167}, panicked=False

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

### `JoinCommute` — ✅ PROVED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinCommuteRule.java
- Attempts used: 39
- Last updated: 2026-09-22T07:09:24.122493+00:00
- Reason / notes: before() is Join(L, R, INNER, P(l, r)) and after() is Join(R, L, INNER, P(l, r)) with the references correctly remapped to positions (1, 0) (via right.joinField(1, left) / right.joinField(0, left)) followed by Project(l, r) to restore column order — exactly Calcite's VariableReplacer plus column-restoring project for the default INNER-only config, with precisely one shared predicate symbol and two distinct table symbols, so the proof is non-vacuous (a swapped P argument order, a missing project, or two independent predicate symbols would not have proved). The disclosed PARTIAL restrictions are genuine and specific: one-column-per-side is forced by the DSL's single-column Scan, and a single uninterpreted 2-ary predicate is actually the most general single-atom condition over the two-column row (any left-only or right-only condition is an instance of it). The source's self-join exclusion is a planner heuristic (swapping a self-join yields an identical tree), not a semantic precondition, so no essential assumption is silently dropped. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 66898375}, panicked=False

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

### `IntersectToDistinct` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/IntersectToDistinctRule.java
- Attempts used: 30
- Last updated: 2026-09-22T03:13:20.981500+00:00
- Reason / notes: IntersectToDistinct's correctness rests on the counting algebra of COUNT(*) — that aggregating each branch by all columns makes each distinct row appear exactly once per branch, and that a second group-by + count over the UNION ALL then yields the number of branches containing a row, which equals n precisely for set-intersection membership. QED treats aggregate calls as uninterpreted and can only establish aggregate equivalence from bag equality of their inputs (it knows no COUNT/aggregate algebra), so it has no axioms relating the plain set-semantic INTERSECT on the before side to the nested-aggregate pipeline on the after side, regardless of encoding. The porter actually died on an infrastructure error (context-length overflow) before testing anything, but the conclusion holds; the non-pushdown variant is doubly out of reach since its `COUNT(*) FILTER (WHERE ...)` form isn't even expressible in the current AggCall API, and no DSL extension can change QED's aggregate semantics. ```

### `JoinPushTransitivePredicates` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/JoinPushTransitivePredicatesRule.java
- Attempts used: 30
- Last updated: 2026-09-22T01:21:21.713240+00:00
- Reason / notes: This rule is metadata-driven predicate inference, not a structural rewrite — Calcite decomposes the join condition into equi-join equivalence classes and pushes rewrites of pulled-up predicates, so the added filters are valid only as logical consequences of the existing ones (e.g., l.a=r.b ∧ l.a=5 ⇒ r.b=5). In RuleScript the join condition and all filters are independent uninterpreted symbols, and QED explicitly cannot reason about predicate inference/entailment between independent [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]

### `SortRemove` — ⏭️ SKIPPED

- Source backend: Apache Calcite
- Source rule: Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveRule.java
- Attempts used: 30
- Last updated: 2026-09-22T01:36:57.264726+00:00
- Reason / notes: SortRemove is fundamentally a row-ordering rule: its subject operator (a Sort with no offset/limit) and its defining precondition (the input already satisfies the sort's collation, i.e. RelCollation trait preservation) both live in list/ordering semantics, which QED's bag-semantic model explicitly does not model — Sort/Limit/Offset/Window/Sample have no bag-semantic meaning — and no bag-level property can stand in for "input is sorted", so any encoding that drops the Sort node or the sortedness condition ceases to be this rule. Extending the DSL (e.g. adding a sort builder, which the JSON serializer could even carry) cannot close the gap because the missing semantics are on the QED prover side, not the DSL side, so UNSUPPORTED is the correct conclusion even though the porter's stated reason was just an LLM context-length error rather than that analysis. ```

