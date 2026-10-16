# TryDecorrelateUnion

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 55  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateUnion replaces a Union/UnionAll beneath a ScalarGroupBy with a
cross-join (InnerJoin on True) between two ScalarGroupBy operators. A Project
operator coalesces columns from each join input to produce the final result.
This transformation applies when the ScalarGroupBy has only "any-not-null"
aggregations, which select an arbitrary non-null value from the input column.

Here's a simplified example:

scalar-group-by
├── union-all
│    ├── scan foo
│    └── scan bar (has-outer-cols)
└── aggregations
└── any-not-null
=>
project
├── inner-join (cross)
│    ├── scalar-group-by
│    │    └── scan foo
│    ├── scalar-group-by
│    │    └── scan bar
│    └── filters (true)
└── projections
└── coalesce

This situation occurs after a correlated EXISTS subquery containing a Union is
hoisted. Note that TryDecorrelateUnion does not itself decorrelate the Union,
but makes it easier for other rules to do so.

NOTE: the outer Project operator is necessary just in case the ScalarGroupBy
is synthesizing new columns, despite using any-not-null aggregations.
NOTE: TryDecorrelateUnion should be ordered before TryDecorrelateScalarGroupBy
to ensure that Union operators have a chance to be decorrelated.

TODO(drewk): We could extend this rule to apply to other aggregations; for
example, for a count() we can sum the counts taken on each side of the join.
TODO(drewk): We could extend this rule to handle other set operations. For
example, ExceptAll could become an AntiJoin.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateUnion`, not the other rules in that file):

```
# TryDecorrelateUnion replaces a Union/UnionAll beneath a ScalarGroupBy with a
# cross-join (InnerJoin on True) between two ScalarGroupBy operators. A Project
# operator coalesces columns from each join input to produce the final result.
# This transformation applies when the ScalarGroupBy has only "any-not-null"
# aggregations, which select an arbitrary non-null value from the input column.
#
# Here's a simplified example:
#
#   scalar-group-by
#    ├── union-all
#    │    ├── scan foo
#    │    └── scan bar (has-outer-cols)
#    └── aggregations
#         └── any-not-null
#   =>
#   project
#    ├── inner-join (cross)
#    │    ├── scalar-group-by
#    │    │    └── scan foo
#    │    ├── scalar-group-by
#    │    │    └── scan bar
#    │    └── filters (true)
#    └── projections
#         └── coalesce
#
# This situation occurs after a correlated EXISTS subquery containing a Union is
# hoisted. Note that TryDecorrelateUnion does not itself decorrelate the Union,
# but makes it easier for other rules to do so.
#
# NOTE: the outer Project operator is necessary just in case the ScalarGroupBy
# is synthesizing new columns, despite using any-not-null aggregations.
# NOTE: TryDecorrelateUnion should be ordered before TryDecorrelateScalarGroupBy
# to ensure that Union operators have a chance to be decorrelated.
#
# TODO(drewk): We could extend this rule to apply to other aggregations; for
# example, for a count() we can sum the counts taken on each side of the join.
# TODO(drewk): We could extend this rule to handle other set operations. For
# example, ExceptAll could become an AntiJoin.
[TryDecorrelateUnion, Normalize]
(ScalarGroupBy
    $input:(Union | UnionAll $left:* $right:* $unionPrivate:*) &
        (HasOuterCols $input)
    $aggs:* & (AreAllAnyNotNullAggs $aggs)
    $private:*
)
=>
(Project
    (Project
        (InnerJoin
            (MakeAnyNotNullScalarGroupBy $left)
            (MakeAnyNotNullScalarGroupBy $right)
            []
            (EmptyJoinPrivate)
        )
        (MakeCoalesceProjectionsForUnion $unionPrivate)
        (MakeEmptyColSet)
    )
    (ConvertAnyNotNullAggsToProjections $aggs)
    (IntersectionCols
        (GroupingOutputCols $private $aggs)
        (OutputCols $input)
    )
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness hinges on the identity ANY_NOT_NULL(A ⊎ B) = COALESCE(ANY_NOT_NULL(A), ANY_NOT_NULL(B)), which requires (a) an axiom decomposing an uninterpreted aggregate over a union bag into the same aggregate over its parts — QED explicitly models each aggregate as a single uninterpreted function of the entire input bag with no such decomposition axiom — and (b) COALESCE value-selection semantics, which is absent from the core language and, even if introduced as another uninterpreted scalar, would remain unrelated to the aggregate symbol in QED's SMT encoding; this is a fundamental prover limitation (no aggregate algebra beyond bag-equality-of-input), not a closable DSL gap, so no faithful non-vacuous encoding can be proved. ```
