# FilterSortTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 7  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterSortTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

FilterSortTranspose's entire correctness content is the ordering claim that filtering preserves a relation's collation, but QED decides equivalence only under bag semantics where Sort/Order By carry no meaning, so both sides of the rewrite collapse to the identical bag Filter(cond, R) and any provable encoding would be a vacuous tautology with no bearing on the actual rule. The gap is in the prover itself (no list/ordering semantics for Sort), not merely a missing builder in RelRN, so neither a new encoding nor a DSL extension can capture the rule's non-trivial content. ```
