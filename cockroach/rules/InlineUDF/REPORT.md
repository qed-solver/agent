# InlineUDF

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2

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

InlineUDF's sole correctness content is the definitional identity "an inlinable UDF call evaluates to its single-statement body as a scalar subquery," but QED models the UDF as an uninterpreted scalar operator and the scalar subquery's `unique` as an uninterpreted bag-to-value function (only EXISTS has concrete subquery semantics), with no axiom or concrete evaluation that could ever link a function symbol to its body; hence the only encodable "proof" must assume `udf(args) = unique(body)` verbatim as a shared side-condition and then merely substitute equals for equals, which is vacuous rather than a verification of the rewrite. ```
