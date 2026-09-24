# CommuteConstInequality

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 20  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

CommuteConstInequality is similar to CommuteConst (in scalar.opt), except
that it handles inequality comparison operators that need special handling to
commute operands.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `CommuteConstInequality`, not the other rules in that file):

```
# CommuteConstInequality is similar to CommuteConst (in scalar.opt), except
# that it handles inequality comparison operators that need special handling to
# commute operands.
[CommuteConstInequality, Normalize]
(Le | Lt | Ge | Gt $left:(ConstValue) $right:^(ConstValue))
=>
(CommuteInequality (OpName) $left $right)
```
```

## Independent verifier review

**Verdict:** AGREE

CommuteConstInequality is a scalar normalization whose entire correctness rests on the order-theoretic identity c ≤ x ⟺ x ≥ c (and the < / > flips); in RuleScript the Le/Lt/Ge/Gt operators can only be introduced as uninterpreted predicate symbols, and QED proves equivalences that must hold for every instantiation of uninterpreted symbols, so it cannot entail le(c,x) from ge(x,c) — nor from a flipped-argument version of a single symbol, which would in fact be false for an arbitrary uninterpreted predicate. A faithful relational embedding (a scan filtered by the original comparison vs. one filtered by the commuted, direction-flipped comparison) is expressible in the core language but unprovable precisely because the rule's content is the numeric-order interpretation that QED has no model for (a documented limitation: no reasoning about a backend operator's bespoke internal semantics), and no extend_dsl_file change can close it since the operator names just flow as strings into the fixed, unchanging prover. (The porter's stated reason was an LLM HTTP 400 context-length error rather than a completed analysis, so the transcript contains no real attempt — but the unsupported conclusion itself is correct.) ```
