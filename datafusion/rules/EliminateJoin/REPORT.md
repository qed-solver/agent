# EliminateJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** the join is a LEFT join of two one-column scans with an arbitrary (uninterpreted) ON condition, and its only consuming parent is a group-by whose single group key references the preserved left input's column and has no aggregate calls (a DISTINCT on that column), which is the rule's duplicate-insensitive-parent case for dropping a join whose right side contributes no visible columns.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_join.rs, lines 1-687
```

## Independent verifier review

**Verdict:** CONFIRMED

before()/after() genuinely differ (the LEFT join and its uninterpreted condition appear only in before), and the encoded shape — a grouping-only group-by on the preserved left column above L LEFT JOIN R ON <uninterpreted>, with no right-side column visible above the join — is a real firing of the source rule's ReplaceWithLeft branch: in the Rust code a grouping-only Aggregate sets child_duplicate_insensitive=true, visible_right is empty, and rewritten_join_type returns ReplaceWithLeft, dropping exactly the join and its condition as the encoding does. The narrowing (one of three branches, one-column scans, group-by instead of an arbitrary duplicate-collapsing ancestor or FD-uniqueness proof) is a genuine DSL-expressibility limit rather than a bug, is specifically and honestly declared in the SCOPE: PARTIAL line, and the proof is non-vacuous since it requires QED's group-by key-deduplication semantics to absorb the join's row multiplication — a trivially-identical encoding or a multiplicity-preserving group-by would not have proved. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 11163960
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 32434583
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 910250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 663083
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 26537458
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 32558625
  },
  "total_duration": {
    "secs": 0,
    "nanos": 74794625
  }
}
```
