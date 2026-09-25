# ConsolidateSelectFilters

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 43  **Verification rounds used:** 3

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

The rule's entire semantic content is that CockroachDB's `Range` predicate is a transparent wrapper over its inner conjunction (Range(e) ≡ e in filter context); in RuleScript, `Range` can only be introduced as an uninterpreted predicate symbol, and QED fundamentally cannot prove an uninterpreted symbol equivalent to its argument, and the JSON/DSL format has no channel for operator axioms (the "guaranteed" field only attaches to base-table scans, and RelRN/RexRN/JSONSerializer carry no operator-axiom construct), so extending the DSL cannot supply the required transparency axiom without modifying the trusted prover. Any encoding that drops `Range` collapses the rule to nested-filter AND merging — i.e. the already PROVED FULL FilterMerge — so there is no genuine non-trivial PARTIAL fallback to port. ```
