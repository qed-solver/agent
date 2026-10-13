package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RexRN;

// SCOPE: PARTIAL — the IN list is fixed to exactly two elements (B, C) rather than an arbitrary length
public record NotInToNotIn() implements RRule {
    // V: the uninterpreted type shared by A and every element of the IN list.
    static final RelType.VarType V = new RelType.VarType("V", true);
    static final RelRN source = RelRN.scanMany("Source", Seq.of(V, V, V));

    // A: the value being tested; B, C: the two elements of the InList's list.
    static final RexRN a = source.field(0);
    static final RexRN b = source.field(1);
    static final RexRN c = source.field(2);

    // The two equality conjuncts that the InList expansion is built from.
    static final RexRN aEqB = new RexRN.Pred(SqlStdOperatorTable.EQUALS, Seq.of(a, b));
    static final RexRN aEqC = new RexRN.Pred(SqlStdOperatorTable.EQUALS, Seq.of(a, c));

    @Override
    public RelRN before() {
        // NOT (A IN (B, C)): the non-negated InList, expanded definitionally to the
        // disjunction of equalities with the outer NOT left in place.
        return source.filter(new RexRN.Not(new RexRN.Or(Seq.of(aEqB, aEqC))));
    }

    @Override
    public RelRN after() {
        // A NOT IN (B, C): negate_clause flips the InList's negated flag to true; the
        // negated InList's canonical form is the De Morgan expansion — the AND of the
        // individually negated equalities.
        return source.filter(RexRN.and(new RexRN.Not(aEqB), new RexRN.Not(aEqC)));
    }
}
