# NormalizeJoinAnyFilter

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 64  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

NormalizeJoinAnyFilter is similar to NormalizeSelectAnyFilter, except that it
operates on Any expressions within Join filters rather than Select filters.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `NormalizeJoinAnyFilter`, not the other rules in that file):

```
# NormalizeJoinAnyFilter is similar to NormalizeSelectAnyFilter, except that it
# operates on Any expressions within Join filters rather than Select filters.
[NormalizeJoinAnyFilter, Normalize]
(Join
    $left:*
    $right:*
    $on:[
        ...
        $item:(FiltersItem
            (Any $anyInput:* $scalar:* $anyPrivate:*)
        )
        ...
    ]
    $private:*
)
=>
((OpName)
    $left
    $right
    (ReplaceFiltersItem
        $on
        $item
        (Exists
            (Select
                $anyInput
                [
                    (FiltersItem
                        (ConstructAnyCondition
                            $anyInput
                            $scalar
                            $anyPrivate
                        )
                    )
                ]
            )
            (ConvertSubToExistsPrivate $anyPrivate)
        )
    )
    $private
)
```
```

## Independent verifier review

**Verdict:** AGREE

Confirmed against the DSL source: RexRN has no scalar-subquery or quantifier node (only Field/JoinField, uninterpreted Pred/Proj, And/Or/Not/True/False/Const), and the subquery's input relation is nested inside a scalar expression, so it is unreachable from any join-condition RexRN — the ANY and EXISTS conjuncts can only be encoded as independent uninterpreted predicate symbols, which QED is designed not to relate. The rule's validity rests entirely on the semantic definitions of the ANY/EXISTS scalar quantifiers over a correlated nested bag (including 3VL null handling in the filter context), which lies outside QED's bag-semantics-with-uninterpreted-functions model, and no DSL extension could close this since the fixed prover has no translation for nested-bag quantification inside filter predicates. Pushing the existential to a relational SEMI-join node changes the plan tree shape and would prove a different join-factoring identity with the ANY conjunct still uninterpreted, so no non-trivial encoding of the rule as stated exists. ```
