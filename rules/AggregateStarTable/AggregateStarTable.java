package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: FULL
public record AggregateStarTable() implements RRule {
    // The star table being scanned.
    static final RelRN star = RelRN.scan("Star", "Star_Type");

    // Before: Aggregate over the star table, grouping by uninterpreted key g
    // and computing one aggregate call f.
    static final RelRN beforeAgg = new RelRN.Aggregate(
            star,
            Seq.of(star.groupBy("g")),
            Seq.of(star.field(0).aggCall("f")));

    // After: the lattice's materialized aggregate table at the required
    // level of aggregation (exact-match case: no roll-up, no reproject).
    static final RelRN materialized = RelRN.scanMany("AggTable",
            Seq.of(RexRN.varType("g_type", true), RexRN.varType("f_type", true)));

    @Override
    public RelRN before() {
        return beforeAgg;
    }

    @Override
    public RelRN after() {
        return materialized;
    }
}
