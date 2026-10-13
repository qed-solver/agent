# JoinToSemiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 37  **Verification rounds used:** 2
**Scope detail:** INNER join only, single-column equi key that is exactly the right aggregate's sole group key (no aggregate calls), with a parent project dropping the right column; the LEFT variant (join elimination with an empty aggregate) is not modeled.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRule.java

Note: SemiJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `JoinToSemiJoinRule` variant (not AggregateToSemiJoinRule, JoinOnUniqueToSemiJoinRule, ProjectToSemiJoinRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and faithfully captures the rule's core rewrite — an INNER join of L with (R GROUP BY the join key), projected down to L's columns, is bag-equivalent to a SEMI join of L with the pre-aggregate R — using independent L/R symbols, the semi-join correctly targeting the aggregate's input, and a genuine (non-structural) INNER-vs-SEMI difference that QED proves. It is an honestly-declared narrower special case (INNER only, single-column equi key that is the aggregate's sole group key, no aggregate calls, plus a dropping project to make the INNER case width-matching; the LEFT/empty-aggregate elimination is omitted), so the SCOPE: PARTIAL tag is accurate and the proof is meaningful rather than of an easier claim. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9634290
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35300292
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 894417
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 534917
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 23161875
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35396208
  },
  "total_duration": {
    "secs": 0,
    "nanos": 74427458
  }
}
```
