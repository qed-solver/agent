package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import kala.collection.Seq;

// SCOPE: FULL
public record EliminateCoalesce() implements RRule {

    static final RelRN source = RelRN.scan("Source", "Source_Type");
    // e : an arbitrary uninterpreted scalar expression over the source row.
    static final RexRN e = source.field(0).proj("item", "Item_Type");

    @Override
    public RelRN before() {
        // Coalesce[e]  (single operand)
        return source.project(new RexRN.Coalesce(Seq.of(e)));
    }

    @Override
    public RelRN after() {
        // e
        return source.project(e);
    }
}
