# DetectJoinContradiction

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 30  **Verification rounds used:** 2
**Scope detail:** INNER join only


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

DetectJoinContradiction replaces a Join condition with False if it detects a
contradiction in the filter.

Extracted from `join.opt` (which defines multiple rules — implement specifically `DetectJoinContradiction`, not the other rules in that file):

```
# DetectJoinContradiction replaces a Join condition with False if it detects a
# contradiction in the filter.
[DetectJoinContradiction, Normalize]
(Join
    $left:*
    $right:*
    $on:[
            ...
            $item:(FiltersItem) & (IsContradiction $item)
            ...
        ] &
        ^(IsFilterFalse $on)
    $private:*
)
=>
((OpName) $left $right [ (FiltersItem (False)) ] $private)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors the source transformation — a join whose ON list contains a contradictory (always-false) item alongside uninterpreted remaining items is rewritten to a join with ON = literal False — with `rest` a genuinely uninterpreted predicate over the (L, R) join row and L/R fully general scans, so the proof is non-vacuous (before and after are structurally different plans, both being the empty inner join) and holds universally over all relations and all remaining predicates, exactly as the rule requires; the only narrowing is that the join kind is fixed to INNER while the source's generic `(Join ...)` pattern spans all join kinds (for which the equivalence also holds, since both ON conditions match no pair), and this is honestly and specifically tagged as PARTIAL, is inherent to the one-concrete-join-kind-per-record contract (other kinds are separate family instances rather than a single checkable encoding), and does not make the result degenerate.

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
    "nanos": 303167
  }
}
```
