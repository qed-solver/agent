# SortJoinCopy

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortJoinCopyRule.java
```

## Independent verifier review

**Verdict:** AGREE

The only semantic content of SortJoinCopy is ordering — it rewrites Sort(collation[, offset, fetch])(Join(L, R, cond)) by adding decomposed/shifted sorts to the join inputs, and its correctness is the claim that the final sorted row order (and which rows survive the offset/fetch) is preserved. QED decides equivalence under bag semantics and explicitly does not model row order (Sort/Limit/Offset/Window/Sample have no meaning there), so under the only semantics QED can adjudicate both sides are just the same join over the same bags and the best achievable "proof" would be a vacuous identity rather than an encoding of the rule. (Note the porter's recorded termination reason was an LLM context-limit crash, not a QED verdict, but the diagnosis the porter reached in-transcript — that the ordering dependence is a fundamental QED limitation — is correct, and no non-vacuous bag-level special case exists to salvage.) ```
