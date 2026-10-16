# FoldInNull

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** covers only the In alternative of the rule (a single before/after pair cannot express the In | NotIn disjunction).


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

FoldInNull replaces the In/Not operator with Null when the tuple only
contains null. The NormalizeInConst pattern will reduce multiple nulls to a
single null when it removes duplicates, so this pattern will match that.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `FoldInNull`, not the other rules in that file):

```
# FoldInNull replaces the In/Not operator with Null when the tuple only
# contains null. The NormalizeInConst pattern will reduce multiple nulls to a
# single null when it removes duplicates, so this pattern will match that.
[FoldInNull, Normalize]
(In | NotIn $left:* (Tuple [ (Null) ]))
=>
(Null (BoolType))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is the filter-context relational instance of the In alternative, using the canonical desugaring `x IN (NULL)` ≡ `x = NULL` (the exact form SimplifyInSingleElement in the same file produces), with correct types (nullV typed as the column type V, and the RHS a BOOLEAN null matching `(Null (BoolType))`), no preconditions or symbol-sharing errors, and structurally distinct before/after plans whose equivalence (equality with NULL is never true, so both filters yield the empty bag for every instantiation) is a genuine, non-vacuous proof. The omission of the NotIn alternative is a real DSL limitation (one before/after pair cannot express the In | NotIn disjunction), is honestly and specifically tagged in the SCOPE line, and the resulting rule remains a non-degenerate special case of the original.

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
    "nanos": 278584
  }
}
```
