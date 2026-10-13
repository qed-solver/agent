# FilterAcceptsAllToNoop

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 6  **Verification rounds used:** 2
**Scope detail:** assumes the filter predicate is exactly the TRUE literal rather than any predicate that simplifies to AcceptsAll


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_filter.rs, lines 43-236
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful to the `AcceptsAll` branch of the source rule; while it is a narrower special case than the full `EliminateFilter` rule (as correctly noted in the SCOPE tag), it represents the fundamental logical identity that the source rule relies on. It is non-trivial (the before and after nodes are structurally distinct) and the use of `trueLiteral()` is the most general form of an "accepts all" predicate that the QED prover can verify as semantically equivalent to a no-op. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
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
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 368625
  }
}
```
