# EliminateWindow

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 48  **Verification rounds used:** 4

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

EliminateWindow's correctness rests entirely on the Window operator's internal semantics — that a Window with an empty function list is a row-preserving identity — but QED's bag-semiring prover has no Window operator at all, since Window requires list/ordering semantics the bag model cannot express (the RuleScript paper explicitly excludes Sort/Window as out of scope). Because the trusted Rust prover is unmodifiable, this gap can't be bridged by extending the Java DSL: an empty Window would remain an opaque symbol QED cannot reduce to its input, and any core-operator stand-in (identity Project, redundant group-by) would prove a *different* operator's theorem rather than this one, so no faithful encoding of `Window($input []) => $input` exists. ```
