# PushOffsetIntoLock

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2

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

The rule's central operator, Offset, is precisely one of the list/ordering operators that QED explicitly does not model (qed.pdf §6.2: Sort/Limit/Offset/Window/Sample have no bag-semantic meaning), so no DSL extension can give the prover anything to decide over a plan containing Offset, and the only encodable variant (re-modeling Offset/Lock as uninterpreted filters) would prove a different, vacuous fact (filter commutativity) rather than PushOffsetIntoLock. Compounding this, the rule's validity guard (Lock must not use a SKIP LOCKED wait policy) and its actual purpose (fewer rows locked) depend on Lock's side-effectful, backend-internal semantics, which a bag-equivalence prover cannot express or verify as an uninterpreted symbol. Consequently no genuine non-degenerate special case exists, since Offset has no meaning at all without ordering. ```
