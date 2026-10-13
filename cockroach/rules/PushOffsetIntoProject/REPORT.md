# PushOffsetIntoProject

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

PushOffsetIntoProject pushes the Offset operator into its Project input. It is
desirable to push the Offset operator as low in the query as possible, in
order to minimize the number of rows that other operators need to process.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushOffsetIntoProject`, not the other rules in that file):

```
# PushOffsetIntoProject pushes the Offset operator into its Project input. It is
# desirable to push the Offset operator as low in the query as possible, in
# order to minimize the number of rows that other operators need to process.
[PushOffsetIntoProject, Normalize]
(Offset
    (Project $input:* $projections:* $passthrough:*)
    $offset:*
    $ordering:* &
        (OrderingCanProjectCols
            $ordering
            $cols:(OutputCols $input)
        )
)
=>
(Project
    (Offset $input $offset (PruneOrdering $ordering $cols))
    $projections
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** AGREE

PushOffsetIntoProject's soundness is entirely an ordering fact: Offset drops its first n rows *in the given ordering*, and the rule's guards (`OrderingCanProjectCols`, `PruneOrdering`) exist precisely to ensure the same rows are skipped before and after the projection — QED, by its own design (qed.pdf; the reference's limitations list), has no list/ordering semantics for Sort/Limit/Offset and models them only as uninterpreted bag operators, so the commutation `Offset(π(R)) = π(Offset(R))` has genuine SMT countermodels under any bag interpretation. I also confirmed the core DSL doesn't even expose Offset/Sort builders, but an `extend_dsl_file` addition couldn't help either, since the unmodifiable Rust prover — not the Java builders — is what lacks positional semantics and cannot be given an axiom that Offset's dropped rows are order-determined. The only encodings QED could certify reduce to the vacuous identity-projection tautology, so no non-trivial partial case exists and the UNSUPPORTED call is correct. ```
