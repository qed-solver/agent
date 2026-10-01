# PushFilterIntoAsOfJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 46  **Verification rounds used:** 3
**Scope detail:** ASOF modeled as a LEFT JOIN (left rows preserved once with values unchanged); proves only the left-input pushdown of one deterministic left-only conjunct with the remaining conjuncts kept above the join; the right-input mirror of a key-equality predicate is not modeled


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 1156-1223
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully models the rule's core by splitting the filter into an uninterpreted left-only conjunct `f` (pushed into the left input) and a remainder `g` kept above the join, with correct symbol sharing (`f` applied to the identical left columns on both sides, unchanged `on`, and genuinely distinct before/after plans, so the proof is non-vacuous). Modeling ASOF as a LEFT JOIN is sound because the pushdown's validity rests solely on left-row preservation with unchanged left values plus NULL-padded right columns — properties LEFT JOIN satisfies and which QED verifies; a true ASOF "best-match" operator can't be added via the DSL since QED cannot model that ordering/list semantics. The omitted right-side key-equality mirror is a genuine QED entailment limitation (matched pairs share key values), correctly excluded and honestly tagged PARTIAL, so the provable result is a meaningful [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 12963832
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 40355667
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 935625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 984542
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 30100000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 40582125
  },
  "total_duration": {
    "secs": 0,
    "nanos": 86752083
  }
}
```
