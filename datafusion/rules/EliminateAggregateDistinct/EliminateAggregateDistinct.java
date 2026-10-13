package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the aggregated column is the input relation's unique key and is also the group-by key, so each group holds at most one row and dropping DISTINCT is a per-group no-op; the full rule (Insensitive aggregates such as min/max/bool_and are idempotent, so DISTINCT never changes their value over arbitrary inputs) is unprovable in QED, which models every aggregate as an uninterpreted function knowing only bag-equality of its input.
public record EliminateAggregateDistinct() implements RRule {
    static final RelRN source = RelRN.scan("S", RexRN.varType("S_Type", false), true);
    static final RexRN key = source.field(0);
    static final RelRN.AggCall distinctAgg = new RelRN.AggCall(
            "f", true, RexRN.varType("F_Type", true), Seq.of(key));
    static final RelRN.AggCall plainAgg = new RelRN.AggCall(
            "f", false, RexRN.varType("F_Type", true), Seq.of(key));

    @Override
    public RelRN before() {
        return new RelRN.Aggregate(source, Seq.of(key), Seq.of(distinctAgg));
    }

    @Override
    public RelRN after() {
        return new RelRN.Aggregate(source, Seq.of(key), Seq.of(plainAgg));
    }
}
