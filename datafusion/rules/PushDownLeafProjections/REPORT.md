# PushDownLeafProjections

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 45  **Verification rounds used:** 3
**Scope detail:** only the local filter-pushdown fragment: a projection computing one uninterpreted expression over a pass-through column commuting below a filter whose predicate references that pass-through column only (the full DataFusion rule recurses through arbitrary plan shapes, splits/recombines projection lists, and handles Sort/Limit/Join plus filters referencing computed aliases).


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/extract_leaf_expressions.rs, lines 745-1413
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a genuine, non-vacuous instance of the rule's core filter fragment — `Project(F(a),a) over Filter(P(a),Scan)` vs `Filter(P(a),Project(F(a),a)) over Scan` — where F and P are uninterpreted operators correctly shared across both sides (the predicate is the same symbol applied to the same pass-through value via the projection's column 1, which is exactly the side condition under which DataFusion's name-resolution-based push is valid), and no preconditions are silently dropped (no key/NOT NULL assumptions, nullable VarTypes, plain bag semantics; the volatile/merge/Unnest guards in the source all belong to cases this fragment doesn't touch). The SCOPE line honestly declares this as a PARTIAL special case — one extracted expression, one pass-through column, filter-only, with the full rule's Sort/Limit/Join routing, projection merging, recovery-projection machinery, and filters-over-computed-aliases explicitly out of scope — so the provable result is faithful and not misleading.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5453168
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33605750
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 851750
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 366167
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16109167
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33706833
  },
  "total_duration": {
    "secs": 0,
    "nanos": 65151000
  }
}
```
