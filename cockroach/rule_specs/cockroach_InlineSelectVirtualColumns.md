# Name: InlineSelectVirtualColumns
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/inline.opt

InlineSelectVirtualColumns pushes Select filters referencing virtual columns
into a Project by inlining the virtual column expressions. This makes the
Select independent of the Project. Because these filters are pushed below the
Project, exploration rules that match on the (Select (Scan)) pattern can
generate plans that use indexes on virtual columns.

Filters on non-virtual projected columns are not inlined because the
expression would be executed twice (once in the filter and once in the
projection), adding overhead without any chance of a secondary index on a
virtual column being used in the optimized plan.

Notice that this rule is similar to PushSelectIntoInlinableProject. The key
difference is that PushSelectIntoInlinableProject only inlines simple
expressions that will add negligible overhead when computing twice.
Conversely, InlineSelectVirtualColumns does not discriminate by the type of
expression. It will inline all virtual columns in the hopes that inlining will
lead to a query plan that uses a virtual column index.

Also, PushSelectIntoInlinableProject will inline filters if and only if all of
the filter items are inlinable (by its definition), whereas
InlineSelectVirtualColumns will split the input filters into two groups: one
to inline below the Project, and one to leave above the Project. This allows
filters on virtual columns to be pushed down in more cases.

For example, consider the table and query:

CREATE TABLE t (
a INT,
b INT,
v INT AS (abs(a)) VIRTUAL,
INDEX (v)
)
SELECT v, w FROM (
SELECT v, abs(b) AS w FROM t
) WHERE v = 5 AND w = 10

The partially normalized expression for the SELECT query before
InlineSelectVirtualColumns is applied is:

select
├── columns: v:3 w:6
├── project
│    ├── columns: w:6 v:3
│    ├── scan t
│    │    └── columns: a:1 b:2
│    └── projections
│         ├── abs(b:2) [as=w:6]
│         └── abs(a:1) [as=v:3]
└── filters
├── v:3 = 5
└── w:6 = 10

InlineSelectVirtualColumns will push only the (v = 5) filter below the Project
as (abs(a) = 5) because v is a virtual column. The (w = 10) filter remains
above the Project. Notice the (Select (Scan)) pattern that will allow a
constrained scan over the secondary index to be generated.

select
├── columns: v:3 w:6
├── project
│    ├── columns: w:6 v:3
│    ├── select
│    │    ├── columns: a:1 b:2
│    │    ├── scan t
│    │    │    └── columns: a:1 b:2
│    │    └── filters
│    │         └── abs(a:1) = 5
│    └── projections
│         ├── abs(b:2) [as=w:6]
│         └── abs(a:1) [as=v:3]
└── filters
└── w:6 = 10

This rule has no explicit priority so that it runs before
PushSelectIntoInlinableProject (which is low priority). It must run before
PushSelectIntoInlinableProject in order to match the (Select (Project (Scan)))
pattern which is produced by optbuilder for a filter on a table with virtual
columns.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineSelectVirtualColumns`, not the other rules in that file):

```
# InlineSelectVirtualColumns pushes Select filters referencing virtual columns
# into a Project by inlining the virtual column expressions. This makes the
# Select independent of the Project. Because these filters are pushed below the
# Project, exploration rules that match on the (Select (Scan)) pattern can
# generate plans that use indexes on virtual columns.
#
# Filters on non-virtual projected columns are not inlined because the
# expression would be executed twice (once in the filter and once in the
# projection), adding overhead without any chance of a secondary index on a
# virtual column being used in the optimized plan.
#
# Notice that this rule is similar to PushSelectIntoInlinableProject. The key
# difference is that PushSelectIntoInlinableProject only inlines simple
# expressions that will add negligible overhead when computing twice.
# Conversely, InlineSelectVirtualColumns does not discriminate by the type of
# expression. It will inline all virtual columns in the hopes that inlining will
# lead to a query plan that uses a virtual column index.
#
# Also, PushSelectIntoInlinableProject will inline filters if and only if all of
# the filter items are inlinable (by its definition), whereas
# InlineSelectVirtualColumns will split the input filters into two groups: one
# to inline below the Project, and one to leave above the Project. This allows
# filters on virtual columns to be pushed down in more cases.
#
# For example, consider the table and query:
#
#   CREATE TABLE t (
#     a INT,
#     b INT,
#     v INT AS (abs(a)) VIRTUAL,
#     INDEX (v)
#   )
#   SELECT v, w FROM (
#     SELECT v, abs(b) AS w FROM t
#   ) WHERE v = 5 AND w = 10
#
# The partially normalized expression for the SELECT query before
# InlineSelectVirtualColumns is applied is:
#
#   select
#    ├── columns: v:3 w:6
#    ├── project
#    │    ├── columns: w:6 v:3
#    │    ├── scan t
#    │    │    └── columns: a:1 b:2
#    │    └── projections
#    │         ├── abs(b:2) [as=w:6]
#    │         └── abs(a:1) [as=v:3]
#    └── filters
#         ├── v:3 = 5
#         └── w:6 = 10
#
# InlineSelectVirtualColumns will push only the (v = 5) filter below the Project
# as (abs(a) = 5) because v is a virtual column. The (w = 10) filter remains
# above the Project. Notice the (Select (Scan)) pattern that will allow a
# constrained scan over the secondary index to be generated.
#
#   select
#    ├── columns: v:3 w:6
#    ├── project
#    │    ├── columns: w:6 v:3
#    │    ├── select
#    │    │    ├── columns: a:1 b:2
#    │    │    ├── scan t
#    │    │    │    └── columns: a:1 b:2
#    │    │    └── filters
#    │    │         └── abs(a:1) = 5
#    │    └── projections
#    │         ├── abs(b:2) [as=w:6]
#    │         └── abs(a:1) [as=v:3]
#    └── filters
#         └── w:6 = 10
#
# This rule has no explicit priority so that it runs before
# PushSelectIntoInlinableProject (which is low priority). It must run before
# PushSelectIntoInlinableProject in order to match the (Select (Project (Scan)))
# pattern which is produced by optbuilder for a filter on a table with virtual
# columns.
[InlineSelectVirtualColumns, Normalize]
(Select
    (Project
        $scan:(Scan $scanPrivate:*)
        $projections:*
        $passthrough:*
    )
    $filters:* &
        ^(ColsAreEmpty
            $virtualColumns:(VirtualColumns $scanPrivate)
        ) &
        ^(IsFilterEmpty
            $inlinableFilters:(InlinableVirtualColumnFilters
                $filters
                $virtualColumns
            )
        )
)
=>
(Select
    (Project
        (Select
            $scan
            (InlineSelectProject $inlinableFilters $projections)
        )
        $projections
        $passthrough
    )
    (DiffFilters $filters $inlinableFilters)
)
```
