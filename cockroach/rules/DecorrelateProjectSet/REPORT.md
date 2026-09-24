# DecorrelateProjectSet

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 20  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

DecorrelateProjectSet pulls an input relation outside of a ProjectSet if the
input is not correlated with any of the functions in the ProjectSet. The
input is then cross-joined with a new ProjectSet, which contains the same
functions but has an empty input (a unary VALUES node).

The advantage of this transformation is it means each of the functions in the
ProjectSet only need to be executed once in total, instead of once for each
input row.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `DecorrelateProjectSet`, not the other rules in that file):

```
# DecorrelateProjectSet pulls an input relation outside of a ProjectSet if the
# input is not correlated with any of the functions in the ProjectSet. The
# input is then cross-joined with a new ProjectSet, which contains the same
# functions but has an empty input (a unary VALUES node).
#
# The advantage of this transformation is it means each of the functions in the
# ProjectSet only need to be executed once in total, instead of once for each
# input row.
[DecorrelateProjectSet, Normalize]
(ProjectSet
    $input:^(Values)
    $zip:* & ^(IsZipCorrelated $zip (OutputCols $input))
)
=>
(InnerJoin
    $input
    (ProjectSet (ConstructNoColsRow) $zip)
    []
    (EmptyJoinPrivate)
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness depends entirely on the structural side condition that the set-returning ("zip") functions reference no input column (non-correlation), a free-variable/dependency property that QED — a bag-semantic equivalence prover over uninterpreted symbols — has no way to express or assume; "ProjectSet"/set-returning functions also have no operator in the DSL core language (no `RelRN` method, no `JSONSerializer` case) and no semantic model in QED's unmodifiable prover. Any faithful model of the SRF as an uninterpreted row-dependent relation makes the general rule false (hence unprovable), while a model that assumes independence collapses before and after to the identical cross join R × S — a trivially provable but vacuous identity containing no ProjectSet to actually transform.
