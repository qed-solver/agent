# FoldEqTrue

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** public record FoldEqTrue() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

FoldEqTrue replaces x = True with x.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldEqTrue`, not the other rules in that file):

```
# FoldEqTrue replaces x = True with x.
[FoldEqTrue, Normalize]
(Eq $left:* (True))
=>
$left
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous (before is `Filter(EQUALS(left, true), Source)` vs after `Filter(left, Source)` — structurally distinct, forcing QED to actually prove the `b = true ≡ b` identity), faithful in its operator choices (concrete `EQUALS` and `trueLiteral` because the rule specifically rewrites `Eq ... (True)`, with the left operand correctly left as an uninterpreted predicate over the row to match `$left:*`), and has no shared-symbol or missing-precondition issues: the same single `left` and `Source` symbols appear on both sides exactly as the rule requires, the source rule has no side conditions, and `x = True` ≡ `x` holds even under 3VL/NULL semantics, so `// SCOPE: FULL` is honest (filter-hosting a scalar boolean law over an arbitrary relation is the standard full encoding for this DSL, as in the FilterMerge example).

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
    "nanos": 342667
  }
}
```
