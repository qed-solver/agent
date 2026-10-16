# TryDecorrelateLimit

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2

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

TryDecorrelateLimit's entire correctness claim is the order-based equivalence "first n rows of the right input per the given ordering (LIMIT)" ≡ "rows of the right input with ROW_NUMBER() (computed in that ordering) ≤ n", and QED only decides bag-semantic equivalence — it has no model for the ordering semantics of Sort/Limit/Offset/Window at all (a documented limitation, confirmed by the "List semantic" failure category in QED's own Calcite/CockroachDB evaluation in qed.pdf). No non-vacuous special case rescues it: without ordering, "the first n rows" (even for n=1) is undefined over a bag, so the best available encodings either model LIMIT and ROW_NUMBER() as independent uninterpreted functions (identity then unprovable) or collapse them into one symbol (tautological, proving nothing about the actual operators). The gap sits in the unmodifiable QED prover itself rather than in a missing DSL builder, so extending RelRN/RexRN could not make this rule decidable. ```
