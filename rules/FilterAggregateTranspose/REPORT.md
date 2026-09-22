# FilterAggregateTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 13  **Verification rounds used:** 1
**Scope detail:** Group.SIMPLE, a single identity group key over a two-column input, one non-distinct aggregate call, one pushed conjunct depending only on the group key, one remaining conjunct over the aggregate output.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterAggregateTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a non-vacuous, faithful instance of the rule's core transformation: the conjunct P depending only on the (identity) group key moves from the filter above the SIMPLE aggregate to a filter below it, while the conjuncts over the aggregate's output stay above, and P is correctly one shared uninterpreted symbol applied to the corresponding key column on both sides (modeling the rule's identity RexInputConverter). Operator usage and preconditions match the source — Filter-over-Aggregate shape, SIMPLE grouping, non-distinct uninterpreted aggregate, groupCount>0 and canPush's key-only condition built in, and per-key filtering commutes with group-by even under null semantics — with the two-column scan-join input being the standard DSL construction for a multi-column table (scans are single-column), not a fixed join the rule constrains. The narrowings (single identity group key, one aggregate call, one pushed conjunct) are genuine shape restrictions, honestly and specifically disclosed in the PARTIAL scope line, and since the proven equivalence is the exact semantic atom the rule applies per conjunct (the extra key-referencing remaining conjunct Qa is inert to P's movement), the provable verdict is meaningful rather than coincidental. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 13193667
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 53267500
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 980750
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1013875
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 30671375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 53538416
  },
  "total_duration": {
    "secs": 0,
    "nanos": 100425542
  }
}
```
