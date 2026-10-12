# ReplaceNullWithFalseInPredicate

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** encodes only the Filter context where a NULL boolean literal sits in one arm of a top-level And, i.e. Filter(And(p, NULL), R) => Filter(And(p, FALSE), R); the Or/If/CaseWhen/Not-In/Like-match-all/Join-and-write-action contexts of the rule are not encoded


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/ReplaceNullWithFalseInPredicate.scala, lines 53-183
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (Filter(And(p, NULL), R)) and after() (Filter(And(p, FALSE), R)) are structurally distinct, and the proof is of the genuine universal claim — for every row of every relation and every uninterpreted predicate p, p∧NULL and p∧FALSE agree in filter (three-valued) semantics — which is exactly the null-as-false justification the Spark rule relies on in a Filter context. The only predicate is an uninterpreted symbol correctly shared across both sides, types are all consistently boolean, and the shape (null in one arm of a top-level And under a Filter) matches what replaceNullWithFalse actually produces there. The encoding is admittedly a narrow fragment — Join/DML/Or/CaseWhen/If/Not-In/Like contexts are not covered (and most are beyond the DSL anyway, e.g. no LIKE/In/lambda operators, no DML nodes) — but the SCOPE: PARTIAL line is accurate and specific, and the result is non-degenerate: had the other literal been TRUE instead of FALSE the equivalence would fail, so the verdict validates the real semantics rather than a vacuous before()==after() coincidence. ```

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
    "nanos": 201375
  }
}
```
