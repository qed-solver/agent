// SCOPE: PARTIAL — the uncorrelated top-level EXISTS case only: a plain EXISTS(subquery) predicate in a Filter becomes a LeftSemi join whose right input is the (uncorrelated) subquery plan, keeping its own filter, with a true join condition; the correlated-EXISTS/IN-equality and mark-join paths are out of scope
package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexSubQuery;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

public record DecorrelatePredicateSubquery() implements RRule {
    // EXISTS over a subquery plan, round-tripping as QED's exists-subquery scalar
    record Exists(RelRN input) implements RexRN {
        @Override
        public RexNode semantics() {
            return RexSubQuery.exists(input.semantics());
        }
    }

    static final RelRN outer = RelRN.scan("Outer", "Outer_Type");
    static final RelRN inner = RelRN.scan("Inner", "Inner_Type");
    // The subquery's own (uncorrelated) predicate — it stays inside the
    // subquery plan, which becomes the join's right input unchanged.
    static final RexRN innerPred = inner.pred("inner_pred");

    @Override
    public RelRN before() {
        return outer.filter(new Exists(inner.filter(innerPred)));
    }

    @Override
    public RelRN after() {
        return outer.join(JoinRelType.SEMI, RexRN.trueLiteral(), inner.filter(innerPred));
    }
}
