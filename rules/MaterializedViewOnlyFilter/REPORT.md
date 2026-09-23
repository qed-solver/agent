# MaterializedViewOnlyFilter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewOnlyFilterRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's validity rests on a catalog data-maintenance invariant — the materialized view table's contents must equal the result of its defining plan over the base tables — i.e., a required coupling between two uninterpreted relations that QED cannot assume, since it decides bag-equivalence for *every* instantiation of free table symbols and the DSL/JSON format carries only per-table keys/constraints, with no cross-table "defined-as" relation or hypothesis mechanism. Any encoding that keeps the view as a distinct `Scan` on the after side is unprovable (the view and base tables are independent symbols), while inlining the view definition on both sides — the only way to get a proof — erases the rule's actual content (serving the query from a precomputed artifact without re-scanning the base) and degenerates to the pure relational tautology σ_q(A) ≡ σ_{q∧v}(A) ∪ σ_{q∧¬v}(A), which no longer involves a materialized view at all; the rule's semantic core is therefore outside QED's decision domain, not a missing DSL builder.
