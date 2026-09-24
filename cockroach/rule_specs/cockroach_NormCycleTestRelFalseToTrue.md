# Name: NormCycleTestRelFalseToTrue
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/cycle.opt

Extracted from `cycle.opt` (which defines multiple rules — implement specifically `NormCycleTestRelFalseToTrue`, not the other rules in that file):

```
[NormCycleTestRelFalseToTrue, Normalize]
(NormCycleTestRel (False))
=>
(NormCycleTestRel (True))
```
