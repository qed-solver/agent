package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the aggregate's constant group key is a literal column emitted by a projection directly below the aggregate, not deduced from pulled-up predicates over an arbitrary input.
public record AggregateProjectPullUpConstants() implements RRule {
    static final RelRN source = RelRN.scan("S", "S_Type");
    // Input to the aggregate: col 0 = constant literal, col 1 = non-constant.
    static final RexRN constant = RexRN.trueLiteral();
    static final RelRN input = source.project(Seq.of(constant, source.field(0)));

    static final RexRN key0 = input.field(0); // constant
    static final RexRN key1 = input.field(1); // non-constant
    static final RelRN.AggCall f = key1.aggCall("f");

    // Before: Aggregate(input, GROUP BY (key0, key1), f(key1)).
    static final RelRN beforeAgg = new RelRN.Aggregate(
            input,
            Seq.of(key0, key1),
            Seq.of(f));

    // After: reduced = Aggregate(input, GROUP BY (key1), f(key1)); re-emit the constant.
    static final RelRN reduced = new RelRN.Aggregate(
            input,
            Seq.of(key1),
            Seq.of(f));
    static final RelRN afterAgg = reduced.project(Seq.of(
            constant,
            reduced.field(0),
            reduced.field(1)));

    @Override
    public RelRN before() {
        return beforeAgg;
    }

    @Override
    public RelRN after() {
        return afterAgg;
    }
}