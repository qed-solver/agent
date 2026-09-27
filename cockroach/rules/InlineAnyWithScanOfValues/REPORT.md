# InlineAnyWithScanOfValues

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3

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

QED's contract is to prove the two patterns equivalent for *every* instantiation of their uninterpreted symbols, and it has no facility for definitional side-conditions. This rule is only sound under the premise that the CTE `W` is *defined as* a specific constant VALUES — i.e., that the `WithScan(W)` symbol equals that Values expression — but QED's table model (an uninterpreted scan plus only per-row "guaranteed" constraints such as keys/uniqueness) cannot express "this scan's rows are exactly these constants", so QED will always admit an instantiation where `Scan(W) ≠` the inlined Values, making before and after non-equivalent. Hence the blocker is in QED's semantics rather than the DSL surface, and adding a scalar ANY/IN subquery operator or a Values-with-constants operator via `extend_dsl_file` could not close the gap. ```
