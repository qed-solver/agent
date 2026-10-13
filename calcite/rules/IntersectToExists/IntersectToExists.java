package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RRule;

// SCOPE: PARTIAL — the 2-input, single-column instance in which both inputs share one row type, so the rule's type-unification casts are identity (the general n-way rule is obtained by repeated application of this binary step)
public record IntersectToExists() implements RRule {
    static final RelRN a = RelRN.scan("A", "T");
    static final RelRN b = RelRN.scan("B", "T");

    @Override
    public RelRN before() {
        // INTERSECT (set semantics, all = false)
        return a.intersect(false, b);
    }

    @Override
    public RelRN after() {
        // Filter_A( EXISTS b IN B . b IS NOT DISTINCT FROM a ) — modeled as the
        // decorrelated form: a left-semi correlate of A with B on IS NOT DISTINCT
        // FROM — then DISTINCT (the rule's final builder.distinct() = group-by-all
        // aggregate with no agg calls).
        RelRN exists = a.correlate(JoinRelType.SEMI,
                SqlStdOperatorTable.IS_NOT_DISTINCT_FROM, b);
        return new RelRN.Aggregate(exists, Seq.of(exists.field(0)), Seq.empty());
    }
}
