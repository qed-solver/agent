# SimplifyCoalesce

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 61  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifyCoalesce discards any leading null operands, and then if the next
operand is a constant, replaces with that constant. Note that ConstValue
matches nulls as well as other constants.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyCoalesce`, not the other rules in that file):

```
# SimplifyCoalesce discards any leading null operands, and then if the next
# operand is a constant, replaces with that constant. Note that ConstValue
# matches nulls as well as other constants.
[SimplifyCoalesce, Normalize]
(Coalesce
    $args:[
        $arg:* & (IsConstValueOrGroupOfConstValues $arg)
        ...
    ]
)
=>
(SimplifyCoalesce $args)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests entirely on COALESCE's first-non-null semantics — the identity "leading-NULL args drop, so a null-constant prefix plus a non-null constant c collapses to c" — but QED models the coalesce call only as an uninterpreted function symbol, so that equality is not valid for every instantiation of the symbol and is unprovable. No non-trivial special case is expressible either: the DSL has no NULL literal, no ite/CASE, and no IS-NULL predicate to reconstruct coalesce's semantics from primitives, and there is no relational workaround because "first non-null argument" needs row ordering (Limit/Sort), which QED has no bag-semantic model for — so encoding it any other way would just bake the simplification into before() instead of deriving it. ```
