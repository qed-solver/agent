package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RRule;
import org.qed.RexRN;

// SCOPE: PARTIAL — INNER join only, single-column equi key that is exactly the right aggregate's sole group key (a pure GROUP BY with no aggregate calls), with a left-field-only top project; the LEFT variant (full join elimination) and multi-key / aggregate-call cases are not modeled.
public record ProjectToSemiJoin() implements RRule {
    static final RelType.VarType T = RexRN.varType("T", true);
    // Left input: L.0 is the join key, L.1 is an extra column carried by the top project.
    static final RelRN left = RelRN.scanMany("L", Seq.of(T, T));
    // R is the pre-aggregate right input: R.0 is a non-key column, R.1 is the group/join key.
    static final RelRN rawRight = RelRN.scanMany("R", Seq.of(T, T));
    // Right child of the join: pure GROUP BY over R.1 (no aggregate calls).
    static final RelRN agg = new RelRN.Aggregate(rawRight, Seq.of(rawRight.field(1)), Seq.empty());

    // INNER join condition: L.0 = agg.0 (agg.0 is the group key R.1).
    // Join output: L.0(0), L.1(1), agg.0(2).
    static final RexRN joinCond = new RexRN.Pred(SqlStdOperatorTable.EQUALS,
            Seq.of(left.joinField(0, agg), left.joinField(2, agg)));

    // Semi-join condition remapped onto the pre-aggregate input: L.0 = R.1.
    // Semi-join output: L.0(0), L.1(1), R.0(2), R.1(3).
    static final RexRN semiCond = new RexRN.Pred(SqlStdOperatorTable.EQUALS,
            Seq.of(left.joinField(0, rawRight), left.joinField(3, rawRight)));

    @Override
    public RelRN before() {
        RelRN innerJoin = left.join(JoinRelType.INNER, joinCond, agg);
        // Top project keeps only the left fields (drops agg.0), permuted L.1, L.0.
        return innerJoin.project(Seq.of(innerJoin.field(1), innerJoin.field(0)));
    }

    @Override
    public RelRN after() {
        RelRN semiJoin = left.join(JoinRelType.SEMI, semiCond, rawRight);
        // Same left-field project (permuted L.1, L.0) over the semi-join output.
        return semiJoin.project(Seq.of(semiJoin.field(1), semiJoin.field(0)));
    }
}
