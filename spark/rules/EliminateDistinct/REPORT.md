# EliminateDistinct

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** only case 2 of the Spark rule (distinct key on child): the child is a unique scan on its single column and the DISTINCT aggregate takes exactly that column as its (sole) argument, so per-group argument values are already distinct; case 1 (Min/Max/BitAnd/BitOr/CollectSet/First/Last are duplicate-agnostic) is unprovable in QED, which models every aggregate, even one named "Min", as an uninterpreted function knowing only bag-equality of its input.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 603-629
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` and `after()` are structurally distinct (they differ only in the `distinct` flag of the `AggCall` for the shared uninterpreted aggregate `f`), so the proof is of the real Spark Catalyst `EliminateDistinct` transformation (drop `DISTINCT`) and not vacuous; the symbols are shared correctly — the group key `g(col)` is functionally determined by the same single unique column `col` that is the sole aggregate argument, so all rows in each group carry one argument value and the distinct and plain input bags coincide, making `f(distinct)=f(plain)` provable without any assumption about `f`; the one genuine restriction is that the distinct key on the child is encoded as a unique single-column scan with the group key derived from that same column (a valid subsumption of the `distinctKeys ⊇ {arg}` condition), while Spark's duplicate-agnostic case (Min/Max/etc.) is truly unprovable in QED and is honestly excluded, so the PARTIAL scope tag is accurate.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8804126
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 44448958
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1174250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 863167
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22158375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 44669083
  },
  "total_duration": {
    "secs": 0,
    "nanos": 85040833
  }
}
```
