package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;

// SCOPE: PARTIAL — filter predicate restricted to exactly one IS_NOT_NULL conjunct per single-column join side (FULL→INNER branch only; no projection inlining, no other join-type branches)
public record FullJoinBothNullRejectingToInner() implements RRule {
    static final RelRN left = RelRN.scan("L", "V");
    static final RelRN right = RelRN.scan("R", "V");
    static final RexRN on = left.joinPred("on", right);
    static final RexRN rejL = new RexRN.Pred(SqlStdOperatorTable.IS_NOT_NULL,
            Seq.of(new RexRN.JoinField(0, left, right)));
    static final RexRN rejR = new RexRN.Pred(SqlStdOperatorTable.IS_NOT_NULL,
            Seq.of(new RexRN.JoinField(1, left, right)));
    static final RexRN p = RexRN.and(rejL, rejR);

    @Override
    public RelRN before() {
        return left.join(JoinRelType.FULL, on, right).filter(p);
    }

    @Override
    public RelRN after() {
        return left.join(JoinRelType.INNER, on, right).filter(p);
    }
}
