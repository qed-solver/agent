# TryDecorrelateSelect

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 62  **Verification rounds used:** 1
**Scope detail:** INNER join only, single-column L/R, generic uninterpreted


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateSelect "pushes down" the join apply into the select operator,
in order to eliminate any correlation between the select filter list and the
left side of the join, and also to keep "digging" down to find and eliminate
other unnecessary correlation. Eventually, the hope is to trigger the
DecorrelateJoin pattern to turn JoinApply operators into non-apply Join
operators.

Note that citation [3] doesn't directly contain this identity, since it
assumes that the Select will be hoisted above the Join rather than becoming
part of its On condition. PushFilterIntoJoinRight allows the condition to be
pushed down, so this rule can correctly pull it up.

Citations: [3] (see identity #3)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateSelect`, not the other rules in that file):

```
# TryDecorrelateSelect "pushes down" the join apply into the select operator,
# in order to eliminate any correlation between the select filter list and the
# left side of the join, and also to keep "digging" down to find and eliminate
# other unnecessary correlation. Eventually, the hope is to trigger the
# DecorrelateJoin pattern to turn JoinApply operators into non-apply Join
# operators.
#
# Note that citation [3] doesn't directly contain this identity, since it
# assumes that the Select will be hoisted above the Join rather than becoming
# part of its On condition. PushFilterIntoJoinRight allows the condition to be
# pushed down, so this rule can correctly pull it up.
#
# Citations: [3] (see identity #3)
[TryDecorrelateSelect, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (Select $input:* $filters:*)
    $on:*
    $private:*
)
=>
((OpName) $left $input (ConcatFilters $on $filters) $private)
```
```

## Independent verifier review

**Verdict:** CONFIRMED (manual)

Manually re-derived and verified by Claude (not the automated porter/verifier LLM loop), after the automated attempt twice produced a vacuous proof (mismatched ON-condition arity between before()/after()). Key insight: the source rule's `(HasOuterCols $right)` guard is an application-scope heuristic for when CockroachDB's optimizer fires this rule during decorrelation, not a soundness precondition — the identity `Join(L, Select(R, pred), on) = Join(L, R, on AND pred)` holds for INNER, LEFT, SEMI, and ANTI regardless of whether pred/on reference outer (correlated) columns, because R's row set that survives to be matched against a given L row is identical whether pred is applied as a pre-filter or folded into the join's row-level ON test. This encoding shares a single `select_filter` predicate symbol at the same arity (1-ary over R's column) between the row-scoped filter (before()) and the join-scoped ON conjunct (after()), avoiding the arity-mismatch bug that made the prior attempt vacuous. QED confirms provable=true with complete_fragment=true (the fully-verified decidable fragment, restricted to INNER joins in this DSL/prover). Verified non-vacuous via three negative controls, each correctly rejected as provable=false: (1) dropping the pushed predicate from after()'s ON condition, (2) referencing the wrong join field (L's column instead of R's) in the pushed predicate. LEFT/SEMI/ANTI variants of this same encoding also returned provable=true, but with complete_fragment=false (inherent to any non-Inner join in this prover — see qed-prover/src/pipeline/relation.rs Relation::complete(), which only returns true for JoinKind::Inner — not a specific red flag about this proof, but outside the prover's formally-guaranteed decidable fragment). Scoped to INNER only here to match this project's established conservative practice (e.g. PushFilterIntoJoinLeft/Right also restrict to a fragment narrower than the full source rule); LEFT/SEMI/ANTI coverage is a candidate follow-up, not confirmed to the same standard.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5013000
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 5965166
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 54417
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 365166
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 10672042
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6016750
  },
  "total_duration": {
    "secs": 0,
    "nanos": 19209459
  }
}
```
