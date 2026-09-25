# MinusToDistinct

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MinusToDistinctRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's after-pattern encodes set difference purely through cardinality — `COUNT() FILTER (branch=i)` per group, then a final `Filter(cnt0 > 0 AND cnt1 = 0)` — so proving equivalence requires the count algebra fact that a group's count is 0 iff no row from that branch is present and >0 iff one is, which QED explicitly does not have: it models every aggregate call, including COUNT, as an uninterpreted function of its input bag, and that lives in the fixed Rust prover, not the DSL. No faithful encoding sidesteps this — a faithful port must compare an aggregate output against 0/positivity against MINUS set-difference semantics, which is undecidable with uninterpreted aggregates (and filtered aggregates aren't even representable today: `RelRN.AggCall` has no filter field and `JSONSerializer` drops it, so extending the DSL would only serialize something the prover still can't interpret). The only provable variants are degenerate (e.g., identical inputs collapsing both sides to the empty relation, which validates none of the rule's actual logic) or a different rule entirely (an anti-join formulation, i.e. MinusToAntiJoin), so the porter's UNSUPPORTED conclusion is correct. ```
