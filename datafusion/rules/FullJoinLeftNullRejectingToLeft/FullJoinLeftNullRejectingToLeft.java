package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the null-rejecting filter is restricted to an explicit IS_NOT_NULL on the left input's column, and only the FullJoin→LeftJoin variant is modeled (the other eliminate_outer cases are not)
public record FullJoinLeftNullRejectingToLeft() implements RRule {
    static final RelRN left = RelRN.scan("L", "L_Type");
    static final RelRN right = RelRN.scan("R", "R_Type");

    // g: the join's ON condition, unchanged by the rewrite.
    static final RexRN on = left.joinPred("on", right);

    // The null-rejecting filter, seen from above the join: IS_NOT_NULL on the
    // left side's column as it appears in the join row (field 0). In a full
    // join, only the right-only (NULL-extended-on-the-left) rows have a NULL
    // left side, so this filter removes exactly those rows.
    static final RexRN rejectAbove =
            new RexRN.Pred(SqlStdOperatorTable.IS_NOT_NULL, Seq.of(left.joinField(0, right)));

    @Override
    public RelRN before() {
        // Filter(IS_NOT_NULL(l), FullJoin(L, R, g)) — the filter sits above
        // the join and rejects the full join's NULL-extended left side.
        return left.join(JoinRelType.FULL, on, right).filter(rejectAbove);
    }

    @Override
    public RelRN after() {
        // Filter(IS_NOT_NULL(l), LeftJoin(L, R, g)) — only the join kind is
        // flipped (FULL→LEFT); the same filter remains above the join,
        // exactly as in the source rule (no pushdown).
        return left.join(JoinRelType.LEFT, on, right).filter(rejectAbove);
    }
}
