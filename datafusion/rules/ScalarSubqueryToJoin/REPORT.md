# ScalarSubqueryToJoin

**Status:** SKIPPED
**Source backend:** Apache DataFusion

## Source rule (as given to the porter)

```
# Name: ScalarSubqueryToJoin
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/scalar_subquery_to_join.rs:1-437
```

## Reason

The rule rewrites a Filter or Projection holding a correlated scalar subquery into a left join against a decorrelated subquery, with compensation expressions for aggregates such as COUNT. QED has no scalar-subquery value: the prover receives it as an opaque operator unrelated to any join. Modelling the subquery as a LEFT correlate over a scalar aggregate and comparing it with a LEFT join against a per-key aggregate is not provable, because an uninterpreted aggregate of the empty bag is not tied to the NULL a left join produces for a missing key. The Count-bug compensation, the alias generation, and the Filter-versus-Projection handling are Rust logic with no DSL counterpart. A narrower encoding would prove only a different identity, so the rule is recorded as skipped.
