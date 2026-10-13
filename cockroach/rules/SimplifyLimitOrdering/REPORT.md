# SimplifyLimitOrdering

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyLimitOrdering removes redundant columns from the Limit operator's
input ordering.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyLimitOrdering`, not the other rules in that file):

```
# SimplifyLimitOrdering removes redundant columns from the Limit operator's
# input ordering.
[SimplifyLimitOrdering, Normalize]
(Limit
    $input:*
    $limit:*
    $ordering:* &
        (CanSimplifyLimitOffsetOrdering $input $ordering)
)
=>
(Limit
    $input
    $limit
    (SimplifyLimitOffsetOrdering $input $ordering)
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's only change is rewriting the Limit's ordering annotation via functional-dependency reasoning, leaving the input relation and limit value untouched, and QED explicitly decides bag equivalence with no model of list/ordering semantics (Sort/Limit/Offset/Window have no bag-semantic meaning). The DSL has no channel to express an ordering property at all, so before and after are indistinguishable to the prover and the FD-based argument is a physical-property claim with no bag-level content to verify — extending the DSL with sort/limit builders would yield at most a vacuous identity proof, since the immutable QED prover does not model orderings. ```
