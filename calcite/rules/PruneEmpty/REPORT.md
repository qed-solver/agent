# PruneEmpty

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 61  **Verification rounds used:** 3
**Scope detail:** only the Filter-over-empty subrule of Calcite's PruneEmptyRule is encoded (Filter on an empty input collapses to the empty relation); the union/intersect/minus/join/correlate/sort/window variants are not covered.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/PruneEmptyRules.java

Note: PruneEmptyRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `PruneEmptyRule` variant (not RemoveEmptySingleRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous rendering of Calcite's `RemoveEmptySingleRuleConfig.FILTER` instance (a Filter over an empty `Values` collapses to that same empty `Values`): the filter condition is an uninterpreted predicate symbol, the row type is an uninterpreted VarType, `before()` and `after()` are structurally different (the Filter node is removed, so the proof is not vacuous), and this variant carries no extra preconditions (no metadata check, no grand-total guard) that the encoding omits. The SCOPE: PARTIAL tag is honest and specific — `PruneEmptyRules` is a family of a dozen-plus distinct rewrites, this record covers the Filter member, which is a genuine standalone Calcite rule instance rather than an artificial narrowing, and the only residual restriction (a single-column schema, dictated by the DSL's concrete type handling) has no logical bearing on this schema-independent equivalence. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 0
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 722541
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 193334
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 1262583
  }
}
```
