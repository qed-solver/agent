// SCOPE: PARTIAL — single-column L and R, one uninterpreted correlated Select filter and one uninterpreted projection of the R column, with the LeftJoinApply ON clause omitted
package org.qed.RRuleInstances;

import com.google.common.collect.ImmutableSet;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.rel.logical.LogicalCorrelate;
import org.apache.calcite.rel.logical.LogicalFilter;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.util.ImmutableBitSet;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

public record TryDecorrelateProjectSelect() implements RRule {
    static final RelRN left = RelRN.scan("L", RexRN.varType("K", true), false);
    static final RelRN right = RelRN.scan("R", RexRN.varType("K", true), false);

    private record Shape(RelRN l, RelRN r, boolean hoisted) implements RelRN {
        @Override
        public RelNode semantics() {
            var b = RuleBuilder.create();
            var rb = b.getRexBuilder();
            var filterOp = b.genericPredicateOp("select", true);
            var projOp = b.genericProjectionOp("proj", RexRN.varType("P", true));
            RelNode ln = l.semantics();
            RelNode rn = r.semantics();
            RexNode rCol = rb.makeInputRef(rn, 0);
            if (!hoisted) {
                var corrId = b.getCluster().createCorrel();
                var corrVar = rb.makeCorrel(ln.getRowType(), corrId);
                RexNode cond = rb.makeCall(filterOp, rb.makeFieldAccess(corrVar, 0), rCol);
                RelNode selected = LogicalFilter.create(rn, cond);
                RelNode projected = b.push(selected).project(rb.makeCall(projOp, b.field(0))).build();
                return LogicalCorrelate.create(ln, projected, java.util.List.of(), corrId,
                        ImmutableBitSet.range(ln.getRowType().getFieldCount()), JoinRelType.LEFT);
            }
            RelNode widened = b.push(rn).project(rb.makeCall(projOp, b.field(0)), b.field(0)).build();
            RexNode on = rb.makeCall(filterOp, rb.makeInputRef(ln, 0),
                    rb.makeInputRef(widened.getRowType().getFieldList().get(1).getType(), ln.getRowType().getFieldCount() + 1));
            b.push(ln).push(widened).join(JoinRelType.LEFT, on);
            return b.project(b.field(0), b.field(1)).build();
        }
    }

    @Override
    public RelRN before() {
        return new Shape(left, right, false);
    }

    @Override
    public RelRN after() {
        return new Shape(left, right, true);
    }
}
