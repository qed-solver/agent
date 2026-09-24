# Name: EliminateRedundantBarrier
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/barrier.opt

EliminateRedundantBarrier removes a Barrier operator when it wraps another
identical Barrier. This deduplication avoids unnecessary nesting of
equivalent Barriers. The rule applies only when both Barrier operators have
the same configuration.

Extracted from `barrier.opt` (which defines multiple rules — implement specifically `EliminateRedundantBarrier`, not the other rules in that file):

```
# EliminateRedundantBarrier removes a Barrier operator when it wraps another
# identical Barrier. This deduplication avoids unnecessary nesting of
# equivalent Barriers. The rule applies only when both Barrier operators have
# the same configuration.
[EliminateRedundantBarrier, Normalize]
(Barrier
    (Barrier $input:* $innerLeakproofPermeable:*)
    $outerLeakproofPermeable:* &
        (EqualsBool
            $innerLeakproofPermeable
            $outerLeakproofPermeable
        )
)
=>
(Barrier $input $outerLeakproofPermeable)
```
