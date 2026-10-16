# EliminateConstValueSubquery

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 66  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateConstValueSubquery replaces a subquery with a constant value if the
subquery's input is a single-row, single-column Values expression with a
constant value.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateConstValueSubquery`, not the other rules in that file):

```
# EliminateConstValueSubquery replaces a subquery with a constant value if the
# subquery's input is a single-row, single-column Values expression with a
# constant value.
[EliminateConstValueSubquery, Normalize]
(Subquery (Values [ (Tuple [ $value:(Const) ]) ]))
=>
$value
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on the evaluation semantics of the value-returning UNIQUE scalar-subquery operator (a single-row constant VALUES body must evaluate to exactly that constant), and QED's formalization gives concrete semantics only to the cardinality-based EXISTS subquery operator (JExists(Q) = Σ‖JQK‖); a value-returning UNIQUE subquery is an uninterpreted higher-order operator in the semiring Q-expression model that the prover cannot see through, since bag/multiplicity semantics cannot express "return the value of the unique row." Any faithful encoding of this rule must name that subquery operator (the relational core language has no substitute for it), so the failure is a genuine QED limitation rather than an encoding bug — the BOOLEAN-vs-INTEGER type mismatch the porter observed is a secondary blemish of their attempt, not the cause.
