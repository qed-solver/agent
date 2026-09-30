# Name: PushDownLeafProjections
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/extract_leaf_expressions.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownLeafProjections`.

Pass 2 of the pair introduced by ExtractLeafExpressions. Takes a Projection created by ExtractLeafExpressions (or any Projection computing only complex-leaf expressions over a single input column) and, when its input can absorb it without changing semantics, merges it further down past that input (`try_push_input`) -- moving the leaf-expression computation as close to its source column as the plan shape allows, e.g. below another Projection or a Filter/Sort that doesn't reference the extracted expression's underlying column in a conflicting way. Implement the representative case: an extraction Projection sitting directly above a plain column-selecting Projection merges into (moves below) it.
