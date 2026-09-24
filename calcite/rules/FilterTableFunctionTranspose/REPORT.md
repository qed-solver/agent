# FilterTableFunctionTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 35  **Verification rounds used:** 2
**Scope detail:** assumes the (single-input, identity 1-to-1 column mapping) table function has a 2-column input row; the original rule admits any equal field count


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterTableFunctionTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule: under the source's side conditions (single input, identity 1-to-1 non-derived mapping), the table function is row-preserving, and the porter models it exactly as an INNER join of the input with an auxiliary witness table on uninterpreted J projected back to the input fields — which preserves per-row bag multiplicities for arbitrary (J, T), with the same P and J symbols consistently shared and typed on both sides. before() (Filter above Project(Join(S,T))) and after() (Join(Filter(S),T)) are structurally distinct, so the proof is a genuine, non-vacuous pushdown equivalence (verified by hand: multiplicities of surviving rows match on both sides). The only narrowing — fixing the input row at 2 columns instead of any equal field count — is inherent to the fixed-shape pattern language (arity cannot be a symbolic/uninterpreted dimension), is honestly and specifically tagged SCOPE: PARTIAL, and the 2-column case is a non-degenerate, representative instance that still exercises multi-column predicates P. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7342377
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34546375
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1007292
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 551125
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19904584
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34651666
  },
  "total_duration": {
    "secs": 0,
    "nanos": 71165291
  }
}
```
