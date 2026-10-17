// SCOPE: PARTIAL — only the '=' ANY (IN) set comparison under a Filter, where DataFusion's CASE-over-EXISTS rewrite reduces in filter context to EXISTS(subquery WHERE x = y); the ALL kind, other comparison operators, and non-filter (null-preserving CASE) contexts are not encoded
package org.qed.RRuleInstances;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.logical.LogicalFilter;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexSubQuery;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

public record RewriteSetComparison() implements RRule {
    // `x IN (subquery)`: the outer relation's single field x matched with IN
    // against the subquery's single field — round-trips as QED's in-subquery scalar.
    record In(RelRN outer, RelRN inner) implements RexRN {
        @Override
        public RexNode semantics() {
            var b = RuleBuilder.create();
            var l = outer.semantics();
            var x = b.getRexBuilder().makeInputRef(l.getRowType().getFieldList().get(0).getType(), 0);
            return b.in(inner.semantics(), ImmutableList.of(x));
        }
    }

    // EXISTS(subquery WHERE x = y) with the equality correlating the outer field,
    // as a filter over the outer relation (correlation carried by the filter node).
    private record ExistsEq(RelRN outer, RelRN inner) implements RelRN {
        @Override
        public RelNode semantics() {
            var b = RuleBuilder.create();
            var l = outer.semantics();
            var r = inner.semantics();
            var rb = b.getRexBuilder();
            var corrId = b.getCluster().createCorrel();
            var corrVar = rb.makeCorrel(l.getRowType(), corrId);
            RexNode cond = rb.makeCall(SqlStdOperatorTable.EQUALS, rb.makeFieldAccess(corrVar, 0),
                    rb.makeInputRef(r.getRowType().getFieldList().get(0).getType(), 0));
            RelNode sub = LogicalFilter.create(r, cond);
            return LogicalFilter.create(l, RexSubQuery.exists(sub), ImmutableSet.of(corrId));
        }
    }

    static final RelRN outer = RelRN.scan("Outer", "K_type");
    static final RelRN inner = RelRN.scan("Inner", "K_type");
    // The subquery's own (uncorrelated) predicate, kept inside the subquery plan.
    static final RexRN innerPred = inner.pred("inner_pred");
    static final RelRN subquery = inner.filter(innerPred);

    @Override
    public RelRN before() {
        return outer.filter(new In(outer, subquery));
    }

    @Override
    public RelRN after() {
        return new ExistsEq(outer, subquery);
    }
}
