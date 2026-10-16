# SimplifyEqualsAnyTuple

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 3
**Scope detail:** models x = ANY(tuple) as S SEMI JOIN V ON S.c0=V.c0 and x IN (tuple) as S FILTER (S.c0 IN (SELECT c0 FROM V)), with the scalar input x as an uninterpreted column S.c0 and the tuple as an uninterpreted 1-column relation V, so the equivalence holds for all instantiations of V.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifyEqualsAnyTuple converts a scalar ANY operation to an IN comparison.
It transforms

x = ANY (...)

to

x IN (...)

Which allows scans to be constrained.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyEqualsAnyTuple`, not the other rules in that file):

```
# SimplifyEqualsAnyTuple converts a scalar ANY operation to an IN comparison.
# It transforms
#
#   x = ANY (...)
#
# to
#
#   x IN (...)
#
# Which allows scans to be constrained.
[SimplifyEqualsAnyTuple, Normalize]
(AnyScalar $input:* $tuple:(Tuple) $cmp:* & (OpsAreSame $cmp Eq))
=>
(In $input $tuple)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (S SEMI JOIN V ON S.c0=V.c0) and after() (S FILTER (S.c0 IN (SELECT c0 FROM V))) are structurally distinct relational forms of the same scalar identity, so the proof is non-vacuous, and the correspondence (semi-join = `= ANY`, IN-subquery filter = `IN`) matches the rule's direction and semantics under bag/3VL semantics for all instantiations, including duplicate and empty V; fixing the comparison to concrete EQUALS is faithful because the source rule's `(OpsAreSame $cmp Eq)` guard restricts it to Eq, modeling x as an uninterpreted column S.c0 and the tuple as an arbitrary uninterpreted 1-column relation V is at least as general as a scalar input and constant tuple, with S and V correctly shared and no dropped preconditions. The FULL scope tag is therefore the correct and honest classification.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9776875
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 37772583
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1056250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 493875
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 23046542
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 37891292
  },
  "total_duration": {
    "secs": 0,
    "nanos": 76659417
  }
}
```
