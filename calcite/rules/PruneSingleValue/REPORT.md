# PruneSingleValue

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 62  **Verification rounds used:** 3
**Scope detail:** inner join only, with the right input a single-row Values holding one constant column (a Boolean literal)


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SingleValuesOptimizationRules.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and matches the source rule's core inner-join transformation exactly: `O ⋈_{jcond} Values{true}` is rewritten to `π(o, true)(σ_{jcond(o, true)} O)`, with the same uninterpreted predicate "jcond" correctly shared (in `before` its second argument is bound to the single-row Values' column, which holds `true`, matching the `true` literal in `after`'s filter and project, in the correct left-then-right output order). The proof universally quantifies over the table `O`, its row type, and the join condition — the parts that actually vary across queries — and no source precondition (single-row Values, non-outer join type) is silently dropped; the INNER choice satisfies the source's `isJoinTransformable` check. The restriction to the right-side inner-join case with a one-column Boolean-literal row is genuinely narrower than the full rule (which also covers LEFT/RIGHT/SEMI/LEFT_MARK variants, multi-column rows, and the Project-over-Values "WithExpr" variants, with the `true` constant hard-coded only because the DSL exposes no builder for a Values row with an uninterpreted constant), but that is specifically and honestly tagged as SCOPE: PARTIAL, and the instance remains non-degenerate, so the "provable" verdict is a faithful verification of the stated special case. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5658625
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34926291
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 929000
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 402125
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16806875
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35025292
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67434084
  }
}
```
