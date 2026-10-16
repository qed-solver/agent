# EliminateExistsProject

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** uncorrelated EXISTS only (modeled as a left-semi join on a constant-true condition), with a single uninterpreted projection expression


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateExistsProject discards a Project input to the Exists operator. The
Project operator never changes the row cardinality of its input, and row
cardinality is the only thing that Exists cares about, so Project is a no-op.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateExistsProject`, not the other rules in that file):

```
# EliminateExistsProject discards a Project input to the Exists operator. The
# Project operator never changes the row cardinality of its input, and row
# cardinality is the only thing that Exists cares about, so Project is a no-op.
[EliminateExistsProject, Normalize]
(Exists (Project $input:*) $existsPrivate:*)
=>
(Exists $input $existsPrivate)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's core semantic — a Project is a no-op under EXISTS because it preserves non-emptiness — by modeling an uncorrelated EXISTS as a left-semi-join of a fresh outer relation against the subquery on a constant-true condition; the semi-join-with-true keeps each outer row iff the subquery is non-empty, and because `outer` is a free uninterpreted symbol, the equivalence is forced to reduce exactly to nonempty(Project(input)) ⟺ nonempty(input), which is precisely why the real rule is valid. The `before()`/`after()` genuinely differ by the Project node (non-vacuous), the operators and symbol sharing are correct (SEMI for existence semantics, distinct outer/input/projection symbols, no baked-in predicates), and the SCOPE: PARTIAL tag is honest and specific — the only narrowing is the uncorrelated/single-projection case, which is a genuine and non-degenerate special case (the correlated case needs a non-column-free join condition whose column references would shift when the Project is removed, making it hard to express in this DSL).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8535376
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36228667
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1114625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 524292
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21993000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36344333
  },
  "total_duration": {
    "secs": 0,
    "nanos": 74269833
  }
}
```
