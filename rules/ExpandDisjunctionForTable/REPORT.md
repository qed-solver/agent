# ExpandDisjunctionForTable

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 33  **Verification rounds used:** 2
**Scope detail:** INNER join of two plain table scans whose condition is a cross-table predicate E AND a one-level disjunction of two conjunctions, each branch pairing a left-table-only predicate (a, c) with a right-table-only predicate (b, d), expanded by AND-ing in the per-table disjunctions (a OR c) and (b OR D).


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ExpandDisjunctionForTableRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` and `after()` are structurally distinct (after ANDs in the per-table disjuncts `(a∨c)` and `(b∨d)`), and the encoding uses properly distinct uninterpreted symbols — `a`,`c` on t1-only fields, `b`,`d` on t2-only fields, `e` cross-table — in the inner-join shape that mirrors the rule's actual transformation, so the proved identity (core ⇒ (a∨c)∧(b∨d)) is the genuine logical core of the rule and holds under bag semantics with no hidden PK/NOT NULL precondition. The narrowing to an INNER join of two plain scans with a two-branch, one-conjunct-per-table DNF is a real restriction versus the source (which matches any join type, Filters, and arbitrary disjunctions), but it is accurately and fully declared in the `SCOPE: PARTIAL` line, making the provable result an honest, non-vacuous special case rather than a misleading one.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7296125
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36228125
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 914833
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 503416
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19984584
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36362000
  },
  "total_duration": {
    "secs": 0,
    "nanos": 72386042
  }
}
```
