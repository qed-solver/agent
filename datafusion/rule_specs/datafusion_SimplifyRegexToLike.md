# Name: SimplifyRegexToLike
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs

Registered DataFusion optimizer rule this was extracted from: `SimplifyExpressions (regex)`.

A `~`/`~*` (regex match) or `!~`/`!~*` (regex non-match) comparison whose pattern operand is a literal string containing no actual regex metacharacters (just literal text, possibly with a leading/trailing `.*` translatable to a `%` wildcard) rewrites to the equivalent `LIKE`/`NOT LIKE`/`ILIKE`/`NOT ILIKE` comparison, which is typically cheaper to evaluate than a full regex engine invocation. See `test_simplify_regex` / `test_simplify_not_regex_match`. Implement the representative case: `x ~ 'abc'` (no metacharacters) rewrites to `x LIKE 'abc'`.
