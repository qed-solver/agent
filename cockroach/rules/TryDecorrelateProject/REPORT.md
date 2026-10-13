# TryDecorrelateProject

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 62  **Verification rounds used:** 5
**Scope detail:** INNER join only; right Project a pure column remap (passthrough, no computed projections); 1-col left / 2-col right


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateProject "pushes down" a Join into a Project operator, in an
attempt to eliminate any correlation between the projection list and the left
side of the join, and also to keep "digging" down to find and eliminate other
unnecessary correlation. The eventual hope is to trigger the DecorrelateJoin
rule to turn a JoinApply operator into a non-apply Join operator.

Citations: [3] (see identity #4)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateProject`, not the other rules in that file):

```
# TryDecorrelateProject "pushes down" a Join into a Project operator, in an
# attempt to eliminate any correlation between the projection list and the left
# side of the join, and also to keep "digging" down to find and eliminate other
# unnecessary correlation. The eventual hope is to trigger the DecorrelateJoin
# rule to turn a JoinApply operator into a non-apply Join operator.
#
# Citations: [3] (see identity #4)
[TryDecorrelateProject, Normalize]
(InnerJoin | InnerJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (Project $input:* $projections:* $passthrough:*)
    $on:*
    $private:*
)
=>
(Select
    (Project
        ((OpName) $left $input [] $private)
        $projections
        (UnionCols (OutputCols $left) $passthrough)
    )
    $on
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the structural essence of the original rule (Join(left, Project(input,…), on) → Filter(on, Project(CrossJoin(left, input), …))) with uninterpreted predicate/types; before and after are genuinely distinct plan shapes (conditional join vs. cross-join + reorder + filter), and the SCOPE line accurately and specifically states the narrowings (INNER-only, pure column-swap project, fixed 1-col/2-col arities) that make the general case intractable in the current DSL.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7437042
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33835042
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 837459
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 507792
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 20057375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33961167
  },
  "total_duration": {
    "secs": 0,
    "nanos": 69713667
  }
}
```
