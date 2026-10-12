# ExtractGroupingPythonUDFFromAggregate

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** single-column input, one grouping key that is a scalar deterministic PythonUDF g applied to that column, and one non-distinct aggregate f over the same column.


## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/python/ExtractPythonUDFs.scala, lines 92-150
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding correctly models the rule's core transformation—pulling an uninterpreted UDF g out of a grouping expression into a Project below the Aggregate and replacing the expression with a column reference—using consistent symbol sharing (same "g" operator in the groupBy expression and the projection, same "f" aggregate operator in both AggCalls) so the proof is non-vacuous. It is a genuinely narrow special case (single column, one group key, one non-distinct aggregate over that same column, UDF applied directly to the column rather than an arbitrary sub-expression, no distinct flag, no multiple group keys or aggregates), which the SCOPE: PARTIAL line accurately and specifically states; the full rule handles any number of columns, keys, and aggregates and UDFs over arbitrary sub-expressions, but the encoded fragment is non-degenerate (before is Aggregate(Scan,…g(x)…f(x)…) while after is Aggregate(Project(Scan,[g(x),x]),…col0…f(col1)…), and the equivalence rests on genuine bag-semantic reasoning about grouping by the same value and feeding the same input bag to the uninterpreted aggregate, not on structural identity.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 11115915
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 46910792
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1138458
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 844834
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 26726417
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 47155958
  },
  "total_duration": {
    "secs": 0,
    "nanos": 90344791
  }
}
```
