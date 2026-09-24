# ProjectToCalc

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** the input is fixed at 2 columns and the project list is exactly two uninterpreted expressions over them (the original rule applies to any input arity and any expression list); the Calc's absent condition is represented by the constant-TRUE filter, per this DSL's Calc convention


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectToCalcRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully maps the rule's two node shapes — `before()` is the bare `Project([E1,E2], S)` (the `LogicalProject`), and `after()` is `Project([E1,E2], Filter(TRUE, S))`, the DSL's filter-then-project encoding of a `LogicalCalc` whose RexProgram condition is absent (null→TRUE), in the correct order (condition on input columns, then projection), with E1/E2 as two independent uninterpreted projection operators rather than over-constrained shared ones. `before()` and `after()` are genuinely structurally distinct (the extra TRUE filter), so the proof is not vacuous; its near-instant success is consistent with the rule being a semantic no-op (a Calc with no condition *is* a Project), so the triviality is inherent to the rule, not a porter error. The only narrowing — 2-column input / 2-expression list — is honestly and specifically tagged PARTIAL, is not a concrete-value or join-type hard-coding that would change the semantic claim, and since "Filter(TRUE, X) ≡ X" is arity-independent, the 2-column instance is a faithful, non-degenerate representative of the fully general rewrite. ```

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
    "nanos": 92708
  }
}
```
