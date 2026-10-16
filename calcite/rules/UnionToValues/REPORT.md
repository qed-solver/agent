# UnionToValues

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** concretized to a 3-way UNION ALL of fixed 2-column constant


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/UnionToValuesRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (a 3-way UNION ALL of three constant `Values` nodes) and `after()` (a single `Values`) are structurally different plans, and the deliberately duplicated `(3,4)` row makes the proof a genuine check of bag-multiplicity preservation under UNION ALL — precisely the semantic content of the source rule's `all=true` branch — with identical row types on all inputs satisfying the rule's `equalsSansFieldNamesAndNullability` precondition. The hard-coded tuples, arity, and column count are forced rather than lazy over-constraint: the DSL has no uninterpreted constants or literal-row symbols for `Values` (uninterpreted symbols are only predicates/projections/aggregate ops/scan tables), so this concretization is the only encodable form and the PARTIAL tag discloses it honestly and specifically. The unmodeled `all=false` distinct variant is an omitted sibling instance, not a soundness hole in this proof, and the encoding is not degenerate (dropping one copy of `(3,4)` would have made it unprovable).

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
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
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
    "nanos": 335917
  }
}
```
