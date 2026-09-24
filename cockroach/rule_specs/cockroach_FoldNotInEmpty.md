# Name: FoldNotInEmpty
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNotInEmpty replaces the NotIn with True when the right input is empty.
Note that this is correct even if the left side is Null, since even an unknown
value can't be in an empty set.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNotInEmpty`, not the other rules in that file):

```
# FoldNotInEmpty replaces the NotIn with True when the right input is empty.
# Note that this is correct even if the left side is Null, since even an unknown
# value can't be in an empty set.
[FoldNotInEmpty, Normalize]
(NotIn * (Tuple []))
=>
(True)
```
