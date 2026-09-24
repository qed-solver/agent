# Name: LeftAssociateJoinsRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

LeftAssociateJoinsRight is a variant on LeftAssociateJoinsLeft.
The transformation:

SELECT * FROM (SELECT * FROM xy INNER JOIN uv ON True)
INNER JOIN ab
ON a=x AND b=u
=>
SELECT * FROM xy
INNER JOIN (SELECT * FROM uv INNER JOIN ab ON b=u)
ON a=x

Extracted from `join.opt` (which defines multiple rules — implement specifically `LeftAssociateJoinsRight`, not the other rules in that file):

```
# LeftAssociateJoinsRight is a variant on LeftAssociateJoinsLeft.
# The transformation:
#
#   SELECT * FROM (SELECT * FROM xy INNER JOIN uv ON True)
#   INNER JOIN ab
#   ON a=x AND b=u
# =>
#   SELECT * FROM xy
#   INNER JOIN (SELECT * FROM uv INNER JOIN ab ON b=u)
#   ON a=x
#
[LeftAssociateJoinsRight, Normalize, LowPriority]
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
                $cols:(OutputCols2 $insideRight $outsideRight)
            )
        ...
    ]
    $outsidePrivate:* & (NoJoinHints $outsidePrivate)
)
=>
(InnerJoin
    $insideLeft
    (InnerJoin
        $insideRight
        $outsideRight
        (ExtractBoundConditions $outsideOn $cols)
        (EmptyJoinPrivate)
    )
    (ExtractUnboundConditions $outsideOn $cols)
    (EmptyJoinPrivate)
)
```
