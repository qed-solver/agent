# ProjectInnerJoinValues

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** modeled with a representative 1-column left input, a single-row 2-column Values (1, 2), and an uninterpreted on-predicate over all join columns; the correlated InnerJoinApply variant is not modeled.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

ProjectInnerJoinValues transforms an inner join with a single-row Values
operator to a Project operator. This allows decorrelation of e.g.:

SELECT (SELECT CASE WHEN ord.approved THEN 'Approved' ELSE '---' END)
FROM (VALUES (1, true), (2, false)) ord(id, approved)

Extracted from `join.opt` (which defines multiple rules — implement specifically `ProjectInnerJoinValues`, not the other rules in that file):

```
# ProjectInnerJoinValues transforms an inner join with a single-row Values
# operator to a Project operator. This allows decorrelation of e.g.:
#
#   SELECT (SELECT CASE WHEN ord.approved THEN 'Approved' ELSE '---' END)
#   FROM (VALUES (1, true), (2, false)) ord(id, approved)
#
[ProjectInnerJoinValues, Normalize]
(InnerJoin | InnerJoinApply
    $left:*
    $right:(Values) & (HasOneRow $right)
    $on:*
)
=>
(Select
    (Project
        $left
        (MakeProjectionsFromValues $right)
        (OutputCols $left)
    )
    $on
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (an INNER join with a one-row literal Values) and after() (an identity+constants Project followed by a Filter) are structurally distinct, and QED's proof universally quantifies over both the left table and the uninterpreted "on" predicate — including constant-true/false instantiations covering empty/true on-clauses — so the bag-equivalence is substantive, not vacuous. The modeling choices are faithful: the same "on" symbol is shared across the join condition and the final filter exactly as the source rule reuses $on, the projected constants (1,2) match the Values row contents identically on both sides as MakeProjectionsFromValues requires, the join kind is INNER, and the sole source precondition (HasOneRow on a Values) is modeled precisely. The disclosed PARTIAL narrowing (1-column left input, fixed 2-column row (1,2), correlated InnerJoinApply variant omitted) is honest and specific, and representative rather than degenerate, since the rule's correctness is independent of left/values arity and all genuinely free logical parameters remain fully uninterpreted. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5687834
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33387750
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 944500
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 356667
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16804917
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33427458
  },
  "total_duration": {
    "secs": 0,
    "nanos": 65470791
  }
}
```
