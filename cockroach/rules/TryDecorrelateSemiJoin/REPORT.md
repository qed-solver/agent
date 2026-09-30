# TryDecorrelateSemiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 47  **Verification rounds used:** 4
**Scope detail:** left is a fully-keyed (unique) single-column scan (modeling EnsureKey whose key is the column), right is a single-column scan, the correlate condition is an uninterpreted 2-ary predicate, and left has no non-key columns (so no ConstAgg output columns)


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateSemiJoin maps a SemiJoin to an equivalent GroupBy/InnerJoin
complex in hopes of triggering further rules that will ultimately decorrelate
the query. Once this rule fires, a corresponding InnerJoin decorrelation rule
will match (i.e. TryDecorrelateGroupBy or TryDecorrelateProject).

Citations: [5]

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateSemiJoin`, not the other rules in that file):

```
# TryDecorrelateSemiJoin maps a SemiJoin to an equivalent GroupBy/InnerJoin
# complex in hopes of triggering further rules that will ultimately decorrelate
# the query. Once this rule fires, a corresponding InnerJoin decorrelation rule
# will match (i.e. TryDecorrelateGroupBy or TryDecorrelateProject).
#
# Citations: [5]
[TryDecorrelateSemiJoin, Normalize]
(SemiJoin | SemiJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (CanHaveZeroRows $right) &

        # Let EliminateExistsGroupBy match instead.
        (GroupBy | DistinctOn | Project | ProjectSet | Window)
    $on:*
    $private:*
)
=>
(Project
    # Needed to project away any columns added by EnsureKey.
    (GroupBy
        (InnerJoinApply
            $newLeft:(EnsureKey $left)
            $right
            $on
            $private
        )
        (MakeAggCols ConstAgg (NonKeyCols $newLeft))
        (MakeGrouping (KeyCols $newLeft) (EmptyOrdering))
    )
    []
    (OutputCols $left)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core algebraic identity of TryDecorrelateSemiJoin — SEMI correlate(L, R, on) ≡ GroupBy(INNER correlate(L, R, on), group-by L's key) — with the `unique=true` flag on L correctly modeling EnsureKey's guarantee (without which GroupBy would collapse duplicate left rows that SEMI preserves). The restrictions (single-column keyed left, single-column right, uninterpreted 2-ary condition) are genuine and honestly documented in the SCOPE line, and before()/after() are structurally distinct operators (SemiJoin vs GroupBy-over-InnerJoin), so the proof is non-vacuous.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7105292
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36884583
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 876792
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 466166
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19198542
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36989042
  },
  "total_duration": {
    "secs": 0,
    "nanos": 71918250
  }
}
```
