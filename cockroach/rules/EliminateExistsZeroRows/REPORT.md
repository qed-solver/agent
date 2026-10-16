# EliminateExistsZeroRows

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 9  **Verification rounds used:** 1
**Scope detail:** uncorrelated EXISTS only (modeled as a left-semi join on a constant-true condition), with the subquery input known to have zero rows


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateExistsZeroRows converts an Exists subquery to False when it's known
that the input produces zero rows.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateExistsZeroRows`, not the other rules in that file):

```
# EliminateExistsZeroRows converts an Exists subquery to False when it's known
# that the input produces zero rows.
[EliminateExistsZeroRows, Normalize]
(Exists $input:* & (HasZeroRows $input))
=>
(False)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully models the uncorrelated case of the rule: before() is the standard relational desugaring of `WHERE EXISTS(empty subquery)` (outer left-semi-join on a constant-true condition against a zero-row relation), and after() is the desugared form of the rewritten query (outer filtered by constant False); the two are structurally different plan shapes whose bag-semantic equality is exactly the rule's content, not a vacuous identity. SEMI join kind, true join condition, `Empty` for HasZeroRows, and `falseLiteral` for the constant False are all the correct operators, the only symbols are the independent Outer and the type-carrying Input (no spurious sharing), and no hidden preconditions are assumed. The restriction to an uncorrelated EXISTS appearing as the whole filter is a genuine consequence of RuleScript lacking a scalar-subquery/Exists symbol (so the rule can only be stated at the relational level), it is explicitly and specifically declared in the SCOPE line, and the result is a real, non-degenerate special case rather than a structural no-op. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 111709
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
    "nanos": 1033125
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 256417
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 9099000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 25094166
  }
}
```
