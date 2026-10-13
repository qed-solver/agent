# FoldNotInEmpty

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** the scalar NotIn predicate over an empty tuple is modeled as a relational anti-join against a zero-row relation, since QED has no scalar In/NotIn predicate or list constant.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNotInEmpty replaces the NotIn with True when the right input is empty.
Note that this is correct even if the left side is Null, since even an unknown
value can't be in an empty set.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNotInEmpty`, not the other rules in that file):

```
# FoldNotInEmpty replaces the NotIn with True when the right input is empty.
# Note that this is correct even if the left side is Null, since even an unknown
# value can't be in an empty set.
[FoldNotInEmpty, Normalize]
(NotIn * (Tuple []))
=>
(True)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous — before() is an ANTI join of L against a genuine zero-row relation and after() is the bare scan L — and the proved identity (anti-join with the empty set is the identity, for every uninterpreted condition, key, and type) is exactly the relational reading of "x NOT IN () ≡ TRUE, so the node disappears," including the NULL-left case the source comment explicitly calls out. ANTI is the correct join kind for NotIn (a semi-join would model IN), `(Tuple [])` is correctly a zero-row `LogicalValues` (not a Filter(False)), and all backend-specific elements (L_Type, R_Type, cond) are uninterpreted with no missing preconditions since the source rule has none. The PARTIAL tag is honest and specific: the DSL has no scalar In/NotIn operator or list constant, so the scalar rule is embedded in its relational filter/join context — a genuine, non-degenerate special case that remains a useful plan simplification. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6230041
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 25437375
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 860750
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 346333
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18297916
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 25540542
  },
  "total_duration": {
    "secs": 0,
    "nanos": 60533417
  }
}
```
