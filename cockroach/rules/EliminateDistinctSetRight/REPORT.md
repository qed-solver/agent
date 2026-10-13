# EliminateDistinctSetRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** the left operand is modeled as structurally empty, which is the faithful encoding of `HasZeroRows $left` since the DSL has no cardinality constraint.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/set.opt

EliminateDistinctSetRight mirrors EliminateDistinctSetLeft. Note that it only
applies to Union because Except operators only output left input rows.

Extracted from `set.opt` (which defines multiple rules — implement specifically `EliminateDistinctSetRight`, not the other rules in that file):

```
# EliminateDistinctSetRight mirrors EliminateDistinctSetLeft. Note that it only
# applies to Union because Except operators only output left input rows.
[EliminateDistinctSetRight, Normalize]
(Union $left:* & (HasZeroRows $left) $right:* $colMap:*)
=>
(DistinctOn
    $project:(Project
        $right
        (ProjectColMapRight $colMap)
        (ProjectPassthroughRight $colMap)
    )
    []
    (MakeGrouping (OutputCols $project) (EmptyOrdering))
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures EliminateDistinctSetRight's semantics: a distinct `Union` (correctly `union(false, ...)`, since the source `Union` is the dedup variant, not `UnionAll`/`Except`) whose left input has zero rows is replaced by a DistinctOn over the colmapped right input, which the porter models as a GROUP BY on all output fields with no aggregate calls — exactly the `DistinctOn(..., [], MakeGrouping(OutputCols, EmptyOrdering))` shape. `HasZeroRows $left` is encoded as a structurally empty relation of the union's row type, which is the only faithful encoding available (the DSL/JSON format has no cardinality-constraint mechanism), and this narrowing is honestly declared in the SCOPE line; the left and right remain independent table symbols, and the shared row types are *required* for a well-formed union rather than an over-constraint. `before()` (distinct union with an Empty) and `after()` (full-column distinct) are structurally different plans, so the proof is non-vacuous and establishes exactly the rule's content: dropping a provably-empty operand from a distinct union leaves the dedup of the surviving side; the non-identity projection applied identically to both operands correctly models the colmap remapping (incidental to the equivalence but present on both sides, as in the source). ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7976460
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 26451292
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 924416
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 521291
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21238416
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 26527708
  },
  "total_duration": {
    "secs": 0,
    "nanos": 63726334
  }
}
```
