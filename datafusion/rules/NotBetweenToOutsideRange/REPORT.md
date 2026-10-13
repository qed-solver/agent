# NotBetweenToOutsideRange

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 102  **Verification rounds used:** 6
**Scope detail:** public record NotBetweenToOutsideRange() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 352-359
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful expansion of the source rule: `before()` is `NOT(A>=B AND A<=C)` (the standard expansion of `NOT (A BETWEEN B AND C)`, matching the rule's left side) and `after()` is `A<B OR A>C` (the standard outside-range expansion of `A NOT BETWEEN B AND C`, matching the rule's right side) — a non-trivial, structurally-distinct pair. Because the DSL has no BETWEEN/NOT-BETWEEN node, expanding to comparison operators is the correct approach, the strict-vs-inclusive boundaries are right (BETWEEN inclusive, NOT-BETWEEN strict), and A, B, C are three independent columns of the same comparable virtual type, so the proof universally quantifies over all their values with no symbol-sharing or missing-precondition issues.

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
    "nanos": 441792
  }
}
```
