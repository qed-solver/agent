# FoldNullInNonEmpty

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** encodes only the In branch over a fixed 2-element non-empty tuple; NotIn and other non-empty arities are structurally identical but each needs its own before()/after() pair


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullInNonEmpty replaces the In/NotIn with null when the left input is
null and the right input is not empty. Null is the unknown value, and if the
set is non-empty, it is unknown whether it's in/not in the set.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullInNonEmpty`, not the other rules in that file):

```
# FoldNullInNonEmpty replaces the In/NotIn with null when the left input is
# null and the right input is not empty. Null is the unknown value, and if the
# set is non-empty, it is unknown whether it's in/not in the set.
[FoldNullInNonEmpty, Normalize]
(In | NotIn (Null) (Tuple ^[]))
=>
(Null (BoolType))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding correctly models `NULL IN (a,b)` as `filter(Or(=(null,a), =(null,b)))` and the folded result as `filter(nullBool)`; both sides reduce to the empty relation under SQL three-valued logic (any comparison with NULL yields NULL, NULL OR NULL is NULL, and a NULL filter condition excludes the row), so the proof is non-vacuous and structurally distinct. The PARTIAL scope is honestly stated — fixed 2-element arity and the In-branch only — but the instance is a genuine, non-degenerate special case of the original rule that exercises the same three-valued-logic reasoning.

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
    "nanos": 284500
  }
}
```
