# SimplifyCaseSingleFalsyBranch

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

SimplifyCaseSingleFalsyBranch replaces a CASE expression in a filter with a
falsy ELSE branch when it has a single WHEN branch that evaluates to False or
Null.

For example:

SELECT * FROM abc WHERE CASE WHEN a > 1 THEN false ELSE false END
=>
SELECT * FROM abc WHERE false

NOTE: The optimizer guarantees that CASE branches with side-effects will only
be evaluated if their conditions are true and the ELSE branch with
side-effects will only be evaluated if non of the branch conditions are true
(see props.VolatilitySet). Therefore, this rule is only valid when the ELSE
branch is leakproof. We currently only match constant False and Null values,
which are guaranteed to be leakproof.

Extracted from `select.opt` (which defines multiple rules — implement specifically `SimplifyCaseSingleFalsyBranch`, not the other rules in that file):

```
# SimplifyCaseSingleFalsyBranch replaces a CASE expression in a filter with a
# falsy ELSE branch when it has a single WHEN branch that evaluates to False or
# Null.
#
# For example:
#
#  SELECT * FROM abc WHERE CASE WHEN a > 1 THEN false ELSE false END
#  =>
#  SELECT * FROM abc WHERE false
#
# NOTE: The optimizer guarantees that CASE branches with side-effects will only
# be evaluated if their conditions are true and the ELSE branch with
# side-effects will only be evaluated if non of the branch conditions are true
# (see props.VolatilitySet). Therefore, this rule is only valid when the ELSE
# branch is leakproof. We currently only match constant False and Null values,
# which are guaranteed to be leakproof.
[SimplifyCaseSingleFalsyBranch, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Case
                (True)
                $whens:[ (When * (False | Null)) ]
                $orElse:(False | Null)
            )
        )
        ...
    ]
)
=>
(Select $input (ReplaceFiltersItem $filters $item $orElse))
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire soundness content is that CASE's three-valued branch selection makes `CASE WHEN c THEN falsy ELSE falsy END` never True in a filter context, but CASE/if-then-else is not in QED's interpreted scalar fragment (ite is only the internal 3VL encoding of And/Or/In/Some, per the paper), so a faithful before-side filter serializes the CASE as an uninterpreted operator that SMT is free to make true for some row and can never equate with the constant-False filter on the after side. Every alternative encoding fails for the same fundamental reason: abstracting the CASE to a shared uninterpreted symbol proves a vacuous identity, rewriting it out of And/Or/Not proves a different rule (constant-false absorption) rather than this one, and a DSL extension cannot help because the immutable prover has no CASE interpretation to key on — this is the "specific operator internal semantics QED cannot see through" limitation, so UNSUPPORTED is correct. ```
