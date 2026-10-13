# FoldNonNullIsNull

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 3
**Scope detail:** public record FoldNonNullIsNull() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

FoldNonNullIsNull replaces x IS NULL with False where x is a non-Null constant.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNonNullIsNull`, not the other rules in that file):

```
# FoldNonNullIsNull replaces x IS NULL with False where x is a non-Null constant.
[FoldNonNullIsNull, Normalize]
(Is $left:(IsNeverNull $left) (Null))
=>
(False)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (filter on `x IS NULL`) and `after()` (filter on the `False` literal) are structurally different, and the equivalence can hold only because the single uninterpreted source column is declared non-nullable — exactly the source rule's `IsNeverNull` side condition, expressed via the DSL's only mechanism for "never null" (a non-nullable `VarType`), so the proof is of the real claim, not a vacuous one. The table (`Source`) and value domain (`X_Type`) remain uninterpreted, the `IS_NULL`/`False` operators match `Is … (Null) => (False)`, and the shared scan symbol is correct, so the encoding is the full relational closure of the scalar rule and the `// SCOPE: FULL` tag is honest.

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
    "nanos": 373333
  }
}
```
