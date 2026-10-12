# PushExtraPredicateThroughJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 40  **Verification rounds used:** 4
**Scope detail:** INNER join only; one column per side; both-side conjunct Or(And(p(l),q(r)),And(p(l),s(r)))


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushExtraPredicateThroughJoin.scala, lines 33-81
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's core semantic move — side-specific predicates that are implied by the join condition (p on L appears in every disjunct; q∨s on R) are pushed down as filters into the respective inputs while the join condition itself is left unchanged, exactly as the source does. The before/after forms are structurally distinct (no vacuity), the field/symbol sharing is correct (pl=plj, pr=prj, sr=srj, so no coincidental over-constraint), and the INNER-join + fixed disjunctive-condition shape is an honest, non-degenerate special case of the rule that genuinely exercises its extraction logic, accurately reflected in the `SCOPE: PARTIAL` line. The source rule is broader (six join types with type-specific push sides, arbitrary column counts and condition shapes), so this is correctly a narrower case rather than the full rule, but nothing in the encoding is unsound or misleading. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6820418
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 37449958
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1063375
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 516917
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18647458
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 37547458
  },
  "total_duration": {
    "secs": 0,
    "nanos": 72032459
  }
}
```
