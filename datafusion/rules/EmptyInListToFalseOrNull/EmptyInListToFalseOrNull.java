package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RRule;
import org.qed.RelRN;

// SCOPE: PARTIAL — encodes only the IN direction (x IN () -> false) as a semi-join of the left side with a zero-row right side collapsing to the empty relation of the left side's schema, because the DSL has no scalar InList predicate or list constant (the NOT IN direction is the sibling anti-join rule).
public record EmptyInListToFalseOrNull() implements RRule {
    static final RelRN left = RelRN.scan("L", "L_Type");
    // The empty IN list — a zero-row relation.
    static final RelRN right = RelRN.scan("R", "R_Type").empty();

    @Override
    public RelRN before() {
        // x IN (empty): no right row can ever match x, so no left row
        // survives — exactly the relational semi-join of L with the empty right.
        return left.join(JoinRelType.SEMI, "cond", right);
    }

    @Override
    public RelRN after() {
        // x IN () folds to false, and Filter(false, L) is the empty
        // relation of L's schema.
        return left.empty();
    }
}
