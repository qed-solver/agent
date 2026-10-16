# SimplifyWindowOrdering

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2

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

The rule's sole change is the window's ordering/private data, and its entire justification is that the simplified ordering induces the same intra-partition row order, hence the same frame boundaries and window-function values — a purely ordering-dependent fact, while QED's bag semantics deliberately model no list/order meaning for Sort/Limit/Window (per qed.pdf's own evaluation), and the trusted Rust prover cannot be modified to add such semantics (the DSL has no Window builder and the JSON format has no window form to feed it). Any non-vacuous encoding would have to show W(I,F,p) ≡ W(I,F,p') for two *distinct* orderings p and p': either assume that equivalence (proving only the trivial identity of identical terms), or leave both orderings as distinct uninterpreted values with no ordering/FD axioms QED can use (unprovable), and even expanding a single window function like rank() into self-join/aggregates over an order predicate would still require predicate-entailment reasoning between order relations that QED explicitly cannot perform. So the porter's UNSUPPORTED conclusion is sound — this is a fundamental QED limitation, not a missing builder to close with extend_dsl_file. ```
