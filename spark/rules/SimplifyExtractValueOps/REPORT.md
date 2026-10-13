# SimplifyExtractValueOps

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 10  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/ComplexTypes.scala, lines 28-72
```

## Independent verifier review

**Verdict:** AGREE

Every case of SimplifyExtractValueOps is a constructor/accessor identity over Spark complex types (get_struct(mk_struct(v…),i)=v(i), get_array_item(mk_array(e…),k)=e(k), get_map(mk_map(p…),k)=case-when(k,p), plus a null-guarded UpdateFields case) that is valid only under Spark's intended struct/array/map and 3-valued-null semantics. QED's fixed semiring model has no list/index, map-key-lookup, or SQL-null semantics and represents all of these operators as uninterpreted scalar functions, and since its SMT decision must hold for every instantiation of uninterpreted symbols, no composition law such as get(mk(v…)) = v(i) is derivable — so this is a fundamental limitation of the unchanged Rust prover (same class as RewriteSizeOfArrayStruct), and adding DSL builders could not close the gap.
