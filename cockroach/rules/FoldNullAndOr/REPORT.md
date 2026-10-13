# FoldNullAndOr

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3
**Scope detail:** only the And branch of the rule (And(Null,Null) => Null); the Or branch is structurally identical but not expressible in a single before()/after() pair


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

FoldNullAndOr replaces the operator with null if both operands are null.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `FoldNullAndOr`, not the other rules in that file):

```
# FoldNullAndOr replaces the operator with null if both operands are null.
[FoldNullAndOr, Normalize]
(And | Or (Null) (Null))
=>
(Null (BoolType))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the And branch of FoldNullAndOr: the hard-coded null literal is correct because the source rule matches the Null *constant* (not an uninterpreted operand), reusing one symbol for both operands is right since the rule requires both to be the same constant, and before()/after() are structurally and semantically distinct (an AND call vs. a bare literal), so the proof is non-vacuous; filter-level embedding of a scalar boolean identity follows the DSL's established convention (cf. FilterMerge), and the source rule carries no side conditions (unlike NegateComparison) that the encoding omits. The PARTIAL scope tag is honest and specific — the only narrowing is the absent Or branch (which is expressible as a separate instance via `new RexRN.Or(Seq.of(nullLit, nullLit))`), and the covered instance remains a genuine, useful, non-degenerate rule.

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
    "nanos": 316667
  }
}
```
