package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RelType;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — inner join over single-column sides, where the join condition and the top projection are expressed via shared uninterpreted per-side expressions (the decomposition the rule itself performs)
public record ProjectJoinTranspose() implements RRule {
    static final RelRN left = RelRN.scan("L", "L_Type");
    static final RelRN right = RelRN.scan("R", "R_Type");

    // Shared uninterpreted symbols: the per-side pushed-down expressions TL/TR,
    // the join condition C over the projected row, and the top projection G.
    static final SqlOperator tlOp =
            RuleBuilder.create().genericProjectionOp("TL", new RelType.VarType("P1_Type", true));
    static final SqlOperator trOp =
            RuleBuilder.create().genericProjectionOp("TR", new RelType.VarType("P2_Type", true));
    static final SqlOperator cOp =
            RuleBuilder.create().genericPredicateOp("C", true);
    static final SqlOperator gOp =
            RuleBuilder.create().genericProjectionOp("G", new RelType.VarType("G_Type", true));

    // In the before join row (L_Type, R_Type): C(TL(l), TR(r)) and G(TL(l), TR(r)).
    static final RexRN lRef = left.joinField(0, right);
    static final RexRN rRef = left.joinField(1, right);
    static final RexRN tlBefore = new RexRN.Proj(tlOp, Seq.of(lRef));
    static final RexRN trBefore = new RexRN.Proj(trOp, Seq.of(rRef));
    static final RexRN condBefore = new RexRN.Pred(cOp, Seq.of(tlBefore, trBefore));
    static final RexRN topBefore = new RexRN.Proj(gOp, Seq.of(tlBefore, trBefore));

    // Child projections pushing TL/TR below the join.
    static final RelRN leftProject =
            left.project(new RexRN.Proj(tlOp, Seq.of(left.field(0))));
    static final RelRN rightProject =
            right.project(new RexRN.Proj(trOp, Seq.of(right.field(0))));

    // After the transpose the join row is (P1_Type, P2_Type), so the same
    // uninterpreted C and G apply directly to the projected columns.
    static final RexRN p1 = leftProject.joinField(0, rightProject);
    static final RexRN p2 = leftProject.joinField(1, rightProject);
    static final RexRN condAfter = new RexRN.Pred(cOp, Seq.of(p1, p2));
    static final RexRN topAfter = new RexRN.Proj(gOp, Seq.of(p1, p2));

    @Override
    public RelRN before() {
        return left.join(JoinRelType.INNER, condBefore, right).project(topBefore);
    }

    @Override
    public RelRN after() {
        return leftProject.join(JoinRelType.INNER, condAfter, rightProject).project(topAfter);
    }
}
