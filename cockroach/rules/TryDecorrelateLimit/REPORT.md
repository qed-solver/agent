# TryDecorrelateLimit

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rewrite replaces an ordered LIMIT with a row_number() window plus a rowNum <= limit filter, so correctness depends on which row occurrences are selected under a given ordering. QED only reasons about bag semantics and gives Sort/Limit/Window no ordering algebra, so no RuleScript encoding can prove the required row-selection equivalence even if Limit/Window builders were added. ```
