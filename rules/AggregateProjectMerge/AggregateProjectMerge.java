package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the aggregate's input is a two-column relation (two one-column scans inner-joined with a true condition), the project below the aggregate is the pure column swap, and the aggregate has one group key and one aggregate call, both over the same plain field reference
public record AggregateProjectMerge() implements RRule {
    static final RelRN scanA = RelRN.scan("A", "A_Type");
    static final RelRN scanB = RelRN.scan("B", "B_Type");
    // Base columns: 0 = a, 1 = b.
    static final RelRN base =
            scanA.join(JoinRelType.INNER, RexRN.trueLiteral(), scanB);
    // Project columns: 0 = b, 1 = a (pure column swap).
    static final RelRN project = base.project(Seq.of(
            base.field(1),
            base.field(0)));
    // Before: group by P.col0 (= b), aggregate f(P.col0) (= f(b)).
    static final RelRN beforeAgg = new RelRN.Aggregate(
            project,
            Seq.of(project.field(0)),
            Seq.of(project.field(0).aggCall("f")));
    // After: group by base.col1 (= b), aggregate f(base.col1) (= f(b)).
    static final RelRN afterAgg = new RelRN.Aggregate(
            base,
            Seq.of(base.field(1)),
            Seq.of(base.field(1).aggCall("f")));

    @Override
    public RelRN before() {
        return beforeAgg;
    }

    @Override
    public RelRN after() {
        return afterAgg;
    }
}
