# SimplifyUdafToScalar

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1677-1684
```

## Independent verifier review

**Verdict:** AGREE

The cited lines are a regex-predicate dispatch (`simplify_regex_expr` behind a regex-compilation planning-budget guard), not a UDAF hook, so the porter's aggregate-lifting explanation misread the source — but the conclusion holds: any concrete rewrite this dispatch performs (constant-folding a literal regex match, or relating the regex operator to other operators or equivalent patterns) rests on the regex engine's internal pattern-matching semantics, which QED cannot model because it treats scalar operators as uninterpreted symbols and cannot reason about entailment between independent symbols. No DSL extension can close that gap — it is a fundamental prover limitation, not a missing operator. ```
