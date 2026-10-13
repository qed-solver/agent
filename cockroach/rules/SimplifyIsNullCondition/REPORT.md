# SimplifyIsNullCondition

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** public record SimplifyIsNullCondition() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

SimplifyIsNullCondition replaces a filter with an IS NULL condition with
False when the given column has a NOT NULL constraint.

Extracted from `select.opt` (which defines multiple rules — implement specifically `SimplifyIsNullCondition`, not the other rules in that file):

```
# SimplifyIsNullCondition replaces a filter with an IS NULL condition with
# False when the given column has a NOT NULL constraint.
[SimplifyIsNullCondition, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Is
                (Variable $col:* & (IsColNotNull $col $input))
                (Null)
            )
        )
        ...
    ]
)
=>
(Select $input [ (FiltersItem (False)) ])
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is structurally non-vacuous and captures the exact logical content of the rule: `before()` is the conjunction of an arbitrary uninterpreted predicate `other` with `x IS NULL` on a non-nullable column 0, and `after()` is a filter by the false literal, so the proven claim is precisely "a conjunct refutable by a NOT NULL guarantee makes the whole filter refutable, degenerating the Select to a false filter" — which is what the source rule does (replace the entire filter list with `[False]`). The key precondition `IsColNotNull` is faithfully encoded via the non-nullable `Col_Type`, and the near-zero proof time is consistent with the prover constant-folding IS NULL on a non-nullable column to false, making both sides empty; using the concrete `SqlStdOperatorTable.IS_NULL` (rather than an uninterpreted symbol) is correct because that null-awareness is the whole mechanism of the rule, and the other conjuncts are correctly left as a single uninterpreted predicate (instantiable to true, covering the zero-extra-conjuncts case). The 2-column scan (NOT NULL column + nullable stand-in for all remaining columns, touched only by the uninterpreted predicate) is the standard faithful stand-in for an arbitrary input relation of any width, the nested-filter conjunction matches the source's filter list, and the input symbol is shared unchanged between sides, so `// SCOPE: FULL` is honest. ```

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
    "nanos": 190834
  }
}
```
