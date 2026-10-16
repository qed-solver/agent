# TryDecorrelateMax1Row

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule's only non-vacuous content is the coincidence of error behavior — Max1Row aborts when a correlated subquery instance holds >1 row, while EnsureDistinctOn aborts when a left-key group holds >1 row — plus the ≤1-row-per-group invariant that makes both collapse to the same cross-join; QED's semiring model has no error semantics, no Max1Row/EnsureDistinctOn operators it can see through as uninterpreted symbols, and no way to assume the ≤1-row cardinality precondition since it decides unconditional universal bag equality. Hence any faithful encoding is refutable (QED instantiates the two distinct error operators differently), while the only provable encoding is the vacuous tautology Join(left,input) ≡ Project(Join(EnsureKey(left),input)) after erasing both operators to identity — a genuine QED limitation, not a missing DSL builder a Max1Row constructor could close.
