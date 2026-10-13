# ProjectMerge

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache Calcite
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** public record ProjectMerge() implements RRule {


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectMergeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (a Project stacked on a Project) and `after()` (a single Project) are structurally distinct, so the proof is non-vacuous and matches ProjectMerge's actual shape of collapsing two projections into one. Both projection layers are fully uninterpreted functions (`Bottom`, `Top`), and `Top`/`bottomExpr` are correctly shared as the *same* symbols on both sides, so what is proved is the fully general term-identity `Project(Top(Bottom(c)), Source) == Project(Top(Bottom(c)), Project(Bottom(c), Source))` rather than any concrete predicate or fixed arity-dependent structure. None of the source rule's guards (convention match, correlation variables, bloat/force) are bag-semantic preconditions, so nothing logical is missing; the only limitation is single-column arity, which is inherent to this DSL (scans and `Project` are 1-column) and does not narrow the algebraic content, since the per-expression composition is already fully general. ```

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
    "nanos": 308708
  }
}
```
