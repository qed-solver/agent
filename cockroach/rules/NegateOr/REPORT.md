# NegateOr

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** public record NegateOr() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

NegateOr converts the negation of a disjunction into a conjunction of
negations.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `NegateOr`, not the other rules in that file):

```
# NegateOr converts the negation of a disjunction into a conjunction of
# negations.
[NegateOr, Normalize]
(Not (Or $left:* $right:*))
=>
(And (Not $left) (Not $right))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures NegateOr — `Not(Or(L,R))` ⟹ `And(Not(L),Not(R))` — using two *distinct* uninterpreted predicates (`left`, `right`) over an uninterpreted base table, so `before()` and `after()` are structurally different and the proof is a genuine universal De Morgan proof rather than a vacuous identity. The source rule is a pure scalar rewrite with no preconditions (and the duality holds even under 3-valued null logic), so nothing is missing; the single-column scan + filter wrapper is the DSL's standard idiom for boolean rewrites (as in the FilterMerge example), and since the identity is row-wise and independent of the predicate's argument structure, the one-column/uninterpreted-predicate setup does not narrow the logical claim — hence `// SCOPE: FULL` is honest. ```

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
    "nanos": 355959
  }
}
```
