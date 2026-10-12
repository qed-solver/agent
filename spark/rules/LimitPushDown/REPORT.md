# LimitPushDown

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 926-1030
```

## Independent verifier review

**Verdict:** AGREE

Every branch of Spark's LimitPushDown relocates or adjusts Limit/LocalLimit/Offset nodes (through Union, through Join, limit-1 through a group-only Aggregate, merging Limit over Offset, limit-1 onto the right side of a condition-less semi/anti join), and all of these operators are defined by row-position/prefix semantics (keep-first-n, skip-first-m, arbitrary-row selection) that QED's bag-semantic model has no meaning for — "the first n rows of a bag" is not a deterministic bag operation, so no SMT equivalence decision is even defined over such plans. The one branch with a bag-flavored core (limit-1 on the right of a cross semi/anti join, where only non-emptiness of the right side matters) still requires a Limit operator whose choice of *which* row survives is nondeterministic and therefore inexpressible as a QED relation, and the DSL has no Limit/Offset/Sort builders at all; since the missing piece is in QED's own semantics (it does not model ordering/prefix semantics) rather than a missing builder, `extend_dsl_file` cannot close the gap, leaving no non-trivial provable subcase. The porter's UNSUPPORTED verdict is correct.
