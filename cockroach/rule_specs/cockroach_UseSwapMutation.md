# Name: UseSwapMutation
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/mutation.opt

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
