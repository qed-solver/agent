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

The rule's entire content is naming/metadata semantics: its guard (`name == attr.name`), its purpose (preserving output schema names, incl. the AUTO_GENERATED_ALIAS metadata case), and all its guardrails (join self-reference excludes, Union's first child, subquery references) manage Spark attribute identity/exprIds — i.e. names, not values — while at the value level an `Alias` over a bare attribute is just a one-column passthrough projection, so before/after are trivially value-identical and the only thing QED could prove (identity-projection elimination) is a tautological consequence of projection semantics rather than non-trivial content of this rule. QED's model is name-erased bag semantics (every `RelType.VarType` serializes as "INTEGER" and the query JSON carries no output-column names or metadata at all), so the
