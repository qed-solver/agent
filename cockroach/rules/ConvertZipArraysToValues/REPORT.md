# ConvertZipArraysToValues

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 20  **Verification rounds used:** 1

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

The porter's stated reason is only an LLM context-length crash (no real analysis was run), but the UNSUPPORTED conclusion is nonetheless correct on the merits. The rule's entire correctness content is list semantics — unnest/json_array_elements expand a list into one row per element, and zip pairs elements index-wise with null-padding of shorter arrays — plus the CanConstructValuesFromZips guard, which depends on the *identity* of those specific functions; QED maps every type to an opaque integer with no list structure and treats all functions as uninterpreted, so it can model neither property. Additionally, the InnerJoinApply's right side is a Values relation *generated from the left row's array values* (data-dependent, correlated row generation), for which RuleScript has no operator — Correlate can only filter a fixed right relation by a predicate over the left row, and no DSL extension could help since QED's serialized Q-expression format and prover have no notion of "the elements of a column" — so even a narrower special case is inexpressible, not merely unprovable.
