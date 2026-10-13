# FoldNotNull

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 10  **Verification rounds used:** 1
**Scope detail:** public record FoldNotNull() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

FoldNotNull replaces NOT(Null) with Null.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `FoldNotNull`, not the other rules in that file):

```
# FoldNotNull replaces NOT(Null) with Null.
[FoldNotNull, Normalize]
(Not (Null))
=>
(Null (BoolType))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures CockroachDB's FoldNotNull rule (`(Not (Null)) => (Null (BoolType))`) by embedding the expression-level boolean normalization in a filter context: `Filter(Not(NULL), R)` before and `Filter(NULL), R)` after, with a properly-typed BOOLEAN null literal matching the source's `(Null (BoolType))`. The rule has no free variables, no preconditions, and no join-type or other parameters, so the scan is purely a relational carrier and SCOPE: FULL is accurate; the structural difference (presence vs. absence of the `Not` wrapper) is exactly the rewrite the source rule specifies, making the proof non-vacuous. ```

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
    "nanos": 80083
  }
}
```
