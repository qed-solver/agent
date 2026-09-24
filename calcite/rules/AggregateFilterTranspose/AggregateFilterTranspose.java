package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — Case 1 of the rule (Group.SIMPLE, filter columns all in the group set): the aggregate has exactly one group key which is a plain field of a two-column input, one non-distinct aggregate call over the other field, and the filter predicate depends only on the group key.
public record AggregateFilterTranspose() implements RRule {
    static final RelRN scanA = RelRN.scan("A", "A_Type");
    static final RelRN scanB = RelRN.scan("B", "B_Type");
    // Two-column base: col 0 = a (group key), col 1 = b (aggregate input).
    static final RelRN base = scanA.join(JoinRelType.INNER, RexRN.trueLiteral(), scanB);

    // Group key: plain field a; filter predicate over the group key only; agg call f over b.
    static final RexRN groupKey = base.field(0);
    static final RexRN pred = groupKey.pred("p");
    static final RelRN.AggCall f = base.field(1).aggCall("f");

    // Before: Aggregate(Filter(p(a), R), GROUP BY a, f(b)).
    static final RelRN beforeAgg =
            new RelRN.Aggregate(base.filter(pred), Seq.of(groupKey), Seq.of(f));

    // After: Filter(p(a'), Aggregate(R, GROUP BY a, f(b))) — a' is the
    // aggregate's group-key output column.
    static final RelRN innerAgg =
            new RelRN.Aggregate(base, Seq.of(groupKey), Seq.of(f));
    static final RelRN afterAgg = innerAgg.filter(innerAgg.field(0).pred("p"));

    @Override
    public RelRN before() {
        return beforeAgg;
    }

    @Override
    public RelRN after() {
        return afterAgg;
    }
}
