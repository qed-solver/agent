# MapFilterIntoJoinLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3
**Scope detail:** INNER join only, one single-key equi conjunct, one 2-ary ON-clause item referencing exactly the right key plus one left non-key column, rebound to the left key.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

MapFilterIntoJoinLeft maps a filter that is not bound by the left side of
the join to use the columns from the left side. This will allow
the filter to be pushed down by the PushFilterIntoJoinLeft rule.
For example, consider this query:

SELECT * FROM a INNER JOIN b ON a.x = b.x AND b.x + a.y < 5

In this case, we can map b.x + a.y < 5 to the left side by replacing b.x
with the equivalent column a.x.
NOTE: This rule only applies to cases where it is not possible or not safe
to map the filter to both sides. If it can be mapped to both sides, it
will be handled by PushFilterIntoJoinLeftAndRight (which must be
ordered above this rule). For performance reasons, this rule should
be ordered before PushFilterIntoJoinLeft (otherwise,
PushFilterIntoJoinLeft might need to be applied multiple times).

Extracted from `join.opt` (which defines multiple rules — implement specifically `MapFilterIntoJoinLeft`, not the other rules in that file):

```
# MapFilterIntoJoinLeft maps a filter that is not bound by the left side of
# the join to use the columns from the left side. This will allow
# the filter to be pushed down by the PushFilterIntoJoinLeft rule.
# For example, consider this query:
#
#   SELECT * FROM a INNER JOIN b ON a.x = b.x AND b.x + a.y < 5
#
# In this case, we can map b.x + a.y < 5 to the left side by replacing b.x
# with the equivalent column a.x.
# NOTE: This rule only applies to cases where it is not possible or not safe
#       to map the filter to both sides. If it can be mapped to both sides, it
#       will be handled by PushFilterIntoJoinLeftAndRight (which must be
#       ordered above this rule). For performance reasons, this rule should
#       be ordered before PushFilterIntoJoinLeft (otherwise,
#       PushFilterIntoJoinLeft might need to be applied multiple times).
[MapFilterIntoJoinLeft, Normalize]
(InnerJoin | InnerJoinApply | SemiJoin | SemiJoinApply
    $left:* & ^(HasOuterCols $left)
    $right:*
    $on:[
        ...
        $item:* &
            ^(FiltersItem (Eq (Variable) (Variable))) &
            ^(IsBoundBy $item $leftCols:(OutputCols $left)) &
            (CanMapJoinOpFilter
                $item
                $leftCols
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
        (MapJoinOpFilter $item $leftCols $equivSet)
    )
    $private
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous capture of the rule's core transformation — it reproduces the rule's own documented example (`ON a.x=b.x AND b.x+a.y<5` → `ON a.x=b.x AND a.x+a.y<5`) as an INNER join where a right-bound filter item `g(r0,y)` is rebound to the left key `g(l0,y)` while the equi conjunct is retained; the proof is genuine (not structural) because it hinges on congruence of the shared uninterpreted predicate `g` under the *concrete* `EQUALS` conjunct, which is exactly the semantic content the rule relies on. The field indices, join kind, ON-list shape, and symbol sharing (one shared `g`, distinct l0/y/r0 fields) are all correct, no real semantic precondition (not-left-bound, mappable-via-equality, no outer cols) is silently dropped, and the narrowing to INNER-only with a single equi conjunct and a single 2-ary item is specific, honestly disclosed in the SCOPE line, and largely inherent to what the DSL can express — a legitimate, useful special case rather than a degenerate or misleading one. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7673291
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36820916
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 796542
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 457916
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19479083
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36926750
  },
  "total_duration": {
    "secs": 0,
    "nanos": 71800916
  }
}
```
