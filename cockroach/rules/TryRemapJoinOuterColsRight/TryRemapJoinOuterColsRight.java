// SCOPE: PARTIAL — INNER apply join only, one single-column L (x) and a two-column R (a, b), a single equality outer-column reference a = x in the right Select, and a single ON equality b = x
package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.rel.logical.LogicalCorrelate;
import org.apache.calcite.rel.logical.LogicalFilter;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.util.ImmutableBitSet;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

public record TryRemapJoinOuterColsRight() implements RRule {
    static final RelRN left = RelRN.scan("L", RexRN.varType("K", true), false);
    static final RelRN right = RelRN.scanMany("R", Seq.of(RexRN.varType("K", true), RexRN.varType("K", true)));

    private record Apply(RelRN l, RelRN r, boolean remapped) implements RelRN {
        @Override
        public RelNode semantics() {
            var b = RuleBuilder.create();
            var rb = b.getRexBuilder();
            RelNode ln = l.semantics();
            RelNode rn = r.semantics();
            var corrId = b.getCluster().createCorrel();
            var corrVar = rb.makeCorrel(ln.getRowType(), corrId);
            RexNode a = rb.makeInputRef(rn, 0);
            RexNode bCol = rb.makeInputRef(rn, 1);
            RexNode rightCond = remapped
                    ? rb.makeCall(SqlStdOperatorTable.EQUALS, a, bCol)
                    : rb.makeCall(SqlStdOperatorTable.EQUALS, a, rb.makeFieldAccess(corrVar, 0));
            RelNode apply = LogicalCorrelate.create(ln, LogicalFilter.create(rn, rightCond), java.util.List.of(), corrId,
                    ImmutableBitSet.of(0), JoinRelType.INNER);
            RexNode on = rb.makeCall(SqlStdOperatorTable.EQUALS,
                    rb.makeInputRef(apply.getRowType().getFieldList().get(0).getType(), 0),
                    rb.makeInputRef(apply.getRowType().getFieldList().get(2).getType(), 2));
            return b.push(apply).filter(on).build();
        }
    }

    @Override
    public RelRN before() {
        return new Apply(left, right, false);
    }

    @Override
    public RelRN after() {
        return new Apply(left, right, true);
    }
}
