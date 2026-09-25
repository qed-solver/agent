package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: FULL
public record ConvertJSONSubscriptToFetchValue() implements RRule {
    static final RelRN rows = RelRN.scanMany("Rows", Seq.of(
            RexRN.varType("Input_Type", true),
            RexRN.varType("Index_Type", true)));
    static final RexRN input = rows.field(0);
    static final RexRN index = rows.field(1);

    @Override
    public RelRN before() {
        return rows.filter(input.pred("isjson")).project("indirection", "Result_Type");
    }

    @Override
    public RelRN after() {
        return rows.filter(input.pred("isjson")).project("fetchval", "Result_Type");
    }
}
