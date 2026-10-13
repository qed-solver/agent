# FilterMerge

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache Calcite
**Porter attempts used:** 1  **Verification rounds used:** 1
**Scope detail:** public record FilterMerge() implements RRule {


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterMergeRule.java
```

## Independent verifier review

**Verdict:** NO_VERIFIER

(no verifier LLM configured — result not independently reviewed)

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5733459
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34623208
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 823500
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 319667
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16776333
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34726625
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67062875
  }
}
```
