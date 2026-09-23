# JoinToMultiJoin

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinToMultiJoinRule.java
```

## Independent verifier review

**Verdict:** AGREE

Calcite's MultiJoin (the rule's target operator) has no representation in RuleScript's core language or in QED's JSON node vocabulary — the serializer's switch only knows a binary `join` node — and the unmodifiable Rust prover cannot be given an N-ary-join constructor, so `extend_dsl_file` cannot close the gap (a new Java builder would emit JSON the prover cannot parse). MultiJoin's semantics are also bespoke backend-internal state — per-input outer-join type/condition arrays, the full-outer flag, post-join filters, and join-field reference counts — that QED's bag-semiring model does not interpret, so even a hypothetical encoding could not be checked. Any DSL-expressible "after" side degenerates to a binary-join tree, collapsing the statement to join associativity (a different, already-provable rule class like JoinAssociate), leaving no non-trivial special case of this rule to encode. ```
