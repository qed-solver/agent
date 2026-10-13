package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — one bare-column group key, one redundant group expression that is a single uninterpreted (deterministic) function of that key, one non-distinct aggregate over a second input column; the general rule (arbitrary deterministic expressions over constants and any subset of the bare group columns) reduces to the same shape since QED's uninterpreted scalar symbols stand in for those expressions.
public record EliminateGroupByConstant() implements RRule {
    // Input: two columns — k is the bare grouping column, a is the aggregate argument.
    static final RelRN base = RelRN.scanMany("S", Seq.of(
            RexRN.varType("K_Type", true),
            RexRN.varType("A_Type", true)));

    static final RexRN k = base.field(0);
    static final RexRN a = base.field(1);

    // The redundant group expression: an uninterpreted scalar function h of the
    // bare group key k (models any deterministic expression built from constants
    // and columns already present as bare references in the GROUP BY — including
    // pure constants, since an uninterpreted function may ignore its argument).
    static final RexRN hExpr = k.proj("h", "H_Type");

    // One uninterpreted aggregate over the non-grouped column a.
    static final RelRN.AggCall fCall = new RelRN.AggCall(
            "f", false, RexRN.varType("F_Type", true), Seq.of(a));

    @Override
    public RelRN before() {
        // Aggregate over input, group set [k, h(k)], aggregate f(a).
        return new RelRN.Aggregate(base, Seq.of(k, hExpr), Seq.of(fCall));
    }

    @Override
    public RelRN after() {
        // Simplified aggregate: redundant group expression dropped, group set [k].
        RelRN simplified = new RelRN.Aggregate(base, Seq.of(k), Seq.of(fCall));
        // Top projection re-emits the original output schema [k, h(k), f(a)],
        // re-deriving the redundant expression from the remaining key column.
        return new RelRN.ProjectMany(
                Seq.of(simplified.field(0), simplified.field(0).proj("h", "H_Type"), simplified.field(1)),
                simplified);
    }
}
