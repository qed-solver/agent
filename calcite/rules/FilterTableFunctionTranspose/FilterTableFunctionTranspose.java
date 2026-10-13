package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — assumes the (single-input, identity 1-to-1 column mapping) table function has a 2-column input row; the original rule admits any equal field count
public record FilterTableFunctionTranspose() implements RRule {
    // S: the table function's single relational input.
    static final RelRN input = RelRN.scanMany("S", Seq.of(
            RexRN.varType("S0_Type", true),
            RexRN.varType("S1_Type", true)));

    // T: auxiliary witness relation. Under the rule's own side conditions
    // (single input, identity 1-to-1 mapping, non-derived, equal field count)
    // the table function is row-preserving: it emits each input row r as a
    // copy of r once per witness t with J(r, t). That is modeled exactly as an
    // INNER join of the input with T on uninterpreted J, projected back to the
    // left fields.
    static final RelRN aux = RelRN.scanMany("T", Seq.of(
            RexRN.varType("T_Type", true)));

    // Shared uninterpreted symbols, reused by name on both sides:
    // J = relation between an input row and a witness row,
    // P = the filter predicate over the whole row.
    static final SqlOperator jOp = RuleBuilder.create().genericPredicateOp("J", true);
    static final SqlOperator pOp = RuleBuilder.create().genericPredicateOp("P", true);

    /** The table-function scan built on top of an arbitrary left input. */
    static RelRN tableFunction(RelRN left) {
        return left.join(JoinRelType.INNER, left.joinPred(jOp, aux), aux)
                .project(Seq.of(left.joinField(0, aux), left.joinField(1, aux)));
    }

    @Override
    public RelRN before() {
        RelRN tfs = tableFunction(input);
        return tfs.filter(new RexRN.Pred(pOp, Seq.of(tfs.field(0), tfs.field(1))));
    }

    @Override
    public RelRN after() {
        RelRN filtered = input.filter(new RexRN.Pred(pOp, Seq.of(input.field(0), input.field(1))));
        return tableFunction(filtered);
    }
}
