package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;

// SCOPE: PARTIAL — encodes the AGGREGATE variant of Calcite's RemoveEmptySingleRule: a non-grand-total (grouped) aggregate whose single input is empty collapses to the empty relation of the aggregate's output row type; the project/filter/calc/sort/window variants of the same rule are not covered here.
public record RemoveEmptySingle() implements RRule {
    // Base relation; the "empty" child is an empty Values of this row type.
    static final RelRN source = RelRN.scan("S", "S_Type");
    static final RelRN empty = source.empty();

    // A non-grand-total aggregate (isNotGrandTotal: group key is non-empty)
    // applied directly over the empty input: group key = column 0, plus one
    // aggregate call over column 0.
    static final RelRN beforeAgg = new RelRN.Aggregate(
            empty,
            Seq.of(empty.field(0)),
            Seq.of(empty.field(0).aggCall("f")));

    @Override
    public RelRN before() {
        return beforeAgg;
    }

    @Override
    public RelRN after() {
        // Empty relation of the aggregate's output row type.
        return beforeAgg.empty();
    }
}
