# ConstantFolding

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 41  **Verification rounds used:** 3
**Scope detail:** the folded expression is the specific closed boolean expression true AND false, replaced by the literal false.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 50-122
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is not vacuous — before() contains a real AND node (`true ∧ false`) that after() eliminates, which is precisely the literal-evaluation step Spark's `ConstantFolding` performs on this fully-literal foldable expression, applied in a legitimate expression context (a Filter's condition, which `mapExpressions` rewrites in place). The narrowing to one specific closed boolean formula is forced by a genuine QED limitation rather than a porter error: uninterpreted symbols carry no evaluation algebra, so the general claim "any foldable expression equals its evaluated literal" is unstateable/unprovable in this framework, and the `SCOPE: PARTIAL` tag names the assumed condition accurately. Symbol sharing is correct (the same scan is the input relation in both plans, and no independent symbol is needed), and no preconditions (determinism, non-null literals) are silently missing for this fully-constant case.

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
    "nanos": 315583
  }
}
```
