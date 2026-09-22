# UnionEliminator

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache Calcite
**Porter attempts used:** 10  **Verification rounds used:** 1
**Scope detail:** public record UnionEliminator() implements RRule {


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/UnionEliminatorRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding exactly mirrors the rule's `matches` condition for the union variant — a set op with `all=true` over exactly one input (`input.union(true)`) rewritten to that same input — so the proof is the genuine, non-vacuous bag-semantic theorem that a one-input UNION ALL equals its input, with a shared uninterpreted `Input` scan standing in for any relation of any type (nothing concrete is baked in). The `SCOPE: FULL` tag is honest for the rule as named: the file's INTERSECT and MINUS configs are separate rules (IntersectEliminator/MinusEliminator), and their `all=true` (bag) form is a documented QED limitation (only the set variant of INTERSECT/MINUS is modeled, per the serializer's `when !all` guards), so the porter assumed nothing the UnionEliminator rule itself does not require — this is the full rule, not a narrowed special case.

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
    "nanos": 277834
  }
}
```
