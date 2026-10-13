# PushTopKThroughJoin

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit/topk_through_join.rs, lines 92-308
```

## Independent verifier review

**Verdict:** AGREE

The rule's substance — that the top-N rows of a LEFT/RIGHT join, ordered by keys that depend only on the preserved side, are all producible from the top-N of that side — is an ordering/top-k statement, while QED only decides bag equality and (per qed.pdf and the reference) has no semantics for Sort/Limit at all, which the RelRN DSL doesn't even expose a builder for, so `extend_dsl_file` could at best emit a "sort" JSON node that the unmodifiable prover has no ordering axioms to interpret. Furthermore the rewrite is not a bag equality in general: with equal sort keys, the before- and after-plans can yield different N-row result sets (both valid top-k results under arbitrary tie-breaking), so even a hypothetical ordering-aware equivalence prover could not certify it. I also checked the tempting narrow special cases (unique sort key via `scan(..., unique)`, at-most-one match per preserved row): they would make top-N deterministic, but "first N rows in an order" is still not expressible in the bag language — no comparison/total-order axiom on uninterpreted key values exists — so no provable, non-vacuous encoding exists, consistent with the PushLimitIntoLeftJoin precedent. ```
