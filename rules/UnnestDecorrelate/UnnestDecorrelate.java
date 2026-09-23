package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — the left subquery is assumed to be the array column plus exactly one extra unused column, and the unnested element relation is single-column
public record UnnestDecorrelate() implements RRule {
    // L: the left input of the INNER correlate. col0 is the array column the
    // uncollect reads through $cor0.ARRAY; col1 is an extra column the outer
    // project does not reference (its presence makes the column drop on the
    // right-hand side a real, non-trivial transformation).
    static final RelRN left = RelRN.scanMany("L", Seq.of(
            RexRN.varType("L_Array_Type", true),
            RexRN.varType("L1_Type", true)));

    // E: the relation of elements emitted by Uncollect for one array value.
    // Uncollect's list semantics are uninterpretable, so the uncollect is
    // abstracted as an INNER join of the input with E under the uninterpreted
    // membership predicate M(array, elem); the identity is then proven
    // uniformly for every M.
    static final RelRN elements = RelRN.scan("E", "E_Type");

    // Shared uninterpreted symbols (the same operator instance is used on both
    // sides): M = membership predicate over (array, element);
    // P = the outer project (composed with the optional inner project), which
    // by the rule's side condition references only uncollect (right-side)
    // columns, hence over the element value alone.
    static final SqlOperator mOp = RuleBuilder.create().genericPredicateOp("M", true);
    static final SqlOperator pOp = RuleBuilder.create()
            .genericProjectionOp("P", RexRN.varType("Result_Type", true));

    /** "Uncollect over the input's array column (its first column)": the input
     *  joined with the element relation under M. */
    static RelRN uncollect(RelRN input) {
        int elemOrdinal = input.semantics().getRowType().getFieldCount();
        RexRN cond = new RexRN.Pred(mOp,
                Seq.of(input.joinField(0, elements), input.joinField(elemOrdinal, elements)));
        return input.join(JoinRelType.INNER, cond, elements);
    }

    @Override
    public RelRN before() {
        // outerProject( innerProject? ( Uncollect( Project($cor0.ARRAY over Values{0}) ) ) )
        // with the correlate (INNER) as a join against the left subquery:
        RelRN joined = uncollect(left);
        return joined.project(new RexRN.Proj(pOp, Seq.of(joined.field(2))));
    }

    @Override
    public RelRN after() {
        // Uncollect( Project( ARRAY_COLUMN ) over LeftSubquery )
        RelRN leftArray = left.project(Seq.of(left.field(0)));
        RelRN joined = uncollect(leftArray);
        return joined.project(new RexRN.Proj(pOp, Seq.of(joined.field(1))));
    }
}
