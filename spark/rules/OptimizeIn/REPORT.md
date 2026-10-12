# OptimizeIn

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 9  **Verification rounds used:** 1
**Scope detail:** assumes the IN list is the minimal dedup-to-one case (exactly two occurrences of the same expression e), so In(v, [e, e]) is encoded as (v = e) OR (v = e) reduced to v = e, omitting the empty-list, longer-list, and InSet-threshold branches.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 355-385
```

## Independent verifier review

**Verdict:** CONFIRMED

The before() (filter `Or(eq, eq)`) and after() (filter `eq`) are genuinely distinct shapes, so the proof is non-vacuous; the single shared `V` type and shared field `e` are correctly uninterpreted (not a concrete predicate or fixed operator) and the reused `eq` symbol faithfully encodes the source rule's "identical list elements" precondition, since `In(v,[e,e])` is semantically `(v=e) OR (v=e)`. The only caveat is the rule is a narrow, honestly-tagged special case (dedup-to-one / two-identical-elements), with the empty-list, InSet-threshold, and general-dedup branches omitted as the SCOPE line states — a legitimate limitation given QED has no `In`/`InSet` operator and can't express the `inSetConvertible`/threshold conditions — but the encoded fragment is a specific, non-degenerate rewrite, not a vacuous `before()==after()`.

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
    "nanos": 324375
  }
}
```
