# ConvertZipArraysToValues

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/project_set.opt

ConvertZipArraysToValues applies the unnest, json_array_elements and
jsonb_array_elements zip functions to array inputs, converting them into a
Values operator within an InnerJoinApply. This allows Values and decorrelation
rules to fire. It is especially useful in cases where the contents are passed
as a PREPARE parameter, such as:

SELECT * FROM xy WHERE y IN unnest($1)
or:
SELECT json_array_elements($1)

The replace pattern is equivalent to the match pattern because the
InnerJoinApply outputs every value in the array for every row in the input,
and outputs nulls to pad shorter arrays. It also supports correlation between
the array arguments and the input expression.

Extracted from `project_set.opt` (which defines multiple rules — implement specifically `ConvertZipArraysToValues`, not the other rules in that file):

```
# ConvertZipArraysToValues applies the unnest, json_array_elements and
# jsonb_array_elements zip functions to array inputs, converting them into a
# Values operator within an InnerJoinApply. This allows Values and decorrelation
# rules to fire. It is especially useful in cases where the contents are passed
# as a PREPARE parameter, such as:
#
#   SELECT * FROM xy WHERE y IN unnest($1)
# or:
#   SELECT json_array_elements($1)
#
# The replace pattern is equivalent to the match pattern because the
# InnerJoinApply outputs every value in the array for every row in the input,
# and outputs nulls to pad shorter arrays. It also supports correlation between
# the array arguments and the input expression.
[ConvertZipArraysToValues, Normalize]
(ProjectSet $input:* $zip:* & (CanConstructValuesFromZips $zip))
=>
(InnerJoinApply
    $input
    (ConstructValuesFromZips $zip)
    []
    (EmptyJoinPrivate)
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's core is a set-returning/row-generating operation — `ProjectSet` expanding an array column (`unnest`/`json_array_elements`) into one row per element — plus a correlated `InnerJoinApply` whose right side is a dynamic Values whose rows are that specific left row's array elements. QED only models bag semantics with *scalar* uninterpreted functions and has no list/array type semantics (all types flatten to integers in `RelType`), no row-generating operator, and `JSONSerializer`/the Q-expression format carry no construct for a Values whose contents are derived from a correlated left row (the DSL's `Correlate` only filters a *fixed* right relation). This is a fundamental QED limitation (unmodelable list/row-generation semantics, and the Rust prover can't be modified), not a missing builder `extend_dsl_file` could close, so no non-vacuous special case is provable.
