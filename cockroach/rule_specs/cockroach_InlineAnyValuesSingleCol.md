# Name: InlineAnyValuesSingleCol
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

InlineAnyValuesSingleCol converts Any with Values input to AnyScalar.
This version handles the case where there is a single column.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `InlineAnyValuesSingleCol`, not the other rules in that file):

```
# InlineAnyValuesSingleCol converts Any with Values input to AnyScalar.
# This version handles the case where there is a single column.
[InlineAnyValuesSingleCol, Normalize]
(Any $values:(Values) $scalar:* $private:*)
=>
(AnyScalar $scalar (InlineValues $values) (SubqueryCmp $private))
```
