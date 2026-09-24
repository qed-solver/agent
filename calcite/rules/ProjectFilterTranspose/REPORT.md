# ProjectFilterTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 39  **Verification rounds used:** 2
**Scope detail:** fixed-shape whole-expressions mode: 2-column input, projection (E(x,y), F(x)) pushed through filter P(E(x,y), y) by hoisting E and F into an intermediate projection


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectFilterTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful fixed-shape instantiation of the rule's whole-expressions mode: before() = Filter(P(E(x,y), y), S) → Project(E(x,y), F(x)), and after() hoists E and F into Project(S, [x, y, E(x,y), F(x)]), filters on the hoisted columns via the same uninterpreted P, and projects them back out — exactly the sub-project/filter/top-project shape Calcite's Replacer builds. The proof is non-vacuous (the plans genuinely differ: filter over raw scan with embedded uninterpreted calls vs. filter over a larger intermediate projection), symbols are correctly shared (E, F, P reused by name across both sides, with no spurious identification of independent expressions), and no hidden preconditions are needed since bag-semantics commutation of Filter and Project holds for any uninterpreted operators and all column types — which the prover checked universally. The remaining gap to the full rule (arbitrary numbers of input columns, project expressions, and filter operands, plus ref-collection across arbitrary shapes) is inherent to RuleScript's lack of let-binding/bounded-schema constructs, not an avoidable hard-coding; the SCOPE: PARTIAL line is specific, accurate, and the rule is a useful non-degenerate special case. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7432417
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 44972417
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1427541
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 523875
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 20972166
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 45185875
  },
  "total_duration": {
    "secs": 0,
    "nanos": 84999541
  }
}
```
