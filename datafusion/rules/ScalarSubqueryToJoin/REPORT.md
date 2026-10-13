# ScalarSubqueryToJoin

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 114  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/scalar_subquery_to_join.rs, lines 1-437
```

## Independent verifier review

**Verdict:** AGREE

The scalar-subquery → LEFT JOIN rewrite is only valid because a global (groupless) aggregate over an *empty* input yields the same value the LEFT JOIN injects as its synthetic right-side NULL row (and DataFusion's own `CASE ... IS NULL` "count-bug" compensation exists precisely to recover non-NULL empty-input values like COUNT), so the proof depends on the aggregate's value at the empty multiset and on the groupless aggregate being a singleton. QED models every aggregate as an uninterpreted function of its input bag and "knows nothing about a specific aggregate function's algebra beyond bag equality of its input," so it cannot relate `agg(∅)` to the join's NULL — confirmed by the SMT solver actually running (~36 ms) and refuting with a concrete unmatched-row counterexample. This is a genuine limitation of the prover's aggregate-algebra/cardinality reasoning, not a missing DSL builder; adding a "constrain to one row" operator would not close the gap because the blocker is the uninterpreted aggregate's unknown empty-input value.
