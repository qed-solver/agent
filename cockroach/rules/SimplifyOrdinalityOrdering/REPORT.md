# SimplifyOrdinalityOrdering

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyOrdinalityOrdering removes redundant columns from the Ordinality
operator's input ordering.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyOrdinalityOrdering`, not the other rules in that file):

```
# SimplifyOrdinalityOrdering removes redundant columns from the Ordinality
# operator's input ordering.
[SimplifyOrdinalityOrdering, Normalize]
(Ordinality
    $input:*
    $ordinalityPrivate:* &
        (CanSimplifyOrdinalityOrdering $input $ordinalityPrivate)
)
=>
(Ordinality
    $input
    (SimplifyOrdinalityOrdering $input $ordinalityPrivate)
)
```
```

## Independent verifier review

**Verdict:** AGREE

SimplifyOrdinalityOrdering leaves the input — and therefore the produced rows — unchanged; its entire content is that, under a functional-dependency side condition, the required ordering on the Ordinality input may be replaced by a simplified ordering (dropping a functionally determined trailing column), which is a purely list/ordering-semantics equivalence. QED decides bag-equivalence over relations with uninterpreted operators and models no orderings at all (Sort/Limit/Offset/Window/Sample have no bag-semantic meaning), and the DSL exposes no operator that could carry an ordering requirement, so the only possible encodings are either a vacuous before≡after identity (reusing the same predicate symbol, which verifies nothing about the simplification) or two distinct uninterpreted predicates that QED can never relate, since it performs no entailment reasoning between independent symbols. This is a fundamental QED limitation, not a missing DSL builder that `extend_dsl_file` could close (the prover itself is the unchangeable arbiter and has no ordering theory), so UNSUPPORTED is correct.
