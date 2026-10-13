# NormalizeNestedAnds

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** assumes $left, $innerLeft, $innerRight are each atomic (non-And) predicates


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

NormalizeNestedAnds ensures that And expressions are normalized into a left-
deep tree. For example, the expression:

A AND (B AND (C AND D))

would be normalized to:

And
/   \
And   D
/   \
And   C
/   \
A     B

This normalization makes conjuncts easier to traverse for other rules, such as
the ExtractRedundantConjunct rule.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `NormalizeNestedAnds`, not the other rules in that file):

```
# NormalizeNestedAnds ensures that And expressions are normalized into a left-
# deep tree. For example, the expression:
#
#   A AND (B AND (C AND D))
#
# would be normalized to:
#
#         And
#        /   \
#       And   D
#      /   \
#     And   C
#    /   \
#   A     B
#
# This normalization makes conjuncts easier to traverse for other rules, such as
# the ExtractRedundantConjunct rule.
[NormalizeNestedAnds, Normalize]
(And $left:* (And $innerLeft:* $innerRight:*))
=>
(And (ConcatLeftDeepAnds $left $innerLeft) $innerRight)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous base case of the rule — `before()` = `p ∧ (q ∧ r)` and `after()` = `(p ∧ q) ∧ r` are structurally distinct trees (so the proof isn't trivial), the three slots use three independent uninterpreted predicates exactly as the source pattern's three `$:*` slots require, and the source rule carries no side conditions that could be silently dropped. It is a special case rather than the full rule: the Optgen rule is a recursive left-deep flattening where any slot may itself be an `And`, which no single finite RuleScript pattern can express (the DSL has no expression-level metavariables, and the after-side shape recursively depends on the before-side's structure via `ConcatLeftDeepAnds` — a genuine DSL/prover limitation, not a missing builder, since an expression "hole" would semantically reduce to what `pred` already provides), so the honest `// SCOPE: PARTIAL` tag with a specific condition (atomic conjuncts, fixed depth-2 shape) is appropriate, and the proved claim — that the left-deep re-association preserves bag semantics for all instantiations of uninterpreted (i.e., maximally general boolean) conjuncts — is exactly the semantic content the optimizer must trust.

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
    "nanos": 348625
  }
}
```
