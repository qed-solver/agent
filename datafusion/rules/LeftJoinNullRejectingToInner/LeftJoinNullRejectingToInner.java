package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the null-rejecting filter is restricted to an explicit IS_NOT_NULL on the right input's column, and only the LeftJoin→InnerJoin variant of eliminate_outer is modeled (other join-type branches and projection inlining are not)
public record LeftJoinNullRejectingToInner() implements RRule {
    static final RelRN left = RelRN.scan("L", "L_Type");
    static final RelRN right = RelRN.scan("R", "R_Type");

    // g: the join's ON condition, unchanged by the rewrite.
    static final RexRN on = left.joinPred("on", right);

    // The null-rejecting filter, seen from above the join: IS_NOT_NULL on the
    // right side's column as it appears in the join row (field 1). In a left
    // join, only the NULL-extended (unmatched) rows have a NULL right side,
    // so this filter removes exactly those rows.
    static final RexRN rejectAbove =
            new RexRN.Pred(SqlStdOperatorTable.IS_NOT_NULL, Seq.of(left.joinField(1, right)));

    @Override
    public RelRN before() {
        // Filter(IS_NOT_NULL(r), LeftJoin(L, R, on)) — the filter sits above
        // the join and rejects the left join's NULL-extended right side.
        return left.join(JoinRelType.LEFT, on, right).filter(rejectAbove);
    }

    @Override
    public RelRN after() {
        // Filter(IS_NOT_NULL(r), InnerJoin(L, R, on)) — only the join kind is
        // flipped (LEFT→INNER); the same filter remains above the join,
        // exactly as in the source rule (no pushdown).
        return left.join(JoinRelType.INNER, on, right).filter(rejectAbove);
    }
}