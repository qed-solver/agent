# Name: SimplifyCaseSingleTruthyBranch
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

SimplifyCaseSingleTruthyBranch replaces a CASE expression in a filter with its
condition when it has a single WHEN branch that evaluates to True, and an ELSE
branch that evaluates to False or Null.

For example:

SELECT * FROM abc WHERE CASE WHEN a > 5 THEN true ELSE false END
=>
SELECT * FROM abc WHERE a > 5

Extracted from `select.opt` (which defines multiple rules — implement specifically `SimplifyCaseSingleTruthyBranch`, not the other rules in that file):

```
# SimplifyCaseSingleTruthyBranch replaces a CASE expression in a filter with its
# condition when it has a single WHEN branch that evaluates to True, and an ELSE
# branch that evaluates to False or Null.
#
# For example:
#
#  SELECT * FROM abc WHERE CASE WHEN a > 5 THEN true ELSE false END
#  =>
#  SELECT * FROM abc WHERE a > 5
#
[SimplifyCaseSingleTruthyBranch, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Case
                (True)
                $whens:[ (When $cond:* (True)) ]
                (False | Null)
            )
        )
        ...
    ]
)
=>
(Select $input (ReplaceFiltersItem $filters $item $cond))
```
