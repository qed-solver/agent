# FoldNeTrue

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 3  **Verification rounds used:** 1
**Scope detail:** public record FoldNeTrue() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

FoldNeTrue replaces x != True with NOT x.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNeTrue`, not the other rules in that file):

```
# FoldNeTrue replaces x != True with NOT x.
[FoldNeTrue, Normalize]
(Ne $left:* (True))
=>
(Not $left)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully lifts the scalar rule `(Ne $left:* (True)) => (Not $left)` into `Filter(scan, NOT_EQUALS(left, TRUE)) => Filter(scan, NOT(left))`, modeling the arbitrary boolean `$left` as a single uninterpreted *nullable* predicate — since it is uninterpreted it stands for any boolean expression, so the one-column scan is a grounding vehicle, not a real restriction on generality. The operators map exactly (NOT_EQUALS↔Ne, trueLiteral↔True, Not↔Not) with the same `left` symbol shared across both sides, and the identity `x <> TRUE ≡ NOT x` holds under three-valued logic for every case (TRUE, FALSE, NULL), so no precondition (e.g. NOT NULL) is silently assumed — the structurally distinct before/after makes the proof non-vacuous and the FULL scope tag is honest. ```

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
    "nanos": 103583
  }
}
```
