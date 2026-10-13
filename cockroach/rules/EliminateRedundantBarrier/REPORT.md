# EliminateRedundantBarrier

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** the barrier's input is fixed to a single column; the original rule applies to any arity


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/barrier.opt

EliminateRedundantBarrier removes a Barrier operator when it wraps another
identical Barrier. This deduplication avoids unnecessary nesting of
equivalent Barriers. The rule applies only when both Barrier operators have
the same configuration.

Extracted from `barrier.opt` (which defines multiple rules — implement specifically `EliminateRedundantBarrier`, not the other rules in that file):

```
# EliminateRedundantBarrier removes a Barrier operator when it wraps another
# identical Barrier. This deduplication avoids unnecessary nesting of
# equivalent Barriers. The rule applies only when both Barrier operators have
# the same configuration.
[EliminateRedundantBarrier, Normalize]
(Barrier
    (Barrier $input:* $innerLeakproofPermeable:*)
    $outerLeakproofPermeable:* &
        (EqualsBool
            $innerLeakproofPermeable
            $outerLeakproofPermeable
        )
)
=>
(Barrier $input $outerLeakproofPermeable)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully models a Barrier as an identity projection (project(field(0)) over a single-column scan), which correctly captures the Barrier's logical pass-through semantics; the shape before()=Project∘Project∘Scan vs after()=Project∘Scan matches the source rule's Barrier(Barrier(input))→Barrier(input) and is structurally non-trivial (two projection nodes vs one), and the equal-config precondition is irrelevant to bag semantics since a Barrier's leakproofPermeable parameter is a flow-control knob with no effect on row values, so ignoring it is sound. The single-column restriction is a genuine but mild under-generalization (the identity∘identity=identity property is arity-independent and the DSL requires a fixed scan arity), and it is honestly tagged SCOPE: PARTIAL, leaving a useful non-degenerate proof. ```

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
    "nanos": 353833
  }
}
```
