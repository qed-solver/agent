# NullPropagation

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 26  **Verification rounds used:** 2
**Scope detail:** only the `IsNull(c) -> false` branch is encoded, applied where `c` is statically non-nullable; Spark's NullPropagation rule also folds IsNotNull(non-nullable) -> true, Coalesce null-stripping, In/InSubquery folds, Count folds, and EqualNullSafe, which rely on operator semantics (coalesce, IN, aggregates, Spark-specific null-safe equality) that QED does not model


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 992-1061
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous instance of the source rule's `IsNull(c) if !c.nullable → Literal(false)` branch: the non-nullable precondition is actually carried in the encoding via the non-nullable `VarType` (and `unique=false` avoids smuggling in a key), `IS_NULL` is the exact operator Spark folds (not a baked-in stand-in), and `before()`/`after()` are structurally distinct (IS_NULL predicate vs. false literal under the same filter), so QED's proof certifies a real rewrite rather than an identity. The scope line honestly tags it PARTIAL and correctly enumerates the unencoded branches (IsNotNull, Coalesce, In/InSubquery, Count, EqualNullSafe), which depend on operator semantics (aggregates, IN, null-safe equality) that QED does not model, making the narrowing genuine and the retained rule non-degenerate. ```

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
    "nanos": 359125
  }
}
```
