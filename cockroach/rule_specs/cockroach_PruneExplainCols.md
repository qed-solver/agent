# Name: PruneExplainCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneExplainCols discards Explain input columns that are never used by its
required physical properties.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneExplainCols`, not the other rules in that file):

```
# PruneExplainCols discards Explain input columns that are never used by its
# required physical properties.
[PruneExplainCols, Normalize]
(Explain
    $input:*
    $explainPrivate:* &
        (CanPruneCols
            $input
            $needed:(NeededExplainCols $explainPrivate)
        )
)
=>
(Explain (PruneCols $input $needed) $explainPrivate)
```
