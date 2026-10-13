# EliminateDistinctNoColumns

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateDistinctNoColumns eliminates a distinct operator with no grouping
columns, replacing it with a projection and a LIMIT 1. For example:
SELECT DISTINCT ON (a) a, b FROM ab WHERE a=1
is equivalent to:
SELECT a, b FROM ab WHERE a=1 LIMIT 1

Note that this rule does not apply to EnsureDistinctOn or
EnsureUpsertDistinctOn, since they will raise an error if there are duplicate
rows.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateDistinctNoColumns`, not the other rules in that file):

```
# EliminateDistinctNoColumns eliminates a distinct operator with no grouping
# columns, replacing it with a projection and a LIMIT 1. For example:
#   SELECT DISTINCT ON (a) a, b FROM ab WHERE a=1
# is equivalent to:
#   SELECT a, b FROM ab WHERE a=1 LIMIT 1
#
# Note that this rule does not apply to EnsureDistinctOn or
# EnsureUpsertDistinctOn, since they will raise an error if there are duplicate
# rows.
[EliminateDistinctNoColumns, Normalize]
(DistinctOn | UpsertDistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* & (HasNoGroupingCols $groupingPrivate)
)
=>
(ConstructProjectionFromDistinctOn
    (Limit
        $input
        (IntConst (DInt 1))
        (GroupingInputOrdering $groupingPrivate)
    )
    (MakeEmptyColSet)
    $aggregations
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule rewrites a no-grouping-columns DistinctOn (which emits at most one row — the input's first row under GroupingInputOrdering) into an ordered `LIMIT 1` over the same input, so its correctness rests entirely on both sides selecting the identical row *by order*. QED has no bag semantics for `Sort`/`Limit` (at best an uninterpreted `QOp`, qed.pdf §6.2 "List semantic" failure category), and the before side's first-row-under-ordering selection is likewise unencodable in the bag core language — a keyless `Aggregate` would emit a row on empty input (wrong cardinality) and its uninterpreted output values could never be linked to the opaque `Limit`, so even the most plausible special case (unique-keyed input) still leaves an inseparable opaque operator in `after()`. Since the gap sits in QED's own semantics rather than a missing DSL builder, `extend_dsl_file` cannot close it; UNSUPPORTED is correct. ```
