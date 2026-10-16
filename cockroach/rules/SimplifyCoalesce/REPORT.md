# SimplifyCoalesce

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3

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

QED encodes COALESCE as an uninterpreted scalar function with no first-non-null selection axiom, so the rule's essential step COALESCE(NULL, x) → x is a bespoke operator-semantic identity that the SMT layer cannot justify for any instantiation of the symbols. The DSL also has no NULL literal, so the "leading null operands" premise cannot even be expressed, and closing that with `extend_dsl_file` would not help because the missing axiom lives in the fixed prover (which cannot be modified), not in the Java API — and any relational re-encoding of "first non-null" would require interpreting the very uninterpreted symbols QED abstracts away. This falls squarely under the limitation of operators whose specific internal semantics QED cannot see through, so the porter's UNSUPPORTED conclusion is correct.
