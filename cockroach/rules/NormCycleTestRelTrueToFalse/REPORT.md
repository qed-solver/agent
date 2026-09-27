# NormCycleTestRelTrueToFalse

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/cycle.opt

The following two rules create a normalization rule cycle for the
NormCycleTestRel expression. This rule cycle is used to test that the cycle
can be detected and a stack overflow does not occur. See the cycle test file.

Extracted from `cycle.opt` (which defines multiple rules — implement specifically `NormCycleTestRelTrueToFalse`, not the other rules in that file):

```
# The following two rules create a normalization rule cycle for the
# NormCycleTestRel expression. This rule cycle is used to test that the cycle
# can be detected and a stack overflow does not occur. See the cycle test file.
[NormCycleTestRelTrueToFalse, Normalize]
(NormCycleTestRel (True))
=>
(NormCycleTestRel (False))
```
```

## Independent verifier review

**Verdict:** AGREE

The rule rewrites `NormCycleTestRel(True)` to `NormCycleTestRel(False)`, where that operator is an opaque test-only function with no defined algebraic semantics, so the two sides are distinct applications of an uninterpreted function to different constants. QED is a universal bag-equivalence prover: for any honest encoding, an SMT counterexample instantiation (f(true) ≠ f(false), one row) refutes the equivalence, so no proof can exist, and the only encodings that would pass are ones that silently erase the difference the rule is defined to make. This is a fundamental limitation, not a missing DSL capability — the rule is deliberately non-equivalence-preserving (a cycle-detection test fixture), so there is nothing for QED to certify and UNSUPPORTED is the correct conclusion. ```
