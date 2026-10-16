# EliminateUDFCallSubquery

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 58  **Verification rounds used:** 3

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

The rule's correctness rests exactly on the algebraic identity of the value-returning scalar-subquery operator — unique(Values[<e>]) = <e> — and QED's documented model gives concrete semantics only to the cardinality-based EXISTS, treating aggregate/subquery operators as uninterpreted bag-to-value functions known solely up to input bag equality. Every faithful RuleScript encoding must carry that opaque operator on the before side and the bare UDF term on the after side, so no Java-side DSL extension (which only affects JSON construction, not the untouchable prover's axioms) can bridge the gap — consistent with the independently verified identical wall for the constant-payload sibling EliminateConstValueSubquery. ```
