# Name: NegateAnd
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

NegateAnd converts the negation of a conjunction into a disjunction of
negations.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `NegateAnd`, not the other rules in that file):

```
# NegateAnd converts the negation of a conjunction into a disjunction of
# negations.
[NegateAnd, Normalize]
(Not (And $left:* $right:*))
=>
(Or (Not $left) (Not $right))
```
