package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the null-rejecting filter is restricted to an explicit IS_NOT_NULL on the left input's column, and only the RightJoin→InnerJoin variant of eliminate_outer is modeled (other join-type branches and projection inlining are not)
public record RightJoinNullRejectingToInner() implements RRule {
    static final RelRN left = RelRN.scan("L", "L_Type");
    static final RelRN right = RelRN.scan("R", "R_Type");

    // g: the join's ON condition, unchanged by the rewrite.
    static final RexRN on = left.joinPred("on", right);

    // The null-rejecting filter, seen from above the join: IS_NOT_NULL on the
    // left side's column as it appears in the join row (field 0). In a right
    // join, only the NULL-extended (unmatched) rows have a NULL left side,
    // so this filter removes exactly those rows.
    static final RexRN rejectAbove =
            new RexRN.Pred(SqlStdOperatorTable.IS_NOT_NULL, Seq.of(left.joinField(0, right)));

    @Override
    public RelRN before() {
        // Filter(IS_NOT_NULL(l), RightJoin(L, R, on)) — the filter sits above
        // the join and rejects the right join's NULL-extended left side.
        return left.join(JoinRelType.RIGHT, on, right).filter(rejectAbove);
    }

    @Override
    public RelRN after() {
        // Filter(IS_NOT_NULL(l), InnerJoin(L, R, on)) — only the join kind is
        // flipped (RIGHT→INNER); the same filter remains above the join,
        // exactly as in the source rule (no pushdown).
        return left.join(JoinRelType.INNER, on, right).filter(rejectAbove);
    }
}
