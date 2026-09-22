# SortRemove

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveRule.java
```

## Independent verifier review

**Verdict:** AGREE

SortRemove is fundamentally a row-ordering rule: its subject operator (a Sort with no offset/limit) and its defining precondition (the input already satisfies the sort's collation, i.e. RelCollation trait preservation) both live in list/ordering semantics, which QED's bag-semantic model explicitly does not model — Sort/Limit/Offset/Window/Sample have no bag-semantic meaning — and no bag-level property can stand in for "input is sorted", so any encoding that drops the Sort node or the sortedness condition ceases to be this rule. Extending the DSL (e.g. adding a sort builder, which the JSON serializer could even carry) cannot close the gap because the missing semantics are on the QED prover side, not the DSL side, so UNSUPPORTED is the correct conclusion even though the porter's stated reason was just an LLM context-length error rather than that analysis. ```
