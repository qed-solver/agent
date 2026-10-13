# IntersectReorder

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** a fixed 3-cycle reordering of a 3-input set (non-ALL) INTERSECT is verified; Calcite's full rule reorders the inputs of any arity by row count, which is a cost heuristic rather than a single fixed equivalence.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/IntersectReorderRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

before (A∩B∩C) and after (C∩A∩B) differ by a genuine 3-cycle permutation of three distinct uninterpreted inputs sharing one type, so the proof is non-vacuous and captures exactly the rule's semantic core (permutation invariance of INTERSECT), while Calcite's cost-sorted any-arity reordering is a heuristic family that no single equivalence can express — the PARTIAL scope note states this accurately. The encoding's n-ary set (all=false) INTERSECT matches the actual shape of the source rewrite (pushAll + n-ary intersect on LogicalIntersect) and QED's set-variant-only INTERSECT modeling, which is what justifies excluding INTERSECT ALL. There is no symbol-sharing error (A/B/C are independent scans; shared type "T" is required since intersect inputs must be type-compatible), and the source rule imposes no preconditions beyond inputs > 1 that the encoding silently drops. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9887500
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 39827917
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 775417
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 639958
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 24822583
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 39947083
  },
  "total_duration": {
    "secs": 0,
    "nanos": 80958875
  }
}
```
