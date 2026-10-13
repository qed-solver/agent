# EliminateExistsGroupBy

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 43  **Verification rounds used:** 3
**Scope detail:** uncorrelated EXISTS only (modeled as a left-semi join on a constant-true condition), instantiated with a single uninterpreted group key and no aggregate calls


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateExistsGroupBy discards a non-scalar GroupBy input to the Exists
operator. While non-scalar GroupBy (or DistinctOn) can change row cardinality,
it always returns a non-empty set if its input is non-empty. Similarly, if its
input is empty, then it returns the empty set. Therefore, it's a no-op for
Exists.

NOTE: EnsureDistinctOn has the side effect of error'ing if the input has
duplicates, so do not eliminate it.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateExistsGroupBy`, not the other rules in that file):

```
# EliminateExistsGroupBy discards a non-scalar GroupBy input to the Exists
# operator. While non-scalar GroupBy (or DistinctOn) can change row cardinality,
# it always returns a non-empty set if its input is non-empty. Similarly, if its
# input is empty, then it returns the empty set. Therefore, it's a no-op for
# Exists.
#
# NOTE: EnsureDistinctOn has the side effect of error'ing if the input has
# duplicates, so do not eliminate it.
[EliminateExistsGroupBy, Normalize]
(Exists (GroupBy | DistinctOn $input:*) $existsPrivate:*)
=>
(Exists $input $existsPrivate)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful and non-vacuous: before() places a non-scalar Aggregate (one uninterpreted group key, no aggregate calls) under a constant-true SEMI join — the exact plan-level semantics of an uncorrelated EXISTS — and after() drops it, so the proof genuinely requires both directions of the rule's core fact that group-by preserves empty/non-emptiness (it would fail if QED's group-by semantics produced a row for an empty input). The rule's key precondition is respected (non-scalar group-by, i.e. a non-empty group set — a scalar group-by would break the rule, and the porter correctly avoided that), the outer and subquery-input scans are properly independent uninterpreted symbols, and the omitted EnsureDistinctOn caveat is sidestepped by not encoding DistinctOn. The PARTIAL scope line is honest and specific about the real restrictions (uncorrelated EXISTS only, single uninterpreted group key, no aggregate calls), and the result is still a non-degenerate theorem rather than a structurally identical pair. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9979957
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33321083
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 968000
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 653708
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 25138417
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33432875
  },
  "total_duration": {
    "secs": 0,
    "nanos": 75449875
  }
}
```
