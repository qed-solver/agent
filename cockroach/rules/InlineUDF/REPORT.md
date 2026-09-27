# InlineUDF

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 26  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/inline.opt

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
```

## Independent verifier review

**Verdict:** AGREE

InlineUDF's soundness rests entirely on the definitional link between the UDF's call symbol and the subquery derived from its function body — i.e. a backend operator's bespoke internal semantics that QED models as uninterpreted, and QED cannot reason about entailment between independent uninterpreted symbols or axiomatize a function definition. Extending the DSL with a scalar-subquery term (which JSONSerializer can already carry) would not close the gap, because the missing piece is a definitional axiom `f(args) = subquery(body)`, not a term: any encoding either bakes that axiom in by modeling the call as the subquery (making before/after the same expression, a vacuous identity "proof" of nothing about the actual rule) or keeps `f` and the subquery as independent symbols that SMT can never relate. ```
