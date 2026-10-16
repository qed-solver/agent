# PushLimitIntoJoinRight

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2

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

`PushLimitIntoJoinRight`'s correctness rests entirely on top-n/ordering semantics — distributing `Limit` through a join that preserves the right rows — but QED decides equivalence over bag semantics with uninterpreted functions and gives `Limit`/`Sort`/`Offset` no bag-semantic meaning (they are uninterpreted QOps), so the before term `Limit(Join(L,R),n)` and the after term `Limit(Join(L,Limit(R,n)),n)` are structurally distinct opaque applications whose inner arguments cannot be shown equal, and SMT cannot equate them. The only equatable degenerate case (empty left input) makes both sides collapse to the same `Limit(∅)` without exercising the pushdown, so it's vacuous and not a usable special case. No `extend_dsl_file` can close the gap because the missing top-n axioms live in the unmodifiable Rust prover, not in the DSL or serializer.
