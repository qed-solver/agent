# SimplifyGroupByOrdering

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 26  **Verification rounds used:** 2

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

The rule is a properties-only rewrite: it only replaces GroupingPrivate.Ordering with a relaxation computed by OrderingChoice.Simplify over the input's functional dependencies, while the plan's data and the emitted bag are identical before and after. Its correctness content is entirely a statement about row orderings — that sequences of rows satisfying the original requirement also satisfy the simplified one — and QED is a bag-semantics decision procedure with no notion of required/physical ordering (the same fundamental gap that excludes Sort/Limit/Offset/Window). Any RuleScript encoding would have to render before and after as the same Aggregate(source, groupSet, aggCalls), a vacuous identity that QED could only prove trivially; extending the DSL wouldn't help either, since no ordering channel exists through semantics()/JSONSerializer into the immutable prover, which would erase it back to bag equality. ```
