# UnionToDistinct

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** the union has exactly two inputs (Calcite's rule fires on any number of inputs); both inputs are one-column relations of the same type, as required by Calcite's union type-checking.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/UnionToDistinctRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding matches the rule's exact shape: `before()` is a UNION DISTINCT (`union(false)`) over two independent uninterpreted inputs, and `after()` is an Aggregate grouping by all output columns with zero aggregate calls over the same inputs with `union(true)` — precisely what Calcite's `relBuilder.distinct()` produces — with the shared "T" type being a genuine union precondition rather than an over-constraint, no uniqueness assumptions, and no triviality since the two sides are structurally different. The only restrictions are the fixed two-input arity and single-column inputs, both forced by the DSL's construction (fixed-arity `union`, single-column `Scan`) and honestly flagged in the SCOPE line as a PARTIAL special case. The result is a genuine, non-degenerate instance of the rule: the dedup-by-aggregate rewrite over arbitrary (possibly duplicate) bags of the same type, which is the rule's full semantic content at 2 inputs × 1 column. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 10166456
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 24998375
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 946250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 445625
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 24930791
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 25081375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 65529417
  }
}
```
