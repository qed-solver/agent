package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RuleBuilder;
import org.qed.RexRN;

// SCOPE: PARTIAL — the aggregate contains only single-argument DISTINCT aggregates (two, sharing the same one distinct column) with exactly one group-by key and no non-distinct sum/min/max or count-rollup aggregates, which the source rule also permits alongside the distinct field.
public record SingleDistinctToGroupBy() implements RRule {
    static final RelRN source = RelRN.scanMany("S", Seq.of(
            RexRN.varType("S_a_Type", true), RexRN.varType("S_b_Type", true)));
    static final RexRN g = source.groupBy("g"); // the single group key, an uninterpreted expr over the input
    static final RexRN x = source.groupBy("x"); // the one shared distinct argument
    static final RelType f1Type = new RelType.VarType("f1_type", true);
    static final RelType f2Type = new RelType.VarType("f2_type", true);

    // Before: Agg(group=[g], aggr=[f1(DISTINCT x), f2(DISTINCT x)])
    static final RelRN.AggCall f1Distinct = new RelRN.AggCall(
            "f1", RuleBuilder.create().genericAggregateOp("f1", f1Type), true, f1Type, Seq.of(x));
    static final RelRN.AggCall f2Distinct = new RelRN.AggCall(
            "f2", RuleBuilder.create().genericAggregateOp("f2", f2Type), true, f2Type, Seq.of(x));
    static final RelRN beforeAgg = new RelRN.Aggregate(source, Seq.of(g), Seq.of(f1Distinct, f2Distinct));

    // After: inner group-by deduplicates on (g, x); an empty aggregate list projects to the group keys,
    // so the outer aggregate runs the same functions without DISTINCT over the deduplicated column.
    static final RelRN inner = new RelRN.Aggregate(source, Seq.of(g, x), Seq.empty());
    static final RelRN.AggCall f1Plain = new RelRN.AggCall(
            "f1", RuleBuilder.create().genericAggregateOp("f1", f1Type), false, f1Type, Seq.of(inner.field(1)));
    static final RelRN.AggCall f2Plain = new RelRN.AggCall(
            "f2", RuleBuilder.create().genericAggregateOp("f2", f2Type), false, f2Type, Seq.of(inner.field(1)));
    static final RelRN outer = new RelRN.Aggregate(inner, Seq.of(inner.field(0)), Seq.of(f1Plain, f2Plain));
    // The source rule's trailing alias-renaming projection is value-identity on (group keys, aggregates).
    static final RelRN after = outer.project(Seq.of(outer.field(0), outer.field(1), outer.field(2)));

    @Override
    public RelRN before() {
        return beforeAgg;
    }

    @Override
    public RelRN after() {
        return after;
    }
}
