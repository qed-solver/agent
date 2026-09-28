# PushFilterIntoSetOp

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** the 2-input set (distinct) INTERSECT instance with both inputs sharing one column type and one uninterpreted predicate, whereas Cockroach's rule applies to Union/Except/Intersect, all- or distinct-variants, of any arity.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

PushFilterIntoSetOp pushes filters down to both the left and right sides
of all set operators. For example, consider this query:

SELECT * FROM (SELECT x FROM a UNION ALL SELECT y FROM b) WHERE x < 5

In this case, we can map x < 5 to both sides based on the knowledge that
in the union the x out col corresponds to the x column in the (a) table and
the y column in the (b) table. Therefore our mapping becomes x < 5 in the
left side, and y <  5 in the right side. Given this mapping, we can now
safely push the filter down to both sides as follows:

SELECT * FROM (SELECT x FROM a UNION ALL SELECT y FROM b) WHERE x < 5
=>
(SELECT x FROM a WHERE x < 5) UNION ALL (SELECT y FROM b WHERE y < 5)

Pushing (all) the filters down for each of the set operators (Union, Union
All, Except, Except All, Intersect, Intersect All) is logically equivalent
to filtering after applying the set operator. Here's some justification for
this claim:

Notice that each of the set operators this rule applies to, only works on
union compatible relations. The resulting column set after applying the set
operator, is also necessarily a subset of the column set of the (any)
relations it was composed of. And so, any filter applied must have a
corresponding column in each of the base relations.

This works in the case of Union because a row passing the filter is
independent of the other rows in its relation. It works in the case of
Intersect because if a row `a` was filtered from the intersection, it will
also be filtered from the LHS and RHS. It works in the case of Except because
if a row is filtered from the RHS, preventing its removal from the LHS, it
will also have been filtered from the LHS.

Visualization of the rule:
Let A and B be sets. Let the filter be represented by eliminating some set C.

Union (All):        (A Union B) \ C => (A \ C) Union (B \ C)
Intersection (All): (A Intersect B) \ C => (A \ C) Intersect (B \ C)
Except (All):       (A Except B) \ C => (A \ C) Except (B \ C)

We don't push a filter down if it references outer columns because doing so
prevents decorrelation.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushFilterIntoSetOp`, not the other rules in that file):

```
# PushFilterIntoSetOp pushes filters down to both the left and right sides
# of all set operators. For example, consider this query:
#
#   SELECT * FROM (SELECT x FROM a UNION ALL SELECT y FROM b) WHERE x < 5
#
# In this case, we can map x < 5 to both sides based on the knowledge that
# in the union the x out col corresponds to the x column in the (a) table and
# the y column in the (b) table. Therefore our mapping becomes x < 5 in the
# left side, and y <  5 in the right side. Given this mapping, we can now
# safely push the filter down to both sides as follows:
#
# SELECT * FROM (SELECT x FROM a UNION ALL SELECT y FROM b) WHERE x < 5
# =>
# (SELECT x FROM a WHERE x < 5) UNION ALL (SELECT y FROM b WHERE y < 5)
#
# Pushing (all) the filters down for each of the set operators (Union, Union
# All, Except, Except All, Intersect, Intersect All) is logically equivalent
# to filtering after applying the set operator. Here's some justification for
# this claim:
#
# Notice that each of the set operators this rule applies to, only works on
# union compatible relations. The resulting column set after applying the set
# operator, is also necessarily a subset of the column set of the (any)
# relations it was composed of. And so, any filter applied must have a
# corresponding column in each of the base relations.
#
# This works in the case of Union because a row passing the filter is
# independent of the other rows in its relation. It works in the case of
# Intersect because if a row `a` was filtered from the intersection, it will
# also be filtered from the LHS and RHS. It works in the case of Except because
# if a row is filtered from the RHS, preventing its removal from the LHS, it
# will also have been filtered from the LHS.
#
# Visualization of the rule:
# Let A and B be sets. Let the filter be represented by eliminating some set C.
#
# Union (All):        (A Union B) \ C => (A \ C) Union (B \ C)
# Intersection (All): (A Intersect B) \ C => (A \ C) Intersect (B \ C)
# Except (All):       (A Except B) \ C => (A \ C) Except (B \ C)
#
# We don't push a filter down if it references outer columns because doing so
# prevents decorrelation.
[PushFilterIntoSetOp, Normalize]
(Select
    $input:(Set $left:* $right:* $colmap:*)
    $filter:[
        ...
        $item:* &
            (CanMapOnSetOp $item) &
            (IsBoundBy $item $inputCols:(OutputCols $input))
        ...
    ]
)
=>
(Select
    ((OpName $input)
        (Select
            $left
            [ (FiltersItem (MapSetOpFilterLeft $item $colmap)) ]
        )
        (Select
            $right
            [ (FiltersItem (MapSetOpFilterRight $item $colmap)) ]
        )
        $colmap
    )
    (RemoveFiltersItem $filter $item)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-trivial instance of the rule: `before()` is `Filter(Intersect(L,R), P)` and `after()` is `Intersect(Filter(L,P), Filter(R,P))`, where the single uninterpreted predicate `P` is deliberately shared across both inputs and the set-op output — precisely the rule's "map the same filter through the column map to each side" step, with the identity column map on one shared column type. The SCOPE tag honestly discloses the narrowing to the 2-input distinct (set) INTERSECT variant, which is a genuine QED limitation for the ALL/bag variants (not modeled, and `intersect(all=true)` can't even be serialized — the JSONSerializer only handles `!all`), and nothing in the source rule's preconditions (filter bound to the set op's output cols, no outer refs) is violated or silently dropped by this shape. The result is not degenerate: `before()` and `after()` are structurally different, and the proof establishes that filtering commutes with set intersection for an arbitrary uninterpreted predicate — a real property (it would fail, e.g., if the right side were left unfiltered), with correct symbol sharing rather than coincidental over-constraint. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8410875
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 25163625
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 809500
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 484708
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21918917
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 25276333
  },
  "total_duration": {
    "secs": 0,
    "nanos": 62495584
  }
}
```
