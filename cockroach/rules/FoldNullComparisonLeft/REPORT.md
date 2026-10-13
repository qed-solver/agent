# FoldNullComparisonLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 26  **Verification rounds used:** 2
**Scope detail:** only the Eq branch of the 18-operator fold (eq(Null, x) ⇒ Null) is encoded; the other 17 branches are structurally identical but each requires its own before()/after() pair


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

FoldNullComparisonLeft replaces the comparison operator with null if its
left input is null.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNullComparisonLeft`, not the other rules in that file):

```
# FoldNullComparisonLeft replaces the comparison operator with null if its
# left input is null.
[FoldNullComparisonLeft, Normalize]
(Eq | Ne | Ge | Gt | Le | Lt | Like | NotLike | ILike | NotILike
        | SimilarTo | NotSimilarTo | RegMatch | NotRegMatch
        | RegIMatch | NotRegIMatch | Contains | ContainedBy
        | Overlaps | JsonExists | JsonSomeExists | JsonAllExists
    $left:(Null)
    *
)
=>
(Null (BoolType))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (`Filter(S, NULL = x)`) and after() (`Filter(S, NULL::bool)`) are structurally different over an arbitrary non-unique, nullable source, so the proof is not vacuous — it verifies the actual content of the rule's Eq branch, namely that a comparison with a null left operand never evaluates true, so both filters admit no rows (had `NULL = x` been able to be true, null-`x` rows would survive on the left side and the proof would fail). Baking in the concrete `SqlStdOperatorTable.EQUALS` is the right call rather than an under-generalization: an uninterpreted predicate symbol carries no null-propagation semantics for QED to reason about, so each of the source rule's 22 operator branches (the SCOPE comment's "18/17" is a miscount, but the stated Eq-only restriction is accurate and specific) requires its own instance with a concrete operator; symbol sharing is correct (same source, same right input `x`, left operand a null literal rather than a shared symbol, matching `$left:(Null) *`), no spurious preconditions are introduced, and the result is a genuine, non-degenerate, honestly-flagged PARTIAL special case in the rule's canonical boolean (filter) position.

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
    "nanos": 380417
  }
}
```
