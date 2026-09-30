# TryDecorrelateProjectInnerJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 72  **Verification rounds used:** None
**Scope detail:** LEFT outer join with INNER inner join over single-column L/IL/IR and one 2-arg computed projection f


## Source rule (as given to the porter)

```
Decorrelates by hoisting an InnerJoin's filter from below a LeftJoin/Project combo, merging it into the LeftJoin condition.
```

## Independent verifier review

**Verdict:** CONFIRMED

The automated porter's candidate was actually correct -- it originally landed as FAILED only because the independent verifier's LLM response got truncated at the token limit mid-reply and was conservatively (and incorrectly) treated as REJECTED since it couldn't be parsed, not because of any real flaw, and the 6th forced round then ran out with no more retries. Manually reviewed the candidate line-by-line against the source .opt pattern: it correctly models the ConcatFilters merge (innerOn gets AND'd into the outer LeftJoin's on-condition, matching argument order to before()'s on/innerOn calls), the UnionCols passthrough widening (IR gets added to the pushed Project's passthrough on the after side, needed so innerOn can reference it once re-evaluated at the outer join), and the final Project correctly restricting back to the original output columns. Same accepted narrowing precedent as TryDecorrelateSelect/TryDecorrelateProjectSelect: on/innerOn are modeled as uncorrelated predicates (the FiltersBoundBy/outer-cols guard is a firing heuristic, not a soundness precondition). Re-ran try_rule fresh (provable=true, complete_fragment=false -- real SMT engagement) and verified non-vacuous with a negative control that breaks the UnionCols widening (drops IR from the after-side passthrough, so innerOn can't be faithfully re-evaluated) -- correctly fails to prove.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 15616960
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 13283166
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 156250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1293625
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 30184667
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 13437292
  },
  "total_duration": {
    "secs": 0,
    "nanos": 47355792
  }
}
```
