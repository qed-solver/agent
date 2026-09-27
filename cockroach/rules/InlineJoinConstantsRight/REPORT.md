# InlineJoinConstantsRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** the right input's inlinable constant column is modeled as an INNER equality join r = c (concrete EQUALS) against a unique single-column relation (the DSL has no typed constant literals), and one occurrence of the variable column in one uninterpreted INNER join conjunct is inlined to the constant column.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/inline.opt

InlineJoinConstantsRight finds variable references in a join condition that
refers to constant values projected by the right input. It then inlines those
constant values in place of the corresponding variable references. This
sometimes allows further simplifications such as constant folding or filter
pushdown.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineJoinConstantsRight`, not the other rules in that file):

```
# InlineJoinConstantsRight finds variable references in a join condition that
# refers to constant values projected by the right input. It then inlines those
# constant values in place of the corresponding variable references. This
# sometimes allows further simplifications such as constant folding or filter
# pushdown.
[InlineJoinConstantsRight, Normalize]
(Join
    $left:*
    $right:* &
        ^(ColsAreEmpty
            $constCols:(FindInlinableConstants $right)
        )
    $on:[
        ...
        $item:* & (ColsIntersect (OuterCols $item) $constCols)
        ...
    ]
    $private:* & (NoJoinHints $private)
)
=>
((OpName)
    $left
    $right
    (InlineFilterConstants $on $right $constCols)
    $private
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-trivial and faithful: `before()` references right-input column 0 (`r`) in the join condition while `after()` references column 2 (`c`), and QED must genuinely derive their equality from the inner join's `r = c` constraint on the right input — this is a real semantic consequence, not a structural identity. The porter correctly preserves the original rule's structure (same inputs, same join kind, only the condition's column reference changes), uses an uninterpreted `H` for the join predicate (matching the rule's arbitrary `$on`), models the constant via a unique single-column relation (a sound proxy for a literal since the DSL lacks typed constants), and honestly tags the scope as PARTIAL with specific, concrete restrictions (INNER join only, single conjunct, one occurrence, concrete EQUALS for the constant-enforcing inner join).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5434085
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6552125
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 76250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 462084
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 10900209
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6598958
  },
  "total_duration": {
    "secs": 0,
    "nanos": 20083000
  }
}
```
