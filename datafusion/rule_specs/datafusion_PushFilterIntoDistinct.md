# Name: PushFilterIntoDistinct
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_filter.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownFilter`.

`Filter(predicate, input: Distinct(distinct))` pushes the Filter below the Distinct (for `Distinct::All`, wraps `distinct.input` in the Filter; for `Distinct::On`, sets `distinct.input` to the filtered input directly) -- filtering before or after deduplication produces the same set of distinct rows, so pushing down lets the dedup work over fewer rows. This is the `LogicalPlan::Distinct` arm.
