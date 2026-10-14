# ProjectOverSumToSum0

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectOverSumToSum0Rule.java
```

## Independent verifier review

**Verdict:** AGREE

The rewrite operates entirely on window aggregates (`SUM(x) OVER (frame)` → `SUM0(x) OVER (frame)`), and QED has no bag-semantic model for `Window` (listed with Sort/Limit/Offset/Sample), with no window node in its JSON format per `JSONSerializer.java` — a genuine prover-level gap no `extend_dsl_file` change can close, since the Rust prover is the fixed arbiter. Even abstracting away the OVER, `SUM` and `SUM0` would be two distinct uninterpreted aggregate symbols QED cannot algebraically relate, and the standalone rewrite is not universally valid in the first place: on an empty/all-NULL frame SUM yields NULL while SUM0 yields 0, a difference Calcite only reconciles through its SUM0-lowering (CASE/COALESCE) chain outside this rule's scope — so no honest encoding could be provable. ```
