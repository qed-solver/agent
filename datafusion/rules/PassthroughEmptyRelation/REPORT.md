# PassthroughEmptyRelation

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** source family covers Projection/Filter/Window/Sort/SubqueryAlias/Repartition/Limit nodes whose child is a zero-row EmptyRelation; this encoding pins the Filter member with an uninterpreted predicate over an arbitrary input type (Window/Sort/Limit have no bag semantics and SubqueryAlias/Repartition are identity aliases absent from the DSL).


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/propagate_empty_relation.rs, lines 62-74
```

## Independent verifier review

**Verdict:** CONFIRMED

before() = Filter(pred, Empty(child)) and after() = Empty(child) are structurally distinct, and the proved claim — for every uninterpreted predicate over an arbitrary uninterpreted row type, filtering a zero-row bag yields that same zero-row bag — is precisely the semantic content of the source rule's Filter arm, where a filter's output schema equals its input's, so the replacement empty correctly carries the child's row type. The zero-row precondition (produce_one_row = false) is baked into the pattern's empty() shape, and nothing concrete (predicate, type, join kind) is hard-coded or coincidentally shared, so the proof is neither vacuous nor over-constrained. The PARTIAL scope is honestly disclosed: the encoding pins one representative member of the 7-node family (Window/Sort/Limit have no bag semantics in QED, and SubqueryAlias/Repartition are identity aliases with no DSL shape), a genuine, specific, non-degenerate narrowing. ```

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
    "nanos": 749834
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 230584
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
    "nanos": 1364417
  }
}
```
