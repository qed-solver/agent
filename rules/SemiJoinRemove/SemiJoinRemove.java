package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;

// Candidate 2: Calcite SemiJoinRemoveRule — X ⋉ Y ⟹ X with a trivially-true
// semi-join condition (the strongest case: every X-row matches whenever Y is
// non-empty).
public record SemiJoinRemove() implements RRule {
    static final RelRN X = RelRN.scan("X", "X_Type");
    static final RelRN Y = RelRN.scan("Y", "Y_Type");

    @Override
    public RelRN before() {
        return X.join(JoinRelType.SEMI, RexRN.trueLiteral(), Y);
    }

    @Override
    public RelRN after() {
        return X;
    }
}
