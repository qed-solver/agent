# SimplifyWithBindingOrdering

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 6  **Verification rounds used:** 1

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

`SimplifyWithBindingOrdering` is a properties-only rewrite — it leaves `$binding` and `$main` untouched and only relaxes `WithPrivate.BindingOrdering` (a required row-ordering property derived from the input's functional dependencies), so the emitted bag is identical before and after and its entire correctness content is a statement about physical/required ordering. QED is a bag-semantics decision procedure with no notion of required row order (per the reference, `Sort`/`Limit`/`Offset`/`Window`/`Sample` carry no bag-semantic meaning), and the DSL exposes no `With`/let-binding operator nor any ordering operator, so any RuleScript encoding would collapse `before`/`after` to the same plan and prove only a tautology — the ordering-simplification content is genuinely unmodelable, not a gap a DSL extension could close.
