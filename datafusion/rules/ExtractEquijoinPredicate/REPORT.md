# ExtractEquijoinPredicate

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** inner join only, one input column per side, exactly one equi conjunct of the join's residual filter moved into the equi-conditions


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/extract_equijoin_predicate.rs, lines 1-273
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully models DataFusion's ExtractEquijoinPredicate for the INNER join case: `before()` is `Filter(eq∧rest, Join_on(L,R))` and `after()` is `Filter(rest, Join_{on∧eq}(L,R))`, which are structurally distinct and the proof is non-vacuous. The three predicates (on, eq, rest) are correctly independent uninterpreted symbols over the joined row, the residual-filter-as-Fill-above-Join shape matches DataFusion's Join.filter semantics, and the INNER-only restriction is genuine (for outer joins, null-extended unmatched rows interact unsoundly with moving a conjunct into the join condition). The scope tag honestly and specifically states the restrictions.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6945499
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34021041
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 829583
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 496334
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19396583
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34139042
  },
  "total_duration": {
    "secs": 0,
    "nanos": 69243458
  }
}
```
