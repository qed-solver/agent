# OrCommonFactorDistribution

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** each disjunct is a two-conjunct conjunction that shares exactly one common factor, with one residual conjunct per side


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1018-1039
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous instance of the rule: `before()` is `Filter((A∧B)∨(A∧C))` and `after()` is `Filter(A∧(B∨C))` over the same scan with A, B, C as genuinely independent uninterpreted predicates, and reusing symbol `A` across both disjuncts correctly mirrors the source rule's requirement (via `lhs.contains(e)`) that the factored expression be literally identical on both sides. The equivalence `(A∧B)∨(A∧C) = A∧(B∨C)` holds under both two- and three-valued logic and needs no extra preconditions, so the proven claim matches the real rewrite. The scope line is honest and specific: the general DataFusion rule handles arbitrary numbers of conjunctions and common factors, but the DSL can only build fixed-arity patterns, so the 2+2 conjunctions / 1 common factor / 1 residual per side case is a genuine, non-degenerate, textbook-minimal special case that is correctly declared as PARTIAL rather than overclaimed as FULL. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5648624
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 38013250
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 825250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 319208
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16734250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 38112292
  },
  "total_duration": {
    "secs": 0,
    "nanos": 70148791
  }
}
```
