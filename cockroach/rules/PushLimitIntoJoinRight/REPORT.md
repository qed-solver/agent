# PushLimitIntoJoinRight

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

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
```

## Independent verifier review

**Verdict:** AGREE

PushLimitIntoJoinRight's soundness rests on LIMIT's ordered-prefix semantics — selecting the top-n rows under an ordering, with the ordering restricted to right-input columns and the join preserving right rows — and QED only decides bag-semantic equivalence, where Sort/Limit/Offset are (at best) uninterpreted operators with no prefix/ordering meaning. Because the Limit node sits in different positions on the two sides (above the join vs. pushed into the right input), the goal cannot be reduced to a pure bag identity on the join subexpressions — unlike the previously-proved AssociateLimitJoins rules, where an identical outer Limit in the same position factors out by congruence — so no RuleScript encoding can make it provable without modifying the QED prover, which is off-limits. ```
