# FoldAssignmentCast

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 6  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldAssignmentCast is similar to FoldCast, but it involves an assignment cast
operation. As with FoldCast, FoldAssignmentCast applies as long as the
evaluation would not cause an error.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldAssignmentCast`, not the other rules in that file):

```
# FoldAssignmentCast is similar to FoldCast, but it involves an assignment cast
# operation. As with FoldCast, FoldAssignmentCast applies as long as the
# evaluation would not cause an error.
[FoldAssignmentCast, Normalize]
(AssignmentCast
    $input:*
    $typ:* &
        (IsConstValueOrGroupOfConstValues $input) &
        (Let ($result $ok):(FoldAssignmentCast $input $typ) $ok)
)
=>
$result
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests entirely on the assignment-cast operator's operational evaluation semantics (that casting a constant datum yields a precomputed value without error), which QED cannot model because every scalar operator — including an AssignmentCast added via extend_dsl_file — is lowered to an uninterpreted function, so the LHS term assign_cast(c, T) and the folded constant r are distinct terms no available theory can equate (unlike the proven FoldNotFalse precedent, where ¬false ≡ true is a logical truth the SMT solver knows). The IsConstValueOrGroupOfConstValues guard and the $ok no-error side condition are symbol/type-level properties with no counterpart in the pattern language, so the only encoding QED could ever prove is the vacuous identity before() ≡ after(). ```
