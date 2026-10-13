# SimplifyAndFalse

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** public record SimplifyAndFalse() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyAndFalse maps the And operator to False if its right input is False.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyAndFalse`, not the other rules in that file):

```
# SimplifyAndFalse maps the And operator to False if its right input is False.
[SimplifyAndFalse, Normalize]
(And * $right:(False))
=>
$right
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures `(And * (False)) => (False)`: the left operand is a fully uninterpreted predicate (universally quantified over all instantiations, which subsumes any boolean expression the `*` could match), the right operand is correctly hard-coded as the literal `False` because the rule requires the syntactic False constant, and `before()`/`after()` genuinely differ (the And conjunct is eliminated), so the proof is non-vacuous. Embedding the boolean identity in filter position over an arbitrary, unconstrained scan is the standard semantics-preserving translation in this DSL (same treatment as the FilterMerge worked example), and in QED's two-valued semantics the proven statement `∀R, f: filter(And(f,⊥)) = filter(⊥)` is exactly equivalent to the pointwise expression equality `And(x, False) ≡ False` for all x — no keys, NOT NULLs, or other preconditions are assumed that the unguarded source rule does not itself impose, and the single symbol `left` is shared only where the source pattern requires. Hence `// SCOPE: FULL` is honest: no assumption anywhere narrows the family beyond the canonical relational witness of the expression rule.

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
    "nanos": 284458
  }
}
```
