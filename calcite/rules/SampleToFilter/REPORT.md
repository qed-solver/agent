# SampleToFilter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 13  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SampleToFilterRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's only claimed equivalence, Sample(R) ≡ Filter(rand() < rate, R), is a probabilistic (Bernoulli) identity — both sides keep rows nondeterministically — while QED is a deterministic decision procedure over bag semantics with uninterpreted functions, in which Sample has no bag meaning at all (same no-semantics class as Sort/Limit/Window) and rand() admits no deterministic function-of-input reading. The two sides can therefore only be modeled as independent uninterpreted symbols (Sample-as-operator vs Filter(P,R), or P vs Q), whose equality QED cannot infer — the porter's probe confirmed this is unprovable — and extend_dsl_file cannot close the gap because no deterministic semantics exists to attach that would not simply hardcode the conclusion rather than prove it. ```
