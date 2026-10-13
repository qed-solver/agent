# DedupeGroupByExprs

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 61  **Verification rounds used:** 4
**Scope detail:** Aggregate branch only: group keys are plain input columns with the first key duplicated as the third, and a single aggregate call.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_duplicated_expr.rs, lines 115-138
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is non-vacuous and correctly shaped: `before()` groups by three keys whose third is a projected copy of the first (the only way to materialize a duplicate group key, since Calcite's group key collapses duplicate references) and then drops the redundant column to match the after-side schema, while `after()` is the clean two-key aggregate with the same uninterpreted `sum` and shared scan symbols — no concrete values or operators were baked in, and if QED had *not* seen through the copy-projection the claim would actually be false (a 3-key grouping with an independent third column refines the 2-key grouping and changes the sums), so the PROVABLE result confirms it recognized the duplicate. This is a narrower special case than the full DataFusion rule (plain-column duplicate of the first key in the third position, one aggregate call, arbitrary duplicate expressions/positions/multiple aggregates not covered), but the restriction is specific and the `SCOPE: PARTIAL` line states it precisely, so it is an honest, non-degenerate encoding of the rule's core algebra (redundant group key is inert, with the corresponding output column dropped).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 10313376
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 45381333
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 915958
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 780542
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 25509459
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 45568667
  },
  "total_duration": {
    "secs": 0,
    "nanos": 86806375
  }
}
```
