# SimplifyRange

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 98  **Verification rounds used:** 5
**Scope detail:** CockroachDB's Range scalar is a transparent wrapper around an And; it is modeled as a no-op conjunct (input AND true), which is equivalent to its input in the boolean/filter context where this rule operates, rather than as a fully transparent scalar operator.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyRange simplifies a Range operator for which the input is no longer an
And expression, likely due to simplification of the And operator itself.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyRange`, not the other rules in that file):

```
# SimplifyRange simplifies a Range operator for which the input is no longer an
# And expression, likely due to simplification of the And operator itself.
[SimplifyRange, Normalize]
(Range $input:^(And))
=>
$input
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The source rule is sound precisely because CockroachDB's Range is a semantically transparent boolean marker, and since QED cannot attach an axiom like range(x)=x to an uninterpreted function symbol, building the transparency in concretely (as the no-op conjunct `input AND true`) is the only faithful provable encoding — the porter proved its removal from a filter condition preserves bag semantics for an arbitrary uninterpreted predicate over an arbitrary table, with before()/after() structurally distinct (the wrapper conjunct appears only on the LHS) and the single shared `input` symbol correctly mirroring the same `$input` on both sides. The original `x:^(And)` guard limits rule firing rather than being a soundness precondition, so omitting it proves a strictly stronger claim, and the `SCOPE: PARTIAL` line honestly and specifically discloses the Range↦(input AND true) modeling and the filter-context embedding, which is the canonical boolean context for the rule and a genuine, non-degenerate special case.

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
    "nanos": 296083
  }
}
```
