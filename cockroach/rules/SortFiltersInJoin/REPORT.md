# SortFiltersInJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 27  **Verification rounds used:** 2
**Scope detail:** swaps two distinct ON-filter items in an INNER join (minimal 2-element unsorted list) rather than sorting an arbitrary-length filter list with the backend's SortFilters


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

SortFiltersInJoin ensures that any filters in an inner join are canonicalized
by sorting them.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SortFiltersInJoin`, not the other rules in that file):

```
# SortFiltersInJoin ensures that any filters in an inner join are canonicalized
# by sorting them.
[SortFiltersInJoin, Normalize]
(InnerJoin
    $left:*
    $right:*
    $on:* & ^(AreFiltersSorted $on)
    $private:*
)
=>
(InnerJoin $left $right (SortFilters $on) $private)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the only semantic content of SortFiltersInJoin — that reordering conjunctive ON-filters of an InnerJoin is bag-semantics-preserving — using two distinct uninterpreted predicates over the full join fields with a transposition, which is the minimal non-degenerate instance of the backend's sort (before() and after() are genuinely different plans, and the proof is a universal one over all instantiations of a, b, L, R). The full general rule (arbitrary-length filter list, SortFilters function, AreFiltersSorted guard) is unmodelable in QED because it has no list/ordering semantics, so the narrowing is forced by a real QED limitation rather than a missing DSL capability; the join kind (INNER, exactly as the source) and distinct-symbol usage are correct, no semantic precondition is missing (AreFiltersSorted is a firing side-condition, and the unconditional proof is strictly stronger), and the PARTIAL scope line is honest and specific. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6721875
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33892084
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 882709
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 419583
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18794750
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33991459
  },
  "total_duration": {
    "secs": 0,
    "nanos": 68643166
  }
}
```
