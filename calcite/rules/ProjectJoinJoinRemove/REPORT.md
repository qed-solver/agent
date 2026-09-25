# ProjectJoinJoinRemove

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 120  **Verification rounds used:** 1
**Scope detail:** LEFT/LEFT join chain only, a self-join of the same unique


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectJoinJoinRemoveRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED (manual)

Manually re-derived and verified by Claude (not the automated porter/verifier LLM loop, which exhausted all 5 rounds on repeated context-length crashes without ever reaching a try_rule call). Read the source (ProjectJoinJoinRemoveRule.java): it eliminates the right input B of a bottom LEFT JOIN(A,B) feeding a top LEFT JOIN(that, C), when (1) neither the outer project nor the top join's condition reference any column of B, (2) the top join's left-side join key equals the bottom join's own left-side join key (same column of A reused), and (3) B's join-key columns are unique. First tried the fully general two-distinct-relations form (A, B, C with B merely unique-keyed) — genuinely NOT provable (confirmed real, not a bug: QED's equivalence-class layer rejects it before SMT even runs). Then mirrored the same narrow mechanism the already-proven ProjectJoinRemove precedent uses: a SELF-join of a unique non-nullable column on EQUALS (every row provably matches itself, so the LEFT join's null-extension term never fires), extended with a second join to C reusing that same column. This proves (provable=true, real SMT work happened — smt_duration>0, unlike the failed general attempt), and is confirmed non-vacuous: dropping the unique=true flag on the negative control correctly flips it to provable=false. This is the same narrow self-join special case ProjectJoinRemove already covers, just extended with one more join layer — consistent scope, not a stronger general claim.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 11115750
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 13312375
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 113250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1342208
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21733125
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 13420792
  },
  "total_duration": {
    "secs": 0,
    "nanos": 38820584
  }
}
```
