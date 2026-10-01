# ExtractLeafExpressions

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** only the Filter-parent case with exactly one extracted sub-expression over a single-column input: Filter(P(e(x)), R) is rewritten by projecting e(x) into a new column below the filter, filtering on that column, and dropping it with a final projection so the output schema is unchanged.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/extract_leaf_expressions.rs, lines 164-744
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and correctly shared: before() = Filter(R, P(e(x))) differs structurally from after() = π_x(σ_{P(e(x))}(π_{e(x),x}(R))), with the same uninterpreted symbols e (leaf projection) and P (predicate) used on both sides, no uniqueness flag or other precondition assumed, and the exact shape of DataFusion's Filter-parent case (extraction projection inserted below the filter with the extracted column, filter rewritten to reference the materialized column, recovery projection dropping it to restore the output schema). The scope line is accurate and specific: this is a declared PARTIAL special case — Filter parent only, single-column input, one extracted expression — rather than a full rule covering Aggregate/Join/Sort/Limit and N extractions, but it is a genuine, non-degenerate core instance of the transformation (nothing that should be a symbol was hard-coded concrete), so QED's provable result is a real, faithful proof of a meaningful fragment of the source rule. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5314875
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35899333
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 908375
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 377750
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 15604834
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35976292
  },
  "total_duration": {
    "secs": 0,
    "nanos": 66765042
  }
}
```
