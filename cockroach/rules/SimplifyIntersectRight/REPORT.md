# SimplifyIntersectRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 48  **Verification rounds used:** 3
**Scope detail:** 2-input, single-column, same row type, identity column mapping, with the right input a base scan declared unique (has a strict key).


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/set.opt

SimplifyIntersectRight converts an Intersect operator into an IntersectAll
operator when the right input has a key. This avoids the de-duplication step.

This works because IntersectAll creates a one-to-one mapping between left and
right rows. If there is more than one row with a particular value on the
left side, then there must be at least that many rows with the same value on
the right side in order for the left rows to be preserved in the output.
Therefore, if the right input has a strict key, the output rows will be
de-duplicated for 'free', and an IntersectAll can safely be used.

Extracted from `set.opt` (which defines multiple rules — implement specifically `SimplifyIntersectRight`, not the other rules in that file):

```
# SimplifyIntersectRight converts an Intersect operator into an IntersectAll
# operator when the right input has a key. This avoids the de-duplication step.
#
# This works because IntersectAll creates a one-to-one mapping between left and
# right rows. If there is more than one row with a particular value on the
# left side, then there must be at least that many rows with the same value on
# the right side in order for the left rows to be preserved in the output.
# Therefore, if the right input has a strict key, the output rows will be
# de-duplicated for 'free', and an IntersectAll can safely be used.
[SimplifyIntersectRight, Normalize]
(Intersect $left:* $right:* & (HasStrictKey $right) $colMap:*)
=>
(IntersectAll $left $right $colMap)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous — a set-INTERSECT (before) vs. a SEMI-join (after) are genuinely different operators, and the equality hinges on the right scan's `unique=true` + non-nullable type, which faithfully models `HasStrictKey` (uniqueness *and* non-null), so the precondition is present rather than missing. Under that strict-key precondition the encoded after() (R SEMI JOIN L) is definitionally the same bag as the source rule's IntersectAll RHS — both yield exactly the right-side rows whose values also occur in the left — so the proof certifies the rule's actual content (the de-duplication is eliminable), which is the right workaround since QED models only the set (not bag) variant of INTERSECT and cannot express IntersectAll directly. The narrowing to 2 inputs (the rule's own shape), one shared column, and identity column mapping is a genuine DSL limitation (multi-column scans cannot carry a uniqueness key) that is honestly tagged PARTIAL and does not dilute the semantic argument, which is purely about per-row value equality and multiplicity. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7018794
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34465083
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 853875
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 415042
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19419417
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34554333
  },
  "total_duration": {
    "secs": 0,
    "nanos": 69542875
  }
}
```
