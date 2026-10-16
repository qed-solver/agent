# ConvertCountToCountRows

**Status:** SKIPPED
**Source backend:** CockroachDB

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt
```

## Reason

The rule is conditional: COUNT(x) becomes COUNT(*) only when ExprIsNeverNull(x) holds. QED's prover does model null-skipping, but only when an aggregate call carries ignoreNulls=true, and the DSL cannot produce that flag: RelRN.AggCall has no ignoreNulls field and Aggregate.semantics() builds calls through the plain RelBuilder.aggregateCall overload, which defaults it to false. Encoding COUNT(x) against a row count on a column typed non-null does prove, but the same encoding also proves when the column is marked nullable, so it establishes the weaker unconditional identity 'tally rows == tally rows' and not the rule's non-null precondition. Publishing it as proved would misstate what was checked, and the porter's own late attempts were heading to the same conclusion: it was drafting an ignoreNulls extension to AggCall and the serializer, which is a DSL change rather than an encoding of this rule.
