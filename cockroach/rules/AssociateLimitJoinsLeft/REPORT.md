# AssociateLimitJoinsLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 28  **Verification rounds used:** 2
**Scope detail:** the identical Limit/ordering wrapper is omitted and join conditions are uninterpreted predicates over fixed single-column fields


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

AssociateLimitJoinsLeft reorders an InnerJoin and LeftJoin under a Limit if:
1. The InnerJoin does not preserve rows from its left input.
2. The InnerJoin's ON condition does not reference the right side of the
LeftJoin (because the reordering would be invalid).
3. Neither join has join hints.

Why condition #1? If the InnerJoin preserves left rows, the limit can already
be pushed down into the LeftJoin, so there's no need to reorder the joins.

Here's the transformation:

SELECT *
FROM (SELECT * FROM xy LEFT JOIN uv ON u = x)
INNER JOIN ab
ON a = y
LIMIT 10
=>
SELECT *
FROM (SELECT * FROM xy INNER JOIN ab ON a = y)
LEFT JOIN uv
ON u = x
LIMIT 10

Citations: [1] (See identity (6) in section 2.2)

Extracted from `limit.opt` (which defines multiple rules — implement specifically `AssociateLimitJoinsLeft`, not the other rules in that file):

```
# AssociateLimitJoinsLeft reorders an InnerJoin and LeftJoin under a Limit if:
# 1. The InnerJoin does not preserve rows from its left input.
# 2. The InnerJoin's ON condition does not reference the right side of the
#    LeftJoin (because the reordering would be invalid).
# 3. Neither join has join hints.
#
# Why condition #1? If the InnerJoin preserves left rows, the limit can already
# be pushed down into the LeftJoin, so there's no need to reorder the joins.
#
# Here's the transformation:
#
#   SELECT *
#   FROM (SELECT * FROM xy LEFT JOIN uv ON u = x)
#   INNER JOIN ab
#   ON a = y
#   LIMIT 10
# =>
#   SELECT *
#   FROM (SELECT * FROM xy INNER JOIN ab ON a = y)
#   LEFT JOIN uv
#   ON u = x
#   LIMIT 10
#
# Citations: [1] (See identity (6) in section 2.2)
[AssociateLimitJoinsLeft, Normalize, LowPriority]
(Limit
    $limitInput:(InnerJoin
            $outsideLeft:(LeftJoin
                $insideLeft:*
                $insideRight:*
                $insideOn:*
                $insidePrivate:* & (NoJoinHints $insidePrivate)
            )
            $outsideRight:*
            $outsideOn:* &
                ^(ColsIntersect
                    (FilterOuterCols $outsideOn)
                    (OutputCols $insideRight)
                )
            $outsidePrivate:* & (NoJoinHints $outsidePrivate)
        ) &
        ^(JoinPreservesLeftRows $limitInput)
    $limitValue:*
    $limitOrdering:*
)
=>
(Limit
    (LeftJoin
        (InnerJoin
            $insideLeft
            $outsideRight
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

The encoding faithfully captures the rule's substantive identity — ((A LEFT JOIN B ON p_ab) INNER JOIN C ON p_ac) ≡ reprojected((A INNER JOIN C ON p_ac) LEFT JOIN B ON p_ab) — with exactly the right join kinds, shared uninterpreted predicates used with consistent argument correspondence (p_ab over A+B, p_ac over A+C), and the one soundness-critical precondition (the outside ON condition must not reference the inner left join's right input) structurally enforced by making p_ac a predicate over A and C columns only. The two sides are genuinely different join trees so the proof is non-vacuous, and the only restrictions — single-column scans (the identity is row-wise and column-arity independent), omission of the Limit/ordering wrapper that QED cannot model anyway (both sides share the identical wrapper, so the join equivalence is the full meaningful claim), and omission of the non-soundness priority guards `^(JoinPreservesLeftRows)` and `NoJoinHints` — are honestly stated in the PARTIAL scope line.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 16947794
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 41071791
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1290584
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1160916
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 36855750
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 41278625
  },
  "total_duration": {
    "secs": 0,
    "nanos": 96351500
  }
}
```
