# SimplifyDateTimeConversions

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 12  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 1298-1338
```

## Independent verifier review

**Verdict:** AGREE

Both cases of the rule reduce to algebraic identities over the conversion functions — case 1 needs `to_str_p(to_ts_p(x)) = x` and case 2 needs `to_ts_p(to_str_p(to_ts_p(x))) = to_ts_p(x)` — and that round-trip-cancellation property is precisely the bespoke internal semantics of Spark's date_format/to_timestamp operators, which QED cannot see: it models every conversion as an uninterpreted function symbol with only congruence, and neither the DSL nor the JSON the fixed prover consumes has any channel for declaring inverse/composition axioms about function symbols (the schema side only carries table keys and row constraints). The porter's faithful encoding failing with a complete (non-timeout) refutation is consistent with this, and no non-vacuous special case exists: the only way to make an instance provable would be to assume the round-trip identity as a table premise and then prove it, which is tautological and not a faithful narrowing of Spark's unconditional rule — so UNSUPPORTED is the correct conclusion. ```
