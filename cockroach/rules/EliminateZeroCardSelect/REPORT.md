# EliminateZeroCardSelect

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 3  **Verification rounds used:** 1
**Scope detail:** the zero-rows precondition is encoded as a literal .empty() relation (a statically known-empty shape), matching the source rule's HasZeroRows guard


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

EliminateZeroCardSelect eliminates a Select when its input has zero
cardinality. The filter expressions are per-row and will never be evaluated,
so it is safe to remove them regardless of volatility. This complements the
SimplifyZeroCardinalityGroup rule which requires the entire expression to be
leakproof.

Extracted from `select.opt` (which defines multiple rules — implement specifically `EliminateZeroCardSelect`, not the other rules in that file):

```
# EliminateZeroCardSelect eliminates a Select when its input has zero
# cardinality. The filter expressions are per-row and will never be evaluated,
# so it is safe to remove them regardless of volatility. This complements the
# SimplifyZeroCardinalityGroup rule which requires the entire expression to be
# leakproof.
[EliminateZeroCardSelect, Normalize]
(Select $input:* & (HasZeroRows $input) $filters:*)
=>
$input
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful and non-degenerate: `before()` (two nested uninterpreted filters over a structurally-empty `.empty()` relation) genuinely differs from `after()` (the bare empty relation), and the proof that they are bag-equivalent is exactly the rule's validity claim — filtering a zero-row input yields the same zero-row input, so the Select can be dropped. The `HasZeroRows` precondition is captured in the only form the DSL permits (a statically-known-empty shape rather than a general side-condition over an uninterpreted input), which is a real QED limitation rather than a missing operator, so the narrower special case is the correct fallback and is honestly flagged PARTIAL; the operators (nested Filters modeling a multi-filter Select, the empty relation) and the shared input symbol are all correct. ```

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
    "nanos": 783167
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 256958
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
    "nanos": 1406125
  }
}
```
