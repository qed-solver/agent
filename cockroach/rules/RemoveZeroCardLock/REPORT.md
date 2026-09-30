# RemoveZeroCardLock

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 43  **Verification rounds used:** 5
**Scope detail:** Lock is modeled as an uninterpreted filter predicate and the HasZeroRows precondition as a structurally empty input, since the DSL has no Lock operator or cardinality constraint


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/mutation.opt

RemoveZeroCardLock removes lock operations when we know no rows will be locked.

Extracted from `mutation.opt` (which defines multiple rules — implement specifically `RemoveZeroCardLock`, not the other rules in that file):

```
# RemoveZeroCardLock removes lock operations when we know no rows will be locked.
[RemoveZeroCardLock, Normalize]
(Lock $rows:* & (HasZeroRows $rows))
=>
$rows
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The rule only fires when `$rows` has zero rows — i.e. `$rows` is bag-equal to ∅ — so modeling `$rows` as a structurally empty scan and `Lock` as an uninterpreted filter encodes the rule's entire applicable domain (`filter(∅) = ∅`), not an arbitrary narrowing. `before()` (a Filter over the empty relation) genuinely differs from `after()` (the bare empty relation), so the proof is non-vacuous and reflects the real optimization of dropping a lock over zero rows rather than being structurally identical. The PARTIAL scope tag is honest and specific, correctly naming the genuine limitation (the DSL has no Lock operator or cardinality constraint to express `HasZeroRows` as a guard).

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
    "nanos": 24458
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 63791
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
    "nanos": 163750
  }
}
```
