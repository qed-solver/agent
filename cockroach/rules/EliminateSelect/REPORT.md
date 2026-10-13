# EliminateSelect

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** public record EliminateSelect() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

EliminateSelect discards an unnecessary Select operator in the case where its
filter is always true. Keep this near the top of the file so that it tends to
be checked early.

Extracted from `select.opt` (which defines multiple rules — implement specifically `EliminateSelect`, not the other rules in that file):

```
# EliminateSelect discards an unnecessary Select operator in the case where its
# filter is always true. Keep this near the top of the file so that it tends to
# be checked early.
[EliminateSelect, Normalize]
(Select $input:* [])
=>
$input
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful: `before()` is a Filter with a literal-True condition over a scan and `after()` is the same bare scan, so the two sides are structurally distinct (non-vacuous) and correctly model a Select with an empty, always-true filter list — the true literal (not an uninterpreted predicate) is the right choice, since literal True passes every row with no null edge cases. The scan is a fully uninterpreted bag, so the proved claim σ_true(B) = B for all bags B is exactly the rule's complete semantic content: EliminateSelect is input-agnostic and has no side conditions, and treating the leaf as an uninterpreted scan is the DSL's standard treatment of "any $input" (as in the FilterMerge worked example), making `SCOPE: FULL` honest. There are no symbol-sharing, wrong-operator, or missing-precondition issues.

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
    "nanos": 293958
  }
}
```
