# SimplifyTrueAnd

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** public record SimplifyTrueAnd() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyTrueAnd simplifies the And operator by discarding a True condition on
the left side.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyTrueAnd`, not the other rules in that file):

```
# SimplifyTrueAnd simplifies the And operator by discarding a True condition on
# the left side.
[SimplifyTrueAnd, Normalize]
(And (True) $right:*)
=>
$right
```
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` is `Filter(AND(TRUE, P), S)` and `after()` is `Filter(P, S)` over the same uninterpreted scan `S` and the same uninterpreted predicate `P`, so the proof is a genuine, non-vacuous universal proof of exactly the boolean identity `And(True, E) ≡ E` the source rule states, with the True literal on the correct (left) side, no concrete predicates or operators baked in, and symbol sharing matching the rule precisely. The identity holds under SQL three-valued logic for every truth value including NULL (`TRUE AND NULL = NULL`), so no precondition is silently missing, and embedding the scalar rule in a filter over a general scan (same shape as the reference FilterMerge exemplar) captures the rule's full logical content — the identity is compositional and its proof is universal over the uninterpreted symbols — so the `SCOPE: FULL` tag is honest.

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
    "nanos": 1029375
  }
}
```
