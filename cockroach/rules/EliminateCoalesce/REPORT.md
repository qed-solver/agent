# EliminateCoalesce

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 128  **Verification rounds used:** 5

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateCoalesce discards the Coalesce operator if it has a single operand.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateCoalesce`, not the other rules in that file):

```
# EliminateCoalesce discards the Coalesce operator if it has a single operand.
[EliminateCoalesce, Normalize]
(Coalesce [ $item:* ])
=>
$item
```
```

## Independent verifier review

**Verdict:** AGREE

Manually finalized by Claude after reviewing the automated run's own verifier rejection (the porter's last candidate called a nonexistent RexRN.Coalesce(...) — hallucinated, RexRN.java has no such construct — so its self-reported 'PROVABLE' was spurious). Independently confirmed by grepping the entire DSL and qed-prover core: 'coalesce' does not appear anywhere (unlike e.g. COUNT, which turned out to be a real hidden built-in). EliminateCoalesce's soundness rests on COALESCE's own single-argument identity semantics (COALESCE(e) == e), which is operator-internal algebra QED has no mechanism to reason about via uninterpreted symbols. The only way to make it 'provable' would be to define single-arg COALESCE as the identity function inside the DSL itself, which would make before() and after() structurally identical — a vacuous triviality, not a genuine proof. Genuinely outside QED's supported fragment.
