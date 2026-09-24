# IntersectToDistinct

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/IntersectToDistinctRule.java
```

## Independent verifier review

**Verdict:** AGREE

IntersectToDistinct's correctness rests on the counting algebra of COUNT(*) — that aggregating each branch by all columns makes each distinct row appear exactly once per branch, and that a second group-by + count over the UNION ALL then yields the number of branches containing a row, which equals n precisely for set-intersection membership. QED treats aggregate calls as uninterpreted and can only establish aggregate equivalence from bag equality of their inputs (it knows no COUNT/aggregate algebra), so it has no axioms relating the plain set-semantic INTERSECT on the before side to the nested-aggregate pipeline on the after side, regardless of encoding. The porter actually died on an infrastructure error (context-length overflow) before testing anything, but the conclusion holds; the non-pushdown variant is doubly out of reach since its `COUNT(*) FILTER (WHERE ...)` form isn't even expressible in the current AggCall API, and no DSL extension can change QED's aggregate semantics. ```
