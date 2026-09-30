# UnifyComparisonTypes

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 46  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

UnifyComparisonTypes takes a mixed-type comparison between a non-constant and
a constant and, if appropriate, converts the constant to the type of the
non-constant to allow constraints to be generated.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `UnifyComparisonTypes`, not the other rules in that file):

```
# UnifyComparisonTypes takes a mixed-type comparison between a non-constant and
# a constant and, if appropriate, converts the constant to the type of the
# non-constant to allow constraints to be generated.
[UnifyComparisonTypes, Normalize]
(Comparison
    $left:(Variable)
    $right:(Const) &
        (Let ($result $ok):(UnifyComparison $left $right) $ok)
)
=>
((OpName) $left $result)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests on a type-system invariant — that a specific cast of the constant to the variable's type preserves the comparison's truth — but in any faithful RuleScript encoding the cast and the comparison predicate are both uninterpreted symbols with no axiom relating them (the prover maps all virtual types to a single integer domain and treats every operator uninterpreted, and the prover itself cannot be modified to add cast axioms), so `cmp(x, c) ↔ cmp(x, f(c))` has an SMT countermodel under every instantiation and cannot be proven. The only "provable" encoding would be the vacuous identity case where the constant is left untouched (reusing the same field, so no conversion is actually expressed), which does not encode this rule at all — this is exactly the documented limitation that QED cannot reason about type casts or a backend operator's bespoke internal semantics, not a missing DSL operator that `extend_dsl_file` could fix. ```
