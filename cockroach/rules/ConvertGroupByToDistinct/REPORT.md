# ConvertGroupByToDistinct

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** the input has exactly two columns, which are the grouping columns (the DSL's scan has fixed arity); a no-agg GroupBy's output depends only on its grouping columns, so the only loss is the number of grouping columns.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

ConvertGroupByToDistinct converts a GroupBy operator that has no aggregations
to an equivalent DistinctOn operator.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `ConvertGroupByToDistinct`, not the other rules in that file):

```
# ConvertGroupByToDistinct converts a GroupBy operator that has no aggregations
# to an equivalent DistinctOn operator.
[ConvertGroupByToDistinct, Normalize]
(GroupBy $input:* $aggregations:[] $groupingPrivate:*)
=>
(DistinctOn $input $aggregations $groupingPrivate)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding has the correct relational shape for the source rule: a no-aggregation `Aggregate` (group set = all columns, empty agg list) on a single shared input, rewritten to a set-variant self-intersect (`all=false`, the only INTERSECT QED models/serializes), which is exactly what a DistinctOn with empty aggregations computes — unique rows on the grouping columns — and the Optgen rule is unconditional, so no preconditions are silently dropped. The fixed 2-column arity is a genuine limitation of the fixed-arity DSL rather than a hard-coded concrete symbol (no baked-in predicates or join kinds; the "no extra input columns" choice is semantically inert since a no-agg GroupBy's output depends only on its grouping keys), and this restriction is honestly disclosed in the `// SCOPE: PARTIAL` line. The two sides are structurally different operators (group-by vs. set-intersect) over the same symbol, so the proof establishes a real, non-trivial equivalence — a faithful special case of ConvertGroupByToDistinct, not a vacuous one.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 12153332
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 29649875
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1028708
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 727583
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 29163125
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 29764125
  },
  "total_duration": {
    "secs": 0,
    "nanos": 76884625
  }
}
```
