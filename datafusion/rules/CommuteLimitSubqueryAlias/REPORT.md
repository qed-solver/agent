# CommuteLimitSubqueryAlias

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 45  **Verification rounds used:** 3
**Scope detail:** the limit's skip and fetch are fixed to the concrete literals 0 and 10 (Calcite's sortLimit builder rejects non-literal offset/fetch nodes, and QED's JSON carries RexNode literals)


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 193-199
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully matches the source rule's shape — Limit directly above SubqueryAlias commutes to SubqueryAlias directly above Limit — with SubqueryAlias correctly represented as an all-fields identity projection (aliasing changes no row values or multiplicities) and DataFusion's orderless Limit modeled as a sort with empty collation plus offset/fetch, the closest QED can carry. The only narrowing, concrete skip=0/fetch=10 literals, is honestly tagged PARTIAL and is in fact uniform: the Limit node is identical on both sides and the alias is identity, so the commutation holds for any skip/fetch, which matches the preconditional-free real rule (no missing constraints or symbol-sharing artifacts). The near-instant provable result is expected and non-vacuous in a meaningful sense: alias normalizes away on both sides, leaving the same Limit expression, exactly the genuine (tautological) commutation the real rule performs.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
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
    "nanos": 339875
  }
}
```
