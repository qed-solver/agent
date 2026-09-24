# Name: RightAssociateJoinsRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

RightAssociateJoinsRight is a variant on LeftAssociateJoinsLeft.
The transformation:

SELECT * FROM ab
INNER JOIN (SELECT * FROM xy INNER JOIN uv ON True)
ON a=x AND b=u
=>
SELECT * FROM (SELECT * FROM ab INNER JOIN uv ON b=u)
INNER JOIN xy
ON a=x

Extracted from `join.opt` (which defines multiple rules — implement specifically `RightAssociateJoinsRight`, not the other rules in that file):

```
# RightAssociateJoinsRight is a variant on LeftAssociateJoinsLeft.
# The transformation:
#
#   SELECT * FROM ab
#   INNER JOIN (SELECT * FROM xy INNER JOIN uv ON True)
#   ON a=x AND b=u
# =>
#   SELECT * FROM (SELECT * FROM ab INNER JOIN uv ON b=u)
#   INNER JOIN xy
#   ON a=x
#
[RightAssociateJoinsRight, Normalize, LowPriority]
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
                $cols:(OutputCols2 $insideRight $outsideLeft)
            )
        ...
    ]
    $outsidePrivate:* & (NoJoinHints $outsidePrivate)
)
=>
(InnerJoin
    (InnerJoin
        $outsideLeft
        $insideRight
        (ExtractBoundConditions $outsideOn $cols)
        (EmptyJoinPrivate)
    )
    $insideLeft
    (ExtractUnboundConditions $outsideOn $cols)
    (EmptyJoinPrivate)
)
```
