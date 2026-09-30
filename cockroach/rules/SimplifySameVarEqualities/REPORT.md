# SimplifySameVarEqualities

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** only the `Eq` alternative of the source rule is encoded (a single RRule instance has one before/after pair), so the `Le`/`Ge` alternatives are not covered


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifySameVarEqualities converts `x = x` and other equality
comparisons into `x IS NOT NULL OR NULL`. The `OR NULL` is necessary
when x is NULL.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifySameVarEqualities`, not the other rules in that file):

```
# SimplifySameVarEqualities converts `x = x` and other equality
# comparisons into `x IS NOT NULL OR NULL`. The `OR NULL` is necessary
# when x is NULL.
[SimplifySameVarEqualities, Normalize]
(Eq | Le | Ge
    $left:(Variable)
    $right:(Variable) & (VarsAreSame $left $right)
)
=>
(Or (IsNot $left (Null (TypeOf $left))) (Null (BoolType)))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous capture of the rule's Eq branch: reusing the single `x` for both operands correctly enforces `VarsAreSame`, the nullable source column puts the critical null case in the proof domain, and the equivalence being proved — `x = x` ⟺ `x IS NOT NULL OR NULL`, where each side is TRUE on non-null rows and NULL on null rows — is precisely the null-sensitive 3VL claim at the heart of the source rule, one that could not be accidentally provable under a null-ignoring model (where `x = x` would collapse to TRUE while the RHS would not), so QED's "provable" result reflects genuine three-valued-logic reasoning rather than a trivial or coincidental proof. `before()` and `after()` are structurally different filters whose equivalence is a real semantic claim, and the type-uninterpreted, nullable source keeps the quantification fully general. The only gap versus the source rule is that the `Le`/`Ge` alternatives are not covered (a single RRule instance carries one before/after pair, and the DSL's family mechanism only substitutes join types, not operators), which the PARTIAL scope line states accurately and specifically, leaving a useful, non-degenerate result. ```

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
    "nanos": 350834
  }
}
```
