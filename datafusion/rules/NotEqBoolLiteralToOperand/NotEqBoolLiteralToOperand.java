package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — encodes only the `true != A --> !A` arm of the source rule (A an arbitrary boolean expression); the `false != A --> A` and `null != A --> null` arms have distinct rewrite targets that one before/after pair cannot express
public record NotEqBoolLiteralToOperand() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    // A: arbitrary boolean-typed expression over the row, as an uninterpreted predicate
    static final RexRN a = source.pred("A");

    @Override
    public RelRN before() {
        // true != A
        return source.filter(new RexRN.Pred(SqlStdOperatorTable.NOT_EQUALS, Seq.of(RexRN.trueLiteral(), a)));
    }

    @Override
    public RelRN after() {
        // !A
        return source.filter(new RexRN.Not(a));
    }
}
