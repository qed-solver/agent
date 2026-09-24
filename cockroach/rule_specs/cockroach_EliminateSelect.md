# Name: EliminateSelect
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

EliminateSelect discards an unnecessary Select operator in the case where its
filter is always true. Keep this near the top of the file so that it tends to
be checked early.

Extracted from `select.opt` (which defines multiple rules — implement specifically `EliminateSelect`, not the other rules in that file):

```
# EliminateSelect discards an unnecessary Select operator in the case where its
# filter is always true. Keep this near the top of the file so that it tends to
# be checked early.
[EliminateSelect, Normalize]
(Select $input:* [])
=>
$input
```
