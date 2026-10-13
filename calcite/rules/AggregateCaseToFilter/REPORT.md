# AggregateCaseToFilter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateCaseToFilterRule.java
```

## Independent verifier review

**Verdict:** AGREE

QED models aggregates as uninterpreted bag functions and, per its stated limitation, "knows nothing about a specific aggregate function's algebra beyond bag equality of its input," so it can only prove two aggregate calls equal when their input bags are literally identical. This rule's soundness rests exactly on the NULL-identity/NULL-skip property it lacks: the pre-rewrite aggregate consumes `{x : p} ∪ {NULL : ¬p}` while the post-rewrite filtered aggregate consumes `{x : p}`, and those bags differ precisely by the CASE's non-matching NULLs (or the ELSE-0s in the SUM0 styles), an asymmetry no non-trivial special case removes. Hence even closing the (secondary) DSL gap — `AggCall`/`JSONSerializer` currently expose no `FILTER` argument — could not make it provable, because QED still could not equate the two uninterpreted aggregates over non-bag-equal inputs. ```
