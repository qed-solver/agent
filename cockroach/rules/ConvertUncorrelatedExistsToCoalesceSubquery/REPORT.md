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

The after-side's validity rests entirely on `Limit(Q,1)` having 0 rows iff `Q` is empty and 1 row iff non-empty, plus scalar-subquery NULL-when-empty and `Coalesce`'s null-skip — but QED assigns `Limit` no bag-semantic meaning and treats scalar subqueries/`Coalesce` as uninterpreted atoms with no cardinality or NULL axioms, so SMT can always instantiate the after-side atom to disagree with the before-side's non-emptiness. No `extend_dsl_file` gap can close this because the frozen Rust prover has no non-emptiness/cardinality semantics for `Limit` or scalar subqueries to expose — the identical Spark `RewriteNonCorrelatedExists` was independently verified on the same ground.
