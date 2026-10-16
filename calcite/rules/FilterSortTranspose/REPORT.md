# FilterSortTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 7  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterSortTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire correctness content is that filtering a sorted stream preserves relative row order — a list/ordering property — and QED only decides bag-semantic equivalence, under which `Filter(p, Sort(R))` and `Sort(Filter(p, R))` both reduce to the identical bag {r in R | p(r)}, leaving zero non-trivial content to prove. Even if a Sort builder were added via `extend_dsl_file` (the JSON serializer can already carry `LogicalSort`), the prover would treat Sort as having no bag meaning and certify the tautology Filter(p,R) ≡ Filter(p,R), so the gap is a fundamental QED limitation (Sort/Limit/Offset/Window/Sample excluded from scope), not a missing DSL capability — the porter's UNSUPPORTED conclusion and reasoning are correct. ```
