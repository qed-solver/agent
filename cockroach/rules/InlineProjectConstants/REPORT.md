# InlineProjectConstants

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** the input's inlinable constant column is modeled as an INNER equality join a = c (concrete EQUALS) against a unique single-column relation (the DSL has no typed constant literals), and one occurrence of the variable column in one uninterpreted projection item is inlined to the constant column.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/inline.opt

InlineProjectConstants finds variable references in Projections expressions
that refer to constant input values, and then inlines those constant values
in place of the corresponding variable references. This sometimes allows
further simplifications such as constant folding or Project merging.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineProjectConstants`, not the other rules in that file):

```
# InlineProjectConstants finds variable references in Projections expressions
# that refer to constant input values, and then inlines those constant values
# in place of the corresponding variable references. This sometimes allows
# further simplifications such as constant folding or Project merging.
[InlineProjectConstants, Normalize]
(Project
    $input:* &
        ^(ColsAreEmpty
            $constCols:(FindInlinableConstants $input)
        )
    $projections:[
        ...
        $item:* & (ColsIntersect (OuterCols $item) $constCols)
        ...
    ]
    $passthrough:*
)
=>
(Project
    $input
    (InlineProjectionConstants $projections $input $constCols)
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures `InlineProjectConstants`' core shape — a `Project` whose input carries an inlinable constant column, with one projection item's reference to that column swapped for the constant — and the rewrite is non-vacuous (over the shared inner-equality join `a=c`, projecting `f(a)` vs `f(c)` differ structurally and are equal only because the join forces `a=c`, so the proof is of a real rewrite, not an identity). It uses the correct relational operator (inlining inside a projection, not the filter-based sibling rules), correctly shares the uninterpreted function `f` and the join across both sides while keeping `a`/`c` as distinct join fields, and encodes the inlinable-constant precondition via the concrete `EQUALS` condition, which is the rule's defining premise rather than an arbitrary hard-coded predicate. The narrowing (the constant modeled as a unique single-column relation joined by equality because the DSL has no typed literals) is specific, genuine, and honestly tagged `SCOPE: PARTIAL`, yielding a useful, non-degenerate result.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 3856459
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
    "nanos": 66875
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 380583
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 8170083
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 10614958
  }
}
```
