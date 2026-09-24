# FilterRemoveIsNotDistinctFrom

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 37  **Verification rounds used:** 2
**Scope detail:** IS NOT DISTINCT FROM is exactly the top-level filter condition applied over a two-column row built as an inner join of two single-column scans; the operands are plain (non-struct) columns of a single shared type


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterRemoveIsNotDistinctFromRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-trivial and operator-correct: before() is Filter(IS_NOT_DISTINCT_FROM(x,y)) and after() is the DNF `(x IS NULL AND y IS NULL) OR IS_TRUE(x = y)`, which I verified against Calcite's RelOptUtil.isDistinctFrom(rexBuilder, x, y, true) CASE expansion across all four null/non-null combinations under three-valued logic — both sides are never NULL and agree on every branch (both-null → TRUE; one-null → FALSE; both non-null → x = y). Symbol usage is right: x and y are two independent nullable columns of a single shared uninterpreted type V (sharing the type mirrors SQL's same-type requirement for the operator, not an over-constraint), and because they live in an inner cross-product join, the proven identity is pointwise over all value pairs (a,b), which by substitution subsumes any occurrence of the operator nested inside a larger filter condition. The SCOPE:PARTIAL tag is honest and specific — the pattern matches only a top-level IS NOT DISTINCT FROM over a two-column join row, which is the strongest relational shape the DSL can express (scans are single-column and there is no uninterpreted predicate-context hole) — so the narrowing is real, degenerate, and loses no logical content. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7543751
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36764459
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 847541
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 447500
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19984292
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36858000
  },
  "total_duration": {
    "secs": 0,
    "nanos": 72516208
  }
}
```
