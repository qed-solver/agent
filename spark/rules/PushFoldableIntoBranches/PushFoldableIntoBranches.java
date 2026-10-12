package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RRule;
import org.qed.RuleBuilder;

// SCOPE: FULL
public record PushFoldableIntoBranches() implements RRule {

    // Source has two value columns (the If branches t and e, same type) and
    // the rule is purely scalar: f(if(c, t, e))  ->  if(c, f(t), f(e)),
    // with c a boolean predicate over the row and f a scalar (projection) op.
    static final RelRN source = RelRN.scanMany(
        "Source", Seq.of(RexRN.varType("Val_Type", true), RexRN.varType("Val_Type", true)));

    static final RexRN t = source.field(0);
    static final RexRN e = source.field(1);
    static final RexRN c = source.pred("c");

    static final SqlOperator fOp =
        RuleBuilder.create().genericProjectionOp("f", RexRN.varType("Val_Type", true));
    static final SqlOperator ifOp =
        RuleBuilder.create().genericProjectionOp("if", RexRN.varType("Val_Type", true));

    @Override
    public RelRN before() {
        RexRN ifTerm = new RexRN.Proj(ifOp, Seq.of(c, t, e));      // if(c, t, e)
        RexRN expr = new RexRN.Proj(fOp, Seq.of(ifTerm));          // f(if(c, t, e))
        return source.project(expr);
    }

    @Override
    public RelRN after() {
        RexRN ft = new RexRN.Proj(fOp, Seq.of(t));                 // f(t)
        RexRN fe = new RexRN.Proj(fOp, Seq.of(e));                 // f(e)
        RexRN expr = new RexRN.Proj(ifOp, Seq.of(c, ft, fe));      // if(c, f(t), f(e))
        return source.project(expr);
    }
}