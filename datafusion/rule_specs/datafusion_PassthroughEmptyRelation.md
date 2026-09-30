# Name: PassthroughEmptyRelation
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/propagate_empty_relation.rs

Registered DataFusion optimizer rule this was extracted from: `PropagateEmptyRelation`.

A single-input node -- `Projection`, `Filter`, `Window`, `Sort`, `SubqueryAlias`, `Repartition`, or `Limit` (all seven share this one arm in the source's `match plan`) -- whose only input is `EmptyRelation { produce_one_row: false, .. }` rewrites to `EmptyRelation` of the parent's own schema: any of these operators applied to a statically-empty input produces a statically-empty output. Implement one representative instance (e.g. Filter or Projection over an EmptyRelation input) -- the identity is identical for all seven wrapper node types, see `empty_child`.
