package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;

// SCOPE: FULL
public record SemiJoinFilterTranspose() implements RRule {
    static final RelRN X = RelRN.scanMany("X", Seq.of(
            RexRN.varType("X0_Type", true), RexRN.varType("X1_Type", true)));
    static final RelRN Y = RelRN.scanMany("Y", Seq.of(
            RexRN.varType("Y0_Type", true), RexRN.varType("Y1_Type", true)));

    // The semi-join condition: an uninterpreted predicate over the
    // combined (X, Y) row.
    static final RexRN cond = X.joinPred("join_cond", Y);
    // The filter predicate: an uninterpreted predicate over X's columns.
    static final RexRN filterPred = X.pred("filter");

    @Override
    public RelRN before() {
        // SemiJoin(Filter(X, F), Y, C)
        return X.filter(filterPred).join(JoinRelType.SEMI, cond, Y);
    }

    @Override
    public RelRN after() {
        // Filter(SemiJoin(X, Y, C), F). A semi-join's output is exactly
        // its left input's columns (X's), so the very same predicate
        // object F over X's columns applies directly to the semi-join
        // output, as Calcite's rule copies filter.getCondition() verbatim.
        RelRN semi = X.join(JoinRelType.SEMI, cond, Y);
        return semi.filter(filterPred);
    }
}
