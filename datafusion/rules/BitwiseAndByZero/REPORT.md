# BitwiseAndByZero

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1229-1235
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests entirely on the algebraic identity of the bitwise-and operator (x & 0 = 0), and QED models scalar operators such as & only as uninterpreted function symbols; its oracle theory (equality, total order, ite, and natural-number addition used solely for bag multiplicity) contains no integer/bitwise arithmetic and no zero constant carrying that property, so f(x,0)=0 is not entailed under any instantiation. This is exactly the "backend operator's bespoke internal semantics" limitation from the reference — and it is fundamental rather than a DSL gap, because even if a numeric `0` constant could be added, the uninterpreted & symbol still has no axiom relating it to zero, so no encoding of the rule could be proved. ```
