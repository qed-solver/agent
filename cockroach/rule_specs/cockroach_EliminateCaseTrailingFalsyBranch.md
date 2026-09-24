# Name: EliminateCaseTrailingFalsyBranch
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

EliminateCaseTrailingFalsyBranch removes a trailing WHEN branch that results
in False or Null from a CASE expression in a filter when the ELSE branch also
results in False or Null. This is valid because the trailing WHEN branch
cannot change the result of the CASE expression, regardless of the results of
its condition.

For example:

SELECT * FROM abc WHERE CASE WHEN a > 1 THEN true WHEN b > 2 THEN false ELSE false END
=>
SELECT * FROM abc WHERE CASE WHEN a > 1 THEN true ELSE false END

NOTE: The optimizer guarantees that CASE branches with side-effects will only
be evaluated if their conditions are true and the ELSE branch with
side-effects will only be evaluated if non of the branch conditions are true
(see props.VolatilitySet). Therefore, this rule is only valid when the ELSE
branch is leakproof. We currently only match constant False and Null values,
which are guaranteed to be leakproof.

Extracted from `select.opt` (which defines multiple rules — implement specifically `EliminateCaseTrailingFalsyBranch`, not the other rules in that file):

```
# EliminateCaseTrailingFalsyBranch removes a trailing WHEN branch that results
# in False or Null from a CASE expression in a filter when the ELSE branch also
# results in False or Null. This is valid because the trailing WHEN branch
# cannot change the result of the CASE expression, regardless of the results of
# its condition.
#
# For example:
#
#  SELECT * FROM abc WHERE CASE WHEN a > 1 THEN true WHEN b > 2 THEN false ELSE false END
#  =>
#  SELECT * FROM abc WHERE CASE WHEN a > 1 THEN true ELSE false END
#
# NOTE: The optimizer guarantees that CASE branches with side-effects will only
# be evaluated if their conditions are true and the ELSE branch with
# side-effects will only be evaluated if non of the branch conditions are true
# (see props.VolatilitySet). Therefore, this rule is only valid when the ELSE
# branch is leakproof. We currently only match constant False and Null values,
# which are guaranteed to be leakproof.
[EliminateCaseTrailingFalsyBranch, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Case
                (True)
                $whens:[ ... (When * (False | Null)) ] &
                    (LenGT $whens 1)
                $orElse:(False | Null)
            )
        )
        ...
    ]
)
=>
(Select
    $input
    (ReplaceFiltersItem
        $filters
        $item
        (Case (True) (DropLast $whens) $orElse)
    )
)
```
