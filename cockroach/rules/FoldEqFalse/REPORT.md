# FoldEqFalse

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** public record FoldEqFalse() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

FoldEqFalse replaces x = False with NOT x.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldEqFalse`, not the other rules in that file):

```
# FoldEqFalse replaces x = False with NOT x.
[FoldEqFalse, Normalize]
(Eq $left:* (False))
=>
(Not $left)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors `FoldEqFalse`: `before()` is `Filter(left = False, Source)` and `after()` is `Filter(NOT left, Source)`, so they differ structurally exactly in the way the real rule rewrites, with `left` a single uninterpreted predicate (`pred("left")`) standing in for the arbitrary `$left:*` expression — no constants, column choices, or operator shapes are hard-coded, and the same symbol is correctly shared on both sides. The source rule has no side conditions (it's valid under three-valued logic too: NULL = False and NOT NULL both yield NULL, which filters drop), and the filter-over-scan embedding is the standard faithful relational form of a boolean-condition rewrite, with QED's proof quantifying over all table contents and all instantiations of the uninterpreted symbol. SCOPE: FULL is honest — the uninterpreted predicate covers any boolean expression over the input row, which is the full generality of the scalar rule.

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
    "nanos": 329875
  }
}
```
