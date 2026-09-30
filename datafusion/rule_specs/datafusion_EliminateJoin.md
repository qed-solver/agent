# Name: EliminateJoin
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/eliminate_join.rs

Registered DataFusion optimizer rule this was extracted from: `EliminateJoin`.

Performs a liveness analysis (`LiveColumns`) top-down over the plan and, whenever a `LogicalPlan::Join` node's output columns from one side are entirely unused by anything above it AND that side cannot affect row cardinality/duplication for the surviving side (see `rewrite_join`'s exact conditions in the file, e.g. an INNER/LEFT/SEMI join against a side that is provably duplicate-free and whose columns are all dead), the Join is eliminated and replaced by just the side that is still needed. Every other `LogicalPlan::X` arm in `rewrite_node` is pure plumbing that threads the `live` column set down to children -- implement the actual elimination identity in `rewrite_join`, not the dispatch plumbing.
