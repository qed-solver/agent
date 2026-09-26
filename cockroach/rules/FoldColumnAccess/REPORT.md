# FoldColumnAccess

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 43  **Verification rounds used:** 3
**Scope detail:** a 2-element statically constructed tuple in projection position: a ProjectMany building two arbitrary scalar expressions over a base, with a single-column field access on that tuple, replaced by projecting the accessed element directly.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldColumnAccess eliminates a column access operator applied to a tuple value
that is statically constructed, like this:

(((i, i+1) as foo, bar)).foo
(((1, 2) as foo, bar)).bar

The rule replaces the column access operator with the referenced tuple
element.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldColumnAccess`, not the other rules in that file):

```
# FoldColumnAccess eliminates a column access operator applied to a tuple value
# that is statically constructed, like this:
#
#   (((i, i+1) as foo, bar)).foo
#   (((1, 2) as foo, bar)).bar
#
# The rule replaces the column access operator with the referenced tuple
# element.
[FoldColumnAccess, Normalize]
(ColumnAccess
    $input:*
    $idx:* &
        (Let ($result $ok):(FoldColumnAccess $input $idx) $ok)
)
=>
$result
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() nests a single-column field access on top of a two-element tuple construction while after() drops the intermediate construction, so the proof is of a real, non-vacuous identity in the correct relational shape (Project of a field over a constructed row ⇒ Project of the element) that faithfully models "column access into a statically constructed tuple folds to the referenced element," with the statically-constructed-tuple precondition correctly represented by the intermediate ProjectMany. The tuple elements e0/e1 and the base table are fully uninterpreted and kept as independent symbols, so the proven identity holds for arbitrary element expressions (columns, constants, nested expressions) exactly as the source rule requires, with no coincidental over-constraint. The only hard-coded choices are tuple arity 2 and accessed index 1, which cannot be uninterpreted in this fixed-shape pattern language (projection width and field ordinal are concrete), match the rule's own documented two-tuple examples, and are precisely disclosed by the PARTIAL tag — a genuine, non-degenerate special case. ```

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
    "nanos": 467125
  }
}
```
