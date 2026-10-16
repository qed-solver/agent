# ConsolidateSelectFilters

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 32  **Verification rounds used:** 3

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

ConsolidateSelectFilters is a pure representational rewrite whose sole correctness claim is that CockroachDB's Range scalar is evaluation-transparent to its inner And (both the constraint builder and Eval delegate to t.And) — an axiom about a backend operator's internal semantics, not a relational-algebraic fact. QED quantifies universally over uninterpreted symbols with no mechanism for definitional axioms, so an encoding with an uninterpreted Range predicate is genuinely unprovable, and the only encodable variant (modeling Range as the And it is) collapses before() and after() into the same plan, reducing the "proof" to a vacuous identity that certifies nothing about the backend rule. A DSL extension cannot close this gap, since QED's serialization/prover has no notion of a "defined" symbol — the port is blocked by the stated limitation that QED cannot see through an operator's bespoke internal semantics. ```
