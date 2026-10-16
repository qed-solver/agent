# FoldFunction

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 32  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldFunction is similar to FoldBinary, but it involves a function with
constant inputs. As with FoldBinary, FoldFunction applies as long as the
evaluation would not cause an error. Additionally, only certain functions
are safe to fold as part of normalization. Other functions rely on context
that may change between runs of a prepared query.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldFunction`, not the other rules in that file):

```
# FoldFunction is similar to FoldBinary, but it involves a function with
# constant inputs. As with FoldBinary, FoldFunction applies as long as the
# evaluation would not cause an error. Additionally, only certain functions
# are safe to fold as part of normalization. Other functions rely on context
# that may change between runs of a prepared query.
[FoldFunction, Normalize]
(Function
    $args:* & (IsListOfConstants $args)
    $private:* &
        (Let ($result $ok):(FoldFunction $args $private) $ok)
)
=>
$result
```
```

## Independent verifier review

**Verdict:** AGREE

FoldFunction requires evaluating an arbitrary function call over constants, which means the prover must know that function's internal evaluation semantics. RuleScript can only express such a call as an uninterpreted pred/proj/aggCall symbol, and QED has no constant-folding semantics for those symbols; the only constant-foldable scalar operators are the built-in logical connectives/literals, which correspond to the sibling Unary/Binary/Comparison rules rather than the Function node. Therefore the gap is a fundamental limitation of the fixed QED model, not a missing Java DSL constructor, so the UNSUPPORTED conclusion is justified.
