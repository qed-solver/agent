// SCOPE: PARTIAL — the source rule's family covers LEFT/RIGHT/FULL joins of arbitrary width where one side is the empty relation; this encoding pins the LEFT join with an empty right side and a fixed two-column shape for both sides.
package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.rex.RexNode;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RuleBuilder;

/**
 * Port of the "empty optional join side" branches of DataFusion's
 * PropagateEmptyRelation (datafusion/optimizer/src/propagate_empty_relation.rs,
 * lines 75-175): when one side of an outer join is an EmptyRelation
 * (produce_one_row = false), the join is replaced by a Projection that
 * passes the surviving side's columns through and emits
 * CAST(NULL AS &lt;type&gt;) for each column of the empty side
 * (build_null_padded_projection).
 *
 * The representative member of that family is the LEFT join with an empty
 * right side: every left row survives, null-extended over the right
 * columns, and the join condition can never match (no right rows exist).
 */
public record EmptyOptionalJoinSideToNullPadded() implements RRule {
    static final RelType.VarType l0 = new RelType.VarType("Left_Type0", true);
    static final RelType.VarType l1 = new RelType.VarType("Left_Type1", true);
    static final RelType.VarType r0 = new RelType.VarType("Right_Type0", true);
    static final RelType.VarType r1 = new RelType.VarType("Right_Type1", true);

    static final RelRN left = RelRN.scanMany("Left", Seq.of(l0, l1));
    // The right input is the empty relation: zero rows of the right side's
    // row type (DataFusion's EmptyRelation { produce_one_row: false }).
    static final RelRN rightEmpty = RelRN.scanMany("Right", Seq.of(r0, r1)).empty();

    // Typed NULL literals standing in for each right column:
    // CAST(NULL AS Right_Type_i), matching the join's null-extension.
    static final RexRN nullR0 = new RexRN() {
        @Override
        public RexNode semantics() {
            var b = RuleBuilder.create();
            return b.getRexBuilder().makeNullLiteral(r0);
        }
    };
    static final RexRN nullR1 = new RexRN() {
        @Override
        public RexNode semantics() {
            var b = RuleBuilder.create();
            return b.getRexBuilder().makeNullLiteral(r1);
        }
    };

    @Override
    public RelRN before() {
        // LeftJoin(Left, EmptyRelation(Right)) ON <uninterpreted>: the
        // condition can never match (the right side has no rows), so every
        // left row is emitted null-extended over the right columns.
        return left.join(JoinRelType.LEFT, "join", rightEmpty);
    }

    @Override
    public RelRN after() {
        // build_null_padded_projection(Left, join_schema, ...): pass the
        // left columns through, emit a typed NULL for each right column.
        return left.project(Seq.of(
                left.field(0),
                left.field(1),
                nullR0,
                nullR1
        ));
    }
}