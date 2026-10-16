# SimplifyOrdinalityOrdering

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2

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

The rewrite's entire effect is changing the physical ordering requirement attached to the Ordinality operator — the input relation and its bag are identical on both sides — so in any RuleScript encoding, before() and after() are literally the same relational expression, and the only proof QED could return would be a vacuous identity that says nothing about the actual simplification. The rule's real soundness rests on row-order semantics (a key column makes subsequent sort columns redundant in a total row order) and functional-dependence entailment, both of which QED fundamentally cannot model (Sort/Limit/Offset/Window/Sample have no bag-semantic meaning, and it cannot reason about FD/entailment between uninterpreted symbols); RelRN also exposes no Ordinality or ordering-requirement construct, and an extend_dsl_file cannot close the gap because the prover — the unchanging arbiter — erases ordering rather than the DSL missing a builder. ```
