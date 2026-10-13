# SimplifyAndTrue

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 3  **Verification rounds used:** 1
**Scope detail:** public record SimplifyAndTrue() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyAndTrue simplifies the And operator by discarding a True condition on
the right side.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyAndTrue`, not the other rules in that file):

```
# SimplifyAndTrue simplifies the And operator by discarding a True condition on
# the right side.
[SimplifyAndTrue, Normalize]
(And $left:* (True))
=>
$left
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful relational embedding of CockroachDB's scalar rule (And $left:* (True)) => $left: before() is Filter(And(P, True), R) and after() is Filter(P, R), which are structurally different, so the proof is non-vacuous. `left` is a single uninterpreted predicate shared by both sides (correct symbol sharing, not over-constrained), the base scan carries no uniqueness/key assumption, and since QED's proof is universal over all instantiations of that predicate, every possible boolean expression for $left — including compound ones and three-valued NULL cases, where TRUE is genuinely the identity of AND — is covered; nothing the original rule requires is assumed away, so `// SCOPE: FULL` is honest. ```

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
    "nanos": 357459
  }
}
```
