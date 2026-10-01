# NullAndOrToNull

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 9  **Verification rounds used:** 1
**Scope detail:** only the And branch of the rule (NULL AND NULL => NULL) is encoded; the Or branch is structurally identical but not expressible in a single before()/after() pair


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 860-865
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the AND branch of the source rule: `before()` is `Filter(Source, AND(NULL, NULL))` and `after()` is `Filter(Source, NULL)`, which are structurally distinct (an AND node vs. a bare literal), correctly use a boolean-typed null literal matching the original's `is_null`/`lit_bool_null()` semantics, and wrap the expression in a filter — a valid context for verifying boolean-expression equivalence. The SCOPE line honestly and specifically notes the only limitation (OR branch omitted), which is genuine since the DSL has no pattern-disjunction mechanism to match "AND or OR" in a single before/after pair. ```

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
    "nanos": 416917
  }
}
```
