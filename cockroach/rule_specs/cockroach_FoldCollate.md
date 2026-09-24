# Name: FoldCollate
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

FoldCollate converts a Collate expr over an uncollated string into a collated
string constant.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `FoldCollate`, not the other rules in that file):

```
# FoldCollate converts a Collate expr over an uncollated string into a collated
# string constant.
[FoldCollate, Normalize]
(Collate $input:(Const) $locale:*)
=>
(CastToCollatedString $input $locale)
```
