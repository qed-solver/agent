# DSL extension audit — triggered by `AggregateUnionAggregate`

**Files changed:** RelRN.java
**Reason given by porter:** (extension made while porting AggregateUnionAggregate)
**Final outcome:** kept

## Mechanical regression re-proof (fresh, real qed-prover)

Re-checked 27 rule(s) proved before this change.

| Rule | Still provable? |
|---|---|
| `AggregateValues` | ✅ yes |
| `AggregateExtractProject` | ✅ yes |
| `FilterMerge` | ✅ yes |
| `AggregateMerge` | ✅ yes |
| `ProjectJoinTranspose` | ✅ yes |
| `JoinCommute` | ✅ yes |
| `AggregateRemove` | ✅ yes |
| `FilterRemoveIsNotDistinctFrom` | ✅ yes |
| `FilterAggregateTranspose` | ✅ yes |
| `JoinPushThroughJoin` | ✅ yes |
| `ProjectRemove` | ✅ yes |
| `AggregateExpandDistinctAggregates` | ✅ yes |
| `JoinAssociate` | ✅ yes |
| `AggregateJoinRemove` | ✅ yes |
| `UnionEliminator` | ✅ yes |
| `UnionToDistinct` | ✅ yes |
| `ProjectMerge` | ✅ yes |
| `JoinExtractFilter` | ✅ yes |
| `FilterProjectTranspose` | ✅ yes |
| `AggregateFilterTranspose` | ✅ yes |
| `UnionMerge` | ✅ yes |
| `FilterJoin` | ✅ yes |
| `FilterSetOpTranspose` | ✅ yes |
| `JoinPushExpressions` | ✅ yes |
| `AggregateProjectPullUpConstants` | ✅ yes |
| `AggregateProjectMerge` | ✅ yes |
| `ProjectSetOpTranspose` | ✅ yes |

## Independent LLM review of the diff

**Verdict:** SAFE

The change only adds a new `scanMany` factory and `ScanMany` record while leaving the existing single-column scan semantics unchanged. The new record follows the same `QedTable`/`RuleBuilder` construction pattern and naming convention as the surrounding DSL. It is a plausible, general extension for rules that need a base relation with multiple uninterpreted columns. ```

## Diff: `RelRN.java`

```diff
--- RelRN.java (before)
+++ RelRN.java (after)
@@ -26,6 +26,13 @@
         return scan(id, RexRN.varType(typeName, true), false);
     }
 
+    /** Multi-column scan — additive alongside the single-column {@link #scan}
+     * above, for rules whose base relation needs more than one column (e.g.
+     * one column used as a join key, another as a separate group-by key). */
+    static ScanMany scanMany(String id, Seq<RelType.VarType> tys) {
+        return new ScanMany(id, tys);
+    }
+
     RelNode semantics();
 
     default RexRN field(int ordinal) {
@@ -142,6 +149,16 @@
         public RelNode semantics() {
             var table = new QedTable(name, Seq.of("col-" + name), Seq.of(ty), unique ?
                     Set.of(ImmutableBitSet.of(0)) : Set.empty(), Set.empty());
+            return RuleBuilder.create().addTable(table).scan(name).build();
+        }
+    }
+
+    record ScanMany(String name, Seq<RelType.VarType> tys) implements RelRN {
+        @Override
+        public RelNode semantics() {
+            var colNames = tys.mapIndexed((i, t) -> "col-" + name + "-" + i);
+            Seq<org.apache.calcite.rel.type.RelDataType> colTypes = tys.map(t -> t);
+            var table = new QedTable(name, colNames, colTypes, Set.empty(), Set.empty());
             return RuleBuilder.create().addTable(table).scan(name).build();
         }
     }
```
