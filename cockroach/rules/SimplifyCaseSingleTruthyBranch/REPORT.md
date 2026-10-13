# SimplifyCaseSingleTruthyBranch

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2

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

The rule's soundness rests entirely on the three-valued branch-selection semantics of the CASE operator (in a filter, CASE TRUE WHEN cond THEN TRUE ELSE {FALSE|NULL} END keeps a row iff cond is TRUE), but QED's interpreted scalar fragment contains only the boolean connectives/literals — ite appears only as their internal 3VL encoding, not as a nameable operator — so any faithful before-side encoding must serialize CASE as an uninterpreted operator symbol, and SMT cannot equate Filter(case(cond), R) with Filter(cond, R): an uninterpreted function admits countermodels (e.g. mapping a non-TRUE cond to TRUE) that SMT can refute no assumption away. This is the "backend operator's specific internal semantics" limitation, not a DSL gap — an `extend_dsl_file` constructor could only emit another operator name the fixed Rust prover treats as uninterpreted, so no faithful encoding (or narrower special case, since even a non-NULL cond can't be named through the operator) can make the proof go through. ```
