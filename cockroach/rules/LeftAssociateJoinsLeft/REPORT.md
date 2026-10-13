# LeftAssociateJoinsLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** the outer ON is a fixed representative split into two uninterpreted conjuncts (one bound to insideLeft+outsideRight, one unbound referencing insideRight+outsideRight) over single-column scans, not an arbitrary ExtractBound/Unbound decomposition


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

LeftAssociateJoinsLeft reorders InnerJoins so that join filters can be brought
closer to the relations they reference. This in done in hopes of allowing
other rules (for example, limit push-down) to fire. It also has the effect of
pushing cross joins up the operator tree. LeftAssociateJoinsLeft matches when
the following conditions are true:
1. The inside InnerJoin is the left input of the outside InnerJoin.
2. The outside InnerJoin's ON condition has an equality between the right input
of the outside InnerJoin and the left input of the inside InnerJoin.
3. The inside InnerJoin's ON condition is empty.

The transformation:

SELECT * FROM (SELECT * FROM xy INNER JOIN uv ON True)
INNER JOIN ab
ON a=x AND b=u
=>
SELECT * FROM uv
INNER JOIN (SELECT * FROM xy INNER JOIN ab ON a=x)
ON b=u

In this example, neither of the filters in the original query could be pushed
down because they both reference ab. With the joins reordered, the a=x filter
can be pushed down closer to xy.

There are three variants of LeftAssociateJoinsLeft below this rule definition.

In the worst case scenario, LeftAssociateJoinsLeft and its variants will
be fired (n^2)/4 times, where n is the number of joins in the join tree.

LeftAssociateJoinsLeft and its variants are LowPriority so that other rules
(such as filter push-down) have a chance to fire first.

Extracted from `join.opt` (which defines multiple rules — implement specifically `LeftAssociateJoinsLeft`, not the other rules in that file):

```
# LeftAssociateJoinsLeft reorders InnerJoins so that join filters can be brought
# closer to the relations they reference. This in done in hopes of allowing
# other rules (for example, limit push-down) to fire. It also has the effect of
# pushing cross joins up the operator tree. LeftAssociateJoinsLeft matches when
# the following conditions are true:
# 1. The inside InnerJoin is the left input of the outside InnerJoin.
# 2. The outside InnerJoin's ON condition has an equality between the right input
#    of the outside InnerJoin and the left input of the inside InnerJoin.
# 3. The inside InnerJoin's ON condition is empty.
#
# The transformation:
#
#   SELECT * FROM (SELECT * FROM xy INNER JOIN uv ON True)
#   INNER JOIN ab
#   ON a=x AND b=u
# =>
#   SELECT * FROM uv
#   INNER JOIN (SELECT * FROM xy INNER JOIN ab ON a=x)
#   ON b=u
#
# In this example, neither of the filters in the original query could be pushed
# down because they both reference ab. With the joins reordered, the a=x filter
# can be pushed down closer to xy.
#
# There are three variants of LeftAssociateJoinsLeft below this rule definition.
#
# In the worst case scenario, LeftAssociateJoinsLeft and its variants will
# be fired (n^2)/4 times, where n is the number of joins in the join tree.
#
# LeftAssociateJoinsLeft and its variants are LowPriority so that other rules
# (such as filter push-down) have a chance to fire first.
[LeftAssociateJoinsLeft, Normalize, LowPriority]
(InnerJoin
    (InnerJoin
        $insideLeft:*
        $insideRight:*
        []
        $insidePrivate:* & (NoJoinHints $insidePrivate)
    )
    $outsideRight:*
    $outsideOn:[
        ...
        $item:* &
            (IsBoundBy
                $item
                $cols:(OutputCols2 $insideLeft $outsideRight)
            )
        ...
    ]
    $outsidePrivate:* & (NoJoinHints $outsidePrivate)
)
=>
(InnerJoin
    $insideRight
    (InnerJoin
        $insideLeft
        $outsideRight
        (ExtractBoundConditions $outsideOn $cols)
        (EmptyJoinPrivate)
    )
    (ExtractUnboundConditions $outsideOn $cols)
    (EmptyJoinPrivate)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core algebraic identity of LeftAssociateJoinsLeft — reassociating ((A×B) ⋈_{P(A,C)∧Q(B,C)} C) into B ⋈_{Q(B,C)} (A ⋈_{P(A,C)} C) up to column reordering — with correct inner-join kinds, a true-literal cross join for the empty inner ON, properly shared uninterpreted predicates (bound over (A,C), unbound over (B,C)) whose argument positions are consistently mapped across both sides, and a projection to equalize output column order. The SCOPE line honestly flags the main narrowing (fixed two-conjunct ON with unbound restricted to (B,C) rather than the fully general ExtractBound/Unbound split which could also reference A in the unbound conjunct), and the proof is non-vacuous since before and after are structurally distinct join trees whose equivalence is a genuine universal algebraic identity over arbitrary instantiations of the uninterpreted symbols. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8500625
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 32963791
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 889583
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 619542
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22142583
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33065541
  },
  "total_duration": {
    "secs": 0,
    "nanos": 70874625
  }
}
```
