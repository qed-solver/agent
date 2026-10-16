# TryAddLimitToRecursiveBranch

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 10  **Verification rounds used:** 1

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

The rule's before-side `RecursiveCTE` is a least fixed point over a working table — no finite bag-semiring expression and no core-language operator can represent it, and no `extend_dsl_file` builder could help since QED itself (the unmodifiable arbiter) has no fixed-point semantics to check it against. Its side conditions (`HasBoundedCardinality`, `CanAddRecursiveLimit`) are row-count bounds on whole relations, which the DSL cannot express — uninterpreted symbols are only row-level predicates/projections/functions, with no cardinality-constraint mechanism. And the introduced `Limit` has no bag semantics in QED: its safety as a no-op rests entirely on the inexpressible "never exceeds N rows" row-count argument, so any encodable before/after pair would either omit the limit/fixed point (collapsing to a vacuous identity) or be unencodable — the porter's UNSUPPORTED conclusion is correct.
