# Name: SimplifyWithBindingOrdering
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/ordering.opt

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
