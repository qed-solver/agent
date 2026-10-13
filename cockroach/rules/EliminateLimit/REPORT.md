# EliminateLimit

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2

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

Limit is one of the operators QED explicitly does not model (Sort/Limit/Offset have no bag-semantic meaning; Limit is left uninterpreted with no row-retention, ordering, or cardinality axioms), and the rule's correctness rests exactly on that missing semantics — the side condition LimitGeMaxRows needs a bound on the input's maximum row count, and the conclusion needs the axiom that a Limit with limit >= maxRows acts as the identity. Since the rewrite *removes* the Limit (the after side is the bare input), a faithful encoding cannot share the Limit symbol as a common wrapper on both sides to factor out as in the proven AssociateLimitJoinsLeft-style precedent; the two sides become Limit(x) versus x for an uninterpreted function, which is not valid for arbitrary x, and no core-language stand-in (filter, join, union, aggregate) can faithfully model positional row selection, so no narrower special case is provable either.
