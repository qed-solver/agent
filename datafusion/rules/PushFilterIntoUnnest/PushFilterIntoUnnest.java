package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — one array column, one non-unnest conjunct, one unnest conjunct, and a single element column
public record PushFilterIntoUnnest() implements RRule {
    // L: the Unnest's input. col0 is the array column being unnested;
    // col1 is a regular (non-unnested) input column.
    static final RelRN left = RelRN.scanMany("L", Seq.of(
            RexRN.varType("Arr_Type", true),
            RexRN.varType("NonUnnest_Type", true)));

    // E: the relation of elements the array column expands to. Unnest's list
    // semantics are uninterpretable to QED, so the Unnest is abstracted as an
    // INNER join of the input with E under the uninterpreted membership
    // predicate M(array, elem); the identity is then proven uniformly for
    // every M.
    static final RelRN elements = RelRN.scan("E", "E_Type");

    // Shared uninterpreted symbols (the same operator instance is used on both
    // sides): M = membership predicate over (array, element);
    // P_NON = the conjunct over the non-unnested input column;
    // P_UNNEST = the conjunct over the unnested element column.
    static final SqlOperator mOp = RuleBuilder.create().genericPredicateOp("M", true);
    static final SqlOperator pNonOp = RuleBuilder.create()
            .genericPredicateOp("P_NON", true);
    static final SqlOperator pUnnestOp = RuleBuilder.create()
            .genericPredicateOp("P_UNNEST", true);

    /** "Unnest of the input's array column (its first column)": the input
     *  joined with the element relation under M(array, elem). */
    static RelRN unnest(RelRN input) {
        int elemOrdinal = input.semantics().getRowType().getFieldCount();
        RexRN cond = new RexRN.Pred(mOp,
                Seq.of(input.joinField(0, elements), input.joinField(elemOrdinal, elements)));
        return input.join(JoinRelType.INNER, cond, elements);
    }

    @Override
    public RelRN before() {
        // Filter(P_NON(non) AND P_UNNEST(elem)) over Unnest(L)
        RelRN joined = unnest(left);
        RexRN pNon = new RexRN.Pred(pNonOp, Seq.of(left.joinField(1, elements)));
        RexRN pUnnest = new RexRN.Pred(pUnnestOp, Seq.of(left.joinField(2, elements)));
        return joined.filter(RexRN.and(pNon, pUnnest));
    }

    @Override
    public RelRN after() {
        // Filter(P_UNNEST(elem)) over Unnest( Filter(P_NON(non)) over L )
        RelRN filtered = left.filter(new RexRN.Pred(pNonOp, Seq.of(left.field(1))));
        RelRN joined = unnest(filtered);
        return joined.filter(new RexRN.Pred(pUnnestOp, Seq.of(filtered.joinField(2, elements))));
    }
}
