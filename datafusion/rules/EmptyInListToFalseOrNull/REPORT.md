# EmptyInListToFalseOrNull

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 26  **Verification rounds used:** 3
**Scope detail:** encodes only the IN direction (x IN () -> false) as a semi-join of the left side with a zero-row right side collapsing to the empty relation of the left side's schema, because the DSL has no scalar InList predicate or list constant (the NOT IN direction is the sibling anti-join rule).


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1814-1819
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (SEMI join of an uninterpreted L with a zero-row right side under an uninterpreted condition) and after() (the zero-row relation of L's schema) are structurally distinct, and their equivalence is exactly the bag-level content of `x IN () -> false`: no left row can match a value set containing no rows, and since L, the join condition, and both column types are all uninterpreted, the proof covers every left input and every match condition, subsuming the actual equality condition of a real IN. No precondition is silently missing — the empty-list case has no NULL subtlety (the empty disjunction is false even for a NULL x, and an empty right side makes the semi-join condition moot), so the relational statement is not an easier claim than the source rule. The only narrowing is that the negated branch (NOT IN () -> true, the sibling ANTI(L, ∅) = L rule) is not included, and that restriction is genuine (the DSL has no scalar InList predicate or list constants, and QED models no list semantics) and honestly tagged PARTIAL with a specific condition. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 117583
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
    "nanos": 815708
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 278500
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 9547375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 25380875
  }
}
```
