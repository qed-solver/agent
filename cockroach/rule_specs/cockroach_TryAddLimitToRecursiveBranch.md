# Name: TryAddLimitToRecursiveBranch
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/with.opt

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
