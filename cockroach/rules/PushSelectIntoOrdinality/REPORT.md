# PushSelectIntoOrdinality

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 46  **Verification rounds used:** 3
**Scope detail:** the ordinality-generated column is modeled as one shared uninterpreted function O of the input columns (the ForDuplicateRemoval case, where O's values don't matter) and every filter conjunct references only input columns (no unbound conjunct over O, so the outer Select is vacuous).


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoOrdinality pushes the Select operator into its Ordinality input
if the Ordinality operation was built for the purposes of removing duplicate
rows, and the actual values returned by the Ordinality operation don't matter.
This is typically preferable because it allows the Select to be pushed into
operations beneath the Ordinality, minimizing the number of rows subsequent
operations need to process and potentially pushing the Select down far enough
to enable constrained scans. This may also enable other normalization rules
which match on Select expressions to fire.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoOrdinality`, not the other rules in that file):

```
# PushSelectIntoOrdinality pushes the Select operator into its Ordinality input
# if the Ordinality operation was built for the purposes of removing duplicate
# rows, and the actual values returned by the Ordinality operation don't matter.
# This is typically preferable because it allows the Select to be pushed into
# operations beneath the Ordinality, minimizing the number of rows subsequent
# operations need to process and potentially pushing the Select down far enough
# to enable constrained scans. This may also enable other normalization rules
# which match on Select expressions to fire.
[PushSelectIntoOrdinality, Normalize]
(Select
    (Ordinality $input:* $private:*) &
        (ForDuplicateRemoval $private)
    $filters:[
        ...
        $item:* &
            (IsBoundBy $item $inputCols:(OutputCols $input))
        ...
    ]
)
=>
(Select
    (Ordinality
        (Select
            $input
            (ExtractBoundConditions $filters $inputCols)
        )
        $private
    )
    (ExtractUnboundConditions $filters $inputCols)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors the source rule's core transformation — before() is `Select(Ordinality(input), P(input))` and after() is `Ordinality(Select(input, P))` — with the exact right relational shape (projection standing in for the ForDuplicateRemoval ordinality, filter pushed from above the projection to below it), uninterpreted symbols throughout (O as a shared function of the input row, P as a shared uninterpreted predicate on the input column, so no concrete predicate/join-type/column-count is baked in), and correct symbol sharing (O and P are the same logical quantity re-derived on both sides, as the rule requires). before() and after() are genuinely structurally different (Filter-Project-Scan vs. Project-Filter-Scan), so the proof is non-vacuous, and the shared O symbol is a sound over-constraint rather than a spurious one: it encodes precisely the property the rule's ForDuplicateRemoval comment relies on (the ordinality column's values don't matter / are stably re-derived from the input), without which the pushdown would not hold. The only narrowing — all filter conjuncts are bound by the input columns (so ExtractUnboundConditions is empty and the vacuous outer Select is omitted, consistent with EliminateSelect) — is exactly what the SCOPE tag states, leaving the result a genuine, useful special case of the real rule (the case that yields the maximal pushdown) rather than a degenerate or mis-shaped one. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6178624
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34276875
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 857375
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 460167
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 17534000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34397166
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67693667
  }
}
```
