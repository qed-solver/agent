# EliminateEnsureDistinctNoColumns

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 45  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateEnsureDistinctNoColumns is similar to EliminateDistinctNoColumns,
except that Max1Row will raise an error if there are no grouping columns and
the input has more than one row. No grouping columns means there is at most
one group. And the Max1Row operator is needed to raise an error if that group
has more than one row, which is a requirement of the EnsureDistinct and
EnsureUpsertDistinct operators.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateEnsureDistinctNoColumns`, not the other rules in that file):

```
# EliminateEnsureDistinctNoColumns is similar to EliminateDistinctNoColumns,
# except that Max1Row will raise an error if there are no grouping columns and
# the input has more than one row. No grouping columns means there is at most
# one group. And the Max1Row operator is needed to raise an error if that group
# has more than one row, which is a requirement of the EnsureDistinct and
# EnsureUpsertDistinct operators.
[EliminateEnsureDistinctNoColumns, Normalize]
(EnsureDistinctOn | EnsureUpsertDistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* & (HasNoGroupingCols $groupingPrivate)
)
=>
(ConstructProjectionFromDistinctOn
    (Max1Row $input (ErrorOnDup $groupingPrivate))
    (MakeEmptyColSet)
    $aggregations
)
```
```

## Independent verifier review

**Verdict:** AGREE

Both sides of this rule are custom operators whose correctness rests on runtime error semantics — EnsureDistinctOn/EnsureUpsertDistinctOn with an empty grouping col-set and Max1Row(ErrorOnDup) each *raise an error* when the input has more than one row — and QED decides total bag equivalence with no notion of errors, no Max1Row/Limit-style cardinality-cap operator in the core language, and no uninterpreted relational operator to stand in for either side. The only near-candidate encoding, a keyless group-by, diverges on empty input (it always yields one row, Max1Row yields zero), and the language offers no way to constrain an uninterpreted input to at most one row (scan `unique`/key marks column uniqueness, not row count), so the error condition that is the entire point of the "Ensure" variant can neither be expressed nor proven, even as a special case. ```
