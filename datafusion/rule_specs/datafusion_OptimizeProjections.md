# Name: OptimizeProjections
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/optimize_projections/mod.rs

Registered DataFusion optimizer rule this was extracted from: `OptimizeProjections`.

A whole-plan column-pruning pass: starting from 'every output column of the top-level plan is required' and walking top-down, computes at each node the set of column indices that node's parent(s) actually need (`RequiredIndices`), then rewrites that node to only compute/pass through those columns -- inserting a narrower `Projection` (or shrinking an existing one, see `merge_consecutive_projections`) wherever a node was producing columns nothing above it uses. This is DataFusion's single unified dead-column-elimination mechanism, analogous in spirit to cockroach's PruneWindowInputCols/PruneWindowOutputCols family but implemented as one generic recursive pass rather than separate per-operator rules. Implement the representative case: a `Projection(a, b, c)` feeding a parent that only ever references `a` gets narrowed to `Projection(a)`.
