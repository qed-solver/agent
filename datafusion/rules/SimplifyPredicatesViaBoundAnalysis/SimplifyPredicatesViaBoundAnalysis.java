package org.qed.RRuleInstances;

import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RuleBuilder;
import org.qed.RexRN;
import kala.collection.Seq;

// SCOPE: PARTIAL — only the same-direction bound-reduction branch: a conjunction of two lower bounds on one column keeps the larger (e.g. x > 5 AND x > 6 to x > 6), omitting the empty-range-to-false, equality subsumption and !=-drop branches
public record SimplifyPredicatesViaBoundAnalysis() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN x = source.field(0);

    static RexRN literal(int v) {
        return new RexRN() {
            @Override
            public RexNode semantics() {
                return RuleBuilder.create().literal(v);
            }
        };
    }

    static final RexRN five = literal(5);
    static final RexRN six = literal(6);

    static final RexRN xGtFive = new RexRN.Pred(SqlStdOperatorTable.GREATER_THAN, Seq.of(x, five));
    static final RexRN xGtSix = new RexRN.Pred(SqlStdOperatorTable.GREATER_THAN, Seq.of(x, six));

    @Override
    public RelRN before() {
        return source.filter(RexRN.and(xGtFive, xGtSix));
    }

    @Override
    public RelRN after() {
        return source.filter(xGtSix);
    }
}
