# PushOffsetIntoLock

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 26  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

PushOffsetIntoLock pushes the Offset operator into its Lock input, as long as
the Lock does not use a SKIP LOCKED wait policy. This helps minimize the
number of rows locked.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushOffsetIntoLock`, not the other rules in that file):

```
# PushOffsetIntoLock pushes the Offset operator into its Lock input, as long as
# the Lock does not use a SKIP LOCKED wait policy. This helps minimize the
# number of rows locked.
[PushOffsetIntoLock, Normalize]
(Offset
    (Lock $input:* $private:*) & ^(LockUsesSkipLocked $private)
    $offset:*
    $ordering:*
)
=>
(Lock (Offset $input $offset $ordering) $private)
```
```

## Independent verifier review

**Verdict:** AGREE

Offset's meaning — dropping the first k rows of a given ordering — is exactly the list/ordering semantics QED's bag-semantic decision procedure does not model, and that is a limitation of the (unmodifiable) prover, not a DSL gap that `extend_dsl_file` could close; the rule's soundness additionally rests on the backend-specific fact that a non-SKIP-LOCKED Lock passes every input row through, so modeling Lock honestly as an uninterpreted row-dropping bag function makes the commutation `Offset∘Lock = Lock∘Offset` refutable (suffix-of-filtered ≠ filter-of-suffix), while the only model under which it holds (row-identity) collapses the rule to the degenerate `Offset(x) = Offset(x)`. No non-degenerate, semantically honest encoding exists for QED to prove.
