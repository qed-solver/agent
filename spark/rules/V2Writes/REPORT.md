# V2Writes

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 11  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/v2/V2Writes.scala, lines 42-198
```

## Independent verifier review

**Verdict:** AGREE

V2Writes is a write-command construction rule, and its two observable changes are (1) filling a V2 write *command* node's `write` field with an opaque engine-built Write object — a side-effect payload that is not a relation and is bespoke engine-internal state QED cannot see through — and (2) wrapping the child query in Sort/Repartition/Rebalance to satisfy the write's distribution/ordering, which QED's bag model has no notion of (no ordering/distribution semantics at all). The before/after plans are the same uninterpreted command node whose child *bag* is actually invariant (distribution and the `write` field are bag-irrelevant), so there is no bag-equivalence statement between two relations for QED to decide; this is a genuine QED/DSL limitation, not a mismodeling or a missing symbol-sharing step. ```
