# FilterSetOpTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** the 2-input UNION ALL instance with both inputs sharing one column type, whereas Calcite's rule applies to any set-op kind (UNION/INTERSECT/MINUS, ALL or DISTINCT) of any arity.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterSetOpTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

before() = Filter(P, UnionAll(L, R)) and after() = UnionAll(Filter(P, L), Filter(P, R)) are structurally distinct and exactly the pushdown the source rule performs, with the details faithful: "P" is one uninterpreted predicate symbol shared across all three occurrences (matching the source rule reusing the same condition over each input after identity column remapping), `union(true, ...)` is a legitimate SetOp instance with the correct ALL flag, and the source rule has no hidden preconditions (keys, nullability) that the encoding silently drops. The PARTIAL tag is honest and specific — 2-input UNION ALL is one instance of the rule, where other arities/kinds need separate encodings and bag INTERSECT/MINUS are genuinely outside QED's set-only semantics — and the proven special case remains a useful, non-degenerate rewrite.

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
    "nanos": 803875
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 443167
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
    "nanos": 1623125
  }
}
```
