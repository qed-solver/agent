# SimplifySameVarInequalities

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** only the `Ne` alternative of the source rule is encoded (a single RRule instance has one before/after pair), so the `Lt`/`Gt` alternatives are not covered


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifySameVarInequalities converts `x != x` and other inequality
comparisons into `x IS NULL AND NULL`. The `AND NULL` is necessary
when x is NULL.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifySameVarInequalities`, not the other rules in that file):

```
# SimplifySameVarInequalities converts `x != x` and other inequality
# comparisons into `x IS NULL AND NULL`. The `AND NULL` is necessary
# when x is NULL.
[SimplifySameVarInequalities, Normalize]
(Ne | Lt | Gt
    $left:(Variable)
    $right:(Variable) & (VarsAreSame $left $right)
)
=>
(And (Is $left (Null (TypeOf $left))) (Null (BoolType)))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the source rule's `Ne` branch: `before` is the filter `x != x` (both operands the same field `x`, correctly encoding `VarsAreSame`), and `after` is `filter((x IS NULL) AND NULL)` — two structurally distinct expressions that are genuinely 3VL-equivalent (per row: non-null ⇒ FALSE/FALSE, null ⇒ NULL/NULL), so both filters yield the empty bag and the proof is non-vacuous, with no symbol-sharing or missing-precondition errors. The `Lt`/`Gt` alternatives are not encoded (a single RRule is one before/after pair), but this restriction is specific and honestly declared in the SCOPE line, and all three self-comparison inequality alternatives have identical 3VL behavior rewriting to the same `x IS NULL AND NULL`, so the Ne instance is a valid, non-degenerate special case.

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
    "nanos": 286083
  }
}
```
