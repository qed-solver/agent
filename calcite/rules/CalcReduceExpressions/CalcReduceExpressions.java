package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RelType;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — the Calc's condition is the constant FALSE literal (the branch where constant reduction of the condition yields a constant false, so the whole Calc is replaced by an empty relation with the Calc's output row type); general constant folding of projections/conditions is not modelable with uninterpreted symbols
public record CalcReduceExpressions() implements RRule {
    // Two-column input relation; the Calc projects two uninterpreted expressions.
    static final RelRN source = RelRN.scanMany("S", Seq.of(
            RexRN.varType("X_Type", true),
            RexRN.varType("Y_Type", true)));

    static final RexRN x = source.field(0);
    static final RexRN y = source.field(1);

    // Uninterpreted projection expressions E1(x, y), E2(x) — the Calc's project list.
    static final SqlOperator e1Op =
            RuleBuilder.create().genericProjectionOp("E1", new RelType.VarType("E1_Type", true));
    static final SqlOperator e2Op =
            RuleBuilder.create().genericProjectionOp("E2", new RelType.VarType("E2_Type", true));

    static final RexRN e1 = new RexRN.Proj(e1Op, Seq.of(x, y));
    static final RexRN e2 = new RexRN.Proj(e2Op, Seq.of(x));

    // Before: Calc(R, condition = FALSE, project = [E1(x,y), E2(x)]),
    // modeled as the equivalent filter-then-project.
    @Override
    public RelRN before() {
        return source.filter(RexRN.falseLiteral()).project(Seq.of(e1, e2));
    }

    // After: empty relation with the Calc's output row type (E1_Type, E2_Type),
    // mirroring createEmptyRelOrEquivalent (builder.push(calc).empty()).
    @Override
    public RelRN after() {
        return source.project(Seq.of(e1, e2)).empty();
    }
}