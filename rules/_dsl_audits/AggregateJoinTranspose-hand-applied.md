# DSL extension audit — triggered by `AggregateJoinTranspose-hand-applied`

**Files changed:** RelRN.java
**Reason given by porter:** Hand-applied by the harness operator after the small model repeatedly crashed on context-length exhaustion across all 5 verification rounds without ever reaching a try_rule call; the independent verifier's DISAGREE on the resulting FAILED outcome identified a concrete, achievable PARTIAL-scope encoding (DEFAULT config: group-by-only, no agg functions, spanning both join sides) that only needed an additive multi-column scan constructor, which QedTable already supported but RelRN never exposed.
**Final outcome:** kept

## Mechanical regression re-proof (fresh, real qed-prover)

Re-checked 27 rule(s) proved before this change.

| Rule | Still provable? |
|---|---|
| `AggregateExpandDistinctAggregates` | ✅ yes |
| `AggregateExtractProject` | ✅ yes |
| `AggregateFilterTranspose` | ✅ yes |
| `AggregateJoinRemove` | ✅ yes |
| `AggregateMerge` | ✅ yes |
| `AggregateProjectMerge` | ✅ yes |
| `AggregateProjectPullUpConstants` | ✅ yes |
| `AggregateRemove` | ✅ yes |
| `AggregateValues` | ✅ yes |
| `FilterAggregateTranspose` | ✅ yes |
| `FilterJoin` | ✅ yes |
| `FilterMerge` | ✅ yes |
| `FilterProjectTranspose` | ✅ yes |
| `FilterRemoveIsNotDistinctFrom` | ✅ yes |
| `FilterSetOpTranspose` | ✅ yes |
| `JoinAssociate` | ✅ yes |
| `JoinCommute` | ✅ yes |
| `JoinExtractFilter` | ✅ yes |
| `JoinPushExpressions` | ✅ yes |
| `JoinPushThroughJoin` | ✅ yes |
| `ProjectJoinTranspose` | ✅ yes |
| `ProjectMerge` | ✅ yes |
| `ProjectRemove` | ✅ yes |
| `ProjectSetOpTranspose` | ✅ yes |
| `UnionEliminator` | ✅ yes |
| `UnionMerge` | ✅ yes |
| `UnionToDistinct` | ✅ yes |

## Independent LLM review of the diff

**Verdict:** SAFE

It adds only a new static factory and a new nested record without altering any existing signatures or semantics, and its `semantics()` follows the existing uninterpreted-table/Calcite-scan pattern. A multi-column scan is a natural generalization needed when a base relation must expose distinct columns for join and grouping, not a one-off rule hack. ```

## Diff: `RelRN.java`

```diff
--- RelRN.java (before)
+++ RelRN.java (after)
@@ -26,6 +26,13 @@ public interface RelRN {
         return scan(id, RexRN.varType(typeName, true), false);
     }
 
+    /** Multi-column scan -- additive alongside the single-column {@link #scan}
+     * above, for rules whose base relation needs more than one column (e.g.
+     * one column used as a join key, another as a separate group-by key). */
+    static ScanMany scanMany(String id, Seq<RelType.VarType> tys) {
+        return new ScanMany(id, tys);
+    }
+
     RelNode semantics();
 
@@ -138,6 +153,16 @@ public interface RelRN {
         }
     }
 
+    record ScanMany(String name, Seq<RelType.VarType> tys) implements RelRN {
+        @Override
+        public RelNode semantics() {
+            var colNames = tys.mapIndexed((i, t) -> "col-" + name + "-" + i);
+            Seq<org.apache.calcite.rel.type.RelDataType> colTypes = tys.map(t -> t);
+            var table = new QedTable(name, colNames, colTypes, Set.empty(), Set.empty());
+            return RuleBuilder.create().addTable(table).scan(name).build();
+        }
+    }
+
     record Filter(RexRN cond, RelRN source) implements RelRN {
```
