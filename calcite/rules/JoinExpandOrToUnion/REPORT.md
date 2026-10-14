# JoinExpandOrToUnion

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 66  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinExpandOrToUnionRule.java
```

## Independent verifier review

**Verdict:** AGREE

The porter's encoding was verified faithful — the serialized JSON shows the identical uninterpreted symbols c1/c2 applied to the same join columns and the shared L/R scans in both queries — and the prover's rejection is pre-SMT (smt_duration:0, unify≈2µs, reproduced twice), i.e., a structural unification decision rather than an encoding or symbol-sharing bug. Any sound encoding of this rule necessarily rewrites the join predicate into an exclusive disjunction (one join summand with p∨q vs. a union of joins with p and q∧¬p), and their equivalence rests on the predicate-level Boolean identity p∨q = p + q∧¬p over independent uninterpreted symbols — exactly the class of predicate inference QED does not perform, since its SMT stage only verifies domain alignment of structurally aligned summands. No alternate faithful shape (cross-join plus filters, distinct union over unique scans) avoids that coefficient rewrite, and the non-exclusive split is unsound under bag semantics, so the UNSUPPORTED conclusion is sound. ```
