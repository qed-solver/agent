# EliminateWindowPartitions

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 64  **Verification rounds used:** 4
**Scope detail:** QED has no window operator, so the foldable partition expression is modeled as a constant INTEGER literal group key, and the proven fragment is that eliminating a constant group key from a group-by over an uninterpreted aggregate preserves the output bag.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/EliminateWindowPartitions.scala, lines 28-41
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the exact invariant the Spark rule relies on — that a foldable (constant) component of a partition/grouping key is redundant because it never splits rows — by placing the shared `constKey` literal as a group-by key in `before()` and removing it (while re-projecting it) in `after()`, so the proof genuinely depends on `constKey` being a constant rather than being vacuous. `before()` and `after()` are structurally distinct and the equivalence holds universally over the uninterpreted source table's contents, with no over-constraining symbol reuse, no missing precondition, and the aggregate correctly left uninterpreted so the claim is independent of the specific window/agg function. The group-by vehicle is the correct bag-semantic stand-in for window partitioning (QED cannot model windows at all), and the `// SCOPE: PARTIAL` line honestly and specifically discloses this, making the result a non-degenerate, useful fragment of the real rule rather than a misleading full proof. ```

(This proof relies on an accepted RuleScript DSL extension made during this session — see the extended file(s) for what changed.)

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 10715834
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 47290999
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1128709
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 812042
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 25390458
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 47443959
  },
  "total_duration": {
    "secs": 0,
    "nanos": 88863000
  }
}
```
