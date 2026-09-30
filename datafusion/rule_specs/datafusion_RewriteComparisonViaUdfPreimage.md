# Name: RewriteComparisonViaUdfPreimage
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/udf_preimage.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions submodule: udf_preimage.rs`.

`udf(x) op literal`, where `udf` is a scalar user-defined function that declares an invertible 'preimage' function `udf_inv` for operator `op` (see the `preimage` trait hook), rewrites to `x op' udf_inv(literal)` for the corresponding (possibly operator-flipped, e.g. for a decreasing function) comparison `op'` -- avoiding evaluating `udf` on every row of the input by instead evaluating its inverse once on the literal. Implement the representative case for `=`: `udf(x) = c` rewrites to `x = udf_inv(c)`; see `test_preimage_eq_rewrite`, and the negative-control precedent in `test_preimage_non_literal_rhs_no_rewrite` (no rewrite when the comparison isn't against a literal).
