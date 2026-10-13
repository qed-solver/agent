# PushFilterIntoJoinRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 3  **Verification rounds used:** 1
**Scope detail:** INNER join only (no Semi/Left/Full/Anti), one right-bound conjunct with no outer columns, one unbound conjunct, single-column inputs


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

PushFilterIntoJoinRight is symmetric with PushFilterIntoJoinLeft. It pushes
Join filter conditions into the right side of the join rather than into the
left side. See that rule's comments for more details.

Extracted from `join.opt` (which defines multiple rules — implement specifically `PushFilterIntoJoinRight`, not the other rules in that file):

```
# PushFilterIntoJoinRight is symmetric with PushFilterIntoJoinLeft. It pushes
# Join filter conditions into the right side of the join rather than into the
# left side. See that rule's comments for more details.
[PushFilterIntoJoinRight, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:* & ^(HasOuterCols $right)
    $on:[
        ...
        $item:* &
            (IsBoundBy $item $rightCols:(OutputCols $right))
        ...
    ]
    $private:*
)
=>
((OpName)
    $left
    (Select $right (ExtractBoundConditions $on $rightCols))
    (ExtractUnboundConditions $on $rightCols)
    $private
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful (though honestly scope-limited) INNER-join instance of the rule: `before()` is `L ⋈_INNER R ON f(R) ∧ g(L,R)` and `after()` is `L ⋈_INNER (R σ_f) ON g(L,R)`, structurally different in exactly the way the real push-down rewrites, so the proof is non-vacuous. The symbols are genuinely uninterpreted — `f` (right-bound, applied to R's column in both the join-row and the filtered-right contexts) and `g` (residual, cross-referencing both sides) are shared correctly, matching the source's "bound by right cols / extract unbound" split, and the "no outer columns" precondition is satisfied by the plain scans. The narrowing to INNER-only (the source also covers Left/Semi/Anti, which QED's MetaJoinType can't express beyond INNER semantics) and to single-conjunct/single-column inputs is specific and disclosed in the SCOPE line, not a hidden shortcut, so the proved equivalence is a real, non-degenerate special case rather than a misleading artifact.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6672167
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33628167
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 832250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 423709
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18791500
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33731292
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67885459
  }
}
```
