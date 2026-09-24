# UnionToValues

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 13  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/UnionToValuesRule.java
```

## Independent verifier review

**Verdict:** AGREE

UnionToValues is a constant-folding rule whose operand definition (a Union whose inputs are all Values) gives it no free relational or predicate symbols, and in the JSON/DSL model a `values` node is a closed bag of concrete literal rows — the DSL only exposes the empty values, and even a hypothetical `values(rows)` builder could only fix one ground instance, leaving nothing relational to universally quantify over. The rule's soundness rests on the engine-internal invariant that a Values computes exactly the bag of its hard-coded tuple list and that the rule's list concatenation/deduplication preserves it — a backend operator's bespoke internal semantics that QED takes as an axiom of its model rather than something it can verify, so any encodable "proof" would be a single closed bag equality (or, at best, a scalar-constant bag-identity checking QED's own model consistency) rather than a genuine proof of the rewrite.
