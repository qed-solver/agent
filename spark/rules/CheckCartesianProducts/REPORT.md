# CheckCartesianProducts

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 11  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3160-3183
```

## Independent verifier review

**Verdict:** AGREE

CheckCartesianProducts is a pure validator — its only effects are identity (plan passes through) or throwing joinConditionMissingOrTrivialError, so there is no successful post-rewrite plan and hence no non-vacuous before/after pair for QED to decide equivalence on; the only encodable candidate would be an identity join ⟹ join, which ignores the rule's content entirely. Compounding this, the rule's trigger is a syntactic property of the predicate term (whether its conjuncts' references span both the left and right output sets), which QED's encoding cannot even inspect, since uninterpreted predicates are opaque boolean functions whose argument-reference sets carry no semantic meaning in the prover. The DSL and JSONSerializer expose only relational operators (scan/filter/project/join/correlate/group/union/intersect/except/sort/values) with no throw/abort/guard node, so the rule's defining behavior is unrepresentable rather than mis-modeled. ```
