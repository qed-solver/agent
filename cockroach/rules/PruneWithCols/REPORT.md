# PruneWithCols

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneWithCols pushes a Project operator beneath a With. It's ok to
unconditionally push this Project down, since in the pruning case, we're
getting the project closer to the source of any prune requests, and if we
just end up with a Project incidentally, it's safe to just always push it
down.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneWithCols`, not the other rules in that file):

```
# PruneWithCols pushes a Project operator beneath a With. It's ok to
# unconditionally push this Project down, since in the pruning case, we're
# getting the project closer to the source of any prune requests, and if we
# just end up with a Project incidentally, it's safe to just always push it
# down.
[PruneWithCols, Normalize]
(Project
    (With $binding:* $input:* $private:*)
    $projections:*
    $passthrough:*
)
=>
(With
    $binding
    (Project $input $projections $passthrough)
    $private
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's only semantic content is relocating a Project across a `With` (CTE/let-binding) node, and QED's relational language — the fixed set of node types serialized for the Rust prover (scan, values, filter, project, join, correlate, group, union, intersect, except, sort) — has no With/let-binding operator, which `JSONSerializer.java` confirms; no Java-side `extend_dsl_file` addition can introduce a node the unmodifiable prover would interpret. Modeling With as row-transparent identity collapses before and after to the *same* plan (tautology), and any stand-in wrapper (e.g. a true-join against a one-row relation) would only certify a different rule about a different operator, not PruneWithCols. The gap is a fundamental absence of the operator from the prover's language — not a missing DSL builder — so UNSUPPORTED is correct. ```
