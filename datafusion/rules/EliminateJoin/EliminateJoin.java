package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;

// SCOPE: PARTIAL — the join is a LEFT join of two one-column scans with an arbitrary (uninterpreted) ON condition, and its only consuming parent is a group-by whose single group key references the preserved left input's column and has no aggregate calls (a DISTINCT on that column), which is the rule's duplicate-insensitive-parent case for dropping a join whose right side contributes no visible columns.
public record EliminateJoin() implements RRule {
    static final RelRN left = RelRN.scan("L", "L_Type");
    static final RelRN right = RelRN.scan("R", "R_Type");

    // Uninterpreted join condition over the full join row (L's column, R's column).
    static final RexRN joinCond = left.joinPred("join_cond", right);

    static final RelRN join = left.join(JoinRelType.LEFT, joinCond, right);

    // Before: group-by over the LEFT join. The single group key references
    // only the preserved left input's column (join.field(0)), so no column
    // of the right side is visible anywhere above the join.
    static final RelRN beforeAgg = new RelRN.Aggregate(
            join,
            Seq.of(join.field(0)),
            Seq.empty());

    // After: the join and its right input are eliminated; the same group-by
    // is taken over the left input alone. A LEFT join emits every left row
    // at least once and never null-extends left columns, and a group-by
    // with no aggregate calls ignores duplicates, so the output is
    // unchanged regardless of how many right rows each left row matched.
    static final RelRN afterAgg = new RelRN.Aggregate(
            left,
            Seq.of(left.field(0)),
            Seq.empty());

    @Override
    public RelRN before() {
        return beforeAgg;
    }

    @Override
    public RelRN after() {
        return afterAgg;
    }
}
