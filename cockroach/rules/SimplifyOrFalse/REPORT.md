# SimplifyOrFalse

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 8  **Verification rounds used:** 2
**Scope detail:** assumes the Or-with-False occurs as the entire filter predicate, not nested inside another boolean operator or used as a join condition


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyOrFalse simplifies the Or operator by discarding a False condition on
the right side.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyOrFalse`, not the other rules in that file):

```
# SimplifyOrFalse simplifies the Or operator by discarding a False condition on
# the right side.
[SimplifyOrFalse, Normalize]
(Or $left:* (False))
=>
$left
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core boolean identity of SimplifyOrFalse (Or(x, False) ≡ x) using a properly uninterpreted predicate `left` as the arbitrary left operand, with a structurally non-trivial before/after pair (filter with Or vs. filter alone). The SCOPE: PARTIAL tag is honest and specific — the restriction to "entire filter predicate, not nested" is a genuine scoping choice in a relational prover that cannot express arbitrary scalar-position rewrites, and the result remains a useful, non-degenerate proof of the identity for all instantiations of the uninterpreted symbol. ```

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
    "nanos": 78583
  }
}
```
