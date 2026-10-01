# RuleScript porting progress

_Last updated: 2026-10-01T13:01:33.456155+00:00_

**74/110 rules proved** (0 failed, 36 skipped as out of QED's supported fragment).

| Rule | Backend | Status | Scope | Attempts | Notes |
|---|---|---|---|---|---|
| `AndFalseAbsorption` | Apache DataFusion | ✅ PROVED | PARTIAL | 10 | The encoding correctly captures the core algebraic identity of the DataFusion rule (`false AND P → false`) by using an uninterpreted pred... |
| `AndNotSelfContradiction` | Apache DataFusion | ✅ PROVED | FULL | 4 | The proof is non-trivial and faithful — `before()` filters on the uninterpreted predicate `A AND NOT(A)` while `after()` filters on the l... |
| `AndOrAbsorption` | Apache DataFusion | ✅ PROVED | FULL | 8 | The encoding is non-vacuous (`Filter(A AND (A OR B))` vs `Filter(A)`) and uses genuinely uninterpreted predicates `A`/`B`, with `A` corre... |
| `AndSelfIdempotent` | Apache DataFusion | ✅ PROVED | PARTIAL | 3 | The encoding correctly captures the core idempotency property (left AND right ≡ left when right is a conjunct of left) with B shared betw... |
| `AndTrueIdentity` | Apache DataFusion | ✅ PROVED | FULL | 7 | The before pattern is genuinely different from the after pattern because it contains `AND(trueLiteral, right)` under a filter, while the ... |
| `BinaryOpNullPropagation` | Apache DataFusion | ✅ PROVED | PARTIAL | 8 | The encoding is a faithful, non-vacuous instance of the simplifier: `Filter(x = NULL)` ⟹ `Filter(NULL::Boolean)` is exactly DataFusion's ... |
| `CommonSubexprEliminate` | Apache DataFusion | ✅ PROVED | PARTIAL | 21 | before() genuinely duplicates the nested subterm g(c0) under two distinct uninterpreted projections f1/f2 over a 1-column scan, while aft... |
| `DeMorganNotAnd` | Apache DataFusion | ✅ PROVED | FULL | 7 | before() = Filter(¬(P∧Q)) and after() = Filter((¬P)∨(¬Q)) are structurally distinct and match the source's `not(A and B) ⟹ (not A) or (no... |
| `DeMorganNotOr` | Apache DataFusion | ✅ PROVED | FULL | 4 | The encoding is a non-vacuous De Morgan rewrite under a generic row-wise filter, with `left` and `right` as independent uninterpreted pre... |
| `DecorrelateLateralJoin` | Apache DataFusion | ✅ PROVED | PARTIAL | 24 | The proof is non-vacuous — `before()` is a correlated (dependent) INNER join while `after()` is an ordinary INNER join, and the uninterpr... |
| `DecorrelatePredicateSubquery` | Apache DataFusion | ✅ PROVED | PARTIAL | 25 | The encoding exactly mirrors the source rule's uncorrelated top-level EXISTS branch of `build_join_top`/`build_join`: `JoinType::LeftSemi... |
| `DedupeGroupByExprs` | Apache DataFusion | ✅ PROVED | PARTIAL | 61 | The proof is non-vacuous and correctly shaped: `before()` groups by three keys whose third is a projected copy of the first (the only way... |
| `DistinctToGroupBy` | Apache DataFusion | ✅ PROVED | PARTIAL | 3 | before() models DataFusion's Distinct(All(input)) as a set-variant self-intersection (all=false, the only intersect variant QED models, y... |
| `DoubleNegationElimination` | Apache DataFusion | ✅ PROVED | FULL | 15 | `before()` is Filter(Not(Not(pred)), Source) and `after()` is Filter(pred, Source) — structurally and semantically distinct plans related... |
| `EliminateAggregateDistinct` | Apache DataFusion | ✅ PROVED | PARTIAL | 5 | The encoding faithfully mirrors the source rule's transformation shape — an Aggregate node with identical group-by whose single aggregate... |
| `EliminateCrossJoin` | Apache DataFusion | ✅ PROVED | PARTIAL | 8 | The encoding faithfully captures the core semantic step of the DataFusion rule—moving a join predicate from a surrounding Filter into the... |
| `EliminateGroupByConstant` | Apache DataFusion | ✅ PROVED | PARTIAL | 11 | `before()` (group set [k, h(k)]) and `after()` (group set [k] + top projection re-deriving h(k)) are structurally distinct and exactly mi... |
| `EliminateJoin` | Apache DataFusion | ✅ PROVED | PARTIAL | 23 | before()/after() genuinely differ (the LEFT join and its uninterpreted condition appear only in before), and the encoded shape — a groupi... |
| `EmptyAggregateToEmpty` | Apache DataFusion | ✅ PROVED | PARTIAL | 25 | `before()` is `Aggregate(Empty)` and `after()` is a structurally distinct `Empty` values node carrying the aggregate's output schema, so ... |
| `EmptyInListToFalseOrNull` | Apache DataFusion | ✅ PROVED | PARTIAL | 26 | before() (SEMI join of an uninterpreted L with a zero-row right side under an uninterpreted condition) and after() (the zero-row relation... |
| `EmptyOptionalJoinSideToNullPadded` | Apache DataFusion | ✅ PROVED | PARTIAL | 7 | The encoding faithfully captures the LEFT-join-with-empty-right branch of the source rule: `before()` is a LEFT join of a left relation a... |
| `EmptyRequiredJoinSideToEmpty` | Apache DataFusion | ✅ PROVED | PARTIAL | 4 | The proof is non-vacuous and semantically exact for the branch it covers: before() is a real INNER join whose left input is a zero-row re... |
| `EmptyUnionBranchElimination` | Apache DataFusion | ✅ PROVED | PARTIAL | 3 | before() (3-branch UNION ALL containing a zero-row, schema-matching branch) and after() (2-branch UNION ALL) are structurally distinct, s... |
| `EqBoolLiteralToOperand` | Apache DataFusion | ✅ PROVED | PARTIAL | 46 | This is a faithful, non-vacuous encoding of the rule's `true = A → A` arm: `A` is a genuinely uninterpreted boolean predicate over the ro... |
| `EqSelfToNotNull` | Apache DataFusion | ✅ PROVED | PARTIAL | 5 | The encoding mirrors the source rule's exact structure — the same uninterpreted column `x` appears on both sides of `EQUALS` (faithfully ... |
| `ExtractEquijoinPredicate` | Apache DataFusion | ✅ PROVED | PARTIAL | 23 | The encoding faithfully models DataFusion's ExtractEquijoinPredicate for the INNER join case: `before()` is `Filter(eq∧rest, Join_on(L,R)... |
| `ExtractLeafExpressions` | Apache DataFusion | ✅ PROVED | PARTIAL | 23 | The encoding is non-vacuous and correctly shared: before() = Filter(R, P(e(x))) differs structurally from after() = π_x(σ_{P(e(x))}(π_{e(... |
| `FilterAcceptsAllToNoop` | Apache DataFusion | ✅ PROVED | PARTIAL | 6 | The encoding is faithful to the `AcceptsAll` branch of the source rule; while it is a narrower special case than the full `EliminateFilte... |
| `FilterNullJoinKeysLeft` | Apache DataFusion | ✅ PROVED | PARTIAL | 8 | The encoding is a faithful, non-trivial special case: before() and after() are structurally different (an extra Filter is added on the le... |
| `FilterNullJoinKeysRight` | Apache DataFusion | ✅ PROVED | PARTIAL | 22 | The encoding correctly captures the rule's key semantic precondition by using real null-rejecting `EQUALS` for the equi-conjunct (not an ... |
| `FilterRejectsAllToEmpty` | Apache DataFusion | ✅ PROVED | PARTIAL | 4 | This faithfully encodes the source's RejectsAll→Empty branch restricted to the FALSE-literal sub-case, which the SCOPE line honestly and ... |
| `FlattenNestedUnions` | Apache DataFusion | ✅ PROVED | PARTIAL | 4 | before() (Union(A, Union(B, C), D)) and after() (Union(A, B, C, D)) are structurally distinct, so the proof is the genuine, non-vacuous c... |
| `FullJoinBothNullRejectingToInner` | Apache DataFusion | ✅ PROVED | PARTIAL | 22 | before() and after() genuinely differ (FULL vs INNER join under the same shared uninterpreted `on` condition and the same shared filter `... |
| `FullJoinLeftNullRejectingToLeft` | Apache DataFusion | ✅ PROVED | PARTIAL | 6 | The encoding faithfully models the rule's named case — a null-rejecting filter (IS_NOT_NULL on the left column, whose type is nullable so... |
| `FullJoinRightNullRejectingToRight` | Apache DataFusion | ✅ PROVED | PARTIAL | 5 | The encoding faithfully captures the named branch (DataFusion's `eliminate_outer` case (Full,false,true) → Right): `before()` and `after(... |
| `InListDedup` | Apache DataFusion | ✅ PROVED | PARTIAL | 21 | The before/after patterns are structurally different (a 4-way OR with one repeated disjunct vs. the deduplicated 3-way OR), so the proof ... |
| `IsUnknownToIsNull` | Apache DataFusion | ✅ PROVED | PARTIAL | 29 | The encoding is faithful and non-trivial: it correctly models the side condition by declaring the scan column non-nullable (`varType("X_T... |
| `LeftJoinNullRejectingToInner` | Apache DataFusion | ✅ PROVED | PARTIAL | 6 | The encoding faithfully captures the Left→Inner branch of `eliminate_outer` with the filter remaining above the join (matching the source... |
| `LiteralIsNullFold` | Apache DataFusion | ✅ PROVED | FULL | 5 | The encoding is faithful and non-vacuous: `before()` is `Filter(x IS NOT NULL, S)` vs `after()` = `Filter(true, S)` (structurally distinc... |
| `MergeConsecutiveFilters` | Apache DataFusion | ✅ PROVED | PARTIAL | 6 | The encoding matches the source rule's core merge branch exactly — `Filter(Q, Filter(P, R))` → `Filter(P ∧ Q, R)` with the child's conjun... |
| `NegateComparisonOperator` | Apache DataFusion | ✅ PROVED | PARTIAL | 42 | The encoding is a faithful, non-degenerate slice: before() = Filter(NOT(x > y)) and after() = Filter(x <= y) over the cross product of tw... |
| `NotBetweenToOutsideRange` | Apache DataFusion | ✅ PROVED | FULL | 102 | The encoding is a faithful expansion of the source rule: `before()` is `NOT(A>=B AND A<=C)` (the standard expansion of `NOT (A BETWEEN B ... |
| `NotEqBoolLiteralToOperand` | Apache DataFusion | ✅ PROVED | PARTIAL | 8 | The encoding faithfully captures the `true != A → !A` arm: `before()` uses `NOT_EQUALS(true_literal, A)` and `after()` uses `Not(A)`, whi... |
| `NotInToNotIn` | Apache DataFusion | ✅ PROVED | PARTIAL | 6 | `before()` (NOT(OR(a=b, a=c))) and `after()` (AND(¬(a=b), ¬(a=c))) are structurally distinct, so the proof is a genuine De Morgan equival... |
| `NotIsNullToIsNotNull` | Apache DataFusion | ✅ PROVED | FULL | 4 | The encoding matches the named rule exactly — Filter(R, NOT(R.f IS NULL)) vs. Filter(R, R.f IS NOT NULL) over a fully uninterpreted scan ... |
| `NullAndOrToNull` | Apache DataFusion | ✅ PROVED | PARTIAL | 9 | The encoding faithfully captures the AND branch of the source rule: `before()` is `Filter(Source, AND(NULL, NULL))` and `after()` is `Fil... |
| `OptimizeProjections` | Apache DataFusion | ✅ PROVED | PARTIAL | 21 | `before()` (two stacked projections, the outer dropping column 2) and `after()` (one projection) are structurally distinct, and their dif... |
| `OrAndAbsorption` | Apache DataFusion | ✅ PROVED | FULL | 7 | `before()` and `after()` are structurally different filters over the same scan whose conditions are `A OR (A AND B)` vs `A`, with A and B... |
| `OrCommonFactorDistribution` | Apache DataFusion | ✅ PROVED | PARTIAL | 5 | The encoding is a faithful, non-vacuous instance of the rule: `before()` is `Filter((A∧B)∨(A∧C))` and `after()` is `Filter(A∧(B∨C))` over... |
| `OrFalseIdentity` | Apache DataFusion | ✅ PROVED | FULL | 8 | The encoding differs from `after()` in exactly the intended way — `Filter(OR(falseLiteral, P), source)` vs `Filter(P, source)` — where `P... |
| `OrSelfIdempotent` | Apache DataFusion | ✅ PROVED | PARTIAL | 12 | The before/after conditions genuinely differ (`(A OR B) OR A` vs `A OR B` over the same scan), and reusing the single `A` symbol for both... |
| `OrTrueAbsorption` | Apache DataFusion | ✅ PROVED | FULL | 4 | `before()` (`Filter(Or(<uninterpreted pred>, trueLiteral), S)`) and `after()` (`Filter(trueLiteral), S`) are structurally distinct, so th... |
| `PassthroughEmptyRelation` | Apache DataFusion | ✅ PROVED | PARTIAL | 5 | before() = Filter(pred, Empty(child)) and after() = Empty(child) are structurally distinct, and the proved claim — for every uninterprete... |
| `PushDistinctAllThroughUnion` | Apache DataFusion | ✅ PROVED | PARTIAL | 5 | The encoding faithfully mirrors the rule for its stated scope: DISTINCT is correctly modeled as a group-by-with-no-aggregate-calls (exact... |
| `PushDownLeafProjections` | Apache DataFusion | ✅ PROVED | PARTIAL | 45 | The encoding is a genuine, non-vacuous instance of the rule's core filter fragment — `Project(F(a),a) over Filter(P(a),Scan)` vs `Filter(... |
| `PushFilterIntoAggregate` | Apache DataFusion | ✅ PROVED | PARTIAL | 6 | `before()` (Filter over Aggregate) and `after()` (Aggregate over Filter) are genuinely different plan shapes, so the proof is non-vacuous... |
| `PushFilterIntoAsOfJoin` | Apache DataFusion | ✅ PROVED | PARTIAL | 46 | The encoding faithfully models the rule's core by splitting the filter into an uninterpreted left-only conjunct `f` (pushed into the left... |
| `PushFilterIntoDistinct` | Apache DataFusion | ✅ PROVED | PARTIAL | 9 | `before()` (Filter(p, dedup(R))) and `after()` (dedup(Filter(p, R))) are structurally distinct plans sharing the same single scan and the... |
| `PushFilterIntoExtension` | Apache DataFusion | ✅ PROVED | PARTIAL | 23 | The encoding is non-vacuous and faithful: before() = π·σ_{P(A)∧Q(A,F(B,t))}(S ⋈_J T) and after() = π·σ_{Q(A,F(B,t))}(σ_{P(A)} S ⋈_J T) ar... |
| `PushFilterIntoJoin` | Apache DataFusion | ✅ PROVED | PARTIAL | 26 | The encoding is a faithful, non-vacuous capture of the exact sub-case it claims: a single filter conjunct over the left input's column (o... |
| `PushFilterIntoProjection` | Apache DataFusion | ✅ PROVED | PARTIAL | 8 | The encoding faithfully mirrors DataFusion's pushdown: `before` is `Filter(F(P1(c)) AND G(P2(c)), Project[P1,P2](Source))` and `after` is... |
| `PushFilterIntoRepartition` | Apache DataFusion | ✅ PROVED | FULL | 6 | The encoding is faithful: Repartition's bag semantics is exactly a schema-preserving pass-through, so the identity projection is the corr... |
| `PushFilterIntoSubqueryAlias` | Apache DataFusion | ✅ PROVED | FULL | 5 | The encoding is faithful: SubqueryAlias is correctly modeled as an identity projection (a pure schema relabel with no effect on row value... |
| `PushFilterIntoUnionBranches` | Apache DataFusion | ✅ PROVED | PARTIAL | 5 | The encoding is non-vacuous (the filter moves from above the UNION ALL down into each branch, which is exactly DataFusion's rewrite) and ... |
| `PushFilterIntoUnnest` | Apache DataFusion | ✅ PROVED | PARTIAL | 46 | The encoding faithfully captures the rule's essence: the non-unnest conjunct P_NON is applied only to the input column and the unnest con... |
| `PushFilterIntoWindow` | Apache DataFusion | ✅ PROVED | PARTIAL | 5 | The encoding faithfully captures the rule's essence: the window is a join-back of the input to a partition-keyed uninterpreted aggregate ... |
| `RedundantDistinctElimination` | Apache DataFusion | ✅ PROVED | PARTIAL | 6 | before() (the set-variant self-intersection of the input) and after() (the bare input) are structurally distinct, and the proven equality... |
| `ReorderPredicatesByCost` | Apache DataFusion | ✅ PROVED | PARTIAL | 5 | The encoding is non-vacuous and faithful to the rule's only bag-semantic core: `before()` and `after()` differ structurally (conjunct ord... |
| `RewriteComparisonViaUdfPreimage` | Apache DataFusion | ✅ PROVED | PARTIAL | 43 | The encoding faithfully captures the core semantic content of DataFusion's `rewrite_with_preimage` for the `=` case: under the step-funct... |
| `RightJoinNullRejectingToInner` | Apache DataFusion | ✅ PROVED | PARTIAL | 7 | The encoding is non-vacuous and semantically correct: `before()` and `after()` differ exactly in join kind (RIGHT vs INNER), the filter `... |
| `SimplifyPredicatesViaBoundAnalysis` | Apache DataFusion | ✅ PROVED | PARTIAL | 52 | before() (filter x>5 AND x>6) and after() (filter x>6) are structurally distinct, and their equivalence is non-vacuous — it requires the ... |
| `SingleDistinctToGroupBy` | Apache DataFusion | ✅ PROVED | PARTIAL | 7 | The proof is non-vacuous and shape-faithful: before() is a single-level DISTINCT aggregate while after() reproduces exactly what the Data... |
| `UnionsToFilter` | Apache DataFusion | ✅ PROVED | PARTIAL | 4 | The encoding is non-vacuous (UNION ALL of two filtered branches vs. a single OR-filtered branch) and semantically faithful to DataFusion'... |
| `UnwrapSingletonUnion` | Apache DataFusion | ✅ PROVED | FULL | 5 | The encoding is faithful and non-trivial: `before()` is a `Union(all=true)` with exactly one source and `after()` is that same source, pr... |
| `BitwiseAndByZero` | Apache DataFusion | ⏭️ SKIPPED | — | 22 | The rule's only non-trivial content is the operator-specific numeric law `A & 0 = 0`, but RuleScript can introduce `&` solely as an unint... |
| `BitwiseOrByZero` | Apache DataFusion | ⏭️ SKIPPED | — | 21 | The rule's entire content is the semantic identity x | 0 = x of the specific bitwise-or operation, but RuleScript can only introduce `|` ... |
| `BitwiseShiftByZeroNoop` | Apache DataFusion | ⏭️ SKIPPED | — | 21 | The rule's soundness rests on the algebraic identity x >> 0 = x, but in QED the shift operator can only appear as an uninterpreted scalar... |
| `BitwiseXorByZero` | Apache DataFusion | ⏭️ SKIPPED | — | 24 | The rule requires the bitwise-Xor identity `x ^ 0 = x`, but RuleScript can only introduce `^` as an uninterpreted projection/predicate an... |
| `CapSortFetchWithLimit` | Apache DataFusion | ⏭️ SKIPPED | — | 7 | QED works in pure bag semantics and, per qed.pdf §6.2, explicitly has no model for the ordering semantics of Sort/Limit/Offset ("does not... |
| `CaseBranchAlwaysFalseElided` | Apache DataFusion | ⏭️ SKIPPED | — | 8 | The rule is pure scalar CASE algebra whose correctness rests entirely on CASE's branch-selection semantics — a value-level ite (first-TRU... |
| `CaseFirstBranchAlwaysTrue` | Apache DataFusion | ⏭️ SKIPPED | — | 25 | This rule's correctness rests entirely on the CASE operator's 3-valued branch-selection semantics (e.g. `case(TRUE, a, b) ≡ a`, dropping ... |
| `CaseNoBranchesTrueToElse` | Apache DataFusion | ⏭️ SKIPPED | — | 9 | The rule's core claims (CASE WHEN true THEN A ELSE B → A, and dropping false branches) require an interpreted value-level ite/CASE operat... |
| `CastLiteralFold` | Apache DataFusion | ⏭️ SKIPPED | — | 24 | The rule's essential content is cast value algebra — QED models any cast as an uninterpreted function (qed.pdf §6.2) with no cast identit... |
| `CommuteLimitProjection` | Apache DataFusion | ⏭️ SKIPPED | — | 22 | The rule's correctness rests on Limit's ordered-prefix semantics — *which* rows are retained depends on row order — and QED's bag-semanti... |
| `CommuteLimitSubqueryAlias` | Apache DataFusion | ⏭️ SKIPPED | — | 24 | SubqueryAlias is a pure name change — bag-identity — and RuleScript's core language has no operator for it (faithfully omitting it is the... |
| `DedupeSortExprs` | Apache DataFusion | ⏭️ SKIPPED | — | 7 | The rule only trims the ORDER BY key list while the input (and hence the bag of emitted rows) is unchanged on both sides, so its entire s... |
| `DistinctNoColumnsToLimitOne` | Apache DataFusion | ⏭️ SKIPPED | — | 48 | The rule's target is exactly `Limit { fetch: Some(1) }`, and QED does not model Limit/Offset/Sort (list/ordering semantics have no meanin... |
| `DivideByOne` | Apache DataFusion | ⏭️ SKIPPED | — | 11 | I verified the porter's two claims against the ground-truth sources: `RexRN.java` exposes only `trueLiteral`/`falseLiteral` (no numeric l... |
| `LimitZeroToEmpty` | Apache DataFusion | ⏭️ SKIPPED | — | 7 | The rule's soundness (LIMIT with literal fetch 0 ⇒ empty relation, and skip-0/no-fetch ⇒ identity) rests entirely on the row-count/orderi... |
| `MergeNestedLimits` | Apache DataFusion | ⏭️ SKIPPED | — | 9 | The rule's soundness is a row-position identity — OFFSET drops the first k rows of a stream and LIMIT keeps the first n — but QED's bag s... |
| `ModuloByOne` | Apache DataFusion | ⏭️ SKIPPED | — | 12 | QED models every scalar operator (including `Modulo`) as an uninterpreted function symbol over uninterpreted types, and the DSL exposes o... |
| `MultiplyByOne` | Apache DataFusion | ⏭️ SKIPPED | — | 21 | The rewrite A * 1 ⟹ A is valid only because 1 is the multiplicative identity of real arithmetic, but QED models scalar operators like Mul... |
| `MultiplyByZero` | Apache DataFusion | ⏭️ SKIPPED | — | 41 | MultiplyByZero is a pure scalar arithmetic identity — its soundness rests on the axiom ∀a. *(a, 0) = 0 (guarded by non-nullness of a), bu... |
| `NotLikeToNotLike` | Apache DataFusion | ⏭️ SKIPPED | — | 24 | The rule's entire content is the definitional duality ¬(x LIKE y) ≡ x NOT LIKE y, and QED's fixed logic has no axiom or definitional expa... |
| `OrNotSelfTautology` | Apache DataFusion | ⏭️ SKIPPED | — | 108 | The rule's soundness rests entirely on A being non-nullable (in Kleene logic A OR NOT(A) = NULL when A = NULL), and the porter's encoding... |
| `PushFilterIntoSort` | Apache DataFusion | ⏭️ SKIPPED | — | 7 | I confirmed against the ground-truth RelRN.java that no Sort builder or record exists (JSONSerializer can carry a sort node, but the trus... |
| `PushFilterIntoTableScan` | Apache DataFusion | ⏭️ SKIPPED | — | 7 | The only branch of this rule with bag-semantic content — dropping an `Exact`-pushed conjunct from the Filter above a TableScan — is sound... |
| `PushLimitIntoCrossJoin` | Apache DataFusion | ⏭️ SKIPPED | — | 27 | The cross-join branch rewrites `LIMIT_n(L ⋈cross R)` to `LIMIT_n(LIMIT_n(L) ⋈cross LIMIT_n(R))`, whose correctness is a purely prefix/enu... |
| `PushLimitIntoLeftJoin` | Apache DataFusion | ⏭️ SKIPPED | — | 9 | The soundness of this rewrite rests entirely on limit/row-count semantics — that the outer LIMIT(skip, fetch) can only observe rows produ... |
| `PushLimitIntoRightJoin` | Apache DataFusion | ⏭️ SKIPPED | — | 26 | The rule's soundness rests entirely on row-count/ordering semantics — that in a right join each preserved right-input row appears in the ... |
| `PushLimitIntoTableScan` | Apache DataFusion | ⏭️ SKIPPED | — | 6 | The rule's validity is purely a prefix-in-enumeration-order argument — the outer Limit(skip, fetch) can only consume the first skip+fetch... |
| `PushLimitIntoUnionBranches` | Apache DataFusion | ⏭️ SKIPPED | — | 8 | This rule is valid only because DataFusion’s union preserves branch order and Limit selects a row-position prefix, so capping each branch... |
| `PushTopKThroughJoin` | Apache DataFusion | ⏭️ SKIPPED | — | 21 | The rule's substance — that the top-N rows of a LEFT/RIGHT join, ordered by keys that depend only on the preserved side, are all producib... |
| `RemoveNoopLimit` | Apache DataFusion | ⏭️ SKIPPED | — | 21 | Both halves of this rule (fetch=Literal(0) ⇒ empty relation; fetch=None with skip=Literal(0) ⇒ drop the Limit as identity) draw their ent... |
| `RewriteSetComparison` | Apache DataFusion | ⏭️ SKIPPED | — | 66 | The rule's entire semantic content is a scalar three-valued-logic identity — `x op ANY/ALL (S)` being equal to a CASE over EXISTS subquer... |
| `ScalarSubqueryToJoin` | Apache DataFusion | ⏭️ SKIPPED | — | 114 | The scalar-subquery → LEFT JOIN rewrite is only valid because a global (groupless) aggregate over an *empty* input yields the same value ... |
| `SimplifyRegexToLike` | Apache DataFusion | ⏭️ SKIPPED | — | 13 | The rule's entire semantic content is a string pattern-matching axiom (e.g. `x ~ '^foo$'` ⟺ `x = 'foo'`, `x ~ 'foo'` ⟺ `x LIKE '%foo%'`),... |
| `SimplifyUdafToScalar` | Apache DataFusion | ⏭️ SKIPPED | — | 29 | The assigned source (L1677–1685) is a pure dispatch that hands the aggregate to an arbitrary per-UDAF, user-supplied `simplify()` closure... |
| `StructCastSameFieldCountFold` | Apache DataFusion | ⏭️ SKIPPED | — | 5 | The rule's only effect is plan-time constant evaluation — replacing an eligible CAST/TRY_CAST over a struct literal with the concrete val... |
| `UnwrapCastAroundComparison` | Apache DataFusion | ⏭️ SKIPPED | — | 10 | The rewrite `cast(x) cmp y ⟺ x cmp cast(y)` is only sound because of the specific cast's value-algebra (injectivity/monotonicity, literal... |

## Details

### `AndFalseAbsorption` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1050-1055
- Attempts used: 10
- Last updated: 2026-09-30T23:04:56.349231+00:00
- Reason / notes: The encoding correctly captures the core algebraic identity of the DataFusion rule (`false AND P → false`) by using an uninterpreted predicate for the right operand and a literal `false` for the left, within a `Filter` context. The `before()` and `after()` are structurally distinct (conjunction vs. bare false), the proof is non-vacuous, and the PARTIAL scope marker is honest: the original rule is a general scalar-expression rewrite applicable in any expression context, but RuleScript's relational pattern language naturally restricts it to the filter-condition position on a base scan, which is the most common and practically useful instance. No symbol-sharing, operator, or precondition issues are present. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 177250}, panicked=False

### `AndNotSelfContradiction` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1068-1075
- Attempts used: 4
- Last updated: 2026-09-30T22:57:15.795447+00:00
- Reason / notes: The proof is non-trivial and faithful — `before()` filters on the uninterpreted predicate `A AND NOT(A)` while `after()` filters on the literal `false`, and QED shows both yield the empty bag for every instantiation of the uninterpreted `A`, with the *same* symbol `a` correctly shared between the conjunct and its negation (matching the source's `is_not_of(right, left)` self-contradiction constraint; two independent predicates would not be provable). The source's `!info.nullable(&left)` precondition is not silently dropped in a way that proves an easier or false claim: in a filter position `A AND NOT(A)` excludes every row exactly like `false` regardless of nullability (in both 2-valued and SQL 3-valued semantics), so nothing the proven claim needs is missing. The filter embedding with a fully uninterpreted predicate is therefore the complete relational form of this boolean-condition simplification, `before()`/`after()` are structurally distinct and non-degenerate, and `SCOPE: FULL` is honest.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 509917}, panicked=False

### `AndOrAbsorption` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1096-1102
- Attempts used: 8
- Last updated: 2026-09-30T23:02:45.679122+00:00
- Reason / notes: The encoding is non-vacuous (`Filter(A AND (A OR B))` vs `Filter(A)`) and uses genuinely uninterpreted predicates `A`/`B`, with `A` correctly shared as the identical symbol in both the AND-left and the OR and `B` kept independent — exactly the absorption premise — and the correct relational/boolean shape (a Filter whose condition is `A ∧ (A ∨ B)`). QED's success therefore reflects a real universal tautology `∀A B: A ∧ (A ∨ B) ≡ A` checked pointwise over rows, not a structural coincidence or a hidden precondition (no PK/NOT NULL is needed). It captures the canonical orientation; the commutative variants (`A∧(B∨A)`, `(A∨B)∧A`) are the same boolean identity, so `SCOPE: FULL` is fair and the result is non-degenerate.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 164210709}, panicked=False

### `AndSelfIdempotent` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1084-1089
- Attempts used: 3
- Last updated: 2026-09-30T23:05:13.563865+00:00
- Reason / notes: The encoding correctly captures the core idempotency property (left AND right ≡ left when right is a conjunct of left) with B shared between the left tree and the outer AND, making before() and after() structurally distinct yet semantically equivalent; the narrowing to a single atomic predicate as the duplicated subexpression is a genuine DSL limitation (no subexpression meta-variable exists) and is honestly declared in the SCOPE line, while all other aspects (AND operator, filter shape, symbol reuse) faithfully mirror the DataFusion rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 123333}, panicked=False

### `AndTrueIdentity` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1044-1049
- Attempts used: 7
- Last updated: 2026-09-30T23:06:52.527918+00:00
- Reason / notes: The before pattern is genuinely different from the after pattern because it contains `AND(trueLiteral, right)` under a filter, while the after pattern retains only the same uninterpreted predicate `right`. This captures the DataFusion law for an arbitrary boolean expression without adding a semantic precondition, and the shared `right` symbol correctly preserves the rewritten operand.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 91917}, panicked=False

### `BinaryOpNullPropagation` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 843-856
- Attempts used: 8
- Last updated: 2026-09-30T23:12:33.704098+00:00
- Reason / notes: The encoding is a faithful, non-vacuous instance of the simplifier: `Filter(x = NULL)` ⟹ `Filter(NULL::Boolean)` is exactly DataFusion's rewrite (binary expr with a literal-null operand → NULL literal of the expression's type) applied in predicate position for `Eq`, a `returns_null_on_null` operator, and proving it requires the prover to actually know that `x = NULL` is never true — so the proof certifies the null-propagation property itself, not a structural identity. The narrowing to a concrete operator and one operand position is a genuine QED limitation, not a missing DSL capability (uninterpreted operator symbols can be expressed via `RexRN.Pred`, but the prover has no null-propagation axiom for them and must hold for all instantiations, so per-concrete-operator instances are the widest provable form), and the `SCOPE: PARTIAL` line states precisely this restriction. No failure modes triggered: the shared type `V` between the column and the null literal mirrors the source rule's type-coerced null operand rather than an accidental over-constraint, the `returns_null_on_null` guard is instantiated (not silently dropped), and `nullBool` correctly has the comparison's result type, not the operand's.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 403125}, panicked=False

### `CommonSubexprEliminate` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/common_subexpr_eliminate.rs, lines 1-841
- Attempts used: 21
- Last updated: 2026-09-30T23:58:59.785280+00:00
- Reason / notes: before() genuinely duplicates the nested subterm g(c0) under two distinct uninterpreted projections f1/f2 over a 1-column scan, while after() hoists g into an intermediate projection [g(c0), c0] and references its output column from the outer projection — structurally different plans that mirror DataFusion's try_optimize_proj + build_common_expr_project_plan shape exactly (common expr first, passthrough after, top projection drops the passthrough), with Source/g/f1/f2 correctly shared by name across both sides, so the proof is non-vacuous and universal over the uninterpreted symbols (the extensional-function model matches the rule's own is_volatile_node precondition rather than silently dropping it). The declared PARTIAL scope is honest and specific — projection node only, exactly two projected expressions sharing exactly one common subexpression — even though the full rule also covers Filter/Sort/Window/Aggregate and arbitrary expression lists (Window/Sort aren't even exposed by the DSL); the encoded instance is a non-degenerate, genuine special case of the real rewrite, not a structurally-identical tautology. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 348167}, panicked=False

### `DeMorganNotAnd` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 319-325
- Attempts used: 7
- Last updated: 2026-10-01T00:07:21.055080+00:00
- Reason / notes: before() = Filter(¬(P∧Q)) and after() = Filter((¬P)∨(¬Q)) are structurally distinct and match the source's `not(A and B) ⟹ (not A) or (not B)` exactly, with P and Q as two independent uninterpreted predicates (correct symbol sharing, nothing hard-coded), and the filter-over-scan shape is a neutral vehicle for the scalar identity rather than an added assumption — the identity holds pointwise for all truth assignments (including nulls), so the proof covers the rule's full Boolean content in any relational context, and the nested recursive case is just repeated application of the same universally-proved identity. DataFusion's `negate_clause` And-arm has no preconditions (DeMorgan holds under three-valued logic, and the encoding's nullable predicate types keep nulls in the model), so no precondition is silently dropped, and SCOPE: FULL is honest: the encoding assumes nothing the original rule doesn't actually require. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 433042}, panicked=False

### `DeMorganNotOr` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 326-332
- Attempts used: 4
- Last updated: 2026-10-01T00:05:40.737108+00:00
- Reason / notes: The encoding is a non-vacuous De Morgan rewrite under a generic row-wise filter, with `left` and `right` as independent uninterpreted predicates, so it captures the rule’s full semantic generality without hard-coded structure. Using `Not(left)`/`Not(right)` is an appropriate uninterpreted-predicate abstraction of the source’s recursively negated children, since their internal structure is irrelevant to the semantic equivalence being proven.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 126416}, panicked=False

### `DecorrelateLateralJoin` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/decorrelate_lateral_join.rs, lines 1-373
- Attempts used: 24
- Last updated: 2026-10-01T00:26:41.838334+00:00
- Reason / notes: The proof is non-vacuous — `before()` is a correlated (dependent) INNER join while `after()` is an ordinary INNER join, and the uninterpreted predicate `cond` is applied in identical argument order (left column, then right column) on both sides, so QED proved the genuine dependent-join = join-with-condition identity, which is exactly the core rewrite DecorrelateLateralJoin performs in its simplest form. `L` and `R` are distinct scans with no hidden key/not-null assumptions, the correlation predicate is properly shared (not accidentally duplicated or independently named), and the omitted surface — LEFT join kind, user ON clause, aggregate pull-up, count-bug NULL compensation, HAVING — is declared accurately and specifically in the SCOPE line, with the aggregate/count-bug parts genuinely beyond QED's aggregate-algebra and NULL/CASE modeling. The result is therefore a faithful, honest, non-degenerate PARTIAL encoding rather than a trivial or over-constrained artifact.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 67514916}, panicked=False

### `DecorrelatePredicateSubquery` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/decorrelate_predicate_subquery.rs, lines 1-765
- Attempts used: 25
- Last updated: 2026-10-01T00:30:25.131907+00:00
- Reason / notes: The encoding exactly mirrors the source rule's uncorrelated top-level EXISTS branch of `build_join_top`/`build_join`: `JoinType::LeftSemi` with join filter `lit(true)` (the `(None, None)` case) and the subquery plan carried over unchanged as the right input, with the subquery's own predicate left uninterpreted (`inner_pred`) and `outer`/`inner` as distinct symbols — so `before()` (Filter with EXISTS scalar subquery) and `after()` (SEMI join) are genuinely different plans and the proof is non-vacuous, covering precisely the transformation `Filter(R, EXISTS(S)) ≡ SEMI(R, true, S)` that DataFusion produces in this case (the post-join schema-restoring projection also doesn't fire for semi joins, as in the source). The scope line is honest and specific: NOT EXISTS/IN/NOT-IN, correlation-predicate extraction, null-awareness, and mark-join paths are genuinely out of scope (the DSL has no relational metavariable to keep the subquery/outer fully arbitrary, and QED cannot model null-aware NOT IN semantics), and the proved special case is non-degenerate rather than a structural tautology.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 70735917}, panicked=False

### `DedupeGroupByExprs` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_duplicated_expr.rs, lines 115-138
- Attempts used: 61
- Last updated: 2026-10-01T01:36:23.222586+00:00
- Reason / notes: The proof is non-vacuous and correctly shaped: `before()` groups by three keys whose third is a projected copy of the first (the only way to materialize a duplicate group key, since Calcite's group key collapses duplicate references) and then drops the redundant column to match the after-side schema, while `after()` is the clean two-key aggregate with the same uninterpreted `sum` and shared scan symbols — no concrete values or operators were baked in, and if QED had *not* seen through the copy-projection the claim would actually be false (a 3-key grouping with an independent third column refines the 2-key grouping and changes the sums), so the PROVABLE result confirms it recognized the duplicate. This is a narrower special case than the full DataFusion rule (plain-column duplicate of the first key in the third position, one aggregate call, arbitrary duplicate expressions/positions/multiple aggregates not covered), but the restriction is specific and the `SCOPE: PARTIAL` line states it precisely, so it is an honest, non-degenerate encoding of the rule's core algebra (redundant group key is inert, with the corresponding output column dropped).
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 86806375}, panicked=False

### `DistinctToGroupBy` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/replace_distinct_aggregate.rs, lines 89-127
- Attempts used: 3
- Last updated: 2026-10-01T00:48:18.475848+00:00
- Reason / notes: before() models DataFusion's Distinct(All(input)) as a set-variant self-intersection (all=false, the only intersect variant QED models, yielding each input row exactly once = bag-distinct) and after() is exactly the source's main-branch target, Aggregate::try_new(input, group_expr, vec![]) with group keys = all input columns in order and zero aggregate calls; the two sides are structurally distinct, share the single `input` symbol correctly, and the equivalence is a genuine non-vacuous semantic fact. The disclosed exclusions are genuine: the zero-column LIMIT-1 branch has no bag-semantic counterpart in QED, and the unique-input short-circuit is a separate transform that a single before/after pair can't conditionally express (the main-branch rewrite remains valid even on unique inputs). The fixed two-column, non-nullable scan follows the project's house convention (the reference FilterMerge example is likewise a fixed single-column scan tagged FULL), is honestly tagged PARTIAL with the main-branch restriction spelled out, and the type names remain uninterpreted, so the proof covers the full family of that arity — a faithful, non-degenerate encoding of the rule's core rewrite. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 60773250}, panicked=False

### `DoubleNegationElimination` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 339-340
- Attempts used: 15
- Last updated: 2026-10-01T01:00:54.635809+00:00
- Reason / notes: `before()` is Filter(Not(Not(pred)), Source) and `after()` is Filter(pred, Source) — structurally and semantically distinct plans related by exactly the source rewrite `Expr::Not(Not(A)) => A`, with the source table, its row type, and the predicate all left as uninterpreted symbols, so the universal proof covers arbitrary relations and arbitrary predicates. The filter-over-scan shape is merely the canonical relational host for this purely expression-level Boolean law (no other context carries more content), and no preconditions are missing — ¬¬A and A coincide even under three-valued NULL semantics, since a filter keeps only rows whose condition is TRUE and ¬¬NULL = NULL. Thus the encoding is non-vacuous, free of accidental over-constraint, and the `// SCOPE: FULL` tag is honest. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 321292}, panicked=False

### `EliminateAggregateDistinct` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_aggregate_distinct.rs, lines 1-163
- Attempts used: 5
- Last updated: 2026-10-01T01:08:24.996905+00:00
- Reason / notes: The encoding faithfully mirrors the source rule's transformation shape — an Aggregate node with identical group-by whose single aggregate call has the DISTINCT modifier dropped — and is non-vacuous: before() and after() are structurally different (distinct=true vs false on the same uninterpreted aggregate "f" over the same operand symbol, unified by name), and the proof genuinely depends on the declared unique-key constraint, since over an unconstrained table the claim would be false for an uninterpreted f. The narrowing to a one-column table whose group key is the table's unique key (making every group a ≤1-row bag over which deduplication is the identity) is a genuine QED limitation rather than a DSL gap — QED's uninterpreted-aggregate model cannot exploit min/max/bool_and idempotence, which is the full rule's actual justification, and no DSL extension could teach the frozen prover that algebra — and the SCOPE: PARTIAL line states this assumption specifically and honestly, with no symbol-sharing or operator-shape errors.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 73681958}, panicked=False

### `EliminateCrossJoin` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_cross_join.rs, lines 1-455
- Attempts used: 8
- Last updated: 2026-10-01T01:15:20.629494+00:00
- Reason / notes: The encoding faithfully captures the core semantic step of the DataFusion rule—moving a join predicate from a surrounding Filter into the INNER join's ON clause and dropping it from the Filter—using uninterpreted predicates (join_eq, rest) over the full join row, which is the right abstraction since the bag-semantic equivalence holds for any predicate, not only equi-joins. The INNER join kind with a TRUE condition correctly models a cross join, the shared symbol join_eq is exactly what the rewrite intends (the same predicate relocates from filter to ON), and before()/after() are structurally distinct so the proof is non-vacuous; the SCOPE line honestly and specifically flags the narrowing to a 2-input, single-conjunct case (the general rule also re-associates n-way joins and extracts keys through OR). ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 69143500}, panicked=False

### `EliminateGroupByConstant` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_group_by_constant.rs, lines 1-151
- Attempts used: 11
- Last updated: 2026-10-01T01:26:17.356473+00:00
- Reason / notes: `before()` (group set [k, h(k)]) and `after()` (group set [k] + top projection re-deriving h(k)) are structurally distinct and exactly mirror DataFusion's rewrite — simplified aggregate plus schema-preserving projection — with `h` and `f` correctly shared as uninterpreted symbols, so the source rule's "deterministic function of the bare group columns" precondition holds by construction (h is a function of the retained key k, and the non-empty `required`-keys bail-out is satisfied since one bare key remains). The PARTIAL scope line is honest and specific (one bare key, one redundant expression, one non-distinct aggregate over a second column), and within that shape the proof is fully general over the expression's form (any deterministic expression, including pure constants, is stood in for by uninterpreted h) and the aggregate, with no hidden preconditions, wrong operators, or vacuous identity.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 88593708}, panicked=False

### `EliminateJoin` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_join.rs, lines 1-687
- Attempts used: 23
- Last updated: 2026-10-01T01:36:20.057559+00:00
- Reason / notes: before()/after() genuinely differ (the LEFT join and its uninterpreted condition appear only in before), and the encoded shape — a grouping-only group-by on the preserved left column above L LEFT JOIN R ON <uninterpreted>, with no right-side column visible above the join — is a real firing of the source rule's ReplaceWithLeft branch: in the Rust code a grouping-only Aggregate sets child_duplicate_insensitive=true, visible_right is empty, and rewritten_join_type returns ReplaceWithLeft, dropping exactly the join and its condition as the encoding does. The narrowing (one of three branches, one-column scans, group-by instead of an arbitrary duplicate-collapsing ancestor or FD-uniqueness proof) is a genuine DSL-expressibility limit rather than a bug, is specifically and honestly declared in the SCOPE: PARTIAL line, and the proof is non-vacuous since it requires QED's group-by key-deduplication semantics to absorb the join's row multiplication — a trivially-identical encoding or a multiplicity-preserving group-by would not have proved. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 74794625}, panicked=False

### `EmptyAggregateToEmpty` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/propagate_empty_relation.rs, lines 176-189
- Attempts used: 25
- Last updated: 2026-10-01T01:57:15.895455+00:00
- Reason / notes: `before()` is `Aggregate(Empty)` and `after()` is a structurally distinct `Empty` values node carrying the aggregate's output schema, so the proved equivalence (group-by over empty input ⇒ empty bag) is non-vacuous and matches DataFusion's PropagateEmptyRelation aggregate branch exactly; the group key and aggregate call are uninterpreted symbols over the single empty child, the "no empty grouping set" precondition is satisfied by using a plain `group-by` (no grouping sets modeled), and the `PARTIAL` scope tag is honest and specific.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 4486666}, panicked=False

### `EmptyInListToFalseOrNull` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1814-1819
- Attempts used: 26
- Last updated: 2026-10-01T02:07:47.958240+00:00
- Reason / notes: before() (SEMI join of an uninterpreted L with a zero-row right side under an uninterpreted condition) and after() (the zero-row relation of L's schema) are structurally distinct, and their equivalence is exactly the bag-level content of `x IN () -> false`: no left row can match a value set containing no rows, and since L, the join condition, and both column types are all uninterpreted, the proof covers every left input and every match condition, subsuming the actual equality condition of a real IN. No precondition is silently missing — the empty-list case has no NULL subtlety (the empty disjunction is false even for a NULL x, and an empty right side makes the semi-join condition moot), so the relational statement is not an easier claim than the source rule. The only narrowing is that the negated branch (NOT IN () -> true, the sibling ANTI(L, ∅) = L rule) is not included, and that restriction is genuine (the DSL has no scalar InList predicate or list constants, and QED models no list semantics) and honestly tagged PARTIAL with a specific condition. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 25380875}, panicked=False

### `EmptyOptionalJoinSideToNullPadded` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/propagate_empty_relation.rs, lines 75-175
- Attempts used: 7
- Last updated: 2026-10-01T01:42:56.859324+00:00
- Reason / notes: The encoding faithfully captures the LEFT-join-with-empty-right branch of the source rule: `before()` is a LEFT join of a left relation against a zero-row `Empty` (correctly modeling `produce_one_row = false`) of the right's type, and `after()` projects the left columns plus typed NULL literals for the right columns — a genuinely non-trivial Join→Project equivalence that holds under bag semantics because a LEFT join against an empty relation null-extends every surviving row. The uninterpreted join condition and the independent left/right type symbols are shared correctly (the condition is irrelevant when the right side is empty, and the NULL literal types `r0`/`r1` match the join's null-extension types), with no accidental over-constraint. The SCOPE tag honestly and specifically records the genuine DSL-imposed pinning to a single join type, a single empty side, and fixed two-column arity (variable arity and join-type-parameterized outer-join semantics are not expressible in the current pattern language), making this a valid, non-degenerate partial port of a real rule branch. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 16796000}, panicked=False

### `EmptyRequiredJoinSideToEmpty` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/propagate_empty_relation.rs, lines 75-175
- Attempts used: 4
- Last updated: 2026-10-01T01:50:28.934317+00:00
- Reason / notes: The proof is non-vacuous and semantically exact for the branch it covers: before() is a real INNER join whose left input is a zero-row relation (DataFusion's `EmptyRelation { produce_one_row: false }`) with an uninterpreted join condition, and after() is a bare zero-row relation of the join's output schema (left++right), so the proved equivalence is precisely the source rule's `JoinType::Inner if left_empty` claim, for all instantiations of the uninterpreted types and predicate. The narrowing to INNER + empty-left (with two uninterpreted-typed columns per side) is explicitly and specifically disclosed in the SCOPE line, and it is a non-degenerate representative member: the rest of the source family is genuinely heterogeneous (other join types yield empty, the *surviving* side for the anti-join cases, or a null-padded projection — which needs a null literal the DSL does not yet expose) and cannot be captured by this single before/after pattern, so an honest partial rule is the right form here rather than a misleading claim of full generality.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 265958}, panicked=False

### `EmptyUnionBranchElimination` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/propagate_empty_relation.rs, lines 190-231
- Attempts used: 3
- Last updated: 2026-10-01T01:55:07.962480+00:00
- Reason / notes: before() (3-branch UNION ALL containing a zero-row, schema-matching branch) and after() (2-branch UNION ALL) are structurally distinct, so the proof is non-vacuous and establishes exactly the algebraic fact at the core of the DataFusion rule — eliminating a zero-row branch from a bag union is a no-op — with the remaining branches as independent uninterpreted scans sharing the union's row type and the correct all=true (UNION ALL) flag matching DataFusion's LogicalPlan::Union. The PARTIAL scope tag is honest and specific: it models only the ≥2-branches-remain case, while the collapse-to-single-branch and collapse-to-empty sub-cases (and arbitrary arity in general) cannot be captured by any fixed-arity pattern, so this is a legitimate, non-degenerate special case rather than a vacuous or coincidental one.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 416625}, panicked=False

### `EqBoolLiteralToOperand` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 871-884
- Attempts used: 46
- Last updated: 2026-10-01T02:41:37.914369+00:00
- Reason / notes: This is a faithful, non-vacuous encoding of the rule's `true = A → A` arm: `A` is a genuinely uninterpreted boolean predicate over the row (not a hard-coded condition), the `filter(...)` wrapper is the standard faithful relational carrier for a scalar equivalence (so proving the two filter bags equal is equivalent to proving the scalar expressions equal), and `before()` (`true = A`) vs `after()` (`A`) are structurally distinct, so the proof is not vacuous. The restriction to one of the three arms is a real limit of the single before/after rule format (the three arms map to three distinct targets that one pair cannot express, and QED's rule unit is inherently one pair), it is correctly and specifically tagged `PARTIAL`, and no precondition is silently dropped — `A`'s boolean-ness matches the source's `is_boolean_type` guard, and the equivalence `true = A ≡ A` holds in both 2VL and 3VL, so the "provable" result reflects the real semantics.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 326791}, panicked=False

### `EqSelfToNotNull` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 899-917
- Attempts used: 5
- Last updated: 2026-10-01T02:07:01.272550+00:00
- Reason / notes: The encoding mirrors the source rule's exact structure — the same uninterpreted column `x` appears on both sides of `EQUALS` (faithfully capturing the `left == right` precondition, so the proof is not a coincidental match of independent symbols), the target is precisely `IS_NOT_NULL(x) OR NULL-literal`, and the source column is correctly marked nullable to match the selected branch; the proof is non-vacuous because the equivalence is the genuinely non-trivial 3-valued identity (both sides NULL when x is NULL, both TRUE otherwise), and QED's ability to prove it confirms the standard operators were interpreted semantically rather than as unrelated uninterpreted predicates. The SCOPE tag is honest and specific: the non-nullable branch (`A = A → true`) has a different RHS under a different nullability assumption and genuinely cannot share a single before/after pair, so restricting to the nullable branch is a real limitation of the encoding, not a vacuity — and the remaining rule is a meaningful, non-degenerate optimization.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 98333}, panicked=False

### `ExtractEquijoinPredicate` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/extract_equijoin_predicate.rs, lines 1-273
- Attempts used: 23
- Last updated: 2026-10-01T02:29:36.662797+00:00
- Reason / notes: The encoding faithfully models DataFusion's ExtractEquijoinPredicate for the INNER join case: `before()` is `Filter(eq∧rest, Join_on(L,R))` and `after()` is `Filter(rest, Join_{on∧eq}(L,R))`, which are structurally distinct and the proof is non-vacuous. The three predicates (on, eq, rest) are correctly independent uninterpreted symbols over the joined row, the residual-filter-as-Fill-above-Join shape matches DataFusion's Join.filter semantics, and the INNER-only restriction is genuine (for outer joins, null-extended unmatched rows interact unsoundly with moving a conjunct into the join condition). The scope tag honestly and specifically states the restrictions.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 69243458}, panicked=False

### `ExtractLeafExpressions` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/extract_leaf_expressions.rs, lines 164-744
- Attempts used: 23
- Last updated: 2026-10-01T02:31:27.864668+00:00
- Reason / notes: The encoding is non-vacuous and correctly shared: before() = Filter(R, P(e(x))) differs structurally from after() = π_x(σ_{P(e(x))}(π_{e(x),x}(R))), with the same uninterpreted symbols e (leaf projection) and P (predicate) used on both sides, no uniqueness flag or other precondition assumed, and the exact shape of DataFusion's Filter-parent case (extraction projection inserted below the filter with the extracted column, filter rewritten to reference the materialized column, recovery projection dropping it to restore the output schema). The scope line is accurate and specific: this is a declared PARTIAL special case — Filter parent only, single-column input, one extracted expression — rather than a full rule covering Aggregate/Join/Sort/Limit and N extractions, but it is a genuine, non-degenerate core instance of the transformation (nothing that should be a symbol was hard-coded concrete), so QED's provable result is a real, faithful proof of a meaningful fragment of the source rule. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 66765042}, panicked=False

### `FilterAcceptsAllToNoop` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_filter.rs, lines 43-236
- Attempts used: 6
- Last updated: 2026-10-01T02:46:45.229566+00:00
- Reason / notes: The encoding is faithful to the `AcceptsAll` branch of the source rule; while it is a narrower special case than the full `EliminateFilter` rule (as correctly noted in the SCOPE tag), it represents the fundamental logical identity that the source rule relies on. It is non-trivial (the before and after nodes are structurally distinct) and the use of `trueLiteral()` is the most general form of an "accepts all" predicate that the QED prover can verify as semantically equivalent to a no-op. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 368625}, panicked=False

### `FilterNullJoinKeysLeft` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/filter_null_join_keys.rs, lines 1-109
- Attempts used: 8
- Last updated: 2026-10-01T02:49:08.918278+00:00
- Reason / notes: The encoding is a faithful, non-trivial special case: before() and after() are structurally different (an extra Filter is added on the left input), the INNER join with regular EQUALS correctly models the source rule's non-null-aware, NullEqualsNothing precondition, and the proof genuinely requires NULL semantics (IS NOT NULL on nullable keys is redundant only because NULL = anything is never TRUE in an inner equi-join). The SCOPE line honestly and specifically states the restrictions (INNER join only, two keys, both left keys nullable, left-side filter only), each of which is a genuine narrowing of the full source rule that handles multiple join types, any key count, per-key nullability checks, and both sides. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 72470750}, panicked=False

### `FilterNullJoinKeysRight` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/filter_null_join_keys.rs, lines 1-109
- Attempts used: 22
- Last updated: 2026-10-01T02:59:49.745273+00:00
- Reason / notes: The encoding correctly captures the rule's key semantic precondition by using real null-rejecting `EQUALS` for the equi-conjunct (not an uninterpreted predicate), so the proof that pre-filtering `IS NOT NULL(R.col)` off the right input of an INNER join is bag-equivalent is genuine and non-vacuous — a null-key right row can never satisfy the join condition and thus contributes nothing, making before()/after() differ by a real, meaningful filter pushdown rather than being structurally identical. The `SCOPE: PARTIAL` line honestly and specifically discloses the narrowing (single inner-join equi-pair, right-side filter only, vs. the source's per-pair filter for every right-preserved join kind), and the symbols are correctly shared with matching key types, so this is a faithful, non-degenerate special case of the source rule. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 69125750}, panicked=False

### `FilterRejectsAllToEmpty` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_filter.rs, lines 43-236
- Attempts used: 4
- Last updated: 2026-10-01T02:50:23.021354+00:00
- Reason / notes: This faithfully encodes the source's RejectsAll→Empty branch restricted to the FALSE-literal sub-case, which the SCOPE line honestly and specifically declares; because a RejectsAll predicate must be a *known* always-false value (an uninterpreted symbol would let QED treat it as possibly-true and the rewrite would be invalid), using the concrete FALSE literal is the correct, non-arbitrary choice rather than an under-generalization. before() (Filter over the scan) and after() (empty Values carrying the identical row type) are structurally distinct, the input schema is preserved exactly as the source requires, and the single shared source symbol is the correct one, so the proof is a genuine non-vacuous bag-semantic equivalence (Filter(false)=empty bag), not a degenerate or over-constrained artifact. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 334167}, panicked=False

### `FlattenNestedUnions` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/optimize_unions.rs, lines 63-74
- Attempts used: 4
- Last updated: 2026-10-01T02:53:51.878727+00:00
- Reason / notes: before() (Union(A, Union(B, C), D)) and after() (Union(A, B, C, D)) are structurally distinct, so the proof is the genuine, non-vacuous claim that flattening a nested union-all preserves semantics — exactly the soundness property of DataFusion's `extract_plans_from_union` transform. `union(true, ...)` correctly models DataFusion's n-ary bag (UNION ALL) union (DataFusion has no set-union variant), all branches share the single uninterpreted type T as a union's schema requires, and A/B/C/D are independent scan symbols with no sharing, so the proof isn't an artifact of over-constraint. The fixed 3-input / nested-2-in-second-position shape is a real limitation of the pattern DSL (no inductive pattern variables exist to express arbitrary nesting depth/arity), it is honestly and specifically tagged `SCOPE: PARTIAL`, and the instance is non-degenerate, making this a faithful special case of the rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1790834}, panicked=False

### `FullJoinBothNullRejectingToInner` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_outer_join.rs, lines 102-244
- Attempts used: 22
- Last updated: 2026-10-01T03:08:27.483494+00:00
- Reason / notes: before() and after() genuinely differ (FULL vs INNER join under the same shared uninterpreted `on` condition and the same shared filter `p`), so the proof is non-vacuous — the equivalence holds precisely because `IS_NOT_NULL(l0) ∧ IS_NOT_NULL(r0)` kills the null-padded rows a FULL join emits beyond an INNER join, and a miswiring of the JoinField indices would have falsified it. Symbol usage is correct (distinct L/R scans, one `on` and one filter predicate consistently shared between both sides of the rewrite, matching DataFusion's `eliminate_outer` (Full, true, true)→Inner branch which preserves the predicate on top of the simplified join), and the PARTIAL scope tag is accurate and specific: only that one branch, filter restricted to one IS_NOT_NULL per single-column side, and no projection inlining — a genuine, non-degenerate special case of the source rule that is the natural provable core given QED cannot express "arbitrary null-rejecting predicate" as a side condition on an uninterpreted symbol.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 39460958}, panicked=False

### `FullJoinLeftNullRejectingToLeft` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_outer_join.rs, lines 102-244
- Attempts used: 6
- Last updated: 2026-10-01T03:03:21.575431+00:00
- Reason / notes: The encoding faithfully models the rule's named case — a null-rejecting filter (IS_NOT_NULL on the left column, whose type is nullable so the filter is non-trivial) kept *above* the join while the join kind flips FULL→LEFT with the ON condition preserved as an uninterpreted symbol, exactly matching `eliminate_outer`'s `(Full, left_non_nullable=true, right_non_nullable=false)→Left` branch and the source's keep-the-filter-in-place (no pushdown/removal) behavior. `before()` and `after()` genuinely differ (FULL vs LEFT) so the proof is non-vacuous, and the two independent scans plus the uninterpreted join condition avoid any spurious over-constraint that could mask an unsound rewrite. The restriction to `IS_NOT_NULL` and to this single join-kind case is a genuine, specific, non-degenerate special case that is provable in QED (a general null-rejecting predicate would need predicate-entailment reasoning QED cannot perform), and it is honestly tagged `SCOPE: PARTIAL`.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 55844334}, panicked=False

### `FullJoinRightNullRejectingToRight` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_outer_join.rs, lines 102-244
- Attempts used: 5
- Last updated: 2026-10-01T03:11:45.494137+00:00
- Reason / notes: The encoding faithfully captures the named branch (DataFusion's `eliminate_outer` case (Full,false,true) → Right): `before()` and `after()` differ only in the join kind (FULL vs. RIGHT), with the ON condition (`on`) and the null-rejecting filter (`IS_NOT_NULL` on the right column, field 1) correctly shared across both sides, matching how the source copies `on`/`filter` and re-wraps the same predicate above the rebuilt join. The two restrictions — a concrete `IS_NOT_NULL` rather than an arbitrary null-rejecting predicate, and no intervening projections — are genuine QED limitations (it cannot reason about an uninterpreted predicate's null-rejection/entailment or track nullness through uninterpreted projection expressions), not missing DSL capabilities, and are honestly labeled in the SCOPE line. The result is non-degenerate and semantically correct (the FULL join's null-extended left-only rows are exactly those killed by `IS_NOT_NULL(r)`, so the bag over FULL-filtered equals the bag over RIGHT-filtered), so the proof reflects a real, meaningful instance of the rule.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 50988292}, panicked=False

### `InListDedup` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1850-1872
- Attempts used: 21
- Last updated: 2026-10-01T03:19:53.739879+00:00
- Reason / notes: The before/after patterns are structurally different (a 4-way OR with one repeated disjunct vs. the deduplicated 3-way OR), so the proof is non-vacuous, and the symbol sharing is exactly right: reusing `p2` models the same list element occurring in both IN lists while `p1`/`p3` stay independent, with no concrete predicates baked in and no missing preconditions (the rule's non-negated, same-column requirements are respected by the encoding, and ∨-idempotency holds even under null/3-valued semantics). Since IN is definitionally a disjunction of column-equalities, expanding the source rule for two lists sharing one element yields precisely `P∨Q∨Q∨S ≡ P∨Q∨S` — so the proven statement is the rule's exact semantic core at minimal arity, over a superset of disjunct kinds (arbitrary uninterpreted predicates, not just equalities). The stated PARTIAL scope is therefore honest and specific (the DSL genuinely lacks IN nodes and value literals, and fixed arity is inherent to concrete patterns), and the special case is non-degenerate.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 362500}, panicked=False

### `IsUnknownToIsNull` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1808-1811
- Attempts used: 29
- Last updated: 2026-10-01T03:44:22.473033+00:00
- Reason / notes: The encoding is faithful and non-trivial: it correctly models the side condition by declaring the scan column non-nullable (`varType("X_Type", false)`), uses `SqlStdOperatorTable.IS_UNKNOWN` (matching DataFusion's `IsUnknown` semantics — true iff the operand is NULL/unknown), and proves `σ_{IS_UNKNOWN(x)}(R) = σ_{false}(R)` by relying on QED's nullability constraint. The partial scope (omitting the `IsNull` branch) is explicitly and honestly disclosed in the SCOPE comment, and the proved equivalence is genuinely non-vacuous — it requires the non-nullability guarantee, not just structural identity.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 284041}, panicked=False

### `LeftJoinNullRejectingToInner` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_outer_join.rs, lines 102-244
- Attempts used: 6
- Last updated: 2026-10-01T03:22:47.635349+00:00
- Reason / notes: The encoding faithfully captures the Left→Inner branch of `eliminate_outer` with the filter remaining above the join (matching the source's `Filter::try_new(filter.predicate, rebuilt_inner)`), uses uninterpreted symbols for tables and the ON predicate, and correctly references the right-side column via `joinField(1, right)`; the only narrowing is the restriction of the null-rejecting predicate to explicit `IS_NOT_NULL` (rather than any null-rejecting expression) and omission of the projection-inlining layer, both of which are honestly tagged in the SCOPE line and do not make the proof vacuous since LEFT and INNER joins genuinely differ in NULL-extension behavior.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 36510333}, panicked=False

### `LiteralIsNullFold` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1801-1804
- Attempts used: 5
- Last updated: 2026-10-01T03:35:42.835234+00:00
- Reason / notes: The encoding is faithful and non-vacuous: `before()` is `Filter(x IS NOT NULL, S)` vs `after()` = `Filter(true, S)` (structurally distinct conditions), and the critical side condition `!info.nullable(&expr)` is genuinely encoded via a non-nullable column type that JSONSerializer forwards to QED, so the proof rests on the value actually being non-nullable rather than ignoring the precondition. Modeling the non-nullable value as a universally-quantified scan column and proving only `IS NOT NULL` (which is semantically identical to `IS NOT UNKNOWN`) captures the rule's full logical content, so `SCOPE: FULL` is honest.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 256333}, panicked=False

### `MergeConsecutiveFilters` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 799-864
- Attempts used: 6
- Last updated: 2026-10-01T03:40:25.445295+00:00
- Reason / notes: The encoding matches the source rule's core merge branch exactly — `Filter(Q, Filter(P, R))` → `Filter(P ∧ Q, R)` with the child's conjunct first — using two independent uninterpreted predicates (`inner`, `outer`) over a single shared uninterpreted base relation, which is precisely the symbol structure the real rewrite requires, and `before()` (nested double filter) is genuinely different from `after()` (single filter with `AND`), so the proof is not vacuous. No preconditions are silently dropped: the Limit/Offset guard cannot block this branch (a Filter child has no fetch/skip), predicate deduplication is a semantic no-op under universal quantification, and the one-column scan is the DSL's standard stand-in for an arbitrary base bag that does not weaken the filter-composition claim. The `SCOPE: PARTIAL` tag is honest and specific — it correctly names the excluded conjunct-simplification/cost-reordering steps, recursive application, and the join-pushdown branch that QED cannot express over uninterpreted predicates.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 69404250}, panicked=False

### `NegateComparisonOperator` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 314-317
- Attempts used: 42
- Last updated: 2026-10-01T04:41:56.144710+00:00
- Reason / notes: The encoding is a faithful, non-degenerate slice: before() = Filter(NOT(x > y)) and after() = Filter(x <= y) over the cross product of two arbitrary same-type scans, so x and y remain universally quantified values, and the concrete GREATER_THAN/LESS_THAN_OR_EQUAL operators are exactly what must be concrete — QED cannot relate independent uninterpreted predicates, so an uninterpreted-operand version of this rule would be unprovable by design rather than by over-restriction. The carrier shape (INNER join with a TRUE condition, i.e. a pure Cartesian product) imposes no structural assumption on the source plan, symbol sharing is correct (two distinct fields of the same orderable type, matching a comparable operand pair), and the inequality flip NOT(x > y) ⟺ x <= y holds even under SQL three-valued logic, so no NOT NULL or total-order precondition is silently missing. The PARTIAL scope line is accurate and specific — only the Gt→Le pair of op.negate() is encoded, with the other comparison pairs and De Morgan/double-negation/in-list/between branches explicitly excluded — and the two sides are structurally and semantically distinct, so the proof verifies the genuine rewrite rather than a vacuous identity.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 338875}, panicked=False

### `NotBetweenToOutsideRange` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 352-359
- Attempts used: 102
- Last updated: 2026-10-01T07:00:46.826874+00:00
- Reason / notes: The encoding is a faithful expansion of the source rule: `before()` is `NOT(A>=B AND A<=C)` (the standard expansion of `NOT (A BETWEEN B AND C)`, matching the rule's left side) and `after()` is `A<B OR A>C` (the standard outside-range expansion of `A NOT BETWEEN B AND C`, matching the rule's right side) — a non-trivial, structurally-distinct pair. Because the DSL has no BETWEEN/NOT-BETWEEN node, expanding to comparison operators is the correct approach, the strict-vs-inclusive boundaries are right (BETWEEN inclusive, NOT-BETWEEN strict), and A, B, C are three independent columns of the same comparable virtual type, so the proof universally quantifies over all their values with no symbol-sharing or missing-precondition issues.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 441792}, panicked=False

### `NotEqBoolLiteralToOperand` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 921-934
- Attempts used: 8
- Last updated: 2026-10-01T04:38:45.062071+00:00
- Reason / notes: The encoding faithfully captures the `true != A → !A` arm: `before()` uses `NOT_EQUALS(true_literal, A)` and `after()` uses `Not(A)`, which are structurally distinct yet semantically equivalent (including the NULL case), so the SMT proof is non-vacuous. The single uninterpreted predicate `A` over a minimal single-column scan is the right level of generality, and the `SCOPE: PARTIAL` tag honestly and specifically acknowledges that the `false != A → A` and `null != A → null` arms have different rewrite targets that a single before/after pair cannot express — a genuine DSL limitation, not an encoding shortcut. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 320958}, panicked=False

### `NotInToNotIn` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 345-351
- Attempts used: 6
- Last updated: 2026-10-01T04:58:38.515947+00:00
- Reason / notes: `before()` (NOT(OR(a=b, a=c))) and `after()` (AND(¬(a=b), ¬(a=c))) are structurally distinct, so the proof is a genuine De Morgan equivalence — exactly the semantic content of DataFusion's `negated`-flag flip, since an unnegated InList expands to a disjunction of equalities and a negated one to a conjunction of negated equalities, and the proven equivalence is symmetric, covering both directions (`not (A in)` → `A not in` and its mirror) in the source. Modeling A/B/C as three columns of one uninterpreted scan faithfully represents three independent arbitrary values of a shared type, and using concrete `EQUALS` is correct because equality is definitional to InList membership (not an incidental predicate to be uninterpreted); De Morgan also holds under three-valued null logic, so no null/NOT NULL precondition is silently assumed, and no precondition exists in the source rule. The single restriction — a two-element list — is honestly declared in the SCOPE line, is a [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 337833}, panicked=False

### `NotIsNullToIsNotNull` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 341-344
- Attempts used: 4
- Last updated: 2026-10-01T04:47:51.978138+00:00
- Reason / notes: The encoding matches the named rule exactly — Filter(R, NOT(R.f IS NULL)) vs. Filter(R, R.f IS NOT NULL) over a fully uninterpreted scan with a nullable VarType — so before() and after() are structurally distinct, and the proven identity is the genuine SQL null-logic duality (NOT∘IS_NULL ≡ IS_NOT_NULL, neither side ever NULL), not a vacuity or coincidence of shared symbols. The sibling arm in the source block (NOT(A IS NOT NULL) ⟹ A IS NULL) is a separate symmetric rule that would be its own RRule, not an assumption inside this one, so the FULL tag is honest for the rule as named; nullability is modeled rather than away, which is what makes the claim non-trivial.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 636834}, panicked=False

### `NullAndOrToNull` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 860-865
- Attempts used: 9
- Last updated: 2026-10-01T05:07:37.899796+00:00
- Reason / notes: The encoding faithfully captures the AND branch of the source rule: `before()` is `Filter(Source, AND(NULL, NULL))` and `after()` is `Filter(Source, NULL)`, which are structurally distinct (an AND node vs. a bare literal), correctly use a boolean-typed null literal matching the original's `is_null`/`lit_bool_null()` semantics, and wrap the expression in a filter — a valid context for verifying boolean-expression equivalence. The SCOPE line honestly and specifically notes the only limitation (OR branch omitted), which is genuine since the DSL has no pattern-disjunction mechanism to match "AND or OR" in a single before/after pair. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 416917}, panicked=False

### `OptimizeProjections` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/optimize_projections/mod.rs, lines 1-899
- Attempts used: 21
- Last updated: 2026-10-01T06:30:59.010356+00:00
- Reason / notes: `before()` (two stacked projections, the outer dropping column 2) and `after()` (one projection) are structurally distinct, and their difference is exactly the elimination of the dead `FC` computation — the rule's core "eliminate unused columns / merge consecutive projections" behavior — so the proof is non-vacuous, with symbols correctly shared (same `exprA`/`exprB`/`exprC` instances in both sides, distinct `FA`/`FB`/`FC` operators and scan) and no hidden preconditions (projection merge is unconditionally valid in bag semantics; all types nullable, scan non-unique). The fixed 3-column/two-layer shape is an inherent limit of expressing a recursive plan rule as a finite DSL pattern, is honestly and specifically tagged `PARTIAL`, and captures a genuine, non-degenerate fragment of the real rewrite (the same shape DataFusion's `merge_consecutive_projections` folds).
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 334125}, panicked=False

### `OrAndAbsorption` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1006-1011
- Attempts used: 7
- Last updated: 2026-10-01T06:35:43.355414+00:00
- Reason / notes: `before()` and `after()` are structurally different filters over the same scan whose conditions are `A OR (A AND B)` vs `A`, with A and B as uninterpreted predicates — exactly DataFusion's pattern, and the source guard `is_op_with(And, &right, &left)` (the And must contain the Or's left child as one of its operands) is captured precisely by reusing the symbol A in both positions, while B stays independent. The absorption law is a Boolean tautology that holds pointwise even under 3-valued/null semantics, so no preconditions are missing, and there is no over-constraining: QED proving it confirms the genuine identity, not an artifact of accidental sharing. Embedding the scalar simplification in a filter over a scan is the DSL's standard full expression of this rule (the `B AND A` mirror form is the same semantic claim since AND is commutative), so `SCOPE: FULL` is honest and the proof is non-vacuous. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 65911250}, panicked=False

### `OrCommonFactorDistribution` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1018-1039
- Attempts used: 5
- Last updated: 2026-10-01T06:38:57.601126+00:00
- Reason / notes: The encoding is a faithful, non-vacuous instance of the rule: `before()` is `Filter((A∧B)∨(A∧C))` and `after()` is `Filter(A∧(B∨C))` over the same scan with A, B, C as genuinely independent uninterpreted predicates, and reusing symbol `A` across both disjuncts correctly mirrors the source rule's requirement (via `lhs.contains(e)`) that the factored expression be literally identical on both sides. The equivalence `(A∧B)∨(A∧C) = A∧(B∨C)` holds under both two- and three-valued logic and needs no extra preconditions, so the proven claim matches the real rewrite. The scope line is honest and specific: the general DataFusion rule handles arbitrary numbers of conjunctions and common factors, but the DSL can only build fixed-arity patterns, so the 2+2 conjunctions / 1 common factor / 1 residual per side case is a genuine, non-degenerate, textbook-minimal special case that is correctly declared as PARTIAL rather than overclaimed as FULL. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 70148791}, panicked=False

### `OrFalseIdentity` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 960-965
- Attempts used: 8
- Last updated: 2026-10-01T06:40:23.754262+00:00
- Reason / notes: The encoding differs from `after()` in exactly the intended way — `Filter(OR(falseLiteral, P), source)` vs `Filter(P, source)` — where `P` is a genuinely uninterpreted predicate symbol and `source` is an uninterpreted scan, so the proof covers the full family `false OR A → A` rather than a hard-coded instance. The literal-false left operand and two-argument OR match the DataFusion guard `is_false(&left)` exactly, and no precondition (uniqueness, NOT NULL, etc.) is silently assumed — the disjunction identity even holds pointwise under three-valued null semantics, and the single-column scan is a neutral carrier rather than a restriction, since the boolean identity is independent of the source's shape. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 386458}, panicked=False

### `OrSelfIdempotent` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 994-999
- Attempts used: 12
- Last updated: 2026-10-01T06:49:58.229600+00:00
- Reason / notes: The before/after conditions genuinely differ (`(A OR B) OR A` vs `A OR B` over the same scan), and reusing the single `A` symbol for both occurrences correctly mirrors the source rule's precondition that the right operand be the same expression occurring inside the left OR-chain, so the proof is non-vacuous with correct symbol sharing. `A` and `B` are fully uninterpreted predicates over the shared source row — no concrete predicate, join kind, or precondition was baked in — and since the proven absorption identity `(A∨B)∨A ≡ A∨B` holds for all instantiations of `A`, `B`, it also covers arbitrary longer OR chains (any OR-chain containing A is equivalent to `A∨B` for some `B`), making the `SCOPE: PARTIAL` tag conservative rather than restrictive: the encoding is semantically a full, faithful generalization of the rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 338125}, panicked=False

### `OrTrueAbsorption` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 966-971
- Attempts used: 4
- Last updated: 2026-10-01T06:52:57.736009+00:00
- Reason / notes: `before()` (`Filter(Or(<uninterpreted pred>, trueLiteral), S)`) and `after()` (`Filter(trueLiteral), S`) are structurally distinct, so the proof is non-vacuous; the arbitrary expression `A` of the source rule is modeled by a single fully-general uninterpreted predicate over a nullable column (no NOT-NULL or PK assumption baked in, matching the "even if A is null" case), and the right side is correctly fixed to the `true` literal per `is_true(&right)`, with the filter carrier being the standard faithful embedding of this scalar law in a relational prover. SCOPE: FULL is honest — nothing in the source rule's generality (any `A`, null included) is narrowed by the encoding.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 278250}, panicked=False

### `PassthroughEmptyRelation` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/propagate_empty_relation.rs, lines 62-74
- Attempts used: 5
- Last updated: 2026-10-01T06:59:47.312029+00:00
- Reason / notes: before() = Filter(pred, Empty(child)) and after() = Empty(child) are structurally distinct, and the proved claim — for every uninterpreted predicate over an arbitrary uninterpreted row type, filtering a zero-row bag yields that same zero-row bag — is precisely the semantic content of the source rule's Filter arm, where a filter's output schema equals its input's, so the replacement empty correctly carries the child's row type. The zero-row precondition (produce_one_row = false) is baked into the pattern's empty() shape, and nothing concrete (predicate, type, join kind) is hard-coded or coincidentally shared, so the proof is neither vacuous nor over-constrained. The PARTIAL scope is honestly disclosed: the encoding pins one representative member of the 7-node family (Window/Sort/Limit have no bag semantics in QED, and SubqueryAlias/Repartition are identity aliases with no DSL shape), a genuine, specific, non-degenerate narrowing. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 1364417}, panicked=False

### `PushDistinctAllThroughUnion` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/optimize_unions.rs, lines 75-96
- Attempts used: 5
- Last updated: 2026-10-01T07:13:48.846670+00:00
- Reason / notes: The encoding faithfully mirrors the rule for its stated scope: DISTINCT is correctly modeled as a group-by-with-no-aggregate-calls (exact for the single-column relations used), DataFusion's `Union` is correctly represented as UNION ALL (`all=true`), and A/B are two independent uninterpreted scans sharing type T, so the proof really quantifies over all such instantiations rather than baked-in values. `before()` (inner `Distinct`s present) is structurally distinct from `after()` (inner `Distinct`s removed) and the equality is genuinely non-vacuous, so the proof captures the actual push-through transformation. The only narrowing (exactly two inputs, one column) is honestly disclosed in the SCOPE line, is specific and non-degenerate, and does not make the result misleading.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 72069667}, panicked=False

### `PushDownLeafProjections` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/extract_leaf_expressions.rs, lines 745-1413
- Attempts used: 45
- Last updated: 2026-10-01T07:46:14.489290+00:00
- Reason / notes: The encoding is a genuine, non-vacuous instance of the rule's core filter fragment — `Project(F(a),a) over Filter(P(a),Scan)` vs `Filter(P(a),Project(F(a),a)) over Scan` — where F and P are uninterpreted operators correctly shared across both sides (the predicate is the same symbol applied to the same pass-through value via the projection's column 1, which is exactly the side condition under which DataFusion's name-resolution-based push is valid), and no preconditions are silently dropped (no key/NOT NULL assumptions, nullable VarTypes, plain bag semantics; the volatile/merge/Unnest guards in the source all belong to cases this fragment doesn't touch). The SCOPE line honestly declares this as a PARTIAL special case — one extracted expression, one pass-through column, filter-only, with the full rule's Sort/Limit/Join routing, projection merging, recovery-projection machinery, and filters-over-computed-aliases explicitly out of scope — so the provable result is faithful and not misleading.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 65151000}, panicked=False

### `PushFilterIntoAggregate` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 1012-1067
- Attempts used: 6
- Last updated: 2026-10-01T07:25:58.537550+00:00
- Reason / notes: `before()` (Filter over Aggregate) and `after()` (Aggregate over Filter) are genuinely different plan shapes, so the proof is non-vacuous, and the single uninterpreted predicate "p" is correctly shared on the same group-key column across both sides — exactly mirroring the rule moving a key-only predicate through the aggregate, with the key column properly linked via the group set. The bag semantics genuinely match (for a key-dependent filter, groups are dropped/kept identically on both sides), so no missing precondition changes the claim. The SCOPE line honestly documents the real narrowing (one plain-column group key, one non-distinct call, whole predicate key-dependent) rather than vague hand-waving, and this remains a useful, non-degenerate special case of the core rewrite.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 92967833}, panicked=False

### `PushFilterIntoAsOfJoin` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 1156-1223
- Attempts used: 46
- Last updated: 2026-10-01T08:14:26.255758+00:00
- Reason / notes: The encoding faithfully models the rule's core by splitting the filter into an uninterpreted left-only conjunct `f` (pushed into the left input) and a remainder `g` kept above the join, with correct symbol sharing (`f` applied to the identical left columns on both sides, unchanged `on`, and genuinely distinct before/after plans, so the proof is non-vacuous). Modeling ASOF as a LEFT JOIN is sound because the pushdown's validity rests solely on left-row preservation with unchanged left values plus NULL-padded right columns — properties LEFT JOIN satisfies and which QED verifies; a true ASOF "best-match" operator can't be added via the DSL since QED cannot model that ordering/list semantics. The omitted right-side key-equality mirror is a genuine QED entailment limitation (matched pairs share key values), correctly excluded and honestly tagged PARTIAL, so the provable result is a meaningful [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 86752083}, panicked=False

### `PushFilterIntoDistinct` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 870-884
- Attempts used: 9
- Last updated: 2026-10-01T07:55:29.251780+00:00
- Reason / notes: `before()` (Filter(p, dedup(R))) and `after()` (dedup(Filter(p, R))) are structurally distinct plans sharing the same single scan and the same uninterpreted predicate "p" applied to the same two columns, so the proof establishes the genuine filter/dedup commutation that is the Distinct::All branch of the source rule, not a vacuous identity. The claimed PARTIAL scope is honest and specific: the fixed two-column width is an unavoidable concretization (the DSL offers no uninterpreted schema-width symbol, so any fixed width is equally general and 2 is non-degenerate), and the Distinct::On branch is genuinely inexpressible because its "first row among duplicates" selection requires ordering/arbitrary-choice semantics that QED's bag-semantics model does not have.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 61737583}, panicked=False

### `PushFilterIntoExtension` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 1302-1368
- Attempts used: 23
- Last updated: 2026-10-01T08:22:40.878879+00:00
- Reason / notes: The encoding is non-vacuous and faithful: before() = π·σ_{P(A)∧Q(A,F(B,t))}(S ⋈_J T) and after() = π·σ_{Q(A,F(B,t))}(σ_{P(A)} S ⋈_J T) are structurally different plans whose bag-equivalence is a genuine commutation (the pushed conjunct P references only the pass-through column A, while the kept conjunct Q references the derived prevent column F(B,t) and stays above in both), and it holds for every instantiation of the uninterpreted symbols P, Q, F, J, S, T. The extension's pass-through contract (non-prevent column A preserved unchanged, prevent column arbitrarily derived, rows produced via an arbitrary inner-join relation with a witness) is exactly the precondition under which DataFusion's rule is sound, and the required symbol sharing (same P/Q/F/J objects on both sides via the static fields) is correct rather than coincidentally over-constraining. The narrowing (single input, 2-column schema, exactly one pushed + one kept conjunct) is a genuine, specific, non-degenerate special case and is honestly recorded in the SCOPE line. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 21860500}, panicked=False

### `PushFilterIntoJoin` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 541-576
- Attempts used: 26
- Last updated: 2026-10-01T08:35:25.929069+00:00
- Reason / notes: The encoding is a faithful, non-vacuous capture of the exact sub-case it claims: a single filter conjunct over the left input's column (one uninterpreted `left_filter` shared by name above the join and on the left child, with both occurrences resolving to L's column) is moved from above an INNER join — whose condition is a shared uninterpreted `join_cond` — down to the left child, which is precisely the transformation DataFusion's `push_down_join` performs for its left-only classification, and the universally-proven claim is sound with no missing preconditions (no null-extension or key assumptions are involved for INNER join). `before()` and `after()` are structurally different plans (the filter's position is the whole point), so the proof is not vacuous, and the only hard-codings that narrow the rule (INNER-only join type, single conjunct, left-only columns — versus the source's multi-type, multi-category behavior) are fully disclosed in a specific, concrete `// SCOPE: PARTIAL` line rather than hidden, leaving the result an honest, useful, non-degenerate special case rather than a misleadingly "general" one.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 67874791}, panicked=False

### `PushFilterIntoProjection` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 908-920
- Attempts used: 8
- Last updated: 2026-10-01T08:39:34.348518+00:00
- Reason / notes: The encoding faithfully mirrors DataFusion's pushdown: `before` is `Filter(F(P1(c)) AND G(P2(c)), Project[P1,P2](Source))` and `after` is `Filter(G(P2(c)), Project[P1,P2](Filter(F(P1(c)), Source)))`, with P1/P2 (via `proj`) and F/G all uninterpreted symbols — no baked-in concrete predicates, correct Filter/Project nesting and column ordinals, and no vacuity since the structures genuinely differ (F applied below vs. above the projection), so QED had to prove the non-trivial identity that F(P1(c)) over the raw source equals F(P1(c)) over the projected column, i.e. the real pushdown step. It is a narrower, honestly-tagged special case of the general rule (fixed two-column projection over a single-column source, exactly two-conjunct filter with exactly one pushable conjunct, where the real rule handles arbitrary arities and any number/choice of pushable conjuncts via `rewrite_projection`), but the restriction is specific rather than vague and the rule remains non-degenerate, and no missing preconditions exist (QED's uninterpreted functions are total, so no totality side-condition from the real rule's `ExprRewriter` is silently required).
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 69791583}, panicked=False

### `PushFilterIntoRepartition` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 865-869
- Attempts used: 6
- Last updated: 2026-10-01T08:32:23.911769+00:00
- Reason / notes: The encoding is faithful: Repartition's bag semantics is exactly a schema-preserving pass-through, so the identity projection is the correct (and only) bag-level model, the single uninterpreted predicate P is shared across both sides (matching the rule relocating the same filter), and the source rule is unconditional — no partitioning-strategy or schema preconditions are missing. before() = Filter(P, Repartition(Source)) vs after() = Repartition(Filter(P, Source)) is a genuine structural commutation (filter above vs below the pass-through), not a vacuous identity, over an arbitrary unconstrained multi-column source; fixing the arity at three columns with P over all of them is not restrictive since P is fully uninterpreted, so the SCOPE: FULL claim is accurate.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 357625}, panicked=False

### `PushFilterIntoSubqueryAlias` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 890-907
- Attempts used: 5
- Last updated: 2026-10-01T08:43:57.200277+00:00
- Reason / notes: The encoding is faithful: SubqueryAlias is correctly modeled as an identity projection (a pure schema relabel with no effect on row values/multiplicity), and the single uninterpreted predicate P shared on both sides correctly reflects the rule's by-name column rewrite, which changes reference names but not predicate semantics over row values. before() (Filter above the alias) and after() (Filter below it) are structurally distinct, so the proof is non-vacuous and matches the actual DataFusion transformation; P, the column types, and the source are all uninterpreted, and the source rule has no preconditions beyond plan well-formedness (the `replace_cols_by_name?` fallback only guards ill-formed plans), so SCOPE: FULL is honest — the only fixed aspect is the 3-column width, which the DSL cannot quantify over and which is content-neutral since the commutation argument is uniform in arity. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 372625}, panicked=False

### `PushFilterIntoUnionBranches` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 988-1011
- Attempts used: 5
- Last updated: 2026-10-01T08:47:47.861520+00:00
- Reason / notes: The encoding is non-vacuous (the filter moves from above the UNION ALL down into each branch, which is exactly DataFusion's rewrite) and the symbols are handled correctly: A and B are independent tables, the shared type T reflects the union's identical-branch-schema requirement rather than an extra assumption, `all=true` matches `LogicalPlan::Union` (UNION ALL), and reusing the uninterpreted predicate P across all three filter occurrences is precisely the right model of the source's per-branch column rename (positionally identity in this shape). It is narrower than the source — exactly two single-column branches instead of any arity and column count — but arity and column count are fixed-shape parameters that no single RuleScript pattern can quantify over, the SCOPE tag discloses the restriction specifically and accurately, and the two-branch case is the minimal non-degenerate instance, so the provable result faithfully covers a genuine PARTIAL instance of the real rule.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 522500}, panicked=False

### `PushFilterIntoUnnest` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 921-987
- Attempts used: 46
- Last updated: 2026-10-01T09:53:44.820483+00:00
- Reason / notes: The encoding faithfully captures the rule's essence: the non-unnest conjunct P_NON is applied only to the input column and the unnest conjunct P_UNNEST only to the element column, mirroring DataFusion's column-reference-based split, and modeling Unnest as an INNER join under an uninterpreted membership predicate M correctly preserves the key semantics (empty array yields no rows) while letting QED prove the pushdown uniformly for all M, E, and conjuncts. Before and after are structurally distinct (P_NON above vs. below the join), so the proof is non-vacuous, the shared operator instances and join-field ordinals are all consistent across both sides, no side condition of the source rule is silently dropped, and the INNER-join output retaining the array column is harmless since it is present symmetrically on both sides and the real unnest output is just a projection of it. The flagged PARTIAL scope (one array column, one conjunct per side, no struct columns) is an honest, genuine special-case restriction — the two-conjunct split case still exercises the actual optimization and generalizes by conjunction to more conjuncts. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 68859875}, panicked=False

### `PushFilterIntoWindow` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 1068-1139
- Attempts used: 5
- Last updated: 2026-10-01T08:56:54.704065+00:00
- Reason / notes: The encoding faithfully captures the rule's essence: the window is a join-back of the input to a partition-keyed uninterpreted aggregate (w), the pushed conjunct P is an uninterpreted predicate over *only* the partition column k (exactly DataFusion's "all referenced cols ∈ intersection of partition keys" precondition, making it constant per partition), and the kept conjunct Q is an uninterpreted predicate over the full window row; before() = σ_{P∧Q}(R ⋈ Agg(R)) and after() = σ_Q(σ_P R ⋈ Agg(σ_P R)) are genuinely different plans whose equivalence hinges precisely on P eliminating whole partitions (so the aggregate's input bag, and thus w, is unchanged for surviving rows), so the QED proof is non-trivial and meaningful rather than vacuous. The narrowing to a single window function, single partition column, two-column input, and non-null key is a legitimate special case, is honestly declared in the SCOPE line, and leaves a non-degenerate rewrite; symbol sharing (same P, Q, and w on both sides, equi-join on k) is correct.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 94608208}, panicked=False

### `RedundantDistinctElimination` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/replace_distinct_aggregate.rs, lines 89-119
- Attempts used: 6
- Last updated: 2026-10-01T09:32:44.809150+00:00
- Reason / notes: before() (the set-variant self-intersection of the input) and after() (the bare input) are structurally distinct, and the proven equality is exactly the rule's content — DISTINCT is a no-op when the input is already unique — with the uniqueness precondition present in the encoding as a GROUP BY over all columns (the source comment's prototypical Dependency::Single-over-all-fields case) rather than silently dropped. Modelling Distinct::All as R INTERSECT (set) R is the correct encoding, the symbol reuse across both intersect operands is the intentional self-intersection, and the SCOPE line honestly discloses the remaining narrowings (fixed two-column arity, which is inherent to the DSL's fixed-schema design, and exclusion of the zero-column LIMIT-1 branch, which is unmodelable in QED anyway). ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 64723958}, panicked=False

### `ReorderPredicatesByCost` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/reorder_predicates.rs, lines 1-133
- Attempts used: 5
- Last updated: 2026-10-01T09:50:46.850468+00:00
- Reason / notes: The encoding is non-vacuous and faithful to the rule's only bag-semantic core: `before()` and `after()` differ structurally (conjunct order swapped), and the two conjuncts are independent uninterpreted predicates (`cheap`/`expensive`, distinct symbols over the shared `Source` scan), so QED genuinely proved that reordering filter conjuncts preserves the relation for all instantiations — which is exactly what the DataFusion rule must guarantee, and is in fact stronger than the rule needs (it holds for arbitrary conjuncts, not just allow-listed cheap/expensive pairs). The narrowing to the two-conjunct swap is forced by the DSL (an `And` is a fixed-arity tree; there is no symbolic conjunct-list construct that a DSL extension could expose to the prover), is precisely the rule's minimal firing case, and the omitted volatile-check/allow-list guards are side-effect and cost-model matters with no content in QED's pure bag semantics. The `// SCOPE: PARTIAL` line is specific and accurate about both the arity restriction and the dropped guards, so the provable result is a genuine, non-degenerate special case, not a vacuous one.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 67480333}, panicked=False

### `RewriteComparisonViaUdfPreimage` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/udf_preimage.rs, lines 1-69
- Attempts used: 43
- Last updated: 2026-10-01T10:55:22.566417+00:00
- Reason / notes: The encoding faithfully captures the core semantic content of DataFusion's `rewrite_with_preimage` for the `=` case: under the step-function contract (which correctly encodes that the UDF outputs `val` iff its input falls in `[lower, upper)`), filtering on `fx = val` is equivalent to filtering on `e >= lower AND e < upper`. The before/after predicates are structurally distinct (one references the UDF output column, the other references the input column with range bounds), so the proof is non-vacuous. The SCOPE line honestly states that only `=` is encoded and that NULL-handling variants are excluded, which is a genuine and non-degenerate special case (the `=` case being the most semantically rich, producing a conjunction rather than a single comparison). The non-nullable `Int` type correctly handles the "x is not NULL" precondition from the source rule, and modeling the preimage contract as a shared filter side-condition is the standard and appropriate technique in this DSL for expressing a UDF's registered semantic property.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 72795167}, panicked=False

### `RightJoinNullRejectingToInner` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_outer_join.rs, lines 102-244
- Attempts used: 7
- Last updated: 2026-10-01T10:03:01.086456+00:00
- Reason / notes: The encoding is non-vacuous and semantically correct: `before()` and `after()` differ exactly in join kind (RIGHT vs INNER), the filter `IS_NOT_NULL` on field 0 is the left side of the join row (the null-extended side of a right join), and QED must genuinely reason about null-extended rows to prove the equivalence — so this is the real soundness lemma of the `eliminate_outer` branch `(Right, true, _) => Inner`. The predicate choice is forced rather than lazy: with an uninterpreted predicate the equivalence would be false in general (a predicate could admit padded rows) and QED cannot state the "null-rejecting on left" side condition, so instantiating the canonical `IS_NOT_NULL` is the correct way to capture the rule's core; the `on` condition and filter symbols are properly shared across both sides, `L`/`R` scans are independent, and no spurious preconditions (PK/NOT NULL) are assumed. The SCOPE: PARTIAL line is accurate and specific — only the Right→Inner branch, an explicit IS_NOT_NULL filter, and no projection inlining are modeled, which is a genuine, non-degenerate special case honestly declared as such.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 34932750}, panicked=False

### `SimplifyPredicatesViaBoundAnalysis` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/simplify_predicates.rs, lines 1-389
- Attempts used: 52
- Last updated: 2026-10-01T11:11:15.838168+00:00
- Reason / notes: before() (filter x>5 AND x>6) and after() (filter x>6) are structurally distinct, and their equivalence is non-vacuous — it requires the ordering fact 5<6, which QED genuinely discharged — so this is a real instance of the source rule's same-direction bound-reduction branch, matching exactly the x > 5 AND x > 6 → x > 6 example documented in the Rust source. Concrete literals are unavoidable here: making the bounds or comparison uninterpreted symbols would not generalize the rule but falsify it, since the rewrite's soundness rests on the actual ordering of the values. The SCOPE line honestly flags PARTIAL and names the omitted branches (empty-range-to-false, equality subsumption, !=-drop), the relational shape (Filter over a conjunction on one column, GREATER_THAN) matches the source, and no preconditions (keys, nullness) are missing — so the proof is of a genuine, non-degenerate special case, not of a vacuous or coincidentally over-constrained claim. ```
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 330375}, panicked=False

### `SingleDistinctToGroupBy` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/single_distinct_to_groupby.rs, lines 1-433
- Attempts used: 7
- Last updated: 2026-10-01T11:21:22.574964+00:00
- Reason / notes: The proof is non-vacuous and shape-faithful: before() is a single-level DISTINCT aggregate while after() reproduces exactly what the DataFusion rule emits in the all-distinct case (inner GROUP BY (g, x) with an empty aggregate list as pure deduplication, outer plain re-aggregation over the deduplicated column, trailing identity/alias project), and QED's per-group operand-bag check is precisely the semantic content of the rewrite. All semantic content (f1, f2, g, x, types) is left uninterpreted, the single shared distinct argument matches the source rule's `fields_set.len() == 1` precondition with f1/f2 kept independent, and the source rule imposes no uniqueness/NOT-NULL precondition that the encoding would be silently dropping. The PARTIAL scope tag is honest and specific — excluding non-distinct sum/min/max/count-rollup aggregates is forced by QED (two-phase rollup needs algebra of concrete functions QED does not model), and the remaining restrictions (exactly two distinct aggregates, one group key) are disclosed pattern arities rather than semantic hard-coding, leaving a genuine, non-degenerate special case of the rule's core transformation.
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 114602625}, panicked=False

### `UnionsToFilter` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/unions_to_filter.rs, lines 1-381
- Attempts used: 4
- Last updated: 2026-10-01T11:34:28.941109+00:00
- Reason / notes: The encoding is non-vacuous (UNION ALL of two filtered branches vs. a single OR-filtered branch) and semantically faithful to DataFusion's plan shape: `LogicalPlan::Union` is a bag union with `Distinct::All` on top, correctly encoded as `union(true, ...)` wrapped in an all-columns group-by with no aggregate calls (the standard dedup encoding; if QED's group-by did not deduplicate, the equivalence would be refuted by any row satisfying both predicates, so the PROVABLE result confirms dedup semantics is in effect). Symbol sharing is correct: both branches reuse the same scan `S` exactly as the rule's `GroupKey` identity requirement demands, while `P1`/`P2` are independent uninterpreted predicates, and DataFusion's volatility/`is_repeatable` preconditions are vacuous under QED's deterministic uninterpreted-symbol model, so no precondition is silently missing. The `SCOPE: PARTIAL` line is present, one sentence, and precisely names the genuine restrictions (exactly two branches, one-column source, both branches filtered, no projection/alias wrappers), so the under-generalization is honestly declared and the proved lemma — DISTINCT(F_{P1}(S) ⊎ F_{P2}(S)) = DISTINCT(F_{P1∨P2}(S)) — is still a real, non-degenerate core of the rule. ```
- QED stats: complete_fragment=False, total_duration={'secs': 0, 'nanos': 67525541}, panicked=False

### `UnwrapSingletonUnion` — ✅ PROVED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/optimize_unions.rs, lines 60-62
- Attempts used: 5
- Last updated: 2026-10-01T11:36:14.830977+00:00
- Reason / notes: The encoding is faithful and non-trivial: `before()` is a `Union(all=true)` with exactly one source and `after()` is that same source, precisely mirroring the `inputs.len() == 1` guard, and DataFusion's `Union` node is plain concatenation (UNION ALL; distinct `UNION` is built as a separate Distinct/Aggregate above it), so `union(true)` is the correct bag-semantic operator. The single uninterpreted scan "Input" is the right stand-in for the arbitrary input plan — the rule rewrites only the Union node and passes the input through untouched, so the proof over all instantiations of the uninterpreted relation covers the full rule with no structural generality lost. There are no hidden preconditions in the source rule (no key/NOT NULL assumptions), and the shared symbol is the single, necessary relation on both sides, so SCOPE: FULL is honest.
- QED stats: complete_fragment=True, total_duration={'secs': 0, 'nanos': 280292}, panicked=False

### `BitwiseAndByZero` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1229-1235
- Attempts used: 22
- Last updated: 2026-09-30T23:19:46.493880+00:00
- Reason / notes: The rule's only non-trivial content is the operator-specific numeric law `A & 0 = 0`, but RuleScript can introduce `&` solely as an uninterpreted scalar symbol — the core language has no integer literals or interpreted bit-arithmetic, so the zero operand and its zero-ness cannot be expressed in any more constrained way — and QED's fixed theory over such symbols (equality, uninterpreted functions, ite, ordering, with arithmetic reserved for bag multiplicity) entails no axiom of the form `f(x, c) = c` for an arbitrary constant symbol `c`, so the identity is not valid under all instantiations no matter how the encoding is shaped. This is a genuine limitation of the unmodifiable prover's theory, not a missing DSL builder, so `extend_dsl_file` cannot close the gap and the porter's empirically confirmed `provable=false` is the predicted outcome.

### `BitwiseOrByZero` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1303-1308
- Attempts used: 21
- Last updated: 2026-09-30T23:21:11.663446+00:00
- Reason / notes: The rule's entire content is the semantic identity x | 0 = x of the specific bitwise-or operation, but RuleScript can only introduce `|` as an uninterpreted scalar symbol, and QED's fixed oracle theory (equality/uninterpreted functions, total order, natural-number addition, ite) contains no bitwise-arithmetic axioms; since a rule is just a before/after pattern pair with no assumption mechanism and `extend_dsl_file` cannot alter the unmodifiable Rust prover, no encoding can supply that identity — and bitwise OR is not even definable from the available addition/order arithmetic, so no faithful surrogate encoding exists. A real `try_rule` attempt would therefore only return not-provable (the solver can countermodel `|` as any function, e.g. a constant one), so the porter's UNSUPPORTED conclusion, though reached without a live attempt, is correct.

### `BitwiseShiftByZeroNoop` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1452-1457
- Attempts used: 21
- Last updated: 2026-09-30T23:25:30.742686+00:00
- Reason / notes: The rule's soundness rests on the algebraic identity x >> 0 = x, but in QED the shift operator can only appear as an uninterpreted scalar function, and the prover's fixed oracle theory (equality + UF, total order, addition, ite) contains no shift or zero-identity axiom, so no instance of an uninterpreted f satisfies f(x, 0) = x — indirect encodings (modeling the literal 0 as a constant/values table, key or "guaranteed" constraints on it) get at most congruence (f(x, c) = f(x, 0)), never the identity itself. This is a genuine limitation of the immutable Rust prover, not a missing DSL capability that extend_dsl_file could close: even a first-class shift builder would need the prover to know its axiom, which it doesn't. Any encoding that made the proof go through would have to replace the shift with an identity or ite-based surrogate, which would no longer be a faithful port of DataFusion's rule — exactly the "operator whose specific internal semantics QED cannot see through as an uninterpreted function" limitation in the reference.

### `BitwiseXorByZero` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1376-1381
- Attempts used: 24
- Last updated: 2026-09-30T23:28:54.392042+00:00
- Reason / notes: The rule requires the bitwise-Xor identity `x ^ 0 = x`, but RuleScript can only introduce `^` as an uninterpreted projection/predicate and QED has no prover-level axioms relating that uninterpreted operator to a zero constant. Adding a Java DSL builder could expose the shape, but it cannot add the missing operator theory, so the equivalence is not valid under arbitrary SMT interpretations.

### `CapSortFetchWithLimit` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_limit.rs, lines 161-185
- Attempts used: 7
- Last updated: 2026-09-30T23:26:26.543368+00:00
- Reason / notes: QED works in pure bag semantics and, per qed.pdf §6.2, explicitly has no model for the ordering semantics of Sort/Limit/Offset ("does not support queries that make use of the ordering semantics of Limit, Offset, or Order By"), yet this rule's entire content is row-position semantics: Sort's fetch capping the number of rows emitted (top-K), Limit(0, n) being redundant once its input is bounded to ≤ n rows, and the min-algebra on the fetch arguments — none of which are properties of the bag of rows. Under QED's uninterpreted treatment, the before/after sides differ only in the structure and fetch arguments of those uninterpreted higher-order operators over the same input, so the SMT solver has no algebraic relation to bridge and no narrower special case (e.g. skip=0 with an unchanged fetch cap) removes the dependency on the missing ordering semantics. Note the DSL also lacks a Sort/Limit builder, but that is not the blocker — extending `RelRN` could at best expose operators the prover itself deliberately gives no bag-semantic meaning to, so the UNSUPPORTED conclusion is correct. ```

### `CaseBranchAlwaysFalseElided` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1525-1562
- Attempts used: 8
- Last updated: 2026-09-30T23:36:53.199340+00:00
- Reason / notes: The rule is pure scalar CASE algebra whose correctness rests entirely on CASE's branch-selection semantics — a value-level ite (first-TRUE branch wins; false/NULL conditions fall through to the next branch/ELSE) — and QED's interpreted fragment has no ite: I independently verified `RexRN` offers only And/Or/Not/boolean literals plus uninterpreted `pred`/`proj` symbols, and `JSONSerializer` passes any scalar operator to the fixed Rust prover as a bare name that unmodeled operators receive uninterpreted treatment for, so a CASE minted via `extend_dsl_file` could never be universally equated with its then/else/NULL forms (e.g. `case(false,a,b) ≡ b` fails for an arbitrary function symbol). No relational reconstruction is faithful either: a Filter/Union re-encoding of branch selection drops NULL-condition rows that CASE would send to ELSE (unsound for the non-literal branches the rule preserves), and for the constant-condition branches the only interpretable "encoding" collapses to the already-rewritten relation, making the proof vacuous — the before side of every instance of the rule contains a CASE that nothing in the fragment can name, so no provable special case exists. The gap is therefore in the prover's fixed operator interpretations, which are off-limits to modify (extend_dsl_file only touches the Java builder/serializer layer), so the porter's UNSUPPORTED verdict is correct.

### `CaseFirstBranchAlwaysTrue` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1525-1562
- Attempts used: 25
- Last updated: 2026-09-30T23:39:10.975705+00:00
- Reason / notes: This rule's correctness rests entirely on the CASE operator's 3-valued branch-selection semantics (e.g. `case(TRUE, a, b) ≡ a`, dropping `WHEN false` branches), and QED's interpreted fragment contains no CASE/ite — any CASE encoding serializes to an uninterpreted scalar call, for which the equality is not valid under universal instantiation, so no SMT proof exists. Every instance of the rule, including the narrowest special cases (`CASE WHEN true THEN a END → a`, `CASE WHEN false THEN a ELSE b END → b`), necessarily contains CASE on the before side, so no special case escapes the uninterpreted-operator gap. This is a fixed-prover semantics limitation (the interpretation is keyed by operator name inside the Rust prover), which `extend_dsl_file` cannot address since it only touches the Java builders/serializer. ```

### `CaseNoBranchesTrueToElse` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1525-1562
- Attempts used: 9
- Last updated: 2026-09-30T23:40:15.264585+00:00
- Reason / notes: The rule's core claims (CASE WHEN true THEN A ELSE B → A, and dropping false branches) require an interpreted value-level ite/CASE operator, but RuleScript's surface language (RexRN) only exposes And/Or/Not/bool-literals as interpreted scalar ops and emits any CASE as a named RexCall that the fixed Rust prover treats as an uninterpreted function it quantifies universally over — so case(true,A,B)=A is not entailed for arbitrary instantiations. No builder can be added via extend_dsl_file to make the prover interpret CASE (the prover is the unchangeable arbiter and only special-cases the boolean connectives, with ite being internal to their 3-valued-logic encoding, not a query-fragment operator), and a relational re-encoding (union of filtered projections) would bake the CASE semantics in by hand, proving a tautology rather than the rule. This is a genuine QED limitation of the "uninterpreted operator whose internal semantics the prover cannot see through" class, consistent with the two prior AGREE verdicts.

### `CastLiteralFold` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 534-680
- Attempts used: 24
- Last updated: 2026-09-30T23:56:30.561349+00:00
- Reason / notes: The rule's essential content is cast value algebra — QED models any cast as an uninterpreted function (qed.pdf §6.2) with no cast identities, and since all virtual types flatten to integers and the only interpreted literals are boolean, even a "cast of a boolean literal" special case would assert a specific output of an uninterpreted function, which is by definition not universally provable. No `extend_dsl_file` fix can help, because a new `cast` builder would still serialize to an uninterpreted operator in the JSON that the fixed prover has no algebra for; additionally, the evaluator's surrounding machinery (CASE/COALESCE short-circuiting, deferral flags, plan-time error propagation) is execution-time plan-shape semantics with no bag-semantic meaning at all.

### `CommuteLimitProjection` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_limit.rs, lines 186-192
- Attempts used: 22
- Last updated: 2026-09-30T23:58:55.155399+00:00
- Reason / notes: The rule's correctness rests on Limit's ordered-prefix semantics — *which* rows are retained depends on row order — and QED's bag-semantic model fundamentally has no Sort/Limit/Offset or ordering structure, so that premise cannot be stated. Encoding Limit as an uninterpreted operator gives no axiom linking its output to its input, so `Limit(f,s,Proj(E,R)) = Proj(E,Limit(f,s,R))` is not first-order valid (e.g. a LIMIT that returns one fixed row whenever its input is nonempty is a countermodel for any non-fixing projection E). The only special cases collapse: identity/permutation with unbounded fetch is vacuous, and fetch=0 would require the unmodeled cardinality axiom Limit(0)=∅, so no non-trivial provable special case exists. ```

### `CommuteLimitSubqueryAlias` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_limit.rs, lines 193-199
- Attempts used: 24
- Last updated: 2026-10-01T00:35:20.360242+00:00
- Reason / notes: SubqueryAlias is a pure name change — bag-identity — and RuleScript's core language has no operator for it (faithfully omitting it is the only bag-level model), while per qed.pdf §6.2 QED has no model for Limit's ordering/top-N semantics (Sort/Limit/Offset are uninterpreted with no algebraic laws), so before and after both reduce to the identical uninterpreted Limit over the same input. The rule's only semantic content — that alias transparency preserves Limit's row-position semantics — is exactly what QED's bag model cannot express, and even adding a Limit/Sort builder via DSL extension would not help, since the prover itself has no ordering semantics to reason over, leaving any encodable version a vacuous self-equality rather than a real proof of the rule. ```

### `DedupeSortExprs` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_duplicated_expr.rs, lines 68-114
- Attempts used: 7
- Last updated: 2026-10-01T00:41:39.271951+00:00
- Reason / notes: The rule only trims the ORDER BY key list while the input (and hence the bag of emitted rows) is unchanged on both sides, so its entire soundness claim — that the deduplicated/FD-pruned collation induces the same row order — is a list/ordering property that QED does not model (qed.pdf §6.2; its evaluation's "List semantic" failure category). The missing Sort builder in RelRN is not the decisive gap: JSONSerializer already emits `sort` nodes into QED's JSON, meaning the prover (the unmodifiable arbiter) simply has no ordering semantics for them, so any Sort/Sort pattern would reduce to two trivially bag-equal plans and yield a vacuous "provable" that verifies nothing. Encoding the lexicographic preorder as a surrogate uninterpreted relation would only prove a separate combinatorial lemma, not an equivalence between plan patterns, so no genuine RuleScript encoding of this rule exists.

### `DistinctNoColumnsToLimitOne` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/replace_distinct_aggregate.rs, lines 89-100
- Attempts used: 48
- Last updated: 2026-10-01T01:21:04.202674+00:00
- Reason / notes: The rule's target is exactly `Limit { fetch: Some(1) }`, and QED does not model Limit/Offset/Sort (list/ordering semantics have no meaning in its bag semantics, with no axioms relating such an operator to its input), so the after pattern has no faithful encoding in the core language. The rewrite only holds because the input is zero-column (all rows identical), and the only at-most-one-row construct the bag core offers for a zero-column bag is the set-variant self-intersect — which is precisely how the before side (Distinct) itself is encoded — so any "encoding" either collapses to the trivial identity B∩B ≡ B∩B or is simply false if the placeholder has one or more columns (where DISTINCT can yield many rows while LIMIT 1 yields at most one), with no DSL constraint available to express the all-rows-equal premise. ```

### `DivideByOne` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1197-1204
- Attempts used: 11
- Last updated: 2026-10-01T00:53:13.625879+00:00
- Reason / notes: I verified the porter's two claims against the ground-truth sources: `RexRN.java` exposes only `trueLiteral`/`falseLiteral` (no numeric literal constructor, so the constant "1" cannot even be named faithfully in a pattern), and per the reference's Limitations, QED treats non-boolean scalar operators like `Divide` as uninterpreted function symbols with no axioms connecting them to their arguments — the identity `x / 1 = x` rests precisely on division's internal numeric semantics, which QED "cannot see through" and which no DSL extension can supply, since the prover is the unmodifiable arbiter. No relational/bag-structural re-encoding can avoid the arithmetic identity (the rule is purely a scalar simplification over a single projection), so this is a genuine QED limitation, consistent with the prior FoldDivOne precedent. ```

### `LimitZeroToEmpty` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_limit.rs, lines 1-89
- Attempts used: 7
- Last updated: 2026-10-01T03:26:18.965693+00:00
- Reason / notes: The rule's soundness (LIMIT with literal fetch 0 ⇒ empty relation, and skip-0/no-fetch ⇒ identity) rests entirely on the row-count/ordering semantics of LIMIT/OFFSET, but QED models Sort/Limit/Offset only as uninterpreted query operators (QOp) with no cardinality or ordering axioms, so `Limit(0, X)` is an unconstrained bag that SMT can instantiate as non-empty, making the equivalence with ∅ refutable for every input X. No non-trivial special case rescues it — even with an empty input (e.g. `Limit(0, Filter(false, X))`) there is no axiom linking the uninterpreted QOp's output to its input, so it remains unprovable. And closing it via `extend_dsl_file` is not an option: the DSL simply has no Limit/Sort builders, but adding one would serialize a LogicalSort that QED still treats as uninterpreted, since the limitation is in the trusted prover's bag-semantic model, not in the DSL's surface language. ```

### `MergeNestedLimits` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_limit.rs, lines 93-120
- Attempts used: 9
- Last updated: 2026-10-01T03:48:17.955422+00:00
- Reason / notes: The rule's soundness is a row-position identity — OFFSET drops the first k rows of a stream and LIMIT keeps the first n — but QED's bag semantics models Sort/Limit/Offset only as uninterpreted QOps with no ordering, index, or cardinality axioms (qed.pdf; the AGREE-verified LimitZeroToEmpty and FoldLimits precedents), so `before` and `after` are structurally distinct uninterpreted terms over X that SMT refutes for every instantiation, and no special case (not even idempotent `L(n,L(n,X))=L(n,X)` or an empty input) is derivable. The gap is in the trusted prover's semantics, not the DSL: `RelRN` has no Limit/Sort builder, and `extend_dsl_file` could at best serialize the `LogicalSort` node (sort/offset/limit JSON already exists in `JSONSerializer`) that QED still treats as opaque, so no encoding of the real nested-merge shape can prove. The transcript's isolated "provable" flag must therefore reflect a degenerate/structurally-identical stand-in encoding (consistent with its all-zero timings), not the nested-merge rule, so `UNSUPPORTED` is the correct permanent record.

### `ModuloByOne` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1210-1223
- Attempts used: 12
- Last updated: 2026-10-01T03:45:40.119769+00:00
- Reason / notes: QED models every scalar operator (including `Modulo`) as an uninterpreted function symbol over uninterpreted types, and the DSL exposes only the boolean literals true/false — no way to name the numeric constants 1 and 0 that the rule requires. The identity ∀a. a mod 1 = 0 is not valid for arbitrary instantiations of an uninterpreted modulo symbol and arbitrary constant symbols, so no SMT solver without an arithmetic theory can prove it, and QED's Rust prover has none by design. The gap is in the prover's theory, not in missing DSL surface syntax, so `extend_dsl_file` cannot close it and UNSUPPORTED is the correct conclusion. ```

### `MultiplyByOne` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1152-1159
- Attempts used: 21
- Last updated: 2026-10-01T03:54:40.512490+00:00
- Reason / notes: The rewrite A * 1 ⟹ A is valid only because 1 is the multiplicative identity of real arithmetic, but QED models scalar operators like Multiply as uninterpreted symbols with no axioms tying them to their arguments (its theory covers only equality/uninterpreted functions, a total order, and natural-number addition), so the identity rests on numeric semantics the fixed prover cannot see through — and `RexRN` (verified in source) offers no numeric-literal constructor, only `trueLiteral`/`falseLiteral`, to express the constant `1` at all. Since the missing piece lives in the immutable prover's theory rather than in a DSL builder, `extend_dsl_file` cannot close the gap: even embedding the expression in a `Project` over a scan, `f(a, 1) = a` is unprovable for any uninterpreted `f`, and no faithful narrower special case of this rule exists — the same fundamental limitation as the verifier-agreed DivideByOne skip. ```

### `MultiplyByZero` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1170-1180
- Attempts used: 41
- Last updated: 2026-10-01T04:35:58.583635+00:00
- Reason / notes: MultiplyByZero is a pure scalar arithmetic identity — its soundness rests on the axiom ∀a. *(a, 0) = 0 (guarded by non-nullness of a), but QED's fixed SMT theory translates scalar operators to uninterpreted functions with no arithmetic axioms and no interpreted numeric zero constant (the only interpreted scalar operators are booleans, which is exactly why the DSL exposes And/Or/Not/True/False), so a countermodel with *(a, 0) ≠ 0 is always available and no encoding can avoid needing that axiom. This is the same verified limitation that forced the sibling constant-folding rules FoldPlusZero (a + 0 = a) and BitwiseAndByZero to be skipped, and an `extend_dsl_file` addition can't close it because new multiply/zero builders would merely serialize to additional uninterpreted symbols in the unmodifiable prover.

### `NotLikeToNotLike` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 360-367
- Attempts used: 24
- Last updated: 2026-10-01T06:36:33.630231+00:00
- Reason / notes: The rule's entire content is the definitional duality ¬(x LIKE y) ≡ x NOT LIKE y, and QED's fixed logic has no axiom or definitional expansion for LIKE/NOT_LIKE — so a faithful encoding, where NOT_LIKE is a distinct uninterpreted predicate, leaves `not(like(...))` and `not_like(...)` as independent symbols the prover can never relate (its stated limitation on predicate inference between independent symbols). The only alternative is to encode NOT_LIKE as `Not` of the same LIKE symbol, which collapses before/after into the identical plan and reduces any "proof" to a vacuous identity that assumes the theorem; no DSL extension can bridge this, since the unchanging prover sees uninterpreted function symbols either way.

### `OrNotSelfTautology` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 978-985
- Attempts used: 108
- Last updated: 2026-10-01T08:23:31.216215+00:00
- Reason / notes: The rule's soundness rests entirely on A being non-nullable (in Kleene logic A OR NOT(A) = NULL when A = NULL), and the porter's encodings are corroborated by an internal consistency check: the Kleene-valid dual A AND NOT(A) → FALSE proved while the OR direction was rejected with a complete SMT run, which is the signature of QED modeling uninterpreted predicates as possibly-null rather than a symbol-sharing or encoding bug. QED's JSON contract carries nullability only per table column (verified in JSONSerializer: expression nodes serialize only an operator/operand/type with no nullability flag), so an uninterpreted predicate or projection can never be asserted non-null, and the only guaranteed non-null boolean — a non-nullable scan column used directly in the filter condition — is rejected by QED's filter translator, whose boolean-term grammar admits predicate calls/connectives/literals but panics on a column reference (verified for both a BOOLEAN-named and a generic-typed non-nullable column). This gap cannot be closed via extend_dsl_file, because it lies in the JSON contract (no per-expression nullability channel) and in the prover-side boolean grammar, neither of which is modifiable from the Java DSL — a genuine QED limitation, not a missed encoding. ```

### `PushFilterIntoSort` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 885-889
- Attempts used: 7
- Last updated: 2026-10-01T08:37:57.242473+00:00
- Reason / notes: I confirmed against the ground-truth RelRN.java that no Sort builder or record exists (JSONSerializer can carry a sort node, but the trusted, unmodifiable prover decides equivalence only under bag semantics, under which Sort has no list/ordering meaning, per QED's stated limitations). The rule's sole non-trivial content — that the survivors of Filter(p, Sort(R)) retain the same relative sorted order as Sort(Filter(p, R)) — is a sequence property that cannot be expressed in the bag algebra (row-wise uninterpreted predicates/projections cannot capture relative ordering across a whole relation). Any encodable reduction would degenerate to the tautology Filter(p, R) ≡ Filter(p, R), certifying nothing about order preservation, so the porter's UNSUPPORTED conclusion is correct. ```

### `PushFilterIntoTableScan` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_filter.rs, lines 1224-1301
- Attempts used: 7
- Last updated: 2026-10-01T08:45:39.081913+00:00
- Reason / notes: The only branch of this rule with bag-semantic content — dropping an `Exact`-pushed conjunct from the Filter above a TableScan — is sound solely by the provider's execution-time contract that a scan configured with predicate P returns exactly the rows of Filter(P, T); QED models scans as uninterpreted bag relations whose only axioms are key uniqueness and check constraints on the table's own rows, so no operator or axiom can relate a filter-pushing scan to the filtered original (distinct scan symbols are independent, and same-name scans resolve to one symbol), and the sole available encoding — pre-asserting P as a table precondition — proves constraint-based filter elimination rather than pushdown. The Inexact/volatile/subquery branches introduce no bag-semantic difference, since `scan.filters` is planner metadata invisible to the prover, so no narrower special case carries the rule's actual content. ```

### `PushLimitIntoCrossJoin` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_limit.rs, lines 275-303
- Attempts used: 27
- Last updated: 2026-10-01T09:08:11.473596+00:00
- Reason / notes: The cross-join branch rewrites `LIMIT_n(L ⋈cross R)` to `LIMIT_n(LIMIT_n(L) ⋈cross LIMIT_n(R))`, whose correctness is a purely prefix/enumeration-order argument (the first n output pairs of the nested-loop product draw only on the first n rows of each input, under a specific row-major output order). QED decides equivalence under bag semantics only and, per its documented failure breakdown, does not model the ordering semantics of Sort/Limit/Offset — so even though the JSON format can carry a limit-shaped node (LogicalSort fetch/offset), the unmodifiable prover cannot decide such queries, and no bag-semantic stand-in (uninterpreted filter or projection symbol) can capture prefix selection, since QED cannot reason about predicate entailment or order at all. The limitation sits in the prover itself rather than in a missing DSL feature, so `extend_dsl_file` cannot close the gap and the UNSUPPORTED conclusion stands. ```

### `PushLimitIntoLeftJoin` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_limit.rs, lines 275-303
- Attempts used: 9
- Last updated: 2026-10-01T09:07:06.779894+00:00
- Reason / notes: The soundness of this rewrite rests entirely on limit/row-count semantics — that the outer LIMIT(skip, fetch) can only observe rows produced by at most skip+fetch left rows, a fact no bag-semantic model of QED can express — and QED (qed.pdf §6.2) treats Sort/Limit/Offset as uninterpreted higher-order QOps with no cardinality or ordering axioms, so any faithful encoding leaves before() and after() as structurally unrelated uninterpreted applications that SMT can never equate. This is a genuine prover-model gap rather than a missing builder: JSONSerializer already carries LogicalSort's offset/limit, so even an extend_dsl_file addition of a Limit builder would serialize fine but still be treated opaquely by the trusted prover. The lone "provable" result in the transcript came from a degenerate stand-in encoding (identical structure on both sides), not a proof of the actual rewrite, so the UNSUPPORTED conclusion is correct. ```

### `PushLimitIntoRightJoin` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_limit.rs, lines 275-303
- Attempts used: 26
- Last updated: 2026-10-01T09:23:52.870650+00:00
- Reason / notes: The rule's soundness rests entirely on row-count/ordering semantics — that in a right join each preserved right-input row appears in the join output (in input order), so the first skip+fetch output rows can only come from the first fetch right rows — and QED models Sort/Limit/Offset as uninterpreted QOps with no bag-semantic meaning, no cardinality, and no ordering axioms (qed.pdf §6.2; the core language has no Limit builder at all). Any faithful encoding leaves before() and after() as structurally distinct applications of an opaque uninterpreted operator over different join trees, which the SMT solver can never equate, and even a Limit builder added via extend_dsl_file would only serialize into that same opaque QOp without granting the prover any row-count reasoning. This is a genuine prover-model gap, not a missing DSL surface or an untried encoding — the same fundamental limitation for which the sibling PushLimitIntoLeftJoin was already ruled UNSUPPORTED.

### `PushLimitIntoTableScan` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_limit.rs, lines 128-144
- Attempts used: 6
- Last updated: 2026-10-01T09:14:33.776864+00:00
- Reason / notes: The rule's validity is purely a prefix-in-enumeration-order argument — the outer Limit(skip, fetch) can only consume the first skip+fetch rows of the scan's ordered output, and the tightened scan must return that same prefix rather than merely some smaller subset — and QED models only unordered bags, explicitly lacking any list/ordering semantics for Limit/Offset/Sort. A DSL extension cannot close the gap either: even though JSONSerializer can carry a sort node with offset/limit, the prover has no bag meaning for it, and the scan's fetch cap is a physical operator-internal property with no representation in QED's uninterpreted-table model at all (fetch is not serialized). No bag-semantic stand-in works — a filter predicate cannot cap cardinality or select an order-dependent prefix, and modeling the capped scan as an independent uninterpreted table would destroy the prefix relationship the proof depends on — so no encoding can be proved equivalent. ```

### `PushLimitIntoUnionBranches` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_limit.rs, lines 145-154
- Attempts used: 8
- Last updated: 2026-10-01T09:21:08.943054+00:00
- Reason / notes: This rule is valid only because DataFusion’s union preserves branch order and Limit selects a row-position prefix, so capping each branch to `fetch + skip` rows preserves the outer `Limit(skip, fetch)` result. QED uses bag semantics and explicitly has no ordering semantics for Sort/Limit/Offset, and RuleScript provides no order-preserving limit operator; even if a Limit builder were added, the prover would still treat it as uninterpreted and could not prove the prefix argument.

### `PushTopKThroughJoin` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/push_down_limit/topk_through_join.rs, lines 92-308
- Attempts used: 21
- Last updated: 2026-10-01T09:45:15.254033+00:00
- Reason / notes: The rule's substance — that the top-N rows of a LEFT/RIGHT join, ordered by keys that depend only on the preserved side, are all producible from the top-N of that side — is an ordering/top-k statement, while QED only decides bag equality and (per qed.pdf and the reference) has no semantics for Sort/Limit at all, which the RelRN DSL doesn't even expose a builder for, so `extend_dsl_file` could at best emit a "sort" JSON node that the unmodifiable prover has no ordering axioms to interpret. Furthermore the rewrite is not a bag equality in general: with equal sort keys, the before- and after-plans can yield different N-row result sets (both valid top-k results under arbitrary tie-breaking), so even a hypothetical ordering-aware equivalence prover could not certify it. I also checked the tempting narrow special cases (unique sort key via `scan(..., unique)`, at-most-one match per preserved row): they would make top-N deterministic, but "first N rows in an order" is still not expressible in the bag language — no comparison/total-order axiom on uninterpreted key values exists — so no provable, non-vacuous encoding exists, consistent with the PushLimitIntoLeftJoin precedent. ```

### `RemoveNoopLimit` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/eliminate_limit.rs, lines 1-89
- Attempts used: 21
- Last updated: 2026-10-01T09:51:28.296081+00:00
- Reason / notes: Both halves of this rule (fetch=Literal(0) ⇒ empty relation; fetch=None with skip=Literal(0) ⇒ drop the Limit as identity) draw their entire correctness from Limit/Offset operator semantics, which QED does not model — the prover treats limit/sort as uninterpreted operations with no cardinality or ordering axioms (qed.pdf §6.2), so the SMT solver can refute both equivalences against any input relation. This is a limitation of the trusted prover, not a DSL surface gap: even if `extend_dsl_file` added a Limit/Sort builder to `RelRN` (which currently exposes none), no axioms like `limit(0,R)=∅` or `offset(0,R)=R` could be introduced, since the Rust prover is unreachable and must stay as-is. This matches the independently-reviewed `LimitZeroToEmpty` precedent from the same source, which was likewise found unprovable. ```

### `RewriteSetComparison` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/rewrite_set_comparison.rs, lines 1-175
- Attempts used: 66
- Last updated: 2026-10-01T10:39:08.495751+00:00
- Reason / notes: The rule's entire semantic content is a scalar three-valued-logic identity — `x op ANY/ALL (S)` being equal to a CASE over EXISTS subqueries with IS TRUE / IS NULL / NULL-literal branches — and that is precisely the level QED abstracts away: predicates and comparisons are uninterpreted symbols with two-valued boolean output in the SMT encoding, there is no CASE/NULL/3VL operator or axiom mechanism in the prover to define ANY/ALL in terms of EXISTS, and even a maximal `extend_dsl_file` addition of those builders would only yield independent uninterpreted symbols whose equivalence the prover cannot decide (the stated "predicate entailment between independent symbols" / "bespoke operator internals" limitations). I confirmed against the DSL source that no EXISTS/scalar-subquery predicate, CASE, NULL literal, IS-TRUE/IS-NULL, or set-comparison node exists (the `RexSubQuery` hits are serializer/deserializer plumbing, and `Correlate` is a relational dependent join producing pair-rows, not a boolean EXISTS test), and the only fully-relational re-encoding of both sides collapses to semi-join ≡ semi-join, a tautology that discards the rule's content — so no genuine, non-trivial provable special case exists, and the gap is a fundamental prover limitation, not a missing builder.

### `ScalarSubqueryToJoin` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/scalar_subquery_to_join.rs, lines 1-437
- Attempts used: 114
- Last updated: 2026-10-01T13:01:33.450863+00:00
- Reason / notes: The scalar-subquery → LEFT JOIN rewrite is only valid because a global (groupless) aggregate over an *empty* input yields the same value the LEFT JOIN injects as its synthetic right-side NULL row (and DataFusion's own `CASE ... IS NULL` "count-bug" compensation exists precisely to recover non-NULL empty-input values like COUNT), so the proof depends on the aggregate's value at the empty multiset and on the groupless aggregate being a singleton. QED models every aggregate as an uninterpreted function of its input bag and "knows nothing about a specific aggregate function's algebra beyond bag equality of its input," so it cannot relate `agg(∅)` to the join's NULL — confirmed by the SMT solver actually running (~36 ms) and refuting with a concrete unmatched-row counterexample. This is a genuine limitation of the prover's aggregate-algebra/cardinality reasoning, not a missing DSL builder; adding a "constrain to one row" operator would not close the gap because the blocker is the uninterpreted aggregate's unknown empty-input value.

### `SimplifyRegexToLike` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1710-1723
- Attempts used: 13
- Last updated: 2026-10-01T11:08:01.002545+00:00
- Reason / notes: The rule's entire semantic content is a string pattern-matching axiom (e.g. `x ~ '^foo$'` ⟺ `x = 'foo'`, `x ~ 'foo'` ⟺ `x LIKE '%foo%'`), but QED lowers all column types to integers and treats each operator as a distinct uninterpreted predicate, so `regex(x, p)`, `like(x, p')`, `eq(x, c)`, and `isNotNull(x)` remain unrelated symbols over which the equivalence must hold for *all* instantiations — and the SMT layer has no string/regex/glob theory to connect them, making the before/after pairs inequivalent under QED's universal quantification. No DSL extension can close this: a new builder would only emit yet another uninterpreted symbol into the prover's JSON, and the only encoding that would be provable is one where both sides share a single predicate symbol, which is the vacuous identity rule, not the rewrite — so the porter's UNSUPPORTED verdict and its diagnosis (gap in the unmodifiable prover's theory, not a missing operator shape) are correct.

### `SimplifyUdafToScalar` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1677-1684
- Attempts used: 29
- Last updated: 2026-10-01T11:25:13.208565+00:00
- Reason / notes: The assigned source (L1677–1685) is a pure dispatch that hands the aggregate to an arbitrary per-UDAF, user-supplied `simplify()` closure, so there is no fixed after-pattern and thus no fixed before/after relational pair for QED to be asked to prove — this is a meta-hook, not a concrete transformation. Even specializing to a real UDAF rewrite (e.g. `percentile_cont(col, 0.0) → min(col)`) would still fail, because QED models aggregate functions as uninterpreted and knows nothing about their algebra beyond bag-equality of the input; the simplification changes the aggregate operator itself, which the prover cannot see through as an uninterpreted function. ```

### `StructCastSameFieldCountFold` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 534-680
- Attempts used: 5
- Last updated: 2026-10-01T11:27:01.467581+00:00
- Reason / notes: The rule's only effect is plan-time constant evaluation — replacing an eligible CAST/TRY_CAST over a struct literal with the concrete value the evaluator computes — which amounts to asserting a specific output of a cast that QED models as a purely uninterpreted function with no cast axioms (its only interpreted literals are booleans), so `cast(lit) = folded(lit)` is refutable under some SMT instantiation and no encoding of it can be universally provable; a Java-side `extend_dsl_file` builder for cast/values cannot close this gap because the fixed Rust prover is the unmodifiable arbiter and simply has no cast algebra to apply. The equal-field-count, field-name-overlap, and non-0-row conditions in `can_evaluate` are eligibility heuristics of DataFusion's 1-row dummy-batch evaluator (execution-time semantics with no bag meaning), and the identical source region was already independently verifier-agreed SKIPPED as CastLiteralFold for this same fundamental reason. ```

### `UnwrapCastAroundComparison` — ⏭️ SKIPPED

- Source backend: Apache DataFusion
- Source rule: Source: datafusion/optimizer/src/simplify_expressions/unwrap_cast.rs, lines 1-250
- Attempts used: 10
- Last updated: 2026-10-01T11:35:30.386291+00:00
- Reason / notes: The rewrite `cast(x) cmp y ⟺ x cmp cast(y)` is only sound because of the specific cast's value-algebra (injectivity/monotonicity, literal in-range) plus static type-level guards; in QED every operator — the cast and each comparison — is an uninterpreted function with no axioms, and all VarTypes are erased to INTEGER in the prover, so that commutation is genuinely not a universal identity (the porter's complete, non-timed-out SMT countermodel is the expected answer, not an encoding bug). No DSL extension could help: the prover has no cast theory to hook into, and the pattern language offers no way to state the required side conditions (range/losslessness), so even the degenerate sub-case `cast(lit) = lit` would be unprovable since the "literal" is just an arbitrary column value. ```

