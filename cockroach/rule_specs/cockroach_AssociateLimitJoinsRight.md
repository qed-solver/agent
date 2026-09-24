# Name: AssociateLimitJoinsRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/limit.opt

AssociateLimitJoinsRight mirrors AssociateLimitJoinsLeft (it matches when the
LeftJoin is the right input of the InnerJoin, as opposed to the left input).
Here's the transformation:

SELECT *
FROM ab
INNER JOIN (SELECT * FROM xy LEFT JOIN uv ON u = x)
ON a = y
LIMIT 10
=>
SELECT *
FROM (SELECT * FROM xy INNER JOIN ab ON a = y)
LEFT JOIN uv
ON u = x
LIMIT 10

Extracted from `limit.opt` (which defines multiple rules — implement specifically `AssociateLimitJoinsRight`, not the other rules in that file):

```
# AssociateLimitJoinsRight mirrors AssociateLimitJoinsLeft (it matches when the
# LeftJoin is the right input of the InnerJoin, as opposed to the left input).
# Here's the transformation:
#
#   SELECT *
#   FROM ab
#   INNER JOIN (SELECT * FROM xy LEFT JOIN uv ON u = x)
#   ON a = y
#   LIMIT 10
# =>
#   SELECT *
#   FROM (SELECT * FROM xy INNER JOIN ab ON a = y)
#   LEFT JOIN uv
#   ON u = x
#   LIMIT 10
#
[AssociateLimitJoinsRight, Normalize, LowPriority]
(Limit
    $limitInput:(InnerJoin
            $outsideLeft:*
            $outsideRight:(LeftJoin
                $insideLeft:*
                $insideRight:*
                $insideOn:*
                $insidePrivate:* & (NoJoinHints $insidePrivate)
            )
            $outsideOn:* &
                ^(ColsIntersect
                    (FilterOuterCols $outsideOn)
                    (OutputCols $insideRight)
                )
            $outsidePrivate:* & (NoJoinHints $outsidePrivate)
        ) &
        ^(JoinPreservesRightRows $limitInput)
    $limitValue:*
    $limitOrdering:*
)
=>
(Limit
    (LeftJoin
        (InnerJoin
            $insideLeft
            $outsideLeft
            $outsideOn
            (EmptyJoinPrivate)
        )
        $insideRight
        $insideOn
        (EmptyJoinPrivate)
    )
    $limitValue
    $limitOrdering
)
```
