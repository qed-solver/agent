# FoldNullBinaryRight

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 49  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullBinaryRight replaces the binary operator with null if its right input
is null and it does not allow null arguments.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullBinaryRight`, not the other rules in that file):

```
# FoldNullBinaryRight replaces the binary operator with null if its right input
# is null and it does not allow null arguments.
[FoldNullBinaryRight, Normalize]
(Binary
    $left:*
    $right:(Null) & ^(AllowNullArgs (OpName) $left $right)
)
=>
(FoldNullBinary (OpName) $left $right)
```
```

## Independent verifier review

**Verdict:** AGREE

FoldNullBinaryRight's correctness rests entirely on the operator-specific axiom "if op does not allow null args, then op(x, NULL) = NULL"; in RuleScript/QED that binary op is an uninterpreted function with no behavioral axioms, and the only constraint channel (table `guaranteed` clauses) expresses row-wise predicates over scan contents, not universal function-level identities over flowing columns — so the SMT layer has no way to derive f(x,NULL)=NULL and the equivalence is genuinely unprovable, not a missing builder. ```
