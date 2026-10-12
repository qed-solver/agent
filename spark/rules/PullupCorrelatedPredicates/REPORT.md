# PullupCorrelatedPredicates

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 30  **Verification rounds used:** 3
**Scope detail:** EXISTS branch only, first application (pre-existing conditions empty): one outer-bound conjunct plus one local conjunct in a single subquery filter, rewritten to record the conjunct in the expression's conditions slot (modeled as c AND EXISTS(inner)); the correlated conjunct is a 0-ary symbol since the DSL has no outer-row reference inside a subquery; ScalarSubquery/LateralSubquery branches, output reshaping, and name dedup are out of scope


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/subquery.scala, lines 467-693
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, honestly-scoped PARTIAL capture of the rule's core transformation for the Exists branch: `before()` places the pulled-up conjunct `c` inside the subquery filter (`inner.filter(c∧local)`, empty conditions slot) and `after()` moves it to the conditions slot (`inner.filter(local)`, cond `c`), with structurally distinct plans so the proof is non-vacuous and non-trivial; the key algebraic fact proven — moving a conjunct independent of the inner row across the EXISTS boundary (`∃i: P∧Q(i) ⟺ P∧∃i Q(i)`) — is exactly the soundness condition the real pullup relies on, and making `c` 0-ary (documented in SCOPE) is the correct, disclosed stand-in for "independent of the inner row" given the DSL lacks inner-visible outer references. The genuine narrowings (one conjunct each, single filter, empty pre-existing conditions, EXISTS-only, no output reshaping/dedup) are all specific and disclosed, and no symbol-sharing error or missing precondition makes the result unsound.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6715249
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 7795917
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 61125
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 440750
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 13883041
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 7857833
  },
  "total_duration": {
    "secs": 0,
    "nanos": 24514500
  }
}
```
