package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RexRN;

// SCOPE: FULL
public record NormalizeTupleEquality() implements RRule {
    // The two 3-element tuples of the comparison (a, b, c) = (x, y, z) are
    // represented as six columns of one source relation: fields 0-2 are the
    // left tuple's elements, fields 3-5 the right tuple's elements.
    static final RelType.VarType t = new RelType.VarType("Elem_Type", true);
    static final RelRN source = RelRN.scanMany("Source", Seq.of(t, t, t, t, t, t));

    static final RexRN a = source.field(0);
    static final RexRN b = source.field(1);
    static final RexRN c = source.field(2);
    static final RexRN x = source.field(3);
    static final RexRN y = source.field(4);
    static final RexRN z = source.field(5);

    // (a, b, c) = (x, y, z): one uninterpreted tuple-equality symbol, since
    // QED's value model has no tuple/row type to build a concrete one from.
    static final RexRN tupleEq = source.pred("tuple_eq");

    static final RexRN eqAX = new RexRN.Pred(SqlStdOperatorTable.EQUALS, Seq.of(a, x));
    static final RexRN eqBY = new RexRN.Pred(SqlStdOperatorTable.EQUALS, Seq.of(b, y));
    static final RexRN eqCZ = new RexRN.Pred(SqlStdOperatorTable.EQUALS, Seq.of(c, z));

    @Override
    public RelRN before() {
        return source.filter(tupleEq);
    }

    @Override
    public RelRN after() {
        return source.filter(RexRN.and(eqAX, eqBY, eqCZ));
    }
}
