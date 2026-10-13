# MaterializedViewOnlyAggregate

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewOnlyAggregateRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness is a catalog-level invariant — the stored MV's bag must equal the evaluation of its defining plan over the base tables — i.e. a coupling between two uninterpreted scans, but the DSL/JSON format carries only per-table keys and per-row `guaranteed` constraints, with no cross-table "defined-as" hypothesis mechanism, and QED decides bag-equivalence universally over all instantiations of free symbols, so any encoding keeping the MV as a distinct `Scan` is unprovable; the only alternative that could be decided — inlining the view definition on both sides — erases the rule's identity (there is no precomputed artifact) and degenerates into a two-stage-aggregate identity, which QED also cannot use since it knows nothing about aggregate algebra beyond bag equality of inputs. This matches the fundamental limitation already confirmed for the sibling MaterializedViewOnlyFilter. ```
