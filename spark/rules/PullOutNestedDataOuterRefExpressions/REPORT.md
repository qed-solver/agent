# PullOutNestedDataOuterRefExpressions

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 25  **Verification rounds used:** 3
**Scope detail:** one uninterpreted GetMapValue extraction over an outer column, used in one uninterpreted comparison with an inner column; the correlation context is modeled as an INNER cross-join of the outer and inner scans


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PullOutNestedDataOuterRefExpressions.scala, lines 73-136
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's essential transformation with correct symbol sharing: before() uses the outer-only map-extraction `Gmv(o)` inline in the filter, while after() materializes that same `Gmv(o)` as a new projection column, filters on it, then projects it away — exactly mirroring the source rule's "add the outer expression as a new attribute, reference it, then remove it", and before()/after() are structurally different (so the proof is non-vacuous). The key precondition that makes the optimization sound — the extracted expression referencing only outer columns, not inner ones (`e.references.isEmpty`) — is genuinely respected since `Gmv` is applied to the outer field `o` alone, and the `Gmv`/`Cmp` symbols are the same instances on both sides (no accidental independence). The scope tag honestly concedes it is a PARTIAL special case (single extraction, single comparison, correlation modeled as an INNER cross-join) rather than the fully general rule, and this narrower form remains a genuine, non-degenerate, useful equivalence (materialize-then-drop a computed column vs. computing it inline), which is exactly the core the real rule optimizes.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7022876
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35797958
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1107334
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 554333
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18823167
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35901250
  },
  "total_duration": {
    "secs": 0,
    "nanos": 70465167
  }
}
```
