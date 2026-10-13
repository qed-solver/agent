# FoldInNull

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 100  **Verification rounds used:** 5

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

FoldInNull replaces the In/Not operator with Null when the tuple only
contains null. The NormalizeInConst pattern will reduce multiple nulls to a
single null when it removes duplicates, so this pattern will match that.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `FoldInNull`, not the other rules in that file):

```
# FoldInNull replaces the In/Not operator with Null when the tuple only
# contains null. The NormalizeInConst pattern will reduce multiple nulls to a
# single null when it removes duplicates, so this pattern will match that.
[FoldInNull, Normalize]
(In | NotIn $left:* (Tuple [ (Null) ]))
=>
(Null (BoolType))
```
```

## Independent verifier review

**Verdict:** AGREE

Manually investigated by Claude (the automated run exhausted all 5 rounds on repeated context-length crashes, never reaching a real try_rule call). Same root cause as FoldEqualsAnyNull: the rule needs (x IN (NULL)) == NULL and (x NOT IN (NULL)) == NULL for ANY x — an uninterpreted operator (IN) forced to NULL based on one specific argument (the singleton-NULL tuple), independent of the other (x). QED's prover core has no general null-propagation rule for uninterpreted function/predicate applications (confirmed via qed-prover/src/pipeline/shared.rs: is_null() is just equality against a fresh uninterpreted NULL sentinel, with no strictness rule tied to any operator's arguments) — only a small hardcoded set of operators (COUNT, EXISTS, IS NULL/IS NOT NULL, boolean AND/OR/NOT) get real interpreted semantics, and IN/NOT IN is not among them. No CASE/conditional construct exists in the DSL to manually encode the null-forcing branch either. Genuinely outside QED's supported fragment for the same reason as FoldEqualsAnyNull.
