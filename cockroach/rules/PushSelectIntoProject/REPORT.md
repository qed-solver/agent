# PushSelectIntoProject

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** one passthrough input column and one synthesized column, with exactly two filter conjuncts: one bound to the input column (pushed below the Project) and one referencing the synthesized column (kept above); the IsBoundBy/ExtractBoundConditions/ExtractUnboundConditions guards are enforced structurally by which column each conjunct references.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoProject pushes the Select operator into its Project input. This
is typically preferable because it minimizes the number of rows which Project
needs to process. This is especially important if Project is adding expensive
computed columns.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoProject`, not the other rules in that file):

```
# PushSelectIntoProject pushes the Select operator into its Project input. This
# is typically preferable because it minimizes the number of rows which Project
# needs to process. This is especially important if Project is adding expensive
# computed columns.
[PushSelectIntoProject, Normalize]
(Select
    (Project $input:* $projections:* $passthrough:*)
    $filters:[
        ...
        $item:* &
            (IsBoundBy $item $inputCols:(OutputCols $input))
        ...
    ]
)
=>
(Select
    (Project
        (Select
            $input
            (ExtractBoundConditions $filters $inputCols)
        )
        $projections
        $passthrough
    )
    (ExtractUnboundConditions $filters $inputCols)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully instantiates the rule's essential content — a bound conjunct P on the input/passthrough column is pushed below the Project while the unbound conjunct Q on the synthesized column F(a) stays above — with `before()` and `after()` structurally distinct (an extra Filter node and a lowered Project), so the proof is non-vacuous; the fact that QED accepted it (rather than flagging the missing P) also confirms P is genuinely applied below the Project, not silently dropped. The passthrough is correctly the identity field reference `scan.field(0)` (not an uninterpreted function), which is exactly the property that licenses the push-down, and P/Q/F/the table are all genuinely uninterpreted with correct symbol sharing (the bound condition is the same predicate re-applied to the input column, the unbound condition the same predicate on the synthesized column), so there is no coincidental over-constraint. The source rule carries no key/NOT NULL preconditions — only the structural IsBoundBy guard, which is captured by which column each conjunct references — and the `// SCOPE: PARTIAL` line honestly and specifically labels this 1-input-col / 1-synthesized-col / 1-bound / 1-unbound instance, a genuine non-degenerate special case that preserves the rule's core semantics.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5384624
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33271833
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 814417
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 410416
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 15652750
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33352042
  },
  "total_duration": {
    "secs": 0,
    "nanos": 64322833
  }
}
```
