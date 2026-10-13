package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RexRN;
import org.qed.RRule;

// SCOPE: FULL
public record SimplifyCaseConversionExpressions() implements RRule {
    static final RelRN S = RelRN.scanManyRelTypes("S", Seq.of(
            RelType.fromString("CHAR", true),
            RelType.fromString("CHAR", true)));
    static final RexRN a = S.field(0);
    static final RexRN b = S.field(1);
    static final SqlOperator UPPER = SqlStdOperatorTable.UPPER;
    static final SqlOperator LOWER = SqlStdOperatorTable.LOWER;

    @Override
    public RelRN before() {
        return S.project(Seq.of(
                a.proj(UPPER).proj(UPPER),
                b.proj(LOWER).proj(LOWER)));
    }

    @Override
    public RelRN after() {
        return S.project(Seq.of(
                a.proj(UPPER),
                b.proj(LOWER)));
    }
}
