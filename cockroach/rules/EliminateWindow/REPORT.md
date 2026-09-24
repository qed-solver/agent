# EliminateWindow

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 9  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/window.opt

EliminateWindow removes a Window operator with no window functions (which can
occur via column pruning).

Extracted from `window.opt` (which defines multiple rules — implement specifically `EliminateWindow`, not the other rules in that file):

```
# EliminateWindow removes a Window operator with no window functions (which can
# occur via column pruning).
[EliminateWindow, Normalize]
(Window $input:* [])
=>
$input
```
```

## Independent verifier review

**Verdict:** AGREE

RuleScript has no Window builder and QED’s serializer/prover have no window semantics, so the source left-hand side cannot be represented as a window node. Modeling an empty window as an identity projection instead only proves the unrelated identity-projection tautology, not that CockroachDB’s no-op Window operator can be eliminated.
