package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — assumes a single-input extension with a 2-column input (one pass-through column A and one prevent column derived as F(B, witness)), modeled as an inner join with an auxiliary witness relation, and a predicate split into exactly one pushable conjunct P over A and one kept conjunct Q over (A, F(B, t)).
public record PushFilterIntoExtension() implements RRule {
    // S: the extension node's single input. col0 = A (pass-through, non-prevent),
    // col1 = B (source of the prevent column).
    static final RelRN input = RelRN.scanMany("S", Seq.of(
            RexRN.varType("A_Type", true),
            RexRN.varType("B_Type", true)));

    // T: auxiliary witness relation; the extension emits one output row per
    // (input row, witness) pair satisfying J.
    static final RelRN aux = RelRN.scanMany("T", Seq.of(
            RexRN.varType("T_Type", true)));

    // J: relation between an input row and a witness row (the extension's row production)
    static final SqlOperator jOp = RuleBuilder.create().genericPredicateOp("J", true);
    // F: uninterpreted derivation of the prevent column from (B, witness)
    static final SqlOperator fOp = RuleBuilder.create().genericProjectionOp("F", RexRN.varType("B_Type", true));
    // P: conjunct referencing only the pass-through column -> pushed below the extension
    static final SqlOperator pOp = RuleBuilder.create().genericPredicateOp("P", true);
    // Q: conjunct referencing the prevent column -> kept above the extension
    static final SqlOperator qOp = RuleBuilder.create().genericPredicateOp("Q", true);

    /** Extension node on top of an arbitrary input: Join(left, T on J) projected to (A, F(B, t)). */
    static RelRN extension(RelRN left) {
        RelRN j = left.join(JoinRelType.INNER, left.joinPred(jOp, aux), aux);
        RexRN a = left.joinField(0, aux);
        RexRN b = left.joinField(1, aux);
        RexRN t = left.joinField(2, aux);
        return j.project(Seq.of(a, new RexRN.Proj(fOp, Seq.of(b, t))));
    }

    @Override
    public RelRN before() {
        RelRN ext = extension(input);
        RexRN p = new RexRN.Pred(pOp, Seq.of(ext.field(0)));
        RexRN q = new RexRN.Pred(qOp, Seq.of(ext.field(0), ext.field(1)));
        return ext.filter(RexRN.and(p, q));
    }

    @Override
    public RelRN after() {
        RelRN filtered = input.filter(new RexRN.Pred(pOp, Seq.of(input.field(0))));
        RelRN ext = extension(filtered);
        RexRN q = new RexRN.Pred(qOp, Seq.of(ext.field(0), ext.field(1)));
        return ext.filter(q);
    }
}
