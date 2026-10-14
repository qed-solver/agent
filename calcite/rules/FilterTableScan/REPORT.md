# FilterTableScan

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 6  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterTableScanRule.java
```

## Independent verifier review

**Verdict:** AGREE

FilterTableScanRule is a physical push-down whose correctness rests entirely on the FilterableTable/BindableTableScan execution contract (a scan configured with predicate P returns exactly the rows of Filter(P, T)) — a backend operator's bespoke internal semantics that QED cannot model: under bag semantics the "after" node is definitionally identical to the before side, and QED's fixed Q-expression language has no predicate-parameterized scan operator. QED's table model is an uninterpreted bag with only PrimaryKey/References/Checks row constraints, none of which can express "table T2 is the bag-filter of T", so no encoding (even via a DSL extension, since the prover's operator set is fixed) can distinguish the pushed-scan from the plain filter; the only encodable variants degenerate into a tautology or into constraint-based filter elimination, which is a different rule, so no narrower special case carries this rule's actual content.
