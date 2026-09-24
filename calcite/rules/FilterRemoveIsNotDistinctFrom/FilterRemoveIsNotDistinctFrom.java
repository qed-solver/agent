package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RexRN.Pred;

// SCOPE: PARTIAL — IS NOT DISTINCT FROM is exactly the top-level filter condition applied over a two-column row built as an inner join of two single-column scans; the operands are plain (non-struct) columns of a single shared type
public record FilterRemoveIsNotDistinctFrom() implements RRule {
    static final RelRN left = RelRN.scan("L", "V");
    static final RelRN right = RelRN.scan("R", "V");

    // Identical join on both sides, so only the scalar filter is compared.
    static final RelRN join = left.join(JoinRelType.INNER, RexRN.trueLiteral(), right);
    static final RexRN x = join.field(0);
    static final RexRN y = join.field(1);

    @Override
    public RelRN before() {
        // Filter(x IS NOT DISTINCT FROM y, Join(L, R, true))
        return join.filter(new Pred(SqlStdOperatorTable.IS_NOT_DISTINCT_FROM, Seq.of(x, y)));
    }

    @Override
    public RelRN after() {
        // Filter((x IS NULL AND y IS NULL) OR IS_TRUE(x = y), Join(L, R, true))
        // — the expansion Calcite's RelOptUtil.isDistinctFrom(rexBuilder, x, y, true) produces.
        return join.filter(new RexRN.Or(Seq.of(
                new RexRN.And(Seq.of(
                        new Pred(SqlStdOperatorTable.IS_NULL, Seq.of(x)),
                        new Pred(SqlStdOperatorTable.IS_NULL, Seq.of(y)))),
                new Pred(SqlStdOperatorTable.IS_TRUE,
                        Seq.of(new Pred(SqlStdOperatorTable.EQUALS, Seq.of(x, y)))))));
    }
}
