# AggregateRemoveDuplicateKeys

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateRemoveDuplicateKeysRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's soundness rests on (i) a schema-level functional dependency (the dropped key being determined by the retained keys) which is a premise, not a universal law, and, decisively, (ii) the after() side reintroducing the dropped column via an ANY_VALUE aggregate whose correctness requires that call return the group's (constant) value; QED models every aggregate call as an uninterpreted function of its input bag with no axiom tying its output to a bag element, so it cannot prove ANY_VALUE(col) = col even when a key constraint supplies the FD, and no RelRN/RexRN/JSONSerializer edit can add such a prover axiom — squarely the documented "aggregate algebraic identity QED can't know" / "uninterpreted-function internal semantics" limitation.
