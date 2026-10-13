# ConvertLikeEscapeToLike

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 43  **Verification rounds used:** 3

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

The rule's correctness rests entirely on CockroachDB's backend-specific semantic that `LIKE`'s default escape character is `'\'`, i.e. the entailment `like_escape(x, p, '\')` ⟺ `like(x, p)`. In QED both `like_escape` and `like` can only be introduced as distinct uninterpreted predicate symbols, and the SMT solver has no axiom relating independent symbols (or symbols of different arity), so the equivalence is invalid under the interpretations QED quantifies over. No DSL extension can close this gap, since the missing piece is operator-specific semantic knowledge that lives in the prover — the trusted, unmodifiable arbiter — rather than a missing operator or shape in the DSL. ```
