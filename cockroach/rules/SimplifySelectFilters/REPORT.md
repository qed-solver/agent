# SimplifySelectFilters

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** only the Or-with-Null branch (filter(Or(p, Null)) => filter(p)) over a plain scan, relying on filters treating a Null conjunct as False; the True-removal, False/contradiction, And-flattening, and Is branches are not captured


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

SimplifySelectFilters simplifies the Filters operator in several possible
ways:
- Removes True operands
- Replaces the Filters operator with False if any operand is False, Null, or
a contradiction
- Flattens nested And operands by merging their conditions into parent
- Simplifies Or operands where one side is a Null to the other side
- Simplifies Is operands where the right side is True or False

Note that the Null handling behavior is different than the SimplifyAnd rules,
because Filters only appears as a Select or Join filter condition, both of
which treat a Null filter conjunct exactly as if it were False.

Extracted from `select.opt` (which defines multiple rules — implement specifically `SimplifySelectFilters`, not the other rules in that file):

```
# SimplifySelectFilters simplifies the Filters operator in several possible
# ways:
#   - Removes True operands
#   - Replaces the Filters operator with False if any operand is False, Null, or
#     a contradiction
#   - Flattens nested And operands by merging their conditions into parent
#   - Simplifies Or operands where one side is a Null to the other side
#   - Simplifies Is operands where the right side is True or False
#
# Note that the Null handling behavior is different than the SimplifyAnd rules,
# because Filters only appears as a Select or Join filter condition, both of
# which treat a Null filter conjunct exactly as if it were False.
[SimplifySelectFilters, Normalize, HighPriority]
(Select
    $input:*
    $filters:[
            ...
            $item:(FiltersItem
                    (And | True | False | Null | Or | Is)
                ) &
                ^(IsUnsimplifiableOr $item) &
                ^(IsUnsimplifiableIs $item)
            ...
        ] &
        ^(IsFilterFalse $filters)
)
=>
(Select $input (SimplifyFilters $filters))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (Filter(scan, Or(p, NULL))) and `after()` (Filter(scan, p)) are structurally different, and the proven identity is a genuine, universally-quantified instance of the source rule's Or-with-Null branch: it holds for every table and every instantiation of the uninterpreted predicate `p` under the exactly-filter semantics (Null is falsy) that the source rule's own comment establishes as the precondition for this branch, so no soundness precondition is missing (the Optgen guards `IsUnsimplifiableOr`/`IsFilterFalse` control rule firing, not correctness of the rewrite). The operators match the source (Filter = Select, `Or`, a real boolean NULL literal serialized as a NULL RexLiteral), the same symbol `p` is correctly shared between both sides, and the `SCOPE: PARTIAL` tag is honest and specific: only the Or-with-Null branch over a single-conjunct filter is proven, the True/False/And/Is branches are explicitly excluded, and the plain-scan base is forced by the DSL (MetaAssignment supports only join-kind holes, not arbitrary input relations), leaving a non-degenerate, faithful special case rather than a vacuous or coincidentally-over-constrained proof.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 0
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 347875
  }
}
```
