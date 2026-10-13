# UnionMerge

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** only the UNION ALL instance with the nested union in the second input position; Calcite's rule also covers UNION DISTINCT, INTERSECT, and MINUS.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/UnionMergeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully reproduces Calcite's UnionMerge onMatch for the UNION ALL instance — top union with a nested union in the second input (plus a third input, matching the source's "topOp.getInputs().size() may be more than 2" flattening) rewritten to the flat n-ary union, with the same all=true flag on both sides — and before() and after() are structurally different, so the proof is a genuine, non-vacuous bag-equality result over four independent uninterpreted scans sharing the row type that union requires. The SCOPE tag is accurate and specific: it names exactly the excluded instances (UNION DISTINCT, INTERSECT, MINUS), and the restriction is at least partly a real QED/DSL boundary (only the set variants of INTERSECT/MINUS are modelable — the serializer has no case for their ALL forms), and Calcite itself registers Union/Intersect/Minus merge as three separate rule instances, so this is a useful, non-degenerate special case of the rule family rather than an over-constrained fake. ```

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
    "nanos": 794458
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 656084
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
    "nanos": 1814042
  }
}
```
