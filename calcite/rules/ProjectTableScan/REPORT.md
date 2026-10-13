# ProjectTableScan

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** assumes a fixed 3-column table and a fixed non-identity project (an uninterpreted F over columns 0 and 2 plus a direct reference to column 2); the original rule applies to any table arity and any projection over a ProjectableFilterableTable


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectTableScanRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (Project over Scan) and `after()` (Project over Project over Scan) are structurally distinct, and the proved equivalence is exactly the rule's bag-semantic content: the `BindableTableScan(T,{0,2})` is modeled as `Project([T.0,T.2], Scan(T))` — the only bag meaning that operator can have, per the ProjectableFilterableTable contract — and the remapped project faithfully reproduces the source rule's `Mappings.target` bookkeeping (F(r0,r2)→F(y0,y1), r2→y1), reusing the *same* uninterpreted `F` on both sides, which models "the same expression, re-referenced" correctly rather than over-constraining. The fixed 3-column arity / selected set {0,2} / non-identity project shape is forced by QED's concrete pattern instantiation (its JSON format has no way to quantify over arity or carry a first-class column-restricted scan, so no DSL extension could generalize it), the uninterpreted `F` still makes the proof hold for all functions and table contents of that shape, and the `SCOPE: PARTIAL` line states these assumptions specifically and accurately.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 0
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 316708
  }
}
```
