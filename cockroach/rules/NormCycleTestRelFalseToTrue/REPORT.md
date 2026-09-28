# NormCycleTestRelFalseToTrue

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/cycle.opt

Extracted from `cycle.opt` (which defines multiple rules — implement specifically `NormCycleTestRelFalseToTrue`, not the other rules in that file):

```
[NormCycleTestRelFalseToTrue, Normalize]
(NormCycleTestRel (False))
=>
(NormCycleTestRel (True))
```
```

## Independent verifier review

**Verdict:** AGREE

The rule rewrites between two parameterizations of a test-only leaf operator with no input, so its entire validity rests on the operator's internal, stipulated semantics that its output does not depend on the boolean parameter — a "bespoke internal semantics" of an uninterpreted operator that QED fundamentally cannot reason about: the only leaf forms QED's model admits are uninterpreted scans (where the same symbol reduces the rule to the vacuous identity X = X, and distinct symbols are unrelated bags QED correctly refuses to equate) or concrete `values` (where a "proof" would require hard-coding the False ≡ True conclusion into the encoding). Since QED's JSON format offers no leaf relation form that carries a scalar parameter, and the prover itself is off-limits, this is a genuine QED limitation rather than a closable DSL gap — no alternative encoding yields a non-vacuous, non-circular proof. ```
