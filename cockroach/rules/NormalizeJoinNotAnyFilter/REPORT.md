# NormalizeJoinNotAnyFilter

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 101  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

NormalizeJoinNotAnyFilter is similar to NormalizeSelectNotAnyFilter, except
that it operates on Not Any expressions within Join filters rather than Select
filters.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `NormalizeJoinNotAnyFilter`, not the other rules in that file):

```
# NormalizeJoinNotAnyFilter is similar to NormalizeSelectNotAnyFilter, except
# that it operates on Not Any expressions within Join filters rather than Select
# filters.
[NormalizeJoinNotAnyFilter, Normalize]
(Join
    $left:*
    $right:*
    $on:[
        ...
        $item:(FiltersItem
            (Not (Any $anyInput:* $scalar:* $anyPrivate:*))
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
        (Not
            (Exists
                (Select
                    $anyInput
                    [
                        (FiltersItem
                            (IsNot
                                (ConstructAnyCondition
                                    $anyInput
                                    $scalar
                                    $anyPrivate
                                )
                                (False)
                            )
                        )
                    ]
                )
                (ConvertSubToExistsPrivate $anyPrivate)
            )
        )
    )
    $private
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire content is the semantic identity NOT(ANY(subq)) ≡ NOT(EXISTS(Select(subq, IsNot(ConstructAnyCondition,...)))), which rests on (a) three-valued-logic NULL handling ("a NULL return value is treated as False by the filter") that QED's bag-semantics decision procedure does not model, and (b) subquery operators ANY/EXISTS in the scalar/ON-condition position — neither exists in RuleScript's core language, so both sides would be distinct, unconnected uninterpreted symbols (ConstructAnyCondition being yet another opaque function) that QED has no algebraic definition linking, making equivalence genuinely undecidable rather than a missed encoding. ```
