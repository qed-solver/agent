# TryDecorrelateLimitOne

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 42  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The before pattern's right input is literally `Limit($input, Const 1, $ordering)`, and the after pattern's `DistinctOn` selects rows via an ordering-dependent `First`/`Const` aggregate over `MakeGrouping(KeyCols, $ordering)`; both sides rest on list/ordering semantics (first-row selection and limit-1 cardinality) that QED explicitly does not model, so neither the Limit node nor the order-dependent group-by-First can be faithfully expressed or proven — this is a fundamental QED limitation, not a missing builder.
