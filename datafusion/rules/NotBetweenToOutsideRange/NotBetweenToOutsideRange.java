package org.qed.RRuleInstances;

import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RexRN;
import kala.collection.Seq;

// SCOPE: FULL
public record NotBetweenToOutsideRange() implements RRule {
    static final RelType.VarType V = new RelType.VarType("V", true);
    static final RelRN source = RelRN.scanMany("Source", Seq.of(V, V, V));
    static final RexRN a = source.field(0);
    static final RexRN b = source.field(1);
    static final RexRN c = source.field(2);

    static final RexRN aGeB = new RexRN.Pred(SqlStdOperatorTable.GREATER_THAN_OR_EQUAL, Seq.of(a, b));
    static final RexRN aLeC = new RexRN.Pred(SqlStdOperatorTable.LESS_THAN_OR_EQUAL, Seq.of(a, c));
    static final RexRN aLtB = new RexRN.Pred(SqlStdOperatorTable.LESS_THAN, Seq.of(a, b));
    static final RexRN aGtC = new RexRN.Pred(SqlStdOperatorTable.GREATER_THAN, Seq.of(a, c));

    @Override
    public RelRN before() {
        return source.filter(new RexRN.Not(new RexRN.And(Seq.of(aGeB, aLeC))));
    }

    @Override
    public RelRN after() {
        return source.filter(new RexRN.Or(Seq.of(aLtB, aGtC)));
    }
}
