# InlineAnyValuesSingleCol

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

InlineAnyValuesSingleCol converts Any with Values input to AnyScalar.
This version handles the case where there is a single column.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `InlineAnyValuesSingleCol`, not the other rules in that file):

```
# InlineAnyValuesSingleCol converts Any with Values input to AnyScalar.
# This version handles the case where there is a single column.
[InlineAnyValuesSingleCol, Normalize]
(Any $values:(Values) $scalar:* $private:*)
=>
(AnyScalar $scalar (InlineValues $values) (SubqueryCmp $private))
```
```

## Independent verifier review

**Verdict:** AGREE

The rule rewrites a subquery-based `x op ANY (VALUES (c1),(c2),…)` into a scalar `x op ANY (c1,c2,…)` — an identity that rests entirely on (1) the specific constant tuples inside the `Values` and (2) the backend-defined scalar semantics of `Any`/`AnyScalar`. RuleScript has no way to express constant relations or numeric literals (scans are uninterpreted; only boolean literals exist), and QED's bag-semantics SMT core cannot see into or relate backend-specific scalar operators, so no non-vacuous encoding is possible. ```
