# TryRemapSelectOuterCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 57  **Verification rounds used:** 5
**Scope detail:** the Select's input is an uninterpreted 3-column scan (col 0 standing in for the outer column, col 1 for the local column, col 2 an unrelated extra column), $on is the genuine equality col 0 = col 1 applied as the Select's filter, and the remapped reference is a unary uninterpreted predicate h applied in a filter nested in the input


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryRemapSelectOuterCols is similar to TryRemapJoinOuterColsRight, but it
applies to the input of a Select.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryRemapSelectOuterCols`, not the other rules in that file):

```
# TryRemapSelectOuterCols is similar to TryRemapJoinOuterColsRight, but it
# applies to the input of a Select.
[TryRemapSelectOuterCols, Normalize]
(Select
    $input:* & (HasOuterCols $input)
    $on:* &
        (CanMaybeRemapOuterCols $input $on) &
        (Let ($remapped $ok):(TryRemapOuterCols $input $on) $ok)
)
=>
(Select $remapped $on)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's core mechanism — under the Select filter equality c0=c1 (outer stand-in = local column), the outer-column reference inside the nested input filter h(c0) is remapped to h(c1) while $on is left unchanged, exactly as `Select $input $on => Select $remapped $on` prescribes — and before()/after() are genuinely structurally different, so the proof is non-vacuous. The specializations (3-column uninterpreted scan, the filter being exactly the enabling equality rather than an arbitrary conjunctive $on, and a single unary uninterpreted predicate one level below the filter) are real restrictions of the full rule but are accurate, specific, and honestly declared in the SCOPE line, and the identity is a true instance of the general remap since it holds a fortiori for the constant-per-outer-row case.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5952375
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34668875
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1008750
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 409167
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16291375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34756208
  },
  "total_duration": {
    "secs": 0,
    "nanos": 66621375
  }
}
```
