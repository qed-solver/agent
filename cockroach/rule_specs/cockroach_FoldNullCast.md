# Name: FoldNullCast
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullCast discards the cast operator if it has a null input. The resulting
null value has the same type as the Cast operator would have had.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullCast`, not the other rules in that file):

```
# FoldNullCast discards the cast operator if it has a null input. The resulting
# null value has the same type as the Cast operator would have had.
[FoldNullCast, Normalize]
(Cast $input:(Null) $targetTyp:*)
=>
(Null $targetTyp)
```
