# CommuteConstInequality

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3
**Scope detail:** encodes only the Lt flip (x < y becomes y > x); the Le, Gt, and Ge commutation pairs of the original rule are not covered by this single record.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

CommuteConstInequality is similar to CommuteConst (in scalar.opt), except
that it handles inequality comparison operators that need special handling to
commute operands.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `CommuteConstInequality`, not the other rules in that file):

```
# CommuteConstInequality is similar to CommuteConst (in scalar.opt), except
# that it handles inequality comparison operators that need special handling to
# commute operands.
[CommuteConstInequality, Normalize]
(Le | Lt | Ge | Gt $left:(ConstValue) $right:^(ConstValue))
=>
(CommuteInequality (OpName) $left $right)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully lifts the scalar Lt flip into the relational DSL — a maximally-general carrier (INNER cross-join of two unconstrained scans, same type "V", no shared symbols) with `LESS_THAN(x,y)` in before and `GREATER_THAN(y,x)` in after — which is a genuinely non-vacuous SMT check of the exact duality `CommuteInequality` performs (an uninterpreted-operator model would not prove the swapped-argument equivalence). The source rule's "left is a constant / right is not" guard is a firing condition, not a semantic precondition, so the proved equivalence is at least as general as the rule requires, and the missing operator pairs are an inherent limitation of the one-record-per-rule shape (a FULL version would be a family of the four Lt↔Gt / Le↔Ge / Ge↔Le / Gt↔Lt records), making the PARTIAL scope line accurate, specific, and non-degenerate. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7626833
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35989542
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 824875
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 482250
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19300833
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36088958
  },
  "total_duration": {
    "secs": 0,
    "nanos": 71246167
  }
}
```
