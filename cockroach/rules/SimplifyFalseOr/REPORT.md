# SimplifyFalseOr

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 10  **Verification rounds used:** 2
**Scope detail:** assumes Or(False, right) is the entire predicate of a Filter over a single scan, not an arbitrary boolean context


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyFalseOr simplifies the Or operator by discarding a False condition on
the left side.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyFalseOr`, not the other rules in that file):

```
# SimplifyFalseOr simplifies the Or operator by discarding a False condition on
# the left side.
[SimplifyFalseOr, Normalize]
(Or (False) $right:*)
=>
$right
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding captures the rule's only logical content — the boolean identity Or(False, P) ≡ P, with P a fully uninterpreted predicate over an uninterpreted table and Or/False as interpreted connectives — and `before()` and `after()` are structurally distinct, so the proof is a genuine, non-vacuous identity rather than a triviality. The narrowing to "the entire predicate of a Filter over a single scan" is honestly and specifically tagged on the SCOPE line and is a real limitation of the pattern language (RuleScript/QED can only quantify over uninterpreted symbols at fixed positions — there is no boolean-expression or relation "hole" to express an arbitrary context or base relation), not an avoidable under-generalization; since the identity is a valid boolean equivalence in any context (2-valued or three-valued), no logical content is lost by fixing the carrier. No other failure mode applies: the left-False Or position matches the source exactly, the single `right` symbol is correctly shared between both sides, and the source rule is unconditional, so no preconditions (keys, NOT NULL) are silently omitted.

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
    "nanos": 293541
  }
}
```
