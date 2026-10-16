# FoldAssignmentCast

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 5  **Verification rounds used:** 1

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

FoldAssignmentCast rewrites AssignmentCast(const, T) into a concrete folded literal, so its correctness rests on a value equation cast(c) = c′ about the cast operator's evaluation semantics, but QED models casts only as uninterpreted functions with no axioms (qed.pdf's documented Typing limitation — SMT solvers don't reason about type casting, and all virtual types erase to INTEGER), making any faithful before/after encoding unprovable. Independently, the DSL cannot even express two distinct constant values (RexRN.Const only produces literal 0), so "a constant folds to a generally different constant" is inexpressible regardless of cast semantics. Supplying the needed cast-evaluation axioms would require modifying the unmodifiable prover, confirming the rule is genuinely out of RuleScript/QED's reach (same root cause as the previously verified FoldCast skip). ```
