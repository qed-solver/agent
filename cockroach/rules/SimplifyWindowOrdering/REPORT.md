# SimplifyWindowOrdering

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/window.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire content is rewriting the window's ORDER BY (private) clause, and its soundness rests on intra-partition *ordering* semantics — that ordering by (a,b) and by (a) induce the same relative row order once b is FD-determined by (partition, a). RuleScript's core language has no Window/ordering operator, and QED explicitly has no list/ordering semantics (Sort/Window/Sample carry no bag-semantic meaning), so any bag-level encoding of the window makes before() and after() collapse to the identical pattern. The remaining gap is also fundamental: the FD premise itself is an entailment over uninterpreted columns QED cannot derive (its only input-FD mechanism is the single-column `unique` scan flag, which can't express "b is determined by partition + a"), so even a special-case encoding would leave an unprovable, unstateable justification. ```
