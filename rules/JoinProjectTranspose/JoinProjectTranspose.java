package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RelType;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — the rule's default non-outer config: an inner join whose two inputs are projects, each per-side projection a single uninterpreted expression, with the join condition an uninterpreted predicate over the projected row
public record JoinProjectTranspose() implements RRule {
    // Two-column raw sides.
    static final RelRN left = RelRN.scanMany("L", Seq.of(
            new RelType.VarType("L0_Type", true),
            new RelType.VarType("L1_Type", true)));
    static final RelRN right = RelRN.scanMany("R", Seq.of(
            new RelType.VarType("R0_Type", true),
            new RelType.VarType("R1_Type", true)));

    // Shared uninterpreted symbols: the per-side projections TL (on L's two
    // columns, output type PL_Type) and TR (on R's two columns, output type
    // PR_Type), and the join condition C over the projected row. Reusing the
    // same name in before() and after() tells QED the occurrences are the
    // same symbol.
    static final SqlOperator tlOp =
            RuleBuilder.create().genericProjectionOp("TL", new RelType.VarType("PL_Type", true));
    static final SqlOperator trOp =
            RuleBuilder.create().genericProjectionOp("TR", new RelType.VarType("PR_Type", true));
    static final SqlOperator cOp = RuleBuilder.create().genericPredicateOp("C", true);

    // Before: Join(C, Project(TL, L), Project(TR, R)) — join row is (PL_Type, PR_Type).
    static final RelRN leftProject =
            left.project(new RexRN.Proj(tlOp, Seq.of(left.field(0), left.field(1))));
    static final RelRN rightProject =
            right.project(new RexRN.Proj(trOp, Seq.of(right.field(0), right.field(1))));
    static final RexRN pl = leftProject.joinField(0, rightProject);
    static final RexRN pr = leftProject.joinField(1, rightProject);
    static final RexRN condBefore = new RexRN.Pred(cOp, Seq.of(pl, pr));

    // After: Project(TL, TR) over Join(C', L, R), where C' is the condition
    // expanded through the pulled-up projections: C(TL(l0, l1), TR(r0, r1)).
    // Raw join row: 0,1 = L's columns; 2,3 = R's columns.
    static final RexRN l0 = left.joinField(0, right);
    static final RexRN l1 = left.joinField(1, right);
    static final RexRN r0 = left.joinField(2, right);
    static final RexRN r1 = left.joinField(3, right);
    static final RexRN tlAfter = new RexRN.Proj(tlOp, Seq.of(l0, l1));
    static final RexRN trAfter = new RexRN.Proj(trOp, Seq.of(r0, r1));
    static final RexRN condAfter = new RexRN.Pred(cOp, Seq.of(tlAfter, trAfter));

    @Override
    public RelRN before() {
        return leftProject.join(JoinRelType.INNER, condBefore, rightProject);
    }

    @Override
    public RelRN after() {
        return left.join(JoinRelType.INNER, condAfter, right)
                .project(Seq.of(tlAfter, trAfter));
    }
}
