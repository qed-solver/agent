# ConvertToLocalRelation

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 31  **Verification rounds used:** 2
**Scope detail:** only the Project-over-LocalRelation branch is modeled, on a fixed two-row three-column relation with a pure column-permutation projection; the Limit, LocalLimit, and Filter branches are not modeled.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3296-3325
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (a Project over a values relation) and after() (the folded values relation) are structurally distinct, so the proof is a genuine non-vacuous check of the constant fold, and the instance (column-reference projection) satisfies the rule's "no unevaluable expressions" guard. The hard-coding is forced rather than accidental: this rule's after-side is a closed data-dependent literal that QED cannot express parametrically over an uninterpreted data symbol, the Limit/LocalLimit branches have no bag-semantic meaning in QED, and Filter folding would require interpreted predicates which the DSL/QED only offer as uninterpreted (unfodable) symbols — making a concrete Project-branch instance the maximal expressible fragment. The SCOPE: PARTIAL line names exactly these restrictions and is honest, and the result is non-degenerate (row reordering/projection verified, not a structurally identical pair). ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6205916
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 41096124
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 952625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 506167
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 17578500
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 41169667
  },
  "total_duration": {
    "secs": 0,
    "nanos": 74595833
  }
}
```
