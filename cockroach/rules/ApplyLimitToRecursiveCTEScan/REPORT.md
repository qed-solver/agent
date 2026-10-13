# ApplyLimitToRecursiveCTEScan

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 6  **Verification rounds used:** 1

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

The rule's precondition is a set of subplan cardinality side conditions (HasBoundedCardinality) and its effect is a backend marker operator that changes operator *properties*, not the produced relation — RuleScript's before/after patterns can only express row-level relational structure, with no mechanism to state cardinality-bound side conditions on subexpressions or to capture property propagation at all. Additionally, the operators the rule manipulates are outside QED's model: RecursiveCTE/fixpoint has no bag-semantic meaning, and Limit is explicitly among the operators QED cannot reason about, so neither side of the rewrite is encodable; extend_dsl_file cannot close this gap because the trusted Rust prover (off-limits for modification) has no Q-expression semantics for recursion or ordering/limit. ```
