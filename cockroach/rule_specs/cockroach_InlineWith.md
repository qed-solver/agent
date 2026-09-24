# Name: InlineWith
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/with.opt

InlineWith replaces use of a With which is referenced at most one time with
the contents of the With itself.

Extracted from `with.opt` (which defines multiple rules — implement specifically `InlineWith`, not the other rules in that file):

```
# InlineWith replaces use of a With which is referenced at most one time with
# the contents of the With itself.
[InlineWith, Normalize]
(With
    $binding:*
    $input:*
    $withPrivate:* & (CanInlineWith $binding $input $withPrivate)
)
=>
(InlineWith $binding $input $withPrivate)
```
