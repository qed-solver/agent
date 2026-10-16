# SimplifyCaseSingleTruthyBranch

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 45  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

SimplifyCaseSingleTruthyBranch replaces a CASE expression in a filter with its
condition when it has a single WHEN branch that evaluates to True, and an ELSE
branch that evaluates to False or Null.

For example:

SELECT * FROM abc WHERE CASE WHEN a > 5 THEN true ELSE false END
=>
SELECT * FROM abc WHERE a > 5

Extracted from `select.opt` (which defines multiple rules — implement specifically `SimplifyCaseSingleTruthyBranch`, not the other rules in that file):

```
# SimplifyCaseSingleTruthyBranch replaces a CASE expression in a filter with its
# condition when it has a single WHEN branch that evaluates to True, and an ELSE
# branch that evaluates to False or Null.
#
# For example:
#
#  SELECT * FROM abc WHERE CASE WHEN a > 5 THEN true ELSE false END
#  =>
#  SELECT * FROM abc WHERE a > 5
#
[SimplifyCaseSingleTruthyBranch, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Case
                (True)
                $whens:[ (When $cond:* (True)) ]
                (False | Null)
            )
        )
        ...
    ]
)
=>
(Select $input (ReplaceFiltersItem $filters $item $cond))
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on the CASE operator's branch-selection/3-valued semantics (`CASE WHEN c THEN true ELSE false/NULL END` retains a row iff `c` is true), but QED's interpreted scalar fragment only models the connectives and literals (AND/OR/NOT/TRUE/FALSE) — a CASE call, which every instance of this rule necessarily carries on the before side, down to the narrowest special cases, serializes via the `Rex` visitor as a name-keyed call that the fixed Rust prover treats as an uninterpreted function, so the before predicate has no modeled relation to `cond` and no universal SMT proof exists. No `extend_dsl_file` change on the Java side can close the gap, since operator interpretation is keyed by name inside the immutable prover, and this matches the AGREE-verified SKIPPED precedents for the same rule family (datafusion `CaseFirstBranchAlwaysTrue`, cockroach `EliminateCaseTrailingFalsyBranch`). ```
