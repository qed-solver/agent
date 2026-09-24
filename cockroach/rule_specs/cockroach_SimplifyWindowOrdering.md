# Name: SimplifyWindowOrdering
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/window.opt

SimplifyWindowOrdering reduces an ordering to a simpler form using FDs.

This rules does not match when window functions have a RANGE frame with an
offset, like max(a) OVER (PARTITION BY a ORDER BY a RANGE 1 PRECEDING). The
ordering column cannot be pruned because the execution engine requires an
ordering column in this case, even if the ordering is constant.

Extracted from `window.opt` (which defines multiple rules — implement specifically `SimplifyWindowOrdering`, not the other rules in that file):

```
# SimplifyWindowOrdering reduces an ordering to a simpler form using FDs.
#
# This rules does not match when window functions have a RANGE frame with an
# offset, like max(a) OVER (PARTITION BY a ORDER BY a RANGE 1 PRECEDING). The
# ordering column cannot be pruned because the execution engine requires an
# ordering column in this case, even if the ordering is constant.
[SimplifyWindowOrdering, Normalize]
(Window
    $input:*
    $fn:*
    $private:* &
        (CanSimplifyWindowOrdering $input $private) &
        ^(HasRangeFrameWithOffset $fn)
)
=>
(Window $input $fn (SimplifyWindowOrdering $input $private))
```
