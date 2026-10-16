# InlineAnyWithScanOfValues

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 32  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule's only semantic content is substituting a CTE reference (WithScan) for its defining VALUES expression — i.e., a let-binding of a named relation to a relational expression. QED's semantics (and the JSON format its immutable Rust prover accepts, as mirrored by JSONSerializer's node set) has no construct for a defined/aliased relation: every named relation is an uninterpreted source whose contents can be neither defined by nor proven equal to any expression, so any encoding is either the same symbol on both sides (before ≡ after, vacuous) or two independent sources for which the equivalence is not a universal theorem. No Java-side DSL extension (e.g., adding a `Values` builder, which the JSON format already carries) can close the gap, because the missing piece is let-binding semantics inside the trusted, unmodifiable prover — making this a genuine QED limitation rather than a symbol-sharing bug or a missed encoding. ```
