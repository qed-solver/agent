# Name: FoldEqualsAnyNull
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldEqualsAnyNull, converts a scalar ANY operation to NULL if the right-hand
side tuple is NULL, e.g. x = ANY(NULL::int[]). See #42562.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldEqualsAnyNull`, not the other rules in that file):

```
# FoldEqualsAnyNull, converts a scalar ANY operation to NULL if the right-hand
# side tuple is NULL, e.g. x = ANY(NULL::int[]). See #42562.
[FoldEqualsAnyNull, Normalize]
(AnyScalar * (Null) *)
=>
(Null (BoolType))
```
