# UseSwapMutation

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/mutation.opt

UseSwapMutation converts an Update or a Delete to an UpdateSwap or a
DeleteSwap, respectively. It also replaces the input Scan with Values,
possibly wrapped in a Select.

UpdateSwap and DeleteSwap are optimistic compare-and-swap operations that are
possible when:

- all columns in the primary index are constrained to a single exact value
by the WHERE clause;
- only a single row is modified;
- there are no FK checks or cascades;
- there are no uniqueness checks;
- there are no check constraints;
- there are no vector indexes modified;
- there are no passthrough columns to RETURNING;
- there are no triggers;
- the table only uses a single column family;
- there are no mutation columns;
- there are no mutation indexes;
- there are no columns using composite encoding.

This rule needs to run after PruneMutationInputCols and before
GenerateParameterizedJoin.

Extracted from `mutation.opt` (which defines multiple rules — implement specifically `UseSwapMutation`, not the other rules in that file):

```
# UseSwapMutation converts an Update or a Delete to an UpdateSwap or a
# DeleteSwap, respectively. It also replaces the input Scan with Values,
# possibly wrapped in a Select.
#
# UpdateSwap and DeleteSwap are optimistic compare-and-swap operations that are
# possible when:
#
# - all columns in the primary index are constrained to a single exact value
#   by the WHERE clause;
# - only a single row is modified;
# - there are no FK checks or cascades;
# - there are no uniqueness checks;
# - there are no check constraints;
# - there are no vector indexes modified;
# - there are no passthrough columns to RETURNING;
# - there are no triggers;
# - the table only uses a single column family;
# - there are no mutation columns;
# - there are no mutation indexes;
# - there are no columns using composite encoding.
#
# This rule needs to run after PruneMutationInputCols and before
# GenerateParameterizedJoin.
[UseSwapMutation, Normalize]
(Update | Delete
    $input:* & (HasZeroOrOneRow $input)
    []
    []
    $mutationPrivate:* &
        (CanUseSwapMutation (OpName) $mutationPrivate) &
        (Let
            ($newInput $colMap $ok):(BuildSwapMutationInput
                $input
                $mutationPrivate
            )
            $ok
        )
)
=>
((OpName)
    $newInput
    []
    []
    (UseSwapMutation $mutationPrivate $colMap)
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rewrite is not a bag equivalence under any encoding: even in the best case (a real PK-equality filter, a unique-key scan, and the same uninterpreted per-row mutation symbol on both sides), a scan lacking a row for that PK makes the before-input empty while the synthesized Values(c) row makes the after-input non-empty, so QED would refute rather than prove it — the rule is sound only because UpdateSwap/DeleteSwap has bespoke no-op-on-absent-row runtime semantics that QED's bag model cannot see, plus guard-level inference (HasZeroOrOneRow, full PK constraint, CanUseSwapMutation) that uninterpreted symbols cannot connect. No non-degenerate special case exists: even modeling the mutation as the identity leaves the genuinely false input-bag swap, and the only provable encoding would be a trivial identity, so the missing values() builder is not the binding gap. ```
