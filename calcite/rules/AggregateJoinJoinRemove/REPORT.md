# AggregateJoinJoinRemove

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 60  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateJoinJoinRemoveRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness hinges on the null-extension branch of the bottom left join: rows l with no matching m (¬∃m. PB(l,m)) must be shown to survive via null-extension and then collapse under the distinct group-by, so proving the rewrite requires case-splitting on the existence of a match for an uninterpreted join condition PB — i.e. reasoning about the existential image of an uninterpreted predicate over independent symbols, which QED's bag-semantics normal-form/unification pipeline cannot do (its prover explicitly cannot reason about predicate inference/entailment between independent symbols). The porter's Probe 1 confirms this at the core: even the simplest non-trivial instance, a pure distinct group-by (no aggregate calls) over the nested left join vs. the single left join, was not provable, and no special case can remove that dichotomy without ceasing to be a left-join rule (a true-condition join still leaves the ∃m / M-empty case split over an uninterpreted table). ```
