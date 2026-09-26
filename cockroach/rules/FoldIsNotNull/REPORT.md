# FoldIsNotNull

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 29  **Verification rounds used:** 2
**Scope detail:** public record FoldIsNotNull() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

FoldIsNotNull replaces NULL IS NOT NULL with False.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldIsNotNull`, not the other rules in that file):

```
# FoldIsNotNull replaces NULL IS NOT NULL with False.
[FoldIsNotNull, Normalize]
(IsNot (Null) (Null))
=>
(False)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful and non-vacuous: `before()` filters on `NULL IS NOT NULL` (a genuine null literal under the real `IS_NOT_NULL` operator, the correct image of Optgen's binary `IsNot (Null) (Null)`) and `after()` filters on a `false` literal — structurally different patterns whose equivalence holds exactly because `NULL IS NOT NULL` evaluates to false, which is the entire content of the rule. There are no free variables or side conditions in the source rule to miss, the null literal's INTEGER type is irrelevant to IS NOT NULL semantics (and virtual types erase to integer in the prover anyway), and the input relation is left uninterpreted, so there is no under-generalization or missing precondition. The filter position is the canonical relational embedding of a scalar boolean constant fold, and since the condition doesn't reference the input, the proved statement (σ_{NULL IS NOT NULL}(R) = σ_false(R) for all R) is equivalent to the scalar claim itself — so `SCOPE: FULL` is honest, not a narrowed special case. ```

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
    "nanos": 334750
  }
}
```
