# SampleToFilter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 6  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SampleToFilterRule.java
```

## Independent verifier review

**Verdict:** AGREE

Bernoulli `Sample`'s output is a random per-row selection, not a deterministic function of its input, so it has no bag semantics in QED's SMT model, and the rule's justification rests on statistical properties of `rand` (uniform in [0,1), independent across rows, true with probability exactly the sample rate) that a deterministic bag-equivalence prover neither models nor can verify. The only encodable surrogate — the sampler as an uninterpreted row predicate vs. the `rand(seed) < rate` filter as another independent uninterpreted predicate — is not universally equivalent, so QED would rightly refuse it, while sharing one predicate symbol would only prove the vacuous identity `Filter(P, R) ≡ Filter(P, R)`; even degenerate rate special cases are unprovable since QED cannot know the range or randomness of an uninterpreted `rand`, and the DSL/JSON layer exposes no Sample node that an extension could give meaningful (non-vacuous) semantics to.
