# InlineJoinConstantsLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** the left input's inlinable constant column is modeled as an INNER equality join a = c (concrete EQUALS) against a unique single-column relation (the DSL has no typed constant literals), and one occurrence of the variable column in one uninterpreted INNER join conjunct is inlined to the constant column.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/inline.opt

InlineJoinConstantsLeft finds variable references in a join condition that
refers to constant values projected by the left input. It then inlines those
constant values in place of the corresponding variable references. This
sometimes allows further simplifications such as constant folding or filter
pushdown.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineJoinConstantsLeft`, not the other rules in that file):

```
# InlineJoinConstantsLeft finds variable references in a join condition that
# refers to constant values projected by the left input. It then inlines those
# constant values in place of the corresponding variable references. This
# sometimes allows further simplifications such as constant folding or filter
# pushdown.
[InlineJoinConstantsLeft, Normalize]
(Join
    $left:* &
        ^(ColsAreEmpty $constCols:(FindInlinableConstants $left))
    $right:*
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
    (InlineFilterConstants $on $left $constCols)
    $private
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's semantic essence — the original's inlinable constant column maps to `a`, the inlined-to constant value (a literal the DSL can't express) maps to `c` of a unique 1-row relation, `a = c` is enforced by the INNER join that builds the left input, and the single uninterpreted conjunct `H(a, r)` is rewritten to `H(c, r)`, which is exactly the original's "substitute the constant value for the constant-column reference in the ON condition." `before()` and `after()` genuinely differ (field 0 vs field 1), the `a = c` constraint is load-bearing so the equivalence is non-vacuous, `H` and the three scans are shared/independent correctly, and INNER is a valid instance of the rule's join; the flagged PARTIAL scope (constant realized via a join-with-unique-rel, one conjunct, one occurrence, INNER) is a genuine DSL limitation — the language has no typed constant literals and no way to assert that an opaque column is constant — so this is an honest, non-degenerate special case rather than a vacuous or unsound one.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7482458
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 37019875
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 867167
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 510625
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19517250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 37152792
  },
  "total_duration": {
    "secs": 0,
    "nanos": 72319375
  }
}
```
