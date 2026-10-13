# DeMorganNotOr

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** public record DeMorganNotOr() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 326-332
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a non-vacuous De Morgan rewrite under a generic row-wise filter, with `left` and `right` as independent uninterpreted predicates, so it captures the rule’s full semantic generality without hard-coded structure. Using `Not(left)`/`Not(right)` is an appropriate uninterpreted-predicate abstraction of the source’s recursively negated children, since their internal structure is irrelevant to the semantic equivalence being proven.

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
    "nanos": 126416
  }
}
```
