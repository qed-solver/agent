# FoldNullComparisonRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 32  **Verification rounds used:** 2
**Scope detail:** restricts FoldNullComparisonRight to the Eq (equality) comparison branch, folding `left = NULL` to `NULL` for an arbitrary boolean left input; the other ~20 comparison operators in the rule each require a distinct concrete operator


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

FoldNullComparisonRight replaces the comparison operator with null if its
right input is null.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNullComparisonRight`, not the other rules in that file):

```
# FoldNullComparisonRight replaces the comparison operator with null if its
# right input is null.
[FoldNullComparisonRight, Normalize]
(Eq | Ne | Ge | Gt | Le | Lt | Like | NotLike | ILike | NotILike
        | SimilarTo | NotSimilarTo | RegMatch | NotRegMatch
        | RegIMatch | NotRegIMatch | Contains | ContainedBy
        | Overlaps | JsonExists | JsonSomeExists | JsonAllExists
    *
    $right:(Null)
)
=>
(Null (BoolType))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (filter on `EQUALS(left, NULL)`) and `after()` (filter on the bare `NULL` literal) are structurally and semantically distinct, and the proof checks exactly the rule's null-folding content — `x = NULL ≡ NULL` for every instantiation of the uninterpreted left input, including NULL itself — with the NULL constant correctly shared on the right operand and no preconditions in the source rule left out. The restriction to the Eq branch (one of ~23 operators) is genuine: QED cannot derive null propagation for an uninterpreted operator symbol, so each concrete operator would need its own instance (and CockroachDB-specific ones like RegMatch/JsonExists aren't in the prover's model), and it is honestly and specifically declared in the SCOPE line. Embedding the scalar fold in a filter position is the DSL's idiomatic expression of a scalar predicate rule (as in the FilterMerge example), and the encoding is a faithful, non-degenerate special case.

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
    "nanos": 284958
  }
}
```
