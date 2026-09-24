# MinusToFilter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MinusToFilterRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's correctness depends on `NOT Q` being the exact complement of `Q` (i.e., rows with `Q = NULL` appearing on neither side), but QED models uninterpreted predicates as 3-valued SQL predicates and universally quantifies over all instantiations, including null-returning ones — so rows with `Q = NULL` remain in the set-minus input but are dropped by `Filter(NOT Q)`, a counterexample no re-encoding can avoid. This is confirmed by the porter's discriminating test (the single-filter variant `MINUS(base, Filter(Q,base))` ⟹ `Distinct(Filter(NOT Q,base))` failed), and it cannot be fixed via `extend_dsl_file`: predicate nullability is not even serialized into the prover's JSON (operator types lose nullability in `JSONSerializer.type()`), so no RuleScript encoding can declare a predicate total, and the QED prover itself is off-limits.
