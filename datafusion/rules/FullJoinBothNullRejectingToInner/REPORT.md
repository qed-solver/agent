# FullJoinBothNullRejectingToInner

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** filter predicate restricted to exactly one IS_NOT_NULL conjunct per single-column join side (FULL→INNER branch only; no projection inlining, no other join-type branches)


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_outer_join.rs, lines 102-244
```

## Independent verifier review

**Verdict:** CONFIRMED

before() and after() genuinely differ (FULL vs INNER join under the same shared uninterpreted `on` condition and the same shared filter `p`), so the proof is non-vacuous — the equivalence holds precisely because `IS_NOT_NULL(l0) ∧ IS_NOT_NULL(r0)` kills the null-padded rows a FULL join emits beyond an INNER join, and a miswiring of the JoinField indices would have falsified it. Symbol usage is correct (distinct L/R scans, one `on` and one filter predicate consistently shared between both sides of the rewrite, matching DataFusion's `eliminate_outer` (Full, true, true)→Inner branch which preserves the predicate on top of the simplified join), and the PARTIAL scope tag is accurate and specific: only that one branch, filter restricted to one IS_NOT_NULL per single-column side, and no projection inlining — a genuine, non-degenerate special case of the source rule that is the natural provable core given QED cannot express "arbitrary null-rejecting predicate" as a side condition on an uninterpreted symbol.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7569914
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
    "nanos": 945084
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 867000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22859834
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 39460958
  }
}
```
