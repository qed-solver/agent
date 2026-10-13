# TryAddLimitToRecursiveBranch

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/with.opt

TryAddLimitToRecursiveBranch attempts to infer a limit that applies to the
(unbounded) recursive branch of the CTE. This is accomplished by taking the
upper bound of the initial branch, and checking whether applying that limit
to the recursive WithScans would allow the same bound to be inferred for the
recursive branch. This is useful because it can allow
ApplyLimitToRecursiveCTEScan to fire. See the CanAddRecursiveLimit comment
for details on when it is possible to make this transformation.

The added limit doesn't need an ordering because it will never actually limit
the number of rows returned.

Extracted from `with.opt` (which defines multiple rules — implement specifically `TryAddLimitToRecursiveBranch`, not the other rules in that file):

```
# TryAddLimitToRecursiveBranch attempts to infer a limit that applies to the
# (unbounded) recursive branch of the CTE. This is accomplished by taking the
# upper bound of the initial branch, and checking whether applying that limit
# to the recursive WithScans would allow the same bound to be inferred for the
# recursive branch. This is useful because it can allow
# ApplyLimitToRecursiveCTEScan to fire. See the CanAddRecursiveLimit comment
# for details on when it is possible to make this transformation.
#
# The added limit doesn't need an ordering because it will never actually limit
# the number of rows returned.
[TryAddLimitToRecursiveBranch, Normalize]
(RecursiveCTE
    $binding:* & ^(HasBoundedCardinality $binding)
    $initial:* & (HasBoundedCardinality $initial)
    $recursive:* & ^(HasBoundedCardinality $recursive)
    $private:* &
        (CanAddRecursiveLimit
            $recursive
            (GetRecursiveWithID $private)
            (MakeEmptyColSet)
        )
)
=>
(RecursiveCTE
    $binding
    $initial
    (Limit
        $recursive
        (GetLimitFromCardinality $initial)
        (EmptyOrdering)
    )
    $private
)
```
```

## Independent verifier review

**Verdict:** AGREE

The operator being rewritten on both sides is a `RecursiveCTE` (a recursive fixpoint), which has no bag-semantic/Q-expression translation in the Rust prover and no counterpart in the RelRN core language, and the added `Limit`/`EmptyOrdering` falls squarely under QED's explicit non-modeling of Limit/ordering semantics. Independently, the rule's validity rests on the `CanAddRecursiveLimit`/`HasBoundedCardinality`/`GetLimitFromCardinality` cardinality side-condition, which QED cannot express; even abstracting the unbounded recursive branch to an opaque relation R, the claimed identity `Limit(R, N) ≡ R` holds only when |R| ≤ N rather than universally, so the prover (which checks all instantiations) would correctly refute it. Thus neither a full nor any narrower special case is encodable, confirming the porter's UNSUPPORTED claim. ```
