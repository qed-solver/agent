# MapEqualityIntoJoinLeftAndRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** only the INNER-join three-variable case from the rule's own example: the join condition is exactly two cross-side equalities (a.x = b.x AND b.x = a.y) over one shared column type, remapped to (a.x = a.y AND b.x = a.y) to drop one cross-side condition, and the general multi-condition, multi-join-kind equivalence-class remapping driven by MapJoinOpEqualities' functional-dependency closure is not modeled.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

MapEqualityIntoJoinLeftAndRight checks whether it is possible to map
equality conditions in a join to use different variables so that the
number of conditions crossing both sides of a join are minimized. If so,
the MapEqualityConditions function performs this mapping to construct new
filters.

For example, consider this query:

SELECT * FROM a, b WHERE a.x = b.x AND b.x = a.y;

As written, both equality conditions contain variables from both sides of
the join. We can rewrite this query, however, so that only one condition
spans both sides:

SELECT * FROM a, b WHERE a.x = a.y AND b.x = a.y;

Now the condition a.x = a.y is fully bound by the left side of the join,
and is available to be pushed down by PushFilterIntoJoinLeft.

See the MapEqualityConditions function for more details.

Extracted from `join.opt` (which defines multiple rules — implement specifically `MapEqualityIntoJoinLeftAndRight`, not the other rules in that file):

```
# MapEqualityIntoJoinLeftAndRight checks whether it is possible to map
# equality conditions in a join to use different variables so that the
# number of conditions crossing both sides of a join are minimized. If so,
# the MapEqualityConditions function performs this mapping to construct new
# filters.
#
# For example, consider this query:
#
#   SELECT * FROM a, b WHERE a.x = b.x AND b.x = a.y;
#
# As written, both equality conditions contain variables from both sides of
# the join. We can rewrite this query, however, so that only one condition
# spans both sides:
#
#   SELECT * FROM a, b WHERE a.x = a.y AND b.x = a.y;
#
# Now the condition a.x = a.y is fully bound by the left side of the join,
# and is available to be pushed down by PushFilterIntoJoinLeft.
#
# See the MapEqualityConditions function for more details.
[MapEqualityIntoJoinLeftAndRight, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:* & ^(HasOuterCols $left)
    $right:* & ^(HasOuterCols $right)
    $on:* &
        (CanMapJoinOpEqualities
            $on
            $leftCols:(OutputCols $left)
            $rightCols:(OutputCols $right)
        )
    $private:*
)
=>
((OpName)
    $left
    $right
    (MapJoinOpEqualities $on $leftCols $rightCols)
    $private
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's core equality-remapping operation for its own documented minimal instance: before() uses the two cross-boundary equalities (a.x=b.x ∧ b.x=a.y) and after() the remapped set (b.x=a.y ∧ a.x=a.y), which are structurally different and whose equivalence (all three columns equal) is a genuine equality-transitivity/symmetry fact QED actually had to prove via real EQUALS (not an uninterpreted symbol, whose equivalence would fail), with the join columns and the shared b.x=a.y condition wired correctly and the base-scan inputs satisfying the rule's no-outer-columns guard. The under-generalization (INNER only, fixed two-equality/three-column shape versus the rule's 8 join kinds and arbitrary MapJoinOpEqualities FD-closure remapping) is forced by RuleScript's single before/after pattern expressiveness rather than laziness, and is honestly flagged SCOPE: PARTIAL, so this is a genuine, non-degenerate special case—not a vacuous or misleading proof. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7441959
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34967042
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 829292
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 484792
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19163375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35073709
  },
  "total_duration": {
    "secs": 0,
    "nanos": 69539584
  }
}
```
