# CommuteNullIs

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** only the Is (IS NOT DISTINCT FROM) branch of (Is | IsNot) is encoded; the structurally identical IsNot branch requires its own before()/after() pair


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

CommuteNullIs moves a NULL onto the right side of an IS/IS NOT comparison.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `CommuteNullIs`, not the other rules in that file):

```
# CommuteNullIs moves a NULL onto the right side of an IS/IS NOT comparison.
[CommuteNullIs, Normalize]
(Is | IsNot $left:(Null) $right:^(Null))
=>
((OpName) $right $left)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding correctly models the `Is` branch of CockroachDB's CommuteNullIs as a scalar commutation of `IS_NOT_DISTINCT_FROM` with a NULL literal (before: `NULL IS_NOT_DISTINCT_FROM x`, after: `x IS_NOT_DISTINCT_FROM NULL`), using an uninterpreted type and a single arbitrary column so the proof is universally quantified over all types and values; before() and after() are structurally distinct (swapped operand order) so the proof is non-vacuous; the `^(Null)` side-condition from the source is correctly identified as a no-op-prevention guard rather than a semantic precondition, so its omission only widens (not narrows) the proven claim; and the PARTIAL scope tag honestly and specifically discloses that the structurally-identical `IsNot` branch (`IS_DISTINCT_FROM`) is not encoded due to the single before/after pair constraint of `RRule`. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5400625
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 41888750
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 842125
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 310958
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 15328583
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 41955417
  },
  "total_duration": {
    "secs": 0,
    "nanos": 72450083
  }
}
```
