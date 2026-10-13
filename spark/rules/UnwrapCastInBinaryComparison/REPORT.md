# UnwrapCastInBinaryComparison

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 32  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/UnwrapCastInBinaryComparison.scala, lines 102-522
```

## Independent verifier review

**Verdict:** AGREE

QED's fixed prover models casts as uninterpreted functions and comparison predicates as uninterpreted operators, with no axioms for cast injectivity/losslessness, round-trip preservation, value ranges, or date/timestamp floor behavior — and every non-trivial branch of Spark's UnwrapCastInBinaryComparison (moving the cast onto the literal side, collapsing out-of-range comparisons to true/false, the date<->timestamp floor rewrites, and In/InSet round-trip filtering) is valid precisely because of those cast axioms, so the equivalence is not a bag-semantic tautology under free interpretation. This is a fundamental prover limitation, not a DSL gap: extending RelRN/RexRN to expose a cast builder would only add another uninterpreted function, and the Rust prover cannot be modified to reason about cast algebra, so no encoding or narrower special case can be proved. The porter's probe (`Cast(x,T)=v -> x=v` not provable) and its citation of qed.pdf's "solvers do not reason about type casting" are consistent with this, so the UNSUPPORTED conclusion is sound. ```
