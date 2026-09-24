# Name: SimplifyCaseSingleFalsyBranch
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

SimplifyCaseSingleFalsyBranch replaces a CASE expression in a filter with a
falsy ELSE branch when it has a single WHEN branch that evaluates to False or
Null.

For example:

SELECT * FROM abc WHERE CASE WHEN a > 1 THEN false ELSE false END
=>
SELECT * FROM abc WHERE false

NOTE: The optimizer guarantees that CASE branches with side-effects will only
be evaluated if their conditions are true and the ELSE branch with
side-effects will only be evaluated if non of the branch conditions are true
(see props.VolatilitySet). Therefore, this rule is only valid when the ELSE
branch is leakproof. We currently only match constant False and Null values,
which are guaranteed to be leakproof.

Extracted from `select.opt` (which defines multiple rules — implement specifically `SimplifyCaseSingleFalsyBranch`, not the other rules in that file):

```
# SimplifyCaseSingleFalsyBranch replaces a CASE expression in a filter with a
# falsy ELSE branch when it has a single WHEN branch that evaluates to False or
# Null.
#
# For example:
#
#  SELECT * FROM abc WHERE CASE WHEN a > 1 THEN false ELSE false END
#  =>
#  SELECT * FROM abc WHERE false
#
# NOTE: The optimizer guarantees that CASE branches with side-effects will only
# be evaluated if their conditions are true and the ELSE branch with
# side-effects will only be evaluated if non of the branch conditions are true
# (see props.VolatilitySet). Therefore, this rule is only valid when the ELSE
# branch is leakproof. We currently only match constant False and Null values,
# which are guaranteed to be leakproof.
[SimplifyCaseSingleFalsyBranch, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Case
                (True)
                $whens:[ (When * (False | Null)) ]
                $orElse:(False | Null)
            )
        )
        ...
    ]
)
=>
(Select $input (ReplaceFiltersItem $filters $item $orElse))
```
