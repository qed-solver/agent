# SimplifyOffsetOrdering

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyOffsetOrdering removes redundant columns from the Offset operator's
input ordering.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyOffsetOrdering`, not the other rules in that file):

```
# SimplifyOffsetOrdering removes redundant columns from the Offset operator's
# input ordering.
[SimplifyOffsetOrdering, Normalize]
(Offset
    $input:*
    $offset:*
    $ordering:* &
        (CanSimplifyLimitOffsetOrdering $input $ordering)
)
=>
(Offset
    $input
    $offset
    (SimplifyLimitOffsetOrdering $input $ordering)
)
```
```

## Independent verifier review

**Verdict:** AGREE

SimplifyOffsetOrdering only rewrites the Offset operator's sort-key annotation using functional-dependency/ordered-stream reasoning, while leaving the underlying row bag unchanged. QED proves bag equivalence and has no model of Sort/Limit/Offset ordering semantics, and RuleScript's core relational language does not expose Offset/ordering in a way that could make this non-vacuous.
