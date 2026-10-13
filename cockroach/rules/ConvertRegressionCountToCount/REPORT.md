# ConvertRegressionCountToCount

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 67  **Verification rounds used:** 4

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

The rewrite's correctness rests entirely on a specific algebraic identity between two *different* named aggregates — `RegressionCount(a,b)` (counts rows where both args are non-null) equaling `Count(b)` (counts rows where one arg is non-null) under the side condition that `a` is provably never-null — and QED treats aggregate names as uninterpreted beyond bag equality of their input, with no mechanism in the Java DSL or JSON format to declare such a semantic axiom (the prover is Rust-side and untouchable). Any genuine instance of this rule necessarily changes the aggregate's name and arity, so no non-vacuous special case survives: even the narrowest encoding (e.g. one arg a non-nullable-typed column) still requires the prover to bridge an opaque 2-operand `RegressionCount` to an interpreted 1-operand `Count`, which SMT will refute by choosing an interpretation of the uninterpreted aggregate that disagrees with the count. This is precisely the documented limitation "knows nothing about a specific aggregate function's algebra beyond bag equality of its input" / "a backend operator's bespoke internal semantics," not a gap `extend_dsl_file` could close (multi-operand `AggCall` already exists; only the prover-side semantics are missing). ```
