# UnionPullUpConstants

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 34  **Verification rounds used:** 2
**Scope detail:** the constant column is a boolean literal materialized by an explicit projection below the union-all, rather than a generic constant deduced from table guarantees.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/UnionPullUpConstantsRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is of a real, nontrivial declared special case: a shared boolean-constant projection duplicated under a union-all is hoisted above the union while the nonconstant column is carried through. This matches the source rule's constant-pullup transformation for that instance, and the restrictions (boolean literal, union-all, one constant/nonconstant column, explicit projection) are honest and do not make the equivalence vacuous.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9227666
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 44026083
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 877625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 601000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22798833
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 44187750
  },
  "total_duration": {
    "secs": 0,
    "nanos": 83683833
  }
}
```
