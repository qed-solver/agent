# FoldZeroPlus

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 50  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/numeric.opt

FoldZeroPlus folds 0 + $right for numeric types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldZeroPlus`, not the other rules in that file):

```
# FoldZeroPlus folds 0 + $right for numeric types.
[FoldZeroPlus, Normalize]
(Plus $left:(Const 0) $right:*)
=>
(Cast $right (BinaryType Plus $left $right))
```
```

## Independent verifier review

**Verdict:** AGREE

The rule rewrites the scalar term `PLUS(0, x)` into `CAST(x)`, but both operators are introduced by name as uninterpreted function symbols, and QED's provability requires bag equivalence under *every* instantiation of those symbols — an instantiation with `plus(0,x)=x+1`, `cast(x)=x` refutes the equality, so no SMT reasoning can derive `f(0,x)=g(x)` for independent uninterpreted f and g. This is a fundamental prover limitation, not a DSL gap: the language already exposes arbitrary-arity scalar ops (a multi-source `RexRN.Proj`), constants, and projections, so a faithful encoding (scan one column x; before `project[PLUS(0, x)]`, after `project[CAST(x)]`) would compile, but extending the DSL cannot inject an additive-identity/cast axiom because the QED prover is the unchanging arbiter and its JSON input only carries table-level uninterpreted "guaranteed" predicates, which cannot establish `z = 0` or cast semantics either. The rule's correctness genuinely rests on numeric additive identity and cast semantics — internal operator semantics QED is expressly unable to see through. ```
