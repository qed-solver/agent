# InlineAnyProjectSet

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule's core equivalence — `x OP ANY (unnest($arg))` ⟺ `AnyScalar(x, $arg) AND $arg IS NOT NULL` — depends on a list-valued scalar expanding into row values, i.e. the subquery's relation being exactly the elements of a list cell; QED's bag-of-tuples semantics over uninterpreted sorts has no list type or set-returning operator, and no Java-side DSL extension can supply that meaning since it would have to live in the fixed Rust prover. The NULL fixup (`AND $arg IS NOT NULL`) is itself 3-valued logic — NULL arg gives empty subquery → FALSE on the LHS vs AnyScalar → NULL on the RHS — while QED models only total, two-valued uninterpreted predicates with no NULL literal or IS/IS NOT primitive in the DSL. Moreover the LHS isn't even expressible: the DSL has no scalar-subquery builder (only relation-level Correlate), so Any/AnyScalar would have to be independent uninterpreted symbols that QED can never relate, making the equivalence undecidable rather than merely unproven. ```
