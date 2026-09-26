# FoldNeFalse

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** public record FoldNeFalse() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

FoldNeFalse replaces x != False with x.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNeFalse`, not the other rules in that file):

```
# FoldNeFalse replaces x != False with x.
[FoldNeFalse, Normalize]
(Ne $left:* (False))
=>
$left
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful: `before()` filters on the concrete `NOT_EQUALS(left, false)` while `after()` filters on `left` alone, so the structures genuinely differ and QED's proof is of the real boolean identity `x <> false ≡ x` (valid under both two-valued and SQL three-valued null logic, matching the Optgen rule which has no side conditions). The single uninterpreted predicate `left` is the correct stand-in for Optgen's `$left:*` (any boolean expression over the row, reused on both sides exactly as the rule requires), and the right operand is the concrete `False` literal, matching the rule's exact match on `(False)` rather than over-generalizing to an arbitrary boolean. The only encoding choices are the relational anchor (a `Filter`) and a single-column carrier scan, neither of which restricts the universally quantified identity, so `// SCOPE: FULL` is honest. ```

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
    "nanos": 414500
  }
}
```
