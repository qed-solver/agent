# JoinConditionPush

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 66  **Verification rounds used:** 3
**Scope detail:** INNER join with no filter above, single-column inputs, one left-only conjunct, one right-only conjunct and one cross conjunct


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterJoinRule.java

Note: FilterJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `JoinConditionPushRule` variant (not FilterIntoJoinRule (already ported separately as "FilterJoin"), which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is not vacuous and the encoding is sound: before() and after() differ structurally, and reusing the same uninterpreted predicate operators (left_conj, right_conj, cross_conj) in both plans correctly models the rule moving the very same conjuncts from the ON clause into child filters, with the right-side conjunct bound via joinField(1, right) so it references the join row's right column rather than mis-binding. No hidden preconditions are assumed (scans are non-unique and nullable, and uninterpreted predicates are deterministic by construction, matching the rule's determinism guard), and the narrowing — INNER join, no filter above, one left/right/cross conjunct, single-column inputs — is precisely and honestly declared in the SCOPE line, making this a genuine, non-degenerate, and useful special case of Calcite's FilterJoinRule pushdown (the INNER from-within case of JoinConditionPushRule).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6704335
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 43313083
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 845583
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 456291
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18759958
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 43429209
  },
  "total_duration": {
    "secs": 0,
    "nanos": 78040500
  }
}
```
