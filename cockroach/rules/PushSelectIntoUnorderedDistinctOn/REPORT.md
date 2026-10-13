# PushSelectIntoUnorderedDistinctOn

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** identity unordered DistinctOn only (every input column is a grouping column and there are no aggregate outputs, so every filter is bound by the input columns and the kept/unbound remainder is vacuous); the rule's ConstAgg/FirstAgg-column case is unmodelable because QED's aggregates are uninterpreted.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoUnorderedDistinctOn pushes Select filters into the input of an
unordered DistinctOn. Unlike PushSelectIntoGroupBy, which only pushes filters
on grouping or ConstAgg columns, this rule pushes filters that reference any
column of the DistinctOn's input — including first-agg columns.

This is valid because an unordered DistinctOn can return any row from each
group. Filtering before the DistinctOn simply constrains which rows are
available to be chosen; groups that are entirely eliminated by the filter
produce no output row, which is equivalent to choosing a row and then
filtering it away afterward.

The rule is restricted to unordered DistinctOn: an ordered DistinctOn must
pick a specific row determined by the ordering, so pre-filtering could change
which row is selected.

Note: Do not add EnsureDistinctOn to the match pattern. Pushing the select
filters through the EnsureDistinctOn can prevent it from detecting duplicate
rows and therefore change error behavior.

Filters referencing outer columns are not pushed; they remain above the
DistinctOn.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoUnorderedDistinctOn`, not the other rules in that file):

```
# PushSelectIntoUnorderedDistinctOn pushes Select filters into the input of an
# unordered DistinctOn. Unlike PushSelectIntoGroupBy, which only pushes filters
# on grouping or ConstAgg columns, this rule pushes filters that reference any
# column of the DistinctOn's input — including first-agg columns.
#
# This is valid because an unordered DistinctOn can return any row from each
# group. Filtering before the DistinctOn simply constrains which rows are
# available to be chosen; groups that are entirely eliminated by the filter
# produce no output row, which is equivalent to choosing a row and then
# filtering it away afterward.
#
# The rule is restricted to unordered DistinctOn: an ordered DistinctOn must
# pick a specific row determined by the ordering, so pre-filtering could change
# which row is selected.
#
# Note: Do not add EnsureDistinctOn to the match pattern. Pushing the select
# filters through the EnsureDistinctOn can prevent it from detecting duplicate
# rows and therefore change error behavior.
#
# Filters referencing outer columns are not pushed; they remain above the
# DistinctOn.
[PushSelectIntoUnorderedDistinctOn, Normalize]
(Select
    $input:(DistinctOn
        $groupingInput:*
        $aggregations:*
        $groupingPrivate:* &
            (IsUnorderedGrouping $groupingPrivate)
    )
    $filters:[
        ...
        $item:* &
            (IsBoundBy
                $item
                $inputCols:(OutputCols $groupingInput)
            )
        ...
    ]
)
=>
(Select
    (DistinctOn
        (Select
            $groupingInput
            (ExtractBoundConditions $filters $inputCols)
        )
        $aggregations
        $groupingPrivate
    )
    (ExtractUnboundConditions $filters $inputCols)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-degenerate PARTIAL special case: it models the identity (unordered, no-aggregate) DistinctOn as a group-by-all-columns Aggregate with zero agg calls, so `before()` (filter above the distinct) and `after()` (filter below it) are structurally distinct and the proof is a genuine commute-with-distinct result rather than a vacuous identity. Predicates P/R are properly uninterpreted and shared consistently across the aggregate, matching the source rule's same-filter-conjuncts semantics. The scope tag is honest and specific — the general FirstAgg/ConstAgg-column case is unreachable for a real reason (QED's aggregates are uninterpreted, so it cannot reason about a filter referencing a non-grouping output column), not a missing DSL capability — and the identity restriction is a legitimate instance the source rule actually matches.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5540875
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 5410208
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 58708
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 409625
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 11279292
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 5446875
  },
  "total_duration": {
    "secs": 0,
    "nanos": 19226791
  }
}
```
