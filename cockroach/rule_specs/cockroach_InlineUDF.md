# Name: InlineUDF
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/inline.opt

InlineUDF converts a UDF to a subquery. A UDF can only be inlined if it is
non-volatile and has a single statement in the function body. See
IsInlinableUDF for more details.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineUDF`, not the other rules in that file):

```
# InlineUDF converts a UDF to a subquery. A UDF can only be inlined if it is
# non-volatile and has a single statement in the function body. See
# IsInlinableUDF for more details.
[InlineUDF, Normalize]
(UDFCall $args:* $private:* & (IsInlinableUDF $args $private))
=>
(ConvertUDFToSubquery $args $private)
```
