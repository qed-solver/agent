# RemoveJoinNotNullCondition

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** LEFT join only


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

RemoveJoinNotNullCondition removes a filter with an IS NOT NULL condition when
the given column has a NOT NULL constraint. Only left joins and full joins are
matched because filters can be pushed down from the ON conditions of inner and
semi joins.

Extracted from `join.opt` (which defines multiple rules — implement specifically `RemoveJoinNotNullCondition`, not the other rules in that file):

```
# RemoveJoinNotNullCondition removes a filter with an IS NOT NULL condition when
# the given column has a NOT NULL constraint. Only left joins and full joins are
# matched because filters can be pushed down from the ON conditions of inner and
# semi joins.
[RemoveJoinNotNullCondition, Normalize]
(LeftJoin | FullJoin
    $left:*
    $right:*
    $on:[
        ...
        $item:(FiltersItem
            (IsNot
                (Variable
                    $col:* & (IsColNotNull2 $col $left $right)
                )
                (Null)
            )
        )
        ...
    ]
    $private:*
)
=>
((OpName) $left $right (RemoveFiltersItem $on $item) $private)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully models the rule's core move — dropping a tautological `IS NOT NULL` conjunct from an outer join's ON condition — with the NOT NULL premise correctly carried by the left column's non-nullable `L_Type`; that premise is genuinely load-bearing, since with a nullable column the `IS_NOT_NULL` conjunct would not be removable from a LEFT JOIN (null-key rows would get NULL-extended vs. matched), so the proof is non-vacuous and `before()` ≠ `after()`. It is an honestly-tagged PARTIAL special case (LEFT join only, NOT NULL column on the left, remaining filters abstracted as one uninterpreted `rest` predicate), which is a real, non-degenerate subset of the source `LeftJoin | FullJoin` rule rather than a structurally trivial or over-constrained one.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 0
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 347208
  }
}
```
