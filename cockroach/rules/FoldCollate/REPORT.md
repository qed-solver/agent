# FoldCollate

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

FoldCollate converts a Collate expr over an uncollated string into a collated
string constant.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `FoldCollate`, not the other rules in that file):

```
# FoldCollate converts a Collate expr over an uncollated string into a collated
# string constant.
[FoldCollate, Normalize]
(Collate $input:(Const) $locale:*)
=>
(CastToCollatedString $input $locale)
```
```

## Independent verifier review

**Verdict:** AGREE

FoldCollate's correctness rests on the value identity `collate(const, locale) = castToCollatedString(const, locale)`, but QED models both scalar operators as distinct uninterpreted functions over type-erased (INTEGER) terms, and the SMT solver is free to assign them different values at the same argument point, so no model of the encoding makes the two sides universally equal and the identity is unprovable. The only encodable variant — sharing a single uninterpreted symbol on both sides — degenerates into a vacuous `f(x) = f(x)` identity that no longer expresses the backend rule (and narrowing to `Const` inputs changes nothing, since `f(0,l) = g(0,l)` between distinct uninterpreted functions is still undecidable-true only by axiom), and `extend_dsl_file` can add operators/shapes but cannot add prover axioms about uninterpreted operators — the same fundamental gap for which the structurally identical FoldCast precedent was independently verified SKIPPED. ```
