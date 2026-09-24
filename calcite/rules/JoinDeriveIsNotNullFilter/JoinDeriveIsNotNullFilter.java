package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — the join condition is restricted to a conjunction containing an equality conjunct between a left column and a right column (plus arbitrary other conjuncts over those same two columns); the original rule derives IS NOT NULL filters from any condition for which per-column non-null inference succeeds

public record JoinDeriveIsNotNullFilter() implements RRule {
    static final RelRN left = RelRN.scan("L", "Key_Type");
    static final RelRN right = RelRN.scan("R", "Key_Type");

    // The two columns of the join row (L's column, R's column).
    static final RexRN lcol = left.joinField(0, right);
    static final RexRN rcol = left.joinField(1, right);

    // The equality conjunct L.col = R.col, as built with Calcite's standard
    // EQUALS operator (null-rejecting: it holds only when both sides are non-null).
    static final RexRN eq = new RexRN.Pred(SqlStdOperatorTable.EQUALS, Seq.of(lcol, rcol));

    // An arbitrary additional conjunct over the same two columns; the rule
    // derives no non-null information from it, and it is preserved on both sides.
    static final SqlOperator f = RuleBuilder.create().genericPredicateOp("f", true);
    static final RexRN cond = RexRN.and(eq, new RexRN.Pred(f, Seq.of(lcol, rcol)));

    // The IS NOT NULL predicates the rule derives, one per side.
    static final RexRN lNotNull =
            new RexRN.Pred(SqlStdOperatorTable.IS_NOT_NULL, Seq.of(left.field(0)));
    static final RexRN rNotNull =
            new RexRN.Pred(SqlStdOperatorTable.IS_NOT_NULL, Seq.of(right.field(0)));

    @Override
    public RelRN before() {
        return left.join(JoinRelType.INNER, cond, right);
    }

    @Override
    public RelRN after() {
        RelRN newLeft = left.filter(lNotNull);
        RelRN newRight = right.filter(rNotNull);
        return newLeft.join(JoinRelType.INNER, cond, newRight);
    }
}
