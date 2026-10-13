# TryDecorrelateMax1Row

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 101  **Verification rounds used:** 6

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

The rule's correctness rests entirely on matching runtime error behavior — Max1Row raises an error when its input exceeds one row, and EnsureDistinctOn raises an error when it detects duplicate key values in the join output — so the two sides are equivalent precisely in the sense that they error on the same instantiations and produce the same bag on the non-error instantiations. Neither Max1Row (an at-most-one-row cardinality constraint with error) nor EnsureDistinctOn (a uniqueness check with error) is expressible as a composition of the DSL's bag-semantic operators (scan/filter/project/join/aggregate), and QED's SMT-based decision procedure only compares bag contents for all instantiations, with no notion of runtime errors, row-count limits, or error-condition matching. Any encoding that strips the error semantics reduces Max1Row to a plain input and EnsureDistinctOn to a GroupBy, which are not bag-equivalent when the input has more than one row, so QED would correctly report non-provability; there is no non-vacuous encoding of this rule within the core language. ```
