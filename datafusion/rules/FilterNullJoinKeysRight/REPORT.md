# FilterNullJoinKeysRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** only the inner-join case is encoded: a single equi-join conjunct L.col = R.col on an INNER join with IS NOT NULL(R.col) added to the right input, while the source rule adds one such filter per equi-pair for every join kind in which the right side is preserved.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/filter_null_join_keys.rs, lines 1-109
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding correctly captures the rule's key semantic precondition by using real null-rejecting `EQUALS` for the equi-conjunct (not an uninterpreted predicate), so the proof that pre-filtering `IS NOT NULL(R.col)` off the right input of an INNER join is bag-equivalent is genuine and non-vacuous — a null-key right row can never satisfy the join condition and thus contributes nothing, making before()/after() differ by a real, meaningful filter pushdown rather than being structurally identical. The `SCOPE: PARTIAL` line honestly and specifically discloses the narrowing (single inner-join equi-pair, right-side filter only, vs. the source's per-pair filter for every right-preserved join kind), and the symbols are correctly shared with matching key types, so this is a faithful, non-degenerate special case of the source rule. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7628166
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34459834
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 868792
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 435875
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19186125
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34549541
  },
  "total_duration": {
    "secs": 0,
    "nanos": 69125750
  }
}
```
