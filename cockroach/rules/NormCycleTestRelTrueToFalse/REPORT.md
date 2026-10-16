# NormCycleTestRelTrueToFalse

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2

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

The rewrite `NormCycleTestRel(True) => NormCycleTestRel(False)` has no defined evaluation semantics — it exists solely to exercise the optimizer's cycle-detection machinery — so its only semantic content is asserting f(True) = f(False) for an uninterpreted function f over two distinct boolean constants. Any faithful relational embedding (e.g., Project(f(True)) vs Project(f(False)) over a possibly non-empty scan) is not a valid theorem: a countermodel exists where f maps true and false to different values, and QED, being sound, correctly refuses to prove it. This is not a DSL gap or encoding mistake — the literals and generic projection operators are all present — but a case where the underlying claim is simply false and the intended property (cycle detection) is an engine-level behavior with no bag-semantic content for QED to verify. ```
