# Name: EliminateCrossJoin
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/eliminate_cross_join.rs

Registered DataFusion optimizer rule this was extracted from: `EliminateCrossJoin`.

Flattens a chain of INNER joins/cross-joins connected by a Filter's AND-conjuncts (or already-INNER-joined) into a left-deep chain of genuine equi-joins wherever the filter conjuncts supply usable join keys between two of the flattened inputs (`flatten_join_inputs` + `find_inner_join` + `possible_join_keys`), moving any leftover non-join-key conjuncts back into a residual Filter on top. In effect: `Filter(CrossJoin(CrossJoin(a,b), c), a.x=b.x AND b.y=c.y)` becomes a genuine equi-join chain `Join(Join(a,b,a.x=b.x),c,b.y=c.y)` with no leftover Filter. Implement the core 2-relation cross-join-plus-equality-filter-to-inner-join identity as the narrowed representative case.
