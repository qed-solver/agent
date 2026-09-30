# RemoveNotNullCondition

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** public record RemoveNotNullCondition() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

RemoveNotNullCondition removes a filter with an IS NOT NULL condition
when the given column has a NOT NULL constraint.

Extracted from `select.opt` (which defines multiple rules — implement specifically `RemoveNotNullCondition`, not the other rules in that file):

```
# RemoveNotNullCondition removes a filter with an IS NOT NULL condition
# when the given column has a NOT NULL constraint.
[RemoveNotNullCondition, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (IsNot
                (Variable $col:* & (IsColNotNull $col $input))
                (Null)
            )
        )
        ...
    ]
)
=>
(Select $input (RemoveFiltersItem $filters $item))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's single essential precondition — `IsColNotNull $col $input` — by giving column 0 a non-nullable type, and the rewrite differs before/after by exactly the tautological `IS NOT NULL` conjunct on that column, with all remaining filter items collapsed into one uninterpreted predicate (exactly equivalent to a list of conjuncts). The proof being accepted confirms the prover concretely interprets `IS NOT NULL` via the schema's nullability metadata — had it been treated as an uninterpreted symbol, `filter(other).filter(isNotNull) ≡ filter(other)` would be unprovable — so the proof is of the genuine claim, not a vacuous one. The choices to fix the subject column at ordinal 0 and use two columns are semantically inert (the equivalence is local to the filter operator and depends only on that column's nullability, not its position or the input's column count), and since Select filter lists are conjunctions, the single `other` predicate covers arbitrary remaining items; this is the full rule, so `// SCOPE: FULL` is honest.

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
    "nanos": 410209
  }
}
```
