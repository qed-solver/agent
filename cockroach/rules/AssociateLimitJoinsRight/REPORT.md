# AssociateLimitJoinsRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** the identical Limit/ordering wrapper is omitted and join conditions are uninterpreted predicates over fixed single-column fields


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

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
```

## Independent verifier review

**Verdict:** CONFIRMED

before() = C ⋈ (A ⟕ B ON p_ab) and after() = (A ⋈ C ON p_ac) ⟕ B ON p_ab (re-projected to the common (C,A,B) column order) are genuinely different plan trees, so the proof is of the exact reassociation identity that is the semantic core of the rule, with join types matching the source (inner outside / left inside, flipped to inner inside / left outside). The two ON conditions are shared uninterpreted predicate symbols applied to the same logical columns in the same argument order on both sides (p_ab on (A,B), p_ac on (A,C)), which structurally enforces the rule's essential side condition that $outsideOn not reference $insideRight rather than coincidentally satisfying it, and A/B/C remain three independent uninterpreted tables. The declared PARTIAL scope is honest and non-degenerate: the Limit/ordering wrapper is identical on both sides and unmodelable by QED, the fixed single-column width is an inherent DSL limitation (no arity quantification) with no bearing on the identity's logical content, and the proof still fully exercises the null-extended-row path that is what makes the identity non-trivial. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 20113210
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 47590208
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1066166
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1325875
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 44993417
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 47862791
  },
  "total_duration": {
    "secs": 0,
    "nanos": 109688708
  }
}
```
