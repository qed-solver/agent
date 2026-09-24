# EliminateAggDistinct

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 20  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/agg.opt

EliminateAggDistinct removes AggDistinct for aggregations where DISTINCT
never modifies the result; for example: min(DISTINCT x).

Extracted from `agg.opt` (which defines multiple rules — implement specifically `EliminateAggDistinct`, not the other rules in that file):

```
# EliminateAggDistinct removes AggDistinct for aggregations where DISTINCT
# never modifies the result; for example: min(DISTINCT x).
[EliminateAggDistinct, Normalize]
(AggDistinct $input:(Min | Max | BoolAnd | BoolOr))
=>
$input
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests on Min/Max/BoolAnd/BoolOr being idempotent with respect to duplicate input values — an algebraic identity of specific aggregate functions — but QED models every aggregate (even one literally named "Min") as an uninterpreted function over its input bag and knows only bag-equality of inputs, so it cannot relate agg(DISTINCT x), which aggregates over the deduplicated bag, to agg(x) over the raw bag; an SMT countermodel (an aggregate that depends on value multiplicity) always exists, and the source rule's restriction to those four functions has no semantic content in the DSL since generic aggregate operators carry no algebra. The richer input-key-based special case (like the proven PARTIAL `EliminateDistinct` precedent) at best covers degenerate inputs where deduplication is provably a no-op, not the function-based justification this rule actually has. The porter's recorded failure was an LLM context-length crash rather than an actual QED run, but the UNSUPPORTED conclusion is correct on the merits.
