package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the set-minus shares a unique (keyed) base and both inputs are filters over it, i.e. Minus(Filter(P_L, U), Filter(P_R, U)) -> distinct over Filter(P_L AND NOT(P_R), U); Calcite's rule is general over non-keyed bases and also handles a plain (unfiltered) left input.
public record MinusToFilter() implements RRule {
    static final RelRN source = RelRN.scan("Source", RexRN.varType("Source_Type", true), true);
    static final RexRN left = source.pred("left");
    static final RexRN right = source.pred("right");

    @Override
    public RelRN before() {
        // SET MINUS (all = false, i.e. distinct/set semantics) of two filtered views of the same unique base
        return source.filter(left).minus(false, source.filter(right));
    }

    @Override
    public RelRN after() {
        // SELECT DISTINCT * FROM Source WHERE left AND NOT(right)
        RelRN filtered = source.filter(RexRN.and(left, new RexRN.Not(right)));
        return new RelRN.Aggregate(filtered, Seq.of(filtered.field(0)), Seq.empty());
    }
}
