package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — INNER-join variant with a single non-nullable equi-join key column that is exactly the aggregate's group key, and the project uses only left-side columns
public record ProjectToSemiJoin() implements RRule {
    static final RelType.VarType T = RexRN.varType("T", false); // non-nullable join/group key
    static final RelType.VarType X = RexRN.varType("X", true); // left payload
    static final RelType.VarType U = RexRN.varType("U", true); // aggregate input payload
    static final RelType.VarType W = RexRN.varType("W", true); // aggregate call result

    static final RelRN left = RelRN.scanMany("L", Seq.of(T, X));
    static final RelRN aggIn = RelRN.scanMany("A", Seq.of(T, U));

    // A grouped by its key column (A.col 0), with one uninterpreted aggregate call over the payload
    static final RelRN agg = new RelRN.Aggregate(
            aggIn,
            Seq.of(aggIn.field(0)),
            Seq.of(new RelRN.AggCall(
                    "cnt",
                    RuleBuilder.create().genericAggregateOp("cnt", W),
                    false,
                    W,
                    Seq.of(aggIn.field(1)))));

    // INNER join condition: L.key = A.key  (L.col 0 / agg.col 0, agg.col 0 is the group key)
    static final RexRN joinCond = new RexRN.Pred(SqlStdOperatorTable.EQUALS,
            Seq.of(left.joinField(0, agg), agg.field(0)));

    // SEMI join condition: L.key = A.key (on the aggregate's input)
    static final RexRN semiCond = new RexRN.Pred(SqlStdOperatorTable.EQUALS,
            Seq.of(left.joinField(0, aggIn), aggIn.field(0)));

    @Override
    public RelRN before() {
        RelRN join = left.join(JoinRelType.INNER, joinCond, agg);
        // project keeps only the left-side columns (join col 0, col 1)
        return join.project(new RexRN.Proj(
                RuleBuilder.create().genericProjectionOp("p", T),
                Seq.of(join.field(0), join.field(1))));
    }

    @Override
    public RelRN after() {
        RelRN semi = left.join(JoinRelType.SEMI, semiCond, aggIn);
        // same projection symbol over the semi-join's (left-only) columns
        return semi.project(new RexRN.Proj(
                RuleBuilder.create().genericProjectionOp("p", T),
                Seq.of(semi.field(0), semi.field(1))));
    }
}
