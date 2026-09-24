# ProjectJoinTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 10  **Verification rounds used:** 1
**Scope detail:** inner join over single-column sides, where the join condition and the top projection are expressed via shared uninterpreted per-side expressions (the decomposition the rule itself performs)


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectJoinTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous special case: `before()` (inner join of raw scans with condition C(TL(l),TR(r)) and top projection G(TL(l),TR(r))) and `after()` (the same C and G rewired onto pushed per-side projections π_TL, π_TR below the join) are structurally distinct plans, all symbols are uninterpreted, and the symbol sharing matches exactly what the rule's decomposition requires — no hard-coded predicate, no spurious coupling, and no missing precondition for the inner-join case (no uniqueness/NOT NULL assumptions are needed for this bag identity). The narrowing to inner joins, single-column sides, and condition/projection factoring through the same shared per-side expressions is a genuine, specific, and explicitly disclosed special case (the SCOPE line says so, and single-column sides are in any case forced by the DSL's one-column scans, so the full multi-column/all-join-kind rule is not expressible regardless); the proved universal equivalence is precisely the core transformation of ProjectJoinTranspose, so the result is useful and not misleading.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7096792
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35027667
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 864958
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 575458
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19694500
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35131250
  },
  "total_duration": {
    "secs": 0,
    "nanos": 70527875
  }
}
```
