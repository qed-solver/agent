package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — only the Aggregate branch of DataFusion's PropagateEmptyRelation (group-by over an empty relation, with group expressions present) is encoded; the Projection, Filter, Window, Sort, and Join branches are not modeled.
public record EmptyAggregateToEmpty() implements RRule {
    static final RelRN source      = RelRN.scan("Source", "Source_Type");
    static final RelRN emptyChild  = source.empty();
    static final RexRN groupKey    = emptyChild.groupBy("group_expr");
    static final RelRN.AggCall aggCall = emptyChild.aggCall("agg");
    static final RelRN agg         = new RelRN.Aggregate(emptyChild, Seq.of(groupKey), Seq.of(aggCall));

    @Override
    public RelRN before() {
        return agg;
    }

    @Override
    public RelRN after() {
        return agg.empty();
    }
}
