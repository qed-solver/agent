# Name: EliminateExistsGroupBy
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateExistsGroupBy discards a non-scalar GroupBy input to the Exists
operator. While non-scalar GroupBy (or DistinctOn) can change row cardinality,
it always returns a non-empty set if its input is non-empty. Similarly, if its
input is empty, then it returns the empty set. Therefore, it's a no-op for
Exists.

NOTE: EnsureDistinctOn has the side effect of error'ing if the input has
duplicates, so do not eliminate it.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateExistsGroupBy`, not the other rules in that file):

```
# EliminateExistsGroupBy discards a non-scalar GroupBy input to the Exists
# operator. While non-scalar GroupBy (or DistinctOn) can change row cardinality,
# it always returns a non-empty set if its input is non-empty. Similarly, if its
# input is empty, then it returns the empty set. Therefore, it's a no-op for
# Exists.
#
# NOTE: EnsureDistinctOn has the side effect of error'ing if the input has
# duplicates, so do not eliminate it.
[EliminateExistsGroupBy, Normalize]
(Exists (GroupBy | DistinctOn $input:*) $existsPrivate:*)
=>
(Exists $input $existsPrivate)
```
