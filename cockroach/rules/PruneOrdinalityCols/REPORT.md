# PruneOrdinalityCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 26  **Verification rounds used:** 3
**Scope detail:** the ordinality-generated column is modeled as one uninterpreted function O of the retained input columns, shared by both sides and consumed by the outer projection, so row-position/ordering semantics are not captured


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneOrdinalityCols discards Ordinality input columns that are never used.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneOrdinalityCols`, not the other rules in that file):

```
# PruneOrdinalityCols discards Ordinality input columns that are never used.
[PruneOrdinalityCols, Normalize]
(Project
    (Ordinality $input:* $ordinalityPrivate:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $input
            $needed:(UnionCols3
                (NeededOrdinalityCols $ordinalityPrivate)
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    (Ordinality
        (PruneCols $input $needed)
        (PruneOrderingOrdinality $ordinalityPrivate $needed)
    )
    $projections
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding matches the real rule's shape (outer Project over an Ordinality-derived column, with a column drop pushed beneath the Ordinality) and the symbol sharing is correct: the shared O encodes the rule's own firing precondition that the ordinality derivation references no pruned column, and the shared G encodes the unchanged outer projection, which consumes O so the proof is non-vacuous (QED genuinely verifies that O's values survive the drop and the field-index shift from ordinal 3 to 2 is handled). The remaining narrowness — 3 input columns / exactly 1 pruned, and O abstracted as a value-function of retained columns rather than a true row-position function — stems from QED's fundamental inability to model row position/ordering semantics, not from a fixable encoding choice, and is honestly disclosed in the one-sentence SCOPE line, making this a genuine, non-degenerate special case of the rule.

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
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
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
    "nanos": 334875
  }
}
```
