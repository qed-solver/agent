package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RRule;
import org.qed.RexRN;

public record OptimizeWindowFunctions() implements RRule {
    static final RelType.VarType kT = RexRN.varType("k", false);
    static final RelType.VarType vT = RexRN.varType("v", false);

    // Window input: rows (k, v). k = partition column, v = the first()'d column.
    static final RelRN input = RelRN.scanMany("Input", Seq.of(kT, vT));

    // The window spec is IDENTICAL on both sides (partition by k, non-empty
    // order-by, ROWS BETWEEN UNBOUNDED PRECEDING AND {CURRENT ROW | UNBOUNDED
    // FOLLOWING}); only the window FUNCTION changes: first(v) -> nth_value(v, 1).
    // Under these frame conditions both reduce to "the v of the first-ordered
    // row of the partition" -- a partition-determined value -- so the window
    // result is modeled as an uninterpreted per-partition aggregate over k,
    // joined back to the input on k (precedent: FoldGroupByAndWindow).
    //
    // BEFORE: first(v)  -> uninterpreted aggregate symbol "wfirst" over k.
    static final RelRN winBefore = new RelRN.Aggregate(
            input, Seq.of(input.field(0)), Seq.of(input.field(1).aggCall("wfirst")));
    static final RexRN condBefore = new RexRN.Pred(SqlStdOperatorTable.EQUALS,
            Seq.of(input.joinField(0, winBefore), input.joinField(2, winBefore)));
    static final RelRN joinedBefore = input.join(JoinRelType.INNER, condBefore, winBefore);
    // 0=k, 1=v, 2=winBefore.k, 3=wfirst
    static final RelRN beforeRel = new RelRN.ProjectMany(
            Seq.of(joinedBefore.field(0), joinedBefore.field(1), joinedBefore.field(3)), joinedBefore);

    // AFTER: nth_value(v, 1)  -> a DISTINCT uninterpreted aggregate symbol "wnth" over k.
    static final RelRN winAfter = new RelRN.Aggregate(
            input, Seq.of(input.field(0)), Seq.of(input.field(1).aggCall("wnth")));
    static final RexRN condAfter = new RexRN.Pred(SqlStdOperatorTable.EQUALS,
            Seq.of(input.joinField(0, winAfter), input.joinField(2, winAfter)));
    static final RelRN joinedAfter = input.join(JoinRelType.INNER, condAfter, winAfter);
    // 0=k, 1=v, 2=winAfter.k, 3=wnth
    static final RelRN afterRel = new RelRN.ProjectMany(
            Seq.of(joinedAfter.field(0), joinedAfter.field(1), joinedAfter.field(3)), joinedAfter);

    @Override
    public RelRN before() {
        return beforeRel;
    }

    @Override
    public RelRN after() {
        return afterRel;
    }
}
