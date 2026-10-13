# HoistSelectNotExists

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2
**Scope detail:** the hoisted NOT EXISTS subquery is uncorrelated (references only its own input S), so the empty AntiJoinApply condition is an ANTI join under a constant-true predicate.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistSelectNotExists extracts non-existential subqueries from Select filters,
turning them into anti-joins. This eliminates the subquery, which is often
expensive to execute and restricts the optimizer's plan choices.

This rule is marked as low priority for the same reason as HoistSelectExists.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistSelectNotExists`, not the other rules in that file):

```
# HoistSelectNotExists extracts non-existential subqueries from Select filters,
# turning them into anti-joins. This eliminates the subquery, which is often
# expensive to execute and restricts the optimizer's plan choices.
#
# This rule is marked as low priority for the same reason as HoistSelectExists.
[HoistSelectNotExists, Normalize, LowPriority]
(Select
    $input:*
    $filters:[
        ...
        $item:* &
            (HasHoistableSubquery $item) &
            (FiltersItem (Not (Exists $subquery:*)))
        ...
    ]
)
=>
(Select
    (AntiJoinApply $input $subquery [] (EmptyJoinPrivate))
    (RemoveFiltersItem $filters $item)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the uncorrelated NOT EXISTS → ANTI-join rewrite: before() is σ_{¬EXISTS(σ_{sub_pred}(S)) ∧ rest}(L) and after() is σ_{rest}(L ⋈_{ANTI,true} σ_{sub_pred}(S)), which are bag-equivalent because the uncorrelated EXISTS is a constant per row of L; the fixed subquery shape (Filter over a single-column Scan) and single `rest` predicate do not narrow the semantic content (QED treats them as arbitrary uninterpreted relations/conjunctions), and the SCOPE tag accurately identifies the one genuine restriction—uncorrelated subquery, hence constant-true anti-join condition—which is a real DSL limitation since RuleScript's RexRN has no mechanism to embed a correlated EXISTS subquery inside a filter predicate. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8593292
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36469958
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 849708
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 527625
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22137334
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36588625
  },
  "total_duration": {
    "secs": 0,
    "nanos": 74303917
  }
}
```
