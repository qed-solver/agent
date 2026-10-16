# FoldLimits

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

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
```

## Independent verifier review

**Verdict:** AGREE

FoldLimits is only sound because Limit sorts its input by the named ordering and takes the first n rows, and the guard (inner ordering implies outer ordering) makes the outer re-sort a no-op — i.e., its correctness rests entirely on ordering and row-count semantics that QED explicitly does not model (Sort/Limit/Offset carry no bag-semantic meaning and are treated as uninterpreted operators with no axioms). With Limit uninterpreted, SMT can assign different results to Limit applied at different nesting depths, so even the narrowest special case (Limit(n, Limit(n, X, o)) ≈ Limit(n, X, o)) is refutable, and the OrderingImplies / constant-comparison guards have no footing in the prover at all. Closing this would require adding ordering/count axioms to the QED prover itself — the unchangeable arbiter — so no DSL encoding or extend_dsl_file addition (e.g., a Limit builder feeding JSONSerializer's existing sort form) can make the identity provable. ```
