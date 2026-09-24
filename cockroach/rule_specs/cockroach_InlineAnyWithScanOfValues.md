# Name: InlineAnyWithScanOfValues
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

InlineAnyWithScanOfValues matches on an ANY or NOT ANY expression which was
generated from expressions like `column IN (WithScan)` or `column NOT IN
(WithScan)`, where the WITH clause definition was normalized into a VALUES
clause with constants or placeholders. Inlining the Values expression allows
other optimizations to trigger.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `InlineAnyWithScanOfValues`, not the other rules in that file):

```
# InlineAnyWithScanOfValues matches on an ANY or NOT ANY expression which was
# generated from expressions like `column IN (WithScan)` or `column NOT IN
# (WithScan)`, where the WITH clause definition was normalized into a VALUES
# clause with constants or placeholders. Inlining the Values expression allows
# other optimizations to trigger.
[InlineAnyWithScanOfValues, Normalize]
(Any
    (WithScan $withScanPrivate:*)
    $scalar:* &
        (Let ($values $ok):(BoundValues $withScanPrivate) $ok) &
        (CanInlineWithScanOfValues
            $values
            $withScanPrivate
            $scalar
        )
    $anyPrivate:*
)
=>
(Any
    (InlineWithScanOfValues $values $withScanPrivate)
    $scalar
    $anyPrivate
)
```
