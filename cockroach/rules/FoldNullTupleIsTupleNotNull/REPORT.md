# FoldNullTupleIsTupleNotNull

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** the tuple is fixed to arity 2 with exactly one element being the constant NULL (satisfying the side condition HasNullElement) and the other element an arbitrary nullable value.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

FoldNullTupleIsTupleNotNull replaces x IS NOT NULL with False if x is a tuple
with at least one constant, null element.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNullTupleIsTupleNotNull`, not the other rules in that file):

```
# FoldNullTupleIsTupleNotNull replaces x IS NOT NULL with False if x is a tuple
# with at least one constant, null element.
[FoldNullTupleIsTupleNotNull, Normalize]
(IsTupleNotNull $input:(Tuple) & (HasNullElement $input))
=>
(False)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and on-point: before() carries the real condition AND(IS NOT NULL(NULL), IS NOT NULL(x)) while after() carries the constant FALSE, so the proof establishes exactly the scalar fold the rule states, and the element-conjunction semantics used for IsTupleNotNull is the one forced by all four tuple-folding rules in comp.opt (a constant NULL element makes one conjunct — hence the conjunction — constant FALSE). The arity-2, one-constant-NULL restriction is a genuine, specifically-stated PARTIAL special case rather than a modeling accident: the remaining element is a completely arbitrary nullable column of an uninterpreted table (the maximal generality the DSL can give a free value), and the rule's soundness at any arity rests solely on that single constant-false conjunct, so no additional generality is being hidden; no preconditions are missing (HasNullElement is baked in as the NULL literal) and no symbol is improperly shared or over-constrained. ```

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
    "nanos": 306292
  }
}
```
