# CoerceInputs

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 39  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/CoerceInputsRule.java
```

## Independent verifier review

**Verdict:** AGREE

QED serializes every scalar operator — including any proposed "cast" symbol — as an uninterpreted function with no identity/transparency axiom (paper §6.2; RelType.java translates all virtual types to plain integers), and CoerceInputsRule is sound precisely because coercion casts preserve values while the consumer reads the coerced column, so any encoding where the cast output is load-bearing is unprovable, while any provable encoding must leave the cast column unread, at which point the equivalence holds for *any* uninterpreted function and reduces to trivial column-independence rather than coercion. The rule's very definition also depends on backend-specific `getExpectedInputRowType`/`createCastRel` semantics QED cannot see, and since the Rust prover is the fixed arbiter, no DSL extension could supply the missing cast-identity axiom.
