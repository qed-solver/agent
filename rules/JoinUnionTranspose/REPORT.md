# JoinUnionTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 33  **Verification rounds used:** 2
**Scope detail:** INNER join, UNION ALL on the left side, single-column scans, and a single shared join-condition symbol C


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinUnionTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous instance of the rule: before() = (X ∪ALL Y) ⋈_C O vs after() = (X ⋈_C O) ∪ALL (Y ⋈_C O) is a genuine distributivity law (structurally distinct shapes, not an identity), the double `union(true, ...)` matches the source's `union.all` requirement, INNER keeps the union off any null-generating side so no semantic precondition is dropped, and the single shared condition symbol C correctly mirrors the source reusing the identical `join.getCondition()` in all three joins (independent symbols would have been the error). Nullable, keyless scans match the source's lack of PK/NOT NULL preconditions, so no hidden constraint is assumed. The scope line is honest and specific about the genuine narrowing (INNER join, left-side union) — full multi-join-type coverage cannot be a single provable rule because the join kind is concrete in QED's serialization (meta join types only feed the family generator, whose assignments are proven individually) — and the result is still a substantive, useful special case of the real rule. ```

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
    "nanos": 862958
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 624917
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
    "nanos": 1883000
  }
}
```
