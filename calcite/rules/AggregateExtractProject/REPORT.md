# AggregateExtractProject

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 14  **Verification rounds used:** 1
**Scope detail:** the aggregate's input is a three-column relation (three one-column scans inner-joined with true conditions), it has one plain-field group key and one plain non-distinct aggregate call over a second plain field, and a third column is unused (neither a group key nor an aggregate argument) so the extracted projection drops it.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateExtractProjectRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors the source rule's actual computation: it computes the used columns (group col 0, agg arg col 1), projects them in ascending order below the aggregate, remaps the group key and agg operand to the projected columns (0→0, 1→1), and shares the same uninterpreted aggregate operator `f` and types across both sides, while before() and after() are structurally different (the projection genuinely drops the unused third column), so the proof is non-vacuous. The fixed 3-column / one-group-key / one-non-distinct-agg shape and the wide base modeled as a cross-join of three one-column scans are inherent to the DSL (scans are single-column and Aggregate's group/agg Seqs have fixed length), are honestly and specifically disclosed in the SCOPE: PARTIAL line, and the cross-join base exploits no accidental algebra since the equivalence holds for any base relation by per-group multiset preservation under projection. No missing semantic preconditions (the source rule's "input is not a Project" clause is a cycle-prevention matching constraint, not a semantic side condition), no spurious symbol sharing, and the remapping matches the original rule's inverse-surjection mapping exactly. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 15504039
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 45336917
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 973083
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1234667
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 34964209
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 45589291
  },
  "total_duration": {
    "secs": 0,
    "nanos": 96821708
  }
}
```
