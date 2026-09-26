# EliminateUDFCallSubquery

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateUDFCallSubquery replaces a subquery with a udf call if the subquery's
input is a single-row, single-column Values expression with a udf call.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateUDFCallSubquery`, not the other rules in that file):

```
# EliminateUDFCallSubquery replaces a subquery with a udf call if the subquery's
# input is a single-row, single-column Values expression with a udf call.
[EliminateUDFCallSubquery, Normalize]
(Subquery (Values [ (Tuple [ $udf:(UDFCall) ]) ]))
=>
$udf
```
```

## Independent verifier review

**Verdict:** AGREE

The rule is a scalar rewrite whose LHS is a scalar subquery term (a relation embedded inside a scalar expression, `Subquery(Values(udf))` ⟹ `udf`), and QED's core language is purely relational with bag/semiring semantics — it has no operator that maps a relation to a scalar term, so the only link between the two sides (the subquery's "evaluate and extract the single value" behavior) is bespoke operator semantics that QED's model cannot express or derive, leaving SMT with two unrelated uninterpreted terms. This is confirmed empirically by the structurally identical sibling EliminateConstValueSubquery (same `Subquery(Values(Tuple(single scalar)))` shape), which fast-rejected before SMT after 24 attempts and passed independent verification as unsupported. Closing the DSL gaps (non-empty Values, a subquery-term builder) would only let the pattern be serialized — proving it would require modeling scalar-subquery algebra inside the immutable prover, which is out of bounds, so the UNSUPPORTED call is sound. ```
