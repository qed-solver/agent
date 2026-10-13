# HoistProjectSetSubquery

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 31  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistProjectSetSubquery extracts subqueries from zipped functions and joins
them with the ProjectSet operator's input. This and other subquery hoisting
patterns create a single, top-level relational query with no nesting.

This rule is marked as low priority for the same reason as HoistSelectExists.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistProjectSetSubquery`, not the other rules in that file):

```
# HoistProjectSetSubquery extracts subqueries from zipped functions and joins
# them with the ProjectSet operator's input. This and other subquery hoisting
# patterns create a single, top-level relational query with no nesting.
#
# This rule is marked as low priority for the same reason as HoistSelectExists.
[HoistProjectSetSubquery, Normalize, LowPriority]
(ProjectSet
    $input:*
    $zip:[ ... $item:* & (HasHoistableSubquery $item) ... ]
)
=>
(HoistProjectSetSubquery $input $zip)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's central operator, ProjectSet (set-returning/zip row generation), has no model in QED's bag-semantic core — a set-returning function maps one input row to a variable number of output rows, which is list semantics QED explicitly does not support, and the JSON theory carries no ProjectSet operator. Because both sides depend on it (the before side is a ProjectSet over a zip function containing the subquery; the after side hoists that subquery into a join but still feeds the same row-generating ProjectSet), the row-count-preserving equivalence is unprovable — a genuine QED limitation, not a missing DSL builder.
