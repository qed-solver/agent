# EliminateExistsZeroRows

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 92  **Verification rounds used:** 1
**Scope detail:** the zero-rows precondition is encoded as a literal .empty() relation (a statically known-empty shape), matching the source rule's HasZeroRows guard


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

**Verdict:** CONFIRMED (manual)

Manually investigated by Claude (the automated run exhausted all 5 rounds on repeated context-length crashes, never reaching a real try_rule call). Reused the custom local Exists RexRN (built directly on Calcite's RexSubQuery.exists(RelNode), no shared DSL changes) from EliminateExistsProject. Encoded the zero-rows precondition via RelRN.Empty (a relation of the right shape guaranteed to have zero rows). before() = outer.filter(EXISTS(zeroRowInput)), after() = outer.filter(FALSE). QED proves them equal (the equivalence-class layer resolves it directly via UExpr algebra — squash(sum over an empty relation) reduces to false — without needing SMT). Verified non-vacuous with a negative control: replacing the empty input with an ordinary (non-empty) scan of the same shape correctly breaks the proof (provable=false), confirming the check genuinely depends on the input being empty, not just always-true regardless of row count.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 55208
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
    "nanos": 32916
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 165000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 2282459
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 4527542
  }
}
```
