# HoistSelectAboveUnorderedDistinctOn

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** identity unordered DistinctOn (its output columns are exactly its grouping keys, with no aggregate outputs), and a single hoisted filter conjunct that references only the DistinctOn's output columns, so no FirstAgg synthesis is required.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistSelectAboveUnorderedDistinctOn hoists correlated filter conditions from
inside an unordered DistinctOn's input Select to above the DistinctOn. This
is the reverse of PushSelectIntoUnorderedDistinctOn and is valid because an
unordered DistinctOn can choose any row from each group, so filtering before
or after the grouping produces equivalent results.

This rule aids decorrelation by moving correlated filters to a position where
TryDecorrelateSelect can handle them, avoiding the more expensive
TryDecorrelateGroupBy transformation that requires EnsureKey and additional
ConstAgg columns.

Uncorrelated filters remain inside the DistinctOn for early filtering. If a
correlated filter references an input column that is not already in the
DistinctOn's output, a FirstAgg aggregation is added for that column so
it becomes available above the DistinctOn.

Example:
DistinctOn(Select(input, [s = outer.s, i > 0]), aggs, priv)
=>
Select(DistinctOn(Select(input, [i > 0]), aggs', priv), [s = outer.s])
(where aggs' = aggs + FirstAgg(s) if s was not already projected)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistSelectAboveUnorderedDistinctOn`, not the other rules in that file):

```
# HoistSelectAboveUnorderedDistinctOn hoists correlated filter conditions from
# inside an unordered DistinctOn's input Select to above the DistinctOn. This
# is the reverse of PushSelectIntoUnorderedDistinctOn and is valid because an
# unordered DistinctOn can choose any row from each group, so filtering before
# or after the grouping produces equivalent results.
#
# This rule aids decorrelation by moving correlated filters to a position where
# TryDecorrelateSelect can handle them, avoiding the more expensive
# TryDecorrelateGroupBy transformation that requires EnsureKey and additional
# ConstAgg columns.
#
# Uncorrelated filters remain inside the DistinctOn for early filtering. If a
# correlated filter references an input column that is not already in the
# DistinctOn's output, a FirstAgg aggregation is added for that column so
# it becomes available above the DistinctOn.
#
# Example:
#   DistinctOn(Select(input, [s = outer.s, i > 0]), aggs, priv)
#   =>
#   Select(DistinctOn(Select(input, [i > 0]), aggs', priv), [s = outer.s])
#   (where aggs' = aggs + FirstAgg(s) if s was not already projected)
[HoistSelectAboveUnorderedDistinctOn, Normalize]
(DistinctOn
    (Select $input:* $filters:*)
    $aggregations:*
    $groupingPrivate:* &
        (IsUnorderedGrouping $groupingPrivate) &
        (CanHoistCorrelatedFiltersAbove
            $filters
            (OutputCols $input)
        )
)
=>
(Select
    (DistinctOn
        (Select
            $input
            (ExtractBoundConditions $filters (OutputCols $input))
        )
        (AddFirstAggsForHoistedFilters
            $aggregations
            $filters
            (OutputCols $input)
            (GroupingOutputCols $groupingPrivate $aggregations)
        )
        $groupingPrivate
    )
    (ExtractUnboundConditions $filters (OutputCols $input))
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully models the declared special case: an identity, unordered DistinctOn (output = grouping keys, no aggregate outputs) is correctly represented as a group-by over all input fields with no aggregate calls, and the single hoisted filter is one uninterpreted predicate over exactly those output columns, moved from below the grouping in `before()` to above it in `after()`. The two sides are structurally distinct (filter inside the aggregate vs. filter outside) and their equivalence is a genuine, non-vacuous fact — a group-constant predicate commutes with an unordered grouping — proven universally over all instantiations of the predicate, with correct symbol sharing and no vacuity, wrong operator, or silently dropped precondition. The PARTIAL scope tag is honest and specific (identity DistinctOn, no FirstAgg synthesis, group-key-only filter), and this is a real, non-degenerate narrowing of the source rule rather than a degenerate or over-constrained encoding.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7153625
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 25436125
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 847917
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 491916
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18989542
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 25529792
  },
  "total_duration": {
    "secs": 0,
    "nanos": 60043792
  }
}
```
