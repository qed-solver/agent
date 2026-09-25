# MinusToFilter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MinusToFilterRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rewrite MINUS(base, Filter(Q,base)) → distinct(Filter(NOT Q,base)) is valid only when the right-hand predicate Q is total, because under QED's 3-valued SQL semantics a row with Q=NULL is not in Filter(Q,·) and therefore survives the set-difference MINUS, while NOT Q evaluates to NULL (not TRUE) and is dropped by the rewritten filter. QED decides equivalence by quantifying over every instantiation of the uninterpreted symbol Q, so this NULL interpretation is a genuine counterexample the prover must report, not a sampling artifact. The DSL introduces all filter predicates as uninterpreted symbols with a hardcoded return type and provides no way to declare one total, so no non-degenerate special case closes the gap and the rule as stated is genuinely outside what QED can prove.
