# NotInToNotIn

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 6  **Verification rounds used:** 2
**Scope detail:** the IN list is fixed to exactly two elements (B, C) rather than an arbitrary length


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 345-351
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (NOT(OR(a=b, a=c))) and `after()` (AND(¬(a=b), ¬(a=c))) are structurally distinct, so the proof is a genuine De Morgan equivalence — exactly the semantic content of DataFusion's `negated`-flag flip, since an unnegated InList expands to a disjunction of equalities and a negated one to a conjunction of negated equalities, and the proven equivalence is symmetric, covering both directions (`not (A in)` → `A not in` and its mirror) in the source. Modeling A/B/C as three columns of one uninterpreted scan faithfully represents three independent arbitrary values of a shared type, and using concrete `EQUALS` is correct because equality is definitional to InList membership (not an incidental predicate to be uninterpreted); De Morgan also holds under three-valued null logic, so no null/NOT NULL precondition is silently assumed, and no precondition exists in the source rule. The single restriction — a two-element list — is honestly declared in the SCOPE line, is a [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]

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
    "nanos": 337833
  }
}
```
