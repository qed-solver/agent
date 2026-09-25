# SimplifyLeftJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** self-join in which both inputs are the same single non-nullable-column scan joined on equality of that column


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

SimplifyLeftJoin reduces a LeftJoin operator to an InnerJoin operator (or a
FullJoin to a RightJoin) when it's known that every row in the join's left
input will match at least one row in the right input. Since every row matches,
NULL-extended rows will never be added by the outer join, and therefore can be
mapped to an InnerJoin (or RightJoin in case of FullJoin). See
filtersMatchAllLeftRows comment for conditions in which this rule can match.

Self-join example:
SELECT * FROM xy LEFT JOIN xy AS xy2 ON xy.y = xy2.y
=>
SELECT * FROM xy INNER JOIN xy AS xy2 ON xy.y = xy2.y

Foreign-key example:
SELECT * FROM orders o LEFT JOIN customers c ON o.customer_id = c.id
=>
SELECT * FROM orders o INNER JOIN customers c ON o.customer_id = c.id

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyLeftJoin`, not the other rules in that file):

```
# SimplifyLeftJoin reduces a LeftJoin operator to an InnerJoin operator (or a
# FullJoin to a RightJoin) when it's known that every row in the join's left
# input will match at least one row in the right input. Since every row matches,
# NULL-extended rows will never be added by the outer join, and therefore can be
# mapped to an InnerJoin (or RightJoin in case of FullJoin). See
# filtersMatchAllLeftRows comment for conditions in which this rule can match.
#
# Self-join example:
#   SELECT * FROM xy LEFT JOIN xy AS xy2 ON xy.y = xy2.y
#   =>
#   SELECT * FROM xy INNER JOIN xy AS xy2 ON xy.y = xy2.y
#
# Foreign-key example:
#   SELECT * FROM orders o LEFT JOIN customers c ON o.customer_id = c.id
#   =>
#   SELECT * FROM orders o INNER JOIN customers c ON o.customer_id = c.id
[SimplifyLeftJoin, Normalize]
(LeftJoin | LeftJoinApply | FullJoin
    $left:*
    $right:*
    $on:* & (JoinFiltersMatchAllLeftRows $left $right $on)
    $private:*
)
=>
(ConstructNonLeftJoin (OpName) $left $right $on $private)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and correctly shaped: `before()` and `after()` genuinely differ only in join kind (LEFT vs INNER), and the equivalence is a real theorem that holds *only* because both inputs are the same scan and the condition is `col = col` on a non-nullable column — this is precisely the structural encoding of the rule's `JoinFiltersMatchAllLeftRows` precondition (the self-join example from the rule's own doc comment), and each piece is load-bearing: two independent scans would fail (right side could be empty or lack a matching value) and a nullable column would fail (NULL = NULL is not true, so a NULL left row would get null-extended), so QED could not have proved a looser, wrong claim. The narrowing to plain `LeftJoin` with a single non-nullable-column self-join on equality is specific, non-degenerate, and accurately disclosed in the SCOPE line, so the provable result faithfully certifies a genuine special case of the source rule rather than a trivial or accidentally over-constrained one.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 13721668
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
    "nanos": 1123083
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1169792
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 31464458
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 51125125
  }
}
```
