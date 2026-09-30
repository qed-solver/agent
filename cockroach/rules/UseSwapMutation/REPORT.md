# UseSwapMutation

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3

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

The rule's correctness rests on the backend's bespoke compare-and-swap semantics of UpdateSwap/DeleteSwap (optimistic: a graceful no-op when the PK-pinned row is absent) plus plan-time side conditions (input has zero or one row, WHERE pins every PK column, no FK/unique/check constraints) — QED can express neither, since RuleScript rules are unconditional equivalences with no assumption mechanism (table-level guarantees are only row-level constraints), and its bag-based query semantics has no notion of mutation/state at all. The two sides' inputs are not universally bag-equivalent (a filtered scan yields 0 or 1 data-dependent rows vs. a constructed one-row Values, diverging when the pinned row is absent), and since the Update↔UpdateSwap operator relationship is backend-internal semantics invisible to QED as independent uninterpreted symbols, no DSL builder extension can make it a theorem — and there is no non-trivial provable special case, since even identical inputs under two distinct uninterpreted operators are not provably related. ```
