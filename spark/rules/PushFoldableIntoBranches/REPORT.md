# PushFoldableIntoBranches

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 106  **Verification rounds used:** 6
**Scope detail:** encodes only the unary form U(If(c, t, f)) ⟹ If(c, U(t), U(f)); Spark's CaseWhen and binary-expression-with-foldable-operand variants are omitted


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 788-855
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and faithful to its stated fragment: before() = U(CASE(cond,t,f)) vs after() = CASE(cond,U(t),U(f)) are structurally distinct, and since the equivalence would be false if "CASE" were merely uninterpreted (a simple SMT countermodel exists), QED's PROVABLE verdict confirms it modeled a genuine ite, proving the real pointwise distribution U(ite)=ite∘U for an arbitrary uninterpreted U, cond, and independent branch columns t/f (same branch type being faithful, since Spark's If requires a common branch type). Spark's supportedUnaryExpression whitelist and atMostOneUnfoldable guard are determinism/profitability heuristics, not semantic preconditions — the uninterpreted deterministic U covers exactly the whitelisted semantics, so nothing is silently assumed. The `// SCOPE: PARTIAL` line is honest and specific (unary-If form only; CaseWhen and binary-with-foldable-operand variants omitted), and the proved fragment is a genuine, non-degenerate piece of the source rule rather than a narrowed-to-identity trick.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5948291
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36657375
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1053625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 393000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16601500
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36743625
  },
  "total_duration": {
    "secs": 0,
    "nanos": 68953625
  }
}
```
