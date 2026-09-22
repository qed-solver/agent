# FilterJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 37  **Verification rounds used:** 2
**Scope detail:** INNER join only; the above filter is merged into the join condition rather than being classified by referenced side


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterJoinRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

before() and after() are structurally different (Filter above an inner join vs. the AND-composed join condition), and the symbol sharing is exactly right: the same `joinCond` and `aboveFilter` occur on both sides, they are two independent uninterpreted predicates over the join row (distinct operator names, same field list), and L/R are independent scans — so the proof is neither vacuous nor coincidentally over-constrained. For an INNER join, this merged form is bag-equivalent to what the source rule actually emits (it splits conjuncts into child filters + ON condition, which is semantically neutral under inner joins), and INNER is precisely the only join kind for which the unclassified "merge the whole filter into the condition" step is valid — matching the honest PARTIAL scope tag. The single-column scans are a DSL convention rather than a weakening, since both predicates are fully uninterpreted over the product row and the filter/join-commutation claim is width-independent. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6984584
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 39877709
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 859583
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 424417
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19254125
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 39975000
  },
  "total_duration": {
    "secs": 0,
    "nanos": 75058208
  }
}
```
