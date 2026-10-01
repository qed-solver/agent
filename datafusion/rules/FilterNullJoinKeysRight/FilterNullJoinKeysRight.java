package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — only the inner-join case is encoded: a single equi-join conjunct L.col = R.col on an INNER join with IS NOT NULL(R.col) added to the right input, while the source rule adds one such filter per equi-pair for every join kind in which the right side is preserved.

public record FilterNullJoinKeysRight() implements RRule {
    static final RelRN left = RelRN.scan("L", "Key_Type");
    static final RelRN right = RelRN.scan("R", "Key_Type");

    // The two columns of the join row (L's column, R's column).
    static final RexRN lcol = left.joinField(0, right);
    static final RexRN rcol = left.joinField(1, right);

    // The equi-join conjunct L.col = R.col, as built with Calcite's standard
    // EQUALS operator (null-rejecting: it holds only when both sides are non-null).
    static final RexRN eq = new RexRN.Pred(SqlStdOperatorTable.EQUALS, Seq.of(lcol, rcol));

    // An arbitrary additional conjunct over the same two columns; the rule
    // derives no non-null information from it, and it is preserved unchanged.
    static final SqlOperator f = RuleBuilder.create().genericPredicateOp("f", true);
    static final RexRN cond = RexRN.and(eq, new RexRN.Pred(f, Seq.of(lcol, rcol)));

    // The IS NOT NULL predicate the rule adds on the right input's key column.
    static final RexRN rNotNull =
            new RexRN.Pred(SqlStdOperatorTable.IS_NOT_NULL, Seq.of(right.field(0)));

    @Override
    public RelRN before() {
        return left.join(JoinRelType.INNER, cond, right);
    }

    @Override
    public RelRN after() {
        return left.join(JoinRelType.INNER, cond, right.filter(rNotNull));
    }
}
