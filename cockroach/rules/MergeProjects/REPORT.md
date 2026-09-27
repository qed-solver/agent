# MergeProjects

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 61  **Verification rounds used:** 4
**Scope detail:** one-column source and the outer projection references only the inner passthrough column, so the inner synthesized column is pruned.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/project.opt

MergeProjects merges an outer Project operator with an inner Project operator
if there are no references to the inner synthesized columns. This has the
side effect of pruning unused synthesized columns of the inner Project.

Extracted from `project.opt` (which defines multiple rules — implement specifically `MergeProjects`, not the other rules in that file):

```
# MergeProjects merges an outer Project operator with an inner Project operator
# if there are no references to the inner synthesized columns. This has the
# side effect of pruning unused synthesized columns of the inner Project.
[MergeProjects, Normalize]
(Project
    $input:(Project $innerInput:* $innerProjections:*)
    $projections:* &
        (CanMergeProjections $projections $innerProjections)
    $passthrough:*
)
=>
(Project
    $innerInput
    (MergeProjections
        $projections
        $innerProjections
        $passthrough
    )
    (DifferenceCols
        $passthrough
        (ProjectionCols $innerProjections)
    )
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the Optgen rule's shape, precondition, and effect: `before` is Project(Project(Source, [passthrough, Bottom(passthrough)]), [Top(passthrough)]) — the outer project references only the inner passthrough column, exactly the `CanMergeProjections` condition — and `after` flattens to a single Project over Source, pruning the inner's unused synthesized column, which is the rule's stated purpose. Nothing that should be symbolic is concretized (Top/Bottom operators and all types are uninterpreted; the passthrough symbol is shared consistently), and `before` and `after` are structurally distinct (an extra project layer and a dead computed column), so the proof is non-vacuous. The only restriction is the one-column arity fixed by the fixed-shape DSL, which is honestly disclosed in the SCOPE line and is a genuine, non-degenerate special case rather than a degenerate or vacuous one.

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
    "nanos": 99584
  }
}
```
