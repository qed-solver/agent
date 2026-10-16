# IntersectToDistinct

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 28  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/IntersectToDistinctRule.java
```

## Independent verifier review

**Verdict:** AGREE

Both variants of IntersectToDistinct are correct only by COUNT's cardinality algebra — `COUNT(*) FILTER (WHERE i=k) > 0` means "branch k contributed the key" and `COUNT(*) = N` in the pushdown variant means "the key appears in all N branches" — while QED models every aggregate call as an uninterpreted function over its group bag and knows nothing about COUNT beyond bag equality of its input (confirmed by the serializer emitting only operator name/operands with no kind tag, per paper eq. (5)). The after() side's post-aggregate filter on an aggregate output is therefore an uninterpreted predicate the prover cannot relate to the set-intersection before() side, and since every faithful version of this rewrite (full or partial, either variant, any N) necessarily passes through a count comparison, no encodable special case exists; the gap sits in QED's aggregate semantics rather than the DSL (which can already express intersect-set, union-all, aggregate, filter, and project, and the JSON format already carries all aggregate info), so extend_dsl_file cannot close it without touching the forbidden Rust prover. ```
