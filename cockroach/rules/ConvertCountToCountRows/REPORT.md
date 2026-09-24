# ConvertCountToCountRows

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 40  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

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
```

## Independent verifier review

**Verdict:** AGREE

ConvertCountToCountRows is valid only by virtue of the counting algebra of the COUNT operator itself — that COUNT(x) equals the group's row count when x is never NULL — and its ExprIsNeverNull side condition is exactly the nullability inference that supplies that premise. QED models every aggregate call as an uninterpreted function of its per-group operand bag (per its own evaluation it knows nothing about a specific aggregate's algebra beyond bag equality of its input), so a `Count(x)` call (operand: x) can never be equated with an operand-free `CountRows` call in any encoding, not even one that gives x a non-nullable scan type, since the operator symbols differ and the operand bags differ. The only instances QED could prove are vacuous (e.g., an empty input where both sides collapse to the empty bag) or self-identical encodings that no longer express the rule, so the unsupported conclusion is correct even though the porter's transcript shows it was actually aborted by a context-length error rather than this analysis. ```
