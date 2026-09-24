# ExpandDisjunctionForJoinInputs

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 35  **Verification rounds used:** 2
**Scope detail:** INNER join whose condition is a two-branch disjunction of conjunctions, each branch pairing a left-only with a right-only predicate, expanded by AND-ing in the per-side disjunctions.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ExpandDisjunctionForJoinInputsRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

before() and after() are structurally different — after() ANDs into the join condition the two per-side disjunctions (pl∨rl) and (pr∨sr) — and the equivalence is a genuine, non-vacuous theorem: each added disjunction is logically implied by the two-branch DNF condition (pl∧pr)∨(rl∧sr), but the implication is not syntactic, so the prover had to verify it for arbitrary instantiations. The encoding is faithful in its details: pl/rl reference only L's columns and pr/sr only R's (joinField ordinals 0–3 with a 2+2 split, verified against JoinField's left-column arithmetic), the four predicate symbols are independent as the rule needs, and INNER is inside the source rule's applicability (both canPush flags true for INNER, so both extras are legitimately added — no dropped precondition, and the bloat limit and no-op equality guard are heuristics, not semantic preconditions). The remaining narrowing — INNER only (LEFT/RIGHT variants add fewer extras), the join-condition matcher only rather than the filter-on-join matcher, and a fixed two-branch one-conjunct-per-side DNF rather than arbitrary DNF — is exactly what the single PARTIAL scope line claims, so this is an honestly labeled, non-degenerate special case of the real rule. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7555290
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36188166
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 881417
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 470250
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19710292
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36303042
  },
  "total_duration": {
    "secs": 0,
    "nanos": 71751833
  }
}
```
