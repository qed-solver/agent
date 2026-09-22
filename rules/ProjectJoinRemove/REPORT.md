# ProjectJoinRemove

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 90  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectJoinRemoveRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests on the join condition being an equality on the non-preserved side's key columns, which lets the key constraint bound join fanout to ≤1; RuleScript's core language has no equality operator, so any join condition is an uninterpreted predicate, and with an uninterpreted condition the equivalence is actually false (a left row can match multiple distinct rows of the keyed table, duplicating it), and QED cannot infer an entailment between an independent predicate symbol and the table's key constraint. Thus no encoding — including one that correctly sets the scan's `unique` flag and shares symbols — is provable; the porter's LLM context-length error merely masked a conclusion its transcript was already converging on (QED never even reached SMT, rejecting structurally).
