package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

import kala.collection.Seq;

// SCOPE: PARTIAL — A is restricted to a single uninterpreted predicate (no multi-column expressions).
public record OrNotSelfTautology() implements RRule {
    static final RelRN source = RelRN.scan("Source", RexRN.varType("Source_Type", false), false);
    static final RexRN a = source.pred(RuleBuilder.create().genericPredicateOp("A", false));

    @Override
    public RelRN before() {
        return source.filter(new RexRN.Or(Seq.of(a, new RexRN.Not(a))));
    }

    @Override
    public RelRN after() {
        return source.filter(RexRN.trueLiteral());
    }
}
