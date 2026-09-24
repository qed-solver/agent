package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RexRN.Pred;

// SCOPE: PARTIAL — the join condition is a single equality (L.col = R.col) between one column of each side, an equijoin condition that is never TRUE on null-extended rows
public record FullToLeftAndRightJoin() implements RRule {
    static final RelRN left = RelRN.scan("L", "V");
    static final RelRN right = RelRN.scan("R", "V");

    // Fields of the two-column join-output row; all of FULL/LEFT/RIGHT joins
    // of L and R have this same layout, so the same condition and the same
    // IS_NOT_TRUE filter apply on top of each of them.
    static final RelRN joinRow = left.join(JoinRelType.FULL, RexRN.trueLiteral(), right);
    static final RexRN joinCond =
            new Pred(SqlStdOperatorTable.EQUALS, Seq.of(joinRow.field(0), joinRow.field(1)));

    // IS_NOT_TRUE(join_cond), as built by Calcite's onMatch.
    static final RexRN notMatched =
            new Pred(SqlStdOperatorTable.IS_NOT_TRUE, Seq.of(joinCond));

    @Override
    public RelRN before() {
        return left.join(JoinRelType.FULL, joinCond, right);
    }

    @Override
    public RelRN after() {
        // LEFT JOIN with the same condition, UNION ALL
        // (RIGHT JOIN with the same condition) filtered to rows that did not
        // match (IS NOT TRUE of the condition).
        RelRN leftJoin = left.join(JoinRelType.LEFT, joinCond, right);
        RelRN rightJoin = left.join(JoinRelType.RIGHT, joinCond, right).filter(notMatched);
        return leftJoin.union(true, rightJoin);
    }
}