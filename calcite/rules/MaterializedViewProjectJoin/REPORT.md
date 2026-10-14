# MaterializedViewProjectJoin

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 14  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewProjectJoinRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on the planner-maintained invariant that the MV's physical table holds exactly the bag produced by its defining query — a cross-table identity between two independent uninterpreted relations. QED's schema language only carries per-table premises (keys and single-table "guaranteed" check predicates, which are per-tuple first-order properties that cannot even express multiset equality between two tables), and its SMT procedure treats every table as an independent uninterpreted bag, so it can neither assume nor derive that scan(MV) ≡ the defining Project(Join) over base tables. The only encoding that would make the prover succeed is one that inlines the defining query in place of the MV scan, which degenerates the rule into a vacuous identity and no longer expresses materialization; hence no non-vacuous encoding exists (matching the correctly-verified sibling rules). ```
