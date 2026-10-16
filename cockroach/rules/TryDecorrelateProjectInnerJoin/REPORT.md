# TryDecorrelateProjectInnerJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 72  **Verification rounds used:** 5
**Scope detail:** plain (non-Apply) LEFT/INNER joins over 1-col INTEGER scans; right Project is [pass, pass, f(pass, pass)]; on/innerOn/f uninterpreted


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateProjectInnerJoin tries to decorrelate by hoisting the filter of
an InnerJoin operator that sits below a LeftJoin/Project operator combo. The
Project operator itself can't be reordered above the LeftJoin like it can in
the InnerJoin case. However, the InnerJoin filter can be merged with the
LeftJoin filter, which is enough to decorrelate in several useful cases. This
rule works similarly to TryDecorrelateProjectSelect.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateProjectInnerJoin`, not the other rules in that file):

```
# TryDecorrelateProjectInnerJoin tries to decorrelate by hoisting the filter of
# an InnerJoin operator that sits below a LeftJoin/Project operator combo. The
# Project operator itself can't be reordered above the LeftJoin like it can in
# the InnerJoin case. However, the InnerJoin filter can be merged with the
# LeftJoin filter, which is enough to decorrelate in several useful cases. This
# rule works similarly to TryDecorrelateProjectSelect.
[TryDecorrelateProjectInnerJoin, Normalize, HighPriority]
(LeftJoinApply
    $left:*
    $right:(Project
        $join:(InnerJoin | InnerJoinApply
            $innerLeft:*
            $innerRight:*
            $innerOn:* &
                ^(FiltersBoundBy
                    $innerOn
                    (OutputCols2 $innerLeft $innerRight)
                )
            $innerPrivate:*
        )
        $projections:*
        $passthrough:*
    )
    $on:*
    $private:*
)
=>
(Project
    (LeftJoinApply
        $left
        (Project
            ((OpName $join)
                $innerLeft
                $innerRight
                []
                $innerPrivate
            )
            $projections
            (UnionCols $passthrough (OutputCols $join))
        )
        (ConcatFilters $on $innerOn)
        $private
    )
    []
    (OutputCols2 $left $right)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding exactly mirrors the rule's shape — LEFT(L, Project(InnerJoin(IL, IR, innerOn)), on) rewritten to LEFT(L, Project(InnerJoin(IL, IR, true)), on ∧ innerOn) — with correct symbol sharing (L/IL/IR/on/innerOn/f shared precisely as the rule's bindings, and the FiltersBoundBy precondition enforced structurally since innerOn can only reference inner-join columns), and the proof is non-vacuous because the plans genuinely differ and the row-correspondence (identical matched multisets and identical left-join no-match cases) is a real identity. The PARTIAL tag is honest and specific (1-column INTEGER scans, plain non-Apply joins, fixed [pass, pass, f] projection shape; Apply/correlation is fundamentally unmodelable in QED), and the omitted outer identity-Project in the after-side is semantically inert, so the proved algebraic core is exactly the source rule's transformation.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 17145585
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 43155333
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1068916
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1311708
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 37432417
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 43492375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 98127333
  }
}
```
