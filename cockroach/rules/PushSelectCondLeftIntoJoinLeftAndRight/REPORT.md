# PushSelectCondLeftIntoJoinLeftAndRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 26  **Verification rounds used:** 2
**Scope detail:** LEFT JOIN only (the rule's documented case); one-column inputs joined on the single equality L.0=R.0; pushed filter is a single unary predicate on the left key column, mapped to the right key column


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

PushSelectCondLeftIntoJoinLeftAndRight applies to the case when a condition
bound by the left side of a join can be mapped to the right side using
equality columns from the ON condition of the join. It pushes the original
filter to the left side, and the mapped filter to the right side.
For example, consider this query:

SELECT * FROM l LEFT JOIN r ON l.x = r.x WHERE l.x = 5;

This can safely be converted to:

SELECT * FROM (SELECT * FROM l WHERE l.x = 5)
LEFT JOIN (SELECT * FROM r WHERE r.x = 5) ON l.x = r.x;

It's not normally correct to push filters from the SELECT clause to
the right side of a LEFT JOIN, since those rows might still show up
in the output as NULL-extended rows from the left side. In this case,
however, for any rows removed from the right side, the matching rows are
also removed from the left side (and thus removed from the output).
To ensure that this is the case, it's important that the filter only refers
to columns on the left side that have corresponding equivalent columns on
the right side.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectCondLeftIntoJoinLeftAndRight`, not the other rules in that file):

```
# PushSelectCondLeftIntoJoinLeftAndRight applies to the case when a condition
# bound by the left side of a join can be mapped to the right side using
# equality columns from the ON condition of the join. It pushes the original
# filter to the left side, and the mapped filter to the right side.
# For example, consider this query:
#
#   SELECT * FROM l LEFT JOIN r ON l.x = r.x WHERE l.x = 5;
#
# This can safely be converted to:
#
#   SELECT * FROM (SELECT * FROM l WHERE l.x = 5)
#   LEFT JOIN (SELECT * FROM r WHERE r.x = 5) ON l.x = r.x;
#
# It's not normally correct to push filters from the SELECT clause to
# the right side of a LEFT JOIN, since those rows might still show up
# in the output as NULL-extended rows from the left side. In this case,
# however, for any rows removed from the right side, the matching rows are
# also removed from the left side (and thus removed from the output).
# To ensure that this is the case, it's important that the filter only refers
# to columns on the left side that have corresponding equivalent columns on
# the right side.
[PushSelectCondLeftIntoJoinLeftAndRight, Normalize]
(Select
    $input:(LeftJoin | LeftJoinApply | SemiJoin | SemiJoinApply
            | AntiJoin | AntiJoinApply
        $left:*
        $right:*
        $on:*
        $private:*
    )
    $filters:[
        ...
        $item:(FiltersItem $condition:*) &
            (IsBoundBy $item (OutputCols $left)) &
            (CanMapJoinOpFilter
                $item
                $rightCols:(OutputCols $right)
                $equivSet:(GetEquivGroups $on $left $right)
            )
        ...
    ]
)
=>
(Select
    ((OpName $input)
        (Select $left [ (FiltersItem $condition) ])
        (Select
            $right
            [
                (FiltersItem
                    (MapJoinOpFilter $item $rightCols $equivSet)
                )
            ]
        )
        $on
        $private
    )
    (RemoveFiltersItem $filters $item)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's documented case generalized honestly: arbitrary uninterpreted tables L, R with an arbitrary unary predicate P, where before = Filter(P(l.x), L ⋈_LEFT R ON l.x = r.x) and after = (L σ P) ⋈_LEFT (R σ P) — structurally and semantically distinct, so the proof is non-vacuous; sharing the single symbol pOp for both pushed filters correctly encodes MapJoinOpFilter via the ON-equality equivalence group (with two independent predicates the rewrite would be unsound), and the concrete EQUALS in the join condition is the right way to express the GetEquivGroups/CanMapJoinOpFilter precondition rather than an over-constraining artifact. The narrowing to LEFT JOIN, single equality key, and unary filter on that key is a genuine special case limited by QED's inability to express arbitrary column-remapping equivalence sets, it is honestly declared on the SCOPE line, and it preserves the rule's distinctive content (pushing a left-bound filter into the right side of a LEFT JOIN, the direction the simpler PushSelectIntoJoinLeft does not cover).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 14347625
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 42453792
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 847416
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 975625
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 31492458
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 42652375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 90097584
  }
}
```
