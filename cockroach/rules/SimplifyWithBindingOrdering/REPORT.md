# SimplifyWithBindingOrdering

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 10  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyWithBindingOrdering removes redundant columns from the With operator's
binding ordering. Note that this ordering is only used with the special
propagate_input_ordering flag.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyWithBindingOrdering`, not the other rules in that file):

```
# SimplifyWithBindingOrdering removes redundant columns from the With operator's
# binding ordering. Note that this ordering is only used with the special
# propagate_input_ordering flag.
[SimplifyWithBindingOrdering, Normalize]
(With
    $binding:*
    $main:*
    $withPrivate:* &
        (CanSimplifyWithBindingOrdering $binding $withPrivate)
)
=>
(With
    $binding
    $main
    (SimplifyWithBindingOrdering $binding $withPrivate)
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's only effect is on `WithPrivate.BindingOrdering` — an ordering *requirement* (a physical property) on the CTE binding input — while the emitted row bag is the main query on both sides, so its real content (the functional-dependency claim that trailing sort columns are redundant once a leading column is unique) is a list/ordering-level property that QED's bag-semantics theory has no notion of (Sort/Limit/Offset carry no bag meaning, and no physical-property field exists in the QED JSON format). Any RuleScript encoding therefore degenerates to the vacuous identity With ≡ main, proving nothing about the rule; this is a fundamental prover-theory limitation, not a missing DSL builder, so extending the DSL cannot close it. ```
