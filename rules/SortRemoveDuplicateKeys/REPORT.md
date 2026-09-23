# SortRemoveDuplicateKeys

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveDuplicateKeysRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's only nontrivial content is at the sequence level: both sorts emit every input row exactly once, so bag-equivalence holds unconditionally, and what actually needs justification is that collation [k1, k2] induces the same row order as [k1], which follows only from the k1-determines-k2 functional dependency. QED does not model list/ordering semantics — Sort has no bag-semantic meaning in its Q-expression translation, per its own stated limitations — so any encoding (including one via a hypothetical sort builder added to RelRN, whose JSON the serializer already carries) would either be rejected by the prover or degenerate into the vacuous, unconditionally-true identity "sort ≡ its input" that holds for any two collations and verifies none of the rule's actual claim. This is a genuine QED limitation (correctness resting on row order), not a DSL gap to close with extend_dsl_file, so UNSUPPORTED is the correct conclusion even though the porter's stated reason was an API error rather than this analysis.
