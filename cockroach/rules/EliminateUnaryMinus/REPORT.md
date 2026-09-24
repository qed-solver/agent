# EliminateUnaryMinus

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 20  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/numeric.opt

EliminateUnaryMinus discards a doubled UnaryMinus operator.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `EliminateUnaryMinus`, not the other rules in that file):

```
# EliminateUnaryMinus discards a doubled UnaryMinus operator.
[EliminateUnaryMinus, Normalize]
(UnaryMinus (UnaryMinus $input:*))
=>
$input
```
```

## Independent verifier review

**Verdict:** AGREE

EliminateUnaryMinus is a pure scalar algebraic identity — numeric negation being an involution, −(−x) = x — and the only faithful encoding in RuleScript is a projection operator by name (e.g. scan → project("neg", project("neg", x)) ≡ x), which QED models as an uninterpreted function with no involution axiom, so SMT refutes it with a countermodel where "neg" is an arbitrary non-involutive function. That property is exactly a backend operator's bespoke internal semantics, which lives in the Rust prover's theory (off-limits) and cannot be supplied by any Java DSL extension; asserting f(f(x))=x as a table "guaranteed" constraint would merely assume the conclusion, and a boolean Not(Not(p))≡p encoding would be a different rule, not a special case of numeric negation. ```
