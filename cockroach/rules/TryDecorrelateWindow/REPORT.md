# TryDecorrelateWindow

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 1  **Verification rounds used:** 1
**Scope detail:** the 'left already has a strict key' branch (EnsureKey is a no-op), uncorrelated Input (HasOuterCols is a firing heuristic per the TryDecorrelateSelect/TryDecorrelateGroupBy precedent), Window modeled via the join-back-on-partition-key idiom (no frame/ordering semantics, an uninterpreted aggregate standing in for the window function), starting from EMPTY pre-existing partition columns so only L's key is added, single aggregate call, ON references only the input's value column


## Source rule (as given to the porter)

```
(spec text unavailable at publish time)
```

## Independent verifier review

**Verdict:** CONFIRMED

Manual encoding, following the same 'left already has a strict key, Input uncorrelated' precedent already established (and independently verified) for TryDecorrelateGroupBy's single-key case, plus the join-back-on-partition-key idiom that PushSelectIntoWindow (already PROVED) uses to model Window as an uninterpreted per-partition aggregate joined to its input. The window's pre-existing partition-column set is modeled as EMPTY (a scalar/whole-input window, e.g. RANK() OVER ()), so AddColsToPartition(private, KeyCols(newLeft)) adds exactly L's own key as the sole partition column after decorrelation -- the same single-key shape already confirmed provable for the analogous GroupBy rule (multi-key partition sets hit the same known SMT-completeness gap that blocks multi-key GroupBy, so this PARTIAL scope deliberately stays in the single-key fragment). QED proves it (provable=true); a negative control that cross-joins the per-partition aggregate back instead of joining on the partition key (collapsing per-L-row partitioning into one shared scalar value) correctly fails to prove, confirming the per-partition join is load-bearing.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 10353792
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 18333333
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 142750
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1040417
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 20549375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 18575458
  },
  "total_duration": {
    "secs": 0,
    "nanos": 42510708
  }
}
```
