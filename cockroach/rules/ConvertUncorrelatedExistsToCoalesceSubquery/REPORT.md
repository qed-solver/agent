# ConvertUncorrelatedExistsToCoalesceSubquery

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

ConvertUncorrelatedExistsToCoalesceSubquery converts an uncorrelated Exists
expression to a Coalesce expression with a Subquery. For example:

SELECT EXISTS (
SELECT * FROM b
) FROM a
=>
SELECT COALESCE(
(SELECT true FROM (SELECT * FROM b) LIMIT 1),
false
) FROM a

This transformation simplifies execbuilder and execution code - we do not need
a special execution mode for uncorrelated Exists.

Note: We can use an empty ordering for the Limit because Exists never have an
ordering.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `ConvertUncorrelatedExistsToCoalesceSubquery`, not the other rules in that file):

```
# ConvertUncorrelatedExistsToCoalesceSubquery converts an uncorrelated Exists
# expression to a Coalesce expression with a Subquery. For example:
#
#   SELECT EXISTS (
#     SELECT * FROM b
#   ) FROM a
#   =>
#   SELECT COALESCE(
#     (SELECT true FROM (SELECT * FROM b) LIMIT 1),
#     false
#   ) FROM a
#
# This transformation simplifies execbuilder and execution code - we do not need
# a special execution mode for uncorrelated Exists.
#
# Note: We can use an empty ordering for the Limit because Exists never have an
# ordering.
[ConvertUncorrelatedExistsToCoalesceSubquery, Normalize]
(Exists $input:* & ^(HasOuterCols $input) $existsPrivate:*)
=>
(Coalesce
    [
        (Subquery
            (Project
                (Limit
                    $input
                    (IntConst (DInt 1))
                    (EmptyOrdering)
                )
                [ (ProjectionsItem (True) (MakeBoolCol)) ]
                (MakeEmptyColSet)
            )
            (EmbeddedSubqueryPrivate $existsPrivate)
        )
        (False)
    ]
)
```
```

## Independent verifier review

**Verdict:** AGREE

Both sides of the rewrite are scalar expressions over nested sub-relations — an EXISTS on the left, and COALESCE of a scalar subquery (a Project over a LIMIT) and false on the right — and QED's decision procedure cannot decide scalar expressions that contain a sub-relation at all (the prover marks such nodes non-complete/unhandled), so it has no way to even state the equivalence between the two forms. The rule's soundness also load-bears on LIMIT's row-cap (at most one row, non-empty iff input non-empty), the empty-scalar-subquery→NULL convention, and COALESCE's first-non-null behavior, all of which are bespoke uninterpreted semantics outside QED's bag algebra (and Limit has no bag-semantic meaning per its own evaluation), so no DSL-builder extension can supply them — consistent with the prior SubQueryRemove check where the Exists builder was added and the prover still rejected the encoding. ```
