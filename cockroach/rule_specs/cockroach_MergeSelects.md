# Name: MergeSelects
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

MergeSelects combines two nested Select operators into a single Select that
ANDs the filter conditions of the two Selects.

Extracted from `select.opt` (which defines multiple rules — implement specifically `MergeSelects`, not the other rules in that file):

```
# MergeSelects combines two nested Select operators into a single Select that
# ANDs the filter conditions of the two Selects.
[MergeSelects, Normalize]
(Select (Select $input:* $innerFilters:*) $filters:*)
=>
(Select $input (ConcatFilters $innerFilters $filters))
```
