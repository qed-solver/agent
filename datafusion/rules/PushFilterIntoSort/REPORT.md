# PushFilterIntoSort

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 10  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 885-889
```

## Independent verifier review

**Verdict:** AGREE

The rule's only correctness content is that pushing a Filter below a Sort leaves the surviving rows in the same relative order — a purely list/ordering property — whereas QED decides equivalence only under bag semantics, where Sort (order-only) has no meaning and both `Filter(P, Sort(R))` and `Sort(Filter(P, R))` collapse to the identical bag {r∈R | P(r)}. Because the prover fundamentally cannot model ordering (explicitly listed in the Limitations as having no bag-semantic meaning), even adding a `sort` builder via extend_dsl_file would yield at best a vacuous tautology, and there is no non-trivial bag-semantic special case to salvage, since filtering is order-independent and the two sides are bag-identical for every instantiation. ```
