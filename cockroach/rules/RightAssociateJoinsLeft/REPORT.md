# RightAssociateJoinsLeft

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** public record RightAssociateJoinsLeft() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

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
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully models the source rule: before is `A ⋈(B ⋈_true C)` with condition `p_ab(A,B) ∧ p_abc(A,B,C)`, and after is `(A ⋈_p_ab B) ⋈_p_abc C` — exactly the reassociation with condition-splitting the Optgen rule prescribes (inner ON is empty/true, bound-to-(A,B) conjuncts move into the new inner join, the rest stay outer). Both predicates are uninterpreted symbols shared correctly across before/after, all join kinds are INNER as required, and the only omitted source constraints (NoJoinHints) are optimizer-firing guards with no bag-semantic effect, so the SCOPE: FULL tag is accurate. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8207208
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33071042
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 870333
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 578667
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21703292
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33186292
  },
  "total_duration": {
    "secs": 0,
    "nanos": 70670209
  }
}
```
