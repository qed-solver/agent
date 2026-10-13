# FoldIndirection

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** a 2-element statically constructed array in projection position (a ProjectMany building two arbitrary scalar expressions over a base), with a constant 1-based indirection index 2 (0-based ordinal 1, the in-range case of Cockroach's FoldIndirection), replaced by projecting the referenced element directly.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldIndirection eliminates a constant array indirection operator applied to an
array with a statically known number of elements, like this:

ARRAY[i, i+1][1]
ARRAY[1, 2, 3][2]

The rule replaces the indirection operator with the referenced array element.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldIndirection`, not the other rules in that file):

```
# FoldIndirection eliminates a constant array indirection operator applied to an
# array with a statically known number of elements, like this:
#
#   ARRAY[i, i+1][1]
#   ARRAY[1, 2, 3][2]
#
# The rule replaces the indirection operator with the referenced array element.
[FoldIndirection, Normalize]
(Indirection
    $input:*
    $index:* &
        (IsConstValueOrGroupOfConstValues $index) &
        (Let ($result $ok):(FoldIndirection $input $index) $ok)
)
=>
$result
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is non-vacuous — before() is a nested two-column projection plus a field access while after() is a single direct projection, so QED genuinely had to prove that a constant-ordinal field access through a freshly built row composes to the referenced element expression (with correct symbol sharing: distinct uninterpreted e0/e1, and e1 consistently reused as the referenced element), which is exactly FoldIndirection's essence of replacing a constant-index access on a statically constructed composite with the element itself. The narrowing to a 2-element array with a fixed in-range index is forced by the DSL (field ordinals are concrete ints and QED's JSON format has no symbolic index operator, so no extension could express "any constant index"), not an avoidable hard-coding, and is precisely and specifically disclosed in the SCOPE: PARTIAL line. ```

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
    "nanos": 358042
  }
}
```
