package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RexRN.Pred;

// SCOPE: PARTIAL — LEFT/LEFT join chain only, a self-join of the same unique
// single non-nullable-column scan on equality of that column for the bottom
// join (as in the proven ProjectJoinRemove precedent — every row matches
// itself, so the null-extension term never fires and no duplicates are
// introduced), plus a top join to a second single non-nullable-column scan
// C on that same column, with the outer project keeping only the preserved
// left column and C's column (never the eliminated join's right column).
public record ProjectJoinJoinRemove() implements RRule {
    static final RelType.VarType V = new RelType.VarType("V", false);
    static final RelRN t = RelRN.scan("T", V, true);
    static final RelRN c = RelRN.scan("C", V, false);

    // bottomJoin (self-join) row layout: [T.col, T.col]
    static final RelRN bottomJoin = t.join(JoinRelType.LEFT,
            new Pred(SqlStdOperatorTable.EQUALS, Seq.of(t.joinField(0, t), t.joinField(1, t))), t);

    // topJoin row layout: [T.col, T.col, C.col]
    static final RelRN topJoin = bottomJoin.join(JoinRelType.LEFT,
            new Pred(SqlStdOperatorTable.EQUALS,
                    Seq.of(new RexRN.JoinField(0, bottomJoin, c), new RexRN.JoinField(2, bottomJoin, c))),
            c);

    // Direct T-C join (self-join's right side removed) row layout: [T.col, C.col]
    static final RelRN directJoin = t.join(JoinRelType.LEFT,
            new Pred(SqlStdOperatorTable.EQUALS, Seq.of(t.joinField(0, c), t.joinField(1, c))), c);

    @Override
    public RelRN before() {
        // Project away the eliminated join's right column (ordinal 1),
        // keeping the preserved left column (0) and C's column (2).
        return topJoin.project(Seq.of(topJoin.field(0), topJoin.field(2)));
    }

    @Override
    public RelRN after() {
        // Bottom (self-)join removed: join T directly with C on the same key.
        return directJoin.project(Seq.of(directJoin.field(0), directJoin.field(1)));
    }
}
