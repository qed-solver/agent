# FoldNotTrue

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 3  **Verification rounds used:** 1
**Scope detail:** public record FoldNotTrue() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

FoldNotTrue replaces NOT(True) with False.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `FoldNotTrue`, not the other rules in that file):

```
# FoldNotTrue replaces NOT(True) with False.
[FoldNotTrue, Normalize]
(Not (True))
=>
(False)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (Filter with condition NOT(TRUE)) and after() (Filter with condition FALSE) are structurally distinct, and the transformation is exactly the source rule's rewrite, so the proof is non-vacuous. The source rule has no free variables — it is a pure constant fold — so nothing that should be uninterpreted was hard-coded: the scan is fully uninterpreted (arbitrary bag of arbitrary type), the proof covers every relation, and no preconditions or null assumptions are involved since NOT(TRUE)=FALSE holds unconditionally. As a scalar constant-fold rule embedded in a relational DSL that has no expression metavariables, placing the matched expression as the entire filter condition over an arbitrary source is its maximal relational form, so SCOPE: FULL is honest and the encoding is faithful and non-degenerate. ```

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
    "nanos": 346250
  }
}
```
