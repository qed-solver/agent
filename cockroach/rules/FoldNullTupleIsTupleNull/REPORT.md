# FoldNullTupleIsTupleNull

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** the tuple is fixed to arity 2 (both of its elements are the constant NULL, standing in for the side condition HasAllNullElements).


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

FoldNullTupleIsTupleNull replaces x IS NULL with True if x is a tuple with
only constant, null elements.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNullTupleIsTupleNull`, not the other rules in that file):

```
# FoldNullTupleIsTupleNull replaces x IS NULL with True if x is a tuple with
# only constant, null elements.
[FoldNullTupleIsTupleNull, Normalize]
(IsTupleNull $input:(Tuple) & (HasAllNullElements $input))
=>
(True)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's semantic content: IsTupleNull on a constant all-null tuple is expanded to the conjunction of element-wise `IS NULL` predicates (the operator's defining semantics), and since the source side condition HasAllNullElements requires every element to be exactly the NULL constant, reusing a single NULL literal for both elements is correct rather than a symbol-sharing error (independent uninterpreted elements would be a different, unprovable claim). The proof is not vacuous — before() filters on the closed conjunction IS_NULL(NULL) AND IS_NULL(NULL) while after() filters on TRUE, and the provable content is precisely the constant fold the rule performs. The restriction to tuple arity 2 is honestly tagged (SCOPE: PARTIAL) and genuine, since RuleScript has no tuple value constructor or IsTupleNull operator and the QED prover has no tuple-value semantics to model a general-arity version against, so this is a legitimate non-degenerate special case rather than an avoidable gap. ```

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
    "nanos": 287209
  }
}
```
