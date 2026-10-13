package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: FULL
public record FilterToCalc() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN cond = source.pred("cond");

    @Override
    public RelRN before() {
        return source.filter(cond);
    }

    @Override
    public RelRN after() {
        // The Calc that FilterToCalcRule builds: the filter's condition plus an
        // identity projection (RexProgramBuilder.addIdentity + addCondition),
        // expressed here as filter composed with the identity project, the same
        // convention used for Calcs in CalcMerge/CalcRemove.
        return source.filter(cond).project(source.fields());
    }
}
