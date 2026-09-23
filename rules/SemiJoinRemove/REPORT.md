# SemiJoinRemove

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 60  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRemoveRule.java
```

## Independent verifier review

**Verdict:** AGREE

`SemiJoinRemove` rewrites `X SEMI JOIN Y ON c` to `X`, but a semi-join is an existence filter of X by Y, so this is not a bag-semantic equivalence: it is false in general (e.g., when Y is empty the semi-join result is empty while X is not), and QED decides universal equivalence over every instantiation of the uninterpreted symbols, so no encoding of "drop the semi-join" can be provable. The rule's real precondition — that the semi-join is "advisory," a global, context-dependent planner property that dropping it cannot change the query answer — has no local expression in RuleScript's before/after pattern language and no side-condition mechanism in QED's semantics; even the narrowest special case (e.g., a true-conditional semi-join) still degenerates to empty when the right side is empty, so no provable special case exists, and the porter's abandonment (caused here by an LLM context-length crash rather than a reasoned analysis) happens to land on the correct conclusion.
