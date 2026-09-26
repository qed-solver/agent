# EliminateCaseTrailingFalsyBranch

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 65  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

EliminateCaseTrailingFalsyBranch removes a trailing WHEN branch that results
in False or Null from a CASE expression in a filter when the ELSE branch also
results in False or Null. This is valid because the trailing WHEN branch
cannot change the result of the CASE expression, regardless of the results of
its condition.

For example:

SELECT * FROM abc WHERE CASE WHEN a > 1 THEN true WHEN b > 2 THEN false ELSE false END
=>
SELECT * FROM abc WHERE CASE WHEN a > 1 THEN true ELSE false END

NOTE: The optimizer guarantees that CASE branches with side-effects will only
be evaluated if their conditions are true and the ELSE branch with
side-effects will only be evaluated if non of the branch conditions are true
(see props.VolatilitySet). Therefore, this rule is only valid when the ELSE
branch is leakproof. We currently only match constant False and Null values,
which are guaranteed to be leakproof.

Extracted from `select.opt` (which defines multiple rules — implement specifically `EliminateCaseTrailingFalsyBranch`, not the other rules in that file):

```
# EliminateCaseTrailingFalsyBranch removes a trailing WHEN branch that results
# in False or Null from a CASE expression in a filter when the ELSE branch also
# results in False or Null. This is valid because the trailing WHEN branch
# cannot change the result of the CASE expression, regardless of the results of
# its condition.
#
# For example:
#
#  SELECT * FROM abc WHERE CASE WHEN a > 1 THEN true WHEN b > 2 THEN false ELSE false END
#  =>
#  SELECT * FROM abc WHERE CASE WHEN a > 1 THEN true ELSE false END
#
# NOTE: The optimizer guarantees that CASE branches with side-effects will only
# be evaluated if their conditions are true and the ELSE branch with
# side-effects will only be evaluated if non of the branch conditions are true
# (see props.VolatilitySet). Therefore, this rule is only valid when the ELSE
# branch is leakproof. We currently only match constant False and Null values,
# which are guaranteed to be leakproof.
[EliminateCaseTrailingFalsyBranch, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Case
                (True)
                $whens:[ ... (When * (False | Null)) ] &
                    (LenGT $whens 1)
                $orElse:(False | Null)
            )
        )
        ...
    ]
)
=>
(Select
    $input
    (ReplaceFiltersItem
        $filters
        $item
        (Case (True) (DropLast $whens) $orElse)
    )
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on CASE's own 3-valued if-then-else semantics (a trailing WHEN with a False/Null THEN is dead only because the ELSE is likewise False/Null in a filter context), but CASE is not in QED's modeled scalar fragment (And/Or/Not, comparisons, In/Some) — the paper's ite is only an internal encoding device for 3VL and NULL lifting, not an operator a pattern can name, so the two CASEs serialize as uninterpreted QOp applications of different arity (2N+1 vs 2N−1 arguments) that SMT congruence can never equate. Extending the Java DSL with a CASE builder cannot close this, because JSONSerializer would only emit a generic named call and the immutable Rust prover has no CASE interpretation to apply — this is squarely the "operator-specific internal semantics QED cannot see through as an uninterpreted function" limitation. No faithful narrower special case (e.g. exactly two whens, False-only branches) escapes it, since both pattern sides still contain the unmodeled CASE term, and any encoding that abstracts the CASEs away to shared symbols would prove a vacuous identity or a different rule rather than this one. ```
