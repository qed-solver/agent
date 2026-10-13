package org.qed.RRuleInstances;

import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RelType;
import org.qed.RuleBuilder;

// SCOPE: FULL
public record ProjectMerge() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");

    // Bottom projection: arbitrary uninterpreted function Bottom over the source's columns.
    static final RexRN bottomExpr = source.proj("Bottom", "Bottom_Type");
    static final RelRN inner = source.project(bottomExpr);

    // Top projection: arbitrary uninterpreted function Top over the (single) column of the inner project.
    static final SqlOperator topOp =
            RuleBuilder.create().genericProjectionOp("Top", new RelType.VarType("Top_Type", true));
    static final RexRN topExprBefore = inner.proj(topOp);

    // Merged: Top composed with Bottom, applied directly to the source's columns.
    static final RexRN mergedExprAfter = bottomExpr.proj(topOp);

    @Override
    public RelRN before() {
        return inner.project(topExprBefore);
    }

    @Override
    public RelRN after() {
        return source.project(mergedExprAfter);
    }
}
