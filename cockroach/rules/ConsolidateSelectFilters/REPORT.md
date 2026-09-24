# ConsolidateSelectFilters

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 20  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

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
```

## Independent verifier review

**Verdict:** AGREE

ConsolidateSelectFilters rewrites a Select's filter by wrapping conjunctions of single-variable comparisons in a `Range` node, and its soundness rests entirely on CockroachDB's `Range` scalar operator being semantically transparent (equivalent to its child) — a backend-internal property of the operator. RuleScript can only introduce `Range` as an uninterpreted scalar symbol, and QED cannot prove that an uninterpreted function is the identity (no axiom mechanism exists to assert `range(e) = e`), so the only provable encodings are trivial: either keep `Range` and fail, or drop it and the rewrite degenerates to the identity. The porter's pre-crash investigation had in fact already pinned down exactly this — `Range` is "purely a hint for index constraint construction" with no logical effect — so the UNSUPPORTED conclusion is correct, not an encoding miss. ```
