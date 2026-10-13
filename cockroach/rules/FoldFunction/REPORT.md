# FoldFunction

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 27  **Verification rounds used:** 2

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

FoldFunction's correctness rests on evaluating specific named functions over specific constant arguments and equating the result with the produced constant, but QED treats all function/predicate symbols as uninterpreted with no evaluation axioms — under universal quantification it can never derive a relation between f(c₁,…,cₙ) and any particular folded value — and the scalar language has only boolean True/False literals, so the constant arguments and the folded result can't even be written down (and a scan field is a variable, not a constant, making "f(x) ⇒ r" a different, unprovable statement). Closing this with extend_dsl_file is impossible in principle: new operators serialize as uninterpreted symbols in the JSON QED already consumes, and giving a symbol evaluation semantics would mean modifying the Rust prover itself, which is off-limits — so this is the fundamental "backend operator semantics QED cannot see through" limitation, not a missed encoding. ```
