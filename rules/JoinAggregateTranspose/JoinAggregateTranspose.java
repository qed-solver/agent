package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — right side is a single-column table unique on its column.
public record JoinAggregateTranspose() implements RRule {
    // Left input: column 0 is the join/group key (g), column 1 is the
    // aggregate operand (v).
    static final RelRN left = RelRN.scanMany("L", Seq.of(
            RexRN.varType("g_type", true), RexRN.varType("v_type", true)));
    // Right input: single column r, declared unique (a key) so that
    // duplicate right rows cannot double the join input feeding the
    // after-side aggregate — the same uniqueness requirement Calcite's rule
    // imposes on the join key of the right input.
    static final RelRN right = RelRN.scan("R", RexRN.varType("R_Type", true), true);

    // Uninterpreted join predicate P(g, r). It references only the group key
    // g, not the aggregate operand — the precondition that lets the rule push
    // the join below the aggregate.
    static final SqlOperator joinCond = RuleBuilder.create().genericPredicateOp("join_cond", true);

    // BEFORE: (Aggregate over L: group by g, SUM(v)) INNER JOIN R on P(g, r).
    // Aggregate output is (g, sum); join output is (g, sum, r).
    static final RelRN beforeAgg = new RelRN.Aggregate(
            left,
            Seq.of(left.field(0)),
            Seq.of(left.field(1).aggCall("sum")));

    // AFTER: L INNER JOIN R on P(g, r), join output is (g, v, r);
    // aggregate over (g, r) with SUM(v), output (g, r, sum).
    static final RelRN join = left.join(JoinRelType.INNER,
            new RexRN.Pred(joinCond, left.joinFields(right, 0, 2)), right);

    static final RelRN afterAgg = new RelRN.Aggregate(
            join,
            Seq.of(join.field(0), join.field(2)),
            Seq.of(join.field(1).aggCall("sum")));

    @Override
    public RelRN before() {
        return beforeAgg.join(JoinRelType.INNER,
                new RexRN.Pred(joinCond, beforeAgg.joinFields(right, 0, 2)), right);
    }

    @Override
    public RelRN after() {
        // Reorder (g, r, sum) -> (g, sum, r) to match the before-side column order.
        return afterAgg.project(Seq.of(
                afterAgg.field(0), afterAgg.field(2), afterAgg.field(1)));
    }
}
