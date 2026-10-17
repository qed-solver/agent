# RewriteSetComparison

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 30  **Verification rounds used:** 2
**Scope detail:** only the '=' ANY (IN) set comparison under a Filter, where DataFusion's CASE-over-EXISTS rewrite reduces in filter context to EXISTS(subquery WHERE x = y); the ALL kind, other comparison operators, and non-filter (null-preserving CASE) contexts are not encoded


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/rewrite_set_comparison.rs, lines 1-175
```

## Independent verifier review

**Verdict:** CONFIRMED

before (a Filter whose condition is an IN subquery) and after (a Filter whose condition is a correlated EXISTS subquery) are structurally distinct, so the proof is not vacuous, and they are genuinely equivalent: in filter context both keep exactly the outer rows for which some subquery row satisfies x = y, and the DataFusion CASE-over-EXISTS form collapses to a bare EXISTS there without any NOT NULL or primary-key precondition that the encoding silently omits. The shared K_type is a correct comparability constraint rather than an over-constraint, the inner_pred/subquery stay uninterpreted, and the PARTIAL scope line is honest and specific (only `=` ANY/IN under a Filter; ALL, other operators, and null-preserving non-filter contexts excluded), so the result is a faithful, non-degenerate special case of the real IN-to-EXISTS rewrite. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 12348873
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 40876833
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1238334
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 679333
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 30614041
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 41124833
  },
  "total_duration": {
    "secs": 0,
    "nanos": 88757750
  }
}
```
