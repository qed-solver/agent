# RemoveRedundantAliases

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 10  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 657-838
```

## Independent verifier review

**Verdict:** AGREE

QED's model is positional bag semantics — every field reference serializes to {"column": <int>} over uninterpreted integer-typed values, so it has no notion of column names or attribute identity, and the Spark rule's two load-bearing preconditions (the `name == attr.name` schema-preservation gate, and the exclusion sets that prevent duplicate attribute names across join/union inputs — a name-collision/well-formedness property, not a row-multiset property) are inexpressible in it. In RuleScript, `Alias(attr, name)` and bare `attr` both encode as the identical positional projection, so any encoding collapses to a vacuous P = P that discards the rule's actual preconditions. This is a fundamental limitation of QED's semantic model (the prover's JSON has no name-identity channel and the prover itself is off-limits), not a fixable DSL gap, so no narrower encodable special case of this rule exists. ```
