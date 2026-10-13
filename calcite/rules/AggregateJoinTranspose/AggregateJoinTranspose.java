package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — only Calcite's DEFAULT (no aggregate-function) config of
// the rule: an Aggregate with no agg calls, whose group set spans both sides
// of an INNER equi-join (one join-key column plus one extra group column per
// side), is pushed below the join (grouping by that side's join key + group
// column) and re-grouped above the rejoined result. The EXTENDED config
// (splittable aggregate functions such as SUM/COUNT pushed through the join)
// is out of scope: QED treats every aggregate call as an uninterpreted
// function of its input bag, so it cannot verify that a function's value
// over the rejoined rows equals a combination of the two per-side partial
// aggregates.
public record AggregateJoinTranspose() implements RRule {
    // Left input: column 0 is the join key (a), column 1 is an extra
    // group-by column (x) not used in the join condition.
    static final RelRN.ScanMany left = RelRN.scanMany("L", Seq.of(
            RexRN.varType("a_type", true), RexRN.varType("x_type", true)));
    // Right input: column 0 is the join key (b), column 1 is an extra
    // group-by column (y) not used in the join condition.
    static final RelRN.ScanMany right = RelRN.scanMany("R", Seq.of(
            RexRN.varType("b_type", true), RexRN.varType("y_type", true)));

    static final SqlOperator joinCond = RuleBuilder.create().genericPredicateOp("join_cond", true);

    // Join condition over just the join-key columns: combined index 0 (a)
    // and combined index 2 (b) — column 1 (x) and column 3 (y) are not part
    // of the join predicate.
    static final RexRN beforeJoinCond = new RexRN.Pred(joinCond, left.joinFields(right, 0, 2));

    static final RelRN join = left.join(JoinRelType.INNER, beforeJoinCond, right);

    // Before: group by (x, y) — combined indices 1 and 3 — over the raw join,
    // with no aggregate calls (a DISTINCT projection of x,y over the join).
    static final RelRN beforeAgg = new RelRN.Aggregate(
            join,
            Seq.of(join.field(1), join.field(3)),
            Seq.empty());

    // After: push a group-by down each side (grouping by that side's join
    // key + its own extra column, since the join key must survive to rejoin
    // on), then rejoin, then re-group by (x, y) above the rejoined result to
    // drop the now-redundant join-key columns.
    static final RelRN leftAgg = new RelRN.Aggregate(
            left,
            Seq.of(left.field(0), left.field(1)),
            Seq.empty());
    static final RelRN rightAgg = new RelRN.Aggregate(
            right,
            Seq.of(right.field(0), right.field(1)),
            Seq.empty());

    static final RexRN afterJoinCond = new RexRN.Pred(joinCond, leftAgg.joinFields(rightAgg, 0, 2));

    static final RelRN rejoin = leftAgg.join(JoinRelType.INNER, afterJoinCond, rightAgg);

    static final RelRN afterAgg = new RelRN.Aggregate(
            rejoin,
            Seq.of(rejoin.field(1), rejoin.field(3)),
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
