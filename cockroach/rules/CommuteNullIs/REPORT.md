# CommuteNullIs

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 43  **Verification rounds used:** 3

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

The rewrite's validity rests entirely on the null-aware commutativity of CockroachDB's Is/IsNot operators (Is(NULL,x) ≡ Is(x, NULL)), which requires null-aware-equality semantics (NULL IS NULL is TRUE, whereas NULL = NULL is UNKNOWN) that QED's built-in three-valued equality cannot express and that no RexRN/RelRN construct can build. Any Is/NULL symbol introduced via extend_dsl_file would land as an uninterpreted operator in the fixed prover's SMT encoding, where only congruence holds and no axiom relates Is(a,b) to Is(b,a) or to a null-test, so no relational encoding (e.g. Filter(Is(NULL,col),R) ≡ Filter(Is(col,NULL),R)) is provable — this is squarely the "bespoke internal semantics of a backend operator QED cannot see through as an uninterpreted function" limitation, and the prover itself cannot be given the missing axiom. ```
