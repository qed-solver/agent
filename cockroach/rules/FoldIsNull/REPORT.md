# FoldIsNull

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** public record FoldIsNull() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

FoldIsNull replaces NULL IS NULL with True.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldIsNull`, not the other rules in that file):

```
# FoldIsNull replaces NULL IS NULL with True.
[FoldIsNull, Normalize]
(Is (Null) (Null))
=>
(True)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The source rule is the scalar constant-fold `NULL IS NULL => True`, a context-free boolean identity, and the porter embedded it in the canonical relational home for a boolean predicate (a filter), proving `Filter(NULL IS NULL, S) = Filter(TRUE, S)` over a fully uninterpreted scan `S`. Because the identity is context-free, this establishes the rule's complete logical content with no assumptions: CockroachDB's two-arg `Is(Null,Null)` is correctly rendered as unary `IS_NULL(NULL)`, the result as `trueLiteral()`, `before()`/`after()` are genuinely distinct (so the proof is non-vacuous and requires QED to recognize `IS_NULL(NULL)` as true), and there are no uninterpreted symbols to under-generalize, no missing preconditions, and no spurious relational narrowing — so `SCOPE: FULL` is honest. ```

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
    "nanos": 83708
  }
}
```
