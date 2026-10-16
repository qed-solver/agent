# UnifyComparisonTypes

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 33  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

UnifyComparisonTypes takes a mixed-type comparison between a non-constant and
a constant and, if appropriate, converts the constant to the type of the
non-constant to allow constraints to be generated.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `UnifyComparisonTypes`, not the other rules in that file):

```
# UnifyComparisonTypes takes a mixed-type comparison between a non-constant and
# a constant and, if appropriate, converts the constant to the type of the
# non-constant to allow constraints to be generated.
[UnifyComparisonTypes, Normalize]
(Comparison
    $left:(Variable)
    $right:(Const) &
        (Let ($result $ok):(UnifyComparison $left $right) $ok)
)
=>
((OpName) $left $result)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness is exactly that casting the constant to the variable's type is lossless — i.e. op(x, c) ⟺ op(x, cast(c)) — but QED erases all types to integers and models explicit casts as uninterpreted functions with no axioms (as qed.pdf documents), so cast(c) is a free term and the SMT countermodel (an uninterpreted predicate distinguishing c from f(c)) is a genuine independence result, not an encoding bug. RuleScript exposes no side-condition or axiom mechanism (a rule is just a before/after pair), so the only provable degenerate encoding is the identity cast, which erases the rule entirely; the porter's concrete probe of x = C vs x = cast(C) correctly targeted the irreducible core. ```
