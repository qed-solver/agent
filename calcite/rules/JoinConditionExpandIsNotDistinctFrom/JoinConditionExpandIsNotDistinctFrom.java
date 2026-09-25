package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RexRN.Pred;

// SCOPE: PARTIAL — IS NOT DISTINCT FROM is the entire condition of an inner join between two single-column scans of a single shared type, with the 3-valued-logic expansion on the right-hand side rather than Calcite's COALESCE-based form
public record JoinConditionExpandIsNotDistinctFrom() implements RRule {
    static final RelRN left = RelRN.scan("L", "V");
    static final RelRN right = RelRN.scan("R", "V");

    // Fields of the combined join row: L's column and R's column.
    static final RexRN x = left.joinField(0, right);
    static final RexRN y = left.joinField(1, right);

    @Override
    public RelRN before() {
        // Join(L, R, x IS NOT DISTINCT FROM y)
        return left.join(JoinRelType.INNER,
                new Pred(SqlStdOperatorTable.IS_NOT_DISTINCT_FROM, Seq.of(x, y)),
                right);
    }

    @Override
    public RelRN after() {
        // Join(L, R, (x IS NULL AND y IS NULL) OR IS_TRUE(x = y))
        return left.join(JoinRelType.INNER,
                new RexRN.Or(Seq.of(
                        new RexRN.And(Seq.of(
                                new Pred(SqlStdOperatorTable.IS_NULL, Seq.of(x)),
                                new Pred(SqlStdOperatorTable.IS_NULL, Seq.of(y)))),
                        new Pred(SqlStdOperatorTable.IS_TRUE,
                                Seq.of(new Pred(SqlStdOperatorTable.EQUALS, Seq.of(x, y)))))),
                right);
    }
}
