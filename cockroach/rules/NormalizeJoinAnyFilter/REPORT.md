# NormalizeJoinAnyFilter

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 111  **Verification rounds used:** 6

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

The rule's entire content is a scalar-level predicate swap `x = ANY(subquery)` → `EXISTS(SELECT … WHERE x = scalar)` inside a join's ON condition, but the DSL's scalar language (RexRN: Pred/Proj/And/Or/Not/Field/literals) offers no subquery/Any/Exists/IN predicate node — predicates are only flat uninterpreted symbols over the current row. QED's Q-expression model is flat bag semantics over a fixed schema with no nested/correlated-relation predicate, so it cannot represent either side nor prove the Any↔Exists entailment between independent uninterpreted symbols (a documented limitation). Since the Join wrapper is unchanged on both sides, the subquery predicate swap *is* the whole rule, leaving no relational structure to salvage as a narrower provable special case. ```
