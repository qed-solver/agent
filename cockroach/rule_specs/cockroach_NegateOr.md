# Name: NegateOr
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

NegateOr converts the negation of a disjunction into a conjunction of
negations.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `NegateOr`, not the other rules in that file):

```
# NegateOr converts the negation of a disjunction into a conjunction of
# negations.
[NegateOr, Normalize]
(Not (Or $left:* $right:*))
=>
(And (Not $left) (Not $right))
```
