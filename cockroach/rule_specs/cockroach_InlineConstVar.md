# Name: InlineConstVar
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/inline.opt

InlineConstVar inlines variables which are restricted to be constant, as in
SELECT * FROM foo WHERE a = 4 AND a IN (1, 2, 3, 4).
=>
SELECT * FROM foo WHERE a = 4 AND 4 IN (1, 2, 3, 4).
Note that a single iteration of this rule might not be sufficient to inline
all variables, in which case it will trigger itself again.

This rule is high priority so that it runs before filter pushdown.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineConstVar`, not the other rules in that file):

```
# InlineConstVar inlines variables which are restricted to be constant, as in
#   SELECT * FROM foo WHERE a = 4 AND a IN (1, 2, 3, 4).
# =>
#   SELECT * FROM foo WHERE a = 4 AND 4 IN (1, 2, 3, 4).
# Note that a single iteration of this rule might not be sufficient to inline
# all variables, in which case it will trigger itself again.
#
# This rule is high priority so that it runs before filter pushdown.
[InlineConstVar, Normalize, HighPriority]
(Select $input:* $filters:* & (CanInlineConstVar $filters))
=>
(Select $input (InlineConstVar $filters))
```
