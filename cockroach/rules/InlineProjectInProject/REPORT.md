# InlineProjectInProject

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** one inner synthesized column and one outer computed expression, with a single outer passthrough column equal to that inner synthesized column (no additional inner passthrough or outer columns; the HasDuplicateRefs guard is not bag-semantic).


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/inline.opt

InlineProjectInProject folds an inner Project operator into an outer Project
that references each inner synthesized column no more than one time. If there
are no duplicate references, then there's no benefit to keeping the multiple
nested projections. This rule simplifies the relational expression tree and
makes it more likely that other normalization rules will match.

This rule is low priority so that it runs after the MergeProjects rule, since
that rule is cheaper to match and replace.

Example:
SELECT x2*2 FROM (SELECT x+1 AS x2 FROM xy)
=>
SELECT (x+1)*2 FROM xy

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineProjectInProject`, not the other rules in that file):

```
# InlineProjectInProject folds an inner Project operator into an outer Project
# that references each inner synthesized column no more than one time. If there
# are no duplicate references, then there's no benefit to keeping the multiple
# nested projections. This rule simplifies the relational expression tree and
# makes it more likely that other normalization rules will match.
#
# This rule is low priority so that it runs after the MergeProjects rule, since
# that rule is cheaper to match and replace.
#
# Example:
#   SELECT x2*2 FROM (SELECT x+1 AS x2 FROM xy)
#   =>
#   SELECT (x+1)*2 FROM xy
#
[InlineProjectInProject, Normalize, LowPriority]
(Project
    $input:(Project * $innerProjections:*)
    $projections:*
    $passthrough:* &
        ^(HasDuplicateRefs
            $projections
            $passthrough
            (ProjectionCols $innerProjections)
        )
)
=>
(InlineProjectProject $input $projections $passthrough)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's semantic core — flattening `Project(Project(Source, F(x)), [T(F(x)), F(x)])` into `Project(Source, [T(F(x)), F(x)])` — with `F` and `T` fully uninterpreted and correctly shared between both sides (the equivalence only holds because the same operator symbols are reused, so the proof is non-vacuous and not a structural-identity artifact), and it exercises both substitution paths the real rule performs (a computed outer expression and a passthrough reference to the inner synthesized column). The source rule's `HasDuplicateRefs` guard is a cost-based matching criterion (avoid computing an expression twice), not a bag-semantic precondition, so its absence from the encoding — the encoded instance even references the inner column twice — does not change the validity claim; if anything the proved instance is a semantically stronger claim than the guarded one, and the `SCOPE: PARTIAL` line honestly and specifically discloses the fixed 1-inner-column / 1-computed / 1-passthrough arity rather than claiming full generality.

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
    "nanos": 103167
  }
}
```
