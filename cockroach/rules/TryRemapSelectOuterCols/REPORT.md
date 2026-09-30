# TryRemapSelectOuterCols

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 101  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryRemapSelectOuterCols is similar to TryRemapJoinOuterColsRight, but it
applies to the input of a Select.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryRemapSelectOuterCols`, not the other rules in that file):

```
# TryRemapSelectOuterCols is similar to TryRemapJoinOuterColsRight, but it
# applies to the input of a Select.
[TryRemapSelectOuterCols, Normalize]
(Select
    $input:* & (HasOuterCols $input)
    $on:* &
        (CanMaybeRemapOuterCols $input $on) &
        (Let ($remapped $ok):(TryRemapOuterCols $input $on) $ok)
)
=>
(Select $remapped $on)
```
```

## Independent verifier review

**Verdict:** AGREE

TryRemapSelectOuterCols fundamentally depends on outer column references (a multi-scope concept) and a conditional structural expression-tree rewrite (TryRemapOuterCols) whose soundness rests on the backend's construction of correlated references — RuleScript's single-scope bag-semantic language cannot represent the HasOuterCols/CanMaybeRemapOuterCols premises or the parametric remap, and no DSL extension can close this gap because QED has no notion of correlated scopes or structure-dependent rewrites. ```
