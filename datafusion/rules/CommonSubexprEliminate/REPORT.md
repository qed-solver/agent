# CommonSubexprEliminate

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** projection-only CSE instance: exactly two projected expressions sharing exactly one common subexpression


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/common_subexpr_eliminate.rs, lines 1-841
```

## Independent verifier review

**Verdict:** CONFIRMED

before() genuinely duplicates the nested subterm g(c0) under two distinct uninterpreted projections f1/f2 over a 1-column scan, while after() hoists g into an intermediate projection [g(c0), c0] and references its output column from the outer projection — structurally different plans that mirror DataFusion's try_optimize_proj + build_common_expr_project_plan shape exactly (common expr first, passthrough after, top projection drops the passthrough), with Source/g/f1/f2 correctly shared by name across both sides, so the proof is non-vacuous and universal over the uninterpreted symbols (the extensional-function model matches the rule's own is_volatile_node precondition rather than silently dropping it). The declared PARTIAL scope is honest and specific — projection node only, exactly two projected expressions sharing exactly one common subexpression — even though the full rule also covers Filter/Sort/Window/Aggregate and arbitrary expression lists (Window/Sort aren't even exposed by the DSL); the encoded instance is a non-degenerate, genuine special case of the real rewrite, not a structurally-identical tautology. ```

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
    "nanos": 348167
  }
}
```
