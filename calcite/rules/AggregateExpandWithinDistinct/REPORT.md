# AggregateExpandWithinDistinct

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateExpandWithinDistinctRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's before() requires `WITHIN DISTINCT` aggregate calls (distinct keys) and its full after() requires GROUPING SETS plus the GROUPING() row-provenance function with `g = constant` selection filters — none of which QED's semiring model represents: the JSON `group` node carries only a flat key list and a plain `distinct` boolean, the immutable prover models no dedup-by-keys aggregate input, and GROUPING's output is just an opaque uninterpreted value QED cannot reason about. The core MIN-reconstruction identity (outer `f` over per-(k, wd) `MIN(x)` rows equals `f(x) WITHIN DISTINCT (wd)`) holds only under the per-group functional-dependency premise that x is unique within each (k, wd) group, which the shipped rule enforces exclusively via the runtime `THROW_UNLESS` guard — a side-effecting operator outside bag semantics — so QED's universal quantification over all table contents would actually refute the identity rather than prove it. No expressible universally-valid special case survives: making the distinct keys coincide with the aggregate argument collapses the rewrite to `f(DISTINCT x)` dedup expansion, which is exactly the already-proved AggregateExpandDistinctAggregates result, and DSL extension cannot add the missing prover semantics.
