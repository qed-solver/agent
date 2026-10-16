# SimplifyLimitOrdering

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 10  **Verification rounds used:** 1

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

QED's theory is bag-only with uninterpreted operators and has no list/ordering semantics, so a Limit's ordering directive has no representation the prover can reason about: keeping the ordering makes the two sides distinct uninterpreted applications (unprovable), while erasing it reduces the rule to a vacuous identity, since the rows' multiset is untouched by the rewrite. The rule's entire correctness content — that dropping ordering columns functionally determined by earlier ones preserves the row sequence Limit/Offset select on — lives in exactly that unmodeled list semantics, and no `extend_dsl_file` builder can close the gap because the Rust prover itself (which must not be modified) lacks it; the JSON format already carrying `sort`/`limit` fields confirms this is a prover-theory limitation, not a missing DSL feature.
