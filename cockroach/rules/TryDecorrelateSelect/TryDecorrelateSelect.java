// SCOPE: PARTIAL — INNER apply join only, single-column L and R, one uninterpreted ON predicate and one uninterpreted Select filter over (l, r)
package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RRule;
import org.qed.RuleBuilder;

public record TryDecorrelateSelect() implements RRule {
    static final RelRN left = RelRN.scan("L", RexRN.varType("T", true), false);
    static final RelRN right = RelRN.scan("R", RexRN.varType("T", true), false);
    static final SqlOperator selectOp = RuleBuilder.create().genericPredicateOp("select", true);
    static final SqlOperator onOp = RuleBuilder.create().genericPredicateOp("on", true);

    @Override
    public RelRN before() {
        // InnerJoinApply(L, Select(R, select(l, r)), on): the apply keeps the correlated Select on its right side.
        RelRN apply = left.correlate(JoinRelType.INNER, selectOp, right);
        return apply.filter(new RexRN.Pred(onOp, apply.fields()));
    }

    @Override
    public RelRN after() {
        // InnerJoin(L, R, on AND select): the Select filter is concatenated into the join condition.
        RexRN onPred = new RexRN.Pred(onOp, left.joinFields(right));
        RexRN selectPred = new RexRN.Pred(selectOp, left.joinFields(right));
        return left.join(JoinRelType.INNER, RexRN.and(onPred, selectPred), right);
    }
}
