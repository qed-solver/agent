# EliminateSetLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** covers only the UnionAll arm (the ExceptAll arm requires bag-minus semantics that QED does not model), with the zero-row right operand modeled as a structurally empty relation to encode HasZeroRows.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/set.opt

EliminateSetLeft replaces a UnionAll or ExceptAll operator with a right side
having a cardinality of zero, with just the left side operand.

It is possible for the left and right sides of the set operator to have column
IDs that are also present in the output columns of the operator, e.g. after
the SplitDisjunction exploration rule has been applied. These columns are
included as passthrough columns in the generated Project because they do not
need to be projected. All other column IDs are added to the ProjectionsExpr.

Extracted from `set.opt` (which defines multiple rules — implement specifically `EliminateSetLeft`, not the other rules in that file):

```
# EliminateSetLeft replaces a UnionAll or ExceptAll operator with a right side
# having a cardinality of zero, with just the left side operand.
#
# It is possible for the left and right sides of the set operator to have column
# IDs that are also present in the output columns of the operator, e.g. after
# the SplitDisjunction exploration rule has been applied. These columns are
# included as passthrough columns in the generated Project because they do not
# need to be projected. All other column IDs are added to the ProjectionsExpr.
[EliminateSetLeft, Normalize]
(UnionAll | ExceptAll
    $left:*
    $right:* & (HasZeroRows $right)
    $colmap:*
)
=>
(Project
    $left
    (ProjectColMapLeft $colmap)
    (ProjectPassthroughLeft $colmap)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful to the UnionAll arm: before() is π(Left) UNION ALL Empty while after() is π(Left) — structurally distinct, so the proof is of the rule's genuine semantic core (bag-union with a zero-row operand is identity), not a vacuous equality; Left and Right are distinct scans correctly sharing row-type symbols (required for type-compatible set operands), and the non-identity colmap (2,0,1) is applied uniformly to both operands exactly as the source requires. The PARTIAL scope line is honest and specific: the ExceptAll arm is truly inexpressible (JSONSerializer only serializes set minus and throws for the bag variant), and modeling HasZeroRows as a structural Empty is the closest available proxy, since the DSL exposes no zero-row relation constraint (only uniqueness keys). ```

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
    "nanos": 832833
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 338375
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
    "nanos": 1512375
  }
}
```
