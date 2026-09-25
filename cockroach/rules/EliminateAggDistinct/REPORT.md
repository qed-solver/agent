# EliminateAggDistinct

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** the aggregation argument is the input relation's unique (primary) key, so each group holds at most one row and AggDistinct is a per-group no-op; the full rule (min/max/bool_and/bool_or are idempotent, so AggDistinct never changes their value over arbitrary inputs) is unprovable in QED, which models every aggregate, even one named "Min", as an uninterpreted function knowing only bag-equality of its input.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/agg.opt

EliminateAggDistinct removes AggDistinct for aggregations where DISTINCT
never modifies the result; for example: min(DISTINCT x).

Extracted from `agg.opt` (which defines multiple rules — implement specifically `EliminateAggDistinct`, not the other rules in that file):

```
# EliminateAggDistinct removes AggDistinct for aggregations where DISTINCT
# never modifies the result; for example: min(DISTINCT x).
[EliminateAggDistinct, Normalize]
(AggDistinct $input:(Min | Max | BoolAnd | BoolOr))
=>
$input
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a sound, non-vacuous special case rather than a vacuous one: `before()` and `after()` genuinely differ in the AggCall's `distinct` flag (which the JSON serializer emits), the same uninterpreted aggregate "f" is shared on both sides so the claim is really "distinctification changes nothing here," and that claim depends essentially on the disclosed premise — the scan is unique on field 0 and groups are by field 0, so every group is a singleton and bag distinctification is the identity (drop `unique=true` and QED would refute the equivalence, since it models aggregates as uninterpreted functions). The full rule (Min/Max/BoolAnd/BoolOr idempotent over arbitrary inputs) is genuinely out of reach of QED: no DSL construct or JSON field exists to attach an idempotency axiom to an uninterpreted aggregate, and the prover is the fixed arbiter, so this is a real QED limitation, not an unexplored DSL gap. The `// SCOPE: PARTIAL` line is present, specific, and accurately matches the code (uniqueness on field 0, group-by field 0, aggregation over field 0), with no missing preconditions or symbol-sharing errors.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9255459
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 39847666
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 918625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 610750
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 24128667
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 40046458
  },
  "total_duration": {
    "secs": 0,
    "nanos": 80494625
  }
}
```
