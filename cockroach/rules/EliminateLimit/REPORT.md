# EliminateLimit

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

EliminateLimit discards a Limit operator if its constant limit is greater than
or equal to the maximum number of rows that can be returned by the input. In
this case, the Limit is just a no-op, because the rows are already limited.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `EliminateLimit`, not the other rules in that file):

```
# EliminateLimit discards a Limit operator if its constant limit is greater than
# or equal to the maximum number of rows that can be returned by the input. In
# this case, the Limit is just a no-op, because the rows are already limited.
[EliminateLimit, Normalize]
(Limit
    $input:*
    (Const $limit:*) & (LimitGeMaxRows $limit $input)
)
=>
$input
```
```

## Independent verifier review

**Verdict:** AGREE

EliminateLimit's correctness rests on two things QED fundamentally lacks, per the Limitations section: it models no list/ordering semantics, so a `Limit` node is an uninterpreted operator in the fixed prover with no axiom of any form `Limit(n, X) ≡ X` (or `Limit(n, ∅) ≡ ∅`), and the rule's guard `limit ≥ MaxRows(input)` is a cardinality precondition that RuleScript (unconditional before/after equivalence) and QED's JSON (no row-count field; only row-level `guaranteed` predicates) have no mechanism to express. The only DSL input with a structurally bounded row count is `Empty`, and even that degenerate instance is unprovable for the first reason, so `extend_dsl_file` (which can only add builders, not prover axioms) cannot close the gap — a genuine QED limitation, not a missed encoding. ```
