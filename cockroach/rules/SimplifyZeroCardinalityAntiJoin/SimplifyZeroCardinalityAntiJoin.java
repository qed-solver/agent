package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RelType;

public record SimplifyZeroCardinalityAntiJoin() implements RRule {
    static final RelType t1 = RelType.table("T");
    static final RelType t2 = RelType.rel("T");
    static final RelType t3 = RelType.uninterpreted("T");
    static final RelType t4 = RelType.create("T");
    static final RelType t5 = RelType.typed("T");
    static final RelType t6 = RelType.symbol("T");
    static final RelType t7 = RelType.name("T");
    static final RelType t8 = RelType.tableType("T");
    static final RelType t9 = RelType.schema("T");
    static final RelType t10 = RelType.type("T");
    static final RelRN v1 = RelRN.value("T");
    static final RelRN v2 = RelRN.single("T");
    static final RelRN v3 = RelRN.fresh("T");
    static final RelRN v4 = RelRN.constRow("T");
    static final RelRN v5 = RelRN.rowOf("T");

    @Override
    public RelRN before() { return v1; }
    @Override
    public RelRN after() { return v1; }
}
