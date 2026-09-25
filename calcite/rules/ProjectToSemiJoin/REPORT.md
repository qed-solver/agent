# ProjectToSemiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 26  **Verification rounds used:** 2
**Scope detail:** INNER join only, single-column equi key that is exactly the right aggregate's sole group key (a pure GROUP BY with no aggregate calls), with a left-field-only top project; the LEFT variant (full join elimination) and multi-key / aggregate-call cases are not modeled.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRule.java

Note: SemiJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectToSemiJoinRule` variant (not AggregateToSemiJoinRule, JoinOnUniqueToSemiJoinRule, JoinToSemiJoinRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the INNER-join branch of Calcite's ProjectToSemiJoinRule: before() is a left-fields-only Project over L INNER-JOIN (pure GROUP BY on R.1) with the equi condition L.0 = group key, and after() is the identical Project over L SEMI-JOIN the pre-aggregate relation R on the remapped condition L.0 = R.1 — exactly the transformation perform() performs (semi-join input is aggregate.getInput(), i.e. the shared rawRight symbol, with the right join key correctly remapped to the group key's source column), and the structural preconditions the rule checks (isEqui, rightSet = all group keys) are correctly baked in by using a concrete EQUALS on exactly the group key. before() and after() are genuinely different plans (inner join over a 1-column aggregate vs. semi-join over a 2-column raw scan), so the proof is non-vacuous, and the join→semi-join equivalence holds for every instantiation including nulls. The narrowing (INNER only — the LEFT branch is a distinct join-elimination rewrite, single equi key, no aggregate calls, project modeled as a left-field permutation) is a real, specific, non-degenerate fragment of the rule and is honestly disclosed in the one-line SCOPE tag.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 11197209
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35260250
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 927584
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 761875
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 26229083
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35380750
  },
  "total_duration": {
    "secs": 0,
    "nanos": 77653875
  }
}
```
