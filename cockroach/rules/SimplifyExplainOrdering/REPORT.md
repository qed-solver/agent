# SimplifyExplainOrdering

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2

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

SimplifyExplainOrdering keeps `$input` identical on both sides and only rewrites the ordering directive stored in the Explain operator's private props, but RuleScript has no channel to express ordering directives (no Sort/annotation carrier in the core language) and QED decides equivalence on bag semantics only, so every possible encoding degenerates to the data identity input ≡ input — provable, yet vacuous, since the proof never touches the rule's actual content. That content is a functional-dependency argument about ordered sequences (a key column making later ordering columns redundant), i.e. list/ordering semantics, which is explicitly outside QED's decision procedure; even a hypothetical Sort builder couldn't close the gap, because QED would collapse any two orderings of the same bag to one another and "prove" both correct and incorrect simplifications, so this is a fundamental prover limitation rather than a missing DSL operator.
