# SimplifyExplainOrdering

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 27  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyExplainOrdering removes redundant columns from the Explain operator's
input ordering.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyExplainOrdering`, not the other rules in that file):

```
# SimplifyExplainOrdering removes redundant columns from the Explain operator's
# input ordering.
[SimplifyExplainOrdering, Normalize]
(Explain
    $input:*
    $explainPrivate:* &
        (CanSimplifyExplainOrdering $input $explainPrivate)
)
=>
(Explain $input (SimplifyExplainOrdering $input $explainPrivate))
```
```

## Independent verifier review

**Verdict:** AGREE

SimplifyExplainOrdering leaves the input relation and the Explain output data completely untouched — its only effect is replacing the required input ordering stored in ExplainPrivate.Props with a relaxed version, and the rewrite's validity rests entirely on an ordering/functional-dependency argument (a key column making a shorter ordering requirement sufficient for a longer one). QED models only bag semantics and has no notion of row order or of required orderings as plan properties (ordering semantics of Sort/Limit/Offset/Order By are explicitly unsupported, and even a DSL-extended Sort operator would be bag-erased by the prover, making any such encoding vacuously equivalent rather than a proof of the FD-based relaxation). Consequently, any RuleScript encoding yields bag-identical before/after patterns and a trivial, content-free proof — this is a fundamental limitation of QED's semantics, not a missing DSL builder. ```
