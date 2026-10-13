# PushFilterIntoProjection

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** two-column projection over a single-column source with a two-conjunct filter: the pushable conjunct is rewritten onto its projection expression and pushed below, the retained conjunct stays above


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 908-920
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors DataFusion's pushdown: `before` is `Filter(F(P1(c)) AND G(P2(c)), Project[P1,P2](Source))` and `after` is `Filter(G(P2(c)), Project[P1,P2](Filter(F(P1(c)), Source)))`, with P1/P2 (via `proj`) and F/G all uninterpreted symbols — no baked-in concrete predicates, correct Filter/Project nesting and column ordinals, and no vacuity since the structures genuinely differ (F applied below vs. above the projection), so QED had to prove the non-trivial identity that F(P1(c)) over the raw source equals F(P1(c)) over the projected column, i.e. the real pushdown step. It is a narrower, honestly-tagged special case of the general rule (fixed two-column projection over a single-column source, exactly two-conjunct filter with exactly one pushable conjunct, where the real rule handles arbitrary arities and any number/choice of pushable conjuncts via `rewrite_projection`), but the restriction is specific rather than vague and the rule remains non-degenerate, and no missing preconditions exist (QED's uninterpreted functions are total, so no totality side-condition from the real rule's `ExprRewriter` is silently required).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5627918
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36655459
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 873791
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 414875
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 17043667
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36752875
  },
  "total_duration": {
    "secs": 0,
    "nanos": 69791583
  }
}
```
