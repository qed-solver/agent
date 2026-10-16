# EliminateWindow

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** QED has no Window operator (no frame/ordering semantics), so the zero-function window is modeled, per the established window modeling convention, as an INNER equi-join of a fixed two-column input (non-null partition key k, data column v) with its distinct partition keys.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/window.opt

EliminateWindow removes a Window operator with no window functions (which can
occur via column pruning).

Extracted from `window.opt` (which defines multiple rules — implement specifically `EliminateWindow`, not the other rules in that file):

```
# EliminateWindow removes a Window operator with no window functions (which can
# occur via column pruning).
[EliminateWindow, Normalize]
(Window $input:* [])
=>
$input
```
```

## Independent verifier review

**Verdict:** CONFIRMED

QED fundamentally cannot represent a Window operator (no Window builder, no window case in JSONSerializer, and the unmodifiable prover has no window semantics), so full generality is genuinely out of reach and the porter's modeling of a zero-function partitioned window as an INNER equi-join of the input back to its distinct partition keys (correctly sharing the input symbol, with verified ordinals, condition, and projection, and a declared non-null key) is the only faithful available encoding. The `before()` (Join+Aggregate+Project) is structurally different from `after()` (plain scan), so the proof is non-vacuous, and the fixed two-column/non-null-key restrictions are a direct consequence of that fundamental limitation and are honestly tagged `SCOPE: PARTIAL` with the specific condition stated.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7521791
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35404875
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 906917
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 651042
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18890084
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35497042
  },
  "total_duration": {
    "secs": 0,
    "nanos": 69995375
  }
}
```
