# NormCycleTestRelFalseToTrue

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/cycle.opt

Extracted from `cycle.opt` (which defines multiple rules — implement specifically `NormCycleTestRelFalseToTrue`, not the other rules in that file):

```
[NormCycleTestRelFalseToTrue, Normalize]
(NormCycleTestRel (False))
=>
(NormCycleTestRel (True))
```
```

## Independent verifier review

**Verdict:** AGREE

This is a test-only, cycle-inducing rewrite for `NormCycleTestRel`, an operator with no defined evaluation semantics, so its only semantic content is `f(False) = f(True)` for an uninterpreted function `f` applied to two distinct boolean constants. QED is a sound bag-equivalence prover that models uninterpreted operators as arbitrary functions, and `f(a) = f(b)` for `a ≠ b` is not a theorem (a countermodel exists), so no faithful relational encoding — e.g. `Project(f(False))` vs `Project(f(True))` over a scan — can be proved equivalent unless the operator is misencoded as argument-ignoring, which would no longer be this rule. There is genuinely no correctness property for QED to establish, and `extend_dsl_file` cannot help because the blocker is the rule's lack of a semantic equivalence, not a missing operator. ```
