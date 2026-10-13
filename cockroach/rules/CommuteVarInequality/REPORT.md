# CommuteVarInequality

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 3
**Scope detail:** only the Le variant is encoded, proving a <= b <==> b >= a for two independent same-type column references (inner cross join of two single-column scans of one shared type) under three-valued semantics; Lt/Ge/Gt would need their own separate rule files.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

CommuteVarInequality is similar to CommuteVar (in scalar.opt), except it
handles inequality comparison operators that need special handling to commute
operands.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `CommuteVarInequality`, not the other rules in that file):

```
# CommuteVarInequality is similar to CommuteVar (in scalar.opt), except it
# handles inequality comparison operators that need special handling to commute
# operands.
[CommuteVarInequality, Normalize]
(Le | Lt | Ge | Gt $left:^(Variable) $right:(Variable))
=>
(CommuteInequality (OpName) $left $right)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully lifts the rule's scalar commutation to the relational level — an inner cross join of two independent same-type nullable scans (so x and y are genuinely independent operands) filtered by x≤y versus y≥x, which is precisely the Le→Ge swap produced by the source rule's `CommuteInequality(Le, left, right)`. The proof is non-vacuous (before≠after structurally), uses the concrete ≤/≥ operators that this commutation genuinely requires (an uninterpreted predicate could not be proved commutable), and holds under three-valued/null semantics since the columns are nullable. The PARTIAL scope (Le only, not Lt/Ge/Gt) is honestly and specifically labeled and is a real consequence of the DSL exposing only a single before/after pair with no meta-operator mechanism, not a hidden over-constraint that would make the result misleading.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8757709
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 39524083
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 801000
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 469208
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 20838667
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 39643584
  },
  "total_duration": {
    "secs": 0,
    "nanos": 76185416
  }
}
```
