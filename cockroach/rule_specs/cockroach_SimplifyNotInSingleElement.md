# Name: SimplifyNotInSingleElement
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyNotInSingleElement`, not the other rules in that file):

```
[SimplifyNotInSingleElement, Normalize]
(NotIn $left:* (Tuple [ $right:* ]))
=>
(Ne $left $right)
```
