# PushLimitIntoLock

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 47  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

PushLimitIntoLock pushes the Limit operator into its Lock input, as long as
the Lock does not use a SKIP LOCKED wait policy. This helps minimize the
number of rows locked.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushLimitIntoLock`, not the other rules in that file):

```
# PushLimitIntoLock pushes the Limit operator into its Lock input, as long as
# the Lock does not use a SKIP LOCKED wait policy. This helps minimize the
# number of rows locked.
[PushLimitIntoLock, Normalize]
(Limit
    (Lock $input:* $private:*) & ^(LockUsesSkipLocked $private)
    $limit:*
    $ordering:*
)
=>
(Lock (Limit $input $limit $ordering) $private)
```
```

## Independent verifier review

**Verdict:** AGREE

The transpose is only valid because (a) the Limit keeps the first $limit rows under its ORDER BY — QED explicitly has no list/ordering semantics for Sort/Limit/Offset, which carry no bag-semantic meaning — and (b) Lock is a side-effectful, row-preserving operator whose guard (not SKIP LOCKED) is backend-internal semantics the trusted prover cannot see: the returned rows may be equivalent, but the rule actually constrains *which rows get locked*, and encoding Lock as an uninterpreted unary operator would make Limit∘Lock and Lock∘Limit unequal in general (arbitrary operators don't commute), so it would be refuted, not proved. The gap is in the immutable prover (no ordering model, no side-effect operator; Limit isn't even serializable in the current DSL), not a missing builder, so a DSL extension cannot rescue it.
