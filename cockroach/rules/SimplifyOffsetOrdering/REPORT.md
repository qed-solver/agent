# SimplifyOffsetOrdering

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 7  **Verification rounds used:** 1

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

This rule rewrites the ordering directive of an Offset node, and Offset's very meaning — which rows survive after skipping the first k — is a function of row order, so the rule's entire correctness content sits in exactly the semantics QED excludes: qed.pdf §6.2 states QED uses bag semantics and "does not support queries that make use of the ordering semantics of Limit, Offset, or Order By" (Table 3's "List semantic" category accounts for 103 CockroachDB failures). The core language has no Sort/Limit/Offset builder to express even the shape, and an extend_dsl_file addition could not close the gap because the unmodifiable prover has no ordering theory to decide equality of the offset-selected sequence over. No non-trivial bag-semantic special case remains: even under a unique leading key, verifying that the sub-sequence after skipping k rows is identical under orderings (a,b) vs (a) is pure list reasoning QED cannot perform. ```
