# FoldTupleAccessIntoValues

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3

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

The rule's validity rests on evaluating tuple-field accesses (e.g. `(tup).@1`) over *constant* tuple literals and baking the resulting components into new constant rows of the replacement Values, but QED's object language only has flat rows over uninterpreted sorts (`RelType.VarType` all serialize to the single "INTEGER" sort, and `JSONSerializer` has no product/record type), so a tuple column, its `@i` access operators, and the before/after row constants are all mutually unrelated uninterpreted symbols — QED has no axiom that `access_i(<tuple-const>) = <component-const>`, which no `extend_dsl_file` can add since that axiom must live in the fixed prover's SMT translation (a Values-with-expressions encoding only yields the trivially true identity, not the rule). This is precisely the "operator whose specific internal semantics QED cannot see through as an uninterpreted function" limitation, and the porter's diagnosis of the missing composite sort / component-extraction semantics is accurate.
