# EliminateSemiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** self semi-join in which both inputs are the same scan and the join filter is constant true, so every left row matches whenever the relation is non-empty


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

EliminateSemiJoin discards a SemiJoin when it's known that all left rows will
be matched by the join filters.

Extracted from `join.opt` (which defines multiple rules — implement specifically `EliminateSemiJoin`, not the other rules in that file):

```
# EliminateSemiJoin discards a SemiJoin when it's known that all left rows will
# be matched by the join filters.
[EliminateSemiJoin, Normalize]
(SemiJoin | SemiJoinApply
    $left:*
    $right:*
    $on:* & (JoinFiltersMatchAllLeftRows $left $right $on)
)
=>
$left
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a genuine, non-vacuous instance of EliminateSemiJoin — the SEMI join kind and the `=> $left` conclusion match the source rule, and `before()` (self-semijoin on constant-true) is structurally distinct from `after()`, so the proof exercises real semijoin/emptiness semantics rather than a structural identity. The self-shared scan plus true filter is what makes the source rule's `JoinFiltersMatchAllLeftRows` precondition hold by plan structure alone (non-empty: every left row matches its own copy in the identical right input; empty: both sides are trivially empty), and this narrowing is legitimate because QED cannot express that precondition for independent left/right relations or an uninterpreted filter — it performs no predicate entailment or data-property reasoning, so the fully general rule is unprovable here for reasons inherent to the prover, not a missing builder. The `// SCOPE: PARTIAL` tag states the assumption specifically and accurately, so the provable result is honest: a real, if narrow, special case of the rule rather than a disguised or vacuous one.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7362250
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34949459
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1223291
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 387000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19669709
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35082958
  },
  "total_duration": {
    "secs": 0,
    "nanos": 70274792
  }
}
```
