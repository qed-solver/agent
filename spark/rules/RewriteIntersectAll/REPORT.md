# RewriteIntersectAll

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 11  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3503-3550
```

## Independent verifier review

**Verdict:** AGREE

The rule's left-hand side is `Intersect(left, right, true)` — bag INTERSECT ALL — and QED's Q-expression formalism has no multiset-intersection operation (qed.pdf: the bag variants would require an ordering/monus operator the prover does not introduce), which the ground-truth `JSONSerializer` confirms has no node for (`case LogicalIntersect ... when !intersect.all` throws `Not implemented` on the bag variant, as the porter's probe showed), so the `before()` pattern cannot even be fed to the prover. Independently, the rewrite's correctness rests on `Generate(ReplicateRows(min(c1,c2), payload))` — data-dependent row replication to materialize per-value multiplicities — which has no DSL operator or JSON node, and cannot be emulated with core-language joins/tables because uninterpreted predicates and table contents carry no cardinality semantics QED could reason about.
