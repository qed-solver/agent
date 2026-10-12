# OptimizeWindowFunctions

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 10  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2058-2069
```

## Independent verifier review

**Verdict:** AGREE

This rule is a pure window-function substitution (first(col) → nth_value(col, 1) under an identical ROWS frame), and its validity rests entirely on the order- and frame-dependent identity of those two aggregates — "value of the first-ordered row of the partition" — which requires ordering semantics that QED does not model (Sort/Limit/Offset/Window have no bag-semantic meaning in the prover). Since the core language exposes aggregates only as uninterpreted symbols related by bag equality of their inputs, a faithful encoding with two distinct symbols for first and nth_value gives QED no way to relate them (as the porter's empirical provable=false confirms), while collapsing them to one shared symbol is a vacuous tautology that assumes rather than verifies the very identity in question. ```
