# Name: ConvertJSONSubscriptToFetchValue
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

ConvertJSONSubscriptToFetchValue converts json subscripting
`[...]` into a fetch value operator `->`.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `ConvertJSONSubscriptToFetchValue`, not the other rules in that file):

```
# ConvertJSONSubscriptToFetchValue converts json subscripting
# `[...]` into a fetch value operator `->`.
[ConvertJSONSubscriptToFetchValue, Normalize]
(Indirection $input:* $index:* & (IsJSON $input))
=>
(FetchVal $input $index)
```
