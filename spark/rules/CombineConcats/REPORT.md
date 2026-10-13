# CombineConcats

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 1343-1376
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire content is the identity concat(a, concat(b, c)) ≡ concat(a, b, c) (plus the same-shape cast pushdown cast(concat(a,b)) ≡ concat(cast(a), cast(b))), which holds only by string-concatenation associativity; QED translates all generic projection operators (concat, cast) into uninterpreted function symbols over integer-sorted rows with no algebraic axioms, so differently shaped terms like f(f(a,b),c) vs g(a,b,c) are not validly equivalent (a countermodel in the theory of uninterpreted functions always exists, with different arities/nesting being distinct symbols). No non-trivial special case can escape this, since every instance of flattening changes the term's nesting or arity, leaving only degenerate identity shapes — this is precisely the "operator's bespoke internal semantics QED cannot see through as an uninterpreted function" limitation, not a missing DSL capability. ```
