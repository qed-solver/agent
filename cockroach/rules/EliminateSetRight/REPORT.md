# EliminateSetRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** the left operand is modeled as structurally empty, which is the faithful encoding of `HasZeroRows $left` since the DSL has no cardinality constraint.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/set.opt

EliminateSetRight replaces a UnionAll operator with a left side having a
cardinality of zero, with just the right side operand. Note that it only
applies to UnionAll operators because Except operators only output left input
rows.

See the comment above EliminateSetLeft which describes when columns are
projected vs. passed-through.

Extracted from `set.opt` (which defines multiple rules — implement specifically `EliminateSetRight`, not the other rules in that file):

```
# EliminateSetRight replaces a UnionAll operator with a left side having a
# cardinality of zero, with just the right side operand. Note that it only
# applies to UnionAll operators because Except operators only output left input
# rows.
#
# See the comment above EliminateSetLeft which describes when columns are
# projected vs. passed-through.
[EliminateSetRight, Normalize]
(UnionAll $left:* & (HasZeroRows $left) $right:* $colmap:*)
=>
(Project
    $right
    (ProjectColMapRight $colmap)
    (ProjectPassthroughRight $colmap)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the source rule's semantics: `before()` is a UNION ALL of a structurally-empty left (faithful encoding of `HasZeroRows $left`, the best the DSL can express) with the colmapped right, and `after()` is just the colmapped right — a genuinely non-trivial difference that proves `∅ ∪_bag R = R` for all instantiations. The colmap (concrete permutation applied identically to both sides) correctly models the type-alignment requirement of the set op, the `union(true, …)` flag matches UnionAll (bag union), and the PARTIAL scope tag is honest since the DSL cannot express "arbitrary expression constrained to zero rows" as distinct from "structurally empty relation."

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
    "nanos": 56625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 318041
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
    "nanos": 503666
  }
}
```
