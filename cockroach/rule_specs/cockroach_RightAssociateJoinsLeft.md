# Name: RightAssociateJoinsLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

RightAssociateJoinsLeft is a variant on LeftAssociateJoinsLeft.
The transformation:

SELECT * FROM ab
INNER JOIN (SELECT * FROM xy INNER JOIN uv ON True)
ON a=x AND b=u
=>
SELECT * FROM (SELECT * FROM ab INNER JOIN xy ON a=x)
INNER JOIN uv
ON b=u

Extracted from `join.opt` (which defines multiple rules — implement specifically `RightAssociateJoinsLeft`, not the other rules in that file):

```
# RightAssociateJoinsLeft is a variant on LeftAssociateJoinsLeft.
# The transformation:
#
#   SELECT * FROM ab
#   INNER JOIN (SELECT * FROM xy INNER JOIN uv ON True)
#   ON a=x AND b=u
# =>
#   SELECT * FROM (SELECT * FROM ab INNER JOIN xy ON a=x)
#   INNER JOIN uv
#   ON b=u
#
[RightAssociateJoinsLeft, Normalize, LowPriority]
(InnerJoin
    $outsideLeft:*
    (InnerJoin
        $insideLeft:*
        $insideRight:*
        []
        $insidePrivate:* & (NoJoinHints $insidePrivate)
    )
    $outsideOn:[
        ...
        $item:* &
            (IsBoundBy
                $item
                $cols:(OutputCols2 $insideLeft $outsideLeft)
            )
        ...
    ]
    $outsidePrivate:* & (NoJoinHints $outsidePrivate)
)
=>
(InnerJoin
    (InnerJoin
        $outsideLeft
        $insideLeft
        (ExtractBoundConditions $outsideOn $cols)
        (EmptyJoinPrivate)
    )
    $insideRight
    (ExtractUnboundConditions $outsideOn $cols)
    (EmptyJoinPrivate)
)
```
