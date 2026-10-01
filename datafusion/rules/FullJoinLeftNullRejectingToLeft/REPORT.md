# FullJoinLeftNullRejectingToLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** the null-rejecting filter is restricted to an explicit IS_NOT_NULL on the left input's column, and only the FullJoin→LeftJoin variant is modeled (the other eliminate_outer cases are not)


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_outer_join.rs, lines 102-244
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully models the rule's named case — a null-rejecting filter (IS_NOT_NULL on the left column, whose type is nullable so the filter is non-trivial) kept *above* the join while the join kind flips FULL→LEFT with the ON condition preserved as an uninterpreted symbol, exactly matching `eliminate_outer`'s `(Full, left_non_nullable=true, right_non_nullable=false)→Left` branch and the source's keep-the-filter-in-place (no pushdown/removal) behavior. `before()` and `after()` genuinely differ (FULL vs LEFT) so the proof is non-vacuous, and the two independent scans plus the uninterpreted join condition avoid any spurious over-constraint that could mask an unsound rewrite. The restriction to `IS_NOT_NULL` and to this single join-kind case is a genuine, specific, non-degenerate special case that is provable in QED (a general null-rejecting predicate would need predicate-entailment reasoning QED cannot perform), and it is honestly tagged `SCOPE: PARTIAL`.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 14993500
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
    "nanos": 1074750
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1155750
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 35198834
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 55844334
  }
}
```
