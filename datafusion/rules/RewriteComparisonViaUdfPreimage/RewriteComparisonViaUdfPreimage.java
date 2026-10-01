// SCOPE: PARTIAL — only the `=` comparison is encoded (the other five operators have the identical shape); the UDF is modeled as an fx column plus an explicit side-condition P stating the step-function preimage contract, with the distinct-from/NULL and in-list variants excluded
package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RRule;
import org.qed.RexRN;

public record RewriteComparisonViaUdfPreimage() implements RRule {
    // S columns: 0 = e (UDF input), 1 = fx (UDF output), 2 = lower, 3 = upper, 4 = val
    static final RelType.VarType T = RexRN.varType("Int", false);
    static final RelRN source = RelRN.scanMany("S", Seq.of(T, T, T, T, T));
    static final RexRN e = new RexRN.Field(0, source);
    static final RexRN fx = new RexRN.Field(1, source);
    static final RexRN lower = new RexRN.Field(2, source);
    static final RexRN upper = new RexRN.Field(3, source);
    static final RexRN val = new RexRN.Field(4, source);

    static RexRN cmp(SqlOperator op, RexRN a, RexRN b) {
        return new RexRN.Pred(op, Seq.of(a, b));
    }

    static RexRN imp(RexRN a, RexRN b) {
        return new RexRN.Or(Seq.of(new RexRN.Not(a), b));
    }

    // Step-function preimage contract for fx with respect to val:
    //   e < lower          ->  fx < val
    //   lower <= e < upper ->  fx = val
    //   e >= upper         ->  fx > val
    static final RexRN contract = RexRN.and(
            imp(cmp(SqlStdOperatorTable.LESS_THAN, e, lower),
                cmp(SqlStdOperatorTable.LESS_THAN, fx, val)),
            imp(RexRN.and(cmp(SqlStdOperatorTable.GREATER_THAN_OR_EQUAL, e, lower),
                          cmp(SqlStdOperatorTable.LESS_THAN, e, upper)),
                cmp(SqlStdOperatorTable.EQUALS, fx, val)),
            imp(cmp(SqlStdOperatorTable.GREATER_THAN_OR_EQUAL, e, upper),
                cmp(SqlStdOperatorTable.GREATER_THAN, fx, val)));

    @Override
    public RelRN before() {
        // fx = val
        return source.filter(RexRN.and(contract, cmp(SqlStdOperatorTable.EQUALS, fx, val)));
    }

    @Override
    public RelRN after() {
        // lower <= e < upper
        return source.filter(RexRN.and(contract,
                cmp(SqlStdOperatorTable.GREATER_THAN_OR_EQUAL, e, lower),
                cmp(SqlStdOperatorTable.LESS_THAN, e, upper)));
    }
}
