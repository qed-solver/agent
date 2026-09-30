# Name: NotExistsToNotExists
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/simplify_exprs.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (NOT-pushdown family)`.

`NOT EXISTS (subquery)` rewrites to a `NOT EXISTS`-marked subquery expression directly (rather than a separate `NOT` wrapping a plain `EXISTS`), so downstream decorrelation rules (DecorrelatePredicateSubquery) can recognize it as an anti-join candidate. See `simplify_not_exists`.
