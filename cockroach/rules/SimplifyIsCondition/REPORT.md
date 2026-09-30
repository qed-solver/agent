# SimplifyIsCondition

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** encodes the left-operand-never-null branch of EitherExprIsNeverNull: $left is a never-null (NOT NULL) column and $right is an arbitrary nullable column of the same uninterpreted type


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

SimplifyIsCondition replaces x IS NOT DISTINCT FROM y with x = y. This
transformation is only valid if all of the following are true:

1. The expression is in the context of filtering where NULL is falsy.
2. One of x or y is non-nullable. This is required because while the
expression NULL IS NOT DISTINCT FROM NULL is true, NULL=NULL is NULL
(falsy).
3. Neither x nor y is a tuple. Tuples with NULLs have all sorts of
complicated edge cases, so we avoid them entirely. See #48299.

We conservatively also require the types of x and y to be identical. It may be
possible to lift this restriction if we can prove that it is not necessary.

Extracted from `select.opt` (which defines multiple rules — implement specifically `SimplifyIsCondition`, not the other rules in that file):

```
# SimplifyIsCondition replaces x IS NOT DISTINCT FROM y with x = y. This
# transformation is only valid if all of the following are true:
#
#   1. The expression is in the context of filtering where NULL is falsy.
#   2. One of x or y is non-nullable. This is required because while the
#      expression NULL IS NOT DISTINCT FROM NULL is true, NULL=NULL is NULL
#      (falsy).
#   3. Neither x nor y is a tuple. Tuples with NULLs have all sorts of
#      complicated edge cases, so we avoid them entirely. See #48299.
#
# We conservatively also require the types of x and y to be identical. It may be
# possible to lift this restriction if we can prove that it is not necessary.
[SimplifyIsCondition, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Is
                $left:* & ^(IsTuple $left)
                $right:* &
                    ^(IsTuple $right) &
                    (IdenticalTypes
                        (TypeOf $left)
                        (TypeOf $right)
                    ) &
                    (EitherExprIsNeverNull
                        $left
                        $right
                        (NotNullCols $input)
                    )
            )
        )
        ...
    ]
)
=>
(Select
    $input
    (ReplaceFiltersItem $filters $item (Eq $left $right))
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding matches the rule exactly in every load-bearing respect: before() applies `IS_NOT_DISTINCT_FROM` and after() applies `EQUALS` (the rule's actual rewrite, per its "replaces x IS NOT DISTINCT FROM y with x = y" comment), in a Filter (the NULL-is-falsy context the rule requires), with the other filter items correctly abstracted as a single uninterpreted conjunction shared by both sides; all four source preconditions are present — scalar (non-tuple) operands, `IdenticalTypes` via the shared type symbol V, and the never-null guarantee via the non-nullable column type. The only narrowing — taking just the left-operand-never-null branch of `EitherExprIsNeverNull` (right-nullable rather than also the symmetric branch) — is honestly declared in the PARTIAL scope line, is genuinely forced by the DSL (per-column nullability is a static type property, so a single before/after pair cannot express the disjunction), and is the strictly more general of the expressible alternatives (it subsumes the both-non-null case), leaving a non-vacuous proof that exercises the actual NULL-handling distinction between the two operators. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5560291
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36262750
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 836375
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 382708
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16303417
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36348625
  },
  "total_duration": {
    "secs": 0,
    "nanos": 68120000
  }
}
```
