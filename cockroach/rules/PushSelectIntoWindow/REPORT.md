# PushSelectIntoWindow

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 61  **Verification rounds used:** 4
**Scope detail:** the window functions are modeled as partition-determined uninterpreted aggregates joined back to the input on the partition column (no frame/ordering semantics), with a single partition column, a single window function, a two-column input, one pushed conjunct that references only the partition column (the rule's ColsAreDeterminedBy precondition, encoded by construction), and one remaining conjunct over the window output row.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/window.opt

PushSelectIntoWindow pushes down a Select which can be satisfied by only the
functional closure of the columns being partitioned over. This is valid
because it's "all-or-nothing" - we only entirely eliminate a partition or
don't eliminate it at all.

Extracted from `window.opt` (which defines multiple rules — implement specifically `PushSelectIntoWindow`, not the other rules in that file):

```
# PushSelectIntoWindow pushes down a Select which can be satisfied by only the
# functional closure of the columns being partitioned over. This is valid
# because it's "all-or-nothing" - we only entirely eliminate a partition or
# don't eliminate it at all.
[PushSelectIntoWindow, Normalize]
(Select
    (Window $input:* $fn:* $private:*)
    $filters:[
        ...
        $item:* &
            (ColsAreDeterminedBy
                (OuterCols $item)
                $partitionCols:(WindowPartition $private)
                $input
            )
        ...
    ]
)
=>
(Select
    (Window
        (Select
            $input
            (ExtractDeterminedConditions
                $filters
                $partitionCols
                $input
            )
        )
        $fn
        $private
    )
    (ExtractUndeterminedConditions
        $filters
        $partitionCols
        $input
    )
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The two plans are structurally different — the window's aggregate is computed over the unfiltered input in `before()` but over the P-filtered input in `after()` — and they are equal only because P references only the partition column k, i.e. the rule's ColsAreDeterminedBy precondition encoded by construction, so the proof is of the genuine all-or-nothing pushdown rather than a vacuous identity (a P that could see v would break the equality, since the per-partition aggregate's input bag would differ). Symbols are shared correctly (P, Q, and the w aggregate are the same uninterpreted symbols on both sides; the join-back is an inner equijoin on k that preserves row multiplicity, and Q correctly stays above the window on both sides), and the restrictions — single partition column, single partition-determined window function with no frame/ordering semantics, two-column input, one pushed and one remaining conjunct — are real narrowings of the full Optgen rule but are each specifically named in the SCOPE: PARTIAL line, leaving a non-degenerate, faithful special case. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 12587666
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 48539791
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 939125
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1077375
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 29160042
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 48916583
  },
  "total_duration": {
    "secs": 0,
    "nanos": 94435000
  }
}
```
