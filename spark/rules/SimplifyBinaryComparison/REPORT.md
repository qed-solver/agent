# SimplifyBinaryComparison

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 43  **Verification rounds used:** 3
**Scope detail:** only the `a = a` (EqualTo) case, simplified to the true literal under the condition that a is non-nullable


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 649-697
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is non-vacuous (Filter(a=a) vs Filter(true) are structurally different), and the `a, a` symbol sharing is exactly the rule's trigger (`semanticEquals` on both sides), not an accidental over-constraint — had the porter used two independent fields, the rewrite would be unsound and unprovable. The concrete `SqlStdOperatorTable.EQUALS` is the correct choice rather than an under-generalization: the `x = x → true` rewrite depends on equality's reflexivity, which an uninterpreted predicate on the diagonal could not establish, and the required non-nullability precondition is encoded via the non-nullable column type. The `SCOPE: PARTIAL` line is accurate and specific — only the EqualTo self-comparison-to-true case, in a filter context, under the static-typing (non-nullable) subcondition rather than the `IsNotNull`-conjunct path — so this is an honestly labeled, genuine, non-degenerate special case of the source rule.

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
    "nanos": 267708
  }
}
```
