# PruneUnionAllCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** both UnionAll inputs share the same three-column schema (types C0, C1, C2) with exactly columns 0 and 2 needed, and the outer projection is one uninterpreted projection over column 0 plus a single passthrough of column 2.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneUnionAllCols prunes columns from the left and right input relations that
are never used. Since UNION ALL preserves duplicates, any column may be pruned
if it is not needed, which is not generally true of set operators.

Since UnionAll requires that both inputs have an equal number of columns,
rather than using PruneCols to prune the left and right sides, this rule
pushes down Projects on both sides to ensure that exactly the needed columns
are passed as input to the UnionAll, to prevent situations where one side has
more columns left over after PruneCols than the other (for instance, if $left
is a normal scan where all columns may be pruned, but $right is a scan with a
filter, leading to an additional column being kept on just the right side).
If extraneous, these Projects may be cleaned up later by rules like
EliminateProject.

Note: The projections could reference columns from an outer scope, e.g. due
to an apply-join or routine. We intersect with the UnionAll's output to ensure
that $needed only contains columns from the UnionAll.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneUnionAllCols`, not the other rules in that file):

```
# PruneUnionAllCols prunes columns from the left and right input relations that
# are never used. Since UNION ALL preserves duplicates, any column may be pruned
# if it is not needed, which is not generally true of set operators.
#
# Since UnionAll requires that both inputs have an equal number of columns,
# rather than using PruneCols to prune the left and right sides, this rule
# pushes down Projects on both sides to ensure that exactly the needed columns
# are passed as input to the UnionAll, to prevent situations where one side has
# more columns left over after PruneCols than the other (for instance, if $left
# is a normal scan where all columns may be pruned, but $right is a scan with a
# filter, leading to an additional column being kept on just the right side).
# If extraneous, these Projects may be cleaned up later by rules like
# EliminateProject.
#
# Note: The projections could reference columns from an outer scope, e.g. due
# to an apply-join or routine. We intersect with the UnionAll's output to ensure
# that $needed only contains columns from the UnionAll.
[PruneUnionAllCols, Normalize]
(Project
    $union:(UnionAll $left:* $right:* $colmap:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $union
            $needed:(IntersectionCols
                (UnionCols
                    (ProjectionOuterCols $projections)
                    $passthrough
                )
                (OutputCols $union)
            )
        )
)
=>
(Project
    (UnionAll
        (Project $left [] (NeededColMapLeft $needed $colmap))
        (Project $right [] (NeededColMapRight $needed $colmap))
        (PruneSetPrivate $needed $colmap)
    )
    $projections
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding matches the source rule's exact shape — a Project over a UnionAll (correctly `all=true`) rewritten into the same outer projection over a UnionAll whose two inputs each get a lock-step Project selecting exactly the needed columns — which is precisely the equal-width pruning that PruneUnionAllCols requires (its comment explains why per-side `PruneCols` is replaced by paired input Projects). Symbol handling is correct and the proof is non-vacuous: L and R are independent scans, C0/C1/C2 are shared for union schema congruence, G is shared between before/after with field indices correctly remapped (0,2 → 0,1 after the inputs narrow), and before() is genuinely structurally different from after(), so QED verified the real law that the unreferenced column C1 can be dropped from both inputs simultaneously. The fixed 3-column width with needed set {0,2} is an essential specialization rather than an avoidable one — the needed subset is inherently structural in a projection pattern and cannot be a free uninterpreted symbol — and it is honestly tagged PARTIAL; the only blemish is that the SCOPE sentence calls column 2 a "passthrough" when the code actually feeds it as an argument of G (one output column), but the stated restriction still accurately describes the code, so the verdict remains a faithful, useful special case. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8499291
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 40727542
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 841084
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 589083
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21622125
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 40879792
  },
  "total_duration": {
    "secs": 0,
    "nanos": 78141666
  }
}
```
