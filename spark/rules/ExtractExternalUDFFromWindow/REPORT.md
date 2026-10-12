# ExtractExternalUDFFromWindow

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 23  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/externalUDF/ExtractExternalUDFFromWindow.scala, lines 33-71
```

## Independent verifier review

**Verdict:** AGREE

The rule's only semantic content is commuting a row-wise external UDF across the Window boundary (per-row eval(udf(w)) = udf(eval(w))), and QED's input language has no Window node — JSONSerializer's Rel switch has no LogicalWindow case and falls through to "Not implemented", so the window can only be the aggregate-joined-back composite used in the accepted CollapseWindow precedent. In that composite the window's "inside" and "outside" are both just Projects around one uninterpreted aggregate, so any faithful encoding renders before() and after() structurally identical (udf at the same relational level in both, a vacuous self-proof), while modeling the pre-rewrite window's UDF'd column as a distinct opaque aggregate would require QED to prove g(B) = udf(f(B)) for free aggregate symbols — an equation its uninterpreted-aggregate model, which knows nothing beyond bag equality of inputs, cannot establish. Even the narrowest modelable slice (partition-determined, order-free expressions) therefore yields no non-vacuous provable statement, and the general case additionally needs ordering semantics QED lacks entirely, so the porter's UNSUPPORTED conclusion and its stated reason are correct. ```
