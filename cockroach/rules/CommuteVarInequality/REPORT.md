# CommuteVarInequality

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 20  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

CommuteVarInequality is similar to CommuteVar (in scalar.opt), except it
handles inequality comparison operators that need special handling to commute
operands.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `CommuteVarInequality`, not the other rules in that file):

```
# CommuteVarInequality is similar to CommuteVar (in scalar.opt), except it
# handles inequality comparison operators that need special handling to commute
# operands.
[CommuteVarInequality, Normalize]
(Le | Lt | Ge | Gt $left:^(Variable) $right:(Variable))
=>
(CommuteInequality (OpName) $left $right)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire correctness content is the commutation law `a ≤ b ⟺ b ≥ a` / `a < b ⟺ b > a` — internal semantics of the comparison operators — but RuleScript exposes comparisons only as uninterpreted `RexRN.Pred` symbols (RexRN.java's full construct set is Pred/Proj/GroupBy/And/Or/Not/literals; there are no interpreted comparison builders), and QED's proofs quantify universally over instantiations of those symbols, so it can derive no relation between `LE(a,b)` and `GE(b,a)` (nor between `P(a,b)` and `P(b,a)`, since uninterpreted predicates need not be symmetric) — precisely the documented "no predicate inference between independent symbols / no operator internal semantics" limitation. The rule's side condition (right operand must be a variable, left not) is likewise unexpressible, since a predicate's arguments are opaque atoms in the relational pattern language. So UNSUPPORTED is substantively correct, even though the porter's recorded "reason" was merely an API error: no alternative encoding can make QED prove this rule. ```
