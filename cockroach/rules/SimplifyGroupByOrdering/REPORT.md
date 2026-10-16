# SimplifyGroupByOrdering

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyGroupByOrdering removes redundant columns from the GroupBy operators'
input ordering.

Note: Doesn't match EnsureDistinctOn because test cases were too difficult to
find. If a test case for EnsureDistinctOn is found, it should be added to the
match pattern.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyGroupByOrdering`, not the other rules in that file):

```
# SimplifyGroupByOrdering removes redundant columns from the GroupBy operators'
# input ordering.
#
# Note: Doesn't match EnsureDistinctOn because test cases were too difficult to
# find. If a test case for EnsureDistinctOn is found, it should be added to the
# match pattern.
[SimplifyGroupByOrdering, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* &
        (CanSimplifyGroupingOrdering $input $groupingPrivate)
)
=>
((OpName)
    $input
    $aggregations
    (SimplifyGroupingOrdering $input $groupingPrivate)
)
```
```

## Independent verifier review

**Verdict:** AGREE

QED is a bag-semantics decision procedure with no model of ordering/order-by — Sort/Limit/Offset/Order-By carry no bag meaning (qed.pdf §6.2 "List semantics"; reference Limitations) — yet SimplifyGroupByOrdering's entire content is simplifying the physical OrderingChoice attached to GroupBy/ScalarGroupBy/DistinctOn by dropping ordering columns made redundant by the input's functional-dependency set, which changes only the ordering directive, not the produced rows, so before and after serialize to the identical bag. QED's only FD modeling (unique-key scans) cannot help, because those FDs here drive ordering-column removal rather than any change in row content, so no non-trivial bag-level equivalence exists and any RuleScript encoding degenerates to a vacuous `GroupBy ≡ GroupBy` identity — genuinely outside what the prover can check, not a missed encoding. ```
