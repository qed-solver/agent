# FoldTupleAccessIntoValues

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 81  **Verification rounds used:** 5

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/project.opt

FoldTupleAccessIntoValues replaces a Values that has a single tuple column and
at least one row with a new Values that has a column for each tuple index.
This works as long as the surrounding Project does not reference the original
tuple column itself, since then it would be invalid to eliminate that
reference. However, references to fields within the tuple are allowed, and are
translated to the new unnested Values columns.

This rule simplifies access to the Values operator in hopes of allowing other
rules to fire.

Example:

SELECT (tup).@1, (tup).@2 FROM (VALUES ((1,2)), ((3,4))) AS v(tup)
=>
SELECT tup_1, tup_2 FROM (VALUES (1, 2), (3, 4)) AS v(tup_1, tup_2)

Extracted from `project.opt` (which defines multiple rules — implement specifically `FoldTupleAccessIntoValues`, not the other rules in that file):

```
# FoldTupleAccessIntoValues replaces a Values that has a single tuple column and
# at least one row with a new Values that has a column for each tuple index.
# This works as long as the surrounding Project does not reference the original
# tuple column itself, since then it would be invalid to eliminate that
# reference. However, references to fields within the tuple are allowed, and are
# translated to the new unnested Values columns.
#
# This rule simplifies access to the Values operator in hopes of allowing other
# rules to fire.
#
# Example:
#
#   SELECT (tup).@1, (tup).@2 FROM (VALUES ((1,2)), ((3,4))) AS v(tup)
#   =>
#   SELECT tup_1, tup_2 FROM (VALUES (1, 2), (3, 4)) AS v(tup_1, tup_2)
#
[FoldTupleAccessIntoValues, Normalize]
(Project
    $input:(Values [ * ... ]) &
        (ColsAreLenOne (OutputCols $input)) &
        (CanUnnestTuplesFromValues $input)
    $projections:* &
        (HasNoDirectTupleReferences
            $projections
            $col:(SingleColFromSet (OutputCols $input))
        )
    $passthrough:* & (ColsAreEmpty $passthrough)
)
=>
(Project
    (UnnestTuplesFromValues
        $input
        $tupleCols:(MakeColsForUnnestTuples $col)
    )
    (FoldTupleColumnAccess $projections $tupleCols $col)
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests on tuple-component semantics — that a Values' single tuple column and its unnested sibling columns denote the same data reorganized (sibling j = the j-th component of the tuple) — which is precisely the backend definition of UnnestTuplesFromValues/FoldTupleColumnAccess, a data transformation rather than an operator rearrangement over fixed data that QED normally proves. QED's flat-scalar theory has no record type or constructor/destructor law to equate a tuple column with its components, so the only encodable proof hard-wires the sibling cells as the access expressions, thereby assuming the very fact the rule is meant to establish. This is a genuine limitation of QED's scalar theory (no tuple algebra / no way to relate two relations by a data-rewriting function), not a symbol-sharing bug or modeling slip a further attempt could fix. ```
