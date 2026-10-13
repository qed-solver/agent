package org.qed.RRuleInstances;

import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — only the IsUnknown branch is encoded; IsNull(expr) → false on non-nullable expr is not covered
public record IsUnknownToIsNull() implements RRule {
    // Input relation with a single column x whose uninterpreted type is declared
    // non-nullable — encodes the source rule's side condition
    // `if !info.nullable(&expr)`: x may be any value of that type, but never NULL.
    static final RelRN source = RelRN.scan("Source", RexRN.varType("X_Type", false), false);

    @Override
    public RelRN before() {
        return source.filter(source.field(0).pred(SqlStdOperatorTable.IS_UNKNOWN));
    }

    @Override
    public RelRN after() {
        return source.filter(RexRN.falseLiteral());
    }
}
