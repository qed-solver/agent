# RuleScript porting progress

_Last updated: 2026-10-17T08:34:07.582696+00:00_

**210/307 rules proved** (0 failed, 97 skipped as out of QED's supported fragment).

| Rule | Backend | Status | Scope | Attempts | Notes |
|---|---|---|---|---|---|
| `AssociateLimitJoinsLeft` | CockroachDB | ✅ PROVED | PARTIAL | 28 | The encoding faithfully captures the rule's substantive identity — ((A LEFT JOIN B ON p_ab) INNER JOIN C ON p_ac) ≡ reprojected((A INNER ... |
| `AssociateLimitJoinsRight` | CockroachDB | ✅ PROVED | PARTIAL | 8 | before() = C ⋈ (A ⟕ B ON p_ab) and after() = (A ⋈ C ON p_ac) ⟕ B ON p_ab (re-projected to the common (C,A,B) column order) are genuinely ... |
| `CommuteConst` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding faithfully captures the rule's Eq-case semantic core—commutativity of equality—by deliberately using the concrete SqlStdOper... |
| `CommuteConstInequality` | CockroachDB | ✅ PROVED | PARTIAL | 41 | The encoding faithfully lifts the scalar Lt flip into the relational DSL — a maximally-general carrier (INNER cross-join of two unconstra... |
| `CommuteNullIs` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding correctly models the `Is` branch of CockroachDB's CommuteNullIs as a scalar commutation of `IS_NOT_DISTINCT_FROM` with a NUL... |
| `CommuteRightJoin` | CockroachDB | ✅ PROVED | PARTIAL | 4 | The encoding is non-vacuous and semantically faithful: before() is a genuine RightJoin(L, R, P) and after() is a LeftJoin(R, L) with the ... |
| `CommuteVar` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding is a faithful, non-vacuous proof of the Eq fragment of CommuteVar: `before()` and `after()` are structurally different (conc... |
| `CommuteVarInequality` | CockroachDB | ✅ PROVED | PARTIAL | 24 | The encoding faithfully lifts the rule's scalar commutation to the relational level — an inner cross join of two independent same-type nu... |
| `ConvertGroupByToDistinct` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding has the correct relational shape for the source rule: a no-aggregation `Aggregate` (group set = all columns, empty agg list)... |
| `DecorrelateJoin` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding faithfully captures the core DecorrelateJoin transformation: `before()` builds a `LogicalCorrelate` (INNER) where the right ... |
| `DecorrelateProjectSet` | CockroachDB | ✅ PROVED | PARTIAL | 6 | It is an honest, non-degenerate partial encoding (unary input and emitted column) of the source rule's core independence premise: because... |
| `DeduplicateSelectFilters` | CockroachDB | ✅ PROVED | PARTIAL | 21 | before() (filter chain p, q, p) and after() (p, q) are genuinely different, and the shared uninterpreted symbol `p` correctly enforces th... |
| `DetectJoinContradiction` | CockroachDB | ✅ PROVED | PARTIAL | 30 | The encoding faithfully mirrors the source transformation — a join whose ON list contains a contradictory (always-false) item alongside u... |
| `EliminateAggDistinct` | CockroachDB | ✅ PROVED | PARTIAL | 4 | The encoding is a sound, non-vacuous special case rather than a vacuous one: `before()` and `after()` genuinely differ in the AggCall's `... |
| `EliminateAggDistinctForKeys` | CockroachDB | ✅ PROVED | PARTIAL | 4 | The encoding faithfully captures a genuine special case of the rule (input is a scan whose unique key is also the grouping key and the DI... |
| `EliminateAggFilteredDistinctForKeys` | CockroachDB | ✅ PROVED | PARTIAL | 26 | before() and after() differ genuinely (distinct=true vs false on the same aggregate, a flag the JSONSerializer does pass to QED), and the... |
| `EliminateAntiJoin` | CockroachDB | ✅ PROVED | PARTIAL | 5 | The encoding is faithful and non-trivial: it uses an uninterpreted left relation, a zero-row right (the canonical `Empty` form, which is ... |
| `EliminateDistinct` | CockroachDB | ✅ PROVED | PARTIAL | 26 | The encoding is structurally faithful: DistinctOn is exactly a group-by with no aggregation calls (matching `Aggregate(source, [field(0)]... |
| `EliminateDistinctSetLeft` | CockroachDB | ✅ PROVED | PARTIAL | 47 | The encoding matches the rule's real shape: before() is a distinct set operator (`union(false, …)`, i.e. CRDB's non-All `Union`) whose ri... |
| `EliminateDistinctSetRight` | CockroachDB | ✅ PROVED | PARTIAL | 5 | The encoding faithfully captures EliminateDistinctSetRight's semantics: a distinct `Union` (correctly `union(false, ...)`, since the sour... |
| `EliminateExistsGroupBy` | CockroachDB | ✅ PROVED | PARTIAL | 43 | The encoding is faithful and non-vacuous: before() places a non-scalar Aggregate (one uninterpreted group key, no aggregate calls) under ... |
| `EliminateExistsProject` | CockroachDB | ✅ PROVED | PARTIAL | 5 | The encoding faithfully captures the rule's core semantic — a Project is a no-op under EXISTS because it preserves non-emptiness — by mod... |
| `EliminateExistsZeroRows` | CockroachDB | ✅ PROVED | PARTIAL | 9 | The encoding faithfully models the uncorrelated case of the rule: before() is the standard relational desugaring of `WHERE EXISTS(empty s... |
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
| `EliminateUnaryMinus` | CockroachDB | ✅ PROVED | FULL | 21 | The encoding faithfully mirrors the source rule's shape: before() is Project(UNARY_MINUS(UNARY_MINUS(col)), Scan) vs after() = Scan — str... |
| `EliminateUpsertDistinct` | CockroachDB | ✅ PROVED | PARTIAL | 24 | The encoding is non-trivial (before is an Aggregate/GROUP BY, after is a plain Project) and correctly proves that a GROUP BY on a unique ... |
| `EliminateWindow` | CockroachDB | ✅ PROVED | PARTIAL | 6 | QED fundamentally cannot represent a Window operator (no Window builder, no window case in JSONSerializer, and the unmodifiable prover ha... |
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
| `FoldFunctionWithNullArg` | CockroachDB | ✅ PROVED | PARTIAL | 41 | before() and after() are structurally different (projecting EQUALS(x, NULL) vs a bare BOOLEAN null), and the proof is non-vacuous: it suc... |
| `FoldGroupByAndWindow` | CockroachDB | ✅ PROVED | PARTIAL | 61 | The encoding faithfully captures the core logic of FoldGroupByAndWindow for case 5a: it models the Window as a per-partition aggregate jo... |
| `FoldGroupingOperators` | CockroachDB | ✅ PROVED | PARTIAL | 27 | The encoding is a faithful, non-degenerate special case: before() is a genuine nested Aggregate(Aggregate) that folds to a single Aggrega... |
| `FoldInEmpty` | CockroachDB | ✅ PROVED | PARTIAL | 8 | The encoding is non-vacuous: a SEMI join against a zero-row relation is empty, which is the relational analogue of IN over an empty tuple... |
| `FoldInNull` | CockroachDB | ✅ PROVED | PARTIAL | 21 | The encoding is the filter-context relational instance of the In alternative, using the canonical desugaring `x IN (NULL)` ≡ `x = NULL` (... |
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
| `FoldNullAndOr` | CockroachDB | ✅ PROVED | PARTIAL | 41 | The encoding faithfully captures the And branch of FoldNullAndOr: the hard-coded null literal is correct because the source rule matches ... |
| `FoldNullBinaryLeft` | CockroachDB | ✅ PROVED | PARTIAL | 43 | `before()` (filter on `NULL < x`) and `after()` (filter on the null literal) are structurally different, and QED's proof establishes a ge... |
| `FoldNullBinaryRight` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding is a non-vacuous, faithful instance of the rule: `before()` is `Filter(R, x < NULL)` and `after()` is `Filter(R, NULL-litera... |
| `FoldNullComparisonLeft` | CockroachDB | ✅ PROVED | PARTIAL | 26 | before() (`Filter(S, NULL = x)`) and after() (`Filter(S, NULL::bool)`) are structurally different over an arbitrary non-unique, nullable ... |
| `FoldNullComparisonRight` | CockroachDB | ✅ PROVED | PARTIAL | 32 | `before()` (filter on `EQUALS(left, NULL)`) and `after()` (filter on the bare `NULL` literal) are structurally and semantically distinct,... |
| `FoldNullInNonEmpty` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding correctly models `NULL IN (a,b)` as `filter(Or(=(null,a), =(null,b)))` and the folded result as `filter(nullBool)`; both sid... |
| `FoldNullTupleIsTupleNotNull` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding is non-vacuous and on-point: before() carries the real condition AND(IS NOT NULL(NULL), IS NOT NULL(x)) while after() carrie... |
| `FoldNullTupleIsTupleNull` | CockroachDB | ✅ PROVED | PARTIAL | 7 | The encoding faithfully captures the rule's semantic content: IsTupleNull on a constant all-null tuple is expanded to the conjunction of ... |
| `FoldNullUnary` | CockroachDB | ✅ PROVED | PARTIAL | 48 | The encoding faithfully captures the rule's structure for the unary-minus instance: `Project(UNARY_MINUS(NULL))` vs `Project(NULL)` over ... |
| `FoldUnary` | CockroachDB | ✅ PROVED | PARTIAL | 5 | `before()` and `after()` are structurally distinct (`Not(trueLiteral())` vs `falseLiteral()`) and the equivalence proved is exactly the r... |
| `HoistJoinProjectLeft` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The proved identity is non-vacuous and is exactly the rule's core transformation: `before()` = Join_C(π(L), R) and `after()` = π(Join_{C∘... |
| `HoistJoinProjectRight` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding faithfully mirrors the source's HoistJoinProjectRight: the right input's passthrough project (a pure column remap here — the... |
| `HoistJoinSubquery` | CockroachDB | ✅ PROVED | PARTIAL | 65 | The encoding faithfully captures the core transformation of HoistJoinSubquery for the INNER-join case: moving an uncorrelated EXISTS subq... |
| `HoistSelectAboveUnorderedDistinctOn` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding faithfully models the declared special case: an identity, unordered DistinctOn (output = grouping keys, no aggregate outputs... |
| `HoistSelectExists` | CockroachDB | ✅ PROVED | PARTIAL | 44 | The encoding faithfully captures the uncorrelated-special-case of HoistSelectExists: before() is Filter(And(EXISTS(Filter(S,sub_pred)), r... |
| `HoistSelectNotExists` | CockroachDB | ✅ PROVED | PARTIAL | 25 | The encoding faithfully captures the uncorrelated NOT EXISTS → ANTI-join rewrite: before() is σ_{¬EXISTS(σ_{sub_pred}(S)) ∧ rest}(L) and ... |
| `HoistUnboundFilterFromExistsSubquery` | CockroachDB | ✅ PROVED | PARTIAL | 0 |  |
| `HoistUnboundJoinFilterFromExistsSubquery` | CockroachDB | ✅ PROVED | PARTIAL | 102 | The encoding is non-trivial and symbol-correct: the same uninterpreted predicate "P" is shared between the pre-hoist join condition (via ... |
| `InlineAnyValuesMultiCol` | CockroachDB | ✅ PROVED | PARTIAL | 71 | The encoding faithfully captures the core InlineAnyValuesMultiCol transformation (semi-join against a constant multi-row Values inlined i... |
| `InlineAnyValuesSingleCol` | CockroachDB | ✅ PROVED | PARTIAL | 48 | The proved equivalence is genuine and non-vacuous — S SEMI JOIN the concrete 2-row values bag {(1),(3)} on S.c0 = V.c0 is the exact relat... |
| `InlineConstVar` | CockroachDB | ✅ PROVED | PARTIAL | 46 | The proof is non-vacuous and not coincidental: `before()` and `after()` are structurally distinct (same uninterpreted predicate `f` appli... |
| `InlineJoinConstantsLeft` | CockroachDB | ✅ PROVED | PARTIAL | 24 | The encoding faithfully captures the rule's semantic essence — the original's inlinable constant column maps to `a`, the inlined-to const... |
| `InlineJoinConstantsRight` | CockroachDB | ✅ PROVED | PARTIAL | 5 | The encoding is non-trivial and faithful: `before()` references right-input column 0 (`r`) in the join condition while `after()` referenc... |
| `InlineProjectConstants` | CockroachDB | ✅ PROVED | PARTIAL | 21 | The encoding faithfully captures `InlineProjectConstants`' core shape — a `Project` whose input carries an inlinable constant column, wit... |
| `InlineProjectInProject` | CockroachDB | ✅ PROVED | PARTIAL | 24 | The encoding faithfully captures the rule's semantic core — flattening `Project(Project(Source, F(x)), [T(F(x)), F(x)])` into `Project(So... |
| `InlineSelectConstants` | CockroachDB | ✅ PROVED | PARTIAL | 49 | The encoding is non-vacuous and faithful: before() filters on field 0 (`a`) and after() on field 1 (`c`), which are distinct columns whos... |
| `InlineSelectVirtualColumns` | CockroachDB | ✅ PROVED | PARTIAL | 26 | The encoding faithfully mirrors the rule's actual transformation — before() is `Filter(P(v)∧Q(w), Project(F,G,a,b, Scan))` and after() pu... |
| `InvertMinus` | CockroachDB | ✅ PROVED | FULL | 66 | before() = Project[UNARY_MINUS(MINUS(a,b))] and after() = Project[MINUS(b,a)] over a shared, universally-quantified 2-column scan with in... |
| `LeftAssociateJoinsLeft` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding faithfully captures the core algebraic identity of LeftAssociateJoinsLeft — reassociating ((A×B) ⋈_{P(A,C)∧Q(B,C)} C) into B... |
| `LeftAssociateJoinsRight` | CockroachDB | ✅ PROVED | FULL | 25 | The encoding faithfully captures the source rule's logical transformation: (A⋈B ON true)⋈C ON (p_ax∧p_bu) reassociates to A⋈(B⋈C ON p_bu)... |
| `MapEqualityIntoJoinLeftAndRight` | CockroachDB | ✅ PROVED | PARTIAL | 24 | The encoding faithfully captures the rule's core equality-remapping operation for its own documented minimal instance: before() uses the ... |
| `MapFilterIntoJoinLeft` | CockroachDB | ✅ PROVED | PARTIAL | 41 | The encoding is a faithful, non-vacuous capture of the rule's core transformation — it reproduces the rule's own documented example (`ON ... |
| `MapFilterIntoJoinRight` | CockroachDB | ✅ PROVED | PARTIAL | 28 | before() (ON = a.x=b.x ∧ f(a.x)) and after() (ON = a.x=b.x ∧ f(b.x)) are structurally distinct, so the proof is not vacuous — it requires... |
| `MergeProjectWithValues` | CockroachDB | ✅ PROVED | PARTIAL | 100 | Directly encoded using RuleBuilder's real Calcite Values-with-literal-content support (RelBuilder.values(fieldNames, values...)) wrapped ... |
| `MergeProjects` | CockroachDB | ✅ PROVED | PARTIAL | 61 | The encoding faithfully captures the Optgen rule's shape, precondition, and effect: `before` is Project(Project(Source, [passthrough, Bot... |
| `MergeSelectInnerJoin` | CockroachDB | ✅ PROVED | PARTIAL | 21 | The proof is non-vacuous and captures the rule's exact logical core — before() is Filter(F, InnerJoin(L, R, ON)) and after() is InnerJoin... |
| `MergeSelects` | CockroachDB | ✅ PROVED | FULL | 3 | The encoding faithfully captures MergeSelects: `before()` is two nested filters (inner then outer) and `after()` is a single filter on th... |
| `NegateAnd` | CockroachDB | ✅ PROVED | FULL | 7 | before() is Filter(¬(P∧Q), Source) and after() is Filter(¬P∨¬Q, Source) over the same scan, with P and Q as distinct uninterpreted predic... |
| `NegateComparison` | CockroachDB | ✅ PROVED | PARTIAL | 21 | The proof is non-vacuous and genuine: `before()` is `Filter(¬(x = y))` vs `after()` `Filter(x <> y)` over the cross-join of two *independ... |
| `NegateOr` | CockroachDB | ✅ PROVED | FULL | 5 | The encoding faithfully captures NegateOr — `Not(Or(L,R))` ⟹ `And(Not(L),Not(R))` — using two *distinct* uninterpreted predicates (`left`... |
| `NormalizeCmpPlusConst` | CockroachDB | ✅ PROVED | PARTIAL | 65 | The encoding faithfully captures the rule's core transformation — (a + c1) < c2 ⇒ a < (c2 − c1), with correct Plus/Minus operand directio... |
| `NormalizeInConst` | CockroachDB | ✅ PROVED | PARTIAL | 21 | before() (x IN (a,b,a,b), a 4-operand OR of EQUALS) and after() (x IN (a,b), a 2-operand OR) differ structurally by exactly the duplicate... |
| `NormalizeJoinNotAnyFilter` | CockroachDB | ✅ PROVED | PARTIAL | 0 |  |
| `NormalizeNestedAnds` | CockroachDB | ✅ PROVED | PARTIAL | 21 | The encoding is a faithful, non-vacuous base case of the rule — `before()` = `p ∧ (q ∧ r)` and `after()` = `(p ∧ q) ∧ r` are structurally... |
| `NormalizeSelectAnyFilter` | CockroachDB | ✅ PROVED | PARTIAL | 0 |  |
| `NormalizeSelectNotAnyFilter` | CockroachDB | ✅ PROVED | PARTIAL | 0 |  |
| `ProjectInnerJoinValues` | CockroachDB | ✅ PROVED | PARTIAL | 23 | before() (an INNER join with a one-row literal Values) and after() (an identity+constants Project followed by a Filter) are structurally ... |
| `PruneAggCols` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding is a faithful, non-degenerate special case of PruneAggCols: before() has Aggregate(key; f(a), g(b)) projected to (key, f), a... |
| `PruneExplainCols` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding matches PruneExplainCols exactly in shape — `Explain(input)` ⟹ `Explain(PruneCols(input, needed))` with no wrapping Project ... |
| `PruneGroupByCols` | CockroachDB | ✅ PROVED | PARTIAL | 23 | It is a nontrivial partial instance of PruneGroupByCols: the unused fourth input column is removed before the same uninterpreted GROUP BY... |
| `PruneInsertReturnCols` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding faithfully captures the essential bag-semantic equivalence of `PruneInsertReturnCols` — removing a return column that the ou... |
| `PruneJoinLeftCols` | CockroachDB | ✅ PROVED | PARTIAL | 10 | The encoding faithfully captures `PruneJoinLeftCols` as a specific, honestly-labeled (PARTIAL) instance — an inner join whose left input ... |
| `PruneJoinRightCols` | CockroachDB | ✅ PROVED | PARTIAL | 4 | The encoding faithfully captures the core PruneJoinRightCols transformation: before() is `Project(G(L0,R0), InnerJoin(C(L0,R0), L, R(2col... |
| `PruneMutationFetchCols` | CockroachDB | ✅ PROVED | PARTIAL | 5 | before() (scan(I0,I1) → M(I0)) and after() (scan(I0,I1) → project(I0) → M(I0')) are structurally different and correctly share the M oper... |
| `PruneMutationInputCols` | CockroachDB | ✅ PROVED | PARTIAL | 21 | The encoding captures the rule's genuine soundness content — pushing a column-pruning projection below a per-row operator whose behavior ... |
| `PruneMutationReturnCols` | CockroachDB | ✅ PROVED | PARTIAL | 46 | The encoding is structurally non-trivial (4-wide vs 3-wide mutation output with correct ordinal remapping of the shared passthrough colum... |
| `PruneOrdinalityCols` | CockroachDB | ✅ PROVED | PARTIAL | 26 | The encoding matches the real rule's shape (outer Project over an Ordinality-derived column, with a column drop pushed beneath the Ordina... |
| `PruneProjectCols` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding is a faithful, non-vacuous special case of PruneProjectCols — `before()` is a Scan → inner-Project(3 cols: `col0`, `Used(col... |
| `PruneProjectSetCols` | CockroachDB | ✅ PROVED | PARTIAL | 45 | The encoding faithfully models ProjectSet's row-generating semantics as an INNER join of the input with an uninterpreted emitted-row rela... |
| `PruneScanCols` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding faithfully captures the semantic core of PruneScanCols — inserting a column-pruning projection below an outer projection tha... |
| `PruneSelectCols` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding is non-vacuous and faithful: `before()` = π_Top(σ_F(S₃)) and `after()` = π_Top(σ_F(π_{0,1}(S₃))) are structurally distinct t... |
| `PruneSemiAntiJoinRightCols` | CockroachDB | ✅ PROVED | PARTIAL | 24 | The encoding is a genuine, non-trivial partial instance of PruneSemiAntiJoinRightCols: before() is an ANTI join over a 2-column right sid... |
| `PruneUnionAllCols` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding matches the source rule's exact shape — a Project over a UnionAll (correctly `all=true`) rewritten into the same outer proje... |
| `PruneValuesCols` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding reproduces the source rule's genuine shape — an outer Project (uninterpreted `Top` on one column, passthrough on the other) ... |
| `PruneWindowInputCols` | CockroachDB | ✅ PROVED | PARTIAL | 44 | The encoding faithfully captures the logical content of PruneWindowInputCols for per-partition window aggregates: before() has the window... |
| `PruneWindowOutputCols` | CockroachDB | ✅ PROVED | PARTIAL | 28 | `before()` truly differs from `after()` only by computing an extra unused window function `w2` (two aggregate calls vs one in the per-par... |
| `PruneWithCols` | CockroachDB | ✅ PROVED | PARTIAL | 21 | The encoding faithfully mirrors PruneWithCols's shape — before() is Project([Expr(c0), c0]) above the 1-column identity standing in for t... |
| `PruneWithScanCols` | CockroachDB | ✅ PROVED | PARTIAL | 24 | The encoding faithfully captures PruneWithScanCols: a shared outer Project built from one uninterpreted projection symbol `Top(w0, w1)` o... |
| `PushAggDistinctIntoGroupBy` | CockroachDB | ✅ PROVED | PARTIAL | 31 | The encoding is a genuine, non-vacuous instance of the source rule: before() is a GroupBy whose aggregation list is exactly one DISTINCT ... |
| `PushAssignmentCastsIntoValues` | CockroachDB | ✅ PROVED | PARTIAL | 104 | The encoding faithfully mirrors the rule's structure in a non-degenerate special case: an uninterpreted single-column Values, one uninter... |
| `PushFilterIntoJoinLeft` | CockroachDB | ✅ PROVED | PARTIAL | 22 | `before()` (inner join with conjuncts `f(L) ∧ g(L,R)`) and `after()` (left filtered by `f(L)`, then joined on `g(L,R)`) are structurally ... |
| `PushFilterIntoJoinLeftAndRight` | CockroachDB | ✅ PROVED | PARTIAL | 83 | The encoding is a faithful, non-degenerate minimal instance of the rule: `before()` is an inner join with ON `(L.0=R.0 ∧ L.1=R.1 ∧ L.0=R.... |
| `PushFilterIntoJoinRight` | CockroachDB | ✅ PROVED | PARTIAL | 3 | The encoding is a faithful (though honestly scope-limited) INNER-join instance of the rule: `before()` is `L ⋈_INNER R ON f(R) ∧ g(L,R)` ... |
| `PushFilterIntoSetOp` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding is a faithful, non-trivial instance of the rule: `before()` is `Filter(Intersect(L,R), P)` and `after()` is `Intersect(Filte... |
| `PushLeakproofFiltersIntoPermeableBarrier` | CockroachDB | ✅ PROVED | PARTIAL | 46 | The encoding faithfully captures the rule's logical content: a leakproof predicate is pushed from above an identity operation (the Barrie... |
| `PushLeakproofJoinIntoPermeableBarrierLeft` | CockroachDB | ✅ PROVED | PARTIAL | 26 | The encoding is faithful: it models the permeable Barrier as an identity projection (correct, since a Barrier is a pure flow-control poin... |
| `PushLeakproofJoinIntoPermeableBarrierRight` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding captures the rule's bag-semantic core exactly — Join(L, Barrier(R), C) vs Barrier(Join(L, R, C)) with the Barrier modeled as... |
| `PushLeakproofProjectionsIntoPermeableBarrier` | CockroachDB | ✅ PROVED | PARTIAL | 25 | The before/after shapes genuinely reflect the optimization — the Project moves from above the barrier to below it (not a structural ident... |
| `PushSelectCondLeftIntoJoinLeftAndRight` | CockroachDB | ✅ PROVED | PARTIAL | 26 | The encoding faithfully captures the rule's documented case generalized honestly: arbitrary uninterpreted tables L, R with an arbitrary u... |
| `PushSelectIntoGroupBy` | CockroachDB | ✅ PROVED | PARTIAL | 26 | `before()` (Filter above the Aggregate) and `after()` (Filter inside, below the Aggregate) are structurally different plans, so the equiv... |
| `PushSelectIntoInlinableProject` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding is non-vacuous and structurally faithful: `before()` is Filter(P∘col0 ∧ Q∘col1) over Project(F(a,b), G(a,b), a, b) with P/Q ... |
| `PushSelectIntoJoinLeft` | CockroachDB | ✅ PROVED | PARTIAL | 42 | The encoding correctly captures the core LEFT-JOIN push-down transformation: before() is Select(LeftJoin(L,R,g), f(l)∧u(l,r)) and after()... |
| `PushSelectIntoOrdinality` | CockroachDB | ✅ PROVED | PARTIAL | 46 | The encoding faithfully mirrors the source rule's core transformation — before() is `Select(Ordinality(input), P(input))` and after() is ... |
| `PushSelectIntoProject` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding faithfully instantiates the rule's essential content — a bound conjunct P on the input/passthrough column is pushed below th... |
| `PushSelectIntoProjectSet` | CockroachDB | ✅ PROVED | PARTIAL | 4 | The ProjectSet is soundly abstracted as an INNER join of its input L with an emitted bag E under an uninterpreted pairing predicate M — I... |
| `PushSelectIntoUnorderedDistinctOn` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The encoding is a faithful, non-degenerate PARTIAL special case: it models the identity (unordered, no-aggregate) DistinctOn as a group-b... |
| `PushSelectIntoWindow` | CockroachDB | ✅ PROVED | PARTIAL | 61 | The two plans are structurally different — the window's aggregate is computed over the unfiltered input in `before()` but over the P-filt... |
| `ReduceNotNullGroupingCols` | CockroachDB | ✅ PROVED | PARTIAL | 44 | The encoding is a correct, non-degenerate special case: `before()` (GROUP BY col0, col1 where col0≡col1) and `after()` (GROUP BY col0, th... |
| `ReduceWindowPartitionCols` | CockroachDB | ✅ PROVED | PARTIAL | 0 |  |
| `RejectNullsLeftJoin` | CockroachDB | ✅ PROVED | PARTIAL | 27 | The encoding faithfully captures the rule's core semantic transformation (a null-rejecting filter on the right side of a LEFT JOIN makes ... |
| `RejectNullsProject` | CockroachDB | ✅ PROVED | PARTIAL | 104 | The encoding faithfully captures the structural shape of RejectNullsProject (adding an IS NOT NULL filter below a Project while retaining... |
| `RejectNullsRightJoin` | CockroachDB | ✅ PROVED | PARTIAL | 10 | The encoding is a genuine, non-vacuous special case of the source rule: before() uses a FULL join and after() a LEFT join (a real operato... |
| `RejectNullsUnderJoinLeft` | CockroachDB | ✅ PROVED | PARTIAL | 22 | The porter's encoding is a faithful special case of RejectNullsUnderJoinLeft: the original rule fires when the ON condition null-rejects ... |
| `RejectNullsUnderJoinRight` | CockroachDB | ✅ PROVED | PARTIAL | 5 | before() = Join(L, R, on ∧ IS_NOT_NULL(r)) and after() = Join(L, R⇝IS_NOT_NULL(r), on ∧ IS_NOT_NULL(r)) are genuinely different plans, an... |
| `RemoveJoinNotNullCondition` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding faithfully models the rule's core move — dropping a tautological `IS NOT NULL` conjunct from an outer join's ON condition — ... |
| `RemoveNotNullCondition` | CockroachDB | ✅ PROVED | FULL | 23 | The encoding faithfully captures the rule's single essential precondition — `IsColNotNull $col $input` — by giving column 0 a non-nullabl... |
| `RemoveZeroCardLock` | CockroachDB | ✅ PROVED | PARTIAL | 43 | The rule only fires when `$rows` has zero rows — i.e. `$rows` is bag-equal to ∅ — so modeling `$rows` as a structurally empty scan and `L... |
| `RightAssociateJoinsLeft` | CockroachDB | ✅ PROVED | FULL | 23 | The encoding faithfully models the source rule: before is `A ⋈(B ⋈_true C)` with condition `p_ab(A,B) ∧ p_abc(A,B,C)`, and after is `(A ⋈... |
| `RightAssociateJoinsRight` | CockroachDB | ✅ PROVED | FULL | 45 | The encoding exactly mirrors the source variant: before = A ⋈_{p_ac∧p_abc} (B ⋈_true C), after = (A ⋈_{p_ac} C) ⋈_{p_abc} B, with INNER j... |
| `SimplifyAndFalse` | CockroachDB | ✅ PROVED | FULL | 4 | The encoding faithfully captures `(And * (False)) => (False)`: the left operand is a fully uninterpreted predicate (universally quantifie... |
| `SimplifyAndTrue` | CockroachDB | ✅ PROVED | FULL | 3 | The encoding is a faithful relational embedding of CockroachDB's scalar rule (And $left:* (True)) => $left: before() is Filter(And(P, Tru... |
| `SimplifyCaseSingleFalsyBranch` | CockroachDB | ✅ PROVED | PARTIAL | 47 | The encoding is non-vacuous and faithful: the WHEN condition is a proper uninterpreted predicate, the 3VL expansion (cond∧False)∨(¬cond∧N... |
| `SimplifyEqualsAnyTuple` | CockroachDB | ✅ PROVED | FULL | 24 | before() (S SEMI JOIN V ON S.c0=V.c0) and after() (S FILTER (S.c0 IN (SELECT c0 FROM V))) are structurally distinct relational forms of t... |
| `SimplifyExcept` | CockroachDB | ✅ PROVED | PARTIAL | 14 | before() correctly encodes set EXCEPT (`minus(all=false)`), and although after() uses an ANTI join rather than the source's `ExceptAll` (... |
| `SimplifyFalseAnd` | CockroachDB | ✅ PROVED | FULL | 3 | The before pattern is `Filter(Source, AND(false, right))` and after is `Filter(Source, false)`, with `right` an uninterpreted predicate a... |
| `SimplifyFalseOr` | CockroachDB | ✅ PROVED | PARTIAL | 10 | The encoding captures the rule's only logical content — the boolean identity Or(False, P) ≡ P, with P a fully uninterpreted predicate ove... |
| `SimplifyInSingleElement` | CockroachDB | ✅ PROVED | FULL | 27 | The encoding models `x IN (y)` by its definitional expansion — a single-disjunct `Or(x = y)` — in `before()`, and the bare `Eq` in `after... |
| `SimplifyIntersectLeft` | CockroachDB | ✅ PROVED | PARTIAL | 25 | before() (set-semantic INTERSECT, all=false, 2 inputs) and after() (SEMI join of the keyed left against the right on IS NOT DISTINCT FROM... |
| `SimplifyIntersectRight` | CockroachDB | ✅ PROVED | PARTIAL | 48 | The encoding is non-vacuous — a set-INTERSECT (before) vs. a SEMI-join (after) are genuinely different operators, and the equality hinges... |
| `SimplifyIsCondition` | CockroachDB | ✅ PROVED | PARTIAL | 24 | The encoding matches the rule exactly in every load-bearing respect: before() applies `IS_NOT_DISTINCT_FROM` and after() applies `EQUALS`... |
| `SimplifyIsNullCondition` | CockroachDB | ✅ PROVED | FULL | 22 | The encoding is structurally non-vacuous and captures the exact logical content of the rule: `before()` is the conjunction of an arbitrar... |
| `SimplifyJoinFilters` | CockroachDB | ✅ PROVED | PARTIAL | 21 | before() (`ON rest AND True`) and after() (`ON rest`) are structurally distinct, share exactly one uninterpreted `rest` predicate over th... |
| `SimplifyJoinNotNullEquality` | CockroachDB | ✅ PROVED | PARTIAL | 72 | The encoding is a faithful, non-trivial special case: it correctly models the `IsNot(Eq, False) → Eq` sub-case with non-null column types... |
| `SimplifyLeftJoin` | CockroachDB | ✅ PROVED | PARTIAL | 23 | The encoding is non-vacuous and correctly shaped: `before()` and `after()` genuinely differ only in join kind (LEFT vs INNER), and the eq... |
| `SimplifyLeftJoinWithZeroRowsRight` | CockroachDB | ✅ PROVED | PARTIAL | 29 | The encoding faithfully mirrors the source rule — a LEFT join whose right input is structurally the empty relation (the exact bag-theoret... |
| `SimplifyNotInSingleElement` | CockroachDB | ✅ PROVED | FULL | 6 | The encoding is a faithful relational statement of the scalar rule: before() builds `left NOT IN (right)` as filter `NOT(left = right)` (... |
| `SimplifyOrFalse` | CockroachDB | ✅ PROVED | PARTIAL | 8 | The encoding faithfully captures the core boolean identity of SimplifyOrFalse (Or(x, False) ≡ x) using a properly uninterpreted predicate... |
| `SimplifyOrTrue` | CockroachDB | ✅ PROVED | FULL | 7 | The encoding is a direct, faithful translation of `SimplifyOrTrue`: the LHS `(Or * True)` becomes the filter condition `Or(left, True)` w... |
| `SimplifyRange` | CockroachDB | ✅ PROVED | PARTIAL | 98 | The source rule is sound precisely because CockroachDB's Range is a semantically transparent boolean marker, and since QED cannot attach ... |
| `SimplifyRightJoin` | CockroachDB | ✅ PROVED | PARTIAL | 6 | The encoding uses the correct operators (FULL→LEFT on the same shared `cond`), and `before()`/`after()` genuinely differ in join kind so ... |
| `SimplifySameVarEqualities` | CockroachDB | ✅ PROVED | PARTIAL | 24 | The encoding is a faithful, non-vacuous capture of the rule's Eq branch: reusing the single `x` for both operands correctly enforces `Var... |
| `SimplifySameVarInequalities` | CockroachDB | ✅ PROVED | PARTIAL | 24 | The encoding faithfully captures the source rule's `Ne` branch: `before` is the filter `x != x` (both operands the same field `x`, correc... |
| `SimplifySelectFilters` | CockroachDB | ✅ PROVED | PARTIAL | 22 | `before()` (Filter(scan, Or(p, NULL))) and `after()` (Filter(scan, p)) are structurally different, and the proven identity is a genuine, ... |
| `SimplifyTrueAnd` | CockroachDB | ✅ PROVED | FULL | 4 | `before()` is `Filter(AND(TRUE, P), S)` and `after()` is `Filter(P, S)` over the same uninterpreted scan `S` and the same uninterpreted p... |
| `SimplifyTrueOr` | CockroachDB | ✅ PROVED | FULL | 3 | The encoding faithfully captures CockroachDB's `SimplifyTrueOr` rule (`Or(True, *) → True`) by using `RexRN.trueLiteral()` for the litera... |
| `SimplifyZeroCardinalityAntiJoin` | CockroachDB | ✅ PROVED | PARTIAL | 88 | The encoding matches the rule's exact shape — an ANTI join of an uninterpreted left relation with the source's empty on-list rendered as ... |
| `SimplifyZeroCardinalitySemiJoin` | CockroachDB | ✅ PROVED | PARTIAL | 24 | The encoding faithfully captures a genuine special case of the source rule: it proves that SEMI(left, on, ∅) ≡ ∅(left's columns) for all ... |
| `SortFiltersInJoin` | CockroachDB | ✅ PROVED | PARTIAL | 27 | The encoding faithfully captures the only semantic content of SortFiltersInJoin — that reordering conjunctive ON-filters of an InnerJoin ... |
| `TryDecorrelateGroupBy` | CockroachDB | ✅ PROVED | PARTIAL | 68 | The encoding is a faithful, non-vacuous special case of the source rule: `before()` (join L with pre-grouped R, then filter `on`) and `af... |
| `TryDecorrelateInnerJoin` | CockroachDB | ✅ PROVED | PARTIAL | 82 | The encoding faithfully captures the core algebraic identity of `TryDecorrelateInnerJoin` for the INNER-join case: [NOTE: response was tr... |
| `TryDecorrelateInnerLeftJoin` | CockroachDB | ✅ PROVED | PARTIAL | 27 | The encoding correctly captures the source rule's structural identity — `L INNER JOIN (IL LEFT JOIN IR ON innerOn) ON on(L,IL)` ⟹ `(L INN... |
| `TryDecorrelateProject` | CockroachDB | ✅ PROVED | PARTIAL | 62 | The encoding faithfully captures the structural essence of the original rule (Join(left, Project(input,…), on) → Filter(on, Project(Cross... |
| `TryDecorrelateProjectInnerJoin` | CockroachDB | ✅ PROVED | PARTIAL | 72 | The encoding exactly mirrors the rule's shape — LEFT(L, Project(InnerJoin(IL, IR, innerOn)), on) rewritten to LEFT(L, Project(InnerJoin(I... |
| `TryDecorrelateProjectSelect` | CockroachDB | ✅ PROVED | PARTIAL | 0 |  |
| `TryDecorrelateProjectSet` | CockroachDB | ✅ PROVED | PARTIAL | 41 | The encoding faithfully captures the core structural rewrite: before is `L ⋈_on (I ⋈_M E)` and after is `Filter_on ((L×I) ⋈_M E)`, which ... |
| `TryDecorrelateSelect` | CockroachDB | ✅ PROVED | PARTIAL | 0 |  |
| `TryDecorrelateSemiJoin` | CockroachDB | ✅ PROVED | PARTIAL | 47 | The encoding faithfully captures the core algebraic identity of TryDecorrelateSemiJoin — SEMI correlate(L, R, on) ≡ GroupBy(INNER correla... |
| `TryDecorrelateWindow` | CockroachDB | ✅ PROVED | PARTIAL | 1 | Manual encoding via PushSelectIntoWindow's join-back-on-partition idiom + TryDecorrelateGroupBy's single-key precedent; confirmed with ne... |
| `TryRemapJoinOuterColsLeft` | CockroachDB | ✅ PROVED | PARTIAL | 83 | The encoding is a non-vacuous, honest PARTIAL special case: it models the InnerJoinApply variant where a correlated INNER join enforces L... |
| `TryRemapJoinOuterColsRight` | CockroachDB | ✅ PROVED | PARTIAL | 0 |  |
| `TryRemapSelectOuterCols` | CockroachDB | ✅ PROVED | PARTIAL | 57 | The encoding faithfully captures the rule's core mechanism — under the Select filter equality c0=c1 (outer stand-in = local column), the ... |
| `ApplyLimitToRecursiveCTEScan` | CockroachDB | ⏭️ SKIPPED | — | 8 | ApplyLimitToRecursiveCTEScan is a pure optimizer-metadata rule — it only re-derives and re-attaches a bounded-cardinality property to the... |
| `CollapseRepeatedLikePatternWildcards` | CockroachDB | ⏭️ SKIPPED | — | 21 | The rule's sole semantic content is the glob-theory fact that consecutive '%' wildcards are equivalent to one ('x LIKE a%%b' ⟺ 'x LIKE a%... |
| `ConsolidateSelectFilters` | CockroachDB | ⏭️ SKIPPED | — | 32 | ConsolidateSelectFilters is a pure representational rewrite whose sole correctness claim is that CockroachDB's Range scalar is evaluation... |
| `ConvertCountToCountRows` | CockroachDB | ⏭️ SKIPPED | — | 0 | The rule is conditional: COUNT(x) becomes COUNT(*) only when ExprIsNeverNull(x) holds. QED's prover does model null-skipping, but only wh... |
| `ConvertJSONSubscriptToFetchValue` | CockroachDB | ⏭️ SKIPPED | — | 25 | The rule's entire content is the semantic identity `Indirection(x, y) ≡ FetchVal(x, y)` on JSON inputs — i.e., a backend operator's bespo... |
| `ConvertLevenshteinToLevenshteinLessEqualLeft` | CockroachDB | ⏭️ SKIPPED | — | 44 | The rule's validity rests on the clamp/truncation semantics of `levenshtein_less_equal(a,b,m)` relative to `levenshtein(a,b)` (its value ... |
| `ConvertLevenshteinToLevenshteinLessEqualRight` | CockroachDB | ⏭️ SKIPPED | — | 21 | The rewrite's soundness rests entirely on CockroachDB's bespoke internal semantics — `levenshtein_less_equal(a,b,n) = min(levenshtein(a,b... |
| `ConvertLikeEscapeToLike` | CockroachDB | ⏭️ SKIPPED | — | 26 | The rewrite is valid only because of a CockroachDB function-library fact — the 2-arg `Like(x, p)` is by definition the same boolean funct... |
| `ConvertRegressionCountToCount` | CockroachDB | ⏭️ SKIPPED | — | 27 | The rule's correctness rests on an algebraic identity between two distinct aggregate functions — regr_count(y,x) ≡ count(x) when one argu... |
| `ConvertUncorrelatedExistsToCoalesceSubquery` | CockroachDB | ⏭️ SKIPPED | — | 24 | The after-side's validity rests entirely on `Limit(Q,1)` having 0 rows iff `Q` is empty and 1 row iff non-empty, plus scalar-subquery NUL... |
| `ConvertUnionToDistinctUnionAll` | CockroachDB | ⏭️ SKIPPED | — | 101 | The rule's RHS requires DistinctOn/ConstAgg to return non-key values that are functionally determined by the grouping key, but QED treats... |
| `ConvertZipArraysToValues` | CockroachDB | ⏭️ SKIPPED | — | 85 | The rule's soundness is a list-level identity — row-wise unrolling of an array by a set-returning zip function (unnest/json_array_element... |
| `EliminateCaseTrailingFalsyBranch` | CockroachDB | ⏭️ SKIPPED | — | 45 | The rule's correctness depends on CASE's three-valued branch-selection semantics, but the RuleScript scalar fragment only interprets And/... |
| `EliminateCast` | CockroachDB | ⏭️ SKIPPED | — | 9 | EliminateCast's soundness rests entirely on the axiom "a cast to the input's own type is the identity function," but QED's SMT prover tre... |
| `EliminateCoalesce` | CockroachDB | ⏭️ SKIPPED | — | 28 | EliminateCoalesce's validity rests entirely on the scalar identity COALESCE(x) = x, but in RuleScript "Coalesce" is just an uninterpreted... |
| `EliminateConstValueSubquery` | CockroachDB | ⏭️ SKIPPED | — | 66 | The rule's soundness rests entirely on the evaluation semantics of the value-returning UNIQUE scalar-subquery operator (a single-row cons... |
| `EliminateDistinctNoColumns` | CockroachDB | ⏭️ SKIPPED | — | 22 | The rule’s validity depends on first-row semantics: a `DistinctOn` with no grouping columns returns the first input row under a specific ... |
| `EliminateDistinctOnValues` | CockroachDB | ⏭️ SKIPPED | — | 0 | The rule removes a DistinctOn whose input is a constant Values that is already distinct on the grouping columns. The porter's encoding pr... |
| `EliminateEnsureDistinctNoColumns` | CockroachDB | ⏭️ SKIPPED | — | 102 | The rule's essential mechanism is error-raising — EnsureDistinctOn/Max1Row/ErrorOnDup must fail when the no-grouping-columns input has mo... |
| `EliminateExistsLimit` | CockroachDB | ⏭️ SKIPPED | — | 14 | The identity holds only under the scalar existential projection (EXISTS) — the two sides are not bag-equal (`LIMIT n·Q ≠ Q` as bags), and... |
| `EliminateGroupBy` | CockroachDB | ⏭️ SKIPPED | — | 46 | QED encodes group-by aggregate calls as uninterpreted operators whose only equational power is bag-equality of their inputs, and it has n... |
| `EliminateLimit` | CockroachDB | ⏭️ SKIPPED | — | 22 | EliminateLimit's correctness rests on two things QED fundamentally lacks, per the Limitations section: it models no list/ordering semanti... |
| `EliminateMax1Row` | CockroachDB | ⏭️ SKIPPED | — | 24 | Max1Row is an order-dependent LIMIT-1 operator ("keep the first row"), and QED's bag-semantic theory explicitly models no list/ordering s... |
| `EliminateOffset` | CockroachDB | ⏭️ SKIPPED | — | 22 | QED models Offset as an uninterpreted QOp with no algebraic axioms (per qed.pdf §6.2, which states it does not support queries using Limi... |
| `EliminateUDFCallSubquery` | CockroachDB | ⏭️ SKIPPED | — | 58 | The rule's correctness rests exactly on the algebraic identity of the value-returning scalar-subquery operator — unique(Values[<e>]) = <e... |
| `FoldArray` | CockroachDB | ⏭️ SKIPPED | — | 24 | In any encoding, FoldArray reduces to a value equation between an uninterpreted function application and a stored constant: the `Array` c... |
| `FoldAssignmentCast` | CockroachDB | ⏭️ SKIPPED | — | 5 | FoldAssignmentCast rewrites AssignmentCast(const, T) into a concrete folded literal, so its correctness rests on a value equation cast(c)... |
| `FoldCast` | CockroachDB | ⏭️ SKIPPED | — | 8 | FoldCast's claim is that `cast(const) = folded_literal`, and its correctness rests entirely on the cast operator's value-producing semant... |
| `FoldCollate` | CockroachDB | ⏭️ SKIPPED | — | 23 | FoldCollate's correctness rests on the value identity `collate(const, locale) = castToCollatedString(const, locale)`, but QED models both... |
| `FoldDivOne` | CockroachDB | ⏭️ SKIPPED | — | 21 | The rule's correctness rests on the scalar identity x / 1 = Cast(x, τ), but QED's fixed theory (equality, uninterpreted functions/predica... |
| `FoldEqualsAnyNull` | CockroachDB | ⏭️ SKIPPED | — | 10 | I verified directly in the DSL and type sources that QED's theory has no array sort (RelType.java states all virtual types are translated... |
| `FoldFloorDivOne` | CockroachDB | ⏭️ SKIPPED | — | 6 | The rule's correctness rests on the numeric identity floor_div(x, 1) = x plus the semantics of the int→int cast, but QED's fixed theory (... |
| `FoldFunction` | CockroachDB | ⏭️ SKIPPED | — | 32 | FoldFunction requires evaluating an arbitrary function call over constants, which means the prover must know that function's internal eva... |
| `FoldJSONAccessIntoValues` | CockroachDB | ⏭️ SKIPPED | — | 0 | The rule unnests a single-column Values whose rows are JSON objects into one Values column per JSON key, and rewrites -> field accesses t... |
| `FoldLimits` | CockroachDB | ⏭️ SKIPPED | — | 21 | FoldLimits is only sound because Limit sorts its input by the named ordering and takes the first n rows, and the guard (inner ordering im... |
| `FoldMinusZero` | CockroachDB | ⏭️ SKIPPED | — | 24 | FoldMinusZero is a purely scalar arithmetic normalization (`x - 0` → `cast(x, τ)`) whose validity rests on the subtract-by-zero identity ... |
| `FoldMultOne` | CockroachDB | ⏭️ SKIPPED | — | 23 | FoldMultOne's correctness rests entirely on the scalar identity x * 1 = cast_τ(x), but QED's fixed theory contains no multiplication axio... |
| `FoldNullCast` | CockroachDB | ⏭️ SKIPPED | — | 51 | FoldNullCast's entire content is the scalar identity cast(NULL) = NULL, whose correctness rests on the internal null-preserving semantics... |
| `FoldOneMult` | CockroachDB | ⏭️ SKIPPED | — | 24 | FoldOneMult is a purely scalar normalization whose only semantic content is the identity 1·x = cast_τ(x) (multiply-by-one is the value id... |
| `FoldPlusZero` | CockroachDB | ⏭️ SKIPPED | — | 27 | FoldPlusZero's correctness rests entirely on the additive identity (x + 0 = x) and cast semantics, but QED's fixed oracle theory models s... |
| `FoldTupleAccessIntoValues` | CockroachDB | ⏭️ SKIPPED | — | 81 | The rule's correctness rests on tuple-component semantics — that a Values' single tuple column and its unnested sibling columns denote th... |
| `FoldZeroPlus` | CockroachDB | ⏭️ SKIPPED | — | 50 | The rule rewrites the scalar term `PLUS(0, x)` into `CAST(x)`, but both operators are introduced by name as uninterpreted function symbol... |
| `HoistProjectSetSubquery` | CockroachDB | ⏭️ SKIPPED | — | 0 | The rule operates on ProjectSet, whose zipped set-returning functions have no operator in the DSL or the prover, so even the unhoisted in... |
| `HoistProjectSubquery` | CockroachDB | ⏭️ SKIPPED | — | 0 | In the DSL a scalar subquery reaches the prover as an opaque $SCALAR_QUERY operator with no relation to a join, so a subquery in a filter... |
| `HoistSelectSubquery` | CockroachDB | ⏭️ SKIPPED | — | 0 | In the DSL a scalar subquery reaches the prover as an opaque $SCALAR_QUERY operator with no relation to a join, so a subquery in a filter... |
| `HoistValuesSubquery` | CockroachDB | ⏭️ SKIPPED | — | 101 | The rule's correctness hinges on the scalar-subquery cardinality invariant — that the subquery relation R has at most one row — which mak... |
| `InlineAnyProjectSet` | CockroachDB | ⏭️ SKIPPED | — | 63 | The rule's correctness rests on the bespoke relationship between `unnest` (array-element enumeration, NULL→empty) and `AnyScalar` (elemen... |
| `InlineAnyWithScanOfValues` | CockroachDB | ⏭️ SKIPPED | — | 32 | The rule's only semantic content is substituting a CTE reference (WithScan) for its defining VALUES expression — i.e., a let-binding of a... |
| `InlineExistsSelectTuple` | CockroachDB | ⏭️ SKIPPED | — | 26 | The rule's core semantic content is the definitional expansion of one tuple-equality predicate into a conjunction of per-component equali... |
| `InlineUDF` | CockroachDB | ⏭️ SKIPPED | — | 23 | InlineUDF's sole correctness content is the definitional identity "an inlinable UDF call evaluates to its single-statement body as a scal... |
| `InlineWith` | CockroachDB | ⏭️ SKIPPED | — | 50 | QED's relational fragment contains no let/With/alias operator — JSONSerializer only emits scan/values/filter/project/join/correlate/group... |
| `NormCycleTestRelFalseToTrue` | CockroachDB | ⏭️ SKIPPED | — | 21 | This is a test-only, cycle-inducing rewrite for `NormCycleTestRel`, an operator with no defined evaluation semantics, so its only semanti... |
| `NormCycleTestRelTrueToFalse` | CockroachDB | ⏭️ SKIPPED | — | 24 | The rewrite `NormCycleTestRel(True) => NormCycleTestRel(False)` has no defined evaluation semantics — it exists solely to exercise the op... |
| `NormalizeArrayFlattenToAgg` | CockroachDB | ⏭️ SKIPPED | — | 31 | The rule's soundness is precisely the identity `ArrayFlatten(S) ≡ COALESCE(scalar-subquery(array_agg over S), [])`, which requires list/o... |
| `NormalizeCmpConstMinus` | CockroachDB | ⏭️ SKIPPED | — | 43 | The rule's soundness rests on the interpreted arithmetic/order identity c1 − a OP c2 ⟺ (c1 − c2) OP a (plus the backend's overflow/fold s... |
| `NormalizeCmpMinusConst` | CockroachDB | ⏭️ SKIPPED | — | 22 | The rewrite's validity rests on the value-level algebraic identity (x − c1) OP c2 ⟺ x OP (c2 + c1), but in QED the Minus, Plus, and compa... |
| `NormalizeCmpTimeZoneFunction` | CockroachDB | ⏭️ SKIPPED | — | 45 | The rule is a purely scalar rewrite whose validity depends on the two timezone overloads being mutual (order-preserving) inverses, but QE... |
| `NormalizeCmpTimeZoneFunctionTZ` | CockroachDB | ⏭️ SKIPPED | — | 62 | The rule's soundness rests entirely on an axiom QED does not and cannot have — that the two `timezone` overloads are mutual inverses (and... |
| `NormalizeJoinAnyFilter` | CockroachDB | ⏭️ SKIPPED | — | 64 | Confirmed against the DSL source: RexRN has no scalar-subquery or quantifier node (only Field/JoinField, uninterpreted Pred/Proj, And/Or/... |
| `NormalizeLikeAny` | CockroachDB | ⏭️ SKIPPED | — | 24 | The rule's entire semantic content is the equivalence `x LIKE '%' ≡ x IS NOT NULL`, which rests on string/glob theory (that the pattern '... |
| `NormalizeTupleEquality` | CockroachDB | ⏭️ SKIPPED | — | 44 | The rule's soundness rests on tuple extensionality — pack(a,b,c)=pack(x,y,z) ⟺ a=x ∧ b=y ∧ c=z — but QED's theory is equality over uninte... |
| `PruneLimitCols` | CockroachDB | ⏭️ SKIPPED | — | 41 | PruneLimitCols's correctness rests entirely on Sort/Limit ordering semantics — specifically that, with all ordering columns retained, the... |
| `PruneOffsetCols` | CockroachDB | ⏭️ SKIPPED | — | 23 | PruneOffsetCols' soundness rests entirely on order-dependent row selection — that pruning columns not referenced by the ordering or outer... |
| `PushAggFilterIntoScalarGroupBy` | CockroachDB | ⏭️ SKIPPED | — | 48 | The rule's only transformation is hoisting a per-call aggregate FILTER (SELECT agg(x) FILTER(WHERE p) FROM t) into a Filter on the Scalar... |
| `PushColumnRemappingIntoValues` | CockroachDB | ⏭️ SKIPPED | — | 25 | The rule's only effect is renaming a Values column and moving a plain column-reference projection item into the passthrough set — a pure ... |
| `PushLimitIntoJoinLeft` | CockroachDB | ⏭️ SKIPPED | — | 29 | The rule's soundness rests entirely on ordered top-N semantics — that the outer Limit's selected rows must come from within the first-n l... |
| `PushLimitIntoJoinRight` | CockroachDB | ⏭️ SKIPPED | — | 22 | `PushLimitIntoJoinRight`'s correctness rests entirely on top-n/ordering semantics — distributing `Limit` through a join that preserves th... |
| `PushLimitIntoLock` | CockroachDB | ⏭️ SKIPPED | — | 22 | The rewrite Limit(Lock(X)) ⟹ Lock(Limit(X)) is only sound because (a) a non-SKIP-LOCKED Lock is row-identity over its input — a backend-s... |
| `PushLimitIntoOffset` | CockroachDB | ⏭️ SKIPPED | — | 33 | The rule's correctness rests entirely on order- and cardinality-based semantics: which rows `Limit` keeps depends on which rows come firs... |
| `PushLimitIntoOrdinality` | CockroachDB | ⏭️ SKIPPED | — | 25 | The rule's correctness rests entirely on ordering/prefix-selection semantics — which rows the Limit keeps, the ordinals Ordinality assign... |
| `PushLimitIntoProject` | CockroachDB | ⏭️ SKIPPED | — | 6 | The rule's soundness rests entirely on Limit's ordered-prefix semantics plus its side condition `OrderingCanProjectCols`: the top-N rows ... |
| `PushLimitIntoWindow` | CockroachDB | ⏭️ SKIPPED | — | 25 | PushLimitIntoWindow's soundness rests entirely on two things QED's model categorically lacks: the row-ordering semantics of Limit/Sort (t... |
| `PushOffsetIntoLock` | CockroachDB | ⏭️ SKIPPED | — | 26 | Offset's meaning — dropping the first k rows of a given ordering — is exactly the list/ordering semantics QED's bag-semantic decision pro... |
| `PushOffsetIntoProject` | CockroachDB | ⏭️ SKIPPED | — | 8 | The rule's soundness rests entirely on row order — Offset means "skip the first k rows in $ordering", and the OrderingCanProjectCols/Prun... |
| `ReduceGroupingCols` | CockroachDB | ⏭️ SKIPPED | — | 28 | ReduceGroupingCols is sound only because of a specific algebraic identity — ConstAgg/ANY_VALUE(r) over a group returns the group's (const... |
| `RejectNullsGroupBy` | CockroachDB | ⏭️ SKIPPED | — | 0 | The rewrite adds a col IS NOT NULL filter below a GroupBy/ScalarGroupBy because the aggregate ignores NULL inputs and the filter above re... |
| `SimplifyAnyScalarArray` | CockroachDB | ⏭️ SKIPPED | — | 16 | The rule is a scalar representation normalization whose soundness rests on a backend-specific collection semantic — that a constant array... |
| `SimplifyCaseSingleTruthyBranch` | CockroachDB | ⏭️ SKIPPED | — | 45 | The rule's soundness rests entirely on the CASE operator's branch-selection/3-valued semantics (`CASE WHEN c THEN true ELSE false/NULL EN... |
| `SimplifyCaseWhenConstValue` | CockroachDB | ⏭️ SKIPPED | — | 22 | The rule's soundness rests entirely on CASE's internal evaluation semantics (ordered first-true-branch selection with SQL NULL-skip), whi... |
| `SimplifyCoalesce` | CockroachDB | ⏭️ SKIPPED | — | 41 | QED encodes COALESCE as an uninterpreted scalar function with no first-non-null selection axiom, so the rule's essential step COALESCE(NU... |
| `SimplifyExplainOrdering` | CockroachDB | ⏭️ SKIPPED | — | 25 | SimplifyExplainOrdering keeps `$input` identical on both sides and only rewrites the ordering directive stored in the Explain operator's ... |
| `SimplifyGroupByOrdering` | CockroachDB | ⏭️ SKIPPED | — | 9 | QED is a bag-semantics decision procedure with no model of ordering/order-by — Sort/Limit/Offset/Order-By carry no bag meaning (qed.pdf §... |
| `SimplifyLimitOrdering` | CockroachDB | ⏭️ SKIPPED | — | 10 | QED's theory is bag-only with uninterpreted operators and has no list/ordering semantics, so a Limit's ordering directive has no represen... |
| `SimplifyNotDisjoint` | CockroachDB | ⏭️ SKIPPED | — | 26 | The rule is purely scalar, and its entire validity rests on Cockroach's library identity st_intersects(args) ≡ ¬st_disjoint(args); in QED... |
| `SimplifyOffsetOrdering` | CockroachDB | ⏭️ SKIPPED | — | 7 | This rule rewrites the ordering directive of an Offset node, and Offset's very meaning — which rows survive after skipping the first k — ... |
| `SimplifyOrdinalityOrdering` | CockroachDB | ⏭️ SKIPPED | — | 25 | The rewrite's entire effect is changing the physical ordering requirement attached to the Ordinality operator — the input relation and it... |
| `SimplifyPartialIndexProjections` | CockroachDB | ⏭️ SKIPPED | — | 84 | The rewrite is conditional on a static column-set side condition (the UPDATE writes none of the partial index's or predicate's columns), ... |
| `SimplifyWindowOrdering` | CockroachDB | ⏭️ SKIPPED | — | 24 | The rule's sole change is the window's ordering/private data, and its entire justification is that the simplified ordering induces the sa... |
| `SimplifyWithBindingOrdering` | CockroachDB | ⏭️ SKIPPED | — | 10 | The rule's only effect is on `WithPrivate.BindingOrdering` — an ordering *requirement* (a physical property) on the CTE binding input — w... |
| `TryAddLimitToRecursiveBranch` | CockroachDB | ⏭️ SKIPPED | — | 10 | The rule's before-side `RecursiveCTE` is a least fixed point over a working table — no finite bag-semiring expression and no core-languag... |
| `TryDecorrelateLimit` | CockroachDB | ⏭️ SKIPPED | — | 25 | TryDecorrelateLimit's entire correctness claim is the order-based equivalence "first n rows of the right input per the given ordering (LI... |
| `TryDecorrelateLimitOne` | CockroachDB | ⏭️ SKIPPED | — | 45 | TryDecorrelateLimitOne's correctness is precisely the identity "top-1 row of each left row's matches under collation O" (Limit 1 + orderi... |
| `TryDecorrelateMax1Row` | CockroachDB | ⏭️ SKIPPED | — | 22 | The rule's only non-vacuous content is the coincidence of error behavior — Max1Row aborts when a correlated subquery instance holds >1 ro... |
| `TryDecorrelateScalarGroupBy` | CockroachDB | ⏭️ SKIPPED | — | 0 | The rule pushes a join into a ScalarGroupBy by turning it into a GroupBy over a left join keyed on a strict key of the left input. It dep... |
| `TryDecorrelateUnion` | CockroachDB | ⏭️ SKIPPED | — | 55 | The rule's correctness hinges on the identity ANY_NOT_NULL(A ⊎ B) = COALESCE(ANY_NOT_NULL(A), ANY_NOT_NULL(B)), which requires (a) an axi... |
| `UnifyComparisonTypes` | CockroachDB | ⏭️ SKIPPED | — | 33 | The rule's soundness is exactly that casting the constant to the variable's type is lossless — i.e. op(x, c) ⟺ op(x, cast(c)) — but QED e... |
| `UseSwapMutation` | CockroachDB | ⏭️ SKIPPED | — | 23 | The rewrite is not a bag equivalence under any encoding: even in the best case (a real PK-equality filter, a unique-key scan, and the sam... |

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

### `CommuteNullIs` — ✅ PROVED

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
- Attempts used: 22
- Last updated: 2026-10-14T17:09:35.011401+00:00
- Reason / notes: The encoding correctly models the `Is` branch of CockroachDB's CommuteNullIs as a scalar commutation of `IS_NOT_DISTINCT_FROM` with a NULL literal (before: `NULL IS_NOT_DISTINCT_FROM x`, after: `x IS_NOT_DISTINCT_FROM NULL`), using an uninterpreted type and a single arbitrary column so the proof is universally quantified over all types and values; before() and after() are structurally distinct (swapped operand order) so the proof is non-vacuous; the `^(Null)` side-condition from the source is correctly identified as a no-op-prevention guard rather than a semantic precondition, so its omission only widens (not narrows) the proven claim; and the PARTIAL scope tag honestly and specifically discloses that the structurally-identical `IsNot` branch (`IS_DISTINCT_FROM`) is not encoded due to the single before/after pair constraint of `RRule`. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 72450083}, panicked=False

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

### `DecorrelateProjectSet` — ✅ PROVED

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
- Attempts used: 6
- Last updated: 2026-10-14T23:19:30.014709+00:00
- Reason / notes: It is an honest, non-degenerate partial encoding (unary input and emitted column) of the source rule's core independence premise: because the zip is uncorrelated with the input, the emitted-row predicate M depends only on E, so
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 68738041}, panicked=False

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
- Attempts used: 26
- Last updated: 2026-10-15T00:35:08.516396+00:00
- Reason / notes: before() and after() differ genuinely (distinct=true vs false on the same aggregate, a flag the JSONSerializer does pass to QED), and the proof is non-vacuous: the scan's key on the aggregate's argument forces the argument values to be unique, so dedup is the identity and the uninterpreted aggregate's input bags are equal. The encoding is a sound, honestly-tagged PARTIAL special case — ScalarGroupBy (where encoding AggFilter as a pre-aggregate Filter is strictly equivalent, as the SCOPE line notes) with the argument as the unique key, which satisfies the original rule's HasStrictKey + CanRemoveAggDistinctForKeys preconditions — with no hard-coded predicates, wrong operator shapes, or missing premises. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 71677500}, panicked=False

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
- Attempts used: 5
- Last updated: 2026-10-15T02:07:45.086020+00:00
- Reason / notes: The encoding faithfully captures the rule's core semantic — a Project is a no-op under EXISTS because it preserves non-emptiness — by modeling an uncorrelated EXISTS as a left-semi-join of a fresh outer relation against the subquery on a constant-true condition; the semi-join-with-true keeps each outer row iff the subquery is non-empty, and because `outer` is a free uninterpreted symbol, the equivalence is forced to reduce exactly to nonempty(Project(input)) ⟺ nonempty(input), which is precisely why the real rule is valid. The `before()`/`after()` genuinely differ by the Project node (non-vacuous), the operators and symbol sharing are correct (SEMI for existence semantics, distinct outer/input/projection symbols, no baked-in predicates), and the SCOPE: PARTIAL tag is honest and specific — the only narrowing is the uncorrelated/single-projection case, which is a genuine and non-degenerate special case (the correlated case needs a non-column-free join condition whose column references would shift when the Project is removed, making it hard to express in this DSL).
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 74269833}, panicked=False

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
- Attempts used: 9
- Last updated: 2026-10-15T02:16:20.421879+00:00
- Reason / notes: The encoding faithfully models the uncorrelated case of the rule: before() is the standard relational desugaring of `WHERE EXISTS(empty subquery)` (outer left-semi-join on a constant-true condition against a zero-row relation), and after() is the desugared form of the rewritten query (outer filtered by constant False); the two are structurally different plan shapes whose bag-semantic equality is exactly the rule's content, not a vacuous identity. SEMI join kind, true join condition, `Empty` for HasZeroRows, and `falseLiteral` for the constant False are all the correct operators, the only symbols are the independent Outer and the type-carrying Input (no spurious sharing), and no hidden preconditions are assumed. The restriction to an uncorrelated EXISTS appearing as the whole filter is a genuine consequence of RuleScript lacking a scalar-subquery/Exists symbol (so the rule can only be stated at the relational level), it is explicitly and specifically declared in the SCOPE line, and the result is a real, non-degenerate special case rather than a structural no-op. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 25094166}, panicked=False

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

### `EliminateUnaryMinus` — ✅ PROVED

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
- Attempts used: 21
- Last updated: 2026-10-15T03:07:54.079290+00:00
- Reason / notes: The encoding faithfully mirrors the source rule's shape: before() is Project(UNARY_MINUS(UNARY_MINUS(col)), Scan) vs after() = Scan — structurally different (so the proof is not vacuous), with the input universally quantified over arbitrary rows of a nullable, uninterpreted-type table, exactly matching the guard-free, universally-quantified $input of the original. Reusing the concrete SqlStdOperatorTable.UNARY_MINUS in both positions is essential and correct, not under-generalization: --x = x holds only for the *same* real negation operator (an uninterpreted symbol would make the claim false and unprovable), so QED's successful proof necessarily reflects its genuine arithmetic semantics of negation, which coincides with CockroachDB's UnaryMinus. No preconditions are missing (the source rule has no guards, and double negation is an involution even for NULL), and the input's
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 285917}, panicked=False

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

### `EliminateWindow` — ✅ PROVED

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
- Attempts used: 6
- Last updated: 2026-10-15T03:06:55.824798+00:00
- Reason / notes: QED fundamentally cannot represent a Window operator (no Window builder, no window case in JSONSerializer, and the unmodifiable prover has no window semantics), so full generality is genuinely out of reach and the porter's modeling of a zero-function partitioned window as an INNER equi-join of the input back to its distinct partition keys (correctly sharing the input symbol, with verified ordinals, condition, and projection, and a declared non-null key) is the only faithful available encoding. The `before()` (Join+Aggregate+Project) is structurally different from `after()` (plain scan), so the proof is non-vacuous, and the fixed two-column/non-null-key restrictions are a direct consequence of that fundamental limitation and are honestly tagged `SCOPE: PARTIAL` with the specific condition stated.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 69995375}, panicked=False

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

### `FoldFunctionWithNullArg` — ✅ PROVED

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
- Attempts used: 41
- Last updated: 2026-10-15T04:57:36.393966+00:00
- Reason / notes: before() and after() are structurally different (projecting EQUALS(x, NULL) vs a bare BOOLEAN null), and the proof is non-vacuous: it succeeds only because QED's model gives `=` genuine three-valued null-propagation semantics, and the porter's re-verified provable=false for an uninterpreted f(x, NULL) rules out a vacuous or over-constrained shortcut. The encoding is a faithful, specific special case of FoldFunctionWithNullArg — EQUALS is a normal (non-aggregate/window/generator) function that is not invoked on null input, the null argument satisfies HasNullArg, and full generality is genuinely out of reach since the DSL cannot express function-metadata side conditions and QED has no null-propagation contract for uninterpreted function symbols. The SCOPE: PARTIAL line accurately and specifically discloses exactly this restriction. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 70042}, panicked=False

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

### `FoldInEmpty` — ✅ PROVED

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
- Attempts used: 8
- Last updated: 2026-10-15T04:35:37.247822+00:00
- Reason / notes: The encoding is non-vacuous: a SEMI join against a zero-row relation is empty, which is the relational analogue of IN over an empty tuple being false. The left relation is uninterpreted and the SEMI join kind matches IN specifically, so it is not a vacuous structural identity or an accidental over-constraint. The PARTIAL scope honestly states that scalar IN and empty tuple constants are approximated by a relational semi-join, making this a genuine, non-degenerate special case rather than the full expression-level rule.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 25550417}, panicked=False

### `FoldInNull` — ✅ PROVED

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
- Attempts used: 21
- Last updated: 2026-10-15T04:51:06.783862+00:00
- Reason / notes: The encoding is the filter-context relational instance of the In alternative, using the canonical desugaring `x IN (NULL)` ≡ `x = NULL` (the exact form SimplifyInSingleElement in the same file produces), with correct types (nullV typed as the column type V, and the RHS a BOOLEAN null matching `(Null (BoolType))`), no preconditions or symbol-sharing errors, and structurally distinct before/after plans whose equivalence (equality with NULL is never true, so both filters yield the empty bag for every instantiation) is a genuine, non-vacuous proof. The omission of the NotIn alternative is a real DSL limitation (one before/after pair cannot express the In | NotIn disjunction), is honestly and specifically tagged in the SCOPE line, and the resulting rule remains a non-degenerate special case of the original.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 278584}, panicked=False

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

### `FoldNullAndOr` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

FoldNullAndOr replaces the operator with null if both operands are null.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `FoldNullAndOr`, not the other rules in that file):

```
# FoldNullAndOr replaces the operator with null if both operands are null.
[FoldNullAndOr, Normalize]
(And | Or (Null) (Null))
=>
(Null (BoolType))
```
- Attempts used: 41
- Last updated: 2026-09-26T19:59:46.762575+00:00
- Reason / notes: The encoding faithfully captures the And branch of FoldNullAndOr: the hard-coded null literal is correct because the source rule matches the Null *constant* (not an uninterpreted operand), reusing one symbol for both operands is right since the rule requires both to be the same constant, and before()/after() are structurally and semantically distinct (an AND call vs. a bare literal), so the proof is non-vacuous; filter-level embedding of a scalar boolean identity follows the DSL's established convention (cf. FilterMerge), and the source rule carries no side conditions (unlike NegateComparison) that the encoding omits. The PARTIAL scope tag is honest and specific — the only narrowing is the absent Or branch (which is expressible as a separate instance via `new RexRN.Or(Seq.of(nullLit, nullLit))`), and the covered instance remains a genuine, useful, non-degenerate rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 316667}, panicked=False

### `FoldNullBinaryLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullBinaryLeft replaces the binary operator with null if its left input
is null and it does not allow null arguments.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullBinaryLeft`, not the other rules in that file):

```
# FoldNullBinaryLeft replaces the binary operator with null if its left input
# is null and it does not allow null arguments.
[FoldNullBinaryLeft, Normalize]
(Binary
    $left:(Null)
    $right:* & ^(AllowNullArgs (OpName) $left $right)
)
=>
(FoldNullBinary (OpName) $left $right)
```
- Attempts used: 43
- Last updated: 2026-10-15T05:47:18.814394+00:00
- Reason / notes: `before()` (filter on `NULL < x`) and `after()` (filter on the null literal) are structurally different, and QED's proof establishes a genuine, non-vacuous fact — that a null-left comparison is never TRUE in QED's three-valued model, so replacing it with a null condition preserves the bag, which is exactly what the rule asserts in filter position for `<`. The narrowing to a concrete primitive operator and filter context is forced by QED's inability to model null propagation for uninterpreted operators (the `!AllowNullArgs` condition cannot be axiomatized over them), and the `SCOPE: PARTIAL` line honestly and specifically names the assumed condition and its cause, so the result is a legitimate non-degenerate special case rather than a disguised triviality.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 78083}, panicked=False

### `FoldNullBinaryRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullBinaryRight replaces the binary operator with null if its right input
is null and it does not allow null arguments.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullBinaryRight`, not the other rules in that file):

```
# FoldNullBinaryRight replaces the binary operator with null if its right input
# is null and it does not allow null arguments.
[FoldNullBinaryRight, Normalize]
(Binary
    $left:*
    $right:(Null) & ^(AllowNullArgs (OpName) $left $right)
)
=>
(FoldNullBinary (OpName) $left $right)
```
- Attempts used: 22
- Last updated: 2026-10-15T05:39:23.331616+00:00
- Reason / notes: The encoding is a non-vacuous, faithful instance of the rule: `before()` is `Filter(R, x < NULL)` and `after()` is `Filter(R, NULL-literal)`, which are structurally different and whose bag-equivalence depends precisely on the null-propagation property (`<` does not allow null args, so `x < NULL` is never true) that `FoldNullBinaryRight` relies on, with an arbitrary left value `x` standing for `$left:*` and a genuine null literal for `$right:(Null)`, in filter position — a legitimate embedding of the expression-level rewrite with the result type forced to boolean. Hard-coding `<` rather than an uninterpreted binary operator is a genuine QED limitation (uninterpreted symbols carry no null semantics, and the prover cannot be modified to grant them), not a missing DSL capability, and the `SCOPE: PARTIAL` line honestly and specifically names exactly this restriction, leaving the result a real, non-degenerate rewrite.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 335042}, panicked=False

### `FoldNullComparisonLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

FoldNullComparisonLeft replaces the comparison operator with null if its
left input is null.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNullComparisonLeft`, not the other rules in that file):

```
# FoldNullComparisonLeft replaces the comparison operator with null if its
# left input is null.
[FoldNullComparisonLeft, Normalize]
(Eq | Ne | Ge | Gt | Le | Lt | Like | NotLike | ILike | NotILike
        | SimilarTo | NotSimilarTo | RegMatch | NotRegMatch
        | RegIMatch | NotRegIMatch | Contains | ContainedBy
        | Overlaps | JsonExists | JsonSomeExists | JsonAllExists
    $left:(Null)
    *
)
=>
(Null (BoolType))
```
- Attempts used: 26
- Last updated: 2026-09-26T20:38:56.488308+00:00
- Reason / notes: before() (`Filter(S, NULL = x)`) and after() (`Filter(S, NULL::bool)`) are structurally different over an arbitrary non-unique, nullable source, so the proof is not vacuous — it verifies the actual content of the rule's Eq branch, namely that a comparison with a null left operand never evaluates true, so both filters admit no rows (had `NULL = x` been able to be true, null-`x` rows would survive on the left side and the proof would fail). Baking in the concrete `SqlStdOperatorTable.EQUALS` is the right call rather than an under-generalization: an uninterpreted predicate symbol carries no null-propagation semantics for QED to reason about, so each of the source rule's 22 operator branches (the SCOPE comment's "18/17" is a miscount, but the stated Eq-only restriction is accurate and specific) requires its own instance with a concrete operator; symbol sharing is correct (same source, same right input `x`, left operand a null literal rather than a shared symbol, matching `$left:(Null) *`), no spurious preconditions are introduced, and the result is a genuine, non-degenerate, honestly-flagged PARTIAL special case in the rule's canonical boolean (filter) position.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 380417}, panicked=False

### `FoldNullComparisonRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

FoldNullComparisonRight replaces the comparison operator with null if its
right input is null.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNullComparisonRight`, not the other rules in that file):

```
# FoldNullComparisonRight replaces the comparison operator with null if its
# right input is null.
[FoldNullComparisonRight, Normalize]
(Eq | Ne | Ge | Gt | Le | Lt | Like | NotLike | ILike | NotILike
        | SimilarTo | NotSimilarTo | RegMatch | NotRegMatch
        | RegIMatch | NotRegIMatch | Contains | ContainedBy
        | Overlaps | JsonExists | JsonSomeExists | JsonAllExists
    *
    $right:(Null)
)
=>
(Null (BoolType))
```
- Attempts used: 32
- Last updated: 2026-09-26T20:46:31.451699+00:00
- Reason / notes: `before()` (filter on `EQUALS(left, NULL)`) and `after()` (filter on the bare `NULL` literal) are structurally and semantically distinct, and the proof checks exactly the rule's null-folding content — `x = NULL ≡ NULL` for every instantiation of the uninterpreted left input, including NULL itself — with the NULL constant correctly shared on the right operand and no preconditions in the source rule left out. The restriction to the Eq branch (one of ~23 operators) is genuine: QED cannot derive null propagation for an uninterpreted operator symbol, so each concrete operator would need its own instance (and CockroachDB-specific ones like RegMatch/JsonExists aren't in the prover's model), and it is honestly and specifically declared in the SCOPE line. Embedding the scalar fold in a filter position is the DSL's idiomatic expression of a scalar predicate rule (as in the FilterMerge example), and the encoding is a faithful, non-degenerate special case.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 284958}, panicked=False

### `FoldNullInNonEmpty` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullInNonEmpty replaces the In/NotIn with null when the left input is
null and the right input is not empty. Null is the unknown value, and if the
set is non-empty, it is unknown whether it's in/not in the set.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullInNonEmpty`, not the other rules in that file):

```
# FoldNullInNonEmpty replaces the In/NotIn with null when the left input is
# null and the right input is not empty. Null is the unknown value, and if the
# set is non-empty, it is unknown whether it's in/not in the set.
[FoldNullInNonEmpty, Normalize]
(In | NotIn (Null) (Tuple ^[]))
=>
(Null (BoolType))
```
- Attempts used: 22
- Last updated: 2026-09-26T21:02:29.425968+00:00
- Reason / notes: The encoding correctly models `NULL IN (a,b)` as `filter(Or(=(null,a), =(null,b)))` and the folded result as `filter(nullBool)`; both sides reduce to the empty relation under SQL three-valued logic (any comparison with NULL yields NULL, NULL OR NULL is NULL, and a NULL filter condition excludes the row), so the proof is non-vacuous and structurally distinct. The PARTIAL scope is honestly stated — fixed 2-element arity and the In-branch only — but the instance is a genuine, non-degenerate special case of the original rule that exercises the same three-valued-logic reasoning.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 284500}, panicked=False

### `FoldNullTupleIsTupleNotNull` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

FoldNullTupleIsTupleNotNull replaces x IS NOT NULL with False if x is a tuple
with at least one constant, null element.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNullTupleIsTupleNotNull`, not the other rules in that file):

```
# FoldNullTupleIsTupleNotNull replaces x IS NOT NULL with False if x is a tuple
# with at least one constant, null element.
[FoldNullTupleIsTupleNotNull, Normalize]
(IsTupleNotNull $input:(Tuple) & (HasNullElement $input))
=>
(False)
```
- Attempts used: 23
- Last updated: 2026-09-26T22:09:39.311518+00:00
- Reason / notes: The encoding is non-vacuous and on-point: before() carries the real condition AND(IS NOT NULL(NULL), IS NOT NULL(x)) while after() carries the constant FALSE, so the proof establishes exactly the scalar fold the rule states, and the element-conjunction semantics used for IsTupleNotNull is the one forced by all four tuple-folding rules in comp.opt (a constant NULL element makes one conjunct — hence the conjunction — constant FALSE). The arity-2, one-constant-NULL restriction is a genuine, specifically-stated PARTIAL special case rather than a modeling accident: the remaining element is a completely arbitrary nullable column of an uninterpreted table (the maximal generality the DSL can give a free value), and the rule's soundness at any arity rests solely on that single constant-false conjunct, so no additional generality is being hidden; no preconditions are missing (HasNullElement is baked in as the NULL literal) and no symbol is improperly shared or over-constrained. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 306292}, panicked=False

### `FoldNullTupleIsTupleNull` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

FoldNullTupleIsTupleNull replaces x IS NULL with True if x is a tuple with
only constant, null elements.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNullTupleIsTupleNull`, not the other rules in that file):

```
# FoldNullTupleIsTupleNull replaces x IS NULL with True if x is a tuple with
# only constant, null elements.
[FoldNullTupleIsTupleNull, Normalize]
(IsTupleNull $input:(Tuple) & (HasAllNullElements $input))
=>
(True)
```
- Attempts used: 7
- Last updated: 2026-09-26T21:09:59.149360+00:00
- Reason / notes: The encoding faithfully captures the rule's semantic content: IsTupleNull on a constant all-null tuple is expanded to the conjunction of element-wise `IS NULL` predicates (the operator's defining semantics), and since the source side condition HasAllNullElements requires every element to be exactly the NULL constant, reusing a single NULL literal for both elements is correct rather than a symbol-sharing error (independent uninterpreted elements would be a different, unprovable claim). The proof is not vacuous — before() filters on the closed conjunction IS_NULL(NULL) AND IS_NULL(NULL) while after() filters on TRUE, and the provable content is precisely the constant fold the rule performs. The restriction to tuple arity 2 is honestly tagged (SCOPE: PARTIAL) and genuine, since RuleScript has no tuple value constructor or IsTupleNull operator and the QED prover has no tuple-value semantics to model a general-arity version against, so this is a legitimate non-degenerate special case rather than an avoidable gap. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 287209}, panicked=False

### `FoldNullUnary` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullUnary discards any unary operator with a null input, and replaces it
with a null value having the same type as the unary expression would have.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullUnary`, not the other rules in that file):

```
# FoldNullUnary discards any unary operator with a null input, and replaces it
# with a null value having the same type as the unary expression would have.
[FoldNullUnary, Normalize]
(Unary $input:(Null))
=>
(FoldNullUnary (OpName) $input)
```
- Attempts used: 48
- Last updated: 2026-09-26T21:30:22.542863+00:00
- Reason / notes: The encoding faithfully captures the rule's structure for the unary-minus instance: `Project(UNARY_MINUS(NULL))` vs `Project(NULL)` over the same scan, with a null *literal* input (correctly matching `$input:(Null)`, not a column reference), and no dropped preconditions since the source's unary rule (unlike the binary ones) carries no guard. The narrowing to a concrete operator is a genuine QED limitation, not a fixable DSL gap — an uninterpreted unary symbol would require proving `op(NULL) = NULL` for an uninterpreted function, which QED cannot derive — and the PARTIAL scope line states exactly this restriction, so the fast SMT result proves a real, sound, non-degenerate special case (-NULL = NULL), with before()/after() structurally different.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 108959}, panicked=False

### `FoldUnary` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldUnary is similar to FoldBinary, but it involves a unary operation over a
single constant input. As with FoldBinary, FoldUnary applies as long as the
evaluation would not cause an error.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldUnary`, not the other rules in that file):

```
# FoldUnary is similar to FoldBinary, but it involves a unary operation over a
# single constant input. As with FoldBinary, FoldUnary applies as long as the
# evaluation would not cause an error.
[FoldUnary, Normalize]
(Unary
    $input:* &
        (IsConstValueOrGroupOfConstValues $input) &
        (Let ($result $ok):(FoldUnary (OpName) $input) $ok)
)
=>
$result
```
- Attempts used: 5
- Last updated: 2026-09-26T21:44:58.096385+00:00
- Reason / notes: `before()` and `after()` are structurally distinct (`Not(trueLiteral())` vs `falseLiteral()`) and the equivalence proved is exactly the rule's content — a unary operator applied to a constant replaced by its folded constant — so the proof is not vacuous. The hard-coding of NOT/TRUE/FALSE is forced rather than lazy: QED treats arbitrary operators as uninterpreted and cannot know that `op(const)` equals any new constant, so this concrete boolean instance is the only provable shape (a DSL extension couldn't close the gap, since the prover is the fixed arbiter of operator semantics). The single-sentence `// SCOPE: PARTIAL` line accurately and specifically discloses the restriction (NOT over TRUE, filter position), and no precondition (constant input, successful evaluation `$ok`) is silently dropped for this instance, making it an honest, non-degenerate special case of FoldUnary.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 289000}, panicked=False

### `HoistJoinProjectLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

HoistJoinProjectLeft is the same as HoistJoinProjectRight, but for the left
input of the join.

Extracted from `join.opt` (which defines multiple rules — implement specifically `HoistJoinProjectLeft`, not the other rules in that file):

```
# HoistJoinProjectLeft is the same as HoistJoinProjectRight, but for the left
# input of the join.
[HoistJoinProjectLeft, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply
    $left:(Project
        $input:*
        $projections:* & ^(HasVolatileProjection $projections)
        $passThrough:*
    )
    $right:* &

        # For apply-joins, the right input could reference the projected
        # columns, in which case pulling the Project up would be incorrect.
        # This isn't an issue for HoistJoinProjectRight because outer column
        # references cannot be from the left input to the right input.
        # TODO(drewk): we could remap the right input as well.
        ^(IsCorrelated $right (ProjectionCols $projections))
    $on:*
    $private:* &

        # We can only hoist the projection if each new column is either:
        # 1. a simple remapping that can be reversed in the join condition, OR
        # 2. not referenced in the join condition AND
        # 3. the projection does not reference input columns
        #
        # The last condition, (3), is a very conservative heuristic to avoid
        # hoisting projections that could prevent column pruning. We might be
        # able to remove it or make it smarter.
        #
        # TODO(michae2): we could work around (2) by inlining the projection
        # expression into the join condition, similar to
        # PushSelectIntoInlinableProject.
        (Let
            ($remap $other $ok):(CanHoistNonRemappingProjections
                $projections
            )
            $ok
        ) &
        ^(ColsIntersect
            (FilterOuterCols $on)
            (ProjectionCols $other)
        ) &
        ^(ColsIntersect
            (ProjectionOuterCols $other)
            (OutputCols $input)
        )
)
=>
(Project
    ((OpName)
        $input
        $right
        (UnbindFiltersFromProjections $remap $on)
        $private
    )
    $projections
    (UnionCols $passThrough (OutputCols $right))
)
```
- Attempts used: 22
- Last updated: 2026-09-26T22:20:08.894673+00:00
- Reason / notes: The proved identity is non-vacuous and is exactly the rule's core transformation: `before()` = Join_C(π(L), R) and `after()` = π(Join_{C∘π}(L, R)) are structurally different plans (project below vs. above the join) over uninterpreted tables, uninterpreted types, and one shared uninterpreted condition, and the unbinding bookkeeping is consistent — the condition evaluates C on (L1, L0, R) in both encodings (join-row ordinals (0,1,2) before, (1,0,2) after) and the outer project restores the (L1, L0, R) output, so the bags match for every instantiation. Every side condition of the source rule (¬HasVolatileProjection, ¬IsCorrelated on the right input, the ColsIntersect checks on non-remap projections) is vacuously satisfied by a pure column swap on an INNER join, so no precondition is silently missing, and the `SCOPE: PARTIAL` line honestly and specifically names the genuine restrictions (INNER only vs. the rule's InnerJoin/InnerJoinApply/LeftJoin/LeftJoinApply set; a fixed two-column remap swap vs. arbitrary hoistable projections; a single uninterpreted predicate) — a legitimate minimal instance, not a degenerate or misleading one.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 68453042}, panicked=False

### `HoistJoinProjectRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

HoistJoinProjectRight lifts a passthrough Project operator from within a Join
operator's right input to outside the join. This often allows the Project
operator to be merged with an outer Project. Since Project operators tend to
prevent other rules from matching, this and other rules try to either push
them down (to prune columns), or else to pull them up (to get them out of the
way of other operators).

It's not always beneficial to hoist projections above joins, but we need some
projection hoisting to happen to help join reordering, and doing it all in an
exploration rule risks creating a huge number of plans when combined with join
reordering.

Projections are allowed in the case when they are simple remaps from input to
output column IDs, in which case it is simple to replace the column references
in the join condition.

TODO(andyk): Add other join types.

Extracted from `join.opt` (which defines multiple rules — implement specifically `HoistJoinProjectRight`, not the other rules in that file):

```
# HoistJoinProjectRight lifts a passthrough Project operator from within a Join
# operator's right input to outside the join. This often allows the Project
# operator to be merged with an outer Project. Since Project operators tend to
# prevent other rules from matching, this and other rules try to either push
# them down (to prune columns), or else to pull them up (to get them out of the
# way of other operators).
#
# It's not always beneficial to hoist projections above joins, but we need some
# projection hoisting to happen to help join reordering, and doing it all in an
# exploration rule risks creating a huge number of plans when combined with join
# reordering.
#
# Projections are allowed in the case when they are simple remaps from input to
# output column IDs, in which case it is simple to replace the column references
# in the join condition.
#
# TODO(andyk): Add other join types.
[HoistJoinProjectRight, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply
    $left:*
    $right:(Project
        $input:*
        $projections:* &
            (AllAreRemappingProjections $projections) &

            # Ensure that there are no outer-column references in the
            # projections, since otherwise hoisting the Project could change
            # the result of a left-join due to the NULL-extended rows.
            # TODO(drewk): we could allow this for inner-joins.
            (ColsAreSubset
                (ProjectionOuterCols $projections)
                (OutputCols $input)
            )
        $passThrough:*
    )
    $on:*
    $private:*
)
=>
(Project
    ((OpName)
        $left
        $input
        (UnbindFiltersFromProjections $projections $on)
        $private
    )
    $projections
    (UnionCols (OutputCols $left) $passThrough)
)
```
- Attempts used: 23
- Last updated: 2026-09-26T22:33:33.338573+00:00
- Reason / notes: The encoding faithfully mirrors the source's HoistJoinProjectRight: the right input's passthrough project (a pure column remap here — the swap (r1,r0)) is hoisted above the join, the same uninterpreted on-predicate C is re-based onto the un-remapped input columns in the correct swapped order (condAfter feeds C the values (l0, r1, r0), matching condBefore), and the extra outer project above the after-join is exactly what the source's `(UnionCols ...)` output-column reconstruction requires under the DSL's positional (not column-ID) model. The SCOPE line is honest about the genuine narrowing (LEFT join only vs. the source's Inner/Left + Apply variants; fixed 1-column left and 2-column right inputs; the remap restricted to the two-column swap), the before/after plans are structurally distinct and non-vacuous, and no precondition from the source (remap-only projection, no outer-column references in the projection) is silently violated.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 95328375}, panicked=False

### `HoistJoinSubquery` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistJoinSubquery extracts subqueries from a join filter and joins them with
the join's right input. This and other subquery hoisting patterns create a
single, top-level relational query with no nesting. This rule only applies to
join types which have a legal apply variant.

This rule is marked as low priority for the same reason as HoistSelectExists.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistJoinSubquery`, not the other rules in that file):

```
# HoistJoinSubquery extracts subqueries from a join filter and joins them with
# the join's right input. This and other subquery hoisting patterns create a
# single, top-level relational query with no nesting. This rule only applies to
# join types which have a legal apply variant.
#
# This rule is marked as low priority for the same reason as HoistSelectExists.
[HoistJoinSubquery, Normalize, LowPriority]
(InnerJoin | LeftJoin | SemiJoin | AntiJoin
    $left:*
    $right:*
    $on:[ ... $item:* & (HasHoistableSubquery $item) ... ]
    $private:*
)
=>
(HoistJoinSubquery (OpName) $left $right $on $private)
```
- Attempts used: 65
- Last updated: 2026-09-26T23:08:11.467556+00:00
- Reason / notes: The encoding faithfully captures the core transformation of HoistJoinSubquery for the INNER-join case: moving an uncorrelated EXISTS subquery from the join's ON condition into a pre-filter on the right input, which is semantically equivalent under bag semantics. The before() and after() are structurally distinct (the EXISTS conjunct relocates from the join condition to a Filter node on R), the join kind (INNER) is one of the four the source rule targets and is the one where the transformation is unconditionally valid without additional preconditions, all symbols (L, R, S, on_cond, sub_pred) are correctly uninterpreted and independently shared, and the SCOPE tag honestly and specifically documents both the INNER-only restriction and the uncorrelated-EXISTS restriction. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 77232292}, panicked=False

### `HoistSelectAboveUnorderedDistinctOn` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistSelectAboveUnorderedDistinctOn hoists correlated filter conditions from
inside an unordered DistinctOn's input Select to above the DistinctOn. This
is the reverse of PushSelectIntoUnorderedDistinctOn and is valid because an
unordered DistinctOn can choose any row from each group, so filtering before
or after the grouping produces equivalent results.

This rule aids decorrelation by moving correlated filters to a position where
TryDecorrelateSelect can handle them, avoiding the more expensive
TryDecorrelateGroupBy transformation that requires EnsureKey and additional
ConstAgg columns.

Uncorrelated filters remain inside the DistinctOn for early filtering. If a
correlated filter references an input column that is not already in the
DistinctOn's output, a FirstAgg aggregation is added for that column so
it becomes available above the DistinctOn.

Example:
DistinctOn(Select(input, [s = outer.s, i > 0]), aggs, priv)
=>
Select(DistinctOn(Select(input, [i > 0]), aggs', priv), [s = outer.s])
(where aggs' = aggs + FirstAgg(s) if s was not already projected)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistSelectAboveUnorderedDistinctOn`, not the other rules in that file):

```
# HoistSelectAboveUnorderedDistinctOn hoists correlated filter conditions from
# inside an unordered DistinctOn's input Select to above the DistinctOn. This
# is the reverse of PushSelectIntoUnorderedDistinctOn and is valid because an
# unordered DistinctOn can choose any row from each group, so filtering before
# or after the grouping produces equivalent results.
#
# This rule aids decorrelation by moving correlated filters to a position where
# TryDecorrelateSelect can handle them, avoiding the more expensive
# TryDecorrelateGroupBy transformation that requires EnsureKey and additional
# ConstAgg columns.
#
# Uncorrelated filters remain inside the DistinctOn for early filtering. If a
# correlated filter references an input column that is not already in the
# DistinctOn's output, a FirstAgg aggregation is added for that column so
# it becomes available above the DistinctOn.
#
# Example:
#   DistinctOn(Select(input, [s = outer.s, i > 0]), aggs, priv)
#   =>
#   Select(DistinctOn(Select(input, [i > 0]), aggs', priv), [s = outer.s])
#   (where aggs' = aggs + FirstAgg(s) if s was not already projected)
[HoistSelectAboveUnorderedDistinctOn, Normalize]
(DistinctOn
    (Select $input:* $filters:*)
    $aggregations:*
    $groupingPrivate:* &
        (IsUnorderedGrouping $groupingPrivate) &
        (CanHoistCorrelatedFiltersAbove
            $filters
            (OutputCols $input)
        )
)
=>
(Select
    (DistinctOn
        (Select
            $input
            (ExtractBoundConditions $filters (OutputCols $input))
        )
        (AddFirstAggsForHoistedFilters
            $aggregations
            $filters
            (OutputCols $input)
            (GroupingOutputCols $groupingPrivate $aggregations)
        )
        $groupingPrivate
    )
    (ExtractUnboundConditions $filters (OutputCols $input))
)
```
- Attempts used: 22
- Last updated: 2026-09-27T00:26:40.598455+00:00
- Reason / notes: The encoding faithfully models the declared special case: an identity, unordered DistinctOn (output = grouping keys, no aggregate outputs) is correctly represented as a group-by over all input fields with no aggregate calls, and the single hoisted filter is one uninterpreted predicate over exactly those output columns, moved from below the grouping in `before()` to above it in `after()`. The two sides are structurally distinct (filter inside the aggregate vs. filter outside) and their equivalence is a genuine, non-vacuous fact — a group-constant predicate commutes with an unordered grouping — proven universally over all instantiations of the predicate, with correct symbol sharing and no vacuity, wrong operator, or silently dropped precondition. The PARTIAL scope tag is honest and specific (identity DistinctOn, no FirstAgg synthesis, group-key-only filter), and this is a real, non-degenerate narrowing of the source rule rather than a degenerate or over-constrained encoding.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 60043792}, panicked=False

### `HoistSelectExists` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistSelectExists extracts existential subqueries from Select filters,
turning them into semi-joins. This eliminates the subquery, which is often
expensive to execute and restricts the optimizer's plan choices.

This rule is marked as low priority so that it runs after other rules like
filter pushdown. Hoisting a correlated subquery is an expensive operation that
can't be undone, so do it only once all other work is complete. For example,
filter pushdown rules might be able to move the subquery nearer to the input
to which it's correlated before it's hoisted, making it easier to decorrelate.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistSelectExists`, not the other rules in that file):

```
# HoistSelectExists extracts existential subqueries from Select filters,
# turning them into semi-joins. This eliminates the subquery, which is often
# expensive to execute and restricts the optimizer's plan choices.
#
# This rule is marked as low priority so that it runs after other rules like
# filter pushdown. Hoisting a correlated subquery is an expensive operation that
# can't be undone, so do it only once all other work is complete. For example,
# filter pushdown rules might be able to move the subquery nearer to the input
# to which it's correlated before it's hoisted, making it easier to decorrelate.
[HoistSelectExists, Normalize, LowPriority]
(Select
    $input:*
    $filters:[
        ...
        $item:* &
            (HasHoistableSubquery $item) &
            (FiltersItem (Exists $subquery:*))
        ...
    ]
)
=>
(Select
    (SemiJoinApply $input $subquery [] (EmptyJoinPrivate))
    (RemoveFiltersItem $filters $item)
)
```
- Attempts used: 44
- Last updated: 2026-09-26T23:37:25.415167+00:00
- Reason / notes: The encoding faithfully captures the uncorrelated-special-case of HoistSelectExists: before() is Filter(And(EXISTS(Filter(S,sub_pred)), rest), L) and after() is Filter(rest, L SEMI-join Filter(S,sub_pred) ON true), which is structurally distinct (nested subquery vs. flat semi-join), uses correct uninterpreted symbols for input/subquery/remaining-filters, the SEMI+true correctly models SemiJoinApply with empty condition in the uncorrelated case, and the SCOPE:PARTIAL tag accurately names the one assumption (uncorrelated subquery) that the full rule does not require.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 62616292}, panicked=False

### `HoistSelectNotExists` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistSelectNotExists extracts non-existential subqueries from Select filters,
turning them into anti-joins. This eliminates the subquery, which is often
expensive to execute and restricts the optimizer's plan choices.

This rule is marked as low priority for the same reason as HoistSelectExists.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistSelectNotExists`, not the other rules in that file):

```
# HoistSelectNotExists extracts non-existential subqueries from Select filters,
# turning them into anti-joins. This eliminates the subquery, which is often
# expensive to execute and restricts the optimizer's plan choices.
#
# This rule is marked as low priority for the same reason as HoistSelectExists.
[HoistSelectNotExists, Normalize, LowPriority]
(Select
    $input:*
    $filters:[
        ...
        $item:* &
            (HasHoistableSubquery $item) &
            (FiltersItem (Not (Exists $subquery:*)))
        ...
    ]
)
=>
(Select
    (AntiJoinApply $input $subquery [] (EmptyJoinPrivate))
    (RemoveFiltersItem $filters $item)
)
```
- Attempts used: 25
- Last updated: 2026-09-26T23:49:20.124365+00:00
- Reason / notes: The encoding faithfully captures the uncorrelated NOT EXISTS → ANTI-join rewrite: before() is σ_{¬EXISTS(σ_{sub_pred}(S)) ∧ rest}(L) and after() is σ_{rest}(L ⋈_{ANTI,true} σ_{sub_pred}(S)), which are bag-equivalent because the uncorrelated EXISTS is a constant per row of L; the fixed subquery shape (Filter over a single-column Scan) and single `rest` predicate do not narrow the semantic content (QED treats them as arbitrary uninterpreted relations/conjunctions), and the SCOPE tag accurately identifies the one genuine restriction—uncorrelated subquery, hence constant-true anti-join condition—which is a real DSL limitation since RuleScript's RexRN has no mechanism to embed a correlated EXISTS subquery inside a filter predicate. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 74303917}, panicked=False

### `HoistUnboundFilterFromExistsSubquery` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt
- Attempts used: 0
- Last updated: 2026-10-15T18:30:54.125281+00:00
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 26372916}, panicked=False

### `HoistUnboundJoinFilterFromExistsSubquery` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistUnboundJoinFilterFromExistsSubquery is similar to
HoistUnboundFilterFromExistsSubquery, but it applies to a join filter.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistUnboundJoinFilterFromExistsSubquery`, not the other rules in that file):

```
# HoistUnboundJoinFilterFromExistsSubquery is similar to
# HoistUnboundFilterFromExistsSubquery, but it applies to a join filter.
[HoistUnboundJoinFilterFromExistsSubquery, Normalize]
(Select
    $input:* & (CanHoistUnboundFilterFromExistsSubquery)
    $filters:[
        ...
        $item:(FiltersItem
            (Exists
                $join:(InnerJoin | InnerJoinApply | SemiJoin
                        | SemiJoinApply
                    $left:*
                    $right:*
                    $joinFilters:[
                        ...
                        $innerItem:(FiltersItem $unboundCond:*) &
                            (IsBoundBy
                                $innerItem
                                $inputCols:(OutputCols $input)
                            )
                        ...
                    ]
                    $joinPrivate:*
                )
                $existsPrivate:*
            )
        )
        ...
    ]
)
=>
(Select
    $input
    (AppendFiltersItem
        (ReplaceFiltersItem
            $filters
            $item
            (Exists
                ((OpName $join)
                    $left
                    $right
                    (RemoveFiltersItem $joinFilters $innerItem)
                    $joinPrivate
                )
                $existsPrivate
            )
        )
        $unboundCond
    )
)
```
- Attempts used: 102
- Last updated: 2026-10-15T11:28:06.761313+00:00
- Reason / notes: The encoding is non-trivial and symbol-correct: the same uninterpreted predicate "P" is shared between the pre-hoist join condition (via joinField(0)) and the post-hoist outer filter, and the structural difference (P moved from the SEMI-join ON into a pre-filter on the outer input) is exactly the rule's transformation, which the proof establishes under QED's bag/3VL semantics (∃.P∧R ≡ P∧∃.R, valid across true/false/null cases). The specialization is real but genuine and honestly disclosed in the SCOPE line: RuleScript has no EXISTS/subquery operator (a DSL gap, not a QED one), so flattening EXISTS-over-join to a SEMI join with a single inner table and one conjunct per side is a semantically faithful reduction, and the SEMI join kind matches the rule's existential semantics (consistent with HoistSelectExists); the proof covers the rule's core logical identity in a specific, non-degenerate tree shape rather than all four join kinds and filter lists, which the PARTIAL tag accurately conveys. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 73868541}, panicked=False

### `InlineAnyValuesMultiCol` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

InlineAnyValuesMultiCol converts Any with Values input to AnyScalar.
This version handles the case where there are multiple columns; in this case,
the Values is wrapped into a Project that converts each row to a tuple.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `InlineAnyValuesMultiCol`, not the other rules in that file):

```
# InlineAnyValuesMultiCol converts Any with Values input to AnyScalar.
# This version handles the case where there are multiple columns; in this case,
# the Values is wrapped into a Project that converts each row to a tuple.
[InlineAnyValuesMultiCol, Normalize]
(Any
    (Project
        $values:(Values * $valuesPrivate:*)
        [ (ProjectionsItem $tuple:(Tuple)) ] &
            (IsTupleOfVars $tuple (ValuesCols $valuesPrivate))
        $passthrough:* & (ColsAreEmpty $passthrough)
    )
    $scalar:*
    $private:*
)
=>
(AnyScalar $scalar (InlineValues $values) (SubqueryCmp $private))
```
- Attempts used: 71
- Last updated: 2026-10-15T13:16:59.805036+00:00
- Reason / notes: The encoding faithfully captures the core InlineAnyValuesMultiCol transformation (semi-join against a constant multi-row Values inlined into a filter with OR-of-ANDs) in a minimal non-trivial multi-column case; SEMI JOIN correctly models `= ANY`/IN semantics, the 2-column conjunction distinguishes it from the single-col variant, and concrete values are structurally required by the rule (it inlines constant Values, not arbitrary subqueries). The SCOPE tag honestly and specifically acknowledges the fixed arity and equality-only restriction. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 74828334}, panicked=False

### `InlineAnyValuesSingleCol` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

InlineAnyValuesSingleCol converts Any with Values input to AnyScalar.
This version handles the case where there is a single column.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `InlineAnyValuesSingleCol`, not the other rules in that file):

```
# InlineAnyValuesSingleCol converts Any with Values input to AnyScalar.
# This version handles the case where there is a single column.
[InlineAnyValuesSingleCol, Normalize]
(Any $values:(Values) $scalar:* $private:*)
=>
(AnyScalar $scalar (InlineValues $values) (SubqueryCmp $private))
```
- Attempts used: 48
- Last updated: 2026-10-15T13:35:03.239221+00:00
- Reason / notes: The proved equivalence is genuine and non-vacuous — S SEMI JOIN the concrete 2-row values bag {(1),(3)} on S.c0 = V.c0 is the exact relational meaning of `x = ANY (VALUES (1),(3))`, and its equivalence to the inlined filter `S.c0 = 1 OR S.c0 = 3` is the rule's core invariant, with the constants correctly shared (if the Values literals and the filter literals had not unified, QED's SMT check would have refuted the equivalence rather than proving it). The hard-coded EQUALS comparison and 2-row values list are genuine, specifically-named restrictions of this encoding (RuleScript's fixed-shape patterns cannot quantify over the values-list length, and inlining inherently requires concrete constants), and the file's SCOPE: PARTIAL line declares them precisely, making this a faithful, non-degenerate special instance of the rule rather than a misleading full-generality claim.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 72862959}, panicked=False

### `InlineConstVar` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/inline.opt

InlineConstVar inlines variables which are restricted to be constant, as in
SELECT * FROM foo WHERE a = 4 AND a IN (1, 2, 3, 4).
=>
SELECT * FROM foo WHERE a = 4 AND 4 IN (1, 2, 3, 4).
Note that a single iteration of this rule might not be sufficient to inline
all variables, in which case it will trigger itself again.

This rule is high priority so that it runs before filter pushdown.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineConstVar`, not the other rules in that file):

```
# InlineConstVar inlines variables which are restricted to be constant, as in
#   SELECT * FROM foo WHERE a = 4 AND a IN (1, 2, 3, 4).
# =>
#   SELECT * FROM foo WHERE a = 4 AND 4 IN (1, 2, 3, 4).
# Note that a single iteration of this rule might not be sufficient to inline
# all variables, in which case it will trigger itself again.
#
# This rule is high priority so that it runs before filter pushdown.
[InlineConstVar, Normalize, HighPriority]
(Select $input:* $filters:* & (CanInlineConstVar $filters))
=>
(Select $input (InlineConstVar $filters))
```
- Attempts used: 46
- Last updated: 2026-09-27T04:25:24.931761+00:00
- Reason / notes: The proof is non-vacuous and not coincidental: `before()` and `after()` are structurally distinct (same uninterpreted predicate `f` applied to join field 0 vs field 1), and the equivalence genuinely depends on the concrete `EQUALS` join condition `a = c` plus congruence — with an uninterpreted equality symbol it would not prove, so the porter made exactly the right symbol choices. Modeling "a variable restricted to a constant c" as an INNER join against a unique single-column relation is semantically equivalent to the real rule's conjunct-based premise (`Join_{a=c}` ≡ cross-join filtered by `a = c`), so the relational shape faithfully reproduces the source rule's example modulo tagging rows with the constant — a reasonable workaround given the DSL exposes no typed constant literals (only boolean literals in `RexRN`). The narrowing to one variable/one conjunct/one occurrence is a genuine, specific, and honestly disclosed special case that captures precisely the single-substitution step the original rule applies (and re-triggers) on, so the `PARTIAL` scope tag is accurate and the result is a useful, non-degenerate lemma rather than a vacuous identity.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 80385791}, panicked=False

### `InlineJoinConstantsLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/inline.opt

InlineJoinConstantsLeft finds variable references in a join condition that
refers to constant values projected by the left input. It then inlines those
constant values in place of the corresponding variable references. This
sometimes allows further simplifications such as constant folding or filter
pushdown.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineJoinConstantsLeft`, not the other rules in that file):

```
# InlineJoinConstantsLeft finds variable references in a join condition that
# refers to constant values projected by the left input. It then inlines those
# constant values in place of the corresponding variable references. This
# sometimes allows further simplifications such as constant folding or filter
# pushdown.
[InlineJoinConstantsLeft, Normalize]
(Join
    $left:* &
        ^(ColsAreEmpty $constCols:(FindInlinableConstants $left))
    $right:*
    $on:[
        ...
        $item:* & (ColsIntersect (OuterCols $item) $constCols)
        ...
    ]
    $private:* & (NoJoinHints $private)
)
=>
((OpName)
    $left
    $right
    (InlineFilterConstants $on $left $constCols)
    $private
)
```
- Attempts used: 24
- Last updated: 2026-09-27T05:33:40.063791+00:00
- Reason / notes: The encoding faithfully captures the rule's semantic essence — the original's inlinable constant column maps to `a`, the inlined-to constant value (a literal the DSL can't express) maps to `c` of a unique 1-row relation, `a = c` is enforced by the INNER join that builds the left input, and the single uninterpreted conjunct `H(a, r)` is rewritten to `H(c, r)`, which is exactly the original's "substitute the constant value for the constant-column reference in the ON condition." `before()` and `after()` genuinely differ (field 0 vs field 1), the `a = c` constraint is load-bearing so the equivalence is non-vacuous, `H` and the three scans are shared/independent correctly, and INNER is a valid instance of the rule's join; the flagged PARTIAL scope (constant realized via a join-with-unique-rel, one conjunct, one occurrence, INNER) is a genuine DSL limitation — the language has no typed constant literals and no way to assert that an opaque column is constant — so this is an honest, non-degenerate special case rather than a vacuous or unsound one.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 72319375}, panicked=False

### `InlineJoinConstantsRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/inline.opt

InlineJoinConstantsRight finds variable references in a join condition that
refers to constant values projected by the right input. It then inlines those
constant values in place of the corresponding variable references. This
sometimes allows further simplifications such as constant folding or filter
pushdown.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineJoinConstantsRight`, not the other rules in that file):

```
# InlineJoinConstantsRight finds variable references in a join condition that
# refers to constant values projected by the right input. It then inlines those
# constant values in place of the corresponding variable references. This
# sometimes allows further simplifications such as constant folding or filter
# pushdown.
[InlineJoinConstantsRight, Normalize]
(Join
    $left:*
    $right:* &
        ^(ColsAreEmpty
            $constCols:(FindInlinableConstants $right)
        )
    $on:[
        ...
        $item:* & (ColsIntersect (OuterCols $item) $constCols)
        ...
    ]
    $private:* & (NoJoinHints $private)
)
=>
((OpName)
    $left
    $right
    (InlineFilterConstants $on $right $constCols)
    $private
)
```
- Attempts used: 5
- Last updated: 2026-09-27T05:45:30.280643+00:00
- Reason / notes: The encoding is non-trivial and faithful: `before()` references right-input column 0 (`r`) in the join condition while `after()` references column 2 (`c`), and QED must genuinely derive their equality from the inner join's `r = c` constraint on the right input — this is a real semantic consequence, not a structural identity. The porter correctly preserves the original rule's structure (same inputs, same join kind, only the condition's column reference changes), uses an uninterpreted `H` for the join predicate (matching the rule's arbitrary `$on`), models the constant via a unique single-column relation (a sound proxy for a literal since the DSL lacks typed constants), and honestly tags the scope as PARTIAL with specific, concrete restrictions (INNER join only, single conjunct, one occurrence, concrete EQUALS for the constant-enforcing inner join).
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 20083000}, panicked=False

### `InlineProjectConstants` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/inline.opt

InlineProjectConstants finds variable references in Projections expressions
that refer to constant input values, and then inlines those constant values
in place of the corresponding variable references. This sometimes allows
further simplifications such as constant folding or Project merging.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineProjectConstants`, not the other rules in that file):

```
# InlineProjectConstants finds variable references in Projections expressions
# that refer to constant input values, and then inlines those constant values
# in place of the corresponding variable references. This sometimes allows
# further simplifications such as constant folding or Project merging.
[InlineProjectConstants, Normalize]
(Project
    $input:* &
        ^(ColsAreEmpty
            $constCols:(FindInlinableConstants $input)
        )
    $projections:[
        ...
        $item:* & (ColsIntersect (OuterCols $item) $constCols)
        ...
    ]
    $passthrough:*
)
=>
(Project
    $input
    (InlineProjectionConstants $projections $input $constCols)
    $passthrough
)
```
- Attempts used: 21
- Last updated: 2026-09-27T06:04:37.519915+00:00
- Reason / notes: The encoding faithfully captures `InlineProjectConstants`' core shape — a `Project` whose input carries an inlinable constant column, with one projection item's reference to that column swapped for the constant — and the rewrite is non-vacuous (over the shared inner-equality join `a=c`, projecting `f(a)` vs `f(c)` differ structurally and are equal only because the join forces `a=c`, so the proof is of a real rewrite, not an identity). It uses the correct relational operator (inlining inside a projection, not the filter-based sibling rules), correctly shares the uninterpreted function `f` and the join across both sides while keeping `a`/`c` as distinct join fields, and encodes the inlinable-constant precondition via the concrete `EQUALS` condition, which is the rule's defining premise rather than an arbitrary hard-coded predicate. The narrowing (the constant modeled as a unique single-column relation joined by equality because the DSL has no typed literals) is specific, genuine, and honestly tagged `SCOPE: PARTIAL`, yielding a useful, non-degenerate result.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 10614958}, panicked=False

### `InlineProjectInProject` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/inline.opt

InlineProjectInProject folds an inner Project operator into an outer Project
that references each inner synthesized column no more than one time. If there
are no duplicate references, then there's no benefit to keeping the multiple
nested projections. This rule simplifies the relational expression tree and
makes it more likely that other normalization rules will match.

This rule is low priority so that it runs after the MergeProjects rule, since
that rule is cheaper to match and replace.

Example:
SELECT x2*2 FROM (SELECT x+1 AS x2 FROM xy)
=>
SELECT (x+1)*2 FROM xy

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineProjectInProject`, not the other rules in that file):

```
# InlineProjectInProject folds an inner Project operator into an outer Project
# that references each inner synthesized column no more than one time. If there
# are no duplicate references, then there's no benefit to keeping the multiple
# nested projections. This rule simplifies the relational expression tree and
# makes it more likely that other normalization rules will match.
#
# This rule is low priority so that it runs after the MergeProjects rule, since
# that rule is cheaper to match and replace.
#
# Example:
#   SELECT x2*2 FROM (SELECT x+1 AS x2 FROM xy)
#   =>
#   SELECT (x+1)*2 FROM xy
#
[InlineProjectInProject, Normalize, LowPriority]
(Project
    $input:(Project * $innerProjections:*)
    $projections:*
    $passthrough:* &
        ^(HasDuplicateRefs
            $projections
            $passthrough
            (ProjectionCols $innerProjections)
        )
)
=>
(InlineProjectProject $input $projections $passthrough)
```
- Attempts used: 24
- Last updated: 2026-09-27T05:59:25.588963+00:00
- Reason / notes: The encoding faithfully captures the rule's semantic core — flattening `Project(Project(Source, F(x)), [T(F(x)), F(x)])` into `Project(Source, [T(F(x)), F(x)])` — with `F` and `T` fully uninterpreted and correctly shared between both sides (the equivalence only holds because the same operator symbols are reused, so the proof is non-vacuous and not a structural-identity artifact), and it exercises both substitution paths the real rule performs (a computed outer expression and a passthrough reference to the inner synthesized column). The source rule's `HasDuplicateRefs` guard is a cost-based matching criterion (avoid computing an expression twice), not a bag-semantic precondition, so its absence from the encoding — the encoded instance even references the inner column twice — does not change the validity claim; if anything the proved instance is a semantically stronger claim than the guarded one, and the `SCOPE: PARTIAL` line honestly and specifically discloses the fixed 1-inner-column / 1-computed / 1-passthrough arity rather than claiming full generality.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 103167}, panicked=False

### `InlineSelectConstants` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/inline.opt

InlineSelectConstants finds variable references in Filters expressions that
refer to constant input values, and then inlines those constant values in
place of the corresponding variable references. This sometimes allows further
simplifications such as constant folding or generation of constrained scans.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineSelectConstants`, not the other rules in that file):

```
# InlineSelectConstants finds variable references in Filters expressions that
# refer to constant input values, and then inlines those constant values in
# place of the corresponding variable references. This sometimes allows further
# simplifications such as constant folding or generation of constrained scans.
[InlineSelectConstants, Normalize]
(Select
    $input:* &
        ^(ColsAreEmpty
            $constCols:(FindInlinableConstants $input)
        )
    $filters:[
        ...
        $item:* & (ColsIntersect (OuterCols $item) $constCols)
        ...
    ]
)
=>
(Select
    $input
    (InlineFilterConstants $filters $input $constCols)
)
```
- Attempts used: 49
- Last updated: 2026-09-27T07:05:22.420732+00:00
- Reason / notes: The encoding is non-vacuous and faithful: before() filters on field 0 (`a`) and after() on field 1 (`c`), which are distinct columns whose equivalence follows solely from the INNER join condition `a = c`, so the proof captures exactly the rule's core soundness claim — substituting an equal constant value for a column reference inside a filter predicate — with the constant legitimately modeled as a unique single-column relation joined by concrete EQUALS since the DSL lacks typed constant literals. The uninterpreted predicate `g` is correctly shared across both sides (inlining changes the referenced column, not the predicate), the symbol sharing between the join condition and the filter references is semantically right (same physical columns), and no missing precondition changes the claim (the join invariant `a = c` on every input row is precisely what inlining requires, and the original's "inlinable constant" case is a special case of it). The `SCOPE: PARTIAL` line is honest and specific: it narrows to one uninterpreted filter item with one occurrence of one argument and one constant column, which is a genuine, non-degenerate special case of CockroachDB's `InlineSelectConstants` rather than a structural identity. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 67750542}, panicked=False

### `InlineSelectVirtualColumns` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/inline.opt

InlineSelectVirtualColumns pushes Select filters referencing virtual columns
into a Project by inlining the virtual column expressions. This makes the
Select independent of the Project. Because these filters are pushed below the
Project, exploration rules that match on the (Select (Scan)) pattern can
generate plans that use indexes on virtual columns.

Filters on non-virtual projected columns are not inlined because the
expression would be executed twice (once in the filter and once in the
projection), adding overhead without any chance of a secondary index on a
virtual column being used in the optimized plan.

Notice that this rule is similar to PushSelectIntoInlinableProject. The key
difference is that PushSelectIntoInlinableProject only inlines simple
expressions that will add negligible overhead when computing twice.
Conversely, InlineSelectVirtualColumns does not discriminate by the type of
expression. It will inline all virtual columns in the hopes that inlining will
lead to a query plan that uses a virtual column index.

Also, PushSelectIntoInlinableProject will inline filters if and only if all of
the filter items are inlinable (by its definition), whereas
InlineSelectVirtualColumns will split the input filters into two groups: one
to inline below the Project, and one to leave above the Project. This allows
filters on virtual columns to be pushed down in more cases.

For example, consider the table and query:

CREATE TABLE t (
a INT,
b INT,
v INT AS (abs(a)) VIRTUAL,
INDEX (v)
)
SELECT v, w FROM (
SELECT v, abs(b) AS w FROM t
) WHERE v = 5 AND w = 10

The partially normalized expression for the SELECT query before
InlineSelectVirtualColumns is applied is:

select
├── columns: v:3 w:6
├── project
│    ├── columns: w:6 v:3
│    ├── scan t
│    │    └── columns: a:1 b:2
│    └── projections
│         ├── abs(b:2) [as=w:6]
│         └── abs(a:1) [as=v:3]
└── filters
├── v:3 = 5
└── w:6 = 10

InlineSelectVirtualColumns will push only the (v = 5) filter below the Project
as (abs(a) = 5) because v is a virtual column. The (w = 10) filter remains
above the Project. Notice the (Select (Scan)) pattern that will allow a
constrained scan over the secondary index to be generated.

select
├── columns: v:3 w:6
├── project
│    ├── columns: w:6 v:3
│    ├── select
│    │    ├── columns: a:1 b:2
│    │    ├── scan t
│    │    │    └── columns: a:1 b:2
│    │    └── filters
│    │         └── abs(a:1) = 5
│    └── projections
│         ├── abs(b:2) [as=w:6]
│         └── abs(a:1) [as=v:3]
└── filters
└── w:6 = 10

This rule has no explicit priority so that it runs before
PushSelectIntoInlinableProject (which is low priority). It must run before
PushSelectIntoInlinableProject in order to match the (Select (Project (Scan)))
pattern which is produced by optbuilder for a filter on a table with virtual
columns.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineSelectVirtualColumns`, not the other rules in that file):

```
# InlineSelectVirtualColumns pushes Select filters referencing virtual columns
# into a Project by inlining the virtual column expressions. This makes the
# Select independent of the Project. Because these filters are pushed below the
# Project, exploration rules that match on the (Select (Scan)) pattern can
# generate plans that use indexes on virtual columns.
#
# Filters on non-virtual projected columns are not inlined because the
# expression would be executed twice (once in the filter and once in the
# projection), adding overhead without any chance of a secondary index on a
# virtual column being used in the optimized plan.
#
# Notice that this rule is similar to PushSelectIntoInlinableProject. The key
# difference is that PushSelectIntoInlinableProject only inlines simple
# expressions that will add negligible overhead when computing twice.
# Conversely, InlineSelectVirtualColumns does not discriminate by the type of
# expression. It will inline all virtual columns in the hopes that inlining will
# lead to a query plan that uses a virtual column index.
#
# Also, PushSelectIntoInlinableProject will inline filters if and only if all of
# the filter items are inlinable (by its definition), whereas
# InlineSelectVirtualColumns will split the input filters into two groups: one
# to inline below the Project, and one to leave above the Project. This allows
# filters on virtual columns to be pushed down in more cases.
#
# For example, consider the table and query:
#
#   CREATE TABLE t (
#     a INT,
#     b INT,
#     v INT AS (abs(a)) VIRTUAL,
#     INDEX (v)
#   )
#   SELECT v, w FROM (
#     SELECT v, abs(b) AS w FROM t
#   ) WHERE v = 5 AND w = 10
#
# The partially normalized expression for the SELECT query before
# InlineSelectVirtualColumns is applied is:
#
#   select
#    ├── columns: v:3 w:6
#    ├── project
#    │    ├── columns: w:6 v:3
#    │    ├── scan t
#    │    │    └── columns: a:1 b:2
#    │    └── projections
#    │         ├── abs(b:2) [as=w:6]
#    │         └── abs(a:1) [as=v:3]
#    └── filters
#         ├── v:3 = 5
#         └── w:6 = 10
#
# InlineSelectVirtualColumns will push only the (v = 5) filter below the Project
# as (abs(a) = 5) because v is a virtual column. The (w = 10) filter remains
# above the Project. Notice the (Select (Scan)) pattern that will allow a
# constrained scan over the secondary index to be generated.
#
#   select
#    ├── columns: v:3 w:6
#    ├── project
#    │    ├── columns: w:6 v:3
#    │    ├── select
#    │    │    ├── columns: a:1 b:2
#    │    │    ├── scan t
#    │    │    │    └── columns: a:1 b:2
#    │    │    └── filters
#    │    │         └── abs(a:1) = 5
#    │    └── projections
#    │         ├── abs(b:2) [as=w:6]
#    │         └── abs(a:1) [as=v:3]
#    └── filters
#         └── w:6 = 10
#
# This rule has no explicit priority so that it runs before
# PushSelectIntoInlinableProject (which is low priority). It must run before
# PushSelectIntoInlinableProject in order to match the (Select (Project (Scan)))
# pattern which is produced by optbuilder for a filter on a table with virtual
# columns.
[InlineSelectVirtualColumns, Normalize]
(Select
    (Project
        $scan:(Scan $scanPrivate:*)
        $projections:*
        $passthrough:*
    )
    $filters:* &
        ^(ColsAreEmpty
            $virtualColumns:(VirtualColumns $scanPrivate)
        ) &
        ^(IsFilterEmpty
            $inlinableFilters:(InlinableVirtualColumnFilters
                $filters
                $virtualColumns
            )
        )
)
=>
(Select
    (Project
        (Select
            $scan
            (InlineSelectProject $inlinableFilters $projections)
        )
        $projections
        $passthrough
    )
    (DiffFilters $filters $inlinableFilters)
)
```
- Attempts used: 26
- Last updated: 2026-09-27T06:34:35.819791+00:00
- Reason / notes: The encoding faithfully mirrors the rule's actual transformation — before() is `Filter(P(v)∧Q(w), Project(F,G,a,b, Scan))` and after() pushes only the virtual-column filter as `P(F(a,b))` (now referencing solely scan columns) below the Project while keeping the non-virtual filter `Q(G(a,b))` above, exactly what InlineSelectVirtualColumns does; predicates P/Q and expressions F/G are all uninterpreted, and the inlined filter correctly reuses the same symbol P on the defining expression as it did on the virtual column, so the equivalence is non-vacuous rather than structurally identical. The scope is honestly and specifically declared PARTIAL (fixed to one virtual + one non-virtual synthesized + two passthrough columns, one inlinable and one non-inlinable filter, with the Optgen ColsAreEmpty/IsFilterEmpty guards omitted as non-bag-semantic preconditions), a genuine non-degenerate special case whose before/after genuinely differ.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 67466750}, panicked=False

### `InvertMinus` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/numeric.opt

InvertMinus rewrites -(a - b) to (b - a) if the operand types allow it.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `InvertMinus`, not the other rules in that file):

```
# InvertMinus rewrites -(a - b) to (b - a) if the operand types allow it.
[InvertMinus, Normalize]
(UnaryMinus
    (Minus $left:* $right:*) &
        (CanConstructBinary Minus $right $left)
)
=>
(Minus $right $left)
```
- Attempts used: 66
- Last updated: 2026-10-15T14:52:08.120441+00:00
- Reason / notes: before() = Project[UNARY_MINUS(MINUS(a,b))] and after() = Project[MINUS(b,a)] over a shared, universally-quantified 2-column scan with independent fields a and b — a non-vacuous, structurally faithful rendering of `UnaryMinus(Minus $left $right) => Minus $right $left` using the actual MINUS/UNARY_MINUS operators and no symbol-sharing shortcut. The same-type INTEGER columns and the unexpressed CanConstructBinary guard (a meta-level type-constructibility check, tautological for same-type operands) cannot narrow the proven claim, since QED maps all types into one integer theory (per RelType.java's ground-truth note), so the proved identity ∀a,b: -(a-b) = b-a is exactly the rule's full value-level content, making SCOPE: FULL honest. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 74509292}, panicked=False

### `LeftAssociateJoinsLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

LeftAssociateJoinsLeft reorders InnerJoins so that join filters can be brought
closer to the relations they reference. This in done in hopes of allowing
other rules (for example, limit push-down) to fire. It also has the effect of
pushing cross joins up the operator tree. LeftAssociateJoinsLeft matches when
the following conditions are true:
1. The inside InnerJoin is the left input of the outside InnerJoin.
2. The outside InnerJoin's ON condition has an equality between the right input
of the outside InnerJoin and the left input of the inside InnerJoin.
3. The inside InnerJoin's ON condition is empty.

The transformation:

SELECT * FROM (SELECT * FROM xy INNER JOIN uv ON True)
INNER JOIN ab
ON a=x AND b=u
=>
SELECT * FROM uv
INNER JOIN (SELECT * FROM xy INNER JOIN ab ON a=x)
ON b=u

In this example, neither of the filters in the original query could be pushed
down because they both reference ab. With the joins reordered, the a=x filter
can be pushed down closer to xy.

There are three variants of LeftAssociateJoinsLeft below this rule definition.

In the worst case scenario, LeftAssociateJoinsLeft and its variants will
be fired (n^2)/4 times, where n is the number of joins in the join tree.

LeftAssociateJoinsLeft and its variants are LowPriority so that other rules
(such as filter push-down) have a chance to fire first.

Extracted from `join.opt` (which defines multiple rules — implement specifically `LeftAssociateJoinsLeft`, not the other rules in that file):

```
# LeftAssociateJoinsLeft reorders InnerJoins so that join filters can be brought
# closer to the relations they reference. This in done in hopes of allowing
# other rules (for example, limit push-down) to fire. It also has the effect of
# pushing cross joins up the operator tree. LeftAssociateJoinsLeft matches when
# the following conditions are true:
# 1. The inside InnerJoin is the left input of the outside InnerJoin.
# 2. The outside InnerJoin's ON condition has an equality between the right input
#    of the outside InnerJoin and the left input of the inside InnerJoin.
# 3. The inside InnerJoin's ON condition is empty.
#
# The transformation:
#
#   SELECT * FROM (SELECT * FROM xy INNER JOIN uv ON True)
#   INNER JOIN ab
#   ON a=x AND b=u
# =>
#   SELECT * FROM uv
#   INNER JOIN (SELECT * FROM xy INNER JOIN ab ON a=x)
#   ON b=u
#
# In this example, neither of the filters in the original query could be pushed
# down because they both reference ab. With the joins reordered, the a=x filter
# can be pushed down closer to xy.
#
# There are three variants of LeftAssociateJoinsLeft below this rule definition.
#
# In the worst case scenario, LeftAssociateJoinsLeft and its variants will
# be fired (n^2)/4 times, where n is the number of joins in the join tree.
#
# LeftAssociateJoinsLeft and its variants are LowPriority so that other rules
# (such as filter push-down) have a chance to fire first.
[LeftAssociateJoinsLeft, Normalize, LowPriority]
(InnerJoin
    (InnerJoin
        $insideLeft:*
        $insideRight:*
        []
        $insidePrivate:* & (NoJoinHints $insidePrivate)
    )
    $outsideRight:*
    $outsideOn:[
        ...
        $item:* &
            (IsBoundBy
                $item
                $cols:(OutputCols2 $insideLeft $outsideRight)
            )
        ...
    ]
    $outsidePrivate:* & (NoJoinHints $outsidePrivate)
)
=>
(InnerJoin
    $insideRight
    (InnerJoin
        $insideLeft
        $outsideRight
        (ExtractBoundConditions $outsideOn $cols)
        (EmptyJoinPrivate)
    )
    (ExtractUnboundConditions $outsideOn $cols)
    (EmptyJoinPrivate)
)
```
- Attempts used: 23
- Last updated: 2026-09-27T07:21:54.446339+00:00
- Reason / notes: The encoding faithfully captures the core algebraic identity of LeftAssociateJoinsLeft — reassociating ((A×B) ⋈_{P(A,C)∧Q(B,C)} C) into B ⋈_{Q(B,C)} (A ⋈_{P(A,C)} C) up to column reordering — with correct inner-join kinds, a true-literal cross join for the empty inner ON, properly shared uninterpreted predicates (bound over (A,C), unbound over (B,C)) whose argument positions are consistently mapped across both sides, and a projection to equalize output column order. The SCOPE line honestly flags the main narrowing (fixed two-conjunct ON with unbound restricted to (B,C) rather than the fully general ExtractBound/Unbound split which could also reference A in the unbound conjunct), and the proof is non-vacuous since before and after are structurally distinct join trees whose equivalence is a genuine universal algebraic identity over arbitrary instantiations of the uninterpreted symbols. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 70874625}, panicked=False

### `LeftAssociateJoinsRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

LeftAssociateJoinsRight is a variant on LeftAssociateJoinsLeft.
The transformation:

SELECT * FROM (SELECT * FROM xy INNER JOIN uv ON True)
INNER JOIN ab
ON a=x AND b=u
=>
SELECT * FROM xy
INNER JOIN (SELECT * FROM uv INNER JOIN ab ON b=u)
ON a=x

Extracted from `join.opt` (which defines multiple rules — implement specifically `LeftAssociateJoinsRight`, not the other rules in that file):

```
# LeftAssociateJoinsRight is a variant on LeftAssociateJoinsLeft.
# The transformation:
#
#   SELECT * FROM (SELECT * FROM xy INNER JOIN uv ON True)
#   INNER JOIN ab
#   ON a=x AND b=u
# =>
#   SELECT * FROM xy
#   INNER JOIN (SELECT * FROM uv INNER JOIN ab ON b=u)
#   ON a=x
#
[LeftAssociateJoinsRight, Normalize, LowPriority]
(InnerJoin
    (InnerJoin
        $insideLeft:*
        $insideRight:*
        []
        $insidePrivate:* & (NoJoinHints $insidePrivate)
    )
    $outsideRight:*
    $outsideOn:[
        ...
        $item:* &
            (IsBoundBy
                $item
                $cols:(OutputCols2 $insideRight $outsideRight)
            )
        ...
    ]
    $outsidePrivate:* & (NoJoinHints $outsidePrivate)
)
=>
(InnerJoin
    $insideLeft
    (InnerJoin
        $insideRight
        $outsideRight
        (ExtractBoundConditions $outsideOn $cols)
        (EmptyJoinPrivate)
    )
    (ExtractUnboundConditions $outsideOn $cols)
    (EmptyJoinPrivate)
)
```
- Attempts used: 25
- Last updated: 2026-09-27T07:37:04.515789+00:00
- Reason / notes: The encoding faithfully captures the source rule's logical transformation: (A⋈B ON true)⋈C ON (p_ax∧p_bu) reassociates to A⋈(B⋈C ON p_bu) ON p_ax, where p_bu (bound by B,C) is pushed into the new inner join and p_ax (referencing all three) remains at the outer level. The two uninterpreted predicates correctly model the source rule's partition of $outsideOn into ExtractBoundConditions and ExtractUnboundConditions, the join kinds (INNER) and the empty inner condition (true) match the source, and the column contexts in joinField ordinals are consistent across both sides. The only omissions are backend-specific join-hint metadata (NoJoinHints/EmptyJoinPrivate) which have no bearing on logical equivalence and are inexpressible in the DSL, so SCOPE: FULL is appropriate. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 69396084}, panicked=False

### `MapEqualityIntoJoinLeftAndRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

MapEqualityIntoJoinLeftAndRight checks whether it is possible to map
equality conditions in a join to use different variables so that the
number of conditions crossing both sides of a join are minimized. If so,
the MapEqualityConditions function performs this mapping to construct new
filters.

For example, consider this query:

SELECT * FROM a, b WHERE a.x = b.x AND b.x = a.y;

As written, both equality conditions contain variables from both sides of
the join. We can rewrite this query, however, so that only one condition
spans both sides:

SELECT * FROM a, b WHERE a.x = a.y AND b.x = a.y;

Now the condition a.x = a.y is fully bound by the left side of the join,
and is available to be pushed down by PushFilterIntoJoinLeft.

See the MapEqualityConditions function for more details.

Extracted from `join.opt` (which defines multiple rules — implement specifically `MapEqualityIntoJoinLeftAndRight`, not the other rules in that file):

```
# MapEqualityIntoJoinLeftAndRight checks whether it is possible to map
# equality conditions in a join to use different variables so that the
# number of conditions crossing both sides of a join are minimized. If so,
# the MapEqualityConditions function performs this mapping to construct new
# filters.
#
# For example, consider this query:
#
#   SELECT * FROM a, b WHERE a.x = b.x AND b.x = a.y;
#
# As written, both equality conditions contain variables from both sides of
# the join. We can rewrite this query, however, so that only one condition
# spans both sides:
#
#   SELECT * FROM a, b WHERE a.x = a.y AND b.x = a.y;
#
# Now the condition a.x = a.y is fully bound by the left side of the join,
# and is available to be pushed down by PushFilterIntoJoinLeft.
#
# See the MapEqualityConditions function for more details.
[MapEqualityIntoJoinLeftAndRight, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:* & ^(HasOuterCols $left)
    $right:* & ^(HasOuterCols $right)
    $on:* &
        (CanMapJoinOpEqualities
            $on
            $leftCols:(OutputCols $left)
            $rightCols:(OutputCols $right)
        )
    $private:*
)
=>
((OpName)
    $left
    $right
    (MapJoinOpEqualities $on $leftCols $rightCols)
    $private
)
```
- Attempts used: 24
- Last updated: 2026-09-27T07:41:26.624182+00:00
- Reason / notes: The encoding faithfully captures the rule's core equality-remapping operation for its own documented minimal instance: before() uses the two cross-boundary equalities (a.x=b.x ∧ b.x=a.y) and after() the remapped set (b.x=a.y ∧ a.x=a.y), which are structurally different and whose equivalence (all three columns equal) is a genuine equality-transitivity/symmetry fact QED actually had to prove via real EQUALS (not an uninterpreted symbol, whose equivalence would fail), with the join columns and the shared b.x=a.y condition wired correctly and the base-scan inputs satisfying the rule's no-outer-columns guard. The under-generalization (INNER only, fixed two-equality/three-column shape versus the rule's 8 join kinds and arbitrary MapJoinOpEqualities FD-closure remapping) is forced by RuleScript's single before/after pattern expressiveness rather than laziness, and is honestly flagged SCOPE: PARTIAL, so this is a genuine, non-degenerate special case—not a vacuous or misleading proof. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 69539584}, panicked=False

### `MapFilterIntoJoinLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

MapFilterIntoJoinLeft maps a filter that is not bound by the left side of
the join to use the columns from the left side. This will allow
the filter to be pushed down by the PushFilterIntoJoinLeft rule.
For example, consider this query:

SELECT * FROM a INNER JOIN b ON a.x = b.x AND b.x + a.y < 5

In this case, we can map b.x + a.y < 5 to the left side by replacing b.x
with the equivalent column a.x.
NOTE: This rule only applies to cases where it is not possible or not safe
to map the filter to both sides. If it can be mapped to both sides, it
will be handled by PushFilterIntoJoinLeftAndRight (which must be
ordered above this rule). For performance reasons, this rule should
be ordered before PushFilterIntoJoinLeft (otherwise,
PushFilterIntoJoinLeft might need to be applied multiple times).

Extracted from `join.opt` (which defines multiple rules — implement specifically `MapFilterIntoJoinLeft`, not the other rules in that file):

```
# MapFilterIntoJoinLeft maps a filter that is not bound by the left side of
# the join to use the columns from the left side. This will allow
# the filter to be pushed down by the PushFilterIntoJoinLeft rule.
# For example, consider this query:
#
#   SELECT * FROM a INNER JOIN b ON a.x = b.x AND b.x + a.y < 5
#
# In this case, we can map b.x + a.y < 5 to the left side by replacing b.x
# with the equivalent column a.x.
# NOTE: This rule only applies to cases where it is not possible or not safe
#       to map the filter to both sides. If it can be mapped to both sides, it
#       will be handled by PushFilterIntoJoinLeftAndRight (which must be
#       ordered above this rule). For performance reasons, this rule should
#       be ordered before PushFilterIntoJoinLeft (otherwise,
#       PushFilterIntoJoinLeft might need to be applied multiple times).
[MapFilterIntoJoinLeft, Normalize]
(InnerJoin | InnerJoinApply | SemiJoin | SemiJoinApply
    $left:* & ^(HasOuterCols $left)
    $right:*
    $on:[
        ...
        $item:* &
            ^(FiltersItem (Eq (Variable) (Variable))) &
            ^(IsBoundBy $item $leftCols:(OutputCols $left)) &
            (CanMapJoinOpFilter
                $item
                $leftCols
                $equivSet:(GetEquivGroups $on $left $right)
            )
        ...
    ]
    $private:*
)
=>
((OpName)
    $left
    $right
    (ReplaceFiltersItem
        $on
        $item
        (MapJoinOpFilter $item $leftCols $equivSet)
    )
    $private
)
```
- Attempts used: 41
- Last updated: 2026-09-27T08:02:17.883550+00:00
- Reason / notes: The encoding is a faithful, non-vacuous capture of the rule's core transformation — it reproduces the rule's own documented example (`ON a.x=b.x AND b.x+a.y<5` → `ON a.x=b.x AND a.x+a.y<5`) as an INNER join where a right-bound filter item `g(r0,y)` is rebound to the left key `g(l0,y)` while the equi conjunct is retained; the proof is genuine (not structural) because it hinges on congruence of the shared uninterpreted predicate `g` under the *concrete* `EQUALS` conjunct, which is exactly the semantic content the rule relies on. The field indices, join kind, ON-list shape, and symbol sharing (one shared `g`, distinct l0/y/r0 fields) are all correct, no real semantic precondition (not-left-bound, mappable-via-equality, no outer cols) is silently dropped, and the narrowing to INNER-only with a single equi conjunct and a single 2-ary item is specific, honestly disclosed in the SCOPE line, and largely inherent to what the DSL can express — a legitimate, useful special case rather than a degenerate or misleading one. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 71800916}, panicked=False

### `MapFilterIntoJoinRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

MapFilterIntoJoinRight is symmetric with MapFilterIntoJoinLeft. It maps
Join filter conditions to use columns from the right side of the join rather
than the left side. See that rule's comments for more details.

Extracted from `join.opt` (which defines multiple rules — implement specifically `MapFilterIntoJoinRight`, not the other rules in that file):

```
# MapFilterIntoJoinRight is symmetric with MapFilterIntoJoinLeft. It maps
# Join filter conditions to use columns from the right side of the join rather
# than the left side. See that rule's comments for more details.
[MapFilterIntoJoinRight, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:* & ^(HasOuterCols $right)
    $on:[
        ...
        $item:* &
            ^(FiltersItem (Eq (Variable) (Variable))) &
            ^(IsBoundBy $item $rightCols:(OutputCols $right)) &
            (CanMapJoinOpFilter
                $item
                $rightCols
                $equivSet:(GetEquivGroups $on $left $right)
            )
        ...
    ]
    $private:*
)
=>
((OpName)
    $left
    $right
    (ReplaceFiltersItem
        $on
        $item
        (MapJoinOpFilter $item $rightCols $equivSet)
    )
    $private
)
```
- Attempts used: 28
- Last updated: 2026-09-27T08:09:57.771492+00:00
- Reason / notes: before() (ON = a.x=b.x ∧ f(a.x)) and after() (ON = a.x=b.x ∧ f(b.x)) are structurally distinct, so the proof is not vacuous — it requires genuine congruence reasoning (under the eq conjunct, f(a.x) ≡ f(b.x)), which is exactly the equivalence-class column remapping that MapFilterIntoJoinRight performs, and the symbol handling is correct: one shared uninterpreted conjunct f, the shared equality kept in place, and two distinct same-type columns. The restriction to INNER join, single equality, and a single one-argument conjunct is real (the source pattern also covers Left/Semi/Anti joins and multi-equality equiv sets), but it is specific, non-degenerate, and honestly tagged on the SCOPE line — and since MetaJoinType desugars to INNER for the prover, no more general single-rule encoding was available. No preconditions are silently missing: the CanMapJoinOpFilter condition is structurally satisfied by the one equality, no unique keys or nullability assumptions were added (unique=false), and the rule's remap is self-contained (the downstream push-down is a separate rule the porter correctly did not conflate).
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 19848750}, panicked=False

### `MergeProjectWithValues` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Merges an outer Project with an inner single-row Values operator.
- Attempts used: 100
- Last updated: 2026-09-27T10:24:27.314891+00:00
- Reason / notes: Directly encoded using RuleBuilder's real Calcite Values-with-literal-content support (RelBuilder.values(fieldNames, values...)) wrapped in a small local RelRN record -- QED's prover core already has a real interpreted Values{schema, content} variant (relation.rs) and JSONSerializer.java already serializes LogicalValues with its actual row content, so this isn't a DSL gap at all, just needed a custom local RelRN to reach the existing Calcite API (same trick class as the Exists RexRN record). Modeled as a representative single-row, 2-column Values with one passthrough column and one input-independent synthesized (literal) column; verified non-vacuous with a negative control (wrong synthesized literal) correctly failing to prove.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 13622833}, panicked=False

### `MergeProjects` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/project.opt

MergeProjects merges an outer Project operator with an inner Project operator
if there are no references to the inner synthesized columns. This has the
side effect of pruning unused synthesized columns of the inner Project.

Extracted from `project.opt` (which defines multiple rules — implement specifically `MergeProjects`, not the other rules in that file):

```
# MergeProjects merges an outer Project operator with an inner Project operator
# if there are no references to the inner synthesized columns. This has the
# side effect of pruning unused synthesized columns of the inner Project.
[MergeProjects, Normalize]
(Project
    $input:(Project $innerInput:* $innerProjections:*)
    $projections:* &
        (CanMergeProjections $projections $innerProjections)
    $passthrough:*
)
=>
(Project
    $innerInput
    (MergeProjections
        $projections
        $innerProjections
        $passthrough
    )
    (DifferenceCols
        $passthrough
        (ProjectionCols $innerProjections)
    )
)
```
- Attempts used: 61
- Last updated: 2026-09-27T08:42:06.641595+00:00
- Reason / notes: The encoding faithfully captures the Optgen rule's shape, precondition, and effect: `before` is Project(Project(Source, [passthrough, Bottom(passthrough)]), [Top(passthrough)]) — the outer project references only the inner passthrough column, exactly the `CanMergeProjections` condition — and `after` flattens to a single Project over Source, pruning the inner's unused synthesized column, which is the rule's stated purpose. Nothing that should be symbolic is concretized (Top/Bottom operators and all types are uninterpreted; the passthrough symbol is shared consistently), and `before` and `after` are structurally distinct (an extra project layer and a dead computed column), so the proof is non-vacuous. The only restriction is the one-column arity fixed by the fixed-shape DSL, which is honestly disclosed in the SCOPE line and is a genuine, non-degenerate special case rather than a degenerate or vacuous one.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 99584}, panicked=False

### `MergeSelectInnerJoin` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

MergeSelectInnerJoin merges a Select operator with an InnerJoin input by
AND'ing the filter conditions of each and creating a new InnerJoin with that
On condition. This is only safe to do with InnerJoin in the general case
where the conditions could filter either left or right rows. The special case
where a condition filters only one or the other is already taken care of by
the PushSelectIntoJoin rules.
NOTE: Keep this rule ordered before the PushSelectIntoJoin rules to avoid
missing out on the potential for new filter inference based on
equivalent columns.

Extracted from `select.opt` (which defines multiple rules — implement specifically `MergeSelectInnerJoin`, not the other rules in that file):

```
# MergeSelectInnerJoin merges a Select operator with an InnerJoin input by
# AND'ing the filter conditions of each and creating a new InnerJoin with that
# On condition. This is only safe to do with InnerJoin in the general case
# where the conditions could filter either left or right rows. The special case
# where a condition filters only one or the other is already taken care of by
# the PushSelectIntoJoin rules.
# NOTE: Keep this rule ordered before the PushSelectIntoJoin rules to avoid
#       missing out on the potential for new filter inference based on
#       equivalent columns.
[MergeSelectInnerJoin, Normalize]
(Select
    $input:(InnerJoin | InnerJoinApply
        $left:*
        $right:*
        $on:*
        $private:*
    )
    $filters:*
)
=>
((OpName $input)
    $left
    $right
    (ConcatFilters $on $filters)
    $private
)
```
- Attempts used: 21
- Last updated: 2026-09-27T08:29:08.551392+00:00
- Reason / notes: The proof is non-vacuous and captures the rule's exact logical core — before() is Filter(F, InnerJoin(L, R, ON)) and after() is InnerJoin(L, R, ON ∧ F), with ON and F as distinct uninterpreted predicates over the full joined (L++R) row, the correct INNER join kind (the source rule is by design restricted to inner joins), and no silently-added preconditions, since Optgen's rule has none either (and abstracting the filter *list* as one uninterpreted predicate is harmless, as the rule's claim is only about relocating the conjunction). The single genuine narrowing — omitting the InnerJoinApply (correlated) variant — is explicitly and specifically tagged in the SCOPE line, is a real limitation of the current DSL (Correlate's right-side filter is one monolithic condition and cannot AND in an independently built predicate without an extension), and leaves a useful, fully general, non-degenerate rule, so the "provable" result is neither vacuous nor misleading.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 68062375}, panicked=False

### `MergeSelects` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

MergeSelects combines two nested Select operators into a single Select that
ANDs the filter conditions of the two Selects.

Extracted from `select.opt` (which defines multiple rules — implement specifically `MergeSelects`, not the other rules in that file):

```
# MergeSelects combines two nested Select operators into a single Select that
# ANDs the filter conditions of the two Selects.
[MergeSelects, Normalize]
(Select (Select $input:* $innerFilters:*) $filters:*)
=>
(Select $input (ConcatFilters $innerFilters $filters))
```
- Attempts used: 3
- Last updated: 2026-09-27T22:05:42.862499+00:00
- Reason / notes: The encoding faithfully captures MergeSelects: `before()` is two nested filters (inner then outer) and `after()` is a single filter on the AND of the two uninterpreted predicates, which is exactly the source rule `(Select (Select $input $innerFilters) $filters) => (Select $input (ConcatFilters $innerFilters $filters))` since a Select's filter list is semantically the conjunction of its items. The two predicates are independent uninterpreted symbols (no spurious sharing), the input is a single uninterpreted scan (the standard stand-in for "any relation" in this DSL, with the row domain abstracted away so column count is irrelevant to the logical identity), and the source rule has no side conditions, so nothing is missing. SCOPE: FULL is honest — the proof covers arbitrary predicates over arbitrary relations, which subsumes the real rule's "any filter lists" generality. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 66762250}, panicked=False

### `NegateAnd` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

NegateAnd converts the negation of a conjunction into a disjunction of
negations.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `NegateAnd`, not the other rules in that file):

```
# NegateAnd converts the negation of a conjunction into a disjunction of
# negations.
[NegateAnd, Normalize]
(Not (And $left:* $right:*))
=>
(Or (Not $left) (Not $right))
```
- Attempts used: 7
- Last updated: 2026-09-27T22:06:41.543781+00:00
- Reason / notes: before() is Filter(¬(P∧Q), Source) and after() is Filter(¬P∨¬Q, Source) over the same scan, with P and Q as distinct uninterpreted predicates — a structurally different, non-vacuous embedding of Optgen's (Not (And l r)) ⇒ (Or (Not l) (Not r)). The filter-over-scan context is the canonical most-general relational host for a boolean rewrite: the proven row-level identity ∀r. ¬(P(r)∧Q(r)) ↔ ¬P(r)∨Q(r) is precisely the scalar rule itself (and De Morgan holds under both 2VL and SQL three-valued logic), so the FULL scope tag is accurate. Nothing is hard-coded, the two predicate symbols are correctly independent (not coincidentally unified), and the source rule carries no preconditions (no keys/NOT-NULL) that the encoding omits. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 349666}, panicked=False

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

### `NegateOr` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

NegateOr converts the negation of a disjunction into a conjunction of
negations.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `NegateOr`, not the other rules in that file):

```
# NegateOr converts the negation of a disjunction into a conjunction of
# negations.
[NegateOr, Normalize]
(Not (Or $left:* $right:*))
=>
(And (Not $left) (Not $right))
```
- Attempts used: 5
- Last updated: 2026-09-27T22:07:10.180491+00:00
- Reason / notes: The encoding faithfully captures NegateOr — `Not(Or(L,R))` ⟹ `And(Not(L),Not(R))` — using two *distinct* uninterpreted predicates (`left`, `right`) over an uninterpreted base table, so `before()` and `after()` are structurally different and the proof is a genuine universal De Morgan proof rather than a vacuous identity. The source rule is a pure scalar rewrite with no preconditions (and the duality holds even under 3-valued null logic), so nothing is missing; the single-column scan + filter wrapper is the DSL's standard idiom for boolean rewrites (as in the FilterMerge example), and since the identity is row-wise and independent of the predicate's argument structure, the one-column/uninterpreted-predicate setup does not narrow the logical claim — hence `// SCOPE: FULL` is honest. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 355959}, panicked=False

### `NormalizeCmpPlusConst` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

NormalizeCmpPlusConst builds up constant expression trees on one side of the
comparison, in cases like this:
cmp          cmp
/   \        /   \
[+]    2  ->  a   [-]
/   \             /   \
a     1           2     1

The rule can only perform this transformation if all of the following criteria
are met:

1. The generated Minus expression will error if there is an overflow (see
ArithmeticErrorsOnOverflow).
2. A Minus overload for the given input types exists and has an appropriate
volatility.
2. There is no error when evaluating the new binary expression.

NOTE: Ne is not part of the operator choices because it wasn't handled in
normalize.go either. We can add once we've proved it's OK to do so.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `NormalizeCmpPlusConst`, not the other rules in that file):

```
# NormalizeCmpPlusConst builds up constant expression trees on one side of the
# comparison, in cases like this:
#       cmp          cmp
#      /   \        /   \
#    [+]    2  ->  a   [-]
#   /   \             /   \
#  a     1           2     1
#
# The rule can only perform this transformation if all of the following criteria
# are met:
#
#   1. The generated Minus expression will error if there is an overflow (see
#      ArithmeticErrorsOnOverflow).
#   2. A Minus overload for the given input types exists and has an appropriate
#      volatility.
#  2. There is no error when evaluating the new binary expression.
#
# NOTE: Ne is not part of the operator choices because it wasn't handled in
#       normalize.go either. We can add once we've proved it's OK to do so.
[NormalizeCmpPlusConst, Normalize]
(Eq | Ge | Gt | Le | Lt
    (Plus $leftLeft:^(ConstValue) $leftRight:(Const))
    $right:(Const) &
        (ArithmeticErrorsOnOverflow
            (TypeOf $right)
            (TypeOf $leftRight)
        ) &
        (CanConstructBinary Minus $right $leftRight) &
        (Let
            ($result $ok):(FoldBinary Minus $right $leftRight)
            $ok
        )
)
=>
((OpName) $leftLeft $result)
```
- Attempts used: 65
- Last updated: 2026-10-15T16:08:08.843717+00:00
- Reason / notes: The encoding faithfully captures the rule's core transformation — (a + c1) < c2 ⇒ a < (c2 − c1), with correct Plus/Minus operand directions — and concrete INTEGER arithmetic is forced by provability, since QED cannot reason about uninterpreted +/−/< and its no-wraparound integer theory (with matching null propagation) corresponds exactly to the source rule's ArithmeticErrorsOnOverflow guard. The 3-column scan is sound because QED's bag-equality proof over all table contents establishes the pointwise predicate equivalence for all value triples, and the only restriction — Lt rather than the full Eq|Ge|Gt|Le|Lt family — is a genuine DSL gap (no scalar-operator-family mechanism exists, unlike MetaJoinType for joins) that is accurately and specifically tagged as PARTIAL; before/after are structurally distinct and the proof is non-vacuous, so this is a useful, non-degenerate special case.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 21330042}, panicked=False

### `NormalizeInConst` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

NormalizeInConst ensures that the In operator's tuple operand is sorted with
duplicates removed (since duplicates do not change the result).

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `NormalizeInConst`, not the other rules in that file):

```
# NormalizeInConst ensures that the In operator's tuple operand is sorted with
# duplicates removed (since duplicates do not change the result).
[NormalizeInConst, Normalize]
(In | NotIn
    $left:*
    $right:(Tuple $elems:*) & (NeedSortedUniqueList $elems)
)
=>
((OpName) $left (Tuple (ConstructSortedUniqueList $elems)))
```
- Attempts used: 21
- Last updated: 2026-09-27T23:17:40.924568+00:00
- Reason / notes: before() (x IN (a,b,a,b), a 4-operand OR of EQUALS) and after() (x IN (a,b), a 2-operand OR) differ structurally by exactly the duplicate disjuncts, so the proof is non-vacuous and captures the rule's actual justification — dedup of the IN list, i.e. OR-idempotency (order/sorting is absorbed by OR-commutativity in the prover). Symbol sharing is correct: x, a, b are independent uninterpreted columns of a constraint-free scan as in reality, eqA/eqB are reused at the duplicate positions, and modeling the tuple elements as source columns is a safe over-generalization that subsumes the rule's constant-tuple case, so the proved claim transfers to real queries. The declared SCOPE is honest and specific — only the In branch with the fixed duplicated shape (a,b,a,b) is encoded, with NotIn and other arities omitted but following the same idempotency fact — making this a faithful, non-degenerate special case of NormalizeInConst. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 362667}, panicked=False

### `NormalizeJoinNotAnyFilter` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt
- Attempts used: 0
- Last updated: 2026-10-15T18:30:51.920832+00:00
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 30158208}, panicked=False

### `NormalizeNestedAnds` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

NormalizeNestedAnds ensures that And expressions are normalized into a left-
deep tree. For example, the expression:

A AND (B AND (C AND D))

would be normalized to:

And
/   \
And   D
/   \
And   C
/   \
A     B

This normalization makes conjuncts easier to traverse for other rules, such as
the ExtractRedundantConjunct rule.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `NormalizeNestedAnds`, not the other rules in that file):

```
# NormalizeNestedAnds ensures that And expressions are normalized into a left-
# deep tree. For example, the expression:
#
#   A AND (B AND (C AND D))
#
# would be normalized to:
#
#         And
#        /   \
#       And   D
#      /   \
#     And   C
#    /   \
#   A     B
#
# This normalization makes conjuncts easier to traverse for other rules, such as
# the ExtractRedundantConjunct rule.
[NormalizeNestedAnds, Normalize]
(And $left:* (And $innerLeft:* $innerRight:*))
=>
(And (ConcatLeftDeepAnds $left $innerLeft) $innerRight)
```
- Attempts used: 21
- Last updated: 2026-09-27T23:58:29.503051+00:00
- Reason / notes: The encoding is a faithful, non-vacuous base case of the rule — `before()` = `p ∧ (q ∧ r)` and `after()` = `(p ∧ q) ∧ r` are structurally distinct trees (so the proof isn't trivial), the three slots use three independent uninterpreted predicates exactly as the source pattern's three `$:*` slots require, and the source rule carries no side conditions that could be silently dropped. It is a special case rather than the full rule: the Optgen rule is a recursive left-deep flattening where any slot may itself be an `And`, which no single finite RuleScript pattern can express (the DSL has no expression-level metavariables, and the after-side shape recursively depends on the before-side's structure via `ConcatLeftDeepAnds` — a genuine DSL/prover limitation, not a missing builder, since an expression "hole" would semantically reduce to what `pred` already provides), so the honest `// SCOPE: PARTIAL` tag with a specific condition (atomic conjuncts, fixed depth-2 shape) is appropriate, and the proved claim — that the left-deep re-association preserves bag semantics for all instantiations of uninterpreted (i.e., maximally general boolean) conjuncts — is exactly the semantic content the optimizer must trust.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 348625}, panicked=False

### `NormalizeSelectAnyFilter` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt
- Attempts used: 0
- Last updated: 2026-10-15T18:30:47.452406+00:00
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 77046833}, panicked=False

### `NormalizeSelectNotAnyFilter` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt
- Attempts used: 0
- Last updated: 2026-10-15T18:30:49.706920+00:00
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 26235542}, panicked=False

### `ProjectInnerJoinValues` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

ProjectInnerJoinValues transforms an inner join with a single-row Values
operator to a Project operator. This allows decorrelation of e.g.:

SELECT (SELECT CASE WHEN ord.approved THEN 'Approved' ELSE '---' END)
FROM (VALUES (1, true), (2, false)) ord(id, approved)

Extracted from `join.opt` (which defines multiple rules — implement specifically `ProjectInnerJoinValues`, not the other rules in that file):

```
# ProjectInnerJoinValues transforms an inner join with a single-row Values
# operator to a Project operator. This allows decorrelation of e.g.:
#
#   SELECT (SELECT CASE WHEN ord.approved THEN 'Approved' ELSE '---' END)
#   FROM (VALUES (1, true), (2, false)) ord(id, approved)
#
[ProjectInnerJoinValues, Normalize]
(InnerJoin | InnerJoinApply
    $left:*
    $right:(Values) & (HasOneRow $right)
    $on:*
)
=>
(Select
    (Project
        $left
        (MakeProjectionsFromValues $right)
        (OutputCols $left)
    )
    $on
)
```
- Attempts used: 23
- Last updated: 2026-09-28T02:01:21.472026+00:00
- Reason / notes: before() (an INNER join with a one-row literal Values) and after() (an identity+constants Project followed by a Filter) are structurally distinct, and QED's proof universally quantifies over both the left table and the uninterpreted "on" predicate — including constant-true/false instantiations covering empty/true on-clauses — so the bag-equivalence is substantive, not vacuous. The modeling choices are faithful: the same "on" symbol is shared across the join condition and the final filter exactly as the source rule reuses $on, the projected constants (1,2) match the Values row contents identically on both sides as MakeProjectionsFromValues requires, the join kind is INNER, and the sole source precondition (HasOneRow on a Values) is modeled precisely. The disclosed PARTIAL narrowing (1-column left input, fixed 2-column row (1,2), correlated InnerJoinApply variant omitted) is honest and specific, and representative rather than degenerate, since the rule's correctness is independent of left/values arity and all genuinely free logical parameters remain fully uninterpreted. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 65470791}, panicked=False

### `PruneAggCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneAggCols discards aggregation columns in a GroupBy that are never used.
Note that UpsertDistinctOn is not included here because its columns are always
used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneAggCols`, not the other rules in that file):

```
# PruneAggCols discards aggregation columns in a GroupBy that are never used.
# Note that UpsertDistinctOn is not included here because its columns are always
# used.
[PruneAggCols, Normalize]
(Project
    $input:(GroupBy | ScalarGroupBy | DistinctOn
            | EnsureDistinctOn
        $innerInput:*
        $aggregations:*
        $groupingPrivate:*
    )
    $projections:*
    $passthrough:* &
        (CanPruneAggCols
            $aggregations
            $needed:(UnionCols
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    ((OpName $input)
        $innerInput
        (PruneAggCols $aggregations $needed)
        $groupingPrivate
    )
    $projections
    $passthrough
)
```
- Attempts used: 23
- Last updated: 2026-09-28T02:20:21.447984+00:00
- Reason / notes: The encoding is a faithful, non-degenerate special case of PruneAggCols: before() has Aggregate(key; f(a), g(b)) projected to (key, f), and after() has Aggregate(key; f(a)) projected to (key, f) — a genuine structural difference that captures the rule's core transformation (removing an unused aggregation from a GroupBy when the outer project doesn't reference it). The shared uninterpreted aggregate symbol `f` is correctly reused across both sides (matching the source rule's requirement that kept aggregations are unchanged), `g` appears only in before(), and the SCOPE: PARTIAL tag honestly and specifically names the restrictions (fixed 3-column scan, one group key, one retained and one pruned single-arg aggregate). QED's fast proof is consistent with this being a structural bag-semantic equivalence (dropping an unreferenced column from a group-by's output doesn't change the remaining columns' multiset), not a vacuous or coincidental result. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 81950583}, panicked=False

### `PruneExplainCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneExplainCols discards Explain input columns that are never used by its
required physical properties.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneExplainCols`, not the other rules in that file):

```
# PruneExplainCols discards Explain input columns that are never used by its
# required physical properties.
[PruneExplainCols, Normalize]
(Explain
    $input:*
    $explainPrivate:* &
        (CanPruneCols
            $input
            $needed:(NeededExplainCols $explainPrivate)
        )
)
=>
(Explain (PruneCols $input $needed) $explainPrivate)
```
- Attempts used: 22
- Last updated: 2026-09-28T02:42:01.593314+00:00
- Reason / notes: The encoding matches PruneExplainCols exactly in shape — `Explain(input)` ⟹ `Explain(PruneCols(input, needed))` with no wrapping Project (unlike the sibling Prune* rules), modeled as an invariant outer operator `Proj(E)` over the input with a projection of the needed column pushed beneath it in `after()`. Modeling Explain as the uninterpreted per-row function `E` of only the needed column faithfully captures the rule's sole validity condition (the output depends only on the needed columns, whose bag is identical before/after), and the proof is genuinely non-trivial: `before()` and `after()` are structurally different and equal only because `E` ignores the pruned column. The only restriction — a fixed 2-column input with 1 needed column and 1 output column — is forced by the DSL's lack of variable arity (not an avoidable hard-coding), is accurately and specifically disclosed in the SCOPE line, and leaves a non-degenerate, useful rewrite. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 511708}, panicked=False

### `PruneGroupByCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneGroupByCols discards GroupBy input columns that are never used. Note that
UpsertDistinctOn is not included here because its columns are always used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneGroupByCols`, not the other rules in that file):

```
# PruneGroupByCols discards GroupBy input columns that are never used. Note that
# UpsertDistinctOn is not included here because its columns are always used.
[PruneGroupByCols, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn | EnsureDistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* &
        (CanPruneCols
            $input
            $needed:(UnionCols
                (AggregationOuterCols $aggregations)
                (NeededGroupingCols $groupingPrivate)
            )
        )
)
=>
((OpName)
    (PruneCols $input $needed)
    $aggregations
    (PruneOrderingGroupBy $groupingPrivate $needed)
)
```
- Attempts used: 23
- Last updated: 2026-09-28T02:47:19.084667+00:00
- Reason / notes: It is a nontrivial partial instance of PruneGroupByCols: the unused fourth input column is removed before the same uninterpreted GROUP BY/aggregates, while the group key and aggregate arguments are preserved. The added all-column projection is an identity context, and the PARTIAL line honestly states the fixed arity/aggregate assumptions.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 100798333}, panicked=False

### `PruneInsertReturnCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneInsertReturnCols removes columns from the Insert operator's ReturnCols
set if they are not used in the RETURNING clause of the mutation.
Removing ReturnCols will then allow the PruneMutationFetchCols to be more
conservative with the fetch columns.
TODO(msirek): Mutations shouldn't need to return the primary key
columns. Investigate appropriate changes to SQL execution to accommodate this,
through #111733.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneInsertReturnCols`, not the other rules in that file):

```
# PruneInsertReturnCols removes columns from the Insert operator's ReturnCols
# set if they are not used in the RETURNING clause of the mutation.
# Removing ReturnCols will then allow the PruneMutationFetchCols to be more
# conservative with the fetch columns.
# TODO(msirek): Mutations shouldn't need to return the primary key
# columns. Investigate appropriate changes to SQL execution to accommodate this,
# through #111733.
[PruneInsertReturnCols, Normalize]
(Project
    $input:(Insert
        $innerInput:*
        $uniqueChecks:*
        $fastPathUniqueChecks:*
        $fkChecks:*
        $mutationPrivate:*
    )
    $projections:*
    $passthrough:* &
        (CanPruneMutationReturnCols
            $mutationPrivate
            $needed:(UnionCols3
                (PrimaryKeyCols (MutationTable $mutationPrivate))
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    ((OpName $input)
        $innerInput
        $uniqueChecks
        $fastPathUniqueChecks
        $fkChecks
        (PruneMutationReturnCols $mutationPrivate $needed)
    )
    $projections
    $passthrough
)
```
- Attempts used: 23
- Last updated: 2026-10-15T19:58:45.476251+00:00
- Reason / notes: The encoding faithfully captures the essential bag-semantic equivalence of `PruneInsertReturnCols` — removing a return column that the outer RETURNING projection never reads preserves the result — by modeling the insert's returned columns as uninterpreted functions of its input and dropping the unread column from the child, while keeping the key (matching the rule's `PrimaryKeyCols` requirement) and leaving the outer projection unchanged. `before()` and `after()` are structurally distinct (a 3-column vs 2-column child project feeding an identical outer `Proj`), so the proof is non-vacuous rather than a trivial `before()==after()`. The `SCOPE: PARTIAL` tag is present, accurate, and names the genuine, specific restrictions (one-column input = the passed-through PK, exactly two returned columns, the pruned one being the unread trailing column), so this is a real, non-degenerate special case rather than an over-constrained or mis-structured encoding.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 344000}, panicked=False

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

### `PruneJoinRightCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneJoinRightCols discards columns on the right side of a join that are never
used. AddDerivedOnClauseConditionsFromFKContraints builds equijoin predicates
which might be added during optimization, if any, to ensure those columns are
not pruned away.

The PruneCols property should prevent this rule (which pushes Project below
Join) from cycling with the TryDecorrelateProject rule (which pushes Join
below Project).

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneJoinRightCols`, not the other rules in that file):

```
# PruneJoinRightCols discards columns on the right side of a join that are never
# used. AddDerivedOnClauseConditionsFromFKContraints builds equijoin predicates
# which might be added during optimization, if any, to ensure those columns are
# not pruned away.
#
# The PruneCols property should prevent this rule (which pushes Project below
# Join) from cycling with the TryDecorrelateProject rule (which pushes Join
# below Project).
[PruneJoinRightCols, Normalize]
(Project
    $input:(Join $left:* $right:* $on:* $private:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $right
            $needed:(UnionCols3
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
        $left
        (PruneCols $right $needed)
        $on
        $private
    )
    $projections
    $passthrough
)
```
- Attempts used: 4
- Last updated: 2026-09-28T02:59:45.932669+00:00
- Reason / notes: The encoding faithfully captures the core PruneJoinRightCols transformation: before() is `Project(G(L0,R0), InnerJoin(C(L0,R0), L, R(2col)))` and after() is `Project(G(L0,R0), InnerJoin(C(L0,R0), L, Project(R0,R)))` — structurally different plans where the unneeded right-side column R1 is eliminated. The uninterpreted symbols C and G are correctly shared, the field indices resolve properly to the join's flat column layout, and the SCOPE line honestly and specifically states the genuine restrictions (INNER join only, fixed 1+2 column shapes) rather than claiming full generality.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 68651291}, panicked=False

### `PruneMutationFetchCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneMutationFetchCols removes columns from the mutation operator's FetchCols
set if they are never used. Removing FetchCols can in turn can trigger the
PruneMutationInputCols rule, which can prune any input columns which are now
unreferenced.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneMutationFetchCols`, not the other rules in that file):

```
# PruneMutationFetchCols removes columns from the mutation operator's FetchCols
# set if they are never used. Removing FetchCols can in turn can trigger the
# PruneMutationInputCols rule, which can prune any input columns which are now
# unreferenced.
[PruneMutationFetchCols, Normalize]
(Update | Upsert | Delete
    $input:*
    $uniqueChecks:*
    $fkChecks:*
    $mutationPrivate:* &
        (CanPruneMutationFetchCols
            $mutationPrivate
            $needed:(NeededMutationFetchCols
                (OpName)
                $mutationPrivate
            )
        )
)
=>
((OpName)
    $input
    $uniqueChecks
    $fkChecks
    (PruneMutationFetchCols $mutationPrivate $needed)
)
```
- Attempts used: 5
- Last updated: 2026-10-15T20:09:16.349540+00:00
- Reason / notes: before() (scan(I0,I1) → M(I0)) and after() (scan(I0,I1) → project(I0) → M(I0')) are structurally different and correctly share the M operator and input scan, so the proof covers the rule's genuine crux — a mutation whose output depends only on the needed FetchCols is unchanged when unneeded fetched columns are dropped — with that precondition faithfully encoded by construction (M takes only the needed column), the only provable form given QED's inability to reason about entailment between independent symbols. The exactly-two-columns/one-needed restriction is an honest, specific, minimal non-degenerate special case (declared in the SCOPE line), and the unchanged other children (unique checks, fk checks, fixed private data) are soundly absorbed into the held-fixed uninterpreted M, matching the source rewrite which alters only the FetchCols.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 372458}, panicked=False

### `PruneMutationInputCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneMutationInputCols discards input columns that are never used by the
mutation operator. This is high priority so that it runs before
UseSwapMutation, UseSwapMutationWithProjection, and
UseSwapMutationWithProjectionProjection.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneMutationInputCols`, not the other rules in that file):

```
# PruneMutationInputCols discards input columns that are never used by the
# mutation operator. This is high priority so that it runs before
# UseSwapMutation, UseSwapMutationWithProjection, and
# UseSwapMutationWithProjectionProjection.
[PruneMutationInputCols, Normalize, HighPriority]
(Update | Upsert | Delete
    $input:*
    $uniqueChecks:*
    $fkChecks:*
    $mutationPrivate:* &
        (CanPruneCols
            $input
            $needed:(NeededMutationCols
                $mutationPrivate
                $uniqueChecks
                $fkChecks
            )
        )
)
=>
((OpName)
    (PruneCols $input $needed)
    $uniqueChecks
    $fkChecks
    $mutationPrivate
)
```
- Attempts used: 21
- Last updated: 2026-09-28T03:32:46.859069+00:00
- Reason / notes: The encoding captures the rule's genuine soundness content — pushing a column-pruning projection below a per-row operator whose behavior depends only on the kept columns — by modeling the mutation as an uninterpreted single-column projection of exactly the needed input column, with before() = π_M(c0)(I) and after() = π_M(x0)(π_c0(I)), which are structurally distinct (a real pushdown, not an identity) and correctly share the input scan `I` and the single mutation symbol `M`. The source rule's `CanPruneCols`/`NeededMutationCols` machinery is backend meta-level (applicability/hygiene and column-set bookkeeping, not a semantic precondition), and the Update/Upsert/Delete family is properly abstracted to one uninterpreted per-row operator, with the fixed mutation data soundly absorbed into `M`. The only real restriction — fixing 2 input columns with only the first needed — is forced by the DSL (uninterpreted ops have fixed argument positions; it cannot quantify over column sets), is honestly and specifically declared in the SCOPE line, and loses no proof content, since any other (n, S) instance states the identical per-row commutation claim.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 465792}, panicked=False

### `PruneMutationReturnCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneReturningCols removes columns from the mutation operator's ReturnCols
set if they are not used in the RETURNING clause of the mutation.
Removing ReturnCols will then allow the PruneMutationFetchCols to be more
conservative with the fetch columns.
TODO(msirek): Mutations shouldn't need to return the primary key
columns. Investigate appropriate changes to SQL execution to accommodate this,
through #111733.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneMutationReturnCols`, not the other rules in that file):

```
# PruneReturningCols removes columns from the mutation operator's ReturnCols
# set if they are not used in the RETURNING clause of the mutation.
# Removing ReturnCols will then allow the PruneMutationFetchCols to be more
# conservative with the fetch columns.
# TODO(msirek): Mutations shouldn't need to return the primary key
# columns. Investigate appropriate changes to SQL execution to accommodate this,
# through #111733.
[PruneMutationReturnCols, Normalize]
(Project
    $input:(Update | Upsert | Delete
        $innerInput:*
        $uniqueChecks:*
        $fkChecks:*
        $mutationPrivate:*
    )
    $projections:*
    $passthrough:* &
        (CanPruneMutationReturnCols
            $mutationPrivate
            $needed:(UnionCols3
                (PrimaryKeyCols (MutationTable $mutationPrivate))
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    ((OpName $input)
        $innerInput
        $uniqueChecks
        $fkChecks
        (PruneMutationReturnCols $mutationPrivate $needed)
    )
    $projections
    $passthrough
)
```
- Attempts used: 46
- Last updated: 2026-10-15T20:50:26.481925+00:00
- Reason / notes: The encoding is structurally non-trivial (4-wide vs 3-wide mutation output with correct ordinal remapping of the shared passthrough column r3, field 3→field 2) and faithfully models the rule's distinctive content: an uninterpreted 1:1 row transformation standing in for Update/Upsert/Delete, one non-PK return column pruned, and one unused PK return column deliberately retained — matching the source rule's `PrimaryKeyCols ∪ ProjectionOuterCols ∪ passthrough` needed-set, so this is this specific rule rather than generic column pruning. The fixed 4-column arities and specific used/unused/PK pattern make it a genuine narrower instance, but that is a real DSL limitation (RuleScript records are concrete shapes with no arity generalization, and MetaAssignment only covers join types), and the scope line honestly states exactly that assumption with a specific condition, so the provable result is a faithful, non-degenerate special case. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 348292}, panicked=False

### `PruneOrdinalityCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneOrdinalityCols discards Ordinality input columns that are never used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneOrdinalityCols`, not the other rules in that file):

```
# PruneOrdinalityCols discards Ordinality input columns that are never used.
[PruneOrdinalityCols, Normalize]
(Project
    (Ordinality $input:* $ordinalityPrivate:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols3
                (NeededOrdinalityCols $ordinalityPrivate)
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    (Ordinality
        (PruneCols $input $needed)
        (PruneOrderingOrdinality $ordinalityPrivate $needed)
    )
    $projections
    $passthrough
)
```
- Attempts used: 26
- Last updated: 2026-09-28T04:14:51.631838+00:00
- Reason / notes: The encoding matches the real rule's shape (outer Project over an Ordinality-derived column, with a column drop pushed beneath the Ordinality) and the symbol sharing is correct: the shared O encodes the rule's own firing precondition that the ordinality derivation references no pruned column, and the shared G encodes the unchanged outer projection, which consumes O so the proof is non-vacuous (QED genuinely verifies that O's values survive the drop and the field-index shift from ordinal 3 to 2 is handled). The remaining narrowness — 3 input columns / exactly 1 pruned, and O abstracted as a value-function of retained columns rather than a true row-position function — stems from QED's fundamental inability to model row position/ordering semantics, not from a fixable encoding choice, and is honestly disclosed in the one-sentence SCOPE line, making this a genuine, non-degenerate special case of the rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 334875}, panicked=False

### `PruneProjectCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneProjectCols discards columns from a nested project which are not used by
the outer project.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneProjectCols`, not the other rules in that file):

```
# PruneProjectCols discards columns from a nested project which are not used by
# the outer project.
[PruneProjectCols, Normalize]
(Project
    $project:(Project)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $project
            $needed:(UnionCols
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project (PruneCols $project $needed) $projections $passthrough)
```
- Attempts used: 22
- Last updated: 2026-09-28T04:00:29.568475+00:00
- Reason / notes: The encoding is a faithful, non-vacuous special case of PruneProjectCols — `before()` is a Scan → inner-Project(3 cols: `col0`, `Used(col0)`, `Unused(col0)`) → outer-Project(`Top(inner.col0, inner.col1)`), while `after()` is the equivalent Scan → Project(`Top(col0, Used(col0))`), so the proof genuinely captures "drop the nested project's never-referenced column" (plus the consequent merge of the now-pure inner project) rather than a structurally identical triviality. Symbols are shared correctly (the same `top` operator and the same `passthrough`/`usedSyn` expressions appear on both sides, while `unusedSyn` appears only in `before()`), and the omitted `CanPruneCols` is an optimizer bookkeeping guard, not a semantic precondition, so its absence does not change the claim; the `PARTIAL` tag honestly and specifically names the three-column/one-unused-column restriction, and QED proves the equivalence for all instantiations of the uninterpreted table and functions.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 460708}, panicked=False

### `PruneProjectSetCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneProjectSetCols discards ProjectSet columns that are never used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneProjectSetCols`, not the other rules in that file):

```
# PruneProjectSetCols discards ProjectSet columns that are never used.
[PruneProjectSetCols, Normalize]
(Project
    $input:(ProjectSet $innerInput:* $zip:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols3
                (ZipOuterCols $zip)
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    (ProjectSet (PruneCols $innerInput $needed) $zip)
    $projections
    $passthrough
)
```
- Attempts used: 45
- Last updated: 2026-09-28T04:25:01.890941+00:00
- Reason / notes: The encoding faithfully models ProjectSet's row-generating semantics as an INNER join of the input with an uninterpreted emitted-row relation E under an uninterpreted membership predicate M, keeping both M and the outer projection G uninterpreted (not hard-coded) and correctly enforcing the rule's core precondition by ensuring M never references the pruned column L1; before() (L with two columns) and after() (L projected to one column) are structurally distinct and the bag-multiplicity argument holds (each (L0,L1) pair maps to one L0 row, preserving join multiplicities), so the proof is non-vacuous, and the scope is honestly tagged PARTIAL with a specific condition.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 104714666}, panicked=False

### `PruneScanCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneScanCols discards Scan operator columns that are never used. The needed
columns are pushed down into the Scan's opt.ScanOpDef private.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneScanCols`, not the other rules in that file):

```
# PruneScanCols discards Scan operator columns that are never used. The needed
# columns are pushed down into the Scan's opt.ScanOpDef private.
[PruneScanCols, Normalize]
(Project
    $input:(Scan)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project (PruneCols $input $needed) $projections $passthrough)
```
- Attempts used: 22
- Last updated: 2026-09-28T04:20:30.028885+00:00
- Reason / notes: The encoding faithfully captures the semantic core of PruneScanCols — inserting a column-pruning projection below an outer projection that doesn't reference all scan columns — with a non-vacuous before/after difference (2-operator vs. 3-operator plans). The uninterpreted Top operator correctly stands in for an arbitrary projection expression over the needed columns, and the shared source scan plus correct field-index remapping through the pruned relation are handled properly. The PARTIAL scope is honestly declared and reflects a genuine DSL limitation (no way to parametrize over column count or the dynamically-computed $needed set), while the chosen 3-column/1-unused instance is the minimal non-degenerate case that exercises the rule's actual transformation. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 107291}, panicked=False

### `PruneSelectCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneSelectCols discards Select input columns that are never used.

The PruneCols property should prevent this rule (which pushes Project below
Select) from cycling with the PushSelectIntoProject rule (which pushes Select
below Project).

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneSelectCols`, not the other rules in that file):

```
# PruneSelectCols discards Select input columns that are never used.
#
# The PruneCols property should prevent this rule (which pushes Project below
# Select) from cycling with the PushSelectIntoProject rule (which pushes Select
# below Project).
[PruneSelectCols, Normalize]
(Project
    (Select $input:* $filters:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols3
                (FilterOuterCols $filters)
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    (Select (PruneCols $input $needed) $filters)
    $projections
    $passthrough
)
```
- Attempts used: 23
- Last updated: 2026-09-28T04:28:09.546845+00:00
- Reason / notes: The encoding is non-vacuous and faithful: `before()` = π_Top(σ_F(S₃)) and `after()` = π_Top(σ_F(π_{0,1}(S₃))) are structurally distinct trees, and the provable claim is a genuine universal bag-semantic identity (the rewrite preserves multiplicity row-for-row since F and Top ignore the pruned column). The uninterpreted symbols F and Top are correctly shared between both sides with exact column correspondence (columns 0,1 of the 3-column input under the pruning projection), and by constructing them over only columns 0/1 the encoding captures the source rule's `CanPruneCols` precondition — the pruned column is unreferenced — by construction rather than silently dropping it. The plan shape matches `PruneSelectCols` (outer Project and Select unchanged, new Project inserted between Select and its input, with a Scan input satisfying the "projection can merge" condition), and the disclosed PARTIAL scope — a fixed three-column scan whose filter and projection reference exactly columns 0,1 — is a specific, non-degenerate special case that is essentially forced, since QED cannot quantify over which columns an uninterpreted symbol happens to reference.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 65426291}, panicked=False

### `PruneSemiAntiJoinRightCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneSemiAntiJoinRightCols discards columns on the right side of a
Semi or Anti join that are never used. This is similar to PruneJoinRightCols.
PruneJoinRightCols normally prunes the RHS of a join but it can't do that
in the case of Semi/Anti joins because the projection is eliminated after
the LHS is pruned. This rule doesn't require a projection over the Semi/Anti
join in order to prune the RHS.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneSemiAntiJoinRightCols`, not the other rules in that file):

```
# PruneSemiAntiJoinRightCols discards columns on the right side of a
# Semi or Anti join that are never used. This is similar to PruneJoinRightCols.
# PruneJoinRightCols normally prunes the RHS of a join but it can't do that
# in the case of Semi/Anti joins because the projection is eliminated after
# the LHS is pruned. This rule doesn't require a projection over the Semi/Anti
# join in order to prune the RHS.
[PruneSemiAntiJoinRightCols, Normalize]
(SemiJoin | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:*
    $on:*
    $private:* &
        (CanPruneCols $right $needed:(FilterOuterCols $on))
)
=>
((OpName) $left (PruneCols $right $needed) $on $private)
```
- Attempts used: 24
- Last updated: 2026-09-28T05:16:14.681756+00:00
- Reason / notes: The encoding is a genuine, non-trivial partial instance of PruneSemiAntiJoinRightCols: before() is an ANTI join over a 2-column right side where only col0 appears in the uninterpreted predicate, and after() projects the right to just col0 — the structural difference is real (right arity changes from 2 to 1), the predicate symbol is correctly shared, and ANTI is one of the four join kinds the source rule covers. The SCOPE line honestly and specifically documents the restrictions (only ANTI, fixed 1-col left / 2-col right, uncorrelated), and the proof confirms the core semantic principle (right-side columns not referenced by the join condition can be eliminated in a semi/anti join) is universally valid.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 72349667}, panicked=False

### `PruneUnionAllCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneUnionAllCols prunes columns from the left and right input relations that
are never used. Since UNION ALL preserves duplicates, any column may be pruned
if it is not needed, which is not generally true of set operators.

Since UnionAll requires that both inputs have an equal number of columns,
rather than using PruneCols to prune the left and right sides, this rule
pushes down Projects on both sides to ensure that exactly the needed columns
are passed as input to the UnionAll, to prevent situations where one side has
more columns left over after PruneCols than the other (for instance, if $left
is a normal scan where all columns may be pruned, but $right is a scan with a
filter, leading to an additional column being kept on just the right side).
If extraneous, these Projects may be cleaned up later by rules like
EliminateProject.

Note: The projections could reference columns from an outer scope, e.g. due
to an apply-join or routine. We intersect with the UnionAll's output to ensure
that $needed only contains columns from the UnionAll.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneUnionAllCols`, not the other rules in that file):

```
# PruneUnionAllCols prunes columns from the left and right input relations that
# are never used. Since UNION ALL preserves duplicates, any column may be pruned
# if it is not needed, which is not generally true of set operators.
#
# Since UnionAll requires that both inputs have an equal number of columns,
# rather than using PruneCols to prune the left and right sides, this rule
# pushes down Projects on both sides to ensure that exactly the needed columns
# are passed as input to the UnionAll, to prevent situations where one side has
# more columns left over after PruneCols than the other (for instance, if $left
# is a normal scan where all columns may be pruned, but $right is a scan with a
# filter, leading to an additional column being kept on just the right side).
# If extraneous, these Projects may be cleaned up later by rules like
# EliminateProject.
#
# Note: The projections could reference columns from an outer scope, e.g. due
# to an apply-join or routine. We intersect with the UnionAll's output to ensure
# that $needed only contains columns from the UnionAll.
[PruneUnionAllCols, Normalize]
(Project
    $union:(UnionAll $left:* $right:* $colmap:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $union
            $needed:(IntersectionCols
                (UnionCols
                    (ProjectionOuterCols $projections)
                    $passthrough
                )
                (OutputCols $union)
            )
        )
)
=>
(Project
    (UnionAll
        (Project $left [] (NeededColMapLeft $needed $colmap))
        (Project $right [] (NeededColMapRight $needed $colmap))
        (PruneSetPrivate $needed $colmap)
    )
    $projections
    $passthrough
)
```
- Attempts used: 22
- Last updated: 2026-09-28T04:39:41.194944+00:00
- Reason / notes: The encoding matches the source rule's exact shape — a Project over a UnionAll (correctly `all=true`) rewritten into the same outer projection over a UnionAll whose two inputs each get a lock-step Project selecting exactly the needed columns — which is precisely the equal-width pruning that PruneUnionAllCols requires (its comment explains why per-side `PruneCols` is replaced by paired input Projects). Symbol handling is correct and the proof is non-vacuous: L and R are independent scans, C0/C1/C2 are shared for union schema congruence, G is shared between before/after with field indices correctly remapped (0,2 → 0,1 after the inputs narrow), and before() is genuinely structurally different from after(), so QED verified the real law that the unreferenced column C1 can be dropped from both inputs simultaneously. The fixed 3-column width with needed set {0,2} is an essential specialization rather than an avoidable one — the needed subset is inherently structural in a projection pattern and cannot be a free uninterpreted symbol — and it is honestly tagged PARTIAL; the only blemish is that the SCOPE sentence calls column 2 a "passthrough" when the code actually feeds it as an argument of G (one output column), but the stated restriction still accurately describes the code, so the verdict remains a faithful, useful special case. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 78141666}, panicked=False

### `PruneValuesCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneValuesCols discards Values columns that are never used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneValuesCols`, not the other rules in that file):

```
# PruneValuesCols discards Values columns that are never used.
[PruneValuesCols, Normalize]
(Project
    $input:(Values)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project (PruneCols $input $needed) $projections $passthrough)
```
- Attempts used: 23
- Last updated: 2026-09-28T04:47:27.854020+00:00
- Reason / notes: The encoding reproduces the source rule's genuine shape — an outer Project (uninterpreted `Top` on one column, passthrough on the other) over a Values whose unreferenced third column is removed — and it is not vacuous: the after-side constant (1,2) must be exactly the projection of (1,2,3), a link SMT genuinely checks (a wrong pruned tuple would refute it), and `before()`/`after()` are structurally different plans. The narrowing to a concrete single-row 3→2-column literal Values is forced by QED's semantics rather than a missed uninterpreted symbol: Values contents are always concrete constants in the QED model (there is no symbolic-values symbol to quantify over, and substituting an independent uninterpreted scan on the after side would leave the constant-to-constant link inexpressible and would instead prove a different, project-pushing rewrite with the wrong operator shape). The `// SCOPE: PARTIAL` line accurately and specifically names the restriction (fixed single row, fixed 3-column→2-column pruning, literal content), the `CanPruneCols` guard is a rule-firing condition rather than a soundness assumption, and the result remains a non-degenerate proof of the rule's core transformation, so the PROVABLE verdict is meaningful and not misleading. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 54671833}, panicked=False

### `PruneWindowInputCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneWindowInputCols discards window passthrough columns which are never used.
NB: This rule should go after PruneWindowOutputCols, or else this rule can get
into a cycle.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneWindowInputCols`, not the other rules in that file):

```
# PruneWindowInputCols discards window passthrough columns which are never used.
# NB: This rule should go after PruneWindowOutputCols, or else this rule can get
# into a cycle.
[PruneWindowInputCols, Normalize]
(Project
    $input:(Window $innerInput:* $fn:* $private:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols3
                (NeededWindowCols $fn $private)
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    (Window (PruneCols $innerInput $needed) $fn $private)
    $projections
    $passthrough
)
```
- Attempts used: 44
- Last updated: 2026-09-28T05:23:14.036342+00:00
- Reason / notes: The encoding faithfully captures the logical content of PruneWindowInputCols for per-partition window aggregates: before() has the window's input as (k,v,x) joined back to the per-partition aggregate on k, while after() has it as (k,v) joined back to the same aggregate — the only difference is the unused column x, and the proof is non-vacuous because the SMT solver must verify bag-equivalence of the outer Top(k,v,w) projection across two structurally different join inputs. The restriction to per-partition constant window functions (modeled as an uninterpreted aggregate grouped by the partition key, joined back via INNER equi-join) is a genuine QED/DSL limitation (no Window operator exists in the serializer), is honestly stated in the SCOPE line, and still yields a useful, non-degenerate result covering common windows like SUM/COUNT/MIN/MAX OVER (PARTITION BY k). Symbol sharing is correct (same Top, same perPart, correct joinField ordinals in the concatenated field space), the equi-join on the partition key is appropriately concrete rather than uninterpreted (it is part of the window's bag semantics, not a variable aspect of the rule), and the NeededWindowCols precondition is correctly encoded by construction since the aggregate references only k and v. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 116643000}, panicked=False

### `PruneWindowOutputCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneWindowOutputCols eliminates unused window functions from a Window
expression.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneWindowOutputCols`, not the other rules in that file):

```
# PruneWindowOutputCols eliminates unused window functions from a Window
# expression.
[PruneWindowOutputCols, Normalize]
(Project
    (Window $input:* $windows:* $private:*)
    $projections:*
    $passthrough:* &
        (CanPruneWindows
            $needed:(UnionCols
                (ProjectionOuterCols $projections)
                $passthrough
            )
            $windows
        )
)
=>
(Project
    (Window $input (PruneWindows $needed $windows) $private)
    $projections
    $passthrough
)
```
- Attempts used: 28
- Last updated: 2026-09-28T05:20:52.409015+00:00
- Reason / notes: `before()` truly differs from `after()` only by computing an extra unused window function `w2` (two aggregate calls vs one in the per-partition window, then dropped by the identical outer projection), which is exactly the pruning content of `PruneWindowOutputCols`, and the `input ⋈ (grouped-by-k aggregate)` model preserves one-output-row-per-input-row cardinality, so the proof is non-vacuous and the reused `cond`/field indices resolve correctly in both plans; the fixed two-column input and exactly-two-functions (one kept, one pruned) shape is a genuine, honestly-declared consequence of QED having no bag semantics for `Window` (no frame/ordering), so the PARTIAL scope tag is accurate and the result is a faithful, non-degenerate special case rather than a vacuous or over-constrained one.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 87225791}, panicked=False

### `PruneWithCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneWithCols pushes a Project operator beneath a With. It's ok to
unconditionally push this Project down, since in the pruning case, we're
getting the project closer to the source of any prune requests, and if we
just end up with a Project incidentally, it's safe to just always push it
down.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneWithCols`, not the other rules in that file):

```
# PruneWithCols pushes a Project operator beneath a With. It's ok to
# unconditionally push this Project down, since in the pruning case, we're
# getting the project closer to the source of any prune requests, and if we
# just end up with a Project incidentally, it's safe to just always push it
# down.
[PruneWithCols, Normalize]
(Project
    (With $binding:* $input:* $private:*)
    $projections:*
    $passthrough:*
)
=>
(With
    $binding
    (Project $input $projections $passthrough)
    $private
)
```
- Attempts used: 21
- Last updated: 2026-10-15T20:29:56.495356+00:00
- Reason / notes: The encoding faithfully mirrors PruneWithCols's shape — before() is Project([Expr(c0), c0]) above the 1-column identity standing in for the pass-through With, after() is the 2-column identity (same With) above the same project pushed below the scan — and the trees are structurally different (Expr outer vs. inner), so the proof is not vacuous. The uninterpreted Expr and scan symbols are correctly shared, the identity modeling of With matches the source rule's own justification, and the PARTIAL scope line accurately and specifically states the 1-column / 1-expression / 1-passthrough restriction, which is a genuine non-degenerate special case limited by the DSL's inability to express arbitrary-length sequences of distinct uninterpreted projections.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 314792}, panicked=False

### `PruneWithScanCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneWithScanCols discards columns scanned from the WithScan that are never
used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneWithScanCols`, not the other rules in that file):

```
# PruneWithScanCols discards columns scanned from the WithScan that are never
# used.
[PruneWithScanCols, Normalize]
(Project
    $input:(WithScan)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project (PruneCols $input $needed) $projections $passthrough)
```
- Attempts used: 24
- Last updated: 2026-09-28T05:34:18.433678+00:00
- Reason / notes: The encoding faithfully captures PruneWithScanCols: a shared outer Project built from one uninterpreted projection symbol `Top(w0, w1)` over a shared three-column WithScan scan, where `before()` scans all three columns and `after()` applies the same unmodified Project over the scan with the never-referenced third column dropped (the pruned WithScan is correctly modeled as a two-column projection of the full scan, the only way to express a column-pruned scan in the fixed-schema DSL). `before()` and `after()` are structurally distinct (three-column vs. two-column intermediate), so the proof is a genuine, non-vacuous verification that removing the unreferenced column preserves bag semantics for every scan bag and every instantiation of the uninterpreted symbols, with correct symbol sharing (same `Top`, same source) and no missing preconditions for this instance. The `SCOPE: PARTIAL` tag is honest and specific — fixed arity (3 columns), a single uninterpreted projection expression, and no passthrough columns are genuine restrictions forced by the DSL's fixed-schema scans, not vague or degenerate narrowing, so the result remains a useful, non-trivial special case of the rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 93750}, panicked=False

### `PushAggDistinctIntoGroupBy` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

PushAggDistinctIntoGroupBy pushes an aggregate function DISTINCT modifier into
the input of a GroupBy or ScalarGroupBy operator. This allows the optimizer to
take advantage of an index on the column(s) subject to the DISTINCT operation.
PushAggDistinctIntoGroupBy can match any single aggregate function, including
those that have multiple input arguments.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `PushAggDistinctIntoGroupBy`, not the other rules in that file):

```
# PushAggDistinctIntoGroupBy pushes an aggregate function DISTINCT modifier into
# the input of a GroupBy or ScalarGroupBy operator. This allows the optimizer to
# take advantage of an index on the column(s) subject to the DISTINCT operation.
# PushAggDistinctIntoGroupBy can match any single aggregate function, including
# those that have multiple input arguments.
[PushAggDistinctIntoGroupBy, Normalize]
(GroupBy | ScalarGroupBy
    $input:*
    $aggregations:[
        $item:(AggregationsItem (AggDistinct $agg:*) $aggColID:*)
    ]
    $groupingPrivate:*
)
=>
((OpName)
    (DistinctOn
        $input
        (MakeAggCols
            FirstAgg
            (OrderingCols
                (ExtractGroupingOrdering $groupingPrivate)
            )
        )
        (MakeGrouping
            (UnionCols
                (GroupingCols $groupingPrivate)
                (ExtractAggInputColumns $agg)
            )
            (EmptyOrdering)
        )
    )
    [ (AggregationsItem $agg $aggColID) ]
    $groupingPrivate
)
```
- Attempts used: 31
- Last updated: 2026-09-28T06:13:38.886729+00:00
- Reason / notes: The encoding is a genuine, non-vacuous instance of the source rule: before() is a GroupBy whose aggregation list is exactly one DISTINCT aggregate (matching the source rule's own single-item `$aggregations` match condition, which the `...`-free pattern in Optgen syntax enforces), and after() is the rule's rewrite — a no-aggregate group-by acting as DistinctOn over grouping cols ∪ distinct-arg cols, feeding the same `f` without the distinct flag over the same grouping keys. Symbol sharing is exactly right (f, k1, k2, x shared across both sides as the rule requires, with no accidental over-constraint), and there are no preconditions to miss because the source rule carries no side-conditions; the proof can only go through if the prover folds the DISTINCT flag into input deduplication and the inner group-by into key deduplication, which is precisely the bag-structural content of the rewrite. The PARTIAL scope line is honest and specific — the real restrictions (single-argument distinct call, two non-empty grouping columns, and no grouping ordering, which the DSL doesn't model at all) are stated, while "exactly one aggregation / no non-distinct aggregations" is the source rule's own match requirement rather than a hidden assumption — so the narrowing is a useful, non-degenerate special case. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 93598292}, panicked=False

### `PushAssignmentCastsIntoValues` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/project.opt

PushAssignmentCastsIntoValues pushes assignment cast projections into Values
rows.

Example:

project
├── columns: x:2 y:3
├── values
│    ├── columns: column1:1
│    ├── cardinality: [2 - 2]
│    ├── (1,)
│    └── (2,)
└── projections
├── assignment-cast: STRING [as=x:2]
│    └── column1:1
└── 'foo' [as=y:3]
=>
project
├── columns: x:2 y:3
├── values
│    ├── columns: x:2
│    ├── cardinality: [2 - 2]
│    ├── tuple
│    │    └── assignment-cast: STRING
│    │        └── 1
│    └── tuple
│         └── assignment-cast: STRING
│             └── 2
└── projections
└── 'foo' [as=y:3]

This allows other rules to fire, with the ultimate goal of eliminating the
project so that the insert fast-path optimization is used in more cases and
uniqueness checks for gen_random_uuid() values are eliminated in more cases.

Assignment casts in projections cannot be pushed into values expressions if
the casted column is referenced in another projection expression
(AssignmentCastCols ensures this) or if the casted column also a passthrough
column (notice the DifferenceCols function).

Extracted from `project.opt` (which defines multiple rules — implement specifically `PushAssignmentCastsIntoValues`, not the other rules in that file):

```
# PushAssignmentCastsIntoValues pushes assignment cast projections into Values
# rows.
#
# Example:
#
# project
#  ├── columns: x:2 y:3
#  ├── values
#  │    ├── columns: column1:1
#  │    ├── cardinality: [2 - 2]
#  │    ├── (1,)
#  │    └── (2,)
#  └── projections
#       ├── assignment-cast: STRING [as=x:2]
#       │    └── column1:1
#       └── 'foo' [as=y:3]
# =>
# project
#  ├── columns: x:2 y:3
#  ├── values
#  │    ├── columns: x:2
#  │    ├── cardinality: [2 - 2]
#  │    ├── tuple
#  │    │    └── assignment-cast: STRING
#  │    │        └── 1
#  │    └── tuple
#  │         └── assignment-cast: STRING
#  │             └── 2
#  └── projections
#       └── 'foo' [as=y:3]
#
# This allows other rules to fire, with the ultimate goal of eliminating the
# project so that the insert fast-path optimization is used in more cases and
# uniqueness checks for gen_random_uuid() values are eliminated in more cases.
#
# Assignment casts in projections cannot be pushed into values expressions if
# the casted column is referenced in another projection expression
# (AssignmentCastCols ensures this) or if the casted column also a passthrough
# column (notice the DifferenceCols function).
[PushAssignmentCastsIntoValues, Normalize]
(Project
    $input:(Values)
    $projections:*
    $passthrough:* &
        ^(ColsAreEmpty
            $castCols:(IntersectionCols
                (DifferenceCols
                    (AssignmentCastCols $projections)
                    $passthrough
                )
                (OutputCols $input)
            )
        )
)
=>
(PushAssignmentCastsIntoValues
    $input
    $projections
    $passthrough
    $castCols
)
```
- Attempts used: 104
- Last updated: 2026-10-16T04:25:15.113740+00:00
- Reason / notes: The encoding faithfully mirrors the rule's structure in a non-degenerate special case: an uninterpreted single-column Values, one uninterpreted cast projection moved from the outer project into the base relation (modeled as an inner projection layer, since the DSL has no expression-row Values), and one independent constant projection carried through both sides with matching output column order — and before() and after() are structurally distinct, so the proof establishes a genuine projection-commutation/bag-preservation claim rather than a vacuous identity. The source rule's guards (cast source not referenced by other projections, not a passthrough column, at least one pushable cast) are enforced by the encoding's shape, so no precondition is silently dropped, and the PARTIAL scope line honestly names the 1-column/constant-other restriction.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 382667}, panicked=False

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

### `PushFilterIntoJoinLeftAndRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

PushFilterIntoJoinLeftAndRight pushes a filter into both the left and right
sides of an InnerJoin or SemiJoin if it can be mapped to use the columns of
both sides. For example, consider this query:

SELECT * FROM a JOIN b ON a.x=b.x AND a.y=b.y AND a.x + b.y < 5

In this case, we can map a.x + b.y < 5 to both sides based on the equality
filters a.x=b.x AND a.y=b.y. For the left side, we can map it to
a.x + a.y < 5, and for the right side, we can map it to b.x + b.y < 5.
Given this mapping, we can safely push the filter down to both sides and
remove it from the ON filters list.

Note that this rule is only applied when the left and right inputs do not have
outer columns. If they do, then this rule can cause undetectable cycles with
TryDecorrelateSelect, since the filter is pushed down to both sides, but then
only pulled up from the right side by TryDecorrelateSelect. For this reason,
the rule also does not apply to InnerJoinApply or SemiJoinApply.

NOTE: It is important that this rule is first among the join filter push-down
rules.

Extracted from `join.opt` (which defines multiple rules — implement specifically `PushFilterIntoJoinLeftAndRight`, not the other rules in that file):

```
# PushFilterIntoJoinLeftAndRight pushes a filter into both the left and right
# sides of an InnerJoin or SemiJoin if it can be mapped to use the columns of
# both sides. For example, consider this query:
#
#   SELECT * FROM a JOIN b ON a.x=b.x AND a.y=b.y AND a.x + b.y < 5
#
# In this case, we can map a.x + b.y < 5 to both sides based on the equality
# filters a.x=b.x AND a.y=b.y. For the left side, we can map it to
# a.x + a.y < 5, and for the right side, we can map it to b.x + b.y < 5.
# Given this mapping, we can safely push the filter down to both sides and
# remove it from the ON filters list.
#
# Note that this rule is only applied when the left and right inputs do not have
# outer columns. If they do, then this rule can cause undetectable cycles with
# TryDecorrelateSelect, since the filter is pushed down to both sides, but then
# only pulled up from the right side by TryDecorrelateSelect. For this reason,
# the rule also does not apply to InnerJoinApply or SemiJoinApply.
#
# NOTE: It is important that this rule is first among the join filter push-down
#       rules.
[PushFilterIntoJoinLeftAndRight, Normalize]
(InnerJoin | SemiJoin
    $left:* & ^(HasOuterCols $left)
    $right:* & ^(HasOuterCols $right)
    $on:[
        ...
        $item:* &
            ^(FiltersItem (Eq (Variable) (Variable))) &
            (CanMapJoinOpFilter
                $item
                $leftCols:(OutputCols $left)
                $equivSet:(GetEquivGroups $on $left $right)
            ) &
            (CanMapJoinOpFilter
                $item
                $rightCols:(OutputCols $right)
                $equivSet
            )
        ...
    ]
    $private:*
)
=>
((OpName)
    (Select
        $left
        [
            (FiltersItem
                (MapJoinOpFilter $item $leftCols $equivSet)
            )
        ]
    )
    (Select
        $right
        [
            (FiltersItem
                (MapJoinOpFilter $item $rightCols $equivSet)
            )
        ]
    )
    (RemoveFiltersItem $on $item)
    $private
)
```
- Attempts used: 83
- Last updated: 2026-09-28T08:16:53.632229+00:00
- Reason / notes: The encoding is a faithful, non-degenerate minimal instance of the rule: `before()` is an inner join with ON `(L.0=R.0 ∧ L.1=R.1 ∧ L.0=R.1)`, and `after()` pushes the single mappable cross equality (`L.0=R.1`) down as `L.0=L.1` on the left and `R.0=R.1` on the right using the equivalence groups established by the two supporting equalities, then drops it from the ON — exactly the `MapJoinOpFilter`/`RemoveFiltersItem` transformation, with concrete `EQUALS` being the correct (not over-constraining) choice since the rule reasons about equality-based equivalence groups that uninterpreted `pred(...)` symbols can't express. Symbols are shared correctly (`e00`/`e11` retained in both ONs, `e01` removed; `fL`/`fR` distinct side filters), so the proof is of the real rule instance rather than a coincidental `before==after`, and the `SCOPE: PARTIAL` line honestly and specifically discloses the genuine narrowing to inner-only / two-column / one-mappable-plus-two-supporting equalities, which is a real, non-trivial family (the equivalence requires genuine transitivity reasoning, not structural identity).
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 72593750}, panicked=False

### `PushFilterIntoJoinRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

PushFilterIntoJoinRight is symmetric with PushFilterIntoJoinLeft. It pushes
Join filter conditions into the right side of the join rather than into the
left side. See that rule's comments for more details.

Extracted from `join.opt` (which defines multiple rules — implement specifically `PushFilterIntoJoinRight`, not the other rules in that file):

```
# PushFilterIntoJoinRight is symmetric with PushFilterIntoJoinLeft. It pushes
# Join filter conditions into the right side of the join rather than into the
# left side. See that rule's comments for more details.
[PushFilterIntoJoinRight, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:* & ^(HasOuterCols $right)
    $on:[
        ...
        $item:* &
            (IsBoundBy $item $rightCols:(OutputCols $right))
        ...
    ]
    $private:*
)
=>
((OpName)
    $left
    (Select $right (ExtractBoundConditions $on $rightCols))
    (ExtractUnboundConditions $on $rightCols)
    $private
)
```
- Attempts used: 3
- Last updated: 2026-09-28T07:15:46.159428+00:00
- Reason / notes: The encoding is a faithful (though honestly scope-limited) INNER-join instance of the rule: `before()` is `L ⋈_INNER R ON f(R) ∧ g(L,R)` and `after()` is `L ⋈_INNER (R σ_f) ON g(L,R)`, structurally different in exactly the way the real push-down rewrites, so the proof is non-vacuous. The symbols are genuinely uninterpreted — `f` (right-bound, applied to R's column in both the join-row and the filtered-right contexts) and `g` (residual, cross-referencing both sides) are shared correctly, matching the source's "bound by right cols / extract unbound" split, and the "no outer columns" precondition is satisfied by the plain scans. The narrowing to INNER-only (the source also covers Left/Semi/Anti, which QED's MetaJoinType can't express beyond INNER semantics) and to single-conjunct/single-column inputs is specific and disclosed in the SCOPE line, not a hidden shortcut, so the proved equivalence is a real, non-degenerate special case rather than a misleading artifact.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 67885459}, panicked=False

### `PushFilterIntoSetOp` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

PushFilterIntoSetOp pushes filters down to both the left and right sides
of all set operators. For example, consider this query:

SELECT * FROM (SELECT x FROM a UNION ALL SELECT y FROM b) WHERE x < 5

In this case, we can map x < 5 to both sides based on the knowledge that
in the union the x out col corresponds to the x column in the (a) table and
the y column in the (b) table. Therefore our mapping becomes x < 5 in the
left side, and y <  5 in the right side. Given this mapping, we can now
safely push the filter down to both sides as follows:

SELECT * FROM (SELECT x FROM a UNION ALL SELECT y FROM b) WHERE x < 5
=>
(SELECT x FROM a WHERE x < 5) UNION ALL (SELECT y FROM b WHERE y < 5)

Pushing (all) the filters down for each of the set operators (Union, Union
All, Except, Except All, Intersect, Intersect All) is logically equivalent
to filtering after applying the set operator. Here's some justification for
this claim:

Notice that each of the set operators this rule applies to, only works on
union compatible relations. The resulting column set after applying the set
operator, is also necessarily a subset of the column set of the (any)
relations it was composed of. And so, any filter applied must have a
corresponding column in each of the base relations.

This works in the case of Union because a row passing the filter is
independent of the other rows in its relation. It works in the case of
Intersect because if a row `a` was filtered from the intersection, it will
also be filtered from the LHS and RHS. It works in the case of Except because
if a row is filtered from the RHS, preventing its removal from the LHS, it
will also have been filtered from the LHS.

Visualization of the rule:
Let A and B be sets. Let the filter be represented by eliminating some set C.

Union (All):        (A Union B) \ C => (A \ C) Union (B \ C)
Intersection (All): (A Intersect B) \ C => (A \ C) Intersect (B \ C)
Except (All):       (A Except B) \ C => (A \ C) Except (B \ C)

We don't push a filter down if it references outer columns because doing so
prevents decorrelation.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushFilterIntoSetOp`, not the other rules in that file):

```
# PushFilterIntoSetOp pushes filters down to both the left and right sides
# of all set operators. For example, consider this query:
#
#   SELECT * FROM (SELECT x FROM a UNION ALL SELECT y FROM b) WHERE x < 5
#
# In this case, we can map x < 5 to both sides based on the knowledge that
# in the union the x out col corresponds to the x column in the (a) table and
# the y column in the (b) table. Therefore our mapping becomes x < 5 in the
# left side, and y <  5 in the right side. Given this mapping, we can now
# safely push the filter down to both sides as follows:
#
# SELECT * FROM (SELECT x FROM a UNION ALL SELECT y FROM b) WHERE x < 5
# =>
# (SELECT x FROM a WHERE x < 5) UNION ALL (SELECT y FROM b WHERE y < 5)
#
# Pushing (all) the filters down for each of the set operators (Union, Union
# All, Except, Except All, Intersect, Intersect All) is logically equivalent
# to filtering after applying the set operator. Here's some justification for
# this claim:
#
# Notice that each of the set operators this rule applies to, only works on
# union compatible relations. The resulting column set after applying the set
# operator, is also necessarily a subset of the column set of the (any)
# relations it was composed of. And so, any filter applied must have a
# corresponding column in each of the base relations.
#
# This works in the case of Union because a row passing the filter is
# independent of the other rows in its relation. It works in the case of
# Intersect because if a row `a` was filtered from the intersection, it will
# also be filtered from the LHS and RHS. It works in the case of Except because
# if a row is filtered from the RHS, preventing its removal from the LHS, it
# will also have been filtered from the LHS.
#
# Visualization of the rule:
# Let A and B be sets. Let the filter be represented by eliminating some set C.
#
# Union (All):        (A Union B) \ C => (A \ C) Union (B \ C)
# Intersection (All): (A Intersect B) \ C => (A \ C) Intersect (B \ C)
# Except (All):       (A Except B) \ C => (A \ C) Except (B \ C)
#
# We don't push a filter down if it references outer columns because doing so
# prevents decorrelation.
[PushFilterIntoSetOp, Normalize]
(Select
    $input:(Set $left:* $right:* $colmap:*)
    $filter:[
        ...
        $item:* &
            (CanMapOnSetOp $item) &
            (IsBoundBy $item $inputCols:(OutputCols $input))
        ...
    ]
)
=>
(Select
    ((OpName $input)
        (Select
            $left
            [ (FiltersItem (MapSetOpFilterLeft $item $colmap)) ]
        )
        (Select
            $right
            [ (FiltersItem (MapSetOpFilterRight $item $colmap)) ]
        )
        $colmap
    )
    (RemoveFiltersItem $filter $item)
)
```
- Attempts used: 22
- Last updated: 2026-09-28T07:29:27.345531+00:00
- Reason / notes: The encoding is a faithful, non-trivial instance of the rule: `before()` is `Filter(Intersect(L,R), P)` and `after()` is `Intersect(Filter(L,P), Filter(R,P))`, where the single uninterpreted predicate `P` is deliberately shared across both inputs and the set-op output — precisely the rule's "map the same filter through the column map to each side" step, with the identity column map on one shared column type. The SCOPE tag honestly discloses the narrowing to the 2-input distinct (set) INTERSECT variant, which is a genuine QED limitation for the ALL/bag variants (not modeled, and `intersect(all=true)` can't even be serialized — the JSONSerializer only handles `!all`), and nothing in the source rule's preconditions (filter bound to the set op's output cols, no outer refs) is violated or silently dropped by this shape. The result is not degenerate: `before()` and `after()` are structurally different, and the proof establishes that filtering commutes with set intersection for an arbitrary uninterpreted predicate — a real property (it would fail, e.g., if the right side were left unfiltered), with correct symbol sharing rather than coincidental over-constraint. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 62495584}, panicked=False

### `PushLeakproofFiltersIntoPermeableBarrier` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

PushLeakproofFiltersIntoPermeableBarrier splits filter expressions based on
leakproofness and pushes only the leakproof filters beneath a permeable
Barrier. The remaining filters stay above the Barrier.

This allows safe reordering of leakproof expressions while preserving the
Barrier to block unsafe transformations involving non-leakproof filters.
The Barrier must be marked as LeakproofPermeable to allow this behavior.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushLeakproofFiltersIntoPermeableBarrier`, not the other rules in that file):

```
# PushLeakproofFiltersIntoPermeableBarrier splits filter expressions based on
# leakproofness and pushes only the leakproof filters beneath a permeable
# Barrier. The remaining filters stay above the Barrier.
#
# This allows safe reordering of leakproof expressions while preserving the
# Barrier to block unsafe transformations involving non-leakproof filters.
# The Barrier must be marked as LeakproofPermeable to allow this behavior.
[PushLeakproofFiltersIntoPermeableBarrier, Normalize]
(Select
    (Barrier
        $input:*
        $leakproofPermeable:* & (If $leakproofPermeable)
    )
    $filters:* &
        (Let
            (
                $leakproofFilters
                $remainingFilters
                $ok
            ):(SplitLeakproofFilters $filters)
            $ok
        )
)
=>
(Select
    (Barrier
        (Select $input $leakproofFilters)
        $leakproofPermeable
    )
    $remainingFilters
)
```
- Attempts used: 46
- Last updated: 2026-09-28T07:53:16.454708+00:00
- Reason / notes: The encoding faithfully captures the rule's logical content: a leakproof predicate is pushed from above an identity operation (the Barrier) to below it, while the remaining predicate stays above, which is exactly what the source rule rewrites. The barrier-as-identity-projection modeling is correct since a Barrier has no bag-semantic effect on rows, the uninterpreted predicates `Leakproof` and `Remaining` are properly shared across both sides, and the single-column arity restriction (the only narrowing) is honestly documented in the SCOPE line and does not change the logical principle being proved, since filter-through-identity commutativity is arity-independent. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 17282292}, panicked=False

### `PushLeakproofJoinIntoPermeableBarrierLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

PushLeakproofJoinIntoPermeableBarrierLeft moves a join below a permeable
Barrier on its left input when all ON filters are leakproof. The Barrier is
then placed above the join. This is safe because leakproof filters can be
reordered freely, and the Barrier allows such movement when marked as
LeakproofPermeable. This rule is effectively pushing the left input into the
Barrier, so the left input must be leakproof as well.

Extracted from `join.opt` (which defines multiple rules — implement specifically `PushLeakproofJoinIntoPermeableBarrierLeft`, not the other rules in that file):

```
# PushLeakproofJoinIntoPermeableBarrierLeft moves a join below a permeable
# Barrier on its left input when all ON filters are leakproof. The Barrier is
# then placed above the join. This is safe because leakproof filters can be
# reordered freely, and the Barrier allows such movement when marked as
# LeakproofPermeable. This rule is effectively pushing the left input into the
# Barrier, so the left input must be leakproof as well.
[PushLeakproofJoinIntoPermeableBarrierLeft, Normalize]
(InnerJoin | InnerJoinApply
    (Barrier
        $left:*
        $leakproofPermeable:* & (If $leakproofPermeable)
    )
    $right:* & (IsLeakproof $right)
    $on:* & (HasAllLeakProofFilters $on)
    $private:*
)
=>
(Barrier
    ((OpName) $left $right $on $private)
    $leakproofPermeable
)
```
- Attempts used: 26
- Last updated: 2026-09-29T04:06:50.799277+00:00
- Reason / notes: The encoding is faithful: it models the permeable Barrier as an identity projection (correct, since a Barrier is a pure flow-control point with no row-bag effect), shares the uninterpreted ON predicate "C" and both input scans/types between before/after so the two sides are the same symbols, and produces genuinely different trees (projection below the join vs. above it) rather than a vacuous before()==after(). The rule's real preconditions (LeakproofPermeable via If, IsLeakproof on the right, HasAllLeakProofFilters on the ON list) gate evaluation-order/partiality behavior, which has no bag-semantic counterpart in QED, so the only bag-semantic content is that an identity barrier commutes with the inner join—exactly what is proved, and the porter documents this correctly. The two under-generalizations (inputs fixed to two columns, and plain INNER join instead of also matching InnerJoinApply) are genuine, specific, and honestly disclosed in the SCOPE: PARTIAL line, and neither degenerates the result. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 337167}, panicked=False

### `PushLeakproofJoinIntoPermeableBarrierRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

PushLeakproofJoinIntoPermeableBarrierRight is the right-side variant.
It moves a join below a permeable Barrier on its right input when all ON
filters are leakproof, then rewraps the join in the Barrier to preserve its
blocking behavior for non-leakproof expressions higher in the plan. This rule
is effectively pushing the right input into the Barrier, so the right input
must be leakproof as well.

Extracted from `join.opt` (which defines multiple rules — implement specifically `PushLeakproofJoinIntoPermeableBarrierRight`, not the other rules in that file):

```
# PushLeakproofJoinIntoPermeableBarrierRight is the right-side variant.
# It moves a join below a permeable Barrier on its right input when all ON
# filters are leakproof, then rewraps the join in the Barrier to preserve its
# blocking behavior for non-leakproof expressions higher in the plan. This rule
# is effectively pushing the right input into the Barrier, so the right input
# must be leakproof as well.
[PushLeakproofJoinIntoPermeableBarrierRight, Normalize]
(InnerJoin | InnerJoinApply
    $left:* & (IsLeakproof $left)
    (Barrier
        $right:*
        $leakproofPermeable:* & (If $leakproofPermeable)
    )
    $on:* & (HasAllLeakProofFilters $on)
    $private:*
)
=>
(Barrier
    ((OpName) $left $right $on $private)
    $leakproofPermeable
)
```
- Attempts used: 23
- Last updated: 2026-09-29T00:49:35.293734+00:00
- Reason / notes: The encoding captures the rule's bag-semantic core exactly — Join(L, Barrier(R), C) vs Barrier(Join(L, R, C)) with the Barrier modeled as an identity projection — and before()/after() are structurally different (identity projection nested under the right join input vs. wrapping the whole join), so the proof is not vacuous; L, R, and C are correctly shared as the same uninterpreted symbols across both sides, with no spurious over-constraint. The only deviations are honestly and specifically declared in the SCOPE line (plain INNER instead of also InnerJoinApply, whose correlated/dependent-join semantics are outside the bag model, and a 2-column arity that is semantically neutral since the equivalence does not depend on arity), and the rule's IsLeakproof / HasAllLeakProofFilters / leakproofPermeable preconditions are planner-safety flags with no effect on the row bag, so no data-semantic precondition was silently dropped. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 422416}, panicked=False

### `PushLeakproofProjectionsIntoPermeableBarrier` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/project.opt

PushLeakproofProjectionsIntoPermeableBarrier moves a Project below a Barrier
when all projection expressions are leakproof and the Barrier is marked as
permeable. This is safe because leakproof expressions cannot reveal
information through their evaluation, and a permeable Barrier allows such
projections to pass through it.

Extracted from `project.opt` (which defines multiple rules — implement specifically `PushLeakproofProjectionsIntoPermeableBarrier`, not the other rules in that file):

```
# PushLeakproofProjectionsIntoPermeableBarrier moves a Project below a Barrier
# when all projection expressions are leakproof and the Barrier is marked as
# permeable. This is safe because leakproof expressions cannot reveal
# information through their evaluation, and a permeable Barrier allows such
# projections to pass through it.
[PushLeakproofProjectionsIntoPermeableBarrier, Normalize]
(Project
    (Barrier
        $input:*
        $leakproofPermeable:* & (If $leakproofPermeable)
    )
    $projections:* & (HasAllLeakProofProjections $projections)
    $passthrough:*
)
=>
(Barrier
    (Project $input $projections $passthrough)
    $leakproofPermeable
)
```
- Attempts used: 25
- Last updated: 2026-09-29T00:54:54.908526+00:00
- Reason / notes: The before/after shapes genuinely reflect the optimization — the Project moves from above the barrier to below it (not a structural identity) — and the barrier is modeled as a row-identity projection, the only row-level behavior a barrier has, with the single uninterpreted projection operator and passthrough field correctly shared as the same symbols on both sides. The rule's HasAllLeakProofProjections/(If $leakproofPermeable) preconditions are security properties of expression evaluation, not constraints on row values, so they cannot be expressed in QED's bag model (a real QED limitation, acknowledged in the code comments); the proved unconditional row-level equivalence is therefore the strongest row-semantic claim the rule needs and subsumes it. The fixed 1-column / 1-expression / 1-passthrough arity is the minimal shape in which the rule can exist, is specific and honestly disclosed in the PARTIAL tag (full variadic arity is not expressible in the DSL at any width), and is not degenerate. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 416959}, panicked=False

### `PushSelectCondLeftIntoJoinLeftAndRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

PushSelectCondLeftIntoJoinLeftAndRight applies to the case when a condition
bound by the left side of a join can be mapped to the right side using
equality columns from the ON condition of the join. It pushes the original
filter to the left side, and the mapped filter to the right side.
For example, consider this query:

SELECT * FROM l LEFT JOIN r ON l.x = r.x WHERE l.x = 5;

This can safely be converted to:

SELECT * FROM (SELECT * FROM l WHERE l.x = 5)
LEFT JOIN (SELECT * FROM r WHERE r.x = 5) ON l.x = r.x;

It's not normally correct to push filters from the SELECT clause to
the right side of a LEFT JOIN, since those rows might still show up
in the output as NULL-extended rows from the left side. In this case,
however, for any rows removed from the right side, the matching rows are
also removed from the left side (and thus removed from the output).
To ensure that this is the case, it's important that the filter only refers
to columns on the left side that have corresponding equivalent columns on
the right side.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectCondLeftIntoJoinLeftAndRight`, not the other rules in that file):

```
# PushSelectCondLeftIntoJoinLeftAndRight applies to the case when a condition
# bound by the left side of a join can be mapped to the right side using
# equality columns from the ON condition of the join. It pushes the original
# filter to the left side, and the mapped filter to the right side.
# For example, consider this query:
#
#   SELECT * FROM l LEFT JOIN r ON l.x = r.x WHERE l.x = 5;
#
# This can safely be converted to:
#
#   SELECT * FROM (SELECT * FROM l WHERE l.x = 5)
#   LEFT JOIN (SELECT * FROM r WHERE r.x = 5) ON l.x = r.x;
#
# It's not normally correct to push filters from the SELECT clause to
# the right side of a LEFT JOIN, since those rows might still show up
# in the output as NULL-extended rows from the left side. In this case,
# however, for any rows removed from the right side, the matching rows are
# also removed from the left side (and thus removed from the output).
# To ensure that this is the case, it's important that the filter only refers
# to columns on the left side that have corresponding equivalent columns on
# the right side.
[PushSelectCondLeftIntoJoinLeftAndRight, Normalize]
(Select
    $input:(LeftJoin | LeftJoinApply | SemiJoin | SemiJoinApply
            | AntiJoin | AntiJoinApply
        $left:*
        $right:*
        $on:*
        $private:*
    )
    $filters:[
        ...
        $item:(FiltersItem $condition:*) &
            (IsBoundBy $item (OutputCols $left)) &
            (CanMapJoinOpFilter
                $item
                $rightCols:(OutputCols $right)
                $equivSet:(GetEquivGroups $on $left $right)
            )
        ...
    ]
)
=>
(Select
    ((OpName $input)
        (Select $left [ (FiltersItem $condition) ])
        (Select
            $right
            [
                (FiltersItem
                    (MapJoinOpFilter $item $rightCols $equivSet)
                )
            ]
        )
        $on
        $private
    )
    (RemoveFiltersItem $filters $item)
)
```
- Attempts used: 26
- Last updated: 2026-09-29T06:04:06.512819+00:00
- Reason / notes: The encoding faithfully captures the rule's documented case generalized honestly: arbitrary uninterpreted tables L, R with an arbitrary unary predicate P, where before = Filter(P(l.x), L ⋈_LEFT R ON l.x = r.x) and after = (L σ P) ⋈_LEFT (R σ P) — structurally and semantically distinct, so the proof is non-vacuous; sharing the single symbol pOp for both pushed filters correctly encodes MapJoinOpFilter via the ON-equality equivalence group (with two independent predicates the rewrite would be unsound), and the concrete EQUALS in the join condition is the right way to express the GetEquivGroups/CanMapJoinOpFilter precondition rather than an over-constraining artifact. The narrowing to LEFT JOIN, single equality key, and unary filter on that key is a genuine special case limited by QED's inability to express arbitrary column-remapping equivalence sets, it is honestly declared on the SCOPE line, and it preserves the rule's distinctive content (pushing a left-bound filter into the right side of a LEFT JOIN, the direction the simpler PushSelectIntoJoinLeft does not cover).
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 90097584}, panicked=False

### `PushSelectIntoGroupBy` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoGroupBy pushes a Select condition below a GroupBy in the case
where it only references grouping columns or ConstAgg columns.

This rule doesn't work on ScalarGroupBy which exhibits different behavior if
the input is empty:
SELECT MAX(y) FROM a

If "a" is empty, this returns a single row containing a null value. This is
different behavior than a GroupBy with grouping columns, which would return
the empty set for a similar query:
SELECT MAX(y) FROM a GROUP BY x

Citations: [2]

Note: Do not add EnsureDistinctOn to the match pattern. Pushing the select
filters through the EnsureDistinctOn can prevent it from detecting duplicate
rows and therefore change error behavior.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoGroupBy`, not the other rules in that file):

```
# PushSelectIntoGroupBy pushes a Select condition below a GroupBy in the case
# where it only references grouping columns or ConstAgg columns.
#
# This rule doesn't work on ScalarGroupBy which exhibits different behavior if
# the input is empty:
#   SELECT MAX(y) FROM a
#
# If "a" is empty, this returns a single row containing a null value. This is
# different behavior than a GroupBy with grouping columns, which would return
# the empty set for a similar query:
#   SELECT MAX(y) FROM a GROUP BY x
#
# Citations: [2]
#
# Note: Do not add EnsureDistinctOn to the match pattern. Pushing the select
# filters through the EnsureDistinctOn can prevent it from detecting duplicate
# rows and therefore change error behavior.
[PushSelectIntoGroupBy, Normalize]
(Select
    $input:(GroupBy | DistinctOn
        $groupingInput:*
        $aggregations:*
        $groupingPrivate:*
    )
    $filters:[
        ...
        $item:* &
            (IsBoundBy
                $item
                $passthrough:(GroupingAndConstCols
                    $groupingPrivate
                    $aggregations
                )
            )
        ...
    ]
)
=>
(Select
    ((OpName $input)
        (Select
            $groupingInput
            (ExtractBoundConditions $filters $passthrough)
        )
        $aggregations
        $groupingPrivate
    )
    (ExtractUnboundConditions $filters $passthrough)
)
```
- Attempts used: 26
- Last updated: 2026-09-29T05:41:24.887955+00:00
- Reason / notes: `before()` (Filter above the Aggregate) and `after()` (Filter inside, below the Aggregate) are structurally different plans, so the equivalence proof is non-vacuous and captures the rule's actual optimization—pushing a filter on the grouping column beneath the GroupBy. The uninterpreted filter predicates P/R, the aggregate `f`, and the group key are correctly shared across both sides (the same predicate applied to the same logical group-key column, referenced in the input below and the aggregate output above), so this is a faithful symbol sharing rather than a coincidental over-constraint, and since every conjunct is bound the vacuous outer `Select` is rightly omitted per `EliminateSelect`. The restriction to the GroupBy branch (not DistinctOn), a single identity group key, one non-distinct uninterpreted aggregate, and grouping-column-only conjuncts is a genuine, honestly-flagged PARTIAL scope (the ConstAgg case is correctly deemed unmodelable because QED's aggregates are uninterpreted and cannot see per-group constancy), and the result remains a useful, non-degenerate special case.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 90020791}, panicked=False

### `PushSelectIntoInlinableProject` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/inline.opt

PushSelectIntoInlinableProject pushes the Select operator into a Project, even
though the filter references it. This is made possible by inlining the
references to projected columns so that the Select becomes independent of the
Project, and therefore can be reordered. This normalization is important for
enabling Any filter conditions to be pushed down into scans.

This rule is low priority so that it runs after the PushSelectIntoProject
and MergeProjectProject rules, since those rules are cheaper to match and
replace.

Example:
SELECT * FROM (SELECT x+1 AS x2 FROM xy) WHERE x2=10
=>
SELECT x+1 AS x2 FROM (SELECT * FROM xy WHERE (x+1)=10)

Extracted from `inline.opt` (which defines multiple rules — implement specifically `PushSelectIntoInlinableProject`, not the other rules in that file):

```
# PushSelectIntoInlinableProject pushes the Select operator into a Project, even
# though the filter references it. This is made possible by inlining the
# references to projected columns so that the Select becomes independent of the
# Project, and therefore can be reordered. This normalization is important for
# enabling Any filter conditions to be pushed down into scans.
#
# This rule is low priority so that it runs after the PushSelectIntoProject
# and MergeProjectProject rules, since those rules are cheaper to match and
# replace.
#
# Example:
#   SELECT * FROM (SELECT x+1 AS x2 FROM xy) WHERE x2=10
#   =>
#   SELECT x+1 AS x2 FROM (SELECT * FROM xy WHERE (x+1)=10)
#
[PushSelectIntoInlinableProject, Normalize, LowPriority]
(Select
    (Project
        $input:*
        $projections:* & (CanInlineProjections $projections)
        $passthrough:*
    )
    $filters:* & ^(FilterHasCorrelatedSubquery $filters)
)
=>
(Project
    (Select $input (InlineSelectProject $filters $projections))
    $projections
    $passthrough
)
```
- Attempts used: 23
- Last updated: 2026-09-29T04:50:47.799831+00:00
- Reason / notes: The encoding is non-vacuous and structurally faithful: `before()` is Filter(P∘col0 ∧ Q∘col1) over Project(F(a,b), G(a,b), a, b) with P/Q genuinely applied to the Project's *output* columns, while `after()` re-applies the same shared symbols P/Q to the defining expressions F(a,b)/G(a,b) over the scan and lifts the identical projection list back on top — so the proof is exactly the inlining pushdown property, not an identity. The fixed 2+2 column arity and two unary filters are a specific, declared PARTIAL restriction (schema-arity parameters that don't dilute the semantic content, which is universally quantified over F, G, P, Q and the table), and the determinism of F/G that backs CanInlineProjections is inherent to RuleScript's uninterpreted-function model, so the proved claim subsumes rather than evades the original guard's domain. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 67495959}, panicked=False

### `PushSelectIntoJoinLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoJoinLeft pushes Select filter conditions into the left side of
an input Join. This is possible in the case of InnerJoin, LeftJoin, SemiJoin,
and AntiJoin, as long as the condition has no dependencies on the right side
of the join. Right and Full joins are not eligible, since attempting to filter
left rows would just result in NULL left rows instead.

-- No row is returned for a.x=1, a.y=2, b.x=1, since the WHERE excludes it.
SELECT * FROM a RIGHT JOIN b ON a.x=b.x WHERE a.y < 0

-- But if the filter is incorrectly pushed down in RIGHT/FULL JOIN case,
-- then a row containing null values on the left side is returned.
SELECT * FROM (SELECT * FROM a WHERE a.y < 0) a RIGHT JOIN b ON a.x=b.x

Citations: [1]

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoJoinLeft`, not the other rules in that file):

```
# PushSelectIntoJoinLeft pushes Select filter conditions into the left side of
# an input Join. This is possible in the case of InnerJoin, LeftJoin, SemiJoin,
# and AntiJoin, as long as the condition has no dependencies on the right side
# of the join. Right and Full joins are not eligible, since attempting to filter
# left rows would just result in NULL left rows instead.
#
#   -- No row is returned for a.x=1, a.y=2, b.x=1, since the WHERE excludes it.
#   SELECT * FROM a RIGHT JOIN b ON a.x=b.x WHERE a.y < 0
#
#   -- But if the filter is incorrectly pushed down in RIGHT/FULL JOIN case,
#   -- then a row containing null values on the left side is returned.
#   SELECT * FROM (SELECT * FROM a WHERE a.y < 0) a RIGHT JOIN b ON a.x=b.x
#
# Citations: [1]
[PushSelectIntoJoinLeft, Normalize]
(Select
    $input:(LeftJoin | LeftJoinApply | SemiJoin | SemiJoinApply
            | AntiJoin | AntiJoinApply
        $left:*
        $right:*
        $on:*
        $private:*
    )
    $filters:[
        ...
        $item:* & (IsBoundBy $item $leftCols:(OutputCols $left))
        ...
    ]
)
=>
(Select
    ((OpName $input)
        (Select
            $left
            (ExtractBoundConditions $filters $leftCols)
        )
        $right
        $on
        $private
    )
    (ExtractUnboundConditions $filters $leftCols)
)
```
- Attempts used: 42
- Last updated: 2026-09-29T05:46:43.376600+00:00
- Reason / notes: The encoding correctly captures the core LEFT-JOIN push-down transformation: before() is Select(LeftJoin(L,R,g), f(l)∧u(l,r)) and after() is Select(LeftJoin(Select(L,f),R,g), u(l,r)), which are structurally distinct and the equivalence is a genuine (non-vacuous) bag-semantics proof. All predicates (f, u, g) are uninterpreted symbols, f is correctly modeled as bound-by-left-only (single argument = L's column in both the join-row and row-scoped contexts), and u correctly references both sides so it stays above. The SCOPE line honestly and specifically discloses the narrowings (LEFT only vs. the original's Left/Semi/Anti + Apply variants; exactly one bound + one unbound conjunct; one ON condition; single-column inputs), and none of these restrictions introduce a semantic error — they merely reduce the family of instantiations QED must verify while preserving the rule's essential logic. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 91881166}, panicked=False

### `PushSelectIntoOrdinality` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoOrdinality pushes the Select operator into its Ordinality input
if the Ordinality operation was built for the purposes of removing duplicate
rows, and the actual values returned by the Ordinality operation don't matter.
This is typically preferable because it allows the Select to be pushed into
operations beneath the Ordinality, minimizing the number of rows subsequent
operations need to process and potentially pushing the Select down far enough
to enable constrained scans. This may also enable other normalization rules
which match on Select expressions to fire.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoOrdinality`, not the other rules in that file):

```
# PushSelectIntoOrdinality pushes the Select operator into its Ordinality input
# if the Ordinality operation was built for the purposes of removing duplicate
# rows, and the actual values returned by the Ordinality operation don't matter.
# This is typically preferable because it allows the Select to be pushed into
# operations beneath the Ordinality, minimizing the number of rows subsequent
# operations need to process and potentially pushing the Select down far enough
# to enable constrained scans. This may also enable other normalization rules
# which match on Select expressions to fire.
[PushSelectIntoOrdinality, Normalize]
(Select
    (Ordinality $input:* $private:*) &
        (ForDuplicateRemoval $private)
    $filters:[
        ...
        $item:* &
            (IsBoundBy $item $inputCols:(OutputCols $input))
        ...
    ]
)
=>
(Select
    (Ordinality
        (Select
            $input
            (ExtractBoundConditions $filters $inputCols)
        )
        $private
    )
    (ExtractUnboundConditions $filters $inputCols)
)
```
- Attempts used: 46
- Last updated: 2026-09-29T06:14:59.008759+00:00
- Reason / notes: The encoding faithfully mirrors the source rule's core transformation — before() is `Select(Ordinality(input), P(input))` and after() is `Ordinality(Select(input, P))` — with the exact right relational shape (projection standing in for the ForDuplicateRemoval ordinality, filter pushed from above the projection to below it), uninterpreted symbols throughout (O as a shared function of the input row, P as a shared uninterpreted predicate on the input column, so no concrete predicate/join-type/column-count is baked in), and correct symbol sharing (O and P are the same logical quantity re-derived on both sides, as the rule requires). before() and after() are genuinely structurally different (Filter-Project-Scan vs. Project-Filter-Scan), so the proof is non-vacuous, and the shared O symbol is a sound over-constraint rather than a spurious one: it encodes precisely the property the rule's ForDuplicateRemoval comment relies on (the ordinality column's values don't matter / are stably re-derived from the input), without which the pushdown would not hold. The only narrowing — all filter conjuncts are bound by the input columns (so ExtractUnboundConditions is empty and the vacuous outer Select is omitted, consistent with EliminateSelect) — is exactly what the SCOPE tag states, leaving the result a genuine, useful special case of the real rule (the case that yields the maximal pushdown) rather than a degenerate or mis-shaped one. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 67693667}, panicked=False

### `PushSelectIntoProject` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoProject pushes the Select operator into its Project input. This
is typically preferable because it minimizes the number of rows which Project
needs to process. This is especially important if Project is adding expensive
computed columns.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoProject`, not the other rules in that file):

```
# PushSelectIntoProject pushes the Select operator into its Project input. This
# is typically preferable because it minimizes the number of rows which Project
# needs to process. This is especially important if Project is adding expensive
# computed columns.
[PushSelectIntoProject, Normalize]
(Select
    (Project $input:* $projections:* $passthrough:*)
    $filters:[
        ...
        $item:* &
            (IsBoundBy $item $inputCols:(OutputCols $input))
        ...
    ]
)
=>
(Select
    (Project
        (Select
            $input
            (ExtractBoundConditions $filters $inputCols)
        )
        $projections
        $passthrough
    )
    (ExtractUnboundConditions $filters $inputCols)
)
```
- Attempts used: 22
- Last updated: 2026-09-29T06:02:03.322465+00:00
- Reason / notes: The encoding faithfully instantiates the rule's essential content — a bound conjunct P on the input/passthrough column is pushed below the Project while the unbound conjunct Q on the synthesized column F(a) stays above — with `before()` and `after()` structurally distinct (an extra Filter node and a lowered Project), so the proof is non-vacuous; the fact that QED accepted it (rather than flagging the missing P) also confirms P is genuinely applied below the Project, not silently dropped. The passthrough is correctly the identity field reference `scan.field(0)` (not an uninterpreted function), which is exactly the property that licenses the push-down, and P/Q/F/the table are all genuinely uninterpreted with correct symbol sharing (the bound condition is the same predicate re-applied to the input column, the unbound condition the same predicate on the synthesized column), so there is no coincidental over-constraint. The source rule carries no key/NOT NULL preconditions — only the structural IsBoundBy guard, which is captured by which column each conjunct references — and the `// SCOPE: PARTIAL` line honestly and specifically labels this 1-input-col / 1-synthesized-col / 1-bound / 1-unbound instance, a genuine non-degenerate special case that preserves the rule's core semantics.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 64322833}, panicked=False

### `PushSelectIntoProjectSet` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoProjectSet pushes filters into a ProjectSet. In particular,
the filters that are bound to the input columns of the ProjectSet are
pushed down into it, in hopes of being pushed down further into joins
and scans underneath the ProjectSet.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoProjectSet`, not the other rules in that file):

```
# PushSelectIntoProjectSet pushes filters into a ProjectSet. In particular,
# the filters that are bound to the input columns of the ProjectSet are
# pushed down into it, in hopes of being pushed down further into joins
# and scans underneath the ProjectSet.
[PushSelectIntoProjectSet, Normalize]
(Select
    (ProjectSet $input:* $zip:*)
    $filters:[
        ...
        $item:* &
            (IsBoundBy $item $inputCols:(OutputCols $input))
        ...
    ]
)
=>
(Select
    (ProjectSet
        (Select
            $input
            (ExtractBoundConditions $filters $inputCols)
        )
        $zip
    )
    (ExtractUnboundConditions $filters $inputCols)
)
```
- Attempts used: 4
- Last updated: 2026-09-29T06:10:43.528939+00:00
- Reason / notes: The ProjectSet is soundly abstracted as an INNER join of its input L with an emitted bag E under an uninterpreted pairing predicate M — INNER correctly models the row-dropping behavior when the lateral set yields zero rows, the universal quantification over M covers both zipped and unzipped modes, and the bound conjunct P references only the passed-through input column (structurally enforcing the IsBoundBy guard) while the unbound conjunct Q references only the synthesized column, with the same symbols (M, P, Q, L, E) shared correctly between both sides. before() and after() are genuinely different plans (P filters above vs. below the join), so the proof is non-vacuous and encodes exactly the source rule's transformation (Select over ProjectSet ⟹ Select over ProjectSet-over-Select, with filters split bound/unbound). The honestly tagged PARTIAL restrictions — one input column, one emitted column, exactly one bound conjunct and one unbound conjunct, with the unbound conjunct restricted to the synthesized column — are specific rather than vague, remain a non-degenerate instance of the rule, and the single-conjunct form effectively subsumes arbitrary conjunctions since P and Q range over all uninterpreted predicates.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 19624875}, panicked=False

### `PushSelectIntoUnorderedDistinctOn` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoUnorderedDistinctOn pushes Select filters into the input of an
unordered DistinctOn. Unlike PushSelectIntoGroupBy, which only pushes filters
on grouping or ConstAgg columns, this rule pushes filters that reference any
column of the DistinctOn's input — including first-agg columns.

This is valid because an unordered DistinctOn can return any row from each
group. Filtering before the DistinctOn simply constrains which rows are
available to be chosen; groups that are entirely eliminated by the filter
produce no output row, which is equivalent to choosing a row and then
filtering it away afterward.

The rule is restricted to unordered DistinctOn: an ordered DistinctOn must
pick a specific row determined by the ordering, so pre-filtering could change
which row is selected.

Note: Do not add EnsureDistinctOn to the match pattern. Pushing the select
filters through the EnsureDistinctOn can prevent it from detecting duplicate
rows and therefore change error behavior.

Filters referencing outer columns are not pushed; they remain above the
DistinctOn.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoUnorderedDistinctOn`, not the other rules in that file):

```
# PushSelectIntoUnorderedDistinctOn pushes Select filters into the input of an
# unordered DistinctOn. Unlike PushSelectIntoGroupBy, which only pushes filters
# on grouping or ConstAgg columns, this rule pushes filters that reference any
# column of the DistinctOn's input — including first-agg columns.
#
# This is valid because an unordered DistinctOn can return any row from each
# group. Filtering before the DistinctOn simply constrains which rows are
# available to be chosen; groups that are entirely eliminated by the filter
# produce no output row, which is equivalent to choosing a row and then
# filtering it away afterward.
#
# The rule is restricted to unordered DistinctOn: an ordered DistinctOn must
# pick a specific row determined by the ordering, so pre-filtering could change
# which row is selected.
#
# Note: Do not add EnsureDistinctOn to the match pattern. Pushing the select
# filters through the EnsureDistinctOn can prevent it from detecting duplicate
# rows and therefore change error behavior.
#
# Filters referencing outer columns are not pushed; they remain above the
# DistinctOn.
[PushSelectIntoUnorderedDistinctOn, Normalize]
(Select
    $input:(DistinctOn
        $groupingInput:*
        $aggregations:*
        $groupingPrivate:* &
            (IsUnorderedGrouping $groupingPrivate)
    )
    $filters:[
        ...
        $item:* &
            (IsBoundBy
                $item
                $inputCols:(OutputCols $groupingInput)
            )
        ...
    ]
)
=>
(Select
    (DistinctOn
        (Select
            $groupingInput
            (ExtractBoundConditions $filters $inputCols)
        )
        $aggregations
        $groupingPrivate
    )
    (ExtractUnboundConditions $filters $inputCols)
)
```
- Attempts used: 22
- Last updated: 2026-09-29T06:19:48.262286+00:00
- Reason / notes: The encoding is a faithful, non-degenerate PARTIAL special case: it models the identity (unordered, no-aggregate) DistinctOn as a group-by-all-columns Aggregate with zero agg calls, so `before()` (filter above the distinct) and `after()` (filter below it) are structurally distinct and the proof is a genuine commute-with-distinct result rather than a vacuous identity. Predicates P/R are properly uninterpreted and shared consistently across the aggregate, matching the source rule's same-filter-conjuncts semantics. The scope tag is honest and specific — the general FirstAgg/ConstAgg-column case is unreachable for a real reason (QED's aggregates are uninterpreted, so it cannot reason about a filter referencing a non-grouping output column), not a missing DSL capability — and the identity restriction is a legitimate instance the source rule actually matches.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 19226791}, panicked=False

### `PushSelectIntoWindow` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/window.opt

PushSelectIntoWindow pushes down a Select which can be satisfied by only the
functional closure of the columns being partitioned over. This is valid
because it's "all-or-nothing" - we only entirely eliminate a partition or
don't eliminate it at all.

Extracted from `window.opt` (which defines multiple rules — implement specifically `PushSelectIntoWindow`, not the other rules in that file):

```
# PushSelectIntoWindow pushes down a Select which can be satisfied by only the
# functional closure of the columns being partitioned over. This is valid
# because it's "all-or-nothing" - we only entirely eliminate a partition or
# don't eliminate it at all.
[PushSelectIntoWindow, Normalize]
(Select
    (Window $input:* $fn:* $private:*)
    $filters:[
        ...
        $item:* &
            (ColsAreDeterminedBy
                (OuterCols $item)
                $partitionCols:(WindowPartition $private)
                $input
            )
        ...
    ]
)
=>
(Select
    (Window
        (Select
            $input
            (ExtractDeterminedConditions
                $filters
                $partitionCols
                $input
            )
        )
        $fn
        $private
    )
    (ExtractUndeterminedConditions
        $filters
        $partitionCols
        $input
    )
)
```
- Attempts used: 61
- Last updated: 2026-09-29T08:32:43.685899+00:00
- Reason / notes: The two plans are structurally different — the window's aggregate is computed over the unfiltered input in `before()` but over the P-filtered input in `after()` — and they are equal only because P references only the partition column k, i.e. the rule's ColsAreDeterminedBy precondition encoded by construction, so the proof is of the genuine all-or-nothing pushdown rather than a vacuous identity (a P that could see v would break the equality, since the per-partition aggregate's input bag would differ). Symbols are shared correctly (P, Q, and the w aggregate are the same uninterpreted symbols on both sides; the join-back is an inner equijoin on k that preserves row multiplicity, and Q correctly stays above the window on both sides), and the restrictions — single partition column, single partition-determined window function with no frame/ordering semantics, two-column input, one pushed and one remaining conjunct — are real narrowings of the full Optgen rule but are each specifically named in the SCOPE: PARTIAL line, leaving a non-degenerate, faithful special case. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 94435000}, panicked=False

### `ReduceNotNullGroupingCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

ReduceNotNullGroupingCols is similar to ReduceGroupingCols, but with the
additional restriction that nullable columns cannot be removed from the set of
grouping columns. This is because the UpsertDistinctOn operator treats NULL
values as not equal to one another, and therefore will not group them
together. Since removing a grouping column is equivalent to grouping all
values of that column together, this would be incorrect in the case where all
input rows are NULL for that column:

SELECT c FROM t WHERE c IS NULL

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `ReduceNotNullGroupingCols`, not the other rules in that file):

```
# ReduceNotNullGroupingCols is similar to ReduceGroupingCols, but with the
# additional restriction that nullable columns cannot be removed from the set of
# grouping columns. This is because the UpsertDistinctOn operator treats NULL
# values as not equal to one another, and therefore will not group them
# together. Since removing a grouping column is equivalent to grouping all
# values of that column together, this would be incorrect in the case where all
# input rows are NULL for that column:
#
#   SELECT c FROM t WHERE c IS NULL
#
[ReduceNotNullGroupingCols, Normalize]
(UpsertDistinctOn | EnsureUpsertDistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* &
        ^(ColsAreEmpty
            $redundantCols:(IntersectionCols
                (RedundantCols
                    $input
                    (GroupingCols $groupingPrivate)
                )
                (NotNullCols $input)
            )
        )
)
=>
((OpName)
    $input
    (AppendAggCols $aggregations ConstAgg $redundantCols)
    (RemoveGroupingCols $groupingPrivate $redundantCols)
)
```
- Attempts used: 44
- Last updated: 2026-09-29T07:32:43.890077+00:00
- Reason / notes: The encoding is a correct, non-degenerate special case: `before()` (GROUP BY col0, col1 where col0≡col1) and `after()` (GROUP BY col0, then project col0 twice) are structurally distinct and the proof is non-vacuous. The scope limitation—making the "redundant" column literally the same field rather than a distinct-but-functionally-dependent column—is forced by the DSL's inability to express functional dependencies between separate columns, and QED's inability to reason about ConstAgg's algebra on a distinct dependent column, making the fully general rule unprovable without (and likely even with) DSL extensions. The SCOPE line accurately and specifically states both assumptions (literal field identity and no aggregation columns), the non-nullable type faithfully captures the source rule's `NotNullCols` precondition, and the overall shape (reducing the grouping set while preserving output width via a pass-through) mirrors the source rule's rewrite.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 68223750}, panicked=False

### `ReduceWindowPartitionCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/window.opt
- Attempts used: 0
- Last updated: 2026-10-16T07:30:21.279129+00:00
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 76089583}, panicked=False

### `RejectNullsLeftJoin` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsLeftJoin reduces a LeftJoin operator to an InnerJoin operator (or a
FullJoin to a RightJoin) when there is a null-rejecting filter on any column
from the right side. The effect of the null-rejecting filter is that output
rows with all NULL values on the right side created by the left (or full) join
are eliminated, making the join equivalent to an inner (or right) join. For
example:

SELECT * FROM a LEFT OUTER JOIN b ON a.x = b.x WHERE b.y < 5

can be reduced to:

SELECT * FROM a INNER JOIN b ON a.x = b.x WHERE b.y < 5

since b.y < 5 is a null-rejecting filter on the right side.

This rule is marked as high priority so that it runs before Select filter
pushdown rules. Those rules may remove a filter before it's had a chance to
rewrite the input join.

Citations: [1]

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsLeftJoin`, not the other rules in that file):

```
# RejectNullsLeftJoin reduces a LeftJoin operator to an InnerJoin operator (or a
# FullJoin to a RightJoin) when there is a null-rejecting filter on any column
# from the right side. The effect of the null-rejecting filter is that output
# rows with all NULL values on the right side created by the left (or full) join
# are eliminated, making the join equivalent to an inner (or right) join. For
# example:
#
#   SELECT * FROM a LEFT OUTER JOIN b ON a.x = b.x WHERE b.y < 5
#
# can be reduced to:
#
#   SELECT * FROM a INNER JOIN b ON a.x = b.x WHERE b.y < 5
#
# since b.y < 5 is a null-rejecting filter on the right side.
#
# This rule is marked as high priority so that it runs before Select filter
# pushdown rules. Those rules may remove a filter before it's had a chance to
# rewrite the input join.
#
# Citations: [1]
[RejectNullsLeftJoin, Normalize, HighPriority]
(Select
    $input:(LeftJoin | LeftJoinApply | FullJoin
        $left:*
        $right:*
        $on:*
        $private:*
    )
    $filters:* &
        (HasNullRejectingFilter $filters (OutputCols $right))
)
=>
(Select
    (ConstructNonLeftJoin
        (OpName $input)
        $left
        $right
        $on
        $private
    )
    $filters
)
```
- Attempts used: 27
- Last updated: 2026-09-29T08:12:46.679766+00:00
- Reason / notes: The encoding faithfully captures the rule's core semantic transformation (a null-rejecting filter on the right side of a LEFT JOIN makes the null-extended rows disappear, reducing it to an INNER JOIN) with the filter position and join-condition sharing matching the source rule exactly. The restriction to the concrete IS_NOT_NULL operator is a genuine QED limitation (an uninterpreted predicate could return TRUE on NULL, breaking the equivalence, so QED must interpret the operator to verify null-rejection), and the omission of FullJoin→RightJoin and Apply variants is a real, honestly-documented narrowing. The proof is non-vacuous (LEFT vs INNER is a semantic difference that only holds because IS_NOT_NULL filters the null-extended rows), symbols are correctly independent/shared, and no preconditions are missing. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 35165292}, panicked=False

### `RejectNullsProject` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsProject adds a "col IS NOT NULL" null-rejecting filter to the input
of a project if the following conditions hold:
1. The parent Select operator rejects nulls on a synthesized (projection)
column.
2. At least one of the input columns of the projection is in the
RejectNullCols ColSet of the input of the Project.
3. The projection 'transmits' NULLS from the candidate column; if the column
is NULL, the output of the projection is NULL.

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsProject`, not the other rules in that file):

```
# RejectNullsProject adds a "col IS NOT NULL" null-rejecting filter to the input
# of a project if the following conditions hold:
#   1. The parent Select operator rejects nulls on a synthesized (projection)
#      column.
#   2. At least one of the input columns of the projection is in the
#      RejectNullCols ColSet of the input of the Project.
#   3. The projection 'transmits' NULLS from the candidate column; if the column
#      is NULL, the output of the projection is NULL.
[RejectNullsProject, Normalize]
(Select
    $input:(Project
            $innerInput:*
            $projections:* &
                ^(ColsAreEmpty
                    $projectionCols:(ProjectionCols $projections)
                )
            $passthrough:*
        ) &
        ^(ColsAreEmpty $rejectNullCols:(RejectNullCols $input))
    $filters:* &
        ^(ColsAreEmpty
            $nullRejectedCols:(IntersectionCols
                (IntersectionCols
                    $projectionCols
                    $rejectNullCols
                )
                (GetNullRejectedCols $filters)
            )
        )
)
=>
(Select
    (Project
        (Select
            $innerInput
            [
                (FiltersItem
                    (IsNot
                        (NullRejectProjections
                            $projections
                            $nullRejectedCols
                            (RejectNullCols $innerInput)
                        )
                        (Null (AnyType))
                    )
                )
            ]
        )
        $projections
        $passthrough
    )
    $filters
)
```
- Attempts used: 104
- Last updated: 2026-09-29T09:22:25.978757+00:00
- Reason / notes: The encoding faithfully captures the structural shape of RejectNullsProject (adding an IS NOT NULL filter below a Project while retaining the parent filter) for a concrete null-strict projection (UNARY_MINUS); the proof is non-vacuous because it requires QED's null-strictness model for UNARY_MINUS (an uninterpreted function would not satisfy the equivalence), and the SCOPE line honestly and specifically documents that the general null-transmission analysis over arbitrary projections is not modeled.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 20303666}, panicked=False

### `RejectNullsRightJoin` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsRightJoin is symmetric with RejectNullsLeftJoin. It reduces a
FullJoin operator to a LeftJoin when there is a null-rejecting filter on any
column from the left side.

This rule is marked as high priority for the same reason as
RejectNullsLeftJoin.

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsRightJoin`, not the other rules in that file):

```
# RejectNullsRightJoin is symmetric with RejectNullsLeftJoin. It reduces a
# FullJoin operator to a LeftJoin when there is a null-rejecting filter on any
# column from the left side.
#
# This rule is marked as high priority for the same reason as
# RejectNullsLeftJoin.
[RejectNullsRightJoin, Normalize, HighPriority]
(Select
    $input:(FullJoin $left:* $right:* $on:* $private:*)
    $filters:* &
        (HasNullRejectingFilter $filters (OutputCols $left))
)
=>
(Select (LeftJoin $left $right $on $private) $filters)
```
- Attempts used: 10
- Last updated: 2026-09-29T08:18:46.519566+00:00
- Reason / notes: The encoding is a genuine, non-vacuous special case of the source rule: before() uses a FULL join and after() a LEFT join (a real operator difference), with the source rule's semantic precondition "null-rejecting filter on a left column" instantiated as the canonical IS_NOT_NULL on the left column — the one null-rejecting filter QED can actually verify, since an uninterpreted predicate cannot be assumed to reject nulls, making this a fundamental QED limitation rather than a lazy narrowing. Symbol sharing is correct (same uninterpreted ON condition and same filter above the join in both plans, matching the rule's $on/$filters), no preconditions are missing (the identity Filter(FullJoin)≡Filter(LeftJoin) holds for exactly the rows the null-rejecting filter kills: the (NULL,r) null-extended-left rows), and the single-column/single-filter shape loses no semantic generality for this identity; the SCOPE: PARTIAL tag is accurate and specific.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 29602292}, panicked=False

### `RejectNullsUnderJoinLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsUnderJoinLeft adds "col IS NOT NULL" null-rejecting filters to the
left input of a join for each column that is both in the NullRejectCols ColSet
and is null-rejected by the join's filters. Note that a left join cannot be
matched even if its filters reject nulls on a column because left joins add
back unmatched columns to the output. RejectNullsUnderJoinLeft is low priority
to allow filters to be pushed down entirely, if possible.

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsUnderJoinLeft`, not the other rules in that file):

```
# RejectNullsUnderJoinLeft adds "col IS NOT NULL" null-rejecting filters to the
# left input of a join for each column that is both in the NullRejectCols ColSet
# and is null-rejected by the join's filters. Note that a left join cannot be
# matched even if its filters reject nulls on a column because left joins add
# back unmatched columns to the output. RejectNullsUnderJoinLeft is low priority
# to allow filters to be pushed down entirely, if possible.
[RejectNullsUnderJoinLeft, Normalize, LowPriority]
(InnerJoin | InnerJoinApply | SemiJoin | SemiJoinApply
    $left:* & ^(ColsAreEmpty $rejectCols:(RejectNullCols $left))
    $right:*
    $on:* &
        ^(ColsAreEmpty
            $nullRejectedCols:(IntersectionCols
                $rejectCols
                (GetNullRejectedCols $on)
            )
        )
    $private:*
)
=>
((OpName)
    (Select $left (MakeNullRejectFilters $nullRejectedCols))
    $right
    $on
    $private
)
```
- Attempts used: 22
- Last updated: 2026-09-29T08:31:51.091665+00:00
- Reason / notes: The porter's encoding is a faithful special case of RejectNullsUnderJoinLeft: the original rule fires when the ON condition null-rejects a left-input column (tracked via ColSet analysis) and pushes an IS NOT NULL filter below the join while leaving the ON condition unchanged; the porter instantiates this with the null-rejection made syntactically explicit as an IS_NOT_NULL conjunct in the ON condition (the one form QED can verify with an otherwise-uninterpreted predicate), yielding before=InnerJoin(L, R, on∧IS_NOT_NULL(l)) and after=InnerJoin(Filter(L, IS_NOT_NULL(l)), R, on∧IS_NOT_NULL(l)), which is exactly what the original rule produces in that sub-case. The proof is non-vacuous (the filter node is absent in before and present in after), the ON predicate is genuinely uninterpreted, the join kind and filter placement match the source rule, and the SCOPE tag honestly names both restrictions (InnerJoin-only; single explicit IS_NOT_NULL conjunct). ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 60469084}, panicked=False

### `RejectNullsUnderJoinRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsUnderJoinRight mirrors RejectNullsUnderJoinLeft.

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsUnderJoinRight`, not the other rules in that file):

```
# RejectNullsUnderJoinRight mirrors RejectNullsUnderJoinLeft.
[RejectNullsUnderJoinRight, Normalize, LowPriority]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | AntiJoin
    $left:*
    $right:* &
        ^(ColsAreEmpty $rejectCols:(RejectNullCols $right))
    $on:* &
        ^(ColsAreEmpty
            $nullRejectedCols:(IntersectionCols
                $rejectCols
                (GetNullRejectedCols $on)
            )
        )
    $private:*
)
=>
((OpName)
    $left
    (Select $right (MakeNullRejectFilters $nullRejectedCols))
    $on
    $private
)
```
- Attempts used: 5
- Last updated: 2026-09-29T08:41:06.891628+00:00
- Reason / notes: before() = Join(L, R, on ∧ IS_NOT_NULL(r)) and after() = Join(L, R⇝IS_NOT_NULL(r), on ∧ IS_NOT_NULL(r)) are genuinely different plans, and their bag equivalence is exactly the source rule's transformation (add an IS NOT NULL filter under the right input, leaving $on unchanged) instantiated to a case the rule truly covers — an ON condition explicitly null-rejecting right column r (the file header lists `x IS NOT NULL` as a null-rejecting expression, and bare scans satisfy the ColsAreEmpty guards, which are filter-firing hygiene, not semantic preconditions). Symbol sharing is correct and load-bearing: the IS_NOT_NULL conjunct in the ON and the pushed-down filter serialize as the same operator applied to the same underlying column (join field 1 vs. right field 0), and if they had been independent symbols the equivalence would not hold, so the acceptance confirms the sharing rather than masking an error; the uninterpreted `on` is likewise shared unchanged as the source rule requires. The SCOPE: PARTIAL line is honest and specific — only InnerJoin (of the source's six join kinds) and a single explicit IS_NOT_NULL conjunct (vs. analysis-based implicit rejection over possibly multiple columns) — making this a genuine, non-degenerate special case whose proved claim is a true, meaningful instance of the real optimization. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 59373708}, panicked=False

### `RemoveJoinNotNullCondition` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

RemoveJoinNotNullCondition removes a filter with an IS NOT NULL condition when
the given column has a NOT NULL constraint. Only left joins and full joins are
matched because filters can be pushed down from the ON conditions of inner and
semi joins.

Extracted from `join.opt` (which defines multiple rules — implement specifically `RemoveJoinNotNullCondition`, not the other rules in that file):

```
# RemoveJoinNotNullCondition removes a filter with an IS NOT NULL condition when
# the given column has a NOT NULL constraint. Only left joins and full joins are
# matched because filters can be pushed down from the ON conditions of inner and
# semi joins.
[RemoveJoinNotNullCondition, Normalize]
(LeftJoin | FullJoin
    $left:*
    $right:*
    $on:[
        ...
        $item:(FiltersItem
            (IsNot
                (Variable
                    $col:* & (IsColNotNull2 $col $left $right)
                )
                (Null)
            )
        )
        ...
    ]
    $private:*
)
=>
((OpName) $left $right (RemoveFiltersItem $on $item) $private)
```
- Attempts used: 23
- Last updated: 2026-09-29T08:49:48.711603+00:00
- Reason / notes: The encoding faithfully models the rule's core move — dropping a tautological `IS NOT NULL` conjunct from an outer join's ON condition — with the NOT NULL premise correctly carried by the left column's non-nullable `L_Type`; that premise is genuinely load-bearing, since with a nullable column the `IS_NOT_NULL` conjunct would not be removable from a LEFT JOIN (null-key rows would get NULL-extended vs. matched), so the proof is non-vacuous and `before()` ≠ `after()`. It is an honestly-tagged PARTIAL special case (LEFT join only, NOT NULL column on the left, remaining filters abstracted as one uninterpreted `rest` predicate), which is a real, non-degenerate subset of the source `LeftJoin | FullJoin` rule rather than a structurally trivial or over-constrained one.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 347208}, panicked=False

### `RemoveNotNullCondition` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

RemoveNotNullCondition removes a filter with an IS NOT NULL condition
when the given column has a NOT NULL constraint.

Extracted from `select.opt` (which defines multiple rules — implement specifically `RemoveNotNullCondition`, not the other rules in that file):

```
# RemoveNotNullCondition removes a filter with an IS NOT NULL condition
# when the given column has a NOT NULL constraint.
[RemoveNotNullCondition, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (IsNot
                (Variable $col:* & (IsColNotNull $col $input))
                (Null)
            )
        )
        ...
    ]
)
=>
(Select $input (RemoveFiltersItem $filters $item))
```
- Attempts used: 23
- Last updated: 2026-09-29T08:52:48.196660+00:00
- Reason / notes: The encoding faithfully captures the rule's single essential precondition — `IsColNotNull $col $input` — by giving column 0 a non-nullable type, and the rewrite differs before/after by exactly the tautological `IS NOT NULL` conjunct on that column, with all remaining filter items collapsed into one uninterpreted predicate (exactly equivalent to a list of conjuncts). The proof being accepted confirms the prover concretely interprets `IS NOT NULL` via the schema's nullability metadata — had it been treated as an uninterpreted symbol, `filter(other).filter(isNotNull) ≡ filter(other)` would be unprovable — so the proof is of the genuine claim, not a vacuous one. The choices to fix the subject column at ordinal 0 and use two columns are semantically inert (the equivalence is local to the filter operator and depends only on that column's nullability, not its position or the input's column count), and since Select filter lists are conjunctions, the single `other` predicate covers arbitrary remaining items; this is the full rule, so `// SCOPE: FULL` is honest.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 410209}, panicked=False

### `RemoveZeroCardLock` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/mutation.opt

RemoveZeroCardLock removes lock operations when we know no rows will be locked.

Extracted from `mutation.opt` (which defines multiple rules — implement specifically `RemoveZeroCardLock`, not the other rules in that file):

```
# RemoveZeroCardLock removes lock operations when we know no rows will be locked.
[RemoveZeroCardLock, Normalize]
(Lock $rows:* & (HasZeroRows $rows))
=>
$rows
```
- Attempts used: 43
- Last updated: 2026-09-29T09:28:20.647367+00:00
- Reason / notes: The rule only fires when `$rows` has zero rows — i.e. `$rows` is bag-equal to ∅ — so modeling `$rows` as a structurally empty scan and `Lock` as an uninterpreted filter encodes the rule's entire applicable domain (`filter(∅) = ∅`), not an arbitrary narrowing. `before()` (a Filter over the empty relation) genuinely differs from `after()` (the bare empty relation), so the proof is non-vacuous and reflects the real optimization of dropping a lock over zero rows rather than being structurally identical. The PARTIAL scope tag is honest and specific, correctly naming the genuine limitation (the DSL has no Lock operator or cardinality constraint to express `HasZeroRows` as a guard).
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 163750}, panicked=False

### `RightAssociateJoinsLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

RightAssociateJoinsLeft is a variant on LeftAssociateJoinsLeft.
The transformation:

SELECT * FROM ab
INNER JOIN (SELECT * FROM xy INNER JOIN uv ON True)
ON a=x AND b=u
=>
SELECT * FROM (SELECT * FROM ab INNER JOIN xy ON a=x)
INNER JOIN uv
ON b=u

Extracted from `join.opt` (which defines multiple rules — implement specifically `RightAssociateJoinsLeft`, not the other rules in that file):

```
# RightAssociateJoinsLeft is a variant on LeftAssociateJoinsLeft.
# The transformation:
#
#   SELECT * FROM ab
#   INNER JOIN (SELECT * FROM xy INNER JOIN uv ON True)
#   ON a=x AND b=u
# =>
#   SELECT * FROM (SELECT * FROM ab INNER JOIN xy ON a=x)
#   INNER JOIN uv
#   ON b=u
#
[RightAssociateJoinsLeft, Normalize, LowPriority]
(InnerJoin
    $outsideLeft:*
    (InnerJoin
        $insideLeft:*
        $insideRight:*
        []
        $insidePrivate:* & (NoJoinHints $insidePrivate)
    )
    $outsideOn:[
        ...
        $item:* &
            (IsBoundBy
                $item
                $cols:(OutputCols2 $insideLeft $outsideLeft)
            )
        ...
    ]
    $outsidePrivate:* & (NoJoinHints $outsidePrivate)
)
=>
(InnerJoin
    (InnerJoin
        $outsideLeft
        $insideLeft
        (ExtractBoundConditions $outsideOn $cols)
        (EmptyJoinPrivate)
    )
    $insideRight
    (ExtractUnboundConditions $outsideOn $cols)
    (EmptyJoinPrivate)
)
```
- Attempts used: 23
- Last updated: 2026-09-29T09:14:54.038215+00:00
- Reason / notes: The encoding faithfully models the source rule: before is `A ⋈(B ⋈_true C)` with condition `p_ab(A,B) ∧ p_abc(A,B,C)`, and after is `(A ⋈_p_ab B) ⋈_p_abc C` — exactly the reassociation with condition-splitting the Optgen rule prescribes (inner ON is empty/true, bound-to-(A,B) conjuncts move into the new inner join, the rest stay outer). Both predicates are uninterpreted symbols shared correctly across before/after, all join kinds are INNER as required, and the only omitted source constraints (NoJoinHints) are optimizer-firing guards with no bag-semantic effect, so the SCOPE: FULL tag is accurate. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 70670209}, panicked=False

### `RightAssociateJoinsRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

RightAssociateJoinsRight is a variant on LeftAssociateJoinsLeft.
The transformation:

SELECT * FROM ab
INNER JOIN (SELECT * FROM xy INNER JOIN uv ON True)
ON a=x AND b=u
=>
SELECT * FROM (SELECT * FROM ab INNER JOIN uv ON b=u)
INNER JOIN xy
ON a=x

Extracted from `join.opt` (which defines multiple rules — implement specifically `RightAssociateJoinsRight`, not the other rules in that file):

```
# RightAssociateJoinsRight is a variant on LeftAssociateJoinsLeft.
# The transformation:
#
#   SELECT * FROM ab
#   INNER JOIN (SELECT * FROM xy INNER JOIN uv ON True)
#   ON a=x AND b=u
# =>
#   SELECT * FROM (SELECT * FROM ab INNER JOIN uv ON b=u)
#   INNER JOIN xy
#   ON a=x
#
[RightAssociateJoinsRight, Normalize, LowPriority]
(InnerJoin
    $outsideLeft:*
    (InnerJoin
        $insideLeft:*
        $insideRight:*
        []
        $insidePrivate:* & (NoJoinHints $insidePrivate)
    )
    $outsideOn:[
        ...
        $item:* &
            (IsBoundBy
                $item
                $cols:(OutputCols2 $insideRight $outsideLeft)
            )
        ...
    ]
    $outsidePrivate:* & (NoJoinHints $outsidePrivate)
)
=>
(InnerJoin
    (InnerJoin
        $outsideLeft
        $insideRight
        (ExtractBoundConditions $outsideOn $cols)
        (EmptyJoinPrivate)
    )
    $insideLeft
    (ExtractUnboundConditions $outsideOn $cols)
    (EmptyJoinPrivate)
)
```
- Attempts used: 45
- Last updated: 2026-09-29T09:41:54.703243+00:00
- Reason / notes: The encoding exactly mirrors the source variant: before = A ⋈_{p_ac∧p_abc} (B ⋈_true C), after = (A ⋈_{p_ac} C) ⋈_{p_abc} B, with INNER join kinds, a true literal for the empty inner ON list, and the same uninterpreted predicates applied to the same column values (I verified every joinField index and argument order: p_ac always sees (A,C), p_abc always sees (A,B,C)). The two-predicate split is fully general since any outer condition P(A,B,C) can be written as true ∧ P, the source rule's guards (NoJoinHints, existence of a bound conjunct) are applicability conditions rather than soundness preconditions, and the trailing projection only aligns the (A,C,B)→(A,B,C) output column order that the source rule changes implicitly via column IDs — so the proof is of the genuine, non-vacuous reassociation the rule performs.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 71880333}, panicked=False

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

### `SimplifyAndTrue` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyAndTrue simplifies the And operator by discarding a True condition on
the right side.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyAndTrue`, not the other rules in that file):

```
# SimplifyAndTrue simplifies the And operator by discarding a True condition on
# the right side.
[SimplifyAndTrue, Normalize]
(And $left:* (True))
=>
$left
```
- Attempts used: 3
- Last updated: 2026-09-29T09:24:16.029895+00:00
- Reason / notes: The encoding is a faithful relational embedding of CockroachDB's scalar rule (And $left:* (True)) => $left: before() is Filter(And(P, True), R) and after() is Filter(P, R), which are structurally different, so the proof is non-vacuous. `left` is a single uninterpreted predicate shared by both sides (correct symbol sharing, not over-constrained), the base scan carries no uniqueness/key assumption, and since QED's proof is universal over all instantiations of that predicate, every possible boolean expression for $left — including compound ones and three-valued NULL cases, where TRUE is genuinely the identity of AND — is covered; nothing the original rule requires is assumed away, so `// SCOPE: FULL` is honest. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 357459}, panicked=False

### `SimplifyCaseSingleFalsyBranch` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

SimplifyCaseSingleFalsyBranch replaces a CASE expression in a filter with a
falsy ELSE branch when it has a single WHEN branch that evaluates to False or
Null.

For example:

SELECT * FROM abc WHERE CASE WHEN a > 1 THEN false ELSE false END
=>
SELECT * FROM abc WHERE false

NOTE: The optimizer guarantees that CASE branches with side-effects will only
be evaluated if their conditions are true and the ELSE branch with
side-effects will only be evaluated if non of the branch conditions are true
(see props.VolatilitySet). Therefore, this rule is only valid when the ELSE
branch is leakproof. We currently only match constant False and Null values,
which are guaranteed to be leakproof.

Extracted from `select.opt` (which defines multiple rules — implement specifically `SimplifyCaseSingleFalsyBranch`, not the other rules in that file):

```
# SimplifyCaseSingleFalsyBranch replaces a CASE expression in a filter with a
# falsy ELSE branch when it has a single WHEN branch that evaluates to False or
# Null.
#
# For example:
#
#  SELECT * FROM abc WHERE CASE WHEN a > 1 THEN false ELSE false END
#  =>
#  SELECT * FROM abc WHERE false
#
# NOTE: The optimizer guarantees that CASE branches with side-effects will only
# be evaluated if their conditions are true and the ELSE branch with
# side-effects will only be evaluated if non of the branch conditions are true
# (see props.VolatilitySet). Therefore, this rule is only valid when the ELSE
# branch is leakproof. We currently only match constant False and Null values,
# which are guaranteed to be leakproof.
[SimplifyCaseSingleFalsyBranch, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Case
                (True)
                $whens:[ (When * (False | Null)) ]
                $orElse:(False | Null)
            )
        )
        ...
    ]
)
=>
(Select $input (ReplaceFiltersItem $filters $item $orElse))
```
- Attempts used: 47
- Last updated: 2026-10-16T07:59:24.590527+00:00
- Reason / notes: The encoding is non-vacuous and faithful: the WHEN condition is a proper uninterpreted predicate, the 3VL expansion (cond∧False)∨(¬cond∧Null) matches the CASE for all three values of cond, both sides are genuinely all-dropping filters, and the leakproof precondition is honored via constants. The SCOPE line honestly discloses the specific False/Null restriction (the only combination expressible via AND/OR/NOT), and the only further implicit narrowing — a single-item filter list — is compositionally trivial since both sides are empty regardless of other conjuncts.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 392083}, panicked=False

### `SimplifyEqualsAnyTuple` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifyEqualsAnyTuple converts a scalar ANY operation to an IN comparison.
It transforms

x = ANY (...)

to

x IN (...)

Which allows scans to be constrained.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyEqualsAnyTuple`, not the other rules in that file):

```
# SimplifyEqualsAnyTuple converts a scalar ANY operation to an IN comparison.
# It transforms
#
#   x = ANY (...)
#
# to
#
#   x IN (...)
#
# Which allows scans to be constrained.
[SimplifyEqualsAnyTuple, Normalize]
(AnyScalar $input:* $tuple:(Tuple) $cmp:* & (OpsAreSame $cmp Eq))
=>
(In $input $tuple)
```
- Attempts used: 24
- Last updated: 2026-10-16T09:20:07.941798+00:00
- Reason / notes: before() (S SEMI JOIN V ON S.c0=V.c0) and after() (S FILTER (S.c0 IN (SELECT c0 FROM V))) are structurally distinct relational forms of the same scalar identity, so the proof is non-vacuous, and the correspondence (semi-join = `= ANY`, IN-subquery filter = `IN`) matches the rule's direction and semantics under bag/3VL semantics for all instantiations, including duplicate and empty V; fixing the comparison to concrete EQUALS is faithful because the source rule's `(OpsAreSame $cmp Eq)` guard restricts it to Eq, modeling x as an uninterpreted column S.c0 and the tuple as an arbitrary uninterpreted 1-column relation V is at least as general as a scalar input and constant tuple, with S and V correctly shared and no dropped preconditions. The FULL scope tag is therefore the correct and honest classification.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 76659417}, panicked=False

### `SimplifyExcept` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/set.opt

SimplifyExcept converts an Except operator into an ExceptAll operator when the
left input has a key. This avoids the de-duplication step.

Extracted from `set.opt` (which defines multiple rules — implement specifically `SimplifyExcept`, not the other rules in that file):

```
# SimplifyExcept converts an Except operator into an ExceptAll operator when the
# left input has a key. This avoids the de-duplication step.
[SimplifyExcept, Normalize]
(Except $left:* & (HasStrictKey $left) $right:* $colMap:*)
=>
(ExceptAll $left $right $colMap)
```
- Attempts used: 14
- Last updated: 2026-10-16T07:58:55.191254+00:00
- Reason / notes: before() correctly encodes set EXCEPT (`minus(all=false)`), and although after() uses an ANTI join rather than the source's `ExceptAll` (bag MINUS is unmodelable by QED's serializer, which only handles the set variant), the two are semantically identical under the encoded `HasStrictKey` precondition (unique left scan), so the proof captures the rule's actual claim — "no de-duplication step needed, output is left rows absent from right" — as a stronger, genuine universal statement that is non-vacuous (multiplicities diverge if the left key is dropped, e.g. L=[a,a], R=[b]). The two scans are properly independent symbols sharing a type (as required by minus compatibility), the key precondition is present, and the SCOPE: PARTIAL line honestly and specifically lists the real restrictions (single column, identity colmap, left a declared-unique scan).
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 62428750}, panicked=False

### `SimplifyFalseAnd` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyFalseAnd maps the And operator to False if its left input is False.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyFalseAnd`, not the other rules in that file):

```
# SimplifyFalseAnd maps the And operator to False if its left input is False.
[SimplifyFalseAnd, Normalize]
(And $left:(False) *)
=>
$left
```
- Attempts used: 3
- Last updated: 2026-09-29T10:38:53.960289+00:00
- Reason / notes: The before pattern is `Filter(Source, AND(false, right))` and after is `Filter(Source, false)`, with `right` an uninterpreted predicate and `Source` an arbitrary scan, faithfully matching `(And False *) => False`. It is nontrivial, uses the correct left-false `And` variant rather than the right-false sibling, and does not hard-code the arbitrary operand.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 297042}, panicked=False

### `SimplifyFalseOr` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyFalseOr simplifies the Or operator by discarding a False condition on
the left side.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyFalseOr`, not the other rules in that file):

```
# SimplifyFalseOr simplifies the Or operator by discarding a False condition on
# the left side.
[SimplifyFalseOr, Normalize]
(Or (False) $right:*)
=>
$right
```
- Attempts used: 10
- Last updated: 2026-09-29T10:50:28.385823+00:00
- Reason / notes: The encoding captures the rule's only logical content — the boolean identity Or(False, P) ≡ P, with P a fully uninterpreted predicate over an uninterpreted table and Or/False as interpreted connectives — and `before()` and `after()` are structurally distinct, so the proof is a genuine, non-vacuous identity rather than a triviality. The narrowing to "the entire predicate of a Filter over a single scan" is honestly and specifically tagged on the SCOPE line and is a real limitation of the pattern language (RuleScript/QED can only quantify over uninterpreted symbols at fixed positions — there is no boolean-expression or relation "hole" to express an arbitrary context or base relation), not an avoidable under-generalization; since the identity is a valid boolean equivalence in any context (2-valued or three-valued), no logical content is lost by fixing the carrier. No other failure mode applies: the left-False Or position matches the source exactly, the single `right` symbol is correctly shared between both sides, and the source rule is unconditional, so no preconditions (keys, NOT NULL) are silently omitted.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 293541}, panicked=False

### `SimplifyInSingleElement` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyInSingleElement`, not the other rules in that file):

```
[SimplifyInSingleElement, Normalize]
(In $left:* (Tuple [ $right:* ]))
=>
(Eq $left $right)
```
- Attempts used: 27
- Last updated: 2026-09-29T11:00:04.378829+00:00
- Reason / notes: The encoding models `x IN (y)` by its definitional expansion — a single-disjunct `Or(x = y)` — in `before()`, and the bare `Eq` in `after()`; since the DSL has no `IN` operator and an uninterpreted `IN` symbol could never be proven equivalent to an independent `Eq` symbol, proving `Filter(Or(eq)) ≡ Filter(eq)` is exactly the non-vacuous semantic content of the source rule (single-element IN ≡ equality, valid in three-valued logic with no added preconditions). `before()` and `after()` are structurally different (Or-wrapper vs bare predicate), so the proof is not vacuous; the two operands are arbitrary columns of a shared uninterpreted nullable type, so no relationship between `$left` and `$right` is assumed, and the filter-over-scan embedding is the standard representative of this position-independent scalar rule, making the FULL scope tag honest. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 349458}, panicked=False

### `SimplifyIntersectLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/set.opt

SimplifyIntersectLeft converts an Intersect operator into an IntersectAll
operator when the left input has a key. This avoids the de-duplication step.

Extracted from `set.opt` (which defines multiple rules — implement specifically `SimplifyIntersectLeft`, not the other rules in that file):

```
# SimplifyIntersectLeft converts an Intersect operator into an IntersectAll
# operator when the left input has a key. This avoids the de-duplication step.
[SimplifyIntersectLeft, Normalize]
(Intersect $left:* & (HasStrictKey $left) $right:* $colMap:*)
=>
(IntersectAll $left $right $colMap)
```
- Attempts used: 25
- Last updated: 2026-09-29T11:08:45.705265+00:00
- Reason / notes: before() (set-semantic INTERSECT, all=false, 2 inputs) and after() (SEMI join of the keyed left against the right on IS NOT DISTINCT FROM) are structurally distinct operators, and the strict-key precondition is genuinely encoded — the left scan is declared with a key on its only column (unique=true → table key [0]) — so the SMT proof is of the real claim "keyed left ⇒ no dedup needed," not a vacuous identity. Using a semi-join for the target is faithful, not a wrong operator: IntersectAll with a unique left side is bag-identical to L SEMI-JOIN R (min(multiplicity) collapses to membership), and QED fundamentally cannot model the bag variant of INTERSECT, so the semi-join is the only expressible form of the rule's target. The restrictions (2-input, single column, same/identity column mapping, left as a base unique scan, non-nullable matching strict-key semantics) are all disclosed in the SCOPE line, are genuine QED-expressibility boundaries rather than avoidable under-generalization, and the proven instance remains a non-degenerate core case of the source rule. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 61596541}, panicked=False

### `SimplifyIntersectRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/set.opt

SimplifyIntersectRight converts an Intersect operator into an IntersectAll
operator when the right input has a key. This avoids the de-duplication step.

This works because IntersectAll creates a one-to-one mapping between left and
right rows. If there is more than one row with a particular value on the
left side, then there must be at least that many rows with the same value on
the right side in order for the left rows to be preserved in the output.
Therefore, if the right input has a strict key, the output rows will be
de-duplicated for 'free', and an IntersectAll can safely be used.

Extracted from `set.opt` (which defines multiple rules — implement specifically `SimplifyIntersectRight`, not the other rules in that file):

```
# SimplifyIntersectRight converts an Intersect operator into an IntersectAll
# operator when the right input has a key. This avoids the de-duplication step.
#
# This works because IntersectAll creates a one-to-one mapping between left and
# right rows. If there is more than one row with a particular value on the
# left side, then there must be at least that many rows with the same value on
# the right side in order for the left rows to be preserved in the output.
# Therefore, if the right input has a strict key, the output rows will be
# de-duplicated for 'free', and an IntersectAll can safely be used.
[SimplifyIntersectRight, Normalize]
(Intersect $left:* $right:* & (HasStrictKey $right) $colMap:*)
=>
(IntersectAll $left $right $colMap)
```
- Attempts used: 48
- Last updated: 2026-09-29T11:28:48.610680+00:00
- Reason / notes: The encoding is non-vacuous — a set-INTERSECT (before) vs. a SEMI-join (after) are genuinely different operators, and the equality hinges on the right scan's `unique=true` + non-nullable type, which faithfully models `HasStrictKey` (uniqueness *and* non-null), so the precondition is present rather than missing. Under that strict-key precondition the encoded after() (R SEMI JOIN L) is definitionally the same bag as the source rule's IntersectAll RHS — both yield exactly the right-side rows whose values also occur in the left — so the proof certifies the rule's actual content (the de-duplication is eliminable), which is the right workaround since QED models only the set (not bag) variant of INTERSECT and cannot express IntersectAll directly. The narrowing to 2 inputs (the rule's own shape), one shared column, and identity column mapping is a genuine DSL limitation (multi-column scans cannot carry a uniqueness key) that is honestly tagged PARTIAL and does not dilute the semantic argument, which is purely about per-row value equality and multiplicity. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 69542875}, panicked=False

### `SimplifyIsCondition` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

SimplifyIsCondition replaces x IS NOT DISTINCT FROM y with x = y. This
transformation is only valid if all of the following are true:

1. The expression is in the context of filtering where NULL is falsy.
2. One of x or y is non-nullable. This is required because while the
expression NULL IS NOT DISTINCT FROM NULL is true, NULL=NULL is NULL
(falsy).
3. Neither x nor y is a tuple. Tuples with NULLs have all sorts of
complicated edge cases, so we avoid them entirely. See #48299.

We conservatively also require the types of x and y to be identical. It may be
possible to lift this restriction if we can prove that it is not necessary.

Extracted from `select.opt` (which defines multiple rules — implement specifically `SimplifyIsCondition`, not the other rules in that file):

```
# SimplifyIsCondition replaces x IS NOT DISTINCT FROM y with x = y. This
# transformation is only valid if all of the following are true:
#
#   1. The expression is in the context of filtering where NULL is falsy.
#   2. One of x or y is non-nullable. This is required because while the
#      expression NULL IS NOT DISTINCT FROM NULL is true, NULL=NULL is NULL
#      (falsy).
#   3. Neither x nor y is a tuple. Tuples with NULLs have all sorts of
#      complicated edge cases, so we avoid them entirely. See #48299.
#
# We conservatively also require the types of x and y to be identical. It may be
# possible to lift this restriction if we can prove that it is not necessary.
[SimplifyIsCondition, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Is
                $left:* & ^(IsTuple $left)
                $right:* &
                    ^(IsTuple $right) &
                    (IdenticalTypes
                        (TypeOf $left)
                        (TypeOf $right)
                    ) &
                    (EitherExprIsNeverNull
                        $left
                        $right
                        (NotNullCols $input)
                    )
            )
        )
        ...
    ]
)
=>
(Select
    $input
    (ReplaceFiltersItem $filters $item (Eq $left $right))
)
```
- Attempts used: 24
- Last updated: 2026-09-29T11:16:07.492718+00:00
- Reason / notes: The encoding matches the rule exactly in every load-bearing respect: before() applies `IS_NOT_DISTINCT_FROM` and after() applies `EQUALS` (the rule's actual rewrite, per its "replaces x IS NOT DISTINCT FROM y with x = y" comment), in a Filter (the NULL-is-falsy context the rule requires), with the other filter items correctly abstracted as a single uninterpreted conjunction shared by both sides; all four source preconditions are present — scalar (non-tuple) operands, `IdenticalTypes` via the shared type symbol V, and the never-null guarantee via the non-nullable column type. The only narrowing — taking just the left-operand-never-null branch of `EitherExprIsNeverNull` (right-nullable rather than also the symmetric branch) — is honestly declared in the PARTIAL scope line, is genuinely forced by the DSL (per-column nullability is a static type property, so a single before/after pair cannot express the disjunction), and is the strictly more general of the expressible alternatives (it subsumes the both-non-null case), leaving a non-vacuous proof that exercises the actual NULL-handling distinction between the two operators. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 68120000}, panicked=False

### `SimplifyIsNullCondition` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

SimplifyIsNullCondition replaces a filter with an IS NULL condition with
False when the given column has a NOT NULL constraint.

Extracted from `select.opt` (which defines multiple rules — implement specifically `SimplifyIsNullCondition`, not the other rules in that file):

```
# SimplifyIsNullCondition replaces a filter with an IS NULL condition with
# False when the given column has a NOT NULL constraint.
[SimplifyIsNullCondition, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Is
                (Variable $col:* & (IsColNotNull $col $input))
                (Null)
            )
        )
        ...
    ]
)
=>
(Select $input [ (FiltersItem (False)) ])
```
- Attempts used: 22
- Last updated: 2026-09-29T11:17:30.114695+00:00
- Reason / notes: The encoding is structurally non-vacuous and captures the exact logical content of the rule: `before()` is the conjunction of an arbitrary uninterpreted predicate `other` with `x IS NULL` on a non-nullable column 0, and `after()` is a filter by the false literal, so the proven claim is precisely "a conjunct refutable by a NOT NULL guarantee makes the whole filter refutable, degenerating the Select to a false filter" — which is what the source rule does (replace the entire filter list with `[False]`). The key precondition `IsColNotNull` is faithfully encoded via the non-nullable `Col_Type`, and the near-zero proof time is consistent with the prover constant-folding IS NULL on a non-nullable column to false, making both sides empty; using the concrete `SqlStdOperatorTable.IS_NULL` (rather than an uninterpreted symbol) is correct because that null-awareness is the whole mechanism of the rule, and the other conjuncts are correctly left as a single uninterpreted predicate (instantiable to true, covering the zero-extra-conjuncts case). The 2-column scan (NOT NULL column + nullable stand-in for all remaining columns, touched only by the uninterpreted predicate) is the standard faithful stand-in for an arbitrary input relation of any width, the nested-filter conjunction matches the source's filter list, and the input symbol is shared unchanged between sides, so `// SCOPE: FULL` is honest. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 190834}, panicked=False

### `SimplifyJoinFilters` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

SimplifyJoinFilters works like SimplifySelectFilters, except that it operates
on Join filters rather than Select filters.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyJoinFilters`, not the other rules in that file):

```
# SimplifyJoinFilters works like SimplifySelectFilters, except that it operates
# on Join filters rather than Select filters.
[SimplifyJoinFilters, Normalize, HighPriority]
(Join
    $left:*
    $right:*
    $on:[
            ...
            $item:(FiltersItem
                    (And | True | False | Null | Or | Is)
                ) &
                ^(IsUnsimplifiableOr $item) &
                ^(IsUnsimplifiableIs $item) &
                ^(IsContradiction $item)
            ...
        ] &
        ^(IsFilterFalse $on)
    $private:*
)
=>
((OpName) $left $right (SimplifyFilters $on) $private)
```
- Attempts used: 21
- Last updated: 2026-09-29T11:28:45.414874+00:00
- Reason / notes: before() (`ON rest AND True`) and after() (`ON rest`) are structurally distinct, share exactly one uninterpreted `rest` predicate over the join fields with independent L/R scans, and use the correct conjunction/inner-join shape, so the proof is a genuine, non-vacuous check rather than a coincidental over-constraint; the source rule's guards (IsUnsimplifiableOr/Is, IsContradiction, IsFilterFalse) are anti-firing conditions, and their absence only strengthens the proved claim, since the equivalence holds for every instantiation of `rest`. The restriction to True-item removal on INNER joins is honestly declared in the SCOPE line, is specific and genuine, and is largely forced by the single-file contract — the full SimplifyFilters is a backend simplification function that no single structural rewrite can express, and one before/after pair can fix only one join kind — while the result remains a non-degenerate, verifiable instance of the real rule's behavior (True is among the item kinds the source rule explicitly matches). ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 342250}, panicked=False

### `SimplifyJoinNotNullEquality` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

SimplifyJoinNotNullEquality simplifies an Is/IsNot equality filter condition
when it's not possible for it to be null, as in the following case:

WHERE (a=b) IS NOT False

If a and b are not null, then this can be simplified to:

WHERE a=b

This pattern can be generated by the NormalizeNotAnyFilter rule, and its
simplification is necessary for making anti-joins efficient, such as in TPCH
query 16.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyJoinNotNullEquality`, not the other rules in that file):

```
# SimplifyJoinNotNullEquality simplifies an Is/IsNot equality filter condition
# when it's not possible for it to be null, as in the following case:
#
#   WHERE (a=b) IS NOT False
#
# If a and b are not null, then this can be simplified to:
#
#   WHERE a=b
#
# This pattern can be generated by the NormalizeNotAnyFilter rule, and its
# simplification is necessary for making anti-joins efficient, such as in TPCH
# query 16.
[SimplifyJoinNotNullEquality, Normalize]
(Join
    $left:*
    $right:*
    $on:[
        ...
        $item:(FiltersItem
            $condition:(Is | IsNot
                $eq:(Eq
                    # Check whether variable is a not-null column of left or right input.
                    (Variable
                        $col1:* &
                            (IsColNotNull2 $col1 $left $right)
                    )
                    (Variable
                        $col2:* &
                            (IsColNotNull2 $col2 $left $right)
                    )
                )
                $cnst:(True | False | Null)
            )
        )
        ...
    ]
    $private:*
)
=>
((OpName)
    $left
    $right
    (ReplaceFiltersItem
        $on
        $item
        (SimplifyNotNullEquality
            $eq
            (OpName $condition)
            (OpName $cnst)
        )
    )
    $private
)
```
- Attempts used: 72
- Last updated: 2026-09-29T12:00:27.517982+00:00
- Reason / notes: The encoding is a faithful, non-trivial special case: it correctly models the `IsNot(Eq, False) → Eq` sub-case with non-null column types (matching the source's `IsColNotNull2` precondition), an uninterpreted `rest` predicate for the other filter items, and structurally distinct before/after patterns whose equivalence requires genuine three-valued-logic reasoning (not a vacuous identity). The SCOPE line honestly and specifically acknowledges the two real restrictions (only 1 of 6 Is/IsNot×True/False/Null sub-cases; INNER join only), and the single-column scan choice does not weaken the logical content since `rest` is uninterpreted regardless of arity.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 375709}, panicked=False

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

### `SimplifyLeftJoinWithZeroRowsRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

SimplifyLeftJoinWithZeroRowsRight replaces a LeftJoin with a Project when
the right input never returns any rows. The Project passes through columns
from the left input and produces NULL values for each column in the right
input.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyLeftJoinWithZeroRowsRight`, not the other rules in that file):

```
# SimplifyLeftJoinWithZeroRowsRight replaces a LeftJoin with a Project when
# the right input never returns any rows. The Project passes through columns
# from the left input and produces NULL values for each column in the right
# input.
[SimplifyLeftJoinWithZeroRowsRight, Normalize]
(LeftJoin $left:* $right:* & (HasZeroRows $right))
=>
(Project $left (MakeNullProjections $right) (OutputCols $left))
```
- Attempts used: 29
- Last updated: 2026-09-29T12:16:29.000689+00:00
- Reason / notes: The encoding faithfully mirrors the source rule — a LEFT join whose right input is structurally the empty relation (the exact bag-theoretic reading of `HasZeroRows`) rewritten to a projection that passes the left's columns through and emits NULL literals typed by the right's columns — with independent left/right scans and independent types, and structurally different before/after trees (Join vs. ProjectMany), so the proof is of the actual rewrite, not a vacuous identity. The PARTIAL scope tag is honest and the restriction is genuine rather than an avoidable DSL gap: QED decides equivalence of concrete queries, so universally-quantified arbitrary-width `$left`/`$right` simply cannot be expressed, and the fixed two-column shape remains a non-degenerate special case that still exercises the real optimization (eliminating the join in favor of a null-project for arbitrary left-side content). ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 46614958}, panicked=False

### `SimplifyNotInSingleElement` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyNotInSingleElement`, not the other rules in that file):

```
[SimplifyNotInSingleElement, Normalize]
(NotIn $left:* (Tuple [ $right:* ]))
=>
(Ne $left $right)
```
- Attempts used: 6
- Last updated: 2026-09-29T11:58:26.352857+00:00
- Reason / notes: The encoding is a faithful relational statement of the scalar rule: before() builds `left NOT IN (right)` as filter `NOT(left = right)` (single-element IN = one-arg Or of `left = right`, then Not), and after() builds `Ne` as filter `left <> right`, so QED's universal proof that these filters agree over all contents of a two-column table is exactly the semantic validity of the CockroachDB rewrite. left and right are two *distinct* uninterpreted columns of a common uninterpreted type V (no over-constraint), and no precondition is missing because `NOT(x = y)` and `x <> y` share the same three-valued truth table including the NULL case; the only adaptation—embedding the scalar rewrite inside a Filter—is a necessary relational-DSL artifact rather than a semantic narrowing, so the `SCOPE: FULL` tag is honest. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 360166}, panicked=False

### `SimplifyOrFalse` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyOrFalse simplifies the Or operator by discarding a False condition on
the right side.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyOrFalse`, not the other rules in that file):

```
# SimplifyOrFalse simplifies the Or operator by discarding a False condition on
# the right side.
[SimplifyOrFalse, Normalize]
(Or $left:* (False))
=>
$left
```
- Attempts used: 8
- Last updated: 2026-09-29T12:15:22.368422+00:00
- Reason / notes: The encoding faithfully captures the core boolean identity of SimplifyOrFalse (Or(x, False) ≡ x) using a properly uninterpreted predicate `left` as the arbitrary left operand, with a structurally non-trivial before/after pair (filter with Or vs. filter alone). The SCOPE: PARTIAL tag is honest and specific — the restriction to "entire filter predicate, not nested" is a genuine scoping choice in a relational prover that cannot express arbitrary scalar-position rewrites, and the result remains a useful, non-degenerate proof of the identity for all instantiations of the uninterpreted symbol. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 78583}, panicked=False

### `SimplifyOrTrue` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyOrTrue maps the Or operator to True if its right input is True.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyOrTrue`, not the other rules in that file):

```
# SimplifyOrTrue maps the Or operator to True if its right input is True.
[SimplifyOrTrue, Normalize]
(Or * $right:(True))
=>
$right
```
- Attempts used: 7
- Last updated: 2026-09-29T12:13:00.071234+00:00
- Reason / notes: The encoding is a direct, faithful translation of `SimplifyOrTrue`: the LHS `(Or * True)` becomes the filter condition `Or(left, True)` where `left` is an uninterpreted predicate standing in for the `*` wildcard and the `True` literal sits on the right exactly as the rule specifies, while the RHS `$right` (i.e. `True`) becomes `filter(trueLiteral)` — doing precisely the boolean simplification and nothing more (the filter is left in place, matching the source rule rather than over-reaching to delete it). It is non-vacuous (the `Or` appears only in `before()`), has no spurious symbol sharing and no omitted preconditions, and because `left` is uninterpreted the proof establishes the full identity `Or(x, True) = True` for every predicate `x` in the minimal relational (filter-over-scan) context the DSL requires, so the `SCOPE: FULL` tag is accurate. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 68458}, panicked=False

### `SimplifyRange` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyRange simplifies a Range operator for which the input is no longer an
And expression, likely due to simplification of the And operator itself.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyRange`, not the other rules in that file):

```
# SimplifyRange simplifies a Range operator for which the input is no longer an
# And expression, likely due to simplification of the And operator itself.
[SimplifyRange, Normalize]
(Range $input:^(And))
=>
$input
```
- Attempts used: 98
- Last updated: 2026-09-29T13:17:13.556779+00:00
- Reason / notes: The source rule is sound precisely because CockroachDB's Range is a semantically transparent boolean marker, and since QED cannot attach an axiom like range(x)=x to an uninterpreted function symbol, building the transparency in concretely (as the no-op conjunct `input AND true`) is the only faithful provable encoding — the porter proved its removal from a filter condition preserves bag semantics for an arbitrary uninterpreted predicate over an arbitrary table, with before()/after() structurally distinct (the wrapper conjunct appears only on the LHS) and the single shared `input` symbol correctly mirroring the same `$input` on both sides. The original `x:^(And)` guard limits rule firing rather than being a soundness precondition, so omitting it proves a strictly stronger claim, and the `SCOPE: PARTIAL` line honestly and specifically discloses the Range↦(input AND true) modeling and the filter-context embedding, which is the canonical boolean context for the rule and a genuine, non-degenerate special case.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 296083}, panicked=False

### `SimplifyRightJoin` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

SimplifyRightJoin reduces a FullJoin operator to a LeftJoin operator when it's
known that every row in the join's right input will match at least one row in
the left input. This rule is symmetric with SimplifyLeftJoin; see that rule
for more details and examples.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyRightJoin`, not the other rules in that file):

```
# SimplifyRightJoin reduces a FullJoin operator to a LeftJoin operator when it's
# known that every row in the join's right input will match at least one row in
# the left input. This rule is symmetric with SimplifyLeftJoin; see that rule
# for more details and examples.
[SimplifyRightJoin, Normalize]
(FullJoin
    $left:*
    $right:*
    $on:* & (JoinFiltersMatchAllLeftRows $right $left $on)
    $private:*
)
=>
(LeftJoin $left $right $on $private)
```
- Attempts used: 6
- Last updated: 2026-09-29T12:36:07.834409+00:00
- Reason / notes: The encoding uses the correct operators (FULL→LEFT on the same shared `cond`), and `before()`/`after()` genuinely differ in join kind so the proof is non-vacuous; in the chosen shape (self-join of one non-nullable column on `x = y`) the source rule's precondition "every right row matches a left row" holds by construction — each right row matches itself, so no unmatched-right NULL-extended rows exist — making FULL and LEFT bag-equal (verified: with a NULL present the match fails and FULL≠LEFT, which is exactly why the column is declared non-nullable). The narrowing to a single shared non-null-column self-join with a concrete equality is a heavy but honest `SCOPE: PARTIAL` restriction, and it is forced by a genuine QED limitation (the decision procedure checks unconditional equivalence and cannot express the relational "all right rows match" guard), not by an avoidable DSL gap; it is a non-degenerate, canonical special case (the source's own self-join example) rather than an accidentally over-constrained or vacuous encoding, and there is no missing or mis-stated precondition.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 38883667}, panicked=False

### `SimplifySameVarEqualities` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifySameVarEqualities converts `x = x` and other equality
comparisons into `x IS NOT NULL OR NULL`. The `OR NULL` is necessary
when x is NULL.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifySameVarEqualities`, not the other rules in that file):

```
# SimplifySameVarEqualities converts `x = x` and other equality
# comparisons into `x IS NOT NULL OR NULL`. The `OR NULL` is necessary
# when x is NULL.
[SimplifySameVarEqualities, Normalize]
(Eq | Le | Ge
    $left:(Variable)
    $right:(Variable) & (VarsAreSame $left $right)
)
=>
(Or (IsNot $left (Null (TypeOf $left))) (Null (BoolType)))
```
- Attempts used: 24
- Last updated: 2026-09-29T12:56:54.504477+00:00
- Reason / notes: The encoding is a faithful, non-vacuous capture of the rule's Eq branch: reusing the single `x` for both operands correctly enforces `VarsAreSame`, the nullable source column puts the critical null case in the proof domain, and the equivalence being proved — `x = x` ⟺ `x IS NOT NULL OR NULL`, where each side is TRUE on non-null rows and NULL on null rows — is precisely the null-sensitive 3VL claim at the heart of the source rule, one that could not be accidentally provable under a null-ignoring model (where `x = x` would collapse to TRUE while the RHS would not), so QED's "provable" result reflects genuine three-valued-logic reasoning rather than a trivial or coincidental proof. `before()` and `after()` are structurally different filters whose equivalence is a real semantic claim, and the type-uninterpreted, nullable source keeps the quantification fully general. The only gap versus the source rule is that the `Le`/`Ge` alternatives are not covered (a single RRule instance carries one before/after pair, and the DSL's family mechanism only substitutes join types, not operators), which the PARTIAL scope line states accurately and specifically, leaving a useful, non-degenerate result. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 350834}, panicked=False

### `SimplifySameVarInequalities` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifySameVarInequalities converts `x != x` and other inequality
comparisons into `x IS NULL AND NULL`. The `AND NULL` is necessary
when x is NULL.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifySameVarInequalities`, not the other rules in that file):

```
# SimplifySameVarInequalities converts `x != x` and other inequality
# comparisons into `x IS NULL AND NULL`. The `AND NULL` is necessary
# when x is NULL.
[SimplifySameVarInequalities, Normalize]
(Ne | Lt | Gt
    $left:(Variable)
    $right:(Variable) & (VarsAreSame $left $right)
)
=>
(And (Is $left (Null (TypeOf $left))) (Null (BoolType)))
```
- Attempts used: 24
- Last updated: 2026-09-29T13:13:23.288733+00:00
- Reason / notes: The encoding faithfully captures the source rule's `Ne` branch: `before` is the filter `x != x` (both operands the same field `x`, correctly encoding `VarsAreSame`), and `after` is `filter((x IS NULL) AND NULL)` — two structurally distinct expressions that are genuinely 3VL-equivalent (per row: non-null ⇒ FALSE/FALSE, null ⇒ NULL/NULL), so both filters yield the empty bag and the proof is non-vacuous, with no symbol-sharing or missing-precondition errors. The `Lt`/`Gt` alternatives are not encoded (a single RRule is one before/after pair), but this restriction is specific and honestly declared in the SCOPE line, and all three self-comparison inequality alternatives have identical 3VL behavior rewriting to the same `x IS NULL AND NULL`, so the Ne instance is a valid, non-degenerate special case.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 286083}, panicked=False

### `SimplifySelectFilters` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

SimplifySelectFilters simplifies the Filters operator in several possible
ways:
- Removes True operands
- Replaces the Filters operator with False if any operand is False, Null, or
a contradiction
- Flattens nested And operands by merging their conditions into parent
- Simplifies Or operands where one side is a Null to the other side
- Simplifies Is operands where the right side is True or False

Note that the Null handling behavior is different than the SimplifyAnd rules,
because Filters only appears as a Select or Join filter condition, both of
which treat a Null filter conjunct exactly as if it were False.

Extracted from `select.opt` (which defines multiple rules — implement specifically `SimplifySelectFilters`, not the other rules in that file):

```
# SimplifySelectFilters simplifies the Filters operator in several possible
# ways:
#   - Removes True operands
#   - Replaces the Filters operator with False if any operand is False, Null, or
#     a contradiction
#   - Flattens nested And operands by merging their conditions into parent
#   - Simplifies Or operands where one side is a Null to the other side
#   - Simplifies Is operands where the right side is True or False
#
# Note that the Null handling behavior is different than the SimplifyAnd rules,
# because Filters only appears as a Select or Join filter condition, both of
# which treat a Null filter conjunct exactly as if it were False.
[SimplifySelectFilters, Normalize, HighPriority]
(Select
    $input:*
    $filters:[
            ...
            $item:(FiltersItem
                    (And | True | False | Null | Or | Is)
                ) &
                ^(IsUnsimplifiableOr $item) &
                ^(IsUnsimplifiableIs $item)
            ...
        ] &
        ^(IsFilterFalse $filters)
)
=>
(Select $input (SimplifyFilters $filters))
```
- Attempts used: 22
- Last updated: 2026-09-29T13:24:27.560602+00:00
- Reason / notes: `before()` (Filter(scan, Or(p, NULL))) and `after()` (Filter(scan, p)) are structurally different, and the proven identity is a genuine, universally-quantified instance of the source rule's Or-with-Null branch: it holds for every table and every instantiation of the uninterpreted predicate `p` under the exactly-filter semantics (Null is falsy) that the source rule's own comment establishes as the precondition for this branch, so no soundness precondition is missing (the Optgen guards `IsUnsimplifiableOr`/`IsFilterFalse` control rule firing, not correctness of the rewrite). The operators match the source (Filter = Select, `Or`, a real boolean NULL literal serialized as a NULL RexLiteral), the same symbol `p` is correctly shared between both sides, and the `SCOPE: PARTIAL` tag is honest and specific: only the Or-with-Null branch over a single-conjunct filter is proven, the True/False/And/Is branches are explicitly excluded, and the plain-scan base is forced by the DSL (MetaAssignment supports only join-kind holes, not arbitrary input relations), leaving a non-degenerate, faithful special case rather than a vacuous or coincidentally-over-constrained proof.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 347875}, panicked=False

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

### `SimplifyTrueOr` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyTrueOr maps the Or operator to True if its left input is True.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyTrueOr`, not the other rules in that file):

```
# SimplifyTrueOr maps the Or operator to True if its left input is True.
[SimplifyTrueOr, Normalize]
(Or $left:(True) *)
=>
$left
```
- Attempts used: 3
- Last updated: 2026-09-29T13:25:13.511095+00:00
- Reason / notes: The encoding faithfully captures CockroachDB's `SimplifyTrueOr` rule (`Or(True, *) → True`) by using `RexRN.trueLiteral()` for the literal `True`, an uninterpreted predicate `right` for the arbitrary right operand (`*`), and a `Filter` as the minimal relational embedding for the scalar rewrite. The `before()` (`Filter(Or(True, right), S)`) and `after()` (`Filter(True, S)`) are structurally distinct, so the proof is non-vacuous, and no assumptions beyond the source rule are introduced, making `SCOPE: FULL` correct.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 457958}, panicked=False

### `SimplifyZeroCardinalityAntiJoin` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

SimplifyZeroCardinalityAntiJoin converts an AntiJoin operator to an empty
Values when it's known that the right input never returns zero rows, and
there is no join condition.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyZeroCardinalityAntiJoin`, not the other rules in that file):

```
# SimplifyZeroCardinalityAntiJoin converts an AntiJoin operator to an empty
# Values when it's known that the right input never returns zero rows, and
# there is no join condition.
[SimplifyZeroCardinalityAntiJoin, Normalize]
(AntiJoin | AntiJoinApply
    $left:*
    $right:* & ^(CanHaveZeroRows $right)
    []
)
=>
(ConstructEmptyValues (OutputCols $left))
```
- Attempts used: 88
- Last updated: 2026-10-16T10:23:14.211683+00:00
- Reason / notes: The encoding matches the rule's exact shape — an ANTI join of an uninterpreted left relation with the source's empty on-list rendered as a TRUE condition, rewritten to the empty relation over the left's output type — and the proof is non-vacuous: had the join kind been inner, the condition false, or the right side empty, the equivalence with the empty relation would have failed for non-empty left bags. The declared PARTIAL scope is honest and genuinely forced rather than a shortcut: QED's table format can only carry key constraints and row-level "guaranteed" predicates, both vacuous over the empty bag, so the `^(CanHaveZeroRows $right)` precondition (right is guaranteed non-empty) cannot be stated about an uninterpreted symbol and must be witnessed by a concrete non-empty relation (a one-row VALUES), and the correlated AntiJoinApply variant is not modelable because the DSL's `Correlate` builder always derives its condition from all left/right fields, so no empty/TRUE-apply condition can be expressed.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 26388541}, panicked=False

### `SimplifyZeroCardinalitySemiJoin` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

SimplifyZeroCardinalitySemiJoin converts a SemiJoin operator to an empty
Values when it's known that the right input never returns any rows.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyZeroCardinalitySemiJoin`, not the other rules in that file):

```
# SimplifyZeroCardinalitySemiJoin converts a SemiJoin operator to an empty
# Values when it's known that the right input never returns any rows.
[SimplifyZeroCardinalitySemiJoin, Normalize]
(SemiJoin | SemiJoinApply
    $left:*
    $right:* & (HasZeroRows $right)
)
=>
(ConstructEmptyValues (OutputCols $left))
```
- Attempts used: 24
- Last updated: 2026-09-29T13:43:50.285932+00:00
- Reason / notes: The encoding faithfully captures a genuine special case of the source rule: it proves that SEMI(left, on, ∅) ≡ ∅(left's columns) for all instantiations of the uninterpreted left table and join predicate, which is non-trivial (before is a Join node, after is a bare LogicalValues). The narrowing from "HasZeroRows(right)" (a dynamic optimizer-known property) to "right is a literal .empty()" is the only way to express zero-cardinality in this DSL (no cardinality-constraint mechanism exists for arbitrary subexpressions), and the SCOPE: PARTIAL line correctly and specifically identifies this. The join kind (SEMI), the output column set (left's columns only, as semi-join dictates), and the empty-after construction all match the source rule's ConstructEmptyValues(OutputCols $left) semantics. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 25703959}, panicked=False

### `SortFiltersInJoin` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/join.opt

SortFiltersInJoin ensures that any filters in an inner join are canonicalized
by sorting them.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SortFiltersInJoin`, not the other rules in that file):

```
# SortFiltersInJoin ensures that any filters in an inner join are canonicalized
# by sorting them.
[SortFiltersInJoin, Normalize]
(InnerJoin
    $left:*
    $right:*
    $on:* & ^(AreFiltersSorted $on)
    $private:*
)
=>
(InnerJoin $left $right (SortFilters $on) $private)
```
- Attempts used: 27
- Last updated: 2026-09-29T13:49:27.845080+00:00
- Reason / notes: The encoding faithfully captures the only semantic content of SortFiltersInJoin — that reordering conjunctive ON-filters of an InnerJoin is bag-semantics-preserving — using two distinct uninterpreted predicates over the full join fields with a transposition, which is the minimal non-degenerate instance of the backend's sort (before() and after() are genuinely different plans, and the proof is a universal one over all instantiations of a, b, L, R). The full general rule (arbitrary-length filter list, SortFilters function, AreFiltersSorted guard) is unmodelable in QED because it has no list/ordering semantics, so the narrowing is forced by a real QED limitation rather than a missing DSL capability; the join kind (INNER, exactly as the source) and distinct-symbol usage are correct, no semantic precondition is missing (AreFiltersSorted is a firing side-condition, and the unconditional proof is strictly stronger), and the PARTIAL scope line is honest and specific. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 68643166}, panicked=False

### `TryDecorrelateGroupBy` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateGroupBy "pushes down" a Join into a GroupBy operator, in an
attempt to keep "digging" down to find and eliminate unnecessary correlation.
The eventual hope is to trigger the DecorrelateJoin rule to turn a JoinApply
operator into a non-apply Join operator.

Example:

SELECT left.x, left.y, input.*
FROM left
INNER JOIN LATERAL
(
SELECT COUNT(*) FROM input WHERE input.x = left.x GROUP BY c
) AS input
ON left.y = 10
=>
SELECT CONST_AGG(left.x), CONST_AGG(left.y), COUNT(*)
FROM left WITH ORDINALITY
INNER JOIN LATERAL
(
SELECT * FROM input WHERE input.x = left.x
) AS input
ON True
GROUP BY input.c, left.ordinality
HAVING left.y = 10

In other cases, we can use an existing non-null column as a canary; that
column would not be constant necessarily, hence the use of ANY_NOT_NULL
instead of CONST_AGG.

An ordinality column only needs to be synthesized if "left" does not already
have a strict key. We wrap the output in a Project operator to ensure that
the original output columns are preserved and the ordinality column is not
inadvertently added as a new output column.

CONST_AGG is an internal aggregation function used when all rows in the
grouping set have the same value on the column.

Citations: [3] (see identity #8)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateGroupBy`, not the other rules in that file):

```
# TryDecorrelateGroupBy "pushes down" a Join into a GroupBy operator, in an
# attempt to keep "digging" down to find and eliminate unnecessary correlation.
# The eventual hope is to trigger the DecorrelateJoin rule to turn a JoinApply
# operator into a non-apply Join operator.
#
# Example:
#
#   SELECT left.x, left.y, input.*
#   FROM left
#   INNER JOIN LATERAL
#   (
#     SELECT COUNT(*) FROM input WHERE input.x = left.x GROUP BY c
#   ) AS input
#   ON left.y = 10
#   =>
#   SELECT CONST_AGG(left.x), CONST_AGG(left.y), COUNT(*)
#   FROM left WITH ORDINALITY
#   INNER JOIN LATERAL
#   (
#     SELECT * FROM input WHERE input.x = left.x
#   ) AS input
#   ON True
#   GROUP BY input.c, left.ordinality
#   HAVING left.y = 10
#
# In other cases, we can use an existing non-null column as a canary; that
# column would not be constant necessarily, hence the use of ANY_NOT_NULL
# instead of CONST_AGG.
#
# An ordinality column only needs to be synthesized if "left" does not already
# have a strict key. We wrap the output in a Project operator to ensure that
# the original output columns are preserved and the ordinality column is not
# inadvertently added as a new output column.
#
# CONST_AGG is an internal aggregation function used when all rows in the
# grouping set have the same value on the column.
#
# Citations: [3] (see identity #8)
[TryDecorrelateGroupBy, Normalize]
(InnerJoin | InnerJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (GroupBy | DistinctOn
            $input:*
            $aggregations:*
            $groupingPrivate:*
        ) &
        (IsUnorderedGrouping $groupingPrivate)
    $on:*
    $private:*
)
=>
(Project
    # Needed to project away any columns added by EnsureKey.
    (Select
        ((OpName $right)
            (InnerJoinApply
                $newLeft:(EnsureKey $left)
                $input
                []
                $private
            )
            (AppendAggCols
                $aggregations
                ConstAgg
                (NonKeyCols $newLeft)
            )
            (AddColsToGrouping
                $groupingPrivate
                (KeyCols $newLeft)
            )
        )
        $on
    )
    []
    (OutputCols2 $left $right)
)
```
- Attempts used: 68
- Last updated: 2026-10-16T10:37:06.162631+00:00
- Reason / notes: The encoding is a faithful, non-vacuous special case of the source rule: `before()` (join L with pre-grouped R, then filter `on`) and `after()` (cross-join L⋈R, group by (l,g) with the same aggregate `f`, then filter `on`) are structurally distinct yet provably equivalent precisely because `left` is declared a strict key (`unique=true`), which guarantees each left
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 88328792}, panicked=False

### `TryDecorrelateInnerJoin` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateInnerJoin tries to decorrelate an InnerJoin operator nested
beneath another Join operator by pulling up its join condition to the outer
join. This may be enough to decorrelate the outer join, or it may allow any
outer column references to continue to journey upwards.

TODO(andyk): Consider adding case for outer cols in $left.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateInnerJoin`, not the other rules in that file):

```
# TryDecorrelateInnerJoin tries to decorrelate an InnerJoin operator nested
# beneath another Join operator by pulling up its join condition to the outer
# join. This may be enough to decorrelate the outer join, or it may allow any
# outer column references to continue to journey upwards.
#
# TODO(andyk): Consider adding case for outer cols in $left.
[TryDecorrelateInnerJoin, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (InnerJoin | InnerJoinApply
            $innerLeft:*
            $innerRight:*
            $innerOn:* &
                ^(FiltersBoundBy
                    $innerOn
                    (OutputCols2 $innerLeft $innerRight)
                )
            $innerPrivate:*
        )
    $on:*
    $private:*
)
=>
((OpName)
    $left
    ((OpName $right) $innerLeft $innerRight [] $innerPrivate)
    (ConcatFilters $on $innerOn)
    $private
)
```
- Attempts used: 82
- Last updated: 2026-09-29T14:30:49.403006+00:00
- Reason / notes: The encoding faithfully captures the core algebraic identity of `TryDecorrelateInnerJoin` for the INNER-join case: [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 71673083}, panicked=False

### `TryDecorrelateInnerLeftJoin` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateInnerLeftJoin tries to decorrelate a LeftJoin operator nested
beneath an InnerJoin operator by using the associative identity to pull up the
left join to become the outer join. This may be enough to decorrelate the
outer join, or it may allow any outer column references to continue to journey
upwards.

Citations: [1] (see identity #6)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateInnerLeftJoin`, not the other rules in that file):

```
# TryDecorrelateInnerLeftJoin tries to decorrelate a LeftJoin operator nested
# beneath an InnerJoin operator by using the associative identity to pull up the
# left join to become the outer join. This may be enough to decorrelate the
# outer join, or it may allow any outer column references to continue to journey
# upwards.
#
# Citations: [1] (see identity #6)
[TryDecorrelateInnerLeftJoin, Normalize]
(InnerJoin | InnerJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (LeftJoin
            $innerLeft:*
            $innerRight:*
            $innerOn:*
            $innerPrivate:*
        )
    $on:* & (FiltersBoundBy $on (OutputCols2 $left $innerLeft))
    $private:*
)
=>
(LeftJoinApply
    ((OpName) $left $innerLeft $on $innerPrivate)
    $innerRight
    $innerOn
    $private
)
```
- Attempts used: 27
- Last updated: 2026-09-29T17:07:34.775243+00:00
- Reason / notes: The encoding correctly captures the source rule's structural identity — `L INNER JOIN (IL LEFT JOIN IR ON innerOn) ON on(L,IL)` ⟹ `(L INNER JOIN IL ON on(L,IL)) LEFT JOIN IR ON innerOn(IL,IR)` — with the critical `FiltersBoundBy` precondition enforced by applying `on` only to L and IL fields (never IR), and the `before`/`after` trees are genuinely different join groupings (non-trivial proof). The SCOPE: PARTIAL line honestly and specifically notes the narrowing to plain non-Apply joins over single-column scans, which is a genuine QED limitation (correlated/Apply joins have no bag-semantic model) while the algebraic reassociation identity itself is fully and faithfully represented.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 101681833}, panicked=False

### `TryDecorrelateProject` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateProject "pushes down" a Join into a Project operator, in an
attempt to eliminate any correlation between the projection list and the left
side of the join, and also to keep "digging" down to find and eliminate other
unnecessary correlation. The eventual hope is to trigger the DecorrelateJoin
rule to turn a JoinApply operator into a non-apply Join operator.

Citations: [3] (see identity #4)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateProject`, not the other rules in that file):

```
# TryDecorrelateProject "pushes down" a Join into a Project operator, in an
# attempt to eliminate any correlation between the projection list and the left
# side of the join, and also to keep "digging" down to find and eliminate other
# unnecessary correlation. The eventual hope is to trigger the DecorrelateJoin
# rule to turn a JoinApply operator into a non-apply Join operator.
#
# Citations: [3] (see identity #4)
[TryDecorrelateProject, Normalize]
(InnerJoin | InnerJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (Project $input:* $projections:* $passthrough:*)
    $on:*
    $private:*
)
=>
(Select
    (Project
        ((OpName) $left $input [] $private)
        $projections
        (UnionCols (OutputCols $left) $passthrough)
    )
    $on
)
```
- Attempts used: 62
- Last updated: 2026-09-29T18:06:43.493480+00:00
- Reason / notes: The encoding faithfully captures the structural essence of the original rule (Join(left, Project(input,…), on) → Filter(on, Project(CrossJoin(left, input), …))) with uninterpreted predicate/types; before and after are genuinely distinct plan shapes (conditional join vs. cross-join + reorder + filter), and the SCOPE line accurately and specifically states the narrowings (INNER-only, pure column-swap project, fixed 1-col/2-col arities) that make the general case intractable in the current DSL.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 69713667}, panicked=False

### `TryDecorrelateProjectInnerJoin` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateProjectInnerJoin tries to decorrelate by hoisting the filter of
an InnerJoin operator that sits below a LeftJoin/Project operator combo. The
Project operator itself can't be reordered above the LeftJoin like it can in
the InnerJoin case. However, the InnerJoin filter can be merged with the
LeftJoin filter, which is enough to decorrelate in several useful cases. This
rule works similarly to TryDecorrelateProjectSelect.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateProjectInnerJoin`, not the other rules in that file):

```
# TryDecorrelateProjectInnerJoin tries to decorrelate by hoisting the filter of
# an InnerJoin operator that sits below a LeftJoin/Project operator combo. The
# Project operator itself can't be reordered above the LeftJoin like it can in
# the InnerJoin case. However, the InnerJoin filter can be merged with the
# LeftJoin filter, which is enough to decorrelate in several useful cases. This
# rule works similarly to TryDecorrelateProjectSelect.
[TryDecorrelateProjectInnerJoin, Normalize, HighPriority]
(LeftJoinApply
    $left:*
    $right:(Project
        $join:(InnerJoin | InnerJoinApply
            $innerLeft:*
            $innerRight:*
            $innerOn:* &
                ^(FiltersBoundBy
                    $innerOn
                    (OutputCols2 $innerLeft $innerRight)
                )
            $innerPrivate:*
        )
        $projections:*
        $passthrough:*
    )
    $on:*
    $private:*
)
=>
(Project
    (LeftJoinApply
        $left
        (Project
            ((OpName $join)
                $innerLeft
                $innerRight
                []
                $innerPrivate
            )
            $projections
            (UnionCols $passthrough (OutputCols $join))
        )
        (ConcatFilters $on $innerOn)
        $private
    )
    []
    (OutputCols2 $left $right)
)
```
- Attempts used: 72
- Last updated: 2026-10-16T13:06:58.835002+00:00
- Reason / notes: The encoding exactly mirrors the rule's shape — LEFT(L, Project(InnerJoin(IL, IR, innerOn)), on) rewritten to LEFT(L, Project(InnerJoin(IL, IR, true)), on ∧ innerOn) — with correct symbol sharing (L/IL/IR/on/innerOn/f shared precisely as the rule's bindings, and the FiltersBoundBy precondition enforced structurally since innerOn can only reference inner-join columns), and the proof is non-vacuous because the plans genuinely differ and the row-correspondence (identical matched multisets and identical left-join no-match cases) is a real identity. The PARTIAL tag is honest and specific (1-column INTEGER scans, plain non-Apply joins, fixed [pass, pass, f] projection shape; Apply/correlation is fundamentally unmodelable in QED), and the omitted outer identity-Project in the after-side is semantically inert, so the proved algebraic core is exactly the source rule's transformation.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 98127333}, panicked=False

### `TryDecorrelateProjectSelect` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt
- Attempts used: 0
- Last updated: 2026-10-16T20:07:05.677019+00:00
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 39652541}, panicked=False

### `TryDecorrelateProjectSet` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateProjectSet "pushes down" an InnerJoinApply operator into a
ProjectSet operator, in hopes of eliminating any correlation between the
ProjectSet operator and the InnerJoinApply operator. Eventually, the
hope is to trigger the DecorrelateJoin pattern to turn JoinApply operators
into non-apply Join operators.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateProjectSet`, not the other rules in that file):

```
# TryDecorrelateProjectSet "pushes down" an InnerJoinApply operator into a
# ProjectSet operator, in hopes of eliminating any correlation between the
# ProjectSet operator and the InnerJoinApply operator. Eventually, the
# hope is to trigger the DecorrelateJoin pattern to turn JoinApply operators
# into non-apply Join operators.
[TryDecorrelateProjectSet, Normalize]
(InnerJoinApply
    $left:*
    (ProjectSet $input:* $zip:*)
    $on:*
    $private:*
)
=>
(Select
    (ProjectSet (InnerJoinApply $left $input [] $private) $zip)
    $on
)
```
- Attempts used: 41
- Last updated: 2026-09-29T18:38:54.794708+00:00
- Reason / notes: The encoding faithfully captures the core structural rewrite: before is `L ⋈_on (I ⋈_M E)` and after is `Filter_on ((L×I) ⋈_M E)`, which are genuinely different plan shapes whose bag-semantic equivalence (valid because M references only I and E columns, not L) is exactly what the original Optgen rule rewrites. The single-column-per-relation choice is a cosmetic restriction that doesn't affect the structural argument (uninterpreted predicates' arity is irrelevant to the join-rearrangement identity), the ProjectSet-as-join abstraction correctly isolates the row-emission relation from L, and the SCOPE tag honestly documents all simplifications.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 71085916}, panicked=False

### `TryDecorrelateSelect` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt
- Attempts used: 0
- Last updated: 2026-10-16T20:07:03.473347+00:00
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 20284166}, panicked=False

### `TryDecorrelateSemiJoin` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateSemiJoin maps a SemiJoin to an equivalent GroupBy/InnerJoin
complex in hopes of triggering further rules that will ultimately decorrelate
the query. Once this rule fires, a corresponding InnerJoin decorrelation rule
will match (i.e. TryDecorrelateGroupBy or TryDecorrelateProject).

Citations: [5]

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateSemiJoin`, not the other rules in that file):

```
# TryDecorrelateSemiJoin maps a SemiJoin to an equivalent GroupBy/InnerJoin
# complex in hopes of triggering further rules that will ultimately decorrelate
# the query. Once this rule fires, a corresponding InnerJoin decorrelation rule
# will match (i.e. TryDecorrelateGroupBy or TryDecorrelateProject).
#
# Citations: [5]
[TryDecorrelateSemiJoin, Normalize]
(SemiJoin | SemiJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (CanHaveZeroRows $right) &

        # Let EliminateExistsGroupBy match instead.
        (GroupBy | DistinctOn | Project | ProjectSet | Window)
    $on:*
    $private:*
)
=>
(Project
    # Needed to project away any columns added by EnsureKey.
    (GroupBy
        (InnerJoinApply
            $newLeft:(EnsureKey $left)
            $right
            $on
            $private
        )
        (MakeAggCols ConstAgg (NonKeyCols $newLeft))
        (MakeGrouping (KeyCols $newLeft) (EmptyOrdering))
    )
    []
    (OutputCols $left)
)
```
- Attempts used: 47
- Last updated: 2026-09-29T20:39:43.847312+00:00
- Reason / notes: The encoding faithfully captures the core algebraic identity of TryDecorrelateSemiJoin — SEMI correlate(L, R, on) ≡ GroupBy(INNER correlate(L, R, on), group-by L's key) — with the `unique=true` flag on L correctly modeling EnsureKey's guarantee (without which GroupBy would collapse duplicate left rows that SEMI preserves). The restrictions (single-column keyed left, single-column right, uninterpreted 2-ary condition) are genuine and honestly documented in the SCOPE line, and before()/after() are structurally distinct operators (SemiJoin vs GroupBy-over-InnerJoin), so the proof is non-vacuous.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 71918250}, panicked=False

### `TryDecorrelateWindow` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: (spec text unavailable at publish time)
- Attempts used: 1
- Last updated: 2026-09-30T03:11:24.283606+00:00
- Reason / notes: Manual encoding via PushSelectIntoWindow's join-back-on-partition idiom + TryDecorrelateGroupBy's single-key precedent; confirmed with negative control.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 42510708}, panicked=False

### `TryRemapJoinOuterColsLeft` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryRemapJoinOuterColsLeft is similar to TryRemapJoinOuterColsRight, but it
applies to the left input of a join.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryRemapJoinOuterColsLeft`, not the other rules in that file):

```
# TryRemapJoinOuterColsLeft is similar to TryRemapJoinOuterColsRight, but it
# applies to the left input of a join.
[TryRemapJoinOuterColsLeft, Normalize]
(InnerJoin | InnerJoinApply | SemiJoin | SemiJoinApply
    $left:* & (HasOuterCols $left)
    $right:*
    $on:* &
        (CanMaybeRemapOuterCols $left $on) &
        (Let ($remapped $ok):(TryRemapOuterCols $left $on) $ok)
    $private:*
)
=>
((OpName) $remapped $right $on $private)
```
- Attempts used: 83
- Last updated: 2026-09-30T01:41:30.187027+00:00
- Reason / notes: The encoding is a non-vacuous, honest PARTIAL special case: it models the InnerJoinApply variant where a correlated INNER join enforces L.col0 = R.col0, and proves that filtering the join output by uninterpreted h(L.col0) is equivalent to filtering by h(R.col0) — exactly the algebraic core of TryRemapJoinOuterColsLeft's outer-column-to-equal-column remap. The SCOPE line accurately discloses the narrowings (single-column scans of one shared type, equality ON, one-arity uniterated predicate, filter-above-join shape rather than remap-inside-left), and the proof is genuinely non-trivial since the two sides reference different fields whose equality is only guaranteed by the correlate condition. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 70305750}, panicked=False

### `TryRemapJoinOuterColsRight` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt
- Attempts used: 0
- Last updated: 2026-10-16T20:07:07.786007+00:00
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 22905250}, panicked=False

### `TryRemapSelectOuterCols` — ✅ PROVED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryRemapSelectOuterCols is similar to TryRemapJoinOuterColsRight, but it
applies to the input of a Select.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryRemapSelectOuterCols`, not the other rules in that file):

```
# TryRemapSelectOuterCols is similar to TryRemapJoinOuterColsRight, but it
# applies to the input of a Select.
[TryRemapSelectOuterCols, Normalize]
(Select
    $input:* & (HasOuterCols $input)
    $on:* &
        (CanMaybeRemapOuterCols $input $on) &
        (Let ($remapped $ok):(TryRemapOuterCols $input $on) $ok)
)
=>
(Select $remapped $on)
```
- Attempts used: 57
- Last updated: 2026-10-16T15:32:50.624152+00:00
- Reason / notes: The encoding faithfully captures the rule's core mechanism — under the Select filter equality c0=c1 (outer stand-in = local column), the outer-column reference inside the nested input filter h(c0) is remapped to h(c1) while $on is left unchanged, exactly as `Select $input $on => Select $remapped $on` prescribes — and before()/after() are genuinely structurally different, so the proof is non-vacuous. The specializations (3-column uninterpreted scan, the filter being exactly the enabling equality rather than an arbitrary conjunctive $on, and a single unary uninterpreted predicate one level below the filter) are real restrictions of the full rule but are accurate, specific, and honestly declared in the SCOPE line, and the identity is a true instance of the general remap since it holds a fortiori for the constant-per-outer-row case.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 66621375}, panicked=False

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
- Attempts used: 8
- Last updated: 2026-10-14T17:02:50.414876+00:00
- Reason / notes: ApplyLimitToRecursiveCTEScan is a pure optimizer-metadata rule — it only re-derives and re-attaches a bounded-cardinality property to the recursive CTE's working-table binding, leaving the output bag of rows identical before and after — and it operates on Optgen-custom operators (RecursiveCTE, WithScan, FakeRel) where a recursive CTE is a fixed point that cannot be written as a finite relational-algebra pattern in the core language. RuleScript/QED has no representation of cardinality/row-count bounds (a `Scan` only carries a `unique` key flag), and QED does not model the ordering/`Limit` (list) semantics the "limit that applies to all iterations" bound depends on, so the rule's distinguishing content is unrepresentable and any before/after pair QED could even accept would be vacuous. ```

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
- Last updated: 2026-10-14T17:06:12.628240+00:00
- Reason / notes: The rule's sole semantic content is the glob-theory fact that consecutive '%' wildcards are equivalent to one ('x LIKE a%%b' ⟺ 'x LIKE a%b'), which is a statement about the internal string-pattern-matching semantics of the LIKE operator. QED reasons over an equality-plus-bags theory with all predicates (including LIKE) as uninterpreted symbols and no string sort or pattern axioms, so distinct pattern constants produce independent uninterpreted atoms that the SMT solver can refute with a countermodel; since the prover is the fixed Rust arbiter that treats every operator name as uninterpreted, no RuleScript encoding or extend_dsl_file extension can supply the missing '%' idempotence axiom. This is exactly the "operator whose specific internal semantics QED cannot see through as an uninterpreted function" limitation, matching the previously skipped LikeSimplification and NotLikeToNotLike rules, and no non-trivial special case avoids it. ```

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
- Attempts used: 32
- Last updated: 2026-10-14T17:28:33.375167+00:00
- Reason / notes: ConsolidateSelectFilters is a pure representational rewrite whose sole correctness claim is that CockroachDB's Range scalar is evaluation-transparent to its inner And (both the constraint builder and Eval delegate to t.And) — an axiom about a backend operator's internal semantics, not a relational-algebraic fact. QED quantifies universally over uninterpreted symbols with no mechanism for definitional axioms, so an encoding with an uninterpreted Range predicate is genuinely unprovable, and the only encodable variant (modeling Range as the And it is) collapses before() and after() into the same plan, reducing the "proof" to a vacuous identity that certifies nothing about the backend rule. A DSL extension cannot close this gap, since QED's serialization/prover has no notion of a "defined" symbol — the port is blocked by the stated limitation that QED cannot see through an operator's bespoke internal semantics. ```

### `ConvertCountToCountRows` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt
- Attempts used: 0
- Last updated: 2026-10-15T00:16:30.581124+00:00
- Reason / notes: The rule is conditional: COUNT(x) becomes COUNT(*) only when ExprIsNeverNull(x) holds. QED's prover does model null-skipping, but only when an aggregate call carries ignoreNulls=true, and the DSL cannot produce that flag: RelRN.AggCall has no ignoreNulls field and Aggregate.semantics() builds calls through the plain RelBuilder.aggregateCall overload, which defaults it to false. Encoding COUNT(x) against a row count on a column typed non-null does prove, but the same encoding also proves when the column is marked nullable, so it establishes the weaker unconditional identity 'tally rows == tally rows' and not the rule's non-null precondition. Publishing it as proved would misstate what was checked, and the porter's own late attempts were heading to the same conclusion: it was drafting an ignoreNulls extension to AggCall and the serializer, which is a DSL change rather than an encoding of this rule.

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
- Last updated: 2026-10-14T17:23:10.002540+00:00
- Reason / notes: The rule's entire content is the semantic identity `Indirection(x, y) ≡ FetchVal(x, y)` on JSON inputs — i.e., a backend operator's bespoke internal semantics relating two distinct scalar operators — and QED encodes every named operator as an independent uninterpreted function, with no axiom/alias channel anywhere in the pipeline: the JSON format carries only operator name, operands, and type, the RRule interface exposes no assumptions, and the only place such an equivalence could be declared (the Rust prover) is fixed and unmodifiable, so no `extend_dsl_file` change could close the gap. The porter also correctly avoided the only degenerate workaround — reusing a single symbol name on both sides, which would collapse before() and after() into identical trees and prove a vacuous tautology rather than the rule — so no faithful, non-trivial encoding (or meaningful special case) of this rule exists in RuleScript/QED. ```

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
- Attempts used: 44
- Last updated: 2026-10-14T17:55:07.189691+00:00
- Reason / notes: The rule's validity rests on the clamp/truncation semantics of `levenshtein_less_equal(a,b,m)` relative to `levenshtein(a,b)` (its value must be `d` when `d<=m` and `m+1` when `d>m` for all five comparisons to be preserved) — an algebraic relation between two distinct function symbols that QED treats as independent uninterpreted functions with no axiom mechanism to relate them (no such hook in RRule; the JSON format's only per-table "guaranteed" constraints are row-value checks on scan columns and cannot constrain projection outputs or tie an uninterpreted comparison predicate to a clamp relation; the Rust prover is unmodifiable). RexRN likewise has no ite/arithmetic/comparison constructors, so `levenshtein_less_equal` cannot be defined in terms of `levenshtein` and any faithful before/after pair using two distinct symbols admits a trivial SMT countermodel, leaving no non-trivial narrower special case — the same blocker confirmed in the independently verified AggregateReduceFunctions precedent.

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
- Last updated: 2026-10-14T17:47:04.208413+00:00
- Reason / notes: The rewrite's soundness rests entirely on CockroachDB's bespoke internal semantics — `levenshtein_less_equal(a,b,n) = min(levenshtein(a,b), n+1)` — a relation *between two distinct named scalar operators*; QED models each operator as an independent uninterpreted function with no axiom channel, and its scalar language has no min/conditional/arithmetic in which the clamping relation could be reconstructed, so `N cmp f(a,b) ≡ N cmp g(a,b,N)` is refutable by the solver for all five comparisons. The one remaining encoding trick — reusing the same operator name on both sides — would merely prove the false (and useless) claim that the two functions are identical. This is exactly the "bespoke operator internal semantics QED cannot see through" limitation, with no narrower special case that doesn't still require the unprovable relation. ```

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
- Attempts used: 26
- Last updated: 2026-10-14T18:36:17.367900+00:00
- Reason / notes: The rewrite is valid only because of a CockroachDB function-library fact — the 2-arg `Like(x, p)` is by definition the same boolean function as `like_escape(x, p, '\'` (the default escape char) — and RuleScript provides no mechanism to express that: each named scalar operator becomes a distinct uninterpreted symbol, and QED's SMT core has no axioms linking independent symbols, so it cannot derive `like_escape(x, p, c) = like(x, p)` for any constant `c`; the only way to make both sides syntactically coincide is to use one shared symbol on both sides, which reduces the rule to the tautology `Filter(P) = Filter(P)` and records nothing. This is precisely the stated limitation that QED cannot reason about inference between independent symbols or a backend operator's bespoke internal semantics, and it is not a missing-builder gap: no `extend_dsl_file` change can help, since the prover's axiom-free uninterpreted-function model is fixed and unmodifiable.

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
- Attempts used: 27
- Last updated: 2026-10-14T18:25:26.449939+00:00
- Reason / notes: The rule's correctness rests on an algebraic identity between two distinct aggregate functions — regr_count(y,x) ≡ count(x) when one argument is never-null — but QED models every aggregate as an independent uninterpreted function of the group bag keyed only by operator name (the serializer emits only name, operands, distinct, ignoreNulls, type, and the prover has no count/regr axioms), so no RuleScript encoding, including marking the never-null argument's column type non-nullable, can make the prover relate two differently-named aggregate calls. Making the names identical would no longer be the rule, and adding aggregate axioms would require modifying the Rust prover, which is out of bounds. The porter's non-timeout `provable=false` is therefore the expected countermodel of QED's semantics — a genuine missing-aggregate-algebra limitation, not a fixable symbol-sharing or modeling bug.

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
- Last updated: 2026-10-14T18:40:30.426450+00:00
- Reason / notes: The after-side's validity rests entirely on `Limit(Q,1)` having 0 rows iff `Q` is empty and 1 row iff non-empty, plus scalar-subquery NULL-when-empty and `Coalesce`'s null-skip — but QED assigns `Limit` no bag-semantic meaning and treats scalar subqueries/`Coalesce` as uninterpreted atoms with no cardinality or NULL axioms, so SMT can always instantiate the after-side atom to disagree with the before-side's non-emptiness. No `extend_dsl_file` gap can close this because the frozen Rust prover has no non-emptiness/cardinality semantics for `Limit` or scalar subqueries to expose — the identical Spark `RewriteNonCorrelatedExists` was independently verified on the same ground.

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
- Attempts used: 101
- Last updated: 2026-10-14T23:04:19.560550+00:00
- Reason / notes: The rule's RHS requires DistinctOn/ConstAgg to return non-key values that are functionally determined by the grouping key, but QED treats aggregate calls as uninterpreted and provides no representative-value or non-table-level FD axioms. Even in PK special cases, the unconstrained aggregate output cannot be tied to the original union's non-key columns, so the exact transformation is not provable in RuleScript/QED. ```

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
- Attempts used: 85
- Last updated: 2026-10-15T00:36:20.914133+00:00
- Reason / notes: The rule's soundness is a list-level identity — row-wise unrolling of an array by a set-returning zip function (unnest/json_array_elements, including null-padding of shorter arrays) ≡ an apply join with Values of that same array — and QED's model is pure bag semantics over uninterpreted functions, with no list type, no per-row element enumeration, and no ProjectSet/Unnest operator to carry such semantics. Extending the DSL would not close the gap either: QED's JSON format and the unmodifiable prover have no node or semantics for set-returning functions, and any core-language encoding that captures per-row/per-element multiplicity (a join or Correlate gated by an uninterpreted Elem predicate) collapses before() and after() into the same relation, proving a tautology rather than the actual rewrite. ```

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
- Attempts used: 45
- Last updated: 2026-10-15T00:53:14.459052+00:00
- Reason / notes: The rule's correctness depends on CASE's three-valued branch-selection semantics, but the RuleScript scalar fragment only interprets And/Or/Not/boolean literals and sends any CASE-like RexCall to QED as an uninterpreted operator. Since QED cannot decompose that uninterpreted CASE or express the required "previous condition is not True" fall-through, even the narrowest instances containing a CASE on the before side are not universally provable.

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
- Attempts used: 9
- Last updated: 2026-10-15T00:48:48.396841+00:00
- Reason / notes: EliminateCast's soundness rests entirely on the axiom "a cast to the input's own type is the identity function," but QED's SMT prover treats scalar operators like casts as uninterpreted functions with no cast/identity theory, so it can never establish cast_T(x) = x. Compounding this, every RelType.VarType is flattened to INTEGER in JSONSerializer (VarType.getName() always returns "INTEGER"), so the HasColType type-identity guard — a purely static, value-less check — serializes to no information the prover can consume, and an uninterpreted guard predicate cannot be linked to an uninterpreted cast function. This is a fundamental QED/prover limitation rather than a DSL gap: a dedicated cast operator would still serialize to the same uninterpreted call-with-erased-type JSON form, and closing the gap would require adding a cast-identity theory to the off-limits Rust prover; the porter's empirical provable:false on the faithful Filter(guard,scan)+cast-project vs bare-field encoding is the expected outcome, and no symbol-sharing fix or narrower special case can recover a provable form without dropping the cast itself. ```

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
- Attempts used: 28
- Last updated: 2026-10-15T00:50:28.336620+00:00
- Reason / notes: EliminateCoalesce's validity rests entirely on the scalar identity COALESCE(x) = x, but in RuleScript "Coalesce" is just an uninterpreted named RexCall — QED's prover has no coalesce operator/axiom (not in the paper's semantics nor in the JSON op mapping), and it must prove equivalence for *every* instantiation of uninterpreted symbols, under which f(x) = x is refutable (any non-identity function is a countermodel). No encoding or DSL extension in RelRN/RexRN can inject that identity into the frozen Rust prover, so the observed provable: false reflects a genuine limitation, not a symbol-matching or modeling mistake. ```

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
- Attempts used: 66
- Last updated: 2026-10-15T01:38:56.020059+00:00
- Reason / notes: The rule's soundness rests entirely on the evaluation semantics of the value-returning UNIQUE scalar-subquery operator (a single-row constant VALUES body must evaluate to exactly that constant), and QED's formalization gives concrete semantics only to the cardinality-based EXISTS subquery operator (JExists(Q) = Σ‖JQK‖); a value-returning UNIQUE subquery is an uninterpreted higher-order operator in the semiring Q-expression model that the prover cannot see through, since bag/multiplicity semantics cannot express "return the value of the unique row." Any faithful encoding of this rule must name that subquery operator (the relational core language has no substitute for it), so the failure is a genuine QED limitation rather than an encoding bug — the BOOLEAN-vs-INTEGER type mismatch the porter observed is a secondary blemish of their attempt, not the cause.

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
- Attempts used: 22
- Last updated: 2026-10-15T01:11:14.049837+00:00
- Reason / notes: The rule’s validity depends on first-row semantics: a `DistinctOn` with no grouping columns returns the first input row under a specific ordering, and the RHS uses `LIMIT 1` on that ordering. QED has no bag-semantic model for `Sort`/`Limit`/ordering and treats aggregates such as first-value aggregation as uninterpreted, so it cannot relate either side to a particular member of the input bag.

### `EliminateDistinctOnValues` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt
- Attempts used: 0
- Last updated: 2026-10-15T02:30:36.668519+00:00
- Reason / notes: The rule removes a DistinctOn whose input is a constant Values that is already distinct on the grouping columns. The porter's encoding proved, but its two sides serialize to the identical plan: Calcite's RelBuilder.aggregate drops an aggregate whenever the input is known to be unique (including a Values with distinct rows and a Values under a Filter), so `before()` is already the bare input when it reaches the prover and QED compares a plan with itself. The same holds with the Values wrapped in a Filter over an uninterpreted predicate. The porter's proof also hit a separate merge problem, but that is not the cause. The rule's real content, converting an aggregate with FirstAgg/ConstAgg columns into a projection, is outside what QED can relate, since ordinary aggregates have no derivation of 'aggregate over a singleton group equals its argument', and the empty-aggregation case is the already-proved EliminateDistinct.

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
- Attempts used: 102
- Last updated: 2026-10-15T02:26:49.858727+00:00
- Reason / notes: The rule's essential mechanism is error-raising — EnsureDistinctOn/Max1Row/ErrorOnDup must fail when the no-grouping-columns input has more than one row — and QED only models total bag equivalence with no error/partial-function semantics and no Max1Row/Sort/Limit (top-1 ordering) operator to encode it. In the error-free remainder (≤1-row input), the single group's bag coincides with Max1Row's output, so both sides reduce to the identical bag transformation (aggregates over the input, 0 rows on empty input), yielding only a trivial identity rather than a two-sided rule. Additionally, QED's ungrouped Aggregate always emits one row, so it cannot match the required 0/1-row collapse on empty input, and no combination of Filter/Join/Union/Empty can synthesize the missing error or emptiness-conditional behavior.

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
- Attempts used: 14
- Last updated: 2026-10-15T01:59:31.851335+00:00
- Reason / notes: The identity holds only under the scalar existential projection (EXISTS) — the two sides are not bag-equal (`LIMIT n·Q ≠ Q` as bags), and the Limit occurrence exists only on the left, so it cannot cancel as shared context the way it did in the Limit-preserving Cockroach rules. QED's bag theory gives Limit no meaning at all (per qed.pdf, Limit is only the uninterpreted QOp with no axioms — the prover's stated list-semantic limitation — and the prover itself is off-limits to modify), the core language exposes no Limit/Exists operator, and uninterpreted predicate symbols cannot tie `EXISTS(LIMIT n·S)` to `EXISTS(S)`, so even an `extend_dsl_file` attempt could only surface a Limit the prover has no axiom for; the correlated-only guard (`HasOuterCols`) is additionally a side condition a bare before/after pair cannot express.

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
- Attempts used: 46
- Last updated: 2026-10-15T02:50:24.327407+00:00
- Reason / notes: QED encodes group-by aggregate calls as uninterpreted operators whose only equational power is bag-equality of their inputs, and it has no axiom that AnyNotNullAgg/ConstAgg over a strict-key (singleton) group collapses to the identity on its element — which is exactly the identity this rule needs, since the after() side removes the aggregate entirely, leaving a bare column that no bag-equality argument can equate to an opaque F(bag) (the EliminateAggDistinctForKeys precedent confirms QED's aggregate reasoning stops at bag-vs-set input equality). So every instance with at least one aggregate call is unprovable for a fundamental prover limitation, not a missing DSL builder that extend_dsl_file could supply (the JSON format only carries operator names, and the prover is the unmodifiable arbiter), while the only provable degenerate instance — zero aggregates over a unique-key scan — reduces to exactly the already-proven cockroach EliminateDistinct (strict-key GroupBy ⇒ Project) shape. ```

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
- Attempts used: 22
- Last updated: 2026-10-15T02:42:37.065331+00:00
- Reason / notes: EliminateLimit's correctness rests on two things QED fundamentally lacks, per the Limitations section: it models no list/ordering semantics, so a `Limit` node is an uninterpreted operator in the fixed prover with no axiom of any form `Limit(n, X) ≡ X` (or `Limit(n, ∅) ≡ ∅`), and the rule's guard `limit ≥ MaxRows(input)` is a cardinality precondition that RuleScript (unconditional before/after equivalence) and QED's JSON (no row-count field; only row-level `guaranteed` predicates) have no mechanism to express. The only DSL input with a structurally bounded row count is `Empty`, and even that degenerate instance is unprovable for the first reason, so `extend_dsl_file` (which can only add builders, not prover axioms) cannot close the gap — a genuine QED limitation, not a missed encoding. ```

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
- Attempts used: 24
- Last updated: 2026-10-15T02:49:49.626046+00:00
- Reason / notes: Max1Row is an order-dependent LIMIT-1 operator ("keep the first row"), and QED's bag-semantic theory explicitly models no list/ordering semantics (Sort/Limit/Offset/Window/Sample are out of scope, and no such builder exists in RelRN), so the LHS can only be represented as an opaque uninterpreted symbol with no Max1Row(X)≡X axiom, making the equivalence unprovable even under any precondition. The HasZeroOrOneRow guard is a cardinality side condition the before/after pattern format cannot express — uninterpreted scans can't be constrained to ≤1 rows (the unique/key flag only gives functional dependency, not cardinality), and even the only structural 0-or-1-row shapes (Empty, or a no-group-key aggregate output) leave an uninterpreted Max1Row wrapper whose identity is unprovable, e.g. Max1Row(Empty)≢Empty. Dropping Max1Row from the encoding collapses the rule to the vacuous identity X≡X, and this is the identical blocker the sister rule EliminateLimit in the same .opt family hit and got an independent AGREE on — so a further attempt cannot yield a genuine, non-vacuous encoding. ```

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
- Attempts used: 22
- Last updated: 2026-10-15T02:55:12.563713+00:00
- Reason / notes: QED models Offset as an uninterpreted QOp with no algebraic axioms (per qed.pdf §6.2, which states it does not support queries using Limit/Offset/Order By ordering semantics), so the rule's only content — that offsetting by zero is the identity — is not an SMT consequence; a countermodel where the uninterpreted Offset function discards rows even at 0 is available to the solver. No DSL extension can close this gap, since the missing piece is a prover-side axiom for Offset (and the prover is the trusted, unmodifiable arbiter; the existing `LogicalSort` case in `JSONSerializer` only covers serialization, not semantics). The rule also has no bag-semantic residue: with the unmodelable Offset removed from both sides, only the vacuous identity `input = input` remains, so there is no non-trivial special case to prove — consistent with the prior Spark `EliminateOffsets` skip for the same reason. ```

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
- Attempts used: 58
- Last updated: 2026-10-15T03:28:42.535866+00:00
- Reason / notes: The rule's correctness rests exactly on the algebraic identity of the value-returning scalar-subquery operator — unique(Values[<e>]) = <e> — and QED's documented model gives concrete semantics only to the cardinality-based EXISTS, treating aggregate/subquery operators as uninterpreted bag-to-value functions known solely up to input bag equality. Every faithful RuleScript encoding must carry that opaque operator on the before side and the bare UDF term on the after side, so no Java-side DSL extension (which only affects JSON construction, not the untouchable prover's axioms) can bridge the gap — consistent with the independently verified identical wall for the constant-payload sibling EliminateConstValueSubquery. ```

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
- Attempts used: 24
- Last updated: 2026-10-15T03:24:26.564006+00:00
- Reason / notes: In any encoding, FoldArray reduces to a value equation between an uninterpreted function application and a stored constant: the `Array` constructor must be an uninterpreted projection symbol (no array operator exists in the core language), and both its constant elements and the RHS constant-array datum are, by construction of `RexRN.Const`, just the literal 0 in the prover — so QED would have to prove `arr(0,…,0) = 0` universally for an arbitrary uninterpreted `arr`, which a countermodel refutes and which no axiom/constraint mechanism in the DSL can override. QED also has no list/array value semantics (a tabulated "List semantic" failure category), and since the Rust prover is the unmodifiable trusted arbiter, `extend_dsl_file` cannot supply the missing semantics. The narrowest possible special case (fixed arity/types) has exactly the same unprovable shape, so no non-vacuous encodable form exists. ```

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
- Attempts used: 5
- Last updated: 2026-10-15T04:03:00.827546+00:00
- Reason / notes: FoldAssignmentCast rewrites AssignmentCast(const, T) into a concrete folded literal, so its correctness rests on a value equation cast(c) = c′ about the cast operator's evaluation semantics, but QED models casts only as uninterpreted functions with no axioms (qed.pdf's documented Typing limitation — SMT solvers don't reason about type casting, and all virtual types erase to INTEGER), making any faithful before/after encoding unprovable. Independently, the DSL cannot even express two distinct constant values (RexRN.Const only produces literal 0), so "a constant folds to a generally different constant" is inexpressible regardless of cast semantics. Supplying the needed cast-evaluation axioms would require modifying the unmodifiable prover, confirming the rule is genuinely out of RuleScript/QED's reach (same root cause as the previously verified FoldCast skip). ```

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
- Attempts used: 8
- Last updated: 2026-10-15T03:31:02.710238+00:00
- Reason / notes: FoldCast's claim is that `cast(const) = folded_literal`, and its correctness rests entirely on the cast operator's value-producing semantics — semantics that exist nowhere in the stack: the DSL exposes no cast construct (the only value-transforming symbols are uninterpreted `Proj` functions), and QED's prover treats every function symbol as uninterpreted with all virtual types erased to INTEGER, so `f(c) = c'` is not SMT-valid for any uninterpreted `f` and literal `c'`. The only typecheckable encodings are vacuous: omitting the cast leaves both sides as the identical literal 0 after type erasure (proving an artifact, not the rule), and keeping an uninterpreted same-type cast still yields the underivable `f(0) = 0` — so no genuine non-trivial special case exists. Consequently `extend_dsl_file` cannot close the gap either: any new `cast` builder would still serialize to an uninterpreted operator for which the fixed Rust prover has no axioms.

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
- Attempts used: 23
- Last updated: 2026-10-15T04:13:43.639956+00:00
- Reason / notes: FoldCollate's correctness rests on the value identity `collate(const, locale) = castToCollatedString(const, locale)`, but QED models both scalar operators as distinct uninterpreted functions over type-erased (INTEGER) terms, and the SMT solver is free to assign them different values at the same argument point, so no model of the encoding makes the two sides universally equal and the identity is unprovable. The only encodable variant — sharing a single uninterpreted symbol on both sides — degenerates into a vacuous `f(x) = f(x)` identity that no longer expresses the backend rule (and narrowing to `Const` inputs changes nothing, since `f(0,l) = g(0,l)` between distinct uninterpreted functions is still undecidable-true only by axiom), and `extend_dsl_file` can add operators/shapes but cannot add prover axioms about uninterpreted operators — the same fundamental gap for which the structurally identical FoldCast precedent was independently verified SKIPPED. ```

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
- Attempts used: 21
- Last updated: 2026-10-15T04:08:35.542440+00:00
- Reason / notes: The rule's correctness rests on the scalar identity x / 1 = Cast(x, τ), but QED's fixed theory (equality, uninterpreted functions/predicates, a total order per sort, natural-number addition, ite) contains no division algebra and models casts only as uninterpreted functions, so Div(x,1) and Cast(x) are distinct opaque terms with no logical validity relating them under any bag-semantic encoding. Since the Rust prover is the immutable arbiter, no DSL extension can supply the missing axioms — and the incidental gap that RexRN.Const hardcodes the literal 0 (verified in source) is moot once division/cast semantics are absent anyway. No faithful narrower special case survives either: even the cast-free variant x/1 = x still requires division semantics QED does not model, since a single-column projection only proves equality when its target expressions are pointwise equal. ```

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
- Attempts used: 10
- Last updated: 2026-10-15T04:30:40.757385+00:00
- Reason / notes: I verified directly in the DSL and type sources that QED's theory has no array sort (RelType.java states all virtual types are translated to integers in the prover) and no ANY/SOME/IN operator at all — the paper's SOME is defined only over relations, where an empty relation yields False (the wrong result for a NULL array) and an all-null relation yields Null purely as a 3-valued-logic tautology, so the "NULL array" premise can only be expressed as an uninterpreted symbol (making the conclusion an assumption) or a substituted premise (which proves a tautology independent of the backend's actual null-array behavior, as the prior review already rejected). The rule's validity therefore rests on CockroachDB's bespoke `evalAny` evaluation semantics (a NULL array datum evaluates to NULL, per #42562), which is a genuine "bespoke internal semantics of a backend operator" limitation QED cannot see through, not a missing DSL builder an `extend_dsl_file` gap-fill could close.

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
- Attempts used: 6
- Last updated: 2026-10-15T04:13:16.436700+00:00
- Reason / notes: The rule's correctness rests on the numeric identity floor_div(x, 1) = x plus the semantics of the int→int cast, but QED's fixed theory (equality, uninterpreted functions, order, addition, ite) contains no division or cast algebra, so `FloorDiv` and `Cast` can only be entered as uninterpreted symbols and no encoding yields a valid relation between floorDiv(x,1) and cast(x,τ) — the only equality-based encoding available (filtering/joining on the terms being equal) is circular, since it assumes exactly the identity the rule claims and QED cannot do entailment between independent uninterpreted symbols. This is compounded by RexRN.Const hardcoding literal 0 (the constant 1 cannot be named at all), and closing it is impossible via extend_dsl_file because the missing axioms live in the QED prover's theory, not the Java DSL — the same fundamental limitation that correctly caused FoldDivOne to be skipped. ```

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
- Attempts used: 32
- Last updated: 2026-10-15T04:48:32.687324+00:00
- Reason / notes: FoldFunction requires evaluating an arbitrary function call over constants, which means the prover must know that function's internal evaluation semantics. RuleScript can only express such a call as an uninterpreted pred/proj/aggCall symbol, and QED has no constant-folding semantics for those symbols; the only constant-foldable scalar operators are the built-in logical connectives/literals, which correspond to the sibling Unary/Binary/Comparison rules rather than the Function node. Therefore the gap is a fundamental limitation of the fixed QED model, not a missing Java DSL constructor, so the UNSUPPORTED conclusion is justified.

### `FoldJSONAccessIntoValues` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/project.opt
- Attempts used: 0
- Last updated: 2026-10-15T07:49:54.814972+00:00
- Reason / notes: The rule unnests a single-column Values whose rows are JSON objects into one Values column per JSON key, and rewrites -> field accesses to read those columns. QED has no JSON object value and no key-addressed field access: Values content can only carry literal constants, and a JSON fetch is just an uninterpreted function of an opaque column. The new columns are derived from the keys and values inside each JSON document, which the DSL cannot express, so the after-side Values cannot be built from the before-side one. The only encoding the porter reached splits a Values into a union of single-row Values while the projection still reads the original column. That proves only that splitting a Values by rows commutes with a projection, which is unrelated to this rule, and the verifier rejected it for that reason. The key-presence guards (all referenced keys in the first row, all first-row keys in every row) cannot be stated either. Recorded as skipped rather than published as a proof of something else.

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
- Attempts used: 21
- Last updated: 2026-10-15T05:04:59.474784+00:00
- Reason / notes: FoldLimits is only sound because Limit sorts its input by the named ordering and takes the first n rows, and the guard (inner ordering implies outer ordering) makes the outer re-sort a no-op — i.e., its correctness rests entirely on ordering and row-count semantics that QED explicitly does not model (Sort/Limit/Offset carry no bag-semantic meaning and are treated as uninterpreted operators with no axioms). With Limit uninterpreted, SMT can assign different results to Limit applied at different nesting depths, so even the narrowest special case (Limit(n, Limit(n, X, o)) ≈ Limit(n, X, o)) is refutable, and the OrderingImplies / constant-comparison guards have no footing in the prover at all. Closing this would require adding ordering/count axioms to the QED prover itself — the unchangeable arbiter — so no DSL encoding or extend_dsl_file addition (e.g., a Limit builder feeding JSONSerializer's existing sort form) can make the identity provable. ```

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
- Attempts used: 24
- Last updated: 2026-10-15T05:17:12.623330+00:00
- Reason / notes: FoldMinusZero is a purely scalar arithmetic normalization (`x - 0` → `cast(x, τ)`) whose validity rests on the subtract-by-zero identity and on type-cast semantics; the core language has no scalar minus or cast operator at all (RelRN.Minus is relational EXCEPT), and any operator introduced — including via a DSL extension — serializes as an uninterpreted function symbol with no axioms in the unmodifiable Rust prover, so `minus(x, 0)` and `cast(x)` — or even `minus(x, 0)` and bare `x` — are unequal in some model of QED's fixed theory (equality, UF, order, nat addition, ite) for every instantiation, making UNSUPPORTED the correct call.

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
- Attempts used: 23
- Last updated: 2026-10-15T05:17:53.115925+00:00
- Reason / notes: FoldMultOne's correctness rests entirely on the scalar identity x * 1 = cast_τ(x), but QED's fixed theory contains no multiplication axioms (its only built-in arithmetic is natural-number addition) and, per its own evaluation, interprets explicit type casts only as uninterpreted functions. In RuleScript, `Mult` and `Cast` can therefore only be introduced as distinct uninterpreted operator symbols (via `genericProjectionOp`), so `Mult(x, 1) = Cast(x)` is not a logical validity for any instantiation — a countermodel exists whenever the value domain is non-empty, and narrowing to constant operands or unique/empty sources cannot help since the two symbols are opaque on every value. The missing axioms would have to be added inside the Rust prover itself, which is the immutable arbiter, so no DSL extension or special case can recover this — consistent with the already-verified AGREE verdicts for the sibling rules FoldDivOne and FoldCast. ```

### `FoldNullCast` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullCast discards the cast operator if it has a null input. The resulting
null value has the same type as the Cast operator would have had.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullCast`, not the other rules in that file):

```
# FoldNullCast discards the cast operator if it has a null input. The resulting
# null value has the same type as the Cast operator would have had.
[FoldNullCast, Normalize]
(Cast $input:(Null) $targetTyp:*)
=>
(Null $targetTyp)
```
- Attempts used: 51
- Last updated: 2026-10-15T06:28:26.660980+00:00
- Reason / notes: FoldNullCast's entire content is the scalar identity cast(NULL) = NULL, whose correctness rests on the internal null-preserving semantics of the cast operator — exactly the "operator whose specific internal semantics QED cannot see through as an uninterpreted function" limitation. QED's SMT theory treats casts as uninterpreted functions with no null-propagation axiom, so cast(NULL) = NULL is not a valid consequence for any instantiation (the porter's empirical provable=false on the structurally distinct Project(cast(null)) vs Project(null) encoding is consistent with this), and the only concrete-Cast route degenerates because Calcite constant-folds CAST(NULL AS T) to a typed null literal at pattern-construction time, yielding a vacuous X=X. No DSL extension can close this, since a cast builder would still serialize to an uninterpreted call and the missing fact lives in the unmodifiable prover's semantics, not in a missing builder. ```

### `FoldOneMult` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/numeric.opt

FoldOneMult folds 1 * $right for numeric types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldOneMult`, not the other rules in that file):

```
# FoldOneMult folds 1 * $right for numeric types.
[FoldOneMult, Normalize]
(Mult $left:(Const 1) $right:*)
=>
(Cast $right (BinaryType Mult $left $right))
```
- Attempts used: 24
- Last updated: 2026-10-15T05:59:13.261388+00:00
- Reason / notes: FoldOneMult is a purely scalar normalization whose only semantic content is the identity 1·x = cast_τ(x) (multiply-by-one is the value identity, wrapped in a type-correcting cast), but QED's fixed theory provides no multiplication or cast axioms — only equality, uninterpreted functions, order, nat-addition, and ite — so any faithful encoding, in which `Mult` and `Cast` can enter only as generic uninterpreted operator symbols (and the literal `1` is not even expressible, since `RexRN.Const` hardcodes 0), is refuted by an SMT countermodel for every instantiation. This is a genuine prover-theory limitation, not a missing DSL builder: `extend_dsl_file` can add operators but cannot add axioms, because the axioms live in the trusted, unchangeable Rust prover, and no non-vacuous special case avoids the multiply-by-one identity that *is* the rule. Hence the UNSUPPORTED call stands, consistently with the independently verified AGREE/SKIPPED verdicts for the mirror-image siblings FoldMultOne and FoldMinusZero.

### `FoldPlusZero` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/numeric.opt

FoldPlusZero folds $left + 0 for numeric types.

Note: It is necessary to cast $left to the column type of the binary
operation since the type of $left may not match the column type. For example,
1::int + 0::decimal should result in 1::decimal, not 1::int. The execution
engine panics when it expects one type but receives another, so this cast is
essential. If $left is already of the correct type, the cast will be removed
by the EliminateCast rule. Otherwise, if $left is a constant, the cast will
be folded away by the FoldCast rule.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldPlusZero`, not the other rules in that file):

```
# FoldPlusZero folds $left + 0 for numeric types.
#
# Note: It is necessary to cast $left to the column type of the binary
# operation since the type of $left may not match the column type. For example,
# 1::int + 0::decimal should result in 1::decimal, not 1::int. The execution
# engine panics when it expects one type but receives another, so this cast is
# essential. If $left is already of the correct type, the cast will be removed
# by the EliminateCast rule. Otherwise, if $left is a constant, the cast will
# be folded away by the FoldCast rule.
[FoldPlusZero, Normalize]
(Plus $left:* $right:(Const 0))
=>
(Cast $left (BinaryType Plus $left $right))
```
- Attempts used: 27
- Last updated: 2026-10-15T06:14:53.270171+00:00
- Reason / notes: FoldPlusZero's correctness rests entirely on the additive identity (x + 0 = x) and cast semantics, but QED's fixed oracle theory models scalar operators and casts only as uninterpreted function symbols with no axioms — even encoding the pattern with Calcite's real PLUS/CAST SqlOperators would serialize to UFs the prover has no theory for (the only interpreted addition is internal nat-addition, not user-facing). No re-encoding can bridge that gap: `plus(x, 0)` and `cast(x)` have different operand structures so they cannot be collapsed into one shared symbol, and smuggling the identity in as a table "guaranteed" constraint would merely assume the rule as a premise, making the proof circular rather than universal. Hence a countermodel exists for every encoding, no genuine non-trivial special case is provable (the same reasoning sinks the whole numeric-fold family), and the porter's UNSUPPORTED conclusion is correct.

### `FoldTupleAccessIntoValues` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/project.opt

FoldTupleAccessIntoValues replaces a Values that has a single tuple column and
at least one row with a new Values that has a column for each tuple index.
This works as long as the surrounding Project does not reference the original
tuple column itself, since then it would be invalid to eliminate that
reference. However, references to fields within the tuple are allowed, and are
translated to the new unnested Values columns.

This rule simplifies access to the Values operator in hopes of allowing other
rules to fire.

Example:

SELECT (tup).@1, (tup).@2 FROM (VALUES ((1,2)), ((3,4))) AS v(tup)
=>
SELECT tup_1, tup_2 FROM (VALUES (1, 2), (3, 4)) AS v(tup_1, tup_2)

Extracted from `project.opt` (which defines multiple rules — implement specifically `FoldTupleAccessIntoValues`, not the other rules in that file):

```
# FoldTupleAccessIntoValues replaces a Values that has a single tuple column and
# at least one row with a new Values that has a column for each tuple index.
# This works as long as the surrounding Project does not reference the original
# tuple column itself, since then it would be invalid to eliminate that
# reference. However, references to fields within the tuple are allowed, and are
# translated to the new unnested Values columns.
#
# This rule simplifies access to the Values operator in hopes of allowing other
# rules to fire.
#
# Example:
#
#   SELECT (tup).@1, (tup).@2 FROM (VALUES ((1,2)), ((3,4))) AS v(tup)
#   =>
#   SELECT tup_1, tup_2 FROM (VALUES (1, 2), (3, 4)) AS v(tup_1, tup_2)
#
[FoldTupleAccessIntoValues, Normalize]
(Project
    $input:(Values [ * ... ]) &
        (ColsAreLenOne (OutputCols $input)) &
        (CanUnnestTuplesFromValues $input)
    $projections:* &
        (HasNoDirectTupleReferences
            $projections
            $col:(SingleColFromSet (OutputCols $input))
        )
    $passthrough:* & (ColsAreEmpty $passthrough)
)
=>
(Project
    (UnnestTuplesFromValues
        $input
        $tupleCols:(MakeColsForUnnestTuples $col)
    )
    (FoldTupleColumnAccess $projections $tupleCols $col)
    $passthrough
)
```
- Attempts used: 81
- Last updated: 2026-10-15T07:14:38.658093+00:00
- Reason / notes: The rule's correctness rests on tuple-component semantics — that a Values' single tuple column and its unnested sibling columns denote the same data reorganized (sibling j = the j-th component of the tuple) — which is precisely the backend definition of UnnestTuplesFromValues/FoldTupleColumnAccess, a data transformation rather than an operator rearrangement over fixed data that QED normally proves. QED's flat-scalar theory has no record type or constructor/destructor law to equate a tuple column with its components, so the only encodable proof hard-wires the sibling cells as the access expressions, thereby assuming the very fact the rule is meant to establish. This is a genuine limitation of QED's scalar theory (no tuple algebra / no way to relate two relations by a data-rewriting function), not a symbol-sharing bug or modeling slip a further attempt could fix. ```

### `FoldZeroPlus` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/numeric.opt

FoldZeroPlus folds 0 + $right for numeric types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldZeroPlus`, not the other rules in that file):

```
# FoldZeroPlus folds 0 + $right for numeric types.
[FoldZeroPlus, Normalize]
(Plus $left:(Const 0) $right:*)
=>
(Cast $right (BinaryType Plus $left $right))
```
- Attempts used: 50
- Last updated: 2026-10-15T08:08:18.395820+00:00
- Reason / notes: The rule rewrites the scalar term `PLUS(0, x)` into `CAST(x)`, but both operators are introduced by name as uninterpreted function symbols, and QED's provability requires bag equivalence under *every* instantiation of those symbols — an instantiation with `plus(0,x)=x+1`, `cast(x)=x` refutes the equality, so no SMT reasoning can derive `f(0,x)=g(x)` for independent uninterpreted f and g. This is a fundamental prover limitation, not a DSL gap: the language already exposes arbitrary-arity scalar ops (a multi-source `RexRN.Proj`), constants, and projections, so a faithful encoding (scan one column x; before `project[PLUS(0, x)]`, after `project[CAST(x)]`) would compile, but extending the DSL cannot inject an additive-identity/cast axiom because the QED prover is the unchanging arbiter and its JSON input only carries table-level uninterpreted "guaranteed" predicates, which cannot establish `z = 0` or cast semantics either. The rule's correctness genuinely rests on numeric additive identity and cast semantics — internal operator semantics QED is expressly unable to see through. ```

### `HoistProjectSetSubquery` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt
- Attempts used: 0
- Last updated: 2026-10-15T18:32:27.071667+00:00
- Reason / notes: The rule operates on ProjectSet, whose zipped set-returning functions have no operator in the DSL or the prover, so even the unhoisted input cannot be written. In the DSL a scalar subquery reaches the prover as an opaque $SCALAR_QUERY operator with no relation to a join, so a subquery in a filter or projection cannot be shown equal to the same value taken from a joined column. Encodings with a COUNT scalar subquery in a Select filter against a Project over a left join, and against an inner join, both come back unprovable. The rewrite is a Go function that builds apply joins.

### `HoistProjectSubquery` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt
- Attempts used: 0
- Last updated: 2026-10-15T18:32:27.062783+00:00
- Reason / notes: In the DSL a scalar subquery reaches the prover as an opaque $SCALAR_QUERY operator with no relation to a join, so a subquery in a filter or projection cannot be shown equal to the same value taken from a joined column. Encodings with a COUNT scalar subquery in a Select filter against a Project over a left join, and against an inner join, both come back unprovable. The same hoisting applies to a Project's projection list, and the rewrite is a Go function that builds apply joins, so there is no declarative form to encode.

### `HoistSelectSubquery` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt
- Attempts used: 0
- Last updated: 2026-10-15T18:32:27.055864+00:00
- Reason / notes: In the DSL a scalar subquery reaches the prover as an opaque $SCALAR_QUERY operator with no relation to a join, so a subquery in a filter or projection cannot be shown equal to the same value taken from a joined column. Encodings with a COUNT scalar subquery in a Select filter against a Project over a left join, and against an inner join, both come back unprovable. The rule is also generic: HasHoistableSubquery matches any scalar, Any or Exists subquery and the rewrite is a Go function that builds apply joins, which has no declarative form to encode.

### `HoistValuesSubquery` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistValuesSubquery extracts subqueries from row tuples and joins them with
the Values operator. This and other subquery hoisting patterns create a
single, top-level relational query with no nesting.

This rule is marked as low priority for the same reason as HoistSelectExists.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistValuesSubquery`, not the other rules in that file):

```
# HoistValuesSubquery extracts subqueries from row tuples and joins them with
# the Values operator. This and other subquery hoisting patterns create a
# single, top-level relational query with no nesting.
#
# This rule is marked as low priority for the same reason as HoistSelectExists.
[HoistValuesSubquery, Normalize, LowPriority]
(Values
    $rows:[ ... $item:* & (HasHoistableSubquery $item) ... ]
    $private:*
)
=>
(HoistValuesSubquery $rows $private)
```
- Attempts used: 101
- Last updated: 2026-10-15T12:58:44.685314+00:00
- Reason / notes: The rule's correctness hinges on the scalar-subquery cardinality invariant — that the subquery relation R has at most one row — which makes `Values(row_containing_subquery(R))` bag-equivalent to a cross-join with R. QED reasons over arbitrary uninterpreted bags and cannot verify this ≤1-row constraint for a symbol R; any attempt to encode the before/after pair would be refuted by a two-row instantiation of R. Additionally, the DSL has no constructor for a Values node carrying non-trivial row content or a RexSubQuery expression in a value-producing (non-boolean) position, and even extending the DSL with such constructors would not close the gap, since QED's semiring translation has no modeled semantics for a subquery populating a row value — only boolean-context effects (EXISTS/IN) are recognized. This is a genuine fundamental limitation of QED's relational-algebra fragment, not a missed encoding. ```

### `InlineAnyProjectSet` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

InlineAnyProjectSet replaces an "unnest" subquery used for an ANY comparison
with the "unnest" argument. We only match when the ProjectSet has an empty
input and only projects the result of a single unnest function.

There is some subtlety if the unnest argument evaluates to NULL. In that case,
the result of unnest is empty, and the Any filter evaluates to false. However,
AnyScalar with a NULL second argument evaluates to NULL. To handle this, we
AND the result of AnyScalar with an IsNot NULL check on the argument.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `InlineAnyProjectSet`, not the other rules in that file):

```
# InlineAnyProjectSet replaces an "unnest" subquery used for an ANY comparison
# with the "unnest" argument. We only match when the ProjectSet has an empty
# input and only projects the result of a single unnest function.
#
# There is some subtlety if the unnest argument evaluates to NULL. In that case,
# the result of unnest is empty, and the Any filter evaluates to false. However,
# AnyScalar with a NULL second argument evaluates to NULL. To handle this, we
# AND the result of AnyScalar with an IsNot NULL check on the argument.
[InlineAnyProjectSet, Normalize]
(Any
    (ProjectSet
            (Values [ (Tuple []) ])
            [
                (ZipItem
                    (Function
                        [ $arg:* ]
                        $fnPrivate:(FunctionPrivate "unnest")
                    )
                )
            ]
        ) &
        (CanInlineAnyUnnestSubquery)
    $scalar:*
    $private:*
)
=>
(And
    (AnyScalar $scalar $arg (SubqueryCmp $private))
    (IsNot $arg (Null (TypeOf $arg)))
)
```
- Attempts used: 63
- Last updated: 2026-10-15T12:25:13.897216+00:00
- Reason / notes: The rule's correctness rests on the bespoke relationship between `unnest` (array-element enumeration, NULL→empty) and `AnyScalar` (element-wise ANY, NULL array→NULL) under three-valued NULL semantics, but QED has no array/list type or ANY-over-subquery operator and models only bag semantics over uninterpreted symbols, so it cannot see that `scalar ANY (elements(arg))` entails `AnyScalar(scalar,arg) AND arg IS NOT NULL`. Every faithful encoding therefore either pre-bakes the RHS's AnyScalar into the LHS (a tautology verifying nothing) or models the subquery as a semi-join over an arbitrary uninterpreted element relation that the prover refutes by countermodel, and extending the DSL cannot close the gap since the missing algebra lives in the unchangeable prover, not the pattern builders.

### `InlineAnyWithScanOfValues` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

InlineAnyWithScanOfValues matches on an ANY or NOT ANY expression which was
generated from expressions like `column IN (WithScan)` or `column NOT IN
(WithScan)`, where the WITH clause definition was normalized into a VALUES
clause with constants or placeholders. Inlining the Values expression allows
other optimizations to trigger.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `InlineAnyWithScanOfValues`, not the other rules in that file):

```
# InlineAnyWithScanOfValues matches on an ANY or NOT ANY expression which was
# generated from expressions like `column IN (WithScan)` or `column NOT IN
# (WithScan)`, where the WITH clause definition was normalized into a VALUES
# clause with constants or placeholders. Inlining the Values expression allows
# other optimizations to trigger.
[InlineAnyWithScanOfValues, Normalize]
(Any
    (WithScan $withScanPrivate:*)
    $scalar:* &
        (Let ($values $ok):(BoundValues $withScanPrivate) $ok) &
        (CanInlineWithScanOfValues
            $values
            $withScanPrivate
            $scalar
        )
    $anyPrivate:*
)
=>
(Any
    (InlineWithScanOfValues $values $withScanPrivate)
    $scalar
    $anyPrivate
)
```
- Attempts used: 32
- Last updated: 2026-10-15T13:51:08.612195+00:00
- Reason / notes: The rule's only semantic content is substituting a CTE reference (WithScan) for its defining VALUES expression — i.e., a let-binding of a named relation to a relational expression. QED's semantics (and the JSON format its immutable Rust prover accepts, as mirrored by JSONSerializer's node set) has no construct for a defined/aliased relation: every named relation is an uninterpreted source whose contents can be neither defined by nor proven equal to any expression, so any encoding is either the same symbol on both sides (before ≡ after, vacuous) or two independent sources for which the equivalence is not a universal theorem. No Java-side DSL extension (e.g., adding a `Values` builder, which the JSON format already carries) can close the gap, because the missing piece is let-binding semantics inside the trusted, unmodifiable prover — making this a genuine QED limitation rather than a symbol-sharing bug or a missed encoding. ```

### `InlineExistsSelectTuple` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

InlineExistsSelectTuple splits a tuple equality filter into multiple
(per-column) equalities, in the case where the tuple on one side is being
projected.

We are specifically handling the case when this is under Exists because we
don't have to keep the same output columns for the Select. This case is
important because it is produced for an IN subquery:

SELECT * FROM ab WHERE (a, b) IN (SELECT c, d FROM cd)

Without this rule, we would not be able to produce a lookup join plan for such
a query.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `InlineExistsSelectTuple`, not the other rules in that file):

```
# InlineExistsSelectTuple splits a tuple equality filter into multiple
# (per-column) equalities, in the case where the tuple on one side is being
# projected.
#
# We are specifically handling the case when this is under Exists because we
# don't have to keep the same output columns for the Select. This case is
# important because it is produced for an IN subquery:
#
#   SELECT * FROM ab WHERE (a, b) IN (SELECT c, d FROM cd)
#
# Without this rule, we would not be able to produce a lookup join plan for such
# a query.
#
[InlineExistsSelectTuple, Normalize]
(Exists
    (Select
        (Project
            $input:*
            [
                ...
                (ProjectionsItem $tuple:(Tuple) $tupleCol:*)
                ...
            ]
        )
        $filters:[
            ...
            $item:(FiltersItem
                (Eq
                    # CommuteVar ensures that the variable is on the left.
                    (Variable
                        $varCol:* &
                            (EqualsColumn $varCol $tupleCol)
                    )
                    $rhs:(Tuple) &
                        (TuplesHaveSameLength $tuple $rhs)
                )
            )
            ...
        ]
    )
    $existsPrivate:*
)
=>
(Exists
    (Select
        $input
        (ConcatFilters
            (RemoveFiltersItem $filters $item)
            (SplitTupleEq $tuple $rhs)
        )
    )
    $existsPrivate
)
```
- Attempts used: 26
- Last updated: 2026-10-15T14:57:47.990439+00:00
- Reason / notes: The rule's core semantic content is the definitional expansion of one tuple-equality predicate into a conjunction of per-component equalities, but QED models every scalar operator as an uninterpreted function over a flat value domain — RelType.java states all types are lowered to integers for the prover, JSONSerializer.java has no tuple-construction or tuple-comparison node (scalars are only columns, literals, uninterpreted calls, and field accesses), and the Rust prover is off-limits — so there is no JSON/SMT encoding under which `tEq(T(c,d), T(a,b))` is related to `c=a ∧ d=b`; a countermodel (tEq true, T arbitrary, components unequal) defeats it. A `extend_dsl_file` edit can only add Java builders that ultimately serialize to an uninterpreted `RexCall`, so this is a fundamental QED limitation, not a missing builder; the only provable encoding would pre-split the before side, which stops matching the rule's pattern (an unsplit tuple `Eq`) and degenerates into the separate `EliminateExistsProject` + filter-pushdown rule that is itself a distinct rule in

### `InlineUDF` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/inline.opt

InlineUDF converts a UDF to a subquery. A UDF can only be inlined if it is
non-volatile and has a single statement in the function body. See
IsInlinableUDF for more details.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineUDF`, not the other rules in that file):

```
# InlineUDF converts a UDF to a subquery. A UDF can only be inlined if it is
# non-volatile and has a single statement in the function body. See
# IsInlinableUDF for more details.
[InlineUDF, Normalize]
(UDFCall $args:* $private:* & (IsInlinableUDF $args $private))
=>
(ConvertUDFToSubquery $args $private)
```
- Attempts used: 23
- Last updated: 2026-10-15T13:55:59.416842+00:00
- Reason / notes: InlineUDF's sole correctness content is the definitional identity "an inlinable UDF call evaluates to its single-statement body as a scalar subquery," but QED models the UDF as an uninterpreted scalar operator and the scalar subquery's `unique` as an uninterpreted bag-to-value function (only EXISTS has concrete subquery semantics), with no axiom or concrete evaluation that could ever link a function symbol to its body; hence the only encodable "proof" must assume `udf(args) = unique(body)` verbatim as a shared side-condition and then merely substitute equals for equals, which is vacuous rather than a verification of the rewrite. ```

### `InlineWith` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/with.opt

InlineWith replaces use of a With which is referenced at most one time with
the contents of the With itself.

Extracted from `with.opt` (which defines multiple rules — implement specifically `InlineWith`, not the other rules in that file):

```
# InlineWith replaces use of a With which is referenced at most one time with
# the contents of the With itself.
[InlineWith, Normalize]
(With
    $binding:*
    $input:*
    $withPrivate:* & (CanInlineWith $binding $input $withPrivate)
)
=>
(InlineWith $binding $input $withPrivate)
```
- Attempts used: 50
- Last updated: 2026-10-15T14:32:58.449757+00:00
- Reason / notes: QED's relational fragment contains no let/With/alias operator — JSONSerializer only emits scan/values/filter/project/join/correlate/group/union/intersect/except/sort, and the prover knows no defined-relation node, so a CTE reference is an uninterpreted table whose contents can be neither defined by nor equated to its defining expression (table constraints are only single-table integrity checks). The only encodings are to treat reference and definition as unrelated symbols (QED correctly cannot prove equality) or as the same symbol (before and after become the identical plan, a vacuous proof), and since the prover is the unmodifiable arbiter no DSL extension can add a with node — the rule's entire content is precisely that binding substitution, so it is genuinely outside what QED can prove. ```

### `NormCycleTestRelFalseToTrue` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/cycle.opt

Extracted from `cycle.opt` (which defines multiple rules — implement specifically `NormCycleTestRelFalseToTrue`, not the other rules in that file):

```
[NormCycleTestRelFalseToTrue, Normalize]
(NormCycleTestRel (False))
=>
(NormCycleTestRel (True))
```
- Attempts used: 21
- Last updated: 2026-10-15T14:46:15.819999+00:00
- Reason / notes: This is a test-only, cycle-inducing rewrite for `NormCycleTestRel`, an operator with no defined evaluation semantics, so its only semantic content is `f(False) = f(True)` for an uninterpreted function `f` applied to two distinct boolean constants. QED is a sound bag-equivalence prover that models uninterpreted operators as arbitrary functions, and `f(a) = f(b)` for `a ≠ b` is not a theorem (a countermodel exists), so no faithful relational encoding — e.g. `Project(f(False))` vs `Project(f(True))` over a scan — can be proved equivalent unless the operator is misencoded as argument-ignoring, which would no longer be this rule. There is genuinely no correctness property for QED to establish, and `extend_dsl_file` cannot help because the blocker is the rule's lack of a semantic equivalence, not a missing operator. ```

### `NormCycleTestRelTrueToFalse` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/cycle.opt

The following two rules create a normalization rule cycle for the
NormCycleTestRel expression. This rule cycle is used to test that the cycle
can be detected and a stack overflow does not occur. See the cycle test file.

Extracted from `cycle.opt` (which defines multiple rules — implement specifically `NormCycleTestRelTrueToFalse`, not the other rules in that file):

```
# The following two rules create a normalization rule cycle for the
# NormCycleTestRel expression. This rule cycle is used to test that the cycle
# can be detected and a stack overflow does not occur. See the cycle test file.
[NormCycleTestRelTrueToFalse, Normalize]
(NormCycleTestRel (True))
=>
(NormCycleTestRel (False))
```
- Attempts used: 24
- Last updated: 2026-10-15T14:56:57.893197+00:00
- Reason / notes: The rewrite `NormCycleTestRel(True) => NormCycleTestRel(False)` has no defined evaluation semantics — it exists solely to exercise the optimizer's cycle-detection machinery — so its only semantic content is asserting f(True) = f(False) for an uninterpreted function f over two distinct boolean constants. Any faithful relational embedding (e.g., Project(f(True)) vs Project(f(False)) over a possibly non-empty scan) is not a valid theorem: a countermodel exists where f maps true and false to different values, and QED, being sound, correctly refuses to prove it. This is not a DSL gap or encoding mistake — the literals and generic projection operators are all present — but a case where the underlying claim is simply false and the intended property (cycle detection) is an engine-level behavior with no bag-semantic content for QED to verify. ```

### `NormalizeArrayFlattenToAgg` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

ArrayFlattenToAgg converts a correlated ArrayFlatten to an aggregation.
This rule exists because:

1. We cannot do the aggregation method if we don't have a scalar type
(for instance, if we have a tuple type).
2. We cannot decorrelate an ArrayFlatten directly (but we can decorrelate
an aggregation). So it's desirable to perform this conversion in the
interest of decorrelation.

So the outcome is that we can perform uncorrelated ARRAY(...)s over any
datatype, and correlated ones only over the types that array_agg supports.

Note that optbuilder should have already verified that if the input is
correlated, then we can array_agg over the input type.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `NormalizeArrayFlattenToAgg`, not the other rules in that file):

```
# ArrayFlattenToAgg converts a correlated ArrayFlatten to an aggregation.
# This rule exists because:
#
#     1. We cannot do the aggregation method if we don't have a scalar type
#        (for instance, if we have a tuple type).
#     2. We cannot decorrelate an ArrayFlatten directly (but we can decorrelate
#        an aggregation). So it's desirable to perform this conversion in the
#        interest of decorrelation.
#
# So the outcome is that we can perform uncorrelated ARRAY(...)s over any
# datatype, and correlated ones only over the types that array_agg supports.
#
# Note that optbuilder should have already verified that if the input is
# correlated, then we can array_agg over the input type.
[NormalizeArrayFlattenToAgg, Normalize]
(ArrayFlatten
    $input:*
    $private:* & (CanNormalizeArrayFlatten $input $private)
)
=>
(Coalesce
    [
        (Subquery
            (ScalarGroupBy
                $input
                [
                    (AggregationsItem
                        (ArrayAgg
                            (Variable
                                $requestedCol:(SubqueryRequestedCol
                                    $private
                                )
                            )
                        )
                        (MakeArrayAggCol
                            (ArrayType $requestedCol)
                        )
                    )
                ]
                (MakeGrouping
                    (MakeEmptyColSet)
                    (SubqueryOrdering $private)
                )
            )
            (MakeUnorderedSubquery)
        )
        (Array [] (ArrayType $requestedCol))
    ]
)
```
- Attempts used: 31
- Last updated: 2026-10-15T15:14:32.137610+00:00
- Reason / notes: The rule's soundness is precisely the identity `ArrayFlatten(S) ≡ COALESCE(scalar-subquery(array_agg over S), [])`, which requires list/ordering semantics for arrays, array_agg's specific algebra (its output being the ordered list of the input's values per the subquery ordering), and a value-returning subquery's NULL-on-empty single-row behavior — none of which exist in QED's bag-semiring model, where all virtual types become integers and aggregates/subqueries are uninterpreted atoms knowable only up to bag equality of their inputs, so the two sides of the rule remain independent symbols whose equality is not a theorem under arbitrary instantiation (and cannot be made one by renaming, since that would collapse the rule to a trivial identity). A DSL extension cannot close this gap: even adding scalar-subquery or coalesce builders, the frozen prover lacks the required list/NULL/cardinality axioms (the same verified limitation behind the AGREE'd sibling scalar.opt subquery rulings), and no narrowing — uncorrelated, single-column, or non-empty input — removes the core "array_agg over S equals the array of S" obligation. ```

### `NormalizeCmpConstMinus` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

NormalizeCmpConstMinus builds up constant expression trees on one side of the
comparison, in cases like this:
cmp          cmp
/  \         /  \
[-]   2  ->  [-]   a
/   \        /   \
1     a      1     2

See NormalizeCmpPlusConst for more details.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `NormalizeCmpConstMinus`, not the other rules in that file):

```
# NormalizeCmpConstMinus builds up constant expression trees on one side of the
# comparison, in cases like this:
#      cmp          cmp
#      /  \         /  \
#    [-]   2  ->  [-]   a
#   /   \        /   \
#  1     a      1     2
#
# See NormalizeCmpPlusConst for more details.
[NormalizeCmpConstMinus, Normalize]
(Eq | Ge | Gt | Le | Lt
    (Minus $leftLeft:(Const) $leftRight:^(ConstValue))
    $right:(Const) &
        (ArithmeticErrorsOnOverflow
            (TypeOf $leftLeft)
            (TypeOf $right)
        ) &
        (CanConstructBinary Minus $leftLeft $right) &
        (Let
            ($result $ok):(FoldBinary Minus $leftLeft $right) $ok
        )
)
=>
((OpName) $result $leftRight)
```
- Attempts used: 43
- Last updated: 2026-10-15T15:28:40.844841+00:00
- Reason / notes: The rule's soundness rests on the interpreted arithmetic/order identity c1 − a OP c2 ⟺ (c1 − c2) OP a (plus the backend's overflow/fold side conditions), i.e. on the specific algebraic semantics of subtraction and of Eq/Ge/Gt/Le/Lt. QED models minus and comparison as uninterpreted projection/predicate symbols with no ring or order axioms, and that prover is the fixed, unmodifiable arbiter — so under its universal quantification over all instantiations the before and after filters are not equivalent, and no DSL-level encoding or extension can supply the missing axioms. This is a genuine fundamental limitation of QED (it "can't reason about a backend operator's bespoke internal semantics"), not a missed modeling trick. ```

### `NormalizeCmpMinusConst` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

NormalizeCmpMinusConst builds up constant expression trees on one side of the
comparison, in cases like this:
cmp         cmp
/  \        /  \
[-]   2  ->  a  [+]
/   \           /   \
a     1         2     1

See NormalizeCmpPlusConst for more details.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `NormalizeCmpMinusConst`, not the other rules in that file):

```
# NormalizeCmpMinusConst builds up constant expression trees on one side of the
# comparison, in cases like this:
#      cmp         cmp
#      /  \        /  \
#    [-]   2  ->  a  [+]
#   /   \           /   \
#  a     1         2     1
#
# See NormalizeCmpPlusConst for more details.
[NormalizeCmpMinusConst, Normalize]
(Eq | Ge | Gt | Le | Lt
    (Minus $leftLeft:^(ConstValue) $leftRight:(Const))
    $right:(Const) &
        (ArithmeticErrorsOnOverflow
            (TypeOf $right)
            (TypeOf $leftRight)
        ) &
        (CanConstructBinary Plus $right $leftRight) &
        (Let
            ($result $ok):(FoldBinary Plus $right $leftRight) $ok
        )
)
=>
((OpName) $leftLeft $result)
```
- Attempts used: 22
- Last updated: 2026-10-15T15:11:50.912101+00:00
- Reason / notes: The rewrite's validity rests on the value-level algebraic identity (x − c1) OP c2 ⟺ x OP (c2 + c1), but in QED the Minus, Plus, and comparison operators are all uninterpreted function/predicate symbols with no axioms, and the prover's JSON format offers no way to declare group/inverse/order properties over function symbols, so SMT cannot derive the required predicate entailment for any of Eq/Ge/Gt/Le/Lt. No relational embedding (e.g., the two filters over a shared scan) or specialization to particular constants can escape this, since even degenerate instances need facts like minus(x,0)=x, and the FoldBinary/ArithmeticErrorsOnOverflow guards are backend value-domain semantics (overflow behavior) that QED's abstract uninterpreted model does not represent at all — this is a fundamental QED limitation, not a missing DSL builder.

### `NormalizeCmpTimeZoneFunction` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

NormalizeCmpTimeZoneFunction normalizes timezone functions within
comparison operators. It only matches expressions when:

1. The left side of the comparison is a timezone() function.
2. The second argument to timezone() is a variable of type TIMESTAMP.
3. The right side of the comparison is a constant value TIMESTAMPTZ.

Here's an example:

timezone('America/Denver', ts) = '2020-06-01 12:35:55-07'
=>
ts = timezone('America/Denver', '2020-06-01 12:35:55-07')

This normalization is valid because the overloaded function timezone(zone,
TIMESTAMP) is the inverse of timezone(zone, TIMESTAMPTZ).

Extracted from `comp.opt` (which defines multiple rules — implement specifically `NormalizeCmpTimeZoneFunction`, not the other rules in that file):

```
# NormalizeCmpTimeZoneFunction normalizes timezone functions within
# comparison operators. It only matches expressions when:
#
#   1. The left side of the comparison is a timezone() function.
#   2. The second argument to timezone() is a variable of type TIMESTAMP.
#   3. The right side of the comparison is a constant value TIMESTAMPTZ.
#
# Here's an example:
#
#   timezone('America/Denver', ts) = '2020-06-01 12:35:55-07'
#   =>
#   ts = timezone('America/Denver', '2020-06-01 12:35:55-07')
#
# This normalization is valid because the overloaded function timezone(zone,
# TIMESTAMP) is the inverse of timezone(zone, TIMESTAMPTZ).
[NormalizeCmpTimeZoneFunction, Normalize]
(Eq | Ge | Gt | Le | Lt
    (Function $args:* $private:(FunctionPrivate "timezone"))
    $right:(ConstValue) &
        (IsTimestampTZ $right) &
        (Let ($zone $ts $ok):(ScalarPair $args) $ok) &
        (IsTimestamp $ts)
)
=>
((OpName) $ts (MakeTimeZoneFunction $zone $right))
```
- Attempts used: 45
- Last updated: 2026-10-15T15:49:26.023694+00:00
- Reason / notes: The rule is a purely scalar rewrite whose validity depends on the two timezone overloads being mutual (order-preserving) inverses, but QED treats timezone and the comparison operators as uninterpreted symbols with no inverse/monotonicity axioms, so it correctly finds counter-instances and no non-vacuous encoding of any of Eq/Ge/Gt/Le/Lt is provable. The only constraint channel in the JSON is the per-table `guaranteed` field (row-level predicates over a single table's columns via QedTable.getConstraints()), which cannot express a first-order axiom over scalar function symbols, and RRule exposes no rule-level axiom API. Since the trusted Rust prover is unmodifiable, extending the DSL alone cannot supply the missing function axioms, so the limitation is fundamental, not a porter error. ```

### `NormalizeCmpTimeZoneFunctionTZ` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

NormalizeCmpTimeZoneFunctionTZ normalizes timezone functions within
comparison operators. It only matches expressions when:

1. The left side of the comparison is a timezone() function.
2. The second argument to timezone() is a variable of type TIMESTAMPTZ.
3. The right side of the comparison is a constant value TIMESTAMP.

Here's an example:

timezone('America/Denver', tz) = '2020-06-01 12:35:55'
=>
tz = timezone('America/Denver', '2020-06-01 12:35:55')

This normalization is possible because the overloaded function timezone(zone,
TIMESTAMPTZ) is the inverse of timezone(zone, TIMESTAMP).

Extracted from `comp.opt` (which defines multiple rules — implement specifically `NormalizeCmpTimeZoneFunctionTZ`, not the other rules in that file):

```
# NormalizeCmpTimeZoneFunctionTZ normalizes timezone functions within
# comparison operators. It only matches expressions when:
#
#   1. The left side of the comparison is a timezone() function.
#   2. The second argument to timezone() is a variable of type TIMESTAMPTZ.
#   3. The right side of the comparison is a constant value TIMESTAMP.
#
# Here's an example:
#
#   timezone('America/Denver', tz) = '2020-06-01 12:35:55'
#   =>
#   tz = timezone('America/Denver', '2020-06-01 12:35:55')
#
# This normalization is possible because the overloaded function timezone(zone,
# TIMESTAMPTZ) is the inverse of timezone(zone, TIMESTAMP).
[NormalizeCmpTimeZoneFunctionTZ, Normalize]
(Eq | Ge | Gt | Le | Lt
    (Function $args:* $private:(FunctionPrivate "timezone"))
    $right:(ConstValue) &
        (IsTimestamp $right) &
        (Let ($zone $tz $ok):(ScalarPair $args) $ok) &
        (IsTimestampTZ $tz) &
        ^(IsConstValueOrGroupOfConstValues $tz)
)
=>
((OpName) $tz (MakeTimeZoneFunction $zone $right))
```
- Attempts used: 62
- Last updated: 2026-10-15T16:11:39.491453+00:00
- Reason / notes: The rule's soundness rests entirely on an axiom QED does not and cannot have — that the two `timezone` overloads are mutual inverses (and order-preserving for Ge/Gt/Le/Lt) — but in RuleScript they can only be modeled as independent uninterpreted function symbols, and `f(z, tz) OP c ⟺ tz OP g(z, c)` is not valid for arbitrary f, g under any instantiation. No encoding closes the gap: per-table `guaranteed` constraints are row-scoped over a table's own columns and cannot assert a functional-inverse law at the query's arbitrary non-row constant, the prover's JSON format has no axiom channel, and `extend_dsl_file` only extends the Java DSL, not the unmodifiable Rust prover. My own fresh exploration of alternative encodings (relational joins over a representation-relation table, threading the constant through a scan) either still requires relating two independent uninterpreted symbols or degenerates into a trivial identity that no longer states the rule — so this is the documented limitation of no predicate entailment between independent symbols / no access to a backend operator's bespoke semantics, matching the independently-verified skip of the type-swap sibling.

### `NormalizeJoinAnyFilter` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

NormalizeJoinAnyFilter is similar to NormalizeSelectAnyFilter, except that it
operates on Any expressions within Join filters rather than Select filters.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `NormalizeJoinAnyFilter`, not the other rules in that file):

```
# NormalizeJoinAnyFilter is similar to NormalizeSelectAnyFilter, except that it
# operates on Any expressions within Join filters rather than Select filters.
[NormalizeJoinAnyFilter, Normalize]
(Join
    $left:*
    $right:*
    $on:[
        ...
        $item:(FiltersItem
            (Any $anyInput:* $scalar:* $anyPrivate:*)
        )
        ...
    ]
    $private:*
)
=>
((OpName)
    $left
    $right
    (ReplaceFiltersItem
        $on
        $item
        (Exists
            (Select
                $anyInput
                [
                    (FiltersItem
                        (ConstructAnyCondition
                            $anyInput
                            $scalar
                            $anyPrivate
                        )
                    )
                ]
            )
            (ConvertSubToExistsPrivate $anyPrivate)
        )
    )
    $private
)
```
- Attempts used: 64
- Last updated: 2026-10-15T16:56:01.456535+00:00
- Reason / notes: Confirmed against the DSL source: RexRN has no scalar-subquery or quantifier node (only Field/JoinField, uninterpreted Pred/Proj, And/Or/Not/True/False/Const), and the subquery's input relation is nested inside a scalar expression, so it is unreachable from any join-condition RexRN — the ANY and EXISTS conjuncts can only be encoded as independent uninterpreted predicate symbols, which QED is designed not to relate. The rule's validity rests entirely on the semantic definitions of the ANY/EXISTS scalar quantifiers over a correlated nested bag (including 3VL null handling in the filter context), which lies outside QED's bag-semantics-with-uninterpreted-functions model, and no DSL extension could close this since the fixed prover has no translation for nested-bag quantification inside filter predicates. Pushing the existential to a relational SEMI-join node changes the plan tree shape and would prove a different join-factoring identity with the ANY conjunct still uninterpreted, so no non-trivial encoding of the rule as stated exists. ```

### `NormalizeLikeAny` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

NormalizeLikeAny replaces `x LIKE '%'` with `x IS NOT NULL`.

Extracted from `select.opt` (which defines multiple rules — implement specifically `NormalizeLikeAny`, not the other rules in that file):

```
# NormalizeLikeAny replaces `x LIKE '%'` with `x IS NOT NULL`.
[NormalizeLikeAny, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Like | ILike
                $left:*
                $pattern:(Const) &
                    (ConstStringEquals $pattern "%")
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
        (IsNot $left (Null (AnyType)))
    )
)
```
- Attempts used: 24
- Last updated: 2026-10-15T16:23:57.227844+00:00
- Reason / notes: The rule's entire semantic content is the equivalence `x LIKE '%' ≡ x IS NOT NULL`, which rests on string/glob theory (that the pattern '%' matches exactly the non-NULL strings). QED serializes `LIKE` to an uninterpreted predicate by name with no string/glob theory and treats `IS NOT NULL` as an independent symbol, so an SMT model can make the two sides diverge (e.g. a row where `like(x,'%')` is true while x is NULL, or a non-NULL x where `like` is false); no DSL extension can supply the missing glob axiom to the fixed Rust prover. This matches the verified precedent for the related `CollapseRepeatedLikePatternWildcards` and `LikeSimplification` rules, and no non-trivial encoding avoids the gap (a non-nullable column makes `IS NOT NULL` a tautology while leaving the uninterpreted `LIKE` unconstrained, so that degenerate case is unprovable too). ```

### `NormalizeTupleEquality` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/comp.opt

NormalizeTupleEquality breaks up expressions like:
(a, b, c) = (x, y, z)
into
(a = x) AND (b = y) AND (c = z)

This rule makes it easier to extract constraints from boolean expressions,
so that recognition code doesn't have to handle the tuple case separately.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `NormalizeTupleEquality`, not the other rules in that file):

```
# NormalizeTupleEquality breaks up expressions like:
#   (a, b, c) = (x, y, z)
# into
#   (a = x) AND (b = y) AND (c = z)
#
# This rule makes it easier to extract constraints from boolean expressions,
# so that recognition code doesn't have to handle the tuple case separately.
[NormalizeTupleEquality, Normalize]
(Eq (Tuple $left:*) (Tuple $right:*))
=>
(NormalizeTupleEquality $left $right)
```
- Attempts used: 44
- Last updated: 2026-10-15T20:08:43.456783+00:00
- Reason / notes: The rule's soundness rests on tuple extensionality — pack(a,b,c)=pack(x,y,z) ⟺ a=x ∧ b=y ∧ c=z — but QED's theory is equality over uninterpreted symbols with every uninterpreted type flattened to an opaque INTEGER atom (per RelType.java), with no product type and no extensionality/injectivity axiom for uninterpreted functions or predicates. Any RuleScript encoding therefore either uses independent symbols (an uninterpreted tuple-equality predicate vs. a conjunction of field equalities), which QED can refute with a countermodel, or reuses a single symbol for both sides, which is a vacuous identity rather than the rule; closing the gap would require adding an extensionality axiom to the Rust prover's theory, which is off-limits (extend_dsl_file can only add Java builders serializing into the prover's existing JSON, not new theory axioms). ```

### `PruneLimitCols` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneLimitCols discards Limit input columns that are never used.

The PruneCols property should prevent this rule (which pushes Project below
Limit) from cycling with the PushLimitIntoProject rule (which pushes Limit
below Project).

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneLimitCols`, not the other rules in that file):

```
# PruneLimitCols discards Limit input columns that are never used.
#
# The PruneCols property should prevent this rule (which pushes Project below
# Limit) from cycling with the PushLimitIntoProject rule (which pushes Limit
# below Project).
[PruneLimitCols, Normalize]
(Project
    (Limit $input:* $limit:* $ordering:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols3
                (OrderingCols $ordering)
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    (Limit
        (PruneCols $input $needed)
        $limit
        (PruneOrdering $ordering $needed)
    )
    $projections
    $passthrough
)
```
- Attempts used: 41
- Last updated: 2026-10-15T20:04:22.301290+00:00
- Reason / notes: PruneLimitCols's correctness rests entirely on Sort/Limit ordering semantics — specifically that, with all ordering columns retained, the top-N selection on the pruned input is bag-equivalent (modulo tie nondeterminism) to the top-N selection on the full input, followed by the outer projection. QED's bag-semantic model has no notion of row order and treats Sort/Limit/Offset as uninterpreted opaque operations, so the SMT solver cannot relate the two distinct Limit applications (one over R, one over pruned(R)) appearing in before() and after(); extending the DSL with Sort/Limit builders (even though JSONSerializer already emits sort/limit JSON) would not close this gap, and the only provable residue — the trivial projection composition π_D(R) ≡ π_D(π_needed(R)) with the Limit deleted — no longer contains the Limit operator that defines the rule. ```

### `PruneOffsetCols` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneOffsetCols discards Offset input columns that are never used.

The PruneCols property should prevent this rule (which pushes Project below
Offset) from cycling with the PushOffsetIntoProject rule (which pushes Offset
below Project).

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneOffsetCols`, not the other rules in that file):

```
# PruneOffsetCols discards Offset input columns that are never used.
#
# The PruneCols property should prevent this rule (which pushes Project below
# Offset) from cycling with the PushOffsetIntoProject rule (which pushes Offset
# below Project).
[PruneOffsetCols, Normalize]
(Project
    (Offset $input:* $offset:* $ordering:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols3
                (OrderingCols $ordering)
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    (Offset
        (PruneCols $input $needed)
        $offset
        (PruneOrdering $ordering $needed)
    )
    $projections
    $passthrough
)
```
- Attempts used: 23
- Last updated: 2026-10-15T20:24:37.539507+00:00
- Reason / notes: PruneOffsetCols' soundness rests entirely on order-dependent row selection — that pruning columns not referenced by the ordering or outer projection leaves the ordered row sequence (and hence which rows OFFSET k skips) unchanged, including tie-break behavior — and QED's semiring/bag theory has no Sort/Limit/Offset construct, no row ordering or identity (all virtual types map to uninterpreted integer columns), and no axiom that could relate Offset(R) to Offset(π(R)). Adding an Offset builder to RelRN cannot close the gap, because the unmodifiable Rust prover has no ordering semantics to interpret it with (per qed.pdf §6.2), so this is a genuine QED limitation rather than a missing DSL capability — consistent with the verified sibling skips of EliminateOffset and PushProjectionThroughLimitAndOffset. ```

### `PushAggFilterIntoScalarGroupBy` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

PushAggFilterIntoScalarGroupBy pushes an aggregate function FILTER
modifier into the input of the ScalarGroupBy operator. This allows the
optimizer to take advantage of an index on the column(s) subject to the
FILTER operation. PushAggFilterIntoScalarGroupBy can match any single
aggregate function, including those that have multiple input arguments.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `PushAggFilterIntoScalarGroupBy`, not the other rules in that file):

```
# PushAggFilterIntoScalarGroupBy pushes an aggregate function FILTER
# modifier into the input of the ScalarGroupBy operator. This allows the
# optimizer to take advantage of an index on the column(s) subject to the
# FILTER operation. PushAggFilterIntoScalarGroupBy can match any single
# aggregate function, including those that have multiple input arguments.
[PushAggFilterIntoScalarGroupBy, Normalize]
(ScalarGroupBy
    $input:*
    $aggregations:[
        $item:(AggregationsItem
            (AggFilter $agg:* $condition:*)
            $aggColID:*
        )
    ]
    $groupingPrivate:*
)
=>
(ScalarGroupBy
    (Select $input [ (FiltersItem $condition) ])
    [ (AggregationsItem $agg $aggColID) ]
    $groupingPrivate
)
```
- Attempts used: 48
- Last updated: 2026-10-15T20:55:39.916379+00:00
- Reason / notes: The rule's only transformation is hoisting a per-call aggregate FILTER (SELECT agg(x) FILTER(WHERE p) FROM t) into a Filter on the ScalarGroupBy's input, and the DSL's AggCall has no filter modifier (the JSON group node only carries operator/operand/distinct/ignoreNulls), so the before-side can at best be written as the aggregate over the already-filtered input, making both sides identical and proving nothing about FILTER semantics. The genuine equivalence needed is "agg(x) FILTER (WHERE p) over all rows = agg(x) over the rows where p holds", which is exactly the kind of aggregate-modifier internal semantics QED cannot see through: it models aggregates only up to bag equality of their input and knows no aggregate algebra, and the prover cannot be extended to add FILTER semantics. Extending the DSL with a filter field on AggCall would be futile since JSONSerializer has no corresponding field and the Rust QED prover is the immutable arbiter. ```

### `PushColumnRemappingIntoValues` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/project.opt

PushColumnRemappingIntoValues folds ProjectionsItems into the passthrough set
if they simply remap Values output columns that are not already in
passthrough. The Values output columns are replaced with the corresponding
columns projected by the folded ProjectionsItems.

Example:

project
├── columns: x:2!null
├── values
│    ├── columns: column1:1!null
│    ├── cardinality: [2 - 2]
│    ├── (1,)
│    └── (2,)
└── projections
└── column1:1 [as=x:2, outer=(1)]
=>
project
├── columns: x:2!null
└── values
├── columns: x:2!null
├── cardinality: [2 - 2]
├── (1,)
└── (2,)

This allows other rules to fire. In the example above, the project would now
be removed by EliminateProject.

Extracted from `project.opt` (which defines multiple rules — implement specifically `PushColumnRemappingIntoValues`, not the other rules in that file):

```
# PushColumnRemappingIntoValues folds ProjectionsItems into the passthrough set
# if they simply remap Values output columns that are not already in
# passthrough. The Values output columns are replaced with the corresponding
# columns projected by the folded ProjectionsItems.
#
# Example:
#
# project
#  ├── columns: x:2!null
#  ├── values
#  │    ├── columns: column1:1!null
#  │    ├── cardinality: [2 - 2]
#  │    ├── (1,)
#  │    └── (2,)
#  └── projections
#       └── column1:1 [as=x:2, outer=(1)]
# =>
# project
#  ├── columns: x:2!null
#  └── values
#       ├── columns: x:2!null
#       ├── cardinality: [2 - 2]
#       ├── (1,)
#       └── (2,)
#
# This allows other rules to fire. In the example above, the project would now
# be removed by EliminateProject.
[PushColumnRemappingIntoValues, Normalize]
(Project
    $input:(Values)
    $projections:*
    $passthrough:* &
        (CanPushColumnRemappingIntoValues
            $projections
            $passthrough
            $input
        )
)
=>
(PushColumnRemappingIntoValues $input $projections $passthrough)
```
- Attempts used: 25
- Last updated: 2026-10-16T03:22:05.916658+00:00
- Reason / notes: The rule's only effect is renaming a Values column and moving a plain column-reference projection item into the passthrough set — a pure column-identity/metadata transformation with zero row-bag delta, and QED's nameless positional model (where both a passthrough column and a column-ref projection item serialize to the identical `{"column": i}` reference, and Values/Project column names carry no semantics) makes before and after the same object in the model, so any encoding would prove a vacuous identity rather than the rule. This gap lives in the fixed Rust prover's representation, not in a closable DSL builder: even extending the DSL with a Values-with-rows operator would only yield proofs of "identity projection is identity," which hold independently of the rule's conditions.

### `PushLimitIntoJoinLeft` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/limit.opt

PushLimitIntoJoinLeft pushes a Limit into the left input of an InnerJoin or
LeftJoin when rows from the left input are guaranteed to be preserved by the
join. Since the join creates an output row for each left input row, we only
need that many rows from that input. We can only do this if the limit ordering
refers only to the left input columns. We check that the cardinality of the
left input is more than the limit, to prevent repeated applications of the
rule. We also check that the left input has no outer columns to avoid
interfering with decorrelation.

Why can we only match InnerJoins and LeftJoins (e.g. not FullJoins)?

CREATE TABLE t_x (x INT PRIMARY KEY)
CREATE TABLE t_r (r INT NOT NULL REFERENCES t_x(x))

SELECT * FROM t_r FULL JOIN t_x ON r = x LIMIT 10
vs
SELECT * FROM (SELECT * FROM t_r LIMIT 10) FULL JOIN t_x ON r = x LIMIT 10

In the first query, all rows from t_r (left rows) would have a chance to match
with a row from t_x. In the second query, left rows that otherwise would have
matched may be filtered out by the limit. Rows from t_x would then no longer
have matches, and would be outputted by the FullJoin with the left side
(t_r columns) null-extended. Therefore, pushing the limit into a join input
that may be null-extended (either input of a FullJoin) can lead to output rows
being replaced with null values.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushLimitIntoJoinLeft`, not the other rules in that file):

```
# PushLimitIntoJoinLeft pushes a Limit into the left input of an InnerJoin or
# LeftJoin when rows from the left input are guaranteed to be preserved by the
# join. Since the join creates an output row for each left input row, we only
# need that many rows from that input. We can only do this if the limit ordering
# refers only to the left input columns. We check that the cardinality of the
# left input is more than the limit, to prevent repeated applications of the
# rule. We also check that the left input has no outer columns to avoid
# interfering with decorrelation.
#
# Why can we only match InnerJoins and LeftJoins (e.g. not FullJoins)?
#
#   CREATE TABLE t_x (x INT PRIMARY KEY)
#   CREATE TABLE t_r (r INT NOT NULL REFERENCES t_x(x))
#
#   SELECT * FROM t_r FULL JOIN t_x ON r = x LIMIT 10
# vs
#   SELECT * FROM (SELECT * FROM t_r LIMIT 10) FULL JOIN t_x ON r = x LIMIT 10
#
# In the first query, all rows from t_r (left rows) would have a chance to match
# with a row from t_x. In the second query, left rows that otherwise would have
# matched may be filtered out by the limit. Rows from t_x would then no longer
# have matches, and would be outputted by the FullJoin with the left side
# (t_r columns) null-extended. Therefore, pushing the limit into a join input
# that may be null-extended (either input of a FullJoin) can lead to output rows
# being replaced with null values.
[PushLimitIntoJoinLeft, Normalize]
(Limit
    $input:(InnerJoin | LeftJoin
            $left:* & ^(HasOuterCols $left)
            $right:*
            $on:*
            $private:*
        ) &
        (JoinPreservesLeftRows $input)
    $limitExpr:(Const $limit:*) &
        (IsPositiveInt $limit) &
        (CanRepresentMaxRows $limit) &
        ^(LimitGeMaxRows $limit $left)
    $ordering:* &
        (OrderingCanProjectCols
            $ordering
            $cols:(OutputCols $left)
        )
)
=>
(Limit
    ((OpName $input)
        (Limit $left $limitExpr (PruneOrdering $ordering $cols))
        $right
        $on
        $private
    )
    $limitExpr
    $ordering
)
```
- Attempts used: 29
- Last updated: 2026-10-16T02:35:42.077551+00:00
- Reason / notes: The rule's soundness rests entirely on ordered top-N semantics — that the outer Limit's selected rows must come from within the first-n left-input rows under the join's preservation guarantee — but QED models queries under pure bag semantics and treats Limit/Sort as uninterpreted QOps with no row-count, ordering, or top-N axioms (qed.pdf §6.2), so the before-side Limit(Join(L,R)) and after-side Limit(Join(Limit(L),R)) are structurally distinct uninterpreted applications that SMT can never equate; no non-vacuous special case reduces to a bag equality (the rule's own guards explicitly exclude the degenerate limit≥|L| case), and the gap is in the trusted prover's model rather than a missing DSL builder, since JSONSerializer already serializes LogicalSort's offset/limit fields which QED treats opaquely. ```

### `PushLimitIntoJoinRight` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/limit.opt

PushLimitIntoJoinRight mirrors PushLimitIntoJoinLeft.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushLimitIntoJoinRight`, not the other rules in that file):

```
# PushLimitIntoJoinRight mirrors PushLimitIntoJoinLeft.
[PushLimitIntoJoinRight, Normalize]
(Limit
    $input:(InnerJoin
            $left:*
            $right:* & ^(HasOuterCols $right)
            $on:*
            $private:*
        ) &
        (JoinPreservesRightRows $input)
    $limitExpr:(Const $limit:*) &
        (IsPositiveInt $limit) &
        (CanRepresentMaxRows $limit) &
        ^(LimitGeMaxRows $limit $right)
    $ordering:* &
        (OrderingCanProjectCols
            $ordering
            $cols:(OutputCols $right)
        )
)
=>
(Limit
    ((OpName $input)
        $left
        (Limit $right $limitExpr (PruneOrdering $ordering $cols))
        $on
        $private
    )
    $limitExpr
    $ordering
)
```
- Attempts used: 22
- Last updated: 2026-10-16T03:02:23.931826+00:00
- Reason / notes: `PushLimitIntoJoinRight`'s correctness rests entirely on top-n/ordering semantics — distributing `Limit` through a join that preserves the right rows — but QED decides equivalence over bag semantics with uninterpreted functions and gives `Limit`/`Sort`/`Offset` no bag-semantic meaning (they are uninterpreted QOps), so the before term `Limit(Join(L,R),n)` and the after term `Limit(Join(L,Limit(R,n)),n)` are structurally distinct opaque applications whose inner arguments cannot be shown equal, and SMT cannot equate them. The only equatable degenerate case (empty left input) makes both sides collapse to the same `Limit(∅)` without exercising the pushdown, so it's vacuous and not a usable special case. No `extend_dsl_file` can close the gap because the missing top-n axioms live in the unmodifiable Rust prover, not in the DSL or serializer.

### `PushLimitIntoLock` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/limit.opt

PushLimitIntoLock pushes the Limit operator into its Lock input, as long as
the Lock does not use a SKIP LOCKED wait policy. This helps minimize the
number of rows locked.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushLimitIntoLock`, not the other rules in that file):

```
# PushLimitIntoLock pushes the Limit operator into its Lock input, as long as
# the Lock does not use a SKIP LOCKED wait policy. This helps minimize the
# number of rows locked.
[PushLimitIntoLock, Normalize]
(Limit
    (Lock $input:* $private:*) & ^(LockUsesSkipLocked $private)
    $limit:*
    $ordering:*
)
=>
(Lock (Limit $input $limit $ordering) $private)
```
- Attempts used: 22
- Last updated: 2026-10-16T03:14:10.379397+00:00
- Reason / notes: The rewrite Limit(Lock(X)) ⟹ Lock(Limit(X)) is only sound because (a) a non-SKIP-LOCKED Lock is row-identity over its input — a backend-specific internal semantic that QED cannot see through, since Lock would be merely a distinct uninterpreted operator — and (b) Limit's row-count/ordering behavior, which QED explicitly does not model (Sort/Limit/Offset have no bag-semantic meaning; no Limit builder even exists in RelRN, and adding one would still leave it an axiom-free uninterpreted QOp). With both operators uninterpreted and unrelated, the claim reduces to proving two distinct uninterpreted functions commute, which the SMT solver refutes for every instantiation, and the rule's actual payoff (locking fewer rows) is a side effect invisible to bag equivalence, so no non-vacuous special case is encodable. ```

### `PushLimitIntoOffset` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/limit.opt

PushLimitIntoOffset pushes the Limit operator into the offset. This should
not have a negative impact but it would allow the use of the GenerateLimitedScans
rule.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushLimitIntoOffset`, not the other rules in that file):

```
# PushLimitIntoOffset pushes the Limit operator into the offset. This should
# not have a negative impact but it would allow the use of the GenerateLimitedScans
# rule.
[PushLimitIntoOffset, Normalize]
(Limit
    (Offset
        $input:*
        $offsetExpr:(Const $offset:* & (IsPositiveInt $offset))
        $offsetOrdering:*
    )
    (Const $limit:* & (IsPositiveInt $limit))
    $limitOrdering:* &
        (IsSameOrdering $offsetOrdering $limitOrdering) &
        (CanAddConstInts $limit $offset)
)
=>
(Offset
    (Limit $input (AddConstInts $offset $limit) $limitOrdering)
    $offsetExpr
    $offsetOrdering
)
```
- Attempts used: 33
- Last updated: 2026-10-16T03:35:08.883908+00:00
- Reason / notes: The rule's correctness rests entirely on order- and cardinality-based semantics: which rows `Limit` keeps depends on which rows come first after `Offset` skips `off` of them, so `Limit(Offset(X, off), lim) ≡ Offset(Limit(X, off+lim), off)` holds only under row ordering. QED's bag semantics models no `Sort`/`Limit`/`Offset` operators and carries no axioms about row order or row counts (and the DSL exposes no builders for them either), so the two distinct nestings are not provably equivalent — any RuleScript encoding must drop both wrappers, collapsing before and after to the vacuous identity `X = X`, a genuine prover-side limitation that extending `RelRN.java` cannot close since the QED prover itself cannot be modified to add ordering/counting axioms. ```

### `PushLimitIntoOrdinality` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/limit.opt

PushLimitIntoOrdinality pushes the Limit operator into the Ordinality
operator when the ordering associated with both operators allows it.

Pushing the limit as far as possible down the tree shouldn't have negative
effects, but will reduce the number of rows processed by operators higher up,
and if the limit is pushed all the way down to a scan, the scan can be limited
directly.

In order to prevent this rule from affecting:
1. the set of rows kept by the limit,
2. the ordinals assigned to those rows by the ordinality, and
3. the final ordering of the rows,
the new limit's ordering should be "extended" to imply the ordinality's
ordering, so it is set to be an intersection of the original limit ordering
and the ordinality's ordering (see OrderingChoice.Intersection).

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushLimitIntoOrdinality`, not the other rules in that file):

```
# PushLimitIntoOrdinality pushes the Limit operator into the Ordinality
# operator when the ordering associated with both operators allows it.
#
# Pushing the limit as far as possible down the tree shouldn't have negative
# effects, but will reduce the number of rows processed by operators higher up,
# and if the limit is pushed all the way down to a scan, the scan can be limited
# directly.
#
# In order to prevent this rule from affecting:
#   1. the set of rows kept by the limit,
#   2. the ordinals assigned to those rows by the ordinality, and
#   3. the final ordering of the rows,
# the new limit's ordering should be "extended" to imply the ordinality's
# ordering, so it is set to be an intersection of the original limit ordering
# and the ordinality's ordering (see OrderingChoice.Intersection).
[PushLimitIntoOrdinality, Normalize]
(Limit
    (Ordinality $input:* $private:*)
    $limit:*
    $limitOrdering:* &
        (OrderingCanProjectCols
            $limitOrdering
            (OutputCols $input)
        ) &
        (OrderingIntersects
            (OrdinalityOrdering $private)
            $limitOrdering
        )
)
=>
(Ordinality
    (Limit
        $input
        $limit
        (OrderingIntersection
            (OrdinalityOrdering $private)
            $limitOrdering
        )
    )
    $private
)
```
- Attempts used: 25
- Last updated: 2026-10-16T03:44:13.693492+00:00
- Reason / notes: The rule's correctness rests entirely on ordering/prefix-selection semantics — which rows the Limit keeps, the ordinals Ordinality assigns, and the final row order — none of which QED models (Sort/Limit/Offset have no bag-semantic meaning); because both sides differ only in the placement and ordering of that unmodelable Limit, dropping it yields the vacuous identity Ordinality(input) ≡ Ordinality(input), and even extending the DSL with Limit/Ordinality builders would not help since the QED prover fundamentally lacks the semantics to reason about them. ```

### `PushLimitIntoProject` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/limit.opt

PushLimitIntoProject pushes the Limit operator into its Project input. It is
desirable to push the Limit operator as low in the query as possible, in order
to minimize the number of rows that other operators need to process.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushLimitIntoProject`, not the other rules in that file):

```
# PushLimitIntoProject pushes the Limit operator into its Project input. It is
# desirable to push the Limit operator as low in the query as possible, in order
# to minimize the number of rows that other operators need to process.
[PushLimitIntoProject, Normalize]
(Limit
    (Project $input:* $projections:* $passthrough:*)
    $limit:*
    $ordering:* &
        (OrderingCanProjectCols
            $ordering
            $cols:(OutputCols $input)
        )
)
=>
(Project
    (Limit $input $limit (PruneOrdering $ordering $cols))
    $projections
    $passthrough
)
```
- Attempts used: 6
- Last updated: 2026-10-16T03:44:34.277695+00:00
- Reason / notes: The rule's soundness rests entirely on Limit's ordered-prefix semantics plus its side condition `OrderingCanProjectCols`: the top-N rows under an ordering that must be referable only through input columns have to survive projection unchanged. QED operates purely under bag semantics and has no model of Limit/Sort/Offset ordering at all (Limit is at best an uninterpreted QOp whose output is unconstrained by its input, under which L(π(R)) ≡ π(L(R)) is already false — rows that merge under π across the cut point yield different bags), and RRule's before/after pair has no mechanism to state the `OrderingCanProjectCols` guard, so the only provable encoding is the vacuous identity-projection degenerate. ```

### `PushLimitIntoWindow` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/window.opt

PushLimitIntoWindow moves a Limit below a Window when able. This is
all-or-nothing. Even if we could push the limit below *some* of the window
functions, if there are any we cannot, then we don't. This is because
computing additional window functions is not that expensive, and the
expensive part is doing the sorting and partitioning. Once exec supports
passing orderings through and does not require re-partitioning and re-sorting
of window functions, pushing past some-but-not-all of the window functions
might be profitable.

SELECT rank() OVER (ORDER BY c) FROM abc ORDER BY c LIMIT 10
=>
SELECT
rank() OVER (ORDER BY c)
FROM
(SELECT c FROM abc ORDER BY c LIMIT 10)

SELECT rank() OVER (PARTITION BY b ORDER BY c) FROM abc LIMIT 10
=>
SELECT
rank() OVER (PARTITION BY b ORDER BY c)
FROM
(SELECT b, c FROM abc ORDER BY b, c LIMIT 10)

First, we construct a "segmented ordering" consisting of the Window's
partition columns followed by its ordering columns (the relative positions of
the partition columns are arbitrary). This ordering is useful because it
performs the partitioning and then the ordering within each partition.  If
this ordering does not imply the Limit's ordering, we do not proceed.

Since we now know that the segmented ordering is stronger than the Limit's
ordering, it's safe to replace the limit's ordering with it.

The Limit having the segmented ordering means that there are three kinds of
partitions:
1. those that are completely contained within the limited set of rows,
2. those that are completely excluded from the set of rows, and
3. *at most one* partition which is "cut off" partway through.
Including the window function's ordering in the Limit's ordering does not
matter for (1)- and (2)-style partitions (since the window function itself
will re-sort them), but for the (3)-style partition, we need to ensure that
the limit operator allows through a prefix of it, rather than an arbitrary
subset.

Finally, we require that every window function+frame pair being computed has
the "prefix-safe" property. A window function is prefix safe if it can be
correctly computed over only a prefix of a partition. For example, rank() has
this property because rows that come later in the ordering don't affect the
rank of the rows before, but avg()+UNBOUNDED {PRECEDING,FOLLOWING} doesn't,
because we must see the entire partition to compute the average over it.

TODO(justin): Add a rule that translates a limit with an ordering on rank()
or dense_rank() into one using the ordering of the window function. This will
allow us to push down limits in cases like:

SELECT rank() OVER (ORDER BY f) rnk FROM a ORDER BY rnk LIMIT 10
=>
SELECT rank() OVER (ORDER BY f) rnk FROM a ORDER BY f LIMIT 10
=>
SELECT rank() OVER (ORDER BY f) rnk FROM (SELECT * FROM a ORDER BY f LIMIT 10)

Extracted from `window.opt` (which defines multiple rules — implement specifically `PushLimitIntoWindow`, not the other rules in that file):

```
# PushLimitIntoWindow moves a Limit below a Window when able. This is
# all-or-nothing. Even if we could push the limit below *some* of the window
# functions, if there are any we cannot, then we don't. This is because
# computing additional window functions is not that expensive, and the
# expensive part is doing the sorting and partitioning. Once exec supports
# passing orderings through and does not require re-partitioning and re-sorting
# of window functions, pushing past some-but-not-all of the window functions
# might be profitable.
# 
# SELECT rank() OVER (ORDER BY c) FROM abc ORDER BY c LIMIT 10
# => 
# SELECT
#     rank() OVER (ORDER BY c)
# FROM
#     (SELECT c FROM abc ORDER BY c LIMIT 10)
# 
# SELECT rank() OVER (PARTITION BY b ORDER BY c) FROM abc LIMIT 10
# => 
# SELECT
#     rank() OVER (PARTITION BY b ORDER BY c)
# FROM
#     (SELECT b, c FROM abc ORDER BY b, c LIMIT 10)
# 
# First, we construct a "segmented ordering" consisting of the Window's
# partition columns followed by its ordering columns (the relative positions of
# the partition columns are arbitrary). This ordering is useful because it
# performs the partitioning and then the ordering within each partition.  If
# this ordering does not imply the Limit's ordering, we do not proceed.
# 
# Since we now know that the segmented ordering is stronger than the Limit's
# ordering, it's safe to replace the limit's ordering with it.
# 
# The Limit having the segmented ordering means that there are three kinds of
# partitions:
#   1. those that are completely contained within the limited set of rows,
#   2. those that are completely excluded from the set of rows, and
#   3. *at most one* partition which is "cut off" partway through.
# Including the window function's ordering in the Limit's ordering does not
# matter for (1)- and (2)-style partitions (since the window function itself
# will re-sort them), but for the (3)-style partition, we need to ensure that
# the limit operator allows through a prefix of it, rather than an arbitrary
# subset.
# 
# Finally, we require that every window function+frame pair being computed has
# the "prefix-safe" property. A window function is prefix safe if it can be
# correctly computed over only a prefix of a partition. For example, rank() has
# this property because rows that come later in the ordering don't affect the
# rank of the rows before, but avg()+UNBOUNDED {PRECEDING,FOLLOWING} doesn't,
# because we must see the entire partition to compute the average over it.
#
# TODO(justin): Add a rule that translates a limit with an ordering on rank()
# or dense_rank() into one using the ordering of the window function. This will
# allow us to push down limits in cases like:
#
# SELECT rank() OVER (ORDER BY f) rnk FROM a ORDER BY rnk LIMIT 10
# =>
# SELECT rank() OVER (ORDER BY f) rnk FROM a ORDER BY f LIMIT 10
# =>
# SELECT rank() OVER (ORDER BY f) rnk FROM (SELECT * FROM a ORDER BY f LIMIT 10)
[PushLimitIntoWindow, Normalize]
(Limit
    (Window $input:* $fns:* & (AllArePrefixSafe $fns) $private:*)
    $limit:*
    $ordering:* &
        (OrderingCanProjectCols
            $ordering
            $inputCols:(OutputCols $input)
        ) &
        (Let
            ($newOrdering $ok):(MakeSegmentedOrdering
                $input
                (WindowPartition $private)
                (WindowOrdering $private)
                $ordering
            )
            $ok
        )
)
=>
(Window
    (Limit
        $input
        $limit
        (PruneOrdering
            (DerefOrderingChoice $newOrdering)
            $inputCols
        )
    )
    $fns
    $private
)
```
- Attempts used: 25
- Last updated: 2026-10-16T03:59:34.059172+00:00
- Reason / notes: PushLimitIntoWindow's soundness rests entirely on two things QED's model categorically lacks: the row-ordering semantics of Limit/Sort (taking the first N rows under the window's segmented partition+ordering, so the one "cut off" partition must pass through as a *prefix*), and the prefix-safety property of specific window functions (rank() yes, avg() with an unbounded frame no) — a bespoke internal semantics of the operator that QED cannot see through as an uninterpreted function. QED only reasons about bag-equality of operator inputs, so it cannot know that W∘Limit = Limit∘W holds for a "prefix-safe" W; even if the DSL were extended to build Sort/Limit/Window shapes (the JSON serializer already carries LogicalSort), the prover would have to derive an identity between two distinct nestings of uninterpreted ordering-dependent operators, which is not a bag-algebra theorem and would simply fail. This is a genuine fundamental limitation of the model, not a missing encoding trick, so the porter's UNSUPPORTED conclusion is correct. ```

### `PushOffsetIntoLock` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/limit.opt

PushOffsetIntoLock pushes the Offset operator into its Lock input, as long as
the Lock does not use a SKIP LOCKED wait policy. This helps minimize the
number of rows locked.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushOffsetIntoLock`, not the other rules in that file):

```
# PushOffsetIntoLock pushes the Offset operator into its Lock input, as long as
# the Lock does not use a SKIP LOCKED wait policy. This helps minimize the
# number of rows locked.
[PushOffsetIntoLock, Normalize]
(Offset
    (Lock $input:* $private:*) & ^(LockUsesSkipLocked $private)
    $offset:*
    $ordering:*
)
=>
(Lock (Offset $input $offset $ordering) $private)
```
- Attempts used: 26
- Last updated: 2026-10-16T04:15:41.109668+00:00
- Reason / notes: Offset's meaning — dropping the first k rows of a given ordering — is exactly the list/ordering semantics QED's bag-semantic decision procedure does not model, and that is a limitation of the (unmodifiable) prover, not a DSL gap that `extend_dsl_file` could close; the rule's soundness additionally rests on the backend-specific fact that a non-SKIP-LOCKED Lock passes every input row through, so modeling Lock honestly as an uninterpreted row-dropping bag function makes the commutation `Offset∘Lock = Lock∘Offset` refutable (suffix-of-filtered ≠ filter-of-suffix), while the only model under which it holds (row-identity) collapses the rule to the degenerate `Offset(x) = Offset(x)`. No non-degenerate, semantically honest encoding exists for QED to prove.

### `PushOffsetIntoProject` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/limit.opt

PushOffsetIntoProject pushes the Offset operator into its Project input. It is
desirable to push the Offset operator as low in the query as possible, in
order to minimize the number of rows that other operators need to process.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushOffsetIntoProject`, not the other rules in that file):

```
# PushOffsetIntoProject pushes the Offset operator into its Project input. It is
# desirable to push the Offset operator as low in the query as possible, in
# order to minimize the number of rows that other operators need to process.
[PushOffsetIntoProject, Normalize]
(Offset
    (Project $input:* $projections:* $passthrough:*)
    $offset:*
    $ordering:* &
        (OrderingCanProjectCols
            $ordering
            $cols:(OutputCols $input)
        )
)
=>
(Project
    (Offset $input $offset (PruneOrdering $ordering $cols))
    $projections
    $passthrough
)
```
- Attempts used: 8
- Last updated: 2026-10-16T04:06:59.276390+00:00
- Reason / notes: The rule's soundness rests entirely on row order — Offset means "skip the first k rows in $ordering", and the OrderingCanProjectCols/PruneOrdering side conditions exist precisely to guarantee the same rows are kept on both sides — while QED decides equivalence purely under bag semantics with no interpretation for Sort/Limit/Offset (qed.pdf §6.2), operators the DSL also does not expose. No non-vacuous encoding rescues it: an uninterpreted offset symbol carries no order-preservation facts the prover could use to commute it past a projection, and any concrete offset model buildable from the core language (e.g., a count-based filter over a key column) is only valid for identity/monotone projections that the DSL cannot even state, leaving only the vacuous degenerate. (The porter's specific "non-injective projection merges rows" argument was actually wrong — a pointwise projection does commute with tail-skipping as multisets — but the conclusion is correct for the ordering-semantics reason above.)

### `ReduceGroupingCols` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/groupby.opt

ReduceGroupingCols eliminates redundant grouping columns from the GroupBy
operator and replaces them by ConstAgg aggregate functions. A grouping
column is redundant if it is functionally determined by the other grouping
columns. If that's true, then its value must be constant within a group.
Therefore, it has no effect on the grouping and can instead be represented as
an ConstAgg aggregate, since all rows in the group have the same value for
that column.

Note: Doesn't match EnsureDistinctOn because test cases were too difficult to
find. If a test case for EnsureDistinctOn is found, it should be added to the
match pattern.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `ReduceGroupingCols`, not the other rules in that file):

```
# ReduceGroupingCols eliminates redundant grouping columns from the GroupBy
# operator and replaces them by ConstAgg aggregate functions. A grouping
# column is redundant if it is functionally determined by the other grouping
# columns. If that's true, then its value must be constant within a group.
# Therefore, it has no effect on the grouping and can instead be represented as
# an ConstAgg aggregate, since all rows in the group have the same value for
# that column.
#
# Note: Doesn't match EnsureDistinctOn because test cases were too difficult to
# find. If a test case for EnsureDistinctOn is found, it should be added to the
# match pattern.
[ReduceGroupingCols, Normalize]
(GroupBy | DistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* &
        ^(ColsAreEmpty
            $redundantCols:(RedundantCols
                $input
                (GroupingCols $groupingPrivate)
            )
        )
)
=>
((OpName)
    $input
    (AppendAggCols $aggregations ConstAgg $redundantCols)
    (RemoveGroupingCols $groupingPrivate $redundantCols)
)
```
- Attempts used: 28
- Last updated: 2026-10-16T04:26:14.441919+00:00
- Reason / notes: ReduceGroupingCols is sound only because of a specific algebraic identity — ConstAgg/ANY_VALUE(r) over a group returns the group's (constant) value of the redundant column r — and QED models every aggregate as an uninterpreted bag-to-scalar operator with no axioms (it can only relate aggregate calls when their input bags are provably equal, qed.pdf §6.2). Here the rewrite produces an uninterpreted f(bag) on the after-side versus the raw column value on the before-side; no SMT decision procedure can equate those for uninterpreted f, and the FD/key precondition (even if fully expressible, which would require a DSL extension for multi-column key constraints) cannot supply the missing axiom, since that gap is inside the trusted prover, not the DSL. Consequently every instance of this rewrite, including any key-based special case, requires the same unprovable identity, so no narrower provable special case exists and the UNSUPPORTED verdict stands. ```

### `RejectNullsGroupBy` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/reject_nulls.opt
- Attempts used: 0
- Last updated: 2026-10-16T07:27:23.185173+00:00
- Reason / notes: The rewrite adds a col IS NOT NULL filter below a GroupBy/ScalarGroupBy because the aggregate ignores NULL inputs and the filter above rejects a NULL aggregate result. That equivalence rests on the aggregate skipping NULLs (and returning NULL on an all-NULL group). QED models null-skipping only for an aggregate call with ignoreNulls=true, and the DSL cannot set that flag: RelRN.AggCall has no ignoreNulls field and Aggregate.semantics() builds calls through the plain RelBuilder overload. An encoding with a generic aggregate over (key, value) under a null-rejecting filter, pushed filter versus unpushed, is not provable, since an uninterpreted aggregate of a bag has no relation to the same aggregate of a sub-bag.

### `SimplifyAnyScalarArray` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifyAnyScalarArray converts a scalar ANY operation on a constant ARRAY to a scalar
ANY operation on a tuple. In particular, this allows SimplifyEqualsAnyTuple to be
triggered, which allows constraints to be generated.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyAnyScalarArray`, not the other rules in that file):

```
# SimplifyAnyScalarArray converts a scalar ANY operation on a constant ARRAY to a scalar
# ANY operation on a tuple. In particular, this allows SimplifyEqualsAnyTuple to be
# triggered, which allows constraints to be generated.
[SimplifyAnyScalarArray, Normalize]
(AnyScalar $input:* $ary:(Const) & (IsConstArray $ary) $cmp:*)
=>
(AnyScalar $input (ConvertConstArrayToTuple $ary) $cmp)
```
- Attempts used: 16
- Last updated: 2026-10-16T05:32:04.069580+00:00
- Reason / notes: The rule is a scalar representation normalization whose soundness rests on a backend-specific collection semantic — that a constant array and the element-wise constant tuple built from it denote interchangeable collections for an element-wise ANY comparison — and QED's Q-expression format has no array/tuple *value* type (only scalar literals, column refs, and uninterpreted calls) and no axiom linking a const array to its tuple conversion. The DSL's `Any`/`ArrayConst`/`TupleConst` all desugar to the identical Or-of-element-comparisons, so before and after collapse to the same Q-expression (a vacuous P=P that checks nothing about the transformation); keeping the two constants as distinct terms instead yields two unrelated uninterpreted constants the SMT solver cannot equate. No `extend_dsl_file` can close this because the Rust prover's Q format is fixed and cannot be given array/tuple value semantics, so the rule is genuinely outside what RuleScript+QED can prove non-vacuously. ```

### `SimplifyCaseSingleTruthyBranch` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/select.opt

SimplifyCaseSingleTruthyBranch replaces a CASE expression in a filter with its
condition when it has a single WHEN branch that evaluates to True, and an ELSE
branch that evaluates to False or Null.

For example:

SELECT * FROM abc WHERE CASE WHEN a > 5 THEN true ELSE false END
=>
SELECT * FROM abc WHERE a > 5

Extracted from `select.opt` (which defines multiple rules — implement specifically `SimplifyCaseSingleTruthyBranch`, not the other rules in that file):

```
# SimplifyCaseSingleTruthyBranch replaces a CASE expression in a filter with its
# condition when it has a single WHEN branch that evaluates to True, and an ELSE
# branch that evaluates to False or Null.
#
# For example:
#
#  SELECT * FROM abc WHERE CASE WHEN a > 5 THEN true ELSE false END
#  =>
#  SELECT * FROM abc WHERE a > 5
#
[SimplifyCaseSingleTruthyBranch, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Case
                (True)
                $whens:[ (When $cond:* (True)) ]
                (False | Null)
            )
        )
        ...
    ]
)
=>
(Select $input (ReplaceFiltersItem $filters $item $cond))
```
- Attempts used: 45
- Last updated: 2026-10-16T07:27:11.747617+00:00
- Reason / notes: The rule's soundness rests entirely on the CASE operator's branch-selection/3-valued semantics (`CASE WHEN c THEN true ELSE false/NULL END` retains a row iff `c` is true), but QED's interpreted scalar fragment only models the connectives and literals (AND/OR/NOT/TRUE/FALSE) — a CASE call, which every instance of this rule necessarily carries on the before side, down to the narrowest special cases, serializes via the `Rex` visitor as a name-keyed call that the fixed Rust prover treats as an uninterpreted function, so the before predicate has no modeled relation to `cond` and no universal SMT proof exists. No `extend_dsl_file` change on the Java side can close the gap, since operator interpretation is keyed by name inside the immutable prover, and this matches the AGREE-verified SKIPPED precedents for the same rule family (datafusion `CaseFirstBranchAlwaysTrue`, cockroach `EliminateCaseTrailingFalsyBranch`). ```

### `SimplifyCaseWhenConstValue` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifyCaseWhenConstValue removes branches known to not match. Any
branch known to match is used as the ELSE and further WHEN conditions
are skipped. If all WHEN conditions have been removed, the ELSE
expression is used.
This transforms

CASE WHEN v THEN 1 WHEN false THEN a WHEN true THEN b ELSE c END

to

CASE WHEN v THEN 1 ELSE b END

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyCaseWhenConstValue`, not the other rules in that file):

```
# SimplifyCaseWhenConstValue removes branches known to not match. Any
# branch known to match is used as the ELSE and further WHEN conditions
# are skipped. If all WHEN conditions have been removed, the ELSE
# expression is used.
# This transforms
#
#   CASE WHEN v THEN 1 WHEN false THEN a WHEN true THEN b ELSE c END
#
# to
#
#   CASE WHEN v THEN 1 ELSE b END
#
[SimplifyCaseWhenConstValue, Normalize]
(Case
    $condition:(ConstValue)
    $whens:[ ... (When (ConstValue)) ... ]
    $orElse:*
)
=>
(SimplifyWhens $condition $whens $orElse)
```
- Attempts used: 22
- Last updated: 2026-10-16T07:41:34.528622+00:00
- Reason / notes: The rule's soundness rests entirely on CASE's internal evaluation semantics (ordered first-true-branch selection with SQL NULL-skip), which is exactly the class of idiosyncratic operator semantics QED cannot see through: a CASE node serializes as a name-keyed uninterpreted function in the JSON, and interpretation of names happens inside the fixed Rust prover, so no `extend_dsl_file` change on the Java side can supply it. There is also no desugaring workaround — CASE cannot be rewritten in the interpreted AND/OR/NOT vocabulary, since under SQL three-valued logic `CASE WHEN NULL THEN t ELSE e END` yields `e` while `(NULL AND t) OR (NOT NULL AND e)` yields `NULL` — so even the narrowest instance (`CASE WHEN true THEN a ELSE b END` ⟹ `a`) would demand the prover interpret `Case(true,a,b)=a`. And because every matching instance of the rule strictly changes the CASE's structure (branch dropped or collapsed into ELSE), before and after are always structurally distinct uninterpreted terms, so no special case is provable. ```

### `SimplifyCoalesce` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifyCoalesce discards any leading null operands, and then if the next
operand is a constant, replaces with that constant. Note that ConstValue
matches nulls as well as other constants.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyCoalesce`, not the other rules in that file):

```
# SimplifyCoalesce discards any leading null operands, and then if the next
# operand is a constant, replaces with that constant. Note that ConstValue
# matches nulls as well as other constants.
[SimplifyCoalesce, Normalize]
(Coalesce
    $args:[
        $arg:* & (IsConstValueOrGroupOfConstValues $arg)
        ...
    ]
)
=>
(SimplifyCoalesce $args)
```
- Attempts used: 41
- Last updated: 2026-10-16T07:46:54.906930+00:00
- Reason / notes: QED encodes COALESCE as an uninterpreted scalar function with no first-non-null selection axiom, so the rule's essential step COALESCE(NULL, x) → x is a bespoke operator-semantic identity that the SMT layer cannot justify for any instantiation of the symbols. The DSL also has no NULL literal, so the "leading null operands" premise cannot even be expressed, and closing that with `extend_dsl_file` would not help because the missing axiom lives in the fixed prover (which cannot be modified), not in the Java API — and any relational re-encoding of "first non-null" would require interpreting the very uninterpreted symbols QED abstracts away. This falls squarely under the limitation of operators whose specific internal semantics QED cannot see through, so the porter's UNSUPPORTED conclusion is correct.

### `SimplifyExplainOrdering` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyExplainOrdering removes redundant columns from the Explain operator's
input ordering.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyExplainOrdering`, not the other rules in that file):

```
# SimplifyExplainOrdering removes redundant columns from the Explain operator's
# input ordering.
[SimplifyExplainOrdering, Normalize]
(Explain
    $input:*
    $explainPrivate:* &
        (CanSimplifyExplainOrdering $input $explainPrivate)
)
=>
(Explain $input (SimplifyExplainOrdering $input $explainPrivate))
```
- Attempts used: 25
- Last updated: 2026-10-16T08:14:21.648199+00:00
- Reason / notes: SimplifyExplainOrdering keeps `$input` identical on both sides and only rewrites the ordering directive stored in the Explain operator's private props, but RuleScript has no channel to express ordering directives (no Sort/annotation carrier in the core language) and QED decides equivalence on bag semantics only, so every possible encoding degenerates to the data identity input ≡ input — provable, yet vacuous, since the proof never touches the rule's actual content. That content is a functional-dependency argument about ordered sequences (a key column making later ordering columns redundant), i.e. list/ordering semantics, which is explicitly outside QED's decision procedure; even a hypothetical Sort builder couldn't close the gap, because QED would collapse any two orderings of the same bag to one another and "prove" both correct and incorrect simplifications, so this is a fundamental prover limitation rather than a missing DSL operator.

### `SimplifyGroupByOrdering` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyGroupByOrdering removes redundant columns from the GroupBy operators'
input ordering.

Note: Doesn't match EnsureDistinctOn because test cases were too difficult to
find. If a test case for EnsureDistinctOn is found, it should be added to the
match pattern.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyGroupByOrdering`, not the other rules in that file):

```
# SimplifyGroupByOrdering removes redundant columns from the GroupBy operators'
# input ordering.
#
# Note: Doesn't match EnsureDistinctOn because test cases were too difficult to
# find. If a test case for EnsureDistinctOn is found, it should be added to the
# match pattern.
[SimplifyGroupByOrdering, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* &
        (CanSimplifyGroupingOrdering $input $groupingPrivate)
)
=>
((OpName)
    $input
    $aggregations
    (SimplifyGroupingOrdering $input $groupingPrivate)
)
```
- Attempts used: 9
- Last updated: 2026-10-16T08:06:11.777597+00:00
- Reason / notes: QED is a bag-semantics decision procedure with no model of ordering/order-by — Sort/Limit/Offset/Order-By carry no bag meaning (qed.pdf §6.2 "List semantics"; reference Limitations) — yet SimplifyGroupByOrdering's entire content is simplifying the physical OrderingChoice attached to GroupBy/ScalarGroupBy/DistinctOn by dropping ordering columns made redundant by the input's functional-dependency set, which changes only the ordering directive, not the produced rows, so before and after serialize to the identical bag. QED's only FD modeling (unique-key scans) cannot help, because those FDs here drive ordering-column removal rather than any change in row content, so no non-trivial bag-level equivalence exists and any RuleScript encoding degenerates to a vacuous `GroupBy ≡ GroupBy` identity — genuinely outside what the prover can check, not a missed encoding. ```

### `SimplifyLimitOrdering` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyLimitOrdering removes redundant columns from the Limit operator's
input ordering.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyLimitOrdering`, not the other rules in that file):

```
# SimplifyLimitOrdering removes redundant columns from the Limit operator's
# input ordering.
[SimplifyLimitOrdering, Normalize]
(Limit
    $input:*
    $limit:*
    $ordering:* &
        (CanSimplifyLimitOffsetOrdering $input $ordering)
)
=>
(Limit
    $input
    $limit
    (SimplifyLimitOffsetOrdering $input $ordering)
)
```
- Attempts used: 10
- Last updated: 2026-10-16T08:14:37.875768+00:00
- Reason / notes: QED's theory is bag-only with uninterpreted operators and has no list/ordering semantics, so a Limit's ordering directive has no representation the prover can reason about: keeping the ordering makes the two sides distinct uninterpreted applications (unprovable), while erasing it reduces the rule to a vacuous identity, since the rows' multiset is untouched by the rewrite. The rule's entire correctness content — that dropping ordering columns functionally determined by earlier ones preserves the row sequence Limit/Offset select on — lives in exactly that unmodeled list semantics, and no `extend_dsl_file` builder can close the gap because the Rust prover itself (which must not be modified) lacks it; the JSON format already carrying `sort`/`limit` fields confirms this is a prover-theory limitation, not a missing DSL feature.

### `SimplifyNotDisjoint` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifyNotDisjoint converts !st_disjoint to st_intersects so that
the query can be index-accelerated if a suitable inverted index exists.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyNotDisjoint`, not the other rules in that file):

```
# SimplifyNotDisjoint converts !st_disjoint to st_intersects so that
# the query can be index-accelerated if a suitable inverted index exists.
[SimplifyNotDisjoint, Normalize]
(Not (Function $args:* $private:(FunctionPrivate "st_disjoint")))
=>
(MakeIntersectionFunction $args)
```
- Attempts used: 26
- Last updated: 2026-10-16T08:35:49.856032+00:00
- Reason / notes: The rule is purely scalar, and its entire validity rests on Cockroach's library identity st_intersects(args) ≡ ¬st_disjoint(args); in QED's model these are two independent uninterpreted symbols, so the faithful encoding (a Filter over a shared scan, which the porter compiled and ran to completion with no timeout) admits a genuine SMT countermodel, making provable=false the correct verdict rather than a symbol-naming or shape bug. The only route to a proof — postulating that identity as a table "guaranteed" constraint — would import the rule's own premise as an axiom, turning the verification circular, which is precisely the "bespoke internal semantics of a backend operator" limitation QED fundamentally cannot model, not a missing DSL capability worth extending.

### `SimplifyOffsetOrdering` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyOffsetOrdering removes redundant columns from the Offset operator's
input ordering.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyOffsetOrdering`, not the other rules in that file):

```
# SimplifyOffsetOrdering removes redundant columns from the Offset operator's
# input ordering.
[SimplifyOffsetOrdering, Normalize]
(Offset
    $input:*
    $offset:*
    $ordering:* &
        (CanSimplifyLimitOffsetOrdering $input $ordering)
)
=>
(Offset
    $input
    $offset
    (SimplifyLimitOffsetOrdering $input $ordering)
)
```
- Attempts used: 7
- Last updated: 2026-10-16T08:20:42.472663+00:00
- Reason / notes: This rule rewrites the ordering directive of an Offset node, and Offset's very meaning — which rows survive after skipping the first k — is a function of row order, so the rule's entire correctness content sits in exactly the semantics QED excludes: qed.pdf §6.2 states QED uses bag semantics and "does not support queries that make use of the ordering semantics of Limit, Offset, or Order By" (Table 3's "List semantic" category accounts for 103 CockroachDB failures). The core language has no Sort/Limit/Offset builder to express even the shape, and an extend_dsl_file addition could not close the gap because the unmodifiable prover has no ordering theory to decide equality of the offset-selected sequence over. No non-trivial bag-semantic special case remains: even under a unique leading key, verifying that the sub-sequence after skipping k rows is identical under orderings (a,b) vs (a) is pure list reasoning QED cannot perform. ```

### `SimplifyOrdinalityOrdering` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyOrdinalityOrdering removes redundant columns from the Ordinality
operator's input ordering.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyOrdinalityOrdering`, not the other rules in that file):

```
# SimplifyOrdinalityOrdering removes redundant columns from the Ordinality
# operator's input ordering.
[SimplifyOrdinalityOrdering, Normalize]
(Ordinality
    $input:*
    $ordinalityPrivate:* &
        (CanSimplifyOrdinalityOrdering $input $ordinalityPrivate)
)
=>
(Ordinality
    $input
    (SimplifyOrdinalityOrdering $input $ordinalityPrivate)
)
```
- Attempts used: 25
- Last updated: 2026-10-16T08:42:37.227972+00:00
- Reason / notes: The rewrite's entire effect is changing the physical ordering requirement attached to the Ordinality operator — the input relation and its bag are identical on both sides — so in any RuleScript encoding, before() and after() are literally the same relational expression, and the only proof QED could return would be a vacuous identity that says nothing about the actual simplification. The rule's real soundness rests on row-order semantics (a key column makes subsequent sort columns redundant in a total row order) and functional-dependence entailment, both of which QED fundamentally cannot model (Sort/Limit/Offset/Window/Sample have no bag-semantic meaning, and it cannot reason about FD/entailment between uninterpreted symbols); RelRN also exposes no Ordinality or ordering-requirement construct, and an extend_dsl_file cannot close the gap because the prover — the unchanging arbiter — erases ordering rather than the DSL missing a builder. ```

### `SimplifyPartialIndexProjections` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/mutation.opt

SimplifyPartialIndexProjections converts partial index PUT and DEL projected
expressions to false when it is guaranteed that the mutation will not require
changes to the associated partial index. These projected expressions can only
be simplified to false when an UPDATE mutates neither the associated index's
columns nor the columns referenced in the partial index predicate.

Extracted from `mutation.opt` (which defines multiple rules — implement specifically `SimplifyPartialIndexProjections`, not the other rules in that file):

```
# SimplifyPartialIndexProjections converts partial index PUT and DEL projected
# expressions to false when it is guaranteed that the mutation will not require
# changes to the associated partial index. These projected expressions can only
# be simplified to false when an UPDATE mutates neither the associated index's
# columns nor the columns referenced in the partial index predicate.
[SimplifyPartialIndexProjections, Normalize]
(Update
    $project:(Project $input:* $projections:* $passthrough:*)
    $uniqueChecks:*
    $fkChecks:*
    $mutationPrivate:* &
        ^(ColsAreEmpty
            $simplifiableCols:(SimplifiablePartialIndexProjectCols
                $mutationPrivate
                $uniqueChecks
                $fkChecks
                $projections
            )
        )
)
=>
(Update
    (Project
        $input
        (Let
            (
                $simplifiedProjections
                $simplifiedPrivate
            ):(SimplifyPartialIndexProjections
                $projections
                $passthrough
                $simplifiableCols
                $mutationPrivate
            )
            $simplifiedProjections
        )
        $passthrough
    )
    $uniqueChecks
    $fkChecks
    $simplifiedPrivate
)
```
- Attempts used: 84
- Last updated: 2026-10-16T09:48:17.388296+00:00
- Reason / notes: The rewrite is conditional on a static column-set side condition (the UPDATE writes none of the partial index's or predicate's columns), which RuleScript cannot express — QED decides bag equality over all instantiations of the uninterpreted symbols with no mechanism for side conditions — and with that guard dropped the rewrite is not bag-equivalent, since rows satisfying the partial predicate project (true,true) before but (false,false) after. The two outputs are interchangeable only inside CockroachDB's Mutation operator, whose put-XOR-del change detection treats pointwise-equal put/del as a no-op: that is a backend operator's bespoke internal semantics, and the DSL has no Update/Mutation operator QED could reason about — and any `extend_dsl_file` operator carrying that put/del-no-op property would axiomatize the very claim to be proved, so no faithful, non-vacuous encoding exists. ```

### `SimplifyWindowOrdering` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/window.opt

SimplifyWindowOrdering reduces an ordering to a simpler form using FDs.

This rules does not match when window functions have a RANGE frame with an
offset, like max(a) OVER (PARTITION BY a ORDER BY a RANGE 1 PRECEDING). The
ordering column cannot be pruned because the execution engine requires an
ordering column in this case, even if the ordering is constant.

Extracted from `window.opt` (which defines multiple rules — implement specifically `SimplifyWindowOrdering`, not the other rules in that file):

```
# SimplifyWindowOrdering reduces an ordering to a simpler form using FDs.
#
# This rules does not match when window functions have a RANGE frame with an
# offset, like max(a) OVER (PARTITION BY a ORDER BY a RANGE 1 PRECEDING). The
# ordering column cannot be pruned because the execution engine requires an
# ordering column in this case, even if the ordering is constant.
[SimplifyWindowOrdering, Normalize]
(Window
    $input:*
    $fn:*
    $private:* &
        (CanSimplifyWindowOrdering $input $private) &
        ^(HasRangeFrameWithOffset $fn)
)
=>
(Window $input $fn (SimplifyWindowOrdering $input $private))
```
- Attempts used: 24
- Last updated: 2026-10-16T09:00:24.960477+00:00
- Reason / notes: The rule's sole change is the window's ordering/private data, and its entire justification is that the simplified ordering induces the same intra-partition row order, hence the same frame boundaries and window-function values — a purely ordering-dependent fact, while QED's bag semantics deliberately model no list/order meaning for Sort/Limit/Window (per qed.pdf's own evaluation), and the trusted Rust prover cannot be modified to add such semantics (the DSL has no Window builder and the JSON format has no window form to feed it). Any non-vacuous encoding would have to show W(I,F,p) ≡ W(I,F,p') for two *distinct* orderings p and p': either assume that equivalence (proving only the trivial identity of identical terms), or leave both orderings as distinct uninterpreted values with no ordering/FD axioms QED can use (unprovable), and even expanding a single window function like rank() into self-join/aggregates over an order predicate would still require predicate-entailment reasoning between order relations that QED explicitly cannot perform. So the porter's UNSUPPORTED conclusion is sound — this is a fundamental QED limitation, not a missing builder to close with extend_dsl_file. ```

### `SimplifyWithBindingOrdering` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyWithBindingOrdering removes redundant columns from the With operator's
binding ordering. Note that this ordering is only used with the special
propagate_input_ordering flag.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyWithBindingOrdering`, not the other rules in that file):

```
# SimplifyWithBindingOrdering removes redundant columns from the With operator's
# binding ordering. Note that this ordering is only used with the special
# propagate_input_ordering flag.
[SimplifyWithBindingOrdering, Normalize]
(With
    $binding:*
    $main:*
    $withPrivate:* &
        (CanSimplifyWithBindingOrdering $binding $withPrivate)
)
=>
(With
    $binding
    $main
    (SimplifyWithBindingOrdering $binding $withPrivate)
)
```
- Attempts used: 10
- Last updated: 2026-10-16T09:09:31.261515+00:00
- Reason / notes: The rule's only effect is on `WithPrivate.BindingOrdering` — an ordering *requirement* (a physical property) on the CTE binding input — while the emitted row bag is the main query on both sides, so its real content (the functional-dependency claim that trailing sort columns are redundant once a leading column is unique) is a list/ordering-level property that QED's bag-semantics theory has no notion of (Sort/Limit/Offset carry no bag meaning, and no physical-property field exists in the QED JSON format). Any RuleScript encoding therefore degenerates to the vacuous identity With ≡ main, proving nothing about the rule; this is a fundamental prover-theory limitation, not a missing DSL builder, so extending the DSL cannot close it. ```

### `TryAddLimitToRecursiveBranch` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/with.opt

TryAddLimitToRecursiveBranch attempts to infer a limit that applies to the
(unbounded) recursive branch of the CTE. This is accomplished by taking the
upper bound of the initial branch, and checking whether applying that limit
to the recursive WithScans would allow the same bound to be inferred for the
recursive branch. This is useful because it can allow
ApplyLimitToRecursiveCTEScan to fire. See the CanAddRecursiveLimit comment
for details on when it is possible to make this transformation.

The added limit doesn't need an ordering because it will never actually limit
the number of rows returned.

Extracted from `with.opt` (which defines multiple rules — implement specifically `TryAddLimitToRecursiveBranch`, not the other rules in that file):

```
# TryAddLimitToRecursiveBranch attempts to infer a limit that applies to the
# (unbounded) recursive branch of the CTE. This is accomplished by taking the
# upper bound of the initial branch, and checking whether applying that limit
# to the recursive WithScans would allow the same bound to be inferred for the
# recursive branch. This is useful because it can allow
# ApplyLimitToRecursiveCTEScan to fire. See the CanAddRecursiveLimit comment
# for details on when it is possible to make this transformation.
#
# The added limit doesn't need an ordering because it will never actually limit
# the number of rows returned.
[TryAddLimitToRecursiveBranch, Normalize]
(RecursiveCTE
    $binding:* & ^(HasBoundedCardinality $binding)
    $initial:* & (HasBoundedCardinality $initial)
    $recursive:* & ^(HasBoundedCardinality $recursive)
    $private:* &
        (CanAddRecursiveLimit
            $recursive
            (GetRecursiveWithID $private)
            (MakeEmptyColSet)
        )
)
=>
(RecursiveCTE
    $binding
    $initial
    (Limit
        $recursive
        (GetLimitFromCardinality $initial)
        (EmptyOrdering)
    )
    $private
)
```
- Attempts used: 10
- Last updated: 2026-10-16T09:31:07.930872+00:00
- Reason / notes: The rule's before-side `RecursiveCTE` is a least fixed point over a working table — no finite bag-semiring expression and no core-language operator can represent it, and no `extend_dsl_file` builder could help since QED itself (the unmodifiable arbiter) has no fixed-point semantics to check it against. Its side conditions (`HasBoundedCardinality`, `CanAddRecursiveLimit`) are row-count bounds on whole relations, which the DSL cannot express — uninterpreted symbols are only row-level predicates/projections/functions, with no cardinality-constraint mechanism. And the introduced `Limit` has no bag semantics in QED: its safety as a no-op rests entirely on the inexpressible "never exceeds N rows" row-count argument, so any encodable before/after pair would either omit the limit/fixed point (collapsing to a vacuous identity) or be unencodable — the porter's UNSUPPORTED conclusion is correct.

### `TryDecorrelateLimit` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateLimit "pushes down" a Join into a Limit operator with a limit
greater than one, in an attempt to keep "digging" down to find and eliminate
unnecessary correlation. The eventual hope is to trigger the DecorrelateJoin
rule to turn a JoinApply operator into a non-apply Join operator.

The limit is replaced with a row_number window function on the right input and
a filter on top of the apply-join that removes all rows for which row_number()
is less than the limit.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateLimit`, not the other rules in that file):

```
# TryDecorrelateLimit "pushes down" a Join into a Limit operator with a limit
# greater than one, in an attempt to keep "digging" down to find and eliminate
# unnecessary correlation. The eventual hope is to trigger the DecorrelateJoin
# rule to turn a JoinApply operator into a non-apply Join operator.
#
# The limit is replaced with a row_number window function on the right input and
# a filter on top of the apply-join that removes all rows for which row_number()
# is less than the limit.
[TryDecorrelateLimit, Normalize]
(InnerJoin | InnerJoinApply
    $left:*
    $right:(Limit $input:* (Const $limit:*) $ordering:*) &
        (HasOuterCols $right) &
        (IsGreaterThan $limit (DInt 1))
    $on:*
    $private:*
)
=>
(Project
    # Needed to project away the rowNum column.
    (Select
        ((OpName)
            $left
            (Window
                $input
                (Let
                    ($rowNum $rowNumCol):(MakeRowNumberWindowFunc
                    )
                    $rowNum
                )
                (MakeWindowPrivate (MakeEmptyColSet) $ordering)
            )
            $on
            $private
        )
        (LimitToRowNumberFilter $limit $rowNumCol)
    )
    []
    (OutputCols2 $left $right)
)
```
- Attempts used: 25
- Last updated: 2026-10-16T09:57:26.068599+00:00
- Reason / notes: TryDecorrelateLimit's entire correctness claim is the order-based equivalence "first n rows of the right input per the given ordering (LIMIT)" ≡ "rows of the right input with ROW_NUMBER() (computed in that ordering) ≤ n", and QED only decides bag-semantic equivalence — it has no model for the ordering semantics of Sort/Limit/Offset/Window at all (a documented limitation, confirmed by the "List semantic" failure category in QED's own Calcite/CockroachDB evaluation in qed.pdf). No non-vacuous special case rescues it: without ordering, "the first n rows" (even for n=1) is undefined over a bag, so the best available encodings either model LIMIT and ROW_NUMBER() as independent uninterpreted functions (identity then unprovable) or collapse them into one symbol (tautological, proving nothing about the actual operators). The gap sits in the unmodifiable QED prover itself rather than in a missing DSL builder, so extending RelRN/RexRN could not make this rule decidable. ```

### `TryDecorrelateLimitOne` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateLimitOne "pushes down" a Join into a Limit 1 operator, in an
attempt to keep "digging" down to find and eliminate unnecessary correlation.
The eventual hope is to trigger the DecorrelateJoin rule to turn a JoinApply
operator into a non-apply Join operator.

Like the TryDecorrelateGroupBy and TryDecorrelateScalarGroupBy rules, this
rule rewrites the expression to perform the join first, followed by a grouping
that eliminates any extra rows introduced by the join. The DistinctOn operator
uses First aggregates to select values from the first row in each group. Non-
key columns from the left join input become Const aggregates, since they are
functionally dependent on the grouped key columns (and are therefore constant
in each group).

TODO(andyk): Add other join types.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateLimitOne`, not the other rules in that file):

```
# TryDecorrelateLimitOne "pushes down" a Join into a Limit 1 operator, in an
# attempt to keep "digging" down to find and eliminate unnecessary correlation.
# The eventual hope is to trigger the DecorrelateJoin rule to turn a JoinApply
# operator into a non-apply Join operator.
#
# Like the TryDecorrelateGroupBy and TryDecorrelateScalarGroupBy rules, this
# rule rewrites the expression to perform the join first, followed by a grouping
# that eliminates any extra rows introduced by the join. The DistinctOn operator
# uses First aggregates to select values from the first row in each group. Non-
# key columns from the left join input become Const aggregates, since they are
# functionally dependent on the grouped key columns (and are therefore constant
# in each group).
#
# TODO(andyk): Add other join types.
[TryDecorrelateLimitOne, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (Limit $input:* (Const 1) $ordering:*)
    $on:*
    $private:*
)
=>
(Project
    # Needed to project away any columns added by EnsureKey.
    (DistinctOn
        ((OpName) $newLeft:(EnsureKey $left) $input $on $private)
        (MakeAggCols2
            ConstAgg
            (NonKeyCols $newLeft)
            FirstAgg
            (OutputCols $input)
        )
        (MakeGrouping (KeyCols $newLeft) $ordering)
    )
    []
    (OutputCols2 $left $right)
)
```
- Attempts used: 45
- Last updated: 2026-10-16T10:28:49.661619+00:00
- Reason / notes: TryDecorrelateLimitOne's correctness is precisely the identity "top-1 row of each left row's matches under collation O" (Limit 1 + ordering) = "first row of each group under the same collation O" (DistinctOn + FirstAgg), and both sides are order-dependent selections with no bag-semantic meaning — QED decides equivalence only on bag semantics with uninterpreted bag functions, and per the reference, Sort/Limit/Offset/Window have no semantics there (a Java-side builder added via extend_dsl_file could not change the unmodifiable Rust prover). "Pick one row from a group" is not a function of the group's bag, so FirstAgg/ConstAgg cannot be modeled as uninterpreted aggregates (QED knows no aggregate algebra), and any encoding is either unprovable (Limit and DistinctOn as independent uninterpreted symbols) or tautological (collapsing both sides into one shared symbol); there is no order-independent special case that is both valid and expressible, since even the unordered "arbitrary row" variant is not a deterministic bag operation. ```

### `TryDecorrelateMax1Row` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateMax1Row "pushes down" a Join into a Max1Row operator, in an
attempt to keep "digging" down to find and eliminate unnecessary correlation.
The eventual hope is to trigger the DecorrelateJoin rule to turn a JoinApply
operator into a non-apply Join operator.

The Max1Row operator is mapped into an EnsureDistinctOn operator that wraps
the join and raises an error if it detects duplicates in the column(s) that
made up the key of the join's left input. A duplicate value in those key
column(s) indicates that more than one row from the right input matched that
value. Or in other words, it indicates that the Max1Row's subquery input would
have returned more than one row corresponding to that value. Therefore, the
two formulations are equivalent.

TryDecorrelateMax1Row only matches when the join's "on" condition is true.
This is because pushing a non-true filter through the EnsureDistinctOn would
result in different error behavior. Since there are currently no situations
where the join's "on" condition is anything other than true, and since these
cases therefore cannot be tested, TryDecorrelateMax1Row only matches when the
"on" condition is true. If this changes, TryDecorrelateMax1Row should hoist
the non-true "on" conditions above the EnsureDistinctOn operator.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateMax1Row`, not the other rules in that file):

```
# TryDecorrelateMax1Row "pushes down" a Join into a Max1Row operator, in an
# attempt to keep "digging" down to find and eliminate unnecessary correlation.
# The eventual hope is to trigger the DecorrelateJoin rule to turn a JoinApply
# operator into a non-apply Join operator.
#
# The Max1Row operator is mapped into an EnsureDistinctOn operator that wraps
# the join and raises an error if it detects duplicates in the column(s) that
# made up the key of the join's left input. A duplicate value in those key
# column(s) indicates that more than one row from the right input matched that
# value. Or in other words, it indicates that the Max1Row's subquery input would
# have returned more than one row corresponding to that value. Therefore, the
# two formulations are equivalent.
#
# TryDecorrelateMax1Row only matches when the join's "on" condition is true.
# This is because pushing a non-true filter through the EnsureDistinctOn would
# result in different error behavior. Since there are currently no situations
# where the join's "on" condition is anything other than true, and since these
# cases therefore cannot be tested, TryDecorrelateMax1Row only matches when the
# "on" condition is true. If this changes, TryDecorrelateMax1Row should hoist
# the non-true "on" conditions above the EnsureDistinctOn operator.
[TryDecorrelateMax1Row, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (Max1Row $input:* $errorText:*)
    []
    $private:*
)
=>
(Project
    (EnsureDistinctOn
        ((OpName) $newLeft:(EnsureKey $left) $input [] $private)
        (MakeAggCols
            ConstAgg
            (UnionCols (NonKeyCols $newLeft) (OutputCols $input))
        )
        (MakeErrorOnDupGrouping
            (KeyCols $newLeft)
            (EmptyOrdering)
            $errorText
        )
    )
    []
    (OutputCols2 $left $right)
)
```
- Attempts used: 22
- Last updated: 2026-10-16T10:45:00.480345+00:00
- Reason / notes: The rule's only non-vacuous content is the coincidence of error behavior — Max1Row aborts when a correlated subquery instance holds >1 row, while EnsureDistinctOn aborts when a left-key group holds >1 row — plus the ≤1-row-per-group invariant that makes both collapse to the same cross-join; QED's semiring model has no error semantics, no Max1Row/EnsureDistinctOn operators it can see through as uninterpreted symbols, and no way to assume the ≤1-row cardinality precondition since it decides unconditional universal bag equality. Hence any faithful encoding is refutable (QED instantiates the two distinct error operators differently), while the only provable encoding is the vacuous tautology Join(left,input) ≡ Project(Join(EnsureKey(left),input)) after erasing both operators to identity — a genuine QED limitation, not a missing DSL builder a Max1Row constructor could close.

### `TryDecorrelateScalarGroupBy` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt
- Attempts used: 0
- Last updated: 2026-10-16T20:07:07.793538+00:00
- Reason / notes: The rule pushes a join into a ScalarGroupBy by turning it into a GroupBy over a left join keyed on a strict key of the left input. It depends on the aggregates ignoring NULLs so the NULLs introduced by the left join are skipped, and it rewrites CountRows, ConstAgg and other non-ignoring aggregates through a canary column and CASE expressions. QED models null-skipping only for an aggregate call with ignoreNulls=true, and the DSL cannot set that flag, so the null-ignoring requirement the rule relies on cannot be stated. The rewrite also synthesizes a key with RowNumber and a canary column through Go helper functions (EnsureKey, EnsureCanary, TranslateNonIgnoreAggs), which have no counterpart in the DSL. An encoding with an uninterpreted aggregate would not capture any of those conditions.

### `TryDecorrelateUnion` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateUnion replaces a Union/UnionAll beneath a ScalarGroupBy with a
cross-join (InnerJoin on True) between two ScalarGroupBy operators. A Project
operator coalesces columns from each join input to produce the final result.
This transformation applies when the ScalarGroupBy has only "any-not-null"
aggregations, which select an arbitrary non-null value from the input column.

Here's a simplified example:

scalar-group-by
├── union-all
│    ├── scan foo
│    └── scan bar (has-outer-cols)
└── aggregations
└── any-not-null
=>
project
├── inner-join (cross)
│    ├── scalar-group-by
│    │    └── scan foo
│    ├── scalar-group-by
│    │    └── scan bar
│    └── filters (true)
└── projections
└── coalesce

This situation occurs after a correlated EXISTS subquery containing a Union is
hoisted. Note that TryDecorrelateUnion does not itself decorrelate the Union,
but makes it easier for other rules to do so.

NOTE: the outer Project operator is necessary just in case the ScalarGroupBy
is synthesizing new columns, despite using any-not-null aggregations.
NOTE: TryDecorrelateUnion should be ordered before TryDecorrelateScalarGroupBy
to ensure that Union operators have a chance to be decorrelated.

TODO(drewk): We could extend this rule to apply to other aggregations; for
example, for a count() we can sum the counts taken on each side of the join.
TODO(drewk): We could extend this rule to handle other set operations. For
example, ExceptAll could become an AntiJoin.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateUnion`, not the other rules in that file):

```
# TryDecorrelateUnion replaces a Union/UnionAll beneath a ScalarGroupBy with a
# cross-join (InnerJoin on True) between two ScalarGroupBy operators. A Project
# operator coalesces columns from each join input to produce the final result.
# This transformation applies when the ScalarGroupBy has only "any-not-null"
# aggregations, which select an arbitrary non-null value from the input column.
#
# Here's a simplified example:
#
#   scalar-group-by
#    ├── union-all
#    │    ├── scan foo
#    │    └── scan bar (has-outer-cols)
#    └── aggregations
#         └── any-not-null
#   =>
#   project
#    ├── inner-join (cross)
#    │    ├── scalar-group-by
#    │    │    └── scan foo
#    │    ├── scalar-group-by
#    │    │    └── scan bar
#    │    └── filters (true)
#    └── projections
#         └── coalesce
#
# This situation occurs after a correlated EXISTS subquery containing a Union is
# hoisted. Note that TryDecorrelateUnion does not itself decorrelate the Union,
# but makes it easier for other rules to do so.
#
# NOTE: the outer Project operator is necessary just in case the ScalarGroupBy
# is synthesizing new columns, despite using any-not-null aggregations.
# NOTE: TryDecorrelateUnion should be ordered before TryDecorrelateScalarGroupBy
# to ensure that Union operators have a chance to be decorrelated.
#
# TODO(drewk): We could extend this rule to apply to other aggregations; for
# example, for a count() we can sum the counts taken on each side of the join.
# TODO(drewk): We could extend this rule to handle other set operations. For
# example, ExceptAll could become an AntiJoin.
[TryDecorrelateUnion, Normalize]
(ScalarGroupBy
    $input:(Union | UnionAll $left:* $right:* $unionPrivate:*) &
        (HasOuterCols $input)
    $aggs:* & (AreAllAnyNotNullAggs $aggs)
    $private:*
)
=>
(Project
    (Project
        (InnerJoin
            (MakeAnyNotNullScalarGroupBy $left)
            (MakeAnyNotNullScalarGroupBy $right)
            []
            (EmptyJoinPrivate)
        )
        (MakeCoalesceProjectionsForUnion $unionPrivate)
        (MakeEmptyColSet)
    )
    (ConvertAnyNotNullAggsToProjections $aggs)
    (IntersectionCols
        (GroupingOutputCols $private $aggs)
        (OutputCols $input)
    )
)
```
- Attempts used: 55
- Last updated: 2026-10-16T13:58:29.842370+00:00
- Reason / notes: The rule's correctness hinges on the identity ANY_NOT_NULL(A ⊎ B) = COALESCE(ANY_NOT_NULL(A), ANY_NOT_NULL(B)), which requires (a) an axiom decomposing an uninterpreted aggregate over a union bag into the same aggregate over its parts — QED explicitly models each aggregate as a single uninterpreted function of the entire input bag with no such decomposition axiom — and (b) COALESCE value-selection semantics, which is absent from the core language and, even if introduced as another uninterpreted scalar, would remain unrelated to the aggregate symbol in QED's SMT encoding; this is a fundamental prover limitation (no aggregate algebra beyond bag-equality-of-input), not a closable DSL gap, so no faithful non-vacuous encoding can be proved. ```

### `UnifyComparisonTypes` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/scalar.opt

UnifyComparisonTypes takes a mixed-type comparison between a non-constant and
a constant and, if appropriate, converts the constant to the type of the
non-constant to allow constraints to be generated.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `UnifyComparisonTypes`, not the other rules in that file):

```
# UnifyComparisonTypes takes a mixed-type comparison between a non-constant and
# a constant and, if appropriate, converts the constant to the type of the
# non-constant to allow constraints to be generated.
[UnifyComparisonTypes, Normalize]
(Comparison
    $left:(Variable)
    $right:(Const) &
        (Let ($result $ok):(UnifyComparison $left $right) $ok)
)
=>
((OpName) $left $result)
```
- Attempts used: 33
- Last updated: 2026-10-16T15:45:23.906790+00:00
- Reason / notes: The rule's soundness is exactly that casting the constant to the variable's type is lossless — i.e. op(x, c) ⟺ op(x, cast(c)) — but QED erases all types to integers and models explicit casts as uninterpreted functions with no axioms (as qed.pdf documents), so cast(c) is a free term and the SMT countermodel (an uninterpreted predicate distinguishing c from f(c)) is a genuine independence result, not an encoding bug. RuleScript exposes no side-condition or axiom mechanism (a rule is just a before/after pair), so the only provable degenerate encoding is the identity cast, which erases the rule entirely; the porter's concrete probe of x = C vs x = cast(C) correctly targeted the irreducible core. ```

### `UseSwapMutation` — ⏭️ SKIPPED

- Source backend: CockroachDB
- Source rule: Source: pkg/sql/opt/norm/rules/mutation.opt

UseSwapMutation converts an Update or a Delete to an UpdateSwap or a
DeleteSwap, respectively. It also replaces the input Scan with Values,
possibly wrapped in a Select.

UpdateSwap and DeleteSwap are optimistic compare-and-swap operations that are
possible when:

- all columns in the primary index are constrained to a single exact value
by the WHERE clause;
- only a single row is modified;
- there are no FK checks or cascades;
- there are no uniqueness checks;
- there are no check constraints;
- there are no vector indexes modified;
- there are no passthrough columns to RETURNING;
- there are no triggers;
- the table only uses a single column family;
- there are no mutation columns;
- there are no mutation indexes;
- there are no columns using composite encoding.

This rule needs to run after PruneMutationInputCols and before
GenerateParameterizedJoin.

Extracted from `mutation.opt` (which defines multiple rules — implement specifically `UseSwapMutation`, not the other rules in that file):

```
# UseSwapMutation converts an Update or a Delete to an UpdateSwap or a
# DeleteSwap, respectively. It also replaces the input Scan with Values,
# possibly wrapped in a Select.
#
# UpdateSwap and DeleteSwap are optimistic compare-and-swap operations that are
# possible when:
#
# - all columns in the primary index are constrained to a single exact value
#   by the WHERE clause;
# - only a single row is modified;
# - there are no FK checks or cascades;
# - there are no uniqueness checks;
# - there are no check constraints;
# - there are no vector indexes modified;
# - there are no passthrough columns to RETURNING;
# - there are no triggers;
# - the table only uses a single column family;
# - there are no mutation columns;
# - there are no mutation indexes;
# - there are no columns using composite encoding.
#
# This rule needs to run after PruneMutationInputCols and before
# GenerateParameterizedJoin.
[UseSwapMutation, Normalize]
(Update | Delete
    $input:* & (HasZeroOrOneRow $input)
    []
    []
    $mutationPrivate:* &
        (CanUseSwapMutation (OpName) $mutationPrivate) &
        (Let
            ($newInput $colMap $ok):(BuildSwapMutationInput
                $input
                $mutationPrivate
            )
            $ok
        )
)
=>
((OpName)
    $newInput
    []
    []
    (UseSwapMutation $mutationPrivate $colMap)
)
```
- Attempts used: 23
- Last updated: 2026-10-16T15:55:48.253872+00:00
- Reason / notes: The rewrite is not a bag equivalence under any encoding: even in the best case (a real PK-equality filter, a unique-key scan, and the same uninterpreted per-row mutation symbol on both sides), a scan lacking a row for that PK makes the before-input empty while the synthesized Values(c) row makes the after-input non-empty, so QED would refute rather than prove it — the rule is sound only because UpdateSwap/DeleteSwap has bespoke no-op-on-absent-row runtime semantics that QED's bag model cannot see, plus guard-level inference (HasZeroOrOneRow, full PK constraint, CanUseSwapMutation) that uninterpreted symbols cannot connect. No non-degenerate special case exists: even modeling the mutation as the identity leaves the genuinely false input-bag swap, and the only provable encoding would be a trivial identity, so the missing values() builder is not the binding gap. ```

