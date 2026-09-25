# ConvertCountToCountRows

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 130  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

ConvertCountToCountRows replaces a Count operator performed on a non-null
expression with a CountRows operator. CountRows is significantly faster to
execute than Count.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `ConvertCountToCountRows`, not the other rules in that file):

```
# ConvertCountToCountRows replaces a Count operator performed on a non-null
# expression with a CountRows operator. CountRows is significantly faster to
# execute than Count.
[ConvertCountToCountRows, Normalize]
(GroupBy | ScalarGroupBy
    $input:*
    $aggregations:[
        ...
        $item:(AggregationsItem (Count $arg:*)) &
            (ExprIsNeverNull $arg (NotNullCols $input))
        ...
    ]
    $groupingPrivate:*
)
=>
((OpName)
    $input
    (ReplaceAggregationsItem $aggregations $item (CountRows))
    $groupingPrivate
)
```
```

## Independent verifier review

**Verdict:** AGREE (manual — DSL gap identified)

Manually investigated by Claude (not the automated porter/verifier LLM loop, which exhausted all 5 rounds on repeated context-length crashes without ever completing a try_rule call). A prior crashed attempt's cached JSON (.cache/tmp-rules/ConvertCountToCountRows.json) showed it had found QED's real built-in COUNT handling (qed-prover/src/pipeline/relation.rs special-cases the literal string "COUNT") but used the lowercase generic-aggregate operator name "count", which falls through to the generic/uninterpreted-HOp path instead — structurally unrelatable between the two sides, hence not provable. Fixing the operator name to "COUNT" (the RuleScript DSL lets a porter pick any string as an aggregate's name, so this required no DSL change) does make QED report provable=true. However, this is a VACUOUS proof, confirmed via a negative control: the exact same encoding still reports provable=true even when the aggregated column's schema is marked nullable=true (i.e. even when the source rule's actual precondition, ExprIsNeverNull, is violated). The reason: JSONSerializer.java always emits "ignoreNulls": bool(call.ignoreNulls()), and RelRN.Aggregate.semantics() builds the Calcite AggregateCall via the plain RelBuilder.aggregateCall(op, distinct, filter=null, name, operands) overload, which Calcite defaults to ignoreNulls=false with no DSL-exposed way to override it — RelRN.AggCall has no ignoreNulls field at all. QED's own prover core (relation.rs) DOES implement real null-skipping when ignore_nulls=true (it ANDs in a not-null predicate per aggregated row before summing), but the DSL can never actually produce that flag as true, so any COUNT(x)-vs-COUNT() proof built with today's DSL is really proving the strictly weaker, unconditional identity 'COUNT(x) with no null-skipping == COUNT(*)' (true for ANY column, nullable or not, since both sides just tally rows) rather than the source rule's actual conditional identity. Publishing that as PROVED would misrepresent what was verified. This is a genuine, fixable DSL gap (add an ignoreNulls control to RelRN.AggCall, threaded through a RelBuilder call that supports it) rather than a fundamental QED limitation — QED's core already models null-skipping correctly — so it's recorded here as SKIPPED with this specific, actionable cause rather than a generic FAILED.
