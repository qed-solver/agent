# FoldNullCast

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 51  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullCast discards the cast operator if it has a null input. The resulting
null value has the same type as the Cast operator would have had.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullCast`, not the other rules in that file):

```
# FoldNullCast discards the cast operator if it has a null input. The resulting
# null value has the same type as the Cast operator would have had.
[FoldNullCast, Normalize]
(Cast $input:(Null) $targetTyp:*)
=>
(Null $targetTyp)
```
```

## Independent verifier review

**Verdict:** AGREE

FoldNullCast's entire content is the scalar identity cast(NULL) = NULL, whose correctness rests on the internal null-preserving semantics of the cast operator — exactly the "operator whose specific internal semantics QED cannot see through as an uninterpreted function" limitation. QED's SMT theory treats casts as uninterpreted functions with no null-propagation axiom, so cast(NULL) = NULL is not a valid consequence for any instantiation (the porter's empirical provable=false on the structurally distinct Project(cast(null)) vs Project(null) encoding is consistent with this), and the only concrete-Cast route degenerates because Calcite constant-folds CAST(NULL AS T) to a typed null literal at pattern-construction time, yielding a vacuous X=X. No DSL extension can close this, since a cast builder would still serialize to an uninterpreted call and the missing fact lives in the unmodifiable prover's semantics, not in a missing builder. ```
