# Name: DecorrelateLateralJoin
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/decorrelate_lateral_join.rs

Registered DataFusion optimizer rule this was extracted from: `DecorrelateLateralJoin`.

Rewrites a cross/inner/left join whose right side is a `LATERAL` subquery containing outer-column references (`extract_lateral_subquery` finds a `Subquery` node with `contains_outer_reference`) into a plain (non-lateral) join by substituting the outer references and folding the correlation condition into the join's ON predicate. See `rewrite_internal` for the mechanics; supports INNER and LEFT lateral joins. Implement the core single-equality-correlation decorrelation identity.
