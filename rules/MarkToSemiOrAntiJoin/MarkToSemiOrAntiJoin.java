// SCOPE: PARTIAL — single-column inputs; the filter condition is exactly the marker column and the project keeps exactly the left columns (the rule's semi-join case with no extra conjuncts).
package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

public record MarkToSemiOrAntiJoin() implements RRule {
    static final RelRN left = RelRN.scan("L", "T");
    static final RelRN right = RelRN.scan("R", "T");
    static final SqlOperator joinOp = RuleBuilder.create().genericPredicateOp("J", true);
    static final RexRN joinCond = left.joinPred(joinOp, right);
    static final RelRN markJoin = left.join(JoinRelType.LEFT_MARK, joinCond, right);
    static final RexRN marker = markJoin.field(1);

    @Override
    public RelRN before() {
        return markJoin.filter(marker).project(markJoin.field(0));
    }

    @Override
    public RelRN after() {
        return left.join(JoinRelType.SEMI, joinCond, right);
    }
}
