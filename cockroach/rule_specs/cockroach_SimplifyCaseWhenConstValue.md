# Name: SimplifyCaseWhenConstValue
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifyCaseWhenConstValue removes branches known to not match. Any
branch known to match is used as the ELSE and further WHEN conditions
are skipped. If all WHEN conditions have been removed, the ELSE
expression is used.
This transforms

CASE WHEN v THEN 1 WHEN false THEN a WHEN true THEN b ELSE c END

to

CASE WHEN v THEN 1 ELSE b END

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyCaseWhenConstValue`, not the other rules in that file):

```
# SimplifyCaseWhenConstValue removes branches known to not match. Any
# branch known to match is used as the ELSE and further WHEN conditions
# are skipped. If all WHEN conditions have been removed, the ELSE
# expression is used.
# This transforms
#
#   CASE WHEN v THEN 1 WHEN false THEN a WHEN true THEN b ELSE c END
#
# to
#
#   CASE WHEN v THEN 1 ELSE b END
#
[SimplifyCaseWhenConstValue, Normalize]
(Case
    $condition:(ConstValue)
    $whens:[ ... (When (ConstValue)) ... ]
    $orElse:*
)
=>
(SimplifyWhens $condition $whens $orElse)
```
