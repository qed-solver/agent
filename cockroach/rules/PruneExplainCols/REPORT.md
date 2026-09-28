# PruneExplainCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** Explain's input has exactly two columns of which only the first is needed by the fixed explain physical properties, and Explain's output rows are uninterpreted functions of exactly that needed column


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneExplainCols discards Explain input columns that are never used by its
required physical properties.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneExplainCols`, not the other rules in that file):

```
# PruneExplainCols discards Explain input columns that are never used by its
# required physical properties.
[PruneExplainCols, Normalize]
(Explain
    $input:*
    $explainPrivate:* &
        (CanPruneCols
            $input
            $needed:(NeededExplainCols $explainPrivate)
        )
)
=>
(Explain (PruneCols $input $needed) $explainPrivate)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding matches PruneExplainCols exactly in shape — `Explain(input)` ⟹ `Explain(PruneCols(input, needed))` with no wrapping Project (unlike the sibling Prune* rules), modeled as an invariant outer operator `Proj(E)` over the input with a projection of the needed column pushed beneath it in `after()`. Modeling Explain as the uninterpreted per-row function `E` of only the needed column faithfully captures the rule's sole validity condition (the output depends only on the needed columns, whose bag is identical before/after), and the proof is genuinely non-trivial: `before()` and `after()` are structurally different and equal only because `E` ignores the pruned column. The only restriction — a fixed 2-column input with 1 needed column and 1 output column — is forced by the DSL's lack of variable arity (not an avoidable hard-coding), is accurately and specifically disclosed in the SCOPE line, and leaves a non-degenerate, useful rewrite. ```

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
    "nanos": 511708
  }
}
```
