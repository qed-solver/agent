# EliminateCoalesce

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 28  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateCoalesce discards the Coalesce operator if it has a single operand.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateCoalesce`, not the other rules in that file):

```
# EliminateCoalesce discards the Coalesce operator if it has a single operand.
[EliminateCoalesce, Normalize]
(Coalesce [ $item:* ])
=>
$item
```
```

## Independent verifier review

**Verdict:** AGREE

EliminateCoalesce's validity rests entirely on the scalar identity COALESCE(x) = x, but in RuleScript "Coalesce" is just an uninterpreted named RexCall — QED's prover has no coalesce operator/axiom (not in the paper's semantics nor in the JSON op mapping), and it must prove equivalence for *every* instantiation of uninterpreted symbols, under which f(x) = x is refutable (any non-identity function is a countermodel). No encoding or DSL extension in RelRN/RexRN can inject that identity into the frozen Rust prover, so the observed provable: false reflects a genuine limitation, not a symbol-matching or modeling mistake. ```
