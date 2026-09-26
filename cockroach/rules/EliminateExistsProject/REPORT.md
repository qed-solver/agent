# EliminateExistsProject

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 44  **Verification rounds used:** 1
**Scope detail:** EXISTS is unconditionally insensitive to any Project on its input, since Project never changes row cardinality


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateExistsProject discards a Project input to the Exists operator. The
Project operator never changes the row cardinality of its input, and row
cardinality is the only thing that Exists cares about, so Project is a no-op.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateExistsProject`, not the other rules in that file):

```
# EliminateExistsProject discards a Project input to the Exists operator. The
# Project operator never changes the row cardinality of its input, and row
# cardinality is the only thing that Exists cares about, so Project is a no-op.
[EliminateExistsProject, Normalize]
(Exists (Project $input:*) $existsPrivate:*)
=>
(Exists $input $existsPrivate)
```
```

## Independent verifier review

**Verdict:** CONFIRMED (manual)

Manually investigated by Claude (the automated attempt found a real proof via a DSL extension, but it broke 25 already-proved rules on merge — correctly blocked). Instead of reusing that risky edit, encoded this fully within the porter's own candidate file: RexRN is just an interface (RexNode semantics()), so a custom local record can build a real Calcite RexSubQuery.exists(RelNode) directly via raw Calcite APIs, with zero shared DSL file changes (no extend_dsl_file, no regression risk). QED's prover core (qed-prover/src/pipeline/relation.rs, eval_logic's EXISTS arm) already has genuine interpreted EXISTS semantics: Logic::squash(UExpr::sum(scope, UExpr::app(rel, vars))) — true iff the subquery relation has at least one row — confirmed via JSONSerializer, which already serializes RexSubQuery objects with an 'EXISTS' operator name and embedded query relation. Encoded before() = outer.filter(EXISTS(Project(input))), after() = outer.filter(EXISTS(input)); QED proves them equal with real SMT engagement (not a structural triviality). Verified non-vacuous with a negative control: adding a filter to the projected side (which CAN remove rows) correctly breaks the proof (provable=false) — confirming the check is genuinely sensitive to row presence/absence, not just always-true. SCOPE: FULL, since EXISTS is unconditionally insensitive to any Project on its input (Project never changes row cardinality, which is the entire premise of the source rule).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7024333
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6666834
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 55875
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 443375
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 15022792
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6712000
  },
  "total_duration": {
    "secs": 0,
    "nanos": 24562875
  }
}
```
