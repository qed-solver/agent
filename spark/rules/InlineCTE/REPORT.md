# InlineCTE

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 42  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/InlineCTE.scala, lines 58-339
```

## Independent verifier review

**Verdict:** AGREE

CTE inlining fundamentally requires a let/named-subrelation construct — a reference node whose value is defined by another subplan — and QED's relational fragment and JSON grammar contain no such node (JSONSerializer handles only scan/values/filter/project/join/correlate/group/union/intersect/except/sort, and RelRN/RexRN expose no builder for it), so every encoding is either refuted (reference as an independent uninterpreted table) or a vacuous identity (body written inline, making before ≡ after). This gap is in the fixed Rust prover's fragment itself, not a missing Java builder that `extend_dsl_file` could close, since no existing JSON node kind carries let semantics and inventing a new one is off-limits.
