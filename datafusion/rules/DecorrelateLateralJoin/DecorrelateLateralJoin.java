package org.qed.RRuleInstances;

// SCOPE: PARTIAL — INNER lateral join only, with the lateral subquery being a single scan guarded by one uninterpreted binary correlation predicate; no user ON clause, no aggregate, no HAVING
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

public record DecorrelateLateralJoin() implements RRule {
    static final RelRN left = RelRN.scan("L", RexRN.varType("T", true), false);
    static final RelRN right = RelRN.scan("R", RexRN.varType("T", true), false);
    static final SqlOperator cond = RuleBuilder.create().genericPredicateOp("cond", true);

    @Override
    public RelRN before() {
        return left.correlate(JoinRelType.INNER, cond, right);
    }

    @Override
    public RelRN after() {
        return left.join(JoinRelType.INNER, new RexRN.Pred(cond, left.joinFields(right)), right);
    }
}