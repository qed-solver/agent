# FoldNullBinaryLeft

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 85  **Verification rounds used:** 5

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullBinaryLeft replaces the binary operator with null if its left input
is null and it does not allow null arguments.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullBinaryLeft`, not the other rules in that file):

```
# FoldNullBinaryLeft replaces the binary operator with null if its left input
# is null and it does not allow null arguments.
[FoldNullBinaryLeft, Normalize]
(Binary
    $left:(Null)
    $right:* & ^(AllowNullArgs (OpName) $left $right)
)
=>
(FoldNullBinary (OpName) $left $right)
```
```

## Independent verifier review

**Verdict:** AGREE

Manually investigated by Claude (the automated run exhausted all 5 rounds without a usable conclusion). Same root cause as FoldEqualsAnyNull/FoldInNull: the rule needs Binary(op, NULL, right) == NULL for any op/right that doesn't specifically allow null arguments — an uninterpreted operator forced to NULL based on ONE argument being NULL, independent of the other. QED's prover core has no general null-propagation rule for uninterpreted function/predicate applications (qed-prover/src/pipeline/shared.rs: is_null() is just equality against a fresh uninterpreted NULL sentinel, with no strictness tied to any operator's arguments) — only a small hardcoded set of operators (COUNT, EXISTS, IS NULL/IS NOT NULL, boolean AND/OR/NOT) get real interpreted semantics. No CASE/conditional construct exists in the DSL to manually encode the null-forcing branch either. Genuinely outside QED's supported fragment for the same reason as the other Fold*Null* rules in this family (FoldEqualsAnyNull, FoldInNull) — likely applies to FoldNullBinaryRight and other siblings in fold_constants.opt too.
