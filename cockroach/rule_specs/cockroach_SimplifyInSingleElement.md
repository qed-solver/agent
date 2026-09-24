# Name: SimplifyInSingleElement
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyInSingleElement`, not the other rules in that file):

```
[SimplifyInSingleElement, Normalize]
(In $left:* (Tuple [ $right:* ]))
=>
(Eq $left $right)
```
