# FoldJSONAccessIntoValues

**Status:** SKIPPED
**Source backend:** CockroachDB

## Source rule (as given to the porter)

```
# Name: FoldJSONAccessIntoValues
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/project.opt

FoldJSONAccessIntoValues replaces a Values operator that has a single JSON
column and at least one row with a new Values operator that has a column for
each JSON key. This works as long as the surrounding Project does not
reference the original JSON column itself, since then it would be invalid to
eliminate that reference. However, references to fields within the JSON are
allowed, and are translated to the new unnested Values columns. The rule only
fires if all referenced JSON keys exist the first row, and if all JSON keys
from the first row also exist in all other rows.

FoldJSONAccessIntoValues has the side affect of pruning any keys that are not
present in the first Values row (since they are unreferenced).

This rule simplifies access to the Values operator in hopes of allowing other
rules to fire.

Example:

SELECT cust->'id' AS id, cust->'name' AS name
FROM (VALUES
('{"id": 1, "name": 'Drew'}'::JSON),
('{"id": 2, "name": 'Radu'}'::JSON),
('{"id": 3, "name": 'Rebecca'}'::JSON)
) v(cust)
=>
SELECT id, name
FROM (VALUES
(1::JSON, 'Drew'::JSON),
(2::JSON, 'Radu'::JSON),
(3::JSON, 'Rebecca'::JSON)
) v(id, name)

Extracted from `project.opt` (which defines multiple rules — implement specifically `FoldJSONAccessIntoValues`, not the other rules in that file):

```
# FoldJSONAccessIntoValues replaces a Values operator that has a single JSON
# column and at least one row with a new Values operator that has a column for
# each JSON key. This works as long as the surrounding Project does not
# reference the original JSON column itself, since then it would be invalid to
# eliminate that reference. However, references to fields within the JSON are
# allowed, and are translated to the new unnested Values columns. The rule only
# fires if all referenced JSON keys exist the first row, and if all JSON keys
# from the first row also exist in all other rows.
#
# FoldJSONAccessIntoValues has the side affect of pruning any keys that are not
# present in the first Values row (since they are unreferenced).
#
# This rule simplifies access to the Values operator in hopes of allowing other
# rules to fire.
#
# Example:
#
#   SELECT cust->'id' AS id, cust->'name' AS name
#   FROM (VALUES
#      ('{"id": 1, "name": 'Drew'}'::JSON),
#      ('{"id": 2, "name": 'Radu'}'::JSON),
#      ('{"id": 3, "name": 'Rebecca'}'::JSON)
#   ) v(cust)
#   =>
#   SELECT id, name
#   FROM (VALUES
#      (1::JSON, 'Drew'::JSON),
#      (2::JSON, 'Radu'::JSON),
#      (3::JSON, 'Rebecca'::JSON)
#   ) v(id, name)
#
[FoldJSONAccessIntoValues, Normalize]
(Project
    $input:(Values [ * ... ]) &
        (ColsAreLenOne (OutputCols $input))
    $projections:* &
        (CanUnnestJSONFromValues
            $input
            $projections
            $col:(SingleColFromSet (OutputCols $input))
        )
    $passthrough:* & (ColsAreEmpty $passthrough)
)
=>
(Project
    (UnnestJSONFromValues
        $input
        $jsonCols:(MakeColsForUnnestJSON $input $col)
    )
    (FoldJSONFieldAccess $projections $jsonCols $col $input)
    $passthrough
)
```
```

## Reason

The rule unnests a single-column Values whose rows are JSON objects into one Values column per JSON key, and rewrites -> field accesses to read those columns. QED has no JSON object value and no key-addressed field access: Values content can only carry literal constants, and a JSON fetch is just an uninterpreted function of an opaque column. The new columns are derived from the keys and values inside each JSON document, which the DSL cannot express, so the after-side Values cannot be built from the before-side one. The only encoding the porter reached splits a Values into a union of single-row Values while the projection still reads the original column. That proves only that splitting a Values by rows commutes with a projection, which is unrelated to this rule, and the verifier rejected it for that reason. The key-presence guards (all referenced keys in the first row, all first-row keys in every row) cannot be stated either. Recorded as skipped rather than published as a proof of something else.
