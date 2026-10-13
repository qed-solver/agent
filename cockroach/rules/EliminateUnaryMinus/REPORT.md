# EliminateUnaryMinus

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 8  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/numeric.opt

EliminateUnaryMinus discards a doubled UnaryMinus operator.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `EliminateUnaryMinus`, not the other rules in that file):

```
# EliminateUnaryMinus discards a doubled UnaryMinus operator.
[EliminateUnaryMinus, Normalize]
(UnaryMinus (UnaryMinus $input:*))
=>
$input
```
```

## Independent verifier review

**Verdict:** AGREE

The rule requires the numeric negation algebraic law `neg(neg(x)) = x`, but RuleScript expresses that scalar operator as an uninterpreted projection symbol and QED has no involution axiom for uninterpreted functions. The porter’s encoding is therefore faithful, and the failure is a fundamental limitation rather than a symbol-sharing or modeling bug. Asserting the law as a table “guaranteed” constraint would circularly assume the conclusion, and using boolean `Not` would prove a different rule.
