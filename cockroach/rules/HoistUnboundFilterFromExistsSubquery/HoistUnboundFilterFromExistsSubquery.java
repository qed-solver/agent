package org.qed.RRuleInstances;

import com.google.common.collect.ImmutableSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.logical.LogicalFilter;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexSubQuery;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — an EXISTS filter over a subquery whose filter has exactly one outer-only conjunct (an uninterpreted predicate of the first outer column) and one inner-only conjunct, over single-column relations
public record HoistUnboundFilterFromExistsSubquery() implements RRule {
    static final RelRN input = RelRN.scan("Input", "V");
    static final RelRN inner = RelRN.scan("Inner", "W");

    private record ExistsSelect(RelRN outer, RelRN sub, boolean hoisted) implements RelRN {
        @Override
        public RelNode semantics() {
            var b = RuleBuilder.create();
            var rb = b.getRexBuilder();
            RelNode l = outer.semantics();
            RelNode r = sub.semantics();
            var unboundOp = b.genericPredicateOp("unbound", true);
            var boundOp = b.genericPredicateOp("bound", true);
            var corrId = b.getCluster().createCorrel();
            var corrVar = rb.makeCorrel(l.getRowType(), corrId);
            RexNode unbound = rb.makeCall(unboundOp, rb.makeFieldAccess(corrVar, 0));
            RexNode bound = rb.makeCall(boundOp, rb.makeInputRef(r, 0));
            if (!hoisted) {
                RexNode both = rb.makeCall(SqlStdOperatorTable.AND, unbound, bound);
                RexNode ex = RexSubQuery.exists(LogicalFilter.create(r, both));
                return LogicalFilter.create(l, ex, ImmutableSet.of(corrId));
            }
            RexNode ex = RexSubQuery.exists(LogicalFilter.create(r, bound));
            RexNode outerUnbound = rb.makeCall(unboundOp, rb.makeInputRef(l, 0));
            return LogicalFilter.create(l, rb.makeCall(SqlStdOperatorTable.AND, ex, outerUnbound), ImmutableSet.of(corrId));
        }
    }

    @Override
    public RelRN before() {
        return new ExistsSelect(input, inner, false);
    }

    @Override
    public RelRN after() {
        return new ExistsSelect(input, inner, true);
    }
}
