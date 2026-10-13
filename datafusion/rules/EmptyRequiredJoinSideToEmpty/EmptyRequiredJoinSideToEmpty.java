// SCOPE: PARTIAL — the source rule's family covers INNER/LEFT/RIGHT/SEMI/ANTI/FULL joins whose required side is empty; this encoding pins the INNER join with an empty left side (both sides' shapes fixed to two columns).
package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RRule;

/**
 * Port of the "empty required join side" branches of DataFusion's
 * PropagateEmptyRelation (datafusion/optimizer/src/propagate_empty_relation.rs,
 * lines 75-175): when the side of a join that must contribute rows (the
 * required side) is an EmptyRelation (produce_one_row = false), the whole join
 * collapses to an EmptyRelation carrying the join's schema.
 *
 * Representative member: INNER join with an empty left side. An INNER join
 * requires both sides to produce a matched pair; with zero left rows no pair
 * can ever form, so the result is the empty relation of the join's schema
 * (join.schema = left columns ++ right columns).
 */
public record EmptyRequiredJoinSideToEmpty() implements RRule {
    static final RelType.VarType l0 = new RelType.VarType("Left_Type0", true);
    static final RelType.VarType l1 = new RelType.VarType("Left_Type1", true);
    static final RelType.VarType r0 = new RelType.VarType("Right_Type0", true);
    static final RelType.VarType r1 = new RelType.VarType("Right_Type1", true);

    // The left input is the empty relation: zero rows of the left side's row
    // type (DataFusion's EmptyRelation { produce_one_row: false }).
    static final RelRN leftEmpty = RelRN.scanMany("Left", Seq.of(l0, l1)).empty();
    static final RelRN right = RelRN.scanMany("Right", Seq.of(r0, r1));

    @Override
    public RelRN before() {
        // InnerJoin(EmptyRelation(Left), Right) ON <uninterpreted join cond>.
        return leftEmpty.join(JoinRelType.INNER, "join", right);
    }

    @Override
    public RelRN after() {
        // EmptyRelation { produce_one_row: false, schema: join.schema }:
        // an empty relation of the join's output row type (left ++ right).
        return leftEmpty.join(JoinRelType.INNER, "join", right).empty();
    }
}
