# ConvertZipArraysToValues

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 85  **Verification rounds used:** 6

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

The rule's soundness is a list-level identity — row-wise unrolling of an array by a set-returning zip function (unnest/json_array_elements, including null-padding of shorter arrays) ≡ an apply join with Values of that same array — and QED's model is pure bag semantics over uninterpreted functions, with no list type, no per-row element enumeration, and no ProjectSet/Unnest operator to carry such semantics. Extending the DSL would not close the gap either: QED's JSON format and the unmodifiable prover have no node or semantics for set-returning functions, and any core-language encoding that captures per-row/per-element multiplicity (a join or Correlate gated by an uninterpreted Elem predicate) collapses before() and after() into the same relation, proving a tautology rather than the actual rewrite. ```
