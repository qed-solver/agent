# DecorrelateLateralJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** INNER lateral join only, with the lateral subquery being a single scan guarded by one uninterpreted binary correlation predicate; no user ON clause, no aggregate, no HAVING


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/decorrelate_lateral_join.rs, lines 1-373
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is non-vacuous — `before()` is a correlated (dependent) INNER join while `after()` is an ordinary INNER join, and the uninterpreted predicate `cond` is applied in identical argument order (left column, then right column) on both sides, so QED proved the genuine dependent-join = join-with-condition identity, which is exactly the core rewrite DecorrelateLateralJoin performs in its simplest form. `L` and `R` are distinct scans with no hidden key/not-null assumptions, the correlation predicate is properly shared (not accidentally duplicated or independently named), and the omitted surface — LEFT join kind, user ON clause, aggregate pull-up, count-bug NULL compensation, HAVING — is declared accurately and specifically in the SCOPE line, with the aggregate/count-bug parts genuinely beyond QED's aggregate-algebra and NULL/CASE modeling. The result is therefore a faithful, honest, non-degenerate PARTIAL encoding rather than a trivial or over-constrained artifact.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7079333
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 31314333
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 829375
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 443500
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19593000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 31427334
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67514916
  }
}
```
