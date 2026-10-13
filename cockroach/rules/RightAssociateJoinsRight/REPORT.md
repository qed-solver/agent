# RightAssociateJoinsRight

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 45  **Verification rounds used:** 3
**Scope detail:** public record RightAssociateJoinsRight() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

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
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding exactly mirrors the source variant: before = A ⋈_{p_ac∧p_abc} (B ⋈_true C), after = (A ⋈_{p_ac} C) ⋈_{p_abc} B, with INNER join kinds, a true literal for the empty inner ON list, and the same uninterpreted predicates applied to the same column values (I verified every joinField index and argument order: p_ac always sees (A,C), p_abc always sees (A,B,C)). The two-predicate split is fully general since any outer condition P(A,B,C) can be written as true ∧ P, the source rule's guards (NoJoinHints, existence of a bound conjunct) are applicability conditions rather than soundness preconditions, and the trailing projection only aligns the (A,C,B)→(A,B,C) output column order that the source rule changes implicitly via column IDs — so the proof is of the genuine, non-vacuous reassociation the rule performs.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8411083
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34242791
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 862583
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 596875
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21756417
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34358625
  },
  "total_duration": {
    "secs": 0,
    "nanos": 71880333
  }
}
```
