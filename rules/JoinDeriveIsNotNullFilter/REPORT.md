# JoinDeriveIsNotNullFilter

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 10  **Verification rounds used:** 1
**Scope detail:** the join condition is restricted to a conjunction containing an equality conjunct between a left column and a right column (plus arbitrary other conjuncts over those same two columns); the original rule derives IS NOT NULL filters from any condition for which per-column non-null inference succeeds


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinDeriveIsNotNullFilterRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The rewrite is nontrivial and matches the source's inner-join transformation by adding IS NOT NULL filters implied by the null-rejecting equality conjunct while preserving the same condition. Although narrower than the fully generic Calcite rule, the SCOPE line accurately states the specific equality-plus-arbitrary-extra-conjuncts restriction, so the proven result is faithful and non-vacuous.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6536709
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 7648667
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 79042
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 881125
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 13983250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 7704125
  },
  "total_duration": {
    "secs": 0,
    "nanos": 25281250
  }
}
```
