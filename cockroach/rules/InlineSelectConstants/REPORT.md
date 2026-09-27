# InlineSelectConstants

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 49  **Verification rounds used:** 4
**Scope detail:** the input's inlinable constant column is modeled as an INNER equality join a = c (concrete EQUALS) against a unique single-column relation (the DSL has no typed constant literals), and one occurrence of the variable column in one uninterpreted filter item is inlined to the constant column.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/inline.opt

InlineSelectConstants finds variable references in Filters expressions that
refer to constant input values, and then inlines those constant values in
place of the corresponding variable references. This sometimes allows further
simplifications such as constant folding or generation of constrained scans.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineSelectConstants`, not the other rules in that file):

```
# InlineSelectConstants finds variable references in Filters expressions that
# refer to constant input values, and then inlines those constant values in
# place of the corresponding variable references. This sometimes allows further
# simplifications such as constant folding or generation of constrained scans.
[InlineSelectConstants, Normalize]
(Select
    $input:* &
        ^(ColsAreEmpty
            $constCols:(FindInlinableConstants $input)
        )
    $filters:[
        ...
        $item:* & (ColsIntersect (OuterCols $item) $constCols)
        ...
    ]
)
=>
(Select
    $input
    (InlineFilterConstants $filters $input $constCols)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and faithful: before() filters on field 0 (`a`) and after() on field 1 (`c`), which are distinct columns whose equivalence follows solely from the INNER join condition `a = c`, so the proof captures exactly the rule's core soundness claim — substituting an equal constant value for a column reference inside a filter predicate — with the constant legitimately modeled as a unique single-column relation joined by concrete EQUALS since the DSL lacks typed constant literals. The uninterpreted predicate `g` is correctly shared across both sides (inlining changes the referenced column, not the predicate), the symbol sharing between the join condition and the filter references is semantically right (same physical columns), and no missing precondition changes the claim (the join invariant `a = c` on every input row is precisely what inlining requires, and the original's "inlinable constant" case is a special case of it). The `SCOPE: PARTIAL` line is honest and specific: it narrows to one uninterpreted filter item with one occurrence of one argument and one constant column, which is a genuine, non-degenerate special case of CockroachDB's `InlineSelectConstants` rather than a structural identity. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6112250
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35540209
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 913708
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 386208
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16767250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35642458
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67750542
  }
}
```
