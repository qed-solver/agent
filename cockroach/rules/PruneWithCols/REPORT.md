# PruneWithCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** the With's input is fixed to a single column with exactly one uninterpreted projection expression and one passthrough column; the original rule applies at any arity


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneWithCols pushes a Project operator beneath a With. It's ok to
unconditionally push this Project down, since in the pruning case, we're
getting the project closer to the source of any prune requests, and if we
just end up with a Project incidentally, it's safe to just always push it
down.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneWithCols`, not the other rules in that file):

```
# PruneWithCols pushes a Project operator beneath a With. It's ok to
# unconditionally push this Project down, since in the pruning case, we're
# getting the project closer to the source of any prune requests, and if we
# just end up with a Project incidentally, it's safe to just always push it
# down.
[PruneWithCols, Normalize]
(Project
    (With $binding:* $input:* $private:*)
    $projections:*
    $passthrough:*
)
=>
(With
    $binding
    (Project $input $projections $passthrough)
    $private
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors PruneWithCols's shape — before() is Project([Expr(c0), c0]) above the 1-column identity standing in for the pass-through With, after() is the 2-column identity (same With) above the same project pushed below the scan — and the trees are structurally different (Expr outer vs. inner), so the proof is not vacuous. The uninterpreted Expr and scan symbols are correctly shared, the identity modeling of With matches the source rule's own justification, and the PARTIAL scope line accurately and specifically states the 1-column / 1-expression / 1-passthrough restriction, which is a genuine non-degenerate special case limited by the DSL's inability to express arbitrary-length sequences of distinct uninterpreted projections.

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
    "nanos": 314792
  }
}
```
