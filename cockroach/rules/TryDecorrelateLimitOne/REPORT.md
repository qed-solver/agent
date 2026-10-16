# TryDecorrelateLimitOne

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 45  **Verification rounds used:** 3

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

TryDecorrelateLimitOne's correctness is precisely the identity "top-1 row of each left row's matches under collation O" (Limit 1 + ordering) = "first row of each group under the same collation O" (DistinctOn + FirstAgg), and both sides are order-dependent selections with no bag-semantic meaning — QED decides equivalence only on bag semantics with uninterpreted bag functions, and per the reference, Sort/Limit/Offset/Window have no semantics there (a Java-side builder added via extend_dsl_file could not change the unmodifiable Rust prover). "Pick one row from a group" is not a function of the group's bag, so FirstAgg/ConstAgg cannot be modeled as uninterpreted aggregates (QED knows no aggregate algebra), and any encoding is either unprovable (Limit and DistinctOn as independent uninterpreted symbols) or tautological (collapsing both sides into one shared symbol); there is no order-independent special case that is both valid and expressible, since even the unordered "arbitrary row" variant is not a deterministic bag operation. ```
