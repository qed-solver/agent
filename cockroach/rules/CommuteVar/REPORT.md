# CommuteVar

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** only the Eq variant is covered: on a single-column scan, the Variable operand is the column x and the non-Variable operand is the uninterpreted expression e of the same type, proving the universal law e = x <-> x = e under three-valued semantics; the Ne/Is/IsNot/Plus/Mult/Bit*/Vector* variants remain out of reach because QED cannot know commutativity for uninterpreted operators


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

CommuteVar ensures that variable references are on the left side of
commutative comparison and binary operators. Other patterns don't need to
handle both combinations.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `CommuteVar`, not the other rules in that file):

```
# CommuteVar ensures that variable references are on the left side of
# commutative comparison and binary operators. Other patterns don't need to
# handle both combinations.
[CommuteVar, Normalize]
(Eq | Ne | Is | IsNot | Plus | Mult | Bitand | Bitor | Bitxor
        | VectorDistance | VectorCosDistance
        | VectorNegInnerProduct
    $left:^(Variable)
    $right:(Variable)
)
=>
((OpName) $right $left)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous proof of the Eq fragment of CommuteVar: `before()` and `after()` are structurally different (concrete `EQUALS` operand order swapped), the operand shapes match the rule's contract exactly (uninterpreted non-variable expression `e` on the left vs. column `x` — the "Variable" — on the right, over a standard filter embedding of the scalar rewrite), and using the concrete `=` operator is precisely what makes the claim checkable, since QED correctly refuses to assume commutativity of uninterpreted operators (which is a genuine prover limitation, not a DSL gap that `extend_dsl_file` could fix). The missing preconditions check passes (no PK/NOT NULL assumptions; nullable `VarType` keeps three-valued semantics in play, and `=` is symmetric in it), there are no over-constraining symbol-sharing errors (the same `e` and `x` are correctly shared between both sides), and the SCOPE line is honest and specific about the Eq-only narrowing — a genuine, useful, non-degenerate special case, notably the exact variant that `InlineExistsSelectTuple` in the same source file relies on ("CommuteVar ensures that the variable is on the left" above an `Eq` pattern).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5955165
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36567000
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1332542
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 281459
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16389458
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36670167
  },
  "total_duration": {
    "secs": 0,
    "nanos": 68633791
  }
}
```
