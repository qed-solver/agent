# NullDownPropagation

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 26  **Verification rounds used:** 2
**Scope detail:** only the deterministic-comparison branch's IsNull arm (IsNull(x < y) ⟹ IsNull(x) OR IsNull(y)) is encoded; Spark's rule also covers the IsNotNull arm and the Not/GreaterThan/GreaterThanOrEqual/LessThanOrEqual operator families, and each concrete operator/arm needs its own before()/after() pair since QED cannot derive null propagation for an uninterpreted operator symbol


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 1068-1094
```

## Independent verifier review

**Verdict:** CONFIRMED

before() and after() are structurally and semantically distinct (Filter(IS_NULL(LT(x,y))) vs Filter(OR(IS_NULL(x), IS_NULL(y)))) over two independent columns of the scan, so the proof is not vacuous; the fact that it proves at all shows QED has real null-propagation semantics for IS_NULL/LT — an uninterpreted operator symbol could never satisfy IsNull(f(x,y)) <=> IsNull(x) OR IsNull(y), so LessThan is the minimal concrete choice and its nullIntolerant/deterministic guards are baked in by operator identity, not silently dropped. The PARTIAL scope is honest and specific (only the IsNull arm for the LT branch of Spark's five-operator/two-arm rule), the narrowing is a fundamental QED limitation (no null-propagation reasoning for uninterpreted op symbols, no DSL mechanism to attach such a contract) rather than a lazy escape, and the encoded rule is a genuine, non-degenerate instance of the original. ```

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
    "nanos": 368625
  }
}
```
