package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RelType;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — the input is fixed at 2 columns and the project list is exactly two uninterpreted expressions over them (the original rule applies to any input arity and any expression list); the Calc's absent condition is represented by the constant-TRUE filter, per this DSL's Calc convention
public record ProjectToCalc() implements RRule {
    // Two-column input relation, standing in for the Project's arbitrary input.
    static final RelRN source = RelRN.scanMany("S", Seq.of(
            RexRN.varType("X_Type", true),
            RexRN.varType("Y_Type", true)));

    static final RexRN x = source.field(0);
    static final RexRN y = source.field(1);

    // The Project's projection list: two uninterpreted expressions E1(x, y), E2(x, y).
    static final SqlOperator e1Op =
            RuleBuilder.create().genericProjectionOp("E1", new RelType.VarType("E1_Type", true));
    static final SqlOperator e2Op =
            RuleBuilder.create().genericProjectionOp("E2", new RelType.VarType("E2_Type", true));

    static final RexRN e1 = new RexRN.Proj(e1Op, Seq.of(x, y));
    static final RexRN e2 = new RexRN.Proj(e2Op, Seq.of(x, y));

    // Before: Project([E1, E2], Source).
    @Override
    public RelRN before() {
        return source.project(Seq.of(e1, e2));
    }

    // After: the LogicalCalc that ProjectToCalcRule builds — a RexProgram with the
    // same projections and no condition, expressed per this DSL's Calc convention
    // (CalcMerge/FilterToCalc) as filter + project, with the absent condition as
    // the constant TRUE literal.
    @Override
    public RelRN after() {
        return source.filter(RexRN.trueLiteral()).project(Seq.of(e1, e2));
    }
}
