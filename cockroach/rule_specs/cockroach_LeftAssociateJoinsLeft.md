# Name: LeftAssociateJoinsLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

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
