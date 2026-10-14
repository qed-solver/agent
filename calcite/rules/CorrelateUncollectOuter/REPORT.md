# CorrelateUncollectOuter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 64  **Verification rounds used:** 5

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/CorrelateUncollectOuterRule.java
```

## Independent verifier review

**Verdict:** AGREE

The claim holds: the rewrite's only soundness argument is that Uncollect(isOuter=true) over the single-row LogicalValues guarantees at least one (possibly NULL-filled) right row per left row, which makes the LEFT correlate behave as an INNER one — this is Uncollect's list-expansion (UNNEST) semantics, which QED does not model, and even if the Java side were extended to emit an Uncollect node, QED could at best treat it as uninterpreted and would then correctly refute the identity on a left row with an empty list. The encoding gap is real and not closable by extend_dsl_file: RelRN exposes no Uncollect builder, JSONSerializer's exhaustive Rel switch (scan/values/filter/project/join/correlate/group/union/intersect/except/sort) has no uncollect case and throws on LogicalUncollect, and the fixed prover's JSON vocabulary cannot carry one. Nor is there a fallback encoding or provable special case: the "≥1 row per left row" precondition cannot be stated in the DSL/JSON format (there is no non-emptiness assertion, and row-level "guaranteed" constraints are vacuous on empty relations), so any Uncollect-free encoding of LEFT→INNER correlate is simply invalid and would be refuted.
