package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RexRN;

// SCOPE: PARTIAL — only the Inner Join case is modeled with two equi-join keys where both LEFT keys are nullable; the rewrite adds the IS NOT NULL filter to the LEFT input only (the right input is left unchanged)
public record FilterNullJoinKeysLeft() implements RRule {
    // Both inputs have two columns, used as the two equi-join keys.
    static final RelRN left = RelRN.scanMany("L", Seq.of(
            RexRN.varType("L_Key_Type", true),
            RexRN.varType("L_Key_Type", true)));
    static final RelRN right = RelRN.scanMany("R", Seq.of(
            RexRN.varType("R_Key_Type", true),
            RexRN.varType("R_Key_Type", true)));

    // The equi-join keys as seen from above the join (join-row fields).
    static final RexRN lc1 = left.joinField(0, right);
    static final RexRN lc2 = left.joinField(1, right);
    static final RexRN rc1 = left.joinField(2, right);
    static final RexRN rc2 = left.joinField(3, right);

    // The ON condition: L.c1 = R.c1 AND L.c2 = R.c2 (null-rejecting EQUALS).
    static final RexRN eq1 = new RexRN.Pred(SqlStdOperatorTable.EQUALS, Seq.of(lc1, rc1));
    static final RexRN eq2 = new RexRN.Pred(SqlStdOperatorTable.EQUALS, Seq.of(lc2, rc2));
    static final RexRN cond = RexRN.and(eq1, eq2);

    // The IS NOT NULL filters the rule derives on the LEFT input, one per
    // nullable left key, as seen within the left input alone.
    static final RexRN l1NotNull =
            new RexRN.Pred(SqlStdOperatorTable.IS_NOT_NULL, Seq.of(left.field(0)));
    static final RexRN l2NotNull =
            new RexRN.Pred(SqlStdOperatorTable.IS_NOT_NULL, Seq.of(left.field(1)));
    static final RexRN leftFilter = RexRN.and(l1NotNull, l2NotNull);

    @Override
    public RelRN before() {
        // Inner Join (L, R, L.c1 = R.c1 AND L.c2 = R.c2)
        return left.join(JoinRelType.INNER, cond, right);
    }

    @Override
    public RelRN after() {
        // Inner Join (Filter(L, L.c1 IS NOT NULL AND L.c2 IS NOT NULL), R, cond)
        return left.filter(leftFilter).join(JoinRelType.INNER, cond, right);
    }
}
