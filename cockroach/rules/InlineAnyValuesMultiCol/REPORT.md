# InlineAnyValuesMultiCol

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 71  **Verification rounds used:** 4
**Scope detail:** modeled with a 2-column uninterpreted input relation and a concrete 2-row, 2-column Values; the tuple equality is encoded row-wise as a conjunction of column equalities under a SEMI JOIN, rather than as a general uninterpreted Any/Tuple operator.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

InlineAnyValuesMultiCol converts Any with Values input to AnyScalar.
This version handles the case where there are multiple columns; in this case,
the Values is wrapped into a Project that converts each row to a tuple.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `InlineAnyValuesMultiCol`, not the other rules in that file):

```
# InlineAnyValuesMultiCol converts Any with Values input to AnyScalar.
# This version handles the case where there are multiple columns; in this case,
# the Values is wrapped into a Project that converts each row to a tuple.
[InlineAnyValuesMultiCol, Normalize]
(Any
    (Project
        $values:(Values * $valuesPrivate:*)
        [ (ProjectionsItem $tuple:(Tuple)) ] &
            (IsTupleOfVars $tuple (ValuesCols $valuesPrivate))
        $passthrough:* & (ColsAreEmpty $passthrough)
    )
    $scalar:*
    $private:*
)
=>
(AnyScalar $scalar (InlineValues $values) (SubqueryCmp $private))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core InlineAnyValuesMultiCol transformation (semi-join against a constant multi-row Values inlined into a filter with OR-of-ANDs) in a minimal non-trivial multi-column case; SEMI JOIN correctly models `= ANY`/IN semantics, the 2-column conjunction distinguishes it from the single-col variant, and concrete values are structurally required by the rule (it inlines constant Values, not arbitrary subqueries). The SCOPE tag honestly and specifically acknowledges the fixed arity and equality-only restriction. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 10136501
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 37332208
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1079291
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 467084
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21941583
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 37378958
  },
  "total_duration": {
    "secs": 0,
    "nanos": 74828334
  }
}
```
