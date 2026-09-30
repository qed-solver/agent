# PushSelectIntoInlinableProject

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** two synthesized (inlinable) columns and two uninterpreted passthrough columns, with every Select filter inlined below the Project (an uninterpreted predicate over each column's defining expression) and no filter left above the Project; the CanInlineProjections/FilterHasCorrelatedSubquery matching guards are not bag-semantic preconditions.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/inline.opt

PushSelectIntoInlinableProject pushes the Select operator into a Project, even
though the filter references it. This is made possible by inlining the
references to projected columns so that the Select becomes independent of the
Project, and therefore can be reordered. This normalization is important for
enabling Any filter conditions to be pushed down into scans.

This rule is low priority so that it runs after the PushSelectIntoProject
and MergeProjectProject rules, since those rules are cheaper to match and
replace.

Example:
SELECT * FROM (SELECT x+1 AS x2 FROM xy) WHERE x2=10
=>
SELECT x+1 AS x2 FROM (SELECT * FROM xy WHERE (x+1)=10)

Extracted from `inline.opt` (which defines multiple rules — implement specifically `PushSelectIntoInlinableProject`, not the other rules in that file):

```
# PushSelectIntoInlinableProject pushes the Select operator into a Project, even
# though the filter references it. This is made possible by inlining the
# references to projected columns so that the Select becomes independent of the
# Project, and therefore can be reordered. This normalization is important for
# enabling Any filter conditions to be pushed down into scans.
#
# This rule is low priority so that it runs after the PushSelectIntoProject
# and MergeProjectProject rules, since those rules are cheaper to match and
# replace.
#
# Example:
#   SELECT * FROM (SELECT x+1 AS x2 FROM xy) WHERE x2=10
#   =>
#   SELECT x+1 AS x2 FROM (SELECT * FROM xy WHERE (x+1)=10)
#
[PushSelectIntoInlinableProject, Normalize, LowPriority]
(Select
    (Project
        $input:*
        $projections:* & (CanInlineProjections $projections)
        $passthrough:*
    )
    $filters:* & ^(FilterHasCorrelatedSubquery $filters)
)
=>
(Project
    (Select $input (InlineSelectProject $filters $projections))
    $projections
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and structurally faithful: `before()` is Filter(P∘col0 ∧ Q∘col1) over Project(F(a,b), G(a,b), a, b) with P/Q genuinely applied to the Project's *output* columns, while `after()` re-applies the same shared symbols P/Q to the defining expressions F(a,b)/G(a,b) over the scan and lifts the identical projection list back on top — so the proof is exactly the inlining pushdown property, not an identity. The fixed 2+2 column arity and two unary filters are a specific, declared PARTIAL restriction (schema-arity parameters that don't dilute the semantic content, which is universally quantified over F, G, P, Q and the table), and the determinism of F/G that backs CanInlineProjections is inherent to RuleScript's uninterpreted-function model, so the proved claim subsumes rather than evades the original guard's domain. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5782958
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34705208
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 869792
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 447042
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16909958
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34804542
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67495959
  }
}
```
