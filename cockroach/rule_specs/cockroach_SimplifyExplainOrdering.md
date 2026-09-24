# Name: SimplifyExplainOrdering
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyExplainOrdering removes redundant columns from the Explain operator's
input ordering.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyExplainOrdering`, not the other rules in that file):

```
# SimplifyExplainOrdering removes redundant columns from the Explain operator's
# input ordering.
[SimplifyExplainOrdering, Normalize]
(Explain
    $input:*
    $explainPrivate:* &
        (CanSimplifyExplainOrdering $input $explainPrivate)
)
=>
(Explain $input (SimplifyExplainOrdering $input $explainPrivate))
```
