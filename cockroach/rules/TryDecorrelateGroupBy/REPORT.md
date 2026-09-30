# TryDecorrelateGroupBy

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 108  **Verification rounds used:** None

## Source rule (as given to the porter)

```
Pushes a Join into a GroupBy operator to dig for and eliminate unnecessary correlation.
```

## Independent verifier review

**Verdict:** AGREE

Manually investigated by Claude. The rule pushes a (correlated) join under a GroupBy by adding the left relation's key as an extra grouping column, so a left-independent aggregate gets replicated once per left row -- the same 'unique key forces per-row replication' mechanism already proven for EliminateDistinct/EliminateAggDistinctForKeys, applied here across a join rather than within one relation. Tried the 'left already has its own strict key' branch (EnsureKey is a no-op, no Ordinality needed), with the $right-has-outer-cols guard treated as a firing heuristic rather than a soundness precondition (same precedent as the already-PROVED TryDecorrelateSelect). Hand-verified the identity is genuinely true: both sides denote the same bag {(l, g, a_g) : l in L, g a distinct key value in Input, on(l,g)} because L's uniqueness prevents the cross-join+regroup from merging rows that the original per-l join kept separate. Confirmed this empirically too: the degenerate scalar (no grouping-key) special case of the exact same mechanism -- InnerJoin(L, ScalarAgg(Input)) versus GroupBy(InnerJoin(L,Input,true), group-by=L) -- IS QED-provable (tested directly). But the real rule always has a pre-existing grouping key k alongside the newly-added key L; re-tested that two-key version with the post-group filter removed entirely (pure GroupBy(Input,k) vs. GroupBy(cross(L,Input), {k,L}) equivalence, no join condition at all) and QED still reports not provable. So the blocker is specifically QED's aggregate/SMT translation being unable to derive this multi-key group-by refinement fact -- not a missing DSL construct (both sides serialize and compile cleanly, confirmed via the rendered LogicalPlan output matching the intended structure exactly) and not a flaw in the identity itself (true by hand-proof and confirmed on the single-key reduction). No DSL extension can close this since the gap is in the fixed Rust prover's aggregate reasoning, not the Java-side serialization.
