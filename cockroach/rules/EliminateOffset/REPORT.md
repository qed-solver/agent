# EliminateOffset

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

EliminateOffset discards an Offset operator if its offset value is zero.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `EliminateOffset`, not the other rules in that file):

```
# EliminateOffset discards an Offset operator if its offset value is zero.
[EliminateOffset, Normalize]
(Offset $input:* (Const 0))
=>
$input
```
```

## Independent verifier review

**Verdict:** AGREE

QED models Offset as an uninterpreted QOp with no algebraic axioms (per qed.pdf §6.2, which states it does not support queries using Limit/Offset/Order By ordering semantics), so the rule's only content — that offsetting by zero is the identity — is not an SMT consequence; a countermodel where the uninterpreted Offset function discards rows even at 0 is available to the solver. No DSL extension can close this gap, since the missing piece is a prover-side axiom for Offset (and the prover is the trusted, unmodifiable arbiter; the existing `LogicalSort` case in `JSONSerializer` only covers serialization, not semantics). The rule also has no bag-semantic residue: with the unmodelable Offset removed from both sides, only the vacuous identity `input = input` remains, so there is no non-trivial special case to prove — consistent with the prior Spark `EliminateOffsets` skip for the same reason. ```
