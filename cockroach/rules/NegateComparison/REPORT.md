# NegateComparison

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** only the Eq→Ne pair of NegateComparison's operator map is encoded (NOT(x = y) ⟺ x <> y); the full rule covers 12 operator pairs, each requiring its own before/after pair.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

NegateComparison inverts eligible comparison operators when they are negated
by the Not operator. For example, Eq maps to Ne, and Gt maps to Le. All
comparisons can be negated except for the JSON and Geospatial comparisons.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `NegateComparison`, not the other rules in that file):

```
# NegateComparison inverts eligible comparison operators when they are negated
# by the Not operator. For example, Eq maps to Ne, and Gt maps to Le. All
# comparisons can be negated except for the JSON and Geospatial comparisons.
[NegateComparison, Normalize]
(Not
    $input:(Comparison $left:* $right:*) &
        (CanNegateComparison $op:(OpName $input))
)
=>
(NegateComparison $op $left $right)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is non-vacuous and genuine: `before()` is `Filter(¬(x = y))` vs `after()` `Filter(x <> y)` over the cross-join of two *independent* single-column scans, so x and y are universally quantified independent values and the equivalence (which holds even under null semantics, where both sides share the same three-valued truth table) is a real theorem covering the Eq→Ne instance of the source rule's operator map for all value pairs. Using the concrete `EQUALS`/`NOT_EQUALS` operators is correct here, not hard-coding: the rule's entire content is the semantic link between those two operators, which would be unprovable — and false as stated — with independent uninterpreted predicate symbols, and the filter context is the natural relational home for this expression-level rule in a RelRN-based DSL. There are no symbol-sharing or missing-precondition issues (the source's `CanNegateComparison` guard only excludes JSON/geospatial operators, irrelevant to Eq), and the SCOPE line honestly and specifically discloses the narrowing to one of the 12 operator pairs.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
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
    "nanos": 363500
  }
}
```
