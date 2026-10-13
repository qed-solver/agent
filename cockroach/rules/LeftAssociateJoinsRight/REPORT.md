# LeftAssociateJoinsRight

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 3
**Scope detail:** public record LeftAssociateJoinsRight() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

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
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the source rule's logical transformation: (A⋈B ON true)⋈C ON (p_ax∧p_bu) reassociates to A⋈(B⋈C ON p_bu) ON p_ax, where p_bu (bound by B,C) is pushed into the new inner join and p_ax (referencing all three) remains at the outer level. The two uninterpreted predicates correctly model the source rule's partition of $outsideOn into ExtractBoundConditions and ExtractUnboundConditions, the join kinds (INNER) and the empty inner condition (true) match the source, and the column contexts in joinField ordinals are consistent across both sides. The only omissions are backend-specific join-hint metadata (NoJoinHints/EmptyJoinPrivate) which have no bearing on logical equivalence and are inexpressible in the DSL, so SCOPE: FULL is appropriate. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8103918
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 32816750
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 861917
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 541584
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21079542
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 32927375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 69396084
  }
}
```
