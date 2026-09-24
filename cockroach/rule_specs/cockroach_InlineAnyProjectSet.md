# Name: InlineAnyProjectSet
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

InlineAnyProjectSet replaces an "unnest" subquery used for an ANY comparison
with the "unnest" argument. We only match when the ProjectSet has an empty
input and only projects the result of a single unnest function.

There is some subtlety if the unnest argument evaluates to NULL. In that case,
the result of unnest is empty, and the Any filter evaluates to false. However,
AnyScalar with a NULL second argument evaluates to NULL. To handle this, we
AND the result of AnyScalar with an IsNot NULL check on the argument.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `InlineAnyProjectSet`, not the other rules in that file):

```
# InlineAnyProjectSet replaces an "unnest" subquery used for an ANY comparison
# with the "unnest" argument. We only match when the ProjectSet has an empty
# input and only projects the result of a single unnest function.
#
# There is some subtlety if the unnest argument evaluates to NULL. In that case,
# the result of unnest is empty, and the Any filter evaluates to false. However,
# AnyScalar with a NULL second argument evaluates to NULL. To handle this, we
# AND the result of AnyScalar with an IsNot NULL check on the argument.
[InlineAnyProjectSet, Normalize]
(Any
    (ProjectSet
            (Values [ (Tuple []) ])
            [
                (ZipItem
                    (Function
                        [ $arg:* ]
                        $fnPrivate:(FunctionPrivate "unnest")
                    )
                )
            ]
        ) &
        (CanInlineAnyUnnestSubquery)
    $scalar:*
    $private:*
)
=>
(And
    (AnyScalar $scalar $arg (SubqueryCmp $private))
    (IsNot $arg (Null (TypeOf $arg)))
)
```
