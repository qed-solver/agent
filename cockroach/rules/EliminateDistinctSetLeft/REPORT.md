# EliminateDistinctSetLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 47  **Verification rounds used:** 4
**Scope detail:** covers only the distinct-Union arm of the rule (a RuleScript pattern is a single operator tree, so the distinct-Except arm must be a sibling rule); the right operand is modeled as structurally empty, which is the faithful encoding of `HasZeroRows $right` since the DSL has no cardinality constraint.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/set.opt

EliminateDistinctSetLeft replaces a Union or Except operator with a right side
having a cardinality of zero, with a Distinct on just the left side operand.

Extracted from `set.opt` (which defines multiple rules — implement specifically `EliminateDistinctSetLeft`, not the other rules in that file):

```
# EliminateDistinctSetLeft replaces a Union or Except operator with a right side
# having a cardinality of zero, with a Distinct on just the left side operand.
[EliminateDistinctSetLeft, Normalize]
(Union | Except
    $left:*
    $right:* & (HasZeroRows $right)
    $colMap:*
)
=>
(DistinctOn
    $project:(Project
        $left
        (ProjectColMapLeft $colMap)
        (ProjectPassthroughLeft $colMap)
    )
    []
    (MakeGrouping (OutputCols $project) (EmptyOrdering))
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding matches the rule's real shape: before() is a distinct set operator (`union(false, …)`, i.e. CRDB's non-All `Union`) whose right operand is the empty relation — the exact semantic content of `HasZeroRows $right`, the only expressible form of that precondition in the DSL — and after() is a group-by over all output columns with zero aggregate calls, which is precisely `DistinctOn(Project($left, $colMap), [], MakeGrouping (OutputCols …) (EmptyOrdering))`; before() and after() are structurally different plans (Union vs Aggregate), so the SMT proof is of the genuine algebraic law `DistinctUnion(X, ∅) = Distinct(X)`, not a vacuous identity. Generality is preserved where it matters: left/right are independent uninterpreted scans over uninterpreted types (mirroring the single shared $colMap, which the real rule also applies to both sides), and the fixed 3-column width / (2,0,1) permutation are DSL-inherent instantiations of $colMap that do not narrow the proven claim, since the law is uniform in the colmap. The omitted distinct-Except arm is a genuine single-record (one before/after tree) limitation, correctly and specifically disclosed in the SCOPE: PARTIAL line. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7542916
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 25622666
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 910708
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 465167
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19720667
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 25693375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 61041500
  }
}
```
