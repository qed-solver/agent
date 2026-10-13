# FoldNullCast

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 26  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullCast discards the cast operator if it has a null input. The resulting
null value has the same type as the Cast operator would have had.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullCast`, not the other rules in that file):

```
# FoldNullCast discards the cast operator if it has a null input. The resulting
# null value has the same type as the Cast operator would have had.
[FoldNullCast, Normalize]
(Cast $input:(Null) $targetTyp:*)
=>
(Null $targetTyp)
```
```

## Independent verifier review

**Verdict:** AGREE

FoldNullCast's validity rests entirely on the SQL-specific semantic that Cast propagates NULL (with the target type preserved) — a bespoke internal behavior of a specific scalar operator, which is exactly the kind of operator-internal semantics QED's theory of uninterpreted functions cannot model. Cast is expressible in RuleScript only as an uninterpreted projection symbol, and f(NULL) = NULL is not a first-order consequence for an uninterpreted function (nor could it be a sound global axiom, since such symbols must stand in for non-null-propagating functions like COALESCE), and the JSON format offers no mechanism for scalar-function side conditions — only table-level key/guaranteed constraints — so no `extend_dsl_file` extension can supply the missing axiom without altering the trusted prover. The observed refutation (a complete model in which the uninterpreted cast maps the null constant to a non-null value) is the expected outcome, confirming a genuine QED limitation rather than an encoding mistake.
