# FilterSampleTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 10  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterSampleTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

Sample has no deterministic bag semantics for QED to reason about — Bernoulli sampling is per-row probabilistic and system sampling depends on row positions — and the Rust prover, which cannot be modified, has no model of the operator (qed.pdf explicitly lists Sample among operators with no bag-semantic meaning; even the Java-side JSONSerializer has no LogicalSample case, so a DSL extension could not close the gap). The only encodable stand-in, an uninterpreted per-row predicate, reduces the rule to Filter(P,Filter(S,R)) = Filter(S,Filter(P,R)), i.e. trivial conjunctive filter commutativity — a different theorem, not a special case of the transpose, since the rule's content *is* the Sample operator and every faithful instance must contain it. Hence there is no genuine non-vacuous special case for QED to prove, and the UNSUPPORTED conclusion stands. ```
