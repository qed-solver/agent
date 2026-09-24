# Name: SimplifyCoalesce
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifyCoalesce discards any leading null operands, and then if the next
operand is a constant, replaces with that constant. Note that ConstValue
matches nulls as well as other constants.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyCoalesce`, not the other rules in that file):

```
# SimplifyCoalesce discards any leading null operands, and then if the next
# operand is a constant, replaces with that constant. Note that ConstValue
# matches nulls as well as other constants.
[SimplifyCoalesce, Normalize]
(Coalesce
    $args:[
        $arg:* & (IsConstValueOrGroupOfConstValues $arg)
        ...
    ]
)
=>
(SimplifyCoalesce $args)
```
