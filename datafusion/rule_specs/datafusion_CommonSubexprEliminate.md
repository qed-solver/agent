# Name: CommonSubexprEliminate
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/common_subexpr_eliminate.rs

Registered DataFusion optimizer rule this was extracted from: `CommonSubexprEliminate`.

For a `Projection`/`Sort`/`Filter`/`Window`/`Aggregate` node, identifies any non-trivial sub-expression that occurs 2+ times across that node's own expression list (`identify_common_exprs`), hoists ONE evaluation of it into a new `Projection` inserted below the node, and rewrites every occurrence in the node's own expressions to reference that single precomputed column instead (`rewrite_exprs_list`) -- avoiding redundant recomputation of the same sub-expression within one node. The dispatch across five different `LogicalPlan` variants (`try_optimize_proj`/`_sort`/`_filter`/`_window`/`_aggregate`) is the same CSE mechanism applied to each node type's own expression list; `Join`/`AsOfJoin`/`Repartition`/`Union`/`TableScan`/etc. are no-ops (their arm just passes through unchanged). Implement the representative Projection case: `SELECT a+b, (a+b)*2 FROM t` hoists `a+b` into a sub-projection so it's computed once.
