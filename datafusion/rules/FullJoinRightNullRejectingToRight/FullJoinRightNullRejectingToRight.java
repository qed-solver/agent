package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;

// SCOPE: PARTIAL — only the FULL→RIGHT branch is encoded (DataFusion's eliminate_outer case (Full,false,true)), with the null-rejecting filter restricted to an explicit IS_NOT_NULL on the right input's column sitting directly above the join (no intervening projections, whose predicate-inlining is used by the source only for analysis and cannot be modeled as a QED-verifiable property of an uninterpreted predicate).
public record FullJoinRightNullRejectingToRight() implements RRule {
    static final RelRN left = RelRN.scan("L", "L_Type");
    static final RelRN right = RelRN.scan("R", "R_Type");

    // g: the join's ON condition, unchanged by the rewrite.
    static final RexRN on = left.joinPred("on", right);

    // The null-rejecting filter, seen from above the join: IS_NOT_NULL on the
    // right side's column as it appears in the join row (field 1). This rejects
    // the full join's NULL-extended (no left match) rows.
    static final RexRN rejectAbove =
            new RexRN.Pred(SqlStdOperatorTable.IS_NOT_NULL, Seq.of(left.joinField(1, right)));

    @Override
    public RelRN before() {
        // Filter(FullJoin(L, R, g), IS_NOT_NULL(r)) — the filter sits above the
        // join and rejects the full join's NULL-extended right side.
        return left.join(JoinRelType.FULL, on, right).filter(rejectAbove);
    }

    @Override
    public RelRN after() {
        // Filter(RightJoin(L, R, g), IS_NOT_NULL(r)) — only the join kind is
        // flipped (FULL→RIGHT); the same filter remains above the join.
        return left.join(JoinRelType.RIGHT, on, right).filter(rejectAbove);
    }
}