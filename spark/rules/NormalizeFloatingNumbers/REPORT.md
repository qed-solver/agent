# NormalizeFloatingNumbers

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 27  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/NormalizeFloatingNumbers.scala, lines 67-201
```

## Independent verifier review

**Verdict:** AGREE

Both operative sub-rules rest on semantics QED's theory fundamentally cannot express: the Window partition-spec case needs a Window operator, which the DSL does not expose and which QED explicitly has no bag-semantic meaning for (list/ordering operators are out of its model), and the join/NAAJ case rewrites each equi-join `l = r` into `norm(l) = norm(r)` where `norm` (NaN→canonical NaN, −0.0→0.0) is a non-injective canonicalization whose soundness depends on IEEE floating-point equality facts — in QED's theory all virtual types collapse to integers and `norm` is a plain uninterpreted function, so `norm(l)=norm(r)` is a strict weakening of `l=r` and no axiom relating the two exists, making the rewrite unprovable; the only degenerate instance that would be provable (norm = identity) is exactly the case where Spark's `needNormalize` guard is false and the rule doesn't fire, so no faithful provable special case exists. ```
