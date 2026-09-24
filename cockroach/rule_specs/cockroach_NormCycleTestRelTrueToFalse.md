# Name: NormCycleTestRelTrueToFalse
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/cycle.opt

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
