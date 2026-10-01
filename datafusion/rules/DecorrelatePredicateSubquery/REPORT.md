# DecorrelatePredicateSubquery

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 25  **Verification rounds used:** 2
**Scope detail:** the uncorrelated top-level EXISTS case only: a plain EXISTS(subquery) predicate in a Filter becomes a LeftSemi join whose right input is the (uncorrelated) subquery plan, keeping its own filter, with a true join condition; the correlated-EXISTS/IN-equality and mark-join paths are out of scope


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/decorrelate_predicate_subquery.rs, lines 1-765
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding exactly mirrors the source rule's uncorrelated top-level EXISTS branch of `build_join_top`/`build_join`: `JoinType::LeftSemi` with join filter `lit(true)` (the `(None, None)` case) and the subquery plan carried over unchanged as the right input, with the subquery's own predicate left uninterpreted (`inner_pred`) and `outer`/`inner` as distinct symbols — so `before()` (Filter with EXISTS scalar subquery) and `after()` (SEMI join) are genuinely different plans and the proof is non-vacuous, covering precisely the transformation `Filter(R, EXISTS(S)) ≡ SEMI(R, true, S)` that DataFusion produces in this case (the post-join schema-restoring projection also doesn't fire for semi joins, as in the source). The scope line is honest and specific: NOT EXISTS/IN/NOT-IN, correlation-predicate extraction, null-awareness, and mark-join paths are genuinely out of scope (the DSL has no relational metavariable to keep the subquery/outer fully arbitrary, and QED cannot model null-aware NOT IN semantics), and the proved special case is non-degenerate rather than a structural tautology.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8603541
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33512833
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 839167
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 507792
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21861167
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33605792
  },
  "total_duration": {
    "secs": 0,
    "nanos": 70735917
  }
}
```
