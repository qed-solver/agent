# InlineWith

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/with.opt

InlineWith replaces use of a With which is referenced at most one time with
the contents of the With itself.

Extracted from `with.opt` (which defines multiple rules — implement specifically `InlineWith`, not the other rules in that file):

```
# InlineWith replaces use of a With which is referenced at most one time with
# the contents of the With itself.
[InlineWith, Normalize]
(With
    $binding:*
    $input:*
    $withPrivate:* & (CanInlineWith $binding $input $withPrivate)
)
=>
(InlineWith $binding $input $withPrivate)
```
```

## Independent verifier review

**Verdict:** AGREE

InlineWith is fundamentally a let-substitution rule (`let W = D in Q` ≡ `Q[W:=D]`), and QED's Q-expression language — fixed on the Rust prover side, whose JSON contract exposes only scan/values/filter/project/join/correlate/union/intersect/except/distinct/group/sort — has no With/let-binding operator and no mechanism to assert definitional equality between two relation symbols, so the CTE reference and its body can only be modeled as independent uninterpreted symbols for which the substitution is not universally valid. No `extend_dsl_file` attempt can close this gap, because a `With` builder would have to emit a JSON operator the unmodifiable prover cannot interpret, and no meaningful special case survives (identifying the two symbols collapses the rule to a tautological identity; the sibling rules in the file additionally require Limit and recursive-CTE semantics that QED does not model).
