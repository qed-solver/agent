# FoldComparisonWithAny

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3
**Scope detail:** encodes only the constant instance TRUE = ANY (FALSE, TRUE) ⟹ TRUE, as OR(EQ(TRUE,FALSE), EQ(TRUE,TRUE)) ⟹ TRUE, since the DSL has no tuple/ANY node and no NULL literal.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldComparisonWithAny evaluates a comparison operation over a constant
and an ANY/SOME clause, replacing the entire expression with a constant.
It iterates over elements in the clause and tries to find constants that
make the comparison a definite value.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldComparisonWithAny`, not the other rules in that file):

```
# FoldComparisonWithAny evaluates a comparison operation over a constant
# and an ANY/SOME clause, replacing the entire expression with a constant.
# It iterates over elements in the clause and tries to find constants that
# make the comparison a definite value.
[FoldComparisonWithAny, Normalize]
(AnyScalar
    $left:* & (IsConstValueOrGroupOfConstValues $left)
    $right:* & (IsTuple $right)
    $cmp:* &
        (Let
            ($result $ok):(FoldComparisonWithAny
                $cmp
                $left
                $right
            )
            $ok
        )
)
=>
$result
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is not vacuous: before() filters by OR(EQ(TRUE,FALSE), EQ(TRUE,TRUE)) while after() filters by the TRUE literal, so QED genuinely had to validate the constant-folding identity (the tautology supplied by TRUE = TRUE) over the shared scan — a correct relational embedding of a scalar rewrite. The encoding is an extreme special case (one concrete boolean left, one concrete two-element tuple, EQUALS only), but that narrowing is forced by the DSL/QED having no tuple/ANY/list construct and no non-boolean or NULL literals, and the SCOPE line states this restriction specifically and honestly. The ANY-as-OR expansion is exact for this non-empty, null-free tuple, so the declared partial instance is faithful and non-degenerate, not a structurally identical before/after pair. ```

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
    "nanos": 281333
  }
}
```
