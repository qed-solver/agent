# Name: TryDecorrelateSemiJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateSemiJoin maps a SemiJoin to an equivalent GroupBy/InnerJoin
complex in hopes of triggering further rules that will ultimately decorrelate
the query. Once this rule fires, a corresponding InnerJoin decorrelation rule
will match (i.e. TryDecorrelateGroupBy or TryDecorrelateProject).

Citations: [5]

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateSemiJoin`, not the other rules in that file):

```
# TryDecorrelateSemiJoin maps a SemiJoin to an equivalent GroupBy/InnerJoin
# complex in hopes of triggering further rules that will ultimately decorrelate
# the query. Once this rule fires, a corresponding InnerJoin decorrelation rule
# will match (i.e. TryDecorrelateGroupBy or TryDecorrelateProject).
#
# Citations: [5]
[TryDecorrelateSemiJoin, Normalize]
(SemiJoin | SemiJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (CanHaveZeroRows $right) &

        # Let EliminateExistsGroupBy match instead.
        (GroupBy | DistinctOn | Project | ProjectSet | Window)
    $on:*
    $private:*
)
=>
(Project
    # Needed to project away any columns added by EnsureKey.
    (GroupBy
        (InnerJoinApply
            $newLeft:(EnsureKey $left)
            $right
            $on
            $private
        )
        (MakeAggCols ConstAgg (NonKeyCols $newLeft))
        (MakeGrouping (KeyCols $newLeft) (EmptyOrdering))
    )
    []
    (OutputCols $left)
)
```
