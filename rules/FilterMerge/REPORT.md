# FilterMerge

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache Calcite
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** public record FilterMerge() implements RRule {


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterMergeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

before() is a genuinely nested Filter(Filter(scan)) while after() is a single Filter over an uninterpreted conjunction, so the proof is of the actual merge, not a vacuous structural identity. The input scan carries no key or guaranteed constraints (so in QED's translation it is a fully uninterpreted relation, faithfully standing in for the operand tree's `anyInputs`), and "inner"/"outer" are two distinct uninterpreted predicates referenced identically on both sides, exactly matching Calcite's onMatch (push(bottomFilter.getInput()).filter(bottomCond, topCond)), which RelBuilder implements as an AND. The source rule has no side conditions (no keys, nullability guarantees, etc.), so SCOPE: FULL is honest. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5273667
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35867625
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 892333
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 311375
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 15810500
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35957541
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67433250
  }
}
```
