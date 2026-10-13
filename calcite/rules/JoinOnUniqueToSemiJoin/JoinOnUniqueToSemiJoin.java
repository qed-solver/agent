package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RRule;
import org.qed.RexRN;

// SCOPE: PARTIAL — 1-column-per-side equi-join on R's unique key column, INNER variant only, with a parent project that uses only the left column
public record JoinOnUniqueToSemiJoin() implements RRule {
    static final RelType.VarType T = RexRN.varType("T", true);
    static final RelRN left = RelRN.scan("L", T, false);
    static final RelRN right = RelRN.scan("R", T, true); // R is unique on col 0 (its key)

    // INNER-join condition: L.col = R.col
    static final RexRN cond = new RexRN.Pred(SqlStdOperatorTable.EQUALS,
            Seq.of(left.joinField(0, right), left.joinField(1, right)));

    @Override
    public RelRN before() {
        RelRN innerJoin = left.join(JoinRelType.INNER, cond, right);
        // project drops the right column, keeping only the left column
        return innerJoin.project(innerJoin.field(0));
    }

    @Override
    public RelRN after() {
        RelRN semiJoin = left.join(JoinRelType.SEMI, cond, right);
        // semi-join already emits only the left column; project keeps it
        return semiJoin.project(semiJoin.field(0));
    }
}
