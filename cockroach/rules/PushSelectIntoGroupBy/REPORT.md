# PushSelectIntoGroupBy

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 26  **Verification rounds used:** 3
**Scope detail:** only the GroupBy branch of the source rule is modeled (not the DistinctOn alternative), over an uninterpreted two-column base relation with a single identity grouping column and one non-distinct aggregate call, with every filter conjunct referencing only the grouping column (the rule's ConstAgg-column case is unmodelable: QED's aggregates are uninterpreted, so it cannot see that a ConstAgg's output is per-group-constant).


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoGroupBy pushes a Select condition below a GroupBy in the case
where it only references grouping columns or ConstAgg columns.

This rule doesn't work on ScalarGroupBy which exhibits different behavior if
the input is empty:
SELECT MAX(y) FROM a

If "a" is empty, this returns a single row containing a null value. This is
different behavior than a GroupBy with grouping columns, which would return
the empty set for a similar query:
SELECT MAX(y) FROM a GROUP BY x

Citations: [2]

Note: Do not add EnsureDistinctOn to the match pattern. Pushing the select
filters through the EnsureDistinctOn can prevent it from detecting duplicate
rows and therefore change error behavior.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoGroupBy`, not the other rules in that file):

```
# PushSelectIntoGroupBy pushes a Select condition below a GroupBy in the case
# where it only references grouping columns or ConstAgg columns.
#
# This rule doesn't work on ScalarGroupBy which exhibits different behavior if
# the input is empty:
#   SELECT MAX(y) FROM a
#
# If "a" is empty, this returns a single row containing a null value. This is
# different behavior than a GroupBy with grouping columns, which would return
# the empty set for a similar query:
#   SELECT MAX(y) FROM a GROUP BY x
#
# Citations: [2]
#
# Note: Do not add EnsureDistinctOn to the match pattern. Pushing the select
# filters through the EnsureDistinctOn can prevent it from detecting duplicate
# rows and therefore change error behavior.
[PushSelectIntoGroupBy, Normalize]
(Select
    $input:(GroupBy | DistinctOn
        $groupingInput:*
        $aggregations:*
        $groupingPrivate:*
    )
    $filters:[
        ...
        $item:* &
            (IsBoundBy
                $item
                $passthrough:(GroupingAndConstCols
                    $groupingPrivate
                    $aggregations
                )
            )
        ...
    ]
)
=>
(Select
    ((OpName $input)
        (Select
            $groupingInput
            (ExtractBoundConditions $filters $passthrough)
        )
        $aggregations
        $groupingPrivate
    )
    (ExtractUnboundConditions $filters $passthrough)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (Filter above the Aggregate) and `after()` (Filter inside, below the Aggregate) are structurally different plans, so the equivalence proof is non-vacuous and captures the rule's actual optimization—pushing a filter on the grouping column beneath the GroupBy. The uninterpreted filter predicates P/R, the aggregate `f`, and the group key are correctly shared across both sides (the same predicate applied to the same logical group-key column, referenced in the input below and the aggregate output above), so this is a faithful symbol sharing rather than a coincidental over-constraint, and since every conjunct is bound the vacuous outer `Select` is rightly omitted per `EliminateSelect`. The restriction to the GroupBy branch (not DistinctOn), a single identity group key, one non-distinct uninterpreted aggregate, and grouping-column-only conjuncts is a genuine, honestly-flagged PARTIAL scope (the ConstAgg case is correctly deemed unmodelable because QED's aggregates are uninterpreted and cannot see per-group constancy), and the result remains a useful, non-degenerate special case.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 10467584
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 48930708
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 872792
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 795000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 25190333
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 49214208
  },
  "total_duration": {
    "secs": 0,
    "nanos": 90020791
  }
}
```
