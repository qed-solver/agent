# InlineExistsSelectTuple

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 26  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

InlineExistsSelectTuple splits a tuple equality filter into multiple
(per-column) equalities, in the case where the tuple on one side is being
projected.

We are specifically handling the case when this is under Exists because we
don't have to keep the same output columns for the Select. This case is
important because it is produced for an IN subquery:

SELECT * FROM ab WHERE (a, b) IN (SELECT c, d FROM cd)

Without this rule, we would not be able to produce a lookup join plan for such
a query.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `InlineExistsSelectTuple`, not the other rules in that file):

```
# InlineExistsSelectTuple splits a tuple equality filter into multiple
# (per-column) equalities, in the case where the tuple on one side is being
# projected.
#
# We are specifically handling the case when this is under Exists because we
# don't have to keep the same output columns for the Select. This case is
# important because it is produced for an IN subquery:
#
#   SELECT * FROM ab WHERE (a, b) IN (SELECT c, d FROM cd)
#
# Without this rule, we would not be able to produce a lookup join plan for such
# a query.
#
[InlineExistsSelectTuple, Normalize]
(Exists
    (Select
        (Project
            $input:*
            [
                ...
                (ProjectionsItem $tuple:(Tuple) $tupleCol:*)
                ...
            ]
        )
        $filters:[
            ...
            $item:(FiltersItem
                (Eq
                    # CommuteVar ensures that the variable is on the left.
                    (Variable
                        $varCol:* &
                            (EqualsColumn $varCol $tupleCol)
                    )
                    $rhs:(Tuple) &
                        (TuplesHaveSameLength $tuple $rhs)
                )
            )
            ...
        ]
    )
    $existsPrivate:*
)
=>
(Exists
    (Select
        $input
        (ConcatFilters
            (RemoveFiltersItem $filters $item)
            (SplitTupleEq $tuple $rhs)
        )
    )
    $existsPrivate
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's core semantic content is the definitional expansion of one tuple-equality predicate into a conjunction of per-component equalities, but QED models every scalar operator as an uninterpreted function over a flat value domain — RelType.java states all types are lowered to integers for the prover, JSONSerializer.java has no tuple-construction or tuple-comparison node (scalars are only columns, literals, uninterpreted calls, and field accesses), and the Rust prover is off-limits — so there is no JSON/SMT encoding under which `tEq(T(c,d), T(a,b))` is related to `c=a ∧ d=b`; a countermodel (tEq true, T arbitrary, components unequal) defeats it. A `extend_dsl_file` edit can only add Java builders that ultimately serialize to an uninterpreted `RexCall`, so this is a fundamental QED limitation, not a missing builder; the only provable encoding would pre-split the before side, which stops matching the rule's pattern (an unsplit tuple `Eq`) and degenerates into the separate `EliminateExistsProject` + filter-pushdown rule that is itself a distinct rule in
