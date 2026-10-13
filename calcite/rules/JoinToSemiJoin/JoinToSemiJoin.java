package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RRule;
import org.qed.RexRN;

// SCOPE: PARTIAL — INNER join only, single-column equi key that is exactly the right aggregate's sole group key (no aggregate calls), with a parent project dropping the right column; the LEFT variant (join elimination with an empty aggregate) is not modeled.
public record JoinToSemiJoin() implements RRule {
    static final RelType.VarType T = RexRN.varType("T", true);
    static final RelRN left = RelRN.scan("L", T, false);
    // R is the pre-aggregate right input; the aggregate groups by its single
    // column, so the join key on the aggregate side is exactly that group key.
    static final RelRN rawRight = RelRN.scan("R", T, false);
    static final RelRN agg = new RelRN.Aggregate(rawRight, Seq.of(rawRight.field(0)), Seq.empty());

    // INNER join condition: L.col = Agg.groupKey (which is R.col)
    static final RexRN joinCond = new RexRN.Pred(SqlStdOperatorTable.EQUALS,
            Seq.of(left.joinField(0, agg), left.joinField(1, agg)));

    // Semi-join condition remapped onto the pre-aggregate right input: L.col = R.col
    static final RexRN semiCond = new RexRN.Pred(SqlStdOperatorTable.EQUALS,
            Seq.of(left.joinField(0, rawRight), left.joinField(1, rawRight)));

    @Override
    public RelRN before() {
        RelRN innerJoin = left.join(JoinRelType.INNER, joinCond, agg);
        // project drops the aggregate's (right) column, keeping only the left column
        return innerJoin.project(innerJoin.field(0));
    }

    @Override
    public RelRN after() {
        RelRN semiJoin = left.join(JoinRelType.SEMI, semiCond, rawRight);
        // semi-join already emits only the left column; project keeps it
        return semiJoin.project(semiJoin.field(0));
    }
}
