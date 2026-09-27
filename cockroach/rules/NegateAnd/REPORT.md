# NegateAnd

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** package org.qed.RRuleInstances;


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

NegateAnd converts the negation of a conjunction into a disjunction of
negations.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `NegateAnd`, not the other rules in that file):

```
# NegateAnd converts the negation of a conjunction into a disjunction of
# negations.
[NegateAnd, Normalize]
(Not (And $left:* $right:*))
=>
(Or (Not $left) (Not $right))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() is Filter(¬(P∧Q), Source) and after() is Filter(¬P∨¬Q, Source) over the same scan, with P and Q as distinct uninterpreted predicates — a structurally different, non-vacuous embedding of Optgen's (Not (And l r)) ⇒ (Or (Not l) (Not r)). The filter-over-scan context is the canonical most-general relational host for a boolean rewrite: the proven row-level identity ∀r. ¬(P(r)∧Q(r)) ↔ ¬P(r)∨Q(r) is precisely the scalar rule itself (and De Morgan holds under both 2VL and SQL three-valued logic), so the FULL scope tag is accurate. Nothing is hard-coded, the two predicate symbols are correctly independent (not coincidentally unified), and the source rule carries no preconditions (no keys/NOT-NULL) that the encoding omits. ```

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
    "nanos": 349666
  }
}
```
