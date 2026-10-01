package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — only the binary-comparison form cast(col) cmp lit -> col cmp cast(lit); in-list forms and the literal-in-range / supported-type guards are not modeled
public record UnwrapCastAroundComparison() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RelRN lit = RelRN.scan("Lit", "Lit_Type");
    static final RelRN joined = source.join(JoinRelType.INNER, RexRN.trueLiteral(), lit);
    static final SqlOperator cmp = RuleBuilder.create().genericPredicateOp("cmp", true);

    @Override
    public RelRN before() {
        var castCol = joined.field(0).proj("cast", "Cast_Type");
        var litField = joined.field(1);
        return joined.filter(new RexRN.Pred(cmp, Seq.of(castCol, litField)));
    }

    @Override
    public RelRN after() {
        var colField = joined.field(0);
        var castLit = joined.field(1).proj("cast", "Cast_Type");
        return joined.filter(new RexRN.Pred(cmp, Seq.of(colField, castLit)));
    }
}
