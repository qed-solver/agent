# MarkSingleTaskExecution

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 14  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/MarkSingleTaskExecution.scala, lines 49-177
```

## Independent verifier review

**Verdict:** AGREE

MarkSingleTaskExecution's only effect is setting a `TreeNodeTag` planning hint on a private clone — it transforms zero rows, and RuleScript's core language has no representation for plan metadata/annotations, so the tagged and untagged plans serialize to identical Q-expressions. QED decides bag-semantic equivalence of output rows and cannot model the config flags, the LocalRelation row-count bounds, or the physical planner behavior that give the tag its meaning, so the only encodable form is a vacuous `before()==after()` identity with nothing genuine to prove. This is a fundamental mismatch between a physical planning-hint rule and a logical bag-equivalence prover, not a DSL API gap that `extend_dsl_file` could close (any tag carrier would have to map to a semantically neutral RelNode, yielding the same vacuity).
