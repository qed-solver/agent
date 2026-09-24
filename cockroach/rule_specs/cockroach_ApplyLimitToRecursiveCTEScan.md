# Name: ApplyLimitToRecursiveCTEScan
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/with.opt

ApplyLimitToRecursiveCTEScan updates the properties of the recursive with
scans in the input of a recursive CTE to reflect a limit that applies to
all iterations.

Extracted from `with.opt` (which defines multiple rules — implement specifically `ApplyLimitToRecursiveCTEScan`, not the other rules in that file):

```
# ApplyLimitToRecursiveCTEScan updates the properties of the recursive with
# scans in the input of a recursive CTE to reflect a limit that applies to
# all iterations.
[ApplyLimitToRecursiveCTEScan, Normalize]
(RecursiveCTE
    $binding:* & ^(HasBoundedCardinality $binding)
    $initial:* & (HasBoundedCardinality $initial)
    $recursive:* & (HasBoundedCardinality $recursive)
    $private:*
)
=>
(ApplyLimitToRecursiveCTEScan
    $binding
    $initial
    $recursive
    $private
)
```
