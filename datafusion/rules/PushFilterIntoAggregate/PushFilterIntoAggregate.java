package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the aggregate has exactly one group key (a plain input column) and one non-distinct aggregate call, and the filter predicate depends only on the group key.
public record PushFilterIntoAggregate() implements RRule {
    // Two-column input: col 0 = key (group key), col 1 = value (aggregate operand).
    static final RelRN input = RelRN.scanMany("Source", Seq.of(
            RexRN.varType("Key_Type", true),
            RexRN.varType("Value_Type", true)));

    static final RexRN key = input.field(0);
    // Uninterpreted predicate over the group key only — shared between both sides.
    static final RexRN pred = key.pred("p");
    // One non-distinct aggregate call over the value column.
    static final RelRN.AggCall agg = input.field(1).aggCall("f");
    static final Seq<RexRN> groupSet = Seq.of(key);

    // Before: Filter(p(key'), Aggregate(R, GROUP BY key, f(value))).
    static final RelRN innerAgg =
            new RelRN.Aggregate(input, groupSet, Seq.of(agg));
    static final RelRN before =
            innerAgg.filter(innerAgg.field(0).pred("p"));

    // After: Aggregate(Filter(p(key), R), GROUP BY key, f(value)).
    static final RelRN after =
            new RelRN.Aggregate(input.filter(pred), groupSet, Seq.of(agg));

    @Override
    public RelRN before() {
        return before;
    }

    @Override
    public RelRN after() {
        return after;
    }
}
