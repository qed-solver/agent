# InlineExistsSelectTuple

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 44  **Verification rounds used:** 3

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

The rule's core claim is that one tuple-equality predicate is equivalent to a conjunction of per-column equalities, but QED models every scalar value as a flat (integer) value and every comparison as an uninterpreted predicate, with no tuple/row value node and no axiom decomposing a composite value's equality into its components — so the before-side tuple-eq is an independent uninterpreted symbol that QED cannot entail from (or to) the after-side conjunction, which is exactly its documented "predicate inference/entailment between independent symbols" limitation. Because the prover's value model is flat and it is the unchanging trusted arbiter, no `extend_dsl_file` change can add tuple-decomposition semantics, and the only degenerate special case (an arity-1 tuple) collapses to before == after, so no non-trivial provable instance exists. ```
