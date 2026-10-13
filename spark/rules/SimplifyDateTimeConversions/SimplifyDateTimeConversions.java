package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;
import org.apache.calcite.sql.SqlOperator;

// Spark SimplifyDateTimeConversions (expressions.scala 1298-1338).
// Case 1: DateFormat(GetTimestamp(DateFormat(child, p, tz), p, ...)) -> DateFormat(child, p, tz)
// Case 2: GetTimestamp(DateFormat(GetTimestamp(e, p, tz), p, ...))   -> GetTimestamp(e, p, tz)
// Both cases have the identical shape once the surviving inner expression is factored out:
// before = toTs(toStr(toTs(x))), after = toTs(x), where toTs models GetTimestamp (string -> timestamp)
// and toStr models DateFormat (timestamp -> string) with matching pattern/timezone.
public record SimplifyDateTimeConversions() implements RRule {
    static final RelRN S = RelRN.scan("S", RexRN.varType("S_type", false), false);
    static final RexRN x = S.field(0);
    static final SqlOperator toTs = RuleBuilder.create().genericProjectionOp("to_ts", RexRN.varType("T_type", true));
    static final SqlOperator toStr = RuleBuilder.create().genericProjectionOp("to_str", RexRN.varType("S_type", true));

    static final RexRN p1 = x.proj(toTs);   // e = GetTimestamp(child/e, p, tz)
    static final RexRN p2 = p1.proj(toStr); // DateFormat(e, p, tz)
    static final RexRN p3 = p2.proj(toTs);  // outer GetTimestamp(DateFormat(e, ...), p, tz)

    @Override
    public RelRN before() {
        return S.project(p3);
    }

    @Override
    public RelRN after() {
        return S.project(p1);
    }
}
