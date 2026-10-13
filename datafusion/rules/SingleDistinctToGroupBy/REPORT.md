# SingleDistinctToGroupBy

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** the aggregate contains only single-argument DISTINCT aggregates (two, sharing the same one distinct column) with exactly one group-by key and no non-distinct sum/min/max or count-rollup aggregates, which the source rule also permits alongside the distinct field.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/single_distinct_to_groupby.rs, lines 1-433
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is non-vacuous and shape-faithful: before() is a single-level DISTINCT aggregate while after() reproduces exactly what the DataFusion rule emits in the all-distinct case (inner GROUP BY (g, x) with an empty aggregate list as pure deduplication, outer plain re-aggregation over the deduplicated column, trailing identity/alias project), and QED's per-group operand-bag check is precisely the semantic content of the rewrite. All semantic content (f1, f2, g, x, types) is left uninterpreted, the single shared distinct argument matches the source rule's `fields_set.len() == 1` precondition with f1/f2 kept independent, and the source rule imposes no uniqueness/NOT-NULL precondition that the encoding would be silently dropping. The PARTIAL scope tag is honest and specific — excluding non-distinct sum/min/max/count-rollup aggregates is forced by QED (two-phase rollup needs algebra of concrete functions QED does not model), and the remaining restrictions (exactly two distinct aggregates, one group key) are disclosed pattern arities rather than semantic hard-coding, leaving a genuine, non-degenerate special case of the rule's core transformation.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 17495085
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 58379416
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1018417
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1281833
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 39469584
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 58735458
  },
  "total_duration": {
    "secs": 0,
    "nanos": 114602625
  }
}
```
