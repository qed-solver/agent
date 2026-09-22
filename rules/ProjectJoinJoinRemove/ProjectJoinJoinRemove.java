package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RRule;
import org.qed.RexRN;

// SCOPE: PARTIAL — all three inputs are single-column scans with concrete equi-join conditions on the unique key of the removed input (whose column type matches), and the project selects the identity columns of the other two inputs, rather than arbitrary multi-column X/Z-pure conditions and projections.
public record ProjectJoinJoinRemove() implements RRule {
    static final RelType.VarType keyType = RexRN.varType("Key_Type", true);
    static final RelRN x = RelRN.scan("X", keyType, false);
    static final RelRN y = RelRN.scan("Y", keyType, true);
    static final RelRN z = RelRN.scan("Z", RexRN.varType("Z_Type", true), false);

    // Bottom join: X LEFT JOIN Y ON x.col = y.col.
    static final RexRN bottomCond = new RexRN.Pred(SqlStdOperatorTable.EQUALS,
            Seq.of(x.joinField(0, y), x.joinField(1, y)));
    static final RelRN bottomJoin = x.join(JoinRelType.LEFT, bottomCond, y);

    // Top join: bottom LEFT JOIN Z ON X.col = z.col.  joinField indexes into
    // leftCols+rightCols combined: bottom has 2 left cols, so Z's col is 2.
    static final RexRN topCond = new RexRN.Pred(SqlStdOperatorTable.EQUALS,
            Seq.of(bottomJoin.joinField(0, z), bottomJoin.joinField(2, z)));
    static final RelRN topJoin = bottomJoin.join(JoinRelType.LEFT, topCond, z);

    // After: X LEFT JOIN Z ON x.col = z.col.
    static final RexRN newCond = new RexRN.Pred(SqlStdOperatorTable.EQUALS,
            Seq.of(x.joinField(0, z), x.joinField(1, z)));
    static final RelRN newJoin = x.join(JoinRelType.LEFT, newCond, z);

    @Override
    public RelRN before() {
        // topJoin row is (x, y, z); keep (x, z), dropping y.
        return topJoin.project(Seq.of(topJoin.field(0), topJoin.field(2)));
    }

    @Override
    public RelRN after() {
        // newJoin row is (x, z); identity selection.
        return newJoin.project(Seq.of(newJoin.field(0), newJoin.field(1)));
    }
}
