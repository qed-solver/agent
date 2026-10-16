# ConvertRegressionCountToCount

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 27  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

ConvertRegressionCountToCount replaces a RegressionCount operator
performed on a non-null expression with a Count operator. Count can be
normalized again to CountRows which is significantly faster to execute
than RegressionCount.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `ConvertRegressionCountToCount`, not the other rules in that file):

```
# ConvertRegressionCountToCount replaces a RegressionCount operator
# performed on a non-null expression with a Count operator. Count can be
# normalized again to CountRows which is significantly faster to execute
# than RegressionCount.
[ConvertRegressionCountToCount, Normalize]
(GroupBy | ScalarGroupBy
    $input:*
    $aggregations:[
        ...
        $item:(AggregationsItem
                (RegressionCount $arg1:* $arg2:*)
            ) &
            (Let
                ($newArg $ok):(SingleRegressionCountArgument
                    $arg1
                    $arg2
                    $input
                )
                $ok
            )
        ...
    ]
    $groupingPrivate:*
)
=>
((OpName)
    $input
    (ReplaceAggregationsItem $aggregations $item (Count $newArg))
    $groupingPrivate
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests on an algebraic identity between two distinct aggregate functions — regr_count(y,x) ≡ count(x) when one argument is never-null — but QED models every aggregate as an independent uninterpreted function of the group bag keyed only by operator name (the serializer emits only name, operands, distinct, ignoreNulls, type, and the prover has no count/regr axioms), so no RuleScript encoding, including marking the never-null argument's column type non-nullable, can make the prover relate two differently-named aggregate calls. Making the names identical would no longer be the rule, and adding aggregate axioms would require modifying the Rust prover, which is out of bounds. The porter's non-timeout `provable=false` is therefore the expected countermodel of QED's semantics — a genuine missing-aggregate-algebra limitation, not a fixable symbol-sharing or modeling bug.
