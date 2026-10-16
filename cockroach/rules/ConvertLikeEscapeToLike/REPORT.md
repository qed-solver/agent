# ConvertLikeEscapeToLike

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 26  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

ConvertLikeEscapeToLike converts a LIKE x ESCAPE '\' expression, which is
parsed as a like_escape function call, to a LIKE expression. This is valid
because the default escape character is '\'.

Note that the FunctionExpr match pattern is enough to ensure that we are not
transforming a UDF because UDF invocations are always built as UDFCallExprs.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `ConvertLikeEscapeToLike`, not the other rules in that file):

```
# ConvertLikeEscapeToLike converts a LIKE x ESCAPE '\' expression, which is
# parsed as a like_escape function call, to a LIKE expression. This is valid
# because the default escape character is '\'.
#
# Note that the FunctionExpr match pattern is enough to ensure that we are not
# transforming a UDF because UDF invocations are always built as UDFCallExprs.
[ConvertLikeEscapeToLike, Normalize]
(Function
    $args:*
    $private:(FunctionPrivate "like_escape") &
        (Let ($input $iOk):(ScalarExprAt $args 0) $iOk) &
        (Let ($pattern $pOk):(ScalarExprAt $args 1) $pOk) &
        (Let ($escape $eOk):(ScalarExprAt $args 2) $eOk) &
        (ConstStringEquals $escape "\\")
)
=>
(Like $input $pattern)
```
```

## Independent verifier review

**Verdict:** AGREE

The rewrite is valid only because of a CockroachDB function-library fact — the 2-arg `Like(x, p)` is by definition the same boolean function as `like_escape(x, p, '\'` (the default escape char) — and RuleScript provides no mechanism to express that: each named scalar operator becomes a distinct uninterpreted symbol, and QED's SMT core has no axioms linking independent symbols, so it cannot derive `like_escape(x, p, c) = like(x, p)` for any constant `c`; the only way to make both sides syntactically coincide is to use one shared symbol on both sides, which reduces the rule to the tautology `Filter(P) = Filter(P)` and records nothing. This is precisely the stated limitation that QED cannot reason about inference between independent symbols or a backend operator's bespoke internal semantics, and it is not a missing-builder gap: no `extend_dsl_file` change can help, since the prover's axiom-free uninterpreted-function model is fixed and unmodifiable.
