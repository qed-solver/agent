# CalcToWindow

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 61  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectToWindowRule.java

Note: ProjectToWindowRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `CalcToWindowRule` variant (not ProjectToLogicalProjectAndWindowRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire semantic content is the evaluation semantics of the window operator (a Calc/Project containing a `RexOver` being equal to a pipeline containing a `LogicalWindow`), and window results are inherently order- and frame-dependent (partition keys, collation, ROWS/RANGE frames, functions like LAG/LEAD/ROW_NUMBER), whereas QED's bag-semantic SMT model has no notion of `Window` or any list/ordering operator and the Rust prover is the fixed, unmodifiable arbiter. Extending the DSL (a `RexOver` scalar builder, a `Window` rel builder, a `JSONSerializer` case) could at best emit a node kind QED has no semantics for, and encoding the window as an uninterpreted symbol or as a join-with-group-aggregate reduction would make the claim either vacuous (both sides sharing one opaque symbol) or a tautology, so the porter's UNSUPPORTED conclusion is correct.
