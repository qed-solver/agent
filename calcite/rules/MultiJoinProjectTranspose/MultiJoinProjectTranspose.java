package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RelType;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — the LEFT_PROJECT variant of the rule (a two-input join with
// a project on the left input; the Calcite rule also has RIGHT_PROJECT and
// BOTH_PROJECT variants, and its input is a MultiJoin — here modeled as a
// two-column scan per side), with an inner join, the project's expression a
// single uninterpreted expression over the side's two columns, and the join
// condition an uninterpreted predicate over the projected row
public record MultiJoinProjectTranspose() implements RRule {
    // Two-column raw left side (standing in for the left MultiJoin input);
    // the right side of the join is any two-column relation.
    static final RelRN left = RelRN.scanMany("L", Seq.of(
            new RelType.VarType("L0_Type", true),
            new RelType.VarType("L1_Type", true)));
    static final RelRN right = RelRN.scanMany("R", Seq.of(
            new RelType.VarType("R0_Type", true),
            new RelType.VarType("R1_Type", true)));

    // Shared uninterpreted symbols: the left-side projection TL (over the two
    // L columns, output type PL_Type) and the join condition C over the
    // projected row. Reusing the same name in before() and after() tells QED
    // the occurrences are the same symbol.
    static final SqlOperator tlOp =
            RuleBuilder.create().genericProjectionOp("TL", new RelType.VarType("PL_Type", true));
    static final SqlOperator cOp = RuleBuilder.create().genericPredicateOp("C", true);

    // Before: Join(C, Project(TL, L), R) — join row is (PL_Type, R0, R1).
    static final RelRN leftProject =
            left.project(new RexRN.Proj(tlOp, Seq.of(left.field(0), left.field(1))));
    static final RexRN condBefore = new RexRN.Pred(cOp, Seq.of(
            leftProject.joinField(0, right),
            leftProject.joinField(1, right),
            leftProject.joinField(2, right)));

    // After: Project(TL, R's cols) over Join(C', L, R), where C' is the join
    // condition expanded through the pulled-up projection:
    // C(TL(l0, l1), r0, r1). Raw join row: 0,1 = L's columns; 2,3 = R's.
    static final RexRN l0 = left.joinField(0, right);
    static final RexRN l1 = left.joinField(1, right);
    static final RexRN r0 = left.joinField(2, right);
    static final RexRN r1 = left.joinField(3, right);
    static final RexRN tlAfter = new RexRN.Proj(tlOp, Seq.of(l0, l1));
    static final RexRN condAfter = new RexRN.Pred(cOp, Seq.of(tlAfter, r0, r1));

    @Override
    public RelRN before() {
        return leftProject.join(JoinRelType.INNER, condBefore, right);
    }

    @Override
    public RelRN after() {
        return left.join(JoinRelType.INNER, condAfter, right)
                .project(Seq.of(tlAfter, r0, r1));
    }
}