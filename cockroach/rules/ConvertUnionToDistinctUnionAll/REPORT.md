# ConvertUnionToDistinctUnionAll

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/set.opt

ConvertUnionToDistinctUnionAll replaces a Union with a DistinctOn on top of a
UnionAll. This is a valid transformation when we can obtain a key over the
output of the UnionAll that not only functionally determines all columns from
both inputs, but functionally determines the *same* values from both inputs.
ConvertUnionToDistinctUnionAll can match when the left and right inputs satisfy
the following conditions:

1) All columns from both inputs originate from the same base table. This is
necessary because it is safe to de-duplicate over a subset of columns
that form a key over the base table (assuming condition #2 is also
satisfied).

2) All columns from a given side originate from the same meta table. This
avoids cases where joins reuse the same ColumnIDs but add nulls or mix
columns from different subqueries on the same table.

3) Each pair of columns whose rows are unioned together occupy the same
ordinal positions in the original base table. This ensures that the
output (and inputs) of the UnionAll only contains tuples that existed in
the base table (though it may contain duplicates, and with the exception
of null-extension - see condition #5).

4) The output columns of each of the inputs form a strict key over the base
table. It is not sufficient to use keys directly from the input
expressions because the keys from the input expressions may have dropped
columns due to filtering, when those columns may be necessary to
distinguish rows resulting from the union. Ex: union together the same
(single) row, for which the empty set is a key.

5) There must be at least one key column, since in the empty-key case
null-extension by outer joins can violate the requirement that a given
tuple of values on the key columns implies the same values on all other
columns over both sides. (e.g. for an empty key, the key values would
always be an empty tuple, while the remaining columns could have
different values). Null-extension is allowed when the key is non-empty
because when all columns have the same (NULL) value, grouping on any
subset of them results in one (all-NULL) row. This condition only applies
in the rare case when a table can be statically proven to contain only
one row (see #85502).

6) Finally, the key columns must form a strict subset of the union columns.
This is not strictly necessary for correctness, but the transformation
does not gain anything if the number of columns to de-duplicate on does
not decrease.

The above conditions ensure that the DistinctOn-UnionAll complex is equivalent
to the original Union. This transformation allows less comparisons to be made
in de-duplicating the rows, which can add up to significant speedups when rows
are wide. Cases like this one can be produced by rules like SplitDisjunction
and SplitScanIntoUnionScans, which produce a Union over a series of scans over
the same table.

Extracted from `set.opt` (which defines multiple rules — implement specifically `ConvertUnionToDistinctUnionAll`, not the other rules in that file):

```
# ConvertUnionToDistinctUnionAll replaces a Union with a DistinctOn on top of a
# UnionAll. This is a valid transformation when we can obtain a key over the
# output of the UnionAll that not only functionally determines all columns from
# both inputs, but functionally determines the *same* values from both inputs.
# ConvertUnionToDistinctUnionAll can match when the left and right inputs satisfy
# the following conditions:
#
#    1) All columns from both inputs originate from the same base table. This is
#       necessary because it is safe to de-duplicate over a subset of columns
#       that form a key over the base table (assuming condition #2 is also
#       satisfied).
#
#    2) All columns from a given side originate from the same meta table. This
#       avoids cases where joins reuse the same ColumnIDs but add nulls or mix
#       columns from different subqueries on the same table.
#
#    3) Each pair of columns whose rows are unioned together occupy the same
#       ordinal positions in the original base table. This ensures that the
#       output (and inputs) of the UnionAll only contains tuples that existed in
#       the base table (though it may contain duplicates, and with the exception
#       of null-extension - see condition #5).
#
#    4) The output columns of each of the inputs form a strict key over the base
#       table. It is not sufficient to use keys directly from the input
#       expressions because the keys from the input expressions may have dropped
#       columns due to filtering, when those columns may be necessary to
#       distinguish rows resulting from the union. Ex: union together the same
#       (single) row, for which the empty set is a key.
#
#    5) There must be at least one key column, since in the empty-key case
#       null-extension by outer joins can violate the requirement that a given
#       tuple of values on the key columns implies the same values on all other
#       columns over both sides. (e.g. for an empty key, the key values would
#       always be an empty tuple, while the remaining columns could have
#       different values). Null-extension is allowed when the key is non-empty
#       because when all columns have the same (NULL) value, grouping on any
#       subset of them results in one (all-NULL) row. This condition only applies
#       in the rare case when a table can be statically proven to contain only
#       one row (see #85502).
#
#    6) Finally, the key columns must form a strict subset of the union columns.
#       This is not strictly necessary for correctness, but the transformation
#       does not gain anything if the number of columns to de-duplicate on does
#       not decrease.
#
# The above conditions ensure that the DistinctOn-UnionAll complex is equivalent
# to the original Union. This transformation allows less comparisons to be made
# in de-duplicating the rows, which can add up to significant speedups when rows
# are wide. Cases like this one can be produced by rules like SplitDisjunction
# and SplitScanIntoUnionScans, which produce a Union over a series of scans over
# the same table.
[ConvertUnionToDistinctUnionAll, Normalize]
(Union
    $left:*
    $right:*
    $private:(SetPrivate $leftCols:* $rightCols:* $outCols:*) &
        (Let
            ($keyCols $ok):(CanConvertUnionToDistinctUnionAll
                $leftCols
                $rightCols
            )
            $ok
        )
)
=>
(DistinctOn
    (UnionAll $left $right $private)
    (MakeAggCols
        ConstAgg
        (TranslateColSet
            (DifferenceCols (OutputCols $left) $keyCols)
            $leftCols
            $outCols
        )
    )
    (MakeGrouping
        (TranslateColSet $keyCols $leftCols $outCols)
        (EmptyOrdering)
    )
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's after side is a DistinctOn that dedups on a strict subset of columns (the key) while retaining all output columns, i.e. an arbitrary "ConstAgg" selection of the non-key values per key group, and no operator in QED's prover language has those semantics: the set-family operators (distinct/union/intersect/except) only dedup on the whole row, and GroupBy's non-key outputs are uninterpreted aggregates that QED is told nothing about beyond bag equality of their inputs, so no expression can stand in for the dedup side and be tied to the row values. This gap sits in the fixed prover's JSON operator set — JSONSerializer can carry scan/filter/project/join/correlate/group/union(+distinct)/intersect/except/sort and nothing that serializes a subset-key dedup — rather than in the Java builder layer, so extend_dsl_file cannot bridge it, and any "encoding" that avoids the operator (e.g. set ops on the same bag) would only re-prove a tautology without exercising the key→all-columns functional dependency the rule actually rests on.
