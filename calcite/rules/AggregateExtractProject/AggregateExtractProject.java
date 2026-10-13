package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the aggregate's input is a three-column relation (three one-column scans inner-joined with true conditions), it has one plain-field group key and one plain non-distinct aggregate call over a second plain field, and a third column is unused (neither a group key nor an aggregate argument) so the extracted projection drops it.
public record AggregateExtractProject() implements RRule {
    static final RelRN scanA = RelRN.scan("A", "A_Type");
    static final RelRN scanB = RelRN.scan("B", "B_Type");
    static final RelRN scanC = RelRN.scan("C", "C_Type");
    // Three-column base: col 0 = a (group key), col 1 = b (aggregate arg), col 2 = c (unused).
    static final RelRN base =
            scanA.join(JoinRelType.INNER, RexRN.trueLiteral(), scanB)
                 .join(JoinRelType.INNER, RexRN.trueLiteral(), scanC);

    // Before: Aggregate(R, GROUP BY a, f(b)) over the full three-column input.
    static final RelRN beforeAgg = new RelRN.Aggregate(
            base,
            Seq.of(base.field(0)),
            Seq.of(base.field(1).aggCall("f")));

    // The extracted projection keeps only the used fields (a, b), dropping c.
    static final RelRN project = base.project(Seq.of(
            base.field(0),
            base.field(1)));

    // After: the same aggregate rebuilt over the projected input, where the
    // group key and aggregate argument are now the first two projected columns.
    static final RelRN afterAgg = new RelRN.Aggregate(
            project,
            Seq.of(project.field(0)),
            Seq.of(project.field(1).aggCall("f")));

    @Override
    public RelRN before() {
        return beforeAgg;
    }

    @Override
    public RelRN after() {
        return afterAgg;
    }
}
