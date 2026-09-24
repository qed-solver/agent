# JoinConditionExpandIsNotDistinctFrom

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinConditionExpandIsNotDistinctFromRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests entirely on the null-aware data semantics of specific backend operators — `IS NOT DISTINCT FROM`, `COALESCE`, and `IS_NULL` (i.e., that `x IS NOT DISTINCT FROM y` iff `COALESCE(x,0)=COALESCE(y,0) AND (x IS NULL)=(y IS NULL)`). QED operates on uninterpreted symbols and "can't reason about predicate inference/entailment between independent symbols or a backend operator's bespoke internal semantics," so with these operators modeled as opaque generic ops the left and right sides are unrelated SMT symbols and no universal proof exists; QED only knows the Boolean `AND`-composition layer, not the operator-null algebra this rewrite depends on. Adding DSL builders for these operators via `extend_dsl_file` cannot close the gap, since a builder only introduces another uninterpreted name and cannot inject SMT semantics into the Rust prover (the unchangeable arbiter) — so even a narrowed special case (e.g., non-null operands) is unprovable. ```
