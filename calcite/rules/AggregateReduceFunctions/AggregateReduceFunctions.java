package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — single bare AVG(x) over one group column rewritten to SUM(x)/COUNT(x) via an uninterpreted division; the rule's full set of aggregate-algebra rewrites (nullif guards, STDDEV/VAR/COVAR/REGR identities) is out of reach
public record AggregateReduceFunctions() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN key = source.groupBy("g");

    @Override
    public RelRN before() {
        return new RelRN.Aggregate(source, Seq.of(key), Seq.of(source.aggCall("AVG")));
    }

    @Override
    public RelRN after() {
        var rhsAgg = new RelRN.Aggregate(source, Seq.of(key), Seq.of(source.aggCall("SUM"), source.aggCall("COUNT")));
        return rhsAgg.project(
            new RexRN.Proj(
                RuleBuilder.create().genericProjectionOp("div", RexRN.varType("res_type", true)),
                Seq.of(rhsAgg.field(1), rhsAgg.field(2))
            )
        );
    }
}
