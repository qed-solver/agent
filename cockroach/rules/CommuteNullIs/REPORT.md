# CommuteNullIs

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 20  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

CommuteNullIs moves a NULL onto the right side of an IS/IS NOT comparison.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `CommuteNullIs`, not the other rules in that file):

```
# CommuteNullIs moves a NULL onto the right side of an IS/IS NOT comparison.
[CommuteNullIs, Normalize]
(Is | IsNot $left:(Null) $right:^(Null))
=>
((OpName) $right $left)
```
```

## Independent verifier review

**Verdict:** AGREE

CommuteNullIs is a scalar rewrite whose correctness depends entirely on the null-aware (NULL-as-a-value) commutativity of CockroachDB's IS / IS NOT comparison — i.e. that Is(NULL, x) ≡ Is(x, NULL) (and the IsNot variant) — and QED has no semantic model for that bespoke operator: it is not a built-in, so in the SMT encoding it is an uninterpreted function for which only congruence holds and no axiom relates Is(a,b) to Is(b,a) or to a null-test. The DSL also exposes no first-class IsNull/Is operator or NULL literal, and even adding them via extend_dsl_file could not make it provable, because QED fundamentally cannot see through this operator's internal null-aware semantics (regular three-valued `=` is not reducible to `IS`, since NULL = NULL is NULL while NULL IS NULL is True). This is a genuine QED limitation (uninterpretable bespoke-operator semantics), not a missing or mismodeled encoding. ```
