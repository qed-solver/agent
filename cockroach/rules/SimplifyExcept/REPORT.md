# SimplifyExcept

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 42  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/set.opt

SimplifyExcept converts an Except operator into an ExceptAll operator when the
left input has a key. This avoids the de-duplication step.

Extracted from `set.opt` (which defines multiple rules — implement specifically `SimplifyExcept`, not the other rules in that file):

```
# SimplifyExcept converts an Except operator into an ExceptAll operator when the
# left input has a key. This avoids the de-duplication step.
[SimplifyExcept, Normalize]
(Except $left:* & (HasStrictKey $left) $right:* $colMap:*)
=>
(ExceptAll $left $right $colMap)
```
```

## Independent verifier review

**Verdict:** AGREE

SimplifyExcept's after-side operator is EXCEPT ALL (bag-difference), and QED's model contains no bag-variant MINUS: its `except` node is interpreted set-semantically, the Java builder `minus(true, ...)` produces a LogicalMinus that JSONSerializer cannot even serialize (its `except` emission is guarded on `!minus.all`, so the all=true case falls through to "Not implemented"), and bag-difference is not definable from QED's available operators (bag-union/join/project/filter + distinct + set-intersect/set-minus + uninterpreted aggregates), since it requires per-tuple count arithmetic (count_L − min(count_L, count_R)) that QED's uninterpreted-aggregate model cannot express. The rule's precondition (strict key on the left) is expressible via `scan(id, ty, unique=true)` keyed tables, so the constraint side is not the blocker — the missing after-side operator is a fundamental prover limitation that no `extend_dsl_file` edit can close (the Rust prover is fixed, and serializing bag-minus as `except` would unsoundly turn the rule into a trivial identity). The provable neighborhood the porter found (unique-left ⟹ Except ≡ AntiJoin) is a genuinely different rewrite, so giving up on this specific set→bag operator conversion is the correct call.
