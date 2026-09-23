# CoerceInputs

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 16  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/CoerceInputsRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's only non-trivial content is inserting per-input cross-type casts, which in any faithful model are value-changing transformations, so QED must treat the cast as a non-identity uninterpreted function and correctly cannot prove `consumer(cast(x)) ≡ consumer(x)` under the consumer's uninterpreted predicates. Its soundness additionally rests on the consumer's `getExpectedInputRowType(i)` contract — a type-level side condition on a backend operator that the pattern language has no way to express. Adding a cast builder via `extend_dsl_file` would not close the gap: the only value-transparent instance (input type already equal, rename-only) is either a no-op the rule wouldn't perform or inexpressible as a rename in the core language, so no faithful, non-vacuous encoding exists for QED to prove. ```
