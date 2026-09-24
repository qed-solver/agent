# Name: ConvertCountToCountRows
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

ConvertCountToCountRows replaces a Count operator performed on a non-null
expression with a CountRows operator. CountRows is significantly faster to
execute than Count.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `ConvertCountToCountRows`, not the other rules in that file):

```
# ConvertCountToCountRows replaces a Count operator performed on a non-null
# expression with a CountRows operator. CountRows is significantly faster to
# execute than Count.
[ConvertCountToCountRows, Normalize]
(GroupBy | ScalarGroupBy
    $input:*
    $aggregations:[
        ...
        $item:(AggregationsItem (Count $arg:*)) &
            (ExprIsNeverNull $arg (NotNullCols $input))
        ...
    ]
    $groupingPrivate:*
)
=>
((OpName)
    $input
    (ReplaceAggregationsItem $aggregations $item (CountRows))
    $groupingPrivate
)
```
