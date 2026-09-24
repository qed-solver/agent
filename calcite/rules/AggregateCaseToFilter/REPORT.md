# AggregateCaseToFilter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateCaseToFilterRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests on aggregate-function algebra that QED explicitly does not model — that null-skipping aggregates (COUNT/SUM) ignore NULL inputs, that SUM0 is additive with 0/NULL as neutral, and that COUNT of a non-null constant equals counting rows — and per its own evaluation QED can only equate aggregates whose input bags are equal, whereas here the two sides aggregate different bags (CASE values over all rows, NULLs from the else branch included, versus plain values over only the rows satisfying the filter). The gap is on the prover side, not the DSL: even extending the DSL (which today also lacks a `filterArg` on `RelRN.AggCall` and any CASE/NULL literal in `RexRN`, so neither side is expressible as-is) could not make the equivalence derivable, since the null-skipping/sum identity is precisely the "bespoke internal semantics of an aggregate operator" QED cannot see through.
