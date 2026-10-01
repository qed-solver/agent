# EmptyOptionalJoinSideToNullPadded

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** the source rule's family covers LEFT/RIGHT/FULL joins of arbitrary width where one side is the empty relation; this encoding pins the LEFT join with an empty right side and a fixed two-column shape for both sides.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/propagate_empty_relation.rs, lines 75-175
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the LEFT-join-with-empty-right branch of the source rule: `before()` is a LEFT join of a left relation against a zero-row `Empty` (correctly modeling `produce_one_row = false`) of the right's type, and `after()` projects the left columns plus typed NULL literals for the right columns — a genuinely non-trivial Join→Project equivalence that holds under bag semantics because a LEFT join against an empty relation null-extends every surviving row. The uninterpreted join condition and the independent left/right type symbols are shared correctly (the condition is irrelevant when the right side is empty, and the NULL literal types `r0`/`r1` match the join's null-extension types), with no accidental over-constraint. The SCOPE tag honestly and specifically records the genuine DSL-imposed pinning to a single join type, a single empty side, and fixed two-column arity (variable arity and join-type-parameterized outer-join semantics are not expressible in the current pattern language), making this a valid, non-degenerate partial port of a real rule branch. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 3621000
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6325209
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 50041
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 356708
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 7911541
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6358833
  },
  "total_duration": {
    "secs": 0,
    "nanos": 16796000
  }
}
```
