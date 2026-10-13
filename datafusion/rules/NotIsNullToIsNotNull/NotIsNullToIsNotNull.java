package org.qed.RRuleInstances;

import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: FULL
public record NotIsNullToIsNotNull() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN x = source.field(0);

    @Override
    public RelRN before() {
        // NOT(x IS NULL)
        return source.filter(new RexRN.Not(x.pred(SqlStdOperatorTable.IS_NULL)));
    }

    @Override
    public RelRN after() {
        // x IS NOT NULL
        return source.filter(x.pred(SqlStdOperatorTable.IS_NOT_NULL));
    }
}
