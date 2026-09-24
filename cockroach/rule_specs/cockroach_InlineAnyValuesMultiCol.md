# Name: InlineAnyValuesMultiCol
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

InlineAnyValuesMultiCol converts Any with Values input to AnyScalar.
This version handles the case where there are multiple columns; in this case,
the Values is wrapped into a Project that converts each row to a tuple.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `InlineAnyValuesMultiCol`, not the other rules in that file):

```
# InlineAnyValuesMultiCol converts Any with Values input to AnyScalar.
# This version handles the case where there are multiple columns; in this case,
# the Values is wrapped into a Project that converts each row to a tuple.
[InlineAnyValuesMultiCol, Normalize]
(Any
    (Project
        $values:(Values * $valuesPrivate:*)
        [ (ProjectionsItem $tuple:(Tuple)) ] &
            (IsTupleOfVars $tuple (ValuesCols $valuesPrivate))
        $passthrough:* & (ColsAreEmpty $passthrough)
    )
    $scalar:*
    $private:*
)
=>
(AnyScalar $scalar (InlineValues $values) (SubqueryCmp $private))
```
