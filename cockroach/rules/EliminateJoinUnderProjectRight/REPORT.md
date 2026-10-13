# EliminateJoinUnderProjectRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** the INNER-join variant only, on a self-join of the same unique single non-nullable-column scan on equality of that column, with the project using only the preserved right column


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/project.opt

EliminateJoinUnderProjectRight mirrors EliminateJoinUnderProjectLeft, except
that it only matches InnerJoins.

Extracted from `project.opt` (which defines multiple rules — implement specifically `EliminateJoinUnderProjectRight`, not the other rules in that file):

```
# EliminateJoinUnderProjectRight mirrors EliminateJoinUnderProjectLeft, except
# that it only matches InnerJoins.
[EliminateJoinUnderProjectRight, Normalize]
(Project
    $join:(InnerJoin $left:* $right:*) &
        (JoinDoesNotDuplicateRightRows $join) &
        (JoinPreservesRightRows $join)
    $projections:*
    $passthrough:* &
        (CanRemapCols
            $fromCols:(UnionCols
                $passthrough
                (ProjectionOuterCols $projections)
            )
            $rightCols:(OutputCols $right)
            $fds:(FuncDeps $join)
        ) &
        (CanUseImprovedJoinElimination $fromCols $rightCols)
)
=>
(Project
    $right
    (MergeProjections
        (RemapProjectionCols $projections $rightCols $fds)
        (ProjectRemappedCols $passthrough $rightCols $fds)
        $passthrough
    )
    (DifferenceCols $passthrough (OutputCols $left))
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is non-vacuous — before() genuinely contains an inner self-join that after() removes, and equivalence depends essentially on the declared facts (column 0 is a unique key and non-nullable), which structurally enforce the source rule's JoinDoesNotDuplicateRightRows/JoinPreservesRightRows preconditions rather than leaving them silently absent (key ⇒ at most one matching left row; non-null ⇒ every right row self-matches). INNER is the correct join kind because the Right variant, unlike the Left variant, matches only InnerJoins, and the project reading only the right column is a real instance of the CanRemapCols condition. The encoding is a narrower special case (self-join of a single unique non-nullable-keyed scan on equality, project on the preserved right column only), but that restriction is genuine, specific, and exactly as disclosed in the SCOPE line, so the proved statement is a legitimate, non-degenerate instance of the rule rather than a coincidental or over-constrained artifact. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 4699833
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34526291
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 836042
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 300459
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 13947250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34613250
  },
  "total_duration": {
    "secs": 0,
    "nanos": 63861333
  }
}
```
