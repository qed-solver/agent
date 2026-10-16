# ProjectWindowTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** the window is modeled as a single partition-by group of per-partition uninterpreted aggregate window functions over one referenced operand column, with exactly one never-referenced passthrough input column and an uninterpreted top projection that ignores it


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectWindowTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a genuine, non-vacuous PARTIAL instance of the rule: it models a whole-partition, single-group window as an inner equi-join of the input with a group-by on the partition key (the maximal expressible form, since QED fundamentally has no Window/ordering semantics), bakes in exactly the rule's applicability precondition — column x referenced by neither the window functions (f(v), g(v)), the partition key, nor the top projection — and uses the same uninterpreted Top symbol with correctly shifted indices (0,1,4,5 → 0,1,3,4) to mirror the rule's index adjustment, so before() and after() genuinely differ and the proven pushdown identity is the real substance of the rewrite. The SCOPE: PARTIAL line accurately and specifically names the assumed restrictions (single partition group, whole-partition functions over one operand column, exactly one pruned passthrough column, top projection ignoring it), so the result is an honest, non-degenerate special case rather than an overclaimed full rule. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 27109585
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 134140541
  },
  "smt_timed_out": false,
  "nontrivial_perms": true,
  "translate_duration": {
    "secs": 0,
    "nanos": 1190042
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1480083
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 56151584
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 135754833
  },
  "total_duration": {
    "secs": 0,
    "nanos": 208574417
  }
}
```
