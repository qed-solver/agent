# EmptyAggregateToEmpty

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 25  **Verification rounds used:** 3
**Scope detail:** only the Aggregate branch of DataFusion's PropagateEmptyRelation (group-by over an empty relation, with group expressions present) is encoded; the Projection, Filter, Window, Sort, and Join branches are not modeled.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/propagate_empty_relation.rs, lines 176-189
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` is `Aggregate(Empty)` and `after()` is a structurally distinct `Empty` values node carrying the aggregate's output schema, so the proved equivalence (group-by over empty input ⇒ empty bag) is non-vacuous and matches DataFusion's PropagateEmptyRelation aggregate branch exactly; the group key and aggregate call are uninterpreted symbols over the single empty child, the "no empty grouping set" precondition is satisfied by using a plain `group-by` (no grouping sets modeled), and the `PARTIAL` scope tag is honest and specific.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 73708
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 0
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 59167
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 234042
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 2183334
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 4486666
  }
}
```
