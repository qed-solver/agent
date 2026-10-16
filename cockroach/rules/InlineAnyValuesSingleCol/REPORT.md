# InlineAnyValuesSingleCol

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 48  **Verification rounds used:** 3
**Scope detail:** modeled with a 1-column uninterpreted input relation and a concrete 2-row, 1-column Values, with the comparison fixed to EQUALS and encoded as a single column equality under a SEMI JOIN, rather than as a general uninterpreted Any/Tuple operator.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

InlineAnyValuesSingleCol converts Any with Values input to AnyScalar.
This version handles the case where there is a single column.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `InlineAnyValuesSingleCol`, not the other rules in that file):

```
# InlineAnyValuesSingleCol converts Any with Values input to AnyScalar.
# This version handles the case where there is a single column.
[InlineAnyValuesSingleCol, Normalize]
(Any $values:(Values) $scalar:* $private:*)
=>
(AnyScalar $scalar (InlineValues $values) (SubqueryCmp $private))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The proved equivalence is genuine and non-vacuous — S SEMI JOIN the concrete 2-row values bag {(1),(3)} on S.c0 = V.c0 is the exact relational meaning of `x = ANY (VALUES (1),(3))`, and its equivalence to the inlined filter `S.c0 = 1 OR S.c0 = 3` is the rule's core invariant, with the constants correctly shared (if the Values literals and the filter literals had not unified, QED's SMT check would have refuted the equivalence rather than proving it). The hard-coded EQUALS comparison and 2-row values list are genuine, specifically-named restrictions of this encoding (RuleScript's fixed-shape patterns cannot quantify over the values-list length, and inlining inherently requires concrete constants), and the file's SCOPE: PARTIAL line declares them precisely, making this a faithful, non-degenerate special instance of the rule rather than a misleading full-generality claim.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8935582
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35928750
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1113625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 431708
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21415958
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35966916
  },
  "total_duration": {
    "secs": 0,
    "nanos": 72862959
  }
}
```
