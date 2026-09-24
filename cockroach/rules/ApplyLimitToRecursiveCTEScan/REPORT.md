# ApplyLimitToRecursiveCTEScan

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 20  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/with.opt

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
```

## Independent verifier review

**Verdict:** AGREE

This is a logical-property (cardinality-bound) rule, not a bag-equivalence rule: it rewrites a RecursiveCTE into the backend-specific operator ApplyLimitToRecursiveCTEScan whose entire effect is to update cardinality-bound metadata on the recursive scans (with HasBoundedCardinality / ^HasBoundedCardinality as side conditions on subplans), and RuleScript has no notion of operator properties, no way to state cardinality constraints on subexpressions, and no such operator in its core language. The soundness of the transform also rests on recursive-CTE fixpoint/iteration semantics plus Limit semantics (capping the scans each iteration), and QED only decides bag equivalence of finite relational expressions — recursion/fixpoints and Sort/Limit/Ordering have no bag-semantic model in it. This is a fundamental modeling gap, not a missing builder: even a RecursiveCTE node added via extend_dsl_file would have no case in JSONSerializer and no semantics in the unchangeable Rust prover, so no encoding (full or special case) is expressible. ```
