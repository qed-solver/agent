package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: FULL
public record JoinAddRedundantSemiJoin() implements RRule {
    static final RelRN X = RelRN.scan("X", "X_Type");
    static final RelRN Y = RelRN.scan("Y", "Y_Type");

    // The original inner join's condition, an uninterpreted predicate over
    // the joined row (X, Y).
    static final RexRN cond = X.joinPred("cond", Y);

    // before: X ⋈_C Y  (Calcite's LogicalJoin(X, Y) with an INNER condition
    // whose left key analysis succeeds)
    @Override
    public RelRN before() {
        return X.join(JoinRelType.INNER, cond, Y);
    }

    // after: (X ⋉_C Y) ⋈_C Y — the semi-join added on top of the same
    // condition, exactly as Calcite builds it with the original condition
    // kept on the outer join.
    @Override
    public RelRN after() {
        return X.join(JoinRelType.SEMI, cond, Y)
                .join(JoinRelType.INNER, cond, Y);
    }
}
