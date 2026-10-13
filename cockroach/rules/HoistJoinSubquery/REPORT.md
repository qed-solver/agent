# HoistJoinSubquery

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 65  **Verification rounds used:** 4
**Scope detail:** INNER join only; the hoisted subquery is an uncorrelated


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistJoinSubquery extracts subqueries from a join filter and joins them with
the join's right input. This and other subquery hoisting patterns create a
single, top-level relational query with no nesting. This rule only applies to
join types which have a legal apply variant.

This rule is marked as low priority for the same reason as HoistSelectExists.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistJoinSubquery`, not the other rules in that file):

```
# HoistJoinSubquery extracts subqueries from a join filter and joins them with
# the join's right input. This and other subquery hoisting patterns create a
# single, top-level relational query with no nesting. This rule only applies to
# join types which have a legal apply variant.
#
# This rule is marked as low priority for the same reason as HoistSelectExists.
[HoistJoinSubquery, Normalize, LowPriority]
(InnerJoin | LeftJoin | SemiJoin | AntiJoin
    $left:*
    $right:*
    $on:[ ... $item:* & (HasHoistableSubquery $item) ... ]
    $private:*
)
=>
(HoistJoinSubquery (OpName) $left $right $on $private)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core transformation of HoistJoinSubquery for the INNER-join case: moving an uncorrelated EXISTS subquery from the join's ON condition into a pre-filter on the right input, which is semantically equivalent under bag semantics. The before() and after() are structurally distinct (the EXISTS conjunct relocates from the join condition to a Filter node on R), the join kind (INNER) is one of the four the source rule targets and is the one where the transformation is unconditionally valid without additional preconditions, all symbols (L, R, S, on_cond, sub_pred) are correctly uninterpreted and independently shared, and the SCOPE tag honestly and specifically documents both the INNER-only restriction and the uncorrelated-EXISTS restriction. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9826541
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 37105958
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 868791
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 641208
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 24383084
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 37222875
  },
  "total_duration": {
    "secs": 0,
    "nanos": 77232292
  }
}
```
