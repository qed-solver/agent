# BooleanSimplification

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** each conjunct is a two-disjunct disjunction that shares exactly one common factor, with one residual disjunct per side


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 396-639
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the "common factor elimination for conjunction" sub-case of Spark's BooleanSimplification — `(A∨B) ∧ (A∨C) ⟹ A ∨ (B∧C)` — which is exactly what the source produces for a pair of two-disjunct disjunctions sharing one factor; A is correctly shared as the common factor with B/C as distinct residuals, A/B/C are genuinely uninterpreted predicates (not baked-in concrete comparisons), the And/Or operators and nesting match, and this particular case carries no nullability precondition. before() and after() are structurally distinct (a conjunction of disjunctions vs. a disjunction containing a conjunction), so the proof is non-vacuous, and the PARTIAL scope tag is honest and specific about the shape restriction (a genuine, non-degenerate special case rather than the full rule).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6097166
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 29979583
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1056833
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 339291
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16771042
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 30074333
  },
  "total_duration": {
    "secs": 0,
    "nanos": 62308208
  }
}
```
