# RewriteWithExpression

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 49  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/RewriteWithExpression.scala, lines 45-472
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire content is substituting a `With`/`CommonExpressionRef` let-binder by its definition, but neither the RuleScript scalar language (`RexRN` has no let/def node) nor QED's JSON Q-expression format (scalars are only column/literal/uninterpreted-call/subquery; relations only scan/filter/project/join/correlate/group/union/intersect/except/sort) contains a def/ref binder, so a reference and its definition can only be encoded either as the identical structure (baking in the equality, making the rule vacuous) or as independent uninterpreted symbols QED cannot prove equal. Because the Rust prover — the unmodifiable arbiter — accepts no let node, `extend_dsl_file` (Java-only) cannot supply the missing substitution semantics, and the firing gates (reference count, determinism, cheapness) are backend heuristics QED likewise does not model.
