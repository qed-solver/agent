# PushSelectIntoJoinLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 42  **Verification rounds used:** 3
**Scope detail:** LEFT join only (not the also-eligible INNER/SEMI/ANTI or the Apply variants); one left-bound and one unbound filter conjunct; one ON condition; single-column inputs


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoJoinLeft pushes Select filter conditions into the left side of
an input Join. This is possible in the case of InnerJoin, LeftJoin, SemiJoin,
and AntiJoin, as long as the condition has no dependencies on the right side
of the join. Right and Full joins are not eligible, since attempting to filter
left rows would just result in NULL left rows instead.

-- No row is returned for a.x=1, a.y=2, b.x=1, since the WHERE excludes it.
SELECT * FROM a RIGHT JOIN b ON a.x=b.x WHERE a.y < 0

-- But if the filter is incorrectly pushed down in RIGHT/FULL JOIN case,
-- then a row containing null values on the left side is returned.
SELECT * FROM (SELECT * FROM a WHERE a.y < 0) a RIGHT JOIN b ON a.x=b.x

Citations: [1]

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoJoinLeft`, not the other rules in that file):

```
# PushSelectIntoJoinLeft pushes Select filter conditions into the left side of
# an input Join. This is possible in the case of InnerJoin, LeftJoin, SemiJoin,
# and AntiJoin, as long as the condition has no dependencies on the right side
# of the join. Right and Full joins are not eligible, since attempting to filter
# left rows would just result in NULL left rows instead.
#
#   -- No row is returned for a.x=1, a.y=2, b.x=1, since the WHERE excludes it.
#   SELECT * FROM a RIGHT JOIN b ON a.x=b.x WHERE a.y < 0
#
#   -- But if the filter is incorrectly pushed down in RIGHT/FULL JOIN case,
#   -- then a row containing null values on the left side is returned.
#   SELECT * FROM (SELECT * FROM a WHERE a.y < 0) a RIGHT JOIN b ON a.x=b.x
#
# Citations: [1]
[PushSelectIntoJoinLeft, Normalize]
(Select
    $input:(LeftJoin | LeftJoinApply | SemiJoin | SemiJoinApply
            | AntiJoin | AntiJoinApply
        $left:*
        $right:*
        $on:*
        $private:*
    )
    $filters:[
        ...
        $item:* & (IsBoundBy $item $leftCols:(OutputCols $left))
        ...
    ]
)
=>
(Select
    ((OpName $input)
        (Select
            $left
            (ExtractBoundConditions $filters $leftCols)
        )
        $right
        $on
        $private
    )
    (ExtractUnboundConditions $filters $leftCols)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding correctly captures the core LEFT-JOIN push-down transformation: before() is Select(LeftJoin(L,R,g), f(l)∧u(l,r)) and after() is Select(LeftJoin(Select(L,f),R,g), u(l,r)), which are structurally distinct and the equivalence is a genuine (non-vacuous) bag-semantics proof. All predicates (f, u, g) are uninterpreted symbols, f is correctly modeled as bound-by-left-only (single argument = L's column in both the join-row and row-scoped contexts), and u correctly references both sides so it stays above. The SCOPE line honestly and specifically discloses the narrowings (LEFT only vs. the original's Left/Semi/Anti + Apply variants; exactly one bound + one unbound conjunct; one ON condition; single-column inputs), and none of these restrictions introduce a semantic error — they merely reduce the family of instantiations QED must verify while preserving the rule's essential logic. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 13940499
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 43239208
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 844542
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 869583
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 32445792
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 43522958
  },
  "total_duration": {
    "secs": 0,
    "nanos": 91881166
  }
}
```
