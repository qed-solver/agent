# FoldCast

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldCast is similar to FoldUnary, but it involves a cast operation. As with
FoldUnary, FoldCast applies as long as the evaluation would not cause an
error.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldCast`, not the other rules in that file):

```
# FoldCast is similar to FoldUnary, but it involves a cast operation. As with
# FoldUnary, FoldCast applies as long as the evaluation would not cause an
# error.
[FoldCast, Normalize]
(Cast
    $input:*
    $typ:* &
        (IsConstValueOrGroupOfConstValues $input) &
        (Let ($result $ok):(FoldCast $input $typ) $ok)
)
=>
$result
```
```

## Independent verifier review

**Verdict:** AGREE

FoldCast's RHS is a value produced by *evaluating* the cast operator on a constant input, but in RuleScript a cast can only be modeled as an uninterpreted function symbol, and QED has no axiom relating cast(c, T) to any folded result — unlike FoldNotFalse, where NOT and the boolean literals are genuinely interpreted. The rule's side conditions are equally inexpressible: the pattern language has no way to state "input is a constant" (IsConstValueOrGroupOfConstValues), no Let-binder to carry the computed ($result, $ok) pair into the RHS, and no literal/constant constructors at all. So the only provable encoding is the vacuous identity with the same cast symbol on both sides; this rests on the operator's bespoke evaluation semantics that QED fundamentally cannot see through, not on a missed encoding trick. ```
