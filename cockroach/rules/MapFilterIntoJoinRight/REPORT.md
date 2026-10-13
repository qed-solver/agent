# MapFilterIntoJoinRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 28  **Verification rounds used:** 2
**Scope detail:** only the INNER-join, single-equality, single-column case: ON is exactly (a.x = b.x) AND f(a.x) and the filter conjunct is remapped to f(b.x); the rule's other join kinds, multi-equality equivalence sets, and arbitrary filter expressions are not modeled.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

MapFilterIntoJoinRight is symmetric with MapFilterIntoJoinLeft. It maps
Join filter conditions to use columns from the right side of the join rather
than the left side. See that rule's comments for more details.

Extracted from `join.opt` (which defines multiple rules — implement specifically `MapFilterIntoJoinRight`, not the other rules in that file):

```
# MapFilterIntoJoinRight is symmetric with MapFilterIntoJoinLeft. It maps
# Join filter conditions to use columns from the right side of the join rather
# than the left side. See that rule's comments for more details.
[MapFilterIntoJoinRight, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:* & ^(HasOuterCols $right)
    $on:[
        ...
        $item:* &
            ^(FiltersItem (Eq (Variable) (Variable))) &
            ^(IsBoundBy $item $rightCols:(OutputCols $right)) &
            (CanMapJoinOpFilter
                $item
                $rightCols
                $equivSet:(GetEquivGroups $on $left $right)
            )
        ...
    ]
    $private:*
)
=>
((OpName)
    $left
    $right
    (ReplaceFiltersItem
        $on
        $item
        (MapJoinOpFilter $item $rightCols $equivSet)
    )
    $private
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (ON = a.x=b.x ∧ f(a.x)) and after() (ON = a.x=b.x ∧ f(b.x)) are structurally distinct, so the proof is not vacuous — it requires genuine congruence reasoning (under the eq conjunct, f(a.x) ≡ f(b.x)), which is exactly the equivalence-class column remapping that MapFilterIntoJoinRight performs, and the symbol handling is correct: one shared uninterpreted conjunct f, the shared equality kept in place, and two distinct same-type columns. The restriction to INNER join, single equality, and a single one-argument conjunct is real (the source pattern also covers Left/Semi/Anti joins and multi-equality equiv sets), but it is specific, non-degenerate, and honestly tagged on the SCOPE line — and since MetaJoinType desugars to INNER for the prover, no more general single-rule encoding was available. No preconditions are silently missing: the CanMapJoinOpFilter condition is structurally satisfied by the one equality, no unique keys or nullability assumptions were added (unique=false), and the rule's remap is self-contained (the downstream push-down is a separate rule the porter correctly did not conflate).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5385959
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6400167
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 62500
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 345458
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 10957959
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6448417
  },
  "total_duration": {
    "secs": 0,
    "nanos": 19848750
  }
}
```
