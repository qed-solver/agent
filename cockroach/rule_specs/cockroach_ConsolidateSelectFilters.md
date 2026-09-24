# Name: ConsolidateSelectFilters
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

ConsolidateSelectFilters consolidates filters that constrain a single
variable. For example, filters x >= 5 and x <= 10 would be combined into a
single Range operation.

The benefit of consolidating these filters is it allows a single constraint
to be generated for the variable instead of multiple. In the example above,
we can generate the single constraint [/5 - /10] instead of the two
constraints [/5 - ] and [ - /10]. The single constraint allows us to better
estimate the selectivity of the predicate when calculating statistics for
the Select expression.

This rule is low priority so other rules in this file such as
RemoveNotNullCondition can run first.

Extracted from `select.opt` (which defines multiple rules — implement specifically `ConsolidateSelectFilters`, not the other rules in that file):

```
# ConsolidateSelectFilters consolidates filters that constrain a single
# variable. For example, filters x >= 5 and x <= 10 would be combined into a
# single Range operation.
#
# The benefit of consolidating these filters is it allows a single constraint
# to be generated for the variable instead of multiple. In the example above,
# we can generate the single constraint [/5 - /10] instead of the two
# constraints [/5 - ] and [ - /10]. The single constraint allows us to better
# estimate the selectivity of the predicate when calculating statistics for
# the Select expression.
#
# This rule is low priority so other rules in this file such as
# RemoveNotNullCondition can run first.
[ConsolidateSelectFilters, Normalize, LowPriority]
(Select $input:* $filters:* & (CanConsolidateFilters $filters))
=>
(Select $input (ConsolidateFilters $filters))
```
