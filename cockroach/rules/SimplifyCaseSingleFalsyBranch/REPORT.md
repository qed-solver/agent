# SimplifyCaseSingleFalsyBranch

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 47  **Verification rounds used:** 3
**Scope detail:** assumes the single WHEN branch's THEN value is the FALSE constant and the ELSE value is the NULL constant (one of the four False|Null combinations the rule matches)


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

**Verdict:** CONFIRMED

The encoding is non-vacuous and faithful: the WHEN condition is a proper uninterpreted predicate, the 3VL expansion (cond∧False)∨(¬cond∧Null) matches the CASE for all three values of cond, both sides are genuinely all-dropping filters, and the leakproof precondition is honored via constants. The SCOPE line honestly discloses the specific False/Null restriction (the only combination expressible via AND/OR/NOT), and the only further implicit narrowing — a single-item filter list — is compositionally trivial since both sides are empty regardless of other conjuncts.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 0
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 392083
  }
}
```
