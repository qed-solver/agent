# PushFilterIntoSubqueryAlias

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** public record PushFilterIntoSubqueryAlias() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 890-907
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful: SubqueryAlias is correctly modeled as an identity projection (a pure schema relabel with no effect on row values/multiplicity), and the single uninterpreted predicate P shared on both sides correctly reflects the rule's by-name column rewrite, which changes reference names but not predicate semantics over row values. before() (Filter above the alias) and after() (Filter below it) are structurally distinct, so the proof is non-vacuous and matches the actual DataFusion transformation; P, the column types, and the source are all uninterpreted, and the source rule has no preconditions beyond plan well-formedness (the `replace_cols_by_name?` fallback only guards ill-formed plans), so SCOPE: FULL is honest — the only fixed aspect is the 3-column width, which the DSL cannot quantify over and which is content-neutral since the commutation argument is uniform in arity. ```

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
    "nanos": 372625
  }
}
```
