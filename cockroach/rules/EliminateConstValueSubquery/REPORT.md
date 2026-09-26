# EliminateConstValueSubquery

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2

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

The rule's LHS is a scalar subquery wrapping a single-row constant VALUES, and QED's prover cannot represent or reason about a scalar term that embeds a nested sub-relation (the scalar-subquery operator) — the porter confirmed this with a real experiment: a subquery builder was added, compiled, and serialized a genuine nested `query` node, yet the encoding still returned `provable=false` with `smt_duration=0` (fast-rejected before SMT), proving it is a prover limitation, not a closable DSL gap. Independently, the core language has no non-bool constant literal (needed for the RHS `$value`) and no non-empty VALUES builder (needed for the LHS single-row constant), so the rule can't even be written down. Taken together this is a genuine QED/DSL boundary (bespoke scalar-subquery semantics + scalar constant folding), so no faithful encoding can be made provable. ```
