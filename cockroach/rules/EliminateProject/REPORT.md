# EliminateProject

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** the identity project is over a fixed two-column input; the original rule applies to any input arity


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/project.opt

EliminateProject discards a Project operator which is not adding or removing
columns.

Extracted from `project.opt` (which defines multiple rules — implement specifically `EliminateProject`, not the other rules in that file):

```
# EliminateProject discards a Project operator which is not adding or removing
# columns.
[EliminateProject, Normalize]
(Project
    $input:*
    $projections:[]
    $passthrough:* &
        (ColsAreEqual $passthrough (OutputCols $input))
)
=>
$input
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures both preconditions of the source rule — an empty projections list and a passthrough set exactly equal to the input's output columns — as Project(Input, [col0, col1]) over an uninterpreted two-column scan rewritten to the bare scan, and before() is structurally distinct from after() (a Project node on top of the scan vs. the scan alone), so the proof is not vacuous. The only narrowing is the fixed arity of 2, which the SCOPE line states specifically and honestly; arbitrary input arity is not expressible end-to-end because QED decides over concrete schemas with no arity quantification, so this is a genuine, justified special case rather than a fixable DSL gap. There are no predicates, joins, null or key assumptions to get wrong, and no symbol-sharing issues, so no preconditions are silently absent. ```

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
    "nanos": 104625
  }
}
```
