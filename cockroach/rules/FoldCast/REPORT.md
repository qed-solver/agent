# FoldCast

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 8  **Verification rounds used:** 1

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

FoldCast's claim is that `cast(const) = folded_literal`, and its correctness rests entirely on the cast operator's value-producing semantics — semantics that exist nowhere in the stack: the DSL exposes no cast construct (the only value-transforming symbols are uninterpreted `Proj` functions), and QED's prover treats every function symbol as uninterpreted with all virtual types erased to INTEGER, so `f(c) = c'` is not SMT-valid for any uninterpreted `f` and literal `c'`. The only typecheckable encodings are vacuous: omitting the cast leaves both sides as the identical literal 0 after type erasure (proving an artifact, not the rule), and keeping an uninterpreted same-type cast still yields the underivable `f(0) = 0` — so no genuine non-trivial special case exists. Consequently `extend_dsl_file` cannot close the gap either: any new `cast` builder would still serialize to an uninterpreted operator for which the fixed Rust prover has no axioms.
