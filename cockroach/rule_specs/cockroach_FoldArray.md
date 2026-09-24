# Name: FoldArray
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldArray evaluates an Array expression with constant inputs. It replaces the
Array with a Const datum with type TArray.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldArray`, not the other rules in that file):

```
# FoldArray evaluates an Array expression with constant inputs. It replaces the
# Array with a Const datum with type TArray.
[FoldArray, Normalize]
(Array $elems:* & (IsListOfConstants $elems) $typ:*)
=>
(FoldArray $elems $typ)
```
