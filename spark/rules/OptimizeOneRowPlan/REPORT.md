# OptimizeOneRowPlan

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 29  **Verification rounds used:** 3
**Scope detail:** only the group-only Aggregate→Project arm, with the <=1-row child instantiated as a unique single-column scan whose column is the group-by key, so every group is a singleton; the sort-removal arms and the distinct-flip arm are not encoded.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeOneRowPlan.scala, lines 44-68
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (group-only `Aggregate` over the scan) and `after()` (`Project` of the key column) are structurally different, and the equivalence genuinely fails without the uniqueness constraint (a bag with duplicate key values would collapse under group-by but not under project), so the proof is non-vacuous and the rewrite is real. The original rule's `maxRows <= 1` is a statistics/physical precondition with no representation in QED's bag semantics, so the porter correctly substituted the actual semantic validity condition — the grouping column being a unique key (with the non-nullable type correctly sidestepping NULL group-merging) — which is the most general shape the DSL can attach a key constraint to (`ScanMany` carries no keys, and grouping by any non-identity expression over the key would break injectivity). The omitted arms (Sort removal, distinct-flip) depend on capabilities QED fundamentally lacks (ordering semantics and aggregate-function algebra), and the single `// SCOPE: PARTIAL` line accurately and specifically discloses exactly what was assumed, making this an honest, non-degenerate, useful special case.

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
    "nanos": 354250
  }
}
```
