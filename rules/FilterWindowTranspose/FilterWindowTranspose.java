package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — the window is modeled as a group-by/aggregate with a single
// partition key and a single aggregate call, the input is an arbitrary
// two-column relation, and the filter is a conjunction of one conjunct over
// the partition key only (the pushed one) and one conjunct over the window
// output row (the remaining one)
public record FilterWindowTranspose() implements RRule {
    // base: an arbitrary two-column relation.
    // col 0 = partition key (k), col 1 = aggregate operand (v).
    static final RelRN base = RelRN.scanMany("B", Seq.of(
            RexRN.varType("K_Type", true),
            RexRN.varType("V_Type", true)));
    static final RexRN k = base.field(0);
    static final RexRN v = base.field(1);

    // The "window": group by the partition key, aggregate f over the operand.
    // Its output row is (k, f).
    static final RelRN agg = new RelRN.Aggregate(base, Seq.of(k), Seq.of(v.aggCall("f")));

    // The pushed conjunct: a predicate over the partition key ONLY —
    // structurally it references no other column, which is exactly the
    // condition Calcite checks (the filter's used columns are contained in
    // the window's partition keys).
    static final RexRN predP = k.pred("P");

    // The remaining conjunct: a predicate over the window output row
    // (partition key, aggregate value).
    static final RexRN predQ = new RexRN.Pred(
            RuleBuilder.create().genericPredicateOp("Q", true),
            Seq.of(agg.field(0), agg.field(1)));

    @Override
    public RelRN before() {
        // Filter(P(k) AND Q(k, f), Window_k f(v))
        return agg.filter(RexRN.and(predP, predQ));
    }

    @Override
    public RelRN after() {
        // Filter(Q(k, f), Window_k f(v) over Filter(P(k), base))
        // P is pushed below the window: because P depends only on the
        // partition key it is constant over each partition, so it prunes
        // whole partitions below exactly as it prunes whole output rows above.
        return new RelRN.Aggregate(base.filter(predP), Seq.of(k), Seq.of(v.aggCall("f")))
                .filter(predQ);
    }
}
