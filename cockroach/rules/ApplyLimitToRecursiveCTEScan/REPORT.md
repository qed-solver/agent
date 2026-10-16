# ApplyLimitToRecursiveCTEScan

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 8  **Verification rounds used:** 1

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

ApplyLimitToRecursiveCTEScan is a pure optimizer-metadata rule — it only re-derives and re-attaches a bounded-cardinality property to the recursive CTE's working-table binding, leaving the output bag of rows identical before and after — and it operates on Optgen-custom operators (RecursiveCTE, WithScan, FakeRel) where a recursive CTE is a fixed point that cannot be written as a finite relational-algebra pattern in the core language. RuleScript/QED has no representation of cardinality/row-count bounds (a `Scan` only carries a `unique` key flag), and QED does not model the ordering/`Limit` (list) semantics the "limit that applies to all iterations" bound depends on, so the rule's distinguishing content is unrepresentable and any before/after pair QED could even accept would be vacuous. ```
