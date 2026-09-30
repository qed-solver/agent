# TryDecorrelateScalarGroupBy

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 101  **Verification rounds used:** None

## Source rule (as given to the porter)

```
Pushes a Join into a ScalarGroupBy operator to dig for and eliminate unnecessary correlation.
```

## Independent verifier review

**Verdict:** AGREE (manual, re-investigated)

Manually re-investigated after the automated round's own verifier DISAGREE'd with its SKIPPED claim (it then ran out of rounds without resolving the disagreement). The automated reasoning was partly WRONG: it claimed QED 'knows neither that COUNT/SUM ignore the NULL rows a left join synthesizes' -- but grepping relation.rs's Group/Aggr translation shows ignore_nulls is a genuinely interpreted flag (filters the aggregate's summation to non-null argument rows via a real Logic predicate guard), and Calcite's RelBuilder.AggCall exposes .ignoreNulls(true) directly (confirmed serialized correctly in the JSON output on both sides). Tested this directly on the maximally simplified instance -- L already has its own key (no EnsureKey/RowNumber needed), no correlation, no canary, ignoreNulls=true on the aggregate -- both with a generic aggregate operator and with COUNT specifically (QED's most concretely-interpreted aggregate, and the rule's own documented CountRows->Count remapping target): InnerJoin(L, ScalarGroupBy(Input, agg ignoreNulls)) versus Select(GroupBy(LeftJoinApply(L,Input,true), group-by=L, agg ignoreNulls), on) is NOT QED-provable in either case, even with the filter/on-condition removed from consideration. So the blocker is real, but different from what the automated round said: it's that QED's aggregate/SMT translation can't derive this particular LEFT-JOIN-then-regroup refinement even with real ignore_nulls semantics -- the same class of multi-relation join+regroup completeness gap already diagnosed for the sibling rule TryDecorrelateGroupBy (whose scalar/no-extra-key INNER-join variant WAS provable, but the LEFT-join variant tested here is not). On top of that still-genuine gap, the rule's general form also requires two confirmed-absent constructs: a CASE/conditional expression (for the 'notnull' canary discriminating zero-matches from a genuinely-null aggregate result -- grepped relation.rs/shared.rs, no conditional/ternary expression node exists anywhere in the Q-expression model) and an ordinal/RowNumber operator (for EnsureKey's fallback when the left side lacks a strict key -- no such operator in RelRN/JSONSerializer). No DSL extension can close any of these since they are prover-core (Rust, unmodifiable) gaps, not Java-side surface gaps.
