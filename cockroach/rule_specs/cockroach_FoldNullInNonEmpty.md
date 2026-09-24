# Name: FoldNullInNonEmpty
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullInNonEmpty replaces the In/NotIn with null when the left input is
null and the right input is not empty. Null is the unknown value, and if the
set is non-empty, it is unknown whether it's in/not in the set.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullInNonEmpty`, not the other rules in that file):

```
# FoldNullInNonEmpty replaces the In/NotIn with null when the left input is
# null and the right input is not empty. Null is the unknown value, and if the
# set is non-empty, it is unknown whether it's in/not in the set.
[FoldNullInNonEmpty, Normalize]
(In | NotIn (Null) (Tuple ^[]))
=>
(Null (BoolType))
```
