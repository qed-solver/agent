package org.qed.RRuleInstances;

import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: FULL
public record LiteralIsNullFold() implements RRule {
    // Input relation with a single column x whose uninterpreted type is declared
    // non-nullable — this encodes the source rule's side condition
    // (!info.nullable(&expr)): x may be any value of that type, but never NULL.
    static final RelRN source = RelRN.scan("Source", RexRN.varType("X_Type", false), false);

    @Override
    public RelRN before() {
        // x IS NOT NULL  (DataFusion also covers x IS NOT UNKNOWN, which is
        // semantically identical under three-valued logic: both are true
        // exactly when x is not NULL, and hence both are true for non-nullable x)
        return source.filter(source.field(0).pred(SqlStdOperatorTable.IS_NOT_NULL));
    }

    @Override
    public RelRN after() {
        // Folded to the literal boolean `true`
        return source.filter(RexRN.trueLiteral());
    }
}
