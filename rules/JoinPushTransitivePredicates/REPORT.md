# JoinPushTransitivePredicates

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinPushTransitivePredicatesRule.java
```

## Independent verifier review

**Verdict:** AGREE

This rule is metadata-driven predicate inference, not a structural rewrite — Calcite decomposes the join condition into equi-join equivalence classes and pushes rewrites of pulled-up predicates, so the added filters are valid only as logical consequences of the existing ones (e.g., l.a=r.b ∧ l.a=5 ⇒ r.b=5). In RuleScript the join condition and all filters are independent uninterpreted symbols, and QED explicitly cannot reason about predicate inference/entailment between independent [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]
