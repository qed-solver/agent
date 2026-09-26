# FoldLimits

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2

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

FoldLimits reduces to the identity Limit(Limit(R, n_in, ord_in), n_out, ord_out) ≡ Limit(R, n_out, ord_in) under n_out ≤ n_in and ord_in ⇒ ord_out, and its entire correctness rests on ordered positional row selection ("first k rows in ordering o") plus the ordering-implication relation. QED decides equivalence over bag semantics and, per its own stated limitations, does not model list/ordering semantics (Sort/Limit/Offset/Window/Sample have no bag-semantic meaning), and the core language exposes no Limit operator at all; modeling Limit as an uninterpreted operator would strip away exactly the row-retention/ordering axioms the identity needs, and it is not valid for an arbitrary uninterpreted operator (QED would find a countermodel), with no relational sub-identity to factor out since both sides terminate in a single limit over the same base input — so the SKIPPED EliminateLimit/EliminateOffset precedents correctly apply.
