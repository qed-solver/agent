# SimplifyOrTrue

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** public record SimplifyOrTrue() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyOrTrue maps the Or operator to True if its right input is True.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyOrTrue`, not the other rules in that file):

```
# SimplifyOrTrue maps the Or operator to True if its right input is True.
[SimplifyOrTrue, Normalize]
(Or * $right:(True))
=>
$right
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a direct, faithful translation of `SimplifyOrTrue`: the LHS `(Or * True)` becomes the filter condition `Or(left, True)` where `left` is an uninterpreted predicate standing in for the `*` wildcard and the `True` literal sits on the right exactly as the rule specifies, while the RHS `$right` (i.e. `True`) becomes `filter(trueLiteral)` — doing precisely the boolean simplification and nothing more (the filter is left in place, matching the source rule rather than over-reaching to delete it). It is non-vacuous (the `Or` appears only in `before()`), has no spurious symbol sharing and no omitted preconditions, and because `left` is uninterpreted the proof establishes the full identity `Or(x, True) = True` for every predicate `x` in the minimal relational (filter-over-scan) context the DSL requires, so the `SCOPE: FULL` tag is accurate. ```

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
    "nanos": 68458
  }
}
```
