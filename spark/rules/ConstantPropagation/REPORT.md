# ConstantPropagation

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** one filter with one attribute mapped to a constant via an equality conjunct (modeled as an INNER join a = c on a unique single-column relation, since the DSL has no typed constant literals); the constant is inlined into exactly one other uninterpreted conjunct, and the equality conjunct itself is kept on both sides.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 138-270
```

## Independent verifier review

**Verdict:** CONFIRMED

before() and after() genuinely differ (g(a) vs g(c) on the two distinct join fields), so the proof is non-vacuous: QED must use congruence of the uninterpreted predicate g under the join-enforced a=c to show the bags match. The encoding faithfully captures the rule's core transform — inline a constant for an attribute in one conjunct while keeping the equality conjunct on both sides — with the literal modeled as a unique single-column relation joined on a=c (a sound DSL workaround, since on every join row a=c and thus g(a)≡g(c), and null-a rows are excluded by the inner join). It is a narrow but honestly-tagged PARTIAL special case (single attribute, single equality conjunct, single top-level conjunct, no Or/Not nesting or collation/null-safety branching), and that restriction is genuine, specific, and still yields a non-degenerate, useful rule. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6424332
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35579375
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1121833
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 475583
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 17475583
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35705541
  },
  "total_duration": {
    "secs": 0,
    "nanos": 69250250
  }
}
```
