# MultiJoinOptimizeBushy

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 33  **Verification rounds used:** 3
**Scope detail:** fixed 4-way inner multi-join with a single shared filter, rewritten from the left-deep tree (((A⋈B)⋈C)⋈_F D) to the genuinely bushy tree (A⋈B)⋈_F (C⋈D), with the filter kept as one uninterpreted predicate over the full concatenated row rather than the heuristic's cost-driven tree choice and per-conjunct condition distribution


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MultiJoinOptimizeBushyRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and symbolically sound: before() is the left-deep chain (((A⋈B)⋈C)⋈F D) and after() is the genuinely bushy (A⋈B)⋈F (C⋈D), over the same shared scans and the same uninterpreted 4-ary filter F applied in identical argument order on both sides, with INNER joins (matching the rule's outer-join refusal) and true-condition inner joins (matching the MultiJoin's unconstrained factors), so QED actually proved the rule's core semantic claim — that reshaping a flat multi-join into a bushy tree preserves bag semantics — rather than a tautology. The hard-coded choices are faithful to a real instance of the rule (a 4-factor multi-join whose only filter touches all four factors, which the real rule would itself leave as a top-level condition), and the shared-symbol usage is correct rather than coincidentally over-constraining. The SCOPE line honestly and specifically discloses the unavoidable narrowing — fixed 4-way arity, single shared filter, and no cost-driven edge selection or per-conjunct condition distribution, which QED fundamentally cannot model since the heuristic's output tree depends on cardinalities — so the provable result is a genuine, non-degenerate special case, not a misleading one.

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
    "nanos": 887000
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 700208
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
    "nanos": 1983250
  }
}
```
