# NormalizeSelectAnyFilter

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 103  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

NormalizeSelectAnyFilter rewrites an Any expression that is a top-level
conjunct in Select filters, turning it into an Exists expression. Any can be
rewritten as Exists in this context because a NULL return value is treated as
False by the filter.

Exists is more efficient than Any, since its null handling is much simpler. In
addition, the Exists can be transformed into a semi-join.

Citations: [5] (section 3.5)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `NormalizeSelectAnyFilter`, not the other rules in that file):

```
# NormalizeSelectAnyFilter rewrites an Any expression that is a top-level
# conjunct in Select filters, turning it into an Exists expression. Any can be
# rewritten as Exists in this context because a NULL return value is treated as
# False by the filter.
#
# Exists is more efficient than Any, since its null handling is much simpler. In
# addition, the Exists can be transformed into a semi-join.
#
# Citations: [5] (section 3.5)
[NormalizeSelectAnyFilter, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Any $anyInput:* $scalar:* $anyPrivate:*)
        )
        ...
    ]
)
=>
(Select
    $input
    (ReplaceFiltersItem
        $filters
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
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests entirely on SQL three-valued/NULL logic: `x op ANY (…)` can evaluate to NULL (no TRUE, at least one UNKNOWN) while `EXISTS (…)` is never NULL, and it is precisely the Select filter's drop-NULL-as-false behavior (plus both being TRUE iff some `x op v` is TRUE) that equates the two conjuncts. QED models predicates as total, two-valued, NULL-free uninterpreted functions, so in its bag semantics both `Any` and `Exists` lower to the identical semi-join `R ⋉ S ON (x op v)` — there is no NULL to drop and no distinct scalar-subquery-vs-exists semantics to capture, making any RuleScript encoding a vacuous `before() == after()` identity rather than a real proof. This is a fundamental QED modeling limitation (no three-valued logic, no Any/Exists/subquery operator with bespoke NULL handling), not a DSL gap closable via extend_dsl_file. ```
