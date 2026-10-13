package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// Spark SimplifyCasts, case: Cast(e, dt) -> e if e.dataType == dt (cast to the same type is identity).
// The cast is modeled as the uninterpreted projection cast_T: T -> T; correctness of this
// rewrite requires cast_T to be the identity function, which QED does not model for casts.
public record SimplifyCasts() implements RRule {
    static final RelRN S = RelRN.scan("S", RexRN.varType("T", false), false);
    static final RexRN a = S.field(0);
    static final RexRN castT = new RexRN.Proj(
            RuleBuilder.create().genericProjectionOp("cast_T", RexRN.varType("T", false)), Seq.of(a));

    @Override
    public RelRN before() {
        return S.project(castT);
    }

    @Override
    public RelRN after() {
        return S.project(a);
    }
}
