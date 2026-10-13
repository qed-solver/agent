# JoinToHyperGraph

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** a 3-way inner-join tree whose left child is a 2-way join denoting an existing HyperGraph, flattened into the 3-input hypergraph and enumerated as the right-deep tree A⋈(B⋈C) rather than the original left-deep (A⋈B)⋈C, rather than the rule's general flattening of arbitrary input trees and all its supported join types


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinToHyperGraphRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-trivial special case: before is the left-deep tree (A⋈_{pAB}B)⋈_{pAC∧pBC}C and after is the right-deep tree A⋈_{pAB∧pAC}(B⋈_{pBC}C), both using the same three uninterpreted predicates with correct field indices, proving the core property that inner-join hypergraph enumeration order is semantically irrelevant. The SCOPE:PARTIAL tag accurately and specifically states the restrictions (3-way, inner-only, fixed left-child-HyperGraph shape, one particular re-enumeration), which are genuine limitations of expressing the general N-input, multi-join-type flattening into the current DSL without a HyperGraph operator.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9674124
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33918792
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 901916
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 664041
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 24256042
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34047458
  },
  "total_duration": {
    "secs": 0,
    "nanos": 75001291
  }
}
```
