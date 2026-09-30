# PushLeakproofJoinIntoPermeableBarrierLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 26  **Verification rounds used:** 3
**Scope detail:** both join inputs fixed to two columns and the join fixed to a plain inner join (the source rule applies to any arity and also matches the apply-join variant)


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

PushLeakproofJoinIntoPermeableBarrierLeft moves a join below a permeable
Barrier on its left input when all ON filters are leakproof. The Barrier is
then placed above the join. This is safe because leakproof filters can be
reordered freely, and the Barrier allows such movement when marked as
LeakproofPermeable. This rule is effectively pushing the left input into the
Barrier, so the left input must be leakproof as well.

Extracted from `join.opt` (which defines multiple rules — implement specifically `PushLeakproofJoinIntoPermeableBarrierLeft`, not the other rules in that file):

```
# PushLeakproofJoinIntoPermeableBarrierLeft moves a join below a permeable
# Barrier on its left input when all ON filters are leakproof. The Barrier is
# then placed above the join. This is safe because leakproof filters can be
# reordered freely, and the Barrier allows such movement when marked as
# LeakproofPermeable. This rule is effectively pushing the left input into the
# Barrier, so the left input must be leakproof as well.
[PushLeakproofJoinIntoPermeableBarrierLeft, Normalize]
(InnerJoin | InnerJoinApply
    (Barrier
        $left:*
        $leakproofPermeable:* & (If $leakproofPermeable)
    )
    $right:* & (IsLeakproof $right)
    $on:* & (HasAllLeakProofFilters $on)
    $private:*
)
=>
(Barrier
    ((OpName) $left $right $on $private)
    $leakproofPermeable
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful: it models the permeable Barrier as an identity projection (correct, since a Barrier is a pure flow-control point with no row-bag effect), shares the uninterpreted ON predicate "C" and both input scans/types between before/after so the two sides are the same symbols, and produces genuinely different trees (projection below the join vs. above it) rather than a vacuous before()==after(). The rule's real preconditions (LeakproofPermeable via If, IsLeakproof on the right, HasAllLeakProofFilters on the ON list) gate evaluation-order/partiality behavior, which has no bag-semantic counterpart in QED, so the only bag-semantic content is that an identity barrier commutes with the inner join—exactly what is proved, and the porter documents this correctly. The two under-generalizations (inputs fixed to two columns, and plain INNER join instead of also matching InnerJoinApply) are genuine, specific, and honestly disclosed in the SCOPE: PARTIAL line, and neither degenerates the result. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
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
    "nanos": 337167
  }
}
```
