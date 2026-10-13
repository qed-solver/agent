# NormalizeInConst

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** encodes only the In branch with a fixed-shape tuple (a, b, a, b) over two distinct values modeled as source columns; the NotIn branch and arbitrary arities follow the same disjunction-idempotency argument.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

NormalizeInConst ensures that the In operator's tuple operand is sorted with
duplicates removed (since duplicates do not change the result).

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `NormalizeInConst`, not the other rules in that file):

```
# NormalizeInConst ensures that the In operator's tuple operand is sorted with
# duplicates removed (since duplicates do not change the result).
[NormalizeInConst, Normalize]
(In | NotIn
    $left:*
    $right:(Tuple $elems:*) & (NeedSortedUniqueList $elems)
)
=>
((OpName) $left (Tuple (ConstructSortedUniqueList $elems)))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (x IN (a,b,a,b), a 4-operand OR of EQUALS) and after() (x IN (a,b), a 2-operand OR) differ structurally by exactly the duplicate disjuncts, so the proof is non-vacuous and captures the rule's actual justification — dedup of the IN list, i.e. OR-idempotency (order/sorting is absorbed by OR-commutativity in the prover). Symbol sharing is correct: x, a, b are independent uninterpreted columns of a constraint-free scan as in reality, eqA/eqB are reused at the duplicate positions, and modeling the tuple elements as source columns is a safe over-generalization that subsumes the rule's constant-tuple case, so the proved claim transfers to real queries. The declared SCOPE is honest and specific — only the In branch with the fixed duplicated shape (a,b,a,b) is encoded, with NotIn and other arities omitted but following the same idempotency fact — making this a faithful, non-degenerate special case of NormalizeInConst. ```

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
    "nanos": 362667
  }
}
```
